/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.file.stresstests;

import java.io.File;
import java.util.Date;

import com.topcoder.util.file.xslttemplate.XmlTemplateData;
import com.topcoder.util.file.xslttemplate.XsltTemplate;

import junit.framework.TestCase;

/**
 * <p>
 * Testing that applying templates in many threads works correctly.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 3.0
 */
public class MultithreadedTests extends TestCase {

    /**
     * <p>
     * The constant represents data repeats for testing.
     * </p>
     */
    private static final int DATAREPEATS = 10;

    /**
     * <p>
     * The Exception instance for testing.
     * </p>
     */
    private Exception lastException = null;

    /**
     * <p>
     * The constant represents out string for testing.
     * </p>
     */
    private String out0 = "";

    /**
     * <p>
     * The constant represents template string for testing.
     * </p>
     */
    private String strTemplate = "";

    /**
     * <p>
     * The constant represents data string for testing.
     * </p>
     */
    private String strData = "";

    /**
     * <p>
     * The default constructor.
     * </p>
     *
     * @param method the method
     */
    public MultithreadedTests(String method) {
        super(method);
    }

    /**
     * <p>
     * Setup test environment.
     * </p>
     * @throws Exception to JUnit
     *
     */
    public void setUp() throws Exception {
        File fileTempl = new File(new File("test_files\\stresstests"), "stress_template.txt");
        File fileData = new File(new File("test_files\\stresstests"), "stress_data.xml");
        strTemplate = TemplateAndDataStressTests.readFile(fileTempl);
        strData = TemplateAndDataStressTests.readFile(fileData);
    }

    /**
     * <p>
     * Tests that template works correctly with one time.
     * </p>
     *
     * @return the string form the file
     *
     * @throws Exception to JUnit
     */
    private String runOne() throws Exception {
        XsltTemplate template = new XsltTemplate();
        XmlTemplateData data = new XmlTemplateData();

        // setting template and data
        template.setTemplate(strTemplate);
        data.setTemplateData(strData);
        String out = "";

        for (int i = 0; i < DATAREPEATS; i++) {
            // reading data
            out = template.applyTemplate(data);
        }
        return out;
    }

    /**
     * <p>
     * Tests that template works correctly with many threads.
     * </p>
     * @throws Exception to JUnit
     */
    public void testManyThreads() throws Exception {
        out0 = runOne();
        int nthreads = 100;
        Thread[] threads = new Thread[nthreads];
        for (int i = 0; i < nthreads; i++) {
            threads[i] = new Thread(new TemplateThreadHelper());
        }
        Date d0 = new Date();
        for (int i = 0; i < nthreads; i++) {
            threads[i].start();
        }
        for (int i = 0; i < nthreads; i++) {
            threads[i].join();
        }
        Date d1 = new Date();
        if (lastException != null) {
            System.out.println("There was exception in threads " + lastException);
            throw lastException;
        }
        System.out.println("Running " + nthreads + " threads with " + DATAREPEATS + " template application each took "
            + (d1.getTime() - d0.getTime()) + " milliseconds");
    }

    /**
     * <p>
     * Parses and applies template, checks correctness and,
     * if exception happens - writes to LastException.
     * </p>
     *
     * @author TCSDEVELOPER
     * @version 2.1
     */
    class TemplateThreadHelper implements Runnable {
        /**
         * <p>
         * Parses and applies template, checks correctness and,
         * if exception happens - writes to LastException.
         * </p>
         */
        public void run() {
            try {
                String o = runOne();
                if (!out0.equals(o)) {
                    Exception e = new Exception("bad result in helper thread");
                    lastException = e;
                }
            } catch (Exception e) {
                lastException = e;
            }

        }
    }

}
