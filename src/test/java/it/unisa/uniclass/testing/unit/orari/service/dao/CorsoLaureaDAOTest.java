package it.unisa.uniclass.testing.unit.orari.service.dao;

import it.unisa.uniclass.orari.model.AnnoDidattico;
import it.unisa.uniclass.orari.model.CorsoLaurea;
import it.unisa.uniclass.orari.service.dao.CorsoLaureaDAO;
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
 * Test d'unità per la classe CorsoLaureaDAO.
 * Verifica i metodi di recupero, aggiunta e rimozione di corsi di laurea dal database.
 */
@DisplayName("Test per la classe CorsoLaureaDAO")
public class CorsoLaureaDAOTest {

    @Mock
    private EntityManager emUniClass;

    @Mock
    private TypedQuery<CorsoLaurea> typedQueryCorsoLaurea;

    private CorsoLaureaDAO corsoLaureaDAO;
    private CorsoLaurea corsoLaurea;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        corsoLaureaDAO = new CorsoLaureaDAO();
        corsoLaureaDAO.emUniClass = emUniClass;

        corsoLaurea = new CorsoLaurea("Ingegneria Informatica");
    }

    @Nested
    @DisplayName("Test del metodo trovaCorsoLaurea(long id)")
    class TrovaCorsoLaureaByIdTest {

        @Test
        @DisplayName("trovaCorsoLaurea restituisce corso di laurea per ID valido")
        void testTrovaCorsoLaureaById() {
            long id = 1;

            when(emUniClass.createNamedQuery(CorsoLaurea.TROVA_CORSOLAUREA, CorsoLaurea.class))
                    .thenReturn(typedQueryCorsoLaurea);
            when(typedQueryCorsoLaurea.setParameter("id", id))
                    .thenReturn(typedQueryCorsoLaurea);
            when(typedQueryCorsoLaurea.getSingleResult())
                    .thenReturn(corsoLaurea);

            CorsoLaurea result = corsoLaureaDAO.trovaCorsoLaurea(id);

            assertNotNull(result);
            assertEquals("Ingegneria Informatica", result.getNome());
            verify(emUniClass, times(1)).createNamedQuery(CorsoLaurea.TROVA_CORSOLAUREA, CorsoLaurea.class);
            verify(typedQueryCorsoLaurea, times(1)).setParameter("id", id);
            verify(typedQueryCorsoLaurea, times(1)).getSingleResult();
        }

        @Test
        @DisplayName("trovaCorsoLaurea con ID diversi")
        void testTrovaCorsoLaureaByIdDifferenti() {
            for (long id = 1; id <= 3; id++) {
                CorsoLaurea clTest = new CorsoLaurea("Corso " + id);

                when(emUniClass.createNamedQuery(CorsoLaurea.TROVA_CORSOLAUREA, CorsoLaurea.class))
                        .thenReturn(typedQueryCorsoLaurea);
                when(typedQueryCorsoLaurea.setParameter("id", id))
                        .thenReturn(typedQueryCorsoLaurea);
                when(typedQueryCorsoLaurea.getSingleResult())
                        .thenReturn(clTest);

                CorsoLaurea result = corsoLaureaDAO.trovaCorsoLaurea(id);

                assertNotNull(result);
                assertEquals("Corso " + id, result.getNome());
            }
        }
    }

    @Nested
    @DisplayName("Test del metodo trovaCorsoLaurea(String nome)")
    class TrovaCorsoLaureaByNomeTest {

        @Test
        @DisplayName("trovaCorsoLaurea restituisce corso di laurea per nome valido")
        void testTrovaCorsoLaureaByNome() {
            String nome = "Ingegneria Informatica";

            when(emUniClass.createNamedQuery(CorsoLaurea.TROVA_CORSOLAUREA_NOME, CorsoLaurea.class))
                    .thenReturn(typedQueryCorsoLaurea);
            when(typedQueryCorsoLaurea.setParameter("nome", nome))
                    .thenReturn(typedQueryCorsoLaurea);
            when(typedQueryCorsoLaurea.getSingleResult())
                    .thenReturn(corsoLaurea);

            CorsoLaurea result = corsoLaureaDAO.trovaCorsoLaurea(nome);

            assertNotNull(result);
            assertEquals(nome, result.getNome());
            verify(emUniClass, times(1)).createNamedQuery(CorsoLaurea.TROVA_CORSOLAUREA_NOME, CorsoLaurea.class);
            verify(typedQueryCorsoLaurea, times(1)).setParameter("nome", nome);
            verify(typedQueryCorsoLaurea, times(1)).getSingleResult();
        }

        @Test
        @DisplayName("trovaCorsoLaurea con nomi differenti")
        void testTrovaCorsoLaureaByNomeDifferenti() {
            String[] nomi = {"Ingegneria Informatica", "Ingegneria Gestionale", "Ingegneria Civile"};

            for (String nome : nomi) {
                CorsoLaurea clTest = new CorsoLaurea(nome);

                when(emUniClass.createNamedQuery(CorsoLaurea.TROVA_CORSOLAUREA_NOME, CorsoLaurea.class))
                        .thenReturn(typedQueryCorsoLaurea);
                when(typedQueryCorsoLaurea.setParameter("nome", nome))
                        .thenReturn(typedQueryCorsoLaurea);
                when(typedQueryCorsoLaurea.getSingleResult())
                        .thenReturn(clTest);

                CorsoLaurea result = corsoLaureaDAO.trovaCorsoLaurea(nome);

                assertNotNull(result);
                assertEquals(nome, result.getNome());
            }
        }
    }

    @Nested
    @DisplayName("Test del metodo trovaTutti")
    class TrovaTuttiTest {

        @Test
        @DisplayName("trovaTutti restituisce lista di tutti i corsi di laurea")
        void testTrovaTutti() {
            List<CorsoLaurea> corsiLaurea = new ArrayList<>();
            corsiLaurea.add(new CorsoLaurea("Ingegneria Informatica"));
            corsiLaurea.add(new CorsoLaurea("Ingegneria Gestionale"));
            corsiLaurea.add(new CorsoLaurea("Ingegneria Civile"));

            when(emUniClass.createNamedQuery(CorsoLaurea.TROVA_TUTTI, CorsoLaurea.class))
                    .thenReturn(typedQueryCorsoLaurea);
            when(typedQueryCorsoLaurea.getResultList())
                    .thenReturn(corsiLaurea);

            List<CorsoLaurea> result = corsoLaureaDAO.trovaTutti();

            assertNotNull(result);
            assertEquals(3, result.size());
            verify(emUniClass, times(1)).createNamedQuery(CorsoLaurea.TROVA_TUTTI, CorsoLaurea.class);
            verify(typedQueryCorsoLaurea, times(1)).getResultList();
        }

        @Test
        @DisplayName("trovaTutti restituisce lista vuota")
        void testTrovaTuttiVuoto() {
            List<CorsoLaurea> corsiLaurea = new ArrayList<>();

            when(emUniClass.createNamedQuery(CorsoLaurea.TROVA_TUTTI, CorsoLaurea.class))
                    .thenReturn(typedQueryCorsoLaurea);
            when(typedQueryCorsoLaurea.getResultList())
                    .thenReturn(corsiLaurea);

            List<CorsoLaurea> result = corsoLaureaDAO.trovaTutti();

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("trovaTutti con molti corsi di laurea")
        void testTrovaTuttiMolti() {
            List<CorsoLaurea> corsiLaurea = new ArrayList<>();
            for (int i = 1; i <= 20; i++) {
                corsiLaurea.add(new CorsoLaurea("Corso di Laurea " + i));
            }

            when(emUniClass.createNamedQuery(CorsoLaurea.TROVA_TUTTI, CorsoLaurea.class))
                    .thenReturn(typedQueryCorsoLaurea);
            when(typedQueryCorsoLaurea.getResultList())
                    .thenReturn(corsiLaurea);

            List<CorsoLaurea> result = corsoLaureaDAO.trovaTutti();

            assertEquals(20, result.size());
        }
    }

    @Nested
    @DisplayName("Test del metodo aggiungiCorsoLaurea")
    class AggiungiCorsoLaureaTest {

        @Test
        @DisplayName("aggiungiCorsoLaurea aggiunge correttamente un corso di laurea")
        void testAggiungiCorsoLaurea() {
            when(emUniClass.merge(corsoLaurea))
                    .thenReturn(corsoLaurea);

            corsoLaureaDAO.aggiungiCorsoLaurea(corsoLaurea);

            verify(emUniClass, times(1)).merge(corsoLaurea);
        }

        @Test
        @DisplayName("aggiungiCorsoLaurea aggiunge multipli corsi di laurea")
        void testAggiungiCorsoLaureaMultipli() {
            for (int i = 1; i <= 5; i++) {
                CorsoLaurea clTest = new CorsoLaurea("Corso di Laurea " + i);

                when(emUniClass.merge(clTest))
                        .thenReturn(clTest);

                corsoLaureaDAO.aggiungiCorsoLaurea(clTest);

                verify(emUniClass, times(1)).merge(clTest);
            }
        }

        @Test
        @DisplayName("aggiungiCorsoLaurea aggiorna un corso di laurea esistente")
        void testAggiungiCorsoLaureaAggiorna() {
            CorsoLaurea clModificato = new CorsoLaurea("Ingegneria Informatica - Modificato");

            when(emUniClass.merge(clModificato))
                    .thenReturn(clModificato);

            corsoLaureaDAO.aggiungiCorsoLaurea(clModificato);

            verify(emUniClass, times(1)).merge(clModificato);
        }
    }

    @Nested
    @DisplayName("Test del metodo rimuoviCorsoLaurea")
    class RimuoviCorsoLaureaTest {

        @Test
        @DisplayName("rimuoviCorsoLaurea rimuove correttamente un corso di laurea")
        void testRimuoviCorsoLaurea() {
            corsoLaureaDAO.rimuoviCorsoLaurea(corsoLaurea);

            verify(emUniClass, times(1)).remove(corsoLaurea);
        }

        @Test
        @DisplayName("rimuoviCorsoLaurea rimuove multipli corsi di laurea")
        void testRimuoviCorsoLaureaMultipli() {
            for (int i = 1; i <= 5; i++) {
                CorsoLaurea clTest = new CorsoLaurea("Corso di Laurea " + i);

                corsoLaureaDAO.rimuoviCorsoLaurea(clTest);

                verify(emUniClass, times(1)).remove(clTest);
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
            when(emUniClass.merge(corsoLaurea))
                    .thenReturn(corsoLaurea);
            corsoLaureaDAO.aggiungiCorsoLaurea(corsoLaurea);
            verify(emUniClass, times(1)).merge(corsoLaurea);

            // Read by ID
            when(emUniClass.createNamedQuery(CorsoLaurea.TROVA_CORSOLAUREA, CorsoLaurea.class))
                    .thenReturn(typedQueryCorsoLaurea);
            when(typedQueryCorsoLaurea.setParameter("id", 1L))
                    .thenReturn(typedQueryCorsoLaurea);
            when(typedQueryCorsoLaurea.getSingleResult())
                    .thenReturn(corsoLaurea);

            CorsoLaurea result = corsoLaureaDAO.trovaCorsoLaurea(1L);
            assertNotNull(result);

            // Update
            CorsoLaurea clModificato = new CorsoLaurea("Ingegneria Informatica - Aggiornato");
            when(emUniClass.merge(clModificato))
                    .thenReturn(clModificato);
            corsoLaureaDAO.aggiungiCorsoLaurea(clModificato);

            // Delete
            corsoLaureaDAO.rimuoviCorsoLaurea(corsoLaurea);
            verify(emUniClass, atLeastOnce()).remove(corsoLaurea);
        }

        @Test
        @DisplayName("Ricerca per nome e poi modifica")
        void testRicercaNomeEModifica() {
            String nome = "Ingegneria Informatica";
            CorsoLaurea cl = new CorsoLaurea(nome);
            cl.setResti(new ArrayList<>());
            cl.setAnniDidattici(new ArrayList<>());

            when(emUniClass.createNamedQuery(CorsoLaurea.TROVA_CORSOLAUREA_NOME, CorsoLaurea.class))
                    .thenReturn(typedQueryCorsoLaurea);
            when(typedQueryCorsoLaurea.setParameter("nome", nome))
                    .thenReturn(typedQueryCorsoLaurea);
            when(typedQueryCorsoLaurea.getSingleResult())
                    .thenReturn(cl);

            CorsoLaurea result = corsoLaureaDAO.trovaCorsoLaurea(nome);
            assertEquals(nome, result.getNome());

            // Modifica il corso di laurea
            result.setNome("Ingegneria Informatica - Aggiornato");
            when(emUniClass.merge(result))
                    .thenReturn(result);
            corsoLaureaDAO.aggiungiCorsoLaurea(result);

            verify(emUniClass, times(1)).merge(result);
        }
    }
}

