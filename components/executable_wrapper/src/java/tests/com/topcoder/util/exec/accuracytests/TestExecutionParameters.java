package com.topcoder.util.exec.accuracytests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import com.topcoder.util.exec.*;

import java.io.File;
import java.util.HashMap;

/**
 * Tests public methods of TestExecutionParameters class
 *
 * @author mathgodleo
 * @version 1.0
 */
public class TestExecutionParameters extends TestCase {
    /**
     * Tests public methods of ExecutionParameters class
     */
    public void testExecutionParameters() throws ExecutionException{
        
        ExecutionParameters params = 
            new ExecutionParameters(new String[]{"dir","/s"});
        
        // command should be same as command line argument
        assertEquals("command set correctly", params.getCommand()[0],"dir");
        assertEquals("command set correctly", params.getCommand()[1],"/s");

        // make sure we can get/set the working directory properly
        params.setWorkingDirectory(new File("c:\\"));
        assertEquals("working directory set properly",
                     params.getWorkingDirectory(),new File("c:\\"));
        
        // make sure we can get/set the environment properly
        HashMap env = new HashMap();
        env.put("a","b");
        params.setEnvironment(env);
        assertEquals("environment set properly",
                     params.getEnvironment(), env);
    }

    public static Test suite() {
        return new TestSuite(TestExecutionParameters.class);
    }
}





