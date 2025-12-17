package it.unisa.uniclass.testing.unit.orari.model;

import it.unisa.uniclass.orari.model.Aula;
import it.unisa.uniclass.orari.model.Lezione;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test d'unità completi per la classe Aula.
 * Verifica costruttori, getter, setter, relazioni JPA e metodi di utilità.
 */
@DisplayName("Test per la classe Aula")
public class AulaTest {

    private Aula aula;

    @BeforeEach
    void setUp() {
        aula = new Aula();
    }

    @Nested
    @DisplayName("Test dei Costruttori")
    class CostruttoriTest {

        @Test
        @DisplayName("Costruttore di default crea un'istanza valida")
        void testCostruttoreDefault() {
            Aula aulaTest = new Aula();
            assertNotNull(aulaTest);
            assertNull(aulaTest.getNome());
            assertNull(aulaTest.getEdificio());
            assertNotNull(aulaTest.getLezioni());
            assertEquals(0, aulaTest.getId());
        }

        @Test
        @DisplayName("Costruttore con parametri inizializza correttamente")
        void testCostruttoreConParametri() {
            int id = 1;
            String edificio = "Edificio A";
            String nome = "Aula 101";
            Aula aulaTest = new Aula(id, edificio, nome);

            assertNotNull(aulaTest);
            assertEquals(id, aulaTest.getId());
            assertEquals(edificio, aulaTest.getEdificio());
            assertEquals(nome, aulaTest.getNome());
            assertNotNull(aulaTest.getLezioni());
        }

        @Test
        @DisplayName("Costruttore con parametri null")
        void testCostruttoreConParametriNull() {
            Aula aulaTest = new Aula(1, null, null);

            assertNotNull(aulaTest);
            assertEquals(1, aulaTest.getId());
            assertNull(aulaTest.getEdificio());
            assertNull(aulaTest.getNome());
        }

        @Test
        @DisplayName("Costruttore con ID zero")
        void testCostruttoreConIdZero() {
            Aula aulaTest = new Aula(0, "Edificio B", "Aula 201");

            assertNotNull(aulaTest);
            assertEquals(0, aulaTest.getId());
            assertEquals("Edificio B", aulaTest.getEdificio());
            assertEquals("Aula 201", aulaTest.getNome());
        }

        @Test
        @DisplayName("Costruttore con ID negativo")
        void testCostruttoreConIdNegativo() {
            Aula aulaTest = new Aula(-1, "Edificio C", "Aula 301");

            assertNotNull(aulaTest);
            assertEquals(-1, aulaTest.getId());
        }

        @Test
        @DisplayName("Costruttore con stringhe vuote")
        void testCostruttoreConStringheVuote() {
            Aula aulaTest = new Aula(1, "", "");

            assertNotNull(aulaTest);
            assertEquals("", aulaTest.getEdificio());
            assertEquals("", aulaTest.getNome());
        }
    }

    @Nested
    @DisplayName("Test dei Getter e Setter per 'nome'")
    class NomeGetterSetterTest {

        @Test
        @DisplayName("getNome restituisce null per default")
        void testGetNomeDefault() {
            assertNull(aula.getNome());
        }

        @Test
        @DisplayName("setNome e getNome funzionano correttamente")
        void testSetGetNome() {
            String nomeAula = "Aula 102";
            aula.setNome(nomeAula);

            assertEquals(nomeAula, aula.getNome());
        }

        @Test
        @DisplayName("setNome con null è permesso")
        void testSetNomeNull() {
            aula.setNome("Aula 103");
            aula.setNome(null);

            assertNull(aula.getNome());
        }

        @Test
        @DisplayName("setNome con stringa vuota")
        void testSetNomeStringaVuota() {
            aula.setNome("");

            assertEquals("", aula.getNome());
        }

        @Test
        @DisplayName("setNome con nome contente spazi")
        void testSetNomeConSpazi() {
            String nomeAula = "Aula 104 - Laboratorio";
            aula.setNome(nomeAula);

            assertEquals(nomeAula, aula.getNome());
        }

        @Test
        @DisplayName("Modifica sequenziale del nome")
        void testModificaSequenzialaNome() {
            aula.setNome("Aula A");
            assertEquals("Aula A", aula.getNome());

            aula.setNome("Aula B");
            assertEquals("Aula B", aula.getNome());

            aula.setNome("Aula C");
            assertEquals("Aula C", aula.getNome());
        }
    }

