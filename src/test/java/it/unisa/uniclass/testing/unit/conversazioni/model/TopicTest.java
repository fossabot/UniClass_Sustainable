package it.unisa.uniclass.testing.unit.conversazioni.model;

import it.unisa.uniclass.conversazioni.model.Topic;
import it.unisa.uniclass.orari.model.Corso;
import it.unisa.uniclass.orari.model.CorsoLaurea;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test completi per la classe Topic.
 * Coverage massimizzata per JaCoCo: getter, setter, costruttori, toString.
 */
public class TopicTest {

    private Topic topic;
    private CorsoLaurea corsoLaurea;
    private Corso corso;

    @BeforeEach
    public void setUp() {
        // Setup CorsoLaurea
        corsoLaurea = new CorsoLaurea();
        corsoLaurea.setNome("Informatica");

        // Setup Corso
        corso = new Corso();
        corso.setNome("Programmazione Distribuita");
        corso.setCorsoLaurea(corsoLaurea);

        // Crea topic con costruttore vuoto
        topic = new Topic();
    }

    @Test
    public void testCostruttoreVuoto() {
        System.out.println("\n=== Test 1: Costruttore vuoto ===");

        Topic t = new Topic();

        assertNotNull(t);
        assertNull(t.getId());
        assertNull(t.getNome());
        assertNull(t.getCorsoLaurea());
        assertNull(t.getCorso());

        System.out.println("✓ Costruttore vuoto testato correttamente");
    }

    @Test
    public void testGetSetNome() {
        System.out.println("\n=== Test 2: Get/Set Nome ===");

        assertNull(topic.getNome());

        String nome = "Esame Programmazione";
        topic.setNome(nome);

        assertEquals(nome, topic.getNome());

        System.out.println("✓ Getter e Setter di Nome funzionano correttamente");
    }

    @Test
    public void testGetSetNomeVuoto() {
        System.out.println("\n=== Test 3: Get/Set Nome vuoto ===");

        topic.setNome("");

        assertEquals("", topic.getNome());
        assertTrue(topic.getNome().isEmpty());

        System.out.println("✓ Nome vuoto gestito correttamente");
    }

    @Test
    public void testGetSetNomeLungo() {
        System.out.println("\n=== Test 4: Get/Set Nome lungo ===");

        String nomeLungo = "Programmazione Distribuita e Sistemi Paralleli - Modulo Avanzato ".repeat(5);
        topic.setNome(nomeLungo);

        assertEquals(nomeLungo, topic.getNome());
        assertTrue(topic.getNome().length() > 100);

        System.out.println("✓ Nome lungo (" + nomeLungo.length() + " caratteri) gestito correttamente");
    }

    @Test
    public void testGetSetNomeConCaratteriSpeciali() {
        System.out.println("\n=== Test 5: Get/Set Nome con caratteri speciali ===");

        String nomeSpeciale = "Esame àèéìòù - C++ & Java (2024) €50";
        topic.setNome(nomeSpeciale);

        assertEquals(nomeSpeciale, topic.getNome());

        System.out.println("✓ Nome con caratteri speciali gestito correttamente");
    }

    @Test
    public void testGetSetCorsoLaurea() {
        System.out.println("\n=== Test 6: Get/Set CorsoLaurea ===");

        assertNull(topic.getCorsoLaurea());

        topic.setCorsoLaurea(corsoLaurea);

        assertEquals(corsoLaurea, topic.getCorsoLaurea());
        assertEquals("Informatica", topic.getCorsoLaurea().getNome());

        System.out.println("✓ Getter e Setter di CorsoLaurea funzionano correttamente");
    }

    @Test
    public void testGetSetCorsoLaureaNull() {
        System.out.println("\n=== Test 7: Get/Set CorsoLaurea null ===");

        topic.setCorsoLaurea(corsoLaurea);
        assertNotNull(topic.getCorsoLaurea());

        topic.setCorsoLaurea(null);
        assertNull(topic.getCorsoLaurea());

        System.out.println("✓ CorsoLaurea null gestito correttamente");
    }

    @Test
    public void testGetSetCorso() {
        System.out.println("\n=== Test 8: Get/Set Corso ===");

        assertNull(topic.getCorso());

        topic.setCorso(corso);

        assertEquals(corso, topic.getCorso());
        assertEquals("Programmazione Distribuita", topic.getCorso().getNome());

        System.out.println("✓ Getter e Setter di Corso funzionano correttamente");
    }

