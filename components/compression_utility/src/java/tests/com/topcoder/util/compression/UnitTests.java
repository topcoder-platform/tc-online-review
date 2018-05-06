package com.topcoder.util.compression;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>This test case aggregates all unit test cases.</p>
 *
 * @author TopCoder Software
 * @version 1.0
 */
public class UnitTests extends TestCase {

    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTest(CompressionUtilityTestCase.suite());
        suite.addTest(LZ77CodecTestCase.suite());
        suite.addTest(DefaultCodecTestCase.suite());
        suite.addTest(DeflateCodecTestCase.suite());
        suite.addTest(ByteArrayWithLookbackTestCase.suite());
        return suite;
    }

}








