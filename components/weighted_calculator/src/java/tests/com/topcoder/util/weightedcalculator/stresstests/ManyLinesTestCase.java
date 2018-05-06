/**
 * ManyLinesTestCase.java
 *
 * Copyright © 2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.weightedcalculator.stresstests;

import com.topcoder.util.weightedcalculator.*;
import java.util.*;
import junit.framework.*;

/**
 * Stress test checks the behavior on matrices with many lines.
 *
 * @author adic
 * @version 1.0
 */
public class ManyLinesTestCase extends TestCase {

    private static final int MAX=10000; // line count
    private static final int TIMEOUT=3000; // timeout in ms for the test
    private static final double EPS=1e-3; // error tolerance
    private static final long SEED=1234; // fixed random seed ensures deterministic behavior

    /**
     * Stress test checks the behavior on matrices with many lines.
     */
    public void testManyLines() {
        long tic=System.currentTimeMillis();
        Random rand=new Random(SEED);
        MathMatrix matrix=new MathMatrix("test matrix",1000);
        assertNotNull("MathMatrix object should not be null",matrix);

        // generate weights
        double scoreSum=0,weightSum=0;
        double weights[]=new double[MAX];
        for (int i=0;i<MAX;i++) {
            weights[i]=rand.nextDouble();
            weightSum+=weights[i];
        }

        // create items
        for (int i=0;i<MAX;i++) {
            double weight=weights[i]/weightSum; // normalized weight
            double score=rand.nextDouble()*1000;
            LineItem item=new LineItem("line",weight,1000,score);
            assertNotNull("LineItem object should not be null",item);
            matrix.addItem(item);
            scoreSum+=item.getWeightedScore();
            assertTrue("Incorrect weighted score",Math.abs(weight*score-item.getWeightedScore())<EPS);
            assertTrue("Time limit exceeded",System.currentTimeMillis()-tic<TIMEOUT);
        }

        // corectness test
        double score=matrix.getWeightedScore();
        assertTrue("Incorrect weighted score",Math.abs(score-scoreSum)<EPS);

        // timeout ?
        long tac=System.currentTimeMillis();
        assertTrue("Time limit exceeded",tac-tic<TIMEOUT);
        System.out.println("OK "+(tac-tic)+"ms");
    }

    public static Test suite() {
        return new TestSuite(ManyLinesTestCase.class);
    }

    public static void main(String params[]) {
        new ManyLinesTestCase().testManyLines();
    }
}