    @Test
    public void testGetSetCorsoNull() {
        System.out.println("\n=== Test 9: Get/Set Corso null ===");

        topic.setCorso(corso);
        assertNotNull(topic.getCorso());

        topic.setCorso(null);
        assertNull(topic.getCorso());

        System.out.println("✓ Corso null gestito correttamente");
    }

    @Test
    public void testGetIdNonImpostato() {
        System.out.println("\n=== Test 10: Get ID non impostato ===");

        assertNull(topic.getId());

        System.out.println("✓ ID null (non impostato) gestito correttamente");
    }

    @Test
    public void testTopicConCorsoLaurea() {
        System.out.println("\n=== Test 11: Topic associato a CorsoLaurea ===");

        topic.setNome("Avviso Generale Informatica");
        topic.setCorsoLaurea(corsoLaurea);
        topic.setCorso(null); // Topic di corso di laurea, non corso specifico

        assertEquals("Avviso Generale Informatica", topic.getNome());
        assertEquals(corsoLaurea, topic.getCorsoLaurea());
        assertNull(topic.getCorso());
        assertEquals("Informatica", topic.getCorsoLaurea().getNome());

        System.out.println("✓ Topic associato a CorsoLaurea validato");
    }

    @Test
    public void testTopicConCorsoSpecifico() {
        System.out.println("\n=== Test 12: Topic associato a Corso specifico ===");

        topic.setNome("Lezione 5 - Thread");
        topic.setCorso(corso);
        topic.setCorsoLaurea(null); // Topic di corso specifico, non corso di laurea

        assertEquals("Lezione 5 - Thread", topic.getNome());
        assertEquals(corso, topic.getCorso());
        assertNull(topic.getCorsoLaurea());
        assertEquals("Programmazione Distribuita", topic.getCorso().getNome());

        System.out.println("✓ Topic associato a Corso specifico validato");
    }

    @Test
    public void testTopicConEntrambi() {
        System.out.println("\n=== Test 13: Topic con sia Corso che CorsoLaurea ===");

        topic.setNome("Topic Misto");
        topic.setCorso(corso);
        topic.setCorsoLaurea(corsoLaurea);

        assertEquals("Topic Misto", topic.getNome());
        assertNotNull(topic.getCorso());
        assertNotNull(topic.getCorsoLaurea());
        assertEquals(corso, topic.getCorso());
        assertEquals(corsoLaurea, topic.getCorsoLaurea());

        System.out.println("✓ Topic con entrambi Corso e CorsoLaurea gestito");
    }

    @Test
    public void testToStringConTuttiICampi() {
        System.out.println("\n=== Test 14: ToString con tutti i campi ===");

        topic.setNome("Test Topic");
        topic.setCorsoLaurea(corsoLaurea);
        topic.setCorso(corso);

        String result = topic.toString();

        assertNotNull(result);
        assertTrue(result.contains("Topic{"));
        assertTrue(result.contains("id="));
        assertTrue(result.contains("nome='Test Topic'"));
        assertTrue(result.contains("corsoLaurea="));
        assertTrue(result.contains("corso="));

        System.out.println("✓ ToString: " + result.substring(0, Math.min(100, result.length())) + "...");
    }

    @Test
    public void testToStringConCampiVuoti() {
        System.out.println("\n=== Test 15: ToString con campi vuoti ===");

        String result = topic.toString();

        assertNotNull(result);
        assertTrue(result.contains("Topic{"));
        assertTrue(result.contains("id=null"));
        assertTrue(result.contains("nome='null'"));
        assertTrue(result.contains("corsoLaurea=null"));
        assertTrue(result.contains("corso=null"));

        System.out.println("✓ ToString con campi null: " + result);
    }

    @Test
    public void testToStringConSoloNome() {
        System.out.println("\n=== Test 16: ToString con solo nome ===");

        topic.setNome("Solo Nome");

        String result = topic.toString();

        assertNotNull(result);
        assertTrue(result.contains("nome='Solo Nome'"));
        assertTrue(result.contains("corsoLaurea=null"));
        assertTrue(result.contains("corso=null"));

        System.out.println("✓ ToString con solo nome: " + result);
    }

    @Test
    public void testToStringConNomeVuoto() {
        System.out.println("\n=== Test 17: ToString con nome vuoto ===");

        topic.setNome("");
        topic.setCorsoLaurea(corsoLaurea);

        String result = topic.toString();

        assertNotNull(result);
        assertTrue(result.contains("nome=''"));

        System.out.println("✓ ToString con nome vuoto gestito");
    }

