/*
 * Copyright (C) 2006 - 2010 TopCoder Inc., All Rights Reserved.
 */



package com.cronos.onlinereview.login;

import junit.framework.TestCase;

import java.util.AbstractCollection;

/**
 * Unit tests for <code>Util</code> class.
 *
 * @author maone, TCSDEVELOPER
 * @version 1.1
 * @since 1.0
 */
public class UtilTest extends TestCase {

    /**
     * Set up. Load the configurations.
     *
     * @throws Exception
     *             to JUnit.
     */
    protected void setUp() throws Exception {
        super.setUp();

        TestUtil.loadAllConfigurations();
    }

    /**
     * Tear down. Clear the configurations.
     *
     * @throws Exception
     *             to JUnit.
     */
    protected void tearDown() throws Exception {
        TestUtil.clearAllConfigurations();

        super.tearDown();
    }

    /**
     * Test <code>getOptionalPropertyString</code> for existing property.
     * <p>
     * It will return the corresponding value.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testGetOptionalPropertyString_Valid1() throws Exception {
        assertEquals("Failed to getOptionalPropertyString.", "com.topcoder.util.log.basic.BasicLog",
                     Util.getOptionalPropertyString("com.topcoder.util.log", "logClass"));
    }

    /**
     * Test <code>getOptionalPropertyString</code> for non-existing property.
     * <p>
     * It will return null.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testGetOptionalPropertyString_Valid2() throws Exception {
        assertNull("Failed to getOptionalPropertyString.",
                   Util.getOptionalPropertyString("com.cronos.onlinereview.login.LoginActions", "XXX"));
    }

    /**
     * Test <code>getOptionalPropertyString</code> for invalid namespace.
     * <p>
     * It will throw ConfigurationException.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testGetOptionalPropertyString_InvalidNamespace() throws Exception {
        try {
            Util.getOptionalPropertyString("this.namespace.does.not.exist", "property");
            fail("Should throw ConfigurationException for invalid namespace.");
        } catch (ConfigurationException e) {

            // pass
        }
    }

    /**
     * Test <code>getRequiredPropertyString</code> for existing property.
     * <p>
     * It will return the corresponding value.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testGetRequiredPropertyString_Valid1() throws Exception {
        assertEquals("Failed to getOptionalPropertyString.", "com.topcoder.util.log.basic.BasicLog",
                     Util.getRequiredPropertyString("com.topcoder.util.log", "logClass"));
    }

    /**
     * Test <code>getRequiredPropertyString</code> for non-existing property.
     * <p>
     * It will throw ConfigurationException.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testGetRequiredPropertyString_Valid2() throws Exception {
        try {
            Util.getRequiredPropertyString("com.cronos.onlinereview.login.LoginActions", "XXX");
            fail("Should throw ConfigurationException for invalid property.");
        } catch (ConfigurationException e) {

            // pass
        }
    }

    /**
     * Test <code>getOptionalPropertyString</code> for invalid namespace.
     * <p>
     * It will throw ConfigurationException.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testGetRequiredPropertyString_InvalidNamespace() throws Exception {
        try {
            Util.getRequiredPropertyString("this.namespace.does.not.exist", "property");
            fail("Should throw ConfigurationException for invalid namespace.");
        } catch (ConfigurationException e) {

            // pass
        }
    }

    /**
     * Test <code>validateNotNull</code> with non-null value.
     * <p>
     * The given value will be returned.
     * </p>
     */
    public void testValidateNotNull_NotNull() {
        Object ret = Util.validateNotNull(new Integer(1234), "Integer");

        assertEquals("Should return given value.", new Integer(1234), ret);
    }

