package com.topcoder.util.commandline;

import java.util.List;
import java.util.ArrayList ;

/**
 * <p>Describes a command line switch and its validation rules.</p>
 * <p>When a switch is invalid, it will either stop processing by throwing
 *    an exception or store the error and allow processing to continue. This
 *    behavior allows the application to ignore bad parameters if it so
 *    chooses.</p>
 * <p>Important note: Implementation may not guarantee correct processing of
 *    switches with varying numbers of arguments when an argument starts with
 *    a dash. For instance, take the line
 *    "<code>java app -F -D dir</code>". If -F can have zero or one argument,
 *    is "-D" an argument to -F or is it a new switch?</p>
 * <p>To resolve this, arguments that may have switch-like values should be
 *    declared with a fixed number of arguments. (Implementation could provide
 *    an escape for this as well)</p>
 * @author snard6
 * @version 1.0
 */

public class Switch {
    private String name ; // the name of the switch
    private boolean isRequired ; // is this switch required?
    private int minArgs = 0 ; // minimum arguments this switch accepts (default = 0)
    private int maxArgs = -1 ; // maximum arguments this switch accepts (default = no maximum)
    private ArgumentValidator validator = null ; // the validator to use
    private List values = null ; // holds values once populated
    private List errors = null ; // holds validation exceptions if ignoreErrors
    private int switchIndex = -1 ; // switch index once populated
    private String usage = "" ; // usage information

    /**
     * <p>Creates a new Switch with a given name. Allows for validation
     *    of the number and type of arguments.</p>
     * <p>To accept any number of arguments, set maxArgs to -1. The validator may
     *    be null, in which case no validation will be performed.</p>
     * @param name the name of the switch (eg "threads" in -threads 15)
     * @param minArgs the minimum number of arguments to accept
     * @param maxArgs the maximum number of arguments to accept (-1 = any)
     * @param validator an argumentValidator (null may be passed)
     * @throws IllegalSwitchException when minimum arguments are greater than maximum arguments
     */
    public Switch(String name, boolean isRequired, int minArgs, int maxArgs,
                  ArgumentValidator validator)  throws IllegalSwitchException {
        try {
            Integer.parseInt(name);
            throw new IllegalSwitchException(this, "Switches cannot be numbers");
        } catch (NumberFormatException e) {
            //we're safe
        }
        this.name = name ;
        this.isRequired = isRequired ;
        this.minArgs = minArgs ;
        this.maxArgs = maxArgs ;
        if (this.minArgs > this.maxArgs && this.minArgs != 0 
                && this.maxArgs != -1) {
            throw new IllegalSwitchException(this, "Minimum number of arguments greater than maximum number of arguments") ;
        }
        this.validator = validator ;
        this.errors = new ArrayList() ;
    }

    /**
     * <p>Creates a new Switch with a given name. Allows for validation
     *    of the number and type of arguments.</p>
     * <p>To accept any number of arguments, set maxArgs to -1. The validator may
     *    be null, in which case no validation will be performed.</p>
     * @param name the name of the switch (eg "threads" in -threads 15)
     * @param minArgs the minimum number of arguments to accept
     * @param maxArgs the maximum number of arguments to accept (-1 = any)
     * @param validator an argumentValidator (null may be passed)
     * @param usage a short description of valid arguments for the switch
     * @throws IllegalSwitchException when minimum arguments are greater than maximum arguments
    */
    public Switch(String name, boolean isRequired, int minArgs, int maxArgs,
                  ArgumentValidator validator, String usage) throws IllegalSwitchException {
        try {
            Integer.parseInt(name);
            throw new IllegalSwitchException(this, "Switches cannot be numbers");
        } catch (NumberFormatException e) {
            //we're safe
        }
        this.name = name ;
        this.isRequired = isRequired ;
        this.minArgs = minArgs ;
        this.maxArgs = maxArgs ;
        if (this.minArgs > this.maxArgs && this.minArgs != 0 
                && this.maxArgs != -1) {
            throw new IllegalSwitchException(this, "Minimum number of arguments greater than maximum number of arguments") ;
        }
        this.validator = validator ;
        this.errors = new ArrayList() ;
        this.usage = usage ;
    }

