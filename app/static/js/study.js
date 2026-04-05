/* ── State ────────────────────────────────────────────────────── */
let currentCard = null;
let revealTimeStart = 0;
let selectedDeckUid = "all";
let latestDecks = [];
let isFlipped = false;
let isLoading = false;
let sessionReviewed = 0;
let sessionCorrect = 0;
let lastReview = null; // for undo
let quickStats = {};

function syncDeckQueryParam() {
  const url = new URL(window.location.href);
  if (selectedDeckUid && selectedDeckUid !== "all") {
    url.searchParams.set("deck", selectedDeckUid);
  } else {
    url.searchParams.delete("deck");
  }
  window.history.replaceState({}, "", url);
}

/* ── Helpers ──────────────────────────────────────────────────── */
function sanitizeHtml(html) {
  return window.DOMPurify ? DOMPurify.sanitize(html) : html;
}

function renderMd(md) {
  if (!md) return "";
  return window.marked ? sanitizeHtml(marked.parse(md)) : md;
}

function $(id) {
  return document.getElementById(id);
}

function show(el) {
  el?.classList.remove("hidden");
}

function hide(el) {
  el?.classList.add("hidden");
}

/* ── Toast ────────────────────────────────────────────────────── */
function toast(msg, type = "info") {
  let container = $("toast-container");
  if (!container) {
    container = document.createElement("div");
    container.id = "toast-container";
    container.className = "toast-container";
    document.body.appendChild(container);
  }
  const el = document.createElement("div");
  el.className = `toast toast-${type}`;
  el.textContent = msg;
  container.appendChild(el);
  requestAnimationFrame(() => el.classList.add("show"));
  setTimeout(() => {
    el.classList.remove("show");
    setTimeout(() => el.remove(), 300);
  }, 2600);
}

/* ── Deck List ────────────────────────────────────────────────── */
function renderDeckList() {
  const root = $("deck-list");
  if (!root) return;

  const totalCards = latestDecks.reduce((s, d) => s + (d.card_count ?? 0), 0);
  const totalDue = latestDecks.reduce((s, d) => s + (d.due_count ?? 0), 0);
  const totalNew = latestDecks.reduce((s, d) => s + (d.new_count ?? 0), 0);

  const allDeck = { deck_uid: "all", title: "Todos os baralhos", card_count: totalCards, due_count: totalDue, new_count: totalNew };
  const decks = [allDeck, ...latestDecks];

  root.innerHTML = decks
    .map((d) => {
      const active = d.deck_uid === selectedDeckUid ? " active" : "";
      const hasDue = d.due_count > 0;
      const hasNew = d.new_count > 0;
      return `<button class="deck-row${active}" type="button" data-deck-uid="${d.deck_uid}">
        <span class="deck-title">${d.title}</span>
        <span class="deck-badges">
          ${hasDue ? `<span class="badge due">${d.due_count}</span>` : ""}
          ${hasNew ? `<span class="badge fresh">${d.new_count}</span>` : ""}
          <span class="badge total">${d.card_count}</span>
        </span>
      </button>`;
    })
    .join("");

  root.querySelectorAll(".deck-row").forEach((btn) => {
    btn.addEventListener("click", () => {
      const uid = btn.dataset.deckUid || "all";
      if (selectedDeckUid === uid) return;
      selectedDeckUid = uid;
      syncDeckQueryParam();
      sessionReviewed = 0;
      sessionCorrect = 0;
      renderDeckList();
      updateSessionInfo();
      loadNextCard();
      // Close drawer on mobile
      $("deck-drawer")?.classList.remove("open");
    });
  });
}

/* ── Session Info ─────────────────────────────────────────────── */
function updateSessionInfo() {
  const sel = latestDecks.find((d) => d.deck_uid === selectedDeckUid);
  const label = selectedDeckUid === "all" ? "Todos os baralhos" : sel?.title ?? selectedDeckUid;
  const deckLabel = $("session-deck-label");
  if (deckLabel) deckLabel.textContent = label;
}

