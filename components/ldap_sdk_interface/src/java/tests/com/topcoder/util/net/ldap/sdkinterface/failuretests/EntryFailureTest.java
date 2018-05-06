/**
 * Copyright &copy; TopCoder Inc, 2004. All rights reserved.
 */
package com.topcoder.util.net.ldap.sdkinterface.failuretests;
import com.topcoder.util.net.ldap.sdkinterface.*;

import junit.framework.TestCase;
import java.util.Map;
import java.util.HashMap;

/**
 * Failure test for Entry class
 *
 * @author Standlove
 */
public class EntryFailureTest extends TestCase {
    private Entry entry = null;
    private Values values = null;

    /** Create instance to test. */
    public void setUp() {
        entry = new Entry("c=CN");
        values = new Values();
    }

    /** com.topcoder.util.net.ldap.sdkinterface.netscape.Test the ctor with illegal arguments. */
    public void testCtorFailure() {
        try {
            new Entry(null);
            fail("the given dn is null");
        } catch (NullPointerException e) {
        }
    }

    /** com.topcoder.util.net.ldap.sdkinterface.netscape.Test setDn with null argument. */
    public void testDnFailure() {
        try {
            entry.setDn(null);
            fail("the given dn is null");
        } catch (NullPointerException e) {
        }
    }

    /** com.topcoder.util.net.ldap.sdkinterface.netscape.Test setAttribute with bad arguments. */
    public void testSetAttributeFailure() {
        try {
            entry.setAttribute(null, values);
            fail("the given name is null");
        } catch (NullPointerException e) {
        }

        try {
            entry.setAttribute("name", null);
            fail("the given values is null");
        } catch (NullPointerException e) {
        }

        try {
            entry.setAttribute(" ", values);
            fail("the given name is empty");
        } catch (IllegalArgumentException e) {
        }
    }

    /** com.topcoder.util.net.ldap.sdkinterface.netscape.Test setAttributes with bad arguments. */
    public void testSetAttributesFailure() {
        try {
            entry.setAttributes(null);
            fail("the given attributes is null");
        } catch (NullPointerException e) {
        }

        try {
            // null key
            Map attr = new HashMap();
            attr.put(null, values);
            entry.setAttributes(attr);
            fail("the attr contains null key");
        } catch (IllegalArgumentException e) {
        }

        try {
            // empty key
            Map attr = new HashMap();
            attr.put(" ", values);
            entry.setAttributes(attr);
            fail("the attr contains empty key");
        } catch (IllegalArgumentException e) {
        }

        try {
            // non-string key
            Map attr = new HashMap();
            attr.put(new Integer(1), values);
            entry.setAttributes(attr);
            fail("the attr contains non-string key");
        } catch (IllegalArgumentException e) {
        }

        try {
            // null value
            Map attr = new HashMap();
            attr.put("name", null);
            entry.setAttributes(attr);
            fail("the attr contains null value");
        } catch (IllegalArgumentException e) {
        }

        try {
            // non-Values value
            Map attr = new HashMap();
            attr.put("name", "value");
            entry.setAttributes(attr);
            fail("the attr contains non-Values value");
        } catch (IllegalArgumentException e) {
        }
    }

    /** com.topcoder.util.net.ldap.sdkinterface.netscape.Test getValues with bad arguments. */
    public void testGetValuesFailure() {
        try {
            entry.getValues(null);
            fail("the given attr is null");
        } catch (NullPointerException e) {
        }

        try {
            entry.getValues("non-exist");
            fail("the given attr is non-exist");
        } catch (IllegalArgumentException e) {
        }

        try {
            entry.getValues(" ");
            fail("the given attr is empty string");
        } catch (IllegalArgumentException e) {
        }
    }

    /** com.topcoder.util.net.ldap.sdkinterface.netscape.Test deleteAttribute with bad arguments. */
    public void testDeleteAttributeFailure() {
        try {
            entry.deleteAttribute(null);
            fail("the attr is null");
        } catch (NullPointerException e) {
        }

        try {
            entry.deleteAttribute(" ");
            fail("the attr is empty");
        } catch (IllegalArgumentException e) {
        }
    }
}