    @Test
    public void testModificaCampiTopic() {
        System.out.println("\n=== Test 18: Modifica campi esistenti ===");

        // Imposta valori iniziali
        topic.setNome("Nome Iniziale");
        topic.setCorsoLaurea(corsoLaurea);

        assertEquals("Nome Iniziale", topic.getNome());
        assertEquals(corsoLaurea, topic.getCorsoLaurea());

        // Modifica i valori
        topic.setNome("Nome Modificato");

        CorsoLaurea nuovoCorso = new CorsoLaurea();
        nuovoCorso.setNome("Ingegneria Informatica");
        topic.setCorsoLaurea(nuovoCorso);

        assertEquals("Nome Modificato", topic.getNome());
        assertEquals(nuovoCorso, topic.getCorsoLaurea());
        assertEquals("Ingegneria Informatica", topic.getCorsoLaurea().getNome());

        System.out.println("✓ Modifica campi funziona correttamente");
    }

    @Test
    public void testCostantiNamedQueries() {
        System.out.println("\n=== Test 19: Verifica costanti NamedQueries ===");

        assertEquals("Topic.trovaId", Topic.TROVA_ID);
        assertEquals("Topic.trovaNome", Topic.TROVA_NOME);
        assertEquals("Topic.trovaCorsoLaurea", Topic.TROVA_CORSOLAUREA);
        assertEquals("Topic.trovaCorso", Topic.TROVA_CORSO);
        assertEquals("Topic.trovaTutti", Topic.TROVA_TUTTI);

        System.out.println("✓ Tutte le 5 costanti NamedQueries verificate");
    }

    @Test
    public void testTopicDiversi() {
        System.out.println("\n=== Test 20: Topic diversi ===");

        Topic topic1 = new Topic();
        topic1.setNome("Topic 1");
        topic1.setCorsoLaurea(corsoLaurea);

        Topic topic2 = new Topic();
        topic2.setNome("Topic 2");
        topic2.setCorso(corso);

        assertNotEquals(topic1.getNome(), topic2.getNome());
        assertNotNull(topic1.getCorsoLaurea());
        assertNull(topic1.getCorso());
        assertNull(topic2.getCorsoLaurea());
        assertNotNull(topic2.getCorso());

        System.out.println("✓ Topic diversi gestiti correttamente");
    }

    @Test
    public void testTopicPerEsame() {
        System.out.println("\n=== Test 21: Topic per esame ===");

        topic.setNome("Esame Finale - Sessione Estiva");
        topic.setCorso(corso);

        assertEquals("Esame Finale - Sessione Estiva", topic.getNome());
        assertTrue(topic.getNome().contains("Esame"));
        assertNotNull(topic.getCorso());

        System.out.println("✓ Topic per esame creato correttamente");
    }

    @Test
    public void testTopicPerLezione() {
        System.out.println("\n=== Test 22: Topic per lezione ===");

        topic.setNome("Lezione 10 - Sincronizzazione");
        topic.setCorso(corso);

        assertEquals("Lezione 10 - Sincronizzazione", topic.getNome());
        assertTrue(topic.getNome().contains("Lezione"));
        assertNotNull(topic.getCorso());

        System.out.println("✓ Topic per lezione creato correttamente");
    }

    @Test
    public void testTopicPerAvviso() {
        System.out.println("\n=== Test 23: Topic per avviso generale ===");

        topic.setNome("Avviso: Chiusura Ateneo");
        topic.setCorsoLaurea(corsoLaurea);

        assertEquals("Avviso: Chiusura Ateneo", topic.getNome());
        assertTrue(topic.getNome().contains("Avviso"));
        assertNotNull(topic.getCorsoLaurea());

        System.out.println("✓ Topic per avviso creato correttamente");
    }

    @Test
    public void testTopicConDiversiCorsiLaurea() {
        System.out.println("\n=== Test 24: Topic con diversi corsi di laurea ===");

        CorsoLaurea informatica = new CorsoLaurea();
        informatica.setNome("Informatica");

        CorsoLaurea ingegneria = new CorsoLaurea();
        ingegneria.setNome("Ingegneria Informatica");

        Topic t1 = new Topic();
        t1.setNome("Topic Informatica");
        t1.setCorsoLaurea(informatica);

        Topic t2 = new Topic();
        t2.setNome("Topic Ingegneria");
        t2.setCorsoLaurea(ingegneria);

        assertNotEquals(t1.getCorsoLaurea().getNome(), t2.getCorsoLaurea().getNome());
        assertEquals("Informatica", t1.getCorsoLaurea().getNome());
        assertEquals("Ingegneria Informatica", t2.getCorsoLaurea().getNome());

        System.out.println("✓ Topic con diversi corsi di laurea gestiti");
    }

