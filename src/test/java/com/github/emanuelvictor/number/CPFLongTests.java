package com.github.emanuelvictor.number;

import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.github.emanuelvictor.StandaloneBeanValidation;
import com.github.emanuelvictor.annotations.document.CPF;

import javax.validation.ConstraintViolationException;

public class CPFLongTests {

    Entity entity;

    @BeforeEach
    public void beforeEach() {
        entity = new Entity();
    }

    @Test
    public void validateCpfMustPass() {
        entity.setCpf(7074762911L);
        StandaloneBeanValidation.validate(entity);
    }

    @Test
    public void validateCpfMustFail() {
        entity.setCpf(7174762911L);
        final ConstraintViolationException constraintViolationException = Assertions.assertThrows( ConstraintViolationException.class, () -> StandaloneBeanValidation.validate(entity));
        Assertions.assertEquals("cpf: CPF Inválido!",constraintViolationException.getMessage());
    }

    @Test
    public void validateCpfEligibleForCnpjMustFail() {
        entity.setCpf(21975667000180L);
        final ConstraintViolationException constraintViolationException = Assertions.assertThrows( ConstraintViolationException.class, () -> StandaloneBeanValidation.validate(entity));
        Assertions.assertEquals("cpf: CPF Inválido!",constraintViolationException.getMessage());
    }

    @Test
    public void validateDocumentEligibleForCnpjMustPass() {
        entity.setDocument(21975667000180L);
        StandaloneBeanValidation.validate(entity);
    }

    @Test
    public void validateDocumentEligibleForCnpjMustFail() {
        entity.setDocument(7174762911L);
        final ConstraintViolationException constraintViolationException = Assertions.assertThrows( ConstraintViolationException.class, () -> StandaloneBeanValidation.validate(entity));
        Assertions.assertEquals("document: CPF Inválido!",constraintViolationException.getMessage());
    }

    @Setter
    @Getter
    public static class Entity {

        @CPF
        private long cpf;

        @CPF(ignoreIfIsEligibleForCNPJ = true)
        private long document;

    }
}
