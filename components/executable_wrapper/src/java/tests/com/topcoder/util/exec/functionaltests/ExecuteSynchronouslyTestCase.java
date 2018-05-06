package com.topcoder.util.exec.functionaltests;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import com.topcoder.util.exec.*;

/**
 * <p>This test case tests synchronous execution of a command.</p>
 *
 * @author srowen
 * @version 1.0
 */
public class ExecuteSynchronouslyTestCase extends TestCase {

    public void testExecution() throws Exception {
        final ExecutionResult result = Exec.execute(new String[] {"hostname"});
        assertNotNull("Execution result should not be null", result);
        assertEquals("Exit status should be 0", 0, result.getExitStatus());
        final String output = result.getOut();
        assertTrue("Output should not be empty",
                   output != null && output.length() > 0);
        final String error = result.getErr();
        assertTrue("Error should be empty",
                   error != null && error.length() == 0);
    }

    public void testExecutionOfNonexistentCommand() throws Exception {
        try {
            Exec.execute(new String[] {"acommandthatdoesnotexist"});
            fail("Execution of non-existent command should have thrown ExecutionException");
        } catch(ExecutionException ee) {
            // good
        }
    }

    public void testSetEnvironment() throws Exception {
        // Note to developers - this may require a little tweaking to work!
        // You may need to replace it with a comparable test if using this on Windows -
        // does it have echo?
        final String variable = "FOO";
        final String value = "bar";
        String[] execStr;
        String trailingChar;

        if (isUnixPlatform()) {
            execStr = new String[] {"echo", "$"+variable};
            trailingChar = UNIX_TRAILING_CHAR;
        } else {
            execStr = new String[] {"echo", "%"+variable+"%"};
            trailingChar = WIN_TRAILING_CHAR;
        }
        final ExecutionParameters parameters = new ExecutionParameters(execStr);
        final Map environment = new HashMap();
        environment.put(variable, value);
        parameters.setEnvironment(environment);
        final ExecutionResult result = Exec.execute(parameters);
        assertNotNull("Execution result should not be null", result);
        assertEquals("echo output should match value", value+trailingChar,
            result.getOut());
    }

    public void testSetWorkingDirectory() throws Exception {
        // Note to developers - this may require a little tweaking to work!
        // You may need to replace it with a comparable test if using this on Windows -
        // I think it has pwd (print working directory) but not sure...
        final String dir = "test_files"; //File.separatorChar+"tmp";
        String[] execStr;
        String trailingChar;
        String output;
        boolean isUnix = isUnixPlatform();

        if (isUnix) {
            execStr = new String[] {"pwd"};
            trailingChar = UNIX_TRAILING_CHAR;
        } else {
            execStr = new String[] {"cd"};
            trailingChar = WIN_TRAILING_CHAR;
        }
        final ExecutionParameters parameters =
            new ExecutionParameters(execStr);
        parameters.setWorkingDirectory(new File(dir));
        final ExecutionResult result = Exec.execute(parameters);
        output = result.getOut();
        if (!isUnix) {
            output = output.substring(output.indexOf(':') + 1);
        }
        assertNotNull("Execution result should not be null", result);
        assertTrue("pwd output should match working directory", output.indexOf(dir)>=0);
    }

    private static boolean isUnixPlatform() {
        Properties properties = System.getProperties();
        String osName = (String) properties.get("os.name");

        return (osName.indexOf("Windows") == -1);
    }

    public static Test suite() {
        return new TestSuite(ExecuteSynchronouslyTestCase.class);
    }

    private static final String UNIX_TRAILING_CHAR = "\n";
    private static final String WIN_TRAILING_CHAR = "\r\n";
}
