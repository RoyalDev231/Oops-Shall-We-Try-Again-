package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

public class InputValidatorTest {
   private static class IOHarness {
        final Queue<String> inputs = new ArrayDeque<>();
        final List<String> outputs = new ArrayList<>();

        Supplier<String> in() { return inputs::poll; }
        Consumer<String> out() { return outputs::add; }

        void addInputs(String... lines) {
            for (String s : lines) inputs.add(s);
        }
    }
@Test
    void acceptsValidOnFirstTry() {
        IOHarness io = new IOHarness();
        io.addInputs("45");
        int result = InputValidator.getIntInRange(0, 100, "Please enter a value", "Your value is invalid", io.in(), io.out());
        assertEquals(45, result);
        // First prompt shown, no error printed
        assertEquals(List.of("Please enter a value"), io.outputs);
    }

    @Test
    void rejectsNonNumericThenAcceptsValid() {
        IOHarness io = new IOHarness();
        io.addInputs("abc", "42");
        int result = InputValidator.getIntInRange(0, 100, "Please enter a value", "Your value is invalid", io.in(), io.out());
        assertEquals(42, result);
        // Expect: prompt, error, prompt
        assertEquals(List.of("Please enter a value", "Your value is invalid", "Please enter a value"), io.outputs);
    }

    @Test
    void rejectsOutOfRangeThenAcceptsValid() {
        IOHarness io = new IOHarness();
        io.addInputs("500", "-100", "45");
        int result = InputValidator.getIntInRange(-50, 100, "Please enter a value", "Your value is invalid", io.in(), io.out());
        assertEquals(45, result);
        // prompt, error, prompt, error, prompt
        assertEquals(List.of(
                "Please enter a value",
                "Your value is invalid",
                "Please enter a value",
                "Your value is invalid",
                "Please enter a value"
        ), io.outputs);
    }

    @Test
    void acceptsBoundaryValues() {
        IOHarness io1 = new IOHarness();
        io1.addInputs("-50");
        assertEquals(-50, InputValidator.getIntInRange(-50, 100, "P", "E", io1.in(), io1.out()));

        IOHarness io2 = new IOHarness();
        io2.addInputs("100");
        assertEquals(100, InputValidator.getIntInRange(-50, 100, "P", "E", io2.in(), io2.out()));
    }

    @Test
    void throwsWhenLowerGreaterThanUpper() {
        IOHarness io = new IOHarness();
        Executable call = () -> InputValidator.getIntInRange(10, 0, "P", "E", io.in(), io.out());
        assertThrows(IllegalArgumentException.class, call);
    }

    @Test
    void handlesNullInputLineAsInvalidAndReprompts() {
        IOHarness io = new IOHarness();
        // Simulate EOF/Null then a good value
        io.addInputs((String) null, "5");
        int result = InputValidator.getIntInRange(0, 10, "Enter", "Bad", io.in(), io.out());
        assertEquals(5, result);
        assertEquals(List.of("Enter", "Bad", "Enter"), io.outputs);
    }
}


