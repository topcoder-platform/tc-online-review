/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.naming.jndiutility.failuretests;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import junit.framework.TestCase;

import com.topcoder.naming.jndiutility.ConfigurationException;
import com.topcoder.naming.jndiutility.configstrategy.XmlFileConfigurationStrategy;

/**
 * Junit test class for XmlFileConfigurationStrategy.
 * 
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class XmlFileConfigurationStrategyTest extends TestCase {
    /** XML file used in tests. */
    private static final String FILE_NAME = "failuretests/XmlFileConfigurationStrategyTest.xml";

    /** First invalid XML file used in tests. Wrong XML syntax. */
    private static final String INVALID_FILE_1 = "failuretests/XmlFileConfigurationStrategyTest.invalid1.xml";

    /**
     * Test method for {@link XmlFileConfigurationStrategy#XmlFileConfigurationStrategy(File, boolean)}. Failure case ,
     * call with null.
     * 
     * @throws Exception if error occurs
     */
    public void testXmlFileConfigurationStrategyFileBoolean_fileNull() throws Exception {
        try {
            new XmlFileConfigurationStrategy((File) null);
            fail("exception has not been thrown");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Test method for {@link XmlFileConfigurationStrategy#XmlFileConfigurationStrategy(File, boolean)}. Failure case ,
     * call with inexistent file.
     * 
     * @throws Exception if error occurs
     */
    public void testXmlFileConfigurationStrategyFileBoolean_notexitFile() throws Exception {
        try {
            new XmlFileConfigurationStrategy(FailureTestHelper.getFile("inexistent file"));
            fail("ConfigurationException expected");
        } catch (ConfigurationException e) {
            // pass
        }
    }

    /**
     * Test method for {@link XmlFileConfigurationStrategy#XmlFileConfigurationStrategy(File, boolean)}. Failure case ,
     * use wrong xml file( the end tag is wrong).
     * 
     * @throws Exception if error occurs
     */
    public void testXmlFileConfigurationStrategyFileBoolean_xmlFileWrong() throws Exception {
        try {
            new XmlFileConfigurationStrategy(FailureTestHelper.getFile(INVALID_FILE_1));
            fail("ConfigurationException expected");
        } catch (ConfigurationException e) {
            // pass
        }
    }

    /**
     * Test method for {@link XmlFileConfigurationStrategy#XmlFileConfigurationStrategy(InputStream, boolean)}. Failure
     * case , call with null.
     * 
     * @throws Exception if error occurs
     */
    public void testXmlFileConfigurationStrategyInputStreamBoolean_InputStreamNull() throws Exception {
        try {
            new XmlFileConfigurationStrategy((InputStream) null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Test method for {@link XmlFileConfigurationStrategy#XmlFileConfigurationStrategy(InputStream, boolean)}. Failure
     * case , call with a closed stream.
     * 
     * @throws Exception if error occurs
     */
    public void testXmlFileConfigurationStrategyInputStreamBoolean_InputStreamClosed() throws Exception {
        InputStream inputStream = new FileInputStream(FailureTestHelper.getFile(FILE_NAME));
        inputStream.close();

        try {
            new XmlFileConfigurationStrategy(inputStream);
            fail("ConfigurationException expected");
        } catch (ConfigurationException e) {
            // pass
        }
    }

    /**
     * Test method for {@link XmlFileConfigurationStrategy#XmlFileConfigurationStrategy(InputStream, boolean)}. Failure
     * case 3, use wrong xml file.
     * 
     * @throws Exception if error occurs
     */
    public void testXmlFileConfigurationStrategyInputStreamBoolean_wrongInputStream() throws Exception {
        InputStream inputStream = new FileInputStream(FailureTestHelper.getFile(INVALID_FILE_1));

        try {
            new XmlFileConfigurationStrategy(inputStream);
            fail("ConfigurationException expected");
        } catch (ConfigurationException e) {
            // pass
        } finally {
            inputStream.close();
        }
    }

    /**
     * Test method for {@link XmlFileConfigurationStrategy#getProperty(String)}. Failure case , call with empty string.
     * 
     * @throws Exception if error occurs
     */
    public void testGetPropertyFailure_emptyName() throws Exception {
        try {
            new XmlFileConfigurationStrategy(FailureTestHelper.getFile(FILE_NAME)).getProperty(" ");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Test method for {@link XmlFileConfigurationStrategy#getProperty(String)}. Failure case , call with empty string.
     * 
     * @throws Exception if error occurs
     */
    public void testGetPropertyFailure_NullName() throws Exception {
        try {
            new XmlFileConfigurationStrategy(FailureTestHelper.getFile(FILE_NAME)).getProperty(null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
//
//    /**
//     * Test method for {@link XmlFileConfigurationStrategy#getProperty(String)}. Failure case, use xml file with wrong
//     * document element.
//     * 
//     * @throws Exception if error occurs
//     */
//    public void testGetPropertyFailure_NoProperties() throws Exception {
//        try {
//            new XmlFileConfigurationStrategy(FailureTestHelper.getFile(INVALID_FILE_2), false)
//                .getProperty(FailureTestHelper.generateString());
//            fail("ConfigurationException expected");
//        } catch (ConfigurationException e) {
//            // pass
//        }
//    }
//
//    /**
//     * Test method for {@link XmlFileConfigurationStrategy#getProperty(String)}. Failure case 3, use xml file contains
//     * property element without name.
//     * 
//     * @throws Exception if error occurs
//     */
//    public void testGetPropertyFailure_MissingName() throws Exception {
//        try {
//            new XmlFileConfigurationStrategy(FailureTestHelper.getFile(INVALID_FILE_3), false)
//                .getProperty(FailureTestHelper.generateString());
//            fail("ConfigurationException expected");
//        } catch (ConfigurationException e) {
//            // pass
//        }
//    }
//
//    /**
//     * Test method for {@link XmlFileConfigurationStrategy#getProperty(String)}. Failure case 4, use xml file contains
//     * property element with non-text child.
//     * 
//     * @throws Exception if error occurs
//     */
//    public void testGetPropertyFailure_NonTextChild() throws Exception {
//        try {
//            new XmlFileConfigurationStrategy(FailureTestHelper.getFile(INVALID_FILE_4), false)
//                .getProperty("my.property.2");
//            fail("IllegalArgumentException expected");
//        } catch (ConfigurationException e) {
//            // pass
//        }
//    }
//
//    /**
//     * Test method for {@link XmlFileConfigurationStrategy#getProperty(String)}. Failure case 5, use xml file contains
//     * property element without child.
//     * 
//     * @throws Exception if error occurs
//     */
//    public void testGetPropertyFailure_MissingValue() throws Exception {
//        try {
//            new XmlFileConfigurationStrategy(FailureTestHelper.getFile(INVALID_FILE_5), false)
//                .getProperty("my.property.2");
//            fail("IllegalArgumentException expected");
//        } catch (ConfigurationException e) {
//            // pass
//        }
//    }

    /**
     * Test method for {@link XmlFileConfigurationStrategy#setProperty(String, String)}. Failure case 1, call with
     * empty name.
     * 
     * @throws Exception if error occurs
     */
    public void testSetPropertyFailure_EmptyName() throws Exception {
        try {
            new XmlFileConfigurationStrategy(FailureTestHelper.getFile(FILE_NAME)).setProperty(" ",
                FailureTestHelper.generateString());
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Test method for {@link XmlFileConfigurationStrategy#setProperty(String, String)}. Failure case 1, call with
     * empty name.
     * 
     * @throws Exception if error occurs
     */
    public void testSetPropertyFailure_NullName() throws Exception {
        try {
            new XmlFileConfigurationStrategy(FailureTestHelper.getFile(FILE_NAME)).setProperty(null,
                FailureTestHelper.generateString());
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

//    /**
//     * Test method for {@link XmlFileConfigurationStrategy#setProperty(String, String)}. Failure case , call with
//     * empty value.
//     * 
//     * @throws Exception if error occurs
//     */
//    public void testSetPropertyFailure_EmptyValue() throws Exception {
//        try {
//            new XmlFileConfigurationStrategy(FailureTestHelper.getFile(FILE_NAME), false).setProperty(FailureTestHelper
//                .generateString(), " ");
//            fail("IllegalArgumentException expected");
//        } catch (IllegalArgumentException e) {
//            // pass
//        }
//    }
//
//    /**
//     * Test method for {@link XmlFileConfigurationStrategy#setProperty(String, String)}. Failure case 2, call with null
//     * value.
//     * 
//     * @throws Exception if error occurs
//     */
//    public void testSetPropertyFailure_NullValue() throws Exception {
//        try {
//            new XmlFileConfigurationStrategy(FailureTestHelper.getFile(FILE_NAME), false).setProperty(FailureTestHelper
//                .generateString(), null);
//            fail("IllegalArgumentException expected");
//        } catch (IllegalArgumentException e) {
//            // pass
//        }
//    }
//
//    /**
//     * Test method for {@link XmlFileConfigurationStrategy#setProperty(String, String)}. Failure case 3, use xml file
//     * with wrong document element.
//     * 
//     * @throws Exception if error occurs
//     */
//    public void testSetPropertyFailure_NoProperties() throws Exception {
//        try {
//            new XmlFileConfigurationStrategy(FailureTestHelper.getFile(INVALID_FILE_2), false).setProperty(
//                FailureTestHelper.generateString(), FailureTestHelper.generateString());
//            fail("ConfigurationException expected");
//        } catch (ConfigurationException e) {
//            // pass
//        }
//    }
//
//    /**
//     * Test method for {@link XmlFileConfigurationStrategy#setProperty(String, String)}. Failure case 4, use xml file
//     * contains property element without name.
//     * 
//     * @throws Exception if error occurs
//     */
//    public void testSetPropertyFailure_MissingName() throws Exception {
//        try {
//            new XmlFileConfigurationStrategy(FailureTestHelper.getFile(INVALID_FILE_3), false).setProperty(
//                FailureTestHelper.generateString(), FailureTestHelper.generateString());
//            fail("ConfigurationException expected");
//        } catch (ConfigurationException e) {
//            // pass
//        }
//    }
//
//    /**
//     * Test method for {@link XmlFileConfigurationStrategy#setProperty(String, String)}. Failure case 5, use xml file
//     * contains property element with non-text child.
//     * 
//     * @throws Exception if error occurs
//     */
//    public void testSetPropertyFailure_NonTextValue() throws Exception {
//        try {
//            new XmlFileConfigurationStrategy(FailureTestHelper.getFile(INVALID_FILE_4), false).setProperty(
//                "my.property.2", FailureTestHelper.generateString());
//            fail("IllegalArgumentException expected");
//        } catch (ConfigurationException e) {
//            // pass
//        }
//    }
//
//    /**
//     * Test method for {@link XmlFileConfigurationStrategy#setProperty(String, String)}. Failure case 6, use xml file
//     * contains property element without child.
//     * 
//     * @throws Exception if error occurs
//     */
//    public void testSetPropertyFailure_ConfigEmptyValue() throws Exception {
//        try {
//            new XmlFileConfigurationStrategy(FailureTestHelper.getFile(INVALID_FILE_5), false).setProperty(
//                "my.property.2", FailureTestHelper.generateString());
//            fail("IllegalArgumentException expected");
//        } catch (ConfigurationException e) {
//            // pass
//        }
//    }

    /**
     * Test method for {@link XmlFileConfigurationStrategy#commitChanges()}. Failure case 1, use instance constructed
     * with input stream.
     * 
     * @throws Exception if error occurs
     */
    public void testCommitChangesFailure1() throws Exception {
        InputStream inputStream = new FileInputStream(FailureTestHelper.getFile(FILE_NAME));

        try {
            XmlFileConfigurationStrategy strategy = new XmlFileConfigurationStrategy(inputStream);
            strategy.setProperty("my.property.5", FailureTestHelper.generateString());

            try {
                strategy.commitChanges();
                fail("IllegalStateException expected");
            } catch (IllegalStateException e) {
                // pass
            }
        } finally {
            inputStream.close();
        }
    }
}
