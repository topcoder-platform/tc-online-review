/*
 * Copyright (C) 2003, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.config;

import java.util.Iterator;

import junit.framework.TestCase;

/**
 * <p>
 * Tests the escape.
 * </p>
 *
 * <p>
 * <em>Changes in 2.2:</em>
 * <ol>
 * <li>Updated test cases.</li>
 * </ol>
 * </p>
 *
 * @author zsudraco, sparemax
 * @version 2.2
 */
public class EscapeEnhancementTest extends TestCase {

    /**
     * The singleton ConfigManager instance.
     */
    private ConfigManager cm = null;

    /**
     * Set up testing environment.
     *
     * @throws Exception to JUnit.
     */
    protected void setUp() throws Exception {
        cm = ConfigManager.getInstance();
    }

    /**
     * Tear down testing environment.
     *
     * @throws Exception to JUnit.
     */
    protected void tearDown() throws Exception {
        for (Iterator<String> itr = cm.getAllNamespaces(); itr.hasNext();) {
            cm.removeNamespace((String) itr.next());
        }
    }

    /**
     * Test the escape functionality.
     *
     * @throws Exception to JUnit.
     */
    public void testEscapel() throws Exception {
        cm.add("EscapeEnhancement", "test_files/TestEscape.properties", ConfigManager.CONFIG_PROPERTIES_FORMAT);
        String s = cm.getString("EscapeEnhancement", "Prop1");
        assertEquals("'add' should be correct.", "test\n\r\t ! =\\!#test[ = !:%)", s);
    }

    /**
     * Test the escape functionality.
     *
     * @throws Exception to JUnit.
     */
    public void testEscape2() throws Exception {
        cm.add("EscapeEnhancement", "test_files/TestEscape.properties", ConfigManager.CONFIG_PROPERTIES_FORMAT);
        String[] ss = cm.getStringArray("EscapeEnhancement", "Prop2");
        assertEquals("'add' should be correct.", 4, ss.length);

        String s = ss[0];
        assertEquals("'add' should be correct.", 2, s.length());
        assertEquals("'add' should be correct.", (char) 0xffff, s.charAt(0));
        assertEquals("'add' should be correct.", (char) 0x1234, s.charAt(1));
    }

    /**
     * Test the escape functionality.
     *
     * @throws Exception to JUnit.
     */
    public void testEscape3() throws Exception {
        cm.add("EscapeEnhancement", "test_files/TestEscape2.properties", ConfigManager.CONFIG_PROPERTIES_FORMAT);
        String s = cm.getString("EscapeEnhancement", "Prop1");
        assertEquals("'add' should be correct.", "\n\r\t", s);

        cm.createTemporaryProperties("EscapeEnhancement");
        cm.setProperty("EscapeEnhancement", "Prop1", "\n\r\t");
        cm.commit("EscapeEnhancement", "developer");
    }

    /**
     * Test the escape functionality.
     *
     * @throws Exception to JUnit.
     */
    public void testEscape4() throws Exception {
        cm.add("EscapeEnhancement", "test_files/TestEscape.properties", ConfigManager.CONFIG_PROPERTIES_FORMAT);
        String s = cm.getString("EscapeEnhancement", " \n\rProp3 ==!\t\\:!# ");
        assertEquals("'add' should be correct.", "hello", s);
    }

    /**
     * Test the escape functionality.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testEscape5() throws Exception {
        try {
            cm.add("EscapeEnhancement", "test_files/InvalidEscape.properties",
                    ConfigManager.CONFIG_PROPERTIES_FORMAT);
            fail("ConfigManagerException should be thrown for invalid escape.");
        } catch (ConfigManagerException e) {
            // Good
        }
    }

}
