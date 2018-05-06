/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 * 
 */
package com.cronos.onlinereview.ajax.accuracytests;

import com.topcoder.management.resource.Resource;
import com.topcoder.management.resource.ResourceRole;

/**
 * Mock class.
 * 
 * @author assistant
 * @version 1.0
 */
public class MockResource extends Resource {

    private long id;
    public MockResource(long id) {
        this.id = id;
    }
    public MockResource() {
    }
    
    public Long getProject() {
        return new Long(80);
    }
    
    public long getId() {
        return this.id;
    }
    
    public ResourceRole getResourceRole() {        
        return new MockResourceRole(id);
    }
    public Long getPhase() {
        return new Long(1);
    }
}