function updateCounters(stats) {
  if (!stats) return;
  quickStats = stats;
  const due = $("sc-due");
  const fresh = $("sc-new");
  const today = $("sc-today");
  if (due) due.textContent = stats.due_now ?? 0;
  if (fresh) fresh.textContent = stats.new_cards ?? 0;
  if (today) today.textContent = stats.today_reviews ?? 0;

  // Progress bar
  const total = (stats.due_now ?? 0) + (stats.new_cards ?? 0) + sessionReviewed;
  const pct = total > 0 ? Math.round((sessionReviewed / total) * 100) : 0;
  const bar = $("session-progress-bar");
  if (bar) bar.style.width = `${pct}%`;
}

/* ── Card Display ─────────────────────────────────────────────── */
function setCardContent(card) {
  currentCard = card;
  revealTimeStart = Date.now();
  isFlipped = false;

  const q = $("question-content");
  const a = $("answer-content");
  const inner = $("flashcard-inner");
  const flashcard = $("flashcard");

  if (q) q.innerHTML = renderMd(card.question_md);
  if (a) a.innerHTML = renderMd(card.answer_md);

  // Reset flip
  inner?.classList.remove("flipped");
  flashcard?.classList.remove("exit-left", "exit-right", "enter");

  // Show card, hide empty
  show($("flashcard"));
  hide($("study-empty"));
  show($("reveal-btn"));
  hide($("rating-actions"));

  // Card type indicator
  const typeEl = $("session-card-type");
  if (typeEl) typeEl.textContent = card.is_new ? "novo" : "revisão";

  // Animate entrance
  requestAnimationFrame(() => {
    flashcard?.classList.add("enter");
    // Render math/mermaid
    const stage = $("card-stage");
    window.renderMarkdownEnhancements?.(stage);
  });
}

function showEmpty(message) {
  currentCard = null;
  hide($("flashcard"));
  show($("study-empty"));
  hide($("reveal-btn"));
  hide($("rating-actions"));

  const msg = $("empty-message");
  if (msg) msg.textContent = message || "Nada pendente neste deck. Bom trabalho.";

  // Session summary
  const summary = $("session-summary");
  if (summary && sessionReviewed > 0) {
    const accuracy = sessionReviewed > 0 ? Math.round((sessionCorrect / sessionReviewed) * 100) : 0;
    summary.innerHTML = `
      <div class="summary-stats">
        <div class="summary-stat"><strong>${sessionReviewed}</strong><span>revisados</span></div>
        <div class="summary-stat"><strong>${accuracy}%</strong><span>acerto</span></div>
        <div class="summary-stat"><strong>${quickStats.streak_days ?? 0}</strong><span>dias streak</span></div>
      </div>
    `;
  }

  // Set progress to 100%
  const bar = $("session-progress-bar");
  if (bar) bar.style.width = "100%";
}

function flipCard() {
  if (!currentCard || isFlipped) return;
  isFlipped = true;

  $("flashcard-inner")?.classList.add("flipped");
  hide($("reveal-btn"));
  show($("rating-actions"));

  // Render math on answer side after flip
  setTimeout(() => {
    const stage = $("card-stage");
    window.renderMarkdownEnhancements?.(stage);
  }, 350);
}

/* ── Card transition animation ────────────────────────────────── */
function transitionCard(direction, callback) {
  const flashcard = $("flashcard");
  if (!flashcard) {
    callback();
    return;
  }
  const cls = direction === "left" ? "exit-left" : "exit-right";
  flashcard.classList.add(cls);
  setTimeout(() => {
    flashcard.classList.remove(cls);
    callback();
  }, 250);
}

/* ── API ──────────────────────────────────────────────────────── */
async function loadDecks() {
  try {
    const res = await fetch("/api/decks");
    if (!res.ok) return;
    const data = await res.json();
    latestDecks = data.decks || [];
    const available = new Set(["all", ...latestDecks.map((d) => d.deck_uid)]);
    if (!available.has(selectedDeckUid)) selectedDeckUid = "all";
    renderDeckList();
    updateSessionInfo();
  } catch {
    toast("Falha ao carregar baralhos", "error");
  }
}

async function loadQuickStats() {
  try {
    const res = await fetch("/api/stats/summary");
    if (!res.ok) return;
    const data = await res.json();
    updateCounters(data.overview || {});
  } catch {
    toast("Falha ao carregar estatísticas", "error");
  }
}

