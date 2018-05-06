/**
 * ManyRunsSynchronousThreadedTestCase.java
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
public class ManyRunsSynchronousThreadedTestCase extends TestCase {

    private static final int MSPERRUN=500;
    private static final int COUNT=50;

    public void testManyRuns() {
        final String exename = "test_files"+File.separatorChar+"output";

        long ctic=System.currentTimeMillis();

        Thread threads[]=new Thread[COUNT];

        for (int i=0;i<COUNT;i++) {
            final int j=i;
            threads[i]=
                new Thread() {
                    public void run() {
                        ExecutionResult result=null;
                        long tic=System.currentTimeMillis();
                        try {
                            result = Exec.execute(new String[] {exename,"0"});
                        }
                        catch (ExecutionException e) {
                            assertTrue("ExecutionException should not have been thrown: "+e.getMessage(),false);
                        }
                        long tac=System.currentTimeMillis();

                        assertNotNull("Execution result should not be null", result);
                        assertEquals("Exit status should be 0", 0, result.getExitStatus());
                        String stdout = result.getOut();
                        String stderr = result.getErr();
                        assertTrue("Standard output should have the length equal to 0",
                            stdout != null && stdout.length() == 0);
                        assertTrue("Standard error should have the length equal to 0",
                            stderr != null && stderr.length() == 0);
                        //assertTrue("Execution time should be less than 1s",tac-tic<1000);
                        //System.out.println((j+1)+"/"+COUNT+" "+(tac-tic)+"ms"+(char)13);
                    }
                };
            threads[i].start();
        }

        try {
            for (int i=0;i<COUNT;i++) threads[i].join();
        }
        catch (InterruptedException e) {
        }

        long ctac=System.currentTimeMillis();
        assertTrue("Cumulated execution time should be less than "+COUNT*MSPERRUN+"ms",ctac-ctic<COUNT*MSPERRUN);
        System.out.println("OK "+(ctac-ctic)+"ms");
    }

    public static Test suite() {
        return new TestSuite(ManyRunsSynchronousThreadedTestCase.class);
    }
}
