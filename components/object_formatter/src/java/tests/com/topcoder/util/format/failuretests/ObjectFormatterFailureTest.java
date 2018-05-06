/*
 * ObjectFormatterFailureTest.java
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
 * <p>A test class for failures on <code>ObjectFormatterFailureTest</code>s
 * obtained from the <code>FormatMethodFactory</code>.</P>
 *
 * @author lost
 * @version 1.0
 **/
public class ObjectFormatterFailureTest extends TestCase
{

    /** Obtain necessary static references **/
    private static final ObjectFormatter EMPTY_OF =
        ObjectFormatterFactory.getEmptyFormatter( );

    private static final ObjectFormatter PLAIN_OF =
        ObjectFormatterFactory.getPlainFormatter( );

    private static final ObjectFormatter PRETTY_OF =
        ObjectFormatterFactory.getPrettyFormatter( );

    private static final ObjectFormatMethod OFM =
        new ObjectFormatMethod( )
            {
                public String format( Object obj )
                    throws IllegalArgumentException
                {
                    return "test";
                }
            };

    /**
     * Utility class for testing the base functionality of an
     * <code>ObjectFormatters</code>
     **/
    public void baseObjectFormatterTests( ObjectFormatter of )
    {
        Throwable recievedT = null;

        // Unset Object
        try
        {
            of.unsetFormatMethodForClass( null );
        }
        catch( Throwable t )
        {
            //This should be reached
            recievedT = t;
        }
        if( recievedT != null )
        {
            assertTrue( "(unset)IncorrectException: " + recievedT.toString( ),
                recievedT instanceof IllegalArgumentException );
        }
        else
        {
            fail( "unsetting an unset type should throw an " +
               "IllegalArgumentException" );
        }

        // Retrieve format method for null
        try
        {
            recievedT = null;
            of.setFormatMethodForClass( Object.class, null, false );
        }
        catch( Throwable t )
        {
            //This should be reached
            recievedT = t;
        }
        if( recievedT != null )
        {
            assertTrue( "(set null OF)IncorrectException: " +
                recievedT.toString( ),
                recievedT instanceof NullPointerException );
        }
        else
        {
            fail( "Setting a method for a null object should throw an " +
               "NullPointerException" );
        }

        // Retrieve format method for null
        try
        {
            recievedT = null;
            of.setFormatMethodForClass( null, OFM, false );
        }
        catch( Throwable t )
        {
            //This should be reached
            recievedT = t;
        }
        if( recievedT != null )
        {
            assertTrue( "(set null Class)IncorrectException: " +
                recievedT.toString( ),
                recievedT instanceof IllegalArgumentException );
        }
        else
        {
            fail( "Setting a method for a null object should throw an " +
               "IllegalArgumentException" );
        }

        // Retrieve format method for null
        try
        {
            recievedT = null;
            of.getFormatMethodForClass( null );
        }
        catch( Throwable t )
        {
            //This should be reached
            recievedT = t;
        }
        if( recievedT != null )
        {
            assertTrue( "(get)IncorrectException: " + recievedT.toString( ),
                recievedT instanceof IllegalArgumentException );
        }
        else
        {
            fail( "Retrieving a method for a null object should throw an " +
               "IllegalArgumentException" );
        }
    }

    /**
     * Confirm that Exception is thrown when ObjectFormatMethod
     * ambiguity exists.
     **/
    public void ambiguityTester( ObjectFormatter of )
    {
        Throwable recievedT = null;

        of.setFormatMethodForClass( SampleInterfaceOne.class, OFM,
            true );
        of.setFormatMethodForClass( SampleInterfaceTwo.class, OFM,
            true );
        AmbiguousObject ao = new AmbiguousObject( );

        try
        {
            of.format( ao );
        }
        catch( Throwable t )
        {
            //This should be reached
            recievedT = t;
        }
        if( recievedT != null )
        {
            assertTrue( "(format)IncorrectException: " + recievedT.toString( ),
                recievedT instanceof IllegalArgumentException );
        }
        else
        {
            fail( "Trying to format an ambigous object should throw an " +
               "IllegalArgumentException" );
        }

        recievedT = null;
        try
        {
            of.unsetFormatMethodForClass( AmbiguousObject.class );
        }
        catch( Throwable t )
        {
            //This should be reached
            recievedT = t;
        }
        if( recievedT != null )
        {
            assertTrue( "(unset ambiguous)IncorrectException: "
                + recievedT.toString( ),
                recievedT instanceof IllegalArgumentException );
        }
        else
        {
            fail( "Trying to unset a format method for an ambigous " +
               "object should throw an IllegalArgumentException" );
        }

        assertNull( of.getFormatMethodForObject( ao ) );

    }

