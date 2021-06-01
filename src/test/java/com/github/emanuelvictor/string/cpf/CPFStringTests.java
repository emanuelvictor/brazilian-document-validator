package com.github.emanuelvictor.string.cpf;

import com.github.emanuelvictor.StandaloneBeanValidation;
import com.github.emanuelvictor.annotations.document.CPF;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolationException;

public class CPFStringTests {

    Entity entity;

    @BeforeEach
    public void beforeEach() {
        entity = new Entity();
    }

    @Test
    public void validateCpfMustPass() {
        entity.setCpf("07074762911");
        StandaloneBeanValidation.validate(entity);
    }

    @Test
    public void validateCpfMustFail() {
        entity.setCpf("07174762911");
        final ConstraintViolationException constraintViolationException = Assertions.assertThrows( ConstraintViolationException.class, () -> StandaloneBeanValidation.validate(entity));
        Assertions.assertEquals("cpf: CPF Inválido!",constraintViolationException.getMessage());
    }

    @Test
    public void validateCpfEligibleForCnpjMustFail() {
        entity.setCpf("21975667000180");
        final ConstraintViolationException constraintViolationException = Assertions.assertThrows( ConstraintViolationException.class, () -> StandaloneBeanValidation.validate(entity));
        Assertions.assertEquals("cpf: CPF Inválido!",constraintViolationException.getMessage());
    }

    @Test
    public void validateDocumentEligibleForCnpjMustPass() {
        entity.setDocument("21975667000180");
        StandaloneBeanValidation.validate(entity);
    }

    @Test
    public void validateDocumentEligibleForCnpjMustFail() {
        entity.setDocument("07174762911");
        final ConstraintViolationException constraintViolationException = Assertions.assertThrows( ConstraintViolationException.class, () -> StandaloneBeanValidation.validate(entity));
        Assertions.assertEquals("document: CPF Inválido!",constraintViolationException.getMessage());
    }

    @Setter
    @Getter
    public static class Entity {

        @CPF
        private String cpf;

        @CPF(ignoreIfIsEligibleForCNPJ = true)
        private String document;

    }
}
