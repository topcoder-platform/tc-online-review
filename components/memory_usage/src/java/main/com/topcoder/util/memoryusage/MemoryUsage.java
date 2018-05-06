/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.util.memoryusage;

import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.config.UnknownNamespaceException;
import com.topcoder.util.memoryusage.analyzers.Sun14Analyzer;
import com.topcoder.util.objectfactory.InvalidClassSpecificationException;
import com.topcoder.util.objectfactory.ObjectFactory;
import com.topcoder.util.objectfactory.SpecificationFactoryException;
import com.topcoder.util.objectfactory.impl.ConfigManagerSpecificationFactory;

/**
 * <p>The MemoryUsage class is the main access point to the package facilities.
 * This class provides methods to obtain the memory usage for an object
 * (including or not including the embedded objects recursively). Since different
 * JVM implementations will allocate objects differently, this class will take
 * care of determining the right analyzer implementation for the running JVM.
 * The analyzer will be called to perform the actual memory usage analysis.</p>
 * <p> The class can be initialized either through API, or through configuration
 * files. Both ways, it is necessary to define a set of analyzers for various
 * virtual machines. It is also possible to set a default analyzer, to fall back
 * upon in case no specific analyzer is found for the current JVM.
 *
 * <p><b>Thread safety:</b> This class is thread safe, since its internal state
 * is not mutable (once inizialization occurred).</p>
 *
 * <p><b>Configuration requirements:</b> Two out of three constructors of
 * this component require a properly configured namespace.
 * The namespace (either the one provided in construction, or the default one
 * which is <b><code>com.topcoder.util.memoryusage.MemoryUsage</code></b>)
 * must contain the following properties.
 * <table><tr>
 * <th>Property name<th>Required/Optionl<th>Description
 * <tr>
 * <td><b>analyzers_namespace</b><td>Optional
 * <td> This is the namespace which contains the configuration for ObjectFactory
 * to instantiate the MemoryUsageAnalyzer implementation. If not present, it default
 * to the namespace specified in construction (or the default one) with
 * <code>".objectFactory"</code> appended.
 * </tr>
 * <tr><td><b>analyzers</b><td>Required
 * <td>This multi-valued property specifies all the keys to be used with
 * ObjectFactory to instantiate the MemoryUsageAnalyzer's. This property must
 * be present, and non-empty.
 * </tr>
 * <tr><td><b>fallback_analyzer</b><td>Optional
 * <td>The key specifying through ObjectFactory which MemoryUsageAnalyzer to use
 * as a default, when no one of the others is appropriate for the current JVM.
 * </tr>
 * <tr><td><b>default_fallback_analyzer_flag</b><td>Optional
 * <td>This property, if present, should take
 * If this property is present and has the <b><code>"true"</code></b> value, and
 * the <code>fallback_analyzer</code> property is not present, the component
 * will use as default MemoryUsageAnalyzer the {@link Sun14Analyzer} class. In all
 * other cases, this flag has no effect.
 * </tr>
 * </table>
 * </p>
 *
 * @author BryanChen
 * @author AleaActaEst, TexWiller
 * @version 2.0
 */
public class MemoryUsage {

    /**
     * The name of the property storing the list of analyzers to be used.
     */
    private static final String PROP_ANALYZERS = "analyzers";

    /**
     * The name of the property storing the namespace of the configuration
     * for ObjectFactory to create analyzers.
     */
    private static final String PROP_ANALYZERS_NAMESPACE = "analyzers_namespace";

    /**
     * The name of the property storing the name of the fallback analyzer.
     */
    private static final String PROP_FALLBACK_ANALYZER = "fallback_analyzer";

    /**
     * The name of the property storing the flag indicating whether the default
     * fallback analyzer should be used.
     */
    private static final String PROP_FALLBACK_FLAG = "default_fallback_analyzer_flag";

    /**
     * The string to be appended to this object namespace if the analyzers_namespace
     * property is not specified, in order to obtain the default analyzers_namespace
     * property value.
     */
    private static final String OBJECT_FACTORY_NS = ".objectFactory";

