/**
 * Copyright © 2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.naming.jndiutility.failuretests;

import com.topcoder.naming.jndiutility.JNDIUtils;

import java.sql.SQLException;

import javax.naming.CompositeName;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author AdamSelene
 *
 * 
 */
public class JNDIUtilsTest extends TestCase {

	public static Test suite() {
		return new TestSuite(JNDIUtilsTest.class);
	}
	
	protected void setUp()
	{
	/*	try {
			ConfigManager cm = ConfigManager.getInstance();
			
			cm.add("test_files/com/topcoder/naming/jndiutility/JNDIUtils.properties");
			
			
			Properties props;
			
			props = new Properties();
			props.put("factory", "com.sun.jndi.fscontext.RefFSContextFactory");
			props.put("url", "file://.\\test_files");
			JNDIUtils.saveContextConfig("test_context", props);
			
//			Context ctx = JNDIUtils.getltContext();
//			JNDIUtils.createSubcontext(ctx, "com/topcoder/naming/jndiutility");
		} catch (ConfigManagerException e)
		{
			System.err.println(e.getMessage());
		} //catch (NamingException e)
//		{
//			System.err.println(e.getMessage());
//		}
 */
	}
	
	public void testCreateName()
	{
		try {
			JNDIUtils.createName((String)null, '\t');
			fail("Did not catch null name.");
		} catch (NamingException e) {
			fail("Threw an InvalidNameException rather than IllegalArgumentException per design.");
		} catch (IllegalArgumentException e) { /* Expected */ }
/*		
		try{
			JNDIUtils.createName("0093@#$jasoijf",'#');
			fail("Did not catch invalid name. (Garbage)");			
		} catch (NamingException e) 
		{
		    // Expected  
		}
		
		try {
			JNDIUtils.createName("whate\rver\ndoe\0sever", '\t');
			fail("Did not catch invalid name (lbs/nulls)");
		} catch (NamingException e) 
		{ 
		    //  Expected 
		}
*/
	}
	
	public void testCreateString()
	{
		try{
			JNDIUtils.createString((CompositeName)null,'#');
			fail("Did not catch null name.");			
		} catch (IllegalArgumentException e) { /* Expected */ }
	}	
			
	public void testCreateSubcontext()
	{
		//try {
		//	JNDIUtils.createSubcontext(new InitialDirContext(),"test");
		//	fail("Exception should fire.");
		//} catch (NamingException e) { /* Expected */ }	
		
		try {
			JNDIUtils.createSubcontext(null,"test");
			fail("Exception should fire on null context.");
		} catch (NamingException e) {
			fail("Wrong fire on null context.");
		} catch (IllegalArgumentException e) { /* Expected */ }
		
		try {
			JNDIUtils.createSubcontext(new InitialDirContext(),(String)null);
			fail("Exception should fire on null context.");
		} catch (NamingException e) {
			fail("Wrong fire on null string.");
		} catch (IllegalArgumentException e) { /* Expected */ }		
		
		try {
			JNDIUtils.createSubcontext(null,new CompositeName());
			fail("Exception should fire on null context (+Name).");
		} catch (NamingException e) {
			fail("Wrong fire on null context.");
		} catch (IllegalArgumentException e) { /* Expected */ }
		
		try {
			JNDIUtils.createSubcontext(new InitialDirContext(),(CompositeName)null);
			fail("Exception should fire on null context.");
		} catch (NamingException e) {
			fail("Wrong fire on null string.");
		} catch (IllegalArgumentException e) { /* Expected */ }								
	}
	
