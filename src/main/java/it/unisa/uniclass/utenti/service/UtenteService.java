package it.unisa.uniclass.utenti.service;

import it.unisa.uniclass.common.exceptions.AuthenticationException;
import it.unisa.uniclass.utenti.model.Accademico;
import it.unisa.uniclass.utenti.model.PersonaleTA;
import it.unisa.uniclass.utenti.model.Utente;
import it.unisa.uniclass.utenti.service.dao.AccademicoDAO;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;

/**
 * Classe di servizio per la gestione delle operazioni relative agli utenti.
 * Fornisce metodi per autenticare e recuperare utenti.
 */
@Stateless
public class UtenteService {

    // Campi per il supporto ai test (Lazy Loading Pattern)
    private PersonaleTAService personaleTAService;
    private AccademicoService accademicoService;

    // Setter per Dependency Injection (usati SOLO dai Test per iniettare i Mock)
    public void setPersonaleTAService(PersonaleTAService personaleTAService) {
        this.personaleTAService = personaleTAService;
    }

    public void setAccademicoService(AccademicoService accademicoService) {
        this.accademicoService = accademicoService;
    }

    // Metodi Getter Helper con logica Lazy Loading
    // Se il service è nullo (produzione), lo istanzia col costruttore reale (JNDI).
    // Se è già settato (test), restituisce il mock.
    protected PersonaleTAService getPersonaleTAService() {
        if (personaleTAService == null) {
            personaleTAService = new PersonaleTAService();
        }
        return personaleTAService;
    }

    protected AccademicoService getAccademicoService() {
        if (accademicoService == null) {
            accademicoService = new AccademicoService();
        }
        return accademicoService;
    }

    /**
     * Recupera un utente dal database utilizzando la sua email e password.
     *
     * @param email L'email dell'utente da cercare.
     * @param password La password dell'utente da cercare.
     * @return L'oggetto Utente corrispondente all'email e alla password.
     * @throws AuthenticationException Se la password è errata.
     */
    public Utente retrieveByUserAndPassword(String email, String password) {
        try {
            // MODIFICA: Utilizzo dei metodi getter helper invece di 'new' diretto
            PersonaleTA personaleTA = (PersonaleTA) getPersonaleTAService().trovaEmail(email);
            Accademico accademico = (Accademico) getAccademicoService().trovaEmailUniClass(email);
            
            if (personaleTA != null) {
                if (personaleTA.getPassword().equals(password)) {
                    return personaleTA;
                } else {
                    throw new AuthenticationException("Password errata");
                }
            } else if (accademico != null) {
                if (accademico.getPassword().equals(password)) {
                    return accademico;
                } else {
                    throw new AuthenticationException("Password errata");
                }
            }
            return null;
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Recupera un utente dal database utilizzando la sua email.
     *
     * @param email L'email dell'utente da cercare.
     * @return L'oggetto Utente corrispondente all'email.
     */
    public Utente retrieveByEmail(String email) {
        // MODIFICA: Utilizzo dei metodi getter helper invece di 'new' diretto
        PersonaleTA personaleTA = (PersonaleTA) getPersonaleTAService().trovaEmail(email);
        Accademico accademico = (Accademico) getAccademicoService().trovaEmailUniClass(email);
        
        if (personaleTA != null) {
            return personaleTA;
        } else if (accademico != null) {
            return accademico;
        }
        return null;
    }
}
