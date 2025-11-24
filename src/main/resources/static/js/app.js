/* -------------------------
   Cambiar entre vistas
--------------------------*/
function showView(id) {
    document.querySelectorAll(".menu li").forEach(li => li.classList.remove("active"));
    document.querySelectorAll(".view").forEach(v => v.classList.remove("visible"));

    document.querySelector(`.menu li[onclick="showView('${id}')"]`).classList.add("active");
    document.getElementById(id).classList.add("visible");
}

/* -------------------------
   Calendario interactivo
--------------------------*/
let currentDate = new Date();

function renderCalendar() {
    const calendar = document.getElementById("calendar");
    const title = document.getElementById("calendar-title");

    const year = currentDate.getFullYear();
    const month = currentDate.getMonth();

    title.textContent = currentDate.toLocaleString("es-PE", {
        month: "long",
        year: "numeric"
    });

    calendar.innerHTML = "";

    const firstDay = new Date(year, month, 1).getDay();
    const numDays = new Date(year, month + 1, 0).getDate();

    const emptyCells = (firstDay + 6) % 7;
    for (let i = 0; i < emptyCells; i++) {
        calendar.innerHTML += `<div></div>`;
    }

    for (let d = 1; d <= numDays; d++) {
        calendar.innerHTML += `<div class="day">${d}</div>`;
    }
}

function nextMonth() {
    currentDate.setMonth(currentDate.getMonth() + 1);
    renderCalendar();
}

function prevMonth() {
    currentDate.setMonth(currentDate.getMonth() - 1);
    renderCalendar();
}

renderCalendar();

/* -------------------------
   Feedback dinámico
--------------------------*/
function addComentario() {
    const texto = document.getElementById("comentarioInput").value;
    if (!texto.trim()) return;

    // Generar fecha
    const fecha = new Date().toLocaleDateString("es-PE");

    // Generar estrellas aleatorias (1 a 5)
    const rating = Math.floor(Math.random() * 5) + 1;
    const estrellas = "★".repeat(rating) + "☆".repeat(5 - rating);

    // Avatar inicial (solo una letra)
    const inicial = "A"; // Puedes luego hacerlo dinámico

    // Crear contenedor
    const box = document.createElement("div");
    box.className = "comentario-box";

    box.innerHTML = `
        <div class="comentario-avatar">${inicial}</div>

        <div class="comentario-content">
            <div class="comentario-header">
                <strong>Estudiante</strong>
                <span class="comentario-stars">${estrellas}</span>
            </div>

            <p>${texto}</p>

            <span class="comentario-fecha">${fecha}</span>
        </div>
    `;

    document.getElementById("comentarios").appendChild(box);

    document.getElementById("comentarioInput").value = "";
}

