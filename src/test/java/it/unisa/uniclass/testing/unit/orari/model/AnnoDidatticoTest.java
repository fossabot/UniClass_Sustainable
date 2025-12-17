package it.unisa.uniclass.testing.unit.orari.model;

import it.unisa.uniclass.orari.model.AnnoDidattico;
import it.unisa.uniclass.orari.model.Corso;
import it.unisa.uniclass.orari.model.CorsoLaurea;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test d'unità completi per la classe AnnoDidattico.
 * Verifica costruttori, getter, setter, relazioni JPA e metodi di utilità.
 */
@DisplayName("Test per la classe AnnoDidattico")
public class AnnoDidatticoTest {

    private AnnoDidattico annoDidattico;

    @BeforeEach
    void setUp() {
        annoDidattico = new AnnoDidattico();
    }

    @Nested
    @DisplayName("Test dei Costruttori")
    class CostruttoriTest {

        @Test
        @DisplayName("Costruttore di default crea un'istanza valida")
        void testCostruttoreDefault() {
            AnnoDidattico anno = new AnnoDidattico();
            assertNotNull(anno);
            assertNull(anno.getAnno());
            assertNotNull(anno.getCorsiLaurea());
            assertNotNull(anno.getCorsi());
            assertEquals(0, anno.getId());
        }

        @Test
        @DisplayName("Costruttore con parametro anno inizializza correttamente")
        void testCostruttoreConParametro() {
            String nomeAnno = "Anno 1";
            AnnoDidattico anno = new AnnoDidattico(nomeAnno);

            assertNotNull(anno);
            assertEquals(nomeAnno, anno.getAnno());
            assertNotNull(anno.getCorsiLaurea());
            assertNotNull(anno.getCorsi());
        }

        @Test
        @DisplayName("Costruttore con parametro null accetta valore null")
        void testCostruttoreConParametroNull() {
            AnnoDidattico anno = new AnnoDidattico(null);

            assertNotNull(anno);
            assertNull(anno.getAnno());
        }

        @Test
        @DisplayName("Costruttore con stringa vuota")
        void testCostruttoreConStringaVuota() {
            AnnoDidattico anno = new AnnoDidattico("");

            assertNotNull(anno);
            assertEquals("", anno.getAnno());
        }
    }

    @Nested
    @DisplayName("Test dei Getter e Setter per il campo 'anno'")
    class AnnoGetterSetterTest {

        @Test
        @DisplayName("getAnno restituisce null per default")
        void testGetAnnoDefault() {
            assertNull(annoDidattico.getAnno());
        }

        @Test
        @DisplayName("setAnno e getAnno funzionano correttamente")
        void testSetGetAnno() {
            String nomeAnno = "Anno 2";
            annoDidattico.setAnno(nomeAnno);

            assertEquals(nomeAnno, annoDidattico.getAnno());
        }

        @Test
        @DisplayName("setAnno con null è permesso")
        void testSetAnnoNull() {
            annoDidattico.setAnno("Anno 3");
            annoDidattico.setAnno(null);

            assertNull(annoDidattico.getAnno());
        }

        @Test
        @DisplayName("setAnno con stringa vuota")
        void testSetAnnoStringaVuota() {
            annoDidattico.setAnno("");

            assertEquals("", annoDidattico.getAnno());
        }

        @Test
        @DisplayName("setAnno con stringa contenente spazi")
        void testSetAnnoConSpazi() {
            String nomeAnno = "Anno 1 Magistrale";
            annoDidattico.setAnno(nomeAnno);

            assertEquals(nomeAnno, annoDidattico.getAnno());
        }
    }

    @Nested
    @DisplayName("Test del Getter per 'id'")
    class IdGetterTest {

        @Test
        @DisplayName("getId restituisce 0 per default")
        void testGetIdDefault() {
            assertEquals(0, annoDidattico.getId());
        }
    }

    @Nested
    @DisplayName("Test dei Getter e Setter per 'corsiLaurea'")
    class CorsiLaureaGetterSetterTest {

