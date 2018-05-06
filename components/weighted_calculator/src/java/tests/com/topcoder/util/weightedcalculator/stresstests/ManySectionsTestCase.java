/**
 * ManySectionsTestCase.java
 *
 * Copyright © 2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.weightedcalculator.stresstests;

import com.topcoder.util.weightedcalculator.*;
import java.util.*;
import junit.framework.*;

/**
 * Stress test checks the behavior on matrices with many sections, each with two items.
 *
 * @author adic
 * @version 1.0
 */
public class ManySectionsTestCase extends TestCase {

    private static final int MAX=10000; // section count
    private static final int TIMEOUT=3000; // timeout in ms for the test
    private static final double EPS=1e-3; // error tolerance
    private static final long SEED=1234; // fixed random seed ensures deterministic behavior

    /**
     * Stress test checks the behavior on matrices with many sections, each with two items.
     */
    public void testManySections() {
        long tic=System.currentTimeMillis();
        Random rand=new Random(SEED);
        MathMatrix matrix=new MathMatrix("test matrix",1000);
        assertNotNull("MathMatrix object should not be null",matrix);

        // generate weights
        double scoreSum=0,weightSum=0;
        double weights[]=new double[MAX/2];
        for (int i=0;i<MAX/2;i++) {
            weights[i]=rand.nextDouble();
            weightSum+=weights[i];
        }

        // create sections
        for (int i=0;i<MAX/2;i++) {
            double score=rand.nextDouble()*1000;
            double weight=weights[i]/weightSum; // normalized weight
            Section section=new Section("section",weight);
            assertNotNull("Section object should not be null",section);
            double iweight=rand.nextDouble();
            double score1=rand.nextDouble()*1000;
            double score2=rand.nextDouble()*1000;
            Item item1=new LineItem("line1",iweight,1000,score1);
            assertNotNull("LineItem object should not be null",item1);
            Item item2=new LineItem("line2",1-iweight,1000,score2);
            assertNotNull("LineItem object should not be null",item2);
            section.addItem(item1);
            section.addItem(item2);
            matrix.addItem(section);
            scoreSum+=section.getWeightedScore();
            assertTrue("Incorrect weighted score",
                Math.abs(weight*(iweight*score1+(1-iweight)*score2)-section.getWeightedScore())<EPS);
            assertTrue("Time limit exceeded",System.currentTimeMillis()-tic<TIMEOUT);
        }

        // check corectness
        double score=matrix.getWeightedScore();
        assertTrue("Incorrect weighted score",Math.abs(score-scoreSum)<EPS);

        // timeout ?
        long tac=System.currentTimeMillis();
        assertTrue("Time limit exceeded",tac-tic<TIMEOUT);
        System.out.println("OK "+(tac-tic)+"ms");
    }

    public static Test suite() {
        return new TestSuite(ManySectionsTestCase.class);
    }

    public static void main(String params[]) {
        new ManySectionsTestCase().testManySections();
    }
}

