/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 *
 * TCS Project JNDI Context Utility 2.0
 *
 * @ JNDIUtil.java
 */
package com.topcoder.naming.jndiutility;

import com.topcoder.naming.jndiutility.configstrategy.ConfigurationManagerConfigurationStrategy;
import com.topcoder.naming.jndiutility.configstrategy.XmlFileConfigurationStrategy;

import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;

import javax.jms.Queue;
import javax.jms.Topic;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.Name;
import javax.naming.NamingException;


/**
 * <p>This is a version of the utility <code>JNDIUtils</code> functionality but which is instance based. This
 * means that each instance can have its specific Context used (based on either a setter or through configuration).</p>
 *  <p>Other than that (i.e. instance of Context) the functionality is exactly the same as in the
 * <code>JNDIUtils</code> class with the other main difference being that in addition to the
 * <code>ConfigManager</code> (supported by <code>JNDIUtils</code>), this class also supports alternative XML
 * configuration (which mimics the <code>ConfigManager</code> in its scope).</p>
 *  <p><strong>Thread-Safety</strong></p>
 *  <p>This class is not strictly thread-safe since it can be used to modify configuration file data.</p>
 *  <p><strong>Basic Usage Demo</strong></p>
 * <pre>
 * // Using the instance class is very much the same as the static utility with the main difference being
 * // that we can deal with specific namespaces or with specific XML file locations as well as being able
 * // to set a Context directly:
 * // Create an instance of the JNDIUtil instance with different configurations
 * // default Config namespace
 * JNDIUtil jndiUtil = new JNDIUtil();
 * // specific Config namespace
 * jndiUtil = new JNDIUtil("com.topcoder.naming.jndiutility.Demo");
 * // specific InputStream
 * jndiUtil = new JNDIUtil(new FileInputStream("test_files/Demo.xml"));
 * // specific file (for XML)
 * jndiUtil = new JNDIUtil(new File("test_files/Demo.xml"));
 *
 * // Once we have an instance of the jndi util we can call instance methods (which are an exact subset
 * // of the JNDIUtils class)
 *
 * // Create a name with a string that is valid within associated context
 * Name name = jndiUtil.createName("directory/file");
 *
 * // Get an object in the associated context
 * Object object = jndiUtil.getObject("dir");
 *
 * // Get an object with specified type in the associated context
 * object = jndiUtil.getObject(jndiUtil.createName("file.txt"), File.class);
 *
 * // Get a resources in the associated context
 * Topic topic = jndiUtil.getTopic("MyTopic");
 *
 * Queue queue = jndiUtil.getQueue(new CompositeName("MyQueue"));
 *
 * Connection con = jndiUtil.getConnection("MyDataSource");
 *
 * // Save configurations in the xml file which construct this instance (or the namespace of ConfigManager)
 * Properties props = new Properties();
 * props.put("factory", "com.sun.jndi.fscontext.RefFSContextFactory");
 * props.put("url", "file:test_files");
 * jndiUtil.saveContextConfig("test", props);
 *
 * // Get a named context in the configuration
 * Context ctx = jndiUtil.getContext("test");
 *
 * // Create a sub context in the associated context
 * jndiUtil.createSubcontext("dir");
 *
 * // Dump the content with given subcontext name in the associated context
 * ContextRenderer renderer = new ContextConsoleRenderer();
 * jndiUtil.dump("dir", renderer, true);
 * </pre>
 * <p><strong>Sample Configuration File</strong></p>
 * <p>For <code>ConfigurationManagerConfigurationStrategy</code></p>
 * <pre>
 * &lt;Config name="com.topcoder.naming.jndiutility.Demo"&gt;
 *     &lt;Property name="context.default.factory"&gt;
 *         &lt;Value&gt;com.sun.jndi.fscontext.RefFSContextFactory&lt;/Value&gt;
 *     &lt;/Property&gt;
 *     &lt;Property name="context.default.url"&gt;
 *         &lt;Value&gt;file:test_files/test_root&lt;/Value&gt;
 *     &lt;/Property&gt;
 * &lt;/Config&gt;
 * </pre>
 * <p>For <code>XmlFileConfigurationStrategy</code></p>
 * <pre>
 * &lt;Properties&gt;
 *     &lt;Property name="context.default.factory"&gt;com.sun.jndi.fscontext.RefFSContextFactory&lt;/Property&gt;
 *     &lt;Property name="context.default.url"&gt;file:test_files/test_root&lt;/Property&gt;
 * &lt;/Properties&gt;
 * </pre>
 *
 * @author AleaActaEst, Charizard
 * @version 2.0
 */
