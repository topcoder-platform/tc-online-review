/*
 * Copyright (C) 2006, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.objectfactory;

import junit.framework.TestCase;

/**
 * Unit test for ObjectSpecification.
 *
 * @author mgmg, TCSDEVELOPER
 * @version 2.2
 */
public class ObjectSpecificationUnitTest extends TestCase {
    /**
     * The object for test.
     */
    private ObjectSpecification instance = null;

    /**
     * Create the test instance.
     */
    public void setUp() {
        ObjectSpecification[] specifications = new ObjectSpecification[2];
        specifications[0] = new ObjectSpecification(ObjectSpecification.SIMPLE_SPECIFICATION, null, "int", null, "5",
                1, null);
        specifications[1] = new ObjectSpecification(ObjectSpecification.SIMPLE_SPECIFICATION, null, "String", null,
                "aaa", 1, null);
        instance = new ObjectSpecification(ObjectSpecification.COMPLEX_SPECIFICATION,
                TestHelper.getURLString("test_files/test.jar"), "com.test.TestComplex", "arraylist", null, 2,
                specifications);
    }

    /**
     * Test constructor with invalid spec type and simple type.
     * IllegalArgumentException should be thrown because of the invalid
     * parameter.
     */
    public void testConstructor_InvalidSpecType() {
        try {
            new ObjectSpecification("invalid_spec", null, "int", null, null, 1, null);
            fail("IllegalArgumentException should be thrown because of the invalid parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test constructor with null type and simple type. IllegalArgumentException
     * should be thrown because of the invalid parameter.
     */
    public void testConstructor_SimpleNullType() {
        try {
            new ObjectSpecification(ObjectSpecification.SIMPLE_SPECIFICATION, null, null, null, null, 1, null);
            fail("IllegalArgumentException should be thrown because of the invalid parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test constructor with invalid simple type and simple type.
     * IllegalArgumentException should be thrown because of the invalid
     * parameter.
     */
    public void testConstructor_SimpleNotSimpleType() {
        try {
            new ObjectSpecification(ObjectSpecification.SIMPLE_SPECIFICATION, null, "abc", null, null, 1, null);
            fail("IllegalArgumentException should be thrown because of the invalid parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test constructor with valid simple type. No exception will be thrown.
     */
    public void testConstructor_SimpleAccuracy() {
        ObjectSpecification obj = new ObjectSpecification(ObjectSpecification.SIMPLE_SPECIFICATION, null, "int", null,
                "5", 3, new Object[0]);

        assertNotNull("The object should not be null.", obj);
        assertNull("The parameter should be null.", obj.getParameters());
        assertEquals("The dimension should be 1.", obj.getDimension(), 1);
    }

    /**
     * Test constructor with null type and array type. IllegalArgumentException
     * should be thrown because of the invalid parameter.
     */
    public void testConstructor_ArrayNullType() {
        try {
            new ObjectSpecification(ObjectSpecification.ARRAY_SPECIFICATION, null, null, null, null, 2, new Object[] {
                new Object[] { new Integer(1) }, new Object[] { new Integer(45) } });
            fail("IllegalArgumentException should be thrown because of the invalid parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test constructor with invalid type and array type.
     * IllegalArgumentException should be thrown because of the invalid
     * parameter.
     */
    public void testConstructor_ArrayInvalidType() {
        try {
            new ObjectSpecification(ObjectSpecification.ARRAY_SPECIFICATION, null, "abc", null, null, 2, new Object[] {
                new Object[] { new Integer(1) }, new Object[] { new Integer(45) } });
            fail("IllegalArgumentException should be thrown because of the invalid parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test constructor with invalid array and array type.
     * IllegalArgumentException should be thrown because of the invalid
     * parameter.
     */
    public void testConstructor_ArrayInvalidArray() {
        try {
            new ObjectSpecification(ObjectSpecification.ARRAY_SPECIFICATION, null, "java.lang.Integer", null, null, 2,
                    new Object[] { new Object[] { new Integer(1), new Integer(2) }, new Object[] { new Integer(45) } });
            fail("IllegalArgumentException should be thrown because of the invalid parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test constructor with invalid dimension and array type.
     * IllegalArgumentException should be thrown because of the invalid
     * parameter.
     */
    public void testConstructor_ArrayInvalidDimension1() {
        try {
            new ObjectSpecification(ObjectSpecification.ARRAY_SPECIFICATION, null, "java.lang.Integer", null, null, 1,
                    new Object[] { new Object[] { new Integer(1) }, new Object[] { new Integer(45) } });
            fail("IllegalArgumentException should be thrown because of the invalid parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test constructor with negative dimension and array type.
     * IllegalArgumentException should be thrown because of the invalid
     * parameter.
     */
    public void testConstructor_ArrayInvalidDimension2() {
        try {
            new ObjectSpecification(ObjectSpecification.SIMPLE_SPECIFICATION, null, ObjectSpecification.INT, null,
                    "1.0", -2, null);
            fail("IllegalArgumentException should be thrown because of the invalid parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test constructor with valid array type. No exception will be thrown.
     */
    public void testConstructor_ArrayAccuracy() {
        ObjectSpecification obj = new ObjectSpecification(ObjectSpecification.ARRAY_SPECIFICATION, null,
                "java.lang.Integer", null, null, 2,
                new Object[] { new Object[] { new Integer(1) }, new Object[] { new Integer(45) } });

        assertNotNull("The object should not be null.", obj);
    }

    /**
     * Test constructor with simple type of null type. IllegalArgumentException
     * should be thrown because of the invalid parameter.
     */
    public void testConstructor_NullSimpleType() {
        try {
            new ObjectSpecification(ObjectSpecification.NULL_SPECIFICATION, null, ObjectSpecification.FLOAT, null,
                    null, 1, null);
            fail("IllegalArgumentException should be thrown because of the invalid parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test constructor with null type of null type. IllegalArgumentException
     * should be thrown because of the invalid parameter.
     */
    public void testConstructor_NullNullType() {
        try {
            new ObjectSpecification(ObjectSpecification.NULL_SPECIFICATION, null, null, null, null, 1, null);
            fail("IllegalArgumentException should be thrown because of the invalid parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test constructor with invalid type of null type. IllegalArgumentException
     * should be thrown because of the invalid parameter.
     */
    public void testConstructor_NullInvalidType() {
        try {
            new ObjectSpecification(ObjectSpecification.NULL_SPECIFICATION, null, "abc", null, null, 1, null);
            fail("IllegalArgumentException should be thrown because of the invalid parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test constructor with valid null type. No exception will be thrown.
     */
    public void testConstructor_NullAccuracy() {
        ObjectSpecification obj = new ObjectSpecification(ObjectSpecification.NULL_SPECIFICATION, null,
                "java.lang.Integer", null, "abc", 4, null);

        assertNotNull("The object should not be null.", obj);
        assertNull("The value should be null.", obj.getValue());
        assertEquals("The dimension should be 1.", obj.getDimension(), 1);
    }

    /**
     * Test constructor with null type of complex type. IllegalArgumentException
     * should be thrown because of the invalid parameter.
     */
    public void testConstructor_ComplexNullType() {
        try {
            new ObjectSpecification(ObjectSpecification.COMPLEX_SPECIFICATION, null, null, null, null, 1, null);
            fail("IllegalArgumentException should be thrown because of the invalid parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test constructor with invalid type of complex type.
     * IllegalArgumentException should be thrown because of the invalid
     * parameter.
     */
    public void testConstructor_ComplexInvalidType() {
        try {
            new ObjectSpecification(ObjectSpecification.COMPLEX_SPECIFICATION, null, "abc", null, null, 1, null);
            fail("IllegalArgumentException should be thrown because of the invalid parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test constructor with simple type of complex type.
     * IllegalArgumentException should be thrown because of the invalid
     * parameter.
     */
    public void testConstructor_ComplexSimpleType() {
        try {
            new ObjectSpecification(ObjectSpecification.COMPLEX_SPECIFICATION, null, ObjectSpecification.BYTE, null,
                    null, 1, null);
            fail("IllegalArgumentException should be thrown because of the invalid parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test constructor with valid parameter of complex type. No exception will
     * be thrown.
     */
    public void testConstructor_ComplexAccuracy1() {
        ObjectSpecification obj = new ObjectSpecification(ObjectSpecification.COMPLEX_SPECIFICATION, null,
                "java.util.ArrayList", "arraylist", null, 1, new ObjectSpecification[] { new ObjectSpecification(
                        ObjectSpecification.SIMPLE_SPECIFICATION, null, "int", null, "5", 1, null) });

        assertNotNull("The object should not be null.", obj);
    }

    /**
     * Test constructor with valid parameter and valid jar file of complex type.
     * No exception will be thrown.
     */
    public void testConstructor_ComplexAccuracy2() {
        ObjectSpecification obj = new ObjectSpecification(ObjectSpecification.COMPLEX_SPECIFICATION,
            TestHelper.getURLString("test_files/test.jar"), "com.test.TestComplex", "arraylist", null, 1,
            new ObjectSpecification[] {
                new ObjectSpecification(ObjectSpecification.SIMPLE_SPECIFICATION, null, "int", null, "5", 1, null),
                new ObjectSpecification(ObjectSpecification.SIMPLE_SPECIFICATION, null, "String", null, "aaa",
                    1, null) });

        assertNotNull("The object should not be null.", obj);
    }

    /**
     * Test getJar. The result should be correct.
     */
    public void testGetJar() {
        String jar = instance.getJar();

        assertEquals("The returned value is wrong.", jar.substring(0, 4), "file");
    }

    /**
     * Test getJar. The result should be correct.
     */
    public void testGetType() {
        String type = instance.getType();

        assertEquals("The returned value is wrong.", type, "com.test.TestComplex");
    }

    /**
     * Test getIdentifier. The result should be correct.
     */
    public void testGetIdentifier() {
        String id = instance.getIdentifier();

        assertEquals("The returned value is wrong.", id, "arraylist");
    }

    /**
     * Test getValue. The result should be correct.
     */
    public void testGetValue() {
        String v = instance.getValue();

        assertNull("The returned value is wrong.", v);
    }

    /**
     * Test getValue. The result should be correct.
     */
    public void testGetDimension() {
        int d = instance.getDimension();

        assertEquals("The returned dimension is wrong.", d, 1);
    }

    /**
     * Test getParameters. The result should be correct.
     */
    public void testGetParameters() {
        Object[] param = instance.getParameters();

        assertEquals("The returned value is wrong.", 2, param.length);
    }

    /**
     * Test getSpecType. The result should be correct.
     */
    public void testGetSpecType() {
        String spec = instance.getSpecType();

        assertEquals("The returned value is wrong.", ObjectSpecification.COMPLEX_SPECIFICATION, spec);
    }
}
