package validations.validators.cef.document;

import br.com.caelum.stella.validation.CNPJValidator;
import br.com.caelum.stella.validation.CPFValidator;
import lombok.Getter;
import lombok.Setter;
import validations.annotations.commons.document.CPF;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class CpfValidator implements ConstraintValidator<CPF, String> {


    private final CNPJValidator cnpjValidator = new CNPJValidator();
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
    public boolean isValid(final String document, final ConstraintValidatorContext ignore) {
        return this.isValid(document);
    }

    /**
     * @param document String
     * @return boolean
     */
    public boolean isValid(final String document) {

        // Prepare document
        final String doc = prepareDocument(document);

        //
        if (doc == null || doc.length() < 1 || Long.parseLong(doc) == 0) {
            return true;
        }

        // Validate to CPF
        if (ignoreIfIsEligibleForCNPJ && cnpjValidator.isEligible(doc))
            return true;

        return cpfValidator.isEligible(doc) && cpfIsValid(doc);

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
        if (document == null) {
            return null;
        }

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

}
