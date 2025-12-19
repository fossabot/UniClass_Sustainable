function aggiornaListaUtenti() {
    const contextPath = window.location.pathname.substring(0, window.location.pathname.indexOf("/", 1));
    const url = contextPath + "/GetNonAttivati";
    const url2 = contextPath + "/GetAttivati";
    const xhr = new XMLHttpRequest();
    const xhrr = new XMLHttpRequest();

    xhr.open("GET", url, true);
    xhr.onload = () => {
        if (xhr.status === 200) {

            const response = JSON.parse(xhr.responseText);
            console.log(response);

            const listContainer = document.querySelector('.list');
            listContainer.innerHTML = '';

            response.forEach(utente => {
                const listItem = document.createElement('p');
                listItem.innerHTML = `
                    <p><strong>Matricola:</strong> ${utente["matricola"]}</p>
                    <p><strong>Email:</strong> ${utente["email"]}</p>
                    <p><strong>Tipo:</strong> ${utente["tipo"]}</p>
                `;
                listContainer.appendChild(listItem);
            });
        } else {
            console.error("Errore nella richiesta AJAX: " + xhr.status);
        }
    };

    xhrr.open("GET", url2, true);
    xhrr.onload = () => {
        if (xhrr.status === 200) {
            const response = JSON.parse(xhrr.responseText);
            console.log(response);

            const emailSelect = document.getElementById("email-remove");
            if (!emailSelect) {
                console.error("Elemento 'select' non trovato.");
                return;
            }

            emailSelect.innerHTML = '<option value="">-- Seleziona un\'email --</option>';

            response.forEach(utente => {
                const option = document.createElement('option');
                option.value = utente["email"];
                option.textContent = utente["email"];
                emailSelect.appendChild(option);
            });
        } else {
            console.error("Errore nella richiesta AJAX: " + xhrr.status);
        }
    };

    xhrr.send();
    xhr.send();
}

window.onload = () => {
    aggiornaListaUtenti();
};
