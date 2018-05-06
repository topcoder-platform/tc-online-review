/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.latetracker.utility;

import java.security.Permission;

/**
 * <p>
 * Custom security manager that prevents java exit when calling System.exit. It will
 * throw <code>SecurityException</code> instead exiting JVM.
 * </p>
 *
 * @author myxgyy
 * @version 1.0
 */
public class NotExitSecurityManager extends SecurityManager {
    /**
     * Override empty method.
     *
     * @param perm
     *            the permission
     */
    @Override
    public void checkPermission(Permission perm) {
    }

    /**
     * Override empty method.
     *
     * @param perm
     *            the permission
     * @param context
     *            the context
     */
    @Override
    public void checkPermission(Permission perm, Object context) {
    }

    /**
     * Sets the exit code and throw exception instead of exiting java.
     *
     * @param status the exit code to set.
     * @throws SecurityException always thrown to instead of exiting JVM.
     */
    @Override
    public void checkExit(int status) {
        super.checkExit(status);
        throw new SecurityException(String.valueOf(status));
    }
}