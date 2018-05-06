package com.topcoder.util.exec.accuracytests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import com.topcoder.util.exec.*;

/**
 * This tests that TimeoutExecutor throws a TestTimedOutException
 * if the command doesn't execute before the timeout passes.
 *
 * @author mathgodleo
 * @version 1.0
 */
public class TestTimedOutException extends TestCase {
    /**
     * Tests that getExecutionException() throws an ExecutionTimedOutException
     * if execution of a command times out.
     */
    public void testTimeOutException() throws ExecutionException{
        try{
            ExecutionResult result = 
                Exec.execute(new String[]{"dir","c:","/s"}, 10);
            // bad, exception not thrown
            fail("T1: Should have thrown exception");
        }
        catch (ExecutionTimedOutException ex){
            // good, caught as expected
        }
    }

    public static Test suite() {
        return new TestSuite(TestTimedOutException.class);
    }
}