    /**
     * An instance of the DefaultListener, used when the user calls the
     * <code>getDeepMemoryUsage(Object obj)</code> method.
     */
    private static final MemoryUsageListener DEFAULT_LISTENER = new DefaultListener();

    /**
     * This variable holds the appropriate MemoryUsageAnalyzer for the
     * current JVM. It is initialized during construction, and can be <code>null</code>
     * in case the analyzers list does not contain an appropriate analyzer for
     * the current JVM.
     */
    private final MemoryUsageAnalyzer currentAnalyzer;

    /**
     * Represents the fallback analyzer to be used if no specific analyzer
     * from the list analyzers has matched the current jvm. By default this is
     * going to be set to new Sun14Analyzer(). The default will only be set if
     * the user specifically states that they would like to use a default fallback.
     * This can be done either through configuration or through 3rd constructor.
     * It is immutable (but can be obtained through a getter).
     */
    private final MemoryUsageAnalyzer fallbackAnalyzer;

    /**
     * Creates a new MemoryUsage instance. The configuration parameters are taken
     * from the default namespace, <code>com.topcoder.util.memoryusage.MemoryUsage</code>.
     * See the class description for further details on the configuration file structure.
     *
     * @throws ConfigurationException If an error occurred reading the configuration files
     */
    public MemoryUsage() throws ConfigurationException {
        this(MemoryUsage.class.getName());
    }

    /**
     * Creates a new MemoryUsage instance. The configuration parameters are taken
     * from the specified namespace.
     * See the class description for further details on the configuration file structure.
     *
     * @param namespace The namespace to look configuration parameters in. Cannot be
     * <code>null</code> or empty.
     * @throws IllegalArgumentException If <i>namespace</i> is <code>null</code>, or an empty string
     * @throws ConfigurationException If errors occurred reading the configuration,
     * or creating the MemoryUsageAnalyzer implementations
     */
    public MemoryUsage(String namespace) throws ConfigurationException {
        MemoryUsageHelper.checkStringNotNullOrEmpty(namespace, "namespace");
        ConfigManager cManager = ConfigManager.getInstance();
        try {
            // Read the analyzers_namespace and analyzers properties
            // and perform sanity checks
            String analyzersNamespace = cManager.getString(namespace, PROP_ANALYZERS_NAMESPACE);
            if ((analyzersNamespace == null) || (analyzersNamespace.trim().length() == 0)) {
                analyzersNamespace = namespace + OBJECT_FACTORY_NS;
            }
            String[] analyzers = cManager.getStringArray(namespace, PROP_ANALYZERS);
            if ((analyzers == null) || (analyzers.length == 0)) {
                throw new ConfigurationException("The analyzers property must be present, "
                        + "and contain at least one value");
            }

            // Create the ObjectFactory
            ConfigManagerSpecificationFactory specFactory =
                    new ConfigManagerSpecificationFactory(analyzersNamespace);
            ObjectFactory analyzersFactory = new ObjectFactory(specFactory);

            // Instantiate MemoryUsageAnalyzer implementations
            // and look for the correct one for current JVM
            MemoryUsageAnalyzer foundAnalyzer = null;
            for (int i = 0; i < analyzers.length; i++) {
                try {
                    Object analyzer = analyzersFactory.createObject(analyzers[i]);
                    if (!(analyzer instanceof MemoryUsageAnalyzer)) {
                        throw new ConfigurationException("The objects specified with "
                                + "the analyzers property must implement the MemoryUsageAnalyzer interface");
                    }
                    if (((MemoryUsageAnalyzer) analyzer).matchesJVM()) {
                        foundAnalyzer = (MemoryUsageAnalyzer) analyzer;
                    }
                } catch (InvalidClassSpecificationException ex) {
                    throw new ConfigurationException("Error instantiating object " + analyzers[i]);
                }
            }
            currentAnalyzer = foundAnalyzer;

            // Set up the fallback analyzer
            String fallbackAnalyzerKey = cManager.getString(namespace, PROP_FALLBACK_ANALYZER);
            if (fallbackAnalyzerKey != null) {
                try {
                    this.fallbackAnalyzer = (MemoryUsageAnalyzer) analyzersFactory.createObject(fallbackAnalyzerKey);
                } catch (InvalidClassSpecificationException ex) {
                    throw new ConfigurationException("Error instantiating object " + fallbackAnalyzerKey);
                } catch (ClassCastException ex) {
                    throw new ConfigurationException("The object specified with the fallback_analyzere "
                                + "property must implement the MemoryUsageAnalyzer interface");
                }
            } else {
                String defaultFallbackFlag = cManager.getString(namespace, PROP_FALLBACK_FLAG);
                if ("true".equalsIgnoreCase(defaultFallbackFlag)) {
                    this.fallbackAnalyzer = new Sun14Analyzer();
                } else {
                    this.fallbackAnalyzer = null;
                }
            }
        } catch (UnknownNamespaceException ex) {
            throw new ConfigurationException("Error loading configuration files", ex);
        } catch (SpecificationFactoryException ex) {
            throw new ConfigurationException("Error instantiating ObjectFactory items", ex);
        }
    }

