package cpf.string;

import lombok.Getter;
import lombok.Setter;
import org.junit.Before;
import org.junit.Test;
import validations.StandaloneBeanValidation;
import validations.annotations.commons.document.CPF;

public class CpfStringTests {

    Entity entity;

    @Before
    public void beforeEach() {
        entity = new Entity();
    }

    @Test
    public void validateCpfMustPass() {
        entity.setCpf("07074762911");
        StandaloneBeanValidation.validate(entity);
    }

    @Test(expected = javax.validation.ConstraintViolationException.class)
    public void validateCpfMustFail() {
        entity.setCpf("07174762911");
        StandaloneBeanValidation.validate(entity);
    }

    @Test(expected = javax.validation.ConstraintViolationException.class)
    public void validateCpfEligibleForCnpjMustFail() {
        entity.setCpf("21975667000180");
        StandaloneBeanValidation.validate(entity);
    }

    @Test
    public void validateDocumentEligibleForCnpjMustPass() {
        entity.setDocument("21975667000180");
        StandaloneBeanValidation.validate(entity);
    }

    @Test(expected = javax.validation.ConstraintViolationException.class)
    public void validateDocumentEligibleForCnpjMustFail() {
        entity.setDocument("07174762911");
        StandaloneBeanValidation.validate(entity);
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
