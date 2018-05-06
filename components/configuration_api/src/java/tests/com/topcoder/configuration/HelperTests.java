/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.configuration;

import junit.framework.TestCase;
/**
 * The test of the Helper class.
 *
 * @author haozhangr
 * @version 1.0
 */
public class HelperTests extends TestCase {
    /**
     * The failure test of the checkNull method,
     * verify the object can not be null,
     * IllegalArgumentException should be thrown.
     *
     */
    public void test_checkNull() {
        try {
            Helper.checkNull(null, "name");
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }
    /**
     * The failure test of the checkStringNullOrEmpty method,
     * verify the String can not be null,
     * IllegalArgumentException should be thrown.
     *
     */
    public void test_checkStringNullOrEmpty1() {
        try {
            Helper.checkStringNullOrEmpty(null, "name");
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }
    /**
     * The failure test of the checkStringNullOrEmpty method,
     * verify the String can not be empty,
     * IllegalArgumentException should be thrown.
     *
     */
    public void test_checkStringNullOrEmpty2() {
        try {
            Helper.checkStringNullOrEmpty("  ", "name");
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }
}
