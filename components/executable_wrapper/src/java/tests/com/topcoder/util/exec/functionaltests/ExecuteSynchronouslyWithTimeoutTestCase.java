package com.topcoder.util.exec.functionaltests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import com.topcoder.util.exec.*;
import java.io.File;

/**
 * <p>This test case tests synchronous execution of a command,
 * with a timeout.</p>
 *
 * @author srowen
 * @version 1.0
 */
public class ExecuteSynchronouslyWithTimeoutTestCase extends TestCase {

    public void testExecutionNoTimeout() throws Exception {
        final ExecutionResult result = 
            Exec.execute(new String[] {"test_files"+File.separatorChar+"sleep", "2"}, 3000);
        assertNotNull("Execution result should not be null", result);
        assertEquals("Exit status should be 0", 0, result.getExitStatus());
        final String output = result.getOut();
        assertTrue("Output should be empty",
                   output != null && output.length() == 0);
        final String error = result.getErr();
        assertTrue("Error should be empty",
                   error != null && error.length() == 0);
    }

    public void testExecutionTimeout() {
        try {
            Exec.execute(new String[] {"test_files"+File.separatorChar+"sleep", "3"}, 2000);
            fail("Should have timed out and thrown an ExecutionException");
        } catch(ExecutionException ee) {
            // good
        }
    }

    public static Test suite() {
        return new TestSuite(ExecuteSynchronouslyWithTimeoutTestCase.class);
    }
}
