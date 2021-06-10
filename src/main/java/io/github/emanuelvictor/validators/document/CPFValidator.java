package io.github.emanuelvictor.validators.document;

import io.github.emanuelvictor.annotations.document.CPF;
import lombok.Getter;
import lombok.Setter;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.regex.Pattern;


public class CPFValidator extends AbstractValidator implements ConstraintValidator<CPF, Object> {

    private static final int DOCUMENT_LENGTH = 11;

    public static final Pattern UNFORMATTED = Pattern.compile("(\\d{3})(\\d{3})(\\d{3})(\\d{2})");

    private final static br.com.caelum.stella.validation.CPFValidator cpfValidator = new br.com.caelum.stella.validation.CPFValidator();

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
        if (ignoreIfIsEligibleForCNPJ && CNPJValidator.isEligible(doc))
            return true;

        return isEligible(doc) && cpfIsValid(doc);

    }

    /**
     * Validate CPF
     *
     * @param document String
     * @return boolean
     */
    public static boolean cpfIsValid(final String document) {
        try {
            cpfValidator.assertValid(document);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Validate CPF
     *
     * @param document Long
     * @return boolean
     */
    public static boolean cpfIsValid(final Long document) {
        return cpfIsValid(format(document));
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
        return UNFORMATTED.matcher(format(document)).matches();
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
