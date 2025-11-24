// Modal de notificaciones
const modal = document.getElementById("modalNotif");
const btns = [document.getElementById("btnNotif"), document.getElementById("btnNotif2")];
const span = document.querySelector(".close");

btns.forEach(btn => btn.addEventListener("click", () => modal.style.display = "flex"));
span.onclick = () => modal.style.display = "none";
window.onclick = (e) => { if (e.target === modal) modal.style.display = "none"; };
