/*
 * Copyright (C) 2003 TopCoder Inc., All Rights Reserved.
 *
 * @(#) DumpContextTestCase.java
 *
 * 1.0 05/14/2003
 */
package com.topcoder.naming.jndiutility.functionaltests;

import com.topcoder.naming.jndiutility.ContextXMLRenderer;
import com.topcoder.naming.jndiutility.JNDIUtils;

import javax.naming.Context;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * This test case tests the methods provided by JNDIUtils class allowing to dump the bindings in specified Context
 * either to standard output or to XML Document and therefore tests the behaviour of ContextXMLRenderer and
 * ContextConsoleRenderer classes. The rendering functionality  of ContextConsoleRendering is not tested.<p>In
 * order to run this test case a temporary directory should be created and</p>
 *  <p>This test case use a test context that is specified in
 * <code>com/topcoder/naming/jndiutility/JNDIUtils.properties</code> file. This test context is maintained by File
 * System JNDI Service from Sun.</p>
 *  <p>In order to run this test case a temporary directory should be created in the file system and
 * context.test.url property should be modified to point to this directory. The process should have a permission to
 * read from this directory.</p>
 *  <p>The following directory structure should be replicated under this created temporary directory(suppose that
 * temporary directory is named c:\tmp) :</p>
<pre>
 c:\tmp
      |
      +--dir
      |    +-- dirfile.txt
      +--file.txt
</pre>
 *
 * @author isv
 * @version 1.0 05/14/2003
 */
public class DumpContextTestCase extends TestCase {
    /**
     * Creates a new DumpContextTestCase object.
     *
     * @param name name of test case
     */
    public DumpContextTestCase(String name) {
        super(name);
    }

    /**
     * Return suite of this test case.
     *
     * @return suite of this test case.
     */
    public static Test suite() {
        return new TestSuite(DumpContextTestCase.class);
    }

    /**
     * Tests the behavior of ContextXMLRenderer class and indirectly tests the JNDIUtils.dump() methods.
     *
     * @throws Exception if error occurs
     */
    public void test_XML_renderer() throws Exception {
        Context ctx = null;
        ContextXMLRenderer renderer = null;
        Document doc = null;
        Element root = null;
        Element tmp = null;
        Node name = null;
        Node clazz = null;
        NodeList list = null;

        // test the dumping of context without traversing through subcontexts
        ctx = JNDIUtils.getContext("test");
        renderer = new ContextXMLRenderer();
        JNDIUtils.dump(ctx, renderer, false);
        doc = renderer.getDocument();

        root = doc.getDocumentElement();

        assertTrue("The root element should be named as 'Context'", root.getTagName().equals("Context"));

        list = root.getElementsByTagName("Context");

        assertTrue("No <Context> nodes should be created since subcontexts were" + " not traversed",
            list.getLength() == 0);

        list = root.getElementsByTagName("Binding");
        assertTrue("Two bindings should be created since subcontexts were" + " not traversed", list.getLength() == 2);

        tmp = (Element) list.item(0);

        name = tmp.getFirstChild();
        assertTrue("First node in Binding should be Name", name.getNodeName().equals("Name"));
        assertTrue("The value of Name should be a name of subdirectory",
            name.getFirstChild().getNodeValue().trim().equals("dir"));

        clazz = tmp.getLastChild();
        assertTrue("Last node in Binding should be Class", clazz.getNodeName().equals("Class"));
        assertTrue("The value of Class should be a javax.naming.Context",
            clazz.getFirstChild().getNodeValue().trim().equals("javax.naming.Context"));

        tmp = (Element) list.item(1);

        name = tmp.getFirstChild();
        assertTrue("First node in Binding should be Name", name.getNodeName().equals("Name"));
        assertTrue("The value of Name should be a name of file",
            name.getFirstChild().getNodeValue().trim().equals("file.txt"));

        clazz = tmp.getLastChild();
        assertTrue("Last node in Binding should be Class", clazz.getNodeName().equals("Class"));
        assertTrue("The value of Class should be a java.io.File",
            clazz.getFirstChild().getNodeValue().trim().equals("java.io.File"));

        // test the dumping of context with traversing through subcontexts
        ctx = JNDIUtils.getContext("test");
        renderer = new ContextXMLRenderer();
        JNDIUtils.dump(ctx, renderer, true);
        doc = renderer.getDocument();

        root = doc.getDocumentElement();

        assertTrue("The root element should be named as 'Context'", root.getTagName().equals("Context"));

        list = root.getElementsByTagName("Context");

        assertTrue("One nested directory corresponds to one nested Context tag", list.getLength() == 1);

        tmp = (Element) list.item(0);

        list = tmp.getElementsByTagName("Binding");
        assertTrue("Nested directory dir contains only one file that is " + " represented by Bindings now",
            list.getLength() == 1);

        tmp = (Element) list.item(0);

        name = tmp.getFirstChild();
        assertTrue("First node in Binding should be Name", name.getNodeName().equals("Name"));
        assertTrue("The value of Name should be a name of file",
            name.getFirstChild().getNodeValue().trim().equals("dirfile.txt"));

        clazz = tmp.getLastChild();
        assertTrue("Last node in Binding should be Class", clazz.getNodeName().equals("Class"));
        assertTrue("The value of Class should be a java.io.File",
            clazz.getFirstChild().getNodeValue().trim().equals("java.io.File"));

        list = root.getElementsByTagName("Binding");
        assertTrue("The files only are represented by Bindings now", list.getLength() == 2);

        tmp = (Element) list.item(1);

        name = tmp.getFirstChild();
        assertTrue("First node in Binding should be Name", name.getNodeName().equals("Name"));
        assertTrue("The value of Name should be a name of file",
            name.getFirstChild().getNodeValue().trim().equals("file.txt"));

        clazz = tmp.getLastChild();
        assertTrue("Last node in Binding should be Class", clazz.getNodeName().equals("Class"));
        assertTrue("The value of Class should be a java.io.File",
            clazz.getFirstChild().getNodeValue().trim().equals("java.io.File"));

        // test the dumping of subcontext of given Context
        ctx = JNDIUtils.getContext("test");
        renderer = new ContextXMLRenderer();
        JNDIUtils.dump(ctx, "dir", renderer, false);
        doc = renderer.getDocument();

        root = doc.getDocumentElement();

        assertTrue("The root element should be named as 'Context'", root.getTagName().equals("Context"));

        list = root.getElementsByTagName("Context");

        assertTrue("No <Context> nodes should be created since subcontexts were" + " not traversed",
            list.getLength() == 0);

        list = root.getElementsByTagName("Binding");
        assertTrue("One bindings should be created since subcontexts were" + " not traversed and only one file exists",
            list.getLength() == 1);

        tmp = (Element) list.item(0);

        name = tmp.getFirstChild();
        assertTrue("First node in Binding should be Name", name.getNodeName().equals("Name"));
        assertTrue("The value of Name should be a name of file",
            name.getFirstChild().getNodeValue().trim().equals("dirfile.txt"));

        clazz = tmp.getLastChild();
        assertTrue("Last node in Binding should be Class", clazz.getNodeName().equals("Class"));
        assertTrue("The value of Class should be a java.io.File",
            clazz.getFirstChild().getNodeValue().trim().equals("java.io.File"));
    }
}
