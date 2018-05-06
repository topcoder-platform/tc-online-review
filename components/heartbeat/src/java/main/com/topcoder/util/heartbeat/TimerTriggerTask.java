/**
 * TCS Heartbeat
 *
 * TimerTriggerTask.java
 *
 * Copyright (c) 2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.heartbeat;

import java.util.TimerTask;

/**
 * Implementation of a TimerTask that will execute the specified HeartBeat
 * action when run.
 *
 * @author TCSSubmitter, TCSDeveloper
 * @version 1.0
 * @see HeartBeat
 * @see TimerTrigger
 * @see java.util.TimerTask
 */
class TimerTriggerTask extends TimerTask {

    /**
     * The HeartBeat action that will be executed
     *
     * @see HeartBeat
     */
    private HeartBeat heartBeat = null;

    /**
     * Constructor of TimerTriggerTask from the specified HeartBeat
     *
     * @param heartBeat the HeartBeat
     *
     * @throws NullPointerException if heartBeat is null
     *
     * @see HeartBeat
     */
    TimerTriggerTask(HeartBeat heartBeat)
            throws NullPointerException {
        if (heartBeat == null) {
            throw new NullPointerException("heartBeat is null");
        }
        this.heartBeat = heartBeat;
    }

    /**
     * Returns the HeartBeat action that will be executed.
     *
     * @return the HeartBeat action that will be executed.
     *
     * @see HeartBeat
     */
    HeartBeat getHeartBeat() {
        return heartBeat;
    }

    /**
     * Executes the HeartBeat's keepAlive() method.
     *
     * @see HeartBeat#keepAlive()
     */
    public void run() {
        heartBeat.keepAlive();
    }

}
