package it.unisa.uniclass.utenti.model;

import it.unisa.uniclass.conversazioni.model.Messaggio;
import it.unisa.uniclass.orari.model.CorsoLaurea;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static it.unisa.uniclass.utenti.model.Accademico.*;

/**
 * Rappresenta un accademico all'interno del sistema.
 * <p>
 * Questa classe eredita da {@link Utente} e include informazioni specifiche di un accademico come matricola, corso di laurea associato, conversazioni e messaggi inviati/ricevuti.<p>
 * L'oggetto viene gestito come entità JPA con ereditarietà di tipo JOINED.
 *
 * @author [UniClass]
 * @version 1.0
 * */
@Entity
@Access(AccessType.FIELD)
@Inheritance(strategy = InheritanceType.JOINED)
@NamedQueries({
        @NamedQuery(name = "Accademico.trovaAccademico", query = "SELECT a FROM Accademico a WHERE a.matricola = :matricola"),
        @NamedQuery(name = "Accademico.trovaTutti", query = "SELECT a FROM Accademico a"),
        @NamedQuery(name = "Accademico.trovaEmail", query = "SELECT a FROM Accademico a WHERE a.email = :email"),
        @NamedQuery(name = "Accademico.trovaAttivati", query = "SELECT a FROM Accademico a WHERE a.attivato = :attivato"),
        @NamedQuery(name = "Accademico.retrieveEmail", query = "SELECT a.email FROM Accademico a")
})
public class Accademico extends Utente implements Serializable {

    /**
     * Nome della query per trovare un Accademico dato il nome
     * */
    public static final String TROVA_ACCADEMICO = "Accademico.trovaAccademico";
    /**
     * Nome della query per trovare tutti gli accademici
     */
    public static final String TROVA_TUTTI = "Accademico.trovaTutti";
    /**
     * Nome della query per trovare un accademico data l'email
     */
    public static final String TROVA_EMAIL = "Accademico.trovaEmail";

    /**
     * Nome della query per trovare accademici attivati o disattivati
     */
    public static final String TROVA_ATTIVATI = "Accademico.trovaAttivati";

    /**
     * Nome della query per recuperare tutte le email degli accademici
     */
    public static final String RETRIEVE_EMAIL = "Accademico.retrieveEmail";

    /** Relazione unidirezionale {@code @OneToOne}, mappata sul campo {@code corso_laurea_id}
     * */
    @Id
    //@ spec_public
    //@ nullable
    protected String matricola;

    /**
     * Data di iscrizione dell'accademico.
     */
    //@ spec_public
    //@ nullable
    protected LocalDate iscrizione;

    /**
     * Corso di Laurea dell'Accademico
     */
    @OneToOne
    @JoinColumn(name = "corso_laurea_id")
    //@ spec_public
    //@ nullable
    protected CorsoLaurea corsoLaurea;

    /**
     * Stato di attivazione dell'account
     */
    //@ spec_public
    protected boolean attivato = false;

    /** Relazione unidirezionale {@code @OneToMany}, con cascata totale e rimoazione orfana
     * */
    @OneToMany(mappedBy = "autore", cascade = CascadeType.ALL, orphanRemoval = true)
    //@ spec_public
    //@ nullable
    private Set<Messaggio> messaggiInviati = new HashSet<>();

    /** Relazione unidirezionale {@code @OneToMany}, con cascata totale e rimozione orfana.
     */
    @OneToMany(mappedBy = "destinatario", cascade = CascadeType.ALL, orphanRemoval = true)
    //@ spec_public
    //@ nullable
    private Set<Messaggio> messaggiRicevuti = new HashSet<>();


    /** Costruttore predefinito.
     <p>
     Inizializza un'istanza vuota di {@code Accademico}
     */
    //@ skipesc
    public Accademico() {}

    public Accademico(String nome, String cognome, LocalDate dataNascita, String email, String password, Tipo tipo) {
        super();
        this.nome = nome;
        this.cognome = cognome;
        this.dataNascita = dataNascita;
        this.email = email;
        this.password = password;
        this.tipo = tipo;
    }

    /**
     * Restituisce il valore d'attivazione dell'account
     *
     * @return il valore dell'attivazione
     */
    /*@
      @ public normal_behavior
      @ assignable \nothing;
      @ ensures \result == attivato;
      @*/
    public boolean isAttivato() {
        return attivato;
    }

