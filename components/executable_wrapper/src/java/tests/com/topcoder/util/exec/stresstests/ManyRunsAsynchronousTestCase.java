/**
 * ManyRunsAsynchronousTestCase.java
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
 * <p>This test case tests the execution of many commands.</p>
 *
 * @author adic
 * @version 1.0
 */
public class ManyRunsAsynchronousTestCase extends TestCase {

    private static final int MSPERRUN=500;
    private static final int COUNT=50;

    public void testManyRuns() {
        final String exename = "test_files"+File.separatorChar+"sleep";
        long tic=System.currentTimeMillis();

        AsynchronousExecutorHandle handle[] = new AsynchronousExecutorHandle[COUNT];
        for (int i=0;i<COUNT;i++) {
            try {
                handle[i] = Exec.executeAsynchronously(new String[] {exename,"0"});
            }
            catch (ExecutionException e) {
                assertTrue("ExecutionException should not have been thrown: "+e.getMessage(),false);
            }
            assertNotNull("Handle should not be null", handle[i]);
        }

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
            assertTrue("Standard output should have the length equal to 0",
                stdout != null && stdout.length() == 0);
            assertTrue("Standard error should have the length equal to 0",
                stderr != null && stderr.length() == 0);
        }

        long tac=System.currentTimeMillis();
        assertTrue("Cumulated execution time should be less than "+COUNT*MSPERRUN+"ms",tac-tic<COUNT*MSPERRUN);
        System.out.println("OK "+(tac-tic)+"ms");
    }

    public static Test suite() {
        return new TestSuite(ManyRunsAsynchronousTestCase.class);
    }
}
