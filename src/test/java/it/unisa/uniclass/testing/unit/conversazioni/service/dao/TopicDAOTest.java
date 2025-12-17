package it.unisa.uniclass.testing.unit.conversazioni.service.dao;

import it.unisa.uniclass.conversazioni.model.Topic;
import it.unisa.uniclass.conversazioni.service.dao.TopicDAO;
import it.unisa.uniclass.orari.model.Corso;
import it.unisa.uniclass.orari.model.CorsoLaurea;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Test completi per la classe TopicDAO.
 * Coverage massimizzata per JaCoCo: tutti i metodi del DAO.
 */
public class TopicDAOTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<Topic> typedQuery;

    private TopicDAO topicDAO;

    private Topic topic;
    private CorsoLaurea corsoLaurea;
    private Corso corso;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        // Inject EntityManager usando reflection
        topicDAO = new TopicDAO();
        Field emField = TopicDAO.class.getDeclaredField("emUniClass");
        emField.setAccessible(true);
        emField.set(topicDAO, entityManager);

        // Setup CorsoLaurea
        corsoLaurea = new CorsoLaurea();
        corsoLaurea.setNome("Informatica");

        // Setup Corso
        corso = new Corso();
        corso.setNome("Programmazione Distribuita");
        corso.setCorsoLaurea(corsoLaurea);

        // Setup Topic
        topic = new Topic();
        topic.setNome("Esame Finale");
        topic.setCorsoLaurea(corsoLaurea);
    }

    @Test
    public void testTrovaId() {
        System.out.println("\n=== Test 1: trovaId ===");

        long id = 1L;

        when(entityManager.createNamedQuery(eq(Topic.TROVA_ID), eq(Topic.class)))
                .thenReturn(typedQuery);
        when(typedQuery.setParameter(eq("id"), eq(id))).thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenReturn(topic);

        Topic result = topicDAO.trovaId(id);

        assertNotNull(result);
        assertEquals(topic, result);
        assertEquals("Esame Finale", result.getNome());
        verify(entityManager).createNamedQuery(Topic.TROVA_ID, Topic.class);
        verify(typedQuery).setParameter("id", id);
        verify(typedQuery).getSingleResult();

        System.out.println("✓ trovaId funziona correttamente per ID: " + id);
    }

    @Test
    public void testTrovaIdDiversi() {
        System.out.println("\n=== Test 2: trovaId - ID diversi ===");

        long[] ids = {1L, 10L, 100L, 999L};

        when(entityManager.createNamedQuery(eq(Topic.TROVA_ID), eq(Topic.class)))
                .thenReturn(typedQuery);
        when(typedQuery.setParameter(anyString(), anyLong())).thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenReturn(topic);

        for (long id : ids) {
            Topic result = topicDAO.trovaId(id);
            assertNotNull(result);
        }

        verify(entityManager, times(4)).createNamedQuery(Topic.TROVA_ID, Topic.class);

        System.out.println("✓ " + ids.length + " ID diversi testati");
    }

    @Test
    public void testTrovaNome() {
        System.out.println("\n=== Test 3: trovaNome ===");

        String nome = "Esame Finale";

        when(entityManager.createNamedQuery(eq(Topic.TROVA_NOME), eq(Topic.class)))
                .thenReturn(typedQuery);
        when(typedQuery.setParameter(eq("nome"), eq(nome))).thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenReturn(topic);

        Topic result = topicDAO.trovaNome(nome);

        assertNotNull(result);
        assertEquals(topic, result);
        assertEquals(nome, result.getNome());
        verify(entityManager).createNamedQuery(Topic.TROVA_NOME, Topic.class);
        verify(typedQuery).setParameter("nome", nome);
        verify(typedQuery).getSingleResult();

        System.out.println("✓ trovaNome funziona per: '" + nome + "'");
    }

    @Test
    public void testTrovaNomeDiversi() {
        System.out.println("\n=== Test 4: trovaNome - nomi diversi ===");

        String[] nomi = {
            "Lezione 1",
            "Esame Programmazione",
            "Avviso Urgente",
            "Ricevimento Studenti"
        };

        when(entityManager.createNamedQuery(eq(Topic.TROVA_NOME), eq(Topic.class)))
                .thenReturn(typedQuery);
        when(typedQuery.setParameter(anyString(), anyString())).thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenReturn(topic);

        for (String nome : nomi) {
            Topic result = topicDAO.trovaNome(nome);
            assertNotNull(result);
        }

        verify(entityManager, times(4)).createNamedQuery(Topic.TROVA_NOME, Topic.class);

        System.out.println("✓ " + nomi.length + " nomi diversi testati");
    }

    @Test
    public void testTrovaCorsoLaurea() {
        System.out.println("\n=== Test 5: trovaCorsoLaurea ===");

        String nomeCorsoLaurea = "Informatica";

        when(entityManager.createNamedQuery(eq(Topic.TROVA_CORSOLAUREA), eq(Topic.class)))
                .thenReturn(typedQuery);
        when(typedQuery.setParameter(eq("nome"), eq(nomeCorsoLaurea))).thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenReturn(topic);

        Topic result = topicDAO.trovaCorsoLaurea(nomeCorsoLaurea);

        assertNotNull(result);
        assertEquals(topic, result);
        assertNotNull(result.getCorsoLaurea());
        verify(entityManager).createNamedQuery(Topic.TROVA_CORSOLAUREA, Topic.class);
        verify(typedQuery).setParameter("nome", nomeCorsoLaurea);

        System.out.println("✓ trovaCorsoLaurea funziona per: '" + nomeCorsoLaurea + "'");
    }

    @Test
    public void testTrovaCorsoLaureaDiversi() {
        System.out.println("\n=== Test 6: trovaCorsoLaurea - corsi di laurea diversi ===");

        String[] corsiLaurea = {
            "Informatica",
            "Ingegneria Informatica",
            "Matematica",
            "Fisica"
        };

        when(entityManager.createNamedQuery(eq(Topic.TROVA_CORSOLAUREA), eq(Topic.class)))
                .thenReturn(typedQuery);
        when(typedQuery.setParameter(anyString(), anyString())).thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenReturn(topic);

        for (String cdl : corsiLaurea) {
            Topic result = topicDAO.trovaCorsoLaurea(cdl);
            assertNotNull(result);
        }

        verify(entityManager, times(4)).createNamedQuery(Topic.TROVA_CORSOLAUREA, Topic.class);

        System.out.println("✓ " + corsiLaurea.length + " corsi di laurea testati");
    }

    @Test
    public void testTrovaCorso() {
        System.out.println("\n=== Test 7: trovaCorso ===");

        String nomeCorso = "Programmazione Distribuita";

        Topic topicCorso = new Topic();
        topicCorso.setNome("Lezione 1");
        topicCorso.setCorso(corso);

        when(entityManager.createNamedQuery(eq(Topic.TROVA_CORSO), eq(Topic.class)))
                .thenReturn(typedQuery);
        when(typedQuery.setParameter(eq("nome"), eq(nomeCorso))).thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenReturn(topicCorso);

        Topic result = topicDAO.trovaCorso(nomeCorso);

        assertNotNull(result);
        assertEquals(topicCorso, result);
        assertNotNull(result.getCorso());
        verify(entityManager).createNamedQuery(Topic.TROVA_CORSO, Topic.class);
        verify(typedQuery).setParameter("nome", nomeCorso);

        System.out.println("✓ trovaCorso funziona per: '" + nomeCorso + "'");
    }

    @Test
    public void testTrovaCorsiDiversi() {
        System.out.println("\n=== Test 8: trovaCorso - corsi diversi ===");

        String[] corsi = {
            "Programmazione I",
            "Programmazione II",
            "Basi di Dati",
            "Sistemi Operativi"
        };

        when(entityManager.createNamedQuery(eq(Topic.TROVA_CORSO), eq(Topic.class)))
                .thenReturn(typedQuery);
        when(typedQuery.setParameter(anyString(), anyString())).thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenReturn(topic);

        for (String c : corsi) {
            Topic result = topicDAO.trovaCorso(c);
            assertNotNull(result);
        }

        verify(entityManager, times(4)).createNamedQuery(Topic.TROVA_CORSO, Topic.class);

        System.out.println("✓ " + corsi.length + " corsi testati");
    }

    @Test
    public void testTrovaTutti() {
        System.out.println("\n=== Test 9: trovaTutti ===");

        Topic topic1 = new Topic();
        topic1.setNome("Topic 1");
        Topic topic2 = new Topic();
        topic2.setNome("Topic 2");
        Topic topic3 = new Topic();
        topic3.setNome("Topic 3");

        List<Topic> topics = Arrays.asList(topic1, topic2, topic3);

        when(entityManager.createNamedQuery(eq(Topic.TROVA_TUTTI), eq(Topic.class)))
                .thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(topics);

        List<Topic> result = topicDAO.trovaTutti();

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(topics, result);
        verify(entityManager).createNamedQuery(Topic.TROVA_TUTTI, Topic.class);
        verify(typedQuery).getResultList();

        System.out.println("✓ trovaTutti restituisce " + result.size() + " topic");
    }

    @Test
    public void testTrovaTuttiListaVuota() {
        System.out.println("\n=== Test 10: trovaTutti - lista vuota ===");

        when(entityManager.createNamedQuery(eq(Topic.TROVA_TUTTI), eq(Topic.class)))
                .thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(new ArrayList<>());

        List<Topic> result = topicDAO.trovaTutti();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(typedQuery).getResultList();

        System.out.println("✓ Lista vuota gestita correttamente");
    }

    @Test
    public void testTrovaTuttiListaGrande() {
        System.out.println("\n=== Test 11: trovaTutti - lista grande (50 elementi) ===");

        List<Topic> topics = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            Topic t = new Topic();
            t.setNome("Topic " + i);
            topics.add(t);
        }

        when(entityManager.createNamedQuery(eq(Topic.TROVA_TUTTI), eq(Topic.class)))
                .thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(topics);

        List<Topic> result = topicDAO.trovaTutti();

        assertEquals(50, result.size());

        System.out.println("✓ Lista grande con " + result.size() + " elementi gestita");
    }

    @Test
    public void testAggiungiTopicNuovo() {
        System.out.println("\n=== Test 12: aggiungiTopic - nuovo (ID null) ===");

        Topic nuovoTopic = new Topic();
        nuovoTopic.setNome("Nuovo Topic");
        // ID è null per nuovo topic

        doNothing().when(entityManager).persist(any(Topic.class));
        doNothing().when(entityManager).flush();

        topicDAO.aggiungiTopic(nuovoTopic);

        verify(entityManager).persist(nuovoTopic);
        verify(entityManager).flush();
        verify(entityManager, never()).merge(any(Topic.class));

        System.out.println("✓ Nuovo topic aggiunto con persist()");
    }

    @Test
    public void testAggiungiTopicEsistente() throws Exception {
        System.out.println("\n=== Test 13: aggiungiTopic - esistente (ID non null) ===");

        Topic topicEsistente = new Topic();
        topicEsistente.setNome("Topic Esistente");

        // Set ID usando reflection per simulare topic esistente
        Field idField = Topic.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(topicEsistente, 100L);

        when(entityManager.merge(any(Topic.class))).thenReturn(topicEsistente);
        doNothing().when(entityManager).flush();

        topicDAO.aggiungiTopic(topicEsistente);

        verify(entityManager).merge(topicEsistente);
        verify(entityManager).flush();
        verify(entityManager, never()).persist(any(Topic.class));

        System.out.println("✓ Topic esistente aggiornato con merge()");
    }

    @Test
    public void testAggiungiTopicConFlush() {
        System.out.println("\n=== Test 14: aggiungiTopic - verifica flush ===");

        Topic t = new Topic();
        t.setNome("Test Flush");

        doNothing().when(entityManager).persist(any(Topic.class));
        doNothing().when(entityManager).flush();

        topicDAO.aggiungiTopic(t);

        verify(entityManager).flush();

        System.out.println("✓ Flush chiamato correttamente");
    }

    @Test
    public void testAggiungiTopicMultipli() {
        System.out.println("\n=== Test 15: aggiungiTopic - multipli ===");

        Topic t1 = new Topic();
        t1.setNome("Topic 1");
        Topic t2 = new Topic();
        t2.setNome("Topic 2");
        Topic t3 = new Topic();
        t3.setNome("Topic 3");

        doNothing().when(entityManager).persist(any(Topic.class));
        doNothing().when(entityManager).flush();

        topicDAO.aggiungiTopic(t1);
        topicDAO.aggiungiTopic(t2);
        topicDAO.aggiungiTopic(t3);

        verify(entityManager, times(3)).persist(any(Topic.class));
        verify(entityManager, times(3)).flush();

        System.out.println("✓ 3 topic aggiunti correttamente");
    }

    @Test
    public void testRimuoviTopic() {
        System.out.println("\n=== Test 16: rimuoviTopic ===");

        doNothing().when(entityManager).remove(any(Topic.class));

        topicDAO.rimuoviTopic(topic);

        verify(entityManager).remove(topic);

        System.out.println("✓ rimuoviTopic funziona correttamente");
    }

    @Test
    public void testRimuoviTopicMultipli() {
        System.out.println("\n=== Test 17: rimuoviTopic - multipli ===");

        Topic t1 = new Topic();
        Topic t2 = new Topic();
        Topic t3 = new Topic();

        doNothing().when(entityManager).remove(any(Topic.class));

        topicDAO.rimuoviTopic(t1);
        topicDAO.rimuoviTopic(t2);
        topicDAO.rimuoviTopic(t3);

        verify(entityManager, times(3)).remove(any(Topic.class));

        System.out.println("✓ 3 topic rimossi correttamente");
    }

    @Test
    public void testTuttiIMetodiQuery() {
        System.out.println("\n=== Test 18: Verifica tutte le NamedQueries ===");

        when(entityManager.createNamedQuery(anyString(), eq(Topic.class)))
                .thenReturn(typedQuery);
        when(typedQuery.setParameter(anyString(), any())).thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenReturn(topic);
        when(typedQuery.getResultList()).thenReturn(new ArrayList<>());

        // Testa tutte le query
        topicDAO.trovaId(1L);
        topicDAO.trovaNome("Test");
        topicDAO.trovaCorsoLaurea("Informatica");
        topicDAO.trovaCorso("Programmazione");
        topicDAO.trovaTutti();

        // Verifica che tutte le query siano state chiamate
        verify(entityManager, times(5)).createNamedQuery(anyString(), eq(Topic.class));

        System.out.println("✓ Tutte le 5 query verificate");
    }

    @Test
    public void testSequenzaCompleta() {
        System.out.println("\n=== Test 19: Sequenza completa - aggiungi, trova, rimuovi ===");

        Topic t = new Topic();
        t.setNome("Test Sequenza");

        // Aggiungi
        doNothing().when(entityManager).persist(any(Topic.class));
        doNothing().when(entityManager).flush();
        topicDAO.aggiungiTopic(t);
        verify(entityManager).persist(t);

        // Trova
        when(entityManager.createNamedQuery(anyString(), eq(Topic.class)))
                .thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(List.of(t));
        List<Topic> trovati = topicDAO.trovaTutti();
        assertEquals(1, trovati.size());

        // Rimuovi
        doNothing().when(entityManager).remove(any(Topic.class));
        topicDAO.rimuoviTopic(t);
        verify(entityManager).remove(t);

        System.out.println("✓ Sequenza completa eseguita con successo");
    }

    @Test
    public void testAggiungiTopicConCorsoLaurea() {
        System.out.println("\n=== Test 20: aggiungiTopic - con CorsoLaurea ===");

        Topic t = new Topic();
        t.setNome("Topic CDL");
        t.setCorsoLaurea(corsoLaurea);

        doNothing().when(entityManager).persist(any(Topic.class));
        doNothing().when(entityManager).flush();

        topicDAO.aggiungiTopic(t);

        verify(entityManager).persist(t);
        assertNotNull(t.getCorsoLaurea());

        System.out.println("✓ Topic con CorsoLaurea aggiunto");
    }

    @Test
    public void testAggiungiTopicConCorso() {
        System.out.println("\n=== Test 21: aggiungiTopic - con Corso ===");

        Topic t = new Topic();
        t.setNome("Topic Corso");
        t.setCorso(corso);

        doNothing().when(entityManager).persist(any(Topic.class));
        doNothing().when(entityManager).flush();

        topicDAO.aggiungiTopic(t);

        verify(entityManager).persist(t);
        assertNotNull(t.getCorso());

        System.out.println("✓ Topic con Corso aggiunto");
    }

    @Test
    public void testVerificaCostantiNamedQueries() {
        System.out.println("\n=== Test 22: Verifica costanti NamedQueries ===");

        assertEquals("Topic.trovaId", Topic.TROVA_ID);
        assertEquals("Topic.trovaNome", Topic.TROVA_NOME);
        assertEquals("Topic.trovaCorsoLaurea", Topic.TROVA_CORSOLAUREA);
        assertEquals("Topic.trovaCorso", Topic.TROVA_CORSO);
        assertEquals("Topic.trovaTutti", Topic.TROVA_TUTTI);

        System.out.println("✓ Tutte le 5 costanti NamedQueries corrette");
    }

    @Test
    public void testBranchCoverageAggiungiTopic() throws Exception {
        System.out.println("\n=== Test 23: Branch coverage completo aggiungiTopic ===");

        // Branch 1: ID == null (persist)
        Topic nuovoTopic = new Topic();
        nuovoTopic.setNome("Nuovo");

        doNothing().when(entityManager).persist(any(Topic.class));
        doNothing().when(entityManager).flush();

        topicDAO.aggiungiTopic(nuovoTopic);
        verify(entityManager, times(1)).persist(any(Topic.class));

        // Branch 2: ID != null (merge)
        Topic esistente = new Topic();
        esistente.setNome("Esistente");
        Field idField = Topic.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(esistente, 50L);

        when(entityManager.merge(any(Topic.class))).thenReturn(esistente);

        topicDAO.aggiungiTopic(esistente);
        verify(entityManager, times(1)).merge(any(Topic.class));

        System.out.println("✓ Entrambi i branch (persist e merge) coperti al 100%");
    }
}