    /**
     * Creates a new MemoryUsage instance. All the parameters are directly specified
     * to the constructor.
     *
     * @param analyzers An array (non-<code>null</code> and non-empty) of MemoryUsageAnalyzers implementations
     * @param fallbackAnalyzer An optional (can be <code>null</code>) fallback analyzer
     * @param useDefaultFallback A flag which specifies whether to use a default fallback analyzer,
     * in case one was not provided through <i>fallbackAnalyzer</i>. The default analyzer
     * is {@link Sun14Analyzer}.
     * @throws IllegalArgumentException If the input array is <code>null</code> or empty, or
     * contains <code>null</code> elements
     */
    public MemoryUsage(MemoryUsageAnalyzer[] analyzers, MemoryUsageAnalyzer fallbackAnalyzer,
            boolean useDefaultFallback) {
        MemoryUsageHelper.checkArrayContentNotNull(analyzers, "analyzers");

        // Look for correct analyzer
        MemoryUsageAnalyzer foundAnalyzer = null;
        for (int i = 0; i < analyzers.length; i++) {
            if (analyzers[i].matchesJVM()) {
                foundAnalyzer = analyzers[i];
                break;
            }
        }
        this.currentAnalyzer = foundAnalyzer;

        // Setup fallback analyzer
        if (fallbackAnalyzer != null) {
            this.fallbackAnalyzer = fallbackAnalyzer;
        } else {
            this.fallbackAnalyzer = (useDefaultFallback ? new Sun14Analyzer() : null);
        }
    }

    /**
     * Retrieve the shallow memory usage for an object (not including embedded
     * objects memory usage, but counting the memory consumed by the object to keep
     * references to the embedded objects).
     *
     * @param obj The object to get the memory usage for; cannot be <code>null</code>
     * @return The shallow memory usage result of the specified object
     * @throws IllegalArgumentException If <i>obj</i> is <code>null</code>
     * @throws MemoryUsageException If exceptions occurred while traversing the object; typically,
     * security-related exceptions can be raised
     * @throws JVMNotSupportedException If no matching MemoryUsageAnalyzer could
     * be found for the current JVM, and no default MemoryUsageAnalyzer was defined
     */
    public MemoryUsageResult getShallowMemoryUsage(Object obj) throws MemoryUsageException, JVMNotSupportedException {
        MemoryUsageHelper.checkObjectNotNull(obj, "obj");

        MemoryUsageAnalyzer analyzer = findMatchingAnalyzer();
        return analyzer.getShallowMemoryUsage(obj);
    }

    /**
     * Get the deep memory usage for an object (recursively including embedded
     * objects memory usage).
     *
     * @param obj The object to get the memory usage for; cannot be <code>null</code>
     * @return The deep memory usage result of the specified object
     * @throws IllegalArgumentException If <i>obj</i> is <code>null</code>
     * @throws MemoryUsageException If exceptions occurred while traversing the object; typically,
     * security-related exceptions can be raised
     * @throws JVMNotSupportedException If no matching MemoryUsageAnalyzer could
     * be found for the current JVM, and no default MemoryUsageAnalyzer was defined
     */
    public MemoryUsageResult getDeepMemoryUsage(Object obj) throws MemoryUsageException, JVMNotSupportedException {
        return getDeepMemoryUsage(obj, DEFAULT_LISTENER);
    }

