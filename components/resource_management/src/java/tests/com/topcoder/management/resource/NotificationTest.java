/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.resource;

import junit.framework.TestCase;

/**
 * Unit tests for the class: Notification.
 *
 * @author kinfkong
 * @version 1.0
 */
public class NotificationTest extends TestCase {

    /**
     * Tests constructor: Notification(long, NotificationType, long).
     *
     * With null notificationType, IllegalArgumentException should be thrown.
     */
    public void testNotification_NullNotificationType() {
        try {
            new Notification(1, null, 1);
            fail("IllegalArgument should be thrown.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Tests constructor: Notification(long, NotificationType, long).
     *
     * With non-positive project id, IllegalArgumentException should be thrown.
     */
    public void testNotification_NonPostiveProjectId() {
        try {
            new Notification(0, new NotificationType(), 1);
            fail("IllegalArgument should be thrown.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }


    /**
     * Tests constructor: Notification(long, NotificationType, long).
     *
     * With non-positive external id, IllegalArgumentException should be thrown.
     */
    public void testNotification_NonPostiveExternalId() {
        try {
            new Notification(1, new NotificationType(), -1);
            fail("IllegalArgument should be thrown.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Tests constructor: Notification(long, NotificationType, long).
     *
     * Accuracy tests to check if the fields are set properly.
     */
    public void testNotification_Accuracy() {
        NotificationType type = new NotificationType();
        Notification notification = new Notification(100, type, 200);
        // assert not null
        assertNotNull("The instance cannot be created.", notification);
        assertEquals("The project id does not set properly.", 100, notification.getProject());
        assertEquals("The notificationType does not set properly.", type, notification.getNotificationType());
        assertEquals("The external id does not set properly.", 200, notification.getExternalId());
    }


    /**
     * Tests method: getProject().
     *
     * Checks if this method works properly.
     */
    public void testGetProject() {
        NotificationType type = new NotificationType();
        Notification notification = new Notification(100, type, 200);
        assertEquals("The project id does not set properly.", 100, notification.getProject());
    }

    /**
     * Tests method: getNotificationType().
     *
     * Checks if this method works properly.
     */
    public void testGetNotificationType() {
        NotificationType type = new NotificationType();
        Notification notification = new Notification(100, type, 200);
        assertEquals("The notificationType does not set properly.", type, notification.getNotificationType());
    }

    /**
     * Tests method: getExternalId().
     *
     * Checks if this method works properly.
     */
    public void testGetExternalId() {
        NotificationType type = new NotificationType();
        Notification notification = new Notification(100, type, 200);
        assertEquals("The external id does not set properly.", 200, notification.getExternalId());
    }

}
