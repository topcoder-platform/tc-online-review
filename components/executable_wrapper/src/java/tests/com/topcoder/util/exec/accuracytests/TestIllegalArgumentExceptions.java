package com.topcoder.util.exec.accuracytests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import com.topcoder.util.exec.*;

/**
 * This tests that invalid arguments to various methods
 * result in IllegalArgumentException thrown</p>
 *
 * @author mathgodleo
 * @version 1.0
 */
public class TestIllegalArgumentExceptions extends TestCase {
    /**
     * Tests that the Exec class throws IllegalArgumentExceptions
     * whenever it gets invalid arguments.
     */
    public void testExec() throws ExecutionException{
        try{
            String[] command = null;
            Exec.execute(command);
            // bad, exception not thrown
            fail("T1: Should have thrown exception");
        }
        catch (IllegalArgumentException ex){
            // good, caught as expected
        }

        try{
            String[] command = new String[0];
            Exec.execute(command);
            // bad, exception not thrown
            fail("T2: Should have thrown exception");
        }
        catch (IllegalArgumentException ex){
            // good, caught as expected
        }
    
        try{
            String[] command = {"dir"};
            Exec.execute(command, -5);
            // bad, exception not thrown
            fail("T3: Should have thrown exception");
        }
        catch (IllegalArgumentException ex){
            // good, caught as expected
        }
    
        
        try{
            ExecutionParameters params = null;
            Exec.execute(params);
            // bad, exception not thrown
            fail("T4: Should have thrown exception");
        }
        catch (IllegalArgumentException ex){
            // good, caught as expected
        }    
        
        try{
            ExecutionParameters params = 
                new ExecutionParameters(new String[]{"dir"});
            Exec.execute(params, -5);
            // bad, exception not thrown
            fail("T5: Should have thrown exception");
        }
        catch (IllegalArgumentException ex){
            // good, caught as expected
        }


        try{
            String[] command = null;
            Exec.executeAsynchronously(command);
            // bad, exception not thrown
            fail("T6: Should have thrown exception");
        }
        catch (IllegalArgumentException ex){
            // good, caught as expected
        }

        try{
            String[] command = new String[0];
            Exec.executeAsynchronously(command);
            // bad, exception not thrown
            fail("T7: Should have thrown exception");
        }
        catch (IllegalArgumentException ex){
            // good, caught as expected
        }
    
        try{
            String[] command = {"dir"};
            Exec.executeAsynchronously(command, -5);
            // bad, exception not thrown
            fail("T8: Should have thrown exception");
        }
        catch (IllegalArgumentException ex){
            // good, caught as expected
        }
    
        
        try{
            ExecutionParameters params = null;
            Exec.executeAsynchronously(params);
            // bad, exception not thrown
            fail("T9: Should have thrown exception");
        }
        catch (IllegalArgumentException ex){
            // good, caught as expected
        }    
        
        try{
            ExecutionParameters params = 
                new ExecutionParameters(new String[]{"dir"});
            Exec.executeAsynchronously(params, -5);
            // bad, exception not thrown
            fail("T10: Should have thrown exception");
        }
        catch (IllegalArgumentException ex){
            // good, caught as expected
        }
    }

    /**
     * Tests that the ExecutionParameters class throws
     * IllegalArgumentExceptions whenever it gets invalid arguments.
     */
    public void testExecutionParameters(){
        try{
            ExecutionParameters params = new ExecutionParameters(null);
            // bad, exception not thrown
            fail("T1: Should have thrown exception");
        }
        catch (IllegalArgumentException ex){
            // good, caught as expected
        }
        
        try{
            ExecutionParameters params = new ExecutionParameters(new String[]{});
            // bad, exception not thrown
            fail("T2: Should have thrown exception");
        }
        catch (IllegalArgumentException ex){
            // good, caught as expected
        }
    }

    public static Test suite() {
        return new TestSuite(TestIllegalArgumentExceptions.class);
    }
}

