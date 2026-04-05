function renderMath(root) {
  if (!window.renderMathInElement) return;
  window.renderMathInElement(root, {
    delimiters: [
      { left: "$$", right: "$$", display: true },
      { left: "$", right: "$", display: false },
      { left: "\\(", right: "\\)", display: false },
      { left: "\\[", right: "\\]", display: true }
    ],
    throwOnError: false
  });
}

function renderMermaid(root) {
  if (!window.mermaid) return;
  const blocks = root.querySelectorAll("pre code.language-mermaid");
  if (!blocks.length) return;

  window.mermaid.initialize({ startOnLoad: false, theme: "neutral" });

  blocks.forEach((block, index) => {
    const pre = block.parentElement;
    const wrapper = document.createElement("div");
    wrapper.className = "mermaid";
    wrapper.textContent = block.textContent;
    wrapper.id = `mermaid-${index}-${Date.now()}`;
    pre.replaceWith(wrapper);
  });

  window.mermaid.run();
}

function renderMarkdownEnhancements(root = document) {
  renderMermaid(root);
  renderMath(root);
}

window.renderMarkdownEnhancements = renderMarkdownEnhancements;

function wireNav() {
  const toggle = document.getElementById("nav-toggle");
  const nav = document.getElementById("main-nav");
  if (!toggle || !nav) return;

  toggle.addEventListener("click", () => {
    nav.classList.toggle("open");
  });
}

function wireLogout() {
  const button = document.getElementById("logout-btn");
  if (!button) return;

  button.addEventListener("click", async () => {
    try {
      await fetch("/api/auth/logout", { method: "POST" });
    } finally {
      window.location.href = "/auth/login";
    }
  });
}

document.addEventListener("DOMContentLoaded", () => {
  wireNav();
  wireLogout();
  renderMarkdownEnhancements(document);
});
