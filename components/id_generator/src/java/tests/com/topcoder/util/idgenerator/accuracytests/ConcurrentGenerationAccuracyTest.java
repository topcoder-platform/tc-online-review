/**
 * Copyright (C) 2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.idgenerator.accuracytests;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Arrays;

import com.topcoder.db.connectionfactory.*;


import junit.framework.TestCase;

import com.topcoder.util.idgenerator.IDGenerator;
import com.topcoder.util.idgenerator.IDGeneratorFactory;
import com.topcoder.util.idgenerator.IDGeneratorImpl;



/**
 * ConcurrentGenerationAccuracyTest 
 * 
 * @author AdamSelene
 *
 * This class tests the concurrent use of the IDGenerator on a single JVM.
 * 
 * Three threads will be created to begin, two using the IDGeneratorFactory, and then
 * one without.
 * 
 * Three more threads will be created 5 seconds later, with similar methods.
 * 
 * All of these threads will claim 3 blocks worth of IDs.  Block size and sequence start will
 * all be varied.   
 * 
 * This should simulate both same-JVM and multiple-JVM safety (use of the Factory only instantiates
 * one instance of the IDGenerator (same-JVM), while the direct instantiation will act as if it were
 * in a separate JVM, in terms of synchronization.) 
 * 
 */
public final class ConcurrentGenerationAccuracyTest  extends TestCase  {

	private Connection myControlConnection = null;

	private static final String CUSTOM_NAME = "AT_ConGenerator";

	private static final long FAR_NEG = -1000000000;
	private static final long NEAR_NEG = -10;
	private static final int BLOCKS = 3;
	
	private static final boolean TEST_DEBUG_OUTPUT = false;
	

	public void testGenerateSmallBlock6() throws Exception
	{
		generateIDs(CUSTOM_NAME + "Small Block", 0,3);
	}

	public void testGenerateMediumBlock6() throws Exception
	{
		generateIDs(CUSTOM_NAME + "Medium Block", 0,100);
	}
		
	public void testGenerateLargeBlock6() throws Exception
	{
		generateIDs(CUSTOM_NAME + "Large Block", 0,1000);
	}

	public void testGenerateSmallNegBlock6() throws Exception
	{
		generateIDs(CUSTOM_NAME + "Small NBlock", FAR_NEG,3);
	}

	public void testGenerateMediumNegBlock6() throws Exception
	{
		generateIDs(CUSTOM_NAME + "Medium NBlock", FAR_NEG,100);
	}
	
	public void testGenerateLargeNegBlock6() throws Exception
	{
		generateIDs(CUSTOM_NAME + "Large NBlock", FAR_NEG,1000);
	}
	
	public void testGenerateSmallCrossBlock6() throws Exception
	{
		generateIDs(CUSTOM_NAME + "Small CBlock", NEAR_NEG,3);
	}

	public void testGenerateMediumCrossBlock6() throws Exception
	{
		generateIDs(CUSTOM_NAME + "Medium CBlock", NEAR_NEG,100);
	}
	
	public void testGenerateLargeCrossBlock6() throws Exception
	{
		generateIDs(CUSTOM_NAME + "Large CBlock", NEAR_NEG,1000);
	}
	
