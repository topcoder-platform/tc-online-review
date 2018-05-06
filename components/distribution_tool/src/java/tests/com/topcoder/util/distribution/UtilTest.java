/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.distribution;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogManager;

/**
 * <p>
 * Unit tests for <code>Util</code> class.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class UtilTest extends TestCase {
    /**
     * <p>
     * Represents the Log used in tests.
     * </p>
     */
    private static final Log LOG = LogManager.getLog(UtilTest.class.getName());

    /**
     * <p>
     * Returns the test suite of this class.
     * </p>
     *
     * @return the test suite of this class.
     */
    public static Test suite() {
        return new TestSuite(UtilTest.class);
    }

    /**
     * Tests accuracy of method checkNonNull, no exception should be thrown.
     */
    public void testCheckNonNull_Accuracy() {
        Util.checkNonNull("hhhh", "arg1");
    }

    /**
     * Tests failure of method checkNonNull when the argument is null, the exception IllegalArgumentException is
     * expected.
     */
    public void testCheckNonNull_Fail() {
        try {
            Util.checkNonNull(null, "null");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Tests accuracy of method checkNonNullNonEmpty, no exception should be thrown.
     */
    public void testCheckNonNullNonEmpty_Accuracy() {
        Util.checkNonNullNonEmpty("a1", "a1");
    }

    /**
     * Tests failure of method checkNonNullNonEmpty when the argument is null, the exception IllegalArgumentException is
     * expected.
     */
    public void testCheckNonNullNonEmpty_Fail1() {
        try {
            Util.checkNonNullNonEmpty(null, "null");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Tests failure of method checkNonNullNonEmpty when the argument is empty, the exception IllegalArgumentException
     * is expected.
     */
    public void testCheckNonNullNonEmpty_Fail2() {
        try {
            Util.checkNonNullNonEmpty("\t ", "empty");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Tests accuracy of method checkNonNull, no exception should be thrown.
     */
    public void testCheckList_Accuracy() {
        List<Object> list = new ArrayList<Object>();
        list.add("item1");
        list.add("item2");
        Util.checkList(list, "list for test");
    }

    /**
     * Tests accuracy of method checkNonNull, no exception should be thrown.
     */
    public void testCheckList_Accuracy1() {
        List<Object> list = new ArrayList<Object>();
        list.add(new StringBuffer());
        list.add(new StringBuffer());
        Util.checkList(list, "list for test");
    }

    /**
     * Tests failure of method checkList when the argument is null, the exception IllegalArgumentException is expected.
     */
    public void testCheckList_Fail1() {
        try {
            Util.checkList(null, "null");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Tests failure of method checkList when the argument contains null, the exception IllegalArgumentException is
     * expected.
     */
    public void testCheckList_Fail2() {
        List<Object> list = new ArrayList<Object>();
        list.add("item1");
        list.add(null);
        try {
            Util.checkList(list, "null item");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Tests failure of method checkList when the argument contains empty, the exception IllegalArgumentException is
     * expected.
     */
    public void testCheckList_Fail3() {
        List<Object> list = new ArrayList<Object>();
        list.add("item1");
        list.add(" \t");
        try {
            Util.checkList(list, "empty item");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Tests accuracy of method safeClose(InputStream).
     */
    public void testCloseInputStream1() {
        Util.safeClose(null);

        Util.safeClose(new ByteArrayInputStream(new byte[0]));
    }

    /**
     * Tests accuracy of method safeClose(OutputStream).
     */
    public void testCloseOutputStream1() {
        Util.safeClose(null);

        Util.safeClose(new ByteArrayOutputStream());
    }

    /**
     * Tests accuracy of method logInfo(Log log, String message).
     */
    public void testLogInfo() {
        Util.logInfo(null, "null log");

        Util.logInfo(LOG, "log message");
    }

    /**
     * Tests accuracy of method checkParams(Map<String, String> params, String name).
     */
    public void testCheckParams() {
        Map<String, String> tests = new HashMap<String, String>();
        tests.put("key1", "value1");
        tests.put("key2", "value2");
        Util.checkParams(tests, "tests");
    }

    /**
     * Tests failure of method checkParams(Map<String, String> params, String name) when the params is null,
     * IllegalArgumentException is expected.
     */
    public void testCheckParams_Fail1() {
        try {
            Util.checkParams(null, "null params");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Tests failure of method checkParams(Map<String, String> params, String name) when the params contains null key,
     * IllegalArgumentException is expected.
     */
    public void testCheckParams_Fail2() {
        Map<String, String> tests = new HashMap<String, String>();
        tests.put("key1", "value1");
        tests.put(null, "null");
        try {
            Util.checkParams(tests, "null key");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Tests failure of method checkParams(Map<String, String> params, String name) when the params contains empty key,
     * IllegalArgumentException is expected.
     */
    public void testCheckParams_Fail3() {
        Map<String, String> tests = new HashMap<String, String>();
        tests.put("key1", "value1");
        tests.put(" ", "empty");
        try {
            Util.checkParams(tests, "empty key");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Tests failure of method checkParams(Map<String, String> params, String name) when the params contains null value,
     * IllegalArgumentException is expected.
     */
    public void testCheckParams_Fail4() {
        Map<String, String> tests = new HashMap<String, String>();
        tests.put("key1", "value1");
        tests.put("null value", null);
        try {
            Util.checkParams(tests, "null value");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }
}
