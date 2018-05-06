/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.phase.autopilot;


/**
 * <p>
 * Defines the contract for the source of an auto pilot instance. This interface defines a method to
 * retrieve all project ids to auto-pilot. Interface of this interface will be created by AutoPilot
 * using object factory component.
 * </p>
 * <p>
 * Implementations are not required to be thread-safe. Typically the instance will only be used by a
 * single-thread (eg. from command-line). In a multiple thread situation, application is advised to
 * synchronize on the AutoPilot instance to ensure that only a single thread is retrieving/advancing
 * project phases at one time.
 * </p>
 * @author sindu, abelli
 * @version 1.0
 */
public interface AutoPilotSource {
    /**
     * <p>
     * Gets all project ids that are to be automatically managed by AutoPilot.
     * </p>
     * @return a long[] representing project ids to auto pilot (could be empty, but never null)
     * @throws AutoPilotSourceException if an error occurs retrieving the project ids
     */
    public long[] getProjectIds() throws AutoPilotSourceException;
}
