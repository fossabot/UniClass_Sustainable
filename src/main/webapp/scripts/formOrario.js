// Funzione per aggiornare la lista dei resti in base al corso di laurea selezionato
const aggiornaResto = () => {
    const corsoLaurea = document.getElementById("corsoLaurea").value;
    const restoSelect = document.getElementById("resto");
    const annoSelect = document.getElementById("anno");

    // Reset dei select
    restoSelect.innerHTML = '<option value="">-- Seleziona un resto --</option>';
    annoSelect.innerHTML = '<option value="">-- Seleziona un anno --</option>';

    if (!corsoLaurea) {
        return;
    }

    const xhr = new XMLHttpRequest();
    xhr.open("GET", "getResto?corsoLaurea=" + encodeURIComponent(corsoLaurea), true);

    xhr.onload = () => {
        if (xhr.status === 200) {
            const response = JSON.parse(xhr.responseText);
            console.log("Resti ricevuti:", response);

            response.forEach(resto => {
                restoSelect.innerHTML += `<option value="${resto.nome}">${resto.nome}</option>`;
            });
        } else {
            console.error("Errore nella richiesta AJAX per i resti: " + xhr.status);
        }
    };

    xhr.onerror = () => {
        console.error("Errore di rete nella richiesta AJAX per i resti");
    };

    xhr.send();

    // Aggiorna anche gli anni
    aggiornaAnno();
};

// Funzione per aggiornare la lista degli anni in base al corso di laurea selezionato
const aggiornaAnno = () => {
    const corsoLaurea = document.getElementById("corsoLaurea").value;
    const annoSelect = document.getElementById("anno");

    annoSelect.innerHTML = '<option value="">-- Seleziona un anno --</option>';

    if (!corsoLaurea) {
        return;
    }

    const xhr = new XMLHttpRequest();
    xhr.open("GET", "getAnno?corsoLaurea=" + encodeURIComponent(corsoLaurea), true);

    xhr.onload = () => {
        if (xhr.status === 200) {
            const response = JSON.parse(xhr.responseText);
            console.log("Anni ricevuti:", response);

            response.forEach(anno => {
                annoSelect.innerHTML += `<option value="${anno.nome}">${anno.nome}</option>`;
            });
        } else {
            console.error("Errore nella richiesta AJAX per gli anni: " + xhr.status);
        }
    };

    xhr.onerror = () => {
        console.error("Errore di rete nella richiesta AJAX per gli anni");
    };

    xhr.send();
};