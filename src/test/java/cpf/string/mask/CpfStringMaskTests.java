package cpf.string.mask;

import cpf.string.CpfStringTests;
import lombok.Getter;
import lombok.Setter;
import org.junit.Before;
import org.junit.Test;
import validations.StandaloneBeanValidation;
import validations.annotations.commons.document.CPF;

public class CpfStringMaskTests {

    Entity entity;

    @Before
    public void beforeEach() {
        entity = new Entity();
    }

    @Test
    public void validateCpfMustPass(){
        entity.setCpf("070.747.629-11");
        StandaloneBeanValidation.validate(entity);
    }

    @Test(expected = javax.validation.ConstraintViolationException.class)
    public void validateCpfMustFail(){
        entity.setCpf("071.747.629-11");
        StandaloneBeanValidation.validate(entity);
    }

    @Test(expected = javax.validation.ConstraintViolationException.class)
    public void validateCpfEligibleForCnpjMustFail(){
        entity.setCpf("21.975.667/0001-80");
        StandaloneBeanValidation.validate(entity);
    }

    @Test
    public void validateCpfEligibleForCnpjMustPass(){
        entity.setDocument("21.975.667/0001-80");
        StandaloneBeanValidation.validate(entity);
    }

    @Test(expected = javax.validation.ConstraintViolationException.class)
    public void validateDocumentEligibleForCnpjMustFail(){
        entity.setDocument("071.747.629-11");
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