    @Nested
    @DisplayName("Test dei Getter e Setter per 'edificio'")
    class EdificioGetterSetterTest {

        @Test
        @DisplayName("getEdificio restituisce null per default")
        void testGetEdificioDefault() {
            assertNull(aula.getEdificio());
        }

        @Test
        @DisplayName("setEdificio e getEdificio funzionano correttamente")
        void testSetGetEdificio() {
            String edificio = "Edificio D";
            aula.setEdificio(edificio);

            assertEquals(edificio, aula.getEdificio());
        }

        @Test
        @DisplayName("setEdificio con null è permesso")
        void testSetEdificioNull() {
            aula.setEdificio("Edificio E");
            aula.setEdificio(null);

            assertNull(aula.getEdificio());
        }

        @Test
        @DisplayName("setEdificio con stringa vuota")
        void testSetEdificioStringaVuota() {
            aula.setEdificio("");

            assertEquals("", aula.getEdificio());
        }

        @Test
        @DisplayName("setEdificio con nome con numeri")
        void testSetEdificioConNumeri() {
            String edificio = "Edificio 2A";
            aula.setEdificio(edificio);

            assertEquals(edificio, aula.getEdificio());
        }

        @Test
        @DisplayName("Modifica sequenziale dell'edificio")
        void testModificaSequenzialeEdificio() {
            aula.setEdificio("Edificio Nord");
            assertEquals("Edificio Nord", aula.getEdificio());

            aula.setEdificio("Edificio Sud");
            assertEquals("Edificio Sud", aula.getEdificio());

            aula.setEdificio("Edificio Est");
            assertEquals("Edificio Est", aula.getEdificio());
        }
    }

    @Nested
    @DisplayName("Test del Getter per 'id'")
    class IdGetterTest {

        @Test
        @DisplayName("getId restituisce 0 per default")
        void testGetIdDefault() {
            assertEquals(0, aula.getId());
        }

        @Test
        @DisplayName("getId restituisce il valore corretto dopo costruzione")
        void testGetIdConCostruttore() {
            Aula aulaTest = new Aula(42, "Edificio F", "Aula 105");

            assertEquals(42, aulaTest.getId());
        }

        @Test
        @DisplayName("getId restituisce valore con ID molto grande")
        void testGetIdValoreLargo() {
            Aula aulaTest = new Aula(Integer.MAX_VALUE, "Edificio G", "Aula 106");

            assertEquals(Integer.MAX_VALUE, aulaTest.getId());
        }
    }

    @Nested
    @DisplayName("Test dei Getter e Setter per 'lezioni'")
    class LezioniGetterSetterTest {

        @Test
        @DisplayName("getLezioni restituisce lista non null per default")
        void testGetLezioniDefault() {
            List<Lezione> lezioni = aula.getLezioni();

            assertNotNull(lezioni);
            assertTrue(lezioni.isEmpty());
        }

        @Test
        @DisplayName("Modifica della lista restituita da getLezioni si riflette sull'oggetto")
        void testModificaListaLezioni() {
            List<Lezione> lezioni = aula.getLezioni();
            Lezione nuovaLezione = new Lezione();
            lezioni.add(nuovaLezione);

            assertEquals(1, aula.getLezioni().size());
            assertTrue(aula.getLezioni().contains(nuovaLezione));
        }

        @Test
        @DisplayName("Aggiunta multipla di lezioni")
        void testAggiuntaMultiplaLezioni() {
            List<Lezione> lezioni = aula.getLezioni();
            for (int i = 0; i < 5; i++) {
                lezioni.add(new Lezione());
            }

            assertEquals(5, aula.getLezioni().size());
        }

        @Test
        @DisplayName("Rimozione di lezioni dalla lista")
        void testRimozioneLezoni() {
            List<Lezione> lezioni = aula.getLezioni();
            Lezione lez1 = new Lezione();
            Lezione lez2 = new Lezione();
            lezioni.add(lez1);
            lezioni.add(lez2);

            assertEquals(2, aula.getLezioni().size());

            lezioni.remove(lez1);

            assertEquals(1, aula.getLezioni().size());
            assertTrue(aula.getLezioni().contains(lez2));
            assertFalse(aula.getLezioni().contains(lez1));
        }

