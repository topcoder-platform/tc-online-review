/*
 * Copyright (C) 2003 TopCoder Inc., All Rights Reserved.
 *
 * @(#) JNDIUtils.java
 *
 * 1.0  05/14/2003
 */
package com.topcoder.naming.jndiutility;

import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.config.ConfigManagerException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;

import javax.jms.Queue;
import javax.jms.Topic;
import javax.naming.CompoundName;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NameClassPair;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.sql.DataSource;


/**
 * <p>A utility class providing the methods manipulating with JNDI Contexts. Contains methods that allow to
 * maintain the list of predefined JNDI Contexts through the configuration files, dump the bindings in Context to
 * XML Document, get resources from Context, manipulate with Names and Strings.</p>
 *  <p><strong>Thread-Safety</strong></p>
 *  <p>This class has only final static methods, so mutability is not an issue.</p>
 *  <p><strong>Basic Usage Demo</strong></p>
 * <pre>
 * // Almost all methods have overloaded versions for both given context and default initial context
 * // and both java.lang.String and javax.naming.Name names can be used
 * // Get the default context which initial parameters are stored in configuration file under context.default name
 * Context ctx = JNDIUtils.getDefaultContext();
 *
 * // Create given subcontext within given Context
 * ctx = JNDIUtils.createSubcontext(ctx, "dir");
 *
 * // Store initial parameters of Context in configuration file for further use.
 * Properties props = new Properties();
 * props.put("factory", "com.sun.jndi.fscontext.RefFSContextFactory");
 * props.put("url", "file:test_files");
 * JNDIUtils.saveContextConfig("new_context", props);
 *
 * // Create context using initial parameters from configuration file.
 * ctx = JNDIUtils.getContext("new_context");
 *
 * ctx = JNDIUtils.getDefaultContext();
 *
 * // Get the resources from Context
 * Queue queue = JNDIUtils.getQueue(ctx, "MyQueue");
 *
 * Topic topic = JNDIUtils.getTopic(ctx, "MyTopic");
 *
 * Connection con = JNDIUtils.getConnection(ctx, "MyDataSource");
 *
 * // Get object from context
 * Object object = JNDIUtils.getObject(ctx, "dir");
 *
 * // Get object verifying that it can be cast to specified class
 * object = JNDIUtils.getObject(ctx, "dir/dirfile.txt", File.class);
 *
 * // Get the XML Document object corresponding to some subcontext of Context without traversing
 * // through nested subcontexts
 * ContextRenderer renderer = new ContextXMLRenderer();
 * JNDIUtils.dump(ctx, "dir", renderer, false);
 *
 * Document doc = ((ContextXMLRenderer) renderer).getDocument();
 *
 * // Print the XML Document object corresponding to Context traversing through nested subcontexts
 * // to console
 * renderer = new ContextConsoleRenderer();
 * JNDIUtils.dump(ctx, renderer, true);
 *
 * // Convert a String to Name object that is a compound name separated with slashes
 * Name name = JNDIUtils.createName("com.topcoder.util.config", '.');
 *
 * // Convert a Name to String separated with any desired character
 * String string = JNDIUtils.createString(name, '?');
 *
 * // Convert a String to Name object with the rule of given context
 * name = JNDIUtils.createName(ctx, "directory/file");
 *
 * // Main method
 * ////////////////////////////////////////////////////////////////////////////////////////////////
 * // In order to dump a Context to standard output using command line interface following       //
 * // command should be typed :                                                                  //
 * // java com.topcoder.naming.jndiutility.JNDIUtils [-d] contextname [subcontext], where:       //
 * // -d - is an optional switch specifying that nested subcontexts should be traversed          //
 * //      too. If is not specified  then directly nested subcontexts will be represented        //
 * //      like a simple bindings and indirectly nested subcontexts will not be dumped           //
 * //      at all.                                                                               //
 * // contextname - a name of context specified in configuration file                            //
 * // subcontext - an optional name of subcontext to dump                                        //
 * ////////////////////////////////////////////////////////////////////////////////////////////////
 * // dump the test context with the nested subcontexts to be traversed
 * JNDIUtils.main(new String[] { "-d", "test", "." });
 * </pre>
 *
 * @author isv
 * @author preben
 * @version 1.0 08/09/2003
 */
public class JNDIUtils {
    /** A namespace owned by <code>JNDI Context Utility</code> component within Configuration Manager. */
    public static final String NAMESPACE = "com.topcoder.naming.jndiutility";

    /** Maximum number of arguments in main(). */
    private static final int MAX_ARGS_NUM = 3;

    /** A <code>Configuration Manager</code> used to maintain the contexts properties. */
    private static ConfigManager cm = ConfigManager.getInstance();

    /**
     * A private constructor disabling instantiation of <code>JNDIUtils</code> and making it truly utility class.
     */
    private JNDIUtils() {
    }

