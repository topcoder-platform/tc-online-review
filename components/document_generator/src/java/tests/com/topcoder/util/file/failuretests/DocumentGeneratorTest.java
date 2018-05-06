/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.file.failuretests;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.util.file.DocumentGenerator;
import com.topcoder.util.file.TemplateDataFormatException;
import com.topcoder.util.file.TemplateFormatException;
import com.topcoder.util.file.fieldconfig.Node;
import com.topcoder.util.file.fieldconfig.TemplateFields;
import com.topcoder.util.file.templatesource.FileTemplateSource;
import com.topcoder.util.file.templatesource.TemplateSourceException;
import com.topcoder.util.file.xslttemplate.XsltTemplate;

/**
 * @author AdamSelene This class tests DocumentGenerator for proper failure.
 * @author extra
 * @version 3.0
 * @author Chenhong
 * @version 2.1
 * @since 2.0
 */
public class DocumentGeneratorTest extends TestCase {

    /**
     * Represents the document generator for test.
     */
    private DocumentGenerator generator;

    /**
     * Represents file template source for test.
     */
    private FileTemplateSource templateSource;

    /**
     * Sets up test environment.
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        generator = new DocumentGenerator();
        ConfigurationObject config = FailureTestHelper.createConfigurationObject("failure/DocumentManager.xml",
                "myconfig");
        templateSource = new FileTemplateSource("source_id", config);
        generator.setTemplateSource("file", templateSource);
        generator.setDefaultTemplateSource(templateSource);
        super.setUp();
    }

    /**
     * Creates a attachment point for this testcase.
     *
     * @return a wonderful testsuite for this case.
     */
    public static Test suite() {
        return new TestSuite(DocumentGeneratorTest.class);
    }

    /**
     * Clear all the namespace in the config manager.
     *
     * @throws Exception
     *             to junit.
     */
    void clearNamespace() throws Exception {
    }

    /* getTemplate(STRING) */

    /**
     * Tests getTemplate with bad data.
     */
    public void testGetTemplateBadData() {
        try {
            generator.getTemplate("test_files/failure/invalid-simple-brokendefaultvalue.txt");

            fail("Did not fail.");
        } catch (TemplateFormatException e) { /* expected */
        } catch (Exception e) {
            fail("Wrong exception " + e.toString());
        }
    }

    /**
     * Tests getTemplate with bad data.
     */
    public void testGetTemplateBadData2() {
        try {
            generator.getTemplate("test_files/failure/invalid-simple-nofielddata.txt");

            fail("Did not fail.");
        } catch (TemplateFormatException e) { /* expected */
        } catch (Exception e) {
            fail("Wrong exception " + e.toString());
        }
    }

    /**
     * Tests getTemplate with bad data.
     */
    public void testGetTemplateBadData3() {
        try {
            generator.getTemplate("test_files/failure/invalid-simple-nofieldname.txt");

            fail("Did not fail.");
        } catch (TemplateFormatException e) { /* expected */
        } catch (Exception e) {
            fail("Wrong exception " + e.toString());
        }
    }

    /**
     * Tests getTemplate with bad data.
     */
    public void testGetTemplateBadData4() {
        try {
            generator.getTemplate("test_files/failure/invalid-simple-trailingescape.txt");

            fail("Did not fail.");
        } catch (TemplateFormatException e) { /* expected */
        } catch (Exception e) {
            fail("Wrong exception " + e.toString());
        }
    }

    /**
     * Tests locked file.
     */
    public void testGetTemplateLocked() {
        File file = null;
        FileChannel ch = null;
        FileLock lock = null;
        try {
            file = new File("test_files/failure/valid-simple-comment.txt");
            ch = new RandomAccessFile(file, "rw").getChannel();
            lock = ch.lock();
        } catch (IOException e) {
            fail("Non-developer error: could not obtain lock on file.");
        }

        try {
            generator.getTemplate("test_files/failure/valid-simple-comment.txt");
            fail("Did not fail.");
        } catch (TemplateSourceException e) { /* expected */
        } catch (IllegalArgumentException e) {
            fail("IAE only on nulls.");
        } catch (Exception e) {
            fail("wrong exception");
        }

        try {
            lock.release();
            ch.close();
        } catch (IOException e) {
            // do nothing
        }
    }

    /**
     * Tests non-existent file.
     */
    public void testGetTemplateNE() {
        try {
            generator.getTemplate("test_files/failure/doesnotexist.txt");
            fail("Did not fail.");
        } catch (TemplateSourceException e) { /* expected */
        } catch (Exception e) {
            fail("wrong exception" + e.toString());
        }

        assertFalse("Created nonexistant file", new File("test_files/failure/doesnotexist.txt").exists());
    }

