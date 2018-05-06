/**
 * Copyright &copy; TopCoder Inc, 2004. All rights reserved.
 */
package com.topcoder.util.net.ldap.sdkinterface.failuretests;
import com.topcoder.util.net.ldap.sdkinterface.*;

import junit.framework.TestCase;

/**
 * Failure test for Values class
 *
 * @author Standlove
 */
public class ValuesFailureTest extends TestCase {
    private Values values = null;

    /** Create instance to test. */
    public void setUp() {
        values = new Values();
    }

    /** com.topcoder.util.net.ldap.sdkinterface.netscape.Test ctor with bad arguments. */
    public void testCtorFailure() {
        try {
            new Values((String) null);
            fail("The given value is null");
        } catch (NullPointerException e) {
        }

        try {
            new Values((String[]) null);
            fail("The given values is null");
        } catch (NullPointerException e) {
        }
        
        try {
            new Values(new String[] { null });
            fail("The given values contains null element");
        } catch (NullPointerException e) {
        }

        try {
            new Values((byte[]) null);
            fail("the given byte[] value is null");
        } catch (NullPointerException e) {
        }

        try {
            new Values((byte[][]) null);
            fail("the given byte[][] values is null");
        } catch (NullPointerException e) {
        }

        try {
            new Values(new byte[][] { null });
            fail("the given byte[][] contains null element.");
        } catch (NullPointerException e) {
        }
    }

    /** com.topcoder.util.net.ldap.sdkinterface.netscape.Test add with bad arguments. */
    public void testAddFailure() {
        try {
            values.add((String) null);
            fail("the value is null");
        } catch (NullPointerException e) {
        }

        try {
            values.add((String[]) null);
            fail("The values is null");
        } catch (NullPointerException e) {
        }

        try {
            values.add(new String[] { null });
            fail("The values contains null element");
        } catch (NullPointerException e) {
        }

        try {
            values.add((byte[]) null);
            fail("The byte[] value is null");
        } catch (NullPointerException e) {
        }

        try {
            values.add((byte[][]) null);
            fail("The byte[][] value is null");
        } catch (NullPointerException e) {
        }

        try {
            values.add(new byte[][] { null });
            fail("The byte[][] value contains null element");
        } catch (NullPointerException e) {
        }
    }

    /** com.topcoder.util.net.ldap.sdkinterface.netscape.Test delete with bad argument. */
    public void testDeleteFailure() {
        try {
            values.delete((String) null);
            fail("the given String value is null");
        } catch (NullPointerException e) {
        }

        try {
            values.delete((byte[]) null);
            fail("the given byte[] value is null");
        } catch (NullPointerException e) {
        }
    }

    /** com.topcoder.util.net.ldap.sdkinterface.netscape.Test setTextValues with null argument. */
    public void testSetTextValuesFailure() {
        try {
            values.setTextValues(null);
            fail("the given values is null");
        } catch (NullPointerException e) {
        }

        try {
            values.setTextValues(new String[] { null });
            fail("The given values contains null element.");
        } catch (NullPointerException e) {
        }
    }

    /** com.topcoder.util.net.ldap.sdkinterface.netscape.Test setBinaryValues with null argument. */
    public void testSetBinaryValuesFailure() {
        try {
            values.setBinaryValues(null);
            fail("the given values is null");
        } catch (NullPointerException e) {
        }

        try {
            values.setBinaryValues(new byte[][] { null });
            fail("The given values contains null element.");
        } catch (NullPointerException e) {
        }
    }
}