    /**
     * Gets the compound name consisting of components of given <code>String</code> and separated by slashes.
     * For example: <code>createName("com.topcoder.util.config", '.')</code> will return the <code>Name</code>
     * consisting of 4 components and with string representation as <code>com/topcoder/util/config</code>.
     *
     * @param name a source name to get slash-version of
     * @param sep a character separating the components within the source name
     *
     * @return a <code>CompoundName</code> consisting of components from source String and separated with slashes
     *
     * @throws InvalidNameException if given name does not conform to naming syntax
     * @throws IllegalArgumentException if given <code>name</code> is <code> null</code>
     */
    public static Name createName(String name, char sep)
        throws InvalidNameException {
        Helper.checkObject(name, "name");

        if (Character.isSpace(sep)) {
            throw new InvalidNameException("whitespace chars is illegal as separators.");
        }

        // Set the syntax properties
        Properties syntax = new Properties();
        syntax.setProperty("jndi.syntax.direction", "left_to_right");

        // Change the separator
        syntax.setProperty("jndi.syntax.separator", "/");

        CompoundName compoundName = new CompoundName(name.replace(sep, '/'), syntax);

        return compoundName;
    }

    /**
     * Gets the <code>String</code> representation of given <code>Name</code> consisting of components of the
     * given <code>Name</code> separated with given character.
     *
     * @param name a source <code>Name</code> to get <code>String</code> equivalent of
     * @param sep a character separating the components within the result name
     *
     * @return a <code>String</code> equivalent of given <code>Name</code>
     *
     * @throws IllegalArgumentException if given <code>Name</code> is <code> null</code>
     */
    public static String createString(Name name, char sep) {
        Helper.checkObject(name, "name");

        // Set the syntax properties
        Properties syntax = new Properties();
        syntax.setProperty("jndi.syntax.direction", "left_to_right");
        syntax.setProperty("jndi.syntax.separator", String.valueOf(sep));

        return (new ExtendedCompoundName(name.getAll(), syntax)).toString();
    }

    /**
     * Creates a subcontext specified with given name within given Context. Recursively creates all
     * intermediate contexts if they do not exist. If specified subcontext already exists within given Context then
     * simply returns this subcontext.
     *
     * @param ctx a source <code>Context</code>
     * @param name a <code>String</code> representing the target subcontext
     *
     * @return a <code>Context</code> represented by given name and nested within specified context
     *
     * @throws NamingException if any naming exception occurs
     * @throws IllegalArgumentException if any of given <code>Context</code>, <code>name</code> is <code>null</code>
     *         or empty after parsing
     * @throws NameAlreadyBoundException if the given name is already bound with a non-context object in the given
     *         context
     */
    public static Context createSubcontext(Context ctx, String name)
        throws NamingException {
        Helper.checkObject(ctx, "ctx");
        Helper.checkObject(name, "name");

        NameParser parser = ctx.getNameParser("");
        Name parsedName = parser.parse(name);

        return createSubcontext(ctx, parsedName);
    }

    /**
     * Creates a subcontext specified with given name within given Context only if it does not exist
     * otherwise just returns a specified Context. Recursively creates all intermediate contexts if they do not
     * exist. If specified subcontext already exists within given Context then simply returns this subcontext.
     *
     * @param ctx a source <code>Context</code>
     * @param name a <code>Name</code> representing the target context
     *
     * @return a <code>Context</code> represented by given name and nested within specified context
     *
     * @throws NamingException if any naming exception occurs
     * @throws IllegalArgumentException if any of given <code>Context</code>, <code>name</code> is <code>null</code>
     *         or if the <code>name</code> is empty.
     * @throws NameAlreadyBoundException if the given name is already bound with a non-context object in the given
     *         context
     */
    public static Context createSubcontext(Context ctx, Name name)
        throws NamingException {
        Helper.checkObject(ctx, "ctx");
        Helper.checkObject(name, "name");

        if (name.isEmpty()) {
            throw new IllegalArgumentException("an empty name is not allowed");
        }

        Context rootContext = null;

        try {
            rootContext = (Context) ctx.lookup(name);
        } catch (NamingException e) {
            // skip
        } catch (ClassCastException e) {
            throw new NameAlreadyBoundException(name + " is bound but is not a context");
        }

        if (rootContext != null) {
            // The context exists
            return rootContext;
        } else {
            // Create the context
            Enumeration componentsOfName = name.getAll();
            Context currentContext = ctx;

            // Iterate over the components in the name
            while (componentsOfName.hasMoreElements()) {
                String component = (String) componentsOfName.nextElement();

                // Lookup the name and check that it is not bound
                Object nextContext = null;

                try {
                    nextContext = currentContext.lookup(component);
                } catch (NamingException e) {
                    // skip
                }

                if (nextContext == null) {
                    currentContext = currentContext.createSubcontext(component);
                } else if (nextContext instanceof Context) {
                    currentContext = (Context) nextContext;
                } else {
                    throw new NameAlreadyBoundException(name + " is bound but is not a context");
                }
            }

            return currentContext;
        }
    }

    /**
     * Gets the object specified by given name from initial context. The object is returned as it is.
     *
     * @param name the name of the object to look up in initial context
     *
     * @return an object bound under given name within initial context
     *
     * @throws NamingException if a naming exception is encountered
     */
    public static Object getObject(String name) throws NamingException {
        return getObject(new InitialContext(), name);
    }

    /**
     * Gets the object specified by given name from initial context. The object is returned as it is.
     *
     * @param name the name of the object to look up in initial context
     *
     * @return an object bound under given name within initial context
     *
     * @throws NamingException if a naming exception is encountered
     * @throws IllegalArgumentException if <code>name</code> is <code>null</code>.
     */
    public static Object getObject(Name name) throws NamingException {
        return getObject(new InitialContext(), name);
    }

