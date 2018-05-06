/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.compression;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * <p>Tests functionality of the <code>DeflateCodec</code> implementation.</p>
 *
 * @author  srowen, visualage
 * @version 2.0
 */
public class DeflateCodecTestCase extends AbstractCodecTestCase {

    /**
     * @return name of <code>Codec</code> implementation class
     */
    String getCodecClassName() {
        return "com.topcoder.util.compression.DeflateCodec";
    }

    /**
     * <p>
     * Return a new <code>TestSuite</code> containing all test cases in this class.
     * </p>
     *
     * @return a new <code>TestSuite</code> containing all test cases in this class
     */
    public static Test suite() {
        return new TestSuite(DeflateCodecTestCase.class);
    }
}








