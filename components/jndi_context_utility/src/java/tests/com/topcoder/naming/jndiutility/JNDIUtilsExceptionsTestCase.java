/*
 * Copyright (C) 2003 TopCoder Inc., All Rights Reserved.
 *
 * @(#) JNDIUtilsExceptionsTestCase.java
 *
 * 1.0 08/09/2003
 */
package com.topcoder.naming.jndiutility;

import java.io.File;
import java.util.Properties;

import javax.jms.Topic;
import javax.naming.CompoundName;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.naming.NamingException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * This test case tests that the methods in the JNDIUtils class throws all the correct Exceptions.
 *
 * @author preben
 * @version 1.0 08/09/2003
 */
public class JNDIUtilsExceptionsTestCase extends TestCase {
    /** Shall we show warnings. */
    static final boolean WARNING = false;

    /** Constant for error message. */
    static final String IAE = "did not throw IllegalArgumentException";

    /** Constant for error message. */
    static final String INE = "did not throw InvalidNameException";

    /** Constant for error message. */
    static final String NE = "did not throw NamingException";

    /**
     * Constructor.
     *
     * @param name a name
     */
    public JNDIUtilsExceptionsTestCase(String name) {
        super(name);
    }

    /**
     * Return a new Test.
     *
     * @return a new Test
     */
    public static Test suite() {
        return new TestSuite(JNDIUtilsExceptionsTestCase.class);
    }

    /**
     * Test the createName(Context,String) method throw the correct exceptions.
     *
     * @throws Exception if any exception occurs
     */
    public void testCreateNameInContextAndFromString()
        throws Exception {
        String name = "";
        String invalidName = "///...";
        Context ldapContext = JNDIUtils.getContext("ldap");
        Context context = JNDIUtils.getDefaultContext();

        try {
            JNDIUtils.createName(context, (String) null);
            fail(IAE);
        } catch (IllegalArgumentException e) {
            // should land here
        }

        try {
            JNDIUtils.createName((Context) null, name);
            fail(IAE);
        } catch (IllegalArgumentException e) {
            // should land here
        }

        try {
            JNDIUtils.createName(ldapContext, invalidName);
            fail(INE);
        } catch (InvalidNameException e) {
            // should land here
        }
    }

    /**
     * Test that the createName(String) method throw the correct exceptions.
     *
     * @throws Exception if any exception occurs
     */
    public void testCreateNameFromString() throws Exception {
        try {
            JNDIUtils.createName((String) null);
            fail(IAE);
        } catch (IllegalArgumentException e) {
            // should land here
        }

        if (WARNING) {
            fail("did not test invalidname");
        }
    }

    /**
     * Test that the createName(String,char) method throw the correct exceptions.
     *
     * @throws Exception if any exception occurs
     */
    public void testCreateNameFromStringAndSep() throws Exception {
        try {
            JNDIUtils.createName((String) null, '.');
            fail(IAE);
        } catch (IllegalArgumentException e) {
            // should land here
        }

        if (WARNING) {
            fail("did not test invalidname");
        }
    }

