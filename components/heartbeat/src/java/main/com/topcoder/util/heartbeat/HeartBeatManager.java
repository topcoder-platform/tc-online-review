/**
 * TCS Heartbeat
 *
 * HeartBeatManager.java
 *
 * Copyright (c) 2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.heartbeat;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * The HeartBeatManager is the main class for the component. Each HeartBeat
 * is associated one or more HeartBeatTriggers. The HeartBeatTriggers are
 * responsible for trigging the HeartBeat to do its work.
 * <p>
 * This class provides the methods to manage both the HeartBeats and their
 * associated HeartBeatTriggers.
 * </p>
 * @author TCSSubmitter, TCSDeveloper
 * @version 1.0
 */
public class HeartBeatManager {

    /**
     * The list of HeartBeatTriggers.
     * @see HeartBeatTrigger
     */
    private List triggers = new ArrayList();

    /**
     * The HeartBeatTrigger used as the default target for the
     * schedule(HeartBeat, 0, long) method
     * @see TimerTrigger#schedule(HeartBeat, long, long)
     */
    private TimerTrigger timerTrigger = new TimerTrigger();

    /**
     * Constructs the HeartBeatManager with an empty list of triggers
     */
    public HeartBeatManager() {
    }

    /**
     * Adds the specified HeartBeat repeating at the specified time. The method
     * uses an instance of TimerTrigger to execute the HeartBeat at the
     * specified time with no delay. This method is simply a wrapper call
     * around timerTrigger.schedule(heartBeat, 0, period)
     *
     * @param heartBeat the HeartBeat to add
     * @param period the period between executions in milliseconds
     *
     * @throws NullPointerException if heartBeat is null
     * @throws IllegalArgumentException if period is less than or equal to zero
     *
     * @see HeartBeat
     * @see TimerTrigger#schedule(HeartBeat, long, long)
     */
    public synchronized void add(HeartBeat heartBeat,
                                 long period)
            throws NullPointerException, IllegalArgumentException {
        if (heartBeat == null) {
            throw new NullPointerException("heartBeat is null");
        }
        if (period <= 0) {
            throw new IllegalArgumentException("period is non-positive");
        }
        timerTrigger.schedule(heartBeat, 0, period);
    }

    /**
     * Adds the specified heart beat trigger to the list of triggers. Assumes
     * that HeartBeats have already been added or will be added to the trigger.
     *
     * @param trigger the HeartBeatTrigger to add
     *
     * @throws NullPointerException if trigger is null
     *
     * @see HeartBeatTrigger
     */
    public synchronized void add(HeartBeatTrigger trigger)
            throws NullPointerException {
        if (trigger == null) {
            throw new NullPointerException("trigger is null");
        }
        triggers.add(trigger);
    }

    /**
     * Returns a list of all the heart beats registered in any of the
     * HeartBeatTriggers (including the internal TimerTrigger).
     *
     * @return a list of all the heart beats
     *
     * @see HeartBeat
     * @see HeartBeatTrigger#getHeartBeats()
     */
    public synchronized List getHeartBeats() {
        List ret = timerTrigger.getHeartBeats();
        for (Iterator itr = triggers.iterator(); itr.hasNext();) {
            ret.addAll(((HeartBeatTrigger) itr.next()).getHeartBeats());
        }
        return ret;
    }

    /**
     * Returns the list of heart beat triggers.
     *
     * @return the list of heart beat triggers.
     *
     * @see HeartBeatTrigger
     */
    public synchronized List getHeartBeatTriggers() {
        List ret = new ArrayList(triggers);
        ret.add(timerTrigger);
        return ret;
    }

    /**
     * Removes the specified heart beat from all HeartBeatTrigger(s) it is
     * registered with.
     *
     * @param heartBeat the HeartBeat to remove
     *
     * @return true if the HeartBeat was found and removed from atleast one
     * HeartBeatTrigger, false otherwise
     *
     * @throws NullPointerException if heartBeat is null
     *
     * @see HeartBeat
     * @see HeartBeatTrigger#remove(HeartBeat)
     */
    public synchronized boolean remove(HeartBeat heartBeat)
            throws NullPointerException {
        if (heartBeat == null) {
            throw new NullPointerException("heartBeat is null");
        }
        boolean ret = timerTrigger.remove(heartBeat);
        for (Iterator itr = triggers.iterator(); itr.hasNext();) {
            if (((HeartBeatTrigger) itr.next()).remove(heartBeat)) {
                ret = true;
            }
        }
        return ret;
    }

    /**
     * Removes the heart beat trigger from the list of triggers. It is the
     * callers responsibility to remove any registered HeartBeat and to stop
     * the trigger.
     *
     * @param trigger the HeartBeatTrigger to remove.
     *
     * @return true if found and removed, false otherwise
     *
     * @throws NullPointerException if trigger is null
     *
     * @see HeartBeatTrigger
     */
    public synchronized boolean remove(HeartBeatTrigger trigger)
            throws NullPointerException {
        if (trigger == null) {
            throw new NullPointerException("trigger is null");
        }
        return triggers.remove(trigger);
    }

    /**
     * Removes all HeartBeat(s) from all HeartBeatTrigger(s) and returns a list
     * of the HeartBeats.
     *
     * @return the list of HeartBeat(s) that were returned. The list may be
     * empty.
     *
     * @see HeartBeat
     * @see HeartBeatTrigger#removeAll()
     */
    public synchronized List removeAllHeartBeats() {
        List ret = timerTrigger.removeAll();
        for (Iterator itr = triggers.iterator(); itr.hasNext();) {
            ret.addAll(((HeartBeatTrigger) itr.next()).removeAll());
        }
        return ret;
    }

    /**
     * Removes all HeartBeatTriggers. It is the callers responsibility to
     * remove any registered HeartBeat and to stop the trigger.
     *
     * @return a list of all removed HeartBeatTriggers
     *
     * @see HeartBeatTrigger
     */
    public synchronized List removeAllHeartBeatTriggers() {
        List ret = triggers;
        triggers = new ArrayList();
        return ret;
    }

    /**
     * Determines whether the specified HeartBeat is contained in any of the
     * HeartBeatTriggers.
     *
     * @param heartBeat the HeartBeat to query on.
     *
     * @return true if found in any of the HeartBeatTriggers, false otherwise.
     *
     * @throws NullPointerException if heartBeat is null
     *
     * @see HeartBeat
     * @see HeartBeatTrigger#contains(HeartBeat)
     */
    public synchronized boolean contains(HeartBeat heartBeat)
            throws NullPointerException {
        if (heartBeat == null) {
            throw new NullPointerException("heartBeat is null");
        }
        for (Iterator itr = triggers.iterator(); itr.hasNext();) {
            if (((HeartBeatTrigger) itr.next()).contains(heartBeat)) {
                return true;
            }
        }
        return timerTrigger.contains(heartBeat);
    }

    /**
     * Determines whether the specified HeartBeatTrigger is managed by this
     * class.
     *
     * @param trigger the HeartBeatTrigger to inquire on.
     *
     * @return true if the HeartBeatTrigger is managed by this class, false
     * otherwise.
     *
     * @throws NullPointerException if trigger is null
     */
    public synchronized boolean contains(HeartBeatTrigger trigger)
            throws NullPointerException {
        if (trigger == null) {
            throw new NullPointerException("trigger is null");
        }
        return trigger == timerTrigger
            || triggers.contains(trigger);
    }

}
