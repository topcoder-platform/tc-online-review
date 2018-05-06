/**
 * ManyRunsMixedLargeOutputTestCase.java
 *
 * Copyright © 2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.exec.stresstests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import com.topcoder.util.exec.*;
import java.io.File;

/**
 * <p>This test case tests the execution of many commands with large output.</p>
 *
 * @author adic
 * @version 1.0
 */
public class ManyRunsMixedLargeOutputTestCase extends TestCase {

    private static final int SIZE=50000;
    private static final int MSPERRUN=1000;
    private static final int COUNT=30;

    public void testManyRuns() {
        final String exename = "test_files"+File.separatorChar+"output";
        long tic=System.currentTimeMillis();

        AsynchronousExecutorHandle handle[] = new AsynchronousExecutorHandle[COUNT];
        Thread threads[]=new Thread[COUNT];

        for (int i=0;i<COUNT;i++) {
            // launch COUNT threads with synchronous executions
            threads[i]=
                new Thread() {
                    public void run() {
                        ExecutionResult result=null;
                        long tic=System.currentTimeMillis();
                        try {
                            result = Exec.execute(new String[] {exename,""+SIZE});
                        }
                        catch (ExecutionException e) {
                            assertTrue("ExecutionException should not have been thrown: "+e.getMessage(),false);
                        }
                        long tac=System.currentTimeMillis();

                        assertNotNull("Execution result should not be null", result);
                        assertEquals("Exit status should be 0", 0, result.getExitStatus());
                        String stdout = result.getOut();
                        String stderr = result.getErr();
                        assertTrue("Standard output should have the length equal to "+SIZE,
                            stdout != null && stdout.length() == SIZE);
                        assertTrue("Standard error should have the length equal to 0",
                            stderr != null && stderr.length() == 0);
                        //assertTrue("Execution time should be less than 1s",tac-tic<1000);
                    }
                };
            threads[i].start();

            // launch COUNT asynchronous executions
            try {
                handle[i] = Exec.executeAsynchronously(new String[] {exename,""+SIZE});
            }
            catch (ExecutionException e) {
                assertTrue("ExecutionException should not have been thrown: "+e.getMessage(),false);
            }
            assertNotNull("Handle should not be null", handle[i]);
        }

        // wait the end of the asynchronous executions
        for (int i=0;i<COUNT;i++) {
            while (true) {
                if (handle[i].isDone()) break;
                try { Thread.sleep(500); } catch (InterruptedException e) {}
            }
        }

        for (int i=0;i<COUNT;i++) {
            assertTrue("Execution should have terminated",handle[i].isDone());
            ExecutionResult result=handle[i].getExecutionResult();
            assertNotNull("Execution result should not be null", result);
            assertEquals("Exit status should be 0", 0, result.getExitStatus());
            String stdout = result.getOut();
            String stderr = result.getErr();
            assertTrue("Standard output should have the length equal to "+SIZE,
                stdout != null && stdout.length() == SIZE);
            assertTrue("Standard error should have the length equal to 0",
                stderr != null && stderr.length() == 0);
        }

        // join the sychronous execution threads
        try {
            for (int i=0;i<COUNT;i++) threads[i].join();
        }
        catch (InterruptedException e) {
        }

        long tac=System.currentTimeMillis();
        assertTrue("Cumulated execution time should be less than "+COUNT*MSPERRUN*2+"ms",tac-tic<COUNT*MSPERRUN*2);
        System.out.println("OK "+(tac-tic)+"ms");
    }

    public static Test suite() {
        return new TestSuite(ManyRunsMixedLargeOutputTestCase.class);
    }
}
