/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.configuration;
/**
 * <p>
 * The mock class implements BaseConfigurationObject.
 * </p>
 *
 * @author haozhangr
 * @version 1.0
 */
public class ProcessorMock implements Processor {
    /**
     * The count.
     */
    private int count = 0;
    /**
     * The empty constructor.
     *
     */
    public ProcessorMock() {
    }
    /**
     * The implementing method <code>process</code>.
     *
     * @param config ignore
     * @throws ProcessException never
     */
    public void process(ConfigurationObject config) throws ProcessException {
        count++;
    }
    /**
     * Get the count.
     *
     * @return the value of count.
     */
    public int getCount() {
        return count;
    }

}
