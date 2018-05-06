/*
 * Copyright (C) 2003, 2007 TopCoder Inc., All Rights Reserved.
 *
 * TCS Project JNDI Context Utility 2.0 (Unit Tests)
 *
 * @ JNDIUtilsMainMethodTestCase.java.java
 */
package com.topcoder.naming.jndiutility;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.FileOutputStream;
import java.io.PrintStream;


/**
 * This test case tests the main method provided by JNDIUtils class allowing to dump bindings to standard output.
 * Edited in version 2.0: replaced System.out and System.err with file when testing to avoid redundant output. (Only
 * Demo prints result to them)
 *
 * @author preben
 * @author Charizard
 * @version 2.0
 *
 * @since 1.0
 */
public class JNDIUtilsMainMethodTestCase extends TestCase {
    /** Name of file which replaces System.out. */
    private static final String OUT_FILE = "JNDIUtilsMainMethodTestCase.out.txt";

    /** Name of file which replaces System.err. */
    private static final String ERR_FILE = "JNDIUtilsMainMethodTestCase.err.txt";

    static {
        // Delete the old output file for each test run
        TestHelper.getFile(OUT_FILE).delete();
        TestHelper.getFile(ERR_FILE).delete();
    }

    /** Stores the standard output stream. */
    private PrintStream oldOut;

    /** Stores the standard error output stream. */
    private PrintStream oldErr;

    /** Print stream which replaces System.out. */
    private PrintStream out;

    /** Print stream which replaces System.err. */
    private PrintStream err;

    /**
     * Constructor.
     *
     * @param name the name
     */
    public JNDIUtilsMainMethodTestCase(String name) {
        super(name);
    }

    /**
     * Return a new Test.
     *
     * @return a new Test
     */
    public static Test suite() {
        return new TestSuite(JNDIUtilsMainMethodTestCase.class);
    }

    /**
     * Set up method. Save the old System.out and System.err in fields and replace with file output stream.
     *
     * @throws Exception if error occurs
     *
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        oldOut = System.out;
        oldErr = System.err;
        out = new PrintStream(new FileOutputStream(TestHelper.getFile(OUT_FILE), true));
        err = new PrintStream(new FileOutputStream(TestHelper.getFile(ERR_FILE), true));
        System.setOut(out);
        System.setErr(err);
    }

    /**
     * Tear down method. Flush the content of output streams and recover System.out and System.err.
     *
     * @see TestCase#tearDown()
     */
    protected void tearDown() {
        out.flush();
        err.flush();
        out = null;
        err = null;
        System.setOut(oldOut);
        System.setErr(oldErr);
    }

    /**
     * Test main() with zero arguments.
     */
    public void testMainWithZeroArguements() {
        JNDIUtils.main(new String[] {});
    }

    /**
     * Test main() with one argument.
     */
    public void testMainWithOneArgument() {
        JNDIUtils.main(new String[] {"test"});
    }

    /**
     * Test main() with two arguments.
     */
    public void testMainWithTwoArguments() {
        JNDIUtils.main(new String[] {"-d", "test"});
        JNDIUtils.main(new String[] {"test", "subcontext"});
    }

    /**
     * Test main() with three arguments.
     */
    public void testMainWithThreeArguments() {
        JNDIUtils.main(new String[] {"-d", "test", "dir"});
    }

    /**
     * Test main() with more than three arguments.
     */
    public void testMainWithMoreThanThreeArguments() {
        JNDIUtils.main(new String[] {"-d", "test", "dir", "test"});
    }
}
