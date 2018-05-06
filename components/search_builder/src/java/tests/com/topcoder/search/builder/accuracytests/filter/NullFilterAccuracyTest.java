/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.accuracytests.filter;

import com.topcoder.search.builder.filter.NullFilter;
import com.topcoder.search.builder.ValidationResult;
import com.topcoder.util.datavalidator.ObjectValidator;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Assert;

import java.util.Map;
import java.util.HashMap;

/**
 * <p>An accuracy test for {@link NullFilter} class. Tests the methods for proper
 * handling of valid input data and producing accurate results. Passes the valid arguments to the methods and verifies
 * that either the state of the tested instance have been changed appropriately or a correct result is produced by the
 * method.</p>
 *
 * @author isv
 * @version 1.0
 * @since 1.3
 */
public class NullFilterAccuracyTest extends TestCase {

    /**
     * <p>The instance of {@link NullFilter} which is tested. This instance is initialized in {@link #setUp()}
     * method and released in {@link #tearDown()} method.</p>
     */
    private NullFilter testedInstance = null;

    /**
     * <p>Gets the test suite for {@link NullFilter} class.</p>
     *
     * @return a <code>TestSuite</code> providing the tests for {@link NullFilter} class.
     */
    public static Test suite() {
        return new TestSuite(NullFilterAccuracyTest.class);
    }

    /**
     * <p>Sets up the fixture. This method is called before a test is executed.</p>
     *
     * @throws Exception if any error occurs.
     */
    protected void setUp() throws Exception {
        super.setUp();
        this.testedInstance = new NullFilter("NullFilterName");
    }

    /**
     * <p>Tears down the fixture. This method is called after a test is executed.</p>
     *
     * @throws Exception if any error occurs.
     */
    protected void tearDown() throws Exception {
        this.testedInstance = null;
        super.tearDown();
    }

    /**
     * <p>Accuracy test. Tests the {@link NullFilter#getValue()} method for proper behavior.</p>
     *
     * <p>Verifies that the method returns NULL.</p>
     */
    public void testGetValue() {
        Assert.assertNull("Does not return NULL value", this.testedInstance.getValue());
    }

    /**
     * <p>Accuracy test. Tests the {@link NullFilter#getName()} method for proper behavior.</p>
     *
     * <p>Verifies that the method returns name passed at construction time.</p>
     */
    public void testGetName() {
        Assert.assertEquals("Does not return NULL value", "NullFilterName", this.testedInstance.getName());
    }

    /**
     * <p>Accuracy test. Tests the {@link NullFilter#clone()} method for proper behavior.</p>
     *
     * <p>Verifies that the method creates a clone copy correctly.</p>
     */
    public void testClone() {
        NullFilter clone = (NullFilter) this.testedInstance.clone();
        Assert.assertNotSame("The clone copy is not created correctly", this.testedInstance, clone);
        Assert.assertEquals("The clone copy is not created correctly", this.testedInstance.getName(), clone.getName());
        Assert.assertEquals("The clone copy is not created correctly", this.testedInstance.getValue(), clone.getValue());
        Assert.assertEquals("The clone copy is not created correctly",
                            this.testedInstance.getFilterType(), clone.getFilterType());
    }

    /**
     * <p>Accuracy test. Tests the {@link NullFilter#isValid(Map, Map)} method for proper behavior.</p>
     *
     * <p>Verifies that the method returns a valid result if the filter is valid..</p>
     */
    public void testIsValid_Valid() {
        Map aliases = new HashMap();
        Map validators = new HashMap();
        validators.put(this.testedInstance.getName(), new ObjectValidator() {
            public boolean valid(Object obj) { return true; }
            public String getMessage(Object obj) { return null; }
        });

        ValidationResult result = this.testedInstance.isValid(validators, aliases);
        Assert.assertTrue("The filter is not valid", result.isValid());
    }

    /**
     * <p>Accuracy test. Tests the {@link NullFilter#isValid(Map, Map)} method for proper behavior.</p>
     *
     * <p>Verifies that the method returns an invalid result if the filter is invalid..</p>
     */
    public void testIsValid_Invalid() {
        Map aliases = new HashMap();
        Map validators = new HashMap();
        validators.put(this.testedInstance.getName(), new ObjectValidator() {
            public boolean valid(Object obj) { return false; }
            public String getMessage(Object obj) { return "Wrong Object"; }
        });

        ValidationResult result = this.testedInstance.isValid(validators, aliases);
        Assert.assertFalse("The filter is valid", result.isValid());
        Assert.assertEquals("The filter is valid",
                            this.testedInstance.getName(), ((NullFilter) result.getFailedFilter()).getName());
    }
}
