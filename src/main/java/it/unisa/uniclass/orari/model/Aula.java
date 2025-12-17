package it.unisa.uniclass.orari.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static it.unisa.uniclass.orari.model.Aula.*;

/**
 * Classe che rappresenta un'Aula universitaria, con informazioni relative a edificio, nome e relazioni con lezioni e appelli.
 * */
@Entity
@Access(AccessType.FIELD)
@Table(name = "aule")
@NamedQueries({
        @NamedQuery(name = "Aula.trovaAulaNome", query = "SELECT a FROM Aula a WHERE a.nome = :nome"),
        @NamedQuery(name = "Aula.trovaAula", query = "SELECT a FROM Aula a WHERE a.id = :id"),
        @NamedQuery(name = "Aula.trovaAulaEdificio", query = "SELECT a FROM Aula a WHERE a.edificio = :edificio"),
        @NamedQuery(name = "Aula.trovaTutte", query = "SELECT a FROM Aula a"),
        @NamedQuery(name = "Aula.trovaEdifici", query = "SELECT a.edificio FROM Aula a GROUP BY a.edificio")
})
public class Aula implements Serializable {
    /**
     * Nome della query per trovare un'aula dato il suo nome
     * */

    public static final String TROVA_AULANOME = "Aula.trovaAulaNome";
    /**
     * Nome della query per trovare un'aula dato il suo ID
     * */
    public static final String TROVA_AULA = "Aula.trovaAula";
    /**
     * Nome della query per trovare un'aula dato l'edificio
     * */
    public static final String TROVA_AULA_EDIFICIO = "Aula.trovaAulaEdificio";
    /**
     * Nome della query per trovare tutte le aule.
     * */
    public static final String TROVA_TUTTE = "Aula.trovaTutte";

    /**
     * Nome della query per trovare tutti gli edifici (distinti)
     */
    public static final String TROVA_EDIFICI = "Aula.trovaEdifici";

    /**
     * Identificativo univoco dell'Aula
     * */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@ spec_public
    private int id;

    /**
     * Elenco delle lezioni associate all'aula
     * */
    @OneToMany(mappedBy = "aula", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    //@ spec_public
    //@ nullable
    private List<Lezione> lezioni = new ArrayList<>();

    /**
     * Edificio in cui si trova l'Aula
     * */
    //@ spec_public
    //@ nullable
    private String edificio;
    /**
     * Nome dell'Aula
     * */
    //@ spec_public
    //@ nullable
    private String nome;

    /**
     * Costruttore completo per creare un'istanza di Aula.
     *
     * @param id Identificativo dell'Aula
     * @param edificio Nome dell'edificio in cui si trova al'aula.
     * @param nome Nome dell'Aula
     * */
    /*@
      @ public normal_behavior
      @ assignable \everything;
      @ ensures this.id == id;
      @ ensures this.edificio == edificio;
      @ ensures this.nome == nome;
      @ ensures true;
      @*/
    public Aula(int id, String edificio, String nome) {
        this.id = id;
        this.edificio = edificio;
        this.nome = nome;
    }

    /**
     * Costruttore di default per creare un'istanza vuota di Aula.
     * */
    /*@
      @ public normal_behavior
      @ assignable \nothing;
      @ ensures true;
      @*/
    public Aula() {}

    /**
     * Restituisce l'identificativo dell'aula.
     *
     * @return L'identificativo dell'aula
     * */
    /*@
      @ public normal_behavior
      @ assignable \nothing;
      @ ensures \result == id;
      @*/
    public int getId() {
        return id;
    }

    /**
     * Restituisc l'elenco delle lezioni associate all'aula.
     *
     * @return Lista delle lezioni
     * */
    /*@
      @ public normal_behavior
      @ assignable \nothing;
      @ ensures \result == lezioni;
      @*/
    public /*@ nullable */ List<Lezione> getLezioni() {
        return lezioni;
    }

    /**
     * Restituisce l'edificio in cui si trova l'aula.
     *
     * @return Nome del'edificio.
     * */
    /*@
      @ public normal_behavior
      @ assignable \nothing;
      @ ensures \result == edificio;
      @*/
    public /*@ nullable */ String getEdificio() {
        return edificio;
    }

    /**
     * Restituisce il nome dell'aula.
     *
     * @return Nome del'aula
     * */
    /*@
      @ public normal_behavior
      @ assignable \nothing;
      @ ensures \result == nome;
      @*/
    public /*@ nullable */ String getNome() {
        return nome;
    }

    /**
     * Imposta il nome dell'edificio in cui si trova l'aula.
     *
     * @param edificio Nome dell'edificio
     * */
    /*@ public normal_behavior
      @ assignable this.edificio;
      @ ensures this.edificio == edificio;
      @*/
    public void setEdificio(String edificio) {
        this.edificio = edificio;
    }

    /**
     * Imposta il nome dell'Aula
     *
     * @param nome Nome dell'aula.
     * */
    /*@ public normal_behavior
      @ assignable this.nome;
      @ ensures this.nome == nome;
      @*/
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Restituisce una rappresentazione testuale dell'oggetto Aula.
     *
     * @return Sttringa rappresentativa dell'aula.
     * */
    //@ skipesc
    @Override
    public String toString() {
        return "Aula{" +
                "id=" + id +
                ", edificio='" + edificio + '\'' +
                ", nome='" + nome + '\'' +
                '}';
    }
}
