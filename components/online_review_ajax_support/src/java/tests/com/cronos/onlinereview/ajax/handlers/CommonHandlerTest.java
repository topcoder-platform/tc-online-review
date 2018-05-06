/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ajax.handlers;

import java.util.Iterator;

import junit.framework.TestCase;

import com.cronos.onlinereview.ajax.TestHelper;
import com.topcoder.management.resource.MockResource;
import com.topcoder.util.config.ConfigManager;

/**
 * Test the class <code>CommonHandler</code>.
 * In this case, a mock implementation will be used to test.
 *
 * @author assistant
 * @version 1.0
 */
public class CommonHandlerTest extends TestCase {

    /**
     * Represents the handler to test.
     */
    private CommonHandler handler;

    /**
     * Set up the environment.
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        super.setUp();
        // load the configurations
        ConfigManager cm = ConfigManager.getInstance();
        if (!cm.existsNamespace("com.cronos.onlinereview.ajax")) {
            cm.add("default.xml");
            cm.add("objectfactory.xml");
            cm.add("scorecalculator.xml");
        }


        handler = new MockCommonHandler();
    }

    /**
     * Clean up the environment.
     *
     * @throws Exception to JUnit
     */
    protected void tearDown() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();
        Iterator it = cm.getAllNamespaces();
        while (it.hasNext()) {
            cm.removeNamespace(it.next().toString());
        }
    }

    /**
     * Test method for CommonHandler().
     * @throws Exception to JUnit
     */
    public void testCommonHandler() throws Exception {
        assertNotNull("The constructor doesn't work.", handler);

        // verify the manager role id
        // please check MockResourceManager for detail
        // the id should be 1
        Long id = (Long) TestHelper.getPrivateFieldValue(CommonHandler.class, "managerRoleId", handler);
        assertEquals("The id should be 1.", 1, id.longValue());

        assertNotNull("The resource manager should not be null.", handler.getResourceManager());
    }

    /**
     * Test method for checkUserHasRole(long, java.lang.String).
     * In this case, the name is null.
     * Expected exception : {@link IllegalArgumentException}.
     * @throws Exception to JUnit
     */
    public void testCheckUserHasRoleNullName() throws Exception {
        try {
            handler.checkResourceHasRole(new MockResource(), null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for checkUserHasRole(long, java.lang.String).
     * In this case, the name is empty.
     * Expected exception : {@link IllegalArgumentException}.
     * @throws Exception to JUnit
     */
    public void testCheckUserHasRoleEmptyName() throws Exception {
        try {
            handler.checkResourceHasRole(new MockResource(), " ");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for checkUserHasGlobalManagerRole(long).
     * @throws Exception to JUnit
     */
    public void testCheckUserHasGlobalManagerRoleAccuracy1() throws Exception {
        assertTrue("The user 1 should have the role Manager.", handler.checkUserHasGlobalManagerRole(1));
    }

    /**
     * Test method for {@link com.cronos.onlinereview.ajax.handlers.CommonHandler#getResourceManager()}.
     */
    public void testGetResourceManager() {
        assertNotNull("The resource manager is not right.", handler.getResourceManager());
    }

}
