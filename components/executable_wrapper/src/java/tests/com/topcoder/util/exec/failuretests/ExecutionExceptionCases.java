package com.topcoder.util.exec.failuretests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import com.topcoder.util.exec.*;
import java.util.*;

/**
 * <p></p>
 *
 * @author b0b0b0b
 * @version 1.0
 */
public class ExecutionExceptionCases extends TestCase {
	public void testExecutionExceptionCase()
	{
		boolean success = false;
		try {
			ExecutionResult er = Exec.execute(new String[]{"xxxjjj\r\n"});
		} catch (ExecutionException e) { success = true; }
		if (!success) fail("Expected ExecutionException here");
	}

    public static Test suite() {
        return new TestSuite(ExecutionExceptionCases.class);
    }
}
