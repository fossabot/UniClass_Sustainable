package it.unisa.uniclass.conversazioni.model;

import it.unisa.uniclass.utenti.model.Accademico;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * Rappresenta un messaggio scambiato tra utenti in una conversazione.
 * E' persistito in un database tramite JPA.
 * */
@Entity
@Access(AccessType.FIELD)
@Table(name = "messaggi")
@NamedQueries({
        @NamedQuery(name = "Messaggio.trovaMessaggio", query = "SELECT m FROM Messaggio m WHERE m.id = :id"),
        @NamedQuery(name = "Messaggio.trovaMessaggiInviati", query = "SELECT m FROM Messaggio m WHERE m.autore.matricola = :matricola"),
        @NamedQuery(name = "Messaggio.trovaMessaggiRicevuti", query = "SELECT m FROM Messaggio m WHERE m.destinatario.matricola = :matricola"),
        @NamedQuery(name = "Messaggio.trovaMessaggiMessaggeri", query = "SELECT m FROM Messaggio m WHERE ((m.autore.matricola = :autore) AND (m.destinatario.matricola = :destinatario)) OR ((m.autore.matricola = :destinatario) AND (m.destinatario.matricola = :autore))"),
        @NamedQuery(name = "Messaggio.trovaTutti", query = "SELECT m FROM Messaggio m"),
        @NamedQuery(name = "Messaggio.trovaAvvisi", query = "SELECT m FROM Messaggio m WHERE m.topic <> null"),
        @NamedQuery(name = "Messaggio.trovaAvvisiAutore", query = "SELECT m FROM Messaggio m WHERE m.topic <> null AND m.autore.matricola = :autore"),
        @NamedQuery(name = "Messaggio.trovaMessaggiData", query = "SELECT m FROM Messaggio m WHERE m.dateTime = :dateTime"),
        @NamedQuery(name = "Messaggio.trovaTopic", query = "SELECT m FROM Messaggio m WHERE m.topic = :topic")
})
public class Messaggio implements Serializable {

    /**
     * Nome della query per trovare un messaggio tramite ID
     * */
    public static final String TROVA_MESSAGGIO = "Messaggio.trovaMessaggio";
    /**
     * Nome della query per trovare i messaggi inviati da un autore.
     * */
    public static final String TROVA_MESSAGGI_INVIATI = "Messaggio.trovaMessaggiInviati";

    /**
     * Nome della query per trovare i messaggi ricevuti da un accademico
     */
    public static final String TROVA_MESSAGGI_RICEVUTI = "Messaggio.trovaMessaggiRicevuti";

    /**
     * Nome della query per trovare i messaggi tra due specifici utenti.
     * */
    public static final String TROVA_MESSAGGI_MESSAGGERI = "Messaggio.trovaMessaggiMessaggeri";
    /**
     * Nome della query per trovare tutti i messsaggi.
     * */
    public static final String TROVA_TUTTI = "Messaggio.trovaTutti";
    /**
     * Nome della query per trovare tutti gli avvisi.
     * */
    public static final String TROVA_AVVISI = "Messaggio.trovaAvvisi";
    /**
     * Nome della query per trovare gli avvisi creati da un autore specifico.
     * */
    public static final String TROVA_AVVISI_AUTORE = "Messaggio.trovaAvvisiAutore";
    /**
     * Nome della query per trovare i messaggi in base alla data e ora.
     * */
    public static final String TROVA_MESSAGGI_DATA = "Messaggio.trovaMessaggiData";
    /**
     * Nome della query per trovare i messaggi in base all'id del topic (eventuale)
     */
    public static final String TROVA_TOPIC = "Messaggio.trovaTopic";

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@ spec_public
    //@ nullable
    private Long id;

    /**
     * Data e ore di creazione del messaggio.
     */
    //@ spec_public
    //@ nullable
    private LocalDateTime dateTime;

    /**
     * Corpo del messaggio. Non può essere null.
     * */
    @Column(nullable = false)
    //@ spec_public
    //@ nullable
    private String body;

    /**
     * Autore del messaggio. E' un riferimento a un oggetto di tipo Accademico.
     * */
    @ManyToOne
    @JoinColumn(name = "autore")
    //@ spec_public
    //@ nullable
    private Accademico autore;

    /**
     * Destinatario del messaggio. E' un riferimento a un oggetto di tipo Accademico.
     * */
    @ManyToOne
    @JoinColumn(name = "destinatario")
    //@ spec_public
    //@ nullable
    private Accademico destinatario;

