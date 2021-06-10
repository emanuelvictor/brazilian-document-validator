package io.github.emanuelvictor.number;

import io.github.emanuelvictor.StandaloneBeanValidation;
import io.github.emanuelvictor.annotations.document.CNPJ;
import io.github.emanuelvictor.validators.document.CNPJValidator;
import io.github.emanuelvictor.validators.document.CPFValidator;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import javax.validation.ConstraintViolationException;
import java.util.Arrays;

public class CNPJLongTests {

    Entity entity;

    @BeforeEach
    public void beforeEach() {
        entity = new Entity();
    }

    @Test
    public void allMustBeEligibleAndValids() {
        final Long[] longs = new Long[]{34932555865L,
                24362464000106L,
                18221092134L,
                46523710807L,
                11565708911L,
                11182606601L,
                51466753846L,
                31060566000132L,
                51466753846L,
                22083965000128L,
                27095163000151L,
                80611718000153L,
                41119282268L,
                33728828149L,
                1462450164L,
                32056632845L,
                92817610253L,
                40223135879L,
                32726546315L,
                2326017986L,
                7525375712L,
                79868657253L,
                1973343428L,
                3769134958L,
                6340790186L,
                13054461602L,
                2477809000215L};
        Arrays.stream(longs).forEach(aLong -> {
            Assertions.assertTrue((CNPJValidator.isEligible(aLong) || CPFValidator.isEligible(aLong)));
            Assertions.assertTrue((CNPJValidator.cnpjIsValid(aLong) || CPFValidator.cpfIsValid(aLong)));
        });

        Assertions.assertTrue(Arrays.stream(longs).allMatch(aLong -> CNPJValidator.isEligible(aLong) || CPFValidator.isEligible(aLong)));
        Assertions.assertTrue(Arrays.stream(longs).allMatch(aLong -> CNPJValidator.cnpjIsValid(aLong) || CPFValidator.cpfIsValid(aLong)));
        CNPJValidator.isEligible(2477809000215L);
    }

    @Test
    public void validateCnpjMustPass() {
        entity.setCnpj(21975667000180L);
        StandaloneBeanValidation.validate(entity);
    }

    @Test
    public void validateCnpjMustFail() {
        entity.setCnpj(22975667000180L);
        final ConstraintViolationException constraintViolationException = Assertions.assertThrows(ConstraintViolationException.class, () -> StandaloneBeanValidation.validate(entity));
        Assertions.assertEquals("cnpj: CNPJ Inválido!", constraintViolationException.getMessage());
    }

    @Test
    public void validateCnpjEligibleForCpfMustFail() {
        entity.setCnpj(7074762911L);
        final ConstraintViolationException constraintViolationException = Assertions.assertThrows(ConstraintViolationException.class, () -> StandaloneBeanValidation.validate(entity));
        Assertions.assertEquals("cnpj: CNPJ Inválido!", constraintViolationException.getMessage());
    }

    @Test
    public void validateCnpjEligibleForCpfMustPass() {
        entity.setDocument(7074762911L);
        StandaloneBeanValidation.validate(entity);
    }

    @Test
    public void validateDocumentEligibleForCpfMustFail() {
        entity.setDocument(22975667000180L);
        final ConstraintViolationException constraintViolationException = Assertions.assertThrows(ConstraintViolationException.class, () -> StandaloneBeanValidation.validate(entity));
        Assertions.assertEquals("document: CNPJ Inválido!", constraintViolationException.getMessage());
    }

    @Setter
    @Getter
    public static class Entity {

        @CNPJ
        private long cnpj;

        @CNPJ(ignoreIfIsEligibleForCPF = true)
        private long document;

    }
}