    @Test
    public void testTopicConDiversiCorsi() {
        System.out.println("\n=== Test 25: Topic con diversi corsi ===");

        Corso corso1 = new Corso();
        corso1.setNome("Programmazione I");

        Corso corso2 = new Corso();
        corso2.setNome("Programmazione II");

        Topic t1 = new Topic();
        t1.setNome("Topic Prog I");
        t1.setCorso(corso1);

        Topic t2 = new Topic();
        t2.setNome("Topic Prog II");
        t2.setCorso(corso2);

        assertNotEquals(t1.getCorso().getNome(), t2.getCorso().getNome());
        assertEquals("Programmazione I", t1.getCorso().getNome());
        assertEquals("Programmazione II", t2.getCorso().getNome());

        System.out.println("✓ Topic con diversi corsi gestiti");
    }

    @Test
    public void testSetterChaining() {
        System.out.println("\n=== Test 26: Setter multipli in sequenza ===");

        Topic t = new Topic();

        t.setNome("Test");
        t.setCorsoLaurea(corsoLaurea);
        t.setCorso(corso);

        assertNotNull(t.getNome());
        assertNotNull(t.getCorsoLaurea());
        assertNotNull(t.getCorso());

        // Sovrascrittura
        t.setNome("Nuovo Nome");
        t.setCorsoLaurea(null);

        assertEquals("Nuovo Nome", t.getNome());
        assertNull(t.getCorsoLaurea());
        assertNotNull(t.getCorso());

        System.out.println("✓ Setter multipli e sovrascritture gestiti");
    }

    @Test
    public void testToStringConIdNull() {
        System.out.println("\n=== Test 27: ToString con ID null ===");

        topic.setNome("Test ID");

        String result = topic.toString();

        assertTrue(result.contains("id=null"));
        assertTrue(result.contains("nome='Test ID'"));

        System.out.println("✓ ToString con ID null: " + result.substring(0, Math.min(80, result.length())));
    }

    @Test
    public void testNomeConNewlineETabs() {
        System.out.println("\n=== Test 28: Nome con newline e tabs ===");

        String nomeMultiline = "Topic\ncon\nnewline\te\ttab";
        topic.setNome(nomeMultiline);

        assertEquals(nomeMultiline, topic.getNome());
        assertTrue(topic.getNome().contains("\n"));
        assertTrue(topic.getNome().contains("\t"));

        System.out.println("✓ Nome multilinea gestito");
    }

    @Test
    public void testTopicCompleto() {
        System.out.println("\n=== Test 29: Topic completo con tutti i campi ===");

        topic.setNome("Programmazione Distribuita - Esame Finale");
        topic.setCorsoLaurea(corsoLaurea);
        topic.setCorso(corso);

        assertNotNull(topic.getNome());
        assertNotNull(topic.getCorsoLaurea());
        assertNotNull(topic.getCorso());

        assertEquals("Programmazione Distribuita - Esame Finale", topic.getNome());
        assertEquals("Informatica", topic.getCorsoLaurea().getNome());
        assertEquals("Programmazione Distribuita", topic.getCorso().getNome());

        String result = topic.toString();
        assertNotNull(result);
        assertTrue(result.length() > 50);

        System.out.println("✓ Topic completo validato con successo");
    }

    @Test
    public void testResetTopic() {
        System.out.println("\n=== Test 30: Reset di tutti i campi ===");

        // Imposta tutti i campi
        topic.setNome("Nome Test");
        topic.setCorsoLaurea(corsoLaurea);
        topic.setCorso(corso);

        assertNotNull(topic.getNome());
        assertNotNull(topic.getCorsoLaurea());
        assertNotNull(topic.getCorso());

        // Reset tutti i campi
        topic.setNome(null);
        topic.setCorsoLaurea(null);
        topic.setCorso(null);

        assertNull(topic.getNome());
        assertNull(topic.getCorsoLaurea());
        assertNull(topic.getCorso());

        System.out.println("✓ Reset di tutti i campi funziona correttamente");
    }
}
