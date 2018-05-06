/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.external.failuretests.impl;

import com.cronos.onlinereview.external.impl.ExternalProjectImpl;

import junit.framework.TestCase;

/**
 * <p>
 * This test case aggregates all test cases for <code>ExternalProjectImpl</code>.
 * </p>
 *
 * @author idx, liulike
 * @version 1.1
 */
public class ExternalProjectImplFailureTest extends TestCase {

    /** An <code>ExternalProjectImpl</code> instance used for tests. */
    private ExternalProjectImpl impl = null;

    /**
     * Set up.
     *
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        impl = new ExternalProjectImpl(10, 1, "1.0");
    }

    /**
     * Tear down.
     *
     * @throws Exception to JUnit
     */
    protected void tearDown() throws Exception {
        impl = null;
    }

    /**
     * Test <code>ExternalProjectImpl(long, long, String)</code> with null
     * argument. <code>version</code> is null in this test.
     * <code>IllegalArgumentException</code> is expected.
     */
    public void testExternalProjectImpl_NullArg() {
        try {
            new ExternalProjectImpl(10, 1, null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test <code>ExternalProjectImpl(long, long, String)</code> with negative
     * argument. <code>id</code> is negative in this test.
     * <code>IllegalArgumentException</code> is expected.
     */
    public void testExternalProjectImpl_NegativeArg_1() {
        try {
            new ExternalProjectImpl(-10, 1, "1.0");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test <code>ExternalProjectImpl(long, long, String)</code> with negative
     * argument. <code>versionId</code> is negative in this test.
     * <code>IllegalArgumentException</code> is expected.
     */
    public void testExternalProjectImpl_NegativeArg_2() {
        try {
            new ExternalProjectImpl(10, -1, "1.0");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>setCatalogId(long)</code> with negative
     * <code>catalogId</code>. <code>IllegalArgumentException</code> is
     * expected.
     */
    public void testSetCatalogId_NegativeId() {
        try {
            impl.setCatalogId(-1);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>setCatalogId(long)</code> with <code>catalogId</code>
     * already set. <code>IllegalArgumentException</code> is expected.
     */
    public void testSetCatalogId_IdAlreadySet() {
        impl.setCatalogId(1);
        try {
            impl.setCatalogId(2);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>setComments(String)</code> with null
     * <code>comments</code>. <code>IllegalArgumentException</code> is
     * expected.
     */
    public void testSetComments_NullComments() {
        try {
            impl.setComments(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>setComments(String)</code> with <code>comments</code>
     * already set. <code>IllegalArgumentException</code> is expected.
     */
    public void testSetComments_CommentsAlreadySet() {
        impl.setComments("Fine.");
        try {
            impl.setComments("Ok.");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>setComponentId(long)</code> with negative
     * <code>componentId</code>. <code>IllegalArgumentException</code> is
     * expected.
     */
    public void testSetComponentId_NegativeId() {
        try {
            impl.setComponentId(-1);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>setComponentId(long)</code> with
     * <code>componentId</code> already set.
     * <code>IllegalArgumentException</code> is expected.
     */
    public void testSetComponentId_IdAlreadySet() {
        impl.setComponentId(1);
        try {
            impl.setComponentId(2);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>setDescription(String)</code> with null
     * <code>description</code>. <code>IllegalArgumentException</code> is
     * expected.
     */
    public void testSetDescription_NullDescription() {
        try {
            impl.setDescription(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>setDescription(String)</code> with
     * <code>description</code> already set.
     * <code>IllegalArgumentException</code> is expected.
     */
    public void testSetDescription_DescriptionAlreadySet() {
        impl.setDescription("foo");
        try {
            impl.setDescription("bar");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>setForumId(long)</code> with negative
     * <code>forumId</code>. <code>IllegalArgumentException</code> is
     * expected.
     */
    public void testSetForumId_NegativeId() {
        try {
            impl.setForumId(-1);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>setForumId(long)</code> with <code>forumId</code>
     * already set. <code>IllegalArgumentException</code> is expected.
     */
    public void testSetForumId_IdAlreadySet() {
        impl.setForumId(1);
        try {
            impl.setForumId(2);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>setName(String)</code> with null <code>name</code>.
     * <code>IllegalArgumentException</code> is expected.
     */
    public void testSetName_NullName() {
        try {
            impl.setName(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>setName(String)</code> with <code>name</code>
     * already set. <code>IllegalArgumentException</code> is expected.
     */
    public void testSetName_NameAlreadySet() {
        impl.setName("foo");
        try {
            impl.setName("bar");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>setFunctionalDescription(String)</code> with null.
     * <code>IllegalArgumentException</code> is expected.
     */
    public void testSetFunctionalDescription_Null() {
        try {
            impl.setFunctionalDescription(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>setFunctionalDescription(String)</code> when
     * description already set. <code>IllegalArgumentException</code> is
     * expected.
     */
    public void testSetFunctionalDescription_AlreadySet() {
        impl.setFunctionalDescription("foo");
        try {
            impl.setFunctionalDescription("bar");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>setShortDescription(String)</code> with null.
     * <code>IllegalArgumentException</code> is expected.
     */
    public void testSetShortDescription_Null() {
        try {
            impl.setShortDescription(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>setShortDescription(String)</code> when description
     * already set. <code>IllegalArgumentException</code> is expected.
     */
    public void testSetShortDescription_AlreadySet() {
        impl.setShortDescription("foo");
        try {
            impl.setShortDescription("bar");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>addTechnology(String)</code> with null.
     * <code>IllegalArgumentException</code> is expected.
     */
    public void testAddTechnology_Null() {
        try {
            impl.addTechnology(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

}
