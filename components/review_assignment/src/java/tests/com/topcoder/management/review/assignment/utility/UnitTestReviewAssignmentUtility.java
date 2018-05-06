/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.assignment.utility;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;

import com.topcoder.management.review.assignment.BaseTestCase;

/**
 * Testcases for ReviewAssignmentUtility class.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class UnitTestReviewAssignmentUtility extends BaseTestCase {
    /**
     * The instance to store the original system <code>SecurityManager</code>.
     */
    private SecurityManager sm;

    /**
     * The guard file name.
     */
    private String guardFileName = "test_files/guard.txt";

    /**
     * <p>
     * Sets up the test environment.
     * </p>
     *
     * @throws Exception
     *             throws exception if any.
     */
    protected void setUp() throws Exception {
        super.setUp();
        sm = System.getSecurityManager();
        System.setSecurityManager(new NotExitSecurityManager());

        new File(guardFileName).delete();
    }

    /**
     * <p>
     * Tears down the test environment.
     * </p>
     *
     * @throws Exception
     *             throws exception if any.
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        System.setSecurityManager(sm);

        new File(guardFileName).delete();
    }

    /**
     * <p>
     * Accuracy test case for main method.
     * </p>
     * <p>
     * The job should be executed one time.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_main_1() throws Exception {
        runMain(new String[] {"-trackingInterval=20", "-guardFile=" + guardFileName, "-background=true"});
    }

    /**
     * <p>
     * Accuracy test case for main method.
     * </p>
     * <p>
     * Help switch presents, help message should be printed console.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_main_2() throws Exception {
        String result = callMainOut(new String[] {"-h"}, true);

        System.err.println(result);
        checkHelperMessage(result);
    }

    /**
     * <p>
     * Accuracy test case for main method.
     * </p>
     * <p>
     * Help switch presents, help message should be printed console.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_main_3() throws Exception {
        String result = callMainOut(new String[] {"-help"}, true);
        checkHelperMessage(result);
    }

    /**
     * <p>
     * Accuracy test case for main method.
     * </p>
     * <p>
     * Help switch presents, help message should be printed console.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_main_4() throws Exception {
        String result = callMainOut(new String[] {"-?"}, true);
        checkHelperMessage(result);
    }

    /**
     * <p>
     * Failure test case for main method.
     * </p>
     * <p>
     * Switch value not presents, error message should be printed.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_main_5() throws Exception {
        String result = callMainOut(new String[] {"-c="}, true);
        assertTrue("check the output message", result.contains("Fails to parse the arguments"));
    }

    /**
     * <p>
     * Failure test case for main method.
     * </p>
     * <p>
     * The configuration file type is invalid.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_main_6() throws Exception {
        String result = callMainOut(new String[] {"-c=xxx.xxx", "-guardFile=" + guardFileName,
            "-background=true"}, false);
        assertTrue("check the output message", result.contains("Unrecognized file type of the config file."));
    }

    /**
     * <p>
     * Failure test case for main method.
     * </p>
     * <p>
     * The configuration file does not exist.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_main_7() throws Exception {
        String result = callMainOut(new String[] {"-c=notexist.properties", "-guardFile=" + guardFileName,
            "-background=true"}, false);
        assertTrue("check the output message", result.contains("IO error when reading config file."));
    }

    /**
     * <p>
     * Failure test case for main method.
     * </p>
     * <p>
     * The arguments is invalid.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_main_8() throws Exception {
        String result = callMainOut(new String[] {"-trackingInterval=xxx", "-trackingInterval=123",
            "-guardFile=" + guardFileName, "-background=true"}, true);
        assertTrue("check the output message",
            result.contains("Fails to validate the value of interval arguments"));
    }

    /**
     * <p>
     * Failure test case for main method.
     * </p>
     * <p>
     * The trackingInterval cannot parse to integer.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_main_9() throws Exception {
        String result = callMainOut(new String[] {"-trackingInterval=xxx", "-guardFile=" + guardFileName,
            "-background=true"}, true);
        assertTrue("check the output message",
            result.contains("Fails to validate the value of interval arguments"));
    }

    /**
     * <p>
     * Failure test case for main method.
     * </p>
     * <p>
     * The trackingInterval value is not positive.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_main_10() throws Exception {
        String result = callMainOut(new String[] {"-trackingInterval=-15", "-guardFile=" + guardFileName,
            "-background=true"}, true);
        assertTrue("check the output message",
            result.contains("Fails to validate the value of interval arguments"));
    }

    /**
     * <p>
     * Accuracy test case for main method.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_main_11() throws Exception {
        new File(guardFileName).createNewFile();

        callMainOut(new String[] {"-trackingInterval=20", "-guardFile=" + guardFileName, "-background=true"},
            true);
    }

    /**
     * <p>
     * Failure test case for main method.
     * </p>
     * <p>
     * The '-c' switch is invalid.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_main_12() throws Exception {
        String result = callMainOut(new String[] {"-c=com/topcoder/util/config/ConfigManager.properties",
            "-guardFile=" + guardFileName, "-background=true"}, false);
        assertTrue("check the output message", result.contains("Unrecognized namespace : "));
    }

    /**
     * <p>
     * Accuracy test case for main method.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_main_13() throws Exception {
        runMain(new String[] {"-guardFile=" + guardFileName, "-background=false"});
    }

    /**
     * <p>
     * Accuracy test case for main method.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_main_14() throws Exception {
        // To use the custom configuration file the user can provide "-c" switch
        // The user can specify custom import files utility configuration file name and
        // namespace
        runMain(new String[] {"-c=test_files/config/ReviewAssignmentUtility.properties",
            "-ns=com.topcoder.management.review.assignment.utility.ReviewAssignmentUtility",
            "-guardFile=guard.tmp", "-background=true"});
    }

    /**
     * <p>
     * Accuracy test case for main method.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_main_15() throws Exception {
        // To use the custom configuration file the user can provide "-c" switch
        // The user can specify custom import files utility configuration file name and
        // namespace
        runMain(new String[] {"-c=test_files/config/ReviewAssignmentUtility.properties",
            "-ns=com.topcoder.management.review.assignment.utility.ReviewAssignmentUtility",
            "-guardFile=guard.tmp", "-background=xx"});
    }

    /**
     * Checks the output message.
     *
     * @param result
     *            the output message.
     */
    private static void checkHelperMessage(String result) {
        assertTrue("check the output message", result.contains("<file_name> Optional. Provides the"
            + " name of the configuration file for this command line application. This file is read with"
            + " use of Configuration Persistence component. Default is \"com/topcoder/management/"
            + "review/assignment/utility/ReviewAssignmentUtility.properties\"."));
        assertTrue("check the output message", result.contains("<namespace> Optional. The"
            + " namespace in the specified configuration file that contains configuration for this"
            + " command line application. Default is \"com.topcoder.management.review.assignment"
            + ".utility.ReviewAssignmentUtility\"."));
        assertTrue("check the output message", result.contains("<interval_in_sec> Optional. The"
            + " interval in seconds between checks of projects for review assignment. If not specified,"
            + " the value from the scheduler configuration is used."));
        assertTrue("check the output message",
            result.contains("        -? -h -help      print this help message"));
    }

    /**
     * Calls the {@link ReviewAssignmentUtility#main(String[])} and return the standard output string.
     *
     * @param args
     *            the arguments to call main method.
     * @param out
     *            <code>true</code> the System.out; <code>false</code> the System.err.
     *
     * @return the output string.
     *
     * @throws Exception
     *             to JUnit.
     */
    private static String callMainOut(String[] args, boolean out) throws Exception {
        OutputStream os = new ByteArrayOutputStream();
        PrintStream newOut = new PrintStream(os);
        PrintStream oldOut = out ? System.out : System.err;

        try {
            // Redirect the output stream
            if (out) {
                System.setOut(newOut);
            } else {
                System.setErr(newOut);
            }
            // Call main
            ReviewAssignmentUtility.main(args);

        } catch (SecurityException e) {
            // Ignore
        } finally {

            if (os != null) {
                os.close();
            }

            if (newOut != null) {
                newOut.close();
            }

            // Restore
            if (out) {
                System.setOut(oldOut);
            } else {
                System.setErr(oldOut);
            }
        }
        return os.toString();
    }
}
