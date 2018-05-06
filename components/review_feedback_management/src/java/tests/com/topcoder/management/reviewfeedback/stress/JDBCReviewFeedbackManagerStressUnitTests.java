/*
 * Copyright (C) 2012 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.reviewfeedback.stress;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;

import junit.framework.JUnit4TestAdapter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.persistence.XMLFilePersistence;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.management.reviewfeedback.ReviewFeedback;
import com.topcoder.management.reviewfeedback.ReviewFeedbackDetail;
import com.topcoder.management.reviewfeedback.ReviewFeedbackManagementException;
import com.topcoder.management.reviewfeedback.impl.JDBCReviewFeedbackManager;

/**
 * Stress tests for {@link JDBCReviewFeedbackManager} class.
 * 
 * @author TCSDEVELOPER, lqz
 * @version 1.0
 */
public class JDBCReviewFeedbackManagerStressUnitTests {
	/**
	 * Represents the run times.
	 */
	private static final int TIMES = 10;
	/**
	 * Represents the threads.
	 */
	private static final int THREADS = 20;
	/**
	 * Represents the number 100.
	 */
	private static final int HUNDRED = 100;

	private static final int DETAIL_COUNT_PER_FEEDBACK = 10;
	/**
	 * Represents the random object.
	 */
	private final Random rand = new Random();
	/**
	 * Represents the feed backs.
	 */
	private ReviewFeedback[] feedbacks;
	/**
	 * Represents the <code>JDBCReviewFeedbackManager</code> instance used in
	 * tests.
	 */
	private JDBCReviewFeedbackManager instance;
	/**
	 * Represents the connection used in tests.
	 */
	private Connection connection;