async function loadNextCard() {
  if (isLoading) return;
  isLoading = true;

  try {
    const res = await fetch("/api/study/next", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ deck_uid: selectedDeckUid || "all" }),
    });
    if (!res.ok) {
      toast("Falha ao carregar próximo card", "error");
      return;
    }
    const data = await res.json();
    if (data.empty) {
      showEmpty();
    } else {
      setCardContent(data.card);
    }
  } catch {
    toast("Erro de rede", "error");
  } finally {
    isLoading = false;
  }
}

async function submitReview(rating) {
  if (!currentCard || isLoading) return;
  isLoading = true;

  const durationMs = Math.max(0, Date.now() - revealTimeStart);

  // Save for undo
  lastReview = { card_uid: currentCard.card_uid, rating, deck_uid: selectedDeckUid };

  try {
    const res = await fetch("/api/study/review", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ card_uid: currentCard.card_uid, rating, duration_ms: durationMs }),
    });
    if (!res.ok) {
      const err = await res.json().catch(() => ({}));
      toast(err.detail || "Falha ao registrar revisão", "error");
      isLoading = false;
      return;
    }

    const data = await res.json();
    sessionReviewed++;
    if (rating >= 3) sessionCorrect++;

    // Use combined response - no extra requests needed
    if (data.decks) {
      latestDecks = data.decks;
      renderDeckList();
    }
    if (data.quick_stats) {
      updateCounters(data.quick_stats);
    }

    // Transition to next card
    const direction = rating >= 3 ? "right" : "left";
    transitionCard(direction, () => {
      if (data.next?.empty) {
        showEmpty();
      } else if (data.next?.card) {
        setCardContent(data.next.card);
      } else {
        showEmpty();
      }
      isLoading = false;
    });
  } catch {
    toast("Erro de rede", "error");
    isLoading = false;
  }
}

/* ── Keyboard ─────────────────────────────────────────────────── */
function handleKey(e) {
  // Ignore if typing in an input
  if (e.target.tagName === "INPUT" || e.target.tagName === "TEXTAREA") return;

  if (e.code === "Space" || e.key === " ") {
    e.preventDefault();
    if (!isFlipped && currentCard) {
      flipCard();
    }
    return;
  }

  if (["1", "2", "3", "4"].includes(e.key) && isFlipped) {
    e.preventDefault();
    submitReview(Number(e.key));
    return;
  }

  if (e.key === "u" || e.key === "U") {
    // Undo placeholder - toast hint
    if (lastReview) {
      toast("Undo ainda não implementado", "info");
    }
  }
}

/* ── Init ─────────────────────────────────────────────────────── */
function wireEvents() {
  $("reveal-btn")?.addEventListener("click", flipCard);

  $("rating-actions")?.addEventListener("click", (e) => {
    const btn = e.target.closest("button[data-rating]");
    if (!btn) return;
    const r = Number(btn.dataset.rating);
    if (r >= 1 && r <= 4) submitReview(r);
  });

  // Drawer toggle
  $("open-drawer-btn")?.addEventListener("click", () => {
    $("deck-drawer")?.classList.toggle("open");
  });
  $("close-drawer-btn")?.addEventListener("click", () => {
    $("deck-drawer")?.classList.remove("open");
  });

  document.addEventListener("keydown", handleKey);

  // Click on card to flip (mobile-friendly)
  $("flashcard")?.addEventListener("click", (e) => {
    // Don't flip if clicking a link or button inside the card
    if (e.target.closest("a, button, kbd")) return;
    if (!isFlipped && currentCard) {
      flipCard();
    }
  });
}

document.addEventListener("DOMContentLoaded", async () => {
  const requestedDeck = new URLSearchParams(window.location.search).get("deck");
  if (requestedDeck && requestedDeck.trim()) {
    selectedDeckUid = requestedDeck.trim();
  }

  wireEvents();
  // Load decks and stats in parallel, then load first card
  await Promise.all([loadDecks(), loadQuickStats()]);
  syncDeckQueryParam();
  await loadNextCard();
});