public class JNDIUtil {
    /** Separator of property name. */
    private static final String SEPARATOR = ".";

    /** Prefix of properties. */
    private static final String PROPERTY_PREFIX = "context" + SEPARATOR;

    /** Suffix of factory property. */
    private static final String FACTORY_SUFFIX = SEPARATOR + "factory";

    /** Suffix of URL property. */
    private static final String URL_SUFFIX = SEPARATOR + "url";

    /** Name used to get default context. */
    private static final String DEFAULT_NAME = "default";

    /**
     * <p>This is a specific configuration strategy being used to load/store configuration values for an
     * InitialContext. This is initialized in any one of the constructors. Once initialized it will not change.</p>
     *  <p>Currently there are only two provided strategies:</p>
     *  <ul>
     *      <li>Configuration Manager based strategy</li>
     *      <li>XMl File based strategy</li>
     *  </ul>
     *  <p>This cannot be null and must be initialized with a valid instance.</p>
     */
    private final ConfigurationStrategy configProvider;

    /**
     * <p>This is a specific Context being used by this instance. This is initialized in any of the
     * constructors and is used in most methods. Once initialized it will not change.</p>
     *  <p>To initialize the context we will be looking for a specific "default" property: We will be looking
     * for "context.default.factory" and "context.default.url" in the configuration.</p>
     *  <p>It cannot be null. It is not accessible from outside.</p>
     */
    private final Context context;

    /**
     * <p>This is a default constructor which initializes the config provider to
     * <code>ConfigurationManagerConfigurationStrategy</code> with a "namespace" value in the strategy defaulting to
     * <code>JNDIUtils.NAMESPACE</code>. This way the instance will use the default namespace in all its actions.</p>
     *
     * @throws ConfigurationException if there are issues with configuration
     */
    public JNDIUtil() throws ConfigurationException {
        this(JNDIUtils.NAMESPACE);
    }

    /**
     * <p>This is a constructor which initializes the config provider to
     * <code>ConfigurationManagerConfigurationStrategy</code> with a "namespace" value in the strategy being set to
     * the input namespace.</p>
     *
     * @param namespace configuration namespace for <code>ConfigManager</code> based configuration
     *
     * @throws IllegalArgumentException if <code>namespace</code> is <code>null</code> or empty
     * @throws ConfigurationException if there are issues with configuration
     */
    public JNDIUtil(String namespace) throws ConfigurationException {
        configProvider = new ConfigurationManagerConfigurationStrategy(namespace);

        try {
            context = getContext(DEFAULT_NAME);
        } catch (NamingException e) {
            throw new ConfigurationException("error occurred during creating default context", e);
        }
    }

    /**
     * <p>This is a specific constructor which initializes the config provider to
     * <code>XmlFileConfigurationStrategy</code> and read the xml data based on the input stream. Note the instances
     * created by this constructor cannot execute <code>saveContextConfig(String name, Properties props)</code> since
     * there's no destination file available.</p>
     *
     * @param inputSource input stream from which to read the xml configuration data
     *
     * @throws IllegalArgumentException if <code>inputSource</code> is <code>null</code>
     * @throws ConfigurationException if there are issues with configuration
     */
    public JNDIUtil(InputStream inputSource)
        throws ConfigurationException {
        configProvider = new XmlFileConfigurationStrategy(inputSource);

        try {
            context = getContext(DEFAULT_NAME);
        } catch (NamingException e) {
            throw new ConfigurationException("error occurred during creating default context", e);
        }
    }

    /**
     * <p>This is a specific constructor which initializes the config provider to
     * <code>XmlFileConfigurationStrategy</code> and read the xml data based on the input <code>File</code>.</p>
     *
     * @param xmlConfigDataFile configuration file with xml structure
     *
     * @throws IllegalArgumentException if <code>xmlConfigDataFile</code> is <code>null</code>
     * @throws ConfigurationException if there are issues with configuration
     */
    public JNDIUtil(File xmlConfigDataFile)
        throws ConfigurationException {
        configProvider = new XmlFileConfigurationStrategy(xmlConfigDataFile);

        try {
            context = getContext(DEFAULT_NAME);
        } catch (NamingException e) {
            throw new ConfigurationException("error occurred during creating default context", e);
        }
    }

    /**
     * <p>Gets the object specified by given name from initial context associated with this instance. The
     * object is returned as it is.</p>
     *
     * @param name the name of the object to look up in initial context
     *
     * @return an object bound under given name within initial context
     *
     * @throws IllegalArgumentException if <code>name</code> is <code>null</code>.
     * @throws NamingException if a naming exception is encountered
     */
    public Object getObject(String name) throws NamingException {
        return JNDIUtils.getObject(context, name);
    }

