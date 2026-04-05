function pct(part, total) {
  if (!total) return "0%";
  return `${Math.round((part / total) * 100)}%`;
}

async function loadStats() {
  const response = await fetch("/api/stats/summary");
  if (!response.ok) {
    return;
  }

  const data = await response.json();
  const overview = data.overview || {};

  document.getElementById("st-total").textContent = overview.total_cards ?? 0;
  document.getElementById("st-reviewed").textContent = overview.reviewed_cards ?? 0;
  document.getElementById("st-new").textContent = overview.new_cards ?? 0;
  document.getElementById("st-due").textContent = overview.due_now ?? 0;
  document.getElementById("st-today").textContent = overview.today_reviews ?? 0;
  document.getElementById("st-streak").textContent = `${overview.streak_days ?? 0} dias`;

  const tbody = document.getElementById("deck-stats-body");
  if (!tbody) return;

  tbody.innerHTML = (data.decks || [])
    .map((deck) => {
      const progress = pct(deck.reviewed_cards, deck.total_cards);
      return `
        <tr>
          <td>${deck.title}</td>
          <td>${deck.total_cards}</td>
          <td>${deck.reviewed_cards}</td>
          <td>${progress}</td>
        </tr>
      `;
    })
    .join("");
}

document.addEventListener("DOMContentLoaded", () => {
  loadStats();
});
