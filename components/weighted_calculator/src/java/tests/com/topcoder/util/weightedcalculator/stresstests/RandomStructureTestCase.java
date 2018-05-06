/**
 * RandomStructureTestCase.java
 *
 * Copyright © 2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.weightedcalculator.stresstests;

import com.topcoder.util.weightedcalculator.*;
import java.util.*;
import junit.framework.*;

/**
 * Stress test checks the behavior on large matrices with random structure
 * (5184 subsections imbricated randomly, 25910 items)
 *
 * @author adic
 * @version 1.0
 */
public class RandomStructureTestCase extends TestCase {

    private static final int TIMEOUT=3000; // timeout in ms for the test
    private static final double EPS=1e-3; // error tolerance
    private static final long SEED=1234; // fixed random seed ensures deterministic behavior

    /**
     * Stress test checks the behavior on large matrices with random structure
     * (5184 subsections imbricated randomly, 25910 items)
     */
    public void testRandomStructure() {
        long tic=System.currentTimeMillis();
        Random rand=new Random(SEED);
        MathMatrix matrix=new MathMatrix("test matrix",1000);
        assertNotNull("MathMatrix object should not be null",matrix);

        // create recursively a random matrix
        double myScore=rec(matrix,0,rand);

        // check corectness
        assertTrue("Incorrect weighted score",Math.abs(myScore-matrix.getWeightedScore())<EPS);

        // timeout ?
        long tac=System.currentTimeMillis();
        assertTrue("Time limit exceeded",tac-tic<TIMEOUT);
        System.out.println("OK "+(tac-tic)+"ms ");
    }

    /**
     * fill recursively container with random items and sections
     * (2 to 4 subsections per section, 2 to 6 items per section, imbrication level 4 to 9)
     */
    private double rec(Object container,int level,Random rand) {
        boolean leaf=level>4+rand.nextInt(5); // limit imbrication levels 4..8
        int childCount=4+rand.nextInt(5); // child count 4..8

        // generate weigths
        double weights[]=new double[childCount];
        double weightSum=0,scoreSum=0;
        for (int i=0;i<childCount;i++) {
            weights[i]=rand.nextDouble();
            weightSum+=weights[i];
        }

        // selected 2..4 random children to be subsections
        int sectionCount=leaf?0:2+rand.nextInt(3);
        boolean isSection[]=new boolean[childCount];
        for (int i=0;i<sectionCount;i++) isSection[i]=true;
        for (int i=0;i<childCount;i++) {
            int j=rand.nextInt(childCount);
            boolean sw=isSection[i];
            isSection[i]=isSection[j];
            isSection[j]=sw;
        }

        // generate children
        for (int i=0;i<childCount;i++) {
            double weight=weights[i]/weightSum; // normalized weight
            if (!isSection[i]) {
                // create line item
                double score=rand.nextDouble()*1000;
                LineItem item=new LineItem("line",weight,1000,score);
                assertNotNull("LineItem object should not be null",item);
                if (container instanceof Section) ((Section)container).addItem(item);
                else ((MathMatrix)container).addItem(item);
                scoreSum+=score*weight; // make my own computations for corectness testing
            }
            else {
                // create section and fill it recursively with children
                Section section=new Section("section",weight);
                assertNotNull("Section object should not be null",section);
                if (container instanceof Section) ((Section)container).addItem(section);
                else ((MathMatrix)container).addItem(section);
                scoreSum+=weight*rec(section,level+1,rand); // make my own computations for corectness testing
            }
        }
        return scoreSum;
    }

    public static Test suite() {
        return new TestSuite(RandomStructureTestCase.class);
    }

    public static void main(String params[]) {
        new RandomStructureTestCase().testRandomStructure();
    }
}