    /**
     * Topic del messaggio eventuale.
     */

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "topic")
    //@ spec_public
    //@ nullable
    private Topic topic;

    //Getter e Setter con Javadoc
    /**
     * Restituisce il destinatario del messaggio.
     *
     * @return Il destinatario
     * */
    /*@ public normal_behavior
      @ assignable \nothing;
      @ ensures \result == destinatario;
      @*/
    public /*@ nullable */ Accademico getDestinatario() {
        return destinatario;
    }

    /**
     * Imposta il destinatario del messaggio
     *
     * @param destinatario Il destinatario da impostare.
     * */
    /*@ public normal_behavior
      @ assignable this.destinatario;
      @ ensures this.destinatario == destinatario;
      @*/
    public void setDestinatario(Accademico destinatario) {
        this.destinatario = destinatario;
    }

    /**
     * Restituisce l'autore del messaggio.
     *
     * @return L'autore.
     * */
    /*@
      @ public normal_behavior
      @ assignable \nothing;
      @ ensures \result == autore;
      @*/
    public /*@ nullable */ Accademico getAutore() {
        return autore;
    }

    /**
     * Imposta l'autore del messaggio
     *
     * @param autore L'autore da impostare.
     * */
    /*@ public normal_behavior
      @ assignable this.autore;
      @ ensures this.autore == autore;
      @*/
    public void setAutore(Accademico autore) {
        this.autore = autore;
    }

    /**
     * Costruttore vuoto necessario per JPA.
     * */
    /*@ public normal_behavior
      @ assignable \nothing;
      @ ensures true;
      @*/
    public Messaggio() {}

    /**
     * Costruttore per inizializzare un messaggio con i dati principali.
     *
     * @param autore    L'autore del Messaggio
     * @param destinatario  Il destinatario del Messaggio
     * @param topic Il topic del messaggio (opzionale)
     * @param body  Il corpo del Messaggio
     * @param dateTime  La data e ora di creazione
     * */
    /*@ public normal_behavior
      @ assignable \everything;
      @ ensures this.autore == autore;
      @ ensures this.destinatario == destinatario;
      @ ensures this.topic == topic;
      @ ensures this.body == body;
      @ ensures this.dateTime == dateTime;
      @*/
    public Messaggio(Accademico autore, Accademico destinatario, Topic topic, String body, LocalDateTime dateTime) {
        this.autore = autore;
        this.destinatario = destinatario;
        this.topic = topic;
        this.body = body;
        this.dateTime = dateTime;
    }


    /**
     * Restituisce la data e ora di creazione del messaggio.
     *
     * @return La data e ora.
     *
     * */
    /*@ public normal_behavior
      @ assignable \nothing;
      @ ensures \result == dateTime;
      @*/
    public /*@ nullable */ LocalDateTime getDateTime() {
        return dateTime;
    }

    /**
     * Imposta la data e ora di creazione del messaggio.
     *
     * @param dateTime La data e ora da impostare.
     * */
    /*@ public normal_behavior
      @ assignable this.dateTime;
      @ ensures this.dateTime == dateTime;
      @*/
    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    /**
     * Restituisce il corpo del messaggio.
     *
     * @return Il corpo.
     * */
    /*@ public normal_behavior
      @ assignable \nothing;
      @ ensures \result == body;
      @*/
    public /*@ nullable */ String getBody() {
        return body;
    }

    /**
     * Imposta il corpo del messaggio.
     *
     * @param body Il corpo da impostare.
     * */
    /*@ public normal_behavior
      @ assignable this.body;
      @ ensures this.body == body;
      @*/
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * Restituisce il topic del messaggio.
     *
     * @return Il topic.
     * */
    /*@ public normal_behavior
      @ assignable \nothing;
      @ ensures \result == topic;
      @*/
    public /*@ nullable */ Topic getTopic() {
        return topic;
    }

    /**
     * Imposta il topic del messaggio.
     *
     * @param topic Il topic da impostare.
     * */
    /*@ public normal_behavior
      @ assignable this.topic;
      @ ensures this.topic == topic;
      @*/
    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    /**
     * Restituisce l'id del messaggio
     *
     * @return L'id
     */
    /*@ public normal_behavior
      @ assignable \nothing;
      @ ensures \result == id;
      @*/
    public /*@ nullable */ Long getId() {
        return id;
    }

    /**
     * Rappresentazione testuale dell'oggetto Messaggio.
     *
     * @return Una stringa che descrive il messaggio.
     * */
    //@ skipesc
    @Override
    public String toString() {
        return "Messaggio{" +
                "id=" + id +
                ", topic='" + topic + '\'' +
                ", dateTime=" + dateTime +
                ", body='" + body + '\'' +
                ", autore=" + autore +
                ", destinatario=" + destinatario +
                '}';
    }
}
