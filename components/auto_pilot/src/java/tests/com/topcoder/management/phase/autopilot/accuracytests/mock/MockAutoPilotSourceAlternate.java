/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.phase.autopilot.accuracytests.mock;

/**
 * <p></p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class MockAutoPilotSourceAlternate extends MockAutoPilotSource {


    /**
     * <p>Checks the equality of this object to specified one.</p>
     *
     * @param obj the reference object with which to compare.
     * @return <code>true</code> if this object is the same as the obj argument; <code>false</code> otherwise.
     */
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        return this.getClass().getName().equals(obj.getClass().getName());
    }

}
