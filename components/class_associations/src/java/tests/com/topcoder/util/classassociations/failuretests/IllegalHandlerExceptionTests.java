/*
 * Class Associations 1.0
 *
 * IllegalHandlerExceptionTests.java
 *
 * Copyright (c) 2003 TopCoder, Inc.  All rights reserved
 */
package com.topcoder.util.classassociations.failuretests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.util.classassociations.IllegalHandlerException;

/**
 * Failure tests for IllegalHandlerException Class 
 *
 * @author valeriy
 * @version 1.0
 */
public class IllegalHandlerExceptionTests extends TestCase {

    /**
     * Tests IllegalHandlerException constructors with invalid parameters
     */
    public void testIllegalHandlerExceptionConstructor() throws Exception {
        try {
            IllegalHandlerException test = new IllegalHandlerException(null);           
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
        } catch (Exception e) {
            fail("IllegalArgumentException expected, but was "+e);
        }
    }
     
    public static Test suite() {
        return new TestSuite(IllegalHandlerExceptionTests.class);
    }
    
}