    /**
     * Imposta il valore dell'attivazione dell'account
     *
     * @param attivato il nuovo valore d'attivazione
     */
    /*@
      @ public normal_behavior
      @ assignable this.attivato;
      @ ensures this.attivato == attivato;
      @*/
    public void setAttivato(boolean attivato) {
        this.attivato = attivato;
    }

    /**
     * Restituisce la data di iscrizione dell'accademico.
     *
     * @return la data di iscrizione, come {@link LocalDate}
     * */
    /*@
      @ public normal_behavior
      @ assignable \nothing;
      @ ensures \result == iscrizione;
      @*/
    public /*@ nullable */ LocalDate getIscrizione() {
        return iscrizione;
    }

    /**
    Imposta la data di iscrizione dell'accademico
    @param iscrizione la nuova data di iscrizione
    @throws IllegalArgumentException se la data è futura.
    */
    /*@
      @ public normal_behavior
      @ assignable this.iscrizione;
      @ ensures this.iscrizione == iscrizione;
      @*/
    public void setIscrizione(LocalDate iscrizione) {
        this.iscrizione = iscrizione;
    }

    /**
     * Restituisce il corso di laurea associato all'accademico.
     *
     * @return l'oggetto {@link CorsoLaurea}.
     * */
    /*@
      @ public normal_behavior
      @ assignable \nothing;
      @ ensures \result == corsoLaurea;
      @*/
    public /*@ nullable */ CorsoLaurea getCorsoLaurea() {
        return corsoLaurea;
    }

    /** Imposta il corso di laurea associato all'accademico.
     *
     * @param corsoLaurea il nuovo corso di laurea.
     * */
    /*@
      @ public normal_behavior
      @ assignable this.corsoLaurea;
      @ ensures this.corsoLaurea == corsoLaurea;
      @*/
    public void setCorsoLaurea(CorsoLaurea corsoLaurea) {
        this.corsoLaurea = corsoLaurea;
    }

    /**
     * Restituisce la matricola dell'accademico.
     *
     * @return la matricola, come {@link String}.
     * */
    /*@
      @ public normal_behavior
      @ assignable \nothing;
      @ ensures \result == matricola;
      @*/
    public /*@ nullable */ String getMatricola() {
        return matricola;
    }

    /**
     * Imposta la matricola dell'accademico.
     *
     * @param matricola la nuova matricola.
     * @throws IllegalArgumentException se la matricola è vuota o nulla.
     * @exception IllegalArgumentException
     *               Thrown to indicate that a method has been passed an illegal or inappropriate argument.
     * */
    /*@
      @ public normal_behavior
      @ assignable this.matricola;
      @ ensures this.matricola == matricola;
      @*/
    public void setMatricola(String matricola) {
        this.matricola = matricola;
    }


    /**
     * Restituisce i messaggi inviati all'accademico.
     *
     * @return un {@link Set} di {@link Messaggio}.
     * */
    /*@
      @ public normal_behavior
      @ assignable \nothing;
      @ ensures \result == messaggiInviati;
      @*/
    public /*@ nullable */ Set<Messaggio> getMessaggiInviati() {
        return messaggiInviati;
    }

    /**
     * Imposta i messaggi inviati dall'accademico.
     *
     * @param messaggiInviati il nuovo set di messaggi inviati.
     * */
    /*@
      @ public normal_behavior
      @ assignable this.messaggiInviati;
      @ ensures this.messaggiInviati == messaggiInviati;
      @*/
    public void setMessaggiInviati(Set<Messaggio> messaggiInviati) {
        this.messaggiInviati = messaggiInviati;
    }

    /** Restituisce i messaggi ricevuti dall'accademico.
     *
     * @return un {@link Set} di {@link Messaggio}.
     * */
    /*@
      @ public normal_behavior
      @ assignable \nothing;
      @ ensures \result == messaggiRicevuti;
      @*/
    public /*@ nullable */ Set<Messaggio> getMessaggiRicevuti() {
        return messaggiRicevuti;
    }

    /**
     * Imposta i messaggi ricevuti dall'accademico.
     *
     * @param messaggiRicevuti il nuovo set di messaggi ricevuti.
     * */
    /*@
      @ public normal_behavior
      @ assignable this.messaggiRicevuti;
      @ ensures this.messaggiRicevuti == messaggiRicevuti;
      @*/
    public void setMessaggiRicevuti(Set<Messaggio> messaggiRicevuti) {
        this.messaggiRicevuti = messaggiRicevuti;
    }
}
