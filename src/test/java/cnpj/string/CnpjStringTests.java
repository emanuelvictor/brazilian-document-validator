package cnpj.string;

import lombok.Getter;
import lombok.Setter;
import org.junit.Before;
import org.junit.Test;
import validations.StandaloneBeanValidation;
import validations.annotations.commons.document.CNPJ;
import validations.annotations.commons.document.CPF;

public class CnpjStringTests {

    Entity entity;

    @Before
    public void beforeEach() {
        entity = new Entity();
    }

    @Test
    public void validateCnpjMustPass(){
        entity.setCnpj("21975667000180");
        StandaloneBeanValidation.validate(entity);
    }

    @Test(expected = javax.validation.ConstraintViolationException.class)
    public void validateCnpjMustFail(){
        entity.setCnpj("22975667000180");
        StandaloneBeanValidation.validate(entity);
    }

    @Test(expected = javax.validation.ConstraintViolationException.class)
    public void validateCnpjEligibleForCpfMustFail(){
        entity.setCnpj("07074762911");
        StandaloneBeanValidation.validate(entity);
    }

    @Test
    public void validateCnpjEligibleForCpfMustPass(){
        entity.setDocument("07074762911");
        StandaloneBeanValidation.validate(entity);
    }

    @Test(expected = javax.validation.ConstraintViolationException.class)
    public void validateDocumentEligibleForCpfMustFail(){
        entity.setDocument("22975667000180");
        StandaloneBeanValidation.validate(entity);
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
