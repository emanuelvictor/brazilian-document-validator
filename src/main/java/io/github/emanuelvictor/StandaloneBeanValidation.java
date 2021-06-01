package io.github.emanuelvictor;

import javax.validation.*;
import java.util.Set;

public final class StandaloneBeanValidation {

    /**
     * Valida o bean em relação as suas anotações (faz isso sem precisar ir até o banco e abrir transação)
     *
     * @param bean {T}
     */
    public static <T> void validate(T bean) {

        final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        final Validator validator = factory.getValidator();
        factory.close();

        final Set<ConstraintViolation<T>> violations = validator.validate(bean);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

    }

}
