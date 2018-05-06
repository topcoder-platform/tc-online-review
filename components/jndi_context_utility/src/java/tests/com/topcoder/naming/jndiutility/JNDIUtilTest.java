/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 *
 * TCS Project JNDI Context Utility 2.0 (Unit Tests)
 *
 * @ JNDIUtilTest.java
 */
package com.topcoder.naming.jndiutility;

import com.topcoder.naming.jndiutility.configstrategy.XmlFileConfigurationStrategy;

import com.topcoder.util.config.ConfigManager;

import junit.framework.TestCase;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import java.sql.Connection;

import java.util.Properties;

import javax.jms.Queue;
import javax.jms.Topic;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.Name;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;


/**
 * Junit test class for JNDIUtil.
 *
 * @author Charizard
 * @version 2.0
 */
public class JNDIUtilTest extends TestCase {
    /** Namespace used in ConfigManager. */
    private static final String NAMESPACE = "com.topcoder.naming.jndiutility.JNDIUtilTest";

    /** Name of configuration file used as source of ConfigManager. */
    private static final String CONFIG_FILE = "JNDIUtilTest.xml";

    /** Name of xml configuration file used in tests. */
    private static final String XML_FILE = "JNDIUtilTestXML.xml";

    /** Root directory of context used in tests. */
    private static final String TEST_ROOT = "test_root";