    /**
     * Gets the object specified by given name from initial context. It is guaranteed that the returned
     * object can be cast to type specified by given <code>Class</code>.
     *
     * @param name the name of the object to look up in initial context
     * @param clazz a <code>Class</code> to check the cast to
     *
     * @return an object bound under given name within initial context that can be cast to specified type
     *
     * @throws NamingException if a naming exception is encountered
     * @throws IllegalArgumentException if <code>name</code> or <code>clazz</code>is <code>null</code>.
     * @throws ClassCastException if object bound under given name cannot be cast to given class
     */
    public static Object getObject(String name, Class clazz)
        throws NamingException {
        return getObject(new InitialContext(), name, clazz);
    }

    /**
     * Gets the object specified by given name from initial context. It is guaranteed that the returned
     * object can be cast to type specified by given <code>Class</code>.
     *
     * @param name the name of the object to look up in initial context
     * @param clazz a <code>Class</code> to check the cast to
     *
     * @return an object bound under given name within initial context that can be cast to specified type
     *
     * @throws NamingException if a naming exception is encountered
     * @throws IllegalArgumentException if <code>name</code> or <code>clazz</code>is <code>null</code>
     * @throws ClassCastException if object bound under given name cannot be cast to given class
     */
    public static Object getObject(Name name, Class clazz)
        throws NamingException {
        return getObject(new InitialContext(), name, clazz);
    }

    /**
     * Gets the object specified by given name from given context. The object is returned as it is.
     *
     * @param ctx a target Context
     * @param name the name of the object to look up in initial context
     *
     * @return an object bound under given name within initial context
     *
     * @throws NamingException if a naming exception is encountered
     * @throws IllegalArgumentException if any of given <code>ctx</code> or <code>name</code> is <code>null</code>
     */
    public static Object getObject(Context ctx, String name)
        throws NamingException {
        Helper.checkObject(ctx, "ctx");
        Helper.checkObject(name, "name");

        return ctx.lookup(name);
    }

    /**
     * Gets the object specified by given name from given context. The object is returned as it is.
     *
     * @param ctx a target Context
     * @param name the name of the object to look up in initial context
     *
     * @return an object bound under given name within initial context
     *
     * @throws NamingException if a naming exception is encountered
     * @throws IllegalArgumentException if any of given <code>ctx</code> or <code>name</code> is <code>null</code>
     */
    public static Object getObject(Context ctx, Name name)
        throws NamingException {
        Helper.checkObject(ctx, "ctx");
        Helper.checkObject(name, "name");

        return ctx.lookup(name);
    }

    /**
     * Gets the object specified by given name from given context. It is guaranteed that the returned object
     * can be cast to type specified by given <code>Class</code>.
     *
     * @param ctx a target Context
     * @param name the name of the object to look up in given context
     * @param clazz a <code>Class</code> to check the cast to
     *
     * @return an object bound under given name within initial context
     *
     * @throws NamingException if a naming exception is encountered
     * @throws IllegalArgumentException if any of given <code>ctx</code> or <code>name</code> or <code>clazz</code>
     *         is <code>null</code>
     * @throws ClassCastException if object bound under given name cannot be cast to given class
     */
    public static Object getObject(Context ctx, String name, Class clazz)
        throws NamingException {
        Helper.checkObject(ctx, "ctx");
        Helper.checkObject(name, "name");
        Helper.checkObject(clazz, "clazz");

        Object object = ctx.lookup(name);

        if (clazz.isInstance(object)) {
            return object;
        }

        throw new ClassCastException();
    }

    /**
     * Gets the object specified by given name from given context. It is guaranteed that the returned object
     * can be cast to type specified by given <code>Class</code>.
     *
     * @param ctx a target Context
     * @param name the name of the object to look up in given context
     * @param clazz a <code>Class</code> to check the cast to
     *
     * @return an object bound under given name within initial context
     *
     * @throws NamingException if a naming exception is encountered
     * @throws IllegalArgumentException if any of given <code>ctx</code> or <code>name</code> or <code>clazz</code>
     *         is <code>null</code>
     * @throws ClassCastException if object bound under given name cannot be cast to given class
     */
    public static Object getObject(Context ctx, Name name, Class clazz)
        throws NamingException {
        Helper.checkObject(ctx, "ctx");
        Helper.checkObject(name, "name");
        Helper.checkObject(clazz, "clazz");

        Object object = ctx.lookup(name);

        if (clazz.isInstance(object)) {
            return object;
        }

        throw new ClassCastException();
    }

    /**
     * Gets the JMS <code>Queue</code> from given Context.
     *
     * @param ctx a source Context
     * @param name a name of target Queue
     *
     * @return a <code>Queue</code> specified by given name obtained from given Context. If the object bound to given
     *         name is not a <code>Queue</code> then return <code>null</code>
     *
     * @throws NamingException if any naming exception (for example, NameNotFoundException) occurs
     * @throws IllegalArgumentException if any of given <code>name</code> or <code>ctx</code> is <code>null</code>
     */
    public static Queue getQueue(Context ctx, String name)
        throws NamingException {
        Helper.checkObject(ctx, "ctx");
        Helper.checkObject(name, "name");

        Object possibleQueue = ctx.lookup(name);

        if (possibleQueue instanceof Queue) {
            return (Queue) possibleQueue;
        }

        return null;
    }

