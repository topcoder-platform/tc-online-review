/*
 * FormatMethodFactoryFailureTest.java
 *
 * Copyright  2002, TopCoder, Inc. All rights reserved
 *
 */

package com.topcoder.util.format.failuretests;

import com.topcoder.util.format.FormatMethodFactory;
import com.topcoder.util.format.DateFormatMethod;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * <p>A test class for <code>FormatMethodFactory</code> failures.</P>
 *
 * @author lost
 * @version 1.0
 **/
public class FormatMethodFactoryFailureTest extends TestCase
{

    /** For use in testing for proper exception throwing */
    private static final String INVALID_DATE_FORMAT = "*+-#@$%&&^*SDersdf";

    /**
     * Tests the failure case of attempting to obtain a
     * <code>DateFormatMethod</code> from a <code>FormatMethodFactory</code>
	 * with an invalid date format pattern.
     **/
    public void testFormatMethodFactoryWithInvalidPattern()
    {
        boolean properException = false;
        try
        {
            DateFormatMethod dfm =
				FormatMethodFactory.getDefaultDateFormatMethod(
                    INVALID_DATE_FORMAT );
        }
        catch( IllegalArgumentException ia_ex )
        {
            //This should be reached.
            properException = true;
        }
        if( properException == false )
        {
            fail( "An invalid Date format was accepted" );
        }

        try
        {
            properException = false;
            SimpleDateFormat sdf = new SimpleDateFormat( );
            DateFormatMethod dfm =
				FormatMethodFactory.getDefaultDateFormatMethod(
                    INVALID_DATE_FORMAT, sdf );
        }
        catch( IllegalArgumentException ia_ex )
        {
            //This should be reached.
            properException = true;
        }
        if( properException == false )
        {
            fail( "An invalid Date format was accepted" );
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
        TestSuite suite = new TestSuite(FormatMethodFactoryFailureTest.class);

        return suite;
    }
}
