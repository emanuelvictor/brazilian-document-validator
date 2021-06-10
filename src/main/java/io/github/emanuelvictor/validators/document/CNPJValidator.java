package io.github.emanuelvictor.validators.document;


import io.github.emanuelvictor.annotations.document.CNPJ;
import lombok.Getter;
import lombok.Setter;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.regex.Pattern;


public class CNPJValidator implements ConstraintValidator<CNPJ, Object> {

    private static final int DOCUMENT_LENGTH = 14;

    public static final Pattern UNFORMATTED = Pattern.compile("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})");

    private final static br.com.caelum.stella.validation.CNPJValidator cnpjValidator = new br.com.caelum.stella.validation.CNPJValidator();

    /**
     *
     */
    @Getter
    @Setter
    private boolean ignoreIfIsEligibleForCPF = true;


    /**
     * @param constraintAnnotation Document
     */
    @Override
    public void initialize(final CNPJ constraintAnnotation) {
        setIgnoreIfIsEligibleForCPF(constraintAnnotation.ignoreIfIsEligibleForCPF());
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
        if (ignoreIfIsEligibleForCPF && CPFValidator.isEligible(doc))
            return true;

        return isEligible(doc) && cnpjIsValid(doc);

    }

    /**
     * Validate CNPJ
     *
     * @param document String
     * @return boolean
     */
    public static boolean cnpjIsValid(final String document) {
        try {
            cnpjValidator.assertValid(document);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Validate CNPJ
     *
     * @param document Long
     * @return boolean
     */
    public static boolean cnpjIsValid(final Long document) {
        return cnpjIsValid(format(document));
    }

    /**
     * @param digits int
     * @param input  String
     * @return String
     */
    protected static String format(@NotNull final int digits, final @NotBlank String input) {
        return format(digits, Long.parseLong(input));
    }

    /**
     * @param digits int
     * @param input  Long
     * @return String
     */
    protected static String format(@NotNull final int digits, final @NotBlank Long input) {
        return String.format("%0" + digits + "d", input);
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

        return format(document);
    }

    /**
     * @param document String
     * @return boolean
     */
    public static boolean isEligible(final String document) {
        if (document == null)
            return false;

// If you complete one CPF document with left zeros like a CNPJ, it will be eligible for CNPJ.
// The rule is: If is eligible for CPF, cannot be eligible for CNPJ.
            return !CPFValidator.isEligible(document) && UNFORMATTED.matcher(format(document)).matches();
    }

    /**
     * @param input Long
     * @return String
     */
    private static String format(final @NotNull Long input) {
        return format(DOCUMENT_LENGTH, input);
    }

    /**
     * @param input String
     * @return String
     */
    private static String format(final @NotBlank String input) {
        return format(DOCUMENT_LENGTH, input);
    }

    /**
     * @param value Long
     * @return boolean
     */
    public static boolean isEligible(final Long value) {
        return isEligible(format(value));
    }

}