    /**
     * Gets the JMS <code>Queue</code> from given Context.
     *
     * @param ctx a source Context
     * @param name a name of target Queue
     *
     * @return a <code>Queue</code> specified by given name obtained from given Context. If the object bound to given
     *         name is not a <code>Queue</code> then return <code>null</code>
     *
     * @throws NamingException if any naming exception (for example, NameNotFoundException) occurs
     * @throws IllegalArgumentException if any of given <code>name</code> or <code>ctx</code> is <code>null</code>
     */
    public static Queue getQueue(Context ctx, Name name)
        throws NamingException {
        Helper.checkObject(ctx, "ctx");
        Helper.checkObject(name, "name");

        Object possibleQueue = ctx.lookup(name);

        if (possibleQueue instanceof Context) {
            throw new NamingException("name is bound with a context");
        }

        if (possibleQueue instanceof Queue) {
            return (Queue) possibleQueue;
        }

        return null;
    }

    /**
     * Gets the JMS <code>Topic</code> from given Context.
     *
     * @param ctx a source Context
     * @param name a name of target Topic
     *
     * @return a <code>Topic</code> specified by given name obtained from given Context. If the object bound to given
     *         name is not a <code>Topic</code> then return <code>null</code>
     *
     * @throws NamingException if any naming exception (for example, NameNotFoundException) occurs
     * @throws IllegalArgumentException if any of given <code>name</code> or <code>ctx</code> is <code>null</code>
     */
    public static Topic getTopic(Context ctx, String name)
        throws NamingException {
        Helper.checkObject(ctx, "ctx");
        Helper.checkObject(name, "name");

        Object possibleTopic = ctx.lookup(name);

        if (possibleTopic instanceof Topic) {
            return (Topic) possibleTopic;
        }

        return null;
    }

    /**
     * Gets the JMS <code>Topic</code> from given Context.
     *
     * @param ctx a source Context
     * @param name a name of target Topic
     *
     * @return a <code>Topic</code> specified by given name obtained from given Context. If the object bound to given
     *         name is not a <code>Topic</code> then return <code>null</code>
     *
     * @throws NamingException if any naming exception (for example, NameNotFoundException) occurs
     * @throws IllegalArgumentException if any of given <code>name</code> or <code>ctx</code> is <code>null</code>
     */
    public static Topic getTopic(Context ctx, Name name)
        throws NamingException {
        Helper.checkObject(ctx, "ctx");
        Helper.checkObject(name, "name");

        Object possibleTopic = ctx.lookup(name);

        if (possibleTopic instanceof Context) {
            throw new NamingException("name is bound with a context");
        }

        if (possibleTopic instanceof Topic) {
            return (Topic) possibleTopic;
        }

        return null;
    }

    /**
     * Gets the SQL <code>Connection</code> from <code>DataSource</code> specified by given name from given Context.
     *
     * @param ctx a source Context
     * @param name a name of target DataSource to get connection from
     *
     * @return a <code>Connection</code> obtained from <code>DataSource</code> specified by given name from given
     *         Context. If the object bound to given name is not a <code>DataSource</code> then return
     *         <code>null</code>
     *
     * @throws NamingException if any naming exception (for example, NameNotFoundException) occurs
     * @throws SQLException if any SQL error occurs
     * @throws IllegalArgumentException if any of given <code>name</code> or <code>ctx</code> is <code>null</code>
     */
    public static Connection getConnection(Context ctx, String name)
        throws NamingException, SQLException {
        Helper.checkObject(ctx, "ctx");
        Helper.checkObject(name, "name");

        Object possibleDataSource = ctx.lookup(name);

        if (possibleDataSource instanceof DataSource) {
            return ((DataSource) possibleDataSource).getConnection();
        }

        return null;
    }

    /**
     * Gets the SQL <code>Connection</code> from <code>DataSource</code> specified by given name from given Context.
     *
     * @param ctx a source Context
     * @param name a name of target DataSource to get connection from
     *
     * @return a <code>Connection</code> obtained from <code>DataSource</code> specified by given name from given
     *         Context. If the object bound to given name is not a <code>DataSource</code> then return
     *         <code>null</code>
     *
     * @throws NamingException if any naming exception (for example, NameNotFoundException) occurs
     * @throws SQLException if any SQL error occurs
     * @throws IllegalArgumentException if any of given <code>name</code> or <code>ctx</code> is <code>null</code>
     */
    public static Connection getConnection(Context ctx, Name name)
        throws NamingException, SQLException {
        Helper.checkObject(ctx, "ctx");
        Helper.checkObject(name, "name");

        Object possibleDataSource = ctx.lookup(name);

        if (possibleDataSource instanceof Context) {
            throw new NamingException("name is bound with a context");
        }

        if (possibleDataSource instanceof DataSource) {
            return ((DataSource) possibleDataSource).getConnection();
        }

        return null;
    }

