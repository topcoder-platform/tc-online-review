/*
 * Class Associations 1.0
 *
 * DefaultAssociationAlgorithmTests.java
 *
 * Copyright (c) 2003 TopCoder, Inc.  All rights reserved
 */
package com.topcoder.util.classassociations.failuretests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.util.classassociations.ClassAssociator;
import com.topcoder.util.classassociations.DefaultAssociationAlgorithm;

/**
 * Failure tests for DefaultAssociationAlgorithm Class 
 *
 * @author valeriy
 * @version 1.0
 */
public class DefaultAssociationAlgorithmTests extends TestCase {

    /**
     * Tests DefaultAssociationAlgorithm.retrieveHandler() method with invalid parameters
     */
    public void testDefaultAssociationAlgorithmRetrieveHandler() throws Exception {
        DefaultAssociationAlgorithm test = new DefaultAssociationAlgorithm();           
        try {
            test.retrieveHandler(null, String.class);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
        } catch (Exception e) {
            fail("IllegalArgumentException expected, but was "+e);
        }
        try {
            test.retrieveHandler(new ClassAssociator(), null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
        } catch (Exception e) {
            fail("IllegalArgumentException expected, but was "+e);
        }
        try {
            test.retrieveHandler(null, null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
        } catch (Exception e) {
            fail("IllegalArgumentException expected, but was "+e);
        }
    }
     
    public static Test suite() {
        return new TestSuite(DefaultAssociationAlgorithmTests.class);
    }
    
}
