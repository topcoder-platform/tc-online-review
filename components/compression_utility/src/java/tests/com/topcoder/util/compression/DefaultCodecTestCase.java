/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.compression;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * <p>Tests functionality of the <code>DefaultCodec</code> implementation.</p>
 *
 * @author  srowen
 * @version 2.0
 *
 * @since 1.0
 */
public class DefaultCodecTestCase extends AbstractCodecTestCase {

    /**
     * @return name of <code>Codec</code> implementation class
     */
    String getCodecClassName() {
        return "com.topcoder.util.compression.DefaultCodec";
    }

    /**
     * <p>
     * Return a new <code>TestSuite</code> containing all test cases in this class.
     * </p>
     *
     * @return a new <code>TestSuite</code> containing all test cases in this class
     */
    public static Test suite() {
        return new TestSuite(DefaultCodecTestCase.class);
    }
}








