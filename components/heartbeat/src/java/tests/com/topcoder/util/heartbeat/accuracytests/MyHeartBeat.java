/**
 * TCS Heartbeat
 *
 * MyHeartBeat.java
 *
 * Copyright (c) 2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.heartbeat.accuracytests;

import com.topcoder.util.heartbeat.*;
import java.util.List;

/**
 * A trivial HeartBeat implementation for testing.
 *
 * @version 1.0
 */
class MyHeartBeat implements HeartBeat {

    /**
     * Number of fired count.
     */
    private int numFired = 0;

    /**
     * Get fired count.
     *
     * @return fired count
     */
    public int getFiredCount() {
        return numFired;
    }

    /**
     * Keep heartbeat.
     */
    public void keepAlive() {
        ++numFired;
    }

    /**
     * Get last exception.
     *
     * @return null
     */
    public Exception getLastException() {
        return null;
    }

}
