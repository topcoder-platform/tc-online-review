/*
 * ObjectFormatMethodFailureTest.java
 *
 * Copyright  2002, TopCoder, Inc. All rights reserved
 *
 */

package com.topcoder.util.format.failuretests;

import com.topcoder.util.format.FormatMethodFactory;
import com.topcoder.util.format.ObjectFormatMethod;
import com.topcoder.util.format.ObjectFormatter;
import com.topcoder.util.format.ObjectFormatterFactory;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * <p>A test class for failures on <code>ObjectFormatMethodFailure</code>s
 * obtained from the <code>FormatMethodFactory</code>.</P>
 *
 * @author lost
 * @version 1.0
 **/
public class ObjectFormatMethodFailureTest extends TestCase
{

    /** Obtain necessary static references **/
    private static final ObjectFormatMethod OFM =
        FormatMethodFactory.getDefaultObjectFormatMethod( );

    /**
     * Tests the failure case of attempting to obtain a
     * <code>DateFormatMethod</code> from a <code>FormatMethodFactory</code>
	 * with an invalid date format pattern.
     **/
    public void testObjectFormatWithNull()
    {
        Throwable recievedT = null;
        try
        {
            OFM.format( null );
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
            fail( "A null object was accepted" );
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
        TestSuite suite = new TestSuite(ObjectFormatMethodFailureTest.class);

        return suite;
    }
}
