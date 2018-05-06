/**
 * TCS Heartbeat
 *
 * TimerTrigger.java
 *
 * Copyright (c) 2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.heartbeat;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Implementation of a HeartBeatTrigger that will trigger the HeartBeat
 * action keepAlive() based on timing.  This implementation roughly follows
 * the java.util.Timer implementation to provide a number of ways to time a
 * HeartBeat action.
 *
 * @author TCSSubmitter, TCSDeveloper
 * @version 1.0
 * @see HeartBeat#keepAlive()
 */
public class TimerTrigger implements HeartBeatTrigger {

    /**
     * The timer that will be used to trigger the heart beat actions. Note:
     * the timer is set as a daemon thread
     */
    private Timer timer = new Timer(true);

    /**
     * List of the TimerTriggerTasks (which wrap each HeartBeat) that are
     * registered.
     */
    private List timerTriggerTasks = new ArrayList();

    /**
     * Constructor of the TimerTrigger with no tasks.
     */
    public TimerTrigger() {
    }

    /**
     * Schedules a heart beat action that will be executed <b>once</b> at the
     * specified time. If the time represents a date in the past, the heart
     * beat action will immediately be executed. Once the heart beat action has
     * finished, the task will be removed from this trigger.
     *
     * @param heartBeat the heart beat action.
     * @param time the time to execute the heart beat
     *
     * @throws NullPointerException if heartBeat or time is null.
     *
     * @see HeartBeat
     * @see TimerTriggerTask
     * @see java.util.Timer#schedule(TimerTask, Date)
     */
    public synchronized void schedule(HeartBeat heartBeat,
                                      Date time)
            throws NullPointerException {
        if (heartBeat == null) {
            throw new NullPointerException("heartBeat is null");
        }
        if (time == null) {
            throw new NullPointerException("time is null");
        }
        TimerTriggerTask task = new TimerTriggerTask(heartBeat);
        timerTriggerTasks.add(task);
        timer.schedule(new MyTimerTriggerTask(task), time);
    }

    /**
     * Schedules a heart beat action that will start at the specified time and
     * repeat every specified milliseconds (see java.util.Timer documentation
     * for timing issues). If the time represents a date in the past, the heart
     * beat will immediately be executed. The heart beat action will be
     * repeated until removed from this trigger. The heart beat tasks will be
     * scheduled to be sequential (ie synchronized with each other). It is up
     * to the calling application to ensure that the period of time between
     * execution is sufficiently large enough not to overlap.
     *
     * @param heartBeat the heart beat action.
     * @param firstTime the time to begin executing the heart beat action
     * @param period time in milliseconds between successive heart beat actions
     *
     * @throws IllegalArgumentException if period is less or equal to 0.
     * @throws NullPointerException if heartBeat or firstTime is null.
     *
     * @see HeartBeat
     * @see TimerTriggerTask
     * @see java.util.Timer#scheduleAtFixedRate(TimerTask, Date, long)
     */
    public synchronized void schedule(HeartBeat heartBeat,
                                      Date firstTime,
                                      long period)
            throws IllegalArgumentException, NullPointerException {
        if (heartBeat == null) {
            throw new NullPointerException("heartBeat is null");
        }
        if (firstTime == null) {
            throw new NullPointerException("firstTime is null");
        }
        if (period <= 0) {
            throw new IllegalArgumentException("period is non-positive");
        }
        TimerTriggerTask task = new TimerTriggerTask(heartBeat);
        timerTriggerTasks.add(task);
        timer.schedule(task, firstTime, period);
    }

    /**
     * Schedules a heart beat action that will be executed <b>once</b> after
     * the specified delay. If the delay is zero, the heart beat action will
     * immediately be executed. Once the heart beat action has finished, the
     * task will be removed from this trigger.
     *
     * @param heartBeat the heart beat action.
     * @param delay delay in milliseconds before the heart beat action is
     * executed
     *
     * @throws IllegalArgumentException if delay is less than 0
     * @throws NullPointerException if heartBeat is null.
     *
     * @see HeartBeat
     * @see TimerTriggerTask
     * @see java.util.Timer#schedule(TimerTask, long)
     */
    public synchronized void schedule(HeartBeat heartBeat,
                                      long delay)
            throws IllegalArgumentException, NullPointerException {
        if (heartBeat == null) {
            throw new NullPointerException("heartBeat is null");
        }
        if (delay < 0) {
            throw new IllegalArgumentException("delay is negative");
        }
        TimerTriggerTask task = new TimerTriggerTask(heartBeat);
        timerTriggerTasks.add(task);
        timer.schedule(new MyTimerTriggerTask(task), delay);
    }

