package com.topcoder.util.commandline;

/**
 * <p>Indicates an invalid argument and holds information.</p>
 * A user-friendly message is provided for the calling application to use.</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TopCoder</p>
 * @author snard6
 * @version 1.0
 */

public class ArgumentValidationException extends java.lang.Exception {
    int switchIndex = -1 ; // which switch had the problem
    String switchName = null ; // the name of the switch
    String argumentValue = null ; // the value passed to the switch
    String validationMessage = null; // the message from the validator

    /**
     * Create a new validation exception. This version is typically used by
     * the validator and caught by the utility, which adds the switch
     * information and re-throws to the calling application.
     * @param argumentValue the value passed to the switch
     * @param validationMessage the message from the validator
     */
    public ArgumentValidationException(String argumentValue,
                                       String validationMessage) {
        this.argumentValue = argumentValue ;
        this.validationMessage = validationMessage ;
    }

    /**
     * Set the switch index.
     * @param switchIndex the index of the switch that had the problem
     */
    public void setSwitchIndex(int switchIndex) {
        this.switchIndex = switchIndex ;
    }

    /**
     * Get the switch index.
     * @return the index of the switch that had the problem
     */
    public int getSwitchIndex() {
        return this.switchIndex ;
    }

    /**
     * Set the switch name.
     * @param switchName the name of the switch
     */
    public void setSwitchName(String switchName) {
        this.switchName = switchName ;
    }

    /**
     * Get the switch name.
     * @return the name of the switch
     */
    public String getSwitchName() {
        return this.switchName ;
    }

    /**
     * Set the argument value passed to the switch.
     * @param argumentValue the argument value
     */
    public void setArgumentValue(String argumentValue) {
        this.argumentValue = argumentValue ;
    }

    /**
     * Gets the argument value passed to the switch.
     * @return the argument value
     */
    public String getArgumentValue() {
        return this.argumentValue ;
    }

    /**
     * Sets the validation message.
     * @param validationMessage the validation message
     */
    public void setValidationMessage(String validationMessage) {
        this.validationMessage = validationMessage ;
    }

    /**
     * Gets the validation message.
     * @return the validation message
     */
    public String getValidationMessage() {
        return this.validationMessage ;
    }

    /**
     * Provides a one line human-readable version of the exception.
     * @return the exception string
     */
    public String toString() {
        StringBuffer buf = new StringBuffer() ;
        if (this.validationMessage != null) {
            buf.append(this.validationMessage) ;
        } else {
            buf.append("Invalid argument.") ;
        }
        if (this.argumentValue != null) {
            buf.append(" Argument: ") ;
            buf.append(this.argumentValue) ;
            buf.append(".") ;
        }
        if (this.switchName != null) {
            buf.append("Switch: ") ;
            buf.append(this.switchName) ;
            buf.append(".") ;
        }
        if (this.switchIndex != -1) {
            buf.append("Argument number: ") ;
            buf.append(this.switchIndex) ;
            buf.append(".") ;
        }
        return buf.toString() ;
    }
}