/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration;

import java.sql.Connection;

import com.topcoder.dde.util.DWLoad.RatingQubits;

/**
 * The test of RatingQubits.
 *
 * @author brain_cn
 * @version 1.0
 */
public class RatingQubitsUnitTests extends BaseDataTestCases {
	private Connection conn = null;

	/**
	 * Test method for 'runAllScores(Connection, String)'
	 */
	public void testRunAllScores() throws Exception {
		this.conn = this.getJDBCConnection();
		RatingQubits ratingQubits = new RatingQubits();
		ratingQubits.runAllScores(conn, "50");
	}
}
