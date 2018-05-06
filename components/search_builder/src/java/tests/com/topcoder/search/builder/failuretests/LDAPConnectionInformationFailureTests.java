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
 * Failure test cases for DatabaseConnectionInformation.
 *
 * @author WishingBone
 * @version 1.1
 */
public class LDAPConnectionInformationFailureTests extends TestCase {

    /**
     * Create with null factory.
     */
    public void testCreate_NullFactory() {
        try {
            new LDAPConnectionInformation(null, "localhost", 389, false, 1, "dnroot", "password");
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException npe) {
        }
    }

    /**
     * Create with null host.
     */
    public void testCreate_NullHost() {
        try {
            new LDAPConnectionInformation(TestHelper.getLDAPSDK(), null, 389, false, 1, "dnroot", "password");
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException npe) {
        }
    }

    /**
     * Create with empty host.
     */
    public void testCreate_EmptyHost() {
        try {
            new LDAPConnectionInformation(TestHelper.getLDAPSDK(), "", 389, false, 1, "dnroot", "password");
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException iae) {
        }
    }

    /**
     * Create with null property.
     */
    public void testCreate_NullProperty() {
        try {
            new LDAPConnectionInformation(null);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException npe) {
        }
    }

}