        @Test
        @DisplayName("getCorsiLaurea restituisce lista non null per default")
        void testGetCorsiLaureaDefault() {
            List<CorsoLaurea> corsi = annoDidattico.getCorsiLaurea();

            assertNotNull(corsi);
            assertTrue(corsi.isEmpty());
        }

        @Test
        @DisplayName("setCorsiLaurea e getCorsiLaurea funzionano correttamente")
        void testSetGetCorsiLaurea() {
            List<CorsoLaurea> nuoviCorsi = new ArrayList<>();
            CorsoLaurea corso1 = new CorsoLaurea();
            CorsoLaurea corso2 = new CorsoLaurea();
            nuoviCorsi.add(corso1);
            nuoviCorsi.add(corso2);

            annoDidattico.setCorsiLaurea(nuoviCorsi);

            assertEquals(nuoviCorsi, annoDidattico.getCorsiLaurea());
            assertEquals(2, annoDidattico.getCorsiLaurea().size());
        }

        @Test
        @DisplayName("setCorsiLaurea con lista vuota")
        void testSetCorsiLaureaVuota() {
            List<CorsoLaurea> listaVuota = new ArrayList<>();
            annoDidattico.setCorsiLaurea(listaVuota);

            assertNotNull(annoDidattico.getCorsiLaurea());
            assertTrue(annoDidattico.getCorsiLaurea().isEmpty());
        }

        @Test
        @DisplayName("setCorsiLaurea con null è permesso")
        void testSetCorsiLaureaNull() {
            annoDidattico.setCorsiLaurea(null);

            assertNull(annoDidattico.getCorsiLaurea());
        }

        @Test
        @DisplayName("Modifica della lista restituita da getCorsiLaurea si riflette sull'oggetto")
        void testModificaListaCorsiLaurea() {
            List<CorsoLaurea> corsi = annoDidattico.getCorsiLaurea();
            CorsoLaurea nuovoCorso = new CorsoLaurea();
            corsi.add(nuovoCorso);

            assertEquals(1, annoDidattico.getCorsiLaurea().size());
            assertTrue(annoDidattico.getCorsiLaurea().contains(nuovoCorso));
        }
    }

    @Nested
    @DisplayName("Test dei Getter e Setter per 'corsi'")
    class CorsiGetterSetterTest {

        @Test
        @DisplayName("getCorsi restituisce lista non null per default")
        void testGetCorsiDefault() {
            List<Corso> corsi = annoDidattico.getCorsi();

            assertNotNull(corsi);
            assertTrue(corsi.isEmpty());
        }

        @Test
        @DisplayName("setCorsi e getCorsi funzionano correttamente")
        void testSetGetCorsi() {
            List<Corso> nuoviCorsi = new ArrayList<>();
            Corso corso1 = new Corso();
            Corso corso2 = new Corso();
            nuoviCorsi.add(corso1);
            nuoviCorsi.add(corso2);

            annoDidattico.setCorsi(nuoviCorsi);

            assertEquals(nuoviCorsi, annoDidattico.getCorsi());
            assertEquals(2, annoDidattico.getCorsi().size());
        }

        @Test
        @DisplayName("setCorsi con lista vuota")
        void testSetCorsiVuota() {
            List<Corso> listaVuota = new ArrayList<>();
            annoDidattico.setCorsi(listaVuota);

            assertNotNull(annoDidattico.getCorsi());
            assertTrue(annoDidattico.getCorsi().isEmpty());
        }

        @Test
        @DisplayName("setCorsi con null è permesso")
        void testSetCorsiNull() {
            annoDidattico.setCorsi(null);

            assertNull(annoDidattico.getCorsi());
        }

        @Test
        @DisplayName("Modifica della lista restituita da getCorsi si riflette sull'oggetto")
        void testModificaListaCorsi() {
            List<Corso> corsi = annoDidattico.getCorsi();
            Corso nuovoCorso = new Corso();
            corsi.add(nuovoCorso);

            assertEquals(1, annoDidattico.getCorsi().size());
            assertTrue(annoDidattico.getCorsi().contains(nuovoCorso));
        }

