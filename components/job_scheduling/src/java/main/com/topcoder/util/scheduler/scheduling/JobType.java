/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling;

import com.topcoder.util.collection.typesafeenum.Enum;

/**
 * <p>
 * This is an enumeration of the four types of Jobs: <b>External</b>, <b>Java
 * Class</b>, <b>the job created by object factory configured in the given
 * namespace</b> and <b>the job created by
 * <code>ScheduledEnableObjectFactory</code></b>.
 * </p>
 * <p>
 * This class extends the <code>Enum</code> class form the Type Safe
 * Enumeration component.
 * </p>
 * <p>
 * Changes in version 3.1: two new job types are added:
 * <code>JobType.JOB_TYPE_SCHEDULED_ENABLE_OBJECT_NAMESPACE</code> and
 * <code>JobType.JOB_TYPE_SCHEDULED_ENABLE_OBJECT_FACTORY</code>.
 * <code>JobType.JOB_TYPE_SCHEDULED_ENABLE_OBJECT_NAMESPACE</code> represents
 * the job type to create the
 * <code>ScheduledEnable</CODE> object using Object Factory with configured values loaded
 * from Configuration Manager. <code>JobTYpe.JOB_TYPE_SCHEDULED_ENABLE_OBJECT_FACTORY</code> represents the
 * job type to create the <code>ScheduledEnable</code> object from the
 * ScheduledEnableObjectFactory.
 * </p>
 * <p>
 * Thread Safety : This class is thread safe for immutable and its super class
 * is thread safe.
 * </p>
 * @author argolite, TCSDEVELOPER
 * @author Standlove, TCSDEVELOPER
 * @version 3.1
 * @since 3.0
 */
public class JobType extends Enum {
    /**
     * <p>
     * Represents an external job type.
     * </p>
     */
    public static final JobType JOB_TYPE_EXTERNAL = new JobType(0);

    /**
     * <p>
     * Represents a java class job type.
     * </p>
     */
    public static final JobType JOB_TYPE_JAVA_CLASS = new JobType(1);

    /**
     * <p>
     * Represents the job type to create the <code>ScheduledEnable</code>
     * object using Object Factory with configured values loaded from
     * Configuration Manager.
     * </p>
     * @since 3.1
     */
    public static final JobType JOB_TYPE_SCHEDULED_ENABLE_OBJECT_NAMESPACE = new JobType(
            2);

    /**
     * <p>
     * Represents the job type to create the <code>ScheduledEnable</code>
     * object from the <code>ScheduledEnableObjectFactory</code>.
     * </p>
     * @since 3.1
     */
    public static final JobType JOB_TYPE_SCHEDULED_ENABLE_OBJECT_FACTORY = new JobType(
            3);

    /**
     * <p>
     * Represents the type as a int.
     * </p>
     * <p>
     * It is set in the constructor and is immutable.
     * </p>
     */
    private final int type;

    /**
     * <p>
     * Constructs a <code>JobType</code> with type given.
     * </p>
     * <p>
     * This private constructor prevents to create a new instance.
     * </p>
     * @param type The type to set
     */
    private JobType(int type) {
        this.type = type;
    }

    /**
     * <p>
     * Gets the type.
     * </p>
     * @return the type
     */
    public int getType() {
        return type;
    }
}