        @Test
        @DisplayName("Verifica lista lezioni non nulla anche con costruttore con parametri")
        void testLezioniNonNullaConCostruttore() {
            Aula aulaTest = new Aula(1, "Edificio H", "Aula 107");

            assertNotNull(aulaTest.getLezioni());
            assertTrue(aulaTest.getLezioni().isEmpty());
        }
    }

    @Nested
    @DisplayName("Test del metodo toString")
    class ToStringTest {

        @Test
        @DisplayName("toString con valori di default")
        void testToStringDefault() {
            String result = aula.toString();

            assertNotNull(result);
            assertTrue(result.contains("Aula"));
            assertTrue(result.contains("id="));
            assertTrue(result.contains("edificio="));
            assertTrue(result.contains("nome="));
        }

        @Test
        @DisplayName("toString con tutti i valori impostati")
        void testToStringConValori() {
            aula = new Aula(5, "Edificio I", "Aula 108");
            String result = aula.toString();

            assertNotNull(result);
            assertTrue(result.contains("id=5"));
            assertTrue(result.contains("edificio='Edificio I'"));
            assertTrue(result.contains("nome='Aula 108'"));
        }

        @Test
        @DisplayName("toString con nome null")
        void testToStringConNomeNull() {
            aula.setEdificio("Edificio L");
            aula.setNome(null);
            String result = aula.toString();

            assertNotNull(result);
            assertTrue(result.contains("Aula"));
            assertTrue(result.contains("edificio='Edificio L'"));
        }

        @Test
        @DisplayName("toString con edificio null")
        void testToStringConEdificioNull() {
            aula.setNome("Aula 109");
            aula.setEdificio(null);
            String result = aula.toString();

            assertNotNull(result);
            assertTrue(result.contains("Aula"));
            assertTrue(result.contains("nome='Aula 109'"));
        }

