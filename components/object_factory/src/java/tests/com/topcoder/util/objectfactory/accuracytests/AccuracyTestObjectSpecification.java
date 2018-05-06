/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.objectfactory.accuracytests;

import com.topcoder.util.objectfactory.ObjectSpecification;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.File;

/**
 * Tests the class ObjectSpecification for accuracy.
 *
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class AccuracyTestObjectSpecification extends TestCase {
    /** The value of jar file used for test. */
    private static final String JAR_VALUE = "file:///"
        + new File("test_files/accuracytests/MockClass2.jar").getAbsolutePath();

    /** The value of identifier used for test. */
    private static final String IDENTIFIER_VALUE = "identifier";

    /** The value of int type used for test. */
    private static final String INT_VALUE = "10000";

    /** The value of float type used for test. */
    private static final String FLOAT_VALUE = "100000.0f";

    /** The value of double type used for test. */
    private static final String DOUBLE_VALUE = "10000000.0";

    /** The value of char type used for test. */
    private static final String CHAR_VALUE = "z";

    /** The value of byte type used for test. */
    private static final String BYTE_VALUE = "1";

    /** The value of long type used for test. */
    private static final String LONG_VALUE = "10000000000";

    /** The value of short type used for test. */
    private static final String SHORT_VALUE = "10";

    /** The value of boolean type used for test. */
    private static final String BOOLEAN_VALUE = "false";

    /** The value of string type used for test. */
    private static final String STRING_VALUE = "Test string";

    /** The value of complex type used for test. */
    private static final String COMPLEX_TYPE = "com.topcoder.util.objectfactory.accuracytests.MockClass2";

    /** The instance of ObjectSpecification class used for test. */
    private ObjectSpecification os;

    /**
     * Returns the suit to run the test.
     *
     * @return suite to run the test
     */
    public static Test suite() {
        return new TestSuite(AccuracyTestObjectSpecification.class);
    }

    /**
     * Tests constructor with int type.
     */
    public void testConstructorSimpleInt() {
        os = new ObjectSpecification(ObjectSpecification.SIMPLE_SPECIFICATION, JAR_VALUE, ObjectSpecification.INT,
            IDENTIFIER_VALUE, INT_VALUE, 1, null);

        assertEquals("The specType is not valid.", ObjectSpecification.SIMPLE_SPECIFICATION, os.getSpecType());
        assertEquals("The jar is is not valid.", JAR_VALUE, os.getJar());
        assertEquals("The type is not valid.", ObjectSpecification.INT, os.getType());
        assertEquals("The identifier is not valid.", IDENTIFIER_VALUE, os.getIdentifier());
        assertEquals("The value is not valid.", INT_VALUE, os.getValue());
        assertEquals("The dimension is not valid.", 1, os.getDimension());
        assertNull("The parameters should be null.", os.getParameters());
    }

    /**
     * Tests constructor with float type.
     */
    public void testConstructorSimpleFloat() {
        os = new ObjectSpecification(ObjectSpecification.SIMPLE_SPECIFICATION, JAR_VALUE, ObjectSpecification.FLOAT,
            IDENTIFIER_VALUE, FLOAT_VALUE, 1, null);

        assertEquals("The specType is not valid.", ObjectSpecification.SIMPLE_SPECIFICATION, os.getSpecType());
        assertEquals("The jar is is not valid.", JAR_VALUE, os.getJar());
        assertEquals("The type is not valid.", ObjectSpecification.FLOAT, os.getType());
        assertEquals("The identifier is not valid.", IDENTIFIER_VALUE, os.getIdentifier());
        assertEquals("The value is not valid.", FLOAT_VALUE, os.getValue());
        assertEquals("The dimension is not valid.", 1, os.getDimension());
        assertNull("The parameters should be null.", os.getParameters());
    }

    /**
     * Tests constructor with double type.
     */
    public void testConstructorSimpleDouble() {
        os = new ObjectSpecification(ObjectSpecification.SIMPLE_SPECIFICATION, JAR_VALUE, ObjectSpecification.DOUBLE,
            IDENTIFIER_VALUE, DOUBLE_VALUE, 1, null);

        assertEquals("The specType is not valid.", ObjectSpecification.SIMPLE_SPECIFICATION, os.getSpecType());
        assertEquals("The jar is is not valid.", JAR_VALUE, os.getJar());
        assertEquals("The type is not valid.", ObjectSpecification.DOUBLE, os.getType());
        assertEquals("The identifier is not valid.", IDENTIFIER_VALUE, os.getIdentifier());
        assertEquals("The value is not valid.", DOUBLE_VALUE, os.getValue());
        assertEquals("The dimension is not valid.", 1, os.getDimension());
        assertNull("The parameters should be null.", os.getParameters());
    }

    /**
     * Tests constructor with char type.
     */
    public void testConstructorSimpleChar() {
        os = new ObjectSpecification(ObjectSpecification.SIMPLE_SPECIFICATION, JAR_VALUE, ObjectSpecification.CHAR,
            IDENTIFIER_VALUE, CHAR_VALUE, 1, null);

        assertEquals("The specType is not valid.", ObjectSpecification.SIMPLE_SPECIFICATION, os.getSpecType());
        assertEquals("The jar is is not valid.", JAR_VALUE, os.getJar());
        assertEquals("The type is not valid.", ObjectSpecification.CHAR, os.getType());
        assertEquals("The identifier is not valid.", IDENTIFIER_VALUE, os.getIdentifier());
        assertEquals("The value is not valid.", CHAR_VALUE, os.getValue());
        assertEquals("The dimension is not valid.", 1, os.getDimension());
        assertNull("The parameters should be null.", os.getParameters());
    }

    /**
     * Tests constructor with byte type.
     */
    public void testConstructorSimpleByte() {
        os = new ObjectSpecification(ObjectSpecification.SIMPLE_SPECIFICATION, JAR_VALUE, ObjectSpecification.BYTE,
            IDENTIFIER_VALUE, BYTE_VALUE, 1, null);

        assertEquals("The specType is not valid.", ObjectSpecification.SIMPLE_SPECIFICATION, os.getSpecType());
        assertEquals("The jar is is not valid.", JAR_VALUE, os.getJar());
        assertEquals("The type is not valid.", ObjectSpecification.BYTE, os.getType());
        assertEquals("The identifier is not valid.", IDENTIFIER_VALUE, os.getIdentifier());
        assertEquals("The value is not valid.", BYTE_VALUE, os.getValue());
        assertEquals("The dimension is not valid.", 1, os.getDimension());
        assertNull("The parameters should be null.", os.getParameters());
    }

    /**
     * Tests constructor with long type.
     */
    public void testConstructorSimpleLong() {
        os = new ObjectSpecification(ObjectSpecification.SIMPLE_SPECIFICATION, JAR_VALUE, ObjectSpecification.LONG,
            IDENTIFIER_VALUE, LONG_VALUE, 1, null);

        assertEquals("The specType is not valid.", ObjectSpecification.SIMPLE_SPECIFICATION, os.getSpecType());
        assertEquals("The jar is is not valid.", JAR_VALUE, os.getJar());
        assertEquals("The type is not valid.", ObjectSpecification.LONG, os.getType());
        assertEquals("The identifier is not valid.", IDENTIFIER_VALUE, os.getIdentifier());
        assertEquals("The value is not valid.", LONG_VALUE, os.getValue());
        assertEquals("The dimension is not valid.", 1, os.getDimension());
        assertNull("The parameters should be null.", os.getParameters());
    }

    /**
     * Tests constructor with short type.
     */
    public void testConstructorSimpleShort() {
        os = new ObjectSpecification(ObjectSpecification.SIMPLE_SPECIFICATION, JAR_VALUE, ObjectSpecification.SHORT,
            IDENTIFIER_VALUE, SHORT_VALUE, 1, null);

        assertEquals("The specType is not valid.", ObjectSpecification.SIMPLE_SPECIFICATION, os.getSpecType());
        assertEquals("The jar is is not valid.", JAR_VALUE, os.getJar());
        assertEquals("The type is not valid.", ObjectSpecification.SHORT, os.getType());
        assertEquals("The identifier is not valid.", IDENTIFIER_VALUE, os.getIdentifier());
        assertEquals("The value is not valid.", SHORT_VALUE, os.getValue());
        assertEquals("The dimension is not valid.", 1, os.getDimension());
        assertNull("The parameters should be null.", os.getParameters());
    }

    /**
     * Tests constructor with boolean type.
     */
    public void testConstructorSimpleBoolean() {
        os = new ObjectSpecification(ObjectSpecification.SIMPLE_SPECIFICATION, JAR_VALUE, ObjectSpecification.BOOLEAN,
            IDENTIFIER_VALUE, BOOLEAN_VALUE, 1, null);

        assertEquals("The specType is not valid.", ObjectSpecification.SIMPLE_SPECIFICATION, os.getSpecType());
        assertEquals("The jar is is not valid.", JAR_VALUE, os.getJar());
        assertEquals("The type is not valid.", ObjectSpecification.BOOLEAN, os.getType());
        assertEquals("The identifier is not valid.", IDENTIFIER_VALUE, os.getIdentifier());
        assertEquals("The value is not valid.", BOOLEAN_VALUE, os.getValue());
        assertEquals("The dimension is not valid.", 1, os.getDimension());
        assertNull("The parameters should be null.", os.getParameters());
    }

    /**
     * Tests constructor with String type.
     */
    public void testConstructorSimpleString() {
        os = new ObjectSpecification(ObjectSpecification.SIMPLE_SPECIFICATION, JAR_VALUE, ObjectSpecification.STRING,
            IDENTIFIER_VALUE, STRING_VALUE, 1, null);

        assertEquals("The specType is not valid.", ObjectSpecification.SIMPLE_SPECIFICATION, os.getSpecType());
        assertEquals("The jar is is not valid.", JAR_VALUE, os.getJar());
        assertEquals("The type is not valid.", ObjectSpecification.STRING, os.getType());
        assertEquals("The identifier is not valid.", IDENTIFIER_VALUE, os.getIdentifier());
        assertEquals("The value is not valid.", STRING_VALUE, os.getValue());
        assertEquals("The dimension is not valid.", 1, os.getDimension());
        assertNull("The parameters should be null.", os.getParameters());
    }

    /**
     * Tests constructor with complex type.
     */
    public void testObjectSpecificationComplex() {
        final ObjectSpecification os1 = new ObjectSpecification(ObjectSpecification.SIMPLE_SPECIFICATION, JAR_VALUE,
            ObjectSpecification.FLOAT, IDENTIFIER_VALUE, FLOAT_VALUE, 1, null);
        final ObjectSpecification os2 = new ObjectSpecification(ObjectSpecification.SIMPLE_SPECIFICATION, JAR_VALUE,
            ObjectSpecification.DOUBLE, IDENTIFIER_VALUE, DOUBLE_VALUE, 1, null);

        os = new ObjectSpecification(ObjectSpecification.COMPLEX_SPECIFICATION, JAR_VALUE, COMPLEX_TYPE,
            IDENTIFIER_VALUE, null, 1, new ObjectSpecification[] {os1, os2});

        assertEquals("The specType is not valid.", ObjectSpecification.COMPLEX_SPECIFICATION, os.getSpecType());
        assertEquals("The jar is is not valid.", JAR_VALUE, os.getJar());
        assertEquals("The type is not valid.", COMPLEX_TYPE, os.getType());
        assertEquals("The identifier is not valid.", IDENTIFIER_VALUE, os.getIdentifier());
        assertNull("The value should be null.", os.getValue());
        assertEquals("The dimension is not valid.", 1, os.getDimension());

        Object[] parameters = os.getParameters();

        assertEquals("There should be 2 parameters.", parameters.length, 2);
        assertEquals("The parameter1 is not valid.", parameters[0], os1);
        assertEquals("The parameter2 is not valid.", parameters[1], os2);
    }

    /**
     * Tests constructor with array type.
     */
    public void testObjectSpecificationArray() {
        final ObjectSpecification os1 = new ObjectSpecification(ObjectSpecification.SIMPLE_SPECIFICATION, JAR_VALUE,
            ObjectSpecification.INT, IDENTIFIER_VALUE, INT_VALUE, 1, null);
        final ObjectSpecification os2 = new ObjectSpecification(ObjectSpecification.SIMPLE_SPECIFICATION, JAR_VALUE,
            ObjectSpecification.SHORT, IDENTIFIER_VALUE, SHORT_VALUE, 1, null);
        final ObjectSpecification os3 = new ObjectSpecification(ObjectSpecification.SIMPLE_SPECIFICATION, JAR_VALUE,
            ObjectSpecification.LONG, IDENTIFIER_VALUE, LONG_VALUE, 1, null);
        final ObjectSpecification os4 = new ObjectSpecification(ObjectSpecification.SIMPLE_SPECIFICATION, JAR_VALUE,
            ObjectSpecification.BYTE, IDENTIFIER_VALUE, BYTE_VALUE, 1, null);

        os = new ObjectSpecification(ObjectSpecification.ARRAY_SPECIFICATION, JAR_VALUE, ObjectSpecification.STRING,
            IDENTIFIER_VALUE, null, 2, new ObjectSpecification[][] { {os1, os2}, {os3, os4}});

        assertEquals("The specType is not valid.", ObjectSpecification.ARRAY_SPECIFICATION, os.getSpecType());
        assertEquals("The jar is is not valid.", JAR_VALUE, os.getJar());
        assertEquals("The type is not valid.", ObjectSpecification.STRING, os.getType());
        assertEquals("The identifier is not valid.", IDENTIFIER_VALUE, os.getIdentifier());
        assertNull("The value should be null.", os.getValue());
        assertEquals("The dimension is not valid.", 2, os.getDimension());

        Object[] parameters = os.getParameters();

        assertEquals("The length of parameters should be 2.", parameters.length, 2);

        Object[] parameter1 = (Object[]) parameters[0];
        Object[] parameter2 = (Object[]) parameters[1];

        assertEquals("The length of parameter1 should be 2.", parameter1.length, 2);
        assertEquals("The parameter1 is not valid.", parameter1[0], os1);
        assertEquals("The parameter1 is not valid.", parameter1[1], os2);

        assertEquals("The length of parameter2 should be 2.", parameter2.length, 2);
        assertEquals("The parameter2 is not valid.", parameter2[0], os3);
        assertEquals("The parameter2 is not valid.", parameter2[1], os4);
    }

    /**
     * Tests constructor with null specification type.
     */
    public void testObjectSpecificationNull() {
        os = new ObjectSpecification(ObjectSpecification.NULL_SPECIFICATION, JAR_VALUE, ObjectSpecification.STRING,
            IDENTIFIER_VALUE, null, 1, null);

        assertEquals("The specType is not valid.", ObjectSpecification.NULL_SPECIFICATION, os.getSpecType());
        assertEquals("The jar is is not valid.", JAR_VALUE, os.getJar());
        assertEquals("The type is not valid.", ObjectSpecification.STRING, os.getType());
        assertEquals("The identifier is not valid.", IDENTIFIER_VALUE, os.getIdentifier());
        assertNull("The value should be null.", os.getValue());
        assertNull("The parameters should be null.", os.getParameters());
    }
}
