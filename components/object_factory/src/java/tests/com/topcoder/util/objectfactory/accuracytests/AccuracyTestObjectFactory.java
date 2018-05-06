/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.objectfactory.accuracytests;

import com.topcoder.util.objectfactory.ObjectFactory;
import com.topcoder.util.objectfactory.impl.ConfigManagerSpecificationFactory;

import junit.framework.Test;
import junit.framework.TestSuite;

import java.io.File;

import java.net.URL;
import java.net.URLClassLoader;

import java.util.ArrayList;

/**
 * Tests the class ObjectFactory for accuracy.
 *
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class AccuracyTestObjectFactory extends AccuracyTestBaseClass {
    /** The name of the mock class used for test. */
    private static final String MOCK_CLASS_NAME = "com.topcoder.util.objectfactory.accuracytests.MockClass2";

    /** The value of the identifier used for test. */
    private static final String IDENTIFIER = "identifier";

    /** The url of the jar file used for test. */
    private URL url;

    /** The instance of the mock class used for test. */
    private Class clazz;

    /** The instance of ObjectFactory used for test. */
    private ObjectFactory objectFactory;

    /**
     * Returns the suit to run the test.
     *
     * @return suite to run the test
     */
    public static Test suite() {
        return new TestSuite(AccuracyTestObjectFactory.class);
    }

    /**
     * See javadoc for junit.framework.TestCase#setUp().
     *
     * @throws Exception exception that occurs during the setup process.
     *
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp() throws Exception {
        super.setUp();

        url = new File("test_files/accuracytests/MockClass2.jar").toURL();
        clazz = new URLClassLoader(new URL[] {url}).loadClass(MOCK_CLASS_NAME);
    }

    /**
     * Tests getInitStrategy() and setInitStrategy() with complex type configuration.
     *
     * @throws Exception to JUnit from test cases.
     */
    public void testGetAndSetInitStrategy() throws Exception {
        objectFactory = new ObjectFactory(new ConfigManagerSpecificationFactory(COMPLEX_NAMESPACE),
            ObjectFactory.SPECIFICATION_ONLY);

        assertTrue(objectFactory.getInitStrategy().equals(ObjectFactory.SPECIFICATION_ONLY));

        objectFactory.setInitStrategy(ObjectFactory.REFLECTION_ONLY);

        assertTrue(objectFactory.getInitStrategy().equals(ObjectFactory.REFLECTION_ONLY));

        objectFactory.setInitStrategy(ObjectFactory.BOTH);

        assertTrue(objectFactory.getInitStrategy().equals(ObjectFactory.BOTH));
    }

    /**
     * Tests createObject() with complex type configuration.
     *
     * @throws Exception to JUnit from test cases.
     */
    public void testCreateObjectComplex() throws Exception {
        objectFactory = new ObjectFactory(new ConfigManagerSpecificationFactory(COMPLEX_NAMESPACE),
            ObjectFactory.SPECIFICATION_ONLY);

        MockClass1 mock1 = (MockClass1) objectFactory.createObject("mock1");

        assertNotNull("The instance of MockClass1 is not valid.", mock1);

        assertTrue("The int value is not valid.", mock1.getIntValue() == 7);
        assertTrue("The String value is not valid.", mock1.getStrValue().equals("TEST"));

        MockClass2 mock2 = mock1.getMockClass2();

        assertTrue(mock2.getBuffer().toString().equals("StringBuffer"));
        assertNull(mock2.getFirstList());
        assertTrue(mock2.getSecondList().size() == 0);
    }

    /**
     * Tests createObject() with int array type configuration.
     *
     * @throws Exception to JUnit from test cases.
     */
    public void testCreateObjectIntArray1() throws Exception {
        objectFactory = new ObjectFactory(new ConfigManagerSpecificationFactory(INT_ARRAY_NAMESPACE),
            ObjectFactory.SPECIFICATION_ONLY);

        int[] array = (int[]) objectFactory.createObject("intArray1");

        assertTrue("The length is not valid.", array.length == 2);

        assertTrue("The value is not valid.", array[0] == 1);
        assertTrue("The value is not valid.", array[1] == 2);
    }

    /**
     * Tests createObject() with int array type configuration.
     *
     * @throws Exception to JUnit from test cases.
     */
    public void testCreateObjectIntArray2() throws Exception {
        objectFactory = new ObjectFactory(new ConfigManagerSpecificationFactory(INT_ARRAY_NAMESPACE),
            ObjectFactory.SPECIFICATION_ONLY);

        int[][][] array = (int[][][]) objectFactory.createObject("intArray2");

        assertTrue("The length is not valid.", array.length == 1);
        assertTrue("The length is not valid.", array[0].length == 2);
        assertTrue("The length is not valid.", array[0][0].length == 2);
        assertTrue("The length is not valid.", array[0][1].length == 2);

        assertTrue("The value is not valid.", array[0][0][0] == 1);
        assertTrue("The value is not valid.", array[0][0][1] == 2);
        assertTrue("The value is not valid.", array[0][1][0] == 3);
        assertTrue("The value is not valid.", array[0][1][1] == 4);
    }

    /**
     * Tests createObject() with char array type configuration.
     *
     * @throws Exception to JUnit from test cases.
     */
    public void testCreateObjectCharArray() throws Exception {
        objectFactory = new ObjectFactory(new ConfigManagerSpecificationFactory(CHAR_ARRAY_NAMESPACE),
            ObjectFactory.SPECIFICATION_ONLY);

        char[] array = (char[]) objectFactory.createObject("charArray");

        assertTrue("The length is not valid.", array.length == 6);

        assertTrue("The value is not valid.", array[0] == ' ');
        assertTrue("The value is not valid.", array[1] == '\\');
        assertTrue("The value is not valid.", array[2] == '{');
        assertTrue("The value is not valid.", array[3] == '}');
        assertTrue("The value is not valid.", array[4] == ',');
        assertTrue("The value is not valid.", array[5] == '\'');
    }

    /**
     * Tests createObject() with String array type configuration.
     *
     * @throws Exception to JUnit from test cases.
     */
    public void testCreateObjectStringArray() throws Exception {
        objectFactory = new ObjectFactory(new ConfigManagerSpecificationFactory(STRING_ARRAY_NAMESPACE),
            ObjectFactory.SPECIFICATION_ONLY);

        String[][][] array = (String[][][]) objectFactory.createObject("StringArray");

        assertTrue("The length is not valid.", array.length == 2);
        assertTrue("The length is not valid.", array[0].length == 2);
        assertTrue("The length is not valid.", array[0][0].length == 2);
        assertTrue("The length is not valid.", array[0][1].length == 2);
        assertTrue("The length is not valid.", array[1].length == 2);
        assertTrue("The length is not valid.", array[1][0].length == 2);
        assertTrue("The length is not valid.", array[1][1].length == 2);

        assertTrue("The value is not valid.", array[0][0][0].equals("\'[/\\]\'"));
        assertTrue("The value is not valid.", array[0][0][1].equals(","));
        assertTrue("The value is not valid.", array[0][1][0].equals("{"));
        assertTrue("The value is not valid.", array[0][1][1].equals("}"));
        assertTrue("The value is not valid.", array[1][0][0].equals("."));
        assertTrue("The value is not valid.", array[1][0][1].equals("'"));
        assertTrue("The value is not valid.", array[1][1][0].equals(""));
        assertTrue("The value is not valid.", array[1][1][1].equals("8"));
    }

    /**
     * Tests createObject() with complex array type configuration.
     *
     * @throws Exception to JUnit from test cases.
     */
    public void testCreateObjectComplexArray() throws Exception {
        objectFactory = new ObjectFactory(new ConfigManagerSpecificationFactory(INTEGER_ARRAY_NAMESPACE),
            ObjectFactory.SPECIFICATION_ONLY);

        Integer[] array = (Integer[]) objectFactory.createObject("IntegerArray");

        assertTrue("The length is not valid.", array.length == 2);

        assertTrue("The value is not valid.", array[0].equals(new Integer("100")));
        assertNull("The value is not valid.", array[1]);
    }

    /**
     * Tests createObject(String).
     *
     * @throws Exception to JUnit from test cases.
     */
    public void testCreateObjectString() throws Exception {
        objectFactory = new ObjectFactory(new ConfigManagerSpecificationFactory(COMPLEX_NAMESPACE),
            ObjectFactory.REFLECTION_ONLY);

        assertTrue("The object is not valid.", objectFactory.createObject("java.lang.StringBuffer").toString().equals(
            ""));
    }

    /**
     * Tests createObject(Class).
     *
     * @throws Exception to JUnit from test cases.
     */
    public void testCreateObjectClass() throws Exception {
        objectFactory = new ObjectFactory(new ConfigManagerSpecificationFactory(COMPLEX_NAMESPACE),
            ObjectFactory.REFLECTION_ONLY);

        assertTrue("The object is not valid.", objectFactory.createObject(StringBuffer.class).toString().equals(""));
    }

    /**
     * Tests createObject(String, String).
     *
     * @throws Exception to JUnit from test cases.
     */
    public void testCreateObjectStringString() throws Exception {
        objectFactory = new ObjectFactory(new ConfigManagerSpecificationFactory(COMPLEX_NAMESPACE),
            ObjectFactory.REFLECTION_ONLY);

        assertTrue(objectFactory.createObject("java.lang.StringBuffer", IDENTIFIER).toString().equals(""));
    }

    /**
     * Tests createObject(Class, String).
     *
     * @throws Exception to JUnit from test cases.
     */
    public void testCreateObjectClassString() throws Exception {
        objectFactory = new ObjectFactory(new ConfigManagerSpecificationFactory(COMPLEX_NAMESPACE),
            ObjectFactory.REFLECTION_ONLY);

        assertTrue(objectFactory.createObject(StringBuffer.class, IDENTIFIER).toString().equals(""));
    }

    /**
     * Tests createObject(String, String, URL, Object[], Class[], String).
     *
     * @throws Exception to JUnit from test cases.
     */
    public void testCreateObjectStringStringURLObjectArrayClassArrayString() throws Exception {
        objectFactory = new ObjectFactory(new ConfigManagerSpecificationFactory(COMPLEX_NAMESPACE),
            ObjectFactory.REFLECTION_ONLY);

        MockClass2 mock2 = (MockClass2) objectFactory.createObject(MOCK_CLASS_NAME, IDENTIFIER, url, new Object[] {
            new StringBuffer("test"), new ArrayList(), null}, new Class[] {StringBuffer.class, ArrayList.class,
            ArrayList.class}, ObjectFactory.REFLECTION_ONLY);

        assertTrue(mock2.getBuffer().toString().equals("test"));
        assertTrue(mock2.getFirstList().size() == 0);
        assertNull(mock2.getSecondList());
    }

    /**
     * Tests createObject(Class, String, URL, Object[], Class[], String).
     *
     * @throws Exception to JUnit from test cases.
     */
    public void testCreateObjectClassStringURLObjectArrayClassArrayString() throws Exception {
        objectFactory = new ObjectFactory(new ConfigManagerSpecificationFactory(COMPLEX_NAMESPACE),
            ObjectFactory.REFLECTION_ONLY);

        MockClass2 mock2 = (MockClass2) objectFactory.createObject(clazz, IDENTIFIER, url, new Object[] {
            new StringBuffer("test"), new ArrayList(), null}, new Class[] {StringBuffer.class, ArrayList.class,
            ArrayList.class}, ObjectFactory.REFLECTION_ONLY);

        assertTrue(mock2.getBuffer().toString().equals("test"));
        assertTrue(mock2.getFirstList().size() == 0);
        assertNull(mock2.getSecondList());
    }

    /**
     * Tests createObject(String, String, ClassLoader, Object[], Class[], String).
     *
     * @throws Exception to JUnit from test cases.
     */
    public void testCreateObjectStringStringClassLoaderObjectArrayClassArrayString() throws Exception {
        objectFactory = new ObjectFactory(new ConfigManagerSpecificationFactory(COMPLEX_NAMESPACE),
            ObjectFactory.REFLECTION_ONLY);

        MockClass2 mock2 = (MockClass2) objectFactory.createObject(MOCK_CLASS_NAME, IDENTIFIER, new URLClassLoader(
            new URL[] {url}), new Object[] {new StringBuffer("test"), new ArrayList(), null}, new Class[] {
            StringBuffer.class, ArrayList.class, ArrayList.class}, ObjectFactory.REFLECTION_ONLY);

        assertTrue(mock2.getBuffer().toString().equals("test"));
        assertTrue(mock2.getFirstList().size() == 0);
        assertNull(mock2.getSecondList());
    }

    /**
     * Tests createObject(Class, String, ClassLoader, Object[], Class[], String).
     *
     * @throws Exception to JUnit from test cases.
     */
    public void testCreateObjectClassStringClassLoaderObjectArrayClassArrayString() throws Exception {
        objectFactory = new ObjectFactory(new ConfigManagerSpecificationFactory(COMPLEX_NAMESPACE),
            ObjectFactory.REFLECTION_ONLY);

        MockClass2 mock2 = (MockClass2) objectFactory.createObject(clazz, IDENTIFIER, url, new Object[] {
            new StringBuffer("test"), new ArrayList(), null}, new Class[] {StringBuffer.class, ArrayList.class,
            ArrayList.class}, ObjectFactory.REFLECTION_ONLY);

        assertTrue(mock2.getBuffer().toString().equals("test"));
        assertTrue(mock2.getFirstList().size() == 0);
        assertNull(mock2.getSecondList());
    }
}