    /**
     * Utility class for testing the functionality of a populated
     * <code>ObjectFormatters</code>
     **/
    public void populatedObjectFormatterTests( ObjectFormatter of )
    {
        Throwable recievedT = null;

        // Unset Object
        try
        {
            of.format( null );
        }
        catch( Throwable t )
        {
            //This should be reached
            recievedT = t;
        }
        if( recievedT != null )
        {
            assertTrue( "(format null)IncorrectException: "
                + recievedT.toString( ),
                recievedT instanceof IllegalArgumentException );
        }
        else
        {
            fail( "Attempting to format a null should throw an " +
               "IllegalArgumentException" );
        }
    }

    /**
     * Tests the ambiguous cases of the empty <code>ObjectFormatter</code>
     * from an <code>ObjectFormatterFactory</code>
     **/
    public void testAmbiguityEmptyObjectFormatter( )
    {
        ambiguityTester( EMPTY_OF );
    }

    /**
     * Tests the base failure cases of the empty <code>ObjectFormatter</code>
     * from an <code>ObjectFormatterFactory</code>
     **/
    public void testBaseEmptyObjectFormatter( )
    {
		baseObjectFormatterTests( EMPTY_OF );
        Throwable recievedT = null;

        // Unset Object
        try
        {
            EMPTY_OF.unsetFormatMethodForClass( Object.class );
        }
        catch( Throwable t )
        {
            //This should be reached
            recievedT = t;
        }
        if( recievedT != null )
        {
            assertTrue( "(unset EMPTY)IncorrectException: "
                + recievedT.toString( ),
                recievedT instanceof IllegalArgumentException );
        }
        else
        {
            fail( "unsetting an unset type should throw an " +
               "IllegalArgumentException" );
        }

        // Format an Object
        recievedT = null;
        try
        {
            EMPTY_OF.format( new Object( ) );
        }
        catch( Throwable t )
        {
            //This should be reached
            recievedT = t;
        }
        if( recievedT != null )
        {
            assertTrue( "(format EMPTY)IncorrectException: "
                + recievedT.toString( ),
                recievedT instanceof IllegalArgumentException );
        }
        else
        {
            fail( "Attempting to format any Object with the Empty " +
                "ObjectFormatter should throw an IllegalArgumentException" );
        }
    }

    /**
     * Tests the ambiguous cases of the pretty <code>ObjectFormatter</code>
     * from an <code>ObjectFormatterFactory</code>
     **/
    public void testAmbiguityPrettyObjectFormatter( )
    {
        ambiguityTester( PRETTY_OF );
    }

    /**
     * Tests the failure cases of the pretty <code>ObjectFormatter</code>
     * from an <code>ObjectFormatterFactory</code>
     **/
    public void testPrettyObjectFormatter( )
    {
		populatedObjectFormatterTests( PRETTY_OF );
		baseObjectFormatterTests( PRETTY_OF );
    }

    /**
     * Tests the ambiguous cases of the plain <code>ObjectFormatter</code>
     * from an <code>ObjectFormatterFactory</code>
     **/
    public void testAmbiguityPlainObjectFormatter( )
    {
        ambiguityTester( PLAIN_OF );
    }

    /**
     * Tests the failure cases of the plain <code>ObjectFormatter</code>
     * from an <code>ObjectFormatterFactory</code>
     **/
    public void testPlainObjectFormatter( )
    {
		populatedObjectFormatterTests( PLAIN_OF );
		baseObjectFormatterTests( PLAIN_OF );
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
        TestSuite suite = new TestSuite( ObjectFormatterFailureTest.class );

        return suite;
    }
}
