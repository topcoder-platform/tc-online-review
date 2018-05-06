/**
 * Copyright &copy; 2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.classassociations.stresstests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import com.topcoder.util.classassociations.*;

/**
 * <p>This test case tests the retrieveHandler() method.</p>
 *
 * @author garyk
 * @version 1.0
 */
public class RetrieveHandlerStressTests extends TestCase {
	private static final int N = 20;
	private static final long TIME = 3000;
    private static Object[] objs;
    private ClassAssociator ca1;
    private ClassAssociator ca2;
    private ClassAssociator ca3;
    private Object target1;
    private Object target2;
    private Object target3;
    private Object handler1;
    private Object handler2;
    private Object handler3;

	/**
	 * @return a test suite that contains all test methods in the class
	 */
	public static TestSuite suite() {
		return new TestSuite(RetrieveHandlerStressTests.class);
	}

    /*
     * Initialize the object array
     */
    static {
        objs = new Object[N * 3];

        objs[0] = new Object() {};
        objs[1] = new Object() {};
        objs[2] = new Object() {};
        objs[3] = new Object() {};
        objs[4] = new Object() {};
        objs[5] = new Object() {};
        objs[6] = new Object() {};
        objs[7] = new Object() {};
        objs[8] = new Object() {};
        objs[9] = new Object() {};
        objs[10] = new Object() {};
        objs[11] = new Object() {};
        objs[12] = new Object() {};
        objs[13] = new Object() {};
        objs[14] = new Object() {};
        objs[15] = new Object() {};
        objs[16] = new Object() {};
        objs[17] = new Object() {};
        objs[18] = new Object() {};
        objs[19] = new Object() {};
        objs[20] = new Object() {};
        objs[21] = new Object() {};
        objs[22] = new Object() {};
        objs[23] = new Object() {};
        objs[24] = new Object() {};
        objs[25] = new Object() {};
        objs[26] = new Object() {};
        objs[27] = new Object() {};
        objs[28] = new Object() {};
        objs[29] = new Object() {};        
        objs[30] = new Object() {};
        objs[31] = new Object() {};
        objs[32] = new Object() {};
        objs[33] = new Object() {};
        objs[34] = new Object() {};
        objs[35] = new Object() {};
        objs[36] = new Object() {};
        objs[37] = new Object() {};
        objs[38] = new Object() {};
        objs[39] = new Object() {};
        objs[40] = new Object() {};
        objs[41] = new Object() {};
        objs[42] = new Object() {};
        objs[43] = new Object() {};
        objs[44] = new Object() {};
        objs[45] = new Object() {};
        objs[46] = new Object() {};
        objs[47] = new Object() {};
        objs[48] = new Object() {};
        objs[49] = new Object() {};
        objs[50] = new Object() {};
        objs[51] = new Object() {};
        objs[52] = new Object() {};
        objs[53] = new Object() {};
        objs[54] = new Object() {};
        objs[55] = new Object() {};
        objs[56] = new Object() {};
        objs[57] = new Object() {};
        objs[58] = new Object() {};
        objs[59] = new Object() {};    
    }

	static long elapsed(long time) {
		return System.currentTimeMillis() - time;
	}

    public void setUp() {
        ca1 = new ClassAssociator();
        ca2 = new ClassAssociator();
        ca3 = new ClassAssociator();
        
        target1 = handler1 = objs[N - 1];
        target2 = handler2 = objs[2 * N - 1];
        target3 = handler3 = objs[3 * N - 1];
    }

    /*
     * Test retrieveHandler() with classes setup with addAssociation() only
     */
    public void testRetrieveHandler1() throws Exception {
        Object handler;
        long time;
        long count1;
        long count2;
        long count3;

        // set up associations
        for (int i = 0; i < N; i++) {
            ca1.addAssociation(objs[i], objs[i]);

            ca2.addAssociation(objs[i], objs[i]);
            ca2.addAssociation(objs[N + i], objs[N + i]);

            ca3.addAssociation(objs[i], objs[i]);
            ca3.addAssociation(objs[N + i], objs[N + i]);
            ca3.addAssociation(objs[2 * N + i], objs[2 * N + i]);
        }

        // check the handler from ca1
        time = elapsed(0);
        handler = ca1.retrieveHandler(target1);
        time = elapsed(time);

        assertTrue("Should return the correct handler", 
            handler1.getClass().isInstance(handler));


        // check the handler from ca2
        time = elapsed(0);
        handler = ca2.retrieveHandler(target2);
        time = elapsed(time);

        assertTrue("Should return the correct handler", 
            handler2.getClass().isInstance(handler));


        // check the handler from ca3
        time = elapsed(0);
        handler = ca3.retrieveHandler(target3);
        time = elapsed(time);

        assertTrue("Should return the correct handler", 
            handler3.getClass().isInstance(handler));


        // count the number of executions in TIME ms
        count1 = getRunningCount(ca1, target1, TIME);
        System.out.println("OK " + count1 + " number of executions");

        count2 = getRunningCount(ca2, target2, TIME);
        System.out.println("OK " + count2 + " number of executions");

        count3 = getRunningCount(ca3, target3, TIME);
        System.out.println("OK " + count3 + " number of executions");

        assertTrue("The number of " + N + " associations' executions should be "
            + "close to that of " + (N * 2) + " associations' when using "
            + "addAssociation() only", 
            (((double)count1 / (double)count2) < 1.2) 
                && (((double)count2 / (double)count1) < 1.2));

        assertTrue("The number of " + N + " associations' executions should be "
            + "close to that of " + (N * 3) + " associations' when using "
            + "addAssociation() only", 
            (((double)count1 / (double)count3) < 1.2) 
                && (((double)count3 / (double)count1) < 1.2));
    }

