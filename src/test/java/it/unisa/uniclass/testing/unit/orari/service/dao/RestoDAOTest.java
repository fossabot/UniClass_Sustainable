package it.unisa.uniclass.testing.unit.orari.service.dao;

import it.unisa.uniclass.orari.model.CorsoLaurea;
import it.unisa.uniclass.orari.model.Resto;
import it.unisa.uniclass.orari.service.dao.RestoDAO;
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
 * Test d'unità per la classe RestoDAO.
 * Verifica i metodi di recupero, aggiunta e rimozione di resti dal database.
 */
@DisplayName("Test per la classe RestoDAO")
public class RestoDAOTest {

    @Mock
    private EntityManager emUniClass;

    @Mock
    private TypedQuery<Resto> typedQueryResto;

    private RestoDAO restoDAO;
    private Resto resto;
    private CorsoLaurea corsoLaurea;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        restoDAO = new RestoDAO();
        restoDAO.emUniClass = emUniClass;

        // Evita circolarità: crea CorsoLaurea senza dipendenze
        corsoLaurea = new CorsoLaurea("Ingegneria Informatica");

        // Evita circolarità: crea Resto() senza parametri e usa setter
        resto = new Resto();
        resto.setNome("Resto 0");
        resto.setCorsoLaurea(corsoLaurea);
    }

    @Nested
    @DisplayName("Test del metodo trovaRestiCorsoLaurea(CorsoLaurea)")
    class TrovaRestiCorsoLaureaByObjectTest {

        @Test
        @DisplayName("trovaRestiCorsoLaurea restituisce resti per corso di laurea")
        void testTrovaRestiCorsoLaureaByObject() {
            List<Resto> resti = new ArrayList<>();
            resti.add(resto);

            when(emUniClass.createNamedQuery(Resto.TROVA_RESTI_CORSO, Resto.class))
                    .thenReturn(typedQueryResto);
            when(typedQueryResto.setParameter("nome", corsoLaurea.getNome()))
                    .thenReturn(typedQueryResto);
            when(typedQueryResto.getResultList())
                    .thenReturn(resti);

            List<Resto> result = restoDAO.trovaRestiCorsoLaurea(corsoLaurea);

            assertNotNull(result);
            assertEquals(1, result.size());
            verify(emUniClass, times(1)).createNamedQuery(Resto.TROVA_RESTI_CORSO, Resto.class);
        }

        @Test
        @DisplayName("trovaRestiCorsoLaurea restituisce lista vuota")
        void testTrovaRestiCorsoLaureaByObjectVuoto() {
            when(emUniClass.createNamedQuery(Resto.TROVA_RESTI_CORSO, Resto.class))
                    .thenReturn(typedQueryResto);
            when(typedQueryResto.setParameter("nome", corsoLaurea.getNome()))
                    .thenReturn(typedQueryResto);
            when(typedQueryResto.getResultList())
                    .thenReturn(new ArrayList<>());

            List<Resto> result = restoDAO.trovaRestiCorsoLaurea(corsoLaurea);

            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("Test del metodo trovaRestiCorsoLaurea(String)")
    class TrovaRestiCorsoLaureaByStringTest {

        @Test
        @DisplayName("trovaRestiCorsoLaurea restituisce resti per nome corso di laurea")
        void testTrovaRestiCorsoLaureaByString() {
            String nomeCorso = "Ingegneria Informatica";
            List<Resto> resti = new ArrayList<>();
            resti.add(resto);
            resti.add(resto);

            when(emUniClass.createNamedQuery(Resto.TROVA_RESTI_CORSO, Resto.class))
                    .thenReturn(typedQueryResto);
            when(typedQueryResto.setParameter("nome", nomeCorso))
                    .thenReturn(typedQueryResto);
            when(typedQueryResto.getResultList())
                    .thenReturn(resti);

            List<Resto> result = restoDAO.trovaRestiCorsoLaurea(nomeCorso);

            assertNotNull(result);
            assertEquals(2, result.size());
        }

        @Test
        @DisplayName("trovaRestiCorsoLaurea restituisce lista vuota per nome")
        void testTrovaRestiCorsoLaureaByStringVuoto() {
            when(emUniClass.createNamedQuery(Resto.TROVA_RESTI_CORSO, Resto.class))
                    .thenReturn(typedQueryResto);
            when(typedQueryResto.setParameter("nome", "Corso Inesistente"))
                    .thenReturn(typedQueryResto);
            when(typedQueryResto.getResultList())
                    .thenReturn(new ArrayList<>());

            List<Resto> result = restoDAO.trovaRestiCorsoLaurea("Corso Inesistente");

            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("Test del metodo trovaResto(String)")
    class TrovaRestoByStringTest {

        @Test
        @DisplayName("trovaResto restituisce resti per nome")
        void testTrovaRestoByString() {
            String nomeResto = "Resto 0";
            List<Resto> resti = new ArrayList<>();
            resti.add(resto);

            when(emUniClass.createNamedQuery(Resto.TROVA_RESTO_NOME, Resto.class))
                    .thenReturn(typedQueryResto);
            when(typedQueryResto.setParameter("nome", nomeResto))
                    .thenReturn(typedQueryResto);
            when(typedQueryResto.getResultList())
                    .thenReturn(resti);

            List<Resto> result = restoDAO.trovaResto(nomeResto);

            assertNotNull(result);
            assertEquals(1, result.size());
        }

        @Test
        @DisplayName("trovaResto restituisce lista vuota")
        void testTrovaRestoByStringVuoto() {
            when(emUniClass.createNamedQuery(Resto.TROVA_RESTO_NOME, Resto.class))
                    .thenReturn(typedQueryResto);
            when(typedQueryResto.setParameter("nome", "Resto Inesistente"))
                    .thenReturn(typedQueryResto);
            when(typedQueryResto.getResultList())
                    .thenReturn(new ArrayList<>());

            List<Resto> result = restoDAO.trovaResto("Resto Inesistente");

            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("Test del metodo trovaResto(long id)")
    class TrovaRestoByIdTest {

        @Test
        @DisplayName("trovaResto restituisce resto per ID valido")
        void testTrovaRestoById() {
            long id = 1;

            when(emUniClass.createNamedQuery(Resto.TROVA_RESTO, Resto.class))
                    .thenReturn(typedQueryResto);
            when(typedQueryResto.setParameter("id", id))
                    .thenReturn(typedQueryResto);
            when(typedQueryResto.getSingleResult())
                    .thenReturn(resto);

            Resto result = restoDAO.trovaResto(id);

            assertNotNull(result);
            assertEquals("Resto 0", result.getNome());
        }

        @Test
        @DisplayName("trovaResto con ID diversi")
        void testTrovaRestoByIdDiversi() {
            for (long id = 1; id <= 3; id++) {
                Resto restoTest = new Resto();
                restoTest.setNome("Resto " + id);

                when(emUniClass.createNamedQuery(Resto.TROVA_RESTO, Resto.class))
                        .thenReturn(typedQueryResto);
                when(typedQueryResto.setParameter("id", id))
                        .thenReturn(typedQueryResto);
                when(typedQueryResto.getSingleResult())
                        .thenReturn(restoTest);

                Resto result = restoDAO.trovaResto(id);

                assertNotNull(result);
                assertEquals("Resto " + id, result.getNome());
            }
        }
    }

    @Nested
    @DisplayName("Test del metodo trovaRestoNomeCorso(String, CorsoLaurea)")
    class TrovaRestoNomeCorsoByObjectTest {

        @Test
        @DisplayName("trovaRestoNomeCorso restituisce resto per nome e corso")
        void testTrovaRestoNomeCorsoByObject() {
            String nomeResto = "Resto 0";

            when(emUniClass.createNamedQuery(Resto.TROVA_RESTO_NOME_CORSO, Resto.class))
                    .thenReturn(typedQueryResto);
            when(typedQueryResto.setParameter("nome", nomeResto))
                    .thenReturn(typedQueryResto);
            when(typedQueryResto.setParameter("nomeCorso", corsoLaurea.getNome()))
                    .thenReturn(typedQueryResto);
            when(typedQueryResto.getSingleResult())
                    .thenReturn(resto);

            Resto result = restoDAO.trovaRestoNomeCorso(nomeResto, corsoLaurea);

            assertNotNull(result);
            assertEquals(nomeResto, result.getNome());
        }
    }

    @Nested
    @DisplayName("Test del metodo trovaRestoNomeCorso(String, String)")
    class TrovaRestoNomeCorsoByStringTest {

        @Test
        @DisplayName("trovaRestoNomeCorso restituisce resto per nome resto e nome corso")
        void testTrovaRestoNomeCorsoByString() {
            String nomeResto = "Resto 0";
            String nomeCorso = "Ingegneria Informatica";

            when(emUniClass.createNamedQuery(Resto.TROVA_RESTO_NOME_CORSO, Resto.class))
                    .thenReturn(typedQueryResto);
            when(typedQueryResto.setParameter("nome", nomeResto))
                    .thenReturn(typedQueryResto);
            when(typedQueryResto.setParameter("nomeCorso", nomeCorso))
                    .thenReturn(typedQueryResto);
            when(typedQueryResto.getSingleResult())
                    .thenReturn(resto);

            Resto result = restoDAO.trovaRestoNomeCorso(nomeResto, nomeCorso);

            assertNotNull(result);
            assertEquals(nomeResto, result.getNome());
        }

        @Test
        @DisplayName("trovaRestoNomeCorso con combinazioni diverse")
        void testTrovaRestoNomeCorsoByStringDiversi() {
            String[] nomiResti = {"Resto 0", "Resto 1", "Resto 2"};
            String[] nomiCorsi = {"Ingegneria Informatica", "Ingegneria Gestionale"};

            for (String nomeResto : nomiResti) {
                for (String nomeCorso : nomiCorsi) {
                    Resto restoTest = new Resto();
                    restoTest.setNome(nomeResto);

                    when(emUniClass.createNamedQuery(Resto.TROVA_RESTO_NOME_CORSO, Resto.class))
                            .thenReturn(typedQueryResto);
                    when(typedQueryResto.setParameter("nome", nomeResto))
                            .thenReturn(typedQueryResto);
                    when(typedQueryResto.setParameter("nomeCorso", nomeCorso))
                            .thenReturn(typedQueryResto);
                    when(typedQueryResto.getSingleResult())
                            .thenReturn(restoTest);

                    Resto result = restoDAO.trovaRestoNomeCorso(nomeResto, nomeCorso);

                    assertEquals(nomeResto, result.getNome());
                }
            }
        }
    }

    @Nested
    @DisplayName("Test del metodo aggiungiResto")
    class AggiungiRestoTest {

        @Test
        @DisplayName("aggiungiResto aggiunge correttamente un resto")
        void testAggiungiResto() {
            when(emUniClass.merge(resto))
                    .thenReturn(resto);

            restoDAO.aggiungiResto(resto);

            verify(emUniClass, times(1)).merge(resto);
        }

        @Test
        @DisplayName("aggiungiResto aggiunge multipli resti")
        void testAggiungiRestoMultipli() {
            for (int i = 1; i <= 5; i++) {
                Resto restoTest = new Resto();
                restoTest.setNome("Resto " + i);

                when(emUniClass.merge(restoTest))
                        .thenReturn(restoTest);

                restoDAO.aggiungiResto(restoTest);

                verify(emUniClass, times(1)).merge(restoTest);
            }
        }

        @Test
        @DisplayName("aggiungiResto aggiorna un resto esistente")
        void testAggiungiRestoAggiorna() {
            Resto restoModificato = new Resto();
            restoModificato.setNome("Resto Modificato");

            when(emUniClass.merge(restoModificato))
                    .thenReturn(restoModificato);

            restoDAO.aggiungiResto(restoModificato);

            verify(emUniClass, times(1)).merge(restoModificato);
        }
    }

    @Nested
    @DisplayName("Test del metodo rimuoviResto")
    class RimuoviRestoTest {

        @Test
        @DisplayName("rimuoviResto rimuove correttamente un resto")
        void testRimuoviResto() {
            restoDAO.rimuoviResto(resto);

            verify(emUniClass, times(1)).remove(resto);
        }

        @Test
        @DisplayName("rimuoviResto rimuove multipli resti")
        void testRimuoviRestoMultipli() {
            for (int i = 1; i <= 5; i++) {
                Resto restoTest = new Resto();
                restoTest.setNome("Resto " + i);

                restoDAO.rimuoviResto(restoTest);

                verify(emUniClass, times(1)).remove(restoTest);
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
            when(emUniClass.merge(resto))
                    .thenReturn(resto);
            restoDAO.aggiungiResto(resto);
            verify(emUniClass, times(1)).merge(resto);

            // Read
            when(emUniClass.createNamedQuery(Resto.TROVA_RESTO, Resto.class))
                    .thenReturn(typedQueryResto);
            when(typedQueryResto.setParameter("id", 1L))
                    .thenReturn(typedQueryResto);
            when(typedQueryResto.getSingleResult())
                    .thenReturn(resto);

            Resto result = restoDAO.trovaResto(1L);
            assertNotNull(result);

            // Update
            Resto restoModificato = new Resto();
            restoModificato.setNome("Resto Aggiornato");
            when(emUniClass.merge(restoModificato))
                    .thenReturn(restoModificato);
            restoDAO.aggiungiResto(restoModificato);

            // Delete
            restoDAO.rimuoviResto(resto);
            verify(emUniClass, atLeastOnce()).remove(resto);
        }

        @Test
        @DisplayName("Ricerca per corso di laurea e poi modifica")
        void testRicercaCorsoLaureaEModifica() {
            String nomeCorso = "Ingegneria Informatica";
            List<Resto> resti = new ArrayList<>();
            Resto resto1 = new Resto();
            resto1.setNome("Resto 0");
            resti.add(resto1);

            when(emUniClass.createNamedQuery(Resto.TROVA_RESTI_CORSO, Resto.class))
                    .thenReturn(typedQueryResto);
            when(typedQueryResto.setParameter("nome", nomeCorso))
                    .thenReturn(typedQueryResto);
            when(typedQueryResto.getResultList())
                    .thenReturn(resti);

            List<Resto> result = restoDAO.trovaRestiCorsoLaurea(nomeCorso);
            assertEquals(1, result.size());

            result.get(0).setNome("Resto Modificato");
            when(emUniClass.merge(result.get(0)))
                    .thenReturn(result.get(0));
            restoDAO.aggiungiResto(result.get(0));

            verify(emUniClass, times(1)).merge(result.get(0));
        }
    }
}

