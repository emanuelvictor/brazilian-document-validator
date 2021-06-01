package io.github.emanuelvictor.annotations.document;


import io.github.emanuelvictor.validators.document.CNPJValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Repeatable(CNPJ.List.class)
@Documented
@Constraint(validatedBy = {CNPJValidator.class})
public @interface CNPJ {

    boolean ignoreIfIsEligibleForCPF() default false;

    String message() default "CNPJ Inválido!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        CNPJ[] value();
    }
}
