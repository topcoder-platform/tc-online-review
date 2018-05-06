/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * <p>
 * A job group is a grouping of jobs to simplify job scheduler configuration.
 * </p>
 *
 * <p>
 * A group can contains one or more jobs. A job can belong to one or more groups.
 * </p>
 *
 * <p>
 * In current version, the job groups are used to ease &quot;Alert Notification Configuration&quot;.
 * </p>
 *
 * <p>
 * Thread Safety : the only mutable property is handlers, the access of it is synchronized
 * so that this class is thread-safe.
 * </p>
 *
 * @author dmks, singlewood
 * @author argolite, TCSDEVELOPER
 * @version 3.0
 * @since 2.0
 */
public class JobGroup {
    /**
     * <p>
     * Represents the jobs in this group.
     * </p>
     *
     * <p>
     * It contains <code>Job</code> objects.
     * </p>
     *
     * <p>
     * It is initialized in the constructor and never changed later.
     * </p>
     *
     * <p>
     * Can't be null or empty. Can't contain null.
     * </p>
     */
    private final List jobs;

    /**
     * <p>
     * Represents event handlers listening at this group.
     * </p>
     *
     * <p>
     * If any of the jobs of this group raised an event, The handlers will be fired.
     * </p>
     *
     * <p>
     * This property can be accessed by the addHandler, removeHandler, getHandlers and fireEvent method.
     * </p>
     *
     * <p>
     * The access of it is synchronized.
     * </p>
     *
     * <p>
     * It can't be null but can be empty.
     * It contains <code>EventHandler</code> objects and can't contain null.
     * </p>
     */
    private final List handlers = Collections.synchronizedList(new ArrayList());

    /**
     * <p>
     * Represents name of this group. It is initialized in the constructor and never changed later.
     * </p>
     *
     * <p>
     * Can't be null or empty.
     * </p>
     */
    private final String name;

    /**
     * <p>
     * Constructs a <code>JobGroup</code> with the name and all its jobs given.
     * </p>
     *
     * @param name the group name
     * @param jobs the jobs of this group.
     *
     * @throws IllegalArgumentException if name is null or empty, or jobs is null or empty or
     * contains non-Job element
     */
    public JobGroup(String name, List jobs) {
        Util.checkStringNotNullAndEmpty(name, "name");

        // Check elements in the jobs list. If legal, add them to the private jobs list.
        Util.checkListNotNullAndEmpty(jobs, "jobs");

        for (Iterator iter = jobs.iterator(); iter.hasNext();) {
            Object element = iter.next();

            if (!(element instanceof Job)) {
                throw new IllegalArgumentException("jobs contains non-Job or null elements.");
            }
        }

        this.jobs = new ArrayList(jobs);
        this.name = name;
    }

    /**
     * <p>
     * Returns the group name.
     * </p>
     *
     * @return the group name
     */
    public String getName() {
        return name;
    }

    /**
     * <p>
     * Returns the jobs in this group.
     * </p>
     *
     * @return the jobs in this group
     */
    public Job[] getJobs() {
        return (Job[]) jobs.toArray(new Job[jobs.size()]);
    }

    /**
     * <p>
     * Returns the event handlers in this group.
     * </p>
     *
     * @return the event handlers in this group
     */
    public EventHandler[] getHandlers() {
        synchronized (handlers) {
            return (EventHandler[]) handlers.toArray(new EventHandler[handlers.size()]);
        }
    }

    /**
     * <p>
     * Adds an event handler to the handlers in this job group.
     * </p>
     *
     * @param handler the event handler to add to the handlers list.
     *
     * @throws IllegalArgumentException if the handler is null.
     */
    public void addHandler(EventHandler handler) {
        Util.checkObjectNotNull(handler, "handler");

        handlers.add(handler);
    }

    /**
     * <p>
     * Removes an event handler from the handlers list.
     * </p>
     *
     * @param handler the event handler to remove
     *
     * @throws IllegalArgumentException if the handler is null.
     */
    public void removeHandler(EventHandler handler) {
        Util.checkObjectNotNull(handler, "handler");

        handlers.remove(handler);
    }

    /**
     * <p>
     * Fires the handlers listening on the event of the jobs.
     * </p>
     *
     * @param job the job raised the event
     * @param event the event raised by the job
     *
     * @throws IllegalArgumentException if any parameter is null or the event is not one of
     * the events in EventHandler.
     */
    public void fireEvent(Job job, String event) {
        Util.checkObjectNotNull(job, "job");
        Util.checkEventHandler(event, "event");

        synchronized (handlers) {
            for (Iterator iter = handlers.iterator(); iter.hasNext();) {
                EventHandler handler = (EventHandler) iter.next();
                handler.handle(job, event);
            }
        }
    }
}