    /**
     * Tests null arg to getTemplate.
     */
    public void testGetTemplateNull() {
        try {
            generator.getTemplate(null);
            fail("Did not fail.");
        } catch (IllegalArgumentException e) { /* expected */
        } catch (Exception e) {
            fail("wrong exception" + e.toString());
        }

        assertFalse("Created nonexistant file", new File("test_files/failure/doesnotexist.txt").exists());
    }

    /**
     * Tests empty arg to getTemplate.
     */
    public void testGetTemplateES() {
        try {
            generator.getTemplate("");
            fail("Did not fail.");
        } catch (IllegalArgumentException e) { /* expected */
        } catch (Exception e) {
            fail("wrong exception" + e.toString());
        }

        assertFalse("Created nonexistant file", new File("test_files/failure/doesnotexist.txt").exists());
    }

    /* END getTemplate(STRING) */

    /* getTemplate(STRING, STRING) */

    /**
     * Tests getTemplate with bad data.
     */
    public void testGetTemplate2BadData() {
        try {
            generator.getTemplate("file", "test_files/failure/invalid-simple-brokendefaultvalue.txt");

            fail("Did not fail.");
        } catch (TemplateFormatException e) { /* expected */
        } catch (Exception e) {
            fail("Wrong exception " + e.toString());
        }
    }

    /**
     * Tests getTemplate with bad data.
     */
    public void testGetTemplate2BadData2() {
        try {
            generator.getTemplate("file", "test_files/failure/invalid-simple-nofielddata.txt");

            fail("Did not fail.");
        } catch (TemplateFormatException e) { /* expected */
        } catch (Exception e) {
            fail("Wrong exception " + e.toString());
        }
    }

    /**
     * Tests getTemplate with bad data.
     */
    public void testGetTemplate2BadData3() {
        try {
            generator.getTemplate("file", "test_files/failure/invalid-simple-nofieldname.txt");

            fail("Did not fail.");
        } catch (TemplateFormatException e) { /* expected */
        } catch (Exception e) {
            fail("Wrong exception " + e.toString());
        }
    }

    /**
     * Tests getTemplate with bad data.
     */
    public void testGetTemplate2BadData4() {
        try {
            generator.getTemplate("file", "test_files/failure/invalid-simple-trailingescape.txt");

            fail("Did not fail.");
        } catch (TemplateFormatException e) { /* expected */
        } catch (Exception e) {
            fail("Wrong exception " + e.toString());
        }
    }

    /**
     * Tests locked file.
     */
    public void testGetTemplate2Locked() {
        File file = null;
        FileChannel ch = null;
        FileLock lock = null;
        try {
            file = new File("test_files/failure/valid-simple-comment.txt");
            ch = new RandomAccessFile(file, "rw").getChannel();
            lock = ch.lock();
        } catch (IOException e) {
            fail("Non-developer error: could not obtain lock on file.");
        }

        try {
            generator.getTemplate("file", "test_files/failure/valid-simple-comment.txt");
            fail("Did not fail.");
        } catch (TemplateSourceException e) { /* expected */
        } catch (IllegalArgumentException e) {
            fail("IAE only on nulls.");
        } catch (Exception e) {
            fail("wrong exception");
        }

        try {
            lock.release();
            ch.close();
        } catch (IOException e) {
            // do nothing
        }
    }

    /**
     * Tests non-existent file.
     */
    public void testGetTemplate2NE() {
        try {
            generator.getTemplate("file", "test_files/failure/doesnotexist.txt");
            fail("Did not fail.");
        } catch (TemplateSourceException e) { /* expected */
        } catch (Exception e) {
            fail("wrong exception" + e.toString());
        }

        assertFalse("Created nonexistant file", new File("test_files/failure/doesnotexist.txt").exists());
    }

    /**
     * Tests null arg to getTemplate.
     */
    public void testGetTemplate2Null() {
        try {
            generator.getTemplate("file", null);
            fail("Did not fail.");
        } catch (IllegalArgumentException e) { /* expected */
        } catch (Exception e) {
            fail("wrong exception" + e.toString());
        }

        assertFalse("Created nonexistant file", new File("test_files/failure/doesnotexist.txt").exists());
    }

    /**
     * Tests null file source.
     */
    public void testGetTemplate2Null2() {
        try {
            generator.getTemplate((String) null, "test_files/failure/valid-simple-comment.txt");
            fail("Did not fail.");
        } catch (IllegalArgumentException e) { /* expected */
        } catch (Exception e) {
            fail("wrong exception" + e.toString());
        }
    }

