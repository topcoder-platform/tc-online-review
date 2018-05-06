/*
 * Copyright (C) 2006-2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.objectfactory.impl.accuracytests;

import com.topcoder.util.objectfactory.ObjectSpecification;
import com.topcoder.util.objectfactory.impl.ConfigManagerSpecificationFactory;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests the class ConfigManagerSpecificationFactory for accuracy.
 *
 * @author TCSDEVELOPER
 * @version 1.1
 */
public class AccuracyTestConfigManagerSpecificationFactory extends AccuracyTestBaseClass {
    /** The instance of ConfigManagerSpecificationFactory class used for test. */
    private ConfigManagerSpecificationFactory cmsf;

    /**
     * Returns the suit to run the test.
     *
     * @return suite to run the test
     */
    public static Test suite() {
        return new TestSuite(AccuracyTestConfigManagerSpecificationFactory.class);
    }

    /**
     * Tests getObjectSpecification() method with complex type configuration.
     *
     * @throws Exception to JUnit from test cases.
     */
    public void testGetObjectSpecificationComplex() throws Exception {
        cmsf = new ConfigManagerSpecificationFactory(COMPLEX_NAMESPACE);

        ObjectSpecification os = cmsf.getObjectSpecification("mock1", null);

        assertEquals("The specType is not valid.", ObjectSpecification.COMPLEX_SPECIFICATION, os.getSpecType());
        assertNull("The jar should be null.", os.getJar());
        assertEquals("The type is not valid.", "com.topcoder.util.objectfactory.impl.accuracytests.MockClass1", os.getType());
        assertNull("The identifier should be null.", os.getIdentifier());
        assertNull("The value should be null.", os.getValue());
        assertEquals("The dimension is not valid.", 1, os.getDimension());

        checkMockClass1Parameters(os.getParameters());
    }

    /**
     * Checks the parameters of the class com.topcoder.util.objectfactory.accuracytests.MockClass1.
     *
     * @param parameters the parameters of the class com.topcoder.util.objectfactory.accuracytests.MockClass1.
     */
    private void checkMockClass1Parameters(Object[] parameters) {
        assertTrue("There should be 3 parameters.", parameters.length == 3);

        ObjectSpecification param1 = (ObjectSpecification) parameters[0];
        ObjectSpecification param2 = (ObjectSpecification) parameters[1];
        ObjectSpecification param3 = (ObjectSpecification) parameters[2];

        assertEquals("The specType is not valid.", ObjectSpecification.SIMPLE_SPECIFICATION, param1.getSpecType());
        assertNull("The jar should be null.", param1.getJar());
        assertEquals("The type is not valid.", ObjectSpecification.INT, param1.getType());
        assertNull("The identifier should be null.", param1.getIdentifier());
        assertEquals("The value is not valid.", "7", param1.getValue());
        assertEquals("The dimension is not valid.", 1, param1.getDimension());
        assertNull("The parameters should be null.", param1.getParameters());

        assertEquals("The specType is not valid.", ObjectSpecification.SIMPLE_SPECIFICATION, param2.getSpecType());
        assertNull("The jar should be null.", param2.getJar());
        assertEquals("The type is not valid.", ObjectSpecification.STRING, param2.getType());
        assertNull("The identifier should be null.", param2.getIdentifier());
        assertEquals("The value is not valid.", "TEST", param2.getValue());
        assertEquals("The dimension is not valid.", 1, param2.getDimension());
        assertNull("The parameters should be null.", param2.getParameters());

        assertEquals("The specType is not valid.", ObjectSpecification.COMPLEX_SPECIFICATION, param3.getSpecType());
        assertNull("The jar should be null.", param3.getJar());
        assertEquals("The type is not valid.", "com.topcoder.util.objectfactory.impl.accuracytests.MockClass2",
            param3.getType());
        assertNull("The identifier should be null.", param3.getIdentifier());
        assertNull("The value should be null.", param3.getValue());
        assertEquals("The dimension is not valid.", 1, param3.getDimension());

        checkMockClass2Parameters(param3.getParameters());
    }

    /**
     * Checks the parameters of the class com.topcoder.util.objectfactory.accuracytests.MockClass2.
     *
     * @param parameters the parameters of the class com.topcoder.util.objectfactory.accuracytests.MockClass2.
     */
    private void checkMockClass2Parameters(Object[] parameters) {
        assertTrue("There should be 3 parameters.", parameters.length == 3);

        ObjectSpecification param1 = (ObjectSpecification) parameters[0];
        ObjectSpecification param2 = (ObjectSpecification) parameters[1];
        ObjectSpecification param3 = (ObjectSpecification) parameters[2];

        assertEquals("The specType is not valid.", ObjectSpecification.COMPLEX_SPECIFICATION, param1.getSpecType());
        assertNull("The jar should be null.", param1.getJar());
        assertEquals("The type is not valid.", "java.lang.StringBuffer", param1.getType());
        assertNull("The identifier should be null.", param1.getIdentifier());
        assertNull("The value should be null.", param1.getValue());
        assertEquals("The dimension is not valid.", 1, param1.getDimension());

        checkStringBufferParameters(param1.getParameters());

        assertEquals("The specType is not valid.", ObjectSpecification.NULL_SPECIFICATION, param2.getSpecType());
        assertNull("The jar should be null.", param2.getJar());
        assertEquals("The type is not valid.", "java.util.ArrayList", param2.getType());
        assertNull("The identifier should be null.", param2.getIdentifier());
        assertNull("The value should be null.", param2.getValue());
        assertEquals("The dimension is not valid.", 1, param2.getDimension());
        assertNull("The parameters should be null.", param2.getParameters());

        assertEquals("The specType is not valid.", ObjectSpecification.COMPLEX_SPECIFICATION, param3.getSpecType());
        assertNull("The jar should be null.", param3.getJar());
        assertEquals("The type is not valid.", "java.util.ArrayList", param3.getType());
        assertNull("The identifier should be null.", param3.getIdentifier());
        assertNull("The value should be null.", param3.getValue());
        assertEquals("The dimension is not valid.", 1, param3.getDimension());
    }

