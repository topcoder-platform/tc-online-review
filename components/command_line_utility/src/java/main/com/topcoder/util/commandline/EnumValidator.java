package com.topcoder.util.commandline;

import java.util.Collection;

/**
 * <p>Checks that an argument is an value in a specified Collection.</p>
 * @author snard6
 * @version 1.0
 */

public class EnumValidator implements ArgumentValidator {
    private Collection legalValues = null ; // acceptable values

    /**
     * <p>Creates a default validator that accepts all values.</p>
     */
    public EnumValidator() {
    }

    /**
     * <p>Creates a validator that checks values against a list. If the
     * passed list is null, no values will be accepted.</p>
     * @param legalValues list a Collections of valid values
     */
    public EnumValidator(Collection legalValues) {
        if (legalValues != null) {
            this.legalValues = legalValues ;
        } else {
            this.legalValues = java.util.Collections.EMPTY_LIST ;
        }
    }

    /**
     * <p>Validates that the passed string is in the list of legal values.</p>
     * @param value the value to validate
     * @throws ArgumentValidationException if the argument does not validate
     */
    public void validate(String value) throws ArgumentValidationException {
        if (legalValues == null) {
            return ;
        }
        if (legalValues.contains(value)) {
            return ;
        }
        throw new ArgumentValidationException(value,
                "Argument not found in list of valid values.") ;
    }
}