/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 *
 * TCS Project JNDI Context Utility 2.0 (Demo)
 *
 * @ Demo.java
 */
package com.topcoder.naming.jndiutility;

import junit.framework.TestCase;

import org.w3c.dom.Document;

import java.io.File;
import java.io.FileInputStream;

import java.sql.Connection;

import java.util.Properties;

import javax.jms.Queue;
import javax.jms.Topic;

import javax.naming.CompositeName;
import javax.naming.Context;
import javax.naming.Name;


/**
 * Demo of this component.
 *
 * @author Charizard
 * @version 2.0
 */
public class Demo extends TestCase {
    /**
     * Set up method. Created the resources used in demo and load configuration.
     *
     * @throws Exception if error occurs
     *
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        Context defaultContext = JNDIUtils.getDefaultContext();
        defaultContext.bind("MyQueue", new QueueImpl("MyQueue"));
        defaultContext.bind("MyTopic", new TopicImpl("MyTopic"));
        defaultContext.bind("MyDataSource", new DataSourceImpl("MyDataSource"));
        TestHelper.loadConfig("DemoConfig.xml");
    }

    /**
     * Tear down method. Clear the resources and configuration.
     *
     * @throws Exception if error occurs
     *
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        Context defaultContext = JNDIUtils.getDefaultContext();
        defaultContext.unbind("MyQueue");
        defaultContext.unbind("MyTopic");
        defaultContext.unbind("MyDataSource");
        TestHelper.clearConfig();
    }

    /**
     * Demonstrates the usage of JNDIUtils class. Uses no constant string and helper method for clearer demo.
     *
     * @throws Exception if error occurs
     */
    public void testDemoJNDIUtils() throws Exception {
        ////////////////////////////////////////////////////////////////////////////////////////////////
        // JNDIUtils                                                                                  //
        ////////////////////////////////////////////////////////////////////////////////////////////////
        // Almost all methods have overloaded versions for both given context and default initial context
        // and both java.lang.String and javax.naming.Name names can be used
        // Get the default context which initial parameters are stored in configuration file under context.default name
        Context ctx = JNDIUtils.getDefaultContext();
        System.out.println(ctx + ": " + ctx.getNameInNamespace());
        // Create given subcontext within given Context
        ctx = JNDIUtils.createSubcontext(ctx, "dir");
        System.out.println(ctx + ": " + ctx.getNameInNamespace());

        // Store initial parameters of Context in configuration file for further use.
        Properties props = new Properties();
        props.put("factory", "com.sun.jndi.fscontext.RefFSContextFactory");
        props.put("url", "file:test_files");
        JNDIUtils.saveContextConfig("new_context", props);

        // Create context using initial parameters from configuration file.
        ctx = JNDIUtils.getContext("new_context");
        System.out.println(ctx + ": " + ctx.getNameInNamespace());

        ctx = JNDIUtils.getDefaultContext();

        // Get the resources from Context
        Queue queue = JNDIUtils.getQueue(ctx, "MyQueue");
        System.out.println(queue);

        Topic topic = JNDIUtils.getTopic(ctx, "MyTopic");
        System.out.println(topic);

        Connection con = JNDIUtils.getConnection(ctx, "MyDataSource");
        System.out.println(con);

        // Get object from context
        Object object = JNDIUtils.getObject(ctx, "dir");
        System.out.println(object);

        // Get object verifying that it can be cast to specified class
        object = JNDIUtils.getObject(ctx, "dir/dirfile.txt", File.class);
        System.out.println(object);

        // Get the XML Document object corresponding to some subcontext of Context without traversing
        // through nested subcontexts
        ContextRenderer renderer = new ContextXMLRenderer();
        JNDIUtils.dump(ctx, "dir", renderer, false);

        Document doc = ((ContextXMLRenderer) renderer).getDocument();
        System.out.println(doc);

        // Print the XML Document object corresponding to Context traversing through nested subcontexts
        // to console
        renderer = new ContextConsoleRenderer();
        JNDIUtils.dump(ctx, renderer, true);
        System.out.println();

        // Convert a String to Name object that is a compound name separated with slashes
        Name name = JNDIUtils.createName("com.topcoder.util.config", '.');
        System.out.println(name);

        // Convert a Name to String separated with any desired character
        String string = JNDIUtils.createString(name, '?');
        System.out.println(string);

        // Convert a String to Name object with the rule of given context
        name = JNDIUtils.createName(ctx, "directory/file");
        System.out.println(name);

        // Main method
        ////////////////////////////////////////////////////////////////////////////////////////////////
        // In order to dump a Context to standard output using command line interface following       //
        // command should be typed :                                                                  //
        // java com.topcoder.naming.jndiutility.JNDIUtils [-d] contextname [subcontext], where:       //
        // -d - is an optional switch specifying that nested subcontexts should be traversed          //
        //      too. If is not specified  then directly nested subcontexts will be represented        //
        //      like a simple bindings and indirectly nested subcontexts will not be dumped           //
        //      at all.                                                                               //
        // contextname - a name of context specified in configuration file                            //
        // subcontext - an optional name of subcontext to dump                                        //
        ////////////////////////////////////////////////////////////////////////////////////////////////
        // dump the test context with the nested subcontexts to be traversed
        JNDIUtils.main(new String[] {"-d", "test", "."});
        System.out.println();
    }

