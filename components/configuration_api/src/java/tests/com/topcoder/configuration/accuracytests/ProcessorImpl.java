/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.configuration.accuracytests;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.ProcessException;
import com.topcoder.configuration.Processor;

import java.util.HashSet;
import java.util.Set;

/**
 * The mock implement Processor.
 *
 * @author KKD
 * @version 1.0
 */
public class ProcessorImpl implements Processor {
    /**
     * The set.
     */
    @SuppressWarnings("unchecked")
    private Set processedNodes = new HashSet();

    /**
     * The implement.
     *
     * @param config
     *            the ConfigurationObject
     * @throws ProcessException
     *             never throw
     */
    @SuppressWarnings("unchecked")
    public void process(ConfigurationObject config) throws ProcessException {
        try {
            processedNodes.add(config.getName());
        } catch (Exception e) {
            // pass
        }
    }

    /**
     * Get all the node processed.
     *
     * @return the processed node
     */
    @SuppressWarnings("unchecked")
    public Set getAllProcessedNodes() {
        return processedNodes;
    }
}
