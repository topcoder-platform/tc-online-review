/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.objectfactory.impl.accuracytests;

import com.topcoder.util.objectfactory.ObjectFactory;
import com.topcoder.util.objectfactory.ObjectSpecification;
import com.topcoder.util.objectfactory.impl.ConfigurationObjectSpecificationFactory;

/**
 * Accuracy tests for the class <code>ConfigurationObjectSpecificationFactory</code> for accuracy.
 *
 * @author 80x86
 * @version 1.1
 */
public class ConfigurationObjectSpecificationFactoryAccuracyTests extends AccuracyTestBaseClass {
    /** The instance of ConfigManagerSpecificationFactory class used for test. */
    private ConfigurationObjectSpecificationFactory cosf;

    /**
     * Tests getObjectSpecification() method with complex type configuration.
     *
     * @throws Exception
     *             to JUnit from test cases.
     */
    public void testGetObjectSpecification_Complex() throws Exception {
        cosf = new ConfigurationObjectSpecificationFactory(loadConfigurationObjectFromFile(COMPLEX_NAMESPACE,
            COMPLEX_FILE_NAME));

        ObjectSpecification os = cosf.getObjectSpecification("mock1", null);

        assertEquals("The specType is not valid.", ObjectSpecification.COMPLEX_SPECIFICATION, os.getSpecType());
        assertNull("The jar should be null.", os.getJar());
        assertEquals("The type is not valid.", "com.topcoder.util.objectfactory.impl.accuracytests.MockClass1",
            os.getType());
        assertNull("The identifier should be null.", os.getIdentifier());
        assertNull("The value should be null.", os.getValue());
        assertEquals("The dimension is not valid.", 1, os.getDimension());

        checkMockClass1Parameters(os.getParameters());
    }

    /**
     * Checks the parameters of the class com.topcoder.util.objectfactory.accuracytests.MockClass1.
     *
     * @param parameters
     *            the parameters of the class com.topcoder.util.objectfactory.accuracytests.MockClass1.
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
     * @param parameters
     *            the parameters of the class com.topcoder.util.objectfactory.accuracytests.MockClass2.
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
     * @param parameters
     *            the parameters of the class java.lang.StringBuffer.
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
     * @throws Exception
     *             to JUnit from test cases.
     */
    public void testGetObjectSpecification_CharArray() throws Exception {
        cosf = new ConfigurationObjectSpecificationFactory(loadConfigurationObjectFromFile(CHAR_ARRAY_NAMESPACE,
            CHAR_ARRAY_FILE_NAME));

        ObjectSpecification os = cosf.getObjectSpecification("charArray", null);

        assertEquals("The specType is not valid.", ObjectSpecification.ARRAY_SPECIFICATION, os.getSpecType());
        assertNull("The jar should be null.", os.getJar());
        assertEquals("The type is not valid.", ObjectSpecification.CHAR, os.getType());
        assertNull("The identifier should be null.", os.getIdentifier());
        assertNull("The value should be null.", os.getValue());
        assertEquals("The dimension is not valid.", 1, os.getDimension());

        assertNotNull("The parameters is not valid.", os.getParameters());
        assertEquals("should be equal", 6, os.getParameters().length);
    }

    /**
     * Tests getObjectSpecification() with int array type configuration.
     *
     * @throws Exception
     *             to JUnit from test cases.
     */
    public void testGetObjectSpecification_IntArray1() throws Exception {
        cosf = new ConfigurationObjectSpecificationFactory(loadConfigurationObjectFromFile(INT_ARRAY_NAMESPACE,
            INT_ARRAY_FILE_NAME));

        ObjectSpecification os = cosf.getObjectSpecification("intArray1", null);

        assertEquals("The specType is not valid.", ObjectSpecification.ARRAY_SPECIFICATION, os.getSpecType());
        assertNull("The jar should be null.", os.getJar());
        assertEquals("The type is not valid.", ObjectSpecification.INT, os.getType());
        assertNull("The identifier should be null.", os.getIdentifier());
        assertNull("The value should be null.", os.getValue());
        assertEquals("The dimension is not valid.", 1, os.getDimension());

        assertNotNull("The parameters is not valid.", os.getParameters());
        assertEquals("should be equal", 2, os.getParameters().length);
    }

