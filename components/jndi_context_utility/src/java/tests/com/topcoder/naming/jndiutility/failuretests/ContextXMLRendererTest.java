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
public class ContextXMLRendererTest extends TestCase {

	public static Test suite() {
		return new TestSuite(ContextXMLRendererTest.class);
	} 
	
	public void testStart()
	{
		/*
         Test removed per
         http://software.topcoder.com/forum/c_forum_message.jsp?f=6707294&r=7543712
		 try {
		
			ContextXMLRenderer ccr = new ContextXMLRenderer();
			ccr.startContext("somenonsense.whee", "whee");
			fail("Did not throw exception on false context.");			
		}
		catch (ContextRendererException e) {}
		catch (Exception e)
		{
			fail("Non-specified exception thrown by CCR. - " + e.getClass().toString());
		}*/
		
		try {
		
			ContextXMLRenderer ccr = new ContextXMLRenderer();
			ccr.startContext(null, "whee");
			fail("Did not throw exception on null context.");			
		}
		catch (IllegalArgumentException e) {}
		catch (Exception e)
		{
			fail("Non-specified exception thrown by CCR. - " + e.getClass().toString());
		}
		try {
		
			ContextXMLRenderer ccr = new ContextXMLRenderer();
			ccr.startContext("somenonsense.whee",null);
			fail("Did not throw exception on null relative context.");			
		}
		catch (IllegalArgumentException e) {}
		catch (Exception e)
		{
			fail("Non-specified exception thrown by CCR. - " + e.getClass().toString());
		}

	}
	
	public void testEnd()
	{	
		/*
         * test removed per 
         * http://software.topcoder.com/forum/c_forum_message.jsp?f=6707294&r=7543712
		 *try {
		
			ContextXMLRenderer ccr = new ContextXMLRenderer();
			ccr.endContext("somenonsense.whee", "whee");
			fail("Did not throw exception on false context.");			
		}
		catch (ContextRendererException e) {}
		catch (Exception e)
		{
			fail("Non-specified exception thrown by CCR. - " + e.getClass().toString());
		}*/
		try {
		
			ContextXMLRenderer ccr = new ContextXMLRenderer();
			ccr.endContext(null, "whee");
			fail("Did not throw exception on null context.");			
		}
		catch (IllegalArgumentException e) {}
		catch (Exception e)
		{
			fail("Non-specified exception thrown by CCR. - " + e.getClass().toString());
		}
		try {
		
			ContextXMLRenderer ccr = new ContextXMLRenderer();
			ccr.endContext("funk.whee", null);
			fail("Did not throw exception on null relative ctx.");			
		}
		catch (IllegalArgumentException e) {}
		catch (Exception e)
		{
			fail("Non-specified exception thrown by CCR. - " + e.getClass().toString());
		}

	}
	
	public void testBindingFound()
	{
		try {
		
			ContextXMLRenderer ccr = new ContextXMLRenderer();
			ccr.bindingFound("somenonsense.whee", "whee");
			fail("Did not throw exception on false context.");			
		}
		catch (ContextRendererException e) {}
		catch (Exception e)
		{
			fail("Non-specified exception thrown by CCR. - " + e.getClass().toString());
		}
		try {
		
			ContextXMLRenderer ccr = new ContextXMLRenderer();
			ccr.bindingFound(null, "whee");
			fail("Did not throw exception on null context.");			
		}
		catch (IllegalArgumentException e) {}
		catch (Exception e)
		{
			fail("Non-specified exception thrown by CCR. - " + e.getClass().toString());
		}
		try {
		
			ContextXMLRenderer ccr = new ContextXMLRenderer();
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
