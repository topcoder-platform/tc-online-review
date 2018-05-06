/**
 * Copyright © 2004, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.generator.guid;

import java.util.Random;
import java.util.Arrays;


/**
 * This class is only for testing purpouses.
 * It extends the Random class to provide non-random values: it always return 0 for the implemented methods.
 * (Other methods are not implemented because they're not needed for testing the generators).
 * Having a false random generator is usefull to know if the random generator passed as a parameter is really being
 * used, as well as being able to check for more consistency in the results due to the knowledge of the returned
 * "random" values.
 * 
 * @author TCSDEVELOPER
 * @author TCSDESIGNER
 * @version 1.0
 */
public class ZeroRandom extends Random {

    /**
     * Fill the bytes with 0's.
     *  
     * @param bytes the array to be filled with 0's
     */
    public void nextBytes(byte[] bytes) {
        Arrays.fill(bytes, (byte) 0);
    }
    
    /**
     * return 0.
     * @return an integer value of 0
     */
    public int nextInt() {
        return 0;
    }

    /**
     * return 0.
     * 
     * @param n not used
     * @return an integer value of 0
     */
    public int nextInt(int n) {
        return 0;
    }

    /**
     * return 0.
     * @return a long value of 0
     */
    public long nextLong() {
        return 0;
    }

}