    /**
     * <p>Gets the object specified by given name from initial context associated with this instance. The
     * object is returned as it is.</p>
     *
     * @param name the name of the object to look up in initial context
     *
     * @return an object bound under given name within initial context
     *
     * @throws IllegalArgumentException if <code>name</code> is <code>null</code>.
     * @throws NamingException if a naming exception is encountered
     */
    public Object getObject(Name name) throws NamingException {
        return JNDIUtils.getObject(context, name);
    }

    /**
     * <p>Gets the object specified by given name from initial context associated with this instance. It is
     * guaranteed that the returned object can be cast to type specified by given <code>Class</code>.</p>
     *
     * @param name the name of the object to look up in initial context
     * @param clazz a <code>Class</code> to check the cast to
     *
     * @return an object bound under given name within initial context that can be cast to specified type
     *
     * @throws IllegalArgumentException if <code>name</code> or <code>clazz</code> is <code>null</code>.
     * @throws NamingException if a naming exception is encountered
     * @throws ClassCastException if object bound under given name cannot be cast to given class
     */
    public Object getObject(String name, Class clazz) throws NamingException {
        return JNDIUtils.getObject(context, name, clazz);
    }

    /**
     * <p>Gets the object specified by given name from initial context associated with this instance. It is
     * guaranteed that the returned object can be cast to type specified by given <code>Class</code>.</p>
     *
     * @param name the name of the object to look up in initial context
     * @param clazz a <code>Class</code> to check the cast to
     *
     * @return an object bound under given name within initial context that can be cast to specified type
     *
     * @throws IllegalArgumentException if <code>name</code> or <code>clazz</code> is <code>null</code>
     * @throws NamingException if a naming exception is encountered
     * @throws ClassCastException if object bound under given name cannot be cast to given class
     */
    public Object getObject(Name name, Class clazz) throws NamingException {
        return JNDIUtils.getObject(context, name, clazz);
    }

    /**
     * <p>Creates the context using the properties specified in configuration for context specified by given
     * name. The actual configuration being used is abstracted in the form of <code>ConfigurationStrategy</code>.</p>
     *  <p>The names of properties of context in configuration file are started with "context.name." Two
     * properties names "context.name.factory" and "context.name.url" are supported. The required property for each
     * context is a "factory" property. The "url" property is optional. The "context.name.factory" property
     * specifies the factory class used to create the Contexts. The "context.name.url" property contains the URL
     * connection string used to initialize Context and should contain all needed info to initialize the Context.</p>
     *  <p>For example:</p>
     *  <ul>
     *      <li>context.default.factory=com.sun.jndi.ldap.LdapCtxFactory</li>
     *      <li>context.default.url=ldap://ldap.OpenLDAP.org:389/cn=Peoples</li>
     *  </ul>
     *
     * @param name a name of requested Context within configuration file
     *
     * @return a <code>Context</code> created using the properties from configuration file
     *
     * @throws IllegalArgumentException if given <code>name</code> is <code>null</code> or empty
     * @throws NamingException if any naming exception occurs or there is no factory is specified for given context
     * @throws ConfigurationException if any exception related to configuration occurs
     */
    public Context getContext(String name) throws NamingException, ConfigurationException {
        Helper.checkString(name, "name");

        Properties props = new Properties();
        String factory = configProvider.getProperty(PROPERTY_PREFIX + name + FACTORY_SUFFIX);

        if (factory == null) {
            throw new NamingException("Factory not specified in configuration");
        }

        props.put(Context.INITIAL_CONTEXT_FACTORY, factory);

        // URL is optional
        String url = configProvider.getProperty(PROPERTY_PREFIX + name + URL_SUFFIX);

        if (url != null) {
            props.put(Context.PROVIDER_URL, url);
        }

        return new InitialContext(props);
    }

    /**
     * <p>Stores the properties specifying the info needed to create and initialize the Context into
     * configuration file. All properties are stored with their names prefixed with "context.<code>name</code>."</p>
     *
     * @param name a name of the <code>Context</code> within configuration file
     * @param props a Properties to store in configuration file
     *
     * @throws IllegalStateException if this instance is constructed with input stream thus no destination file is
     *         available
     * @throws IllegalArgumentException if any of given <code>name</code> is <code>null</code> or empty, or
     *         <code>props</code> is <code>null</code> or contains empty value
     * @throws ConfigurationException if any exception related to configuration occurs
     */
    public void saveContextConfig(String name, Properties props)
        throws ConfigurationException {
        Helper.checkString(name, "name");
        Helper.checkObject(props, "props");

        Enumeration enumeration = props.propertyNames();

        while (enumeration.hasMoreElements()) {
            String propertyName = (String) enumeration.nextElement();
            configProvider.setProperty(PROPERTY_PREFIX + name + SEPARATOR + propertyName,
                props.getProperty(propertyName));
        }

        configProvider.commitChanges();
    }

