/**
 * TCS Heartbeat
 *
 * ManualTrigger.java
 *
 * Copyright (c) 2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.heartbeat;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Implementation of a HeartBeatTrigger that will trigger the HeartBeat
 * action keepAlive() only when the fireKeepAlive() method is executed.
 *
 * @author TCSSubmitter, TCSDeveloper
 * @version 1.0
 * @see HeartBeat#keepAlive()
 */
public class ManualTrigger implements HeartBeatTrigger {

    /**
     * List of HeartBeat(s) that are registered with this trigger.
     */
    private List heartBeats = new ArrayList();

    /**
     * Adds a HeartBeat to the list of HeartBeat(s) that will be triggered.
     *
     * @param heartBeat the HeartBeat to add.
     *
     * @throws NullPointerException if heartBeat is null
     *
     * @see HeartBeat
     */
    public synchronized void add(HeartBeat heartBeat)
            throws NullPointerException {
        if (heartBeat == null) {
            throw new NullPointerException("heartBeat is null");
        }
        if (!heartBeats.contains(heartBeat)) {
            heartBeats.add(heartBeat);
        }
    }

    /**
     * Removes the specified HeartBeat from the list of HeartBeats that will be
     * triggered
     *
     * @param heartBeat the HeartBeat to remove
     *
     * @return true if the HeartBeat was found and removed, false otherwise.
     *
     * @throws NullPointerException if heartBeat is null
     *
     * @see HeartBeat
     */
    public synchronized boolean remove(HeartBeat heartBeat)
            throws NullPointerException {
        if (heartBeat == null) {
            throw new NullPointerException("heartBeat is null");
        }
        return heartBeats.remove(heartBeat);
    }

    /**
     * Removes all HeartBeat(s) from this trigger.
     *
     * @return a list of HeartBeats that were removed. The list may be empty.
     *
     * @see HeartBeat
     */
    public synchronized List removeAll() {
        List ret = heartBeats;
        heartBeats = new ArrayList();
        return ret;
    }

    /**
     * Determines whether the HeartBeat is registered with this trigger.
     *
     * @param heartBeat the HeartBeat to inquire on.
     *
     * @return true if the HeartBeat is registered with this trigger, false
     * otherwise.
     *
     * @throws NullPointerException if heartBeat is null
     *
     * @see HeartBeat
     */
    public synchronized boolean contains(HeartBeat heartBeat)
            throws NullPointerException {
        if (heartBeat == null) {
            throw new NullPointerException("heartBeat is null");
        }
        return heartBeats.contains(heartBeat);
    }

    /**
     * Returns a list of HeartBeat(s) that are registered with this trigger.
     *
     * @return a list of HeartBeat(s) that are registered with this trigger.
     *
     * @see HeartBeat
     */
    public synchronized List getHeartBeats() {
        return new ArrayList(heartBeats);
    }

    /**
     * Triggers the heart beat action of all registered HeartBeats
     *
     * @see HeartBeat#keepAlive()
     */
    public synchronized void fireKeepAlive() {
        for (Iterator itr = heartBeats.iterator(); itr.hasNext();) {
            ((HeartBeat) itr.next()).keepAlive();
        }
    }

}