	public void testGetObject()
	{
		try {
			JNDIUtils.getObject("asfdjakl;pdfbj203j092430ASFD!#@$)!)");
			fail("Should raise Exception");
		} catch (NamingException e) {/*expected*/}
		
		try {
			JNDIUtils.getObject((String)null);
			fail("Should raise Exception on null");
		} catch (NamingException e) { 
			fail("Wrong Exception");
		} catch (IllegalArgumentException e) { /* Expected */ }
		
		try {
			JNDIUtils.getObject((CompositeName)null);
		fail("Should raise Exception on null Name");
		} catch (NamingException e) { 
			fail("Wrong Exception1");
		} catch (IllegalArgumentException e) { /* Expected */ }
		
		try {
			JNDIUtils.getObject("Nonexistant object", JNDIUtils.class);		
			fail("Object doesn't exist.");
		} catch (NamingException e) {}
		
		try {
			JNDIUtils.getObject((String)null, JNDIUtils.class);
		} catch (NamingException e) {
			fail("Wrong exception2");
		} catch (IllegalArgumentException e) { /*Expected*/ }

		try {
			JNDIUtils.getObject("Nonexistant", null);
		} catch (NamingException e) {
			fail("Wrong exception3");
		} catch (IllegalArgumentException e) { /*Expected*/ }
		
		try {
			JNDIUtils.getObject((String)null, null);
		} catch (NamingException e) {
			fail("Wrong exception on both null (String, class)");
		} catch (IllegalArgumentException e) { /*Expected*/ }

		try {
			JNDIUtils.getObject((CompositeName)null, JNDIUtils.class);
		} catch (NamingException e) {
			fail("Wrong exception on first null (Name, Class)");
		} catch (IllegalArgumentException e) { /*Expected*/ }

		try {
			JNDIUtils.getObject(new CompositeName(), null);
		} catch (NamingException e) {
			fail("Wrong exception on second null (Name, Class)");
		} catch (IllegalArgumentException e) { /*Expected*/ }
		
		try {
			JNDIUtils.getObject((CompositeName)null, null);
		} catch (NamingException e) {
			fail("Wrong exception on both null (Name, Class)");
		} catch (IllegalArgumentException e) { /*Expected*/ }				
		
		try {
			JNDIUtils.getObject((Context)null, "blah");
		} catch (NamingException e) {
			fail("Wrong exception on first null (Context, String)");
		} catch (IllegalArgumentException e) { /*Expected*/ }

		try {
			JNDIUtils.getObject(new InitialDirContext(), (String)null);
		} catch (NamingException e) {
			fail("Wrong exception on second null (Context, String)");
		} catch (IllegalArgumentException e) { /*Expected*/ }
		
		try {
			JNDIUtils.getObject((Context)null, (String)null);
		} catch (NamingException e) {
			fail("Wrong exception on both null (Context, String)");
		} catch (IllegalArgumentException e) { /*Expected*/ }				

		try {
			JNDIUtils.getObject((Context)null, new CompositeName());
		} catch (NamingException e) {
			fail("Wrong exception on first null (Context, Name)");
		} catch (IllegalArgumentException e) { /*Expected*/ }

		try {
			JNDIUtils.getObject(new InitialDirContext(), (CompositeName)null);
		} catch (NamingException e) {
			fail("Wrong exception on second null (Context, Name)");
		} catch (IllegalArgumentException e) { /*Expected*/ }
		
		try {
			JNDIUtils.getObject((Context)null, (CompositeName)null);
		} catch (NamingException e) {
			fail("Wrong exception on both null (Context, Name)");
		} catch (IllegalArgumentException e) { /*Expected*/ }				

		try {
			JNDIUtils.getObject((Context)null, "", JNDIUtils.class);
		} catch (NamingException e) {
			fail("Wrong exception on first null (Context, String, Class)");
		} catch (IllegalArgumentException e) { /*Expected*/ }

		try {
			JNDIUtils.getObject(new InitialDirContext(), (String)null, JNDIUtils.class);
		} catch (NamingException e) {
			fail("Wrong exception on second null (Context, String, Class)");
		} catch (IllegalArgumentException e) { /*Expected*/ }
		
		try {
			JNDIUtils.getObject(new InitialDirContext(), "", null);
		} catch (NamingException e) {
			fail("Wrong exception on third null (Context, String, Class)");
		} catch (IllegalArgumentException e) { /*Expected*/ }
		
		try {
			JNDIUtils.getObject((Context)null, (String)null, null);
		} catch (NamingException e) {
			fail("Wrong exception on both null (Context, String, Class)");
		} catch (IllegalArgumentException e) { /*Expected*/ }				

		try {
			JNDIUtils.getObject((Context)null, new CompositeName(), JNDIUtils.class);
		} catch (NamingException e) {
			fail("Wrong exception on first null (Context, Name, Class)");
		} catch (IllegalArgumentException e) { /*Expected*/ }

		try {
			JNDIUtils.getObject(new InitialDirContext(), (CompositeName)null, JNDIUtils.class);
		} catch (NamingException e) {
			fail("Wrong exception on second null (Context, Name, Class)");
		} catch (IllegalArgumentException e) { /*Expected*/ }
		
		try {
			JNDIUtils.getObject(new InitialDirContext(), new CompositeName(), null);
		} catch (NamingException e) {
			fail("Wrong exception on third null (Context, Name, Class)");
		} catch (IllegalArgumentException e) { /*Expected*/ }
		
		try {
			JNDIUtils.getObject((Context)null, (CompositeName)null, null);
		} catch (NamingException e) {
			fail("Wrong exception on both null (Context, String, Class)");
		} catch (IllegalArgumentException e) { /*Expected*/ }											
	}
	
