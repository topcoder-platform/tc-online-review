/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.date.workdays;

/**
 * <p>
 * This is the interface that every Workdays factory should implement. An instance of this interface creates a Workdays
 * instance.
 * </p>
 *
 * <p>
 * <strong>Thread Safety:</strong> Implementations of this interface are not required to be thread safe.
 * </p>
 *
 * @author TCSDESIGNER, TCSDEVELOPER
 * @version 1.0
 */
public interface WorkdaysFactory {
    /**
     * <p>
     * Creates an instance of Workdays.
     * </p>
     *
     * @return a Workdays instance
     */
    public Workdays createWorkdaysInstance();
}