    /**
     * Tests null arg to getTemplate.
     */
    public void testGetTemplate2Null3() {
        try {
            generator.getTemplate((String) null, (String) null);
            fail("Did not fail.");
        } catch (IllegalArgumentException e) { /* expected */
        } catch (Exception e) {
            fail("wrong exception" + e.toString());
        }
    }

    /**
     * Tests empty arg to getTemplate.
     */
    public void testGetTemplate2ES() {
        try {
            generator.getTemplate("", (String) "test_files/failure/doesnotexist.txt");
            fail("Did not fail.");
        } catch (IllegalArgumentException e) { /* expected */
        } catch (Exception e) {
            fail("wrong exception" + e.toString());
        }

        assertFalse("Created nonexistant file", new File("test_files/failure/doesnotexist.txt").exists());
    }

    /**
     * Tests empty arg to getTemplate.
     */
    public void testGetTemplate2ES2() {
        try {
            generator.getTemplate("file", "");
            fail("Did not fail.");
        } catch (IllegalArgumentException e) { /* expected */
        } catch (Exception e) {
            fail("wrong exception" + e.toString());
        }
    }

    /**
     * Tests empty arg to getTemplate.
     */
    public void testGetTemplate2ES3() {
        try {
            generator.getTemplate("", "");
            fail("Did not fail.");
        } catch (IllegalArgumentException e) { /* expected */
        } catch (Exception e) {
            fail("wrong exception" + e.toString());
        }
    }

    /* END getTemplate(string,string) */

    /* getTemplate(TemplateSource, string) */

    /**
     * Tests getTemplate with bad data.
     */
    public void testGetTemplate3BadData() {
        try {
            generator.getTemplate(templateSource, "test_files/failure/invalid-simple-brokendefaultvalue.txt");

            fail("Did not fail.");
        } catch (TemplateFormatException e) { /* expected */
        } catch (Exception e) {
            fail("Wrong exception " + e.toString());
        }
    }

    /**
     * Tests getTemplate with bad data.
     */
    public void testGetTemplate3BadData2() {
        try {
            generator.getTemplate(templateSource, "test_files/failure/invalid-simple-nofielddata.txt");

            fail("Did not fail.");
        } catch (TemplateFormatException e) { /* expected */
        } catch (Exception e) {
            fail("Wrong exception " + e.toString());
        }
    }

    /**
     * Tests getTemplate with bad data.
     */
    public void testGetTemplate3BadData3() {
        try {
            generator.getTemplate(templateSource, "test_files/failure/invalid-simple-nofieldname.txt");

            fail("Did not fail.");
        } catch (TemplateFormatException e) { /* expected */
        } catch (Exception e) {
            fail("Wrong exception " + e.toString());
        }
    }

    /**
     * Tests getTemplate with bad data.
     */
    public void testGetTemplate3BadData4() {
        try {
            generator.getTemplate(templateSource, "test_files/failure/invalid-simple-trailingescape.txt");

            fail("Did not fail.");
        } catch (TemplateFormatException e) { /* expected */
        } catch (Exception e) {
            fail("Wrong exception " + e.toString());
        }
    }

    /**
     * Tests locked file.
     */
    public void testGetTemplate3Locked() {
        File file = null;
        FileChannel ch = null;
        FileLock lock = null;
        try {
            file = new File("test_files/failure/valid-simple-comment.txt");
            ch = new RandomAccessFile(file, "rw").getChannel();
            lock = ch.lock();
        } catch (IOException e) {
            fail("Non-developer error: could not obtain lock on file.");
        }

        try {
            generator.getTemplate(templateSource, "test_files/failure/valid-simple-comment.txt");
            fail("Did not fail.");
        } catch (TemplateSourceException e) { /* expected */
        } catch (IllegalArgumentException e) {
            fail("IAE only on nulls.");
        } catch (Exception e) {
            fail("wrong exception");
        }

        try {
            lock.release();
            ch.close();
        } catch (IOException e) {
        }
    }

    /**
     * Tests non-existent file.
     */
    public void testGetTemplate3NE() {
        try {
            generator.getTemplate(templateSource, "test_files/failure/doesnotexist.txt");
            fail("Did not fail.");
        } catch (TemplateSourceException e) { /* expected */
        } catch (Exception e) {
            fail("wrong exception" + e.toString());
        }

        assertFalse("Created nonexistant file", new File("test_files/failure/doesnotexist.txt").exists());
    }

