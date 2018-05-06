/*
 * @(#) CycleCase.java
 *
 * 1.0  01/03/2003
 */

package com.topcoder.util.weightedcalculator.failuretests;

import junit.framework.*;
import com.topcoder.util.weightedcalculator.*;

/**
* Builds a more elaborate cycle.
*
* Note that the threading is just so ant will finish.  I am not
* testing synchronization issues at all.
 *
 * @author b0b0b0b
 * @version 1.0
 */
public class ClobberCase extends TestCase {

    public ClobberCase(String testName) {
        super(testName);
    }

    public void testA()
    	throws Exception
    {
		try {
			MathMatrix mm = new MathMatrix("howdy",Double.MAX_VALUE);
			mm.addItem(new LineItem("li",1,1,1));
			mm.getItems().add("unce tice fee times a mady");
			mm.getWeightedScore();
		} catch (ClassCastException cce) {
			fail("you might not want to let me modify the items list for a container (mathmatrix)");
		}
    }

    public void testB()
    	throws Exception
    {
		try {
			Section orig = new Section("failure",.5);
			orig.addItem(new LineItem("li",1,1,1));
			orig.getItems().add("unce tice fee times a mady");
			orig.getWeightedScore();
		} catch (ClassCastException cce) {
			fail("you might not want to let me modify the items list for a container (section)");
		}
    }


    public static Test suite() {
        return new junit.framework.TestSuite(ClobberCase.class);
    }
}
