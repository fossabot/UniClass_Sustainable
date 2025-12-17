package it.unisa.uniclass.testing.unit.common.exceptions;

import it.unisa.uniclass.common.exceptions.IncorrectUserSpecification;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IncorrectUserSpecificationTest {

    @Test
    void testConstructorWithMessage() {
        String msg = "Specifiche utente non corrette";
        IncorrectUserSpecification ex = new IncorrectUserSpecification(msg);

        assertEquals(msg, ex.getMessage());
    }

    @Test
    void testConstructorWithoutMessage() {
        IncorrectUserSpecification ex = new IncorrectUserSpecification();

        // In questo caso il messaggio è null
        assertNull(ex.getMessage());
    }

    @Test
    void testExceptionIsInstanceOfException() {
        IncorrectUserSpecification ex = new IncorrectUserSpecification("msg");
        assertTrue(ex instanceof Exception);
    }
}
