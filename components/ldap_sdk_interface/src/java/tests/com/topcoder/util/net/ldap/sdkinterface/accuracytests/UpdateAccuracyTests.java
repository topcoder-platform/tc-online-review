/**
 *
 * Copyright ?2004, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.net.ldap.sdkinterface.accuracytests;

import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;
import com.topcoder.util.net.ldap.sdkinterface.Update;
import com.topcoder.util.net.ldap.sdkinterface.Values;
import com.topcoder.util.net.ldap.sdkinterface.Modification;

/**
 * Tests various methods in the Update class
 *
 * @author BryanChen
 * @version 1.0
 */
public class UpdateAccuracyTests extends TestCase {
    /** Instance variable */
    private Update update;

    /**
     * Sets up the environment, just instantiates the update variable
     */
    protected void setUp() {
        update = new Update();
    }

    /**
     * Tests the accuracy of the various add methods
     */
    public void testUpdateAddAccuracy() {
        Values value = new Values("blah");
        update.add("blah", value);
        Modification mod = (Modification) update.getModifications().get(0);

        assertEquals("values is not correct", value, mod.getValues());
        assertEquals("attribute name is not correct", "blah", mod.getAttributeName());
        assertEquals("trype is not correct", Modification.ADD, mod.getType());

        update.add("anotherattribute", value);
        mod = (Modification) update.getModifications().get(1);
        assertEquals("values is not correct", value, mod.getValues());
        assertEquals("attribute name is not correct", "anotherattribute", mod.getAttributeName());
        assertEquals("type is not correct", Modification.ADD, mod.getType());
    }

    /**
     * Tests the accuracy of the various delete methods
     */
    public void testUpdateDeleteAccuracy() {
        Values value = new Values("blah");
        update.delete("blah");
        update.delete("anotherattribute", value);
        assertEquals("size is not correct", 2, update.getModifications().size());

        Modification mod = (Modification) update.getModifications().get(1);
        assertEquals("values is not correct", value, mod.getValues());
        assertEquals("attribute name is not correct", "anotherattribute", mod.getAttributeName());
        assertEquals("type is not correct", Modification.DELETE_VALUE, mod.getType());

        mod = (Modification) update.getModifications().get(0);
        assertEquals("type is not correct", Modification.DELETE_ATTRIBUTE, mod.getType());
        assertEquals("attribute name is not correct", "blah", mod.getAttributeName());
    }

    /**
     * Tests the accuracy of the various replace methods
     */
    public void testUpdateReplaceAccuracy() {
        Values value = new Values("blah");
        update.add("blah", value);
        update.delete("wha", value);
        assertEquals(2, update.getModifications().size());

        update.replace("dude", value);
        assertEquals(3, update.getModifications().size());

        Modification mod = (Modification) update.getModifications().get(2);
        assertEquals("type is not correct", Modification.REPLACE, mod.getType());
        assertEquals("attribute name is not correct", "dude", mod.getAttributeName());
        assertEquals("values is not correct", value, mod.getValues());
    }

    /**
     * Returns the suite of tests
     * @return the suite of tests
     */
    public static Test suite() {
        return new TestSuite(UpdateAccuracyTests.class);
    }
}