    /**
     * Tests null arg to getTemplate.
     */
    public void testGetTemplate3Null() {
        try {
            generator.getTemplate(templateSource, null);
            fail("Did not fail.");
        } catch (IllegalArgumentException e) { /* expected */
        } catch (Exception e) {
            fail("wrong exception" + e.toString());
        }
    }

    /**
     * Tests null file source.
     */
    public void testGetTemplate3Null2() {
        try {
            generator.getTemplate((FileTemplateSource) null, "test_files/failure/valid-simple-comment.txt");
            fail("Did not fail.");
        } catch (IllegalArgumentException e) { /* expected */
        } catch (Exception e) {
            fail("wrong exception" + e.toString());
        }
    }

    /**
     * Tests null arg to getTemplate.
     */
    public void testGetTemplate3Null3() {
        try {
            generator.getTemplate((FileTemplateSource) null, (String) null);
            fail("Did not fail.");
        } catch (IllegalArgumentException e) { /* expected */
        } catch (Exception e) {
            fail("wrong exception" + e.toString());
        }
    }

    /**
     * Tests empty arg to getTemplate.
     */
    public void testGetTemplate3ES2() {
        try {
            generator.getTemplate(templateSource, "");
            fail("Did not fail.");
        } catch (IllegalArgumentException e) { /* expected */
        } catch (Exception e) {
            fail("wrong exception" + e.toString());
        }
    }

    /**
     * Tests null arg to parseTemplate.
     */
    public void testParseTemplateNull() {
        try {
            generator.parseTemplate((String) null);
            fail("Did not fail.");
        } catch (IllegalArgumentException e) { /* expected */
        } catch (Exception e) {
            fail("wrong exception" + e.toString());
        }
    }

    /**
     * Tests null arg to parseTemplate.
     */
    public void testParseTemplateES() {
        try {
            generator.parseTemplate("");
            fail("Did not fail.");
        } catch (IllegalArgumentException e) { /* expected */
        } catch (Exception e) {
            fail("wrong exception" + e.toString());
        }
    }

    /**
     * Tests null arg to parseTemplate.
     */
    public void testParseTemplateBad() {
        try {
            generator.parseTemplate("%EXX}{%");
            fail("Did not fail.");
        } catch (TemplateFormatException e) { /* expected */
        } catch (Exception e) {
            fail("wrong exception" + e.toString());
        }
    }

    /**
     * Tests null arg to parseTemplate.
     */
    public void testParseTemplateBad2() {
        try {
            generator.parseTemplate("\\yes");
            fail("Did not fail.");
        } catch (TemplateFormatException e) { /* expected */
        } catch (Exception e) {
            fail("wrong exception" + e.toString());
        }
    }

    /**
     * Tests null arg to parseTemplate.
     */
    public void testParseTemplateBad3() {
        try {
            generator.parseTemplate("yes\\");
            fail("Did not fail.");
        } catch (TemplateFormatException e) { /* expected */
        } catch (Exception e) {
            fail("wrong exception" + e.toString());
        }
    }

    public void testParseTemplateReaderNull() {
        try {
            generator.parseTemplate((Reader) null);
            fail("Did not fail.");
        } catch (IllegalArgumentException e) { /* expected */
        } catch (Exception e) {
            fail("wrong exception" + e.toString());
        }
    }

    /**
     * Tests bad template data arg to parseTemplate.
     */
    public void testParseTemplateReaderBad() {
        try {
            generator.parseTemplate(new FileReader("test_files/failure/invalid-simple-nofielddata.txt"));
            fail("Did not fail.");
        } catch (TemplateFormatException e) { /* expected */
        } catch (Exception e) {
            fail("wrong exception" + e.toString());
        }
    }

    /**
     * Tests bad template data arg to parseTemplate.
     */
    public void testParseTemplateReaderBad2() {
        try {
            generator.parseTemplate(new FileReader("test_files/failure/invalid-simple-nofieldname.txt"));
            fail("Did not fail.");
        } catch (TemplateFormatException e) { /* expected */
        } catch (Exception e) {
            fail("wrong exception" + e.toString());
        }
    }

    /**
     * Tests bad template data arg to parseTemplate.
     */
    public void testParseTemplateReaderBad3() {
        try {
            generator.parseTemplate(new FileReader("test_files/failure/invalid-simple-brokendefaultvalue.txt"));
            fail("Did not fail.");
        } catch (TemplateFormatException e) { /* expected */
        } catch (Exception e) {
            fail("wrong exception" + e.toString());
        }
    }

