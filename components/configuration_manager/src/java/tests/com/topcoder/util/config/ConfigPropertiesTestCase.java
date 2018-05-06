/*
 * Copyright (C) 2003, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.config;

import junit.framework.TestCase;

import org.junit.Test;

/**
 * <p>
 * Tests the common behavior of ConfigProperties.
 * </p>
 *
 * <p>
 * <em>Changes in 2.2:</em>
 * <ol>
 * <li>Added test cases for methods: getListDelimiter(), setListDelimiter(char), isRefreshable() and
 * setRefreshable(Boolean).</li>
 * </ol>
 * </p>
 *
 * @author WishingBone, sparemax
 * @version 2.2
 */
public class ConfigPropertiesTestCase extends TestCase {
    /**
     * The ConfigProperties instance to test.
     */
    private ConfigProperties instance = null;

    /**
     * Set up testing environment.
     */
    protected void setUp() {
        instance = new MockConfigProperties();
    }

    /**
     * Tear down testing environment.
     */
    protected void tearDown() {
        instance = null;
    }

    /**
     * <p>
     * Accuracy test for the constructor <code>ConfigProperties()</code>.<br>
     * Instance should be correctly created.
     * </p>
     *
     * @since 2.2
     */
    @Test
    public void testCtor() {
        instance = new MockConfigProperties();

        assertNull("'root' should be correct.", instance.getRoot());
        assertEquals("'listDelimiter' should be correct.", ';', instance.getListDelimiter());
        assertNull("'refreshable' should be correct.", instance.isRefreshable());
    }

    /**
     * Test set and get root.
     */
    public void testSetGetRoot() {
        Property root = new Property();
        instance.setRoot(root);
        assertTrue("'setRoot' should be correct.", instance.getRoot() == root);
        // root is null
        try {
            instance.setRoot(null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
    }

    /**
     * <p>
     * Accuracy test for the method <code>getListDelimiter()</code>.<br>
     * The value should be properly retrieved.
     * </p>
     *
     * @since 2.2
     */
    public void test_getListDelimiter() {
        char value = '_';
        instance.setListDelimiter(value);

        assertEquals("'getListDelimiter' should be correct.", value, instance.getListDelimiter());
    }

    /**
     * <p>
     * Accuracy test for the method <code>setListDelimiter(char listDelimiter)</code>.<br>
     * The value should be properly set.
     * </p>
     *
     * @since 2.2
     */
    public void test_setListDelimiter() {
        char value = '_';
        instance.setListDelimiter(value);

        assertEquals("'setListDelimiter' should be correct.", value, TestsHelper.getField(instance, "listDelimiter"));
    }

    /**
     * <p>
     * Accuracy test for the method <code>isRefreshable()</code>.<br>
     * The value should be properly retrieved.
     * </p>
     *
     * @since 2.2
     */
    public void test_isRefreshable() {
        Boolean value = true;
        instance.setRefreshable(value);

        assertEquals("'isRefreshable' should be correct.", value, instance.isRefreshable());
    }

    /**
     * <p>
     * Accuracy test for the method <code>setRefreshable(Boolean refreshable)</code>.<br>
     * The value should be properly set.
     * </p>
     *
     * @since 2.2
     */
    public void test_setRefreshable() {
        Boolean value = true;
        instance.setRefreshable(value);

        assertEquals("'setRefreshable' should be correct.", value, TestsHelper.getField(instance, "refreshable"));
    }
}
