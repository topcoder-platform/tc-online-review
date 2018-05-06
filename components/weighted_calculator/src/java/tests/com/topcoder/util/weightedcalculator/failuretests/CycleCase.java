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
public class CycleCase extends TestCase {

    public CycleCase(String testName) {
        super(testName);
    }

	private static int inTest = 1;
    public void testA()
    	throws Exception
    {
		Runnable r = new Runnable() {
			public void run() {
				try {
					Section orig = new Section("failure",.5);
					Section prev = orig;
					for (int i=0; i<1000; i++) {
						Section s = new Section("failure"+i,.5);
						prev.addItem(s);
						prev = s;
					}
					prev.addItem(orig);
//					LineItem l = new LineItem("loop", .5, 555, 222);
//					s.addItem(l);
					orig.getWeightedScore();
					inTest = -1;
				} catch (IllegalArgumentException e) { inTest = 2; } catch (StackOverflowError soe) { inTest = 3; } catch (Throwable t) {
					// inTest = 0; t.printStackTrace();
				} finally {
				}
			}
		};
		Thread t = new Thread(r);
		inTest = 1; t.start();
		Thread.sleep(1500);
        if (t.isAlive()) t.stop();
        handle();
    }

    public void testB()
    	throws Exception
    {
		Runnable r = new Runnable() {
			public void run() {
				try {
					Section orig = new Section("failure",.5);
					Section prev = orig;
					for (int i=0; i<1000; i++) {
						Section s = new Section("failure"+i,.5);
						prev.addItem(s);
						prev = s;
					}
					prev.addItem(orig);
					LineItem l = new LineItem("loop", .5, 555, 222);
					orig.addItem(l);
					l.getWeightedScore();
					inTest = -1;
				} catch (IllegalArgumentException e) { inTest = 2; } catch (StackOverflowError soe) { inTest = 3; } catch (Throwable t) {
					// inTest = 0; t.printStackTrace();
				} finally {
				}
			}
		};
		Thread t = new Thread(r);
		inTest = 1; t.start();
		Thread.sleep(1500);
        if (t.isAlive()) t.stop();
        handle();
    }

    void handle() {
        if (inTest == 1) fail("There is an infinite loop in the component");
        if (inTest == -1) fail("Probably an IllegalArgumentException should be thrown");
        if (inTest == 3) fail("Stack Overflow Error");
        if (inTest == 0) fail("Unexpected Throwable Caught");
	}

    public static Test suite() {
        return new junit.framework.TestSuite(CycleCase.class);
    }
}