    /**
     * <p>Creates a subcontext specified with given name within the context which is associated with the
     * current instance. Recursively creates all intermediate contexts if they do not exist. If specified subcontext
     * already exists within initial context then simply returns this subcontext.</p>
     *
     * @param name a <code>String</code> representing the target subcontext
     *
     * @return a <code>Context</code> represented by given name and nested within initial context
     *
     * @throws IllegalArgumentException if <code>name</code> is <code>null</code> or empty after parsing
     * @throws javax.naming.NameAlreadyBoundException if the given name is already bound with a non-context object in
     *         the associated context
     * @throws NamingException if any other naming exception occurs
     */
    public Context createSubcontext(String name) throws NamingException {
        return JNDIUtils.createSubcontext(context, name);
    }

    /**
     * <p>Creates a subcontext specified with given name within the context which is associated with the
     * current instance, only if it does not exist, otherwise just returns a specified Context. Recursively creates
     * all intermediate contexts if they do not exist. If specified subcontext already exists within initial context
     * then simply returns this subcontext.</p>
     *
     * @param name a <code>Name</code> representing the target context
     *
     * @return a <code>Context</code> represented by given name and nested within default initial context
     *
     * @throws IllegalArgumentException if <code>name</code> is <code>null</code> or empty.
     * @throws javax.naming.NameAlreadyBoundException if the given name is already bound with a non-context object in
     *         the associated context
     * @throws NamingException if any other naming exception occurs
     */
    public Context createSubcontext(Name name) throws NamingException {
        return JNDIUtils.createSubcontext(context, name);
    }

    /**
     * <p>Gets the <code>Name</code> representing the name specified by given <code>String</code> that is
     * valid within the context which is associated with the current instance.</p>
     *
     * @param name a source name to get Name representation of
     *
     * @return a <code>Name</code> constructed from given String that conforms to naming syntax of initial context
     *
     * @throws IllegalArgumentException if <code>name</code> is <code>null</code>.
     * @throws NamingException if any naming exception (for example, InvalidNameException) occurs
     */
    public Name createName(String name) throws NamingException {
        return JNDIUtils.createName(context, name);
    }

    /**
     * <p>Gets the JMS <code>Topic</code> from context that is associated with this instance.</p>
     *
     * @param name a name of target <code>Topic</code>
     *
     * @return a <code>Topic</code> specified by given name obtained from initial context. If the object bound to
     *         given name is not a <code>Topic</code> then return <code>null</code>
     *
     * @throws IllegalArgumentException if <code>name</code> is <code>null</code>.
     * @throws NamingException if any naming exception (for example, NameNotFoundException) occurs
     */
    public Topic getTopic(String name) throws NamingException {
        return JNDIUtils.getTopic(context, name);
    }

    /**
     * <p>Gets the JMS <code>Topic</code> from context that is associated with this instance.</p>
     *
     * @param name a name of target <code>Topic</code>
     *
     * @return a <code>Topic</code> specified by given name obtained from initial context. If the object bound to
     *         given name is not a <code>Topic</code> then return <code>null</code>
     *
     * @throws IllegalArgumentException if <code>name</code> is <code>null</code>.
     * @throws NamingException if any naming exception (for example, NameNotFoundException) occurs
     */
    public Topic getTopic(Name name) throws NamingException {
        return JNDIUtils.getTopic(context, name);
    }

    /**
     * <p>Gets the JMS <code>Queue</code> from context that is associated with this instance.</p>
     *
     * @param name a name of target <code>Queue</code>
     *
     * @return a <code>Queue</code> specified by given name obtained from initial context. If the object bound to
     *         given name is not a <code>Queue</code> then return <code>null</code>
     *
     * @throws IllegalArgumentException if <code>name</code> is <code>null</code>.
     * @throws NamingException if any naming exception (for example, NameNotFoundException) occurs
     */
    public Queue getQueue(String name) throws NamingException {
        return JNDIUtils.getQueue(context, name);
    }

    /**
     * <p>Gets the JMS <code>Queue</code> from context that is associated with this instance.</p>
     *
     * @param name a name of target <code>Queue</code>
     *
     * @return a <code>Queue</code> specified by given name obtained from initial context. If the object bound to
     *         given name is not a <code>Queue</code> then return <code>null</code>
     *
     * @throws IllegalArgumentException if <code>name</code> is <code>null</code>.
     * @throws NamingException if any naming exception (for example, NameNotFoundException) occurs
     */
    public Queue getQueue(Name name) throws NamingException {
        return JNDIUtils.getQueue(context, name);
    }