	/**
	 * Adapter for earlier versions of JUnit.
	 * 
	 * @return a test suite.
	 */
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(
				JDBCReviewFeedbackManagerStressUnitTests.class);
	}

	/**
	 * Sets up the unit tests.
	 * 
	 * @throws Exception
	 *             to JUnit.
	 */
	@Before
	public void setUp() throws Exception {
		feedbacks = new ReviewFeedback[TIMES];
		for (int i = 0; i < feedbacks.length; ++i) {
			feedbacks[i] = new ReviewFeedback();
			feedbacks[i].setProjectId(1);
			List<ReviewFeedbackDetail> list = new ArrayList<ReviewFeedbackDetail>();
			for (int j = 0; j < DETAIL_COUNT_PER_FEEDBACK; j++) {
				ReviewFeedbackDetail detail = new ReviewFeedbackDetail();
				detail.setReviewerUserId(j);
				detail.setScore(rand.nextInt(HUNDRED));
				detail.setFeedbackText("text " + i + " " + j);
				list.add(detail);
			}
			feedbacks[i].setDetails(list);
			feedbacks[i].setComment("comment " + i);
			feedbacks[i].setCreateDate(new Date());
			feedbacks[i].setCreateUser(String.valueOf(rand.nextInt(HUNDRED)));
		}
		instance = new JDBCReviewFeedbackManager(getConfig());
		connection = getConnection();
		executeSQL(connection, "test_files/stress/clear.sql");
		executeSQL(connection, "test_files/stress/data.sql");
	}

	/**
	 * Cleans up the unit tests.
	 * 
	 * @throws Exception
	 *             to JUnit.
	 */
	@After
	public void tearDown() throws Exception {
		for (int i = 0; i < feedbacks.length; ++i) {
			feedbacks[i] = null;
		}
		feedbacks = null;
		executeSQL(connection, "test_files/stress/clear.sql");
		connection.close();
		connection = null;
	}

	/**
	 * Stress test for {@link JDBCReviewFeedbackManager#create(ReviewFeedback)}.
	 * 
	 * @throws Exception
	 *             to JUnit.
	 */
	@Test
	public void test_create() throws Exception {
		long t = System.currentTimeMillis();

		for (int i = 0; i < TIMES; ++i) {
			instance.create(feedbacks[i], "OP1");
		}
		t = System.currentTimeMillis() - t;
		System.out.println("Run create() for " + TIMES + " times, taking " + t
				+ "ms.");

		for (int i = 0; i < TIMES; ++i) {
			ReviewFeedback other = instance.get(feedbacks[i].getId());
			assertEqual(feedbacks[i], other);
		}
	}

	/**
	 * Stress test for {@link JDBCReviewFeedbackManager#delete(long)}.
	 * 
	 * @throws Exception
	 *             to JUnit.
	 */
	@Test
	public void test_delete() throws Exception {
		for (int i = 0; i < TIMES; ++i) {
			instance.create(feedbacks[i], "OP1");
			assertNotNull("The result should be correct.",
					instance.get(feedbacks[i].getId()));
		}

		long t = System.currentTimeMillis();
		for (int i = 0; i < TIMES; ++i) {
			instance.delete(feedbacks[i].getId());
			assertNull("The result should be correct.",
					instance.get(feedbacks[i].getId()));
		}
		t = System.currentTimeMillis() - t;
		System.out.println("Run delete() for " + TIMES + " times, taking " + t
				+ "ms.");
	}

	/**
	 * Stress test for {@link JDBCReviewFeedbackManager#update(ReviewFeedback)}.
	 * 
	 * @throws Exception
	 *             to JUnit.
	 */
	@Test
	public void test_update() throws Exception {
		for (int i = 0; i < TIMES; ++i) {
			instance.create(feedbacks[i], "OP1");
			assertNotNull("The result should be correct.",
					instance.get(feedbacks[i].getId()));
		}

		long t = System.currentTimeMillis();
		for (int i = 0; i < TIMES; ++i) {
			for (int j = 0; j < DETAIL_COUNT_PER_FEEDBACK; j++) {
				ReviewFeedbackDetail detail = feedbacks[i].getDetails().get(j);
				detail.setReviewerUserId(j + 20);
				detail.setScore(rand.nextInt(HUNDRED));
				detail.setFeedbackText("newtext " + i + " " + j);
			}

			// feedbacks[i].setCreateUser(rand.nextInt(HUNDRED) + "");
			feedbacks[i].setComment("new comment " + i);
			instance.update(feedbacks[i], "op2");
			ReviewFeedback other = instance.get(feedbacks[i].getId());
			assertEqual(feedbacks[i], other);
		}
		t = System.currentTimeMillis() - t;
		System.out.println("Run update() for " + TIMES + " times, taking " + t
				+ "ms.");
	}

	/**
	 * Stress test for {@link JDBCReviewFeedbackManager#get(long)}.
	 * 
	 * @throws Exception
	 *             to JUnit.
	 */
	@Test
	public void test_get() throws Exception {
		for (int i = 0; i < TIMES; ++i) {
			instance.create(feedbacks[i], "OP1");
		}

		long t = System.currentTimeMillis();
		for (int i = 0; i < TIMES; ++i) {
			ReviewFeedback other = instance.get(feedbacks[i].getId());
			assertEqual(feedbacks[i], other);
		}
		t = System.currentTimeMillis() - t;
		System.out.println("Run get() for " + TIMES + " times, taking " + t
				+ "ms.");

		// Gets ID from database
		TreeSet<Long> idSet = getIds(connection);
		// The feed backs id are all in the set
		for (int i = 0; i < TIMES; ++i) {
			assertTrue("The result should be correct.",
					idSet.contains(feedbacks[i].getId()));
		}
		// All the id that less than the id in set are not in db.
		for (int i = 1; i < TIMES; ++i) {
			assertNull("The result should be correct.",
					instance.get(idSet.first() - i));
		}
		// All the id that larger than the id in set are not in db.
		for (int i = 1; i < TIMES; ++i) {
			assertNull("The result should be correct.",
					instance.get(idSet.last() + i));
		}
	}

	/**
	 * Gets IDs from database.
	 * 
	 * @param connection
	 *            the connection.
	 * @return the set contains all IDs.
	 * @throws Exception
	 *             to JUNIT.
	 */
	private TreeSet<Long> getIds(Connection connection) throws Exception {
		TreeSet<Long> result = new TreeSet<Long>();
		PreparedStatement ps = null;
		try {
			ps = connection
					.prepareStatement("select review_feedback_id from \"informix\".review_feedback");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				result.add(rs.getLong(1));
			}
		} finally {
			ps.close();
		}
		return result;
	}

	/**
	 * Stress test for multi-thread.
	 */
	@Test
	public void testThread() throws Exception {
		long t = System.currentTimeMillis();
		ManagerThread[] testThreads = new ManagerThread[THREADS];

		for (int i = 0; i < THREADS; i++) {
			testThreads[i] = new ManagerThread();
		}
		for (int i = 0; i < THREADS; i++) {
			testThreads[i].run();
		}
		for (int i = 0; i < THREADS; i++) {
			testThreads[i].join();
		}
		t = System.currentTimeMillis() - t;
		System.out.println("Run get() for " + TIMES + " times, taking " + t
				+ "ms.");
	}

	/**
	 * Reads the content of a given file.
	 * 
	 * @param fileName
	 *            the name of the file to read.
	 * 
	 * @return a string represents the content.
	 * 
	 * @throws IOException
	 *             if any error occurs during reading.
	 */
	private static String readFile(String fileName) throws IOException {
		Reader reader = new FileReader(fileName);

		try {
			// Create a StringBuilder instance
			StringBuilder sb = new StringBuilder();

			// Buffer for reading
			char[] buffer = new char[1024];

			// Number of read chars
			int k = 0;

			// Read characters and append to string builder
			while ((k = reader.read(buffer)) != -1) {
				sb.append(buffer, 0, k);
			}

			// Return read content
			return sb.toString();
		} finally {
			try {
				reader.close();
			} catch (IOException ioe) {
				// Ignore
			}
		}
	}

	/**
	 * Executes the SQL statements in the file. Lines that are empty or starts
	 * with '#' will be ignore.
	 * 
	 * @param connection
	 *            the connection.
	 * @param file
	 *            the file.
	 * 
	 * @throws Exception
	 *             to JUnit.
	 */
	private static void executeSQL(Connection connection, String file)
			throws Exception {
		Statement stmt = null;
		try {
			stmt = connection.createStatement();

			String[] values = readFile(file).split(";");

			for (int i = 0; i < values.length; i++) {
				String sql = values[i].trim();
				if ((sql.length() != 0) && (!sql.startsWith("#"))) {
					stmt.executeUpdate(sql);
				}
			}
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}

	/**
	 * Gets a connection.
	 * 
	 * @return the connection.
	 * 
	 * @throws Exception
	 *             to JUnit.
	 */
	private static Connection getConnection() throws Exception {
		ConfigurationObject config = getConfig();

		// Get configuration of DB Connection Factory
		ConfigurationObject dbConnectionFactoryConfig = config
				.getChild("dbConnectionFactoryConfiguration");

		// Create database connection factory using the extracted configuration
		DBConnectionFactoryImpl dbConnectionFactory = new DBConnectionFactoryImpl(
				dbConnectionFactoryConfig);
		return dbConnectionFactory.createConnection();
	}

	/**
	 * Gets the configuration object used for tests.
	 * 
	 * @return the configuration object.
	 * 
	 * @throws Exception
	 *             to JUnit.
	 */
	private static ConfigurationObject getConfig() throws Exception {
		XMLFilePersistence persistence = new XMLFilePersistence();

		// Get configuration
		ConfigurationObject obj = persistence.loadFile(
				"com.topcoder.management.reviewfeedback.stress", new File(
						"test_files/stress/config.xml"));

		return obj;
	}

	/**
	 * Checks whether they are equal.
	 * 
	 * @param first
	 *            the first object.
	 * @param other
	 *            the other object.
	 */
	private static void assertEqual(ReviewFeedback first, ReviewFeedback other) {
		assertEquals("The result should be correct.", first.getCreateUser(),
				other.getCreateUser());
		assertEquals("The result should be correct.", first.getProjectId(),
				other.getProjectId());
		assertEquals("The result should be correct.", first.getComment(),
				other.getComment());
		assertEquals("The result should be correct.", first.getId(),
				other.getId());
		assertEquals("The result should be correct.",
				first.getDetails().size(), other.getDetails().size());
		Comparator<ReviewFeedbackDetail> comparator = new Comparator<ReviewFeedbackDetail>() {
			public int compare(ReviewFeedbackDetail o1, ReviewFeedbackDetail o2) {
				return o1.getFeedbackText().hashCode()
						- o2.getFeedbackText().hashCode();
			}
		};
		Collections.sort(first.getDetails(), comparator);
		Collections.sort(other.getDetails(), comparator);
		for (int i = 0; i < first.getDetails().size(); i++) {
			assertEquals("The result should be correct.", first.getDetails()
					.get(i).getReviewerUserId(), other.getDetails().get(i)
					.getReviewerUserId());
			assertEquals("The result should be correct.", first.getDetails()
					.get(i).getScore(), other.getDetails().get(i).getScore());
			assertEquals("The result should be correct.", first.getDetails()
					.get(i).getFeedbackText(), other.getDetails().get(i)
					.getFeedbackText());
		}
	}

	private class ManagerThread extends Thread {
		/**
		 * The actual test.
		 */
		public void run() {
			try {
				ReviewFeedback entity = new ReviewFeedback();
				entity.setProjectId(1);

				List<ReviewFeedbackDetail> list = new ArrayList<ReviewFeedbackDetail>();
				for (int j = 0; j < DETAIL_COUNT_PER_FEEDBACK; j++) {
					ReviewFeedbackDetail detail = new ReviewFeedbackDetail();
					detail.setReviewerUserId(j + 1);
					detail.setScore(rand.nextInt(HUNDRED));
					detail.setFeedbackText("text " + 1 + " " + j);
					list.add(detail);
				}
				entity.setDetails(list);
				entity.setComment("c1");
				entity.setCreateDate(new Date());
				entity.setCreateUser(String.valueOf(rand.nextInt(HUNDRED)));
				// create user
				instance.create(entity, "op1");
				// get user
				ReviewFeedback other = instance.get(entity.getId());
				assertEqual(entity, other);
				// modify and update
				entity.setComment("c2");

				for (int j = 0; j < DETAIL_COUNT_PER_FEEDBACK; j++) {
					ReviewFeedbackDetail detail = entity.getDetails().get(j);
					detail.setReviewerUserId(j + 20);
					detail.setScore(rand.nextInt(HUNDRED));
					detail.setFeedbackText("newtext " + 1 + " " + j);
				}
				instance.update(entity, "op2");
				// verify result
				other = instance.get(entity.getId());
				assertEqual(entity, other);
				// delete and verify
				instance.delete(entity.getId());
				assertNull("The result should be correct.",
						instance.get(entity.getId()));
			} catch (ReviewFeedbackManagementException e) {
				fail("Error occurs.");
			}
		}
	}
}
