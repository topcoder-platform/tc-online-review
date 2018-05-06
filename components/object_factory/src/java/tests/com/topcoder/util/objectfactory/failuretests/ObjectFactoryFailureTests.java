/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.objectfactory.failuretests;

import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.objectfactory.InvalidClassSpecificationException;
import com.topcoder.util.objectfactory.ObjectFactory;
import com.topcoder.util.objectfactory.SpecificationFactory;
import com.topcoder.util.objectfactory.impl.ConfigManagerSpecificationFactory;

import junit.framework.TestCase;

import java.io.File;

import java.net.URL;
import java.net.URLClassLoader;

import java.util.Iterator;


/**
 * <p>This UnitTest of ObjectFactory.</p>
 *
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class ObjectFactoryFailureTests extends TestCase {
    /**
    * Represents the configuration manager used in tests.
    */
    private static final ConfigManager CONFIG = ConfigManager.getInstance();

    /**
     * The xml file path.
     */
    private static final String XML = "failure/config.xml";

    /**
     * The default namespace.
     */
    private static final String NAMESPACE = "com.topcoder.util.objectfactory.impl";

    /**
         * The ConfigManagerSpecificationFactory instance used to test.
         */
    private SpecificationFactory specificationFactory = null;

    /**
     * The ObjectFactory instance used to test.
     */
    private ObjectFactory objectFactory = null;

    /**
     * The URL member.
     */
    private URL url = null;

    /**
     * The ClassLoader member.
     */
    private ClassLoader loader = null;

    /**
     * The setUp of the unit test.
     *
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        for (Iterator<String> iter = CONFIG.getAllNamespaces(); iter.hasNext();) {
            CONFIG.removeNamespace((String) iter.next());
        }

        CONFIG.add(XML);
        specificationFactory = new ConfigManagerSpecificationFactory(NAMESPACE);
        objectFactory = new ObjectFactory(specificationFactory);
        url = new File("File").toURL();
        loader = new URLClassLoader(new URL[] {url});
    }

    /**
     * The tearDown of the Unit test.
     *
     * @throws Exception to JUnit
     */
    protected void tearDown() throws Exception {
        for (Iterator<String> iter = CONFIG.getAllNamespaces(); iter.hasNext();) {
            CONFIG.removeNamespace((String) iter.next());
        }
    }

    /**
     * Test the constructor of ObjectFactory with SpecificationFactory, fail
     * for SpecificationFactory is null, IllegalArgumentException should be thrown.
     *
     */
    public void testconstructor_FailForNull() {
        try {
            new ObjectFactory(null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * Test the constructor of ObjectFactory with SpecificationFactory and initStrategy, fail
     * for SpecificationFactory is null, IllegalArgumentException should be thrown.
     *
     */
    public void testconstructor_FailForSpecificationFactoryNull() {
        try {
            new ObjectFactory(null, ObjectFactory.REFLECTION_ONLY);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * Test the constructor of ObjectFactory with SpecificationFactory and initStrategy, fail
     * for initStrategy is null, IllegalArgumentException should be thrown.
     *
     */
    public void testconstructor_FailForinitStrategyNull() {
        try {
            new ObjectFactory(specificationFactory, null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * Test the constructor of ObjectFactory with SpecificationFactory and initStrategy, fail
     * for initStrategy is invalid, IllegalArgumentException should be thrown.
     *
     */
    public void testconstructor_FailForinitStrategyInvalid() {
        try {
            new ObjectFactory(specificationFactory, "invalid");
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * Test the constructor of ObjectFactory with SpecificationFactory and initStrategy, success.
     *
     */
    public void testconstructorWithinitStrategy_Auccracy() {
        try {
            new ObjectFactory(specificationFactory, ObjectFactory.SPECIFICATION_ONLY);
        } catch (Exception e) {
            fail("No Exception should be thrown.");
        }
    }

    /**
     * Test the method createObject(String), fail for String is null,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testcreateObjectWithString_failFornull() throws Exception {
        try {
            objectFactory.createObject((String) null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * Test the method createObject(String), fail for String is invalid,
     * InvalidClassSpecificationException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testcreateObjectWithString_failForInvalidKey() throws Exception {
        try {
            objectFactory.createObject("invalid String");
            fail("InvalidClassSpecificationException should be thrown.");
        } catch (InvalidClassSpecificationException e) {
            //pass
        }
    }

    /**
     * Test the method createObject(String), fail for String is empty,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testcreateObjectWithString_failForEmpty() throws Exception {
        try {
            objectFactory.createObject("");
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * Test the method createObject(Class), fail for Class is null,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testcreateObjectWithClass_failFornull() throws Exception {
        try {
            objectFactory.createObject((String) null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * Test the method createObject(String), fail for Class is invalid,
     * InvalidClassSpecificationException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testcreateObjectWithString_failForInvalidClass() throws Exception {
        try {
            objectFactory.createObject(MockSubDataBean.class);
            fail("InvalidClassSpecificationException should be thrown.");
        } catch (InvalidClassSpecificationException e) {
            //pass
        }
    }

    /**
     * Test the method createObject(String, String), fail for key is null,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testcreateObjectWithStringString_failForKeynull() throws Exception {
        try {
            objectFactory.createObject((String) null, "idn");
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * Test the method createObject(String, String), fail for key is empty,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testcreateObjectWithStringString_failForKeyEmpty() throws Exception {
        try {
            objectFactory.createObject("", "idn");
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * Test the method createObject(String, String), fail for identifier is empty,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testcreateObjectWithStringString_failForidentifierEmpty() throws Exception {
        try {
            objectFactory.createObject("key", "");
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * Test the method createObject(String, String), fail for identifier is null,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testcreateObjectWithStringString_failForidentifierNull() throws Exception {
        try {
            objectFactory.createObject("key", null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * Test the method createObject(String, String), fail for key and identifier are invalid,
     * InvalidClassSpecificationException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testcreateObjectWithStringString_failForinvalidKeyAndidentifier() throws Exception {
        try {
            objectFactory.createObject("key", "invalid");
            fail("InvalidClassSpecificationException should be thrown.");
        } catch (InvalidClassSpecificationException e) {
            //pass
        }
    }

    /**
     * Test the method createObject(Class, String), fail for class is null,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testcreateObjectWithClassString_failForClassnull() throws Exception {
        try {
            objectFactory.createObject((Class<?>) null, "idn");
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * Test the method createObject(Class, String), fail for identifier is empty,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testcreateObjectWithClassString_failForidentifierEmpty() throws Exception {
        try {
            objectFactory.createObject(int.class, "");
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * Test the method createObject(Class, String), fail for identifier is null,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testcreateObjectWithClassString_failForidentifierNull() throws Exception {
        try {
            objectFactory.createObject("key", null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * Test the method createObject(Classs, String), fail for class and identifier is invalid,
     * InvalidClassSpecificationException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testcreateObjectWithClassString_failForinvalidKeyAndidentifier() throws Exception {
        try {
            objectFactory.setInitStrategy(ObjectFactory.SPECIFICATION_ONLY);
            objectFactory.createObject(String.class, "invalid");
            fail("InvalidClassSpecificationException should be thrown.");
        } catch (InvalidClassSpecificationException e) {
            //pass
        }
    }

    /**
     * Test the method createObject with URL, fail for key is null,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testcreateObjectWithURL_failForForKeyisNull() throws Exception {
        try {
            objectFactory.createObject((String) null, "identifier", url, new Object[0], new Class[0],
                ObjectFactory.BOTH);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * Test the method createObject with URL, fail for key is Empty,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testcreateObjectWithURL_failForKeyisEmpty() throws Exception {
        try {
            objectFactory.createObject(" ", "identifier", url, new Object[0], new Class[0], ObjectFactory.BOTH);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * Test the method createObject with URL, fail for key is Empty,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testcreateObjectWithURL_failForlengthNotEqual() throws Exception {
        try {
            objectFactory.createObject("key", "identifier", url, new Object[1], new Class[0], ObjectFactory.BOTH);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * Test the method createObject with URL, fail for the key and identifier is invalid,
     * InvalidClassSpecificationException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testcreateObjectWithURL_failForInvalidkey() throws Exception {
        try {
            objectFactory.createObject("key", "identifier", url, new Object[0], new Class[0],
                ObjectFactory.SPECIFICATION_ONLY);
            fail("InvalidClassSpecificationException should be thrown.");
        } catch (InvalidClassSpecificationException e) {
            //pass
        }
    }

    /**
     * Test the method createObject with URL, fail for the initStrategy is invalid,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testcreateObjectWithURL_failForInvalidinitStrategy() throws Exception {
        try {
            objectFactory.createObject("key", "identifier", url, new Object[1], new Class[0], "invalid");
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * Test the method createObject with Class and URL, fail for class is null,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testcreateObjectWithClass_failForForClassisNull() throws Exception {
        try {
            objectFactory.createObject((Class<?>) null, "identifier", url, new Object[0], new Class[0],
                ObjectFactory.BOTH);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * Test the method createObject with Class and URL, fail for key is Empty,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testcreateObjectWithClass_failForlengthNotEqual() throws Exception {
        try {
            objectFactory.createObject(MockSubDataBean.class, "identifier", url, new Object[1], new Class[0],
                ObjectFactory.BOTH);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * Test the method createObject with Class and URL, fail for the initStrategy is invalid,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testcreateObjectWithClass_failForInvalidinitStrategy() throws Exception {
        try {
            objectFactory.createObject(MockSubDataBean.class, "identifier", url, new Object[1], new Class[0],
                "invalid");
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * Test the method createObject with Class and URL, fail for the key and identifier is invalid,
     * InvalidClassSpecificationException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testcreateObjectWithClassAndURL_failForInvalidkey() throws Exception {
        try {
            objectFactory.createObject(String.class, "identifier", url, new Object[0], new Class[0],
                ObjectFactory.SPECIFICATION_ONLY);
            fail("InvalidClassSpecificationException should be thrown.");
        } catch (InvalidClassSpecificationException e) {
            //pass
        }
    }

    /**
     * Test the method createObject with ClassLoader, fail for key is null,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testcreateObjectWithClassLoader_failForForKeyisNull() throws Exception {
        try {
            objectFactory.createObject((String) null, "identifier", loader, new Object[0], new Class[0],
                ObjectFactory.BOTH);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * Test the method createObject with ClassLoader, fail for key is Empty,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testcreateObjectWithClassLoader_failForKeyisEmpty() throws Exception {
        try {
            objectFactory.createObject(" ", "identifier", loader, new Object[0], new Class[0],
                ObjectFactory.BOTH);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * Test the method createObject with ClassLoader, fail for key is Empty,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testcreateObjectWithClassLoader_failForlengthNotEqual() throws Exception {
        try {
            objectFactory.createObject("key", "identifier", loader, new Object[1], new Class[0],
                ObjectFactory.BOTH);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * Test the method createObject with ClassLoader, fail for the key and identifier is invalid,
     * InvalidClassSpecificationException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testcreateObjectWithClassLoader_failForInvalidkey() throws Exception {
        try {
            objectFactory.createObject("key", "identifier", loader, new Object[0], new Class[0],
                ObjectFactory.SPECIFICATION_ONLY);
            fail("InvalidClassSpecificationException should be thrown.");
        } catch (InvalidClassSpecificationException e) {
            //pass
        }
    }

    /**
     * Test the method createObject with ClassLoader, fail for the initStrategy is invalid,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testcreateObjectWithClassLoader_failForInvalidinitStrategy() throws Exception {
        try {
            objectFactory.createObject("key", "identifier", loader, new Object[1], new Class[0], "invalid");
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * Test the method createObject with Class and ClassLoader, fail for class is null,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testcreateObjectWithClassAndClassLoader_failForForClassisNull() throws Exception {
        try {
            objectFactory.createObject((Class<?>) null, "identifier", loader, new Object[0], new Class[0],
                ObjectFactory.BOTH);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * Test the method createObject with Class and ClassLoader, fail for key is Empty,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testcreateObjectWithClassAndClassLoader_failForlengthNotEqual() throws Exception {
        try {
            objectFactory.createObject(MockSubDataBean.class, "identifier", loader, new Object[1],
                new Class[0], ObjectFactory.BOTH);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * Test the method createObject with Class and ClassLoader, fail for the initStrategy is invalid,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testcreateObjectWithClassAndLoader_failForInvalidinitStrategy() throws Exception {
        try {
            objectFactory.createObject(MockSubDataBean.class, "identifier", loader, new Object[1],
                new Class[0], "invalid");
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * Test the method createObject with Class and ClassLoader, fail for the key and identifier is invalid,
     * InvalidClassSpecificationException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testcreateObjectWithClassAndClassLoader_failForInvalidkey() throws Exception {
        try {
            objectFactory.createObject(String.class, "identifier", loader, new Object[0], new Class[0],
                ObjectFactory.SPECIFICATION_ONLY);
            fail("InvalidClassSpecificationException should be thrown.");
        } catch (InvalidClassSpecificationException e) {
            //pass
        }
    }

    /**
     * Test the method setInitStrategy, fail for the param is invalid.
     *
     */
    public void testsetInitStrategy_failForInvalidParam() {
        try {
            objectFactory.setInitStrategy("invalidString");
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * Test create fail.
     * InvalidClassSpecificationException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testCreatefail1() throws Exception {
        try {
            CONFIG.add("failure/Invalid16.xml");
            specificationFactory = new ConfigManagerSpecificationFactory("InvalidNamespace16");
            objectFactory = new ObjectFactory(specificationFactory);
            objectFactory.createObject("MockSubDataBean", "default1");
            fail("InvalidClassSpecificationException should be thrown.");
        } catch (InvalidClassSpecificationException e) {
            //pass
        }
    }

    /**
     * Test create fail.
     * InvalidClassSpecificationException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testCreatefail2() throws Exception {
        try {
            CONFIG.add("failure/Invalid17.xml");
            specificationFactory = new ConfigManagerSpecificationFactory("InvalidNamespace17");
            objectFactory = new ObjectFactory(specificationFactory);
            objectFactory.createObject("intArray");
            fail("InvalidClassSpecificationException should be thrown.");
        } catch (InvalidClassSpecificationException e) {
            //pass
        }
    }
}