    /**
     * Creates the default context using the properties specified in configuration file for "default"
     * context. The names of properties of default context in configuration file are started with "context.default."
     *
     * @return a default Context created using the properties from configuration file
     *
     * @throws NamingException if any naming exception occurs
     * @throws ConfigManagerException if any exception related to Configuration Manager occurs
     */
    public static Context getDefaultContext() throws NamingException, ConfigManagerException {
        return getContext("default");
    }

    /**
     * <p>Creates the context using the properties specified in configuration file for context specified by
     * given name. The names of properties of context in configuration file are started with "context.&#60name&#62."
     * Two properties names "context.&#60name&#62.factory" and "context.&#60name&#62.url" are supported. The
     * required property for each context is a "factory" property. The "url" property is optional. The
     * "context.&#60name&#62.factory" property specifies the factory class used to create the Contexts. The
     * "context.&#60name&#62.url" property contains the URL connection string used to initialize Context and should
     * contain all needed info to initialize the Context.</p>
     * <p>
     * For example :
     * <pre>
     *     context.default.factory=com.sun.jndi.ldap.LdapCtxFactory
     *     context.default.url=ldap://ldap.OpenLDAP.org:389/cn=Peoples
     * </pre>
     * </p>
     *
     * @param name a name of requested Context within configuration file
     *
     * @return a Context created using the properties from configuration file
     *
     * @throws NamingException if any naming exception occurs or there is no factory is specified for given context
     * @throws ConfigManagerException if any exception related to Configuration Manager occurs
     * @throws IllegalArgumentException if given <code>name</code> is <code>null </code> or empty
     */
    public static Context getContext(String name) throws NamingException, ConfigManagerException {
        Helper.checkString(name, "name");

        Properties props = new Properties();
        name = "context." + name + ".";

        String factory = cm.getString(NAMESPACE, name + "factory");

        if (factory == null) {
            throw new NamingException("Factory not specified");
        }

        props.put(Context.INITIAL_CONTEXT_FACTORY, factory);

        String url = cm.getString(NAMESPACE, name + "url");

        if (url != null) {
            props.put(Context.PROVIDER_URL, url);
        }

        return new InitialContext(props);
    }

    /**
     * Stores the properties specifying the info needed to create and initialize the Context into
     * configuration file. All properties are stored with their names prefixed with "context.&#60name&#62."
     *
     * @param name a name of the Context within configuration file
     * @param props a Properties to store in configuration file
     *
     * @throws ConfigManagerException if any exception related to Configuration Manager occurs
     * @throws IllegalArgumentException if any of given <code>name</code> or <code>props</code> is <code>null</code>
     *         or given <code>name</code> is empty
     */
    public static void saveContextConfig(String name, Properties props)
        throws ConfigManagerException {
        Helper.checkString(name, "name");
        Helper.checkObject(props, "props");

        cm.createTemporaryProperties(NAMESPACE);
        name = "context." + name + ".";

        Enumeration enumeration = props.propertyNames();

        while (enumeration.hasMoreElements()) {
            String pname = (String) enumeration.nextElement();
            cm.setProperty(NAMESPACE, name + pname, (String) props.get(pname));
        }

        cm.commit(NAMESPACE, "JNDIUtils");
    }

    /**
     * Dumps the bindings under specified <code>Context</code>. Traverses through given Context and notifies
     * the given <code>ContextRenderer</code> of found contexts and bindings. The <code>ContextRenderer</code> is
     * responsible for further processing of the found Contexts and their bindings.
     *
     * @param ctx a target Context
     * @param renderer a <code>ContextRenderer</code> used to render the target Context
     * @param recurse a boolean specifying whether the subcontexts of target Context should be traversed too (if
     *        <code>true</code>) or not.
     *
     * @throws NamingException if any naming exception occurs
     * @throws IllegalArgumentException if any of given <code>ctx</code>, <code>ContextRenderer</code> is
     *         <code>null</code>
     */
    public static void dump(Context ctx, ContextRenderer renderer, boolean recurse)
        throws NamingException {
        Helper.checkObject(ctx, "ctx");
        Helper.checkObject(renderer, "renderer");

        dump(ctx, renderer, "", recurse);
    }

    /**
     * Dumps the bindings under specified subcontext of given <code>Context</code>. Traverses through given
     * subcontext and notifies the given <code>ContextRenderer</code> of found contexts and bindings. The
     * <code>ContextRenderer</code> is responsible for further processing of the found Contexts and their bindings.
     *
     * @param ctx a target Context
     * @param subCtx a name of subcontext within given Context that should be dumped
     * @param renderer a <code>ContextRenderer</code> used to render the target subcontext
     * @param recurse a boolean specifying whether the subcontexts of target subcontext should be traversed too (if
     *        <code>true</code>) or not.
     *
     * @throws NamingException if any naming exception occurs
     * @throws IllegalArgumentException if any of given <code>ctx</code>, <code>subCtx</code>,
     *         <code>ContextRenderer</code>, String is <code>null</code>
     */
    public static void dump(Context ctx, String subCtx, ContextRenderer renderer, boolean recurse)
        throws NamingException {
        Helper.checkObject(ctx, "ctx");
        Helper.checkObject(subCtx, "subCtx");
        Helper.checkObject(renderer, "renderer");

        try {
            Context subContext = (Context) ctx.lookup(subCtx);
            dump(subContext, renderer, "", recurse);
        } catch (ClassCastException e) {
            throw new NamingException("subCtx is not a context");
        }
    }

