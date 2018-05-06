/**
 *
 * Copyright © 2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.classassociations;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>This test case aggregates all Unit test cases.</p>
 *
 * @author TopCoder
 * @version 1.0
 */
public class UnitTests extends TestCase {

    public static Test suite() {
        final TestSuite suite = new TestSuite();

        // IllegalHandlerException test case
        suite.addTest(TestIllegalHandlerException.suite());

        // ClassAssociator
        suite.addTest(TestClassAssociator.suite());

        // ClassAssociator Methods
        suite.addTest(TestClassAssociatorMethods.suite());

        // DefaultAssociationAlgorithm
        suite.addTest(TestDefaultAssociationAlgorithm.suite());


        // Test Component Spec Demo code
        suite.addTest(TestDemo.suite());

        return suite;
    }

}