    /**
     * Demonstrates the usage of JNDIUtil class. Uses no constant string and helper method for clearer demo.
     *
     * @throws Exception if error occurs
     */
    public void testDemoJNDIUtil() throws Exception {
        ////////////////////////////////////////////////////////////////////////////////////////////////
        // JNDIUtil                                                                                   //
        ////////////////////////////////////////////////////////////////////////////////////////////////
        // Using the instance class is very much the same as the static utility with the main difference being
        // that we can deal with specific namespaces or with specific XML file locations as well as being able
        // to set a Context directly:
        // Create an instance of the JNDIUtil instance with different configurations
        // default Config namespace
        JNDIUtil jndiUtil = new JNDIUtil();
        // specific Config namespace
        jndiUtil = new JNDIUtil("com.topcoder.naming.jndiutility.Demo");
        // specific InputStream
        jndiUtil = new JNDIUtil(new FileInputStream("test_files/Demo.xml"));
        // specific file (for XML)
        jndiUtil = new JNDIUtil(new File("test_files/Demo.xml"));

        // Once we have an instance of the jndi util we can call instance methods (which are an exact subset
        // of the JNDIUtils class)

        // Create a name with a string that is valid within associated context
        Name name = jndiUtil.createName("directory/file");
        System.out.println(name);

        // Get an object in the associated context
        Object object = jndiUtil.getObject("dir");
        System.out.println(object);

        // Get an object with specified type in the associated context
        object = jndiUtil.getObject(jndiUtil.createName("file.txt"), File.class);
        System.out.println(object);

        // Get a resources in the associated context
        Topic topic = jndiUtil.getTopic("MyTopic");
        System.out.println(topic);

        Queue queue = jndiUtil.getQueue(new CompositeName("MyQueue"));
        System.out.println(queue);

        Connection con = jndiUtil.getConnection("MyDataSource");
        System.out.println(con);

        // Save configurations in the xml file which construct this instance (or the namespace of ConfigManager)
        Properties props = new Properties();
        props.put("factory", "com.sun.jndi.fscontext.RefFSContextFactory");
        props.put("url", "file:test_files");
        jndiUtil.saveContextConfig("test", props);

        // Get a named context in the configuration
        Context ctx = jndiUtil.getContext("test");
        System.out.println(ctx + ": " + ctx.getNameInNamespace());

        // Create a sub context in the associated context
        jndiUtil.createSubcontext("dir");

        // Dump the content with given subcontext name in the associated context
        ContextRenderer renderer = new ContextConsoleRenderer();
        jndiUtil.dump("dir", renderer, true);
        System.out.println();
    }
}
