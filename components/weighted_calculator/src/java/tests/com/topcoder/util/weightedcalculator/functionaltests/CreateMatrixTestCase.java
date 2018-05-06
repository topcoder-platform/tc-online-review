/*
 * @(#) CreateMatrixTestCase.java
 * 
 * 1.0  01/03/2003
 */

package com.topcoder.util.weightedcalculator.functionaltests;

import junit.framework.*;
import com.topcoder.util.weightedcalculator.*;

/**
 * Tests constructors for correct instantiation of <code>MathMatrix</code>
 *
 * @author isv
 * @version 1.0
 */
public class CreateMatrixTestCase extends TestCase {

    public CreateMatrixTestCase(String testName) {
        super(testName);
    }

    public void test() {
        MathMatrix matrix;

        matrix = new MathMatrix("Example matrix", 100);
        assertNotNull(matrix);

        try {
            matrix = new MathMatrix("Example matrix", 0);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            matrix = new MathMatrix("Example matrix", -1);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            matrix = new MathMatrix("", 100);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            matrix = new MathMatrix((String) null, 100);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}
    
        try {
            matrix = new MathMatrix("", 0);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            matrix = new MathMatrix((String) null, 0);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            matrix = new MathMatrix("", -1);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            matrix = new MathMatrix((String) null, -1);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}
    }

    public static Test suite() {
        return new junit.framework.TestSuite(CreateMatrixTestCase.class);
    }
}
