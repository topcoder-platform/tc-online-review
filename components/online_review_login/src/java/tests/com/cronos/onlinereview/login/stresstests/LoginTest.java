/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.login.stresstests;

import java.util.Iterator;
import java.util.Random;

import com.topcoder.util.config.ConfigManager;
import servletunit.struts.MockStrutsTestCase;

/**
 * Test for LoginActions#login.
 *
 * @author Chenhong
 * @version 1.0
 */
public class LoginTest extends MockStrutsTestCase {

    /**
     * Create LoginTest instance for test.
     *
     * @throws Exception
     *             to junit.
     */
    public LoginTest() throws Exception {
        super.setUp();

        ConfigManager cm = ConfigManager.getInstance();
        for (Iterator iter = cm.getAllNamespaces(); iter.hasNext();) {
            cm.removeNamespace((String) iter.next());
        }
        cm.add("stresstests/OnlineReviewLogin.xml");

    }

    /**
     * Test method Login. If the username and password matches, login succeeded, otherwise failure.
     *
     * @throws Exception
     *             to junit.
     */
    public void testLogin() throws Exception {
        Random rand = new Random();
        int id = rand.nextInt(1000);

        String userName = "userName" + id;
        String password = "password" + id;

        // add userName and password to RequestParameter.
        addRequestParameter("userName", userName);
        addRequestParameter("password", password);

        addRequestParameter("method", "login");

        setRequestPathInfo("/login");
        actionPerform();
        if (id > 0 && id < 1000) {
            verifyForward("success");
        } else {
            verifyForward("failure");
        }
    }
}