    /**
     * Tests bad template data arg to parseTemplate.
     */
    public void testParseTemplateReaderBad4() {
        try {
            generator.parseTemplate(new FileReader("test_files/failure/invalid-simple-trailingescape.txt"));
            fail("Did not fail.");
        } catch (TemplateFormatException e) { /* expected */
        } catch (Exception e) {
            fail("wrong exception" + e.toString());
        }
    }

    /**
     * Tests null template arg to getFields.
     */
    public void testGetFieldsNull() {
        try {
            generator.getFields(null);
            fail("Did not fail.");
        } catch (IllegalArgumentException e) { /* expected */
        } catch (Exception e) {
            fail("wrong exception" + e.toString());
        }
    }

    /**
     * Tests null template arg to applyTemplate.
     */
    public void testApplyTemplateNull() {
        try {
            generator.applyTemplate(null, "<DATA></DATA>");
            fail("Did not fail.");
        } catch (IllegalArgumentException e) { /* expected */
        } catch (Exception e) {
            fail("wrong exception" + e.toString());
        }
    }

    /**
     * Tests null template arg to applyTemplate.
     */
    public void testApplyTemplateNull2() {

        try {
            generator.applyTemplate((XsltTemplate) null, (String) null);
            fail("Did not fail.");
        } catch (IllegalArgumentException e) { /* expected */
        } catch (Exception e) {
            fail("wrong exception" + e.toString());
        }
    }

    /**
     * Tests null template arg to applyTemplate.
     */
    public void testApplyTemplateNull3() {
        try {
            generator
                    .applyTemplate(generator.getTemplate("test_files/failure/valid-simple-comment.txt"), (String) null);
            fail("Did not fail.");
        } catch (IllegalArgumentException e) { /* expected */
        } catch (Exception e) {
            fail("wrong exception" + e.toString());
        }
    }

    /**
     * Tests empty template arg to applyTemplate.
     */
    public void testApplyTemplateES() {
        try {
            generator.applyTemplate(generator.getTemplate("test_files/failure/valid-simple-comment.txt"), "");
            fail("Did not fail.");
        } catch (IllegalArgumentException e) { /* expected */
        } catch (Exception e) {
            fail("wrong exception" + e.toString());
        }
    }

    /**
     * Tests invalid template arg to applyTemplate.
     */
    public void testApplyTemplateTrick() {
        try {
            generator.applyTemplate(new XsltTemplate(), "<DATA></DATA>");
            fail("Did not fail.");
        } catch (TemplateFormatException e) { /* expected */
        } catch (Exception e) {
            fail("wrong exception" + e.toString());
        }
    }

    /**
     * Tests bad template arg to applyTemplate.
     */
    public void testApplyTemplateBadData() {
        try {
            generator.applyTemplate(generator.getTemplate("test_files/failure/valid-simple-comment.txt"), "</DATA>");
            fail("Did not fail.");
        } catch (TemplateDataFormatException e) { /* expected */
        } catch (Exception e) {
            fail("wrong exception" + e.toString());
        }
    }

    /**
     * Tests null template arg to applyTemplate.
     */
    public void testApplyTemplate2Null() {
        try {
            generator.applyTemplate(null, new FileReader("test_files/failure/valid-empty-data.xml"));
            fail("Did not fail.");
        } catch (IllegalArgumentException e) { /* expected */
        } catch (Exception e) {
            fail("wrong exception" + e.toString());
        }
    }

    /**
     * Tests null template arg to applyTemplate.
     */
    public void testApplyTemplate2Null2() {

        try {
            generator.applyTemplate((XsltTemplate) null, (FileReader) null);
            fail("Did not fail.");
        } catch (IllegalArgumentException e) { /* expected */
        } catch (Exception e) {
            fail("wrong exception" + e.toString());
        }
    }

    /**
     * Tests null template arg to applyTemplate.
     */
    public void testApplyTemplate2Null3() {
        try {
            generator.applyTemplate(generator.getTemplate("test_files/failure/valid-simple-comment.txt"),
                    (FileReader) null);
            fail("Did not fail.");
        } catch (IllegalArgumentException e) { /* expected */
        } catch (Exception e) {
            fail("wrong exception" + e.toString());
        }
    }

    /**
     * Tests invalid template arg to applyTemplate.
     */
    public void testApplyTemplate2Trick() {
        try {
            generator.applyTemplate(new XsltTemplate(), new FileReader("test_files/failure/valid-empty-data.xml"));
            fail("Did not fail.");
        } catch (TemplateFormatException e) { /* expected */
        } catch (Exception e) {
            fail("wrong exception" + e.toString());
        }
    }

