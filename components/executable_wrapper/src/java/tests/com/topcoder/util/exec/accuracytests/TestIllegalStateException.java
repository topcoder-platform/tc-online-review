package com.topcoder.util.exec.accuracytests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import com.topcoder.util.exec.*;

/**
 * This tests that getExecutionResult and getExecutionException
 * throw an IllegalStateException if called before a command finished
 * executing.
 *
 * @author mathgodleo
 * @version 1.0
 */
public class TestIllegalStateException extends TestCase {
    /**
     * Tests that getExecutionResult() throws an IllegalStateException
     * if called before a command is finished executing.
     */
    public void testResult() throws ExecutionException{
        try{
            AsynchronousExecutorHandle handle = 
                Exec.executeAsynchronously(new String[]{"dir","c:","/s"});
            handle.getExecutionResult();
            // bad, exception not thrown
            fail("T1: Should have thrown exception");
        }
        catch (IllegalStateException ex){
            // good, caught as expected
        }
    }

    /**
     * Tests that getExecutionException() throws an IllegalStateException
     * if called before a command is finished executing.
     */
    public void testException() throws ExecutionException{
        try{
            AsynchronousExecutorHandle handle = 
                Exec.executeAsynchronously(new String[]{"dir","c:","/s"});
            handle.getExecutionException();
            // bad, exception not thrown
            fail("T1: Should have thrown exception");
        }
        catch (IllegalStateException ex){
            // good, caught as expected
        }
    }


    public static Test suite() {
        return new TestSuite(TestIllegalStateException.class);
    }
}


