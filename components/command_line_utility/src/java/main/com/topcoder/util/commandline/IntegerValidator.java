package com.topcoder.util.commandline;

/**
 * <p>Checks that an argument is an integer within a specified range.</p>
 * @author snard6
 * @version 1.0
 */

public class IntegerValidator implements ArgumentValidator {
    private Integer minimum = null ; // minimum value (default is none)
    private Integer maximum = null ; // maximum value (default is none)

    /**
     * <p>Creates a default validator that does not range check.</p>
     */
    public IntegerValidator() {
    }

    /**
     * <p>Creates a validator that does range checking.</p>
     * @param minimum the minimum allowable value (null = none)
     * @param maximum the maximum allowable value (null = none)
     */
    public IntegerValidator(Integer minimum, Integer maximum) {
        this.minimum = minimum ;
        this.maximum = maximum ;
    }

    /**
     * <p>Validates that the passed string is an integer within the range
     * specified at creation.</p>
     * @param value the value to validate
     * @throws ArgumentValidationException if the argument does not validate
     */
    public void validate(String value) throws ArgumentValidationException {
        int intValue ;

        try {
            intValue = Integer.parseInt(value) ;
        } catch (NumberFormatException e) {
            throw new ArgumentValidationException(value,
                    "Argument not an integer.") ;
        }

        if (this.minimum != null) {
            if (intValue < this.minimum.intValue()) {
                throw new ArgumentValidationException(value,
                        "Argument less than minimum value (" + this.minimum +
                        ")") ;
            }
        }
        if (this.maximum != null) {
            if (intValue > this.maximum.intValue()) {
                throw new ArgumentValidationException(value,
                        "Argument greater than maximum value (" + this.maximum +
                        ")") ;
            }
        }
    }
}