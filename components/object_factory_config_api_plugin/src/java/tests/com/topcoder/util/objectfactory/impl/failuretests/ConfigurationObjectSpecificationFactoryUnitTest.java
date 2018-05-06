/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.objectfactory.impl.failuretests;

import junit.framework.TestCase;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.defaults.DefaultConfigurationObject;
import com.topcoder.util.objectfactory.ObjectSpecification;
import com.topcoder.util.objectfactory.UnknownReferenceException;
import com.topcoder.util.objectfactory.impl.ConfigurationObjectSpecificationFactory;
import com.topcoder.util.objectfactory.impl.IllegalReferenceException;

/**
 * <p>
 * Unit test cases for class <code>ConfigurationObjectSepcificationFactory</code>.
 * </p>
 * 
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class ConfigurationObjectSpecificationFactoryUnitTest extends TestCase {
    /**
     * <p>
     * Indicates <code>Object</code> type.
     * </p>
     */
    private final static String TYPE_OBJECT = "java.lang.Object";

    /**
     * <p>
     * Indicates <code>Number</code> type.
     * </p>
     */
    private final static String TYPE_NUMBER = "java.lang.Number";

    /**
     * <p>
     * Indicates <code>int</code> type.
     * </p>
     */
    private final static String TYPE_INT = "int";

    /**
     * <p>
     * Indicates <code>String</code> type.
     * </p>
     */
    private final static String TYPE_SIMPLE_STRING = "String";

    /**
     * <p>
     * Indicates <code>String</code> type.
     * </p>
     */
    private final static String TYPE_FULL_STRING = "java.lang.String";

    /**
     * <p>
     * Property type.
     * </p>
     */
    private final static String PROPERTY_TYPE = "type";

    /**
     * <p>
     * Property name.
     * </p>
     */
    private final static String PROPERTY_NAME = "name";

    /**
     * <p>
     * Property arrayType.
     * </p>
     */
    private final static String PROPERTY_ARRAY_TYPE = "arrayType";

    /**
     * <p>
     * Property dimension.
     * </p>
     */
    private final static String PROPERTY_DIMENSION = "dimension";

    /**
     * <p>
     * Property values.
     * </p>
     */
    private final static String PROPERTY_VALUES = "values";

    /**
     * <p>
     * Property value.
     * </p>
     */
    private final static String PROPERTY_VALUE = "value";

    /**
     * <p>
     * <code>ConfigurationObjectSpecificationFactory</code> instance used for testing.
     * </p>
     */
    private ConfigurationObjectSpecificationFactory specificationFactory;

    /**
     * <p>
     * <code>ConfigurationObject</code> representing the root object of configuration. Used for
     * testing.
     * </p>
     */
    private ConfigurationObject root;

    /**
     * <p>
     * Sets up environment.
     * </p>
     */
    protected void setUp() {
        root = new DefaultConfigurationObject("sample_configuration");
    }

    /**
     * <p>
     * Helper method that creates configuration object.
     * </p>
     * 
     * @param name the name of object to create.
     * @param type the type of object to create.
     * @return created ConfigurationObject.
     * @throws Exception exception to JUnit.
     */
    private ConfigurationObject createObject(String name, String type) throws Exception {
        ConfigurationObject object = new DefaultConfigurationObject(name);
        object.setPropertyValue(PROPERTY_TYPE, type);

        return object;
    }

    /**
     * <p>
     * Helper method that creates configuration object for array.
     * </p>
     * 
     * @param name the name of array to create.
     * @param arrayType the type of array elements.
     * @param dimension the dimension of array to create.
     * @param values the values of array to create.
     * @return created ConfigurationObject.
     * @throws Exception exception to JUnit.
     */
    private ConfigurationObject createArray(String name, String arrayType, String dimension,
        String values) throws Exception {
        ConfigurationObject array = new DefaultConfigurationObject(name);
        array.setPropertyValue(PROPERTY_ARRAY_TYPE, arrayType);
        array.setPropertyValue(PROPERTY_DIMENSION, dimension);
        array.setPropertyValue(PROPERTY_VALUES, values);

        return array;
    }

    /**
     * <p>
     * Helper method that creates configuration object for parameters.
     * </p>
     * 
     * @param num param number.
     * @param type the type of param.
     * @param value the value of param.
     * @return created ConfigurationObject.
     * @throws Exception exception to JUnit.
     */
    private ConfigurationObject createParam(int num, String type, String value) throws Exception {
        ConfigurationObject param = new DefaultConfigurationObject("param" + num);
        param.setPropertyValue(PROPERTY_TYPE, type);
        param.setPropertyValue(PROPERTY_VALUE, value);

        return param;
    }

    /**
     * <p>
     * Helper method that creates configuration object for parameter reference.
     * </p>
     * 
     * @param num param number.
     * @param name the reference name.
     * @return created ConfigurationObject.
     * @throws Exception exception to JUnit.
     */
    private ConfigurationObject createParamReference(int num, String name) throws Exception {
        ConfigurationObject param = new DefaultConfigurationObject("param" + num);
        param.setPropertyValue(PROPERTY_NAME, name);

        return param;
    }

    /**
     * <p>
     * Test of constructor <code>ConfigurationObjectSpecificationFactory</code>.
     * </p>
     * 
     * @throws Exception exception to JUnit.
     */
    public void testConstructor() throws Exception {
        ConfigurationObject object1 = createObject("object:ob1", TYPE_OBJECT);
        ConfigurationObject object2 = createObject("object:ob2", TYPE_NUMBER);
        ConfigurationObject object3 = createObject("something", TYPE_OBJECT);

        ConfigurationObject params1 = new DefaultConfigurationObject("params");
        ConfigurationObject params2 = new DefaultConfigurationObject("params");

        params1.addChild(createParamReference(1, "object:ob2"));
        params1.addChild(createParamReference(2, "something"));

        params2.addChild(createParamReference(1, "something"));

        object1.addChild(params1);
        object2.addChild(params2);

        root.addChild(object3);
        root.addChild(object1);
        root.addChild(object2);

        specificationFactory = new ConfigurationObjectSpecificationFactory(root);

        ObjectSpecification objectSpecification1 = specificationFactory.getObjectSpecification(
            "object", "ob1");

        assertEquals("SpecType should be complex.", ObjectSpecification.COMPLEX_SPECIFICATION,
            objectSpecification1.getSpecType());

        Object[] params = objectSpecification1.getParameters();
        ObjectSpecification param1 = (ObjectSpecification) params[0];
        ObjectSpecification param2 = (ObjectSpecification) params[1];

        assertEquals("Object 'ob1' should have 2 params.", 2, params.length);
        assertTrue("SpecType of params should be complex.", param1.getSpecType().equals(
            ObjectSpecification.COMPLEX_SPECIFICATION)
            && param2.getSpecType().equals(ObjectSpecification.COMPLEX_SPECIFICATION));
        assertEquals("Element should be object:ob2.", specificationFactory.getObjectSpecification(
            "object", "ob2"), param1);
        assertEquals("Element should be something.", specificationFactory.getObjectSpecification(
            "something", null), param2);

        params = param1.getParameters();
        param1 = (ObjectSpecification) params[0];
        assertEquals("Object 'ob2' should have 1 param.", 1, params.length);
        assertEquals("SpecType of param should be complex.",
            ObjectSpecification.COMPLEX_SPECIFICATION, param1.getSpecType());
        assertEquals("Element should be something.", specificationFactory.getObjectSpecification(
            "something", null), param1);
    }

    /**
     * <p>
     * Test of constructor <code>ConfigurationObjectSpecificationFactory</code> with
     * <code>null</code> argument.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     * 
     * @throws Exception exception to JUnit.
     */
    public void testConstructorWithNullArgument() throws Exception {
        try {
            new ConfigurationObjectSpecificationFactory(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * <p>
     * Test of constructor <code>ConfigurationObjectSpecificationFactory</code> with complex type
     * with parameters.
     * </p>
     * 
     * @throws Exception exception to JUnit.
     */
    public void testConstructorWithComplexTypeWithParams() throws Exception {
        ConfigurationObject configurationObject = createObject("name", TYPE_OBJECT);
        ConfigurationObject paramsChild = new DefaultConfigurationObject("params");
        paramsChild.addChild(createParam(1, TYPE_INT, "2"));
        paramsChild.addChild(createParam(2, TYPE_INT, "1"));
        configurationObject.addChild(paramsChild);
        root.addChild(configurationObject);

        specificationFactory = new ConfigurationObjectSpecificationFactory(root);

        ObjectSpecification object = specificationFactory.getObjectSpecification("name", null);

        assertEquals("SpecType should be complex.", ObjectSpecification.COMPLEX_SPECIFICATION,
            object.getSpecType());
        assertNull("Identifier should be null.", object.getIdentifier());
        assertEquals("Type should be 'A'.", TYPE_OBJECT, object.getType());

        Object[] params = object.getParameters();
        ObjectSpecification param1 = (ObjectSpecification) params[0];
        ObjectSpecification param2 = (ObjectSpecification) params[1];

        assertEquals("Object should have 2 params.", 2, params.length);
        assertTrue("SpecType of array elements should be simple.", param1.getSpecType().equals(
            ObjectSpecification.SIMPLE_SPECIFICATION)
            && param2.getSpecType().equals(ObjectSpecification.SIMPLE_SPECIFICATION));
        assertTrue("Elements should be 'int'.", param1.getType().equals(TYPE_INT)
            && param2.getType().equals(TYPE_INT));
        assertEquals("Element should be 1.", "2", param1.getValue());
        assertEquals("Element should be 2.", "1", param2.getValue());
    }

    /**
     * <p>
     * Test of constructor <code>ConfigurationObjectSpecificationFactory</code> with complex type
     * with <code>null</code> parameter.
     * </p>
     * 
     * @throws Exception exception to JUnit.
     */
    public void testConstructorWithComplexTypeWithNullParam() throws Exception {
        ConfigurationObject configurationObject = createObject("name", TYPE_OBJECT);
        ConfigurationObject paramsChild = new DefaultConfigurationObject("params");
        ConfigurationObject param = new DefaultConfigurationObject("param1");
        param.setPropertyValue(PROPERTY_TYPE, TYPE_OBJECT);
        paramsChild.addChild(param);
        configurationObject.addChild(paramsChild);
        root.addChild(configurationObject);

        specificationFactory = new ConfigurationObjectSpecificationFactory(root);

        ObjectSpecification object = specificationFactory.getObjectSpecification("name", null);

        assertEquals("SpecType should be complex.", ObjectSpecification.COMPLEX_SPECIFICATION,
            object.getSpecType());
        assertNull("Identifier should be null.", object.getIdentifier());
        assertEquals("Type should be 'A'.", TYPE_OBJECT, object.getType());

        Object[] params = object.getParameters();
        ObjectSpecification param1 = (ObjectSpecification) params[0];

        assertEquals("Object should have 1 param.", 1, params.length);
        assertEquals("SpecType of array elements should be null.",
            ObjectSpecification.NULL_SPECIFICATION, param1.getSpecType());
    }

    /**
     * <p>
     * Test of constructor <code>ConfigurationObjectSpecificationFactory</code> with complex type
     * with <code>null</code> simple parameter. <code>IllegalReferenceException</code> is
     * expected.
     * </p>
     * 
     * @throws Exception exception to JUnit.
     */
    public void testConstructorWithComplexTypeWithNullSimpleParam() throws Exception {
        ConfigurationObject configurationObject = createObject("name", TYPE_OBJECT);
        ConfigurationObject paramsChild = new DefaultConfigurationObject("params");
        ConfigurationObject param = new DefaultConfigurationObject("param1");
        param.setPropertyValue(PROPERTY_TYPE, TYPE_INT);
        paramsChild.addChild(param);
        configurationObject.addChild(paramsChild);
        root.addChild(configurationObject);

        try {
            specificationFactory = new ConfigurationObjectSpecificationFactory(root);
            fail("IllegalReferenceException is expected.");
        } catch (IllegalReferenceException e) {
            // ok
        }
    }

    /**
     * <p>
     * Test of constructor <code>ConfigurationObjectSpecificationFactory</code> with complex type
     * with malformed parameter. <code>IllegalReferenceException</code> is expected.
     * </p>
     * 
     * @throws Exception exception to JUnit.
     */
    public void testConstructorWithComplexTypeWithMalformedParam1() throws Exception {
        ConfigurationObject configurationObject = createObject("name", TYPE_OBJECT);
        ConfigurationObject paramsChild = new DefaultConfigurationObject("params");
        ConfigurationObject param = new DefaultConfigurationObject("param1");
        paramsChild.addChild(param);
        configurationObject.addChild(paramsChild);
        root.addChild(configurationObject);

        try {
            specificationFactory = new ConfigurationObjectSpecificationFactory(root);
            fail("IllegalReferenceException is expected.");
        } catch (IllegalReferenceException e) {
            // ok
        }
    }

    /**
     * <p>
     * Test of constructor <code>ConfigurationObjectSpecificationFactory</code> with complex type
     * with malformed parameter. <code>IllegalReferenceException</code> is expected.
     * </p>
     * 
     * @throws Exception exception to JUnit.
     */
    public void testConstructorWithComplexTypeWithMalformedParam2() throws Exception {
        ConfigurationObject configurationObject = createObject("name", TYPE_OBJECT);
        ConfigurationObject paramsChild = new DefaultConfigurationObject("params");
        ConfigurationObject param = new DefaultConfigurationObject("param1");
        param.setPropertyValue(PROPERTY_TYPE, TYPE_INT);
        param.setPropertyValue(PROPERTY_NAME, "name");
        paramsChild.addChild(param);
        configurationObject.addChild(paramsChild);
        root.addChild(configurationObject);

        try {
            specificationFactory = new ConfigurationObjectSpecificationFactory(root);
            fail("IllegalReferenceException is expected.");
        } catch (IllegalReferenceException e) {
            // ok
        }
    }

    /**
     * <p>
     * Test of constructor <code>ConfigurationObjectSpecificationFactory</code> with malformed
     * type specification. <code>IllegalReferenceException</code> is expected.
     * </p>
     * 
     * @throws Exception exception to JUnit.
     */
    public void testConstructorWithMalformedTypeSpecification1() throws Exception {
        ConfigurationObject object = createObject("name", TYPE_OBJECT);
        object.setPropertyValue(PROPERTY_DIMENSION, "1");
        root.addChild(object);

        try {
            specificationFactory = new ConfigurationObjectSpecificationFactory(root);
            fail("IllegalReferenceException is expected.");
        } catch (IllegalReferenceException e) {
            // ok
        }
    }

    /**
     * <p>
     * Test of constructor <code>ConfigurationObjectSpecificationFactory</code> with malformed
     * type specification. <code>IllegalReferenceException</code> is expected.
     * </p>
     * 
     * @throws Exception exception to JUnit.
     */
    public void testConstructorWithMalformedTypeSpecification2() throws Exception {
        ConfigurationObject object = createObject("name", TYPE_OBJECT);
        object.setPropertyValue(PROPERTY_ARRAY_TYPE, TYPE_OBJECT);
        root.addChild(object);

        try {
            specificationFactory = new ConfigurationObjectSpecificationFactory(root);
            fail("IllegalReferenceException is expected.");
        } catch (IllegalReferenceException e) {
            // ok
        }
    }

    /**
     * <p>
     * Test of constructor <code>ConfigurationObjectSpecificationFactory</code> with malformed
     * type specification. <code>IllegalReferenceException</code> is expected.
     * </p>
     * 
     * @throws Exception exception to JUnit.
     */
    public void testConstructorWithMalformedTypeSpecification3() throws Exception {
        ConfigurationObject object = createObject("name", TYPE_OBJECT);
        object.setPropertyValue(PROPERTY_VALUES, "{null,null}");
        root.addChild(object);

        try {
            specificationFactory = new ConfigurationObjectSpecificationFactory(root);
            fail("IllegalReferenceException is expected.");
        } catch (IllegalReferenceException e) {
            // ok
        }
    }

    /**
     * <p>
     * Test of constructor <code>ConfigurationObjectSpecificationFactory</code> with malformed
     * type specification. <code>IllegalReferenceException</code> is expected.
     * </p>
     * 
     * @throws Exception exception to JUnit.
     */
    public void testConstructorWithMalformedTypeSpecification4() throws Exception {
        root.addChild(new DefaultConfigurationObject("name"));

        try {
            specificationFactory = new ConfigurationObjectSpecificationFactory(root);
            fail("IllegalReferenceException is expected.");
        } catch (IllegalReferenceException e) {
            // ok
        }
    }

    /**
     * <p>
     * Test of constructor <code>ConfigurationObjectSpecificationFactory</code> with malformed
     * type specification. <code>IllegalReferenceException</code> is expected.
     * </p>
     * 
     * @throws Exception exception to JUnit.
     */
    public void testConstructorWithMalformedTypeSpecification5() throws Exception {
        root.addChild(createArray("name", TYPE_OBJECT, null, "{1, 2}"));

        try {
            specificationFactory = new ConfigurationObjectSpecificationFactory(root);
            fail("IllegalReferenceException is expected.");
        } catch (IllegalReferenceException e) {
            // ok
        }
    }

    /**
     * <p>
     * Test of constructor <code>ConfigurationObjectSpecificationFactory</code> with malformed
     * type specification. <code>IllegalReferenceException</code> is expected.
     * </p>
     * 
     * @throws Exception exception to JUnit.
     */
    public void testConstructorWithMalformedTypeSpecification6() throws Exception {
        root.addChild(createArray("name", TYPE_OBJECT, "1", null));

        try {
            specificationFactory = new ConfigurationObjectSpecificationFactory(root);
            fail("IllegalReferenceException is expected.");
        } catch (IllegalReferenceException e) {
            // ok
        }
    }

    /**
     * <p>
     * Test of constructor <code>ConfigurationObjectSpecificationFactory</code> with malformed
     * type specification. <code>IllegalReferenceException</code> is expected.
     * </p>
     * 
     * @throws Exception exception to JUnit.
     */
    public void testConstructorWithMalformedTypeSpecification7() throws Exception {
        ConfigurationObject array = createArray("name", TYPE_OBJECT, "1", "{1, 2}");
        array.addChild(new DefaultConfigurationObject("params"));
        root.addChild(array);

        try {
            specificationFactory = new ConfigurationObjectSpecificationFactory(root);
            fail("IllegalReferenceException is expected.");
        } catch (IllegalReferenceException e) {
            // ok
        }
    }

    /**
     * <p>
     * Test of constructor <code>ConfigurationObjectSpecificationFactory</code> with malformed
     * type specification. <code>IllegalReferenceException</code> is expected.
     * </p>
     * 
     * @throws Exception exception to JUnit.
     */
    public void testConstructorWithMalformedTypeSpecification8() throws Exception {
        root.addChild(createObject("name", TYPE_INT));

        try {
            specificationFactory = new ConfigurationObjectSpecificationFactory(root);
            fail("IllegalReferenceException is expected.");
        } catch (IllegalReferenceException e) {
            // ok
        }
    }

    /**
     * <p>
     * Test of constructor <code>ConfigurationObjectSpecificationFactory</code> with malformed
     * type specification. <code>IllegalReferenceException</code> is expected.
     * </p>
     * 
     * @throws Exception exception to JUnit.
     */
    public void testConstructorWithNotFoundReference() throws Exception {
        try {
            ConfigurationObject object1 = createObject("object:ob1", TYPE_OBJECT);
            ConfigurationObject params1 = new DefaultConfigurationObject("params");
            params1.addChild(createParamReference(1, "object:ob2"));
            object1.addChild(params1);

            root.addChild(object1);

            specificationFactory = new ConfigurationObjectSpecificationFactory(root);
            fail("IllegalReferenceException is expected.");
        } catch (IllegalReferenceException e) {
            // ok
        }
    }

    /**
     * <p>
     * Test of constructor <code>ConfigurationObjectSpecificationFactory</code> with cycled
     * references. <code>IllegalReferenceException</code> is expected.
     * </p>
     * 
     * @throws Exception exception to JUnit.
     */
    public void testConstructorWithCycledReferences1() throws Exception {
        try {
            ConfigurationObject object1 = createObject("object:ob1", TYPE_OBJECT);
            ConfigurationObject params1 = new DefaultConfigurationObject("params");
            params1.addChild(createParamReference(1, "object:ob1"));
            object1.addChild(params1);

            root.addChild(object1);

            specificationFactory = new ConfigurationObjectSpecificationFactory(root);
            fail("IllegalReferenceException is expected.");
        } catch (IllegalReferenceException e) {
            // ok
        }
    }

    /**
     * <p>
     * Test of constructor <code>ConfigurationObjectSpecificationFactory</code> with cycled
     * references. <code>IllegalReferenceException</code> is expected.
     * </p>
     * 
     * @throws Exception exception to JUnit.
     */
    public void testConstructorWithCycledReferences2() throws Exception {
        try {
            ConfigurationObject object1 = createObject("object:ob1", TYPE_OBJECT);
            ConfigurationObject params1 = new DefaultConfigurationObject("params");
            params1.addChild(createParamReference(1, "object:ob2"));
            object1.addChild(params1);

            ConfigurationObject object2 = createObject("object:ob2", TYPE_OBJECT);
            ConfigurationObject params2 = new DefaultConfigurationObject("params");
            params2.addChild(createParamReference(1, "object:ob1"));
            object2.addChild(params2);

            root.addChild(object1);
            root.addChild(object2);

            specificationFactory = new ConfigurationObjectSpecificationFactory(root);
            fail("IllegalReferenceException is expected.");
        } catch (IllegalReferenceException e) {
            // ok
        }
    }

    /**
     * <p>
     * Test of constructor <code>ConfigurationObjectSpecificationFactory</code> with single
     * dimension simple type array.
     * </p>
     * 
     * @throws Exception exception to JUnit.
     */
    public void testConstructorWithSingleDimensionSimpleTypeArray() throws Exception {
        root.addChild(createArray("array1", TYPE_INT, "1", "{1,2}"));

        specificationFactory = new ConfigurationObjectSpecificationFactory(root);

        ObjectSpecification array = specificationFactory.getObjectSpecification("array1", null);

        assertEquals("SpecType should be array.", ObjectSpecification.ARRAY_SPECIFICATION, array
            .getSpecType());
        assertNull("Identifier should be null.", array.getIdentifier());
        assertEquals("Type should be 'int'.", TYPE_INT, array.getType());
        assertEquals("Dimension should be 1.", 1, array.getDimension());

        Object[] params = array.getParameters();
        ObjectSpecification param1 = (ObjectSpecification) params[0];
        ObjectSpecification param2 = (ObjectSpecification) params[1];

        assertEquals("Array should have 2 elements.", 2, params.length);
        assertTrue("SpecType of array elements should be simple.", param1.getSpecType().equals(
            ObjectSpecification.SIMPLE_SPECIFICATION)
            && param2.getSpecType().equals(ObjectSpecification.SIMPLE_SPECIFICATION));
        assertTrue("Elements should be 'int'.", param1.getType().equals(TYPE_INT)
            && param2.getType().equals(TYPE_INT));
        assertEquals("Element should be 1.", "1", param1.getValue());
        assertEquals("Element should be 2.", "2", param2.getValue());
    }

    /**
     * <p>
     * Test of constructor <code>ConfigurationObjectSpecificationFactory</code> with multiple
     * dimension simple type array.
     * </p>
     * 
     * @throws Exception exception to JUnit.
     */
    public void testConstructorWithMultiDimensionSimpleTypeArray() throws Exception {
        root.addChild(createArray("array1", TYPE_INT, "2", "{{0,1}, {2,3}}"));

        specificationFactory = new ConfigurationObjectSpecificationFactory(root);

        ObjectSpecification array = specificationFactory.getObjectSpecification("array1", null);

        assertEquals("SpecType should be array.", ObjectSpecification.ARRAY_SPECIFICATION, array
            .getSpecType());
        assertNull("Identifier should be null.", array.getIdentifier());
        assertEquals("Type should be 'int'.", TYPE_INT, array.getType());
        assertEquals("Dimension should be 2.", 2, array.getDimension());

        Object[] array1 = array.getParameters();
        Object[] subArray1 = (Object[]) array1[0];
        Object[] subArray2 = (Object[]) array1[1];

        ObjectSpecification[] values = {(ObjectSpecification) subArray1[0],
            (ObjectSpecification) subArray1[1], (ObjectSpecification) subArray2[0],
            (ObjectSpecification) subArray2[1]};

        assertEquals("Array should have 2 elements.", 2, array1.length);
        assertEquals("Array[0] should have 2 elements.", 2, subArray1.length);
        assertEquals("Array[1] should have 2 elements.", 2, subArray2.length);

        for (int i = 0; i < 4; i++) {
            assertEquals("SpecType of array elements should be simple.",
                ObjectSpecification.SIMPLE_SPECIFICATION, values[i].getSpecType());
            assertEquals("Elements should be 'int'.", TYPE_INT, values[i].getType());
            assertEquals("Wrong element value.", (String) ("" + i), values[i].getValue());
        }
    }

    /**
     * <p>
     * Test of constructor <code>ConfigurationObjectSpecificationFactory</code> with simple type
     * array with <code>null</code> element. <code>IllegalReferenceException</code> is expected.
     * </p>
     * 
     * @throws Exception exception to JUnit.
     */
    public void testConstructorWithNullSimpleTypeArray() throws Exception {
        root.addChild(createArray("array1", TYPE_INT, "2", "{{0,1}, {2,null}}"));

        try {
            specificationFactory = new ConfigurationObjectSpecificationFactory(root);
            fail("IllegalReferenceException is expected.");
        } catch (IllegalReferenceException e) {
            // ok
        }
    }

    /**
     * <p>
     * Test of constructor <code>ConfigurationObjectSpecificationFactory</code> with single
     * dimension complex type array.
     * </p>
     * 
     * @throws Exception exception to JUnit.
     */
    public void testConstructorWithSingleDimensionComplexTypeArray() throws Exception {
        root
            .addChild(createArray("array1", TYPE_OBJECT, "1", "{object:ob1,object:ob2, object:ob1}"));
        root.addChild(createObject("object:ob1", TYPE_OBJECT));
        root.addChild(createObject("object:ob2", TYPE_OBJECT));

        specificationFactory = new ConfigurationObjectSpecificationFactory(root);

        ObjectSpecification array = specificationFactory.getObjectSpecification("array1", null);

        assertEquals("SpecType should be array.", ObjectSpecification.ARRAY_SPECIFICATION, array
            .getSpecType());
        assertNull("Identifier should be null.", array.getIdentifier());
        assertEquals("Type should be 'A'.", TYPE_OBJECT, array.getType());
        assertEquals("Dimension should be 1.", 1, array.getDimension());

        Object[] array1 = array.getParameters();

        assertEquals("array1[0] should be object:ob1.", specificationFactory
            .getObjectSpecification("object", "ob1"), array1[0]);
        assertEquals("array1[1] should be object:ob2.", specificationFactory
            .getObjectSpecification("object", "ob2"), array1[1]);
        assertEquals("array1[2] should be object:ob1.", specificationFactory
            .getObjectSpecification("object", "ob1"), array1[2]);
    }

    /**
     * <p>
     * Test of constructor <code>ConfigurationObjectSpecificationFactory</code> with multiple
     * dimension complex type array.
     * </p>
     * 
     * @throws Exception exception to JUnit.
     */
    public void testConstructorWithMultiDimensionComplexTypeArray() throws Exception {
        root.addChild(createArray("array1", TYPE_OBJECT, "2",
            "{{object:ob1,object:ob2}, {object:ob2,object:ob1}}"));
        root.addChild(createObject("object:ob1", TYPE_OBJECT));
        root.addChild(createObject("object:ob2", TYPE_OBJECT));

        specificationFactory = new ConfigurationObjectSpecificationFactory(root);

        ObjectSpecification array = specificationFactory.getObjectSpecification("array1", null);

        assertEquals("SpecType should be array.", ObjectSpecification.ARRAY_SPECIFICATION, array
            .getSpecType());
        assertNull("Identifier should be null.", array.getIdentifier());
        assertEquals("Type should be 'A'.", TYPE_OBJECT, array.getType());
        assertEquals("Dimension should be 2.", 2, array.getDimension());

        Object[] array1 = array.getParameters();
        Object[] subArray1 = (Object[]) array1[0];
        Object[] subArray2 = (Object[]) array1[1];

        assertEquals("Array should have 2 elements.", 2, array1.length);
        assertEquals("Array[0] should have 2 elements.", 2, subArray1.length);
        assertEquals("Array[1] should have 2 elements.", 2, subArray2.length);

        assertEquals("Array[0][0] should be object:ob1.", specificationFactory
            .getObjectSpecification("object", "ob1"), subArray1[0]);
        assertEquals("Array[0][1] should be object:ob2.", specificationFactory
            .getObjectSpecification("object", "ob2"), subArray1[1]);
        assertEquals("Array[0][1] should be object:ob2.", specificationFactory
            .getObjectSpecification("object", "ob2"), subArray2[0]);
        assertEquals("Array[0][0] should be object:ob1.", specificationFactory
            .getObjectSpecification("object", "ob1"), subArray2[1]);
    }

    /**
     * <p>
     * Test of constructor <code>ConfigurationObjectSpecificationFactory</code> with complex type
     * array with <code>null</code> elements.
     * </p>
     * 
     * @throws Exception exception to JUnit.
     */
    public void testConstructorWithNullComplexTypeArray() throws Exception {
        root.addChild(createArray("array1", TYPE_OBJECT, "1", "{object:ob1,object:ob2,null}"));
        root.addChild(createObject("object:ob1", TYPE_OBJECT));
        root.addChild(createObject("object:ob2", TYPE_OBJECT));

        specificationFactory = new ConfigurationObjectSpecificationFactory(root);

        ObjectSpecification array = specificationFactory.getObjectSpecification("array1", null);

        assertEquals("SpecType should be array.", ObjectSpecification.ARRAY_SPECIFICATION, array
            .getSpecType());
        assertNull("Identifier should be null.", array.getIdentifier());
        assertEquals("Type should be 'A'.", TYPE_OBJECT, array.getType());
        assertEquals("Dimension should be 1.", 1, array.getDimension());

        Object[] array1 = array.getParameters();

        assertEquals("array1[0] should be object:ob1.", specificationFactory
            .getObjectSpecification("object", "ob1"), array1[0]);
        assertEquals("array1[1] should be object:ob2.", specificationFactory
            .getObjectSpecification("object", "ob2"), array1[1]);
        assertEquals("array1[2] should be null.", ObjectSpecification.NULL_SPECIFICATION,
            ((ObjectSpecification) array1[2]).getSpecType());
    }

    /**
     * <p>
     * Test of constructor <code>ConfigurationObjectSpecificationFactory</code> with complex type
     * array with not found references.
     * </p>
     * 
     * @throws Exception exception to JUnit.
     */
    public void testConstructorWithNotFoundArrayReference() throws Exception {
        root.addChild(createArray("array1", TYPE_OBJECT, "1", "{object:ob1,object:ob2,null}"));
        root.addChild(createObject("object:ob1", TYPE_OBJECT));

        try {
            specificationFactory = new ConfigurationObjectSpecificationFactory(root);
            fail("IllegalReferenceException is expected.");
        } catch (IllegalReferenceException e) {
            // ok
        }
    }

    /**
     * <p>
     * Test of constructor <code>ConfigurationObjectSpecificationFactory</code> with string type
     * array.
     * </p>
     * 
     * @throws Exception exception to JUnit.
     */
    public void testConstructorWithStringTypeArray() throws Exception {
        root.addChild(createArray("array1", TYPE_FULL_STRING, "1", "{\"string1\", \"string2\"}"));

        specificationFactory = new ConfigurationObjectSpecificationFactory(root);

        ObjectSpecification array = specificationFactory.getObjectSpecification("array1", null);

        assertEquals("SpecType should be array.", ObjectSpecification.ARRAY_SPECIFICATION, array
            .getSpecType());
        assertNull("Identifier should be null.", array.getIdentifier());
        assertEquals("Type should be String.", TYPE_FULL_STRING, array.getType());
        assertEquals("Dimension should be 1.", 1, array.getDimension());

        Object[] array1 = array.getParameters();
        ObjectSpecification string1 = (ObjectSpecification) array1[0];
        ObjectSpecification string2 = (ObjectSpecification) array1[1];

        assertEquals("Array should have 2 elements.", 2, array1.length);
        assertTrue("SpecType of array elements should be simple.", string1.getSpecType().equals(
            ObjectSpecification.SIMPLE_SPECIFICATION)
            && string2.getSpecType().equals(ObjectSpecification.SIMPLE_SPECIFICATION));
        assertTrue("Elements should be 'String'.", string1.getType().equals(TYPE_FULL_STRING)
            && string2.getType().equals(TYPE_FULL_STRING));
        assertEquals("Element should be \"string1\".", "string1", string1.getValue());
        assertEquals("Element should be \"string2\".", "string2", string2.getValue());
    }

    /**
     * <p>
     * Test of constructor <code>ConfigurationObjectSpecificationFactory</code> with string type
     * array with <code>null</code> elements.
     * </p>
     * 
     * @throws Exception exception to JUnit.
     */
    public void testConstructorWithNullStringTypeArray() throws Exception {
        root.addChild(createArray("array1", TYPE_FULL_STRING, "1", "{\"string1\", null}"));

        specificationFactory = new ConfigurationObjectSpecificationFactory(root);

        ObjectSpecification array = specificationFactory.getObjectSpecification("array1", null);

        assertEquals("SpecType should be array.", ObjectSpecification.ARRAY_SPECIFICATION, array
            .getSpecType());
        assertNull("Identifier should be null.", array.getIdentifier());
        assertEquals("Type should be String.", TYPE_FULL_STRING, array.getType());
        assertEquals("Dimension should be 1.", 1, array.getDimension());

        Object[] array1 = array.getParameters();
        ObjectSpecification string1 = (ObjectSpecification) array1[0];
        ObjectSpecification string2 = (ObjectSpecification) array1[1];

        assertEquals("Array should have 2 elements.", 2, array1.length);
        assertEquals("SpecType of array1[0] should be simple.",
            ObjectSpecification.SIMPLE_SPECIFICATION, string1.getSpecType());
        assertEquals("SpecType of array1[1] should be null.",
            ObjectSpecification.NULL_SPECIFICATION, string2.getSpecType());
    }

    /**
     * <p>
     * Test of constructor <code>ConfigurationObjectSpecificationFactory</code> with string type
     * array with references.
     * </p>
     * 
     * @throws Exception exception to JUnit.
     */
    public void testConstructorWithStringTypeArrayWithReferences() throws Exception {
        root.addChild(createArray("array1", TYPE_FULL_STRING, "1", "{str1, \"string2\"}"));

        ConfigurationObject object = createObject("str1", TYPE_FULL_STRING);
        ConfigurationObject params = new DefaultConfigurationObject("params");
        params.addChild(createParam(1, TYPE_FULL_STRING, "string1"));
        object.addChild(params);
        root.addChild(object);

        specificationFactory = new ConfigurationObjectSpecificationFactory(root);

        ObjectSpecification array = specificationFactory.getObjectSpecification("array1", null);

        assertEquals("SpecType should be array.", ObjectSpecification.ARRAY_SPECIFICATION, array
            .getSpecType());
        assertNull("Identifier should be null.", array.getIdentifier());
        assertEquals("Type should be String.", TYPE_FULL_STRING, array.getType());
        assertEquals("Dimension should be 1.", 1, array.getDimension());

        Object[] array1 = array.getParameters();
        ObjectSpecification string1 = (ObjectSpecification) array1[0];
        ObjectSpecification string2 = (ObjectSpecification) array1[1];

        assertEquals("Array should have 2 elements.", 2, array1.length);
        assertTrue("Elements should be 'String'.", string1.getType().equals(TYPE_FULL_STRING)
            && string2.getType().equals(TYPE_FULL_STRING));
        assertEquals("Element should be str1.", specificationFactory.getObjectSpecification("str1",
            null), string1);
        assertEquals("Element should be \"string2\".", "string2", string2.getValue());
    }

    /**
     * <p>
     * Test of constructor <code>ConfigurationObjectSpecificationFactory</code> with malformed
     * array <i>values</i> property.
     * </p>
     * 
     * @throws Exception exception to JUnit.
     */
    public void testConstructorWithMalformedArrayValuesProperty1() throws Exception {
        root.addChild(createArray("array1", TYPE_INT, "1", "{1,2"));

        try {
            specificationFactory = new ConfigurationObjectSpecificationFactory(root);
            fail("IllegalReferenceException is expected.");
        } catch (IllegalReferenceException e) {
            // ok
        }
    }

    /**
     * <p>
     * Test of constructor <code>ConfigurationObjectSpecificationFactory</code> with malformed
     * array <i>values</i> property.
     * </p>
     * 
     * @throws Exception exception to JUnit.
     */
    public void testConstructorWithMalformedArrayValuesProperty2() throws Exception {
        root.addChild(createArray("array1", TYPE_INT, "1", "1,2}"));

        try {
            specificationFactory = new ConfigurationObjectSpecificationFactory(root);
            fail("IllegalReferenceException is expected.");
        } catch (IllegalReferenceException e) {
            // ok
        }
    }

    /**
     * <p>
     * Test of constructor <code>ConfigurationObjectSpecificationFactory</code> with malformed
     * array <i>values</i> property.
     * </p>
     * 
     * @throws Exception exception to JUnit.
     */
    public void testConstructorWithMalformedArrayValuesProperty3() throws Exception {
        root.addChild(createArray("array1", TYPE_SIMPLE_STRING, "1", "{\"1\",\"2}"));

        try {
            specificationFactory = new ConfigurationObjectSpecificationFactory(root);
            fail("IllegalReferenceException is expected.");
        } catch (IllegalReferenceException e) {
            // ok
        }
    }

    /**
     * <p>
     * Test of constructor <code>ConfigurationObjectSpecificationFactory</code> with malformed
     * array <i>values</i> property.
     * </p>
     * 
     * @throws Exception exception to JUnit.
     */
    public void testConstructorWithMalformedArrayValuesProperty4() throws Exception {
        root.addChild(createArray("array1", TYPE_INT, "2", "{{1},{2}}}"));

        try {
            specificationFactory = new ConfigurationObjectSpecificationFactory(root);
            fail("IllegalReferenceException is expected.");
        } catch (IllegalReferenceException e) {
            // ok
        }
    }

    /**
     * <p>
     * Test of constructor <code>ConfigurationObjectSpecificationFactory</code> with malformed
     * array <i>values</i> property.
     * </p>
     * 
     * @throws Exception exception to JUnit.
     */
    public void testConstructorWithMalformedArrayValuesProperty5() throws Exception {
        root.addChild(createArray("array1", TYPE_SIMPLE_STRING, "2", "{{\"1\"},{},}"));

        try {
            specificationFactory = new ConfigurationObjectSpecificationFactory(root);
            fail("IllegalReferenceException is expected.");
        } catch (IllegalReferenceException e) {
            // ok
        }
    }

    /**
     * <p>
     * Test of constructor <code>ConfigurationObjectSpecificationFactory</code> with malformed
     * array <i>values</i> property.
     * </p>
     * 
     * @throws Exception exception to JUnit.
     */
    public void testConstructorWithMalformedArrayValuesProperty6() throws Exception {
        root.addChild(createArray("array1", TYPE_INT, "1", "{1,2,3,,5}"));

        try {
            specificationFactory = new ConfigurationObjectSpecificationFactory(root);
            fail("IllegalReferenceException is expected.");
        } catch (IllegalReferenceException e) {
            // ok
        }
    }

    /**
     * <p>
     * Test of constructor <code>ConfigurationObjectSpecificationFactory</code> with malformed
     * array <i>values</i> property.
     * </p>
     * 
     * @throws Exception exception to JUnit.
     */
    public void testConstructorWithMalformedArrayValuesProperty7() throws Exception {
        root.addChild(createArray("array1", TYPE_INT, "2", "{{1,2},,{3,5}}"));

        try {
            specificationFactory = new ConfigurationObjectSpecificationFactory(root);
            fail("IllegalReferenceException is expected.");
        } catch (IllegalReferenceException e) {
            // ok
        }
    }

    /**
     * <p>
     * Test of <code>getObjectSpecification</code> with <code>null</code> <code>key</code>.
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     * 
     * @throws Exception exception to JUnit.
     */
    public void testGetObjectSpecificationWithNullKey() throws Exception {
        root.addChild(createObject("key:identifier", TYPE_OBJECT));

        specificationFactory = new ConfigurationObjectSpecificationFactory(root);

        try {
            specificationFactory.getObjectSpecification(null, "identifier");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * <p>
     * Test of <code>getObjectSpecification</code> with empty <code>key</code>.
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     * 
     * @throws Exception exception to JUnit.
     */
    public void testGetObjectSpecificationWithEmptyKey() throws Exception {
        root.addChild(createObject("key:identifier", TYPE_OBJECT));

        specificationFactory = new ConfigurationObjectSpecificationFactory(root);

        try {
            specificationFactory.getObjectSpecification("", "identifier");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * <p>
     * Test of <code>getObjectSpecification</code> with <code>null</code> <code>identifier</code>.
     * </p>
     * 
     * @throws Exception exception to JUnit.
     */
    public void testGetObjectSpecificationWithNullIdentifier() throws Exception {
        root.addChild(createObject("key:identifier", TYPE_OBJECT));
        root.addChild(createObject("key", TYPE_NUMBER));

        specificationFactory = new ConfigurationObjectSpecificationFactory(root);

        ObjectSpecification object = specificationFactory.getObjectSpecification("key", null);

        assertEquals("SpecType should be complex.", ObjectSpecification.COMPLEX_SPECIFICATION,
            object.getSpecType());
        assertNull("Identifier should be null.", object.getIdentifier());
        assertEquals("Type should be 'B'.", TYPE_NUMBER, object.getType());
    }

    /**
     * <p>
     * Test of <code>getObjectSpecification</code> with non-null <code>identifier</code>.
     * </p>
     * 
     * @throws Exception exception to JUnit.
     */
    public void testGetObjectSpecificationWithNonNullIdentifier() throws Exception {
        root.addChild(createObject("key:identifier", TYPE_OBJECT));
        root.addChild(createObject("key", TYPE_NUMBER));

        specificationFactory = new ConfigurationObjectSpecificationFactory(root);

        ObjectSpecification object = specificationFactory.getObjectSpecification("key",
            "identifier");

        assertEquals("SpecType should be complex.", ObjectSpecification.COMPLEX_SPECIFICATION,
            object.getSpecType());
        assertEquals("Identifier should be 'identifier'.", "identifier", object.getIdentifier());
        assertEquals("Type should be 'java.lang.Object'.", TYPE_OBJECT, object.getType());
    }

    /**
     * <p>
     * Test of <code>getObjectSpecification</code> with not found key.
     * <code>UnknownReferenceException</code> is expected.
     * </p>
     * 
     * @throws Exception exception to JUnit.
     */
    public void testGetObjectSpecificationWithNotFoundKey() throws Exception {
        root.addChild(createObject("key:identifier", TYPE_OBJECT));

        specificationFactory = new ConfigurationObjectSpecificationFactory(root);

        try {
            specificationFactory.getObjectSpecification("key2", "identifier");
            fail("UnknownReferenceException is expected.");
        } catch (UnknownReferenceException e) {
            // ok
        }
    }

    /**
     * <p>
     * Test of <code>getObjectSpecification</code> with not found identifier.
     * <code>UnknownReferenceException</code> is expected.
     * </p>
     * 
     * @throws Exception exception to JUnit.
     */
    public void testGetObjectSpecificationWithNotFoundIdentifier() throws Exception {
        root.addChild(createObject("key:identifier", TYPE_OBJECT));

        specificationFactory = new ConfigurationObjectSpecificationFactory(root);

        try {
            specificationFactory.getObjectSpecification("key", "identifier2");
            fail("UnknownReferenceException is expected.");
        } catch (UnknownReferenceException e) {
            // ok
        }
    }

    /**
     * <p>
     * Test of <code>getObjectSpecification</code> with with not found <code>null</code>
     * identifier. <code>UnknownReferenceException</code> is expected.
     * </p>
     * 
     * @throws Exception exception to JUnit.
     */
    public void testGetObjectSpecificationWithNotFoundNullIdentifier() throws Exception {
        root.addChild(createObject("key:identifier", TYPE_OBJECT));

        specificationFactory = new ConfigurationObjectSpecificationFactory(root);

        try {
            specificationFactory.getObjectSpecification("key", null);
            fail("UnknownReferenceException is expected.");
        } catch (UnknownReferenceException e) {
            // ok
        }
    }
}
