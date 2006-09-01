/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration;

import java.sql.Connection;

import com.topcoder.dde.util.DWLoad.ReliabilityRating;

/**
 * The test of ReliabilityRating.
 *
 * @author brain_cn
 * @version 1.0
 */
public class ReliabilityRatingUnitTests extends BaseDataTestCases {
	private Connection conn = null;

	/**
	 * Test method for 'updateReliability(Connection, Set, int, long)'
	 */
	public void testUpdateReliability() throws Exception {
		this.conn = this.getJDBCConnection();
		ReliabilityRating ratingQubits = new ReliabilityRating();
		int historyLenght = 15;
		ratingQubits.caculateRilability(conn, historyLenght);
	}
}
