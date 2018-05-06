/*
 * Copyright (C) 2003 TopCoder Inc., All Rights Reserved.
 *
 * @(#) ContextConsoleRenderer.java
 *
 * 1.0  05/14/2003
 */
package com.topcoder.naming.jndiutility;

import org.w3c.dom.Document;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


/**
 * <p>An implementation of <code>ContextRenderer</code> that is used to dump an XML representation of Context to
 * standard output.</p>
 *  <p><strong>Thread-Safety</strong></p>
 *  <p>Implementation is NOT thread-safe.</p>
 *
 * @author isv
 * @author preben
 * @version 1.0  08/09/2003
 */
public class ContextConsoleRenderer implements ContextRenderer {
    /** An XML renderer responsible for rendering. */
    private ContextXMLRenderer contextXmlRenderer;

    /** Flag is true if context is root. */
    private boolean isRoot;

    /** The fullName of root. */
    private String rootFullName;

    /** The relative name of root. */
    private String rootRelativeName;

    /**
     * Constructs a new <code>ContextXMLRenderer</code>.
     *
     * @throws ContextRendererException if there is a problem with the XML parser
     */
    public ContextConsoleRenderer() {
        contextXmlRenderer = new ContextXMLRenderer();
        isRoot = true;
    }

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
    public void startContext(String ctxFullName, String ctxRelativeName) {
        Helper.checkObject(ctxFullName, "ctxFullName");
        Helper.checkObject(ctxRelativeName, "ctxRelativeName");

        if (isRoot) {
            rootFullName = ctxFullName;
            rootRelativeName = ctxRelativeName;
            isRoot = false;
        }

        contextXmlRenderer.startContext(ctxFullName, ctxRelativeName);
    }

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
    public void endContext(String ctxFullName, String ctxRelativeName) {
        Helper.checkObject(ctxFullName, "ctxFullName");
        Helper.checkObject(ctxRelativeName, "ctxRelativeName");

        contextXmlRenderer.endContext(ctxFullName, ctxRelativeName);

        if (ctxFullName.equals(rootFullName) && ctxRelativeName.equals(rootRelativeName)) {
            try {
                writeXmlToStandardOut(contextXmlRenderer.getDocument());
            } catch (TransformerException e) {
                throw new ContextRendererException(e);
            }
        }
    }

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
    public void bindingFound(String name, String type) {
        Helper.checkObject(name, "name");
        Helper.checkObject(type, "type");

        contextXmlRenderer.bindingFound(name, type);
    }

    /**
     * Write an XML document to <code>Standard.out</code>.
     *
     * @param doc the XML document to write to to <code>Standard.out</code>
     *
     * @throws javax.xml.transform.TransformerConfigurationException if a serious configuration error occurs.
     * @throws TransformerException if an exceptional condition occurred during the transformation process.
     */
    private static void writeXmlToStandardOut(Document doc)
        throws TransformerException {
        Source source = new DOMSource(doc);
        Result result = new StreamResult(System.out);
        Transformer xformer = TransformerFactory.newInstance().newTransformer();
        xformer.transform(source, result);
    }
}
