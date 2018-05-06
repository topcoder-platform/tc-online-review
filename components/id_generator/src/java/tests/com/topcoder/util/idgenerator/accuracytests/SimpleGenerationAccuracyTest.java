/**
 * Copyright (C) 2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.idgenerator.accuracytests;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.topcoder.db.connectionfactory.*;


import junit.framework.TestCase;

import com.topcoder.util.idgenerator.IDGenerator;
import com.topcoder.util.idgenerator.IDGeneratorFactory;



/**
 * SimpleGenerationAccuracyTest 
 * 
 * @author AdamSelene
 *
 * This class tests the simple case of ID generation.  One client, one sequence.
 * 
 * This would be encountered in most standalone applications which use this component for artificial
 * primary key generation.
 * 
 */
public final class SimpleGenerationAccuracyTest  extends TestCase  {

	private Connection myControlConnection = null;

	private static final String CUSTOM_NAME = "AT_SimpleGenerator";

	private static final long FAR_NEG = -1000000000;
	private static final long NEAR_NEG = -10;
	

	public void testGenerateSmallBlock() throws Exception
	{
		generateIDs(CUSTOM_NAME + "Small Block", 0,3);
	}

	public void testGenerateMediumBlock() throws Exception
	{
		generateIDs(CUSTOM_NAME + "Medium Block", 0,100);
	}
		
	public void testGenerateLargeBlock() throws Exception
	{
		generateIDs(CUSTOM_NAME + "Large Block", 0,1000);
	}

	public void testGenerateSmallNegBlock() throws Exception
	{
		generateIDs(CUSTOM_NAME + "Small NBlock", FAR_NEG,3);
	}

	public void testGenerateMediumNegBlock() throws Exception
	{
		generateIDs(CUSTOM_NAME + "Medium NBlock", FAR_NEG,100);
	}
	
	public void testGenerateLargeNegBlock() throws Exception
	{
		generateIDs(CUSTOM_NAME + "Large NBlock", FAR_NEG,1000);
	}
	
	public void testGenerateSmallCrossBlock() throws Exception
	{
		generateIDs(CUSTOM_NAME + "Small CBlock", NEAR_NEG,3);
	}

	public void testGenerateMediumCrossBlock() throws Exception
	{
		generateIDs(CUSTOM_NAME + "Medium CBlock", NEAR_NEG,100);
	}
	
	public void testGenerateLargeCrossBlock() throws Exception
	{
		generateIDs(CUSTOM_NAME + "Large CBlock", NEAR_NEG,1000);
	}
	
	public void generateIDs(String testid, long next_block_start, int block_size) throws Exception
	{
		try {	
			IDGenerator idg = null;			
			PreparedStatement ps;
			
			long lastid = next_block_start + block_size * 10;
			long i;
						
			ps = myControlConnection.prepareStatement("INSERT INTO id_sequences (name, next_block_start, block_size) VALUES ('" + testid + "', " + next_block_start +","+ block_size + ");");
			assertFalse("Could not insert to table.", ps.execute());
			
			idg = IDGeneratorFactory.getIDGenerator(testid);
			
			for (i = next_block_start; i < lastid; i++)
			{
				assertEquals(testid + " Main loop #" + i,i, idg.getNextID());
			}			
			
			verifyTableData(testid, testid + " check",lastid,block_size);
			
			assertEquals(testid + " post loop #" + i,i++,idg.getNextID());
			
			verifyTableData(testid, testid + " check2",lastid+block_size, block_size);
			
			if (block_size>1)
			{
				assertEquals(testid + " post loop " + i,i++,idg.getNextID());
				
				verifyTableData(testid, testid + " check2",lastid+block_size, block_size);				
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
        myControlConnection =
            (new DBConnectionFactoryImpl("com.topcoder.db.connectionfactory.DBConnectionFactoryImpl")).createConnection();
		
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