    /**
     * Get the deep memory usage for an object (including embedded objects memory
     * usage recursively). The user can specify a MemoryUsageListener, in order to
     * prevent some of the embedded objects to be analyzed. Before proceeding to analyze
     * each object, the listener will be called and will be able to decide whether
     * the object should be traversed or not.
     *
     * @param obj The object to get the memory usage for; cannot be <code>null</code>
     * @param listener The MemoryUsageListener which will receive the
     * object notifications during traversal; can be <code>null</code>
     * @return The deep memory usage result of the specified object
     * @throws IllegalArgumentException If <i>obj</i> is <code>null</code>
     * @throws MemoryUsageException If exceptions occurred while traversing the object; typically,
     * security-related exceptions can be raised
     * @throws JVMNotSupportedException If no matching MemoryUsageAnalyzer could
     * be found for the current JVM, and no default MemoryUsageAnalyzer was defined
     */
    public MemoryUsageResult getDeepMemoryUsage(Object obj, MemoryUsageListener listener)
        throws MemoryUsageException, JVMNotSupportedException {
        MemoryUsageHelper.checkObjectNotNull(obj, "obj");
        MemoryUsageHelper.checkObjectNotNull(listener, "listener");

        MemoryUsageAnalyzer analyzer = findMatchingAnalyzer();
        return analyzer.getDeepMemoryUsage(obj, listener);
    }

    /**
     * Get the shallow embedded object list for an object. If <i>obj</i> is a
     * simple object, this is the list of objects referred by its fields.
     * If <i>obj</i> is an array object, this is the list of the elements
     * of the array. This array will not contain duplicate objects.
     *
     * @param obj The object to be scanned for embedded objects
     * @return The array of embedded object; will be non-<code>null</code>
     * @throws IllegalArgumentException If the <i>obj</i> parameter is <code>null</code>
     * @throws MemoryUsageException If exceptions occurred while traversing the object; typically,
     * security-related exceptions can be raised
     */
    public static Object[] getEmbeddedObjects(Object obj) throws MemoryUsageException {
        MemoryUsageHelper.checkObjectNotNull(obj, "obj");
        return MemoryUsageHelper.getEmbeddedObjects(obj, null);
    }

    /**
     * Retrieves the fallback MemoryUsageAnalyzer for this object.
     * It can be <code>null</code>, meaning that no fallback analyzer has
     * been specified.
     *
     * @return The current fallback MemoryUsageAnalyzer
     */
    public MemoryUsageAnalyzer getFallBackAnalyzer() {
        return fallbackAnalyzer;
    }

    /**
     * This is a helper method which return the correct analyzer for the current JVM.
     * This is the <i>currentAnalyzer</i> if has been set, or the <i>fallbackAnalyzer</i>
     * otherwise. If none has been set, throws a JVMNotSupportedException.
     *
     * @return A non-<code>null</code> analyzer
     * @throws JVMNotSupportedException If no appropriate analyzer was found
     */
    private MemoryUsageAnalyzer findMatchingAnalyzer() throws JVMNotSupportedException {
        if (currentAnalyzer != null) {
            return currentAnalyzer;
        }
        if (fallbackAnalyzer != null) {
            return fallbackAnalyzer;
        }
        throw new JVMNotSupportedException("Could not find any analyzer for the current JVM");
    }


    /**
     * This class is a simple implementation of the MemoryUsageListener interface,
     * which always returns <code>true</code> for every object. It is used as the default
     * listener when the user calls the <code>getDeepMemoryUsage(Object)</code> method.
     */
    private static class DefaultListener implements MemoryUsageListener {

        /**
         * Default implementation of the interface method.
         * @param obj The Object being visited
         * @return Always <code>true</code>
         */
        public boolean objectReached(Object obj) {
            return true;
        }
    }
}
