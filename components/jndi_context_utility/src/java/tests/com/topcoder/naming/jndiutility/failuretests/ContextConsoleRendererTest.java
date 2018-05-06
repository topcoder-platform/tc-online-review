/**
 * Copyright © 2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.naming.jndiutility.failuretests;

import com.topcoder.naming.jndiutility.*;

import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author AdamSelene
 *
 * 
 */
public class ContextConsoleRendererTest extends TestCase {

	public static Test suite() {
		return new TestSuite(ContextConsoleRendererTest.class);
	} 
	
	public void testStart()
	{
        /*per http://software.topcoder.com/forum/c_forum_message.jsp?f=6707294&r=7543712
		 *try {
		
			ContextConsoleRenderer ccr = new ContextConsoleRenderer();
			ccr.startContext("somenonsense.whee", "whee");
			fail("Did not throw exception on false context.");			
		}
		catch (ContextRendererException e) {}
		catch (Exception e)
		{
			fail("Non-specified exception thrown by CCR. () - " + e.getClass().toString());
		}*/
		
		try {
		
			ContextConsoleRenderer ccr = new ContextConsoleRenderer();
			ccr.startContext(null, "whee");
			fail("Did not throw exception on null context.");			
		}
		catch (IllegalArgumentException e) {}
		catch (Exception e)
		{
			fail("Non-specified exception thrown by CCR. -(n1s) " + e.getClass().toString());
		}
		try {
		
			ContextConsoleRenderer ccr = new ContextConsoleRenderer();
			ccr.startContext("somenonsense.whee",null);
			fail("Did not throw exception on null relative context.");			
		}
		catch (IllegalArgumentException e) {}
		catch (Exception e)
		{
			fail("Non-specified exception thrown by CCR. - (n2s)" + e.getClass().toString());
		}

	}
	
	public void testEnd()
	{	
		/*
         * per http://software.topcoder.com/forum/c_forum_message.jsp?f=6707294&r=7543712
		 *try {
		
			ContextConsoleRenderer ccr = new ContextConsoleRenderer();
			ccr.endContext("somenonsense.whee", "whee");
			fail("Did not throw exception on false context.");			
		}
		catch (ContextRendererException e) {}
		catch (Exception e)
		{
			fail("Non-specified exception thrown by CCR. - (first) " + e.getClass().toString());
		}*/
		try {
		
			ContextConsoleRenderer ccr = new ContextConsoleRenderer();
			ccr.endContext(null, "whee");
			fail("Did not throw exception on null context.");			
		}
		catch (IllegalArgumentException e) {}
		catch (Exception e)
		{
			fail("Non-specified exception thrown by CCR (n1). - " + e.getClass().toString());
		}
		try {
		
			ContextConsoleRenderer ccr = new ContextConsoleRenderer();
			ccr.endContext("funk.whee", null);
			fail("Did not throw exception on null relative ctx.");			
		}
		catch (IllegalArgumentException e) {}
		catch (Exception e)
		{
			fail("Non-specified exception thrown by CCR (n2). - " + e.getClass().toString());
		}

	}
	
	public void testBindingFound()
	{
		try {
		
			ContextConsoleRenderer ccr = new ContextConsoleRenderer();
			ccr.bindingFound("somenonsense.whee", "whee");
			fail("Did not throw exception on false context.");			
		}
		catch (ContextRendererException e) {}
		catch (Exception e)
		{
			fail("Non-specified exception thrown by CCR. - " + e.getClass().toString());
		}
		try {
		
			ContextConsoleRenderer ccr = new ContextConsoleRenderer();
			ccr.bindingFound(null, "whee");
			fail("Did not throw exception on null context.");			
		}
		catch (IllegalArgumentException e) {}
		catch (Exception e)
		{
			fail("Non-specified exception thrown by CCR. - " + e.getClass().toString());
		}
		try {
		
			ContextConsoleRenderer ccr = new ContextConsoleRenderer();
			ccr.bindingFound("funk.whee", null);
			fail("Did not throw exception on null relative ctx.");			
		}
		catch (IllegalArgumentException e) {}
		catch (Exception e)
		{
			fail("Non-specified exception thrown by CCR. - " + e.getClass().toString());
		}
		
	}
}
