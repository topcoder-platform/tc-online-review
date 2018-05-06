/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 *
 * TCS Project JNDI Context Utility 2.0 (Unit Tests)
 *
 * @ JNDIUtilTest.java
 */
package com.topcoder.naming.jndiutility.failuretests;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import javax.naming.Name;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import junit.framework.TestCase;

import com.topcoder.naming.jndiutility.ConfigurationException;
import com.topcoder.naming.jndiutility.ContextRenderer;
import com.topcoder.naming.jndiutility.ContextXMLRenderer;
import com.topcoder.naming.jndiutility.JNDIUtil;
import com.topcoder.util.config.ConfigManager;

/**
 * Failure test class for JNDIUtil.
 * 
 * @author kaqi072821
 * @version 2.0
 */
public class JNDIUtilFailureTest extends TestCase {
    /** Namespace used in ConfigManager. */
    private static final String NAMESPACE = "com.topcoder.naming.jndiutility.JNDIUtilTest.failure";

    /** Name of configuration file used as source of ConfigManager. */
    private static final String CONFIG_FILE = "failuretests/JNDIUtilTest.xml";

    /** Name of xml configuration file used in tests. */
    private static final String XML_FILE = "failuretests/JNDIUtilTestXML.xml";

    /** Root directory of context used in tests. */
    private static final String TEST_ROOT = "test_root";

    /** JNDIUtil instance used in most test cases. */
    private JNDIUtil util;

