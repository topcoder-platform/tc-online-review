package com.topcoder.util.compression.functionaltests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.*;
import com.topcoder.util.compression.*;

/**
 * <p>This test case aggregates all functional test cases.</p>
 *
 * @author TopCoder Software
 * @version 1.0
 */
public class FunctionalTests extends TestCase {

    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTestSuite(CompressionTests.class);
        return suite;
    }


}








