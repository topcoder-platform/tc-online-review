/**
 * TCS Heartbeat
 *
 * URLHeartBeat.java
 *
 * Copyright (c) 2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.heartbeat;

import java.io.InputStream;
import java.net.URL;

/**
 * Implementation of a HeartBeat for a specific URL and is immutable to
 * avoid threading issues.
 * <p>
 * See the keepAlive() method for details on how the HeartBeat works.
 * </p>
 * @author TCSSubmitter, TCSDeveloper
 * @version 1.0
 */
public class URLHeartBeat implements HeartBeat {

    /**
     * The URL this HeartBeat uses.
     */
    private URL url = null;

    /**
     * Represents the last exception that occurred.
     */
    private Exception lastException = null;

    /**
     * Constructs the URLHeartBeat using the specified URL.
     *
     * @param url the URL
     *
     * @throws NullPointerException if url is null.
     */
    public URLHeartBeat(URL url)
            throws NullPointerException {
        if (url == null) {
            throw new NullPointerException("url is null");
        }
        this.url = url;
    }

    /**
     * Returns the URL this HeartBeat queries.
     *
     * @return the URL this HeartBeat queries.
     */
    public URL getURL() {
        return url;
    }

    /**
     * Returns the last exception that occurred in the keepAlive() method.
     * Will return null if the last keepAlive() method invocation was
     * successful.
     *
     * @return the last exception that occurred or null if none occurred.
     */
    public Exception getLastException() {
        return lastException;
    }

    /**
     * The implementation of the HeartBeat for a URL. This implementation will:
     * <ul>
     * <li>Open an inputstream to the URL</li>
     * <li>Read the input stream until EOF</li>
     * <li>Close the input stream</li>
     * <li>Set the lastException variable to null.</li>
     * </ul>
     * <p>
     * If an exception occurs during this process, the input stream (if
     * created) will be closed and the exception will be saved in the
     * lastException variable.
     * </p>
     */
    public void keepAlive() {
        InputStream inputStream = null;
        try {
            inputStream = url.openStream();
            byte[] buf = new byte[1024];
            while (inputStream.read(buf) != -1) {
                ;
            }
            inputStream.close();
            lastException = null;
        } catch (Exception exception) {
            lastException = exception;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception exception) {
                    ;
                }
            }
        }
    }

}
