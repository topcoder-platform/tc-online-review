/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 * 
 */
package com.cronos.onlinereview.ajax.accuracytests;

import com.topcoder.management.review.data.Comment;
import com.topcoder.management.review.data.Item;


/**
 * Mock class.
 * 
 * @author assistant
 * @version 1.0
 */
public class MockItem extends Item {

    public long getId() {
        return 999991;
    }
    public Comment[] getAllComments() {
        return new Comment[] {new MockComment()};
    }
}
