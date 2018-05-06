/*
 * FormatMethodFactoryTest.java
 *
 * Copyright © 2002, TopCoder, Inc. All rights reserved
 *
 */

package com.topcoder.util.format;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * A test class for <code>FormatMethodFactory</code>.<p>
 *
 * @author garyk
 * @version 1.0
 **/
public class FormatMethodFactoryTest extends TestCase {
    /**
     * Creates a DateFormatMethod without parameters
     **/
    private DateFormatMethod createDateFormatMethod() {
        return FormatMethodFactory.getDefaultDateFormatMethod(null);
    }
	
    /**
     * Creates a formatter with the given format "yyyy.MMMMM.dd hh:mm aaa"
     **/
    private DateFormatMethod createDateFormatMethodWithPattern() {
        return FormatMethodFactory.getDefaultDateFormatMethod("yyyy.MMMMM.dd" 
                + " hh:mm aaa");
    }

    /**
     * Creates a formatter with the <code>SimpleDateFormat</code> "dd.MM.yy", 
     * but without the pattern for the formatter
     **/
    private DateFormatMethod createDateFormatMethodWithSDF() {
        return FormatMethodFactory.getDefaultDateFormatMethod(null, 
                new SimpleDateFormat("dd.MM.yy"));
    }

    /**
     * Creates a formatter with the given format "yyyy.MMMMM.dd hh:mm aaa" 
     * and <code>SimpleDateFormat</code> "dd.MM.yy", and the given format 
     * will override the format in <code>SimpleDateFormat</code>
     **/
    private DateFormatMethod createDateFormatMethodWithSDFPattern() {
        return FormatMethodFactory.getDefaultDateFormatMethod("yyyy.MMMMM.dd" 
                + " hh:mm aaa", new SimpleDateFormat("dd.MM.yy"));
    }

    /**
     * Tests creating a <code>PrimitiveFormatter</code> 
     **/ 	
    public void testCreateDateFormatMethod() {
        DateFormatMethod formatter = createDateFormatMethod();
        assertTrue("Creating the default DateFormatMethod", 
                formatter != null);

        formatter = createDateFormatMethodWithPattern();
        assertTrue("Creating a DateFormatMethod with a pattern", 
                formatter != null);

        formatter = createDateFormatMethodWithSDF();
        assertTrue("Creating a DateFormatMethod with a SimpleDateFormat",       
                formatter != null);

        formatter = createDateFormatMethodWithSDFPattern();
        assertTrue("Creating a DateFormatMethod with a pattern and" 
                + " SimpleDateFormat", formatter != null);
    }

    /**
     * Tests formatting a <code>Date</code>
     **/ 
    public void testFormatDate() {
        Calendar c = Calendar.getInstance();
        /* December 25, 2002, 00:00:00 */
        c.set(2002, Calendar.DECEMBER, 25, 0, 0, 0);   
        Date d = c.getTime(); 

        DateFormatMethod formatter = createDateFormatMethod();
        assertEquals("Using the default DateFormatMethod", "12/25/2002", 
                formatter.format(d));

        formatter = createDateFormatMethodWithPattern();
        assertEquals("Using a DateFormatMethod with a pattern", 
                "2002.December.25 12:00 AM", formatter.format(d));

        formatter = createDateFormatMethodWithSDF();
        assertEquals("Using a DateFormatMethod with a SimpleDateFormat",        
                "25.12.02", formatter.format(d));

        formatter = createDateFormatMethodWithSDFPattern();
        assertEquals("Using a DateFormatMethod with a pattern and" 
                + " SimpleDateFormat", "2002.December.25 12:00 AM", 
                formatter.format(d));
    }

    /**
     * Tests formatting an <code>Object</code> using the 
     * <code>ObjectFormatMethod</code> from the 
     * <code>getDefaultObjectFormatMethod</code> method
     **/
    public void testFormatObjectWithDefaultObjectFormatMethod() {
        Date d = new Date();

        ObjectFormatMethod ofm =     
                FormatMethodFactory.getDefaultObjectFormatMethod();

        assertEquals(ofm.format(d), d.toString());
    }

    /**
     * Assembles and returns a test suite for
     * all the test methods of this test case.
     *
     * @return A non-null test suite.
     */
    public static Test suite() {
        /*
         * Reflection is used here to add all
         * the testXXX() methods to the suite.
         */
        TestSuite suite = new TestSuite(FormatMethodFactoryTest.class);

        return suite;
    }
}