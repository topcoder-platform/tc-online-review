/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.file.stresstests;

import java.io.File;
import java.io.FileReader;
import java.io.StringWriter;
import java.util.Date;

import com.topcoder.util.file.xslttemplate.XmlTemplateData;
import com.topcoder.util.file.xslttemplate.XsltTemplate;

import junit.framework.TestCase;

/**
 * <p>
 * Tests of completed work by document generator.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 3.0
 */
public class TemplateAndDataStressTests extends TestCase {
    /**
     * <p>
     * The constant represents repeats for testing.
     * </p>
     */
    private static final int REPEATS = 1000;

    /**
     * <p>
     * The out file for testing.
     * </p>
     */
    private File outFile = null;

    /**
     * <p>
     * Setup test environment.
     * </p>
     * @throws Exception to JUnit
     *
     */
    public void setUp() throws Exception {
        super.setUp();
    }

    /**
     * <p>
     * Tears down test environment.
     * </p>
     */
    public void tearDown() {
        if (outFile != null) {
            outFile.delete();
        }
    }

    /**
     * <p>
     * reading file in string.
     * </p>
     *
     * @param f File to be read
     *
     * @return string with file contents
     *
     * @throws Exception to JUnit
     */
    static String readFile(File f) throws Exception {
        FileReader reader = new FileReader(f);
        StringWriter writer = new StringWriter();
        int num = 0;
        char[] buffer = new char[1024];
        while ((num = reader.read(buffer)) != -1) {
            writer.write(buffer, 0, num);
        }
        writer.close();
        reader.close();
        String result = writer.toString();
        return result;
    }

    /**
     * <p>
     * Tests one time from template file.
     * </p>
     *
     * @throws Exception to JUnit
     *
     */
    public void testOneTemplateManyData() throws Exception {
        // defining Files
        File fileTempl = new File(new File("test_files\\stresstests"), "stress_template.txt");
        File fileData = new File(new File("test_files\\stresstests"), "stress_data.xml");

        // reading template
        String strTemplate = readFile(fileTempl);
        XsltTemplate template = new XsltTemplate();

        // reading template data from XML file
        String strData = readFile(fileData);
        XmlTemplateData data = new XmlTemplateData();

        // starting time count
        Date d0 = new Date();

        // setting template and data
        template.setTemplate(strTemplate);
        data.setTemplateData(strData);

        for (int i = 0; i < REPEATS; i++) {
            // reading data
            template.applyTemplate(data);
        }
        Date d1 = new Date();
        System.out.println("Made one template and " + REPEATS + " applications in " + (d1.getTime() - d0.getTime())
            + " milliseconds");
    }

    /**
     * <p>
     * Tests ten times from template file.
     * </p>
     *
     * @throws Exception to JUnit
     *
     */
    public void test10DataPerTemplate() throws Exception {
        // defining Files
        File fileTempl = new File(new File("test_files\\stresstests"), "stress_template.txt");
        File fileData = new File(new File("test_files\\stresstests"), "stress_data.xml");

        // reading template
        String strTemplate = readFile(fileTempl);
        XsltTemplate template = new XsltTemplate();

        // reading template data from XML file
        String strData = readFile(fileData);
        XmlTemplateData data = new XmlTemplateData();

        // starting time count
        Date d0 = new Date();
        for (int i = 0; i < REPEATS / 10; i++) {
            // setting template and data
            template.setTemplate(strTemplate);
            data.setTemplateData(strData);
            for (int ii = 0; ii < 10; ii++) {
                // reading data
                template.applyTemplate(data);
            }
        }
        Date d1 = new Date();
        System.out.println("Made " + (REPEATS / 10) + " templates and " + REPEATS + " applications in "
            + (d1.getTime() - d0.getTime()) + " milliseconds");
    }

    /**
     * <p>
     * Tests one time from template file.
     * </p>
     *
     * @throws Exception to JUnit
     *
     */
    public void testOneDataPerTemplate() throws Exception {
        // defining Files
        File fileTempl = new File(new File("test_files\\stresstests"), "stress_template.txt");
        File fileData = new File(new File("test_files\\stresstests"), "stress_data.xml");

        // reading template
        String strTemplate = readFile(fileTempl);
        XsltTemplate template = new XsltTemplate();

        // reading template data from XML file
        String strData = readFile(fileData);
        XmlTemplateData data = new XmlTemplateData();

        // starting time count
        Date d0 = new Date();
        for (int i = 0; i < REPEATS; i++) {
            // setting template and data
            template.setTemplate(strTemplate);
            data.setTemplateData(strData);
            for (int ii = 0; ii < 1; ii++) {
                // reading data
                template.applyTemplate(data);
            }
        }
        Date d1 = new Date();
        System.out.println("Made " + (REPEATS / 1) + " templates and " + REPEATS + " applications in "
            + (d1.getTime() - d0.getTime()) + " milliseconds");
    }
}
