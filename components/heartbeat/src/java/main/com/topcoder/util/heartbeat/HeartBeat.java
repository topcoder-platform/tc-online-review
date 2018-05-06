/**
 * TCS Heartbeat
 *
 * HeartBeat.java
 *
 * Copyright (c) 2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.heartbeat;

/**
 * Interface defining the methods a HeartBeat implementation must
 * implement. A HeartBeat implements the protocol for a HeartBeat.
 *
 * @author TCSSubmitter, TCSDeveloper
 * @version 1.0
 */
public interface HeartBeat {

    /**
     * This method will be called whenever a HeartBeat should be executed.
     */
    void keepAlive();

    /**
     * Returns the last exception that occurred in the keepAlive() method. Will
     * return null if the last keepAlive() method invocation was successful.
     *
     * @return the last exception that occurred or null if none occurred.
     */
    Exception getLastException();

}
