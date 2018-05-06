/*
 * Copyright (C) 2003 TopCoder Inc., All Rights Reserved.
 *
 * @(#) ContextXMLRenderer.java
 *
 * 1.0  05/14/2003
 */
package com.topcoder.naming.jndiutility;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import java.util.Stack;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


/**
 * <p>An implementation of <code>ContextRenderer</code> that is used to get an XML Document object representing the
 * dumped Context.</p>
 *  <p><strong>Thread-Safety</strong></p>
 *  <p>Implementation is NOT thread-safe.</p>
 *
 * @author isv
 * @author preben
 * @version 1.0  08/09/2003
 */
public class ContextXMLRenderer implements ContextRenderer {
    /** An XML document. */
    private Document document;

    /** A stack to manage the rendering. */
    private Stack stack;

    /** A variable indicating if a Node is root. */
    private boolean isRoot;

    /**
     * Constructs a new <code>ContextXMLRenderer</code>.
     *
     * @throws ContextRendererException if there is an error with the XML parser.
     */
    public ContextXMLRenderer() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.newDocument();
        } catch (ParserConfigurationException e) {
            throw new ContextRendererException(e);
        }

        this.stack = new Stack();
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
     * @throws IllegalArgumentException if <code>ctxFullName</code> or <code>ctxRelativeName</code> is
     *         <code>null</code>
     * @throws ContextRendererException if any error or exception preventing the further context rendering occurs
     */
    public void startContext(String ctxFullName, String ctxRelativeName) {
        Helper.checkObject(ctxFullName, "ctxFullName");
        Helper.checkObject(ctxRelativeName, "ctxRelativeName");

        try {
            Element element = document.createElement("Context");
            element.setAttribute("name", ctxRelativeName);
            element.setAttribute("fullname", ctxFullName);

            //If it is root just append it to the document
            if (isRoot) {
                document.appendChild(element);
                isRoot = false;
            } else {
                Node topOfStack = (Node) stack.peek();
                topOfStack.appendChild(element);
            }

            //Push the newly created Context node onto the stack
            stack.push(element);
        } catch (DOMException e) {
            throw new ContextRendererException(e);
        }
    }

    /**
     * Receives notification on the end of the <code>Context</code>.  This method is invoked by
     * <code>JNDIUtils.dump()</code> methods to notify the renderer that no other bindings within Context specified
     * with given full and relative names were found (i.e. the end of Context is reached).
     *
     * @param ctxFullName a name of ended <code>Context</code> within its own namespaces
     * @param ctxRelativeName a name of ended <code>Context</code> relative to its owning context
     *
     * @throws IllegalArgumentException if <code>ctxFullName</code> or <code>ctxRelativeName</code> is
     *         <code>null</code>
     * @throws ContextRendererException if any error or exception preventing the further context rendering occurs
     */
    public void endContext(String ctxFullName, String ctxRelativeName) {
        Helper.checkObject(ctxFullName, "ctxFullName");
        Helper.checkObject(ctxRelativeName, "ctxRelativeName");

        if (stack.isEmpty()) {
            throw new ContextRendererException("illegal call to endContex()");
        }

        //Pop the last context since we don't need it anymore
        Element element = (Element) stack.pop();
        String actualCtxFullName = element.getAttribute("fullname");
        String actualCtxRelativeName = element.getAttribute("name");

        if (!(actualCtxFullName.equals(ctxFullName) && actualCtxRelativeName.equals(ctxRelativeName))) {
            throw new ContextRendererException("Incorrect use of endContext() method");
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

        if (stack.isEmpty()) {
            throw new ContextRendererException("Error rendering document");
        }

        try {
            Element binding = document.createElement("Binding");
            Element nameElement = document.createElement("Name");
            Text nameTextNode = document.createTextNode(name);
            nameElement.appendChild(nameTextNode);

            Element typeElement = document.createElement("Class");
            Text typeTextNode = document.createTextNode(type);
            typeElement.appendChild(typeTextNode);

            binding.appendChild(nameElement);
            binding.appendChild(typeElement);

            Node topOfStack = (Node) stack.peek();
            topOfStack.appendChild(binding);
        } catch (DOMException e) {
            throw new ContextRendererException(e);
        }
    }

    /**
     * Gets the XML Document representing the dumped context. This method should be invoked after
     * <code>JNDIUtils.dump()</code> method has been finished.
     *
     * @return an XML Document representing the dumped context.
     */
    public Document getDocument() {
        return document;
    }
}
