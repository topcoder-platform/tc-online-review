/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.file.accuracytests;

import java.io.File;
import java.io.FileReader;

import junit.framework.TestCase;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.defaults.DefaultConfigurationObject;
import com.topcoder.util.file.DocumentGeneratorCommand;

/**
 * <p>
 * Test class for <code>DocumentGeneratorFactory</code>.
 * </p>
 * <p>
 * Please pay attention that you are supposed to implement as what is discussed in this thread:
 * http://forums.topcoder.com/?module=Thread&threadID=614585&start=0&mc=2#980417
 * </p>
 * @author peony
 * @version 3.0
 */
public class DocumentGeneratorCommandAccuracyTest extends TestCase {
    /** Provide a template for testing. */
    private static final String TEMPLATE1 = "test_files/accuracy3.0/template1.txt";

    /** Provide a template for testing. */
    private static final String TEMPLATE2 = "test_files/accuracy3.0/template2.txt";

    /** Provide the corresponding result for template1. */
    private static final String RESULT1 = "test_files/accuracy3.0/expected1.txt";

    /** Provide the corresponding result for template1. */
    private static final String RESULT2 = "test_files/accuracy3.0/expected2.txt";

    /** Provide template data for testing. */
    private static final String TEMPLATE_DATA = "test_files/accuracy3.0/templateData.xml";

    /** Provide a config file for testing. */
    private static final String CONFIG = "test_files/accuracy3.0/DocumentManager.xml";

    /** Provide a namespace for testing. */
    private static final String NAMESPACE = "com.topcoder.util.file";

    /** Provide a temporary file position for output in this class. */
    private static final String TEMP = "test_files/accuracy3.0/temp.txt";

    /**
     * Provide a ConfigurationObject for testing.
     */
    private ConfigurationObject co;

    /**
     * Sets up the environment.
     * @throws Exception
     *             to JUnit
     */
    protected void setUp() throws Exception {
        co = new DefaultConfigurationObject("root");
        co.setPropertyValues("sources", new String[] {"source1"});
        co.setPropertyValue("source1_class", "com.topcoder.util.file.templatesource.FileTemplateSource");
        co.setPropertyValue("source2_class", "com.topcoder.util.file.accuracytests.MockTemplateSource1");
        co.setPropertyValue("source3_class", "com.topcoder.util.file.accuracytests.MockTemplateSource2");
    }

    /**
     * Tears down the environment.
     * @throws Exception
     *             to JUnit
     */
    protected void tearDown() throws Exception {
        new File(TEMP).delete();
    }

    /**
     * Test with template1 and generate function.
     * @throws Exception
     *             to JUnit
     */
    public void testMain1_Generate1() throws Exception {
        DocumentGeneratorCommand.main(new String[] {"-c", CONFIG, "-n", NAMESPACE, "-s", "file", "-t", TEMPLATE1,
            "-x", TEMPLATE_DATA, "-o", TEMP});
        assertEquals("command line incorrect", readString(RESULT1), readString(TEMP));
    }

    /**
     * Test with template2 and generate function.
     * @throws Exception
     *             to JUnit
     */
    public void testMain1_Generate2() throws Exception {
        DocumentGeneratorCommand.main(new String[] {"-c", CONFIG, "-n", NAMESPACE, "-s", "file", "-t", TEMPLATE2,
            "-x", TEMPLATE_DATA, "-o", TEMP});
        assertEquals("command line incorrect", readString(RESULT2), readString(TEMP));
    }

    /**
     * Test with template2 and generate function. Without source specified.
     * @throws Exception
     *             to JUnit
     */
    public void testMain1_Generate3() throws Exception {
        DocumentGeneratorCommand.main(new String[] {"-c", CONFIG, "-n", NAMESPACE, "-t", TEMPLATE2, "-x",
            TEMPLATE_DATA, "-o", TEMP});
        assertEquals("command line incorrect", readString(RESULT2), readString(TEMP));
    }

    /**
     * Test with template2 and generate function. Use mock source template.
     * @throws Exception
     *             to JUnit
     */
    public void testMain1_Generate4() throws Exception {
        DocumentGeneratorCommand.main(new String[] {"-c", CONFIG, "-n", NAMESPACE, "-s", "my_file", "-t",
            TEMPLATE2, "-x", TEMPLATE_DATA, "-o", TEMP});
        assertEquals("command line incorrect", "mock1", readString(TEMP));
    }

