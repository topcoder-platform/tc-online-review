/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 *
 * TCS Project JNDI Context Utility 2.0 (Unit Tests)
 *
 * @ XmlFileConfigurationStrategyTest.java
 */
package com.topcoder.naming.jndiutility.configstrategy;

import com.topcoder.naming.jndiutility.ConfigurationException;
import com.topcoder.naming.jndiutility.TestHelper;

import junit.framework.TestCase;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import java.nio.channels.FileLock;


/**
 * Junit test class for XmlFileConfigurationStrategy.
 *
 * @author Charizard
 * @version 2.0
 */
public class XmlFileConfigurationStrategyTest extends TestCase {
    /** XML file used in tests. */
    private static final String FILE_NAME = "XmlFileConfigurationStrategyTest.xml";

    /** First invalid XML file used in tests. Wrong XML syntax. */
    private static final String INVALID_FILE_1 = "XmlFileConfigurationStrategyTest.invalid1.xml";

    /** Second invalid XML file used in tests. Wrong document element name. */
    private static final String INVALID_FILE_2 = "XmlFileConfigurationStrategyTest.invalid2.xml";

    /** Third invalid XML file used in tests. Contains property element without name. */
    private static final String INVALID_FILE_3 = "XmlFileConfigurationStrategyTest.invalid3.xml";

    /** Fourth invalid XML file used in tests. Contains property element with non-text child. */
    private static final String INVALID_FILE_4 = "XmlFileConfigurationStrategyTest.invalid4.xml";

    /** Fifth invalid XML file used in tests. Contains property element without child. */
    private static final String INVALID_FILE_5 = "XmlFileConfigurationStrategyTest.invalid5.xml";

    /**
     * Test method for {@link XmlFileConfigurationStrategy#XmlFileConfigurationStrategy(File, boolean)}.
     * Accuracy case, try to instantiate.
     *
     * @throws Exception if error occurs
     */
    public void testXmlFileConfigurationStrategyFileBooleanAccuracy()
        throws Exception {
        assertNotNull("failed to instantiate", new XmlFileConfigurationStrategy(TestHelper.getFile(FILE_NAME)));
    }

