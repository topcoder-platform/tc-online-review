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
public class TimeoutExceptionCases extends TestCase {
	public void testTimeoutExceptionCase()
		throws ExecutionException
	{
		String java = System.getProperty("java.home");
		java += System.getProperty("file.separator");
		java += "bin";
		java += System.getProperty("file.separator");
		java += "java";

		boolean success = false;
		try {
			ExecutionResult er = Exec.execute(new String[]{java,"-cp",System.getProperty("java.class.path"),"com.topcoder.util.exec.failuretests.TimeoutExceptionCases"},1);
		} catch (ExecutionTimedOutException e) { success = true; }
		if (!success) fail("Expected ExecutionTimedOutException here");
	}

    public static Test suite() {
        return new TestSuite(TimeoutExceptionCases.class);
    }

    public final static void main(String[] args) {
		for (int i=0; i<100; i++) System.out.println("jaklsdfsalkdj");
	}
}
