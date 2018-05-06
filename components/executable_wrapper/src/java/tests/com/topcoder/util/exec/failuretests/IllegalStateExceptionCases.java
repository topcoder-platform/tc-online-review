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
public class IllegalStateExceptionCases extends TestCase {
	static boolean isbodrius = false;
	static {
		for (Enumeration e = System.getProperties().propertyNames(); e.hasMoreElements();) {
			String s = (String)e.nextElement();
			//System.out.println(s+" "+System.getProperty(s));
			if (System.getProperty(s).toLowerCase().indexOf("bodrius") != -1) { System.out.println("howdy"); isbodrius = true; break; }
		}

	}
	public void testIllegalStateExceptionCase()
		throws ExecutionException
	{
		String java = System.getProperty("java.home");
		java += System.getProperty("file.separator");
		java += "bin";
		java += System.getProperty("file.separator");
		java += "java";

//		ExecutionResult er = Exec.execute(new String[]{java,"-cp",System.getProperty("java.class.path"),"com.topcoder.util.exec.failuretests.IllegalStateExceptionCases"});
//		System.out.println(er.getOut());
		AsynchronousExecutorHandle aeh = Exec.executeAsynchronously(new String[]{java,"-cp",System.getProperty("java.class.path"),"com.topcoder.util.exec.failuretests.IllegalStateExceptionCases"});
		boolean success = false;
		try {
			ExecutionResult er = aeh.getExecutionResult();
		} catch (IllegalStateException e) { success = true; }
		if (!success) fail("Expected IllegalStateException here");
		success = false;
		try {
			ExecutionException er = aeh.getExecutionException();
		} catch (IllegalStateException e) { success = true; }
		if (!success) fail("Expected IllegalStateException here");
		aeh.halt();
		try {
			Thread.currentThread().sleep(5000);
		} catch (Exception e) {}
		ExecutionException ee = aeh.getExecutionException();
		if (!(ee instanceof ExecutionHaltedException)) fail("Expected ExecutionException but got "+(ee==null?"null":ee.getClass().getName()));
	}

    public static Test suite() {
        return new TestSuite(IllegalStateExceptionCases.class);
    }

    public final static void main(String[] args) {
		if (!isbodrius) {
			while (true) {
				try {
					Thread.currentThread().sleep(1000000);
				} catch (Exception e) {
				}
			}
		} else {
			for (int i=0; i<10000; i++) System.out.println("jaklsdfsalkdj");
		}
	}
}
