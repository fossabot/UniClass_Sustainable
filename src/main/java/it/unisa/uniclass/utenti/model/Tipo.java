package it.unisa.uniclass.utenti.model;

/**
 * Enumera i tipi di utenti che possono essere presenti nel sistema UniClass.
 * <p>
 * I tipi disponibili sono:
 * <ul>
 *   <li>{@link #Studente} - Rappresenta uno studente universitario.</li>
 *   <li>{@link #Docente} - Rappresenta un docente universitario.</li>
 *   <li>{@link #Coordinatore} - Rappresenta il coordinatore di un corso o dipartimento.</li>
 *   <li>{@link #PersonaleTA} - Rappresenta il personale tecnico-amministrativo.</li>
 * </ul>
 * </p>
 */
public enum Tipo {
    /**
     * Rappresenta uno studente universitario.
     */
    Studente, // NOSONAR - naming scelto per compatibilità con tutto il codice esistente
    /**
     * Rappresenta un docente universitario.
     */
    Docente, // NOSONAR - naming scelto per compatibilità con tutto il codice esistente
    /**
     * Rappresenta il coordinatore di un corso o dipartimento.
     */
    Coordinatore, // NOSONAR - naming scelto per compatibilità con tutto il codice esistente
    /**
     * Rappresenta il personale tecnico-amministrativo.
     */
    PersonaleTA // NOSONAR - naming scelto per compatibilità con tutto il codice esistente
}
