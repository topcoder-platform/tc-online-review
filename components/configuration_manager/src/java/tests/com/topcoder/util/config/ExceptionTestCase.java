/*
 * Copyright (C) 2003, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.config;

import junit.framework.TestCase;

/**
 * <p>
 * Tests the various exception classes.
 * </p>
 *
 * <p>
 * <em>Changes in 2.2:</em>
 * <ol>
 * <li>Updated test cases.</li>
 * </ol>
 * </p>
 *
 * @author WishingBone, sparemax
 * @version 2.2
 */
public class ExceptionTestCase extends TestCase {
    /**
     * Tests ConfigManagerException.
     */
    public void testConfigManagerException() {
        // no-arg
        Exception cme = new ConfigManagerException();
        assertNotNull("'ConfigManagerException' should be correct.", cme);
        // with message
        cme = new ConfigManagerException("msg");
        assertNotNull("'ConfigManagerException' should be correct.", cme);
        assertTrue("'ConfigManagerException' should be correct.", cme.getMessage().indexOf("msg") != -1);
    }

    /**
     * Tests UnknownNamespaceException.
     */
    public void testUnknownNamespaceException() {
        // no-arg
        Exception une = new UnknownNamespaceException();
        assertNotNull("'UnknownNamespaceException' should be correct.", une);
        // with message
        une = new UnknownNamespaceException("msg");
        assertNotNull("'UnknownNamespaceException' should be correct.", une);
        assertTrue("'UnknownNamespaceException' should be correct.", une.getMessage().indexOf("msg") != -1);
    }

    /**
     * Tests NamespaceAlreadyExistsException.
     */
    public void testNamespaceAlreadyExistsException() {
        // no-arg
        Exception nsae = new NamespaceAlreadyExistsException();
        assertNotNull("'NamespaceAlreadyExistsException' should be correct.", nsae);
        // with message
        nsae = new NamespaceAlreadyExistsException("msg");
        assertNotNull("'NamespaceAlreadyExistsException' should be correct.", nsae);
        assertTrue("'NamespaceAlreadyExistsException' should be correct.", nsae.getMessage().indexOf("msg") != -1);
    }

    /**
     * Tests UnknownConfigFormatException.
     */
    public void testUnknownConfigFormatException() {
        // no-arg
        Exception ucfe = new UnknownConfigFormatException();
        assertNotNull("'UnknownConfigFormatException' should be correct.", ucfe);
        // with message
        ucfe = new UnknownConfigFormatException("msg");
        assertNotNull("'UnknownConfigFormatException' should be correct.", ucfe);
        assertTrue("'UnknownConfigFormatException' should be correct.", ucfe.getMessage().indexOf("msg") != -1);
    }

    /**
     * Tests DuplicatePropertyException.
     */
    public void testDuplicatePropertyException() {
        // no-arg
        Exception dpe = new DuplicatePropertyException();
        assertNotNull("'DuplicatePropertyException' should be correct.", dpe);
        // with message
        dpe = new DuplicatePropertyException("msg");
        assertNotNull("'DuplicatePropertyException' should be correct.", dpe);
        assertTrue("'DuplicatePropertyException' should be correct.", dpe.getMessage().indexOf("msg") != -1);
    }

    /**
     * Tests ConfigLockedException.
     */
    public void testConfigLockedException() {
        // no-arg
        Exception cle = new ConfigLockedException();
        assertNotNull("'ConfigLockedException' should be correct.", cle);
        // with message
        cle = new ConfigLockedException("msg");
        assertNotNull("'ConfigLockedException' should be correct.", cle);
        assertTrue("'ConfigLockedException' should be correct.", cle.getMessage().indexOf("msg") != -1);
    }

    /**
     * Tests ConfigParserException.
     */
    public void testConfigParserException() {
        // no-arg
        Exception cpe = new ConfigParserException();
        assertNotNull("'ConfigParserException' should be correct.", cpe);
        // with message
        cpe = new ConfigParserException("msg");
        assertNotNull("'ConfigParserException' should be correct.", cpe);
        assertTrue("'ConfigParserException' should be correct.", cpe.getMessage().indexOf("msg") != -1);
    }

}
