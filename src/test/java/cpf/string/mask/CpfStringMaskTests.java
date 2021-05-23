package cpf.string.mask;

import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import validations.StandaloneBeanValidation;
import validations.annotations.document.CPF;

import javax.validation.ConstraintViolationException;

public class CpfStringMaskTests {

    Entity entity;

    @BeforeEach
    public void beforeEach() {
        entity = new Entity();
    }

    @Test
    public void validateCpfMustPass(){
        entity.setCpf("070.747.629-11");
        StandaloneBeanValidation.validate(entity);
    }

    @Test
    public void validateCpfMustFail(){
        entity.setCpf("071.747.629-11");
        final ConstraintViolationException constraintViolationException = Assertions.assertThrows( ConstraintViolationException.class, () -> StandaloneBeanValidation.validate(entity));
        Assertions.assertEquals("cpf: CPF Inválido!",constraintViolationException.getMessage());
    }

    @Test
    public void validateCpfEligibleForCnpjMustFail(){
        entity.setCpf("21.975.667/0001-80");
        final ConstraintViolationException constraintViolationException = Assertions.assertThrows( ConstraintViolationException.class, () -> StandaloneBeanValidation.validate(entity));
        Assertions.assertEquals("cpf: CPF Inválido!",constraintViolationException.getMessage());
    }

    @Test
    public void validateCpfEligibleForCnpjMustPass(){
        entity.setDocument("21.975.667/0001-80");
        StandaloneBeanValidation.validate(entity);
    }

    @Test
    public void validateDocumentEligibleForCnpjMustFail(){
        entity.setDocument("071.747.629-11");
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
