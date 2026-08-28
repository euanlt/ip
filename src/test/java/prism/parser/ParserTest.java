package prism.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import prism.PrismException;

public class ParserTest {

    @Test
    public void parseTodoDescription_validInput_returnsTrimmedDescription() throws PrismException {
        String input = "todo read book";
        String description = Parser.parseTodoDescription(input);
        assertEquals("read book", description);
    }

    @Test
    public void parseTodoDescription_paddedInput_returnsTrimmedDescription() throws PrismException {
        String input = "todo    return library book   ";
        String description = Parser.parseTodoDescription(input);
        assertEquals("return library book", description);
    }

    @Test
    public void parseTodoDescription_emptyDescription_throwsException() {
        String input = "todo";
        PrismException exception = assertThrows(
                PrismException.class,
                () -> Parser.parseTodoDescription(input)
        );
        assertEquals("!!! The description of a todo cannot be empty.", exception.getMessage());
    }

    @Test
    public void parseTodoDescription_onlyWhitespace_throwsException() {
        String input = "todo     ";
        assertThrows(PrismException.class, () -> Parser.parseTodoDescription(input));
    }

    @Test
    public void parseFindKeyword_validInput_returnsTrimmedKeyword() throws PrismException {
        assertEquals("book", Parser.parseFindKeyword("find    book   "));
    }

    @Test
    public void parseFindKeyword_emptyKeyword_throwsException() {
        assertThrows(PrismException.class, () -> Parser.parseFindKeyword("find   "));
    }
}