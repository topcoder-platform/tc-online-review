/*
 * PrimitiveFormatterFactoryTest.java
 *
 * Copyright © 2002, TopCoder, Inc. All rights reserved
 *
 */

package com.topcoder.util.format;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.text.DecimalFormat;

/**
 * A test class for <code>PrimitiveFormatterFactory</code>.<p>
 *
 * @author garyk
 * @version 1.0
 **/
public class PrimitiveFormatterFactoryTest extends TestCase {

    /**
     * Creates a plain primitive formatter
     **/
    private PrimitiveFormatter createPlainFormatter() {
        return PrimitiveFormatterFactory.getPlainFormatter();
    }

    /**
     * Creates a pretty primitive formatter
     **/
    private PrimitiveFormatter createPrettyFormatter() {
        return PrimitiveFormatterFactory.getPrettyFormatter();
    }

    /**
     * Creates a Formatter with the given format "0,00.00"
     **/
    private PrimitiveFormatter createFormatterWithPattern() {
        return PrimitiveFormatterFactory.getFormatter("0,00.00");
    }

    /**
     * Creates a Formatter with the <code>DecimalFormat</code> "##E0", 
     * but without the pattern for the formatter
     **/
    private PrimitiveFormatter createFormatterWithDF() {
        return PrimitiveFormatterFactory.getFormatter(null, new
                DecimalFormat("##E0"));
    }

    /**
     * Creates a Formatter with the given format "##E0" and 
     * <code>DecimalFormat</code> "0,00.00", and the given format will 
     * override the format in <code>DecimalFormat</code>
     **/
    private PrimitiveFormatter createFormatterWithDFPattern() {
        return PrimitiveFormatterFactory.getFormatter("##E0", 
                new DecimalFormat("0,00.00"));
    }

    /**
     * Tests creating <code>PrimitiveFormatter</code>s 
     **/ 	
    public void testCreatePrimitiveFormatter() {
        PrimitiveFormatter formatter = createPlainFormatter();
        assertTrue("Creating plain PrimitiveFormatter", 
                formatter != null);

        formatter = createPrettyFormatter();
        assertTrue("Creating pretty PrimitiveFormatter", 
                formatter != null);

        formatter = createFormatterWithPattern();
        assertTrue("Creating PrimitiveFormatter with a pattern", 
                formatter != null);

        formatter = createFormatterWithDF();
        assertTrue("Creating PrimitiveFormatter with a DecimalFormat", 
                formatter != null);

        formatter = createFormatterWithDFPattern();
        assertTrue("Creating PrimitiveFormatter with a pattern and" 
                + " DecimalFormat", formatter != null);
    }

    /**
     * Tests formatting a <code>byte</code> 
     **/ 
    public void testFormatByteWithPrimitiveFormatter() {
        byte b = (byte)8;
        String expected;

        PrimitiveFormatter formatter = createPlainFormatter();
        assertEquals("Using plain PrimitiveFormatter", "8",     
                formatter.format(b));

        formatter = createPrettyFormatter();
        assertEquals("Using pretty PrimitiveFormatter", "8", 
                formatter.format(b));

        formatter = createFormatterWithPattern();
        assertEquals("Using PrimitiveFormatter with the pattern", "0,08.00", 
                formatter.format(b));

        formatter = createFormatterWithDF();
        assertEquals("Using PrimitiveFormatter with the DecimalFormat", "8E0", 
                formatter.format(b));

        formatter = createFormatterWithDFPattern();
        assertEquals("Using PrimitiveFormatter with the pattern and" 
                + " DecimalFormat", "8E0", formatter.format(b));
    }

    /**
     * Tests formatting a <code>char</code> 
     **/ 
    public void testFormatCharWithPrimitiveFormatter() {
        char c = 'c';
        String expected;

        PrimitiveFormatter formatter = createPlainFormatter();
        assertEquals("Using plain PrimitiveFormatter", "c", 
                formatter.format(c));

        formatter = createPrettyFormatter();
        assertEquals("Using pretty PrimitiveFormatter", "99", 
                formatter.format(c));

        formatter = createFormatterWithPattern();
        assertEquals("Using PrimitiveFormatter with the pattern", "0,99.00", 
                formatter.format(c));

        formatter = createFormatterWithDF();
        assertEquals("Using PrimitiveFormatter with the DecimalFormat", "99E0", 
                formatter.format(c));

        formatter = createFormatterWithDFPattern();
        assertEquals("Using PrimitiveFormatter with the pattern and" 
                + " DecimalFormat", "99E0", formatter.format(c));
    }