    /**
     * Tests bad template arg to applyTemplate.
     */
    public void testApplyTemplate2BadData() {
        try {

            generator.applyTemplate(generator.getTemplate("test_files/failure/valid-simple-comment.txt"),
                    new FileReader("test_files/failure/invalid-empty-data.xml"));
            fail("Did not fail.");
        } catch (TemplateDataFormatException e) { /* expected */
        } catch (Exception e) {
            fail("wrong exception" + e.toString());
        }
    }

    /**
     * Tests null template arg to applyTemplate.
     */
    public void testApplyTemplate3Null() {
        try {
            generator.applyTemplate((TemplateFields) null);
            fail("Did not fail.");
        } catch (IllegalArgumentException e) { /* expected */
        } catch (Exception e) {
            fail("wrong exception" + e.toString());
        }
    }

    /**
     * Tests bad template arg to applyTemplate.
     */
    public void testApplyTemplate3BadTemplate() {
        TemplateFields nt = new TemplateFields(new Node[0], new XsltTemplate());

        try {
            generator.applyTemplate(nt);
            fail("Did not fail.");
        } catch (TemplateFormatException e) { /* expected */
        } catch (Exception e) {
            fail("wrong exception" + e.toString());
        }
    }

    /**
     * Tests good template arg to applyTemplate.
     */
    public void testApplyTemplate3GoodTemplate() {
        TemplateFields nt = null;
        try {
            nt = new TemplateFields(new Node[0], generator.getTemplate("test_files/failure/valid-simple-comment.txt"));
        } catch (Exception e) {
            fail("Non dev exception - " + e.toString());
        }

        try {
            generator.applyTemplate(nt);
        } catch (Exception e) {
            fail("no exception expected " + e.toString());
        }
    }

    /**
     * Tests null template arg to applyTemplate.
     */
    public void testApplyTemplate4Null() {
        try {
            generator.applyTemplate(null, "<DATA></DATA>", new StringWriter());
            fail("Did not fail.");
        } catch (IllegalArgumentException e) { /* expected */
        } catch (Exception e) {
            fail("wrong exception" + e.toString());
        }
    }

    /**
     * Tests null template arg to applyTemplate.
     */
    public void testApplyTemplate4Null2() {

        try {
            generator.applyTemplate(generator.getTemplate("test_files/failure/valid-simple-comment.txt"),
                    (String) null, new StringWriter());
            fail("Did not fail.");
        } catch (IllegalArgumentException e) { /* expected */
        } catch (Exception e) {
            fail("wrong exception" + e.toString());
        }
    }

    /**
     * Tests null template arg to applyTemplate.
     */
    public void testApplyTemplate4Null3() {
        try {
            generator.applyTemplate(null, (String) null, new StringWriter());
            fail("Did not fail.");
        } catch (IllegalArgumentException e) { /* expected */
        } catch (Exception e) {
            fail("wrong exception" + e.toString());
        }
    }

    /**
     * Tests null template arg to applyTemplate.
     */
    public void testApplyTemplate4Null4() {

        try {
            generator.applyTemplate(generator.getTemplate("test_files/failure/valid-simple-comment.txt"),
                    (String) null, null);
            fail("Did not fail.");
        } catch (IllegalArgumentException e) { /* expected */
        } catch (Exception e) {
            fail("wrong exception" + e.toString());
        }
    }

    /**
     * Tests null template arg to applyTemplate.
     */
    public void testApplyTemplate4Null5() {

        try {
            generator.applyTemplate(null, "<DATA></DATA>", null);
            fail("Did not fail.");
        } catch (IllegalArgumentException e) { /* expected */
        } catch (Exception e) {
            fail("wrong exception" + e.toString());
        }
    }

    /**
     * Tests null template arg to applyTemplate.
     */
    public void testApplyTemplate4Null6() {
        try {
            generator.applyTemplate((XsltTemplate) null, (String) null, (StringWriter) null);
            fail("Did not fail.");
        } catch (IllegalArgumentException e) { /* expected */
        } catch (Exception e) {
            fail("wrong exception" + e.toString());
        }
    }

    /**
     * Tests null template arg to applyTemplate.
     */
    public void testApplyTemplate4Null7() {

        try {
            generator.applyTemplate(generator.getTemplate("test_files/failure/valid-simple-comment.txt"),
                    "<DATA></DATA>", null);
            fail("Did not fail.");
        } catch (IllegalArgumentException e) { /* expected */
        } catch (Exception e) {
            fail("wrong exception" + e.toString());
        }

    }

