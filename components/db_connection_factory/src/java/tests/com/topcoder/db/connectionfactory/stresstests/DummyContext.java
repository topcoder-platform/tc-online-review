/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.db.connectionfactory.stresstests;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;


/**
 * <p>
 * Dummy context used in this test. Only part of the methods are implemented. It uses a <code>Map</code> instance to
 * store the name and the object instance. If &quot;name.suffix&quot; property is set in the environment, the value
 * is appended to the end of the given name when looking up object.
 * </p>
 *
 * @author magicpig
 * @version 1.0
 */
public final class DummyContext implements Context {
    /**
     * <p>
     * Map the name to the object. All instance share the same map for the ease of test.
     * </p>
     */
    private static Map objectMap = new HashMap();

    /**
     * <p>
     * Environment of this instance.
     * </p>
     */
    private final Hashtable environment;

    /**
     * <p>
     * Create a new instance of <code>DummyContext</code>.
     * </p>
     *
     * @param environment environment used to create the instance
     */
    public DummyContext(Hashtable environment) {
        this.environment = environment;
    }

    /**
     * <p>
     * Convert a <code>Name</code> instance to string. The name prefixes are concatenated by &quot;.&quot;.
     * </p>
     *
     * @param name <code>Name</code> instance to convert
     *
     * @return string representing the <code>Name</code> instance
     */
    private String nameToString(Name name) {
        StringBuffer sb = new StringBuffer();

        if (!name.isEmpty()) {
            sb.append(name.getPrefix(0));
        }

        for (int i = 1; i < name.size(); ++i) {
            sb.append(".");
            sb.append(name.getPrefix(i));
        }

        return sb.toString();
    }

    /**
     * <p>
     * Dummy implementation of <code>Context</code>.
     * </p>
     */
    public void close() {
    }

    /**
     * <p>
     * Dummy implementation of <code>Context</code>.
     * </p>
     *
     * @return empty string
     */
    public String getNameInNamespace() {
        return "";
    }

    /**
     * <p>
     * Dummy implementation of <code>Context</code>.
     * </p>
     *
     * @param name dummy argument
     */
    public void destroySubcontext(String name) {
    }

    /**
     * <p>
     * Unbind an object associated with the given name.
     * </p>
     *
     * @param name name of the object to unbind
     */
    public void unbind(String name) {
        objectMap.remove(name);
    }

    /**
     * <p>
     * Get the environment of this context.
     * </p>
     *
     * @return environment of this context
     */
    public Hashtable getEnvironment() {
        return environment;
    }

    /**
     * <p>
     * Dummy implementation of <code>Context</code>.
     * </p>
     *
     * @param name dummy argument
     */
    public void destroySubcontext(Name name) {
    }

    /**
     * <p>
     * Unbind an object associated with the given name.
     * </p>
     *
     * @param name name of the object to unbind
     */
    public void unbind(Name name) {
        unbind(nameToString(name));
    }

    /**
     * <p>
     * Get the object associated with the given name. If &quot;name.suffix&quot; in environment exists, the value is
     * appended to the given name.
     * </p>
     *
     * @param name name of the object to retrieve
     *
     * @return object associated with the given name if available; <code>null</code> otherwise
     */
    public Object lookup(String name) {
        if (environment.containsKey("name.suffix")) {
            name += environment.get("name.suffix").toString();
        }

        return objectMap.get(name);
    }

    /**
     * <p>
     * Dummy implementation of <code>Context</code>.
     * </p>
     *
     * @param name dummy argument
     *
     * @return <code>null</code>
     */
    public Object lookupLink(String name) {
        return null;
    }

    /**
     * <p>
     * Remove a property from the environment.
     * </p>
     *
     * @param propName property name to remove
     *
     * @return the previous object associated with the property name
     */
    public Object removeFromEnvironment(String propName) {
        return environment.remove(propName);
    }

    /**
     * <p>
     * Bind an object to the given name.
     * </p>
     *
     * @param name name of the object to bind
     * @param obj object to bind
     */
    public void bind(String name, Object obj) {
        objectMap.put(name, obj);
    }

    /**
     * <p>
     * Rebind an object to the given name.
     * </p>
     *
     * @param name name of the object to rebind
     * @param obj object to rebind
     */
    public void rebind(String name, Object obj) {
        objectMap.put(name, obj);
    }

