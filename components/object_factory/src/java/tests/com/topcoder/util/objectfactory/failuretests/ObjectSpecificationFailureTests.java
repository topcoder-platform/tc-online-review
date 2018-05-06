/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.objectfactory.failuretests;

import junit.framework.TestCase;

import com.topcoder.util.objectfactory.ObjectSpecification;


/**
 * <p>This UnitTest of ObjectSpecification.</p>
 *
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class ObjectSpecificationFailureTests extends TestCase {

    /**
     * The jar String.
     */
    private static final String JAR = "java.lang";

    /**
     * The String 'value'
     */
    private static final String VALUE = "value";

    /**
     * The String 'identifier'
     */
    private static final String IDE = "identifier";

    /**
     * Test the cosntructor, fail for the specType null,
     * IllegalArgumentException should be thrown.
     *
     */
    public void testconstructor_failForspecTypeNull() {
        try {
            new ObjectSpecification(null, JAR, ObjectSpecification.INT, IDE, VALUE, 1, new Object[] {});
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * Test the cosntructor, fail for the specType invalid,
     * IllegalArgumentException should be thrown.
     *
     */
    public void testconstructor_failForspecTypeinvalid() {
        try {
            new ObjectSpecification("invalid", JAR, ObjectSpecification.INT, IDE, VALUE, 1, new Object[] {});
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * Test the cosntructor, fail for the type null,
     * IllegalArgumentException should be thrown.
     *
     */
    public void testconstructor_failForTypeNull() {
        try {
            new ObjectSpecification(ObjectSpecification.SIMPLE_SPECIFICATION, JAR, null, IDE, VALUE, 1, null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * Test the cosntructor, fail for the type empty,
     * IllegalArgumentException should be thrown.
     *
     */
    public void testconstructor_failForTypeEmpty() {
        try {
            new ObjectSpecification(ObjectSpecification.SIMPLE_SPECIFICATION, JAR, " ", IDE, VALUE, 1, null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * Test the cosntructor, fail for the dimension < 1,
     * IllegalArgumentException should be thrown.
     *
     */
    public void testconstructor_failFordimensionInvalid() {
        try {
            new ObjectSpecification(ObjectSpecification.SIMPLE_SPECIFICATION, JAR, ObjectSpecification.INT,
                IDE, VALUE, 0, null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * Test the cosntructor, fail for the SpecType is simple, but the type is not simple,
     * IllegalArgumentException should be thrown.
     *
     */
    public void testconstructor_failForSimpleSpecTypeWithOutSimpleType() {
        try {
            new ObjectSpecification(ObjectSpecification.SIMPLE_SPECIFICATION, JAR, "complex_type", IDE, VALUE,
                1, null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * Test the cosntructor, fail for the SpecType is simple, but the value is null,
     * IllegalArgumentException should be thrown.
     *
     */
    public void testconstructor_failForSimpleSpecTypeWithOutValue() {
        try {
            new ObjectSpecification(ObjectSpecification.SIMPLE_SPECIFICATION, JAR, ObjectSpecification.INT,
                IDE, null, 1, null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * Test the cosntructor, fail for the SpecType is complex, but the type is simple,
     * IllegalArgumentException should be thrown.
     *
     */
    public void testconstructor_failForComplexSpecTypeWithSimpleType() {
        try {
            new ObjectSpecification(ObjectSpecification.COMPLEX_SPECIFICATION, JAR, ObjectSpecification.DOUBLE,
                IDE, null, 1, new Object[] {});
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }
}