    /**
     * Schedules a heart beat action that will start after the specified delay
     * and repeat every specified milliseconds (see java.util.Timer
     * documentation for timing issues). If the delay is zero, the heart beat
     * will immediately be executed. The heart beat action will be repeated
     * until removed from this trigger. The heart beat tasks will be scheduled
     * to be sequential (ie synchronized with each other). It is up to the
     * calling application to ensure that the period of time between execution
     * is sufficiently large enough not to overlap.
     *
     * @param heartBeat the heart beat action.
     * @param delay delay in milliseconds before the heart beat action is
     * executed
     * @param period time in milliseconds between successive heart beat actions
     *
     * @throws IllegalArgumentException if delay is less than 0 or period is
     * less or equal to 0.
     * @throws NullPointerException if heartBeat is null.
     *
     * @see HeartBeat
     * @see TimerTriggerTask
     * @see java.util.Timer#scheduleAtFixedRate(TimerTask, long, long)
     */
    public synchronized void schedule(HeartBeat heartBeat,
                                      long delay,
                                      long period)
            throws NullPointerException, IllegalArgumentException {
        if (heartBeat == null) {
            throw new NullPointerException("heartBeat is null");
        }
        if (delay < 0) {
            throw new IllegalArgumentException("delay is negative");
        }
        if (period <= 0) {
            throw new IllegalArgumentException("period is non-positive");
        }
        TimerTriggerTask task = new TimerTriggerTask(heartBeat);
        timerTriggerTasks.add(task);
        timer.schedule(task, delay, period);
    }

    /**
     * Removes the specified heart beat from the trigger.  This action will
     * cancel the execution task prior to returning. If the heart beat action
     * is currently in progress, the current heart beat action will run to
     * completion.
     *
     * @param heartBeat the heart beat to remove.
     *
     * @return true if heartBeat has been found and remove, false otherwise.
     *
     * @throws NullPointerException if heartBeat is null.
     *
     * @see HeartBeat
     * @see TimerTriggerTask
     * @see java.util.TimerTask#cancel()
     */
    public synchronized boolean remove(HeartBeat heartBeat)
            throws NullPointerException {
        if (heartBeat == null) {
            throw new NullPointerException("heartBeat is null");
        }
        boolean ret = false;
        TimerTriggerTask timerTriggerTask;
        for (Iterator itr = timerTriggerTasks.iterator(); itr.hasNext();) {
            timerTriggerTask = (TimerTriggerTask) itr.next();
            if (timerTriggerTask.getHeartBeat() == heartBeat) {
                timerTriggerTask.cancel();
                itr.remove();
                ret = true;
            }
        }
        return ret;
    }

    /**
     * Removes the all HeartBeat(s) from this trigger. This action will cancel
     * the execution task of each HeartBeat prior to returning. If any heart
     * beat action is currently in progress, the heart beat action will run to
     * completion.
     *
     * @return the list of HeartBeats that were removed. The list may be empty.
     *
     * @see HeartBeat
     * @see TimerTriggerTask
     * @see java.util.TimerTask#cancel()
     */
    public synchronized List removeAll() {
        List ret = new ArrayList();
        TimerTriggerTask timerTriggerTask;
        for (Iterator itr = timerTriggerTasks.iterator(); itr.hasNext();) {
            timerTriggerTask = (TimerTriggerTask) itr.next();
            timerTriggerTask.cancel();
            ret.add(timerTriggerTask.getHeartBeat());
        }
        timerTriggerTasks.clear();
        return ret;
    }

    /**
     * Determines if the specified HeartBeat is registered in this trigger.
     * The trigger will search all existing TimerTriggerTasks to see if the
     * HeartBeat exists.
     *
     * @param heartBeat the HeartBeat to inquire on.
     *
     * @return true if the HeartBeat is registered, false otherwise.
     *
     * @throws NullPointerException if heartBeat is null.
     *
     * @see HeartBeat
     * @see TimerTriggerTask#getHeartBeat()
     */
    public synchronized boolean contains(HeartBeat heartBeat)
            throws NullPointerException {
        if (heartBeat == null) {
            throw new NullPointerException("heartBeat is null");
        }
        return getHeartBeats().contains(heartBeat);
    }

    /**
     * Returns a list of all HeartBeat(s) within this trigger.
     *
     * @return a list of all HeartBeat(s) within this trigger.
     */
    public synchronized List getHeartBeats() {
        List ret = new ArrayList();
        TimerTriggerTask timerTriggerTask;
        for (Iterator itr = timerTriggerTasks.iterator(); itr.hasNext();) {
            timerTriggerTask = (TimerTriggerTask) itr.next();
            ret.add(timerTriggerTask.getHeartBeat());
        }
        return ret;
    }

    /**
     * A wrapper class to remove one-time timer task.
     */
    class MyTimerTriggerTask extends TimerTask {

        /**
         * The TimerTriggerTask to trigger and remove.
         */
        private TimerTriggerTask timerTriggerTask = null;

        /**
         * Constructor.
         *
         * @param timerTriggerTask the TimerTriggerTask to trigger and remove
         */
        MyTimerTriggerTask(TimerTriggerTask timerTriggerTask) {
            this.timerTriggerTask = timerTriggerTask;
        }

        /**
         * TimerTask body.
         */
        public void run() {
            timerTriggerTask.run();
            timerTriggerTasks.remove(timerTriggerTask);
        }

    }

}