    /**
     * <p>
     * Get the object associated with the given name.
     * </p>
     *
     * @param name name of the object to retrieve
     *
     * @return object associated with the given name if available; <code>null</code> otherwise
     */
    public Object lookup(Name name) {
        return lookup(nameToString(name));
    }

    /**
     * <p>
     * Dummy implementation of <code>Context</code>.
     * </p>
     *
     * @param name dummy argument
     *
     * @return <code>null</code>
     */
    public Object lookupLink(Name name) {
        return null;
    }

    /**
     * <p>
     * Bind an object to the given name.
     * </p>
     *
     * @param name name of the object to bind
     * @param obj object to bind
     */
    public void bind(Name name, Object obj) {
        bind(nameToString(name), obj);
    }

    /**
     * <p>
     * Rebind an object to the given name.
     * </p>
     *
     * @param name name of the object to rebind
     * @param obj object to rebind
     */
    public void rebind(Name name, Object obj) {
        rebind(nameToString(name), obj);
    }

    /**
     * <p>
     * Dummy implementation of <code>Context</code>.
     * </p>
     *
     * @param oldName dummy argument
     * @param newName dummy argument
     */
    public void rename(String oldName, String newName) {
    }

    /**
     * <p>
     * Dummy implementation of <code>Context</code>.
     * </p>
     *
     * @param name dummy argument
     *
     * @return <code>null</code>
     */
    public Context createSubcontext(String name) {
        return null;
    }

    /**
     * <p>
     * Dummy implementation of <code>Context</code>.
     * </p>
     *
     * @param name dummy argument
     *
     * @return <code>null</code>
     */
    public Context createSubcontext(Name name) {
        return null;
    }

    /**
     * <p>
     * Dummy implementation of <code>Context</code>.
     * </p>
     *
     * @param oldName dummy argument
     * @param newName dummy argument
     */
    public void rename(Name oldName, Name newName) {
    }

    /**
     * <p>
     * Dummy implementation of <code>Context</code>.
     * </p>
     *
     * @param name dummy argument
     *
     * @return <code>null</code>
     */
    public NameParser getNameParser(String name) {
        return null;
    }

    /**
     * <p>
     * Dummy implementation of <code>Context</code>.
     * </p>
     *
     * @param name dummy argument
     *
     * @return <code>null</code>
     */
    public NameParser getNameParser(Name name) {
        return null;
    }

    /**
     * <p>
     * Dummy implementation of <code>Context</code>.
     * </p>
     *
     * @param name dummy argument
     *
     * @return <code>null</code>
     */
    public NamingEnumeration list(String name) {
        return null;
    }

    /**
     * <p>
     * Dummy implementation of <code>Context</code>.
     * </p>
     *
     * @param name dummy argument
     *
     * @return <code>null</code>
     */
    public NamingEnumeration listBindings(String name) {
        return null;
    }

    /**
     * <p>
     * Dummy implementation of <code>Context</code>.
     * </p>
     *
     * @param name dummy argument
     *
     * @return <code>null</code>
     */
    public NamingEnumeration list(Name name) {
        return null;
    }

    /**
     * <p>
     * Dummy implementation of <code>Context</code>.
     * </p>
     *
     * @param name dummy argument
     *
     * @return <code>null</code>
     */
    public NamingEnumeration listBindings(Name name) {
        return null;
    }

    /**
     * <p>
     * Add a property to the environment.
     * </p>
     *
     * @param propName property name
     * @param propVal property value
     *
     * @return the previous value associated with the property name
     */
    public Object addToEnvironment(String propName, Object propVal) {
        return environment.put(propName, propVal);
    }

    /**
     * <p>
     * Dummy implementation of <code>Context</code>.
     * </p>
     *
     * @param name dummy argument
     * @param prefix dummy argument
     *
     * @return <code>null</code>
     */
    public String composeName(String name, String prefix) {
        return null;
    }

    /**
     * <p>
     * Dummy implementation of <code>Context</code>.
     * </p>
     *
     * @param name dummy argument
     * @param prefix dummy argument
     *
     * @return <code>null</code>
     */
    public Name composeName(Name name, Name prefix) {
        return null;
    }
}