        @Test
        @DisplayName("Aggiunta multipla di corsi")
        void testAggiuntaMultiplaCorsi() {
            List<Corso> corsi = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                corsi.add(new Corso());
            }

            annoDidattico.setCorsi(corsi);

            assertEquals(5, annoDidattico.getCorsi().size());
        }
    }

    @Nested
    @DisplayName("Test del metodo toString")
    class ToStringTest {

        @Test
        @DisplayName("toString con valori di default")
        void testToStringDefault() {
            String result = annoDidattico.toString();

            assertNotNull(result);
            assertTrue(result.contains("AnnoDidattico"));
            assertTrue(result.contains("id="));
            assertTrue(result.contains("anno="));
        }

        @Test
        @DisplayName("toString con anno impostato")
        void testToStringConAnno() {
            annoDidattico.setAnno("Anno 1");
            String result = annoDidattico.toString();

            assertNotNull(result);
            assertTrue(result.contains("AnnoDidattico"));
            assertTrue(result.contains("anno='Anno 1'"));
        }

        @Test
        @DisplayName("toString con anno null")
        void testToStringConAnnoNull() {
            annoDidattico.setAnno(null);
            String result = annoDidattico.toString();

            assertNotNull(result);
            assertTrue(result.contains("AnnoDidattico"));
            assertTrue(result.contains("anno="));
        }

        @Test
        @DisplayName("toString non è null o vuoto")
        void testToStringNonVuoto() {
            String result = annoDidattico.toString();

            assertNotNull(result);
            assertFalse(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("Test di integrazione e scenari complessi")
    class ScenariComplessiTest {

        @Test
        @DisplayName("Creazione completa di un anno didattico con tutte le proprietà")
        void testCreazioneCompleta() {
            AnnoDidattico anno = new AnnoDidattico("Anno 2");

            List<CorsoLaurea> corsiLaurea = new ArrayList<>();
            corsiLaurea.add(new CorsoLaurea());
            corsiLaurea.add(new CorsoLaurea());
            anno.setCorsiLaurea(corsiLaurea);

            List<Corso> corsi = new ArrayList<>();
            corsi.add(new Corso());
            corsi.add(new Corso());
            corsi.add(new Corso());
            anno.setCorsi(corsi);

            assertEquals("Anno 2", anno.getAnno());
            assertEquals(2, anno.getCorsiLaurea().size());
            assertEquals(3, anno.getCorsi().size());
        }

        @Test
        @DisplayName("Sostituzione completa delle liste")
        void testSostituzioneCompletaListe() {
            // Prima configurazione
            List<Corso> primaLista = new ArrayList<>();
            primaLista.add(new Corso());
            annoDidattico.setCorsi(primaLista);

            // Seconda configurazione
            List<Corso> secondaLista = new ArrayList<>();
            secondaLista.add(new Corso());
            secondaLista.add(new Corso());
            annoDidattico.setCorsi(secondaLista);

            assertEquals(2, annoDidattico.getCorsi().size());
            assertEquals(secondaLista, annoDidattico.getCorsi());
        }

        @Test
        @DisplayName("Reset di tutte le proprietà")
        void testResetProprietà() {
            annoDidattico.setAnno("Anno 3");
            List<Corso> corsi = new ArrayList<>();
            corsi.add(new Corso());
            annoDidattico.setCorsi(corsi);

            // Reset
            annoDidattico.setAnno(null);
            annoDidattico.setCorsi(new ArrayList<>());
            annoDidattico.setCorsiLaurea(new ArrayList<>());

            assertNull(annoDidattico.getAnno());
            assertTrue(annoDidattico.getCorsi().isEmpty());
            assertTrue(annoDidattico.getCorsiLaurea().isEmpty());
        }

        @Test
        @DisplayName("Verifica immutabilità degli ID")
        void testImmutabilitàId() {
            int idIniziale = annoDidattico.getId();

            annoDidattico.setAnno("Test");
            annoDidattico.setCorsi(new ArrayList<>());

            // L'ID non dovrebbe cambiare (nessun setter pubblico)
            assertEquals(idIniziale, annoDidattico.getId());
        }
    }

    @Nested
    @DisplayName("Test dei valori limite e casi speciali")
    class CasiLimiteTest {

        @Test
        @DisplayName("Anno con caratteri speciali")
        void testAnnoConCaratteriSpeciali() {
            String annoSpeciale = "Anno 1 - Corso 2023/2024 (Informatica)";
            annoDidattico.setAnno(annoSpeciale);

            assertEquals(annoSpeciale, annoDidattico.getAnno());
        }

        @Test
        @DisplayName("Anno con stringa molto lunga")
        void testAnnoConStringaLunga() {
            String annoLungo = "A".repeat(1000);
            annoDidattico.setAnno(annoLungo);

            assertEquals(annoLungo, annoDidattico.getAnno());
            assertEquals(1000, annoDidattico.getAnno().length());
        }

        @Test
        @DisplayName("Lista corsi con molti elementi")
        void testListaCorsiGrande() {
            List<Corso> moltiCorsi = new ArrayList<>();
            for (int i = 0; i < 100; i++) {
                moltiCorsi.add(new Corso());
            }

            annoDidattico.setCorsi(moltiCorsi);

            assertEquals(100, annoDidattico.getCorsi().size());
        }

        @Test
        @DisplayName("Modifica sequenziale dell'anno")
        void testModificaSequenzialeAnno() {
            annoDidattico.setAnno("Anno 1");
            assertEquals("Anno 1", annoDidattico.getAnno());

            annoDidattico.setAnno("Anno 2");
            assertEquals("Anno 2", annoDidattico.getAnno());

            annoDidattico.setAnno("Anno 3");
            assertEquals("Anno 3", annoDidattico.getAnno());
        }
    }

    @Nested
    @DisplayName("Test delle Named Queries (verifica costanti)")
    class NamedQueriesTest {

        @Test
        @DisplayName("Verifica costanti Named Queries sono definite")
        void testCostantiNamedQueries() {
            assertEquals("AnnoDidattico.trovaAnno", AnnoDidattico.TROVA_ANNO);
            assertEquals("AnnoDidattico.trovaId", AnnoDidattico.TROVA_ID);
            assertEquals("AnnoDidattico.trovaTutti", AnnoDidattico.TROVA_TUTTI);
            assertEquals("AnnoDidattico.trovaAnniCorsoLaurea", AnnoDidattico.TROVA_ANNI_CORSOLAUREA);
            assertEquals("AnnoDidattico.trovaAnniCorsoLaureaNome", AnnoDidattico.TROVA_ANNI_CORSOLAUREA_NOME);
        }

        @Test
        @DisplayName("Costanti Named Queries non sono null")
        void testCostantiNonNull() {
            assertNotNull(AnnoDidattico.TROVA_ANNO);
            assertNotNull(AnnoDidattico.TROVA_ID);
            assertNotNull(AnnoDidattico.TROVA_TUTTI);
            assertNotNull(AnnoDidattico.TROVA_ANNI_CORSOLAUREA);
            assertNotNull(AnnoDidattico.TROVA_ANNI_CORSOLAUREA_NOME);
        }

        @Test
        @DisplayName("Costanti Named Queries non sono vuote")
        void testCostantiNonVuote() {
            assertFalse(AnnoDidattico.TROVA_ANNO.isEmpty());
            assertFalse(AnnoDidattico.TROVA_ID.isEmpty());
            assertFalse(AnnoDidattico.TROVA_TUTTI.isEmpty());
            assertFalse(AnnoDidattico.TROVA_ANNI_CORSOLAUREA.isEmpty());
            assertFalse(AnnoDidattico.TROVA_ANNI_CORSOLAUREA_NOME.isEmpty());
        }
    }
}
