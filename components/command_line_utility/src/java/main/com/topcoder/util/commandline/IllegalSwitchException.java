package com.topcoder.util.commandline;

/**
 * <p>Thrown when a switch is defined incorrectly.</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TopCoder</p>
 * @author snard6
 * @version 1.0
 */

public class IllegalSwitchException extends Exception {
    private Switch cswitch ;  // the switch that caused the exception
    private String reason ;  // the reason that the switch is illegal

    /**
     * <p>Creates a new IllegalSwitchException.</p>
     * @param s the illegal switch
     * @param reason the reason the switch is illegal
     */
    public IllegalSwitchException(Switch s, String reason) {
        cswitch = s ;
        this.reason = reason ;
    }

    public String toString() {
        return "Switch " + cswitch.getName() + " was invalid because: " + reason ;
    }
}