    /*
     * Test retrieveHandler() with classes setup with addGroupAssociation() only
     */
    public void testRetrieveHandler2() throws Exception {
        Object handler;
        long time;
        long count1;
        long count2;
        long count3;

        // set up associations
        for (int i = 0; i < N; i++) {
            ca1.addGroupAssociation(objs[i], objs[i]);

            ca2.addGroupAssociation(objs[i], objs[i]);
            ca2.addGroupAssociation(objs[N + i], objs[N + i]);

            ca3.addGroupAssociation(objs[i], objs[i]);
            ca3.addGroupAssociation(objs[N + i], objs[N + i]);
            ca3.addGroupAssociation(objs[2 * N + i], objs[2 * N + i]);
        }

        // check the handler from ca1
        time = elapsed(0);
        handler = ca1.retrieveHandler(target1);
        time = elapsed(time);

        assertTrue("Should return the correct handler", 
            handler1.getClass().isInstance(handler));


        // check the handler from ca2
        time = elapsed(0);
        handler = ca2.retrieveHandler(target2);
        time = elapsed(time);

        assertTrue("Should return the correct handler", 
            handler2.getClass().isInstance(handler));


        // check the handler from ca3
        time = elapsed(0);
        handler = ca3.retrieveHandler(target3);
        time = elapsed(time);

        assertTrue("Should return the correct handler", 
            handler3.getClass().isInstance(handler));


        // count the number of executions in TIME ms
        count1 = getRunningCount(ca1, target1, TIME);
        System.out.println("OK " + count1 + " number of executions");

        count2 = getRunningCount(ca2, target2, TIME);
        System.out.println("OK " + count2 + " number of executions");

        count3 = getRunningCount(ca3, target3, TIME);
        System.out.println("OK " + count3 + " number of executions");

        assertTrue(N + " associations must be at least 1.5X more executions "
            + " than " + (N * 2) + " associations, but actually "
            + ((double)count1 / (double)count2) + "X", 
            ((double)count1 / (double)count2) > 1.5);

        assertTrue(N + " associations must be at least 2.5X more executions "
            + " than " + (N * 3) + " associations, but actually "
            + ((double)count1 / (double)count3) + "X", 
            ((double)count1 / (double)count3) > 2.5);
    }

    /*
     * Test retrieveHandler() with classes setup with addGroupAssociation() only
     * and 3 instances of the ClassAssociator (one with each thread) running
     * at the same time
     */
    public void testRetrieveHandler3() throws Exception {
        Object handler;
        WorkingThread thread1;
        WorkingThread thread2;
        WorkingThread thread3;
        long time;
        long count1;
        long count2;
        long count3;

        // set up associations
        for (int i = 0; i < 3 * N; i++) {
            ca1.addGroupAssociation(objs[i], objs[i]);
            ca2.addGroupAssociation(objs[i], objs[i]);
            ca3.addGroupAssociation(objs[i], objs[i]);
        }

        // check the handler from ca1
        time = elapsed(0);
        handler = ca1.retrieveHandler(target3);
        time = elapsed(time);

        assertTrue("Should return the correct handler", 
            handler3.getClass().isInstance(handler));


        // check the handler from ca2
        time = elapsed(0);
        handler = ca2.retrieveHandler(target3);
        time = elapsed(time);

        assertTrue("Should return the correct handler", 
            handler3.getClass().isInstance(handler));


        // check the handler from ca3
        time = elapsed(0);
        handler = ca3.retrieveHandler(target3);
        time = elapsed(time);

        assertTrue("Should return the correct handler", 
            handler3.getClass().isInstance(handler));


        thread1 = new WorkingThread(ca1, target3);
        thread2 = new WorkingThread(ca2, target3);
        thread3 = new WorkingThread(ca3, target3);

        thread1.start();
        thread2.start();
        thread3.start();

        Thread.sleep(TIME);

        thread1.setIsRunning(false);
        thread2.setIsRunning(false);
        thread3.setIsRunning(false);

        assertNull("Should be no exception when retrieving the handler",
            thread1.getThrowable());
        assertNull("Should be no exception when retrieving the handler",
            thread2.getThrowable());
        assertNull("Should be no exception when retrieving the handler",
            thread3.getThrowable());

        count1 = thread1.getCount();
        System.out.println("OK " + count1 + " number of executions");

        count2 = thread2.getCount();
        System.out.println("OK " + count2 + " number of executions");

        count3 = thread3.getCount();
        System.out.println("OK " + count3 + " number of executions");

        assertTrue("The number of thread1's executions should be "
            + "close to that of thread2's", 
            (((double)count1 / (double)count2) < 1.5) 
                && (((double)count2 / (double)count1) < 1.5));

        assertTrue("The number of thread1's executions should be "
            + "close to that of thread3's", 
            (((double)count1 / (double)count3) < 1.5) 
                && (((double)count3 / (double)count1) < 1.5));
    }