    /**
     * Test method for {@link XmlFileConfigurationStrategy#XmlFileConfigurationStrategy(File, boolean)}.
     * Failure case 1, call with null.
     *
     * @throws Exception if error occurs
     */
    public void testXmlFileConfigurationStrategyFileBooleanFailure1()
        throws Exception {
        try {
            new XmlFileConfigurationStrategy((File) null);
            fail("exception has not been thrown");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link XmlFileConfigurationStrategy#XmlFileConfigurationStrategy(File, boolean)}.
     * Failure case 2, call with inexistent file.
     *
     * @throws Exception if error occurs
     */
    public void testXmlFileConfigurationStrategyFileBooleanFailure2()
        throws Exception {
        try {
            new XmlFileConfigurationStrategy(TestHelper.getFile("inexistent file"));
            fail("exception has not been thrown");
        } catch (ConfigurationException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link XmlFileConfigurationStrategy#XmlFileConfigurationStrategy(File, boolean)}.
     * Failure case 3, use wrong xml file.
     *
     * @throws Exception if error occurs
     */
    public void testXmlFileConfigurationStrategyFileBooleanFailure3()
        throws Exception {
        try {
            new XmlFileConfigurationStrategy(TestHelper.getFile(INVALID_FILE_1));
            fail("exception has not been thrown");
        } catch (ConfigurationException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link XmlFileConfigurationStrategy#XmlFileConfigurationStrategy(InputStream,
     * boolean)}. Accuracy case, try to instantiate.
     *
     * @throws Exception if error occurs
     */
    public void testXmlFileConfigurationStrategyInputStreamBooleanAccuracy()
        throws Exception {
        InputStream inputStream = new FileInputStream(TestHelper.getFile(FILE_NAME));

        try {
            assertNotNull("failed to instantiate", new XmlFileConfigurationStrategy(inputStream));
        } finally {
            inputStream.close();
        }
    }

    /**
     * Test method for {@link XmlFileConfigurationStrategy#XmlFileConfigurationStrategy(InputStream,
     * boolean)}. Failure case 1, call with null.
     *
     * @throws Exception if error occurs
     */
    public void testXmlFileConfigurationStrategyInputStreamBooleanFailure1()
        throws Exception {
        try {
            new XmlFileConfigurationStrategy((InputStream) null);
            fail("exception has not been thrown");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link XmlFileConfigurationStrategy#XmlFileConfigurationStrategy(InputStream,
     * boolean)}. Failure case 2, call with a closed stream.
     *
     * @throws Exception if error occurs
     */
    public void testXmlFileConfigurationStrategyInputStreamBooleanFailure2()
        throws Exception {
        InputStream inputStream = new FileInputStream(TestHelper.getFile(FILE_NAME));
        inputStream.close();

        try {
            new XmlFileConfigurationStrategy(inputStream);
            fail("exception has not been thrown");
        } catch (ConfigurationException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link XmlFileConfigurationStrategy#XmlFileConfigurationStrategy(InputStream,
     * boolean)}. Failure case 3, use wrong xml file.
     *
     * @throws Exception if error occurs
     */
    public void testXmlFileConfigurationStrategyInputStreamBooleanFailure3()
        throws Exception {
        InputStream inputStream = new FileInputStream(TestHelper.getFile(INVALID_FILE_1));

        try {
            new XmlFileConfigurationStrategy(inputStream);
            fail("exception has not been thrown");
        } catch (ConfigurationException e) {
            // should land here
        } finally {
            inputStream.close();
        }
    }

    /**
     * Test method for {@link XmlFileConfigurationStrategy#getProperty(String)}. Accuracy case, check the
     * result.
     *
     * @throws Exception if error occurs
     */
    public void testGetPropertyAccuracy() throws Exception {
        XmlFileConfigurationStrategy strategy = new XmlFileConfigurationStrategy(TestHelper.getFile(FILE_NAME));
        assertEquals("wrong result", "This is the property 1 value", strategy.getProperty("my.property.1"));
        assertEquals("wrong result", "This is the property 2 value", strategy.getProperty("my.property.2"));
        assertEquals("wrong result", "This is the property 3 value", strategy.getProperty("my.property.3"));
        assertNull("wrong result", strategy.getProperty("my.property.4"));
    }

    /**
     * Test method for {@link XmlFileConfigurationStrategy#getProperty(String)}. Failure case 1, call with
     * empty string.
     *
     * @throws Exception if error occurs
     */
    public void testGetPropertyFailure1() throws Exception {
        try {
            new XmlFileConfigurationStrategy(TestHelper.getFile(FILE_NAME)).getProperty(" ");
            fail("exception has not been thrown");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link XmlFileConfigurationStrategy#getProperty(String)}. Failure case 2, use xml file
     * with wrong document element.
     *
     * @throws Exception if error occurs
     */
    public void testGetPropertyFailure2() throws Exception {
        try {
            new XmlFileConfigurationStrategy(TestHelper.getFile(INVALID_FILE_2)).getProperty(TestHelper
                .generateString());
            fail("exception has not been thrown");
        } catch (ConfigurationException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link XmlFileConfigurationStrategy#getProperty(String)}. Failure case 3, use xml file
     * contains property element without name.
     *
     * @throws Exception if error occurs
     */
    public void testGetPropertyFailure3() throws Exception {
        try {
            new XmlFileConfigurationStrategy(TestHelper.getFile(INVALID_FILE_3)).getProperty(TestHelper
                .generateString());
            fail("exception has not been thrown");
        } catch (ConfigurationException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link XmlFileConfigurationStrategy#getProperty(String)}. Failure case 4, use xml file
     * contains property element with non-text child.
     *
     * @throws Exception if error occurs
     */
    public void testGetPropertyFailure4() throws Exception {
        try {
            new XmlFileConfigurationStrategy(TestHelper.getFile(INVALID_FILE_4)).getProperty("my.property.2");
            fail("exception has not been thrown");
        } catch (ConfigurationException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link XmlFileConfigurationStrategy#getProperty(String)}. Failure case 5, use xml file
     * contains property element without child.
     *
     * @throws Exception if error occurs
     */
    public void testGetPropertyFailure5() throws Exception {
        try {
            new XmlFileConfigurationStrategy(TestHelper.getFile(INVALID_FILE_5)).getProperty("my.property.2");
            fail("exception has not been thrown");
        } catch (ConfigurationException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link XmlFileConfigurationStrategy#setProperty(String, String)}. Accuracy case, check
     * the result.
     *
     * @throws Exception if error occurs
     */
    public void testSetPropertyAccuracy() throws Exception {
        XmlFileConfigurationStrategy strategy = new XmlFileConfigurationStrategy(TestHelper.getFile(FILE_NAME));
        String value = TestHelper.generateString();
        strategy.setProperty("my.property.4", value);
        assertEquals("wrong result", value, strategy.getProperty("my.property.4"));

        String newValue = TestHelper.generateString();
        strategy.setProperty("my.property.4", newValue);
        assertEquals("wrong result", newValue, strategy.getProperty("my.property.4"));

        assertNull("should not commit to file",
            new XmlFileConfigurationStrategy(TestHelper.getFile(FILE_NAME)).getProperty("my.property.4"));
    }

    /**
     * Test method for {@link XmlFileConfigurationStrategy#setProperty(String, String)}. Failure case 1, call
     * with empty name.
     *
     * @throws Exception if error occurs
     */
    public void testSetPropertyFailure1() throws Exception {
        try {
            new XmlFileConfigurationStrategy(TestHelper.getFile(FILE_NAME)).setProperty(" ",
                TestHelper.generateString());
            fail("exception has not been thrown");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link XmlFileConfigurationStrategy#setProperty(String, String)}. Failure case 2, call
     * with empty value.
     *
     * @throws Exception if error occurs
     */
    public void testSetPropertyFailure2() throws Exception {
        try {
            new XmlFileConfigurationStrategy(TestHelper.getFile(FILE_NAME)).setProperty(TestHelper
                .generateString(), " ");
            fail("exception has not been thrown");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link XmlFileConfigurationStrategy#setProperty(String, String)}. Failure case 3, use xml file
     * with wrong document element.
     *
     * @throws Exception if error occurs
     */
    public void testSetPropertyFailure3() throws Exception {
        try {
            new XmlFileConfigurationStrategy(TestHelper.getFile(INVALID_FILE_2)).setProperty(TestHelper
                .generateString(), TestHelper.generateString());
            fail("exception has not been thrown");
        } catch (ConfigurationException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link XmlFileConfigurationStrategy#setProperty(String, String)}. Failure case 4, use xml file
     * contains property element without name.
     *
     * @throws Exception if error occurs
     */
    public void testSetPropertyFailure4() throws Exception {
        try {
            new XmlFileConfigurationStrategy(TestHelper.getFile(INVALID_FILE_3)).setProperty(TestHelper
                .generateString(), TestHelper.generateString());
            fail("exception has not been thrown");
        } catch (ConfigurationException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link XmlFileConfigurationStrategy#setProperty(String, String)}. Failure case 5, use xml file
     * contains property element with non-text child.
     *
     * @throws Exception if error occurs
     */
    public void testSetPropertyFailure5() throws Exception {
        try {
            new XmlFileConfigurationStrategy(TestHelper.getFile(INVALID_FILE_4)).setProperty("my.property.2",
                TestHelper.generateString());
            fail("exception has not been thrown");
        } catch (ConfigurationException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link XmlFileConfigurationStrategy#setProperty(String, String)}. Failure case 6, use xml file
     * contains property element without child.
     *
     * @throws Exception if error occurs
     */
    public void testSetPropertyFailure6() throws Exception {
        try {
            new XmlFileConfigurationStrategy(TestHelper.getFile(INVALID_FILE_5)).setProperty("my.property.2",
                TestHelper.generateString());
            fail("exception has not been thrown");
        } catch (ConfigurationException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link XmlFileConfigurationStrategy#commitChanges()}. Accuracy case 1, check the
     * result.
     *
     * @throws Exception if error occurs
     */
    public void testCommitChangesAccuracy1() throws Exception {
        XmlFileConfigurationStrategy strategy = new XmlFileConfigurationStrategy(TestHelper.getFile(FILE_NAME));
        String value = TestHelper.generateString();
        strategy.setProperty("my.property.5", value);
        strategy.commitChanges();

        assertEquals("wrong result", value,
            new XmlFileConfigurationStrategy(TestHelper.getFile(FILE_NAME)).getProperty("my.property.5"));
    }

    /**
     * Test method for {@link XmlFileConfigurationStrategy#commitChanges()}. Accuracy case 2, check it does
     * not actually write the file when there's no change at all.
     *
     * @throws Exception if error occurs
     */
    public void testCommitChangesAccuracy2() throws Exception {
        File file = TestHelper.getFile(FILE_NAME);
        XmlFileConfigurationStrategy strategy = new XmlFileConfigurationStrategy(file);

        // lock the file, so if it attempts to write the file ConfigurationException will be thrown
        FileInputStream inputStream = new FileInputStream(file);
        FileLock lock = inputStream.getChannel().lock(0, Long.MAX_VALUE, true);

        try {
            strategy.commitChanges();
        } finally {
            lock.release();
            inputStream.close();
        }
    }

    /**
     * Test method for {@link XmlFileConfigurationStrategy#commitChanges()}. Failure case 1, use instance
     * constructed with input stream.
     *
     * @throws Exception if error occurs
     */
    public void testCommitChangesFailure1() throws Exception {
        InputStream inputStream = new FileInputStream(TestHelper.getFile(FILE_NAME));

        try {
            XmlFileConfigurationStrategy strategy = new XmlFileConfigurationStrategy(inputStream);
            strategy.setProperty("my.property.5", TestHelper.generateString());

            try {
                strategy.commitChanges();
                fail("exception has not been thrown");
            } catch (IllegalStateException e) {
                // should land here
            }
        } finally {
            inputStream.close();
        }
    }

    /**
     * Test method for {@link XmlFileConfigurationStrategy#commitChanges()}. Failure case 2, lock the file
     * first.
     *
     * @throws Exception if error occurs
     */
    public void testCommitChangesFailure2() throws Exception {
        File file = TestHelper.getFile(FILE_NAME);
        XmlFileConfigurationStrategy strategy = new XmlFileConfigurationStrategy(file);
        strategy.setProperty("my.property.5", TestHelper.generateString());

        // In my OS the file will be cleared when there's an attempt to write it after locked, the error message will
        // also be printed to standard error output stream. I've no idea how this happens, but since it has nothing to
        // do with the functionality of main source and this test case, I just save the file's content and replace
        // System.err before testing, and recover them after that.
        FileInputStream inputStream = new FileInputStream(file);
        ByteArrayOutputStream tempStream = new ByteArrayOutputStream();
        int readByte;

        while ((readByte = inputStream.read()) != -1) {
            tempStream.write(readByte);
        }

        PrintStream oldErr = System.err;
        System.setErr(new PrintStream(new ByteArrayOutputStream()));

        FileLock lock = inputStream.getChannel().lock(0, Long.MAX_VALUE, true);

        try {
            strategy.commitChanges();
            fail("exception has not been thrown");
        } catch (ConfigurationException e) {
            // should land here
        } finally {
            lock.release();
            System.setErr(oldErr);
            inputStream.close();

            OutputStream outputStream = new FileOutputStream(file);
            tempStream.writeTo(outputStream);
            outputStream.flush();
            outputStream.close();
        }
    }
}
