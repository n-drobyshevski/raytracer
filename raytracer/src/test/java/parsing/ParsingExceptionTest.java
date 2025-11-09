package parsing;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ParsingExceptionTest {

    @Test
    void testParsingExceptionMessage() {
        String errorMessage = "Invalid scene file format";
        ParsingException exception = new ParsingException(errorMessage);
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void testParsingExceptionNullMessage() {
        ParsingException exception = new ParsingException(null);
        assertNull(exception.getMessage());
    }
}
