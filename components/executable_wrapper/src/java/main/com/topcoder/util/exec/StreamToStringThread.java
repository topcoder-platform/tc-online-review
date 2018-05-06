/*
 * StreamToStringThread.java
 *
 * Copyright (C) 2002, TopCoder, Inc. All rights reserved
 *
 */

package com.topcoder.util.exec;

import java.io.InputStream;
import java.io.IOException;

/**
 * <p>This thread consumes the output from an <code>InputStream</code> and 
 * stores the result as a String, for later retrieval.</p>
 *
 * @author srowen
 * @author garyk
 * @version 1.0     
 */
class StreamToStringThread extends Thread {

    private StringBuffer output;   // the output string from the input stream
    private InputStream is;  // the input stream to be consumed
    /* a flag to decide whether to continue to read or not */
    private boolean shouldContinueReading;


    StreamToStringThread(final InputStream is) {
        this.is = is;
        output = new StringBuffer();
        shouldContinueReading = true;
    }

    /**
     * Reads the input from the given InputStream and stores the results
     * as a String.
     */
    public void run() {
        int intCh;

        /* try to read data from the input stream */
        try {
            /* continue to read if the flag is true */
            while (shouldContinueReading) {
                /* read in the next byte */
                intCh = is.read();    
     
                /* check if the end of the input stream has reached or not */
                if (intCh == -1) {
                    shouldContinueReading = false;
                    continue;
                } 

                /* append the current byte into the output string */
                output.append((char)intCh);
            } 
        } catch (IOException ie) {
            /* 
             * if the flag has set to be false, then it means this thread was
             * asked to stop reading, and the input stream is closed, so 
             * ignore the IOException in this case.
             */
            if (shouldContinueReading) {
                ie.printStackTrace();
            }
        }
    }

    /**
     * Gets the ouput string of the input stream
     *
     * @return the collected input, as a String
     */
    String getOutput() {
        return output.toString();
    }

    /**
     * Asks the reading thread to stop, and set the input stream to be null
     */
    void stopReading() {
        shouldContinueReading = false;
        is = null;
    }
}