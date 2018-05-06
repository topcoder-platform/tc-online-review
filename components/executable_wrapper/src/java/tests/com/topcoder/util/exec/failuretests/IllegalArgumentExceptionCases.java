package com.topcoder.util.exec.failuretests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import com.topcoder.util.exec.*;

/**
 * <p></p>
 *
 * @author b0b0b0b
 * @version 1.0
 */
public class IllegalArgumentExceptionCases extends TestCase {
	public final static long BADTIMEOUT = -124124;
	public final static long GOODTIMEOUT = 124;
	public void testExec()
		throws ExecutionException
	{
		try {
			exec(null);
			exec(new String[0]);
			exec(new String[50]);
			//exec(new String[]{"howdy"});
			exec(null,BADTIMEOUT);
			exec(new String[0],BADTIMEOUT);
			exec(new String[50],BADTIMEOUT);
			exec(new String[]{"howdy"},BADTIMEOUT);
			exec(null,GOODTIMEOUT);
			exec(new String[0],GOODTIMEOUT);
			exec(new String[50],GOODTIMEOUT);
			//exec(new String[]{"howdy"},GOODTIMEOUT);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception: "+e.toString());
		}
	}

	public void testPlatformSupport()
	{
		boolean success = false;
		try {
			PlatformSupport.registerPlatformSupport(null,new WindowsPlatformSupport());
		} catch (IllegalArgumentException ex) { success = true; }
		if (!success) {
			fail("failed to throw IllegalArgumentException");
			//assertTrue(success);
		}
		success=false;

		try {
			PlatformSupport.registerPlatformSupport(null,null);
		} catch (IllegalArgumentException ex) { success = true; }
		if (!success) {
			fail("failed to throw IllegalArgumentException");
			//assertTrue(success);
		}
		success=false;

		try {
			PlatformSupport.registerPlatformSupport(System.getProperty("os.name"),null);
		} catch (IllegalArgumentException ex) { success = true; }
		if (!success) {
			fail("failed to throw IllegalArgumentException");
			//assertTrue(success);
		}
	}

	private void exec(String[] args)
		throws ExecutionException
	{
		boolean success = false;
		try {
			ExecutionParameters ep = new ExecutionParameters(args);
			Exec.execute(ep);
		} catch (IllegalArgumentException ex) { success = true; }
		if (!success) {
			fail("failed to throw IllegalArgumentException");
			//assertTrue(success);
		}
		success=false;
		try {
			Exec.execute(args);
		} catch (IllegalArgumentException ex) { success = true; }
		if (!success) {
			fail("failed to throw IllegalArgumentException");
			//assertTrue(success);
		}
		success=false;
		try {
			ExecutionParameters ep = new ExecutionParameters(args);
			Exec.executeAsynchronously(ep);
		} catch (IllegalArgumentException ex) { success = true; }
		if (!success) {
			fail("failed to throw IllegalArgumentException");
			//assertTrue(success);
		}
		success=false;
		try {
			Exec.executeAsynchronously(args);
		} catch (IllegalArgumentException ex) { success = true; }
		if (!success) {
			fail("failed to throw IllegalArgumentException");
			//assertTrue(success);
		}
	}

	private void exec(String[] args, long larg)
		throws ExecutionException
	{
		boolean success = false;
		try {
			ExecutionParameters ep = new ExecutionParameters(args);
			Exec.execute(ep,larg);
		} catch (IllegalArgumentException ex) { success = true; }
		if (!success) {
			fail("failed to throw IllegalArgumentException");
			//assertTrue(success);
		}
		success=false;
		try {
			Exec.execute(args,larg);
		} catch (IllegalArgumentException ex) { success = true; }
		if (!success) {
			fail("failed to throw IllegalArgumentException");
			//assertTrue(success);
		}
		success=false;
		try {
			ExecutionParameters ep = new ExecutionParameters(args);
			Exec.executeAsynchronously(ep,larg);
		} catch (IllegalArgumentException ex) { success = true; }
		if (!success) {
			fail("failed to throw IllegalArgumentException");
			//assertTrue(success);
		}
		success=false;
		try {
			Exec.executeAsynchronously(args,larg);
		} catch (IllegalArgumentException ex) { success = true; }
		if (!success) {
			fail("failed to throw IllegalArgumentException");
			//assertTrue(success);
		}
	}

    public static Test suite() {
        return new TestSuite(IllegalArgumentExceptionCases.class);
    }
}