        @Test
        @DisplayName("toString non è null o vuoto")
        void testToStringNonVuoto() {
            String result = aula.toString();

            assertNotNull(result);
            assertFalse(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("Test di integrazione e scenari complessi")
    class ScenariComplessiTest {

        @Test
        @DisplayName("Creazione completa di un'aula con tutte le proprietà")
        void testCreazioneCompleta() {
            Aula aulaCompleta = new Aula(10, "Edificio M", "Aula 110");

            List<Lezione> lezioni = new ArrayList<>();
            lezioni.add(new Lezione());
            lezioni.add(new Lezione());
            lezioni.add(new Lezione());
            aulaCompleta.getLezioni().addAll(lezioni);

            assertEquals(10, aulaCompleta.getId());
            assertEquals("Edificio M", aulaCompleta.getEdificio());
            assertEquals("Aula 110", aulaCompleta.getNome());
            assertEquals(3, aulaCompleta.getLezioni().size());
        }

        @Test
        @DisplayName("Modifica completa di un'aula")
        void testModificaCompleta() {
            aula = new Aula(1, "Edificio N", "Aula 111");

            aula.setEdificio("Edificio O");
            aula.setNome("Aula 112");

            aula.getLezioni().add(new Lezione());
            aula.getLezioni().add(new Lezione());

            assertEquals("Edificio O", aula.getEdificio());
            assertEquals("Aula 112", aula.getNome());
            assertEquals(2, aula.getLezioni().size());
        }

        @Test
        @DisplayName("Reset di tutte le proprietà")
        void testResetProprietà() {
            aula = new Aula(5, "Edificio P", "Aula 113");
            aula.getLezioni().add(new Lezione());

            // Reset
            aula.setEdificio(null);
            aula.setNome(null);
            aula.getLezioni().clear();

            assertNull(aula.getEdificio());
            assertNull(aula.getNome());
            assertTrue(aula.getLezioni().isEmpty());
            assertEquals(5, aula.getId()); // ID non cambia
        }

        @Test
        @DisplayName("Immutabilità dell'ID")
        void testImmutabilitàId() {
            aula = new Aula(20, "Edificio Q", "Aula 114");
            int idIniziale = aula.getId();

            aula.setEdificio("Edificio R");
            aula.setNome("Aula 115");
            aula.getLezioni().add(new Lezione());

            // L'ID non dovrebbe cambiare (nessun setter pubblico)
            assertEquals(idIniziale, aula.getId());
        }

        @Test
        @DisplayName("Scambio di proprietà tra due aule")
        void testScambioProprietà() {
            Aula aula1 = new Aula(1, "Edificio S", "Aula 116");
            Aula aula2 = new Aula(2, "Edificio T", "Aula 117");

            String tempEdificio = aula1.getEdificio();
            String tempNome = aula1.getNome();

            aula1.setEdificio(aula2.getEdificio());
            aula1.setNome(aula2.getNome());
            aula2.setEdificio(tempEdificio);
            aula2.setNome(tempNome);

            assertEquals("Edificio T", aula1.getEdificio());
            assertEquals("Aula 117", aula1.getNome());
            assertEquals("Edificio S", aula2.getEdificio());
            assertEquals("Aula 116", aula2.getNome());
        }
    }

    @Nested
    @DisplayName("Test dei valori limite e casi speciali")
    class CasiLimiteTest {

        @Test
        @DisplayName("Nome aula con caratteri speciali")
        void testNomeConCaratteriSpeciali() {
            String nomeSpeciale = "Aula 118 - Lab (A/B)";
            aula.setNome(nomeSpeciale);

            assertEquals(nomeSpeciale, aula.getNome());
        }

        @Test
        @DisplayName("Nome aula con Unicode")
        void testNomeConUnicode() {
            String nomeUnicode = "Aula 119 - ñáéíóú";
            aula.setNome(nomeUnicode);

            assertEquals(nomeUnicode, aula.getNome());
        }

        @Test
        @DisplayName("Edificio con stringa molto lunga")
        void testEdificioConStringaLunga() {
            String edificioLungo = "E".repeat(1000);
            aula.setEdificio(edificioLungo);

            assertEquals(edificioLungo, aula.getEdificio());
            assertEquals(1000, aula.getEdificio().length());
        }

        @Test
        @DisplayName("Nome con stringa molto lunga")
        void testNomeConStringaLunga() {
            String nomeLungo = "A".repeat(1000);
            aula.setNome(nomeLungo);

            assertEquals(nomeLungo, aula.getNome());
            assertEquals(1000, aula.getNome().length());
        }

        @Test
        @DisplayName("Lista lezioni con molti elementi")
        void testListaLezioniGrande() {
            for (int i = 0; i < 100; i++) {
                aula.getLezioni().add(new Lezione());
            }

            assertEquals(100, aula.getLezioni().size());
        }

        @Test
        @DisplayName("Aula con ID molto grande")
        void testIdValoreLargo() {
            Aula aulaTest = new Aula(Integer.MAX_VALUE, "Edificio U", "Aula 120");

            assertEquals(Integer.MAX_VALUE, aulaTest.getId());
        }

        @Test
        @DisplayName("Aula con ID minimo")
        void testIdValoreMinimo() {
            Aula aulaTest = new Aula(Integer.MIN_VALUE, "Edificio V", "Aula 121");

            assertEquals(Integer.MIN_VALUE, aulaTest.getId());
        }
    }

    @Nested
    @DisplayName("Test delle Named Queries (verifica costanti)")
    class NamedQueriesTest {

        @Test
        @DisplayName("Verifica costanti Named Queries sono definite")
        void testCostantiNamedQueries() {
            assertEquals("Aula.trovaAulaNome", Aula.TROVA_AULANOME);
            assertEquals("Aula.trovaAula", Aula.TROVA_AULA);
            assertEquals("Aula.trovaAulaEdificio", Aula.TROVA_AULA_EDIFICIO);
            assertEquals("Aula.trovaTutte", Aula.TROVA_TUTTE);
            assertEquals("Aula.trovaEdifici", Aula.TROVA_EDIFICI);
        }

        @Test
        @DisplayName("Costanti Named Queries non sono null")
        void testCostantiNonNull() {
            assertNotNull(Aula.TROVA_AULANOME);
            assertNotNull(Aula.TROVA_AULA);
            assertNotNull(Aula.TROVA_AULA_EDIFICIO);
            assertNotNull(Aula.TROVA_TUTTE);
            assertNotNull(Aula.TROVA_EDIFICI);
        }

        @Test
        @DisplayName("Costanti Named Queries non sono vuote")
        void testCostantiNonVuote() {
            assertFalse(Aula.TROVA_AULANOME.isEmpty());
            assertFalse(Aula.TROVA_AULA.isEmpty());
            assertFalse(Aula.TROVA_AULA_EDIFICIO.isEmpty());
            assertFalse(Aula.TROVA_TUTTE.isEmpty());
            assertFalse(Aula.TROVA_EDIFICI.isEmpty());
        }
    }
}
