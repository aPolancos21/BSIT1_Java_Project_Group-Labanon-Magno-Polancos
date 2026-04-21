let activeFilter = "all";
let searchQuery = "";

function urgencyConfig(urgency) {
  if (urgency === "high") {
    return { cls: "urgency-high", label: "\u{1F534} High Urgency" };
  }

  if (urgency === "mid") {
    return { cls: "urgency-mid", label: "\u{1F7E0} See a Doctor" };
  }

  return { cls: "urgency-low", label: "\u{1F7E2} Non-urgent" };
}

function renderGrid() {
  const grid = document.getElementById("grid");
  let items = DATA;

  if (activeFilter !== "all") {
    items = items.filter((item) => item.cat === activeFilter);
  }

  if (searchQuery) {
    const query = searchQuery.toLowerCase();
    items = items.filter(
      (item) =>
        item.title.toLowerCase().includes(query) ||
        item.preview.toLowerCase().includes(query) ||
        item.label.toLowerCase().includes(query)
    );
  }

  if (!items.length) {
    grid.innerHTML = `
      <div class="empty">
        <span class="empty-icon">&#128269;</span>
        <p>No results for "<strong>${searchQuery}</strong>".<br />Try a different keyword.</p>
      </div>
    `;
    return;
  }

  grid.innerHTML = items
    .map((item, index) => {
      const urgency = urgencyConfig(item.urgency);

      return `
        <div class="card" style="animation-delay:${index * 0.05}s" onclick="openModal(${item.id})">
          <div class="card-header">
            <div class="card-icon" style="background:${item.colorDim}">${item.icon}</div>
            <div class="card-title-block">
              <div class="card-label" style="color:${item.color}">${item.label}</div>
              <div class="card-title">${item.title}</div>
              <div class="card-urgency ${urgency.cls}">${urgency.label}</div>
            </div>
          </div>
          <div class="card-preview">${item.preview}</div>
          <div class="card-footer">
            <div class="view-btn">View guide &#8594;</div>
          </div>
        </div>
      `;
    })
    .join("");
}

function filterCat(category, button) {
  activeFilter = category;
  document.querySelectorAll(".cat-btn").forEach((item) => item.classList.remove("active"));
  button.classList.add("active");
  renderGrid();
}

function openModal(id) {
  const item = DATA.find((entry) => entry.id === id);

  if (!item) {
    return;
  }

  const urgency = urgencyConfig(item.urgency);

  document.getElementById("mIcon").textContent = item.icon;
  document.getElementById("mIcon").style.background = item.colorDim;
  document.getElementById("mTitle").textContent = item.title;
  document.getElementById("mUrgency").innerHTML =
    `<div class="card-urgency ${urgency.cls}" style="margin-top:6px">${urgency.label}</div>`;
  document.getElementById("mWarning").innerHTML =
    `<span>\u26A0\uFE0F</span><div>${item.warning}</div>`;
  document.getElementById("mCall").innerHTML =
    `<span>\u{1F4DE}</span><div><strong>When to call for help:</strong> ${item.call}</div>`;

  document.getElementById("modalOverlay").classList.add("open");
  document.body.style.overflow = "hidden";
}

function closeModal(event) {
  if (event.target === document.getElementById("modalOverlay")) {
    closeModalDirect();
  }
}

function closeModalDirect() {
  document.getElementById("modalOverlay").classList.remove("open");
  document.body.style.overflow = "";
}

document.getElementById("searchInput").addEventListener("input", function onInput() {
  searchQuery = this.value.trim();
  renderGrid();
});

document.addEventListener("keydown", (event) => {
  if (event.key === "Escape") {
    closeModalDirect();
  }
});

renderGrid();