    /**
     * Test <code>validateNotNull</code> with null value.
     * <p>
     * It should throw IllegalArgumentException.
     * </p>
     */
    public void testValidateNotNull_Null() {
        try {
            Util.validateNotNull(null, "name");
            fail("Should throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {

            // pass
        }
    }

    /**
     * Test <code>validateNotNullOrEmpty</code> with non-null or empty value.
     * <p>
     * The given value will be returned.
     * </p>
     */
    public void testValidateNotNullOrEmpty_NotNull() {
        Object ret = Util.validateNotNullOrEmpty("foo", "bar");

        assertEquals("Should return given value.", "foo", ret);
    }

    /**
     * Test <code>validateNotNullOrEmpty</code> with null value.
     * <p>
     * It should throw IllegalArgumentException.
     * </p>
     */
    public void testValidateNotNullOrEmpty_Null() {
        try {
            Util.validateNotNullOrEmpty(null, "name");
            fail("Should throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {

            // pass
        }
    }

    /**
     * Test <code>validateNotNullOrEmpty</code> with empty value.
     * <p>
     * It should throw IllegalArgumentException.
     * </p>
     */
    public void testValidateNotNullOrEmpty_Empty() {
        try {
            Util.validateNotNullOrEmpty("  ", "name");
            fail("Should throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {

            // pass
        }
    }

    /**
     * Test <code>creatObject</code>.
     * <p>
     * It should create the object correctly.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void testCreatObject() throws Exception {
        Integer i = (Integer) Util.creatObject(Integer.class.getName(), new Class[] { String.class },
                        new Object[] { "200" }, Integer.class);

        assertNotNull("Should NOT null", i);
    }

    /**
     * Test <code>creatObject</code>.
     * <p>
     * It should throw ConfigurationException if no such class.
     * </p>
     */
    public void testCreatObjectFailure1() {
        try {
            Util.creatObject("java.lang.noSuch", new Class[] { String.class }, new Object[] { "200" }, Integer.class);
            fail("Should throw ConfigurationException.");
        } catch (ConfigurationException e) {

            // pass
        }
    }

    /**
     * Test <code>creatObject</code>.
     * <p>
     * It should throw ConfigurationException if the class does NOT have such a constructor.
     * </p>
     */
    public void testCreatObjectFailure2() {
        try {
            Util.creatObject("java.lang.Integer", new Class[] { Double.class }, new Object[] { 200.0 }, Integer.class);
            fail("Should throw ConfigurationException.");
        } catch (ConfigurationException e) {

            // pass
        }
    }

    /**
     * Test <code>creatObject</code>.
     * <p>
     * It should throw ConfigurationException if the argument for the constructor is wrong.
     * </p>
     */
    public void testCreatObjectFailure3() {
        try {
            Util.creatObject("java.lang.Integer", new Class[] { String.class }, new Object[] { 200.0 }, Integer.class);
            fail("Should throw ConfigurationException.");
        } catch (ConfigurationException e) {

            // pass
        }
    }

    /**
     * Test <code>creatObject</code>.
     * <p>
     * It should throw ConfigurationException if the class is wrong type.
     * </p>
     */
    public void testCreatObjectFailure4() {
        try {
            Util.creatObject("java.lang.Integer", new Class[] { String.class }, new Object[] { "200" }, String.class);
            fail("Should throw ConfigurationException.");
        } catch (ConfigurationException e) {

            // pass
        }
    }

    /**
     * Test <code>creatObject</code>.
     * <p>
     * It should throw ConfigurationException if the class has no public constructor.
     * </p>
     */
    public void testCreatObjectFailure5() {
        try {
            Util.creatObject(AbstractCollection.class.getName(), new Class[] {}, new Object[] {},
                             AbstractCollection.class);
            fail("Should throw ConfigurationException.");
        } catch (ConfigurationException e) {

            // pass
        }
    }

    /**
     * Test <code>creatObject</code>.
     * <p>
     * It should throw ConfigurationException if the class is an abstract class.
     * </p>
     */
    public void testCreatObjectFailure6() {
        try {
            Util.creatObject(AbstractMock.class.getName(), new Class[] {}, new Object[] {}, AbstractMock.class);
            fail("Should throw ConfigurationException.");
        } catch (ConfigurationException e) {

            // pass
        }
    }
}
