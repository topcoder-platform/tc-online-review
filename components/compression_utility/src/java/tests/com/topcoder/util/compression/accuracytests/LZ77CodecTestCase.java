/**
 * LZ77CodecTestCase.java
 * Tests the LV77Codec with the tests contained in
 * AbstractCodecTestCase.
 * @author TCSREVIEWER
 * @version 1.0
 */
package com.topcoder.util.compression.accuracytests;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.topcoder.util.compression.Codec;

import com.topcoder.util.compression.LZ77Codec;

public class LZ77CodecTestCase extends AbstractCodecTestCase {

    private static final Codec CODEC = new LZ77Codec();

    /**
     * Method getCodecName
     *
     * @return   a String
     *
     */
    public String getCodecName() {
        return "com.topcoder.util.compression.LZ77Codec";
    }

    public static Test suite() {
        return new TestSuite(LZ77CodecTestCase.class);
    }
}









