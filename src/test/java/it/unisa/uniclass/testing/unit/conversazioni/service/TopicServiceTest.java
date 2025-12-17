package it.unisa.uniclass.testing.unit.conversazioni.service;

import it.unisa.uniclass.conversazioni.model.Topic;
import it.unisa.uniclass.conversazioni.service.TopicService;
import it.unisa.uniclass.conversazioni.service.dao.TopicRemote;
import it.unisa.uniclass.orari.model.Corso;
import it.unisa.uniclass.orari.model.CorsoLaurea;
import jakarta.persistence.NoResultException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockitoAnnotations;

import javax.naming.InitialContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Test completi per la classe TopicService.
 * Coverage massimizzata per JaCoCo: tutti i metodi del service.
 */
public class TopicServiceTest {

    @Mock
    private TopicRemote topicDao;

    private TopicService topicService;

    private Topic topic;
    private CorsoLaurea corsoLaurea;
    private Corso corso;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        // Mock InitialContext per evitare JNDI lookup nel costruttore
        try (@SuppressWarnings("unused") MockedConstruction<InitialContext> mockedContext = mockConstruction(InitialContext.class,
                (mock, context) -> when(mock.lookup(anyString())).thenReturn(topicDao))) {
            topicService = new TopicService();
        }

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
        when(topicDao.trovaId(id)).thenReturn(topic);

        Topic result = topicService.trovaId(id);

        assertNotNull(result);
        assertEquals(topic, result);
        assertEquals("Esame Finale", result.getNome());
        verify(topicDao).trovaId(id);

