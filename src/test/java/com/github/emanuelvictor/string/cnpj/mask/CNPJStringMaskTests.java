package com.github.emanuelvictor.string.cnpj.mask;

import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.github.emanuelvictor.StandaloneBeanValidation;
import com.github.emanuelvictor.annotations.document.CNPJ;

import javax.validation.ConstraintViolationException;

public class CNPJStringMaskTests {

    Entity entity;

    @BeforeEach
    public void beforeEach() {
        entity = new Entity();
    }

    @Test
    public void validateCnpjMustPass(){
        entity.setCnpj("21.975.667/0001-80");
        StandaloneBeanValidation.validate(entity);
    }

    @Test
    public void validateCnpjMustFail(){
        entity.setCnpj("22.975.667/0001-80");
        final ConstraintViolationException constraintViolationException = Assertions.assertThrows( ConstraintViolationException.class, () -> StandaloneBeanValidation.validate(entity));
        Assertions.assertEquals("cnpj: CNPJ Inválido!",constraintViolationException.getMessage());
    }

    @Test
    public void validateCnpjEligibleForCpfMustFail(){
        entity.setCnpj("070.747.629-11");
        final ConstraintViolationException constraintViolationException = Assertions.assertThrows( ConstraintViolationException.class, () -> StandaloneBeanValidation.validate(entity));
        Assertions.assertEquals("cnpj: CNPJ Inválido!",constraintViolationException.getMessage());
    }

    @Test
    public void validateCnpjEligibleForCpfMustPass(){
        entity.setDocument("070.747.629-11");
        StandaloneBeanValidation.validate(entity);
    }

    @Test
    public void validateDocumentEligibleForCpfMustFail(){
        entity.setDocument("22.975.667/0001-80");
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
