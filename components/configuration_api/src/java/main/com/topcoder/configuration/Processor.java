/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.configuration;


/**
 * <p>
 * This interface defines the contract for ConfigurationObject processor.
 * It only contains a method named as process, which takes a ConfigurationObject
 * parameter and returns nothing.
 * </p>
 *
 * <p>
 * Typically, it would be passed to ConfigurationObject#processDescendants to process all the descendants.
 * </p>
 *
 * <p>Thread safe: All the implementation should treat the thread safe problem by their own.</p>
 *
 * @author maone, haozhangr
 * @version 1.0
 */
public interface Processor {
    /**
     * Process the given ConfigurationObject instance.
     *
     * @param config the ConfigurationObject to be processed
     * @throws IllegalArgumentException if any argument is null
     * @throws ProcessException if any error occurs while processing
     */
    public void process(ConfigurationObject config) throws ProcessException;
}