    /**
     * Tests formatting a <code>short</code>
     **/ 
    public void testFormatShortWithPrimitiveFormatter() {
        short s = -12;
        String expected;

        PrimitiveFormatter formatter = createPlainFormatter();
        assertEquals("Using plain PrimitiveFormatter", "-12", 
                formatter.format(s));

        formatter = createPrettyFormatter();
        assertEquals("Using pretty PrimitiveFormatter", "-12", 
                formatter.format(s));

        formatter = createFormatterWithPattern();
        assertEquals("Using PrimitiveFormatter with the pattern", "-0,12.00", 
                formatter.format(s));

        formatter = createFormatterWithDF();
        assertEquals("Using PrimitiveFormatter with the DecimalFormat", "-12E0",
                formatter.format(s));

        formatter = createFormatterWithDFPattern();
        assertEquals("Using PrimitiveFormatter with the pattern and" 
                + " DecimalFormat", "-12E0", formatter.format(s));
    }

    /**
     * Tests formatting an <code>int</code> 
     **/ 
    public void testFormatIntWithPrimitiveFormatter() {
        int i = 1234;
        String expected;

        PrimitiveFormatter formatter = createPlainFormatter();
        assertEquals("Using plain PrimitiveFormatter", "1234", 
                formatter.format(i));

        formatter = createPrettyFormatter();
        assertEquals("Using pretty PrimitiveFormatter", "1,234", 
                formatter.format(i));

        formatter = createFormatterWithPattern();
        assertEquals("Using PrimitiveFormatter with the pattern", "12,34.00", 
                formatter.format(i));

        formatter = createFormatterWithDF();
        assertEquals("Using PrimitiveFormatter with the DecimalFormat", "12E2", 
                formatter.format(i));

        formatter = createFormatterWithDFPattern();
        assertEquals("Using PrimitiveFormatter with the pattern and" + 
                " DecimalFormat", "12E2", formatter.format(i));
    }

    /**
     * Tests formatting a <code>long</code>
     **/ 
    public void testFormatLongWithPrimitiveFormatter() {
        long l = 12345;
        String expected;

        PrimitiveFormatter formatter = createPlainFormatter();
        assertEquals("Using plain PrimitiveFormatter", "12345", 
                formatter.format(l));

        formatter = createPrettyFormatter();
        assertEquals("Using pretty PrimitiveFormatter", "12,345", 
                formatter.format(l));

        formatter = createFormatterWithPattern();
        assertEquals("Using PrimitiveFormatter with the pattern", 
                "1,23,45.00", formatter.format(l));

        formatter = createFormatterWithDF();
        assertEquals("Using PrimitiveFormatter with the DecimalFormat", 
                "1.2E4", formatter.format(l));

        formatter = createFormatterWithDFPattern();
        assertEquals("Using PrimitiveFormatter with the pattern and" 
                + " DecimalFormat", "1.2E4", formatter.format(l));
    }

    /**
     * Tests formatting a <code>float</code> 
     **/ 
    public void testFormatFloatWithPrimitiveFormatter()
    {
        float f = (float)12345.678;
        String expected;

        PrimitiveFormatter formatter = createPlainFormatter();
        assertEquals("Using plain PrimitiveFormatter", "12345.678", 
                formatter.format(f));

        formatter = createPrettyFormatter();
        assertEquals("Using pretty PrimitiveFormatter", "12,345.68", 
                formatter.format(f));

        formatter = createFormatterWithPattern();
        assertEquals("Using PrimitiveFormatter with the pattern", 
                "1,23,45.68", formatter.format(f));

        formatter = createFormatterWithDF();
        assertEquals("Using PrimitiveFormatter with the DecimalFormat", 
                "1.2E4", formatter.format(f));

        formatter = createFormatterWithDFPattern();
        assertEquals("Using PrimitiveFormatter with the pattern and" 
                + " DecimalFormat", "1.2E4", formatter.format(f));
    }

    /**
     * Tests formatting a <code>double</code>
     **/ 
    public void testFormatDoubleWithPrimitiveFormatter() {
        double d = -1234567.890;
        String expected;

        PrimitiveFormatter formatter = createPlainFormatter();
        assertEquals("Using plain PrimitiveFormatter", "-1234567.89", 
                formatter.format(d));

        formatter = createPrettyFormatter();
        assertEquals("Using pretty PrimitiveFormatter", "-1,234,567.89", 
                formatter.format(d));

        formatter = createFormatterWithPattern();
        assertEquals("Using PrimitiveFormatter with the pattern", 
                "-1,23,45,67.89", formatter.format(d));

        formatter = createFormatterWithDF();
        assertEquals("Using PrimitiveFormatter with the DecimalFormat", 
                "-1.2E6", formatter.format(d));

        formatter = createFormatterWithDFPattern();
        assertEquals("Using PrimitiveFormatter with the pattern and" 
                + " DecimalFormat", "-1.2E6", formatter.format(d));
    }

    /**
     * Assembles and returns a test suite for
     * all the test methods of this test case.
     *
     * @return A non-null test suite.
     */
    public static Test suite() {
        //
        // Reflection is used here to add all
        // the testXXX() methods to the suite.
        //
        TestSuite suite = new TestSuite(PrimitiveFormatterFactoryTest.class);

        return suite;
    }
}