    /**
     * <p>Checks the validity of this switch. A switch is valid if it
     * <li>has no errors, and
     * <li>has a value (if required).</p>
     * @return true if valid, false if invalid
     */
    public boolean isValid() {
        if (this.errors.size() > 0) {
            return false ;
        }
        if (this.isRequired && this.minArgs != 0 && this.values == null) {
            return false ;
        }
        return true ;
    }
    
    /**
     * <p>Gets the errors that occurred during validation. An empty list will
     *    be returned if no errors occurred.</p>
     * @return a List of ArgumentValidationExceptions
     */
    public List getErrors() {
        return this.errors ;
    }

    /**
     * <p>Gets a list of the values for this parameter.</p>
     * @return a List of values (as Strings)
     */
    public List getValues() {
        return this.values ;
    }

    /**
     * <p>Gets value for this switch (or first value if multiple).</p>
     * @return the value of this switch
     */
    public String getValue() {
        if (this.values != null && this.values.size() > 0) {
            return (String)this.values.get(0);
        } else {
            return null ;
        }
    }

    /**
     * <p>Gets the name of this switch.</p>
     * @return the name of the switch
     */
    public String getName() {
        return this.name ;
    }

    /**
     * <p>Gets the usage information for this switch.</p>
     * @return the usage of the switch
     */
    public String getUsage() {
        return this.usage ;
    }

    /**
     * <p>Gets the minimum number of arguments.</p>
     * @return the minimum number of arguments (0 = no minimum)
     */
    public int getMinimumArguments() {
        return this.minArgs ;
    }

    /**
     * <p>Gets the maximum number of arguments.</p>
     * @return the maximum number of arguments (-1 = no maximum)
     */
    public int getMaximumArguments() {
        return this.maxArgs ;
    }

    /**
     * <p>Sets the value of this 1-argument switch based upon user input.</p>
     * <p>Note: Invalid arguments are stored if ignoreErrors is true
     *    (default is false)</p>
     * @param value the value passed
     * @param switchIndex the switch number, provided for use in error messages
     *          and debugging.
     * @param ignoreErrors true = ignore errors ; false = throw exceptions
     * @throws ArgumentValidationException if argument cannot be validated
     */
    public void setValue(String value, int switchIndex, boolean ignoreErrors)
            throws ArgumentValidationException {
        if (value == null) {
            return ;
        }
        this.switchIndex = switchIndex ;
        this.values = new ArrayList() ;
        values.add(value);
        if (this.validator != null) {
            try {
                validator.validate(value);
            } catch (ArgumentValidationException e) {
                e.setSwitchIndex(this.switchIndex);
                e.setSwitchName(this.name);
                if (ignoreErrors) {
                    errors.add(e) ;
                } else {
                    throw e ;
                }
            }
        }
    }

    /**
     * <p>Sets the value of this multi-argument switch based upon user input.</p>
     * <p>Note: Invalid arguments are stored if ignoreErrors is true
     *    (default is false)</p>
     *
     * @param values a List of Strings representing the values passed
     * @param switchNumber the switch number, provided for use in error messages and debugging.
     * @param ignoreErrors true = ignore errors ; false = throw exceptions
     * @throws ArgumentValidationException if argument cannot be validated
     */

    public void setValues(List values, int switchNumber, boolean ignoreErrors)
            throws ArgumentValidationException {
        if (values == null) {
            return ;
        }
        this.switchIndex = switchNumber ;
        this.values = new ArrayList() ;
        for (int i = 0 ; i < values.size() ; i++) {
            String value = (String)values.get(i) ;
            this.values.add(value);

            if (this.validator != null) {
                try {
                    validator.validate(value);
                } catch (ArgumentValidationException e) {
                    e.setSwitchIndex(this.switchIndex);
                    e.setSwitchName(this.name);
                    if (ignoreErrors) {
                        errors.add(e) ;
                    } else {
                        throw e ;
                    }
                }
            }
        }
    }
}