    /**
     * Simply add one template and verify.
     * @throws Exception
     *             to JUnit
     */
    public void testMain1_Add1() throws Exception {
        DocumentGeneratorCommand.main(new String[] {"-c", CONFIG, "-n", NAMESPACE, "-s", "file", "-t", TEMP, "-a",
            TEMPLATE1});
        assertEquals("command line incorrect", readString(TEMPLATE1), readString(TEMP));
    }

    /**
     * Simply add one template and verify. Without source specified.
     * @throws Exception
     *             to JUnit
     */
    public void testMain1_Add2() throws Exception {
        DocumentGeneratorCommand.main(new String[] {"-c", CONFIG, "-n", NAMESPACE, "-t", TEMP, "-a", TEMPLATE1});
        assertEquals("command line incorrect", readString(TEMPLATE1), readString(TEMP));
    }

    /**
     * Simply add one template and verify. Use mock source template.
     * @throws Exception
     *             to JUnit
     */
    public void testMain1_Add3() throws Exception {
        DocumentGeneratorCommand.main(new String[] {"-c", CONFIG, "-n", NAMESPACE, "-s", "my_file", "-t", TEMP,
            "-a", TEMPLATE1});
        assertEquals("command line incorrect", "mock1_add", readString(TEMP));
    }

    /**
     * Simply add one template and them remove it.
     * @throws Exception
     *             to JUnit
     */
    public void testMain1_Remove1() throws Exception {
        DocumentGeneratorCommand.main(new String[] {"-c", CONFIG, "-n", NAMESPACE, "-s", "file", "-t", TEMP, "-a",
            TEMPLATE1});
        DocumentGeneratorCommand.main(new String[] {"-c", CONFIG, "-n", NAMESPACE, "-s", "file", "-t", TEMP, "-r"});
        assertEquals("command line incorrect", false, new File(TEMP).exists());
    }

    /**
     * Simply add one template and them remove it. Without source id specified.
     * @throws Exception
     *             to JUnit
     */
    public void testMain1_Remove2() throws Exception {
        DocumentGeneratorCommand.main(new String[] {"-c", CONFIG, "-n", NAMESPACE, "-s", "file", "-t", TEMP, "-a",
            TEMPLATE1});
        DocumentGeneratorCommand.main(new String[] {"-c", CONFIG, "-n", NAMESPACE, "-t", TEMP, "-r"});
        assertEquals("command line incorrect", false, new File(TEMP).exists());
    }

    /**
     * Simply add one template and them remove it. With a mock source template.
     * @throws Exception
     *             to JUnit
     */
    public void testMain1_Remove3() throws Exception {
        DocumentGeneratorCommand.main(new String[] {"-c", CONFIG, "-n", NAMESPACE, "-s", "file", "-t", TEMP, "-a",
            TEMPLATE1});
        DocumentGeneratorCommand.main(new String[] {"-c", CONFIG, "-n", NAMESPACE, "-s", "my_file", "-t", TEMP,
            "-r"});
        assertEquals("command line incorrect", true, new File(TEMP).exists());
    }

    /**
     * Simply add one template and them remove it. With a mock source template.
     * @throws Exception
     *             to JUnit
     */
    public void testMain1_Remove4() throws Exception {
        DocumentGeneratorCommand.main(new String[] {"-c", CONFIG, "-n", NAMESPACE, "-s", "my_file", "-t", TEMP,
            "-r"});
        assertEquals("command line incorrect", "mock1_remove", readString(TEMP));
    }

    /**
     * Read the content from a file. Spaces and CR/LF will be removed.
     * @param fileName
     *            The filename
     * @return the string of content
     * @throws Exception
     *             to junit.
     */
    private String readString(String fileName) throws Exception {
        FileReader reader = new FileReader(fileName);
        // Accumulate buffer.
        StringBuffer sb = new StringBuffer();

        // Buffer for reading.
        char[] buffer = new char[1024];

        // Number of read chars.
        int n = 0;

        // Read all characters to string.
        while ((n = reader.read(buffer)) != -1) {
            for (int i = 0; i < n; i++) {
                char c = buffer[i];
                if (c != ' ' && c != '\r' && c != '\n') {
                    sb.append(c);
                }
            }
        }
        reader.close();

        return new String(sb);
    }
}
