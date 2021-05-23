package cnpj.string.mask;

import lombok.Getter;
import lombok.Setter;
import org.junit.Before;
import org.junit.Test;
import validations.StandaloneBeanValidation;
import validations.annotations.commons.document.CNPJ;

public class CnpjStringMaskTests {

    Entity entity;

    @Before
    public void beforeEach() {
        entity = new Entity();
    }

    @Test
    public void validateCnpjMustPass(){
        entity.setCnpj("21.975.667/0001-80");
        StandaloneBeanValidation.validate(entity);
    }

    @Test(expected = javax.validation.ConstraintViolationException.class)
    public void validateCnpjMustFail(){
        entity.setCnpj("22.975.667/0001-80");
        StandaloneBeanValidation.validate(entity);
    }

    @Test(expected = javax.validation.ConstraintViolationException.class)
    public void validateCnpjEligibleForCpfMustFail(){
        entity.setCnpj("070.747.629-11");
        StandaloneBeanValidation.validate(entity);
    }

    @Test
    public void validateCnpjEligibleForCpfMustPass(){
        entity.setDocument("070.747.629-11");
        StandaloneBeanValidation.validate(entity);
    }

    @Test(expected = javax.validation.ConstraintViolationException.class)
    public void validateDocumentEligibleForCpfMustFail(){
        entity.setDocument("22.975.667/0001-80");
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
