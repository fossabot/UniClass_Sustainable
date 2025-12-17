package it.unisa.uniclass.testing.unit.utenti.model;

import it.unisa.uniclass.orari.model.Corso;
import it.unisa.uniclass.orari.model.CorsoLaurea;
import it.unisa.uniclass.orari.model.Lezione;
import it.unisa.uniclass.utenti.model.Docente;
import it.unisa.uniclass.utenti.model.Tipo;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DocenteTest {

    @Test
    void testCostruttoriGetSet() {
        // Oggetti fittizi
        CorsoLaurea corsoLaurea = new CorsoLaurea();
        LocalDate dataNascita = LocalDate.of(1980, 1, 1);
        LocalDate iscrizione = LocalDate.of(2020, 10, 1);

        // Costruttore parametrico
        Docente docente = new Docente("Mario", "Rossi", dataNascita, "m.rossi@example.com",
                "password", "MAT123", iscrizione, corsoLaurea, "Informatica"); // ggignore

        // Verifica campi tramite getter
        assertEquals("Mario", docente.getNome());
        assertEquals("Rossi", docente.getCognome());
        assertEquals(dataNascita, docente.getDataNascita());
        assertEquals("m.rossi@example.com", docente.getEmail());
        assertEquals("password", docente.getPassword());
        assertEquals("MAT123", docente.getMatricola());
        assertEquals(iscrizione, docente.getIscrizione());
        assertEquals(corsoLaurea, docente.getCorsoLaurea());
        assertEquals("Informatica", docente.getDipartimento());
        assertEquals(Tipo.Docente, docente.getTipo());

        // Liste inizializzate
        assertNotNull(docente.getCorsi());
        assertNotNull(docente.getLezioni());
        assertTrue(docente.getCorsi().isEmpty());
        assertTrue(docente.getLezioni().isEmpty());

        // Test setters
        List<Corso> corsi = new ArrayList<>();
        List<Lezione> lezioni = new ArrayList<>();
        docente.setCorsi(corsi);
        docente.setLezioni(lezioni);
        docente.setDipartimento("Matematica");

        assertEquals(corsi, docente.getCorsi());
        assertEquals(lezioni, docente.getLezioni());
        assertEquals("Matematica", docente.getDipartimento());

        // Costruttore vuoto
        Docente docenteVuoto = new Docente();
        assertNotNull(docenteVuoto.getCorsi());
        assertTrue(docenteVuoto.getCorsi().isEmpty());
        assertEquals(Tipo.Docente, docenteVuoto.getTipo());
    }

    @Test
    void testToString() {
        Docente docente = new Docente();
        String s = docente.toString();
        assertNotNull(s);
        assertTrue(s.contains("Docente"));
    }
}
