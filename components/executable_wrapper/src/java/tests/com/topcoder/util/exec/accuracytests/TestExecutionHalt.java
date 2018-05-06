package com.topcoder.util.exec.accuracytests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import com.topcoder.util.exec.*;

/**
 * This tests that when execution is halted, getExecutionException()
 * returns an ExecutionHaltedException.
 *
 * @author mathgodleo
 * @version 1.0
 */
public class TestExecutionHalt extends TestCase {
    /**
     * Tests that when execution is halted, getExecutionException()
     * returns an ExecutionHaltedException.
     */
    public void testHalt() throws ExecutionException{
        AsynchronousExecutorHandle handle = 
            Exec.executeAsynchronously(new String[]{"dir","c:","/s"});
        
        handle.halt();
        
        try{
            Thread.currentThread().sleep(1000);
        }
        catch (InterruptedException ex){
            // ignore
        }
        
        if (handle.getExecutionException() == null){
            // bad, exception not thrown
            fail("T1: Should have thrown exception");
        }
    }

    public static Test suite() {
        return new TestSuite(TestExecutionHalt.class);
    }
}