    /**
     * Test that the createString(Name,char) throws the correct exceptions.
     */
    public void testCreateStringFromNameAndSep() {
        try {
            JNDIUtils.createString((Name) null, '.');
            fail(IAE);
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test that the createSubcontex(Context,Name) throws the correct exceptions.
     *
     * @throws Exception if any exception occurs
     */
    public void testCreateSubcontextFromContextAndName()
        throws Exception {
        Name name = new CompoundName("", new Properties());
        Context initialContext = JNDIUtils.getDefaultContext();

        try {
            JNDIUtils.createSubcontext((Context) null, name);
            fail(IAE);
        } catch (IllegalArgumentException e) {
            // should land here
        }

        try {
            JNDIUtils.createSubcontext(initialContext, (Name) null);
            fail(IAE);
        } catch (IllegalArgumentException e) {
            // should land here
        }

        if (WARNING) {
            fail("did not test invalidname");
        }
    }

    /**
     * Test that the createSubcontex(Context,String) throws the correct exceptions.
     *
     * @throws Exception if any exception occurs
     */
    public void testCreateSubcontextFromContextAndString()
        throws Exception {
        String name = "com/topcoder/config";
        Context initialContext = new InitialContext();

        try {
            JNDIUtils.createSubcontext((Context) null, name);
            fail(IAE);
        } catch (IllegalArgumentException e) {
            // should land here
        }

        try {
            JNDIUtils.createSubcontext(initialContext, (String) null);
            fail(IAE);
        } catch (IllegalArgumentException e) {
            // should land here
        }

        if (WARNING) {
            fail("did not test invalidname");
        }
    }

    /**
     * Test that the createSubcontex(String) throws the correct exceptions.
     *
     * @throws Exception if any exception occurs
     */
    public void testCreateSubcontextFromString() throws Exception {
        try {
            JNDIUtils.createSubcontext((String) null);
            fail(IAE);
        } catch (IllegalArgumentException e) {
            // should land here
        }

        if (WARNING) {
            fail("did not test invalidname");
        }
    }

    /**
     * Test that the createSubcontex(Name) throws the correct exceptions.
     *
     * @throws Exception if any exception occurs
     */
    public void testCreateSubcontextFromName() throws Exception {
        try {
            JNDIUtils.createSubcontext((Name) null);
            fail(IAE);
        } catch (IllegalArgumentException e) {
            // should land here
        }

        if (WARNING) {
            fail("did not test invalidname");
        }
    }

    /**
     * Test that the dump(Context,ContextRenderer,boolean) throws the correct exceptions.
     *
     * @throws Exception if any exception occurs
     */
    public void testDumpContext_ContextRenderer_Boolean()
        throws Exception {
        Context initialContext = new InitialContext();
        ContextRenderer contextRenderer = new ContextConsoleRenderer();

        try {
            JNDIUtils.dump((Context) null, contextRenderer, true);
            fail(IAE);
        } catch (IllegalArgumentException e) {
            // should land here
        }

        try {
            JNDIUtils.dump(initialContext, (ContextRenderer) null, true);
            fail(IAE);
        } catch (IllegalArgumentException e) {
            // should land here
        }

        if (WARNING) {
            fail("did not test invalidname");
        }
    }

    /**
     * Test that the dump(Context,Name,ContextRenderer,boolean) throws the correct exceptions.
     *
     * @throws Exception if any exception occurs
     */
    public void testDumpContext_Name_ContextRenderer_Boolean()
        throws Exception {
        Context context = JNDIUtils.getDefaultContext();
        ContextRenderer contextRenderer = new ContextConsoleRenderer();
        Name name = JNDIUtils.createName("com.topcoder", '.');

        try {
            JNDIUtils.dump((Context) null, name, contextRenderer, true);
            fail(IAE);
        } catch (IllegalArgumentException e) {
            // should land here
        }

        try {
            JNDIUtils.dump(context, (Name) null, (ContextRenderer) null, true);
            fail(IAE);
        } catch (IllegalArgumentException e) {
            // should land here
        }

        try {
            JNDIUtils.dump(context, name, (ContextRenderer) null, true);
            fail(IAE);
        } catch (IllegalArgumentException e) {
            // should land here
        }

        if (WARNING) {
            fail("did not test invalidname");
        }
    }

    /**
     * Test that the dump(ContextRenderer,boolean) throws the correct exceptions.
     *
     * @throws Exception if any exception occurs
     */
    public void testDumpContextRenderer_Boolean() throws Exception {
        try {
            JNDIUtils.dump((ContextRenderer) null, true);
            fail(IAE);
        } catch (IllegalArgumentException e) {
            // should land here
        }

        if (WARNING) {
            fail("did not test invalidname");
        }
    }

    /**
     * Test that the dump(Context,String,ContextRenderer,boolean) throws the correct exceptions.
     *
     * @throws Exception if any exception occurs
     */
    public void testDumpContext_String_ContextRenderer_Boolean()
        throws Exception {
        Context context = JNDIUtils.getDefaultContext();
        ContextRenderer contextRenderer = new ContextConsoleRenderer();
        String name = "com.topcoder";

        try {
            JNDIUtils.dump((Context) null, name, contextRenderer, true);
            fail(IAE);
        } catch (IllegalArgumentException e) {
            // should land here
        }

        try {
            JNDIUtils.dump(context, (String) null, (ContextRenderer) null, true);
            fail(IAE);
        } catch (IllegalArgumentException e) {
            // should land here
        }

        try {
            JNDIUtils.dump(context, name, (ContextRenderer) null, true);
            fail(IAE);
        } catch (IllegalArgumentException e) {
            // should land here
        }

        if (WARNING) {
            fail("did not test invalidname");
        }
    }

    /**
     * Test that the dump(String,ContextRenderer,boolean) throws the correct exceptions.
     *
     * @throws Exception if any exception occurs
     */
    public void testDumpString_ContextRenderer_Boolean()
        throws Exception {
        ContextRenderer contextRenderer = new ContextConsoleRenderer();
        String name = "com.topcoder";

        try {
            JNDIUtils.dump((String) null, contextRenderer, true);
            fail(IAE);
        } catch (IllegalArgumentException e) {
            // should land here
        }

        try {
            JNDIUtils.dump(name, (ContextRenderer) null, true);
            fail(IAE);
        } catch (IllegalArgumentException e) {
            // should land here
        }

        if (WARNING) {
            fail("did not test invalidname");
        }
    }

    /**
     * Test that the dump(Name,ContextRenderer,boolean) throws the correct exceptions.
     *
     * @throws Exception if any exception occurs
     */
    public void testDumpName_ContextRenderer_Boolean()
        throws Exception {
        ContextRenderer contextRenderer = new ContextConsoleRenderer();
        Name name = new CompoundName("com.topcoder", new Properties());

        try {
            JNDIUtils.dump((Name) null, contextRenderer, true);
            fail(IAE);
        } catch (IllegalArgumentException e) {
            // should land here
        }

        try {
            JNDIUtils.dump(name, (ContextRenderer) null, true);
            fail(IAE);
        } catch (IllegalArgumentException e) {
            // should land here
        }

        if (WARNING) {
            fail("did not test invalidname");
        }
    }

    /**
     * Test that the getConnection(Context,Name) method throws the correct exceptions.
     *
     * @throws Exception if any exception occurs
     */
    public void testGetConnectionContextName() throws Exception {
        Context context = JNDIUtils.getDefaultContext();
        Name name = new CompoundName("jdbc:something", new Properties());

        try {
            JNDIUtils.getConnection((Context) null, name);
            fail(IAE);
        } catch (IllegalArgumentException e) {
            // should land here
        }

        try {
            JNDIUtils.getConnection(context, (Name) null);
            fail(IAE);
        } catch (IllegalArgumentException e) {
            // should land here
        }

        if (WARNING) {
            fail("did not test SQL exception");
            fail("did not test Naming exception");
        }
    }

    /**
     * Test that the getConnection(Context,String) method throws the correct exceptions.
     *
     * @throws Exception if any exception occurs
     */
    public void testGetConnectionContextString() throws Exception {
        Context context = JNDIUtils.getDefaultContext();
        String name = "jdbc:something";

        try {
            JNDIUtils.getConnection((Context) null, name);
            fail(IAE);
        } catch (IllegalArgumentException e) {
            // should land here
        }

        try {
            JNDIUtils.getConnection(context, (String) null);
            fail(IAE);
        } catch (IllegalArgumentException e) {
            // should land here
        }

        if (WARNING) {
            fail("did not test SQL exception");
            fail("did not test Naming exception");
        }
    }

    /**
     * Test that the getConnection(String) method throws the correct exceptions.
     *
     * @throws Exception if any exception occurs
     */
    public void testGetConnectionString() throws Exception {
        try {
            JNDIUtils.getConnection((String) null);
            fail(IAE);
        } catch (IllegalArgumentException e) {
            // should land here
        }

        if (WARNING) {
            fail("did not test SQL exception");
            fail("did not test Naming exception");
        }
    }

    /**
     * Test that the getConnection(Name) method throws the correct exceptions.
     *
     * @throws Exception if any exception occurs
     */
    public void testGetConnectionName() throws Exception {
        try {
            JNDIUtils.getConnection((Name) null);
            fail(IAE);
        } catch (IllegalArgumentException e) {
            // should land here
        }

        if (WARNING) {
            fail("did not test SQL exception");
            fail("did not test Naming exception");
        }
    }

    /**
     * Test that the getContext(String) method throws the correct exceptions.
     *
     * @throws Exception if any exception occurs
     */
    public void testGetContextString() throws Exception {
        try {
            JNDIUtils.getContext("   ");
            fail(IAE);
        } catch (IllegalArgumentException e) {
            // should land here
        }

        try {
            JNDIUtils.getContext((String) null);
            fail(IAE);
        } catch (IllegalArgumentException e) {
            // should land here
        }

        try {
            JNDIUtils.getContext("NOFACTORY");
            fail(NE);
        } catch (NamingException e) {
            // should land here
        }
    }

    /**
     * Test that the getDefaultContext() method throws the correct exceptions).
     */
    public void testGetDefaultContext() {
        if (WARNING) {
            fail("no test code");
        }
    }

    /**
     * Test that the getObject(Context,Name) throws the correct exceptions.
     *
     * @throws Exception if any exception occurs
     */
    public void testGetObjectContextName() throws Exception {
        Context context = JNDIUtils.getDefaultContext();

        Name name = JNDIUtils.createName("topic");
        Name notInContext = JNDIUtils.createName("does_not_exist");
        context.bind("topic", new TopicImpl("topic"));

        //Test normal use
        JNDIUtils.getObject(context, name);

        try {
            JNDIUtils.getObject((Context) null, name);
            fail(IAE);
        } catch (IllegalArgumentException e) {
            // should land here
        }

        try {
            JNDIUtils.getObject(context, (Name) null);
            fail(IAE);
        } catch (IllegalArgumentException e) {
            // should land here
        }

        try {
            JNDIUtils.getObject(context, notInContext);
            fail(NE);
        } catch (NamingException e) {
            // should land here
        }

        context.unbind("topic");

        if (WARNING) {
            fail("check directory structure");
        }
    }

    /**
     * Test that the getObject(Context,String) throws the correct exceptions.
     *
     * @throws Exception if any exception occurs
     */
    public void testGetObjectContextString() throws Exception {
        Context context = JNDIUtils.getDefaultContext();
        String name = JNDIUtils.createName(context, "topic").toString();
        String notInContext = JNDIUtils.createName(context, "does_not_exist.txt").toString();
        context.bind("topic", new TopicImpl("topic"));

        //Test normal use
        JNDIUtils.getObject(context, name);

        try {
            JNDIUtils.getObject((Context) null, name);
            fail(IAE);
        } catch (IllegalArgumentException e) {
            // should land here
        }

        try {
            JNDIUtils.getObject(context, (String) null);
            fail(IAE);
        } catch (IllegalArgumentException e) {
            // should land here
        }

        try {
            JNDIUtils.getObject(context, notInContext);
            fail(NE);
        } catch (NamingException e) {
            // should land here
        }

        context.unbind("topic");

        if (WARNING) {
            fail("check directory structure");
        }
    }

    /**
     * Test that the getObject(Context,Name,Class) throws the correct exceptions.
     *
     * @throws Exception if any exception occurs
     */
    public void testGetObjectContextNameClass() throws Exception {
        Context context = JNDIUtils.getDefaultContext();
        Name name = JNDIUtils.createName(context, "topic");
        Name notInContext = JNDIUtils.createName(context, "does_not_exist.txt");
        context.bind("topic", new TopicImpl("topic"));

        Class clazz = File.class;

        //Test normal use
        JNDIUtils.getObject(context, name);

        try {
            JNDIUtils.getObject((Context) null, name, clazz);
            fail(IAE);
        } catch (IllegalArgumentException e) {
            // should land here
        }

        try {
            JNDIUtils.getObject(context, (Name) null, clazz);
            fail(IAE);
        } catch (IllegalArgumentException e) {
            // should land here
        }

        try {
            JNDIUtils.getObject(context, name, (Class) null);
            fail(IAE);
        } catch (IllegalArgumentException e) {
            // should land here
        }

        try {
            JNDIUtils.getObject(context, notInContext, File.class);
            fail(NE);
        } catch (NamingException e) {
            // should land here
        }

        try {
            JNDIUtils.getObject(context, name, String.class);
            fail(NE);
        } catch (ClassCastException e) {
            // should land here
        }

        context.unbind("topic");

        if (WARNING) {
            fail("check directory structure");
        }
    }

    /**
     * Test that the getObject(Context,String,Class) throws the correct exceptions.
     *
     * @throws Exception if any exception occurs
     */
    public void testGetObjectContextStringClass() throws Exception {
        Context context = JNDIUtils.getDefaultContext();
        context.bind("topic", new TopicImpl("topic"));

        String name = JNDIUtils.createName(context, "topic").toString();
        String notInContext = JNDIUtils.createName(context, "does_not_exist").toString();
        Class clazz = Topic.class;

        //Test normal use
        JNDIUtils.getObject(context, name);

        try {
            JNDIUtils.getObject((Context) null, name, clazz);
            fail(IAE);
        } catch (IllegalArgumentException e) {
            // should land here
        }

        try {
            JNDIUtils.getObject(context, (String) null, clazz);
            fail(IAE);
        } catch (IllegalArgumentException e) {
            // should land here
        }

        try {
            JNDIUtils.getObject(context, name, (Class) null);
            fail(IAE);
        } catch (IllegalArgumentException e) {
            // should land here
        }

        try {
            JNDIUtils.getObject(context, notInContext, File.class);
            fail(NE);
        } catch (NamingException e) {
            // should land here
        }

        try {
            JNDIUtils.getObject(context, name, String.class);
            fail(NE);
        } catch (ClassCastException e) {
            // should land here
        }

        context.unbind("topic");

        if (WARNING) {
            fail("check directory structure");
        }
    }

    /**
     * Test that the getObject(Name) throws the correct exceptions.
     *
     * @throws NamingException if a NamingException occurs
     */
    public void testGetObjectString() throws NamingException {
        try {
            JNDIUtils.getObject((String) null);
            fail(IAE);
        } catch (IllegalArgumentException e) {
            // should land here
        }

        if (WARNING) {
            fail("not complete");
        }
    }

    /**
     * Test that the getObject(Name,Class) throws the correct exceptions.
     *
     * @throws NamingException if a NamingException occurs
     */
    public void testGetObjectNameClass() throws NamingException {
        Name name = JNDIUtils.createName("test");

        try {
            JNDIUtils.getObject((Name) null, String.class);
            fail(IAE);
        } catch (IllegalArgumentException e) {
            // should land here
        }

        try {
            JNDIUtils.getObject(name, null);
            fail(IAE);
        } catch (IllegalArgumentException e) {
            // should land here
        }

        if (WARNING) {
            fail("not complete");
        }
    }

    /**
     * Test that the getObject(String,Class) throws the correct exceptions.
     *
     * @throws NamingException if a NamingException occurs
     */
    public void testGetObjectStringClass() throws NamingException {
        String name = "test";

        try {
            JNDIUtils.getObject((String) null, String.class);
            fail(IAE);
        } catch (IllegalArgumentException e) {
            // should land here
        }

        try {
            JNDIUtils.getObject(name, null);
            fail(IAE);
        } catch (IllegalArgumentException e) {
            // should land here
        }

        if (WARNING) {
            fail("not complete");
        }
    }

    /**
     * Test that the getQueue(Context,Name) throws the correct exceptions.
     *
     * @throws Exception if any exception occurs
     */
    public void testGetQueueContextName() throws Exception {
        Context context = JNDIUtils.getDefaultContext();
        Name name = JNDIUtils.createName(context, "dirFile.txt");

        try {
            JNDIUtils.getQueue((Context) null, name);
        } catch (IllegalArgumentException e) {
            // should land here
        }

        try {
            JNDIUtils.getQueue(context, (Name) null);
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test that the getQueue(Context,String) throws the correct exceptions.
     *
     * @throws Exception if any exception occurs
     */
    public void testGetQueueContextString() throws Exception {
        Context context = JNDIUtils.getDefaultContext();
        String name = "dirFile.txt";

        try {
            JNDIUtils.getQueue((Context) null, name);
        } catch (IllegalArgumentException e) {
            // should land here
        }

        try {
            JNDIUtils.getQueue(context, (String) null);
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test that the getQueue(Name) throws the correct exceptions.
     *
     * @throws Exception if any exception occurs
     */
    public void testGetQueueName() throws Exception {
        try {
            JNDIUtils.getQueue((Name) null);
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test that the getQueue(String) throws the correct exceptions.
     *
     * @throws Exception if any exception occurs
     */
    public void testGetQueueString() throws Exception {
        try {
            JNDIUtils.getQueue((String) null);
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test that the getTopic(Context,Name) throws the correct exceptions.
     *
     * @throws Exception if any exception occurs
     */
    public void testGetTopicContextName() throws Exception {
        Context context = JNDIUtils.getDefaultContext();
        Name name = JNDIUtils.createName(context, "dirFile.txt");

        try {
            JNDIUtils.getTopic((Context) null, name);
        } catch (IllegalArgumentException e) {
            // should land here
        }

        try {
            JNDIUtils.getTopic(context, (Name) null);
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test that the getTopic(Context,String) throws the correct exceptions.
     *
     * @throws Exception if any exception occurs
     */
    public void testGetTopicContextString() throws Exception {
        Context context = JNDIUtils.getDefaultContext();
        String name = "dirFile.txt";

        try {
            JNDIUtils.getTopic((Context) null, name);
        } catch (IllegalArgumentException e) {
            // should land here
        }

        try {
            JNDIUtils.getTopic(context, (String) null);
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test that the getTopic(Name) throws the correct exceptions.
     *
     * @throws Exception if any exception occurs
     */
    public void testGetTopicName() throws Exception {
        try {
            JNDIUtils.getTopic((Name) null);
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test that the getTopic(String) throws the correct exceptions.
     *
     * @throws Exception if any exception occurs
     */
    public void testGetTopicString() throws Exception {
        try {
            JNDIUtils.getTopic((String) null);
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test that the saveContextConfig(String,Properties) throws the correct exceptions.
     *
     * @throws Exception if any exception occurs
     */
    public void testSaveContextConfig() throws Exception {
        try {
            JNDIUtils.saveContextConfig(" ", new Properties());
            fail(IAE);
        } catch (IllegalArgumentException e) {
            // should land here
        }

        try {
            JNDIUtils.saveContextConfig((String) null, new Properties());
            fail(IAE);
        } catch (IllegalArgumentException e) {
            // should land here
        }

        try {
            JNDIUtils.saveContextConfig("name", (Properties) null);
            fail(IAE);
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }
}
