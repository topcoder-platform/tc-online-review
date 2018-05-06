/**
 * Copyright &copy; TopCoder Inc, 2004. All rights reserved.
 */
package com.topcoder.util.net.ldap.sdkinterface.failuretests;
import com.topcoder.util.net.ldap.sdkinterface.*;

import junit.framework.TestCase;

/**
 * Failure test for Update class
 *
 * @author Standlove
 */
public class UpdateFailureTest extends TestCase {
    private Update update = null;
    private Values values = null;

    /** Create instance to test. */
    public void setUp() {
        update = new Update();
        values = new Values();
    }

    /** com.topcoder.util.net.ldap.sdkinterface.netscape.Test add with bad arguments. */
    public void testAddFailure() {
        try {
            update.add(null, values);
            fail("the given attr is null");
        } catch (NullPointerException e) {
        }

        try {
            update.add(" ", values);
            fail("the given attr is empty string");
        } catch (IllegalArgumentException e) {
        }

        try {
            update.add("name", null);
            fail("the given values is null");
        } catch (NullPointerException e) {
        }
    }

    /** com.topcoder.util.net.ldap.sdkinterface.netscape.Test delete attribute with bad argument. */
    public void testDeleteAttrFailure() {
        try {
            update.delete(null);
            fail("the given attr is null");
        } catch (NullPointerException e) {
        }

        try {
            update.delete(" ");
            fail("the given attr is empty string");
        } catch (IllegalArgumentException e) {
        }
    }

    /** com.topcoder.util.net.ldap.sdkinterface.netscape.Test delete values with bad argument. */
    public void testDeleteValuesFailure() {
        try {
            update.delete(null, values);
            fail("the given attr is null");
        } catch (NullPointerException e) {
        }

        try {
            update.delete(" ", values);
            fail("the given attr is empty string");
        } catch (IllegalArgumentException e) {
        }

        try {
            update.delete("name", null);
            fail("the given values is null");
        } catch (NullPointerException e) {
        }
    }

    /** com.topcoder.util.net.ldap.sdkinterface.netscape.Test replace with bad arguments. */
    public void testReplaceFailure() {
        try {
            update.replace(null, values);
            fail("the given attr is null");
        } catch (NullPointerException e) {
        }

        try {
            update.replace(" ", values);
            fail("the given attr is empty string");
        } catch (IllegalArgumentException e) {
        }

        try {
            update.replace("name", null);
            fail("the given values is null");
        } catch (NullPointerException e) {
        }
    }
}