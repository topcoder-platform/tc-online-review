/**
 * Copyright (c) 2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.search.builder.failuretests;

import com.topcoder.search.builder.*;
import com.topcoder.search.builder.filter.*;
import com.topcoder.search.builder.database.*;
import com.topcoder.search.builder.ldap.*;
import junit.framework.TestCase;

/**
 * Failure test cases for ValidationResult.
 *
 * @author WishingBone
 * @version 1.1
 */
public class ValidationResultFailureTests extends TestCase {

    /**
     * createInvalidResult() with null message.
     */
    public void testCreateInvalidResult_NullMessage() {
        try {
            ValidationResult.createInvalidResult(null, new EqualToFilter("quantity", new Integer(12)));
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException npe) {
        }
    }

    /**
     * createInvalidResult() with null filter.
     */
    public void testCreateInvalidResult_NullFilter() {
        try {
            ValidationResult.createInvalidResult("message", null);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException npe) {
        }
    }

    /**
     * createInvalidResult() with empty message.
     */
    public void testCreateInvalidResult_EmptyMessage() {
        try {
            ValidationResult.createInvalidResult("", new EqualToFilter("quantity", new Integer(12)));
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException iae) {
        }
    }

}
