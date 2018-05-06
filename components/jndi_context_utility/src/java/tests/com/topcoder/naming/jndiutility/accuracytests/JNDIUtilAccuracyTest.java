/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.naming.jndiutility.accuracytests;

import java.io.File;
import java.sql.Connection;
import java.util.Properties;
import java.util.Random;

import javax.jms.Queue;
import javax.jms.Topic;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.Name;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.topcoder.naming.jndiutility.ContextXMLRenderer;
import com.topcoder.naming.jndiutility.DataSourceImpl;
import com.topcoder.naming.jndiutility.JNDIUtil;
import com.topcoder.naming.jndiutility.QueueImpl;
import com.topcoder.naming.jndiutility.TopicImpl;
import com.topcoder.naming.jndiutility.configstrategy.XmlFileConfigurationStrategy;

import junit.framework.TestCase;

/**
 * Accuracy test for JNDIUtil.
 * @author KKD
 * @version 2.0
 */
public class JNDIUtilAccuracyTest extends TestCase {
    /**
     * namespace in ConfigManager used in accuracy test.
     */
    private static final String NAMESPACE = JNDIUtilAccuracyTest.class
            .getName();

    /**
     * Represents separator of file.
     */
    private static final String FS = File.separator;

    /**
     * Name of configuration file used in accuracy test.
     */
    private static final String CONFIGFILE = "test_files" + FS
            + "accuracytests" + FS + "JNDIUtilAccuracyTest.xml";

    /**
     * Name of xml file used in accuracy test.
     */
    private static final String XMLFILE = "test_files" + FS + "accuracytests"
            + FS + "JNDIUtilAccuracyTestXML.xml";

    /**
     * Root directory of context used in accuracy test.
     */
    private static final String ROOT = "test_files" + FS + "accuracy_test_root";

    /**
     * Represents string 'default'.
     */
    private static final String DEFAULTNAME = "default";

    /**
     * JNDIUtil instance used in most test cases.
     */
    private JNDIUtil test;

    /**
     * set up of the test.
     * @throws Exception exception to JUnit.
     */
    protected void setUp() throws Exception {
        test = new JNDIUtil(AccuracyTestHelper.getFile(XMLFILE));
    }

    /**
     * tear down of the test.
     * @throws Exception exception to JUnit.
     */
    protected void tearDown() throws Exception {
        AccuracyTestHelper.clearConfig();
        test = null;
    }

    /**
     * test constructor JNDIUtil().
     * @throws Exception exception to JUnit.
     */
    public void testJNDIUtilAccuracy() throws Exception {
        assertNotNull("instance should be created.", new JNDIUtil());
    }

    /**
     * test constructor JNDIUtil(String).
     * @throws Exception exception to JUnit.
     */
    public void testJNDIUtil_StringAccuracy() throws Exception {
        AccuracyTestHelper.loadConfig(CONFIGFILE);
        assertNotNull("instance should be created.", new JNDIUtil(NAMESPACE));
    }

    /**
     * test constructor JNDIUtil(InputStream, boolean).
     * @throws Exception exception to JUnit.
     */
    public void testJNDIUtil_InputStreamAccuracy() throws Exception {
        assertNotNull("instance should be created.", new JNDIUtil(
                AccuracyTestHelper.getInputStream(XMLFILE)));
    }

    /**
     * test constructor JNDIUtil(File, boolean).
     * @throws Exception exception to JUnit.
     */
    public void testJNDIUtil_FileAccuracy() throws Exception {
        assertNotNull("instance should be created.", new JNDIUtil(
                AccuracyTestHelper.getFile(XMLFILE)));
    }

    /**
     * test method getObject(String).
     * @throws Exception exception to JUnit.
     */
    public void testgetObject_StringAccuracy() throws Exception {
        Object file1 = test.getObject("file1.txt");
        assertTrue("the file1 should be instanc of File.",
                file1 instanceof File);
        assertEquals("file1.txt", ((File) file1).getName());
        Object dir1 = test.getObject("dir1");
        assertTrue("the dir1 should be instanc of Context.",
                dir1 instanceof Context);
    }

    /**
     * test method getObject(Name).
     * @throws Exception exception to JUnit.
     */
    public void testgetObject_NameAccuracy() throws Exception {
        Object dirfile1 = test.getObject(test.createName("dir1" + FS
                + "dir1.txt"));
        assertTrue("the dirfile1 should be instanc of File.",
                dirfile1 instanceof File);
        assertEquals("wrong file name.", "dir1.txt", ((File) dirfile1)
                .getName());
    }

    /**
     * test method getObject(String, Class).
     * @throws Exception exception to JUnit.
     */
    public void testgetObject_StringClassAccuracy() throws Exception {
        Object file1 = test.getObject("file1.txt", File.class);
        assertTrue("the file1 should be instanc of File.",
                file1 instanceof File);
        assertEquals("file1.txt", ((File) file1).getName());
        Object dir1 = test.getObject("dir1", Context.class);
        assertTrue("the dir1 should be instanc of Context.",
                dir1 instanceof Context);
    }