    /** Name to get the default context (the associated context of JNDIUtil). */
    private static final String DEFAULT_NAME = "default";

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
        util = new JNDIUtil(TestHelper.getFile(XML_FILE));
    }

    /**
     * Tear down method. Clear configurations and reset the field to null.
     *
     * @throws Exception if error occurs
     *
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        TestHelper.clearConfig();
        util = null;
    }

    /**
     * Test method for {@link JNDIUtil#JNDIUtil()}. Accuracy case, try to instantiate with it.
     *
     * @throws Exception if error occurs
     */
    public void testJNDIUtilAccuracy() throws Exception {
        assertNotNull("failed to instantiate", new JNDIUtil());
    }

    /**
     * Test method for {@link JNDIUtil#JNDIUtil(String)}. Accuracy case, try to instantiate with it.
     *
     * @throws Exception if error occurs
     */
    public void testJNDIUtilStringAccuracy() throws Exception {
        TestHelper.loadConfig(CONFIG_FILE);
        assertNotNull("failed to instantiate", new JNDIUtil(NAMESPACE));
    }

    /**
     * Test method for {@link JNDIUtil#JNDIUtil(String)}. Failure case 1, call with empty string.
     *
     * @throws Exception if error occurs
     */
    public void testJNDIUtilStringFailure1() throws Exception {
        try {
            new JNDIUtil(" ");
            fail("exception has not been thrown");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link JNDIUtil#JNDIUtil(String)}. Failure case 2, call without loading configuration
     * (inexistent namespace).
     *
     * @throws Exception if error occurs
     */
    public void testJNDIUtilStringFailure2() throws Exception {
        try {
            new JNDIUtil(NAMESPACE);
            fail("exception has not been thrown");
        } catch (ConfigurationException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link JNDIUtil#JNDIUtil(String)}. Failure case 3, use configuration with inexistent
     * url.
     *
     * @throws Exception if error occurs
     */
    public void testJNDIUtilStringFailure3() throws Exception {
        TestHelper.loadConfig(CONFIG_FILE);

        try {
            new JNDIUtil(NAMESPACE + ".Invalid1");
            fail("exception has not been thrown");
        } catch (ConfigurationException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link JNDIUtil#JNDIUtil(InputStream, boolean)}. Accuracy case, try to instantiate
     * with it.
     *
     * @throws Exception if error occurs
     */
    public void testJNDIUtilInputStreamBooleanAccuracy()
        throws Exception {
        assertNotNull("failed to instantiate", new JNDIUtil(new FileInputStream(TestHelper.getFile(XML_FILE))));
    }

    /**
     * Test method for {@link JNDIUtil#JNDIUtil(InputStream, boolean)}. Failure case 1, call with null.
     *
     * @throws Exception if error occurs
     */
    public void testJNDIUtilInputStreamBooleanFailure1()
        throws Exception {
        try {
            new JNDIUtil((InputStream) null);
            fail("exception has not been thrown");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link JNDIUtil#JNDIUtil(InputStream, boolean)}. Failure case 2, use configuration
     * with wrong factory class.
     *
     * @throws Exception if error occurs
     */
    public void testJNDIUtilInputStreamBooleanFailure2()
        throws Exception {
        try {
            new JNDIUtil(new FileInputStream(TestHelper.getFile("JNDIUtilTestXML.invalid1.xml")));
            fail("exception has not been thrown");
        } catch (ConfigurationException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link JNDIUtil#JNDIUtil(File, boolean)}. Accuracy case, try to instantiate without
     * url.
     *
     * @throws Exception if error occurs
     */
    public void testJNDIUtilFileBooleanAccuracy() throws Exception {
        assertNotNull("failed to instantiate", new JNDIUtil(TestHelper.getFile("JNDIUtilTestXML.noURL.xml")));
    }

    /**
     * Test method for {@link JNDIUtil#JNDIUtil(File, boolean)}. Failure case 1, call with null.
     *
     * @throws Exception if error occurs
     */
    public void testJNDIUtilFileBooleanFailure1() throws Exception {
        try {
            new JNDIUtil((File) null);
            fail("exception has not been thrown");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link JNDIUtil#JNDIUtil(File, boolean)}. Failure case 2, call with inexistent file.
     *
     * @throws Exception if error occurs
     */
    public void testJNDIUtilFileBooleanFailure2() throws Exception {
        try {
            new JNDIUtil(TestHelper.getFile("inexistent_file"));
            fail("exception has not been thrown");
        } catch (ConfigurationException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link JNDIUtil#JNDIUtil(File, boolean)}. Failure case 3, use configuration without
     * factory class.
     *
     * @throws Exception if error occurs
     */
    public void testJNDIUtilFileBooleanFailure3() throws Exception {
        try {
            new JNDIUtil(TestHelper.getFile("JNDIUtilTestXML.invalid2.xml"));
            fail("exception has not been thrown");
        } catch (ConfigurationException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link JNDIUtil#JNDIUtil(File, boolean)}. Failure case 4, use configuration with empty
     * url .
     *
     * @throws Exception if error occurs
     */
    public void testJNDIUtilFileBooleanFailure4() throws Exception {
        try {
            new JNDIUtil(TestHelper.getFile("JNDIUtilTestXML.invalid3.xml"));
            fail("exception has not been thrown");
        } catch (ConfigurationException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link JNDIUtil#getContext(String)}. Accuracy case, check the result.
     *
     * @throws Exception if error occurs
     */
    public void testGetContextAccuracy() throws Exception {
        Context context = util.getContext("test");
        assertTrue("wrong result type", context instanceof InitialContext);
        assertEquals("wrong result", new File(TestHelper.TEST_FILES_DIRECTORY).getAbsolutePath(),
            context.getNameInNamespace());
    }

    /**
     * Test method for {@link JNDIUtil#getContext(String)}. Failure case, call with empty string.
     *
     * @throws Exception if error occurs
     */
    public void testGetContextFailure() throws Exception {
        try {
            util.getContext(" ");
            fail("exception has not been thrown");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link JNDIUtil#createName(String)}. Accuracy case, check the result.
     *
     * @throws Exception if error occurs
     */
    public void testCreateNameAccuracy() throws Exception {
        String path = TestHelper.generateString();
        String name = TestHelper.generateString();
        Name result = util.createName(path + File.separator + name);
        assertEquals("wrong result name size", 2, result.size());
        assertEquals("wrong path", path, result.get(0));
        assertEquals("wrong name", name, result.get(1));
    }

    /**
     * Test method for {@link JNDIUtil#createName(String)}. Failure case, call with null.
     *
     * @throws Exception if error occurs
     */
    public void testCreateNameFailure() throws Exception {
        try {
            util.createName(null);
            fail("exception has not been thrown");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link JNDIUtil#getObject(String)}. Accuracy case, check the result.
     *
     * @throws Exception if error occurs
     */
    public void testGetObjectStringAccuracy() throws Exception {
        Object obj = util.getObject("file.txt");
        assertTrue("wrong result", obj instanceof File);
        assertEquals("wrong result", "file.txt", ((File) obj).getName());
    }

    /**
     * Test method for {@link JNDIUtil#getObject(String)}. Failure case, call with null.
     *
     * @throws Exception if error occurs
     */
    public void testGetObjectStringFailure() throws Exception {
        try {
            util.getObject((String) null);
            fail("exception has not been thrown");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link JNDIUtil#getObject(Name)}. Accuracy case, check the result with empty name.
     *
     * @throws Exception if error occurs
     */
    public void testGetObjectNameAccuracy() throws Exception {
        Object obj = util.getObject(util.createName(""));
        assertTrue("wrong result type", obj instanceof Context);
        assertEquals("wrong result", TestHelper.getFile(TEST_ROOT).getAbsolutePath(),
            ((Context) obj).getNameInNamespace());
    }

    /**
     * Test method for {@link JNDIUtil#getObject(Name)}. Failure case 1, call with null.
     *
     * @throws Exception if error occurs
     */
    public void testGetObjectNameFailure1() throws Exception {
        try {
            util.getObject((Name) null);
            fail("exception has not been thrown");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link JNDIUtil#getObject(Name)}. Failure case 2, call with inexistent name.
     *
     * @throws Exception if error occurs
     */
    public void testGetObjectNameFailure2() throws Exception {
        try {
            util.getObject(util.createName("inexistent file"));
            fail("exception has not been thrown");
        } catch (NameNotFoundException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link JNDIUtil#getObject(String, Class)}. Accuracy case, check the result with a
     * folder.
     *
     * @throws Exception if error occurs
     */
    public void testGetObjectStringClassAccuracy() throws Exception {
        Context result = (Context) util.getObject("dir", Context.class);
        assertEquals("wrong result", TestHelper.getFile(TEST_ROOT + File.separator + "dir").getAbsolutePath(),
            result.getNameInNamespace());
    }

    /**
     * Test method for {@link JNDIUtil#getObject(String, Class)}. Failure case, call with null class.
     *
     * @throws Exception if error occurs
     */
    public void testGetObjectStringClassFailure() throws Exception {
        try {
            util.getObject(TEST_ROOT + File.separator + "dir", null);
            fail("exception has not been thrown");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link JNDIUtil#getObject(Name, Class)}. Accuracy case, check the result with a file.
     *
     * @throws Exception if error occurs
     */
    public void testGetObjectNameClassAccuracy() throws Exception {
        File result = (File) util.getObject(util.createName("dir/dirfile.txt"), File.class);
        assertEquals("wrong result",
            TestHelper.getFile(TEST_ROOT + File.separator + "dir" + File.separator + "dirfile.txt").getCanonicalFile(),
            result.getCanonicalFile());
    }

    /**
     * Test method for {@link JNDIUtil#getObject(Name, Class)}. Failure case, call with incompatible class.
     *
     * @throws Exception if error occurs
     */
    public void testGetObjectNameClassFailure() throws Exception {
        try {
            util.getObject(util.createName("dir"), File.class);
            fail("exception has not been thrown");
        } catch (ClassCastException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link JNDIUtil#saveContextConfig(String, Properties)}. Accuracy case, check the
     * result.
     *
     * @throws Exception if error occurs
     */
    public void testSaveContextConfigAccuracy() throws Exception {
        Properties props = new Properties();
        String[] keys = new String[5];
        String[] values = new String[keys.length];

        for (int i = 0; i < keys.length; i++) {
            keys[i] = "property." + i;
            values[i] = TestHelper.generateString();
            props.setProperty(keys[i], values[i]);
        }

        util.saveContextConfig("test", props);

        XmlFileConfigurationStrategy strategy = new XmlFileConfigurationStrategy(TestHelper.getFile(XML_FILE));

        for (int i = 0; i < keys.length; i++) {
            assertEquals("wrong result", values[i], strategy.getProperty("context.test." + keys[i]));
        }
    }

    /**
     * Test method for {@link JNDIUtil#saveContextConfig(String, Properties)}. Failure case 1, call with
     * empty name.
     *
     * @throws Exception if error occurs
     */
    public void testSaveContextConfigFailure1() throws Exception {
        try {
            util.saveContextConfig(" ", new Properties());
            fail("exception has not been thrown");
        } catch (IllegalArgumentException e) {
            // should land here
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
            util.saveContextConfig(TestHelper.generateString(), null);
            fail("exception has not been thrown");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link JNDIUtil#saveContextConfig(String, Properties)}. Failure case 3, call with
     * properties contains empty value.
     *
     * @throws Exception if error occurs
     */
    public void testSaveContextConfigFailure3() throws Exception {
        Properties props = new Properties();
        props.setProperty(TestHelper.generateString(), TestHelper.generateString());
        props.setProperty(TestHelper.generateString(), " ");

        try {
            util.saveContextConfig(TestHelper.generateString(), props);
            fail("exception has not been thrown");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link JNDIUtil#saveContextConfig(String, Properties)}. Failure case 4, lock the
     * namespace first.
     *
     * @throws Exception if error occurs
     */
    public void testSaveContextConfigFailure4() throws Exception {
        TestHelper.loadConfig(CONFIG_FILE);
        util = new JNDIUtil(NAMESPACE);

        Properties props = new Properties();
        props.setProperty(TestHelper.generateString(), TestHelper.generateString());
        props.setProperty(TestHelper.generateString(), TestHelper.generateString());
        ConfigManager.getInstance().lock(NAMESPACE, "test");

        try {
            util.saveContextConfig(TestHelper.generateString(), props);
            fail("exception has not been thrown");
        } catch (ConfigurationException e) {
            // should land here
        } finally {
            ConfigManager.getInstance().forceUnlock(NAMESPACE);
        }
    }

    /**
     * Test method for {@link JNDIUtil#createSubcontext(String)}. Accuracy case, check the result with a new
     * context.
     *
     * @throws Exception if error occurs
     */
    public void testCreateSubcontextStringAccuracy() throws Exception {
        String name = TestHelper.generateString();
        Context result = util.createSubcontext(name);
        File file = TestHelper.getFile(TEST_ROOT + File.separator + name);
        assertEquals("wrong result name", file.getAbsolutePath(), result.getNameInNamespace());
        assertTrue("no directory created", file.exists());
        assertTrue("wrong file type", file.isDirectory());
        file.delete();
    }

    /**
     * Test method for {@link JNDIUtil#createSubcontext(String)}. Failure case, call with zero-length string.
     *
     * @throws Exception if error occurs
     */
    public void testCreateSubcontextStringFailure() throws Exception {
        try {
            util.createSubcontext("");
            fail("exception has not been thrown");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link JNDIUtil#createSubcontext(Name)}. Accuracy case, check the result with an exist
     * context.
     *
     * @throws Exception if error occurs
     */
    public void testCreateSubcontextNameAccuracy() throws Exception {
        Context result = util.createSubcontext(util.createName("dir"));
        File file = TestHelper.getFile(TEST_ROOT + File.separator + "dir");
        assertEquals("wrong result", file.getAbsolutePath(), result.getNameInNamespace());
    }

    /**
     * Test method for {@link JNDIUtil#createSubcontext(Name)}. Failure case 1, call with an exist file (not
     * a context)
     *
     * @throws Exception if error occurs
     */
    public void testCreateSubcontextNameFailure1() throws Exception {
        try {
            util.createSubcontext(util.createName("file.txt"));
            fail("exception has not been thrown");
        } catch (NameAlreadyBoundException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link JNDIUtil#createSubcontext(Name)}. Failure case 2, call with name begin with an
     * exist file.
     *
     * @throws Exception if error occurs
     */
    public void testCreateSubcontextNameFailure2() throws Exception {
        try {
            util.createSubcontext(util.createName("file.txt" + File.separator + TestHelper.generateString()));
            fail("exception has not been thrown");
        } catch (NameAlreadyBoundException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link JNDIUtil#getTopic(String)}. Accuracy case, check the result.
     *
     * @throws Exception if error occurs
     */
    public void testGetTopicStringAccuracy() throws Exception {
        String name = TestHelper.generateString();
        String topicName = TestHelper.generateString();
        Context context = util.getContext(DEFAULT_NAME);
        context.bind(name, new TopicImpl(topicName));

        try {
            Topic result = util.getTopic(name);
            assertTrue("wrong result type", result instanceof TopicImpl);
            assertEquals("wrong result", topicName, result.getTopicName());
        } finally {
            context.unbind(name);
        }
    }

    /**
     * Test method for {@link JNDIUtil#getTopic(String)}. Failure case 1, call with null.
     *
     * @throws Exception if error occurs
     */
    public void testGetTopicStringFailure1() throws Exception {
        try {
            util.getTopic((String) null);
            fail("exception has not been thrown");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link JNDIUtil#getTopic(String)}. Failure case 2, call with an inexistent name.
     *
     * @throws Exception if error occurs
     */
    public void testGetTopicStringFailure2() throws Exception {
        try {
            util.getTopic("inexistent topic");
            fail("exception has not been thrown");
        } catch (NameNotFoundException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link JNDIUtil#getTopic(Name)}. Accuracy case, check the result with an exist file
     * (neither context nor topic).
     *
     * @throws Exception if error occurs
     */
    public void testGetTopicNameAccuracy() throws Exception {
        assertNull("wrong result", util.getTopic(util.createName("file.txt")));
    }

    /**
     * Test method for {@link JNDIUtil#getTopic(Name)}. Failure case, call with an exist context.
     *
     * @throws Exception if error occurs
     */
    public void testGetTopicNameFailure() throws Exception {
        try {
            util.getTopic(util.createName("dir"));
            fail("exception has not been thrown");
        } catch (NamingException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link JNDIUtil#getQueue(String)}. Accuracy case, check the result with an exist file.
     *
     * @throws Exception if error occurs
     */
    public void testGetQueueStringAccuracy() throws Exception {
        assertNull("wrong result", util.getQueue("file.txt"));
    }

    /**
     * Test method for {@link JNDIUtil#getQueue(String)}. Failure case, call with null.
     *
     * @throws Exception if error occurs
     */
    public void testGetQueueStringFailure() throws Exception {
        try {
            util.getQueue((String) null);
            fail("exception has not been thrown");
        } catch (IllegalArgumentException e) {
            // should land here;
        }
    }

    /**
     * Test method for {@link JNDIUtil#getQueue(Name)}. Accuracy case, check the result.
     *
     * @throws Exception if error occurs
     */
    public void testGetQueueNameAccuracy() throws Exception {
        Name name = util.createName(TestHelper.generateString());
        String queueName = TestHelper.generateString();
        Context context = util.getContext(DEFAULT_NAME);
        context.bind(name, new QueueImpl(queueName));

        try {
            Queue result = util.getQueue(name);
            assertTrue("wrong result type", result instanceof QueueImpl);
            assertEquals("wrong result", queueName, result.getQueueName());
        } finally {
            context.unbind(name);
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
            fail("exception has not been thrown");
        } catch (NamingException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link JNDIUtil#getConnection(Name)}. Accuracy case, check the result.
     *
     * @throws Exception if error occurs
     */
    public void testGetConnectionNameAccuracy() throws Exception {
        Name name = util.createName(TestHelper.generateString());
        Context context = util.getContext(DEFAULT_NAME);
        context.bind(name, new DataSourceImpl(TestHelper.generateString()));

        try {
            Connection result = util.getConnection(name);
            // Anonymous inner class
            assertTrue("wrong result type", result.getClass().getName().startsWith(DataSourceImpl.class.getName()));
        } finally {
            context.unbind(name);
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
            fail("exception has not been thrown");
        } catch (NamingException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link JNDIUtil#getConnection(String)}. Accuracy case, call with an exist file.
     *
     * @throws Exception if error occurs
     */
    public void testGetConnectionStringAccuracy() throws Exception {
        assertNull("wrong result", util.getConnection("file.txt"));
    }

    /**
     * Test method for {@link JNDIUtil#getConnection(String)}. Failure case, call with an inexistent name.
     *
     * @throws Exception if error occurs
     */
    public void testGetConnectionString() throws Exception {
        try {
            util.getConnection("inexistent data source");
            fail("exception has not been thrown");
        } catch (NameNotFoundException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link JNDIUtil#dump(ContextRenderer, boolean)}. Accuracy case, check the result.
     *
     * @throws Exception if error occurs
     */
    public void testDumpContextRendererBooleanAccuracy()
        throws Exception {
        ContextXMLRenderer renderer = new ContextXMLRenderer();
        util.dump(renderer, true);

        Element docElement = renderer.getDocument().getDocumentElement();
        assertEquals("wrong element name", "Context", docElement.getNodeName());
        assertEquals("wrong element attribute", TestHelper.getFile(TEST_ROOT).getAbsolutePath(),
            docElement.getAttribute("fullname"));
        assertEquals("wrong element attribute", "", docElement.getAttribute("name"));

        NodeList list = docElement.getChildNodes();
        assertEquals("wrong children number", 2, list.getLength());

        Node child = list.item(0);
        assertEquals("wrong child name", "Context", child.getNodeName());
        assertEquals("wrong context name", "dir", ((Element) child).getAttribute("name"));
        assertEquals("wrong grandchildren size", 1, child.getChildNodes().getLength());
        child = list.item(1);
        assertEquals("wrong child name", "Binding", child.getNodeName());
        assertEquals("wrong binding name", "file.txt", child.getFirstChild().getFirstChild().getNodeValue());
    }

    /**
     * Test method for {@link JNDIUtil#dump(ContextRenderer, boolean)}. Failure case, call with null
     * renderer.
     *
     * @throws Exception if error occurs
     */
    public void testDumpContextRendererBooleanFailure()
        throws Exception {
        try {
            util.dump(null, true);
            fail("exception has not been thrown");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link JNDIUtil#dump(Name, ContextRenderer, boolean)}. Accuracy case, check the
     * result.
     *
     * @throws Exception if error occurs
     */
    public void testDumpNameContextRendererBooleanAccuracy()
        throws Exception {
        ContextXMLRenderer renderer = new ContextXMLRenderer();
        util.dump(util.createName("dir"), renderer, true);

        Element docElement = renderer.getDocument().getDocumentElement();
        assertEquals("wrong element name", "Context", docElement.getNodeName());
        assertEquals("wrong element attribute",
            TestHelper.getFile(TEST_ROOT + File.separator + "dir").getAbsolutePath(),
            docElement.getAttribute("fullname"));
        assertEquals("wrong element attribute", "", docElement.getAttribute("name"));

        NodeList list = docElement.getChildNodes();
        assertEquals("wrong children number", 1, list.getLength());

        Node child = list.item(0);
        assertEquals("wrong child name", "Binding", child.getNodeName());
        assertEquals("wrong binding name", "dirfile.txt", child.getFirstChild().getFirstChild().getNodeValue());
    }

    /**
     * Test method for {@link JNDIUtil#dump(Name, ContextRenderer, boolean)}. Failure case 1, call with null
     * name.
     *
     * @throws Exception if error occurs
     */
    public void testDumpNameContextRendererBooleanFailure1()
        throws Exception {
        try {
            util.dump((Name) null, new ContextXMLRenderer(), false);
            fail("exception has not been thrown");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link JNDIUtil#dump(Name, ContextRenderer, boolean)}. Failure case 2, call with null
     * renderer.
     *
     * @throws Exception if error occurs
     */
    public void testDumpNameContextRendererBooleanFailure2()
        throws Exception {
        try {
            util.dump(util.createName("dir"), null, false);
            fail("exception has not been thrown");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link JNDIUtil#dump(Name, ContextRenderer, boolean)}. Failure case 3, call with file
     * name.
     *
     * @throws Exception if error occurs
     */
    public void testDumpNameContextRendererBooleanFailure3()
        throws Exception {
        try {
            util.dump(util.createName("file.txt"), new ContextXMLRenderer(), false);
            fail("exception has not been thrown");
        } catch (NamingException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link JNDIUtil#dump(String, ContextRenderer, boolean)}. Accuracy case, check the
     * result with empty string.
     *
     * @throws Exception if error occurs
     */
    public void testDumpStringContextRendererBooleanAccuracy()
        throws Exception {
        ContextXMLRenderer renderer = new ContextXMLRenderer();
        util.dump("", renderer, false);

        Element docElement = renderer.getDocument().getDocumentElement();
        assertEquals("wrong element name", "Context", docElement.getNodeName());
        assertEquals("wrong element attribute", TestHelper.getFile(TEST_ROOT).getAbsolutePath(),
            docElement.getAttribute("fullname"));
        assertEquals("wrong element attribute", "", docElement.getAttribute("name"));

        NodeList list = docElement.getChildNodes();
        assertEquals("wrong children number", 2, list.getLength());

        Node child = list.item(0);
        assertEquals("wrong child name", "Binding", child.getNodeName());
        assertEquals("wrong binding name", "dir", child.getFirstChild().getFirstChild().getNodeValue());
        child = list.item(1);
        assertEquals("wrong child name", "Binding", child.getNodeName());
        assertEquals("wrong binding name", "file.txt", child.getFirstChild().getFirstChild().getNodeValue());
    }

    /**
     * Test method for {@link JNDIUtil#dump(String, ContextRenderer, boolean)}. Failure case 1, call with
     * null name.
     *
     * @throws Exception if error occurs
     */
    public void testDumpStringContextRendererBooleanFailure1()
        throws Exception {
        try {
            util.dump((String) null, new ContextXMLRenderer(), false);
            fail("exception has not been thrown");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link JNDIUtil#dump(String, ContextRenderer, boolean)}. Failure case 2, call with
     * null renderer.
     *
     * @throws Exception if error occurs
     */
    public void testDumpStringContextRendererBooleanFailure2()
        throws Exception {
        try {
            util.dump("dir", null, false);
            fail("exception has not been thrown");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link JNDIUtil#dump(String, ContextRenderer, boolean)}. Failure case 3, call with
     * file name.
     *
     * @throws Exception if error occurs
     */
    public void testDumpStringContextRendererBooleanFailure3()
        throws Exception {
        try {
            util.dump("dir" + File.separator + "dirfile.txt", new ContextXMLRenderer(), false);
            fail("exception has not been thrown");
        } catch (NamingException e) {
            // should land here
        }
    }
}
