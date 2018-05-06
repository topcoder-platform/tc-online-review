/*
 * Copyright (C) 2006, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.objectfactory;

import java.net.URL;
import java.util.Collection;

import junit.framework.TestCase;

import com.topcoder.util.objectfactory.impl.ConfigManagerSpecificationFactory;
import com.topcoder.util.objectfactory.testclasses.TestClass1;
import com.topcoder.util.objectfactory.testclasses.TestClass2;

/**
 * Demo for the component.
 *
 * @author mgmg, TCSDEVELOPER
 * @version 2.2
 */
public class Demo extends TestCase {
    /**
     * Create the test instance.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void setUp() throws Exception {
        TestHelper.clearConfig();
        TestHelper.loadSingleConfigFile();
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
     * Convenience demo.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testDemo1() throws Exception {
        // instantiate factory with specification factory
        ObjectFactory factory = new ObjectFactory(new ConfigManagerSpecificationFactory("valid_config"),
                ObjectFactory.BOTH);

        // obtain the configured default frac
        TestClass1 aClass = (TestClass1) factory.createObject("frac", "default");
        assertNotNull(aClass);

        // obtain the TestClass2, without identifier.
        TestClass2 aFrac = (TestClass2) factory.createObject("bar");
        assertNotNull(aFrac);

        // change initialization strategy
        factory.setInitStrategy(ObjectFactory.REFLECTION_ONLY);

        // obtain com.test.TestComplex object in specified jar file. Will use
        // reflection only.
        Object testComplex = factory.createObject("com.topcoder.util.objectfactory.testclasses.TestClass2");
        assertNotNull(testComplex);
    }

    /**
     * Main method demo.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testDemo2() throws Exception {
        // instantiate factory with specification factory
        ObjectFactory factory = new ObjectFactory(new ConfigManagerSpecificationFactory("valid_config"));

        // obtain the configured bar, without using the identifier, and rest as
        // defaults
        TestClass2 bar = (TestClass2) factory.createObject("bar", null, (ClassLoader) null, null, null,
                ObjectFactory.BOTH);
        assertNotNull(bar);

        // obtain TestComplex object, but use this jar and parameters instead,
        Object[] params = { new Integer(12), "abc" };
        URL url = new URL(TestHelper.getURLString("test_files/test.jar"));
        Class<?>[] paramTypes = { int.class, String.class };
        Object complex = factory
                .createObject("com.test.TestComplex", null, url, params, paramTypes, ObjectFactory.BOTH);
        assertNotNull(complex);

        // obtain another TestClass1 object with the same params, but just use
        // reflection
        TestClass1 bar2 = (TestClass1) factory.createObject(TestClass1.class, null, (ClassLoader) null, params,
                paramTypes, ObjectFactory.REFLECTION_ONLY);
        assertNotNull(bar2);

        // obtain Collection array, but just use specification
        Collection<?>[] bar3 = (Collection<?>[]) factory.createObject(Collection.class, "collection",
                (ClassLoader) null, params, paramTypes, ObjectFactory.SPECIFICATION_ONLY);
        assertNotNull(bar3);

        // obtain TestClass1 object using reflection from a specified
        // ClassLoader, using params but no paramTypes since no nulls used.
        params = new Double[] { new Double(12.00) };
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        TestClass1 bar4 = (TestClass1) factory.createObject(TestClass1.class, null, loader, params, null,
                ObjectFactory.REFLECTION_ONLY);
        assertNotNull(bar4);
    }
}
