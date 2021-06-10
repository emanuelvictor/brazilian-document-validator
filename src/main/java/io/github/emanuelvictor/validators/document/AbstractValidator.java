package io.github.emanuelvictor.validators.document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public abstract class AbstractValidator {

    /**
     * @param digits int
     * @param input  String
     * @return String
     */
    protected static String format(@NotNull final int digits, final @NotBlank String input) {
        return format(digits, Long.parseLong(input));
    }

    /**
     * @param digits int
     * @param input  Long
     * @return String
     */
    protected static String format(@NotNull final int digits, final @NotBlank Long input) {
        return String.format("%0" + digits + "d", input);
    }

}
