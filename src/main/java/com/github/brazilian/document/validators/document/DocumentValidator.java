package com.github.brazilian.document.validators.document;


import com.github.brazilian.document.annotations.document.Document;
import lombok.Getter;
import lombok.Setter;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;


public class DocumentValidator implements ConstraintValidator<Document, Object> {

    /**
     *
     */
    @Getter
    @Setter
    private boolean ignoreIfIsEligibleForCPF = true;

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
    public void initialize(final Document constraintAnnotation) {
        setIgnoreIfIsEligibleForCPF(constraintAnnotation.ignoreIfIsEligibleForCPF());
        setIgnoreIfIsEligibleForCNPJ(constraintAnnotation.ignoreIfIsEligibleForCNPJ());
    }

    /**
     * @param document String
     * @param ignore   ConstraintValidatorContext
     * @return boolean
     */
    @Override
    public boolean isValid(final Object document, final ConstraintValidatorContext ignore) {

        // Prepare document
        final String doc;
        if (document instanceof String)
            doc = prepareDocument((String) document);
        else doc = prepareDocument(String.valueOf(document));

        //
        if (doc == null || doc.length() < 1 || Long.parseLong(doc) == 0)
            return true;

        if (ignoreIfIsEligibleForCPF && CPFValidator.isEligible(doc))
            return true;
        else if (ignoreIfIsEligibleForCNPJ && CNPJValidator.isEligible(doc))
            return true;
        else if (ignoreIfIsEligibleForCPF && !CPFValidator.isEligible(doc))
            return CNPJValidator.cnpjIsValid(doc);
        else if (ignoreIfIsEligibleForCNPJ && !CNPJValidator.isEligible(doc))
            return CPFValidator.cpfIsValid(doc);
        else if (!ignoreIfIsEligibleForCNPJ && !ignoreIfIsEligibleForCPF) {
            if (CPFValidator.isEligible(doc))
                return CPFValidator.cpfIsValid(doc);
            else if (CNPJValidator.isEligible(doc))
                return CNPJValidator.cnpjIsValid(doc);
        }
        return true;
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

        document = document.replaceAll(Pattern.quote("."), "");
        document = document.replaceAll(Pattern.quote("/"), "");
        document = document.replaceAll(Pattern.quote("-"), "");

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
