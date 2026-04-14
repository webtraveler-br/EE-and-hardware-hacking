function pct(part, total) {
  if (!total) return "0%";
  return `${Math.round((part / total) * 100)}%`;
}

function escapeHtml(value) {
  return String(value)
    .replaceAll("&", "&amp;")
    .replaceAll("<", "&lt;")
    .replaceAll(">", "&gt;")
    .replaceAll('"', "&quot;")
    .replaceAll("'", "&#39;");
}

async function readApiError(response, fallbackMessage) {
  try {
    const data = await response.json();
    if (data && typeof data.detail === "string" && data.detail.trim()) {
      return data.detail;
    }
  } catch {
    // Ignore invalid JSON body and use the fallback message.
  }
  return fallbackMessage;
}

function renderStatsError(message) {
  for (const id of ["st-total", "st-reviewed", "st-new", "st-due", "st-today", "st-streak"]) {
    const el = document.getElementById(id);
    if (el) el.textContent = "erro";
  }

  const tbody = document.getElementById("deck-stats-body");
  if (!tbody) return;
  tbody.innerHTML = `<tr><td colspan="4">${escapeHtml(message)}</td></tr>`;
}

async function loadStats() {
  try {
    const response = await fetch("/api/stats/summary");
    if (!response.ok) {
      throw new Error(await readApiError(response, "Falha ao carregar estatisticas"));
    }

    const data = await response.json();
    if (!data.overview || typeof data.overview !== "object" || !Array.isArray(data.decks)) {
      throw new Error("Resposta invalida ao carregar estatisticas");
    }

    const overview = data.overview;

    document.getElementById("st-total").textContent = overview.total_cards ?? 0;
    document.getElementById("st-reviewed").textContent = overview.reviewed_cards ?? 0;
    document.getElementById("st-new").textContent = overview.new_cards ?? 0;
    document.getElementById("st-due").textContent = overview.due_now ?? 0;
    document.getElementById("st-today").textContent = overview.today_reviews ?? 0;
    document.getElementById("st-streak").textContent = `${overview.streak_days ?? 0} dias`;

    const tbody = document.getElementById("deck-stats-body");
    if (!tbody) return;

    tbody.innerHTML = data.decks
      .map((deck) => {
        const progress = pct(deck.reviewed_cards, deck.total_cards);
        return `
          <tr>
            <td>${escapeHtml(deck.title)}</td>
            <td>${deck.total_cards}</td>
            <td>${deck.reviewed_cards}</td>
            <td>${progress}</td>
          </tr>
        `;
      })
      .join("");
  } catch (error) {
    const message = error instanceof Error ? error.message : "Falha ao carregar estatisticas";
    renderStatsError(message);
  }
}

document.addEventListener("DOMContentLoaded", () => {
  loadStats();
});
