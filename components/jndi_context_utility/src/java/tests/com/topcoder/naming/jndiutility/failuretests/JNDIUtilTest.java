/**
 * Copyright © 2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.naming.jndiutility.failuretests;

import com.topcoder.naming.jndiutility.JNDIUtil;

import java.io.File;
import java.sql.SQLException;

import javax.naming.CompositeName;
import javax.naming.NamingException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * This class tests ContextXMLRenderer.
 *
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class JNDIUtilTest extends TestCase {

    final static String CONFIGURATION_FILE = ".\\conf\\com\\topcoder\\util\\config\\config.xml";

    private JNDIUtil jndiUtil = null;

    public JNDIUtilTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        File xmlConfigDataFile = new File(CONFIGURATION_FILE);
        jndiUtil = new JNDIUtil(xmlConfigDataFile);
    }

    public void testCreateName() {
        try {
            jndiUtil.createName((String) null);
            fail("Did not catch null name.");
        } catch (NamingException e) {
            fail("Threw an InvalidNameException rather than IllegalArgumentException per design.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    public void testCreateSubcontext() {
        try {
            jndiUtil.createSubcontext((String)null);
            fail("Exception should fire on null context.");
        } catch (NamingException e) {
            fail("Wrong fire on null context.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    public void testGetObject() {
        try {
            jndiUtil.getObject("asfdjakl;pdfbj203j092430ASFD!#@$)!)");
            fail("Should raise Exception");
        } catch (NamingException e) {
            // ok
        }

        try {
            jndiUtil.getObject((String) null);
            fail("Should raise Exception on null");
        } catch (NamingException e) {
            fail("Wrong Exception");
        } catch (IllegalArgumentException e) {
            // ok
        }

        try {
            jndiUtil.getObject((CompositeName) null);
            fail("Should raise Exception on null Name");
        } catch (NamingException e) {
            fail("Wrong Exception1");
        } catch (IllegalArgumentException e) {
            // ok
        }

        try {
            jndiUtil.getObject("Nonexistant object", JNDIUtil.class);
            fail("Object doesn't exist.");
        } catch (NamingException e) {
        }

        try {
            jndiUtil.getObject((String) null, JNDIUtil.class);
        } catch (NamingException e) {
            fail("Wrong exception2");
        } catch (IllegalArgumentException e) {
            // ok
        }

        try {
            jndiUtil.getObject("Nonexistant", null);
        } catch (NamingException e) {
            fail("Wrong exception3");
        } catch (IllegalArgumentException e) {
            // ok
        }

        try {
            jndiUtil.getObject((String) null, null);
        } catch (NamingException e) {
            fail("Wrong exception on both null (String, class)");
        } catch (IllegalArgumentException e) {
            // ok
        }

        try {
            jndiUtil.getObject((CompositeName) null, JNDIUtil.class);
        } catch (NamingException e) {
            fail("Wrong exception on first null (Name, Class)");
        } catch (IllegalArgumentException e) {
            // ok
        }

        try {
            jndiUtil.getObject(new CompositeName(), null);
        } catch (NamingException e) {
            fail("Wrong exception on second null (Name, Class)");
        } catch (IllegalArgumentException e) {
            // ok
        }

        try {
            jndiUtil.getObject((CompositeName) null, null);
        } catch (NamingException e) {
            fail("Wrong exception on both null (Name, Class)");
        } catch (IllegalArgumentException e) {
            // ok
        }

        try {
            jndiUtil.getObject((String) null, JNDIUtil.class);
        } catch (NamingException e) {
            fail("Wrong exception on second null (String, Class)");
        } catch (IllegalArgumentException e) {
            // ok
        }

        try {
            jndiUtil.getObject("", null);
        } catch (NamingException e) {
            fail("Wrong exception on third null (String, Class)");
        } catch (IllegalArgumentException e) {
            // ok
        }

        try {
            jndiUtil.getObject((String) null, null);
        } catch (NamingException e) {
            fail("Wrong exception on both null (String, Class)");
        } catch (IllegalArgumentException e) {
            // ok
        }

        try {
            jndiUtil.getObject((CompositeName) null, JNDIUtil.class);
        } catch (NamingException e) {
            fail("Wrong exception on second null (Name, Class)");
        } catch (IllegalArgumentException e) {
            // ok
        }

        try {
            jndiUtil.getObject(new CompositeName(), null);
        } catch (NamingException e) {
            fail("Wrong exception on third null (Name, Class)");
        } catch (IllegalArgumentException e) {
            // ok
        }

        try {
            jndiUtil.getObject((CompositeName) null, null);
        } catch (NamingException e) {
            fail("Wrong exception on both null ( String, Class)");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    public void testGetQueue() {
        try {
            jndiUtil.getQueue(new CompositeName());
            fail("Should have failed (Name).");
        } catch (NamingException e) {
            // ok
        }

        try {
            jndiUtil.getQueue((CompositeName) null);
            fail("Should have failed. n2(Name)");
        } catch (NamingException e) {
            fail("Wrong Exception Raised on null2 (Name)");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    public void testGetTopic() {
        try {
            jndiUtil.getTopic("funk");
            fail("Should have failed. (String)");
        } catch (NamingException e) {
            // ok
        }

        try {
            jndiUtil.getTopic((String) null);
            fail("Should have failed. n2(String)");
        } catch (NamingException e) {
            fail("Wrong Exception Raised on null2 (String)");
        } catch (IllegalArgumentException e) {
            // ok
        }

        try {
            jndiUtil.getTopic(new CompositeName());
            fail("Should have failed (Name).");
        } catch (NamingException e) {
            // ok
        }

        try {
            jndiUtil.getTopic((CompositeName) null);
            fail("Should have failed. n2(Name)");
        } catch (NamingException e) {
            fail("Wrong Exception Raised on null2 (Name)");
        } catch (IllegalArgumentException e) {
            // ok
        }

        try {
            jndiUtil.getTopic((CompositeName) null);
            fail("Should have failed. na(Name)");
        } catch (NamingException e) {
            fail("Wrong Exception Raised on all (Name)");
        } catch (IllegalArgumentException e) {
        }
    }

    public void testGetConnection() {
        try {
            jndiUtil.getConnection("funk");
            fail("Should have failed. (String)");
        } catch (NamingException e) {
            // ok
        }
        catch (SQLException e) {
            fail("Wrong exception (String)");
        }

        try {
            jndiUtil.getConnection((String) null);
            fail("Should have failed. n2(String)");
        } catch (NamingException e) {
            fail("Wrong Exception Raised on null2 (String)");
        } catch (IllegalArgumentException e) {
        }
        catch (SQLException e) {
            fail("Wrong exception on n2(String)");
        }

        try {
            jndiUtil.getConnection((String) null);
            fail("Should have failed. na(String)");
        } catch (NamingException e) {
            fail("Wrong Exception Raised on all (String)");
        } catch (IllegalArgumentException e) {
        }
        catch (SQLException e) {
            fail("Wrong exception na (String)");
        }

        try {
            jndiUtil.getConnection(new CompositeName());
            fail("Should have failed (Name).");
        } catch (NamingException e) {
        }
        catch (SQLException e) {
            fail("Wrong exception (Name)");
        }

        try {
            jndiUtil.getConnection((CompositeName) null);
            fail("Should have failed. n2(Name)");
        } catch (NamingException e) {
            fail("Wrong Exception Raised on null2 (Name)");
        } catch (IllegalArgumentException e) {
        }
        catch (SQLException e) {
            fail("Wrong exception n2(Name)");
        }

        try {
            jndiUtil.getConnection((CompositeName) null);
            fail("Should have failed. na(Name)");
        } catch (NamingException e) {
            fail("Wrong Exception Raised on all (Name)");
        } catch (IllegalArgumentException e) {
        }
        catch (SQLException e) {
            fail("Wrong exception na(Name)");
        }

        try {
            jndiUtil.getConnection((String) null);
            fail("Should have failed. na(String)");
        } catch (NamingException e) {
            fail("Wrong Exception Raised on all (String)");
        } catch (IllegalArgumentException e) {
            // ok
        }
        catch (SQLException e) {
            fail("Wrong exception na(String)");
        }
    }

    public static Test suite() {
        return new TestSuite(JNDIUtilTest.class);
    }

}
