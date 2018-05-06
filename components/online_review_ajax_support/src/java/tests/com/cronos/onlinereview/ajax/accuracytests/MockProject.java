/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 * 
 */
package com.cronos.onlinereview.ajax.accuracytests;

import com.topcoder.management.project.Project;
import com.topcoder.management.project.ProjectCategory;
import com.topcoder.management.project.ProjectStatus;


/**
 * Mock class.
 * 
 * @author assistant
 * @version 1.0
 */
public class MockProject extends Project {

    public MockProject(ProjectCategory arg0, ProjectStatus arg1) {
        super(arg0, arg1);
    }

    public Object getProperty(String name) {
        if (name.equals("Public")) {
            return "Yes";
        }
        return null;
    }
}
