package com.github.brazilian.document.string.document;

import com.github.brazilian.document.annotations.document.Document;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.github.brazilian.document.StandaloneBeanValidation;
import com.github.brazilian.document.annotations.document.CNPJ;
import com.github.brazilian.document.annotations.document.CPF;

import javax.validation.ConstraintViolationException;

public class DocumentStringTests {

    Entity entity;

    @BeforeEach
    public void beforeEach() {
        entity = new Entity();
    }

    @Test
    public void validateDocumentMustPass(){
        entity.setDocument("07074762911");
        StandaloneBeanValidation.validate(entity);
        entity.setDocument("21975667000180");
        StandaloneBeanValidation.validate(entity);
//        entity.setDocument("07074762914");
//        StandaloneBeanValidation.validate(entity);
//        entity.setDocument("21975667000182");
//        StandaloneBeanValidation.validate(entity);

        entity.setCpf("07074762912");
        StandaloneBeanValidation.validate(entity);
    }

    @Test
    public void validateDocumentWithCnpjInvalidMustFail(){
        entity.setDocument("22975667000181");
        javax.validation.ConstraintViolationException constraintViolationException = Assertions.assertThrows(ConstraintViolationException.class, () -> {
            StandaloneBeanValidation.validate(entity);
        });
        Assertions.assertEquals("document: Invalid CNPJ!", constraintViolationException.getMessage());
    }

    @Setter
    @Getter
    public static class Entity {

        @Document(ignoreIfIsEligibleForCNPJ = true, message = "Invalid CPF!")
        @Document(ignoreIfIsEligibleForCPF = true, message = "Invalid CNPJ!")
        private String document;

        @Document(message = "Invalid CPF!")
        private String cpf;

    }
}
