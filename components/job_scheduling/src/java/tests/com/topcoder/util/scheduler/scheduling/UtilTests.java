/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Test;

/**
 * <p>
 * Unit test cases for Util.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 3.0
 */
public class UtilTests extends TestCase {
    /**
     * <p>
     * Returns all tests.
     * </p>
     *
     * @return all tests
     */
    public static Test suite() {
        return new TestSuite(UtilTests.class);
    }

    /**
     * <p>
     * Tests Util#checkStringNotNullAndEmpty(String,String) for accuracy.
     * </p>
     *
     * <p>
     * It verifies Util#checkStringNotNullAndEmpty(String,String) is correct.
     * </p>
     */
    public void testCheckStringNotNullAndEmpty() {
        Util.checkStringNotNullAndEmpty("test", "test");
    }

    /**
     * <p>
     * Tests Util#checkStringNotNullAndEmpty(String,String) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when str is null and expects IllegalArgumentException.
     * </p>
     */
    public void testCheckStringNotNullAndEmpty_NullStr() {
        try {
            Util.checkStringNotNullAndEmpty(null, "Test");

            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests Util#checkStringNotNullAndEmpty(String,String) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when str is empty and expects IllegalArgumentException.
     * </p>
     */
    public void testCheckStringNotNullAndEmpty_EmptyStr() {
        try {
            Util.checkStringNotNullAndEmpty(" ", "Test");

            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests Util#checkListNotNullAndEmpty(List,String) for accuracy.
     * </p>
     *
     * <p>
     * It verifies Util#checkListNotNullAndEmpty(List,String) is correct.
     * </p>
     */
    public void testCheckListNotNullAndEmpty() {
        List list = new ArrayList();
        list.add("1");

        Util.checkListNotNullAndEmpty(list, "list");

    }

    /**
     * <p>
     * Tests Util#checkListNotNullAndEmpty(List,String) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when list is null and expects IllegalArgumentException.
     * </p>
     */
    public void testCheckListNotNullAndEmpty_NullList() {
        try {
            Util.checkListNotNullAndEmpty(null, "list");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests Util#checkListNotNullAndEmpty(List,String) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when list is empty and expects IllegalArgumentException.
     * </p>
     */
    public void testCheckListNotNullAndEmpty_EmptyList() {
        List list = new ArrayList();
        try {
            Util.checkListNotNullAndEmpty(list, "list");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests Util#checkObjectNotNull(Object,String) for accuracy.
     * </p>
     *
     * <p>
     * It verifies Util#checkObjectNotNull(Object,String) is correct.
     * </p>
     */
    public void testCheckObjectNotNull() {
        Util.checkObjectNotNull(" ", "test");
    }

    /**
     * <p>
     * Tests Util#checkObjectNotNull(Object,String) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when obj is null and expects IllegalArgumentException.
     * </p>
     */
    public void testCheckObjectNotNull_NullObj() {
        try {
            Util.checkObjectNotNull(null, "test");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests Util#checkEventHandler(String,String) for accuracy.
     * </p>
     *
     * <p>
     * It verifies Util#checkEventHandler(String,String) is correct.
     * </p>
     */
    public void testCheckEventHandler() {
        Util.checkEventHandler(EventHandler.NOT_STARTED, "test");
    }

    /**
     * <p>
     * Tests Util#checkEventHandler(String,String) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when event is null and expects IllegalArgumentException.
     * </p>
     */
    public void testCheckEventHandler_NullEvent() {
        try {
            Util.checkEventHandler(null, "test");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests Util#checkEventHandler(String,String) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when event is empty and expects IllegalArgumentException.
     * </p>
     */
    public void testCheckEventHandler_EmptyEvent() {
        try {
            Util.checkEventHandler(" ", "test");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests Util#checkEventHandler(String,String) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when event is invalid and expects IllegalArgumentException.
     * </p>
     */
    public void testCheckEventHandler_InvalidEvent() {
        try {
            Util.checkEventHandler("invalid", "test");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

}