    /**
     * Tests empty template arg to applyTemplate.
     */
    public void testApplyTemplate4ES() {
        try {
            generator.applyTemplate(generator.getTemplate("test_files/failure/valid-simple-comment.txt"), "",
                    new StringWriter());
            fail("Did not fail.");
        } catch (IllegalArgumentException e) { /* expected */
        } catch (Exception e) {
            fail("wrong exception" + e.toString());
        }
    }

    /**
     * Tests invalid template arg to applyTemplate.
     */
    public void testApplyTemplate4Trick() {
        try {
            generator.applyTemplate(new XsltTemplate(), "<DATA></DATA>", new StringWriter());
            fail("Did not fail.");
        } catch (TemplateFormatException e) { /* expected */
        } catch (Exception e) {
            fail("wrong exception" + e.toString());
        }
    }

    /**
     * Tests bad template arg to applyTemplate.
     */
    public void testApplyTemplate4BadData() {
        try {
            generator.applyTemplate(generator.getTemplate("test_files/failure/valid-simple-comment.txt"), "</DATA>",
                    new StringWriter());
            fail("Did not fail.");
        } catch (TemplateDataFormatException e) { /* expected */
        } catch (Exception e) {
            fail("wrong exception" + e.toString());
        }
    }

    /**
     * Tests null template arg to applyTemplate.
     */
    public void testApplyTemplate5Null() {
        try {
            generator.applyTemplate(null, new StringReader("<DATA></DATA>"), new StringWriter());
            fail("Did not fail.");
        } catch (IllegalArgumentException e) { /* expected */
        } catch (Exception e) {
            fail("wrong exception" + e.toString());
        }
    }

    /**
     * Tests null template arg to applyTemplate.
     */
    public void testApplyTemplate5Null2() {

        try {
            generator.applyTemplate(generator.getTemplate("test_files/failure/valid-simple-comment.txt"),
                    (StringReader) null, new StringWriter());
            fail("Did not fail.");
        } catch (IllegalArgumentException e) { /* expected */
        } catch (Exception e) {
            fail("wrong exception" + e.toString());
        }
    }

    /**
     * Tests null template arg to applyTemplate.
     */
    public void testApplyTemplate5Null3() {
        try {
            generator.applyTemplate(generator.getTemplate("test_files/failure/valid-simple-comment.txt"),
                    new StringReader("<DATA></DATA>"), null);
            fail("Did not fail.");
        } catch (IllegalArgumentException e) { /* expected */
        } catch (Exception e) {
            fail("wrong exception" + e.toString());
        }
    }

    /**
     * Tests null template arg to applyTemplate.
     */
    public void testApplyTemplate5Null4() {
        try {
            generator.applyTemplate(null, (StringReader) null, new StringWriter());
            fail("Did not fail.");
        } catch (IllegalArgumentException e) { /* expected */
        } catch (Exception e) {
            fail("wrong exception" + e.toString());
        }
    }

    /**
     * Tests null template arg to applyTemplate.
     */
    public void testApplyTemplate5Null5() {

        try {
            generator.applyTemplate(generator.getTemplate("test_files/failure/valid-simple-comment.txt"),
                    (StringReader) null, null);
            fail("Did not fail.");
        } catch (IllegalArgumentException e) { /* expected */
        } catch (Exception e) {
            fail("wrong exception" + e.toString());
        }
    }

    /**
     * Tests null template arg to applyTemplate.
     */
    public void testApplyTemplate5Null6() {

        try {
            generator.applyTemplate(null, new StringReader("#nothing to see here"), new StringWriter());
            fail("Did not fail.");
        } catch (IllegalArgumentException e) { /* expected */
        } catch (Exception e) {
            fail("wrong exception" + e.toString());
        }
    }

    /**
     * Tests null template arg to applyTemplate.
     */
    public void testApplyTemplate5Null7() {
        try {
            generator.applyTemplate((XsltTemplate) null, (String) null, (StringWriter) null);
            fail("Did not fail.");
        } catch (IllegalArgumentException e) { /* expected */
        } catch (Exception e) {
            fail("wrong exception" + e.toString());
        }
    }

    /**
     * Tests invalid template arg to applyTemplate.
     */
    public void testApplyTemplate5Trick() {
        try {
            generator.applyTemplate(new XsltTemplate(), new StringReader("<DATA></DATA>"), new StringWriter());
            fail("Did not fail.");
        } catch (TemplateFormatException e) { /* expected */
        } catch (Exception e) {
            fail("wrong exception" + e.toString());
        }
    }