    /*
     * Test retrieveHandler() with classes setup with addAssociation() only
     * and 3 instances of the ClassAssociator (one with each thread) running
     * at the same time
     */
    public void testRetrieveHandler4() throws Exception {
        Object handler;
        WorkingThread thread1;
        WorkingThread thread2;
        WorkingThread thread3;
        long time;
        long count1;
        long count2;
        long count3;

        // set up associations
        for (int i = 0; i < 3 * N; i++) {
            ca1.addAssociation(objs[i], objs[i]);
            ca2.addAssociation(objs[i], objs[i]);
            ca3.addAssociation(objs[i], objs[i]);
        }

        // check the handler from ca1
        time = elapsed(0);
        handler = ca1.retrieveHandler(target3);
        time = elapsed(time);

        assertTrue("Should return the correct handler", 
            handler3.getClass().isInstance(handler));


        // check the handler from ca2
        time = elapsed(0);
        handler = ca2.retrieveHandler(target3);
        time = elapsed(time);

        assertTrue("Should return the correct handler", 
            handler3.getClass().isInstance(handler));


        // check the handler from ca3
        time = elapsed(0);
        handler = ca3.retrieveHandler(target3);
        time = elapsed(time);

        assertTrue("Should return the correct handler", 
            handler3.getClass().isInstance(handler));


        thread1 = new WorkingThread(ca1, target3);
        thread2 = new WorkingThread(ca2, target3);
        thread3 = new WorkingThread(ca3, target3);

        thread1.start();
        thread2.start();
        thread3.start();

        Thread.sleep(TIME);

        thread1.setIsRunning(false);
        thread2.setIsRunning(false);
        thread3.setIsRunning(false);

        assertNull("Should be no exception when retrieving the handler",
            thread1.getThrowable());
        assertNull("Should be no exception when retrieving the handler",
            thread2.getThrowable());
        assertNull("Should be no exception when retrieving the handler",
            thread3.getThrowable());

        count1 = thread1.getCount();
        System.out.println("OK " + count1 + " number of executions");

        count2 = thread2.getCount();
        System.out.println("OK " + count2 + " number of executions");

        count3 = thread3.getCount();
        System.out.println("OK " + count3 + " number of executions");

        assertTrue("The number of thread1's executions should be "
            + "close to that of thread2's", 
            (((double)count1 / (double)count2) < 1.5) 
                && (((double)count2 / (double)count1) < 1.5));

        assertTrue("The number of thread1's executions should be "
            + "close to that of thread3's", 
            (((double)count1 / (double)count3) < 1.5) 
                && (((double)count3 / (double)count1) < 1.5));
    }

    private long getRunningCount(ClassAssociator ca, Object target, long time) 
            throws Exception {
        WorkingThread thread = new WorkingThread(ca, target);

        thread.start();

        Thread.sleep(time);

        thread.setIsRunning(false);

        assertTrue("Should be no exception when retrieving the handler",
            thread.getThrowable() == null);

        return thread.getCount();    
    }

    private class WorkingThread extends Thread {
        ClassAssociator ca;
        Object target;
        boolean isRunning;
        long count;
        private Throwable throwable;

        public WorkingThread(ClassAssociator ca, Object target) {
            this.ca = ca;
            this.target = target;
            this.count = 0;
            this.isRunning = true;
            throwable = null;
        }

        public void setIsRunning(boolean isRunning) {
            this.isRunning = isRunning;
        }

        public long getCount() {
            return count;
        }

        public Throwable getThrowable() {
            return throwable;
        }

        public void run() {
            try {
                while (isRunning) {
                    ca.retrieveHandler(target);

                    count++;
                }
            } catch (Throwable t) {
                throwable = t;
            }            
        }
    }
}