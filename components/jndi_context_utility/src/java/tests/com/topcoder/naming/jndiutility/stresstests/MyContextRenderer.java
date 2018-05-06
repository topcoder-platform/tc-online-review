/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.naming.jndiutility.stresstests;

import com.topcoder.naming.jndiutility.ContextRenderer;
/**
 *  mock implement of ContextRenderer for testing.
 *
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class MyContextRenderer implements ContextRenderer {

    public void startContext(String ctxFullName, String ctxRelativeName) {      
    }

    public void endContext(String ctxFullName, String ctxRelativeName) {
    }

    public void bindingFound(String name, String type) {
    }
}