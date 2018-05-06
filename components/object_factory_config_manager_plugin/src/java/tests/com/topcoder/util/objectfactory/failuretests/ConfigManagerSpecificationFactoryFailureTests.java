/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.objectfactory.failuretests;

import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.objectfactory.UnknownReferenceException;
import com.topcoder.util.objectfactory.impl.ConfigManagerSpecificationFactory;
import com.topcoder.util.objectfactory.impl.IllegalReferenceException;
import com.topcoder.util.objectfactory.impl.SpecificationConfigurationException;

import junit.framework.TestCase;

import java.util.Iterator;


/**
 * <p>
 * The Unit test of ConfigManagerSpecificationFactory
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class ConfigManagerSpecificationFactoryFailureTests extends TestCase {
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
     * The invalid config file.
     */
    private static final String XML1 = "failure/Invalid1.xml";

    /**
     * The invalid config file.
     */
    private static final String INVALIDNAMESPACE1 = "InvalidNamespace1";

    /**
     * The invalid config file.
     */
    private static final String XML2 = "failure/Invalid2.xml";

    /**
     * The invalid config file.
     */
    private static final String INVALIDNAMESPACE2 = "InvalidNamespace2";

    /**
     * The invalid config file.
     */
    private static final String XML3 = "failure/Invalid3.xml";

    /**
     * The invalid config file.
     */
    private static final String INVALIDNAMESPACE3 = "InvalidNamespace3";

    /**
     * The invalid config file.
     */
    private static final String XML4 = "failure/Invalid4.xml";

    /**
     * The invalid config file.
     */
    private static final String INVALIDNAMESPACE4 = "InvalidNamespace4";

    /**
     * The invalid config file.
     */
    private static final String XML5 = "failure/Invalid5.xml";

    /**
     * The invalid config file.
     */
    private static final String INVALIDNAMESPACE5 = "InvalidNamespace5";

    /**
     * The invalid config file.
     */
    private static final String XML6 = "failure/Invalid6.xml";

    /**
     * The invalid config file.
     */
    private static final String INVALIDNAMESPACE6 = "InvalidNamespace6";

    /**
     * The invalid config file.
     */
    private static final String XML7 = "failure/Invalid7.xml";

    /**
     * The invalid config file.
     */
    private static final String INVALIDNAMESPACE7 = "InvalidNamespace7";

    /**
     * The invalid config file.
     */
    private static final String XML8 = "failure/Invalid8.xml";

    /**
     * The invalid config file.
     */
    private static final String INVALIDNAMESPACE8 = "InvalidNamespace8";

    /**
     * The invalid config file.
     */
    private static final String XML9 = "failure/Invalid9.xml";

    /**
     * The invalid config file.
     */
    private static final String INVALIDNAMESPACE9 = "InvalidNamespace9";

    /**
     * The invalid config file.
     */
    private static final String XML10 = "failure/Invalid10.xml";

    /**
     * The invalid config file.
     */
    private static final String INVALIDNAMESPACE10 = "InvalidNamespace10";

    /**
     * The invalid config file.
     */
    private static final String XML11 = "failure/Invalid11.xml";

    /**
     * The invalid config file.
     */
    private static final String INVALIDNAMESPACE11 = "InvalidNamespace11";

    /**
     * The invalid config file.
     */
    private static final String XML12 = "failure/Invalid12.xml";

    /**
     * The invalid config file.
     */
    private static final String INVALIDNAMESPACE12 = "InvalidNamespace12";

    /**
     * The invalid config file.
     */
    private static final String XML13 = "failure/Invalid13.xml";

    /**
     * The invalid config file.
     */
    private static final String INVALIDNAMESPACE13 = "InvalidNamespace13";

    /**
     * The invalid config file.
     */
    private static final String XML14 = "failure/Invalid14.xml";

    /**
     * The invalid config file.
     */
    private static final String INVALIDNAMESPACE14 = "InvalidNamespace14";

    /**
     * The invalid config file.
     */
    private static final String XML15 = "failure/Invalid15.xml";

    /**
     * The invalid config file.
     */
    private static final String INVALIDNAMESPACE15 = "InvalidNamespace15";

    /**
     * The invalid config file.
     */
    private static final String XML18 = "failure/Invalid18.xml";

    /**
     * The invalid config file.
     */
    private static final String INVALIDNAMESPACE18 = "InvalidNamespace18";

    /**
     * The ConfigManagerSpecificationFactory instance used to test.
     */
    private ConfigManagerSpecificationFactory factory = null;

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
        factory = new ConfigManagerSpecificationFactory(NAMESPACE);
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
     * Test the cosntructor of ConfigManagerSpecificationFactory, fail for
     * the namespace is null, IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testConstrucotor_failForNameSpaceNull() throws Exception {
        try {
            new ConfigManagerSpecificationFactory(null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * Test the cosntructor of ConfigManagerSpecificationFactory, fail for
     * the namespace is empty, IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testConstrucotor_failForNameSpaceempty() throws Exception {
        try {
            new ConfigManagerSpecificationFactory(" ");
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * Test the cosntructor of ConfigManagerSpecificationFactory, fail for
     * the namespace does not exist, SpecificationConfigurationException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testConstrucotor_failForNameSpaceUnexist() throws Exception {
        try {
            new ConfigManagerSpecificationFactory("no such namespace");
            fail("SpecificationConfigurationException should be thrown.");
        } catch (SpecificationConfigurationException e) {
            //pass
        }
    }

    /**
     * Test the cosntructor of ConfigManagerSpecificationFactory, fail for
     * the namespace invalid, IllegalReferenceException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testConstrucotor_failForNameSpaceInvalid1() throws Exception {
        try {
            CONFIG.add(XML1);
            new ConfigManagerSpecificationFactory(INVALIDNAMESPACE1);
            fail("IllegalReferenceException should be thrown.");
        } catch (IllegalReferenceException e) {
            //pass
        }
    }

    /**
     * Test the cosntructor of ConfigManagerSpecificationFactory, fail for
     * the namespace invalid, IllegalReferenceException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testConstrucotor_failForNameSpaceInvalid2() throws Exception {
        try {
            CONFIG.add(XML2);
            new ConfigManagerSpecificationFactory(INVALIDNAMESPACE2);
            fail("IllegalReferenceException should be thrown.");
        } catch (IllegalReferenceException e) {
            //pass
        }
    }

    /**
     * Test the cosntructor of ConfigManagerSpecificationFactory, fail for
     * the namespace invalid, IllegalReferenceException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testConstrucotor_failForNameSpaceInvalid3() throws Exception {
        try {
            CONFIG.add(XML3);
            new ConfigManagerSpecificationFactory(INVALIDNAMESPACE3);
            fail("IllegalReferenceException should be thrown.");
        } catch (IllegalReferenceException e) {
            //pass
        }
    }

    /**
     * Test the cosntructor of ConfigManagerSpecificationFactory, fail for
     * the namespace invalid, IllegalReferenceException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testConstrucotor_failForNameSpaceInvalid4() throws Exception {
        try {
            CONFIG.add(XML4);
            new ConfigManagerSpecificationFactory(INVALIDNAMESPACE4);
            fail("IllegalReferenceException should be thrown.");
        } catch (IllegalReferenceException e) {
            //pass
        }
    }

    /**
     * Test the cosntructor of ConfigManagerSpecificationFactory, fail for
     * the namespace invalid, IllegalReferenceException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testConstrucotor_failForNameSpaceInvalid5() throws Exception {
        try {
            CONFIG.add(XML5);
            new ConfigManagerSpecificationFactory(INVALIDNAMESPACE5);
            fail("IllegalReferenceException should be thrown.");
        } catch (IllegalReferenceException e) {
            //pass
        }
    }

    /**
     * Test the cosntructor of ConfigManagerSpecificationFactory, fail for
     * the namespace invalid, IllegalReferenceException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testConstrucotor_failForNameSpaceInvalid6() throws Exception {
        try {
            CONFIG.add(XML6);
            new ConfigManagerSpecificationFactory(INVALIDNAMESPACE6);
            fail("IllegalReferenceException should be thrown.");
        } catch (IllegalReferenceException e) {
            //pass
        }
    }

    /**
     * Test the cosntructor of ConfigManagerSpecificationFactory, fail for
     * the namespace invalid, IllegalReferenceException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testConstrucotor_failForNameSpaceInvalid7() throws Exception {
        try {
            CONFIG.add(XML7);
            new ConfigManagerSpecificationFactory(INVALIDNAMESPACE7);
            fail("IllegalReferenceException should be thrown.");
        } catch (IllegalReferenceException e) {
            //pass
        }
    }

    /**
     * Test the cosntructor of ConfigManagerSpecificationFactory, fail for
     * the namespace invalid, IllegalReferenceException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testConstrucotor_failForNameSpaceInvalid8() throws Exception {
        try {
            CONFIG.add(XML8);
            new ConfigManagerSpecificationFactory(INVALIDNAMESPACE8);
            fail("IllegalReferenceException should be thrown.");
        } catch (IllegalReferenceException e) {
            //pass
        }
    }

    /**
     * Test the cosntructor of ConfigManagerSpecificationFactory, fail for
     * the namespace invalid, IllegalReferenceException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testConstrucotor_failForNameSpaceInvalid9() throws Exception {
        try {
            CONFIG.add(XML9);
            new ConfigManagerSpecificationFactory(INVALIDNAMESPACE9);
            fail("IllegalReferenceException should be thrown.");
        } catch (IllegalReferenceException e) {
            //pass
        }
    }

    /**
     * Test the cosntructor of ConfigManagerSpecificationFactory, fail for
     * the namespace invalid, IllegalReferenceException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testConstrucotor_failForNameSpaceInvalid10() throws Exception {
        try {
            CONFIG.add(XML10);
            new ConfigManagerSpecificationFactory(INVALIDNAMESPACE10);
            fail("IllegalReferenceException should be thrown.");
        } catch (IllegalReferenceException e) {
            //pass
        }
    }

    /**
     * Test the cosntructor of ConfigManagerSpecificationFactory, fail for
     * the namespace invalid, IllegalReferenceException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testConstrucotor_failForNameSpaceInvalid11() throws Exception {
        try {
            CONFIG.add(XML11);
            new ConfigManagerSpecificationFactory(INVALIDNAMESPACE11);
            fail("IllegalReferenceException should be thrown.");
        } catch (IllegalReferenceException e) {
            //pass
        }
    }

    /**
     * Test the cosntructor of ConfigManagerSpecificationFactory, fail for
     * the namespace invalid, IllegalReferenceException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testConstrucotor_failForNameSpaceInvalid12() throws Exception {
        try {
            CONFIG.add(XML12);
            new ConfigManagerSpecificationFactory(INVALIDNAMESPACE12);
            fail("IllegalReferenceException should be thrown.");
        } catch (IllegalReferenceException e) {
            //pass
        }
    }

    /**
     * Test the cosntructor of ConfigManagerSpecificationFactory, fail for
     * the namespace invalid, IllegalReferenceException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testConstrucotor_failForNameSpaceInvalid13() throws Exception {
        try {
            CONFIG.add(XML13);
            new ConfigManagerSpecificationFactory(INVALIDNAMESPACE13);
            fail("IllegalReferenceException should be thrown.");
        } catch (IllegalReferenceException e) {
            //pass
        }
    }

    /**
     * Test the cosntructor of ConfigManagerSpecificationFactory, fail for
     * the namespace invalid(Refencen Unexist), IllegalReferenceException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testConstrucotor_failForNameSpaceInvalid14_RefencenUnexist() throws Exception {
        try {
            CONFIG.add(XML14);
            new ConfigManagerSpecificationFactory(INVALIDNAMESPACE14);
            fail("IllegalReferenceException should be thrown.");
        } catch (IllegalReferenceException e) {
            //pass
        }
    }

    /**
     * Test the cosntructor of ConfigManagerSpecificationFactory, fail for
     * the namespace invalid(circle exists between Refencen), IllegalReferenceException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testConstrucotor_failForNameSpaceInvalid15_CircleExists() throws Exception {
        try {
            CONFIG.add(XML15);
            new ConfigManagerSpecificationFactory(INVALIDNAMESPACE15);
            fail("IllegalReferenceException should be thrown.");
        } catch (IllegalReferenceException e) {
            //pass
        }
    }

    /**
     * Test the cosntructor of ConfigManagerSpecificationFactory, fail for
     * the namespace invalid(circle exists between Refencen), IllegalReferenceException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testConstrucotor_failForNameSpaceInvalid18_CircleExists() throws Exception {
        try {
            CONFIG.add(XML18);
            new ConfigManagerSpecificationFactory(INVALIDNAMESPACE18);
            fail("IllegalReferenceException should be thrown.");
        } catch (IllegalReferenceException e) {
            //pass
        }
    }

    /**
     * Test the method ObjectSpecification, fail for key is null,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testObjectSpecification_failForKeynull() throws Exception {
        try {
            factory.getObjectSpecification(null, "default");
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * Test the method ObjectSpecification, fail for key is empty,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testObjectSpecification_failForKeyEmpty() throws Exception {
        try {
            factory.getObjectSpecification(" ", "default");
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * Test the method ObjectSpecification, fail for key is Unknown,
     * UnknownReferenceException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testObjectSpecification_failForUnknownKey() throws Exception {
        try {
            factory.getObjectSpecification("invalid", "default");
            fail("UnknownReferenceException should be thrown.");
        } catch (UnknownReferenceException e) {
            //pass
        }
    }
}
