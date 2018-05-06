/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.distribution.commands;

import java.io.File;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.topcoder.util.distribution.BaseTest;
import com.topcoder.util.distribution.DistributionScriptCommandExecutionException;

/**
 * <p>
 * Unit tests for <code>CommandsUtil</code> class.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class CommandsUtilTest extends BaseTest {
    /**
     * <p>
     * Returns the test suite of this class.
     * </p>
     *
     * @return the test suite of this class.
     */
    public static Test suite() {
        return new TestSuite(CommandsUtilTest.class);
    }

    /**
     * Test accuracy of method createDestPath(String curSourcePath, String curDestPath).
     */
    public void testCreateDestPath() {
        assertEquals("The value should match.", "zzz.",
                CommandsUtil.createDestPath("xxx.yyy/zzz", "{FILENAME}.{EXT}"));
        assertEquals("The value should match.", "file.ext",
                CommandsUtil.createDestPath("file.ext", "{FILENAME}.{EXT}"));
        assertEquals("The value should match.", "file.ext", CommandsUtil.createDestPath("/xxx/yyy/file.ext",
                "{FILENAME}.{EXT}"));
        assertEquals("The value should match.", "file",
                CommandsUtil.createDestPath("/xxx/yyy/file.ext", "{FILENAME}"));
        assertEquals("The value should match.", "noKeyWords", CommandsUtil.createDestPath("/xxx/yyy/file.ext",
                "noKeyWords"));
    }

    /**
     * Test accuracy of method getFileName(String fileName).
     */
    public void testGetFileName() {
        assertEquals("The value should match.", "zzz", CommandsUtil.getFileName("xxx.yyy/zzz"));
        assertEquals("The value should match.", "file", CommandsUtil.getFileName("file.ext"));
        assertEquals("The value should match.", "file", CommandsUtil.getFileName("/xxx/yyy/file.ext"));
        assertEquals("The value should match.", "file", CommandsUtil.getFileName("/xxx/yyy/file"));
    }

    /**
     * Test accuracy of method getExtension(String fileName).
     */
    public void testGetExtension() {
        assertEquals("The value should match.", "", CommandsUtil.getExtension("xxx.yyy/zzz"));
        assertEquals("The value should match.", "", CommandsUtil.getExtension("file.towdots."));
        assertEquals("The value should match.", "ext", CommandsUtil.getExtension("/xxx/yyy/file.ext"));
        assertEquals("The value should match.", "ext", CommandsUtil.getExtension("/xxx/yyy/file..ext"));
    }

    /**
     * Test failure of method copyFile(String curSourcePath, String curDestPath) when error occurs,
     * DistributionScriptCommandExecutionException is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCopyFile_Fail() throws Exception {
        try {
            CommandsUtil.copyFile("noSource", "test_files/tmp");
            fail("DistributionScriptCommandExecutionException is expected.");
        } catch (DistributionScriptCommandExecutionException e) {
            // good
        }
    }
}
