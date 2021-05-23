package document.string;

import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import validations.StandaloneBeanValidation;
import validations.annotations.commons.document.CNPJ;
import validations.annotations.commons.document.CPF;

import javax.validation.ConstraintViolationException;

public class CnpjStringTests {

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

        @CPF(ignoreIfIsEligibleForCNPJ = true, message = "Invalid CPF!")
        @CNPJ(ignoreIfIsEligibleForCPF = true, message = "Invalid CNPJ!")
        private String document;

    }
}