    /**
     * test method getObject(Name, Class).
     * @throws Exception exception to JUnit.
     */
    public void testgetObject_NameClassAccuracy() throws Exception {
        Object dirfile1 = test.getObject(test.createName("dir1" + FS
                + "dir1.txt"), File.class);
        assertTrue("the dirfile1 should be instanc of File.",
                dirfile1 instanceof File);
        assertEquals("wrong file name.", "dir1.txt", ((File) dirfile1)
                .getName());
    }

    /**
     * test method getContext(String).
     * @throws Exception exception to JUnit.
     */
    public void testgetContext_StringAccuracy() throws Exception {
        Context context = test.getContext("test");
        assertTrue("the context should be instanc of InitialContext.",
                context instanceof InitialContext);
        assertEquals("the result should be the path of root.", new File(
                "test_files").getAbsolutePath(), context.getNameInNamespace());
    }

    /**
     * test method saveContextConfig(String, Properties).
     * @throws Exception exception to JUnit.
     */
    public void testsaveContextConfigAccuracy() throws Exception {
        Properties props = new Properties();
        String[] keys = new String[6];
        String[] values = new String[keys.length];

        for (int i = 0; i < keys.length; i++) {
            keys[i] = "property." + i;
            values[i] = new Long(new Random().nextLong()).toString();
            props.setProperty(keys[i], values[i]);
        }

        test.saveContextConfig("test", props);
        XmlFileConfigurationStrategy strategy = new XmlFileConfigurationStrategy(
                AccuracyTestHelper.getFile(XMLFILE));

        for (int i = 0; i < keys.length; i++) {
            assertEquals("wrong result", values[i], strategy
                    .getProperty("context.test." + keys[i]));
        }
    }

    /**
     * test method createSubcontext(String).
     * @throws Exception exception to JUnit.
     */
    public void testcreateSubcontext_StringAccuracy() throws Exception {
        String name = "new sub context1";
        File file = new File(ROOT + FS + name);
        file.delete();
        try {
            Context context = test.createSubcontext(name);
            file = new File(context.getNameInNamespace());
            assertTrue("should be created.", file.exists());
            assertTrue("should be a directory.", file.isDirectory());
        } finally {
            file.delete();
        }
    }

    /**
     * test method createSubcontext(Name).
     * @throws Exception exception to JUnit.
     */
    public void testcreateSubcontext_NameAccuracy() throws Exception {
        Name name = test.createName("new sub context 2");
        File file = new File(ROOT + FS + "new sub context 2");
        file.delete();

        try {
            Context context = test.createSubcontext(name);
            file = new File(context.getNameInNamespace());
            assertTrue("should be created.", file.exists());
            assertTrue("should be a directory.", file.isDirectory());
        } finally {
            file.delete();
        }
    }

    /**
     * test method createName(String).
     * @throws Exception exception to JUnit.
     */
    public void testcreateName_StringAccuracy() throws Exception {
        String path1 = "path1";
        String path2 = "path2";
        Name name = test.createName(path1 + FS + path2);
        assertEquals("the size should be 2.", 2, name.size());
        assertEquals("the first one should be path1.", path1, name.get(0));
        assertEquals("the second one should be path2.", path2, name.get(1));
    }

    /**
     * test method getTopic(String).
     * @throws Exception exception to JUnit.
     */
    public void testgetTopic_StringAccuracy() throws Exception {
        String name = "name1";
        String topicName = "topic name1";
        Context context = test.getContext(DEFAULTNAME);
        context.bind(name, new TopicImpl(topicName));

        try {
            Topic topic = test.getTopic(name);
            assertNotNull("should not be null.", topic);
            assertTrue("topic should be instance of TopicImpl.",
                    topic instanceof TopicImpl);
            assertEquals("invalid result.", topicName, topic.getTopicName());
        } finally {
            context.unbind(name);
        }
    }

    /**
     * test method getTopic(Name).
     * @throws Exception exception to JUnit.
     */
    public void testgetTopic_NameAccuracy() throws Exception {
        Name name = test.createName("name2");
        String topicName = "topic name2";
        Context context = test.getContext(DEFAULTNAME);
        context.bind(name, new TopicImpl(topicName));

        try {
            Topic topic = test.getTopic(name);
            assertNotNull("should not be null.", topic);
            assertTrue("topic should be instance of TopicImpl.",
                    topic instanceof TopicImpl);
            assertEquals("invalid result.", topicName, topic.getTopicName());
        } finally {
            context.unbind(name);
        }
    }

    /**
     * test method getTopic(Name).
     * @throws Exception exception to JUnit.
     */
    // com.topcoder.naming.jndiutility.JNDIUtil.getQueue(String)
    public void testgetQueue_StringAccuracy() throws Exception {
        String name = "name1";
        String queueName = "queue name1";
        Context context = test.getContext(DEFAULTNAME);
        context.bind(name, new QueueImpl(queueName));
        try {
            Queue queue = test.getQueue(name);
            assertNotNull("should not be null.", queue);
            assertTrue("queue should be instance of QueueImpl.",
                    queue instanceof QueueImpl);
            assertEquals("invalid result.", queueName, queue.getQueueName());
        } finally {
            context.unbind(name);
        }
    }

