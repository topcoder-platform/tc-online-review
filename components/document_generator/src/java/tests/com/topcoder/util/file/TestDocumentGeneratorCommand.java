/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.file;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for TestDocumentGeneratorCommand.java class.
 * @author TCSDEVELOPER
 * @version 3.0
 */
public class TestDocumentGeneratorCommand extends TestCase {
    /**
     * This output stream is used in the test.
     */
    private ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024);

    /**
     * Aggregates all tests in this class.
     * @return Test suite aggregating all tests.
     */
    public static Test suite() {
        return new TestSuite(TestDocumentGeneratorCommand.class);
    }

    /**
     * Sets up the environment for the TestCase.
     * @throws Exception
     *             throw exception to JUnit.
     */
    protected void setUp() throws Exception {
        System.setOut(new PrintStream(outputStream));
        new File(TestHelper.TEST_FILES_DIR + "my_files/designer.out").delete();
        new File(TestHelper.TEST_FILES_DIR + "my_files/one.txt").delete();
    }

    /**
     * Tears down the environment for the TestCase.
     * @throws Exception
     *             throw exception to JUnit.
     */
    protected void tearDown() throws Exception {
        new File(TestHelper.TEST_FILES_DIR + "my_files/designer.out").delete();
        new File(TestHelper.TEST_FILES_DIR + "my_files/one.txt").delete();
        System.setOut(System.out);
        outputStream.close();
    }

    /**
     * Test {@link DocumentGenerator#main(String[])} method. This test test how it generate documents.
     * @throws Exception
     *             to junit
     */
    public void testMainGenerateWithoutConfigAndNamepaceWithSource() throws Exception {
        DocumentGeneratorCommand.main(new String[] {"-s", "my_file", "-t", "designer.txt", "-xml",
            TestHelper.TEST_FILES_DIR + "my_files/designer.xml", "-o",
            TestHelper.TEST_FILES_DIR + "my_files/designer.out" });

        // Check that document was generated and check its content.
        assertEquals("The document was not generated",
            TestHelper.readFile(TestHelper.TEST_FILES_DIR + "my_files/designer.out"),
            TestHelper.readFile(TestHelper.TEST_FILES_DIR + "my_files/expect_designer.out"));
    }

    /**
     * Test {@link DocumentGenerator#main(String[])} method. This test test how it generate documents.
     * @throws Exception
     *             to junit
     */
    public void testMainGenerateWithoutConfigAndNamepaceWithSourceWithoutOut() throws Exception {
        DocumentGeneratorCommand.main(new String[] {"-s", "my_file", "-t", "designer.txt", "-x",
            TestHelper.TEST_FILES_DIR + "my_files/designer.xml" });

        // Check that document was generated and check its content.
        assertTrue("The document was not generated", outputStream.toString().startsWith(
            TestHelper.readFile(TestHelper.TEST_FILES_DIR + "my_files/expect_designer.out")));
    }

    /**
     * Test {@link DocumentGenerator#main(String[])} method. This test test how it generate documents.
     * @throws Exception
     *             to junit
     */
    public void testMainGenerateWithoutConfigAndNamepaceWithoutSource() throws Exception {
        DocumentGeneratorCommand.main(new String[] {"-t", "designer.txt", "-x",
            TestHelper.TEST_FILES_DIR + "my_files/designer.xml", "-o",
            TestHelper.TEST_FILES_DIR + "my_files/designer.out" });

        // Check that document was generated and check its content.
        assertEquals("The document was not generated",
            TestHelper.readFile(TestHelper.TEST_FILES_DIR + "my_files/designer.out"),
            TestHelper.readFile(TestHelper.TEST_FILES_DIR + "my_files/expect_designer.out"));
    }

    /**
     * Test {@link DocumentGenerator#main(String[])} method. This test test how it generate documents.
     * @throws Exception
     *             to junit
     */
    public void testMainGenerateWithConfigAndNamespace() throws Exception {
        DocumentGeneratorCommand.main(new String[] {"-s", "my_file", "-t", "designer.txt", "-x",
            TestHelper.TEST_FILES_DIR + "my_files/designer.xml", "-out",
            TestHelper.TEST_FILES_DIR + "my_files/designer.out", "-c", "DocumentManager.xml", "-n",
            "myconfig" });

        // Check that document was generated and check its content.
        assertEquals("The document was not generated",
            TestHelper.readFile(TestHelper.TEST_FILES_DIR + "my_files/designer.out"),
            TestHelper.readFile(TestHelper.TEST_FILES_DIR + "my_files/expect_designer.out"));
    }

    /**
     * Test {@link DocumentGenerator#main(String[])} method. This test test how it add template.
     * @throws Exception
     *             to junit
     */
    public void testMainAddWithoutConfigAndNamespace() throws Exception {
        // Add template to the template source.
        DocumentGeneratorCommand.main(new String[] {"-s", "my_file", "-t", "one.txt", "-a",
            TestHelper.TEST_FILES_DIR + "one.txt" });
        // Check that document was added and check its content.
        assertEquals("The document was not generated", TestHelper.readFile(TestHelper.TEST_FILES_DIR
            + "my_files/one.txt"), TestHelper.readFile(TestHelper.TEST_FILES_DIR + "my_files/expect_one.txt"));
    }

    /**
     * Test {@link DocumentGenerator#main(String[])} method. This test test how it add template.
     * @throws Exception
     *             to junit
     */
    public void testMainAddWithConfigAndNamespace() throws Exception {
        // Add template to the template source.
        DocumentGeneratorCommand.main(new String[] {"-s", "my_file", "-t", "one.txt", "-a",
            TestHelper.TEST_FILES_DIR + "one.txt", "-c", "DocumentManager.xml", "-n", "myconfig" });
        // Check that document was added and check its content.
        assertEquals("The document was not generated", TestHelper.readFile(TestHelper.TEST_FILES_DIR
            + "my_files/one.txt"), TestHelper.readFile(TestHelper.TEST_FILES_DIR + "my_files/expect_one.txt"));
    }

    /**
     * Test {@link DocumentGenerator#main(String[])} method. This test test how it remove template.
     * @throws Exception
     *             to junit
     */
    public void testMainRemoveWithoutConfigAndNamespace() throws Exception {
        // Add template to the template source.
        DocumentGeneratorCommand.main(new String[] {"-s", "my_file", "-t", "one.txt", "-a",
            TestHelper.TEST_FILES_DIR + "one.txt" });
        // Remove template from template source.
        DocumentGeneratorCommand.main(new String[] {"-s", "my_file", "-t", "one.txt", "-r" });

        // Check that template was removed from template source.
        assertTrue("The document was not removed", !new File(TestHelper.TEST_FILES_DIR + "my_files/one.txt")
            .exists());
    }

    /**
     * Test {@link DocumentGenerator#main(String[])} method. This test test how it remove template.
     * @throws Exception
     *             to junit
     */
    public void testMainRemoveWithConfigAndNamespace() throws Exception {
        // Add template to the template source.
        DocumentGeneratorCommand.main(new String[] {"-s", "my_file", "-t", "one.txt", "-a",
            TestHelper.TEST_FILES_DIR + "one.txt", "-c", "DocumentManager.xml", "-n", "myconfig" });
        // Remove template from template source.
        DocumentGeneratorCommand.main(new String[] {"-s", "my_file", "-t", "one.txt", "-r", "-c",
            "DocumentManager.xml", "-n", "myconfig" });

        // Check that template was removed from template source.
        assertTrue("The document was not removed", !new File(TestHelper.TEST_FILES_DIR + "my_files/one.txt")
            .exists());
    }

    /**
     * Test {@link DocumentGenerator#main(String[])} method. This test with a invalid parameter.
     * <p>
     * -t[emplate] parameter is not exist.
     * </p>
     */
    public void testMainInvalid1() {
        DocumentGeneratorCommand.main(new String[] {"-s", "my_file", "-a",
            TestHelper.TEST_FILES_DIR + "one.txt", "-c", "DocumentManager.xml", "-n", "myconfig" });
        assertTrue("output stream should not be empty.", outputStream.toString().length() > 0);
    }

    /**
     * Test {@link DocumentGenerator#main(String[])} method. This test with a invalid parameter.
     * <p>
     * parameter is invalid.
     * </p>
     */
    public void testMainInvalid2() {
        DocumentGeneratorCommand.main(new String[] {"asdf" });
        assertTrue("output stream should not be empty.", outputStream.toString().length() > 0);
    }

    /**
     * Test {@link DocumentGenerator#main(String[])} method. This test with a invalid parameter.
     * <p>
     * -s and -source both exist.
     * </p>
     */
    public void testMainInvalid3() {
        DocumentGeneratorCommand.main(new String[] {"-s", "my_file", "-source", "my_file", "-t",
            "designer.txt", "-x", TestHelper.TEST_FILES_DIR + "my_files/designer.xml", "-o",
            TestHelper.TEST_FILES_DIR + "my_files/designer.out" });
        assertTrue("output stream should not be empty.", outputStream.toString().length() > 0);
    }

    /**
     * Test {@link DocumentGenerator#main(String[])} method. This test with a invalid parameter.
     * <p>
     * -o and -out both exist.
     * </p>
     */
    public void testMainInvalid4() {
        DocumentGeneratorCommand.main(new String[] {"-s", "my_file", "-out",
            TestHelper.TEST_FILES_DIR + "my_files/designer.out", "-t", "designer.txt", "-x",
            TestHelper.TEST_FILES_DIR + "my_files/designer.xml", "-o",
            TestHelper.TEST_FILES_DIR + "my_files/designer.out" });
        assertTrue("output stream should not be empty.", outputStream.toString().length() > 0);
    }

    /**
     * Test {@link DocumentGenerator#main(String[])} method. This test with a invalid parameter.
     * <p>
     * -c and -config both exist.
     * </p>
     */
    public void testMainInvalid5() {
        DocumentGeneratorCommand.main(new String[] {"-s", "my_file", "-config",
            DocumentGeneratorCommand.DEFAULT_CONFIGURATION_FILE, "-c",
            DocumentGeneratorCommand.DEFAULT_CONFIGURATION_FILE, "-t", "designer.txt", "-x",
            TestHelper.TEST_FILES_DIR + "my_files/designer.xml", "-o",
            TestHelper.TEST_FILES_DIR + "my_files/designer.out" });
        assertTrue("output stream should not be empty.", outputStream.toString().length() > 0);
    }

    /**
     * Test {@link DocumentGenerator#main(String[])} method. This test with a invalid parameter.
     * <p>
     * -n and -namespace both exist.
     * </p>
     */
    public void testMainInvalid6() {
        DocumentGeneratorCommand.main(new String[] {"-s", "my_file", "-n",
            DocumentGeneratorCommand.DEFAULT_NAMESPACE, "-namespace",
            DocumentGeneratorCommand.DEFAULT_NAMESPACE, "-t", "designer.txt", "-x",
            TestHelper.TEST_FILES_DIR + "my_files/designer.xml", "-o",
            TestHelper.TEST_FILES_DIR + "my_files/designer.out" });
        assertTrue("output stream should not be empty.", outputStream.toString().length() > 0);
    }

    /**
     * Test {@link DocumentGenerator#main(String[])} method. This test with a invalid parameter.
     * <p>
     * -x and -xml both exist.
     * </p>
     */
    public void testMainInvalid7() {
        DocumentGeneratorCommand.main(new String[] {"-s", "my_file", "-xml",
            TestHelper.TEST_FILES_DIR + "my_files/designer.xml", "-t", "designer.txt", "-x",
            TestHelper.TEST_FILES_DIR + "my_files/designer.xml", "-o",
            TestHelper.TEST_FILES_DIR + "my_files/designer.out" });
        assertTrue("output stream should not be empty.", outputStream.toString().length() > 0);
    }

    /**
     * Test {@link DocumentGenerator#main(String[])} method. This test with a invalid parameter.
     * <p>
     * invalid config.
     * </p>
     */
    public void testMainInvalid8() {
        DocumentGeneratorCommand
            .main(new String[] {"-s", "my_file", "-t", "designer.txt", "-x",
                TestHelper.TEST_FILES_DIR + "my_files/designer.xml", "-o",
                TestHelper.TEST_FILES_DIR + "my_files/designer.out", "-c", "DocumentManager.xml", "-n",
                "invalid" });
        assertTrue("output stream should not be empty.", outputStream.toString().length() > 0);
    }

    /**
     * Test {@link DocumentGenerator#main(String[])} method. This test with a invalid parameter.
     * <p>
     * -o exist, but -xml not exist.
     * </p>
     */
    public void testMainInvalid9() {
        DocumentGeneratorCommand
            .main(new String[] {"-s", "my_file", "-t", "designer.txt", "-o",
                TestHelper.TEST_FILES_DIR + "my_files/designer.out", "-a",
                TestHelper.TEST_FILES_DIR + "one.txt" });
        assertTrue("output stream should not be empty.", outputStream.toString().length() > 0);
    }

    /**
     * Test {@link DocumentGenerator#main(String[])} method. This test with a invalid parameter.
     * <p>
     * source template is not exist.
     * </p>
     */
    public void testMainInvalid10() {
        DocumentGeneratorCommand.main(new String[] {"-s", "test", "-t", "designer.txt", "-x",
            TestHelper.TEST_FILES_DIR + "my_files/designer.xml", "-o",
            TestHelper.TEST_FILES_DIR + "my_files/designer.out" });
        assertTrue("output stream should not be empty.", outputStream.toString().length() > 0);
    }

    /**
     * Test {@link DocumentGenerator#main(String[])} method. This test with a invalid parameter.
     * <p>
     * xml file is invalid.
     * </p>
     */
    public void testMainInvalid11() {
        DocumentGeneratorCommand.main(new String[] {"-s", "my_file", "-t", "designer.txt", "-x",
            TestHelper.TEST_FILES_DIR + "my_files/xxx.xml", "-o",
            TestHelper.TEST_FILES_DIR + "my_files/designer.out" });
        assertTrue("output stream should not be empty.", outputStream.toString().length() > 0);
    }

    /**
     * Test {@link DocumentGenerator#main(String[])} method. This test with a invalid parameter.
     * <p>
     * add file is invalid.
     * </p>
     */
    public void testMainInvalid12() {
        DocumentGeneratorCommand.main(new String[] {"-s", "my_file", "-t", "one.txt", "-a",
            TestHelper.TEST_FILES_DIR + "asdf.txt" });
        assertTrue("output stream should not be empty.", outputStream.toString().length() > 0);
    }

    /**
     * Test {@link DocumentGenerator#main(String[])} method. This test with a invalid parameter.
     * <p>
     * remove file is invalid.
     * </p>
     */
    public void testMainInvalid13() {
        // Remove template from template source.
        DocumentGeneratorCommand.main(new String[] {"-s", "my_file", "-t", "asdf.txt", "-r" });

        assertTrue("output stream should not be empty.", outputStream.toString().length() > 0);
    }

    /**
     * Test {@link DocumentGenerator#main(String[])} method. This test with a invalid parameter.
     * <p>
     * -o exist, but -x not exist.
     * </p>
     */
    public void testMainInvalid14() {
        DocumentGeneratorCommand
            .main(new String[] {"-s", "my_file", "-t", "designer.txt", "-o",
                TestHelper.TEST_FILES_DIR + "my_files/designer.out", "-a",
                TestHelper.TEST_FILES_DIR + "one.txt" });
        assertTrue("output stream should not be empty.", outputStream.toString().length() > 0);
    }

    /**
     * Test {@link DocumentGenerator#main(String[])} method. This test with a invalid parameter.
     * <p>
     * namespace is invalid
     * </p>
     */
    public void testMainInvalid15() {
        DocumentGeneratorCommand.main(new String[] {"-s", "my_file", "-t", "designer.txt", "-x",
            TestHelper.TEST_FILES_DIR + "my_files/designer.xml", "-out",
            TestHelper.TEST_FILES_DIR + "my_files/designer.out", "-c", "DocumentManager.xml", "-n", "bad" });
        assertTrue("output stream should not be empty.", outputStream.toString().length() > 0);
    }

    /**
     * Test {@link DocumentGenerator#main(String[])} method. This test with a invalid parameter.
     * <p>
     * config file is invalid
     * </p>
     */
    public void testMainInvalid16() {
        DocumentGeneratorCommand.main(new String[] {"-s", "my_file", "-t", "designer.txt", "-x",
            TestHelper.TEST_FILES_DIR + "my_files/designer.xml", "-out",
            TestHelper.TEST_FILES_DIR + "my_files/designer.out", "-c", "xxx.xml", "-n", "bad" });
        assertTrue("output stream should not be empty.", outputStream.toString().length() > 0);
    }

}
