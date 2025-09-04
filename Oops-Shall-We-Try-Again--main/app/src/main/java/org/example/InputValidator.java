package org.example;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;


public class InputValidator {

public static int getIntInRange(
    int low,
    int high,
    String prompt,
    String error,
    Supplier<String> in,
    Consumer<String> out
) {
    if (low > high) {
        throw new IllegalArgumentException("Low bound cannot be greater than high bound.");
    }
    Objects.requireNonNull(prompt, "prompt");
        Objects.requireNonNull(error, "error");
        Objects.requireNonNull(in, "in");
        Objects.requireNonNull(out, "out");

        while (true) {
            out.accept(prompt);

            String line = in.get();
            if (line == null) {
                out.accept(error);
                continue;
            }

            line = line.trim();
            try {
                int value = Integer.parseInt(line);
                if (value >= low && value <= high) {
                    return value;
                } else {
                    out.accept(error);
                }
            } catch (NumberFormatException nfe) {
                out.accept(error);
            }
        }
    }
}

