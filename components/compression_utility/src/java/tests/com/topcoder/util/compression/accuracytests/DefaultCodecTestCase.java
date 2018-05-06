/**
 * DefaultCodecTestCase.java
 * Tests the DefaultCodec with the tests contained in
 * AbstractCodecTestCase.
 * @author TCSREVIEWER
 * @version 1.0
 */
package com.topcoder.util.compression.accuracytests;

import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

public class DefaultCodecTestCase extends AbstractCodecTestCase {

    /**
     * Method getCodecName
     *
     * @return   a String
     *
     */
    public String getCodecName() {
        return "com.topcoder.util.compression.DefaultCodec";
    }

    public static Test suite() {
        return new TestSuite(DefaultCodecTestCase.class);
    }
}









