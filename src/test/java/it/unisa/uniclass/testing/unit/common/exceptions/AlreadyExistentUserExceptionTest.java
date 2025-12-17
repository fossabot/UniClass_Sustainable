package it.unisa.uniclass.testing.unit.common.exceptions;

import it.unisa.uniclass.common.exceptions.AlreadyExistentUserException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AlreadyExistentUserExceptionTest {

    @Test
    void testConstructorWithMessage() {
        String msg = "Utente già esistente";
        AlreadyExistentUserException ex = new AlreadyExistentUserException(msg);

        assertEquals(msg, ex.getMessage());
    }

    @Test
    void testConstructorWithoutMessage() {
        AlreadyExistentUserException ex = new AlreadyExistentUserException();

        // In questo caso il messaggio è null
        assertNull(ex.getMessage());
    }

    @Test
    void testExceptionIsInstanceOfException() {
        AlreadyExistentUserException ex = new AlreadyExistentUserException("msg");
        assertTrue(ex instanceof Exception);
    }
}
