package validations.validators.document;


import br.com.caelum.stella.validation.CNPJValidator;
import br.com.caelum.stella.validation.CPFValidator;
import lombok.Getter;
import lombok.Setter;
import validations.annotations.document.CNPJ;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;


public class CnpjValidator implements ConstraintValidator<CNPJ, Object> {

    public static final Pattern UNFORMATTED = Pattern.compile("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})");

    private final CNPJValidator cnpjValidator = new CNPJValidator();

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
        if (ignoreIfIsEligibleForCPF && CpfValidator.isEligible(doc))
            return true;

        return isEligible(doc) && cnpjIsValid(doc);

    }

    /**
     * Validate CNPJ
     *
     * @param document String
     * @return boolean
     */
    public boolean cnpjIsValid(final String document) {
        try {
            cnpjValidator.assertValid(document);
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
