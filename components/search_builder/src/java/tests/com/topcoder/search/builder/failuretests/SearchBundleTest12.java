/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 *
 */
package com.topcoder.search.builder.failuretests;

import com.topcoder.search.builder.SearchBundle;

import junit.framework.TestCase;


/**
 * Failure Tests for <code>SearchBundle</code> class.
 *
 * @author qiucx0161
 * @version 1.2
 */
public class SearchBundleTest12 extends TestCase {
    /**
     * Test for Filter buildLikeFilter(String, String) with null name,
     * IllegalArgumentException should be thrown.
     */
    public void testBuildLikeFilter1NullName() {
        try {
            SearchBundle.buildLikeFilter(null, "SS:123");
        } catch (IllegalArgumentException iae) {
            // ignore.
        }
    }

    /**
     * Test for Filter buildLikeFilter(String, String) with empty name,
     * IllegalArgumentException should be thrown.
     */
    public void testBuildLikeFilter1EmptyName() {
        try {
            SearchBundle.buildLikeFilter(" ", "SS:123");
        } catch (IllegalArgumentException iae) {
            // ignore.
        }
    }

    /**
     * Test for Filter buildLikeFilter(String, String) with null value,
     * IllegalArgumentException should be thrown.
     */
    public void testBuildLikeFilter1NullValue() {
        try {
            SearchBundle.buildLikeFilter("name", null);
        } catch (IllegalArgumentException iae) {
            // ignore.
        }
    }

    /**
     * Test for Filter buildLikeFilter(String, String) with empty value,
     * IllegalArgumentException should be thrown.
     */
    public void testBuildLikeFilter1EmptyValue() {
        try {
            SearchBundle.buildLikeFilter("name", " ");
        } catch (IllegalArgumentException iae) {
            // ignore.
        }
    }

    /**
     * Test for Filter buildLikeFilter(String, String) with invalid value,
     * IllegalArgumentException should be thrown.
     */
    public void testBuildLikeFilter1InvalidValue1() {
        try {
            SearchBundle.buildLikeFilter("name", "Value");
        } catch (IllegalArgumentException iae) {
            // ignore.
        }
    }

    /**
     * Test for Filter buildLikeFilter(String, String) with invalid value,
     * IllegalArgumentException should be thrown.
     */
    public void testBuildLikeFilter1InvalidValue2() {
        try {
            SearchBundle.buildLikeFilter("name", "SS:");
        } catch (IllegalArgumentException iae) {
            // ignore.
        }
    }

    /**
     * Test for Filter buildLikeFilter(String name, String value, char escapeCharacter)
     * with null name, IllegalArgumentException should be thrown.
     */
    public void testBuildLikeFilter2NullName() {
        try {
            SearchBundle.buildLikeFilter(null, "SS:123");
        } catch (IllegalArgumentException iae) {
            // ignore.
        }
    }

    /**
     * Test for Filter buildLikeFilter(String name, String value, char escapeCharacter)
     * with empty name, IllegalArgumentException should be thrown.
     */
    public void testBuildLikeFilter2EmptyName() {
        try {
            SearchBundle.buildLikeFilter(" ", "SS:123");
        } catch (IllegalArgumentException iae) {
            // ignore.
        }
    }

    /**
     * Test for Filter buildLikeFilter(String name, String value, char escapeCharacter)
     * with null value, IllegalArgumentException should be thrown.
     */
    public void testBuildLikeFilter2NullValue() {
        try {
            SearchBundle.buildLikeFilter("name", null);
        } catch (IllegalArgumentException iae) {
            // ignore.
        }
    }

    /**
     * Test for Filter buildLikeFilter(String name, String value, char escapeCharacter)
     * with empty value, IllegalArgumentException should be thrown.
     */
    public void testBuildLikeFilter2EmptyValue() {
        try {
            SearchBundle.buildLikeFilter("name", " ");
        } catch (IllegalArgumentException iae) {
            // ignore.
        }
    }

    /**
     * Test for Filter buildLikeFilter(String name, String value, char escapeCharacter)
     * with invalid value, IllegalArgumentException should be thrown.
     */
    public void testBuildLikeFilter2InvalidValue1() {
        try {
            SearchBundle.buildLikeFilter("name", "Value");
        } catch (IllegalArgumentException iae) {
            // ignore.
        }
    }

    /**
     * Test for Filter buildLikeFilter(String name, String value, char escapeCharacter)
     * with invalid value, IllegalArgumentException should be thrown.
     */
    public void testBuildLikeFilter2InvalidValue2() {
        try {
            SearchBundle.buildLikeFilter("name", "SS:");
        } catch (IllegalArgumentException iae) {
            // ignore.
        }
    }
}