    /**
     * test method getTopic(Name).
     * @throws Exception exception to JUnit.
     */
    // com.topcoder.naming.jndiutility.JNDIUtil.getQueue(Name)
    public void testgetQueue_NameAccuracy() throws Exception {
        Name name = test.createName("name2");
        String queueName = "queue name2";
        Context context = test.getContext(DEFAULTNAME);
        context.bind(name, new QueueImpl(queueName));
        try {
            Queue queue = test.getQueue(name);
            assertNotNull("should not be null.", queue);
            assertTrue("queue should be instance of QueueImpl.",
                    queue instanceof QueueImpl);
            assertEquals("invalid result.", queueName, queue.getQueueName());
        } finally {
            context.unbind(name);
        }
    }

    /**
     * test method dump(ContextRenderer, boolean).
     * --dir1 -- dir1.txt
     * |
     * --file1.txt
     * @throws Exception exception to JUnit.
     */
    public void testdump_ContextRendererAccuracy() throws Exception {
        ContextXMLRenderer renderer = new ContextXMLRenderer();
        test.dump(renderer, true);

        Element doc = renderer.getDocument().getDocumentElement();

        assertEquals("The node name should be 'Context'.", "Context", doc
                .getNodeName());
        assertEquals("wrong element attribute", new File(ROOT)
                .getAbsolutePath(), doc.getAttribute("fullname"));
        assertEquals("wrong element attribute", "", doc.getAttribute("name"));

        NodeList list = doc.getChildNodes();
        assertEquals("wrong children number", 2, list.getLength());

        Node child = list.item(0);
        assertEquals("wrong child name", "Context", child.getNodeName());
        assertEquals("wrong context name", "dir1", ((Element) child)
                .getAttribute("name"));
        assertEquals("wrong grandchildren size", 1, child.getChildNodes()
                .getLength());

        child = list.item(1);
        assertEquals("wrong child name", "Binding", child.getNodeName());
        assertEquals("wrong binding name", "file1.txt", child.getFirstChild()
                .getFirstChild().getNodeValue());

    }

    /**
     * test method dump(Name, ContextRenderer, boolean).
     * @throws Exception exception to JUnit.
     */
    public void testdump_NameAccuracy() throws Exception {
        Name name = test.createName("dir1");
        ContextXMLRenderer renderer = new ContextXMLRenderer();
        test.dump(name, renderer, true);

        Element doc = renderer.getDocument().getDocumentElement();

        assertEquals("The node name should be 'Context'.", "Context", doc
                .getNodeName());
        assertEquals("wrong element attribute", new File(ROOT + FS + "dir1")
                .getAbsolutePath(), doc.getAttribute("fullname"));
        assertEquals("wrong element attribute", "", doc.getAttribute("name"));

        NodeList list = doc.getChildNodes();
        assertEquals("wrong children number", 1, list.getLength());

        Node child = list.item(0);
        assertEquals("wrong child name", "Binding", child.getNodeName());
        assertEquals("wrong binding name", "dir1.txt", child.getFirstChild()
                .getFirstChild().getNodeValue());
    }

    /**
     * test method dump(String, ContextRenderer, boolean).
     * @throws Exception exception to JUnit.
     */
    public void testdump_StringAccuracy() throws Exception {
        ContextXMLRenderer renderer = new ContextXMLRenderer();
        test.dump("dir1", renderer, true);

        Element doc = renderer.getDocument().getDocumentElement();

        assertEquals("The node name should be 'Context'.", "Context", doc
                .getNodeName());
        assertEquals("wrong element attribute", new File(ROOT + FS + "dir1")
                .getAbsolutePath(), doc.getAttribute("fullname"));
        assertEquals("wrong element attribute", "", doc.getAttribute("name"));

        NodeList list = doc.getChildNodes();
        assertEquals("wrong children number", 1, list.getLength());

        Node child = list.item(0);
        assertEquals("wrong child name", "Binding", child.getNodeName());
        assertEquals("wrong binding name", "dir1.txt", child.getFirstChild()
                .getFirstChild().getNodeValue());
    }

    /**
     * test method getConnection(String).
     * @throws Exception exception to JUnit.
     */
    public void testgetConnection_StringAccuracy() throws Exception {
        String name = "name1";
        String connectionName = "connection name1";
        Context context = test.getContext(DEFAULTNAME);
        context.bind(name, new DataSourceImpl(connectionName));
        try {
            Connection connection = test.getConnection(name);
            assertNotNull("should not be null.", connection);
        } finally {
            context.unbind(name);
        }
    }

    /**
     * test method getConnection(Name).
     * @throws Exception exception to JUnit.
     */
    public void testgetConnection_NameAccuracy() throws Exception {
        Name name = test.createName("name2");
        String connectionName = "connection name2";
        Context context = test.getContext(DEFAULTNAME);
        context.bind(name, new DataSourceImpl(connectionName));
        try {
            Connection connection = test.getConnection(name);
            assertNotNull("should not be null.", connection);
        } finally {
            context.unbind(name);
        }
    }

}
