/*
 * @(#) StrangeValuesCase.java
 *
 * 1.0  01/03/2003
 */

package com.topcoder.util.weightedcalculator.failuretests;

import junit.framework.*;
import com.topcoder.util.weightedcalculator.*;

/**
* Why double and not BigDecimal?
 *
 * @author b0b0b0b
 * @version 1.0
 */
public class StrangeValuesCase extends TestCase {
    public StrangeValuesCase(String testName) {
        super(testName);
    }

	void jobu(String s) {
		failed = true;
		failmessage.append(s);
		failmessage.append("\n");
	}

	StringBuffer failmessage = new StringBuffer();
	boolean failed = false;

	/**
	*	Various inputs that are easy to handle
	*
	*/
    public void testA()
    	throws Exception
    {
		double[] dar = new double[]{Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NaN,Double.MIN_VALUE, Double.MAX_VALUE, 5.0};
		for (int a=0; a<dar.length; a++) {
			for (int b=0; b<dar.length; b++) {
				for (int c=0; c<dar.length; c++) {
					for (int d=0; d<dar.length; d++) {
						try {
							MathMatrix matrix = new MathMatrix("matrix with 1 item", dar[a]);
							matrix.addItem(new LineItem("Item", dar[d], dar[b], dar[c]));
//							System.out.println(dar[a]+" "+dar[b]+" "+dar[c]);
//							System.out.println(matrix.getWeightedScore());
//							System.out.println(matrix.getActualScore());

							if (dar[a] < 0) jobu("When the matrix max score is less than 0, we expect an IllegalArgumentException: "+dar[a]);
							if (dar[b] < 0) jobu("When the line Item max score is less than 0, we expect an IllegalArgumentException: "+dar[b]);
							if ((dar[c] < 0 || dar[c] > dar[b])) jobu("When the line Item actual score is not between 0 and the max score, we expect an IllegalArgumentException: "+dar[c]+" "+dar[b]);
							if (dar[a] == Double.NaN) jobu("Why would you want NaN as your matrix max score?");
							if (dar[b] == Double.NaN) jobu("Why would you want NaN as your item max score?");
							if (dar[c] == Double.NaN) jobu("Why would you want NaN as your item actual score?");
							if (dar[d] == Double.NaN) jobu("Why would you want NaN as your item weight?");

						} catch (IllegalArgumentException e) {
						}
					}
				}
			}
		}
		if (failed) fail(failmessage.toString());
    }

	/**
	*  Here we make the math matrix return a max score + EPSILON
	*	java math can be tricky
	*/
    public void testB()
    	throws Exception
    {
		try {
			double MAX = 1.0;
			MathMatrix matrix = new MathMatrix("matrix", MAX);
			matrix.addItem(new LineItem("Item1", 1.0, Double.MAX_VALUE, Double.MAX_VALUE));
			matrix.addItem(new LineItem("Item2", MathMatrix.EPSILON - Double.MIN_VALUE, Double.MAX_VALUE, Double.MAX_VALUE));
			double val = matrix.getWeightedScore();
			assertTrue("Weighted Score ("+val+") = MathMatrix.EPSILON + Max ("+MAX+").  Expected IllegalStateException",val <= MAX);
		} catch (IllegalArgumentException e) {
			// ok with me
		} catch (IllegalStateException e2) {
			// ok with me
		}
	}

	/**
	*  Here we make the math matrix return a value much much greater than the max score
	*
	*/
    public void testC()
    	throws Exception
    {
		try {
			Section orig = new Section("failure",1);
			Section prev = orig;
			for (int i=0; i<500; i++) {
				Section s = new Section("failure"+i,1);
				Section s2 = new Section("failure"+i,MathMatrix.EPSILON / 2);
				prev.addItem(s);
				prev.addItem(s2);
				s2.addItem(new LineItem("howdy", 1, 5, 5));
				prev = s;
			}
			prev.addItem(new LineItem("loop", 1, 5, 5));
			assertTrue("Weighted Score ("+orig.getWeightedScore()+") > Max Score ("+orig.getMaximumScore()+").",orig.getWeightedScore() <= orig.getMaximumScore());
		} catch (IllegalArgumentException e) {
			// ok with me
		} catch (IllegalStateException e2) {
			// ok with me
		}
	}

	/**
	*  let's make it stack overflow
	*
	*/
    public void testD()
    	throws Exception
    {
		try {
			Section orig = new Section("failure",1);
			Section prev = orig;
			for (int i=0; i<2000; i++) {
				Section s = new Section("failure"+i,1);
				Section s2 = new Section("failure"+i,MathMatrix.EPSILON / 2);
				prev.addItem(s);
				prev.addItem(s2);
				s2.addItem(new LineItem("howdy", 1, 5, 5));
				prev = s;
			}
			prev.addItem(new LineItem("loop", 1, 5, 5));
			assertTrue("Weighted Score ("+orig.getWeightedScore()+") > Max Score ("+orig.getMaximumScore()+").",orig.getWeightedScore() <= orig.getMaximumScore());
		} catch (StackOverflowError e) {
			System.err.println("Just checking for stack overflow.  Not a failure.  You stack overflowed in StrangeValuesCase.testD");
			// ok with me
		} catch (IllegalArgumentException e) {
			// ok with me
		} catch (IllegalStateException e2) {
			// ok with me
		}
	}

    public static Test suite() {
        return new junit.framework.TestSuite(StrangeValuesCase.class);
    }
}

