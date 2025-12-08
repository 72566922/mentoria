// Toggle the user dropdown menu
function toggleUserMenu(btn) {
  const dropdown = document.querySelector('.user-dropdown');
  if (!dropdown) return;
  dropdown.classList.toggle('show');
  const expanded = btn.getAttribute('aria-expanded') === 'true';
  btn.setAttribute('aria-expanded', (!expanded).toString());
}

// Simple sidebar toggle for small screens
function toggleSidebar() {
  const layout = document.querySelector('.layout');
  const sidebar = document.querySelector('.sidebar');
  if (!layout || !sidebar) return;
  sidebar.classList.toggle('collapsed');
}

// Close dropdown when clicking outside
document.addEventListener('click', function (e) {
  const open = document.querySelector('.user-dropdown.show');
  if (!open) return;
  const inside = e.target.closest('.user-menu') || e.target.closest('.user-dropdown');
  if (!inside) {
    open.classList.remove('show');
    const btn = document.querySelector('.avatar-btn[aria-expanded="true"]');
    if (btn) btn.setAttribute('aria-expanded', 'false');
  }
});