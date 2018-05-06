/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 * 
 */
package com.cronos.onlinereview.ajax.accuracytests;

import com.topcoder.management.resource.NotificationType;


/**
 * Mock class.
 * 
 * @author assistant
 * @version 1.0
 */
public class MockNotificationType extends NotificationType {
    public String getName() {
        return "Timeline Notification";
    }
    public long getId() {
        return 999990;
    }
}
