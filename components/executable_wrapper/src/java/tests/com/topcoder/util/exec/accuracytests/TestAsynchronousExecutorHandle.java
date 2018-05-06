package com.topcoder.util.exec.accuracytests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import com.topcoder.util.exec.*;

/**
 * Tests public methods of AsynchronousExecutorHandle class
 *
 * @author mathgodleo
 * @version 1.0
 */
public class TestAsynchronousExecutorHandle extends TestCase {
    /**
     * Tests public methods of AsynchronousExecutorHandle class
     */
    public void testAsynchronousExecutorHandle() throws ExecutionException{
        AsynchronousExecutorHandle handle = 
            Exec.executeAsynchronously(new String[]{"dir","c:","/s"});
        
        assertTrue("run time >= 0", handle.getRunningTimeMS() >= 0);
        
        assertTrue("not done with complex task", !handle.isDone());
        
        handle.halt();
        
        assertTrue("done after halting", handle.isDone());
        
        // once task is stopped, running time shouldn't change
        long t1 = handle.getRunningTimeMS();
        try{
            Thread.currentThread().sleep(50);
        }
        catch (InterruptedException ex){
            // ignore
        }
        long t2 = handle.getRunningTimeMS();
        assertEquals("running time doesn't change after task is done",
                     t1,t2);
    }

    public static Test suite() {
        return new TestSuite(TestAsynchronousExecutorHandle.class);
    }
}




