package com.topcoder.security.authenticationfactory.accuracytests;

import com.topcoder.security.authenticationfactory.Principal;
import junit.framework.TestCase;

import java.util.List;


/**
 * Accuracy tests for Principal class.
 */
public class PrincipalAccuracyTest extends TestCase {
    /**
     * The principal instance to test.
     */
    Principal p = null;

    /**
     * The id to create the principal.
     */
    Object id = null;

    /**
     * Setup for each test case.
     *
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        id = new Object();
        p = new Principal(id);
    }

    /**
     * Test the accuracy of the constructor.
     */
    public void testConstructor() {
        assertSame("Id is not correctly initialized.", id, p.getId());
    }

    /**
     * Test the accuracy of the addMapping() method.
     */
    public void testAddMapping() {
        Object value = new Object();
        p.addMapping("key", value);
        assertSame("Not the expected value.", value, p.getValue("key"));
    }

    /**
     * Test the accuracy of the removeMapping() method.
     */
    public void testRemoveMapping() {
        p.addMapping("key", new Object());
        p.removeMapping("key");
        assertFalse("Entry is not removed.", p.containsKey("key"));
    }

    /**
     * Test the accuracy of the clearMappings() method.
     */
    public void testClearMappings() {
        p.addMapping("key1", new Object());
        p.addMapping("key2", new Object());
        p.clearMappings();

        assertFalse("Map is not cleared.", p.containsKey("key1") || p.containsKey("key2"));
    }

    /**
     * Test the accuracy of the getKeys() method.
     */
    public void testGetKeys() {
        p.addMapping("key1", new Object());
        p.addMapping("key2", new Object());

        List keys = p.getKeys();

        assertEquals("Not the expected number of keys.", 2, keys.size());
        assertTrue("The returned key list does not contain 'key1'.", keys.contains("key1"));
        assertTrue("The returned key list does not contain 'key2'.", keys.contains("key2"));
    }

    /**
     * Test the accuracy of the containsKey() method.
     */
    public void testContainsKey() {
        p.addMapping("key1", new Object());

        assertTrue("Wrong result for 'key1'.", p.containsKey("key1"));
        assertFalse("Wrong result for 'key2'.", p.containsKey("key2"));
    }

    /**
     * Test the accuracy of the getId() method.
     */
    public void testGetId() {
        assertSame("Not the expected Id.", id, p.getId());
    }
}