	public void testGetQueue()
	{
		try {
			JNDIUtils.getQueue(new InitialDirContext(),"funk");
			fail("Should have failed. (Context,String)");
		} catch (NamingException e) {}

		try {
			JNDIUtils.getQueue(null,"funk");
			fail("Should have failed. n1(Context,String)");
		} catch (NamingException e) { 
			fail("Wrong Exception Raised on null1 (Context,String)");
		} catch (IllegalArgumentException e) {}

		try {
			JNDIUtils.getQueue(new InitialDirContext(),(String)null);
			fail("Should have failed. n2(Context,String)");
		} catch (NamingException e) { 
			fail("Wrong Exception Raised on null2 (Context,String)");
		} catch (IllegalArgumentException e) {}

		try {
			JNDIUtils.getQueue(null,(String)null);
			fail("Should have failed. na(Context,String)");
		} catch (NamingException e) { 
			fail("Wrong Exception Raised on all (Context,String)");
		} catch (IllegalArgumentException e) {}
		
		try {
			JNDIUtils.getQueue(new InitialDirContext(),new CompositeName());
			fail("Should have failed (Context,Name).");
		} catch (NamingException e) {}

		try {
			JNDIUtils.getQueue(new InitialDirContext(),(CompositeName)null);
			fail("Should have failed. n2(Context,Name)");
		} catch (NamingException e) { 
			fail("Wrong Exception Raised on null2 (Context,Name)");
		} catch (IllegalArgumentException e) {}

		try {
			JNDIUtils.getQueue(null,new CompositeName());
			fail("Should have failed. n1(Context,Name)");
		} catch (NamingException e) { 
			fail("Wrong Exception Raised on n1 (Context,Name");
		} catch (IllegalArgumentException e) {}

		try {
			JNDIUtils.getQueue(null,(CompositeName)null);
			fail("Should have failed. na(Context,Name)");
		} catch (NamingException e) { 
			fail("Wrong Exception Raised on all (Context,Name)");
		} catch (IllegalArgumentException e) {}			
	}

	public void testGetTopic()
	{
		try {
			JNDIUtils.getTopic(new InitialDirContext(),"funk");
			fail("Should have failed. (Context,String)");
		} catch (NamingException e) {}

		try {
			JNDIUtils.getTopic(null,"funk");
			fail("Should have failed. n1(Context,String)");
		} catch (NamingException e) { 
			fail("Wrong Exception Raised on null1 (Context,String)");
		} catch (IllegalArgumentException e) {}

		try {
			JNDIUtils.getTopic(new InitialDirContext(),(String)null);
			fail("Should have failed. n2(Context,String)");
		} catch (NamingException e) { 
			fail("Wrong Exception Raised on null2 (Context,String)");
		} catch (IllegalArgumentException e) {}

		try {
			JNDIUtils.getTopic(null,(String)null);
			fail("Should have failed. na(Context,String)");
		} catch (NamingException e) { 
			fail("Wrong Exception Raised on all (Context,String)");
		} catch (IllegalArgumentException e) {}
		
		try {
			JNDIUtils.getTopic(new InitialDirContext(),new CompositeName());
			fail("Should have failed (Context,Name).");
		} catch (NamingException e) {}

		try {
			JNDIUtils.getTopic(new InitialDirContext(),(CompositeName)null);
			fail("Should have failed. n2(Context,Name)");
		} catch (NamingException e) { 
			fail("Wrong Exception Raised on null2 (Context,Name)");
		} catch (IllegalArgumentException e) {}

		try {
			JNDIUtils.getTopic(null,new CompositeName());
			fail("Should have failed. n1(Context,Name)");
		} catch (NamingException e) { 
			fail("Wrong Exception Raised on n1 (Context,Name");
		} catch (IllegalArgumentException e) {}

		try {
			JNDIUtils.getTopic(null,(CompositeName)null);
			fail("Should have failed. na(Context,Name)");
		} catch (NamingException e) { 
			fail("Wrong Exception Raised on all (Context,Name)");
		} catch (IllegalArgumentException e) {}			
	}