    /**
     * Dumps the bindings under specified subcontext of given <code>Context</code>. Traverses through given
     * subcontext and notifies the given <code>ContextRenderer</code> of found contexts and bindings. The
     * <code>ContextRenderer</code> is responsible for further processing of the found Contexts and their bindings.
     *
     * @param ctx a target Context
     * @param subCtx a name of subcontext within given Context that should be dumped
     * @param renderer a <code>ContextRenderer</code> used to render the target subcontext
     * @param recurse a boolean specifying whether the subcontexts of target subcontext should be tarversed too (if
     *        <code>true</code>) or not.
     *
     * @throws NamingException if any naming exception occurs
     * @throws IllegalArgumentException if any of given <code>ctx</code>, <code>subCtx</code>,
     *         <code>ContextRenderer</code>, Name is <code>null</code>
     */
    public static void dump(Context ctx, Name subCtx, ContextRenderer renderer, boolean recurse)
        throws NamingException {
        Helper.checkObject(ctx, "ctx");
        Helper.checkObject(subCtx, "subCtx");
        Helper.checkObject(renderer, "renderer");

        try {
            Context subContext = (Context) ctx.lookup(subCtx);
            dump(subContext, renderer, "", recurse);
        } catch (ClassCastException e) {
            throw new NamingException("subCtx is not a context");
        }
    }

    /**
     * Gets the Name representing the name specified by given <code>String </code> that is valid within given
     * Context.
     *
     * @param ctx a target Context
     * @param name a source name to get Name representation of
     *
     * @return a <code>Name</code> constructed from given String that conforms to naming syntax of given Context
     *
     * @throws NamingException if any naming exception (for example InvalidNameException)occurs
     * @throws IllegalArgumentException if given <code>name</code> or <code>ctx</code> is <code>null</code>
     */
    public static Name createName(Context ctx, String name)
        throws NamingException {
        Helper.checkObject(ctx, "ctx");
        Helper.checkObject(name, "name");

        // QQQQQQQ What about zero length String?
        // Ask in forum
        NameParser parser = ctx.getNameParser("");
        Name parsedName = parser.parse(name);

        return parsedName;
    }

    // -------- Methods to work with default initial context
    /**
     * Creates a subcontext specified with given name within initial context (created with
     * javax.naming.InitialContext() constructor). Recursivley creates all intermediate contexts if they do not
     * exist. If specified subcontext already exists within initial context then simply returns this subcontext.
     *
     * @param name a <code>String</code> representing the target subcontext
     *
     * @return a <code>Context</code> represented by given name and nested within initial context
     *
     * @throws NamingException if any naming exception occurs
     * @throws IllegalArgumentException if <code>name</code> is <code>null</code>.
     */
    public static Context createSubcontext(String name)
        throws NamingException {
        return createSubcontext(new InitialContext(), name);
    }

    /**
     * Creates a subcontext specified with given name within initial context (created with
     * javax.naming.InitialContext() constructor). only if it does not exist otheriwse just returns a specified
     * Context. Recursivley creates all intermediate contexts if they do not exist. If specified subcontext already
     * exists within initial context then simply returns this subcontext.
     *
     * @param name a <code>Name</code> representing the target context
     *
     * @return a <code>Context</code> represented by given name and nested within default initial context
     *
     * @throws NamingException if any naming exception occurs
     * @throws IllegalArgumentException if <code>name</code> is <code>null</code>.
     */
    public static Context createSubcontext(Name name) throws NamingException {
        return createSubcontext(new InitialContext(), name);
    }

    /**
     * Gets the Name representing the name specified by given <code>String </code> that is valid within
     * initial context (created with <code> javax.naming.InitialContext()</code> constructor).
     *
     * @param name a source name to get Name representation of
     *
     * @return a <code>Name</code> constructed from given String that conforms to naming syntax of initial context
     *
     * @throws NamingException if any naming exception (for example, InvalidNameException) occurs
     * @throws IllegalArgumentException if <code>name</code> is <code>null</code>.
     */
    public static Name createName(String name) throws NamingException {
        return createName(new InitialContext(), name);
    }

    /**
     * Gets the JMS <code>Topic</code> from initial context (created with <code> javax.naming.InitialContext()</code>
     * constructor).
     *
     * @param name a name of target <code>Topic</code>
     *
     * @return a <code>Topic</code> specified by given name obtained from initial context. If the object bound to given
     *         name is not a <code>Topic</code> then return <code>null</code>
     *
     * @throws NamingException if any naming exception (for example, NameNotFoundException) occurs
     * @throws IllegalArgumentException if <code>name</code> is <code>null</code>.
     */
    public static Topic getTopic(String name) throws NamingException {
        return getTopic(new InitialContext(), name);
    }

    /**
     * Gets the JMS <code>Topic</code> from initial context (created with <code> javax.naming.InitialContext()</code>
     * constructor).
     *
     * @param name a name of target <code>Topic</code>
     *
     * @return a <code>Topic</code> specified by given name obtained from initial context. If the object bound to given
     *         name is not a <code>Topic</code> then return <code>null</code>
     *
     * @throws NamingException if any naming exception (for example, NameNotFoundException) occurs
     * @throws IllegalArgumentException if <code>name</code> is <code>null</code>.
     */
    public static Topic getTopic(Name name) throws NamingException {
        return getTopic(new InitialContext(), name);
    }

