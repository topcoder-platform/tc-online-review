/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.project.phases.template.stresstests;

/**
 * <p> This is the interface used for the thread to execute the specific operations. </p>
 *
 * @author yuanyeyuanye
 * @version 1.0
 */
public interface Action {
    /**
     * Execute the specific method.
     *
     * @throws Exception If any exception occurs.
     */
    public void act() throws Exception;
}
