/*
 * Copyright (C) 2003 TopCoder Inc., All Rights Reserved.
 *
 * @(#) ContextRenderer.java
 *
 * 1.0  05/14/2003
 */
package com.topcoder.naming.jndiutility;

/**
 * A renderer of <code>Context</code> receiving notifications on the content of the <code>Context</code> from
 * <code>JNDIUtils.dump</code> methods.
 *  <p><strong>Thread-Safety:</strong></p>
 *  <p>Implementations as NOT expected to be thread safe.</p>
 *
 * @author isv
 * @version 1.0  08/09/2003
 */
public interface ContextRenderer {
    /**
     * Receives notification on the beginning of the <code>Context</code>. This method is invoked by
     * <code>JNDIUtils.dump()</code> methods to notify the renderer that a new Context specified with given full and
     * relative names is found.
     *
     * @param ctxFullName a name of started <code>Context</code> within its own namespaces
     * @param ctxRelativeName a name of started <code>Context</code> relative to its owning context
     *
     * @throws IllegalArgumentException if <code>ctxFullName</code> or <code>ctxRelativeName</code> is <code>null</code>
     * @throws ContextRendererException if any error or exception preventing the further context rendering occurs
     */
    public void startContext(String ctxFullName, String ctxRelativeName);

    /**
     * Receives notification on the end of the <code>Context</code>.  This method is invoked by
     * <code>JNDIUtils.dump()</code> methods to notify the renderer that no other bindings within Context specified
     * with given full and relative names were found (i.e. the end of Context is reached).
     *
     * @param ctxFullName a name of ended <code>Context</code> within its own namespaces
     * @param ctxRelativeName a name of ended <code>Context</code> relative to its owning context
     *
     * @throws IllegalArgumentException if <code>ctxFullName</code> or <code>ctxRelativeName</code> is <code>null</code>
     * @throws ContextRendererException if any error or exception preventing the further context rendering occurs
     */
    public void endContext(String ctxFullName, String ctxRelativeName);

    /**
     * Receives notification on the binding found within the <code>Context</code>. This method is invoked from
     * <code>JNDIUtils.dump()</code> methods to notify the renderer that new binding specified with given name and
     * type within current Context was found.
     *
     * @param name a name of the found binding
     * @param type a name of class of the object bound under given name
     *
     * @throws IllegalArgumentException if <code>name</code> or <code>type</code> is <code>null</code>
     * @throws ContextRendererException if any error or exception preventing the further context rendering occurs
     */
    public void bindingFound(String name, String type);
}
