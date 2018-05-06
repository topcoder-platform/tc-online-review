/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ajax.accuracytests;

import com.cronos.onlinereview.ajax.AjaxRequest;
import com.cronos.onlinereview.ajax.AjaxResponse;
import com.cronos.onlinereview.ajax.ConfigurationException;
import com.cronos.onlinereview.ajax.handlers.CommonHandler;
import com.cronos.onlinereview.ajax.handlers.ResourceException;

import com.topcoder.management.resource.ResourceManager;

import junit.framework.TestCase;


/**
 * Tests for CommonHandler class.
 *
 * @author assistant
 * @version 1.0
 */
public class TestCommonHandler extends TestCase {
    /** MockCommonHandlerImpl instance used for test. */
    private MockCommonHandlerImpl handler = null;

    /**
     * Setup the test environment.
     *
     * @throws Exception to JUnit.
     */
    protected void setUp() throws Exception {
        super.setUp();
        TestHelper.clearNamespaces();
        TestHelper.loadFile("test_files/accuracy/accuracy.xml");
        handler = new MockCommonHandlerImpl();
    }

    /**
     * Setup the test environment.
     *
     * @throws Exception to JUnit.
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        TestHelper.clearNamespaces();
        handler = null;
    }

    /**
     * Test CommonHandler method with accuracy case.
     */
    public void testCtorAccuracy() {
        assertNotNull("creating commonHandler fails.", handler);
        assertTrue("creating commonHandler fails.",
            handler.getResourceManager() instanceof MockResourceManager);
    }

//    /**
//     * Test checkUserHasRole method with accuracy state. The role does not match with the name, so
//     * true is returned.
//     *
//     * @throws ResourceException to JUnit.
//     */
//    public void testCheckUserHasRoleAccuracy3() throws ResourceException {
//        assertFalse("getUserRoleName fails", handler.checkUserHasRole(1, "name2"));
//    }

    /**
     * Test checkUserHasGlobalManagerRole method with accuracy state. The Resource is null, so
     * false is returned.
     *
     * @throws ResourceException to JUnit.
     */
    public void testCheckUserHasGlobalManagerRoleAccuracy()
        throws ResourceException {
        assertFalse("getUserRoleName fails", handler.checkUserHasGlobalManagerRole(-1));
    }

    /**
     * This class extends from CommonHandler used for test.
     *
     * @author assistant
     * @version 1.0
     */
    final class MockCommonHandlerImpl extends CommonHandler {
        /**
         * creating MockCommonHandlerImpl instance.
         *
         * @throws ConfigurationException to throw.
         */
        public MockCommonHandlerImpl() throws ConfigurationException {
            super();
        }

        /**
         * Dummy implementation.
         *
         * @param request DOCUMENT ME!
         * @param userId DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public AjaxResponse service(AjaxRequest request, Long userId) {
            return null;
        }

//        /**
//         * <p>
//         * Returns the role name for the user having userId as its ID.
//         * </p>
//         *
//         * @param userId the id of the user to get its role name
//         *
//         * @return the role name or null if it is not found
//         *
//         * @throws ResourceException if the resource manager has thrown an exception
//         */
//        public String getUserRoleName(long userId) throws ResourceException {
//            return super.getUserRoleName(userId);
//        }
//
//        /**
//         * <p>
//         * Check if a user has the specified role or not.
//         * </p>
//         *
//         * @param userId the id of the user to check its role
//         * @param role the role to check for
//         *
//         * @return true if the user has the role
//         *
//         * @throws ResourceException if the resource manager has thrown an exception
//         * @throws IllegalArgumentException if role parameter is null or empty String
//         */
//        public boolean checkUserHasRole(long userId, String role)
//            throws ResourceException {
//            return super.checkResourceHasRole(userId, role);
//        }

        /**
         * <p>
         * Check if the user has the global manager role or not.
         * </p>
         *
         * @param userId the id of the user to check its role
         *
         * @return true if the user has the global manager role
         *
         * @throws ResourceException if the resource manager has thrown an exception
         */
        public boolean checkUserHasGlobalManagerRole(long userId)
            throws ResourceException {
            return super.checkUserHasGlobalManagerRole(userId);
        }

        /**
         * <p>
         * Returns the resource manager used to get resource data.
         * </p>
         *
         * @return the resource manager used to manage resources
         */
        public ResourceManager getResourceManager() {
            return super.getResourceManager();
        }
    }
}