    /**
     * Gets the JMS <code>Queue</code> from initial context (created with <code> javax.naming.InitialContext()</code>
     * constructor).
     *
     * @param name a name of target <code>Queue</code>
     *
     * @return a <code>Queue</code> specified by given name obtained from initial context. If the object bound to given
     *         name is not a <code>Queue</code> then return <code>null</code>
     *
     * @throws NamingException if any naming exception (for example, NameNotFoundException) occurs
     * @throws IllegalArgumentException if <code>name</code> is <code>null</code>.
     */
    public static Queue getQueue(Name name) throws NamingException {
        return getQueue(new InitialContext(), name);
    }

    /**
     * Gets the JMS <code>Queue</code> from initial context (created with <code> javax.naming.InitialContext()</code>
     * constructor).
     *
     * @param name a name of target <code>Queue</code>
     *
     * @return a <code>Queue</code> specified by given name obtained from initial context. If the object bound to given
     *         name is not a <code>Queue</code> then return <code>null</code>
     *
     * @throws NamingException if any naming exception (for example, NameNotFoundException) occurs
     * @throws IllegalArgumentException if <code>name</code> is <code>null</code>.
     */
    public static Queue getQueue(String name) throws NamingException {
        return getQueue(new InitialContext(), name);
    }

    /**
     * Dumps the bindings under initial <code>Context</code>. Traverses through initial Context and notifies
     * the given <code>ContextRenderer</code> on found contexts and bindings. The <code>ContextRenderer</code> is
     * responsible for further processing of the found Contexts and their bindings.
     *
     * @param renderer a <code>ContextRenderer</code> used to render the initial Context
     * @param recurse a boolean specifying whether the subcontexts of initial Context should be traversed too (if
     *        <code>true</code>) or not.
     *
     * @throws NamingException if any naming exception occurs
     * @throws IllegalArgumentException if <code>ContextRenderer</code> is <code>null</code>
     */
    public static void dump(ContextRenderer renderer, boolean recurse)
        throws NamingException {
        Helper.checkObject(renderer, "renderer");

        dump(new InitialContext(), renderer, "", recurse);
    }

    /**
     * Dumps the bindings under specified subcontext of initial <code>Context</code>. Traverses through
     * given subcontext and notifies the given <code>ContextRenderer</code> of found contexts and bindings. The
     * <code>ContextRenderer</code> is responsible for further processing of the found Contexts and their bindings.
     *
     * @param subCtx a name of subcontext within initial context that should be dumped
     * @param renderer a <code>ContextRenderer</code> used to render the target subcontext
     * @param recurse a boolean specifying whether the subcontexts of target subcontext should be traversed too (if
     *        <code>true</code>) or not.
     *
     * @throws NamingException if any naming exception occurs
     * @throws IllegalArgumentException if any of given <code>subCtx</code>, <code>ContextRenderer</code> is
     *         <code>null</code>
     */
    public static void dump(Name subCtx, ContextRenderer renderer, boolean recurse)
        throws NamingException {
        dump(new InitialContext(), subCtx, renderer, recurse);
    }

    /**
     * Dumps the bindings under specified subcontext of initial <code>Context</code>. Traverses through
     * given subcontext and notifies the given <code>ContextRenderer</code> of found contexts and bindings. The
     * <code>ContextRenderer</code> is responsible for further processing of the found Contexts and their bindings.
     *
     * @param subCtx a name of subcontext within initial context that should be dumped
     * @param renderer a <code>ContextRenderer</code> used to render the target subcontext
     * @param recurse a boolean specifying whether the subcontexts of initial subcontext should be traversed too (if
     *        <code>true</code>) or not.
     *
     * @throws NamingException if any naming exception occurs
     * @throws IllegalArgumentException if any of given <code>subCtx</code>, <code>ContextRenderer</code> is
     *         <code>null</code>
     */
    public static void dump(String subCtx, ContextRenderer renderer, boolean recurse)
        throws NamingException {
        dump(new InitialContext(), subCtx, renderer, recurse);
    }

    /**
     * Gets the SQL <code>Connection</code> from <code>DataSource</code> specified by given name from initial context
     * (created with <code>javax.naming.InitialContext()</code> constructor).
     *
     * @param name a name of target <code>DataSource</code> to get connection from
     *
     * @return a <code>Connection</code> obtained from <code>DataSource</code> specified by given name from initial
     *         context. If the object bound to given name is not a <code>DataSource</code> then return
     *         <code>null</code>
     *
     * @throws NamingException if any naming exception (for example, NameNotFoundException) occurs
     * @throws SQLException if any SQL error occurs
     * @throws IllegalArgumentException if <code>name</code> is <code>null</code>.
     */
    public static Connection getConnection(Name name) throws NamingException, SQLException {
        return getConnection(new InitialContext(), name);
    }

