/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.login.stresstests;

import java.util.Iterator;

import com.topcoder.util.config.ConfigManager;

import servletunit.struts.MockStrutsTestCase;

/**
 * Test for LoginActions#logout.
 *
 * @author Chenhong
 * @version 1.0
 */
public class LogoutTest extends MockStrutsTestCase {

    /**
     * Create Logout test instance.
     *
     * @throws Exception
     *             to junit.
     */
    public LogoutTest() throws Exception {
        super.setUp();
        ConfigManager cm = ConfigManager.getInstance();
        for (Iterator iter = cm.getAllNamespaces(); iter.hasNext();) {
            cm.removeNamespace((String) iter.next());
        }

        cm.add("stresstests/OnlineReviewLogin.xml");
    }

    /**
     * Test logout method, no exception should be thrown during logout. And the page will be forward to logout.
     *
     * @throws Exception
     *             to junit.
     */
    public void testLogout() throws Exception {
        addRequestParameter("method", "logout");
        setRequestPathInfo("/logout");
        actionPerform();

        verifyForward("logout");
        verifyNoActionMessages();
    }
}
