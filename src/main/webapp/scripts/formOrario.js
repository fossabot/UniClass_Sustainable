// Funzione per aggiornare la lista delle email
const aggiornaEmail = () => {
    const xhr = new XMLHttpRequest();
    xhr.open("GET", "GetEmailServlet", true);

    xhr.onload = () => {
        if (xhr.status === 200) {
            const response = JSON.parse(xhr.responseText);
            console.log(response);

            const emailUtenteCorrente = "<%= accademicoSelf.getEmail() %>";

            // Selezione del campo email
            const emailSelect = document.getElementById("email");
            emailSelect.innerHTML = '<option value="" disabled selected>Seleziona un\'email</option>';

            // Aggiunta delle email al dropdown
            response.forEach(email => {
                if (email !== emailUtenteCorrente) {
                    emailSelect.innerHTML += `<option value="${email}">${email}</option>`;
                }
            });
        } else {
            console.error("Errore nella richiesta AJAX: " + xhr.status);
        }
    };

    xhr.send();
};

// Carica le email al caricamento della pagina
window.onload = aggiornaEmail;
