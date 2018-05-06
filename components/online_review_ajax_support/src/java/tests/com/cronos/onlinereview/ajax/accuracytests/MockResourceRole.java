/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 * 
 */
package com.cronos.onlinereview.ajax.accuracytests;

import com.topcoder.management.resource.ResourceRole;


/**
 * Mock class.
 * 
 * @author assistant
 * @version 1.0
 */
public class MockResourceRole extends ResourceRole {
    private long id = -1;

    public MockResourceRole() {        
    }
    public MockResourceRole(long id) {
        this.id = id;
    }
    
    public long getId() {
        return 999990;
    }
    public String getName() {
        if (id == 777770) {
            return "Submitter";
        }
        if (id == 777771) {
            return "Reviewer";
        }
        return "Manager";
    }
}