    /**
     * Tests getObjectSpecification() with int array type configuration.
     *
     * @throws Exception
     *             to JUnit from test cases.
     */
    public void testGetObjectSpecification_IntArray2() throws Exception {
        cosf = new ConfigurationObjectSpecificationFactory(loadConfigurationObjectFromFile(INT_ARRAY_NAMESPACE,
            INT_ARRAY_FILE_NAME));

        ObjectSpecification os = cosf.getObjectSpecification("intArray2", null);

        assertEquals("The specType is not valid.", ObjectSpecification.ARRAY_SPECIFICATION, os.getSpecType());
        assertNull("The jar should be null.", os.getJar());
        assertEquals("The type is not valid.", ObjectSpecification.INT, os.getType());
        assertNull("The identifier should be null.", os.getIdentifier());
        assertNull("The value should be null.", os.getValue());
        assertEquals("The dimension is not valid.", 3, os.getDimension());

        assertNotNull("The parameters is not valid.", os.getParameters());
        assertEquals("should be equal", 1, os.getParameters().length);
        assertEquals("should be equal", 2, ((Object[]) os.getParameters()[0]).length);
    }

    /**
     * Tests getObjectSpecification() with String array type configuration.
     *
     * @throws Exception
     *             to JUnit from test cases.
     */
    public void testGetObjectSpecification_StringArray() throws Exception {
        cosf = new ConfigurationObjectSpecificationFactory(loadConfigurationObjectFromFile(STRING_ARRAY_NAMESPACE,
            STRING_ARRAY_FILE_NAME));

        ObjectSpecification os = cosf.getObjectSpecification("StringArray", null);

        assertEquals("The specType is not valid.", ObjectSpecification.ARRAY_SPECIFICATION, os.getSpecType());
        assertNull("The jar should be null.", os.getJar());
        assertEquals("The type is not valid.", ObjectSpecification.STRING, os.getType());
        assertNull("The identifier should be null.", os.getIdentifier());
        assertNull("The value should be null.", os.getValue());
        assertEquals("The dimension is not valid.", 3, os.getDimension());

        assertNotNull("The parameters is not valid.", os.getParameters());
        assertEquals("should be equal", 2, ((Object[]) os.getParameters()[0]).length);
    }

    /**
     * Tests getObjectSpecification() with complex array type configuration.
     *
     * @throws Exception
     *             to JUnit from test cases.
     */
    public void testGetObjectSpecification_ComplexArray() throws Exception {
        cosf = new ConfigurationObjectSpecificationFactory(loadConfigurationObjectFromFile(INTEGER_ARRAY_NAMESPACE,
            INTEGER_ARRAY_FILE_NAME));

        ObjectSpecification os = cosf.getObjectSpecification("IntegerArray", null);

        assertEquals("The specType is not valid.", ObjectSpecification.ARRAY_SPECIFICATION, os.getSpecType());
        assertNull("The jar should be null.", os.getJar());
        assertEquals("The type is not valid.", "java.lang.Integer", os.getType());
        assertNull("The identifier should be null.", os.getIdentifier());
        assertNull("The value should be null.", os.getValue());
        assertEquals("The dimension is not valid.", 1, os.getDimension());

        assertNotNull("The parameters is not valid.", os.getParameters());
    }

    /**
     * <p>
     * Accuracy test of constructor <code>ConfigurationObjectSpecificationFactory(ConfigurationObject)</code>. With
     * valid ConfigurationObject created manually.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCtor_SampleConfigurationObject() throws Exception {
        cosf = new ConfigurationObjectSpecificationFactory(createSampleConfigurationObject());
        assertNotNull("Should create the instance successfully.", cosf);

        StringBuffer obj = (StringBuffer) (new ObjectFactory(cosf)).createObject("frac", "default");
        assertNotNull(obj);
        assertEquals("should be the same value", "test buffer", obj.toString());
    }
}