    /**
     * Gets the SQL <code>Connection</code> from <code>DataSource</code> specified by given name from initial context
     * (created with <code>javax.naming.InitialContext()</code> constructor).
     *
     * @param name a name of target <code>DataSource</code> to get connection from
     *
     * @return a <code>Connection</code> obtained from <code>DataSource</code> specified by given name from initial
     *         context. If the object bound to given name is not a <code>DataSource</code> then return
     *         <code>null</code>
     *
     * @throws NamingException if any naming exception (for example, NameNotFoundException) occurs
     * @throws SQLException if any SQL error occurs
     * @throws IllegalArgumentException if <code>name</code> is <code>null</code>.
     */
    public static Connection getConnection(String name)
        throws NamingException, SQLException {
        return getConnection(new InitialContext(), name);
    }

    /**
     * A private method that contains all functionality needed to traverse through given Context and dump its
     * content using given <code>ContextRenderer</code>. This method is invoked by public <code>dump</code> methods.
     *
     * @param ctx a target Context to dump
     * @param renderer a <code>ContextRenderer</code> to be notified on content of Context
     * @param relativeName a name of given Context relative to its owning Context. Public <code>dump</code> methods
     *        set this argument to empty String
     * @param mode a boolean specifying whether the subcontexts of target subcontext should be traversed too (if
     *        <code>true</code>) or not.
     *
     * @throws NamingException if any naming exception occurs
     */
    private static void dump(Context ctx, ContextRenderer renderer, String relativeName, boolean mode)
        throws NamingException {
        NamingEnumeration enumeration = ctx.list("");

        // notify renderer on start of context
        renderer.startContext(ctx.getNameInNamespace(), relativeName);

        while (enumeration.hasMore()) {
            NameClassPair pair = (NameClassPair) enumeration.next();

            if (mode) {
                try {
                    Context next = (Context) ctx.lookup(pair.getName());

                    // Recursive call to the renderer
                    dump(next, renderer, pair.getName(), mode);
                } catch (ClassCastException e) {
                    renderer.bindingFound(pair.getName(), pair.getClassName());
                }
            } else {
                // notify renderer on binding
                renderer.bindingFound(pair.getName(), pair.getClassName());
            }
        }

        // notify renderer on end of context
        renderer.endContext(ctx.getNameInNamespace(), relativeName);
    }

    /**
     * <p>A command-line interface providing access to JNDI Dump Functionality. Allows to dump the content of
     * specified Context or subcontext within given Context.</p>
     * <p>Usage:</p>
     * <pre>
     *     java JNDIUtils [-d] context [subcontext]
     * </pre>
     * where :
     * <p><code>-d</code> is an optional switch indicating that subcontexts nested within the <code>context</code>
     * should be traversed and dumped to. If this switch is not specified then the subcontexts are dumped as simple
     * bindings.</p>
     * <p><code>context</code> a target Context to dump</p>
     * <p><code>subcontext</code> is an optional parameter specifying the subcontext within the given context that
     * should be dumped</p>
     *
     * @param args an array of starting parameters
     */
    public static void main(String[] args) {
        if ((args.length == 0) || (args.length > MAX_ARGS_NUM)) {
            printUsage();

            return;
        }

        String contextString = null;
        String subContextString = null;
        boolean recurse = false;

        switch (args.length) {
        case 1:
            contextString = args[0];

            break;

        case 2:

            if (args[0].equals("-d")) {
                recurse = true;
                contextString = args[1];
            } else {
                contextString = args[0];
                subContextString = args[1];
            }

            break;

        default:

            // 3
            if (!args[0].equals("-d")) {
                printUsage();

                return;
            }

            recurse = true;
            contextString = args[1];
            subContextString = args[2];
        }

        Context context = null;
        Context subContext = null;

        try {
            context = getContext(contextString);

            if (subContextString != null) {
                subContext = (Context) getObject(context, subContextString, Context.class);
            } else {
                subContext = null;
            }
        } catch (NamingException e) {
            e.printStackTrace();

            return;
        } catch (ClassCastException e) {
            System.err.println(subContextString + " is not a context");

            return;
        } catch (ConfigManagerException e) {
            e.printStackTrace();

            return;
        }

        ContextConsoleRenderer consoleContextRenderer = null;

        try {
            consoleContextRenderer = new ContextConsoleRenderer();
        } catch (ContextRendererException e) {
            e.printStackTrace();

            return;
        }

        try {
            if (subContext != null) {
                dump(context, subContextString, consoleContextRenderer, recurse);
            } else {
                dump(context, consoleContextRenderer, recurse);
            }
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Print the usage of main method.
     */
    private static void printUsage() {
        System.out.println("Usage:\n" + "    java JNDIUtils [-d] context [subcontext]");
        System.out.println("where :\n" + "    [-d] is an optional switch indicating that subcontexts\n"
            + "    nested within the context should be traversed and dumped to.\n"
            + "    If this switch is not specified then" + "      the subcontexts are dumped as simplebindings.\n"
            + "\n" + "    context is a target Context to dump\n"
            + "    subcontext is an optional parameter specifying the "
            + "    subcontext within the given context that should be dumped\n");
    }

    /**
     * An accessor to CompoundName's protected constructor.
     */
    private static class ExtendedCompoundName extends CompoundName {
        /**
         * Constructor with enumeration of the components and properties that specify the syntax.
         *
         * @param e enumeration of the components passed to the constructor of superclass
         * @param s properties that specify the syntax passed to the constructor of superclass
         */
        ExtendedCompoundName(Enumeration e, Properties s) {
            super(e, s);
        }
    }
}
