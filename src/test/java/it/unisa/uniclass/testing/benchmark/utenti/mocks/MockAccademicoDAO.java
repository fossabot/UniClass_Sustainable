package it.unisa.uniclass.testing.benchmark.utenti.mocks;

import it.unisa.uniclass.utenti.model.Accademico;
import it.unisa.uniclass.utenti.service.dao.AccademicoRemote;
import java.util.Collections;
import java.util.List;

public class MockAccademicoDAO implements AccademicoRemote {

    private Accademico accademicoDaRitornare;

    public void setAccademicoDaRitornare(Accademico accademico) {
        this.accademicoDaRitornare = accademico;
    }

    @Override
    public Accademico trovaAccademicoUniClass(String matricola) {
        if (accademicoDaRitornare != null && matricola.equals("0512105555")) {
            return accademicoDaRitornare;
        }
        return null;
    }

    @Override
    public Accademico trovaEmailUniClass(String email) {
        if (accademicoDaRitornare != null && email.equals(accademicoDaRitornare.getEmail())) {
            return accademicoDaRitornare;
        }
        return null;
    }

    // Metodi dummy per il mock: restituiscono valori vuoti o non fanno nulla.
    // Questi metodi sono intenzionalmente vuoti perché nei test non devono eseguire logica reale.

    @Override
    public List<Accademico> trovaTuttiUniClass() {
        return Collections.emptyList();
    }

    @Override
    public List<Accademico> trovaAttivati(boolean b) {
        return Collections.emptyList();
    }

    @Override
    public void aggiungiAccademico(Accademico a) {
        // Metodo intenzionalmente vuoto: nel mock non è richiesta alcuna implementazione.
    }

    @Override
    public void rimuoviAccademico(Accademico a) {
        // Metodo intenzionalmente vuoto: nel mock non è richiesta alcuna implementazione.
    }

    @Override
    public List<String> retrieveEmail() {
        return Collections.emptyList();
    }

    @Override
    public void cambiaAttivazione(Accademico a, boolean b) {
        // Metodo intenzionalmente vuoto: nel mock non è richiesta alcuna implementazione.
    }
}