    /**
     * Checks the parameters of the class java.lang.StringBuffer.
     *
     * @param parameters the parameters of the class java.lang.StringBuffer.
     */
    private void checkStringBufferParameters(Object[] parameters) {
        assertTrue("There should be 1 parameters.", parameters.length == 1);

        ObjectSpecification param1 = (ObjectSpecification) parameters[0];

        assertEquals("The specType is not valid.", ObjectSpecification.SIMPLE_SPECIFICATION, param1.getSpecType());
        assertNull("The jar should be null.", param1.getJar());
        assertEquals("The type is not valid.", ObjectSpecification.STRING, param1.getType());
        assertNull("The identifier should be null.", param1.getIdentifier());
        assertEquals("The value is not valid.", "StringBuffer", param1.getValue());
        assertEquals("The dimension is not valid.", 1, param1.getDimension());
        assertNull("The parameters should be null.", param1.getParameters());
    }

    /**
     * Tests getObjectSpecification() with char array type configuration.
     *
     * @throws Exception to JUnit from test cases.
     */
    public void testGetObjectSpecificationCharArray() throws Exception {
        cmsf = new ConfigManagerSpecificationFactory(CHAR_ARRAY_NAMESPACE);

        ObjectSpecification os = cmsf.getObjectSpecification("charArray", null);

        assertEquals("The specType is not valid.", ObjectSpecification.ARRAY_SPECIFICATION, os.getSpecType());
        assertNull("The jar should be null.", os.getJar());
        assertEquals("The type is not valid.", ObjectSpecification.CHAR, os.getType());
        assertNull("The identifier should be null.", os.getIdentifier());
        assertNull("The value should be null.", os.getValue());
        assertEquals("The dimension is not valid.", 1, os.getDimension());

        assertNotNull("The parameters is not valid.", os.getParameters());
    }

    /**
     * Tests getObjectSpecification() with int array type configuration.
     *
     * @throws Exception to JUnit from test cases.
     */
    public void testGetObjectSpecificationIntArray1() throws Exception {
        cmsf = new ConfigManagerSpecificationFactory(INT_ARRAY_NAMESPACE);

        ObjectSpecification os = cmsf.getObjectSpecification("intArray1", null);

        assertEquals("The specType is not valid.", ObjectSpecification.ARRAY_SPECIFICATION, os.getSpecType());
        assertNull("The jar should be null.", os.getJar());
        assertEquals("The type is not valid.", ObjectSpecification.INT, os.getType());
        assertNull("The identifier should be null.", os.getIdentifier());
        assertNull("The value should be null.", os.getValue());
        assertEquals("The dimension is not valid.", 1, os.getDimension());

        assertNotNull("The parameters is not valid.", os.getParameters());
    }

    /**
     * Tests getObjectSpecification() with int array type configuration.
     *
     * @throws Exception to JUnit from test cases.
     */
    public void testGetObjectSpecificationIntArray2() throws Exception {
        cmsf = new ConfigManagerSpecificationFactory(INT_ARRAY_NAMESPACE);

        ObjectSpecification os = cmsf.getObjectSpecification("intArray2", null);

        assertEquals("The specType is not valid.", ObjectSpecification.ARRAY_SPECIFICATION, os.getSpecType());
        assertNull("The jar should be null.", os.getJar());
        assertEquals("The type is not valid.", ObjectSpecification.INT, os.getType());
        assertNull("The identifier should be null.", os.getIdentifier());
        assertNull("The value should be null.", os.getValue());
        assertEquals("The dimension is not valid.", 3, os.getDimension());

        assertNotNull("The parameters is not valid.", os.getParameters());
    }

    /**
     * Tests getObjectSpecification() with String array type configuration.
     *
     * @throws Exception to JUnit from test cases.
     */
    public void testGetObjectSpecificationStringArray() throws Exception {
        cmsf = new ConfigManagerSpecificationFactory(STRING_ARRAY_NAMESPACE);

        ObjectSpecification os = cmsf.getObjectSpecification("StringArray", null);

        assertEquals("The specType is not valid.", ObjectSpecification.ARRAY_SPECIFICATION, os.getSpecType());
        assertNull("The jar should be null.", os.getJar());
        assertEquals("The type is not valid.", ObjectSpecification.STRING, os.getType());
        assertNull("The identifier should be null.", os.getIdentifier());
        assertNull("The value should be null.", os.getValue());
        assertEquals("The dimension is not valid.", 3, os.getDimension());

        assertNotNull("The parameters is not valid.", os.getParameters());
    }

    /**
     * Tests getObjectSpecification() with complex array type configuration.
     *
     * @throws Exception to JUnit from test cases.
     */
    public void testGetObjectSpecificationComplexArray() throws Exception {
        cmsf = new ConfigManagerSpecificationFactory(INTEGER_ARRAY_NAMESPACE);

        ObjectSpecification os = cmsf.getObjectSpecification("IntegerArray", null);

        assertEquals("The specType is not valid.", ObjectSpecification.ARRAY_SPECIFICATION, os.getSpecType());
        assertNull("The jar should be null.", os.getJar());
        assertEquals("The type is not valid.", "java.lang.Integer", os.getType());
        assertNull("The identifier should be null.", os.getIdentifier());
        assertNull("The value should be null.", os.getValue());
        assertEquals("The dimension is not valid.", 1, os.getDimension());

        assertNotNull("The parameters is not valid.", os.getParameters());
    }
}
