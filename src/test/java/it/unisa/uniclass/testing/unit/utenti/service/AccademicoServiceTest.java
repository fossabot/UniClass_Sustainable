package it.unisa.uniclass.testing.unit.utenti.service;

import it.unisa.uniclass.utenti.model.Accademico;
import it.unisa.uniclass.utenti.service.AccademicoService;
import it.unisa.uniclass.utenti.service.dao.AccademicoRemote;
import jakarta.persistence.NoResultException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AccademicoServiceTest {

    @Mock
    private AccademicoRemote accademicoDao;

    private AccademicoService accademicoService;

    @BeforeEach
    void setUp() {
        // 1. Inizializza i Mock manualmente (Niente @ExtendWith)
        MockitoAnnotations.openMocks(this);

        // 2. Usa il costruttore per i test che abbiamo aggiunto nel refactoring
        accademicoService = new AccademicoService(accademicoDao);
    }
    @Test
    void testCostruttoreDefault() throws Exception {
        // Mock del lookup JNDI
        try (MockedConstruction<InitialContext> mockedCtx = Mockito.mockConstruction(InitialContext.class,
                (mock, context) -> {
                    when(mock.lookup("java:global/UniClass-Dependability/AccademicoDAO"))
                            .thenReturn(accademicoDao);
                })) {

            AccademicoService service = new AccademicoService(); // Usa il mock
            assertNotNull(service);
        }
    }

    @Test
    void testTrovaEmailUniClass_Esistente() {
        String email = "prof@unisa.it";
        Accademico acc = new Accademico();
        acc.setEmail(email);

        when(accademicoDao.trovaEmailUniClass(email)).thenReturn(acc);

        Accademico result = accademicoService.trovaEmailUniClass(email);
        assertNotNull(result);
        assertEquals(email, result.getEmail());
    }

    @Test
    void testTrovaEmailUniClass_NonTrovato() {
        String email = "fantasma@unisa.it";

        when(accademicoDao.trovaEmailUniClass(email)).thenThrow(new NoResultException());

        Accademico result = accademicoService.trovaEmailUniClass(email);
        assertNull(result, "Se il DAO lancia NoResultException deve ritornare null");
    }

    @Test
    void testTrovaEmailPass_NullPassword() {
        String email = "prof@unisa.it";
        String pass = "qualsiasi";
        Accademico acc = new Accademico();
        acc.setEmail(email);
        acc.setPassword(null); // password null

        when(accademicoDao.trovaEmailUniClass(email)).thenReturn(acc);

        // Deve restituire l'accademico perché password null nel DB
        Accademico result = accademicoService.trovaEmailPassUniclass(email, pass);
        assertNotNull(result);
        assertEquals(email, result.getEmail());
    }

    @Test
    void testTrovaEmailPass_NoResultException() {
        String email = "fantasma@unisa.it";
        String pass = "qualsiasi";

        when(accademicoDao.trovaEmailUniClass(email)).thenThrow(new NoResultException());

        Accademico result = accademicoService.trovaEmailPassUniclass(email, pass);
        assertNull(result, "Se il DAO lancia NoResultException deve ritornare null");
    }
    @Test
    void testTrovaAccademico_Esistente() {
        String matricola = "0512100001";
        Accademico expected = new Accademico();
        expected.setMatricola(matricola);

        when(accademicoDao.trovaAccademicoUniClass(matricola)).thenReturn(expected);

        Accademico result = accademicoService.trovaAccademicoUniClass(matricola);

        assertNotNull(result);
        assertEquals(matricola, result.getMatricola());
    }

    @Test
    void testTrovaAccademico_NonTrovato_GestioneEccezione() {
        // Il service cattura NoResultException e ritorna null
        when(accademicoDao.trovaAccademicoUniClass("999")).thenThrow(new NoResultException());

        Accademico result = accademicoService.trovaAccademicoUniClass("999");

        assertNull(result, "Se il DAO lancia eccezione, il service deve ritornare null");
    }

    @Test
    void testTrovaEmailPass_Successo() {
        String email = "prof@unisa.it";
        String pass = "password123";
        Accademico acc = new Accademico();
        acc.setEmail(email);
        acc.setPassword(pass); // Simuliamo che nel DB la password sia questa

        when(accademicoDao.trovaEmailUniClass(email)).thenReturn(acc);

        Accademico result = accademicoService.trovaEmailPassUniclass(email, pass);
        assertNotNull(result);
        assertEquals(email, result.getEmail());
    }

    @Test
    void testTrovaEmailPass_PasswordErrata() {
        String email = "prof@unisa.it";
        Accademico acc = new Accademico();
        acc.setEmail(email);
        acc.setPassword("giusta"); // ggignore

        when(accademicoDao.trovaEmailUniClass(email)).thenReturn(acc);

        // Proviamo con una password diversa
        Accademico result = accademicoService.trovaEmailPassUniclass(email, "sbagliata");

        assertNull(result, "Deve ritornare null se la password non coincide");
    }

    @Test
    void testAggiungiAccademico() {
        Accademico nuovo = new Accademico();
        nuovo.setMatricola("0512109999");

        accademicoService.aggiungiAccademico(nuovo);

        // Verifica che il metodo del DAO sia stato chiamato 1 volta
        verify(accademicoDao, times(1)).aggiungiAccademico(nuovo);
    }


    @Test
    void testMetodiCRUD_Passacarte() {
        // Test trovaTutti
        accademicoService.trovaTuttiUniClass();
        verify(accademicoDao).trovaTuttiUniClass();

        // Test trovaAttivati
        accademicoService.trovaAttivati(true);
        verify(accademicoDao).trovaAttivati(true);

        // Test retrieveEmail
        accademicoService.retrieveEmail();
        verify(accademicoDao).retrieveEmail();

        // Test rimuovi
        Accademico a = new Accademico();
        accademicoService.rimuoviAccademico(a);
        verify(accademicoDao).rimuoviAccademico(a);

        // Test cambia attivazione
        accademicoService.cambiaAttivazione(a, true);
        verify(accademicoDao).cambiaAttivazione(a, true);
    }

    @Test
    void testCostruttoreDefault_NamingException() {
        try (var mockedCtx = Mockito.mockConstruction(InitialContext.class,
                (mock, context) -> {
                    when(mock.lookup("java:global/UniClass-Dependability/AccademicoDAO"))
                            .thenThrow(new NamingException("Simulated JNDI error"));
                })) {

            RuntimeException ex = assertThrows(RuntimeException.class, AccademicoService::new);

            assertTrue(ex.getMessage().contains("Errore durante il lookup di AccademicoDAO"));
            assertTrue(ex.getCause() instanceof NamingException);
        }
    }

    @Test
    void testTrovaEmailPassUniclass_AccademicoNull() {
        AccademicoRemote dao = mock(AccademicoRemote.class);
        when(dao.trovaEmailUniClass("mail@unisa.it")).thenReturn(null);

        AccademicoService service = new AccademicoService(dao);

        Accademico result = service.trovaEmailPassUniclass("mail@unisa.it", "pwd");
        assertNull(result);
    }
}