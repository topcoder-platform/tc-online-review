/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 *
 * @(#)EmailEngineFailureTests.java
 */
package com.topcoder.message.email;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.mail.Address;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.util.config.ConfigManager;

public class EmailEngineFailureTests extends TestCase {

    private String from = null;

    private String to = null;

    private String PROPERTIES_NAMESPACE = "EmailEngineTest";

    private String PROPERTIES_LOCATION = "com/topcoder/message/email/EmailEngineTest.xml";

    private String PROPERTIES_FORMAT = ConfigManager.CONFIG_XML_FORMAT;

    public void setUp() {
        if (from == null || to == null) {
            ConfigManager cm = ConfigManager.getInstance();

            try {
                if (cm.existsNamespace(PROPERTIES_NAMESPACE)) {
                    cm.refresh(PROPERTIES_NAMESPACE);
                } else {
                    cm.add(PROPERTIES_NAMESPACE, PROPERTIES_LOCATION, PROPERTIES_FORMAT);
                }

                if (!cm.existsNamespace(PROPERTIES_NAMESPACE)) {
                    throw new Exception();
                }
            } catch (Exception e) {
            }

            try {
                from = cm.getString(PROPERTIES_NAMESPACE, "from");
            } catch (Exception e) {
                e.printStackTrace();
                from = null;
            }

            try {
                to = cm.getString(PROPERTIES_NAMESPACE, "to");
            } catch (Exception e) {
                e.printStackTrace();
                to = null;
            }
        }
    }

    public static Test suite() {
        TestSuite ts = new TestSuite("EmailEngineFailureTests");
        ts.addTestSuite(EmailEngineFailureTests.class);
        return (ts);
    }

    public EmailEngineFailureTests(String _name) {
        super(_name);
    }

    public void testGoodFromAddress() throws Exception {
        TCSEmailMessage tcsem = new TCSEmailMessage();
        tcsem.setFromAddress(from);
        assertEquals(from, tcsem.getFromAddress().toString());
    }

    public void testBadFromAddress() throws Exception {
        boolean its_bad = true;
        try {
            TCSEmailMessage tcsem = new TCSEmailMessage();
            tcsem.setFromAddress("the quick red fox jumped over the lazy brown dog");
            its_bad = false;
        } catch (Exception _e) {
            /* do nothing */
        }
        if (!its_bad) {
            throw (new Exception("TCSEmailMessage allowed bad from address"));
        }
    }

    public void testGoodToAddress() throws Exception {
        TCSEmailMessage tcsem = new TCSEmailMessage();
        tcsem.setToAddress(to, TCSEmailMessage.TO);
        Address[] addrs = tcsem.getToAddress(TCSEmailMessage.TO);
        assertTrue(addrs.length == 1);
        assertEquals(to, addrs[0].toString());
    }

    public void testBadToAddress() throws Exception {
        boolean is_bad = true;
        try {
            TCSEmailMessage tcsem = new TCSEmailMessage();
            tcsem.setToAddress("this is not an address", TCSEmailMessage.BCC);
            is_bad = false;
        } catch (Exception _e) {
            /* do nothing */
        }
        if (!is_bad) {
            throw (new Exception("TCSEmailMessage allowed bad to address"));
        }
    }

    public void testBadGetToAddressType() throws Exception {
        boolean is_bad = true;
        try {
            TCSEmailMessage tcsem = new TCSEmailMessage();
            Address[] addrs = tcsem.getToAddress(Integer.MAX_VALUE);
            is_bad = false;
        } catch (Exception _e) {
            /* do nothing */
        }
        if (!is_bad) {
            throw (new Exception("TCSEmailMessage allowed bad type in getToAddress"));
        }
    }

    public void testSubject() throws Exception {
        TCSEmailMessage tcsem = new TCSEmailMessage();
        tcsem.setSubject("TopCoder r00lez!");
        assertEquals("TopCoder r00lez!", tcsem.getSubject());
    }

    public void testBody() throws Exception {
        TCSEmailMessage tcsem = new TCSEmailMessage();
        tcsem.setBody("every body needs some body so move your body");
        assertEquals("every body needs some body so move your body", tcsem.getBody());
    }

    public void testAttachmentName() throws Exception {
        String name = "my picture.jpeg";
        InputStream is = new ByteArrayInputStream("not really a picture".getBytes());
        EmailEngine.AttachmentDataSource ads;
        ads = new EmailEngine.AttachmentDataSource(is, name);
        assertEquals(ads.getName(), name);
    }

    public void testAttachmentType() throws Exception {
        String name = "my picture.jpeg";
        InputStream is = new ByteArrayInputStream("not really a picture".getBytes());
        EmailEngine.AttachmentDataSource ads;
        ads = new EmailEngine.AttachmentDataSource(is, name);
        assertEquals(ads.getContentType(), "image/jpeg");
    }

};
