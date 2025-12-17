package it.unisa.uniclass.testing.unit.orari.service.dao;

import it.unisa.uniclass.orari.model.Aula;
import it.unisa.uniclass.orari.service.dao.AulaDAO;
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
 * Test d'unità per la classe AulaDAO.
 * Verifica i metodi di recupero, aggiunta e rimozione di aule dal database.
 */
@DisplayName("Test per la classe AulaDAO")
public class AulaDAOTest {

    @Mock
    private EntityManager emUniClass;

    @Mock
    private TypedQuery<Aula> typedQueryAula;

    @Mock
    private TypedQuery<String> typedQueryString;

    private AulaDAO aulaDAO;
    private Aula aula;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        aulaDAO = new AulaDAO();
        aulaDAO.emUniClass = emUniClass;

        aula = new Aula(1, "Edificio A", "Aula 101");
    }

    @Nested
    @DisplayName("Test del metodo trovaAula(int id)")
    class TrovaAulaByIdTest {

        @Test
        @DisplayName("trovaAula restituisce aula per ID valido")
        void testTrovaAulaById() {
            int id = 1;

            when(emUniClass.createNamedQuery(Aula.TROVA_AULA, Aula.class))
                    .thenReturn(typedQueryAula);
            when(typedQueryAula.setParameter("id", id))
                    .thenReturn(typedQueryAula);
            when(typedQueryAula.getSingleResult())
                    .thenReturn(aula);

            Aula result = aulaDAO.trovaAula(id);

            assertNotNull(result);
            assertEquals("Aula 101", result.getNome());
            assertEquals("Edificio A", result.getEdificio());
            verify(emUniClass, times(1)).createNamedQuery(Aula.TROVA_AULA, Aula.class);
            verify(typedQueryAula, times(1)).setParameter("id", id);
            verify(typedQueryAula, times(1)).getSingleResult();
        }

        @Test
        @DisplayName("trovaAula con ID diversi")
        void testTrovaAulaByIdDifferenti() {
            for (int id = 1; id <= 3; id++) {
                Aula aulaTest = new Aula(id, "Edificio " + id, "Aula " + (100 + id));

                when(emUniClass.createNamedQuery(Aula.TROVA_AULA, Aula.class))
                        .thenReturn(typedQueryAula);
                when(typedQueryAula.setParameter("id", id))
                        .thenReturn(typedQueryAula);
                when(typedQueryAula.getSingleResult())
                        .thenReturn(aulaTest);

                Aula result = aulaDAO.trovaAula(id);

                assertNotNull(result);
                assertEquals(id, result.getId());
            }
        }
    }

    @Nested
    @DisplayName("Test del metodo trovaAula(String nome)")
    class TrovaAulaByNomeTest {

        @Test
        @DisplayName("trovaAula restituisce aula per nome valido")
        void testTrovaAulaByNome() {
            String nome = "Aula 101";

            when(emUniClass.createNamedQuery(Aula.TROVA_AULANOME, Aula.class))
                    .thenReturn(typedQueryAula);
            when(typedQueryAula.setParameter("nome", nome))
                    .thenReturn(typedQueryAula);
            when(typedQueryAula.getSingleResult())
                    .thenReturn(aula);

            Aula result = aulaDAO.trovaAula(nome);

            assertNotNull(result);
            assertEquals(nome, result.getNome());
            verify(emUniClass, times(1)).createNamedQuery(Aula.TROVA_AULANOME, Aula.class);
            verify(typedQueryAula, times(1)).setParameter("nome", nome);
            verify(typedQueryAula, times(1)).getSingleResult();
        }

        @Test
        @DisplayName("trovaAula con nomi differenti")
        void testTrovaAulaByNomeDifferenti() {
            String[] nomi = {"Aula 101", "Aula 102", "Aula 201"};

            for (String nome : nomi) {
                Aula aulaTest = new Aula(1, "Edificio A", nome);

                when(emUniClass.createNamedQuery(Aula.TROVA_AULANOME, Aula.class))
                        .thenReturn(typedQueryAula);
                when(typedQueryAula.setParameter("nome", nome))
                        .thenReturn(typedQueryAula);
                when(typedQueryAula.getSingleResult())
                        .thenReturn(aulaTest);

                Aula result = aulaDAO.trovaAula(nome);

                assertNotNull(result);
                assertEquals(nome, result.getNome());
            }
        }
    }

    @Nested
    @DisplayName("Test del metodo trovaTutte")
    class TrovaTutteTest {

        @Test
        @DisplayName("trovaTutte restituisce lista di tutte le aule")
        void testTrovaTutte() {
            List<Aula> aule = new ArrayList<>();
            aule.add(new Aula(1, "Edificio A", "Aula 101"));
            aule.add(new Aula(2, "Edificio B", "Aula 102"));
            aule.add(new Aula(3, "Edificio A", "Aula 103"));

            when(emUniClass.createNamedQuery(Aula.TROVA_TUTTE, Aula.class))
                    .thenReturn(typedQueryAula);
            when(typedQueryAula.getResultList())
                    .thenReturn(aule);

            List<Aula> result = aulaDAO.trovaTutte();

            assertNotNull(result);
            assertEquals(3, result.size());
            verify(emUniClass, times(1)).createNamedQuery(Aula.TROVA_TUTTE, Aula.class);
            verify(typedQueryAula, times(1)).getResultList();
        }

        @Test
        @DisplayName("trovaTutte restituisce lista vuota")
        void testTrovaTutteVuoto() {
            List<Aula> aule = new ArrayList<>();

            when(emUniClass.createNamedQuery(Aula.TROVA_TUTTE, Aula.class))
                    .thenReturn(typedQueryAula);
            when(typedQueryAula.getResultList())
                    .thenReturn(aule);

            List<Aula> result = aulaDAO.trovaTutte();

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("trovaTutte con molte aule")
        void testTrovaTutteMolte() {
            List<Aula> aule = new ArrayList<>();
            for (int i = 1; i <= 50; i++) {
                aule.add(new Aula(i, "Edificio", "Aula " + i));
            }

            when(emUniClass.createNamedQuery(Aula.TROVA_TUTTE, Aula.class))
                    .thenReturn(typedQueryAula);
            when(typedQueryAula.getResultList())
                    .thenReturn(aule);

            List<Aula> result = aulaDAO.trovaTutte();

            assertEquals(50, result.size());
        }
    }

    @Nested
    @DisplayName("Test del metodo trovaAuleEdificio")
    class TrovaAuleEdificioTest {

        @Test
        @DisplayName("trovaAuleEdificio restituisce aule per edificio valido")
        void testTrovaAuleEdificio() {
            String edificio = "Edificio A";
            List<Aula> aule = new ArrayList<>();
            aule.add(new Aula(1, edificio, "Aula 101"));
            aule.add(new Aula(2, edificio, "Aula 102"));

            when(emUniClass.createNamedQuery(Aula.TROVA_AULA_EDIFICIO, Aula.class))
                    .thenReturn(typedQueryAula);
            when(typedQueryAula.setParameter("edificio", edificio))
                    .thenReturn(typedQueryAula);
            when(typedQueryAula.getResultList())
                    .thenReturn(aule);

            List<Aula> result = aulaDAO.trovaAuleEdificio(edificio);

            assertNotNull(result);
            assertEquals(2, result.size());
            verify(emUniClass, times(1)).createNamedQuery(Aula.TROVA_AULA_EDIFICIO, Aula.class);
            verify(typedQueryAula, times(1)).setParameter("edificio", edificio);
            verify(typedQueryAula, times(1)).getResultList();
        }

        @Test
        @DisplayName("trovaAuleEdificio restituisce lista vuota")
        void testTrovaAuleEdificioVuoto() {
            String edificio = "Edificio Inesistente";
            List<Aula> aule = new ArrayList<>();

            when(emUniClass.createNamedQuery(Aula.TROVA_AULA_EDIFICIO, Aula.class))
                    .thenReturn(typedQueryAula);
            when(typedQueryAula.setParameter("edificio", edificio))
                    .thenReturn(typedQueryAula);
            when(typedQueryAula.getResultList())
                    .thenReturn(aule);

            List<Aula> result = aulaDAO.trovaAuleEdificio(edificio);

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("trovaAuleEdificio con edifici differenti")
        void testTrovaAuleEdificioDifferenti() {
            String[] edifici = {"Edificio A", "Edificio B", "Edificio C"};

            for (String edificio : edifici) {
                List<Aula> aule = new ArrayList<>();
                aule.add(new Aula(1, edificio, "Aula 101"));
                aule.add(new Aula(2, edificio, "Aula 102"));

                when(emUniClass.createNamedQuery(Aula.TROVA_AULA_EDIFICIO, Aula.class))
                        .thenReturn(typedQueryAula);
                when(typedQueryAula.setParameter("edificio", edificio))
                        .thenReturn(typedQueryAula);
                when(typedQueryAula.getResultList())
                        .thenReturn(aule);

                List<Aula> result = aulaDAO.trovaAuleEdificio(edificio);

                assertEquals(2, result.size());
            }
        }
    }

    @Nested
    @DisplayName("Test del metodo trovaEdifici")
    class TrovaEdificiTest {

        @Test
        @DisplayName("trovaEdifici restituisce lista di edifici")
        void testTrovaEdifici() {
            List<String> edifici = new ArrayList<>();
            edifici.add("Edificio A");
            edifici.add("Edificio B");
            edifici.add("Edificio C");

            when(emUniClass.createNamedQuery(Aula.TROVA_EDIFICI, String.class))
                    .thenReturn(typedQueryString);
            when(typedQueryString.getResultList())
                    .thenReturn(edifici);

            List<String> result = aulaDAO.trovaEdifici();

            assertNotNull(result);
            assertEquals(3, result.size());
            verify(emUniClass, times(1)).createNamedQuery(Aula.TROVA_EDIFICI, String.class);
            verify(typedQueryString, times(1)).getResultList();
        }

        @Test
        @DisplayName("trovaEdifici restituisce lista vuota")
        void testTrovaEdificiVuoto() {
            List<String> edifici = new ArrayList<>();

            when(emUniClass.createNamedQuery(Aula.TROVA_EDIFICI, String.class))
                    .thenReturn(typedQueryString);
            when(typedQueryString.getResultList())
                    .thenReturn(edifici);

            List<String> result = aulaDAO.trovaEdifici();

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("Test del metodo aggiungiAula")
    class AggiungiAulaTest {

        @Test
        @DisplayName("aggiungiAula aggiunge correttamente un'aula")
        void testAggiungiAula() {
            when(emUniClass.merge(aula))
                    .thenReturn(aula);

            aulaDAO.aggiungiAula(aula);

            verify(emUniClass, times(1)).merge(aula);
        }

        @Test
        @DisplayName("aggiungiAula aggiunge multiple aule")
        void testAggiungiAulaMultiple() {
            for (int i = 1; i <= 5; i++) {
                Aula aulaTest = new Aula(i, "Edificio", "Aula " + i);

                when(emUniClass.merge(aulaTest))
                        .thenReturn(aulaTest);

                aulaDAO.aggiungiAula(aulaTest);

                verify(emUniClass, times(1)).merge(aulaTest);
            }
        }

        @Test
        @DisplayName("aggiungiAula aggiorna un'aula esistente")
        void testAggiungiAulaAggiorna() {
            Aula aulaAggiornata = new Aula(1, "Edificio B", "Aula 101 Modificata");

            when(emUniClass.merge(aulaAggiornata))
                    .thenReturn(aulaAggiornata);

            aulaDAO.aggiungiAula(aulaAggiornata);

            verify(emUniClass, times(1)).merge(aulaAggiornata);
        }
    }

    @Nested
    @DisplayName("Test del metodo rimuoviAula")
    class RimuoviAulaTest {

        @Test
        @DisplayName("rimuoviAula rimuove correttamente un'aula")
        void testRimuoviAula() {
            aulaDAO.rimuoviAula(aula);

            verify(emUniClass, times(1)).remove(aula);
        }

        @Test
        @DisplayName("rimuoviAula rimuove multiple aule")
        void testRimuoviAulaMultiple() {
            for (int i = 1; i <= 5; i++) {
                Aula aulaTest = new Aula(i, "Edificio", "Aula " + i);

                aulaDAO.rimuoviAula(aulaTest);

                verify(emUniClass, times(1)).remove(aulaTest);
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
            when(emUniClass.merge(aula))
                    .thenReturn(aula);
            aulaDAO.aggiungiAula(aula);
            verify(emUniClass, times(1)).merge(aula);

            // Read
            when(emUniClass.createNamedQuery(Aula.TROVA_AULA, Aula.class))
                    .thenReturn(typedQueryAula);
            when(typedQueryAula.setParameter("id", 1))
                    .thenReturn(typedQueryAula);
            when(typedQueryAula.getSingleResult())
                    .thenReturn(aula);

            Aula result = aulaDAO.trovaAula(1);
            assertNotNull(result);

            // Update
            Aula aulaModificata = new Aula(1, "Edificio B", "Aula 101 Nuova");
            when(emUniClass.merge(aulaModificata))
                    .thenReturn(aulaModificata);
            aulaDAO.aggiungiAula(aulaModificata);

            // Delete
            aulaDAO.rimuoviAula(aula);
            verify(emUniClass, atLeastOnce()).remove(aula);
        }

        @Test
        @DisplayName("Ricerca con risultati multipli e poi modifica")
        void testRicercaMultiplaEModifica() {
            String edificio = "Edificio A";
            List<Aula> aule = new ArrayList<>();
            Aula aula1 = new Aula(1, edificio, "Aula 101");
            Aula aula2 = new Aula(2, edificio, "Aula 102");
            aule.add(aula1);
            aule.add(aula2);

            when(emUniClass.createNamedQuery(Aula.TROVA_AULA_EDIFICIO, Aula.class))
                    .thenReturn(typedQueryAula);
            when(typedQueryAula.setParameter("edificio", edificio))
                    .thenReturn(typedQueryAula);
            when(typedQueryAula.getResultList())
                    .thenReturn(aule);

            List<Aula> result = aulaDAO.trovaAuleEdificio(edificio);
            assertEquals(2, result.size());

            // Modifica il primo elemento
            result.get(0).setNome("Aula 101 Modificata");
            when(emUniClass.merge(result.get(0)))
                    .thenReturn(result.get(0));
            aulaDAO.aggiungiAula(result.get(0));

            verify(emUniClass, times(1)).merge(result.get(0));
        }
    }
}