    /**
     * <p>Dumps the bindings under the Context for this instance.</p>
     *  <p>Traverses through associated Context and notifies the given <code>ContextRenderer</code> on found
     * contexts and bindings. The <code>ContextRenderer</code> is responsible for further processing of the found
     * Contexts and their bindings.</p>
     *
     * @param renderer a <code>ContextRenderer</code> used to render the initial Context
     * @param recurse a boolean specifying whether the subcontexts of initial Context should be traversed too (if
     *        <code>true</code>) or not.
     *
     * @throws IllegalArgumentException if <code>ContextRenderer</code> is <code>null</code>
     * @throws NamingException if any naming exception occurs
     */
    public void dump(ContextRenderer renderer, boolean recurse)
        throws NamingException {
        JNDIUtils.dump(context, renderer, recurse);
    }

    /**
     * <p>Dumps the bindings under specified subcontext of the Context associated with this instance.</p>
     *  <p>Traverses through given subcontext of the associated Context and notifies the given
     * <code>ContextRenderer</code> of found contexts and bindings. The <code>ContextRenderer</code> is responsible
     * for further processing of the found Contexts and their bindings.</p>
     *
     * @param subCtx a name of subcontext within initial context that should be dumped
     * @param renderer a <code>ContextRenderer</code> used to render the target subcontext
     * @param recurse a boolean specifying whether the subcontexts of target subcontext should be traversed too (if
     *        <code>true</code>) or not.
     *
     * @throws IllegalArgumentException if any of given <code>subCtx</code>, <code>ContextRenderer</code> is
     *         <code>null</code>
     * @throws NamingException if any naming exception occurs
     */
    public void dump(Name subCtx, ContextRenderer renderer, boolean recurse)
        throws NamingException {
        JNDIUtils.dump(context, subCtx, renderer, recurse);
    }

    /**
     * <p>Dumps the bindings under specified subcontext of the Context associated with this instance.</p>
     *  <p>Traverses through given subcontext of the associated Context and notifies the given
     * <code>ContextRenderer</code> of found contexts and bindings. The <code>ContextRenderer</code> is responsible
     * for further processing of the found Contexts and their bindings.</p>
     *
     * @param subCtx a name of subcontext within initial context that should be dumped.
     * @param renderer a <code>ContextRenderer</code> used to render the target subcontext
     * @param recurse a boolean specifying whether the subcontexts of initial subcontext should be traversed too (if
     *        <code>true</code>) or not.
     *
     * @throws IllegalArgumentException if any of given <code>subCtx</code>, <code>ContextRenderer</code> is
     *         <code>null</code>
     * @throws NamingException if any naming exception occurs
     */
    public void dump(String subCtx, ContextRenderer renderer, boolean recurse)
        throws NamingException {
        JNDIUtils.dump(context, subCtx, renderer, recurse);
    }

    /**
     * <p>Gets the SQL <code>Connection</code> from <code>DataSource</code> specified by given name from
     * context that is associated with this instance.</p>
     *
     * @param name a name of target <code>DataSource</code> to get connection from
     *
     * @return a <code>Connection</code> obtained from <code>DataSource</code> specified by given name from initial
     *         context. If the object bound to given name is not a <code>DataSource</code> then return
     *         <code>null</code>
     *
     * @throws IllegalArgumentException if <code>name</code> is <code>null</code>.
     * @throws NamingException if any naming exception (for example, NameNotFoundException) occurs
     * @throws SQLException if any SQL error occurs
     */
    public Connection getConnection(Name name) throws NamingException, SQLException {
        return JNDIUtils.getConnection(context, name);
    }

    /**
     * <p>Gets the SQL <code>Connection</code> from <code>DataSource</code> specified by given name from
     * context that is associated with this instance.</p>
     *
     * @param name a name of target <code>DataSource</code> to get connection from
     *
     * @return a <code>Connection</code> obtained from <code>DataSource</code> specified by given name from the
     *         instance initial context. If the object bound to given name is not a <code>DataSource</code> then
     *         returns <code>null</code>
     *
     * @throws IllegalArgumentException if <code>name</code> is <code>null</code>.
     * @throws NamingException if any naming exception (for example, NameNotFoundException) occurs
     * @throws SQLException if any SQL error occurs
     */
    public Connection getConnection(String name) throws NamingException, SQLException {
        return JNDIUtils.getConnection(context, name);
    }
}
