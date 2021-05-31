package com.github.emanuelvictor.document.string.cnpj;

import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.github.emanuelvictor.document.StandaloneBeanValidation;
import com.github.emanuelvictor.document.annotations.document.CNPJ;

import javax.validation.ConstraintViolationException;

public class CNPJStringTests {

    Entity entity;

    @BeforeEach
    public void beforeEach() {
        entity = new Entity();
    }

    @Test
    public void validateCnpjMustPass(){
        entity.setCnpj("21975667000180");
        StandaloneBeanValidation.validate(entity);
    }

    @Test
    public void validateCnpjMustFail(){
        entity.setCnpj("22975667000180");
        final ConstraintViolationException constraintViolationException = Assertions.assertThrows( ConstraintViolationException.class, () -> StandaloneBeanValidation.validate(entity));
        Assertions.assertEquals("cnpj: CNPJ Inválido!",constraintViolationException.getMessage());}

    @Test
    public void validateCnpjEligibleForCpfMustFail(){
        entity.setCnpj("07074762911");
        final ConstraintViolationException constraintViolationException = Assertions.assertThrows( ConstraintViolationException.class, () -> StandaloneBeanValidation.validate(entity));
        Assertions.assertEquals("cnpj: CNPJ Inválido!",constraintViolationException.getMessage());
    }

    @Test
    public void validateCnpjEligibleForCpfMustPass(){
        entity.setDocument("07074762911");
        StandaloneBeanValidation.validate(entity);
    }

    @Test
    public void validateDocumentEligibleForCpfMustFail(){
        entity.setDocument("22975667000180");
        final ConstraintViolationException constraintViolationException = Assertions.assertThrows( ConstraintViolationException.class, () -> StandaloneBeanValidation.validate(entity));
        Assertions.assertEquals("document: CNPJ Inválido!",constraintViolationException.getMessage());
    }

    @Setter
    @Getter
    public static class Entity {

        @CNPJ
        private String cnpj;

        @CNPJ(ignoreIfIsEligibleForCPF = true)
        private String document;

    }
}
