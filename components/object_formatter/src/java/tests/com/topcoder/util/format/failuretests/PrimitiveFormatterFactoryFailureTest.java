/*
 * PrimitiveFormatterFactoryFailureTest.java
 *
 * Copyright  2002, TopCoder, Inc. All rights reserved
 *
 */

package com.topcoder.util.format.failuretests;

import com.topcoder.util.format.PrimitiveFormatter;
import com.topcoder.util.format.PrimitiveFormatterFactory;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.text.DecimalFormat;


/**
 * <p>A test class for <code>PrimitiveFormatterFactory</code> failures.</P>
 *
 * @author lost
 * @version 1.0
 **/
public class PrimitiveFormatterFactoryFailureTest extends TestCase
{

    /** For use in testing for proper exception throwing */
    private static final String INVALID_NUMBER_FORMAT = "*+-'@$%&&^*SDersdf";

    /**
     * Tests the failure case of attempting to obtain a
     * <code>DateFormatMethod</code> from a <code>FormatMethodFactory</code>
	 * with an invalid date format pattern.
     **/
    public void testFormatMethodFactoryWithInvalidPattern()
    {
        Throwable recievedT = null;
        try
        {
            PrimitiveFormatter pf =
				PrimitiveFormatterFactory.getFormatter(
                    INVALID_NUMBER_FORMAT );
        }
        catch( Throwable t )
        {
            //This should be reached.
            recievedT = t;
        }
        if( recievedT != null )
        {
            assertTrue( "Incorrect Exception: " + recievedT.toString( ),
                recievedT instanceof IllegalArgumentException );
        }
        else
        {
            fail( "An invalid number format was accepted" );
        }

        try
        {
            recievedT = null;
            DecimalFormat df = new DecimalFormat( );
            PrimitiveFormatter pf =
				PrimitiveFormatterFactory.getFormatter(
                    INVALID_NUMBER_FORMAT, df );
        }
        catch( Throwable t )
        {
            //This should be reached.
            recievedT = t;
        }
        if( recievedT != null )
        {
            assertTrue( "Incorrect Exception: " + recievedT.toString( ),
               recievedT instanceof IllegalArgumentException );
        }
        else
        {
            fail( "An invalid number format was accepted" );
        }
    }

    /**
     * Assembles and returns a test suite for
     * all the test methods of this test case.
     *
     * @return A non-null test suite.
     */
    public static Test suite() 
    {
        //
        // Reflection is used here to add all
        // the testXXX() methods to the suite.
        //
        TestSuite suite = new TestSuite( 
            PrimitiveFormatterFactoryFailureTest.class);

        return suite;
    }
}
