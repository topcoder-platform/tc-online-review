/**
 * TCS Heartbeat
 *
 * HeartBeatTrigger.java
 *
 * Copyright (c) 2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.heartbeat;

import java.util.List;

/**
 * Interface defining methods that a HeartBeatTrigger needs to implement. A
 * HeartBeatTrigger implementation is responsible for triggering the heart
 * beat action. This interface defines common methods to retrieve and
 * remove HeartBeats from the trigger. This interface does NOT define
 * methods to add HeartBeats to the trigger since the specific
 * implementations may require additional parameters to operate properly.
 *
 * @author TCSSubmitter, TSCDeveloper
 * @version 1.0
 */
public interface HeartBeatTrigger {

    /**
     * Removes the specified HeartBeat from the HeartBeatTrigger.
     *
     * @param heartBeat the HeartBeat to remove.
     * @return true if the HeartBeat was found and removed, false otherwise.
     * @see HeartBeat
     */
    boolean remove(HeartBeat heartBeat);

    /**
     * Removes all HeartBeat(s) within this trigger.
     *
     * @return the list of HeartBeat(s) that were removed.  The list may be
     * empty.
     * @see HeartBeat
     */
    List removeAll();

    /**
     * Determines whether the specified HeartBeat is registered with this
     * trigger.
     *
     * @param heartBeat the HeartBeat to inquire on.
     * @return true if the HeartBeat is registered with this trigger, false
     * otherwise.
     * @see HeartBeat
     */
    boolean contains(HeartBeat heartBeat);

    /**
     * Returns a list of HeartBeats that are registered with the trigger.
     *
     * @return a list of HeartBeats that are registered with the trigger.
     * @see HeartBeat
     */
    List getHeartBeats();

}
