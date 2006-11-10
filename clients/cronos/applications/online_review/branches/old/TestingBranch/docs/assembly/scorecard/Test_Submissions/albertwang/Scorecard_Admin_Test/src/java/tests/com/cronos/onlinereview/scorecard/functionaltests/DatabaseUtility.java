/*
 * Copyright (C) 2006 TopCoder, Inc. All Rights Reserved.
 */
package com.cronos.onlinereview.scorecard.functionaltests;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.management.scorecard.ScorecardManager;
import com.topcoder.management.scorecard.ScorecardManagerImpl;

/**
 * The utility class to access application database.
 *
 * @author TCSTESTER
 * @version 1.0
 */
public class DatabaseUtility {

	/**
	 * To avoid overhead DatabaseUtility is kept as a singleton.
	 */
	private static DatabaseUtility instance = null;

	/**
	 * The configuration interface.
	 */
	private static Configuration config = new Configuration(
			DatabaseUtility.class.getName());

	/**
	 * The database connection. In order to avoid overhead without connection pooling, an open connection
	 * is kept with the functional test cases. There will not be any threading issue between the test cases
	 * are all executed in a single thread.
	 */
	private Connection conn = null;

	/**
	 * The ScorecardManager instance.
	 */
	private ScorecardManager scorecardManager = null;

	/**
	 * Constructor. Establishes the connection here.
	 *
	 * @throws Exception if connection can not be established.
	 */
	public DatabaseUtility() throws Exception {
		conn = new DBConnectionFactoryImpl(config
				.getProperty("connection_factory_namespace"))
				.createConnection(config.getProperty("connection_name"));
		conn.setAutoCommit(true);
		scorecardManager = new ScorecardManagerImpl();
	}

	/**
	 * Get the singleton instance of the utility.
	 *
	 * @return the singleton instance.
	 *
	 * @return Exception if instance can not be created for the first time.
	 */
	public static DatabaseUtility getInstance() throws Exception {
		if (instance == null) {
			instance = new DatabaseUtility();
		}
		return instance;
	}

	/**
	 * Clear all tables. Notice that only the "dynamic" ones are cleared.
	 *
	 * @throws Exception if tables can not be cleared.
	 */
	public void clearTables() throws Exception {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("DELETE from scorecard_question WHERE NOT cast(scorecard_question_id as varchar(10)) like '88%'");
			ps.executeUpdate();
			ps = conn.prepareStatement("DELETE from scorecard_section WHERE NOT cast(scorecard_section_id as varchar(10)) like '88%'");
			ps.executeUpdate();
			ps = conn.prepareStatement("DELETE from scorecard_group WHERE NOT cast(scorecard_group_id as varchar(10)) like '88%'");
			ps.executeUpdate();
			ps = conn.prepareStatement("DELETE from scorecard WHERE NOT cast(scorecard_id as varchar(10)) like '88%'");
			ps.executeUpdate();
		} finally {
			ps.close();
		}
	}

	/**
	 * Return the ScorecardManager instance.
	 * @return the scorecard manager
	 */
	public ScorecardManager getScorecardManager() {
		return this.scorecardManager;
	}
}
