/*
 * Copyright (C) 2006, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.objectfactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import junit.framework.TestCase;

import com.topcoder.util.objectfactory.impl.ConfigManagerSpecificationFactory;
import com.topcoder.util.objectfactory.testclasses.TestClass1;
import com.topcoder.util.objectfactory.testclasses.TestClass2;
import com.topcoder.util.objectfactory.testclasses.TestClass3;

/**
 * Unit test for ObjectFactory.
 *
 * @author mgmg, TCSDEVELOPER
 * @version 2.2
 */
public class ObjectFactoryUnitTest extends TestCase {
    /**
     * The ConfigManagerSpecificationFactory instance for test.
     */
    private ConfigManagerSpecificationFactory factory = null;

    /**
     * The test instance.
     */
    private ObjectFactory[] instance = new ObjectFactory[4];

    /**
     * Create the test instance.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void setUp() throws Exception {
        TestHelper.clearConfig();
        TestHelper.loadSingleConfigFile();

        factory = new ConfigManagerSpecificationFactory("valid_config");

        instance[0] = new ObjectFactory(factory, ObjectFactory.SPECIFICATION_ONLY);
        instance[1] = new ObjectFactory(factory, ObjectFactory.REFLECTION_ONLY);
        instance[2] = new ObjectFactory(factory, ObjectFactory.BOTH);
        instance[3] = new ObjectFactory();
    }

    /**
     * Clean the config.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void tearDown() throws Exception {
        TestHelper.clearConfig();
    }

    /**
     * Test default constructor. No exception will be thrown.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testConstructor_Accuracy() throws Exception {
        ObjectFactory objFactory = new ObjectFactory();

        assertNotNull("The instance should not be null.", objFactory);
        assertNull("Should get null", ((SpecificationFactory) TestHelper.getPrivateField(ObjectFactory.class,
                objFactory, "specificationFactory")).getObjectSpecification("key", "value"));

    }

    /**
     * Test constructor1 with null parameter. IllegalArgumentException should be
     * thrown.
     */
    public void testConstructor1_NullFactory() {
        try {
            new ObjectFactory(null);
            fail("IllegalArgumentException should be thrown because of the null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test constructor1 with valid parameter. No exception will be thrown.
     */
    public void testConstructor1_Accuracy() {
        ObjectFactory objFactory = new ObjectFactory(factory);

        assertNotNull("The instance should not be null.", objFactory);
    }

    /**
     * Test constructor2 with null parameter. IllegalArgumentException should be
     * thrown.
     */
    public void testConstructor2_NullFactory() {
        try {
            new ObjectFactory(null, ObjectFactory.BOTH);
            fail("IllegalArgumentException should be thrown because of the null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test constructor2 with null parameter. IllegalArgumentException should be
     * thrown.
     */
    public void testConstructor2_NullStrategy() {
        try {
            new ObjectFactory(factory, null);
            fail("IllegalArgumentException should be thrown because of the null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test constructor2 with empty parameter. IllegalArgumentException should
     * be thrown.
     */
    public void testConstructor2_EmptyStrategy() {
        try {
            new ObjectFactory(factory, " ");
            fail("IllegalArgumentException should be thrown because of the null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test constructor2 with null parameter. IllegalArgumentException should be
     * thrown.
     */
    public void testConstructor2_InvalidStrategy() {
        try {
            new ObjectFactory(factory, "invalid_strategy");
            fail("IllegalArgumentException should be thrown because of the null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test constructor2 with valid parameter. No exception will be thrown.
     */
    public void testConstructor2_Accuracy() {
        ObjectFactory objFactory = new ObjectFactory(factory, ObjectFactory.REFLECTION_ONLY);

        assertNotNull("The instance should not be null.", objFactory);
    }

    /**
     * Test getInitStrategy. No exception will be thrown.
     */
    public void testGetInitStrategy() {
        String strategy = instance[1].getInitStrategy();

        assertEquals("The result is wrong.", strategy, ObjectFactory.REFLECTION_ONLY);
    }

    /**
     * Test setInitStrategy with null parameter.. IllegalArgumentException
     * should be thrown.
     */
    public void testSetInitStrategy_NullStrategy() {
        try {
            instance[2].setInitStrategy(null);
            fail("IllegalArgumentException should be thrown because of the null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test setInitStrategy with empty parameter.. IllegalArgumentException
     * should be thrown.
     */
    public void testSetInitStrategy_EmptyStrategy() {
        try {
            instance[1].setInitStrategy(" ");
            fail("IllegalArgumentException should be thrown because of the empty parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test setInitStrategy with invalid parameter.. IllegalArgumentException
     * should be thrown.
     */
    public void testSetInitStrategy_InvalidStrategy() {
        try {
            instance[1].setInitStrategy("invalid_strategy");
            fail("IllegalArgumentException should be thrown because of the invalid parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test setInitStrategy with valid parameter.
     */
    public void testSetInitStrategy_Accuracy() {
        instance[0].setInitStrategy(ObjectFactory.BOTH);

        assertEquals("The result is wrong.", ObjectFactory.BOTH, instance[0].getInitStrategy());
    }

    /**
     * Test createObject1 with null key. IllegalArgumentException should be
     * thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject1_NullKey() throws Exception {
        try {
            instance[0].createObject((String) null);
            fail("IllegalArgumentException should be thrown because of the null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test createObject1 with empty key. IllegalArgumentException should be
     * thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject1_EmptyKey() throws Exception {
        try {
            instance[0].createObject(" ");
            fail("IllegalArgumentException should be thrown because of the empty parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test createObject1 with not-existing key.
     * InvalidClassSpecificationException should be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject1_NotExistKey() throws Exception {
        try {
            instance[2].createObject("key_not_exist");
            fail("InvalidClassSpecificationException should be thrown because of the not-existing key.");
        } catch (InvalidClassSpecificationException e) {
            // expected.
        }
    }

    /**
     * Test createObject1 with invalid object.
     * InvalidClassSpecificationException should be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject1_ObjectThrowException() throws Exception {
        try {
            instance[0].createObject("frac1");
            fail("InvalidClassSpecificationException should be thrown because of the invalid object.");
        } catch (InvalidClassSpecificationException e) {
            // expected.
        }
    }

    /**
     * Test createObject1 with valid key.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject1_Accuracy1() throws Exception {
        Object obj = instance[0].createObject("bar");

        assertTrue("The type is wrong.", obj instanceof TestClass2);
        assertEquals("The result is wrong.", "TestClass2TestClass12Strong2.5", obj.toString());
    }

    /**
     * Test createObject1 with valid key.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject1_Accuracy2() throws Exception {
        Object obj = instance[1].createObject("java.util.HashSet");

        assertTrue("The type is wrong.", obj instanceof HashSet);
    }

    /**
     * Test createObject1 with valid key.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject1_Accuracy3() throws Exception {
        Object obj = instance[2].createObject("testcollection");

        assertTrue("The type is wrong.", obj instanceof Collection[]);
        assertEquals("The result is wrong.", 3, ((Collection[]) obj).length);
        assertTrue("The result is wrong.", ((Collection[]) obj)[0] instanceof HashSet);
        assertNull("The result is wrong.", ((Collection[]) obj)[1]);
        assertTrue("The result is wrong.", ((Collection[]) obj)[2] instanceof ArrayList);
    }

    /**
     * Test createObject1 with valid key.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject1_Accuracy4() throws Exception {
        Object obj = instance[2].createObject("java.util.ArrayList");

        assertTrue("The type is wrong.", obj instanceof ArrayList);
    }

    /**
     * Test createObject1 with valid key.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject1_Accuracy5() throws Exception {
        Object obj = instance[0].createObject("simple");

        assertTrue("The type is wrong.", obj instanceof TestClass3);
        assertEquals("The result is wrong.", "TestClass3true2c4.45.5678StringStringFull", obj.toString());
    }

    /**
     * Test createObject2 with null type. IllegalArgumentException should be
     * thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject2_NullType() throws Exception {
        try {
            instance[0].createObject((Class<?>) null);
            fail("IllegalArgumentException should be thrown because of the null type.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test createObject2 with not-existing type.
     * InvalidClassSpecificationException should be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject2_NotExistType() throws Exception {
        try {
            instance[0].createObject(HashSet.class);
            fail("InvalidClassSpecificationException should be thrown because of the not-existiong type.");
        } catch (InvalidClassSpecificationException e) {
            // expected.
        }
    }

    /**
     * Test createObject2 with valid type.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject2_Accuracy1() throws Exception {
        Object obj = instance[1].createObject(HashSet.class);

        assertTrue("The type is wrong.", obj instanceof HashSet);
    }

    /**
     * Test createObject2 with valid type.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject2_Accuracy2() throws Exception {
        Object obj = instance[2].createObject(HashSet.class);

        assertTrue("The type is wrong.", obj instanceof HashSet);
    }

    /**
     * Test createObject3 with null key. IllegalArgumentException should be
     * thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject3_NullKey() throws Exception {
        try {
            instance[0].createObject((String) null, "default");
            fail("IllegalArgumentException should be thrown because of the null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test createObject3 with empty key. IllegalArgumentException should be
     * thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject3_EmptyKey() throws Exception {
        try {
            instance[0].createObject(" ", "default");
            fail("IllegalArgumentException should be thrown because of the empty parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test createObject3 with null identifier. IllegalArgumentException should
     * be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject3_NullIdentifier() throws Exception {
        try {
            instance[0].createObject("frac", null);
            fail("IllegalArgumentException should be thrown because of the null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test createObject3 with empty identifier. IllegalArgumentException should
     * be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject3_EmptyIdentifier() throws Exception {
        try {
            instance[0].createObject("frac", "  ");
            fail("IllegalArgumentException should be thrown because of the empty parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test createObject3 with not-existing key.
     * InvalidClassSpecificationException should be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject3_NotExistObject() throws Exception {
        try {
            instance[0].createObject("not_exist", "not_exist");
            fail("InvalidClassSpecificationException should be thrown because of the not-existing key.");
        } catch (InvalidClassSpecificationException e) {
            // expected.
        }
    }

    /**
     * Test createObject3 with valid key.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject3_Accuracy1() throws Exception {
        Object obj = instance[0].createObject("intArray", "arrays");

        assertTrue("The type is wrong.", obj instanceof int[][]);
        assertEquals("The result is wrong.", 17, ((int[][]) obj).length);
        assertEquals("The result is wrong.", 2, ((int[][]) obj)[5].length);
        assertEquals("The result is wrong.", 2, ((int[][]) obj)[13].length);
        assertEquals("The result is wrong.", 3, ((int[][]) obj)[4][0]);
        assertEquals("The result is wrong.", 4, ((int[][]) obj)[9][1]);
    }

    /**
     * Test createObject3 with valid key.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject3_Accuracy2() throws Exception {
        Object obj = instance[2].createObject("test", "collection");

        assertTrue("The type is wrong.", obj instanceof Collection[]);
        assertEquals("The result is wrong.", 3, ((Collection[]) obj).length);
        assertTrue("The result is wrong.", ((Collection[]) obj)[0] instanceof HashSet);
        assertNull("The result is wrong.", ((Collection[]) obj)[1]);
        assertTrue("The result is wrong.", ((Collection[]) obj)[2] instanceof ArrayList);
    }

    /**
     * Test createObject4 with null type. IllegalArgumentException should be
     * thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject4_NullType() throws Exception {
        try {
            instance[0].createObject((Class<?>) null, "default");
            fail("IllegalArgumentException should be thrown because of the null type.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test createObject4 with null identifier. IllegalArgumentException should
     * be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject4_NullIdentifier() throws Exception {
        try {
            instance[0].createObject(HashSet.class, null);
            fail("IllegalArgumentException should be thrown because of the null identifier.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test createObject4 with empty identifier. IllegalArgumentException should
     * be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject4_EmptyIdentifier() throws Exception {
        try {
            instance[0].createObject(HashSet.class, " ");
            fail("IllegalArgumentException should be thrown because of the empty identifier.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test createObject4 with not-existing key.
     * InvalidClassSpecificationException should be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject4_KeyNotExist() throws Exception {
        try {
            instance[0].createObject(HashSet.class, "default");
            fail("InvalidClassSpecificationException should be thrown because the object doesn't exist.");
        } catch (InvalidClassSpecificationException e) {
            // expected.
        }
    }

    /**
     * Test createObject4 with valid parameter.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject4_Accuracy1() throws Exception {
        Object obj = instance[1].createObject(HashSet.class, "default");

        assertTrue("The type is wrong.", obj instanceof HashSet);
    }

    /**
     * Test createObject4 with valid parameter.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject4_Accuracy2() throws Exception {
        Object obj = instance[2].createObject(HashSet.class, "default");

        assertTrue("The type is wrong.", obj instanceof HashSet);
    }

    /**
     * Test createObject5 with null parameter. IllegalArgumentException should
     * be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject5_NullKey() throws Exception {
        try {
            instance[0].createObject((String) null, "default", (URL) null, new Object[0], new Class<?>[0],
                    ObjectFactory.BOTH);
            fail("IllegalArgumentException should be thrown because of the null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test createObject5 with empty parameter. IllegalArgumentException should
     * be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject5_EmptyKey() throws Exception {
        try {
            instance[0].createObject(" ", "default", (URL) null, new Object[0], new Class<?>[0], ObjectFactory.BOTH);
            fail("IllegalArgumentException should be thrown because of the empty parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test createObject5 with invalid strategy. IllegalArgumentException should
     * be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject5_InvalidStrategy() throws Exception {
        try {
            instance[1].createObject("java.lang.String", null, (URL) null, new Object[0], new Class<?>[0],
                    "invalid_strategy");
            fail("IllegalArgumentException should be thrown because of the invalid parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test createObject5 with null strategy. IllegalArgumentException should be
     * thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject5_NullStrategy() throws Exception {
        try {
            instance[1].createObject("java.lang.String", null, (URL) null, new Object[0], new Class<?>[0], null);
            fail("IllegalArgumentException should be thrown because of the invalid parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test createObject5 with invalid parameter. IllegalArgumentException
     * should be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject5_InvalidParam1() throws Exception {
        try {
            instance[1].createObject("java.lang.String", null, (URL) null, new Object[0],
                    new Class<?>[] { String.class }, ObjectFactory.REFLECTION_ONLY);
            fail("IllegalArgumentException should be thrown because of the invalid parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test createObject5 with invalid parameter.
     * InvalidClassSpecificationException should be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject5_InvalidParam2() throws Exception {
        try {
            instance[1].createObject("java.lang.String", null, (URL) null, new Object[] { new HashSet<Object>() },
                    new Class<?>[] { HashSet.class }, ObjectFactory.REFLECTION_ONLY);
            fail("InvalidClassSpecificationException should be thrown because of the invalid parameter.");
        } catch (InvalidClassSpecificationException e) {
            // expected.
        }
    }

    /**
     * Test createObject5 with invalid parameter.
     * InvalidClassSpecificationException should be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject5_InvalidParam3() throws Exception {
        try {
            instance[1].createObject("invalid_type", null, (URL) null, new Object[] { new HashSet<Object>() },
                    new Class<?>[] { HashSet.class }, ObjectFactory.REFLECTION_ONLY);
            fail("InvalidClassSpecificationException should be thrown because of the invalid parameter.");
        } catch (InvalidClassSpecificationException e) {
            // expected.
        }
    }

    /**
     * Test createObject5 with invalid parameter.
     * InvalidClassSpecificationException should be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject5_InvalidParam4() throws Exception {
        try {
            instance[1].createObject("invalid_type", "invalid_id", (URL) null, null, null,
                    ObjectFactory.SPECIFICATION_ONLY);
            fail("InvalidClassSpecificationException should be thrown because of the invalid parameter.");
        } catch (InvalidClassSpecificationException e) {
            // expected.
        }
    }

    /**
     * Test createObject5 with invalid parameter.
     * InvalidClassSpecificationException should be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject5_InvalidParam5() throws Exception {
        try {
            instance[2].createObject("frac1", "default", (URL) null, null, null, ObjectFactory.BOTH);
            fail("InvalidClassSpecificationException should be thrown because of the invalid parameter.");
        } catch (InvalidClassSpecificationException e) {
            // expected.
        }
    }

    /**
     * Test createObject5 with invalid parameter.
     * InvalidClassSpecificationException should be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject5_InvalidParam6() throws Exception {
        try {
            instance[2].createObject("int", "Mismatch", (URL) null, null, null, ObjectFactory.BOTH);
            fail("InvalidClassSpecificationException should be thrown because of the invalid parameter.");
        } catch (InvalidClassSpecificationException e) {
            // expected.
        }
    }

    /**
     * Test createObject5 with invalid parameter.
     * InvalidClassSpecificationException should be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject5_InvalidParam7() throws Exception {
        try {
            instance[2].createObject("typeMismatch", null, (URL) null, null, null, ObjectFactory.BOTH);
            fail("InvalidClassSpecificationException should be thrown because of the invalid parameter.");
        } catch (InvalidClassSpecificationException e) {
            // expected.
        }
    }

    /**
     * Test createObject5 with invalid parameter.
     * InvalidClassSpecificationException should be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject5_InvalidParam8() throws Exception {
        try {
            instance[2].createObject("type", null, new URL(TestHelper.getURLString("test_files/not_exist.jar")), null,
                    null, ObjectFactory.BOTH);
            fail("InvalidClassSpecificationException should be thrown because of the invalid parameter.");
        } catch (InvalidClassSpecificationException e) {
            // expected.
        }
    }

    /**
     * Test createObject5 with valid parameter.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject5_Accuracy1() throws Exception {
        Object obj = instance[0].createObject("frac", "default", (URL) null, null, null,
                ObjectFactory.SPECIFICATION_ONLY);

        assertTrue("The type is wrong.", obj instanceof TestClass1);
        assertEquals("The result is wrong.", "TestClass12Strong", obj.toString());
    }

    /**
     * Test createObject5 with valid parameter.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject5_Accuracy2() throws Exception {
        Object obj = instance[0].createObject(TestClass1.class.getName(), null, (URL) null, new Object[] {
            new Integer(5), "abc" }, new Class<?>[] { int.class, String.class }, ObjectFactory.REFLECTION_ONLY);

        assertTrue("The type is wrong.", obj instanceof TestClass1);
        assertEquals("The result is wrong.", "TestClass15abc", obj.toString());
    }

    /**
     * Test createObject5 with valid parameter.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject5_Accuracy3() throws Exception {
        Object obj = instance[0].createObject(TestClass1.class.getName(), null, (URL) null, new Object[] {
            new Integer(5), "abc" }, new Class<?>[] { int.class, String.class }, ObjectFactory.BOTH);

        assertTrue("The type is wrong.", obj instanceof TestClass1);
        assertEquals("The result is wrong.", "TestClass15abc", obj.toString());
    }

    /**
     * Test createObject5 with valid parameter.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject5_Accuracy4() throws Exception {
        Object obj = instance[0].createObject("frac", "default", (URL) null, new Object[] { new Integer(5), "abc" },
                new Class<?>[] { int.class, String.class }, ObjectFactory.BOTH);

        assertTrue("The type is wrong.", obj instanceof TestClass1);
        assertEquals("The result is wrong.", "TestClass12Strong", obj.toString());
    }

    /**
     * Test createObject5 with valid parameter.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject5_Accuracy5() throws Exception {
        Object obj = instance[0].createObject("com.test.TestComplex", null,
                new URL(TestHelper.getURLString("test_files/test.jar")), new Object[] { new Integer(5), "abc" },
                new Class<?>[] { int.class, String.class }, ObjectFactory.BOTH);

        assertEquals("The result is wrong.", obj.getClass().getName(), new URLClassLoader(new URL[] { new URL(
                TestHelper.getURLString("test_files/test.jar")) }).loadClass("com.test.TestComplex").getName());
    }

    /**
     * Test createObject6 with null type. IllegalArgumentException should be
     * thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject6_NullType() throws Exception {
        try {
            instance[0].createObject((Class<?>) null, "default", (URL) null, new Object[0], new Class<?>[0],
                    ObjectFactory.BOTH);
            fail("IllegalArgumentException should be thrown because of the null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test createObject6 with invalid strategy. IllegalArgumentException should
     * be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject6_InvalidStrategy() throws Exception {
        try {
            instance[1]
                    .createObject(String.class, null, (URL) null, new Object[0], new Class<?>[0], "invalid_strategy");
            fail("IllegalArgumentException should be thrown because of the invalid parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test createObject6 with null strategy. IllegalArgumentException should be
     * thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject6_NullStrategy() throws Exception {
        try {
            instance[1].createObject(String.class, null, (URL) null, new Object[0], new Class<?>[0], null);
            fail("IllegalArgumentException should be thrown because of the invalid parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test createObject6 with invalid parameter. IllegalArgumentException
     * should be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject6_InvalidParam1() throws Exception {
        try {
            instance[1].createObject(String.class, null, (URL) null, new Object[0], new Class<?>[] { String.class },
                    ObjectFactory.REFLECTION_ONLY);
            fail("IllegalArgumentException should be thrown because of the invalid parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test createObject6 with invalid parameter.
     * InvalidClassSpecificationException should be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject6_InvalidParam2() throws Exception {
        try {
            instance[1].createObject(String.class, null, (URL) null, new Object[] { new HashSet<Object>() },
                    new Class<?>[] { HashSet.class }, ObjectFactory.REFLECTION_ONLY);
            fail("InvalidClassSpecificationException should be thrown because of the invalid parameter.");
        } catch (InvalidClassSpecificationException e) {
            // expected.
        }
    }

    /**
     * Test createObject6 with invalid parameter.
     * InvalidClassSpecificationException should be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject6_InvalidParam3() throws Exception {
        try {
            instance[1].createObject(String.class, "invalid_id", (URL) null, null, null,
                    ObjectFactory.SPECIFICATION_ONLY);
            fail("InvalidClassSpecificationException should be thrown because of the invalid parameter.");
        } catch (InvalidClassSpecificationException e) {
            // expected.
        }
    }

    /**
     * Test createObject6 with invalid parameter.
     * InvalidClassSpecificationException should be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject6_InvalidParam4() throws Exception {
        try {
            instance[2].createObject(int.class, "Mismatch", (URL) null, null, null, ObjectFactory.BOTH);
            fail("InvalidClassSpecificationException should be thrown because of the invalid parameter.");
        } catch (InvalidClassSpecificationException e) {
            // expected.
        }
    }

    /**
     * Test createObject6 with valid parameter.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject6_Accuracy1() throws Exception {
        Object obj = instance[0].createObject(TestClass1.class, "default", (URL) null, null, null,
                ObjectFactory.SPECIFICATION_ONLY);

        assertTrue("The type is wrong.", obj instanceof TestClass1);
        assertEquals("The result is wrong.", "TestClass12Strong", obj.toString());
    }

    /**
     * Test createObject6 with valid parameter.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject6_Accuracy2() throws Exception {
        Object obj = instance[0].createObject(TestClass1.class, null, (URL) null,
                new Object[] { new Integer(5), "abc" }, new Class<?>[] { int.class, String.class },
                ObjectFactory.REFLECTION_ONLY);

        assertTrue("The type is wrong.", obj instanceof TestClass1);
        assertEquals("The result is wrong.", "TestClass15abc", obj.toString());
    }

    /**
     * Test createObject6 with valid parameter.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject6_Accuracy3() throws Exception {
        Object obj = instance[0].createObject(TestClass1.class, null, (URL) null,
                new Object[] { new Integer(5), "abc" }, new Class<?>[] { int.class, String.class }, ObjectFactory.BOTH);

        assertTrue("The type is wrong.", obj instanceof TestClass1);
        assertEquals("The result is wrong.", "TestClass15abc", obj.toString());
    }

    /**
     * Test createObject6 with valid parameter.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject6_Accuracy4() throws Exception {
        Object obj = instance[0].createObject(TestClass1.class, "default", (URL) null, new Object[] { new Integer(5),
            "abc" }, new Class<?>[] { int.class, String.class }, ObjectFactory.BOTH);

        assertTrue("The type is wrong.", obj instanceof TestClass1);
        assertEquals("The result is wrong.", "TestClass12Strong", obj.toString());
    }

    /**
     * Test createObject7 with null parameter. IllegalArgumentException should
     * be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject7_NullKey() throws Exception {
        try {
            instance[0].createObject((String) null, "default", ClassLoader.getSystemClassLoader(), new Object[0],
                    new Class<?>[0], ObjectFactory.BOTH);
            fail("IllegalArgumentException should be thrown because of the null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test createObject7 with empty parameter. IllegalArgumentException should
     * be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject7_EmptyKey() throws Exception {
        try {
            instance[0].createObject(" ", "default", ClassLoader.getSystemClassLoader(), new Object[0],
                    new Class<?>[0], ObjectFactory.BOTH);
            fail("IllegalArgumentException should be thrown because of the empty parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test createObject7 with invalid strategy. IllegalArgumentException should
     * be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject7_InvalidStrategy() throws Exception {
        try {
            instance[1].createObject("java.lang.String", null, ClassLoader.getSystemClassLoader(), new Object[0],
                    new Class<?>[0], "invalid_strategy");
            fail("IllegalArgumentException should be thrown because of the invalid parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test createObject7 with null strategy. IllegalArgumentException should be
     * thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject7_NullStrategy() throws Exception {
        try {
            instance[1].createObject("java.lang.String", null, ClassLoader.getSystemClassLoader(), new Object[0],
                    new Class<?>[0], null);
            fail("IllegalArgumentException should be thrown because of the invalid parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test createObject7 with invalid parameter. IllegalArgumentException
     * should be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject7_InvalidParam1() throws Exception {
        try {
            instance[1].createObject("java.lang.String", null, ClassLoader.getSystemClassLoader(), new Object[0],
                    new Class<?>[] { String.class }, ObjectFactory.REFLECTION_ONLY);
            fail("IllegalArgumentException should be thrown because of the invalid parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test createObject7 with invalid parameter.
     * InvalidClassSpecificationException should be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject7_InvalidParam2() throws Exception {
        try {
            instance[1].createObject("java.lang.String", null, ClassLoader.getSystemClassLoader(),
                    new Object[] { new HashSet<Object>() }, new Class<?>[] { HashSet.class },
                    ObjectFactory.REFLECTION_ONLY);
            fail("InvalidClassSpecificationException should be thrown because of the invalid parameter.");
        } catch (InvalidClassSpecificationException e) {
            // expected.
        }
    }

    /**
     * Test createObject7 with invalid parameter.
     * InvalidClassSpecificationException should be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject7_InvalidParam3() throws Exception {
        try {
            instance[1].createObject("invalid_type", null, ClassLoader.getSystemClassLoader(),
                    new Object[] { new HashSet<Object>() }, new Class<?>[] { HashSet.class },
                    ObjectFactory.REFLECTION_ONLY);
            fail("InvalidClassSpecificationException should be thrown because of the invalid parameter.");
        } catch (InvalidClassSpecificationException e) {
            // expected.
        }
    }

    /**
     * Test createObject7 with invalid parameter.
     * InvalidClassSpecificationException should be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject7_InvalidParam4() throws Exception {
        try {
            instance[1].createObject("invalid_type", "invalid_id", (ClassLoader) null, null, null,
                    ObjectFactory.SPECIFICATION_ONLY);
            fail("InvalidClassSpecificationException should be thrown because of the invalid parameter.");
        } catch (InvalidClassSpecificationException e) {
            // expected.
        }
    }

    /**
     * Test createObject7 with invalid parameter.
     * InvalidClassSpecificationException should be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject7_InvalidParam5() throws Exception {
        try {
            instance[2].createObject("frac1", "default", ClassLoader.getSystemClassLoader(), null, null,
                    ObjectFactory.BOTH);
            fail("InvalidClassSpecificationException should be thrown because of the invalid parameter.");
        } catch (InvalidClassSpecificationException e) {
            // expected.
        }
    }

    /**
     * Test createObject7 with invalid parameter.
     * InvalidClassSpecificationException should be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject7_InvalidParam6() throws Exception {
        try {
            instance[2].createObject("int", "Mismatch", ClassLoader.getSystemClassLoader(), null, null,
                    ObjectFactory.BOTH);
            fail("InvalidClassSpecificationException should be thrown because of the invalid parameter.");
        } catch (InvalidClassSpecificationException e) {
            // expected.
        }
    }

    /**
     * Test createObject7 with invalid parameter.
     * InvalidClassSpecificationException should be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject7_InvalidParam7() throws Exception {
        try {
            instance[2].createObject("typeMismatch", null, ClassLoader.getSystemClassLoader(), null, null,
                    ObjectFactory.BOTH);
            fail("InvalidClassSpecificationException should be thrown because of the invalid parameter.");
        } catch (InvalidClassSpecificationException e) {
            // expected.
        }
    }

    /**
     * Test createObject7 with invalid parameter.
     * InvalidClassSpecificationException should be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject7_InvalidParam8() throws Exception {
        try {
            instance[0].createObject("java.lang.String", null, (ClassLoader) null, null, null,
                    ObjectFactory.SPECIFICATION_ONLY);
            fail("InvalidClassSpecificationException should be thrown because of the invalid parameter.");
        } catch (InvalidClassSpecificationException e) {
            // expected.
        }
    }

    /**
     * Test createObject7 with invalid parameter.
     * InvalidClassSpecificationException should be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject7_InvalidParam9() throws Exception {
        try {
            instance[0].createObject("testcollection", null, (ClassLoader) null, null, null,
                    ObjectFactory.REFLECTION_ONLY);
            fail("InvalidClassSpecificationException should be thrown because of the invalid parameter.");
        } catch (InvalidClassSpecificationException e) {
            // expected.
        }
    }

    /**
     * Test createObject7 with invalid parameter. IllegalArgumentException
     * should be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject7_InvalidParam10() throws Exception {
        try {
            instance[1].createObject("com.topcoder.util.objectfactory.testclasses.TestClass1", null,
                    ClassLoader.getSystemClassLoader(), new Object[] { new Integer(5), null }, null,
                    ObjectFactory.REFLECTION_ONLY);
            fail("IllegalArgumentException should be thrown because of the invalid parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test createObject7 with invalid parameter. IllegalArgumentException
     * should be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject7_InvalidParam11() throws Exception {
        try {
            instance[1].createObject("com.topcoder.util.objectfactory.testclasses.TestClass1", null,
                    ClassLoader.getSystemClassLoader(), new Object[] { new Integer(5), null },
                    new Class<?>[] { int.class, null }, ObjectFactory.REFLECTION_ONLY);
            fail("IllegalArgumentException should be thrown because of the invalid parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test createObject7 with invalid parameter. IllegalArgumentException
     * should be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject7_InvalidParam12() throws Exception {
        try {
            instance[1].createObject("com.topcoder.util.objectfactory.testclasses.TestClass1", null,
                    ClassLoader.getSystemClassLoader(), null, new Class<?>[] { int.class, null },
                    ObjectFactory.REFLECTION_ONLY);
            fail("IllegalArgumentException should be thrown because of the invalid parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test createObject7 with invalid parameter. IllegalArgumentException
     * should be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject7_InvalidParam13() throws Exception {
        try {
            instance[1].createObject("com.topcoder.util.objectfactory.testclasses.TestClass1", null,
                    ClassLoader.getSystemClassLoader(), null, new Class<?>[] { int.class, String.class },
                    ObjectFactory.REFLECTION_ONLY);
            fail("IllegalArgumentException should be thrown because of the invalid parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test createObject7 with valid parameter.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject7_Accuracy1() throws Exception {
        Object obj = instance[0].createObject("frac", "default", ClassLoader.getSystemClassLoader(), null, null,
                ObjectFactory.SPECIFICATION_ONLY);

        assertTrue("The type is wrong.", obj instanceof TestClass1);
        assertEquals("The result is wrong.", "TestClass12Strong", obj.toString());
    }

    /**
     * Test createObject7 with valid parameter.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject7_Accuracy2() throws Exception {
        Object obj = instance[0].createObject(TestClass1.class.getName(), null, ClassLoader.getSystemClassLoader(),
                new Object[] { new Integer(5), "abc" }, new Class<?>[] { int.class, String.class },
                ObjectFactory.REFLECTION_ONLY);

        assertTrue("The type is wrong.", obj instanceof TestClass1);
        assertEquals("The result is wrong.", "TestClass15abc", obj.toString());
    }

    /**
     * Test createObject7 with valid parameter.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject7_Accuracy3() throws Exception {
        Object obj = instance[0].createObject(TestClass1.class.getName(), null, ClassLoader.getSystemClassLoader(),
                new Object[] { new Integer(5), "abc" }, new Class<?>[] { int.class, String.class }, ObjectFactory.BOTH);

        assertTrue("The type is wrong.", obj instanceof TestClass1);
        assertEquals("The result is wrong.", "TestClass15abc", obj.toString());
    }

    /**
     * Test createObject7 with valid parameter.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject7_Accuracy4() throws Exception {
        Object obj = instance[0].createObject("frac", "default", ClassLoader.getSystemClassLoader(), new Object[] {
            new Integer(5), "abc" }, new Class<?>[] { int.class, String.class }, ObjectFactory.BOTH);

        assertTrue("The type is wrong.", obj instanceof TestClass1);
        assertEquals("The result is wrong.", "TestClass12Strong", obj.toString());
    }

    /**
     * Test createObject7 with valid parameter.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject7_Accuracy5() throws Exception {
        Object obj = instance[0].createObject("java.lang.String", null, (ClassLoader) null, null, null,
                ObjectFactory.REFLECTION_ONLY);

        assertTrue("The type is wrong", obj instanceof String);
    }

    /**
     * Test createObject7 with valid parameter.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject7_Accuracy6() throws Exception {
        Object obj = instance[0].createObject("buffer", "default", (ClassLoader) null, null, null,
                ObjectFactory.SPECIFICATION_ONLY);

        assertTrue("The type is wrong", obj instanceof StringBuffer);
    }

    /**
     * Test createObject7 with valid parameter.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject7_Accuracy7() throws Exception {
        Object obj = instance[0].createObject("intArray", "arrays", (ClassLoader) null, null, null,
                ObjectFactory.SPECIFICATION_ONLY);

        assertTrue("The type is wrong", obj instanceof int[][]);
        assertEquals("The result is wrong.", 17, ((int[][]) obj).length);
        assertEquals("The result is wrong.", 2, ((int[][]) obj)[5].length);
        assertEquals("The result is wrong.", 2, ((int[][]) obj)[13].length);
        assertEquals("The result is wrong.", 3, ((int[][]) obj)[4][0]);
        assertEquals("The result is wrong.", 4, ((int[][]) obj)[9][1]);
    }

    /**
     * Test createObject7 with valid parameter.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject7_Accuracy8() throws Exception {
        Object obj = instance[0].createObject("testcollection", null, (ClassLoader) null, null, null,
                ObjectFactory.BOTH);

        assertTrue("The type is wrong", obj instanceof Collection[]);
        assertEquals("The result is wrong.", 3, ((Collection[]) obj).length);
        assertTrue("The result is wrong.", ((Collection[]) obj)[0] instanceof HashSet);
        assertNull("The result is wrong.", ((Collection[]) obj)[1]);
        assertTrue("The result is wrong.", ((Collection[]) obj)[2] instanceof ArrayList);
    }

    /**
     * Test createObject7 with valid parameter.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject7_Accuracy9() throws Exception {
        Object obj = instance[0].createObject("java.lang.String", null, (ClassLoader) null, null, null,
                ObjectFactory.BOTH);

        assertTrue("The type is wrong", obj instanceof String);
        assertEquals("The result is wrong.", "", obj.toString());
    }

    /**
     * Test createObject7 with valid parameter.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject7_Accuracy10() throws Exception {
        Object obj = instance[1].createObject("com.topcoder.util.objectfactory.testclasses.TestClass1", null,
                ClassLoader.getSystemClassLoader(), new Object[] { new Integer(5), null },
                new Class<?>[] { int.class, String.class }, ObjectFactory.REFLECTION_ONLY);

        assertTrue("The type is wrong", obj instanceof TestClass1);
        assertEquals("The result is wrong.", "TestClass15null", obj.toString());
    }

    /**
     * Test createObject7 with valid parameter.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject7_Accuracy11() throws Exception {
        Object obj = instance[1].createObject("test", "strings", ClassLoader.getSystemClassLoader(), null, null,
                ObjectFactory.BOTH);

        assertTrue("The type is wrong", obj instanceof String[][]);
        assertEquals("The result is wrong.", 3, ((String[][]) obj)[0].length);
        assertEquals("The result is wrong.", 3, ((String[][]) obj)[1].length);
        assertEquals("The result is wrong.", "has,,{1,2},{3,4}h,:set", ((String[][]) obj)[0][0]);
        assertEquals("The result is wrong.", "012", ((String[][]) obj)[0][1]);
        assertNull("The result is wrong.", ((String[][]) obj)[0][2]);
        assertNull("The result is wrong.", ((String[][]) obj)[1][0]);
        assertEquals("The result is wrong.", "012", ((String[][]) obj)[1][1]);
        assertEquals("The result is wrong.", "458", ((String[][]) obj)[1][2]);
    }

    /**
     * Test createObject7 with valid parameter.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject7_Accuracy12() throws Exception {
        Object obj = instance[1].createObject("objectArray", null, ClassLoader.getSystemClassLoader(), null, null,
                ObjectFactory.SPECIFICATION_ONLY);

        assertTrue("The type is wrong", obj instanceof Object[][][]);
        assertEquals("The result is wrong.", 1, ((Object[]) obj).length);
        assertEquals("The result is wrong.", 1, ((Object[][]) obj)[0].length);
        assertEquals("The result is wrong.", 3, ((Object[][][]) obj)[0][0].length);
        assertEquals("The result is wrong.", 3, ((Object[][][]) obj)[0][0].length);
        assertTrue("The result is wrong.", ((Object[][][]) obj)[0][0][0] instanceof TestClass1);
        assertTrue("The result is wrong.", ((Object[][][]) obj)[0][0][1] instanceof TestClass2);
        assertNull("The result is wrong.", ((Object[][][]) obj)[0][0][2]);
    }

    /**
     * Test createObject7 with valid parameter.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject7_Accuracy13() throws Exception {
        Object obj = instance[1].createObject("test", "booleans", ClassLoader.getSystemClassLoader(), null, null,
                ObjectFactory.BOTH);

        assertTrue("The type is wrong", obj instanceof boolean[]);
        assertEquals("The result is wrong.", 3, ((boolean[]) obj).length);
        assertEquals("The result is wrong.", true, ((boolean[]) obj)[0]);
        assertEquals("The result is wrong.", false, ((boolean[]) obj)[1]);
        assertEquals("The result is wrong.", true, ((boolean[]) obj)[2]);
    }

    /**
     * Test createObject7 with valid parameter.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject7_Accuracy14() throws Exception {
        Object obj = instance[1].createObject("test", "bytes", ClassLoader.getSystemClassLoader(), null, null,
                ObjectFactory.BOTH);

        assertTrue("The type is wrong", obj instanceof byte[]);
        assertEquals("The result is wrong.", 3, ((byte[]) obj).length);
        assertEquals("The result is wrong.", 48, ((byte[]) obj)[0]);
        assertEquals("The result is wrong.", 49, ((byte[]) obj)[1]);
        assertEquals("The result is wrong.", 50, ((byte[]) obj)[2]);
    }

    /**
     * Test createObject7 with valid parameter.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject7_Accuracy15() throws Exception {
        Object obj = instance[1].createObject("test", "chars", ClassLoader.getSystemClassLoader(), null, null,
                ObjectFactory.BOTH);

        assertTrue("The type is wrong", obj instanceof char[]);
        assertEquals("The result is wrong.", 3, ((char[]) obj).length);
        assertEquals("The result is wrong.", '4', ((char[]) obj)[0]);
        assertEquals("The result is wrong.", '5', ((char[]) obj)[1]);
        assertEquals("The result is wrong.", '8', ((char[]) obj)[2]);
    }

    /**
     * Test createObject7 with valid parameter.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject7_Accuracy16() throws Exception {
        Object obj = instance[1].createObject("test", "doubles", ClassLoader.getSystemClassLoader(), null, null,
                ObjectFactory.BOTH);

        assertTrue("The type is wrong", obj instanceof double[]);
        assertEquals("The result is wrong.", 3, ((double[]) obj).length);
        assertEquals("The result is wrong.", 4.0, ((double[]) obj)[0]);
        assertEquals("The result is wrong.", 5.0, ((double[]) obj)[1]);
        assertEquals("The result is wrong.", 8.0, ((double[]) obj)[2]);
    }

    /**
     * Test createObject7 with valid parameter.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject7_Accuracy17() throws Exception {
        Object obj = instance[1].createObject("test", "floats", ClassLoader.getSystemClassLoader(), null, null,
                ObjectFactory.BOTH);

        assertTrue("The type is wrong", obj instanceof float[]);
        assertEquals("The result is wrong.", 3, ((float[]) obj).length);
        assertEquals("The result is wrong.", 4.0f, ((float[]) obj)[0]);
        assertEquals("The result is wrong.", 5.0f, ((float[]) obj)[1]);
        assertEquals("The result is wrong.", 8.0f, ((float[]) obj)[2]);
    }

    /**
     * Test createObject7 with valid parameter.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject7_Accuracy18() throws Exception {
        Object obj = instance[1].createObject("test", "ints", ClassLoader.getSystemClassLoader(), null, null,
                ObjectFactory.BOTH);

        assertTrue("The type is wrong", obj instanceof int[]);
        assertEquals("The result is wrong.", 3, ((int[]) obj).length);
        assertEquals("The result is wrong.", 4, ((int[]) obj)[0]);
        assertEquals("The result is wrong.", 5, ((int[]) obj)[1]);
        assertEquals("The result is wrong.", 8, ((int[]) obj)[2]);
    }

    /**
     * Test createObject7 with valid parameter.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject7_Accuracy19() throws Exception {
        Object obj = instance[1].createObject("test", "longs", ClassLoader.getSystemClassLoader(), null, null,
                ObjectFactory.BOTH);

        assertTrue("The type is wrong", obj instanceof long[]);
        assertEquals("The result is wrong.", 3, ((long[]) obj).length);
        assertEquals("The result is wrong.", 4, ((long[]) obj)[0]);
        assertEquals("The result is wrong.", 5, ((long[]) obj)[1]);
        assertEquals("The result is wrong.", 8, ((long[]) obj)[2]);
    }

    /**
     * Test createObject7 with valid parameter.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject7_Accuracy20() throws Exception {
        Object obj = instance[1].createObject("test", "shorts", ClassLoader.getSystemClassLoader(), null, null,
                ObjectFactory.BOTH);

        assertTrue("The type is wrong", obj instanceof short[]);
        assertEquals("The result is wrong.", 3, ((short[]) obj).length);
        assertEquals("The result is wrong.", 4, ((short[]) obj)[0]);
        assertEquals("The result is wrong.", 5, ((short[]) obj)[1]);
        assertEquals("The result is wrong.", 8, ((short[]) obj)[2]);
    }

    /**
     * Test createObject8 with null type. IllegalArgumentException should be
     * thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject8_NullType() throws Exception {
        try {
            instance[0].createObject((Class<?>) null, "default", ClassLoader.getSystemClassLoader(), new Object[0],
                    new Class<?>[0], ObjectFactory.BOTH);
            fail("IllegalArgumentException should be thrown because of the null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test createObject8 with invalid strategy. IllegalArgumentException should
     * be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject8_InvalidStrategy() throws Exception {
        try {
            instance[1].createObject(String.class, null, ClassLoader.getSystemClassLoader(), new Object[0],
                    new Class<?>[0], "invalid_strategy");
            fail("IllegalArgumentException should be thrown because of the invalid parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test createObject8 with null strategy. IllegalArgumentException should be
     * thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject8_NullStrategy() throws Exception {
        try {
            instance[1].createObject(String.class, null, ClassLoader.getSystemClassLoader(), new Object[0],
                    new Class<?>[0], null);
            fail("IllegalArgumentException should be thrown because of the invalid parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test createObject8 with invalid parameter. IllegalArgumentException
     * should be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject8_InvalidParam1() throws Exception {
        try {
            instance[1].createObject(String.class, null, ClassLoader.getSystemClassLoader(), new Object[0],
                    new Class<?>[] { String.class }, ObjectFactory.REFLECTION_ONLY);
            fail("IllegalArgumentException should be thrown because of the invalid parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test createObject8 with invalid parameter.
     * InvalidClassSpecificationException should be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject8_InvalidParam2() throws Exception {
        try {
            instance[1].createObject(String.class, null, ClassLoader.getSystemClassLoader(),
                    new Object[] { new HashSet<Object>() }, new Class<?>[] { HashSet.class },
                    ObjectFactory.REFLECTION_ONLY);
            fail("InvalidClassSpecificationException should be thrown because of the invalid parameter.");
        } catch (InvalidClassSpecificationException e) {
            // expected.
        }
    }

    /**
     * Test createObject8 with invalid parameter.
     * InvalidClassSpecificationException should be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject8_InvalidParam3() throws Exception {
        try {
            instance[1].createObject(String.class, "invalid_id", ClassLoader.getSystemClassLoader(), null, null,
                    ObjectFactory.SPECIFICATION_ONLY);
            fail("InvalidClassSpecificationException should be thrown because of the invalid parameter.");
        } catch (InvalidClassSpecificationException e) {
            // expected.
        }
    }

    /**
     * Test createObject8 with invalid parameter.
     * InvalidClassSpecificationException should be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject8_InvalidParam4() throws Exception {
        try {
            instance[2].createObject(int.class, "Mismatch", ClassLoader.getSystemClassLoader(), null, null,
                    ObjectFactory.BOTH);
            fail("InvalidClassSpecificationException should be thrown because of the invalid parameter.");
        } catch (InvalidClassSpecificationException e) {
            // expected.
        }
    }

    /**
     * Test createObject8 with valid parameter.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject8_Accuracy1() throws Exception {
        Object obj = instance[0].createObject(TestClass1.class, "default", ClassLoader.getSystemClassLoader(), null,
                null, ObjectFactory.SPECIFICATION_ONLY);

        assertTrue("The type is wrong.", obj instanceof TestClass1);
        assertEquals("The result is wrong.", "TestClass12Strong", obj.toString());
    }

    /**
     * Test createObject8 with valid parameter.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject8_Accuracy2() throws Exception {
        Object obj = instance[0].createObject(TestClass1.class, null, ClassLoader.getSystemClassLoader(), new Object[] {
            new Integer(5), "abc" }, new Class<?>[] { int.class, String.class }, ObjectFactory.REFLECTION_ONLY);

        assertTrue("The type is wrong.", obj instanceof TestClass1);
        assertEquals("The result is wrong.", "TestClass15abc", obj.toString());
    }

    /**
     * Test createObject8 with valid parameter.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject8_Accuracy3() throws Exception {
        Object obj = instance[0].createObject(TestClass1.class, null, ClassLoader.getSystemClassLoader(), new Object[] {
            new Integer(5), "abc" }, new Class<?>[] { int.class, String.class }, ObjectFactory.BOTH);

        assertTrue("The type is wrong.", obj instanceof TestClass1);
        assertEquals("The result is wrong.", "TestClass15abc", obj.toString());
    }

    /**
     * Test createObject8 with valid parameter.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testCreateObject8_Accuracy4() throws Exception {
        Object obj = instance[0].createObject(TestClass1.class, "default", ClassLoader.getSystemClassLoader(),
                new Object[] { new Integer(5), "abc" }, new Class<?>[] { int.class, String.class }, ObjectFactory.BOTH);

        assertTrue("The type is wrong.", obj instanceof TestClass1);
        assertEquals("The result is wrong.", "TestClass12Strong", obj.toString());
    }

    /**
     * Test getObjectBySimpleType. The result should be Boolean, Byte,
     * Character, Double, Float, Integer, Long, Short, String object.
     *
     * @throws Exception
     *             to JUnit.
     * @since 2.2
     */
    public void testGetObjectBySimpleType() throws Exception {
        Method method = TestHelper.getPrivateMethod(ObjectFactory.class, "getObjectBySimpleType", new Class<?>[] {
            String.class, String.class });
        assertEquals("fail to get Boolean object", new Boolean(true), method.invoke(null, "boolean", "true"));
        assertEquals("fail to get Byte object", new Byte("21"), method.invoke(null, "byte", "21"));
        assertEquals("fail to get Character object", new Character('c'), method.invoke(null, "char", "c"));
        assertEquals("fail to get Float object", new Float("0.01"), method.invoke(null, "float", "0.01"));
        assertEquals("fail to get Integer object", new Integer("33"), method.invoke(null, "int", "33"));
        assertEquals("fail to get Long object", new Long(1), method.invoke(null, "long", "1"));
        assertEquals("fail to get Short object", new Short("1"), method.invoke(null, "short", "1"));
        assertEquals("faile to get String object", "123", method.invoke(null, "String", "123"));
        assertEquals("faile to get String object", "123", method.invoke(null, "java.lang.String", "123"));
        method.setAccessible(false);
    }

    /**
     * Failure test for <code>getObjectBySimpleType</code>. Expect
     * InvalidClassSpecificationException because the "abc" is not the simple
     * type.
     *
     * @throws Exception
     *             to JUnit.
     * @since 2.2
     */
    public void testGetObjectBySimpleTypeFailure1() throws Exception {
        Method method = TestHelper.getPrivateMethod(ObjectFactory.class, "getObjectBySimpleType", new Class<?>[] {
            String.class, String.class });
        try {
            method.invoke(null, "abc", "abc");
        } catch (InvocationTargetException e) {
            assertTrue("should throw InvalidClassSpecificationException",
                    e.getCause() instanceof InvalidClassSpecificationException);
            method.setAccessible(false);
        }
    }

    /**
     * Failure test for <code>getObjectBySimpleType</code>. Expect
     * InvalidClassSpecificationException because the "abc" can not covert to
     * Integer object.
     *
     * @throws Exception
     *             to JUnit.
     * @since 2.2
     */
    public void testGetObjectBySimpleTypeFailure2() throws Exception {
        Method method = TestHelper.getPrivateMethod(ObjectFactory.class, "getObjectBySimpleType", new Class<?>[] {
            String.class, String.class });
        try {
            method.invoke(null, "int", "abc");
        } catch (InvocationTargetException e) {
            assertTrue("should throw InvalidClassSpecificationException",
                    e.getCause() instanceof InvalidClassSpecificationException);
            method.setAccessible(false);
        }
    }

    /**
     * Failure test for <code>getObjectBySimpleType</code>. Expect
     * InvalidClassSpecificationException because the the value can not be empty
     * when the type is <code>char</code>.
     *
     * @throws Exception
     *             to JUnit.
     * @since 2.2
     */
    public void testGetObjectBySimpleTypeFailure3() throws Exception {
        Method method = TestHelper.getPrivateMethod(ObjectFactory.class, "getObjectBySimpleType", new Class<?>[] {
            String.class, String.class });
        try {
            method.invoke(null, "char", "");
        } catch (InvocationTargetException e) {
            assertTrue("should throw InvalidClassSpecificationException",
                    e.getCause() instanceof InvalidClassSpecificationException);
            method.setAccessible(false);
        }
    }

    /**
     * Test <code>copyMultiDimArray</code>. Should set <code>boolean</code>,
     * <code>double</code>, <code>float</code>, <code>long</code> and others
     * value.
     *
     * @throws Exception
     *             to JUnit.
     * @since 2.2
     */
    public void testCopyMultiDimArray() throws Exception {
        Method method = TestHelper.getPrivateMethod(ObjectFactory.class, "copyMultiDimArray", new Class<?>[] {
            String.class, int.class, Object.class, Object[].class });
        ObjectSpecification objectSpecification = new ObjectSpecification("simple", "abc", "boolean", "abc", "true", 1,
            null);
        boolean[] array1 = { false };
        method.invoke(null, "boolean", 1, array1, new Object[] { objectSpecification });
        assertTrue("fail to set boolean value", array1[0]);
        objectSpecification = new ObjectSpecification("simple", "abc", "double", "abc", "0.1", 1, null);
        double[] array2 = { 0.2 };
        method.invoke(null, "double", 1, array2, new Object[] { objectSpecification });
        assertEquals("fail to set double value ", 0.1, array2[0]);
        objectSpecification = new ObjectSpecification("simple", "abc", "float", "abc", "0.1", 1, null);
        float[] array3 = { 1 };
        method.invoke(null, "float", 1, array3, new Object[] { objectSpecification });
        assertEquals("fail to set float value", (float) 0.1, array3[0]);
        objectSpecification = new ObjectSpecification("simple", "abc", "long", "abc", "1", 1, null);
        long[] array4 = { 2 };
        method.invoke(null, "long", 1, array4, new Object[] { objectSpecification });
        assertEquals("fail to set long value", 1, array4[0]);
        method.invoke(null, "int", 1, array4, new Object[] { 3 });
        assertEquals("fail to set other value", 3, array4[0]);
        method.setAccessible(false);
    }

    /**
     * Failure test for copyMultiDimArray. Expected the
     * InvalidClassSpecificationException because the ClassCastException is
     * thrown.
     *
     * @throws Exception
     *             to JUnit.
     * @since 2.2
     */
    public void testCopyMultiDimArrayFailure1() throws Exception {
        Method method = TestHelper.getPrivateMethod(ObjectFactory.class, "copyMultiDimArray", new Class<?>[] {
            String.class, int.class, Object.class, Object[].class });
        ObjectSpecification objectSpecification = new ObjectSpecification("simple", "abc", "boolean", "abc", "true", 1,
            null);
        boolean[] array1 = { false };
        try {
            method.invoke(null, "int", 1, array1, new Object[] { objectSpecification });
        } catch (InvocationTargetException e) {
            assertTrue("should throw ClassCastException", e.getCause().getCause() instanceof ClassCastException);
            method.setAccessible(false);
        }
    }

    /**
     * Failure test for copyMultiDimArray. Expected the
     * InvalidClassSpecificationException because the IndexOutOfBoundsException
     * is thrown.
     *
     * @throws Exception
     *             to JUnit.
     * @since 2.2
     */
    public void testCopyMultiDimArrayFailure2() throws Exception {
        Method method = TestHelper.getPrivateMethod(ObjectFactory.class, "copyMultiDimArray", new Class<?>[] {
            String.class, int.class, Object.class, Object[].class });
        ObjectSpecification objectSpecification = new ObjectSpecification("simple", "abc", "boolean", "abc", "true", 1,
            null);
        boolean[] array1 = {};
        try {
            method.invoke(null, "boolean", 1, array1, new Object[] { objectSpecification });
        } catch (InvocationTargetException e) {
            assertTrue("should throw IndexOutOfBoundsException",
                e.getCause().getCause() instanceof IndexOutOfBoundsException);
            method.setAccessible(false);
        }
    }

    /**
     * Test for resolveClass. The result should be class of <code>boolean</code>
     * , <code>double</code>, <code>long</code> .
     *
     * @throws Exception
     *             to JUnit.
     * @since 2.2
     */
    public void testResolveClass() throws Exception {
        Method method = TestHelper.getPrivateMethod(ObjectFactory.class, "resolveClass", new Class<?>[] { String.class,
            ClassLoader.class });
        assertEquals("should return boolean.class", boolean.class, method.invoke(null, "boolean", null));
        assertEquals("should return double.class", double.class, method.invoke(null, "double", null));
        assertEquals("should return long.class", long.class, method.invoke(null, "long", null));
        assertEquals("should return short.class", short.class, method.invoke(null, "short", null));
        method.setAccessible(false);
    }

    /**
     * Failure test for createObjectByEnumeration. Expected
     * InvalidClassSpecificationException because the "abc" is not a class type.
     *
     * @throws Exception
     *             to JUnit.
     * @since 2.2
     */
    public void testCreateObjectByEnumerationFailure() throws Exception {
        Method method = TestHelper.getPrivateMethod(ObjectFactory.class, "createObjectByEnumeration", new Class<?>[] {
            String.class, ClassLoader.class, Object[].class });
        try {
            method.invoke(null, "abc", null, null);
        } catch (InvocationTargetException e) {
            assertTrue("should throw ClassNotFoundException", e.getCause().getCause() instanceof ClassNotFoundException);
            method.setAccessible(false);
        }
    }

    /**
     * Failure test for createComplexSpecification. Expected
     * InvalidClassSpecificationException because the "abc" is not a valid url.
     *
     * @throws Exception
     *             to JUnit.
     * @since 2.2
     */
    public void testCreateComplexSpecificationFailure() throws Exception {
        Method method = TestHelper.getPrivateMethod(ObjectFactory.class, "createComplexSpecification", new Class<?>[] {
            ObjectSpecification.class, Object[].class, Class[].class });
        ObjectSpecification objectSpecification = new ObjectSpecification("simple", "abc", "boolean", "abc", "true", 1,
            null);
        try {
            method.invoke(null, objectSpecification, null, null);
        } catch (InvocationTargetException e) {
            assertTrue("should throw MalformedURLException", e.getCause().getCause() instanceof MalformedURLException);
            method.setAccessible(false);
        }
    }

    /**
     * Failure test for getObjectBySpecification. Expected
     * InvalidClassSpecificationException because the "abc" is not a valid url.
     *
     * @throws Exception
     *             to JUnit.
     * @since 2.2
     */
    public void testGetObjectBySpecificationFailure() throws Exception {
        Method method = TestHelper.getPrivateMethod(ObjectFactory.class, "getObjectBySpecification",
            new Class<?>[] { ObjectSpecification.class });
        ObjectSpecification objectSpecification = new ObjectSpecification("simple", "abc", "boolean", "abc", "true", 1,
            null);
        TestHelper.setPrivateField(ObjectSpecification.class, objectSpecification, "specType", "array");
        TestHelper
            .setPrivateField(ObjectSpecification.class, objectSpecification, "parameters", new Object[] { "abc" });
        try {
            method.invoke(null, objectSpecification);
        } catch (InvocationTargetException e) {
            assertTrue("should throw MalformedURLException", e.getCause().getCause() instanceof MalformedURLException);
            method.setAccessible(false);
        }
    }

    /**
     * Failure test for createParameterObjects. Expected
     * InvalidClassSpecificationException because the "abc" is not a valid url.
     *
     * @throws Exception
     *             to JUnit.
     * @since 2.2
     */
    public void testCreateParameterObjectsFailure1() throws Exception {
        Method method = TestHelper.getPrivateMethod(ObjectFactory.class, "createParameterObjects",
            new Class<?>[] { Object[].class });
        ObjectSpecification objectSpecification = new ObjectSpecification("simple", "abc", "boolean", "abc", "true", 1,
            null);
        Object[] params = new Object[] { objectSpecification };
        try {
            method.invoke(null, (Object) params);
        } catch (InvocationTargetException e) {
            assertTrue("should throw MalformedURLException", e.getCause().getCause() instanceof MalformedURLException);
            method.setAccessible(false);
        }
    }

    /**
     * Failure test for createParameterObjects. Expected
     * InvalidClassSpecificationException because the params contains null.
     *
     * @throws Exception
     *             to JUnit.
     * @since 2.2
     */
    public void testCreateParameterObjectsFailure2() throws Exception {
        Method method = TestHelper.getPrivateMethod(ObjectFactory.class, "createParameterObjects",
            new Class<?>[] { Object[].class });
        ObjectSpecification objectSpecification = new ObjectSpecification("simple", null, "boolean", "abc", "true", 1,
            null);
        Object[] params = new Object[] { objectSpecification, null };
        try {
            method.invoke(null, (Object) params);
        } catch (InvocationTargetException e) {
            assertTrue("should throw InvalidClassSpecificationException",
                e.getCause() instanceof InvalidClassSpecificationException);
            method.setAccessible(false);
        }
    }
}
