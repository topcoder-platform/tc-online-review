/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.file.stresstests;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Date;

import com.topcoder.util.file.xslttemplate.XmlTemplateData;
import com.topcoder.util.file.xslttemplate.XsltTemplate;

import junit.framework.TestCase;

/**
 * Tests with big template and data files.
 *
 * @author TCSDEVELOPER
 * @version 3.0
 */
public class BigFileStressTest extends TestCase {
    /**
     * <p>
     * The constant represents repeats for testing.
     * </p>
     */
    private static final int REPEATS = 100;

    /**
     * <p>
     * The constant represents data repeats for testing.
     * </p>
     */
    private static final int DATAREPEATS = 5;

    /**
     * <p>
     * The big template file for testing.
     * </p>
     */
    private File bigTemplateFile;

    /**
     * <p>
     * The big data file for testing.
     * </p>
     */
    private File bigDataFile;

    /**
     * <p>
     * Setup test environment.
     * </p>
     * @throws Exception to JUnit
     *
     */
    public void setUp() throws Exception {
        bigTemplateFile = new File("test_files\\stresstests\\bigTemplateFile.txt");
        bigDataFile = new File("test_files\\stresstests\\bigDataFile.xml");
        writeBigFiles();
    }

    /**
     * <p>
     * Writes the data to files.
     * </p>
     * @throws Exception to JUnit
     *
     */
    public void writeBigFiles() throws Exception {
        // write template
        BufferedWriter fw = new BufferedWriter(new FileWriter(bigTemplateFile));
        for (int i = 0; i < REPEATS; i++) {
            fw.write("Aaaaaaaa part %AAAA" + i + "%");
            fw.newLine();
            fw.write("Bbbbb part %BBBB" + i + "%");
            fw.newLine();
            fw.write("Ccccc part %CCCC" + i + "%");
            fw.newLine();
            fw.write("Ddddddd part %DDDD" + i + "%");
            fw.newLine();
            fw.write("Eeeeeee part %if:VALUE>'150'%" + i + "%endif%");
            fw.newLine();
        }
        fw.close();
        // write data
        fw = new BufferedWriter(new FileWriter(bigDataFile));
        fw.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
        fw.newLine();
        fw.write("<DATA>");
        fw.newLine();
        for (int i = 0; i < REPEATS; i++) {
            fw.write("<AAAA" + i + ">aaaa" + i + "</AAAA" + i + ">");
            fw.newLine();
            fw.write("<BBBB" + i + ">bbbb" + i + "</BBBB" + i + ">");
            fw.newLine();
            fw.write("<CCCC" + i + ">cccc" + i + "</CCCC" + i + ">");
            fw.newLine();
            fw.write("<DDDD" + i + ">dddd" + i + "</DDDD" + i + ">");
            fw.newLine();
            fw.write("<VALUE>" + i + "</VALUE>");
            fw.newLine();
        }
        fw.write("</DATA>");
        fw.newLine();
        fw.close();
    }

    /**
     * <p>
     * Checks the data which is reading from the files.
     * </p>
     * @throws Exception to JUnit
     *
     */
    public void testBigFile() throws Exception {
        String strTemplate = TemplateAndDataStressTests.readFile(bigTemplateFile);
        String strData = TemplateAndDataStressTests.readFile(bigDataFile);
        XsltTemplate template = new XsltTemplate();
        XmlTemplateData data = new XmlTemplateData();

        Date d0 = new Date();
        // setting template and data
        template.setTemplate(strTemplate);
        data.setTemplateData(strData);

        for (int i = 0; i < DATAREPEATS; i++) {
            // reading data
            template.applyTemplate(data);
        }
        Date d1 = new Date();
        System.out.println("Running Big Files took " + (d1.getTime() - d0.getTime()) + " milliseconds");
    }
}
