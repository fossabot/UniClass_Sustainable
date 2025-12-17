package it.unisa.uniclass.testing.unit.utenti.service;

import it.unisa.uniclass.common.exceptions.AuthenticationException;
import it.unisa.uniclass.utenti.model.Accademico;
import it.unisa.uniclass.utenti.model.PersonaleTA;
import it.unisa.uniclass.utenti.model.Utente;
import it.unisa.uniclass.utenti.service.AccademicoService;
import it.unisa.uniclass.utenti.service.PersonaleTAService;
import it.unisa.uniclass.utenti.service.UtenteService;
import jakarta.persistence.NoResultException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UtenteServiceTest {

    // Sottoclasse per esporre i metodi protected
    static class TestableUtenteService extends UtenteService {
        public PersonaleTAService exposePersonaleTAService() {
            return super.getPersonaleTAService();
        }
        public AccademicoService exposeAccademicoService() {
            return super.getAccademicoService();
        }
    }

    @Mock
    private PersonaleTAService personaleTAService;

    @Mock
    private AccademicoService accademicoService;

    private UtenteService utenteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        utenteService = new UtenteService();
        utenteService.setPersonaleTAService(personaleTAService);
        utenteService.setAccademicoService(accademicoService);
    }

    // --- retrieveByEmail ---
    @Test
    void testRetrieveByEmail_PersonaleTAPresente() {
        PersonaleTA pta = new PersonaleTA();
        pta.setEmail("pta@unisa.it");

        when(personaleTAService.trovaEmail("pta@unisa.it")).thenReturn(pta);

        Utente result = utenteService.retrieveByEmail("pta@unisa.it");

        assertNotNull(result);
        assertEquals("pta@unisa.it", result.getEmail());
    }

    @Test
    void testRetrieveByEmail_AccademicoPresente() {
        Accademico acc = new Accademico();
        acc.setEmail("acc@unisa.it");

        when(personaleTAService.trovaEmail("acc@unisa.it")).thenReturn(null);
        when(accademicoService.trovaEmailUniClass("acc@unisa.it")).thenReturn(acc);

        Utente result = utenteService.retrieveByEmail("acc@unisa.it");

        assertNotNull(result);
        assertEquals("acc@unisa.it", result.getEmail());
    }

    @Test
    void testRetrieveByEmail_Null() {
        when(personaleTAService.trovaEmail("unknown@unisa.it")).thenReturn(null);
        when(accademicoService.trovaEmailUniClass("unknown@unisa.it")).thenReturn(null);

        Utente result = utenteService.retrieveByEmail("unknown@unisa.it");
        assertNull(result);
    }

    // --- retrieveByUserAndPassword ---
    @Test
    void testRetrieveByUserAndPassword_PersonaleTACorretta() throws AuthenticationException {
        PersonaleTA pta = new PersonaleTA();
        pta.setEmail("pta@unisa.it");
        pta.setPassword("pwd123"); // ggignore

        when(personaleTAService.trovaEmail("pta@unisa.it")).thenReturn(pta);

        Utente result = utenteService.retrieveByUserAndPassword("pta@unisa.it", "pwd123");
        assertNotNull(result);
        assertEquals("pta@unisa.it", result.getEmail());
    }

    @Test
    void testRetrieveByUserAndPassword_AccademicoCorretta() throws AuthenticationException {
        Accademico acc = new Accademico();
        acc.setEmail("acc@unisa.it");
        acc.setPassword("pass456"); // ggignore

        when(personaleTAService.trovaEmail("acc@unisa.it")).thenReturn(null);
        when(accademicoService.trovaEmailUniClass("acc@unisa.it")).thenReturn(acc);

        Utente result = utenteService.retrieveByUserAndPassword("acc@unisa.it", "pass456");
        assertNotNull(result);
        assertEquals("acc@unisa.it", result.getEmail());
    }

    @Test
    void testRetrieveByUserAndPassword_PasswordErrata() {
        PersonaleTA pta = new PersonaleTA();
        pta.setEmail("pta@unisa.it");
        pta.setPassword("pwd123"); // ggignore

        when(personaleTAService.trovaEmail("pta@unisa.it")).thenReturn(pta);

        assertThrows(AuthenticationException.class, () ->
                utenteService.retrieveByUserAndPassword("pta@unisa.it", "wrong"));
    }

    @Test
    void testRetrieveByUserAndPassword_NessunoTrovato() throws AuthenticationException {
        when(personaleTAService.trovaEmail("unknown@unisa.it")).thenReturn(null);
        when(accademicoService.trovaEmailUniClass("unknown@unisa.it")).thenReturn(null);

        Utente result = utenteService.retrieveByUserAndPassword("unknown@unisa.it", "any");
        assertNull(result);
    }

    // --- setters ---
    @Test
    void testSetters() {
        PersonaleTAService mockPTA = mock(PersonaleTAService.class);
        AccademicoService mockAcc = mock(AccademicoService.class);

        utenteService.setPersonaleTAService(mockPTA);
        utenteService.setAccademicoService(mockAcc);

        assertNotNull(utenteService);
    }

    // --- copertura aggiuntiva ---
    @Test
    void testLazyLoadingPersonaleTAServiceSafe() {
        try (MockedConstruction<PersonaleTAService> mocked = mockConstruction(PersonaleTAService.class)) {
            TestableUtenteService service = new TestableUtenteService(); // non setti nulla
            PersonaleTAService result = service.exposePersonaleTAService();
            assertNotNull(result);
        }
    }

    @Test
    void testRetrieveByUserAndPassword_NoResultException() throws AuthenticationException {
        when(personaleTAService.trovaEmail("x@unisa.it")).thenThrow(new NoResultException());

        Utente result = utenteService.retrieveByUserAndPassword("x@unisa.it", "pwd");
        assertNull(result);
    }

    @Test
    void testRetrieveByUserAndPassword_AccademicoPasswordErrata() {
        Accademico acc = new Accademico();
        acc.setEmail("acc@unisa.it");
        acc.setPassword("pass456"); // ggignore

        when(personaleTAService.trovaEmail("acc@unisa.it")).thenReturn(null);
        when(accademicoService.trovaEmailUniClass("acc@unisa.it")).thenReturn(acc);

        assertThrows(AuthenticationException.class, () ->
                utenteService.retrieveByUserAndPassword("acc@unisa.it", "wrong"));
    }
    @Test
    void testLazyLoadingAccademicoServiceSafe() {
        try (MockedConstruction<AccademicoService> mocked = mockConstruction(AccademicoService.class)) {
            TestableUtenteService service = new TestableUtenteService(); // non setti nulla
            AccademicoService result = service.exposeAccademicoService();
            assertNotNull(result);
        }
    }

}
