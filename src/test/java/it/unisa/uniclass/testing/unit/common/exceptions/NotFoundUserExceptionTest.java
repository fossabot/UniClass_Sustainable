package it.unisa.uniclass.testing.unit.common.exceptions;

import it.unisa.uniclass.common.exceptions.NotFoundUserException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotFoundUserExceptionTest {

    @Test
    void testConstructorWithoutArgs() {
        NotFoundUserException ex = new NotFoundUserException();
        assertEquals("Utente non trovato", ex.getMessage());
    }

    @Test
    void testConstructorWithMessage() {
        String msg = "Custom message";
        NotFoundUserException ex = new NotFoundUserException(msg);
        assertEquals(msg, ex.getMessage());
    }

    @Test
    void testExceptionIsInstanceOfException() {
        NotFoundUserException ex = new NotFoundUserException("msg");
        assertTrue(ex instanceof Exception);
    }
}
