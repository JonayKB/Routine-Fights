package es.iespuertodelacruz.routinefights.shared.config;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import es.iespuertodelacruz.routinefights.shared.exceptions.MailException;

class CustomExceptionResolverTest {

    @Test
    void appAndValidationErrorsAreShown() {
        assertTrue(CustomExceptionResolver.isUserFacing(new MailException("Try again later")));
        assertTrue(CustomExceptionResolver.isUserFacing(new IllegalArgumentException("Comment cannot be empty")));
    }

    @Test
    void internalErrorsAreHidden() {
        assertFalse(CustomExceptionResolver.isUserFacing(new NullPointerException("x.y() on null")));
        assertFalse(CustomExceptionResolver.isUserFacing(new IllegalStateException("Neo4j driver closed")));
    }
}
