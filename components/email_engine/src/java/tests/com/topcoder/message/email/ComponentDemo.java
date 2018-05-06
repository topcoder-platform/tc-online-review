/*
 * Copyright (C) 2005-2008 TopCoder Inc., All Rights Reserved.
 *
 * @(#)ComponentDemo.java
 */
package com.topcoder.message.email;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>This is the Demo of the Email Engine version 3.0.</p>
 *
 * @author  smell, TCSDEVELOPER
 * @version 3.1
 * 
 */
public class ComponentDemo extends TestCase {

    /**
     * This method contains the demo to send a highest priority email.
     *
     * @throws Exception to JUnit
     */
    public void testSending() throws Exception {
        TCSEmailMessage message = new TCSEmailMessage();
        message.setSubject("High Priority");
        message.setBody("Pretty Important\n-Writer\n");
        message.setFromAddress("from@topcoder.com", "My Name");
        message.setToAddress("to@topcoder.com", "Your Name", TCSEmailMessage.TO);
        // For an HTML message rather than a plain text message,
        // the content type should be changed.
        message.setContentType("text/html");


        // This is the new method call.
        message.setPriority(PriorityLevel.HIGHEST);
        EmailEngine.send(message);
    }

    /**
     * Returns the suite containing the Demo.
     *
     * @return suite to run the test
     */
    public static Test suite() {
        return new TestSuite(ComponentDemo.class);
    }

}
