/*
 * Copyright (C) 2010-2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

/**
 * <p>
 * This class implements PhaseHandler interface to provide methods to check if a phase can be executed and to add
 * extra logic to execute a phase. It will be used by Phase Management component. It is configurable using an input
 * namespace. The configurable parameters include database connection, email sending. This class handles the
 * Checkpoint Screening phase. If the input is of other phase types, PhaseNotSupportedException will be thrown.
 * </p>
 * <p>
 * For details, please see the documentation for the parent BaseScreeningPhaseHandler class.
 * </p>
 * <p>
 * Thread safety: This class is thread safe because it is immutable.
 * </p>
 * @author VolodymyrK
 * @version 1.7.5
 * @since 1.6
 */
public class CheckpointScreeningPhaseHandler extends BaseScreeningPhaseHandler {
    /**
     * <p>
     * Represents the default namespace of this class. It is used in the default constructor to load configuration
     * settings.
     * </p>
     */
    public static final String DEFAULT_NAMESPACE = "com.cronos.onlinereview.phases.CheckpointScreeningPhaseHandler";

    /**
     * <p>
     * Create a new instance of CheckpointScreeningPhaseHandler using the default namespace for loading
     * configuration settings.
     * </p>
     * @throws ConfigurationException
     *             if errors occurred while loading configuration settings.
     */
    public CheckpointScreeningPhaseHandler() throws ConfigurationException {
        super(DEFAULT_NAMESPACE, true);
    }

    /**
     * <p>
     * Create a new instance of CheckpointScreeningPhaseHandler using the given namespace for loading configuration
     * settings.
     * </p>
     * @param namespace
     *            the namespace to load configuration settings from.
     * @throws ConfigurationException
     *             if errors occurred while loading configuration settings.
     * @throws IllegalArgumentException
     *             if the input is null or empty string.
     */
    public CheckpointScreeningPhaseHandler(String namespace) throws ConfigurationException {
        super(namespace, true);
    }
}
