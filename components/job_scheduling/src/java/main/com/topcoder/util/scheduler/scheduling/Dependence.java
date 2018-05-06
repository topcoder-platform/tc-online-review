/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling;

/**
 * <p>
 * A job can be dependent on another job regarding execution time. This class represents this
 * relationship.
 * </p>
 *
 * <p>
 * It has a dependent job name, dependent event and delay properties.
 * </p>
 *
 * <p>
 * Job in a group must be external or internal java class implemented <code>ScheduledEnable</code> interface.
 * </p>
 *
 * <p>
 * Thread Safety : This class is immutable and thread-safe.
 * </p>
 *
 * @author dmks, singlewood
 * @author argolite, TCSDEVELOPER
 * @version 3.0
 * @since 2.0
 */
public class Dependence {
    /**
     * <p>
     * Represents the job depends on the completion of another job, regardless whether it executed
     * successfully or not.
     * </p>
     */
    public static final String BOTH = "BOTH";

    /**
     * <p>
     * Represents the name of the job which another job is dependent on.
     * </p>
     *
     * <p>
     * It is initialized in the constructor and never changed later.
     * </p>
     *
     * <p>
     * It will never be null or empty.
     * </p>
     */
    private final String dependentJobName;

    /**
     * <p>
     * Represents the event of the dependentJob which will trigger another job to start.
     * </p>
     *
     * <p>
     * It is initialized in the constructor and never changed later.
     * </p>
     *
     * <p>
     * It can be one of the <code>EventHandler.SUCCESSFUL</code>, <code>EventHandler.FAILED</code>,
     * and <code>Dependence.BOTH</code>.
     * </p>
     */
    private final String dependentEvent;

    /**
     * <p>
     * Represents an option time delay before the triggered job executes after the dependent job.
     * </p>
     *
     * <p>
     * The unit of delay is millisecond.
     * </p>
     *
     * <p>
     * It is initialized in the constructor and never changed later.
     * </p>
     *
     * <p>
     * It Should be &gt;=0.
     * </p>
     */
    private final int delay;

    /**
     * <p>
     * Creates a new <code>Dependence</code>.
     * </p>
     *
     * @param jobName the dependent job name
     * @param event the dependent event
     * @param delay the option time delay before the triggered job executes after the dependent job
     *
     * @throws IllegalArgumentException if any parameter is invalid.
     */
    public Dependence(String jobName, String event, int delay) {
        Util.checkStringNotNullAndEmpty(jobName, "jobName");
        Util.checkObjectNotNull(event, "event");
        if (!(event.equals(EventHandler.SUCCESSFUL) || event.equals(EventHandler.FAILED)
            || event.equals(Dependence.BOTH))) {
            throw new IllegalArgumentException(
                "event is not one of EventHandler.SUCCESSFUL, EventHandler.FAILED, Dependence.BOTH)");
        }

        if (delay < 0) {
            throw new IllegalArgumentException("Illegal delay value, should be >= 0.");
        }

        this.dependentJobName = jobName;
        this.dependentEvent = event;
        this.delay = delay;
    }

    /**
     * <p>
     * Returns the name of the job which another job is dependent on.
     * </p>
     *
     * @return the name of the job which another job is dependent on
     */
    public String getDependentJobName() {
        return dependentJobName;
    }

    /**
     * <p>
     * Returns the dependent event which will trigger another job to start.
     * </p>
     *
     * @return the dependent event which will trigger another job to start.
     */
    public String getDependentEvent() {
        return dependentEvent;
    }

    /**
     * <p>
     * Returns an option time delay before the triggered job executes after the dependent job.
     * </p>
     *
     * <p>
     * Note, the unit is millisecond.
     * </p>
     *
     * @return an option time delay before the triggered job executes after the dependent job
     */
    public int getDelay() {
        return delay;
    }
}
