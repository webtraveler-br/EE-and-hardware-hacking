async function sha256Hex(input) {
  const data = new TextEncoder().encode(input);
  const digest = await crypto.subtle.digest("SHA-256", data);
  return [...new Uint8Array(digest)].map((b) => b.toString(16).padStart(2, "0")).join("");
}

async function solvePow(prefix, difficulty, onTick) {
  const target = "0".repeat(difficulty);
  let nonce = 0;

  while (true) {
    const hash = await sha256Hex(`${prefix}:${nonce}`);
    if (hash.startsWith(target)) {
      return String(nonce);
    }
    nonce += 1;

    if (nonce % 250 === 0) {
      if (onTick) onTick(nonce);
      await new Promise((resolve) => setTimeout(resolve, 0));
    }
  }
}

async function getChallenge() {
  const response = await fetch("/api/auth/challenge", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: "{}"
  });
  if (!response.ok) {
    const text = await response.text();
    throw new Error(text || "Falha ao obter desafio anti-bot");
  }
  return response.json();
}

function setStatus(el, message) {
  if (el) el.textContent = message;
}

document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("auth-form");
  const status = document.getElementById("auth-status");
  if (!form) return;

  const mode = form.dataset.mode;
  const endpoint = mode === "register" ? "/api/auth/register" : "/api/auth/login";

  form.addEventListener("submit", async (event) => {
    event.preventDefault();
    const submitButton = form.querySelector("button[type='submit']");
    if (submitButton) submitButton.disabled = true;

    try {
      const raw = Object.fromEntries(new FormData(form).entries());
      setStatus(status, "Gerando desafio anti-bot...");
      const challenge = await getChallenge();

      setStatus(status, "Processando prova de trabalho...");
      const nonce = await solvePow(challenge.prefix, challenge.difficulty, (tries) => {
        if (tries % 1000 === 0) {
          setStatus(status, `Tentativas: ${tries}`);
        }
      });

      const payload = {
        ...raw,
        challenge_id: challenge.challenge_id,
        nonce
      };

      setStatus(status, "Autenticando...");
      const response = await fetch(endpoint, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
      });

      if (!response.ok) {
        const data = await response.json().catch(() => ({}));
        throw new Error(data.detail || "Falha na autenticação");
      }

      setStatus(status, "Sucesso! Redirecionando...");
      window.location.href = "/study";
    } catch (error) {
      setStatus(status, error.message || "Erro inesperado");
    } finally {
      if (submitButton) submitButton.disabled = false;
    }
  });
});
