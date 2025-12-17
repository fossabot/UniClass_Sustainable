package it.unisa.uniclass.testing.unit.orari.service.dao;

import it.unisa.uniclass.orari.model.AnnoDidattico;
import it.unisa.uniclass.orari.model.CorsoLaurea;
import it.unisa.uniclass.orari.service.dao.AnnoDidatticoDAO;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Test d'unità completi per la classe AnnoDidatticoDAO.
 * Verifica i metodi di recupero, aggiunta e rimozione di anni didattici dal database.
 */
@DisplayName("Test per la classe AnnoDidatticoDAO")
public class AnnoDidatticoDAOTest {

    @Mock
    private EntityManager emUniClass;

    @Mock
    private TypedQuery<AnnoDidattico> typedQuery;

    private AnnoDidatticoDAO annoDidatticoDAO;

    private AnnoDidattico annoDidattico;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        annoDidatticoDAO = new AnnoDidatticoDAO();
        annoDidatticoDAO.emUniClass = emUniClass;

        annoDidattico = new AnnoDidattico("2023-2024");
        annoDidattico.setCorsiLaurea(new ArrayList<>());
        annoDidattico.setCorsi(new ArrayList<>());
    }

    @Nested
    @DisplayName("Test del metodo trovaAnno")
    class TrovaAnnoTest {

        @Test
        @DisplayName("trovaAnno restituisce lista di anni didattici corrispondenti")
        void testTrovaAnnoSuccesso() {
            String anno = "2023-2024";
            List<AnnoDidattico> anni = new ArrayList<>();
            anni.add(annoDidattico);

            when(emUniClass.createNamedQuery(AnnoDidattico.TROVA_ANNO, AnnoDidattico.class))
                    .thenReturn(typedQuery);
            when(typedQuery.setParameter("anno", anno))
                    .thenReturn(typedQuery);
            when(typedQuery.getResultList())
                    .thenReturn(anni);

            List<AnnoDidattico> result = annoDidatticoDAO.trovaAnno(anno);

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(anno, result.get(0).getAnno());
            verify(emUniClass, times(1)).createNamedQuery(AnnoDidattico.TROVA_ANNO, AnnoDidattico.class);
            verify(typedQuery, times(1)).setParameter("anno", anno);
            verify(typedQuery, times(1)).getResultList();
        }

        @Test
        @DisplayName("trovaAnno restituisce lista vuota quando nessun anno corrisponde")
        void testTrovaAnnoVuoto() {
            String anno = "2099-2100";
            List<AnnoDidattico> anni = new ArrayList<>();

            when(emUniClass.createNamedQuery(AnnoDidattico.TROVA_ANNO, AnnoDidattico.class))
                    .thenReturn(typedQuery);
            when(typedQuery.setParameter("anno", anno))
                    .thenReturn(typedQuery);
            when(typedQuery.getResultList())
                    .thenReturn(anni);

            List<AnnoDidattico> result = annoDidatticoDAO.trovaAnno(anno);

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("trovaAnno con multipli anni didattici")
        void testTrovaAnnoMultipli() {
            String anno = "2023-2024";
            List<AnnoDidattico> anni = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                AnnoDidattico ad = new AnnoDidattico(anno);
                anni.add(ad);
            }

            when(emUniClass.createNamedQuery(AnnoDidattico.TROVA_ANNO, AnnoDidattico.class))
                    .thenReturn(typedQuery);
            when(typedQuery.setParameter("anno", anno))
                    .thenReturn(typedQuery);
            when(typedQuery.getResultList())
                    .thenReturn(anni);

            List<AnnoDidattico> result = annoDidatticoDAO.trovaAnno(anno);

            assertNotNull(result);
            assertEquals(3, result.size());
            verify(typedQuery, times(1)).getResultList();
        }
    }

    @Nested
    @DisplayName("Test del metodo trovaId")
    class TrovaIdTest {

        @Test
        @DisplayName("trovaId restituisce anno didattico per ID valido")
        void testTrovaIdSuccesso() {
            int id = 1;

            when(emUniClass.createNamedQuery(AnnoDidattico.TROVA_ID, AnnoDidattico.class))
                    .thenReturn(typedQuery);
            when(typedQuery.setParameter("id", id))
                    .thenReturn(typedQuery);
            when(typedQuery.getSingleResult())
                    .thenReturn(annoDidattico);

            AnnoDidattico result = annoDidatticoDAO.trovaId(id);

            assertNotNull(result);
            assertEquals(annoDidattico.getAnno(), result.getAnno());
            verify(emUniClass, times(1)).createNamedQuery(AnnoDidattico.TROVA_ID, AnnoDidattico.class);
            verify(typedQuery, times(1)).setParameter("id", id);
            verify(typedQuery, times(1)).getSingleResult();
        }

        @Test
        @DisplayName("trovaId con ID diversi")
        void testTrovaIdDiversi() {
            for (int id = 1; id <= 5; id++) {
                AnnoDidattico ad = new AnnoDidattico("2023-2024");

                when(emUniClass.createNamedQuery(AnnoDidattico.TROVA_ID, AnnoDidattico.class))
                        .thenReturn(typedQuery);
                when(typedQuery.setParameter("id", id))
                        .thenReturn(typedQuery);
                when(typedQuery.getSingleResult())
                        .thenReturn(ad);

                AnnoDidattico result = annoDidatticoDAO.trovaId(id);

                assertNotNull(result);
                assertEquals("2023-2024", result.getAnno());
            }
        }
    }

    @Nested
    @DisplayName("Test del metodo trovaTutti")
    class TrovaTuttiTest {

        @Test
        @DisplayName("trovaTutti restituisce lista di tutti gli anni didattici")
        void testTrovaTuttiSuccesso() {
            List<AnnoDidattico> anni = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                AnnoDidattico ad = new AnnoDidattico("Anno " + i);
                anni.add(ad);
            }

            when(emUniClass.createNamedQuery(AnnoDidattico.TROVA_TUTTI, AnnoDidattico.class))
                    .thenReturn(typedQuery);
            when(typedQuery.getResultList())
                    .thenReturn(anni);

            List<AnnoDidattico> result = annoDidatticoDAO.trovaTutti();

            assertNotNull(result);
            assertEquals(5, result.size());
            verify(emUniClass, times(1)).createNamedQuery(AnnoDidattico.TROVA_TUTTI, AnnoDidattico.class);
            verify(typedQuery, times(1)).getResultList();
        }

        @Test
        @DisplayName("trovaTutti restituisce lista vuota quando non ci sono anni")
        void testTrovaTuttiVuoto() {
            List<AnnoDidattico> anni = new ArrayList<>();

            when(emUniClass.createNamedQuery(AnnoDidattico.TROVA_TUTTI, AnnoDidattico.class))
                    .thenReturn(typedQuery);
            when(typedQuery.getResultList())
                    .thenReturn(anni);

            List<AnnoDidattico> result = annoDidatticoDAO.trovaTutti();

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("trovaTutti restituisce lista con molti elementi")
        void testTrovaTuttiGrande() {
            List<AnnoDidattico> anni = new ArrayList<>();
            for (int i = 0; i < 100; i++) {
                AnnoDidattico ad = new AnnoDidattico("Anno " + i);
                anni.add(ad);
            }

            when(emUniClass.createNamedQuery(AnnoDidattico.TROVA_TUTTI, AnnoDidattico.class))
                    .thenReturn(typedQuery);
            when(typedQuery.getResultList())
                    .thenReturn(anni);

            List<AnnoDidattico> result = annoDidatticoDAO.trovaTutti();

            assertEquals(100, result.size());
        }
    }

    @Nested
    @DisplayName("Test del metodo trovaTuttiCorsoLaurea")
    class TrovaTuttiCorsoLaureaTest {

        @Test
        @DisplayName("trovaTuttiCorsoLaurea restituisce anni per corso di laurea valido")
        void testTrovaTuttiCorsoLaureaSuccesso() {
            long idCorso = 1;
            List<AnnoDidattico> anni = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                AnnoDidattico ad = new AnnoDidattico("Anno " + i);
                anni.add(ad);
            }

            when(emUniClass.createNamedQuery(AnnoDidattico.TROVA_ANNI_CORSOLAUREA, AnnoDidattico.class))
                    .thenReturn(typedQuery);
            when(typedQuery.setParameter("corsoId", idCorso))
                    .thenReturn(typedQuery);
            when(typedQuery.getResultList())
                    .thenReturn(anni);

            List<AnnoDidattico> result = annoDidatticoDAO.trovaTuttiCorsoLaurea(idCorso);

            assertNotNull(result);
            assertEquals(3, result.size());
            verify(emUniClass, times(1)).createNamedQuery(AnnoDidattico.TROVA_ANNI_CORSOLAUREA, AnnoDidattico.class);
            verify(typedQuery, times(1)).setParameter("corsoId", idCorso);
        }

        @Test
        @DisplayName("trovaTuttiCorsoLaurea restituisce lista vuota per corso senza anni")
        void testTrovaTuttiCorsoLaureaVuoto() {
            long idCorso = 999;
            List<AnnoDidattico> anni = new ArrayList<>();

            when(emUniClass.createNamedQuery(AnnoDidattico.TROVA_ANNI_CORSOLAUREA, AnnoDidattico.class))
                    .thenReturn(typedQuery);
            when(typedQuery.setParameter("corsoId", idCorso))
                    .thenReturn(typedQuery);
            when(typedQuery.getResultList())
                    .thenReturn(anni);

            List<AnnoDidattico> result = annoDidatticoDAO.trovaTuttiCorsoLaurea(idCorso);

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("trovaTuttiCorsoLaurea con ID differenti")
        void testTrovaTuttiCorsoLaureaDifferenti() {
            for (long idCorso = 1; idCorso <= 5; idCorso++) {
                List<AnnoDidattico> anni = new ArrayList<>();
                for (int i = 0; i < 2; i++) {
                    AnnoDidattico ad = new AnnoDidattico("Anno " + i);
                    anni.add(ad);
                }

                when(emUniClass.createNamedQuery(AnnoDidattico.TROVA_ANNI_CORSOLAUREA, AnnoDidattico.class))
                        .thenReturn(typedQuery);
                when(typedQuery.setParameter("corsoId", idCorso))
                        .thenReturn(typedQuery);
                when(typedQuery.getResultList())
                        .thenReturn(anni);

                List<AnnoDidattico> result = annoDidatticoDAO.trovaTuttiCorsoLaurea(idCorso);

                assertEquals(2, result.size());
            }
        }
    }

    @Nested
    @DisplayName("Test del metodo trovaCorsoLaureaNome")
    class TrovaCorsoLaureaRomeTest {

        @Test
        @DisplayName("trovaCorsoLaureaNome restituisce anno per corso e nome validi")
        void testTrovaCorsoLaureaRomeSuccesso() {
            long idCorso = 1;
            String anno = "2023-2024";

            when(emUniClass.createNamedQuery(AnnoDidattico.TROVA_ANNI_CORSOLAUREA_NOME, AnnoDidattico.class))
                    .thenReturn(typedQuery);
            when(typedQuery.setParameter("corsoId", idCorso))
                    .thenReturn(typedQuery);
            when(typedQuery.setParameter("anno", anno))
                    .thenReturn(typedQuery);
            when(typedQuery.getSingleResult())
                    .thenReturn(annoDidattico);

            AnnoDidattico result = annoDidatticoDAO.trovaCorsoLaureaNome(idCorso, anno);

            assertNotNull(result);
            assertEquals(anno, result.getAnno());
            verify(emUniClass, times(1)).createNamedQuery(AnnoDidattico.TROVA_ANNI_CORSOLAUREA_NOME, AnnoDidattico.class);
            verify(typedQuery, times(1)).setParameter("corsoId", idCorso);
            verify(typedQuery, times(1)).setParameter("anno", anno);
        }

        @Test
        @DisplayName("trovaCorsoLaureaNome con parametri differenti")
        void testTrovaCorsoLaureaRomeDifferenti() {
            for (long idCorso = 1; idCorso <= 3; idCorso++) {
                for (String anno : new String[]{"2021-2022", "2022-2023", "2023-2024"}) {
                    AnnoDidattico ad = new AnnoDidattico(anno);

                    when(emUniClass.createNamedQuery(AnnoDidattico.TROVA_ANNI_CORSOLAUREA_NOME, AnnoDidattico.class))
                            .thenReturn(typedQuery);
                    when(typedQuery.setParameter("corsoId", idCorso))
                            .thenReturn(typedQuery);
                    when(typedQuery.setParameter("anno", anno))
                            .thenReturn(typedQuery);
                    when(typedQuery.getSingleResult())
                            .thenReturn(ad);

                    AnnoDidattico result = annoDidatticoDAO.trovaCorsoLaureaNome(idCorso, anno);

                    assertEquals(anno, result.getAnno());
                }
            }
        }
    }

    @Nested
    @DisplayName("Test del metodo aggiungiAnno")
    class AggiungiAnnoTest {

        @Test
        @DisplayName("aggiungiAnno aggiunge correttamente un anno didattico")
        void testAggiungiAnnoSuccesso() {
            when(emUniClass.merge(annoDidattico))
                    .thenReturn(annoDidattico);

            annoDidatticoDAO.aggiungiAnno(annoDidattico);

            verify(emUniClass, times(1)).merge(annoDidattico);
        }

        @Test
        @DisplayName("aggiungiAnno aggiunge multipli anni didattici")
        void testAggiungiAnnoMultipli() {
            for (int i = 0; i < 5; i++) {
                AnnoDidattico ad = new AnnoDidattico("Anno " + i);

                when(emUniClass.merge(ad))
                        .thenReturn(ad);

                annoDidatticoDAO.aggiungiAnno(ad);

                verify(emUniClass, times(1)).merge(ad);
            }
        }

        @Test
        @DisplayName("aggiungiAnno aggiorna un anno esistente")
        void testAggiungiAnnoAggiorna() {
            AnnoDidattico adEsistente = new AnnoDidattico("2024-2025");

            when(emUniClass.merge(adEsistente))
                    .thenReturn(adEsistente);

            annoDidatticoDAO.aggiungiAnno(adEsistente);

            verify(emUniClass, times(1)).merge(adEsistente);
        }
    }

    @Nested
    @DisplayName("Test del metodo rimuoviAnno")
    class RimuoviAnnoTest {

        @Test
        @DisplayName("rimuoviAnno rimuove correttamente un anno didattico")
        void testRimuoviAnnoSuccesso() {
            annoDidatticoDAO.rimuoviAnno(annoDidattico);

            verify(emUniClass, times(1)).remove(annoDidattico);
        }

        @Test
        @DisplayName("rimuoviAnno rimuove multipli anni didattici")
        void testRimuoviAnnoMultipli() {
            for (int i = 0; i < 5; i++) {
                AnnoDidattico ad = new AnnoDidattico("Anno " + i);

                annoDidatticoDAO.rimuoviAnno(ad);

                verify(emUniClass, times(1)).remove(ad);
            }
        }

        @Test
        @DisplayName("rimuoviAnno con oggetto specifico")
        void testRimuoviAnnoSpecifico() {
            AnnoDidattico annoSpecifico = new AnnoDidattico("2025-2026");

            annoDidatticoDAO.rimuoviAnno(annoSpecifico);

            verify(emUniClass, times(1)).remove(annoSpecifico);
        }
    }

    @Nested
    @DisplayName("Test di integrazione e scenari complessi")
    class ScenariComplessiTest {

        @Test
        @DisplayName("Sequenza di operazioni CRUD")
        void testSequenzaCRUD() {
            // Create
            when(emUniClass.merge(annoDidattico))
                    .thenReturn(annoDidattico);
            annoDidatticoDAO.aggiungiAnno(annoDidattico);
            verify(emUniClass, times(1)).merge(annoDidattico);

            // Read
            when(emUniClass.createNamedQuery(AnnoDidattico.TROVA_ID, AnnoDidattico.class))
                    .thenReturn(typedQuery);
            when(typedQuery.setParameter("id", 1))
                    .thenReturn(typedQuery);
            when(typedQuery.getSingleResult())
                    .thenReturn(annoDidattico);

            AnnoDidattico result = annoDidatticoDAO.trovaId(1);
            assertNotNull(result);

            // Update
            AnnoDidattico adAggiornato = new AnnoDidattico("2024-2025");
            when(emUniClass.merge(adAggiornato))
                    .thenReturn(adAggiornato);
            annoDidatticoDAO.aggiungiAnno(adAggiornato);

            // Delete
            annoDidatticoDAO.rimuoviAnno(annoDidattico);
            verify(emUniClass, atLeastOnce()).remove(annoDidattico);
        }

        @Test
        @DisplayName("Operazioni con gestione errori")
        void testOperazioniConErrori() {
            int id = 999;

            when(emUniClass.createNamedQuery(AnnoDidattico.TROVA_ID, AnnoDidattico.class))
                    .thenReturn(typedQuery);
            when(typedQuery.setParameter("id", id))
                    .thenReturn(typedQuery);
            when(typedQuery.getSingleResult())
                    .thenThrow(new jakarta.persistence.NoResultException("Anno non trovato"));

            assertThrows(jakarta.persistence.NoResultException.class, () -> {
                annoDidatticoDAO.trovaId(id);
            });

            verify(typedQuery, times(1)).getSingleResult();
        }

        @Test
        @DisplayName("Ricerca e modifica combinata")
        void testRicercaEModificaCombinata() {
            String anno = "2023-2024";
            List<AnnoDidattico> anni = new ArrayList<>();
            AnnoDidattico ad = new AnnoDidattico(anno);
            anni.add(ad);

            when(emUniClass.createNamedQuery(AnnoDidattico.TROVA_ANNO, AnnoDidattico.class))
                    .thenReturn(typedQuery);
            when(typedQuery.setParameter("anno", anno))
                    .thenReturn(typedQuery);
            when(typedQuery.getResultList())
                    .thenReturn(anni);

            List<AnnoDidattico> result = annoDidatticoDAO.trovaAnno(anno);
            assertEquals(1, result.size());

            // Modifica il primo elemento
            result.get(0).setAnno("2024-2025");
            when(emUniClass.merge(result.get(0)))
                    .thenReturn(result.get(0));
            annoDidatticoDAO.aggiungiAnno(result.get(0));

            verify(emUniClass, times(1)).merge(result.get(0));
        }
    }

    @Nested
    @DisplayName("Test di verifica delle chiamate a EntityManager")
    class VerificaChiamateTest {

        @Test
        @DisplayName("Verifica che EntityManager è chiamato correttamente in trovaAnno")
        void testVerificaChiamateInTrovaAnno() {
            String anno = "2023-2024";

            when(emUniClass.createNamedQuery(AnnoDidattico.TROVA_ANNO, AnnoDidattico.class))
                    .thenReturn(typedQuery);
            when(typedQuery.setParameter("anno", anno))
                    .thenReturn(typedQuery);
            when(typedQuery.getResultList())
                    .thenReturn(new ArrayList<>());

            annoDidatticoDAO.trovaAnno(anno);

            verify(emUniClass).createNamedQuery(AnnoDidattico.TROVA_ANNO, AnnoDidattico.class);
            verify(typedQuery).setParameter("anno", anno);
            verify(typedQuery).getResultList();
            verifyNoMoreInteractions(typedQuery);
        }

        @Test
        @DisplayName("Verifica che EntityManager è chiamato correttamente in trovaTutti")
        void testVerificaChiamateInTrovaTutti() {
            when(emUniClass.createNamedQuery(AnnoDidattico.TROVA_TUTTI, AnnoDidattico.class))
                    .thenReturn(typedQuery);
            when(typedQuery.getResultList())
                    .thenReturn(new ArrayList<>());

            annoDidatticoDAO.trovaTutti();

            verify(emUniClass).createNamedQuery(AnnoDidattico.TROVA_TUTTI, AnnoDidattico.class);
            verify(typedQuery).getResultList();
        }

        @Test
        @DisplayName("Verifica che EntityManager è chiamato correttamente in aggiungiAnno")
        void testVerificaChiamateInAggiungiAnno() {
            when(emUniClass.merge(annoDidattico))
                    .thenReturn(annoDidattico);

            annoDidatticoDAO.aggiungiAnno(annoDidattico);

            verify(emUniClass, times(1)).merge(annoDidattico);
        }

        @Test
        @DisplayName("Verifica che EntityManager è chiamato correttamente in rimuoviAnno")
        void testVerificaChiamateInRimuoviAnno() {
            annoDidatticoDAO.rimuoviAnno(annoDidattico);

            verify(emUniClass, times(1)).remove(annoDidattico);
        }
    }
}

