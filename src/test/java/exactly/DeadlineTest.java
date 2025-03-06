package exactly;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;

public class DeadlineTest {

    @Test
    public void testDeadlineToString() {
        // Create a Deadline with a known date
        Deadline d = new Deadline("return book", "2019-12-02");
        // Format the date as in the toString method ("MMM dd yyyy")
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd yyyy");
        String expected = "[D][ ] return book (by: " + "Dec 02 2019" + ")";
        assertEquals(expected, d.toString());
    }
}