	public void generateIDs(String testid, long next_block_start, int block_size) throws Exception
	{
		try {		
			int i;
			boolean sentinel = false;
			PreparedStatement ps;
			ps = myControlConnection.prepareStatement("INSERT INTO id_sequences (name, next_block_start, block_size) VALUES ('" + testid + "', " + next_block_start +","+ block_size + ");");
			assertFalse("Could not insert to table.", ps.execute());
			
			IDGThread[] tds = new IDGThread[6];
			
			tds[0] = new IDGThread(IDGeneratorFactory.getIDGenerator(testid),block_size*BLOCKS);
			tds[1] = new IDGThread(IDGeneratorFactory.getIDGenerator(testid),block_size*BLOCKS);
			tds[2] = new IDGThread(new IDGeneratorImpl(testid),block_size*BLOCKS);
			
			Thread.sleep(5000);

			tds[3] = new IDGThread(IDGeneratorFactory.getIDGenerator(testid),block_size*BLOCKS);
			tds[4] = new IDGThread(IDGeneratorFactory.getIDGenerator(testid),block_size*BLOCKS);
			tds[5] = new IDGThread(new IDGeneratorImpl(testid),block_size*BLOCKS);
			
			for (int src = 0; src < 6; src++)
			{
				tds[src].start();
			}					
			
			while (!sentinel)
			{
				sentinel = true;
				Thread.sleep(100);
				for (i = 0; i < 6; i++)
				{	
					sentinel = sentinel && tds[i].finished();
				}				
			}
			
			long[] search = new long[block_size*BLOCKS*6];			
			if (TEST_DEBUG_OUTPUT)
			{
				System.out.println();
				System.out.println(testid + "\n------");	
			}
			
			for(int src = 0; src < 6; src++)
			{
				if (tds[src].exp!=null)
				{
					fail("Exception during concurrent generation (" + testid + ") - " + tds[src].exp.getMessage());
				}
				assertEquals("Did not finish generation, but no exception?", block_size*BLOCKS,tds[src].count);
				
				System.arraycopy(tds[src].generated,0,search,block_size*BLOCKS*src,block_size*BLOCKS);

				if (TEST_DEBUG_OUTPUT)
				{	
					System.out.println("["+tds[src].idg.getIDName() + "] (" + src + ")\n------");
					for (int x = 0; x < block_size*BLOCKS; x++)
					{
						System.out.print(" " + tds[src].generated[x]);
					}
					System.out.println();
				}
			}
			
			Arrays.sort(search);

			if (TEST_DEBUG_OUTPUT)
			{	
				System.out.println("------");
			}
			
			for (i = 0; i < search.length-1; i++)
			{
				if (TEST_DEBUG_OUTPUT)
				{
					System.out.print(" " + search[i]);
				}

				if (search[i]==search[i+1])
				{
					 fail("Duplicate key found (" + search[i] +")");
				}				
			}
					
			ps = myControlConnection.prepareStatement("DELETE FROM id_sequences WHERE name='" + testid + "';");
			assertFalse("Could not clean table.", ps.execute());
			
		} catch (SQLException e){
			throw new Exception("Unexpected SQLException.", e);
		}		
	}
	
	/**
	 * Creates control connection to ensure database has proper
	 * data in table.
	 *  
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		
//		Context ctx = new InitialContext();
//		DataSource dataSource = (DataSource) ctx.lookup("java:comp/env/jdbc/com/topcoder/util/idgenerator/IDGeneratorDataSource");
//		myControlConnection = dataSource.getConnection();
        myControlConnection = (new DBConnectionFactoryImpl("com.topcoder.db.connectionfactory.DBConnectionFactoryImpl")).createConnection();
		
		myControlConnection.setAutoCommit(true);		
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		myControlConnection.prepareStatement("DELETE FROM id_sequences WHERE name LIKE 'AT_%';").execute()	;
	}

	private void verifyTableData(String testid, String errLabel, long expected_nbs, int expected_bs) throws SQLException
	{
		long new_nbs = -1;
		int new_bs = -1;
		
		ResultSet rs1 = myControlConnection.prepareStatement("SELECT next_block_start, block_size FROM id_sequences WHERE name = '"+testid+"';").executeQuery();
		
		assertTrue("Could not select from table.", rs1.next());
		
		new_nbs = rs1.getLong("next_block_start");
		new_bs = rs1.getInt("block_size");
		
		assertEquals(errLabel + " - block_size unexpected.", expected_bs, new_bs);
		
		assertEquals(errLabel + " - next_block_start unexpected.", expected_nbs, new_nbs);
	}
}

final class IDGThread extends Thread
{
	private boolean bFinished=false;
	public IDGenerator idg=null;
	public int count=-1;
	public long[] generated=null;
	public Exception exp = null;
	
	public IDGThread(IDGenerator inIDG, int inCount)
	{
		idg = inIDG;
		count = inCount;
		
		generated = new long[inCount];		
	}
	
	public void run()
	{
		int gen = -1;
		
		try {
			for (gen = 0; gen < count; gen++)
			{
				generated[gen] = idg.getNextID();
			}
		} catch(Exception e) { exp = e; count = gen; }
		
		setFinished();
	}
	
	public synchronized boolean finished()
	{
		return bFinished;
	}
	
	private synchronized void setFinished()
	{
		bFinished = true;
	}
}