    /**
     * Tests bad template arg to applyTemplate.
     */
    public void testApplyTemplate5BadData() {
        try {
            generator.applyTemplate(generator.getTemplate("test_files/failure/valid-simple-comment.txt"),
                    new StringReader("</DATA>"), new StringWriter());
            fail("Did not fail.");
        } catch (TemplateDataFormatException e) { /* expected */
        } catch (Exception e) {
            fail("wrong exception" + e.toString());
        }
    }

    /**
     * Tests null template arg to applyTemplate.
     */
    public void testApplyTemplate6Null() {
        try {
            generator.applyTemplate((TemplateFields) null, new StringWriter());
            fail("Did not fail.");
        } catch (IllegalArgumentException e) { /* expected */
        } catch (Exception e) {
            fail("wrong exception" + e.toString());
        }
    }

    /**
     * Tests null template arg to applyTemplate.
     */
    public void testApplyTemplate6Null2() {
        TemplateFields nt = null;
        try {
            nt = new TemplateFields(new Node[0], generator.getTemplate("test_files/failure/valid-simple-comment.txt"));
        } catch (Exception e) {
            fail("Non dev exception - " + e.toString());
        }

        try {
            generator.applyTemplate(nt, null);
            fail("Did not fail.");
        } catch (IllegalArgumentException e) { /* expected */
        } catch (Exception e) {
            fail("wrong exception" + e.toString());
        }
    }

    /**
     * Tests null template arg to applyTemplate.
     */
    public void testApplyTemplate6Null3() {
        try {
            generator.applyTemplate((TemplateFields) null, null);
            fail("Did not fail.");
        } catch (IllegalArgumentException e) { /* expected */
        } catch (Exception e) {
            fail("wrong exception" + e.toString());
        }
    }

    /**
     * Tests bad template arg to applyTemplate.
     */
    public void testApplyTemplate6BadTemplate() {
        TemplateFields nt = new TemplateFields(new Node[0], new XsltTemplate());

        try {
            generator.applyTemplate(nt, new StringWriter());
            fail("Did not fail.");
        } catch (TemplateFormatException e) { /* expected */
        } catch (Exception e) {
            fail("wrong exception" + e.toString());
        }
    }

    /**
     * Tests good template arg to applyTemplate.
     */
    public void testApplyTemplate6GoodTemplate() {
        TemplateFields nt = null;
        try {
            nt = new TemplateFields(new Node[0], generator.getTemplate("test_files/failure/valid-simple-comment.txt"));
        } catch (Exception e) {
            fail("Non dev exception - " + e.toString());
        }

        try {
            generator.applyTemplate(nt, new StringWriter());
        } catch (Exception e) {
            fail("no exception expected " + e.toString());
        }
    }

    /**
     * Failure tests for method setTemplateSource(String sourceId, TemplateSource source). With null sourceId,
     * IllegalArgumentException expected.
     */
    public void testSetTemplateSource_Null_SourceId() {
        try {
            generator.setTemplateSource(null, templateSource);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Failure tests for method setTemplateSource(String sourceId, TemplateSource source). With empty sourceId,
     * IllegalArgumentException expected.
     */
    public void testSetTemplateSource_Empty_SourceId() {
        try {
            generator.setTemplateSource(" ", templateSource);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Failure tests for method setTemplateSource(String sourceId, TemplateSource source). With null source,
     * IllegalArgumentException expected.
     */
    public void testSetTemplateSource_Null_Source() {
        try {
            generator.setTemplateSource("file", null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Failure tests for method getTemplateSource(String sourceId). With null sourceId, IllegalArgumentException
     * expected.
     */
    public void testGetTemplateSource_Null_SourceId() {
        try {
            generator.getTemplateSource(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Failure tests for method getTemplateSource(String sourceId). With empty sourceId, IllegalArgumentException
     * expected.
     */
    public void testGetTemplateSource_Empty_SourceId() {
        try {
            generator.getTemplateSource(" ");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Failure tests for method removeTemplateSource(String sourceId). With null sourceId, IllegalArgumentException
     * expected.
     */
    public void testRemoveTemplateSource_Null_SourceId() {
        try {
            generator.removeTemplateSource(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Failure tests for method getTemplateSource(String sourceId). With empty sourceId, IllegalArgumentException
     * expected.
     */
    public void testRemoveTemplateSource_Empty_SourceId() {
        try {
            generator.removeTemplateSource(" ");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Failure tests for method setDefaultTemplateSource(TemplateSource source). With null source,
     * IllegalArgumentException expected.
     */
    public void testSetDefaultTemplateSource_Null_Source() {
        try {
            generator.setDefaultTemplateSource(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}