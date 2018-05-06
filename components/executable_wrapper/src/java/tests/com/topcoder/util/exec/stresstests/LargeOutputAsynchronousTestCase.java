/**
 * LargeOutputAsynchronousTestCase.java
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
 * <p>This test case tests the handling of commands
 * which result in large outputs.</p>
 *
 * @author adic
 * @version 1.0
 */
public class LargeOutputAsynchronousTestCase extends TestCase {

    private static final int SIZE=500000;
    private static final int TIMEOUT=5;

    public void testLargeOutput() {
        String exename = "test_files"+File.separatorChar+"output";

        AsynchronousExecutorHandle handle = null;
        long tic=System.currentTimeMillis();
        try {
            handle = Exec.executeAsynchronously(new String[] {exename,""+SIZE});
        }
        catch (ExecutionException e) {
            assertTrue("ExecutionException should not have been thrown: "+e.getMessage(),false);
        }

        assertNotNull("Handle should not be null", handle);

        while (true) {
           if (handle.isDone()) break;
           try { Thread.sleep(100); } catch (InterruptedException e) {}
        }

        long tac=System.currentTimeMillis();

        //assertTrue("Execution should have terminated",handle.isDone());
        ExecutionResult result=handle.getExecutionResult();
        assertNotNull("Execution result should not be null", result);
        assertEquals("Exit status should be 0", 0, result.getExitStatus());
        String stdout = result.getOut();
        String stderr = result.getErr();
        assertTrue("Standard output should have the length equal to "+SIZE,
            stdout != null && stdout.length() == SIZE);
        assertTrue("Standard error should have the length equal to 0",
            stderr != null && stderr.length() == 0);
        assertTrue("Execution time should be less than "+TIMEOUT+"s",tac-tic<TIMEOUT*1000);
        System.out.println("OK "+(tac-tic)+"ms");
    }

    public static Test suite() {
        return new TestSuite(LargeOutputAsynchronousTestCase.class);
    }
}
