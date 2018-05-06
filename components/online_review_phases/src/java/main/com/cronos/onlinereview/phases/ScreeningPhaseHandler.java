/*
 * Copyright (C) 2009-2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

/**
 * <p>
 * This class implements PhaseHandler interface to provide methods to check if a phase can be executed and to add
 * extra logic to execute a phase. It will be used by Phase Management component. It is configurable using an input
 * namespace. The configurable parameters include database connection, email sending. This class handles the
 * Screening phase. If the input is of other phase types, PhaseNotSupportedException will be thrown.
 * </p>
 * <p>
 * For details, please see the documentation for the parent BaseScreeningPhaseHandler class.
 * </p>
 * <p>
 * Thread safety: This class is thread safe because it is immutable.
 * </p>
 * @author VolodymyrK
 * @version 1.7.5
 */
public class ScreeningPhaseHandler extends BaseScreeningPhaseHandler {
    /**
     * Represents the default namespace of this class. It is used in the default
     * constructor to load configuration settings.
     */
    public static final String DEFAULT_NAMESPACE = "com.cronos.onlinereview.phases.ScreeningPhaseHandler";

    /**
     * Create a new instance of ScreeningPhaseHandler using the default
     * namespace for loading configuration settings.
     * @throws ConfigurationException if errors occurred while loading
     *             configuration settings.
     */
    public ScreeningPhaseHandler() throws ConfigurationException {
        super(DEFAULT_NAMESPACE, false);
    }

    /**
     * Create a new instance of ScreeningPhaseHandler using the given namespace
     * for loading configuration settings.
     * @param namespace the namespace to load configuration settings from.
     * @throws ConfigurationException if errors occurred while loading
     *             configuration settings.
     * @throws IllegalArgumentException if the input is null or empty string.
     */
    public ScreeningPhaseHandler(String namespace) throws ConfigurationException {
        super(namespace, false);
    }
}
