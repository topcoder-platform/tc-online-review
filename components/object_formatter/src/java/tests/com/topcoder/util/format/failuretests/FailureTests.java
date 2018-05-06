/*
 * AllTests.java
 *
 * Copyright © 2002, TopCoder, Inc. All rights reserved
 *
 */

package com.topcoder.util.format.failuretests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * This test class will call each failure test suite for the ObjectFormatter
 * Component.<p>
 *
 * @author garyk
 * @version 1.0
 **/
public class FailureTests extends TestCase 
{
    
    public FailureTests( String str ) 
    { 
        super(str); 
    }
    
    /**
     * Assembles and returns a test suite
     * containing all known tests.
     *
     * New tests should be added here!
     *
     * @return A non-null test suite.
     */
    public static Test suite() 
    {
        TestSuite suite = new TestSuite();    

        // test suite of FormatMetodFactory failure tests
        suite.addTest( FormatMethodFactoryFailureTest.suite( ) );

        // test suite of FormatMetodFactorys' ObjectFormatMethod failure tests
        suite.addTest( ObjectFormatMethodFailureTest.suite( ) );

        // test suite of ObjectFormatter failure tests
        suite.addTest( ObjectFormatterFailureTest.suite( ) );

        // test suite of PrimitiveFormatterFactory failure tests
        suite.addTest( PrimitiveFormatterFactoryFailureTest.suite( ) );

        return suite;
    }
}
