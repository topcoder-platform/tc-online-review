/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.date.workdays;

import junit.framework.TestCase;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import java.util.Properties;
import java.util.Scanner;

/**
 * This class is used to check the behavior of the <code>DefaultWorkdaysFactory</code> class, including tests of
 * creating a workdays instance by DefaultWorkdaysFactory.
 *
 * @author TCSDEVELOPER
 * @version 1.1
 */
public class DefaultWorkdaysFactoryTest extends TestCase {
    /**
     * Represent the configuration file path.
     */
    private static final String FILENAME =
        "test_files\\com\\topcoder\\date\\workdays\\defaultWorkdaysFactory.properties";

    /**
     * Represent the incorrect configuration file path.
     */
    private static final String FILENAME1 = "test_files\\errordFactory.properties";

    /**
     * Represent the correct configuration file path.
     */
    private static final String FILENAME2 = "test_files\\dFactory.properties";

    /**
     * A WorkdaysFactory instance to create Workdays instance to test.
     */
    private WorkdaysFactory factory = new DefaultWorkdaysFactory();

    /**
     * The properties to load the DefaultWorkdaysFactory's configuration file.
     */
    private Properties prop = new Properties();

    /**
     * The FileInputStream instance to load the DefaultWorkdaysFactory's configuration file.
     */
    private FileInputStream file = null;

    /**
     * Load the DefaultWorkdaysFactory's configuration file to initialize the parameter prop.
     *
     * @throws Exception
     *             if can't load the configuration file
     */
    protected void setUp() throws Exception {
        file = new FileInputStream(DefaultWorkdaysFactoryTest.class.getClassLoader().getResource(
                DefaultWorkdaysFactory.CONFIGURATION_FILE).getFile());
        prop.load(file);
    }

    /**
     * Clear the properties and close the file input stream.
     *
     * @throws Exception
     *             if any IOException throws when close the file.
     */
    protected void tearDown() throws Exception {
        prop.clear();
        file.close();
    }

    /**
     * <p>
     * Accuracy test of <code>createWorkdaysInstance</code> method when use the ConfigurationFileManager.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCreateWorkdaysAccuracy() throws Exception {
        DefaultWorkdaysFactory instance = new DefaultWorkdaysFactory(true);
        DefaultWorkdays workdays = (DefaultWorkdays) instance.createWorkdaysInstance();
        assertEquals("Test the file_format", DefaultWorkdays.XML_FILE_FORMAT, workdays.getFileFormat());
    }

    /**
     * Test the content of the instance created by DefaultWorkdaysFactory, this method only test the file_name and
     * file_format in the instance, other properties will be tested in the method testing the construtor of
     * DefaultWorkdays.
     */
    public void testCreateWorkdaysInstance() {
        DefaultWorkdays workdays = (DefaultWorkdays) factory.createWorkdaysInstance();
        assertNotNull("Test instance created by DefaultWorkdaysFactory", workdays);
        assertEquals("Test the file_name", prop.getProperty(DefaultWorkdaysFactory.FILE_NAME_PROPERTY), workdays
                .getFileName());

        String fileFormat = prop.getProperty(DefaultWorkdaysFactory.FILE_FORMAT_PROPERTY);

        if (fileFormat == null) {
            // if the file_format property is not specified in configuration file,
            // set XML_FILE_FORMAT as default value
            fileFormat = DefaultWorkdays.XML_FILE_FORMAT;
        }
        assertEquals("Test the file_format", fileFormat, workdays.getFileFormat());
    }

    /**
     * <p>
     * Accuracy test of <code>createWorkdaysInstance</code> method when the filename is null.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCreateWorddaysInstance2() throws Exception {
        copyFile(FILENAME, FILENAME1);
        WorkdaysFactory instance = new DefaultWorkdaysFactory(true);
        DefaultWorkdays workdays = (DefaultWorkdays) instance.createWorkdaysInstance();
        assertEquals("Test the file_format", DefaultWorkdays.XML_FILE_FORMAT, workdays.getFileFormat());
        copyFile(FILENAME, FILENAME2);
    }

    /**
     * Replace the file1 with file2.
     *
     * @param filename1
     *            the file1 path.
     * @param fileName2
     *            the file2 path.
     *
     * @throws Exception
     *             to JUnit
     */
    private static void copyFile(String filename1, String fileName2) throws Exception {
        File file1 = new File(filename1);
        file1.delete();
        File dst = new File(file1.getAbsolutePath());
        File file2 = new File(fileName2);
        Scanner in = new Scanner(new FileInputStream(file2));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dst.getAbsoluteFile())));
        while (in.hasNextLine()) {
            bw.write(in.nextLine());
            bw.write("\n");
        }
        in.close();
        bw.close();
    }
}
