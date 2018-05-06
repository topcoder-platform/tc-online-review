package com.topcoder.util.exec.accuracytests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import com.topcoder.util.exec.*;

/**
 * Tests public methods of Exec class
 *
 * @author mathgodleo
 * @version 1.0
 */
public class TestExec extends TestCase {
    /**
     * Tests public methods of exec class.
     */
    public void testExec() throws ExecutionException{
        ExecutionResult result = 
            Exec.execute(new String[]{"dir"});

        assertEquals("No errors", result.getErr(), "");
        assertFalse("Some output", result.getOut().equals(""));
        assertEquals("Proper exit status", result.getExitStatus(), 0);
    
    
        result = 
            Exec.execute(new ExecutionParameters(new String[]{"dir"}));

        assertEquals("No errors", result.getErr(), "");
        assertFalse("Some output", result.getOut().equals(""));
        assertEquals("Proper exit status", result.getExitStatus(), 0);
    
        result = 
            Exec.execute(new String[]{"dir"}, 100000);

        assertEquals("No errors", result.getErr(), "");
        assertFalse("Some output", result.getOut().equals(""));
        assertEquals("Proper exit status", result.getExitStatus(), 0);
    
    
        result = 
            Exec.execute(new ExecutionParameters(new String[]{"dir"}), 100000);

        assertEquals("No errors", result.getErr(), "");
        assertFalse("Some output", result.getOut().equals(""));
        assertEquals("Proper exit status", result.getExitStatus(), 0);
    
    
    
    
        AsynchronousExecutorHandle handle = 
            Exec.executeAsynchronously(new String[]{"dir","/s"});

        assertEquals("Not done immediately", handle.isDone(), false);
        handle.halt();
        assertEquals("Done after halt", handle.isDone(), true);
        assertEquals("Exited with error",
                    handle.getExecutionResult(),
                    null);
    
    
        handle = 
            Exec.executeAsynchronously(
                new ExecutionParameters(new String[]{"dir","/s"}));

        assertEquals("Not done immediately", handle.isDone(), false);
        handle.halt();
        assertEquals("Done after halt", handle.isDone(), true);
        assertEquals("Exited with error",
                    handle.getExecutionResult(),
                    null);

        handle = 
            Exec.executeAsynchronously(new String[]{"dir","c:\\","/s"}, 500);

        assertEquals("Not done immediately", handle.isDone(), false);
        handle.halt();     
        assertEquals("Done after halt", handle.isDone(), true);
        assertEquals("Exited with error",
                    handle.getExecutionResult(),
                    null);

        handle = 
            Exec.executeAsynchronously(
                new ExecutionParameters(new String[]{"dir","c:\\","/s"}), 500);
    
        assertEquals("Not done immediately", handle.isDone(), false);
        handle.halt();
        assertEquals("Done after halt", handle.isDone(), true);
        assertEquals("Exited with error",
                    handle.getExecutionResult(),
                    null);
    }

    public static Test suite() {
        return new TestSuite(TestExec.class);
    }
}