        System.out.println("✓ trovaId funziona correttamente");
    }

    @Test
    public void testTrovaIdNoResultException() {
        System.out.println("\n=== Test 2: trovaId - NoResultException ===");

        long id = 999L;
        when(topicDao.trovaId(id)).thenThrow(new NoResultException());

        Topic result = topicService.trovaId(id);

        assertNull(result);
        verify(topicDao).trovaId(id);

        System.out.println("✓ NoResultException gestita correttamente, ritorna null");
    }

    @Test
    public void testTrovaIdDiversi() {
        System.out.println("\n=== Test 3: trovaId - ID diversi ===");

        when(topicDao.trovaId(anyLong())).thenReturn(topic);

        Topic result1 = topicService.trovaId(1L);
        Topic result2 = topicService.trovaId(10L);
        Topic result3 = topicService.trovaId(100L);

        assertNotNull(result1);
        assertNotNull(result2);
        assertNotNull(result3);
        verify(topicDao, times(3)).trovaId(anyLong());

        System.out.println("✓ 3 ID diversi testati");
    }

    @Test
    public void testTrovaNome() {
        System.out.println("\n=== Test 4: trovaNome ===");

        String nome = "Esame Finale";
        when(topicDao.trovaNome(nome)).thenReturn(topic);

        Topic result = topicService.trovaNome(nome);

        assertNotNull(result);
        assertEquals(topic, result);
        assertEquals(nome, result.getNome());
        verify(topicDao).trovaNome(nome);

        System.out.println("✓ trovaNome funziona per: '" + nome + "'");
    }

    @Test
    public void testTrovaNomeNoResultException() {
        System.out.println("\n=== Test 5: trovaNome - NoResultException ===");

        String nome = "Topic Inesistente";
        when(topicDao.trovaNome(nome)).thenThrow(new NoResultException());

        Topic result = topicService.trovaNome(nome);

        assertNull(result);
        verify(topicDao).trovaNome(nome);

        System.out.println("✓ NoResultException gestita correttamente");
    }

    @Test
    public void testTrovaNomeDiversi() {
        System.out.println("\n=== Test 6: trovaNome - nomi diversi ===");

        String[] nomi = {"Lezione 1", "Esame", "Avviso", "Ricevimento"};
        when(topicDao.trovaNome(anyString())).thenReturn(topic);

        for (String nome : nomi) {
            Topic result = topicService.trovaNome(nome);
            assertNotNull(result);
        }

        verify(topicDao, times(4)).trovaNome(anyString());

        System.out.println("✓ " + nomi.length + " nomi diversi testati");
    }

    @Test
    public void testTrovaCorsoLaurea() {
        System.out.println("\n=== Test 7: trovaCorsoLaurea ===");

        String nomeCorsoLaurea = "Informatica";
        when(topicDao.trovaCorsoLaurea(nomeCorsoLaurea)).thenReturn(topic);

        Topic result = topicService.trovaCorsoLaurea(nomeCorsoLaurea);

        assertNotNull(result);
        assertEquals(topic, result);
        assertNotNull(result.getCorsoLaurea());
        verify(topicDao).trovaCorsoLaurea(nomeCorsoLaurea);

        System.out.println("✓ trovaCorsoLaurea funziona per: '" + nomeCorsoLaurea + "'");
    }

    @Test
    public void testTrovaCorsoLaureaNoResultException() {
        System.out.println("\n=== Test 8: trovaCorsoLaurea - NoResultException ===");

        String nome = "Corso Inesistente";
        when(topicDao.trovaCorsoLaurea(nome)).thenThrow(new NoResultException());

        Topic result = topicService.trovaCorsoLaurea(nome);

        assertNull(result);
        verify(topicDao).trovaCorsoLaurea(nome);

        System.out.println("✓ NoResultException gestita correttamente");
    }

    @Test
    public void testTrovaCorsoLaureaDiversi() {
        System.out.println("\n=== Test 9: trovaCorsoLaurea - corsi diversi ===");

        String[] corsiLaurea = {"Informatica", "Ingegneria Informatica", "Matematica"};
        when(topicDao.trovaCorsoLaurea(anyString())).thenReturn(topic);

        for (String cdl : corsiLaurea) {
            Topic result = topicService.trovaCorsoLaurea(cdl);
            assertNotNull(result);
        }

        verify(topicDao, times(3)).trovaCorsoLaurea(anyString());

        System.out.println("✓ " + corsiLaurea.length + " corsi di laurea testati");
    }

    @Test
    public void testTrovaCorso() {
        System.out.println("\n=== Test 10: trovaCorso ===");

        String nomeCorso = "Programmazione Distribuita";
        Topic topicCorso = new Topic();
        topicCorso.setNome("Lezione 1");
        topicCorso.setCorso(corso);

        when(topicDao.trovaCorso(nomeCorso)).thenReturn(topicCorso);

        Topic result = topicService.trovaCorso(nomeCorso);

        assertNotNull(result);
        assertEquals(topicCorso, result);
        assertNotNull(result.getCorso());
        verify(topicDao).trovaCorso(nomeCorso);

        System.out.println("✓ trovaCorso funziona per: '" + nomeCorso + "'");
    }

    @Test
    public void testTrovaCorsoNoResultException() {
        System.out.println("\n=== Test 11: trovaCorso - NoResultException ===");

        String nome = "Corso Inesistente";
        when(topicDao.trovaCorso(nome)).thenThrow(new NoResultException());

        Topic result = topicService.trovaCorso(nome);

        assertNull(result);
        verify(topicDao).trovaCorso(nome);

        System.out.println("✓ NoResultException gestita correttamente");
    }

    @Test
    public void testTrovaCorsiDiversi() {
        System.out.println("\n=== Test 12: trovaCorso - corsi diversi ===");

        String[] corsi = {"Programmazione I", "Basi di Dati", "Sistemi Operativi"};
        when(topicDao.trovaCorso(anyString())).thenReturn(topic);

        for (String c : corsi) {
            Topic result = topicService.trovaCorso(c);
            assertNotNull(result);
        }

        verify(topicDao, times(3)).trovaCorso(anyString());

        System.out.println("✓ " + corsi.length + " corsi testati");
    }

    @Test
    public void testTrovaTutti() {
        System.out.println("\n=== Test 13: trovaTutti ===");

        Topic topic1 = new Topic();
        topic1.setNome("Topic 1");
        Topic topic2 = new Topic();
        topic2.setNome("Topic 2");
        Topic topic3 = new Topic();
        topic3.setNome("Topic 3");

        List<Topic> topics = Arrays.asList(topic1, topic2, topic3);
        when(topicDao.trovaTutti()).thenReturn(topics);

        List<Topic> result = topicService.trovaTutti();

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(topics, result);
        verify(topicDao).trovaTutti();

        System.out.println("✓ trovaTutti restituisce " + result.size() + " topic");
    }

    @Test
    public void testTrovaTuttiListaVuota() {
        System.out.println("\n=== Test 14: trovaTutti - lista vuota ===");

        when(topicDao.trovaTutti()).thenReturn(new ArrayList<>());

        List<Topic> result = topicService.trovaTutti();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(topicDao).trovaTutti();

        System.out.println("✓ Lista vuota gestita correttamente");
    }

    @Test
    public void testTrovaTuttiListaGrande() {
        System.out.println("\n=== Test 15: trovaTutti - lista grande ===");

        List<Topic> topics = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Topic t = new Topic();
            t.setNome("Topic " + i);
            topics.add(t);
        }

        when(topicDao.trovaTutti()).thenReturn(topics);

        List<Topic> result = topicService.trovaTutti();

        assertEquals(20, result.size());

        System.out.println("✓ Lista con " + result.size() + " topic gestita");
    }

    @Test
    public void testAggiungiTopic() {
        System.out.println("\n=== Test 16: aggiungiTopic ===");

        doNothing().when(topicDao).aggiungiTopic(topic);

        topicService.aggiungiTopic(topic);

        verify(topicDao).aggiungiTopic(topic);

        System.out.println("✓ aggiungiTopic funziona correttamente");
    }

    @Test
    public void testAggiungiTopicNuovo() {
        System.out.println("\n=== Test 17: aggiungiTopic - nuovo topic ===");

        Topic nuovoTopic = new Topic();
        nuovoTopic.setNome("Nuovo Topic");
        nuovoTopic.setCorsoLaurea(corsoLaurea);

        doNothing().when(topicDao).aggiungiTopic(nuovoTopic);

        topicService.aggiungiTopic(nuovoTopic);

        verify(topicDao).aggiungiTopic(nuovoTopic);

        System.out.println("✓ Nuovo topic aggiunto");
    }

    @Test
    public void testAggiungiTopicMultipli() {
        System.out.println("\n=== Test 18: aggiungiTopic - multipli ===");

        Topic t1 = new Topic();
        Topic t2 = new Topic();
        Topic t3 = new Topic();

        doNothing().when(topicDao).aggiungiTopic(any(Topic.class));

        topicService.aggiungiTopic(t1);
        topicService.aggiungiTopic(t2);
        topicService.aggiungiTopic(t3);

        verify(topicDao, times(3)).aggiungiTopic(any(Topic.class));

        System.out.println("✓ 3 topic aggiunti correttamente");
    }

    @Test
    public void testRimuoviTopic() {
        System.out.println("\n=== Test 19: rimuoviTopic ===");

        doNothing().when(topicDao).rimuoviTopic(topic);

        topicService.rimuoviTopic(topic);

        verify(topicDao).rimuoviTopic(topic);

        System.out.println("✓ rimuoviTopic funziona correttamente");
    }

    @Test
    public void testRimuoviTopicMultipli() {
        System.out.println("\n=== Test 20: rimuoviTopic - multipli ===");

        Topic t1 = new Topic();
        Topic t2 = new Topic();
        Topic t3 = new Topic();

        doNothing().when(topicDao).rimuoviTopic(any(Topic.class));

        topicService.rimuoviTopic(t1);
        topicService.rimuoviTopic(t2);
        topicService.rimuoviTopic(t3);

        verify(topicDao, times(3)).rimuoviTopic(any(Topic.class));

        System.out.println("✓ 3 topic rimossi correttamente");
    }

    @Test
    public void testTuttiIMetodi() {
        System.out.println("\n=== Test 21: Verifica tutti i metodi del service ===");

        // Setup mocks
        when(topicDao.trovaId(anyLong())).thenReturn(topic);
        when(topicDao.trovaNome(anyString())).thenReturn(topic);
        when(topicDao.trovaCorsoLaurea(anyString())).thenReturn(topic);
        when(topicDao.trovaCorso(anyString())).thenReturn(topic);
        when(topicDao.trovaTutti()).thenReturn(new ArrayList<>());
        doNothing().when(topicDao).aggiungiTopic(any(Topic.class));
        doNothing().when(topicDao).rimuoviTopic(any(Topic.class));

        // Chiama tutti i metodi
        topicService.trovaId(1L);
        topicService.trovaNome("Test");
        topicService.trovaCorsoLaurea("Informatica");
        topicService.trovaCorso("Programmazione");
        topicService.trovaTutti();
        topicService.aggiungiTopic(topic);
        topicService.rimuoviTopic(topic);

        // Verifica chiamate
        verify(topicDao).trovaId(anyLong());
        verify(topicDao).trovaNome(anyString());
        verify(topicDao).trovaCorsoLaurea(anyString());
        verify(topicDao).trovaCorso(anyString());
        verify(topicDao).trovaTutti();
        verify(topicDao).aggiungiTopic(any(Topic.class));
        verify(topicDao).rimuoviTopic(any(Topic.class));

        System.out.println("✓ Tutti i 7 metodi pubblici verificati");
    }

    @Test
    public void testSequenzaCompleta() {
        System.out.println("\n=== Test 22: Sequenza completa - aggiungi, trova, rimuovi ===");

        Topic t = new Topic();
        t.setNome("Test Sequenza");

        // Aggiungi
        doNothing().when(topicDao).aggiungiTopic(t);
        topicService.aggiungiTopic(t);
        verify(topicDao).aggiungiTopic(t);

        // Trova
        when(topicDao.trovaTutti()).thenReturn(List.of(t));
        List<Topic> trovati = topicService.trovaTutti();
        assertEquals(1, trovati.size());

        // Rimuovi
        doNothing().when(topicDao).rimuoviTopic(t);
        topicService.rimuoviTopic(t);
        verify(topicDao).rimuoviTopic(t);

        System.out.println("✓ Sequenza completa eseguita con successo");
    }

    @Test
    public void testBranchCoverageTuttiITryCatch() {
        System.out.println("\n=== Test 23: Branch coverage - tutti i try-catch ===");

        // Branch trovaId: try (successo)
        when(topicDao.trovaId(1L)).thenReturn(topic);
        assertNotNull(topicService.trovaId(1L));

        // Branch trovaId: catch (NoResultException)
        when(topicDao.trovaId(999L)).thenThrow(new NoResultException());
        assertNull(topicService.trovaId(999L));

        // Branch trovaNome: try (successo)
        when(topicDao.trovaNome("Esiste")).thenReturn(topic);
        assertNotNull(topicService.trovaNome("Esiste"));

        // Branch trovaNome: catch (NoResultException)
        when(topicDao.trovaNome("NonEsiste")).thenThrow(new NoResultException());
        assertNull(topicService.trovaNome("NonEsiste"));

        // Branch trovaCorsoLaurea: try (successo)
        when(topicDao.trovaCorsoLaurea("Informatica")).thenReturn(topic);
        assertNotNull(topicService.trovaCorsoLaurea("Informatica"));

        // Branch trovaCorsoLaurea: catch (NoResultException)
        when(topicDao.trovaCorsoLaurea("NonEsiste")).thenThrow(new NoResultException());
        assertNull(topicService.trovaCorsoLaurea("NonEsiste"));

        // Branch trovaCorso: try (successo)
        when(topicDao.trovaCorso("Programmazione")).thenReturn(topic);
        assertNotNull(topicService.trovaCorso("Programmazione"));

        // Branch trovaCorso: catch (NoResultException)
        when(topicDao.trovaCorso("NonEsiste")).thenThrow(new NoResultException());
        assertNull(topicService.trovaCorso("NonEsiste"));

        System.out.println("✓ Tutti i branch try-catch coperti al 100%");
    }

    @Test
    public void testTopicConCorsoLaurea() {
        System.out.println("\n=== Test 24: Topic con CorsoLaurea ===");

        Topic t = new Topic();
        t.setNome("Topic CDL");
        t.setCorsoLaurea(corsoLaurea);

        doNothing().when(topicDao).aggiungiTopic(t);
        topicService.aggiungiTopic(t);

        verify(topicDao).aggiungiTopic(t);
        assertNotNull(t.getCorsoLaurea());
        assertEquals("Informatica", t.getCorsoLaurea().getNome());

        System.out.println("✓ Topic con CorsoLaurea gestito");
    }

    @Test
    public void testTopicConCorso() {
        System.out.println("\n=== Test 25: Topic con Corso ===");

        Topic t = new Topic();
        t.setNome("Topic Corso");
        t.setCorso(corso);

        doNothing().when(topicDao).aggiungiTopic(t);
        topicService.aggiungiTopic(t);

        verify(topicDao).aggiungiTopic(t);
        assertNotNull(t.getCorso());
        assertEquals("Programmazione Distribuita", t.getCorso().getNome());

        System.out.println("✓ Topic con Corso gestito");
    }
}
