/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 * 
 */
package com.cronos.onlinereview.ajax.accuracytests;

import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.deliverable.Upload;


/**
 * Mock class.
 * 
 * @author assistant
 * @version 1.0
 */
public class MockSubmission extends Submission {
    public Upload getUpload() {
        return new MockUpload();
    }
}
