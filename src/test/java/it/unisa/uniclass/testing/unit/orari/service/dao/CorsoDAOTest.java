package it.unisa.uniclass.testing.unit.orari.service.dao;

import it.unisa.uniclass.orari.model.AnnoDidattico;
import it.unisa.uniclass.orari.model.Corso;
import it.unisa.uniclass.orari.model.CorsoLaurea;
import it.unisa.uniclass.orari.service.dao.CorsoDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test d'unità per la classe CorsoDAO.
 * Verifica i metodi di recupero, aggiunta e rimozione di corsi dal database.
 */
@DisplayName("Test per la classe CorsoDAO")
public class CorsoDAOTest {

    @Mock
    private EntityManager emUniClass;

    @Mock
    private TypedQuery<Corso> typedQueryCorso;

    private CorsoDAO corsoDAO;
    private Corso corso;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        corsoDAO = new CorsoDAO();
        corsoDAO.emUniClass = emUniClass;

        corso = new Corso("Programmazione I");
        corso.setCorsoLaurea(new CorsoLaurea("Ingegneria Informatica"));
        corso.setAnnoDidattico(new AnnoDidattico("2023-2024"));
    }

    @Nested
    @DisplayName("Test del metodo trovaCorso")
    class TrovaCorsoTest {

        @Test
        @DisplayName("trovaCorso restituisce corso per ID valido")
        void testTrovaCorsoSuccesso() {
            long id = 1;

            when(emUniClass.createNamedQuery(Corso.TROVA_CORSO, Corso.class))
                    .thenReturn(typedQueryCorso);
            when(typedQueryCorso.setParameter("id", id))
                    .thenReturn(typedQueryCorso);
            when(typedQueryCorso.getSingleResult())
                    .thenReturn(corso);

            Corso result = corsoDAO.trovaCorso(id);

            assertNotNull(result);
            assertEquals("Programmazione I", result.getNome());
            verify(emUniClass, times(1)).createNamedQuery(Corso.TROVA_CORSO, Corso.class);
            verify(typedQueryCorso, times(1)).setParameter("id", id);
            verify(typedQueryCorso, times(1)).getSingleResult();
        }

        @Test
        @DisplayName("trovaCorso con ID diversi")
        void testTrovaCorsoIdDiversi() {
            for (long id = 1; id <= 3; id++) {
                Corso corsoTest = new Corso("Corso " + id);

                when(emUniClass.createNamedQuery(Corso.TROVA_CORSO, Corso.class))
                        .thenReturn(typedQueryCorso);
                when(typedQueryCorso.setParameter("id", id))
                        .thenReturn(typedQueryCorso);
                when(typedQueryCorso.getSingleResult())
                        .thenReturn(corsoTest);

                Corso result = corsoDAO.trovaCorso(id);

                assertNotNull(result);
                assertEquals("Corso " + id, result.getNome());
            }
        }
    }

    @Nested
    @DisplayName("Test del metodo trovaCorsiCorsoLaurea")
    class TrovaCorsiCorsoLaureaTest {

        @Test
        @DisplayName("trovaCorsiCorsoLaurea restituisce corsi per corso di laurea valido")
        void testTrovaCorsiCorsoLaureaSuccesso() {
            String nomeCorsoLaurea = "Ingegneria Informatica";
            List<Corso> corsi = new ArrayList<>();
            corsi.add(new Corso("Programmazione I"));
            corsi.add(new Corso("Algoritmi"));
            corsi.add(new Corso("Basi di Dati"));

            when(emUniClass.createNamedQuery(Corso.TROVA_CORSI_CORSOLAUREA, Corso.class))
                    .thenReturn(typedQueryCorso);
            when(typedQueryCorso.setParameter("nomeCorsoLaurea", nomeCorsoLaurea))
                    .thenReturn(typedQueryCorso);
            when(typedQueryCorso.getResultList())
                    .thenReturn(corsi);

            List<Corso> result = corsoDAO.trovaCorsiCorsoLaurea(nomeCorsoLaurea);

            assertNotNull(result);
            assertEquals(3, result.size());
            verify(emUniClass, times(1)).createNamedQuery(Corso.TROVA_CORSI_CORSOLAUREA, Corso.class);
            verify(typedQueryCorso, times(1)).setParameter("nomeCorsoLaurea", nomeCorsoLaurea);
            verify(typedQueryCorso, times(1)).getResultList();
        }

        @Test
        @DisplayName("trovaCorsiCorsoLaurea restituisce lista vuota")
        void testTrovaCorsiCorsoLaureaVuoto() {
            String nomeCorsoLaurea = "Corso Inesistente";
            List<Corso> corsi = new ArrayList<>();

            when(emUniClass.createNamedQuery(Corso.TROVA_CORSI_CORSOLAUREA, Corso.class))
                    .thenReturn(typedQueryCorso);
            when(typedQueryCorso.setParameter("nomeCorsoLaurea", nomeCorsoLaurea))
                    .thenReturn(typedQueryCorso);
            when(typedQueryCorso.getResultList())
                    .thenReturn(corsi);

            List<Corso> result = corsoDAO.trovaCorsiCorsoLaurea(nomeCorsoLaurea);

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("trovaCorsiCorsoLaurea con corsi di laurea diversi")
        void testTrovaCorsiCorsoLaureaDiversi() {
            String[] corsiLaurea = {"Ingegneria Informatica", "Ingegneria Gestionale", "Ingegneria Civile"};

            for (String nome : corsiLaurea) {
                List<Corso> corsi = new ArrayList<>();
                corsi.add(new Corso("Corso 1 di " + nome));
                corsi.add(new Corso("Corso 2 di " + nome));

                when(emUniClass.createNamedQuery(Corso.TROVA_CORSI_CORSOLAUREA, Corso.class))
                        .thenReturn(typedQueryCorso);
                when(typedQueryCorso.setParameter("nomeCorsoLaurea", nome))
                        .thenReturn(typedQueryCorso);
                when(typedQueryCorso.getResultList())
                        .thenReturn(corsi);

                List<Corso> result = corsoDAO.trovaCorsiCorsoLaurea(nome);

                assertEquals(2, result.size());
            }
        }
    }

    @Nested
    @DisplayName("Test del metodo trovaTutti")
    class TrovaTuttiTest {

        @Test
        @DisplayName("trovaTutti restituisce lista di tutti i corsi")
        void testTrovaTutti() {
            List<Corso> corsi = new ArrayList<>();
            corsi.add(new Corso("Programmazione I"));
            corsi.add(new Corso("Algoritmi"));
            corsi.add(new Corso("Basi di Dati"));

            when(emUniClass.createNamedQuery(Corso.TROVA_TUTTE, Corso.class))
                    .thenReturn(typedQueryCorso);
            when(typedQueryCorso.getResultList())
                    .thenReturn(corsi);

            List<Corso> result = corsoDAO.trovaTutti();

            assertNotNull(result);
            assertEquals(3, result.size());
            verify(emUniClass, times(1)).createNamedQuery(Corso.TROVA_TUTTE, Corso.class);
            verify(typedQueryCorso, times(1)).getResultList();
        }

        @Test
        @DisplayName("trovaTutti restituisce lista vuota")
        void testTrovaTuttiVuoto() {
            List<Corso> corsi = new ArrayList<>();

            when(emUniClass.createNamedQuery(Corso.TROVA_TUTTE, Corso.class))
                    .thenReturn(typedQueryCorso);
            when(typedQueryCorso.getResultList())
                    .thenReturn(corsi);

            List<Corso> result = corsoDAO.trovaTutti();

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("trovaTutti con molti corsi")
        void testTrovaTuttiMolti() {
            List<Corso> corsi = new ArrayList<>();
            for (int i = 1; i <= 30; i++) {
                corsi.add(new Corso("Corso " + i));
            }

            when(emUniClass.createNamedQuery(Corso.TROVA_TUTTE, Corso.class))
                    .thenReturn(typedQueryCorso);
            when(typedQueryCorso.getResultList())
                    .thenReturn(corsi);

            List<Corso> result = corsoDAO.trovaTutti();

            assertEquals(30, result.size());
        }
    }

    @Nested
    @DisplayName("Test del metodo aggiungiCorso")
    class AggiungiCorsoTest {

        @Test
        @DisplayName("aggiungiCorso aggiunge correttamente un corso")
        void testAggiungiCorso() {
            when(emUniClass.merge(corso))
                    .thenReturn(corso);

            corsoDAO.aggiungiCorso(corso);

            verify(emUniClass, times(1)).merge(corso);
        }

        @Test
        @DisplayName("aggiungiCorso aggiunge multipli corsi")
        void testAggiungiCorsoMultipli() {
            for (int i = 1; i <= 5; i++) {
                Corso corsoTest = new Corso("Corso " + i);

                when(emUniClass.merge(corsoTest))
                        .thenReturn(corsoTest);

                corsoDAO.aggiungiCorso(corsoTest);

                verify(emUniClass, times(1)).merge(corsoTest);
            }
        }

        @Test
        @DisplayName("aggiungiCorso aggiorna un corso esistente")
        void testAggiungiCorsoAggiorna() {
            Corso corsoModificato = new Corso("Programmazione I - Modificato");

            when(emUniClass.merge(corsoModificato))
                    .thenReturn(corsoModificato);

            corsoDAO.aggiungiCorso(corsoModificato);

            verify(emUniClass, times(1)).merge(corsoModificato);
        }
    }

    @Nested
    @DisplayName("Test del metodo rimuoviCorso")
    class RimuoviCorsoTest {

        @Test
        @DisplayName("rimuoviCorso rimuove correttamente un corso")
        void testRimuoviCorso() {
            corsoDAO.rimuoviCorso(corso);

            verify(emUniClass, times(1)).remove(corso);
        }

        @Test
        @DisplayName("rimuoviCorso rimuove multipli corsi")
        void testRimuoviCorsoMultipli() {
            for (int i = 1; i <= 5; i++) {
                Corso corsoTest = new Corso("Corso " + i);

                corsoDAO.rimuoviCorso(corsoTest);

                verify(emUniClass, times(1)).remove(corsoTest);
            }
        }
    }

    @Nested
    @DisplayName("Test di integrazione")
    class ScenariComplessiTest {

        @Test
        @DisplayName("Sequenza di operazioni CRUD")
        void testSequenzaCRUD() {
            // Create
            when(emUniClass.merge(corso))
                    .thenReturn(corso);
            corsoDAO.aggiungiCorso(corso);
            verify(emUniClass, times(1)).merge(corso);

            // Read
            when(emUniClass.createNamedQuery(Corso.TROVA_CORSO, Corso.class))
                    .thenReturn(typedQueryCorso);
            when(typedQueryCorso.setParameter("id", 1L))
                    .thenReturn(typedQueryCorso);
            when(typedQueryCorso.getSingleResult())
                    .thenReturn(corso);

            Corso result = corsoDAO.trovaCorso(1L);
            assertNotNull(result);

            // Update
            Corso corsoModificato = new Corso("Programmazione I - Modificato");
            when(emUniClass.merge(corsoModificato))
                    .thenReturn(corsoModificato);
            corsoDAO.aggiungiCorso(corsoModificato);

            // Delete
            corsoDAO.rimuoviCorso(corso);
            verify(emUniClass, atLeastOnce()).remove(corso);
        }

        @Test
        @DisplayName("Ricerca per corso di laurea e poi modifica")
        void testRicercaCorsoLaureaEModifica() {
            String nomeCorsoLaurea = "Ingegneria Informatica";
            List<Corso> corsi = new ArrayList<>();
            Corso corso1 = new Corso("Programmazione I");
            Corso corso2 = new Corso("Algoritmi");
            corsi.add(corso1);
            corsi.add(corso2);

            when(emUniClass.createNamedQuery(Corso.TROVA_CORSI_CORSOLAUREA, Corso.class))
                    .thenReturn(typedQueryCorso);
            when(typedQueryCorso.setParameter("nomeCorsoLaurea", nomeCorsoLaurea))
                    .thenReturn(typedQueryCorso);
            when(typedQueryCorso.getResultList())
                    .thenReturn(corsi);

            List<Corso> result = corsoDAO.trovaCorsiCorsoLaurea(nomeCorsoLaurea);
            assertEquals(2, result.size());

            // Modifica il primo corso
            result.get(0).setNome("Programmazione I - Avanzato");
            when(emUniClass.merge(result.get(0)))
                    .thenReturn(result.get(0));
            corsoDAO.aggiungiCorso(result.get(0));

            verify(emUniClass, times(1)).merge(result.get(0));
        }
    }
}

