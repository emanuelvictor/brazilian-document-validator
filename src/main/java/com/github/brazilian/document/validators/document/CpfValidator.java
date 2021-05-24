package com.github.brazilian.document.validators.document;

import br.com.caelum.stella.validation.CPFValidator;
import com.github.brazilian.document.annotations.document.CPF;
import lombok.Getter;
import lombok.Setter;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;


public class CpfValidator implements ConstraintValidator<CPF, Object> {

    public static final Pattern UNFORMATTED = Pattern.compile("(\\d{3})(\\d{3})(\\d{3})(\\d{2})");

    private final CPFValidator cpfValidator = new CPFValidator();

    /**
     *
     */
    @Getter
    @Setter
    private boolean ignoreIfIsEligibleForCNPJ = true;


    /**
     * @param constraintAnnotation Document
     */
    @Override
    public void initialize(final CPF constraintAnnotation) {
        setIgnoreIfIsEligibleForCNPJ(constraintAnnotation.ignoreIfIsEligibleForCNPJ());
    }

    /**
     * @param document String
     * @param ignore   ConstraintValidatorContext
     * @return boolean
     */
    @Override
    public boolean isValid(final Object document, final ConstraintValidatorContext ignore) {
        if (document instanceof String)
            return this.isValid((String) document);
        else return this.isValid(String.valueOf(document));
    }

    /**
     * @param document String
     * @return boolean
     */
    public boolean isValid(final String document) {

        // Prepare document
        final String doc = prepareDocument(document);

        //
        if (doc == null || doc.length() < 1 || Long.parseLong(doc) == 0)
            return true;

        // Validate to CPF
        if (ignoreIfIsEligibleForCNPJ && CnpjValidator.isEligible(doc))
            return true;

        return isEligible(doc) && cpfIsValid(doc);

    }

    /**
     * Validate CPF
     *
     * @param document String
     * @return boolean
     */
    public boolean cpfIsValid(final String document) {
        try {
            cpfValidator.assertValid(document);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Remove '.', '/' e '-'
     *
     * @param document {String}
     * @return {String}
     */
    private static String prepareDocument(String document) {
        if (document == null)
            return null;

        document = document.replaceAll(java.util.regex.Pattern.quote("."), "");
        document = document.replaceAll(java.util.regex.Pattern.quote("/"), "");
        document = document.replaceAll(java.util.regex.Pattern.quote("-"), "");

        // Remove zeros in the left
        try {
            document = String.valueOf(Long.parseLong(document));
        } catch (final Exception ignore) {
            return null;
        }

        if (document.length() < 11)
            return String.format("%0" + 11 + "d", Long.parseLong(document));
        else
            return document;
    }

    /**
     * @param value String
     * @return boolean
     */
    public static boolean isEligible(final String value) {
        if (value == null) {
            return false;
        } else {
            return UNFORMATTED.matcher(value).matches();
        }
    }

}