	public void testGetConnection()
	{
		try {
			JNDIUtils.getConnection(new InitialDirContext(),"funk");
			fail("Should have failed. (Context,String)");
		} catch (NamingException e) {}
		catch (SQLException e) { fail("Wrong exception (Context,String)"); }

		try {
			JNDIUtils.getConnection(null,"funk");
			fail("Should have failed. n1(Context,String)");
		} catch (NamingException e) { 
			fail("Wrong Exception Raised on null1 (Context,String)");
		} catch (IllegalArgumentException e) {}
		catch (SQLException e) { fail("Wrong exception on n1 (Context,String)"); }
		try {
			JNDIUtils.getConnection(new InitialDirContext(),(String)null);
			fail("Should have failed. n2(Context,String)");
		} catch (NamingException e) { 
			fail("Wrong Exception Raised on null2 (Context,String)");
		} catch (IllegalArgumentException e) {}
		catch (SQLException e) { fail("Wrong exception on n2(Context,String)"); }

		try {
			JNDIUtils.getConnection(null,(String)null);
			fail("Should have failed. na(Context,String)");
		} catch (NamingException e) { 
			fail("Wrong Exception Raised on all (Context,String)");
		} catch (IllegalArgumentException e) {}
		catch (SQLException e) { fail("Wrong exception na (Context,String)"); }
		
		try {
			JNDIUtils.getConnection(new InitialDirContext(),new CompositeName());
			fail("Should have failed (Context,Name).");
		} catch (NamingException e) {}
		catch (SQLException e) { fail("Wrong exception (Context,Name)"); }

		try {
			JNDIUtils.getConnection(new InitialDirContext(),(CompositeName)null);
			fail("Should have failed. n2(Context,Name)");
		} catch (NamingException e) { 
			fail("Wrong Exception Raised on null2 (Context,Name)");
		} catch (IllegalArgumentException e) {}
		catch (SQLException e) { fail("Wrong exception n2(Context,Name)"); }

		try {
			JNDIUtils.getConnection(null,new CompositeName());
			fail("Should have failed. n1(Context,Name)");
		} catch (NamingException e) { 
			fail("Wrong Exception Raised on n1 (Context,Name");
		} catch (IllegalArgumentException e) {}
		catch (SQLException e) { fail("Wrong exception n1(Context,Name)"); }

		try {
			JNDIUtils.getConnection(null,(CompositeName)null);
			fail("Should have failed. na(Context,Name)");
		} catch (NamingException e) { 
			fail("Wrong Exception Raised on all (Context,Name)");
		} catch (IllegalArgumentException e) {}			
		catch (SQLException e) { fail("Wrong exception na(Context,Name)"); }
		
		try {
			JNDIUtils.getConnection((CompositeName)null);
			fail("Should have failed. na(Name)");
		} catch (NamingException e) { 
			fail("Wrong Exception Raised on all (Name)");
		} catch (IllegalArgumentException e) {}			
		catch (SQLException e) { fail("Wrong exception na(Name)"); }

		try {
			JNDIUtils.getConnection((String)null);
			fail("Should have failed. na(String)");
		} catch (NamingException e) { 
			fail("Wrong Exception Raised on all (String)");
		} catch (IllegalArgumentException e) {}			
		catch (SQLException e) { fail("Wrong exception na(String)"); }

				
	}

	
}