    /**
     * Set up method. Initializes the field.
     * 
     * @throws Exception if error occurs
     * 
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        util = new JNDIUtil(FailureTestHelper.getFile(XML_FILE));
    }

    /**
     * Tear down method. Clear configurations and reset the field to null.
     * 
     * @throws Exception if error occurs
     * 
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        FailureTestHelper.clearConfig();
        util = null;
    }

    /**
     * Test method for {@link JNDIUtil#JNDIUtil(String)}. Failure case , call with empty string.
     * 
     * @throws Exception if error occurs
     */
    public void testJNDIUtilString_EmptyNamespace() throws Exception {
        try {
            new JNDIUtil(" ");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Test method for {@link JNDIUtil#JNDIUtil(String)}. Failure case , call with null string.
     * 
     * @throws Exception if error occurs
     */
    public void testJNDIUtilString_NullNamespace() throws Exception {
        try {
            new JNDIUtil((String) null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Test method for {@link JNDIUtil#JNDIUtil(String)}. Failure case 2, call without loading configuration
     * (inexistent namespace).
     * 
     * @throws Exception if error occurs
     */
    public void testJNDIUtilString_NonExistNamespace() throws Exception {
        try {
            new JNDIUtil("non exist namespace");
            fail("IllegalArgumentException expected");
        } catch (ConfigurationException e) {
            // pass
        }
    }

    /**
     * Test method for {@link JNDIUtil#JNDIUtil(String)}. Failure case , use configuration with inexistent url.
     * 
     * @throws Exception if error occurs
     */
    public void testJNDIUtilString_notExistURL() throws Exception {
        FailureTestHelper.loadConfig(CONFIG_FILE);

        try {
            new JNDIUtil(NAMESPACE + ".Invalid1");
            fail("ConfigurationException expected");
        } catch (ConfigurationException e) {
            // pass
        }
    }

    /**
     * Test method for {@link JNDIUtil#JNDIUtil(InputStream, boolean)}. Failure case 1, call with null.
     * 
     * @throws Exception if error occurs
     */
    public void testJNDIUtilInputStreamBoolean_InputStreamNull() throws Exception {
        try {
            new JNDIUtil((InputStream) null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Test method for {@link JNDIUtil#JNDIUtil(InputStream, boolean)}. Failure case 2, use configuration with wrong
     * factory class.
     * 
     * @throws Exception if error occurs
     */
    public void testJNDIUtilInputStreamBoolean_WrongFactory() throws Exception {
        try {
            new JNDIUtil(new FileInputStream(FailureTestHelper.getFile("failuretests/JNDIUtilTestXML.invalid1.xml")));
            fail("ConfigurationException expected");
        } catch (ConfigurationException e) {
            // pass
        }
    }

    /**
     * Test method for {@link JNDIUtil#JNDIUtil(File, boolean)}. Failure case 1, call with null.
     * 
     * @throws Exception if error occurs
     */
    public void testJNDIUtilFileBooleanFailure_NullFile() throws Exception {
        try {
            new JNDIUtil((File) null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Test method for {@link JNDIUtil#JNDIUtil(File, boolean)}. Failure case 2, call with inexistent file.
     * 
     * @throws Exception if error occurs
     */
    public void testJNDIUtilFileBooleanFailure_inexistent_file() throws Exception {
        try {
            new JNDIUtil(FailureTestHelper.getFile("inexistent_file"));
            fail("ConfigurationException expected");
        } catch (ConfigurationException e) {
            // pass
        }
    }

    /**
     * Test method for {@link JNDIUtil#JNDIUtil(File, boolean)}. Failure case 3, use configuration without factory
     * class.
     * 
     * @throws Exception if error occurs
     */
    public void testJNDIUtilFileBoolean_NoFactory() throws Exception {
        try {
            new JNDIUtil(FailureTestHelper.getFile("JNDIUtilTestXML.invalid2.xml"));
            fail("ConfigurationException expected");
        } catch (ConfigurationException e) {
            // pass
        }
    }

    /**
     * Test method for {@link JNDIUtil#JNDIUtil(File, boolean)}. Failure case 4, use configuration with empty url .
     * 
     * @throws Exception if error occurs
     */
    public void testJNDIUtilFileBoolean_EmptyURL() throws Exception {
        try {
            new JNDIUtil(FailureTestHelper.getFile("JNDIUtilTestXML.invalid3.xml"));
            fail("ConfigurationException expected");
        } catch (ConfigurationException e) {
            // pass
        }
    }

    /**
     * Test method for {@link JNDIUtil#getContext(String)}. Failure case, call with empty string.
     * 
     * @throws Exception if error occurs
     */
    public void testGetContext_emptyName() throws Exception {
        try {
            util.getContext(" ");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Test method for {@link JNDIUtil#getContext(String)}. Failure case, call with null string.
     * 
     * @throws Exception if error occurs
     */
    public void testGetContext_NullName() throws Exception {
        try {
            util.getContext(null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Test method for {@link JNDIUtil#createName(String)}. Failure case, call with null.
     * 
     * @throws Exception if error occurs
     */
    public void testCreateNameFailure() throws Exception {
        try {
            util.createName(null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Test method for {@link JNDIUtil#getObject(String)}. Failure case, call with null.
     * 
     * @throws Exception if error occurs
     */
    public void testGetObjectStringFailure() throws Exception {
        try {
            util.getObject((String) null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Test method for {@link JNDIUtil#getObject(Name)}. Failure case, call with inexistent name.
     * 
     * @throws Exception if error occurs
     */
    public void testGetObjectNameFailure() throws Exception {
        try {
            util.getObject(util.createName("inexistent file"));
            fail("IllegalArgumentException expected");
        } catch (NamingException e) {
            // pass
        }
    }

    /**
     * Test method for {@link JNDIUtil#getObject(Name)}. Failure case, call with null name.
     * 
     * @throws Exception if error occurs
     */
    public void testGetObjectNameNull() throws Exception {
        try {
            util.getObject((Name) null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Test method for {@link JNDIUtil#getObject(String, Class)}. Failure case, call with null class.
     * 
     * @throws Exception if error occurs
     */
    public void testGetObjectStringClassFailure() throws Exception {
        try {
            util.getObject(TEST_ROOT + File.separator + "dir", null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Test method for {@link JNDIUtil#getObject(String, Class)}. Failure case, call with null name.
     * 
     * @throws Exception if error occurs
     */
    public void testGetObjectStringClassFailure2() throws Exception {
        try {
            util.getObject((String) null, File.class);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Test method for {@link JNDIUtil#getObject(Name, Class)}. Failure case, call with incompatible class.
     * 
     * @throws Exception if error occurs
     */
    public void testGetObjectNameClassFailure() throws Exception {
        try {
            util.getObject(util.createName("dir"), File.class);
            fail("ClassCastException expected");
        } catch (ClassCastException e) {
            // pass
        }
    }

    /**
     * Test method for {@link JNDIUtil#getObject(Name, Class)}. Failure case, call with null class.
     * 
     * @throws Exception if error occurs
     */
    public void testGetObjectNameClass_NullClass() throws Exception {
        try {
            util.getObject(util.createName("dir/dirfile.txt"), null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Test method for {@link JNDIUtil#getObject(Name, Class)}. Failure case, call with null class.
     * 
     * @throws Exception if error occurs
     */
    public void testGetObjectNameClass_NullName() throws Exception {
        try {
            util.getObject((Name) null, File.class);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Test method for {@link JNDIUtil#saveContextConfig(String, Properties)}. Failure case , call with empty name.
     * 
     * @throws Exception if error occurs
     */
    public void testSaveContextConfigFailure1() throws Exception {
        try {
            util.saveContextConfig(" ", new Properties());
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Test method for {@link JNDIUtil#saveContextConfig(String, Properties)}. Failure case , call with empty name.
     * 
     * @throws Exception if error occurs
     */
    public void testSaveContextConfig_nullName() throws Exception {
        try {
            util.saveContextConfig(null, new Properties());
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Test method for {@link JNDIUtil#saveContextConfig(String, Properties)}. Failure case 2, call with null
     * properties.
     * 
     * @throws Exception if error occurs
     */
    public void testSaveContextConfigFailure2() throws Exception {
        try {
            util.saveContextConfig(FailureTestHelper.generateString(), null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

//    /**
//     * Test method for {@link JNDIUtil#saveContextConfig(String, Properties)}. Failure case 3, call with properties
//     * contains empty value.
//     * 
//     * @throws Exception if error occurs
//     */
//    public void testSaveContextConfigFailure3() throws Exception {
//        Properties props = new Properties();
//        props.setProperty(FailureTestHelper.generateString(), FailureTestHelper.generateString());
//        props.setProperty(FailureTestHelper.generateString(), " ");
//
//        try {
//            util.saveContextConfig(FailureTestHelper.generateString(), props);
//            fail("IllegalArgumentException expected");
//        } catch (IllegalArgumentException e) {
//            // pass
//        }
//    }

    /**
     * Test method for {@link JNDIUtil#saveContextConfig(String, Properties)}. Failure case 4, lock the namespace
     * first.
     * 
     * @throws Exception if error occurs
     */
    public void testSaveContextConfigFailure4() throws Exception {
        FailureTestHelper.loadConfig(CONFIG_FILE);
        util = new JNDIUtil(NAMESPACE);

        Properties props = new Properties();
        props.setProperty(FailureTestHelper.generateString(), FailureTestHelper.generateString());
        props.setProperty(FailureTestHelper.generateString(), FailureTestHelper.generateString());
        ConfigManager.getInstance().lock(NAMESPACE, "test");

        try {
            util.saveContextConfig(FailureTestHelper.generateString(), props);
            fail("ConfigurationException expected");
        } catch (ConfigurationException e) {
            // pass
        } finally {
            ConfigManager.getInstance().forceUnlock(NAMESPACE);
        }
    }

    /**
     * Test method for {@link JNDIUtil#createSubcontext(String)}. Failure case, call with zero-length string.
     * 
     * @throws Exception if error occurs
     */
    public void testCreateSubcontextStringFailure() throws Exception {
        try {
            util.createSubcontext("");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Test method for {@link JNDIUtil#createSubcontext(String)}. Failure case, call with null string.
     * 
     * @throws Exception if error occurs
     */
    public void testCreateSubcontextStringFailure2() throws Exception {
        try {
            util.createSubcontext((String) null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Test method for {@link JNDIUtil#createSubcontext(Name)}. Failure case 1, call with an exist file (not a context)
     * 
     * @throws Exception if error occurs
     */
    public void testCreateSubcontextNameFailure1() throws Exception {
        try {
            util.createSubcontext(util.createName("file.txt"));
            fail("NameAlreadyBoundException expected");
        } catch (NameAlreadyBoundException e) {
            // pass
        }
    }

    /**
     * Test method for {@link JNDIUtil#createSubcontext(Name)}. Failure case 2, call with name begin with an exist
     * file.
     * 
     * @throws Exception if error occurs
     */
    public void testCreateSubcontextNameFailure2() throws Exception {
        try {
            util.createSubcontext(util.createName("file.txt" + File.separator + FailureTestHelper.generateString()));
            fail("NameAlreadyBoundException expected");
        } catch (NameAlreadyBoundException e) {
            // pass
        }
    }

    /**
     * Test method for {@link JNDIUtil#getTopic(String)}. Failure case, call with an inexistent name.
     * 
     * @throws Exception if error occurs
     */
    public void testGetTopicStringFailure() throws Exception {
        try {
            util.getTopic("inexistent topic");
            fail("NameNotFoundException expected");
        } catch (NameNotFoundException e) {
            // pass
        }
    }

    /**
     * Test method for {@link JNDIUtil#getTopic(String)}. Failure case, call with an inexistent name.
     * 
     * @throws Exception if error occurs
     */
    public void testGetTopicString_nullName() throws Exception {
        try {
            util.getTopic((String) null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Test method for {@link JNDIUtil#getTopic(Name)}. Failure case, call with an exist context.
     * 
     * @throws Exception if error occurs
     */
    public void testGetTopicNameFailure() throws Exception {
        try {
            util.getTopic(util.createName("dir"));
            fail("NamingException expected");
        } catch (NamingException e) {
            // pass
        }
    }

    /**
     * Test method for {@link JNDIUtil#getTopic(Name)}. Failure case, call with an exist context.
     * 
     * @throws Exception if error occurs
     */
    public void testGetTopicName_NameNull() throws Exception {
        try {
            util.getTopic((Name) null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Test method for {@link JNDIUtil#getQueue(String)}. Failure case, call with null.
     * 
     * @throws Exception if error occurs
     */
    public void testGetQueueStringFailure() throws Exception {
        try {
            util.getQueue((String) null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // pass;
        }
    }

    /**
     * Test method for {@link JNDIUtil#getQueue(Name)}. Failure case, call with an exist context.
     * 
     * @throws Exception if error occurs
     */
    public void testGetQueueNameFailure() throws Exception {
        try {
            util.getQueue(util.createName("dir"));
            fail("IllegalArgumentException expected");
        } catch (NamingException e) {
            // pass
        }
    }

    /**
     * Test method for {@link JNDIUtil#getQueue(Name)}. Failure case, call with a null context.
     * 
     * @throws Exception if error occurs
     */
    public void testGetQueueName_null() throws Exception {
        try {
            util.getQueue((Name) (null));
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Test method for {@link JNDIUtil#getConnection(Name)}. Failure case, call with an existent context.
     * 
     * @throws Exception if error occurs
     */
    public void testGetConnectionNameFailure() throws Exception {
        try {
            util.getConnection(util.createName("dir"));
            fail("IllegalArgumentException expected");
        } catch (NamingException e) {
            // pass
        }
    }

    /**
     * Test method for {@link JNDIUtil#getConnection(Name)}. Failure case, call with a null context.
     * 
     * @throws Exception if error occurs
     */
    public void testGetConnectionNameNull() throws Exception {
        try {
            util.getConnection((Name) null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Test method for {@link JNDIUtil#getConnection(String)}. Failure case, call with an inexistent name.
     * 
     * @throws Exception if error occurs
     */
    public void testGetConnectionString() throws Exception {
        try {
            util.getConnection("inexistent data source");
            fail("IllegalArgumentException expected");
        } catch (NameNotFoundException e) {
            // pass
        }
    }

    /**
     * Test method for {@link JNDIUtil#getConnection(String)}. Failure case, call with a null name.
     * 
     * @throws Exception if error occurs
     */
    public void testGetConnectionString_Null() throws Exception {
        try {
            util.getConnection((String) null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Test method for {@link JNDIUtil#dump(ContextRenderer, boolean)}. Failure case, call with null renderer.
     * 
     * @throws Exception if error occurs
     */
    public void testDumpContextRendererBooleanFailure() throws Exception {
        try {
            util.dump(null, false);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Test method for {@link JNDIUtil#dump(Name, ContextRenderer, boolean)}. Failure case, call with file name.
     * 
     * @throws Exception if error occurs
     */
    public void testDumpNameContextRendererBooleanFailure() throws Exception {
        try {
            util.dump(util.createName("file.txt"), new ContextXMLRenderer(), false);
            fail("NamingException expected");
        } catch (NamingException e) {
            // pass
        }
    }

    /**
     * Test method for {@link JNDIUtil#dump(Name, ContextRenderer, boolean)}. Failure case, call with null Name.
     * 
     * @throws Exception if error occurs
     */
    public void testDumpNameContextRendererBooleanFailure2() throws Exception {
        try {
            util.dump((Name) null, new ContextXMLRenderer(), false);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Test method for {@link JNDIUtil#dump(Name, ContextRenderer, boolean)}. Failure case, call with null
     * ContextRenderer.
     * 
     * @throws Exception if error occurs
     */
    public void testDumpNameContextRendererBooleanFailure3() throws Exception {
        try {
            util.dump(util.createName("dir"), null, false);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Test method for {@link JNDIUtil#dump(String, ContextRenderer, boolean)}. Failure case, call with file name.
     * 
     * @throws Exception if error occurs
     */
    public void testDumpStringContextRendererBooleanFailure() throws Exception {
        try {
            util.dump("dir" + File.separator + "dirfile.txt", new ContextXMLRenderer(), false);
            fail("IllegalArgumentException expected");
        } catch (NamingException e) {
            // pass
        }
    }

    /**
     * Test method for {@link JNDIUtil#dump(String, ContextRenderer, boolean)}. Failure case, call with null file name.
     * 
     * @throws Exception if error occurs
     */
    public void testDumpStringContextRendererBooleanFailure2() throws Exception {
        try {
            util.dump((String) null, new ContextXMLRenderer(), false);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Test method for {@link JNDIUtil#dump(String, ContextRenderer, boolean)}. Failure case, call with null file name.
     * 
     * @throws Exception if error occurs
     */
    public void testDumpStringContextRendererBooleanFailure3() throws Exception {
        try {
            util.dump("", null, false);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
}
