/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.processor.failuretests;

import javax.naming.ConfigurationException;

import junit.framework.TestCase;

import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogFactory;
import com.topcoder.util.log.LogManager;
import com.topcoder.util.scheduler.processor.JobProcessor;
import com.topcoder.util.scheduler.scheduling.Scheduler;

/**
 * <p>
 * Failure tests for the class JobProcessor.
 * </p>
 * 
 * @author Blues
 * @version 1.0
 */
public class JobProcessorFailureTests extends TestCase {
	/** Config file path for the Logging Wrapper */
	private static final String LOG_CONFIG = "failuretests/logConfig.xml";

	/** Namespace of the log config */
	private static final String LOG_NAMESPACE = "com.topcoder.util.log";

	/** Job Processor instance which is used for testing. */
	private JobProcessor processor;

	/** Scheduler instance, we use a MockScheduler instance here. */
	private Scheduler scheduler = new MockScheduler();

	/** Log instance used for testing. */
	private Log log;

	/**
	 * Creates JobProcessor instance used for testing.
	 * 
	 * @throws Exception
	 *             to JUnit.
	 */
	protected void setUp() throws Exception {
		// remove the namespace first if exists
		removeNamespaceFromCM(LOG_NAMESPACE);

		// load the config for logging wrapper
		ConfigManager.getInstance().add(LOG_CONFIG);

		// get logger for the JobProcessor
		log = LogManager.getLog();

		// create JobProcessor
		processor = new JobProcessor(scheduler, 1000, log);
	}

	/**
	 * Removes the log namespace from ConfigManager after single test finishes.
	 * 
	 * @throws Exception
	 *             to JUnit.
	 */
	protected void tearDown() throws Exception {
		removeNamespaceFromCM(LOG_NAMESPACE);
	}

	/**
	 * Removes the specified namespace from ConfigManager.
	 * 
	 * @param namespace
	 *            the namespace to remove.
	 * @throws Exception
	 *             to JUnit.
	 */
	private void removeNamespaceFromCM(String namespace) throws Exception {
		ConfigManager cm = ConfigManager.getInstance();

		if (cm.existsNamespace(namespace)) {
			cm.removeNamespace(namespace);
		}
	}

	/**
	 * Tests the constructor of JobProcessor with null scheduler,
	 * IllegalArgumentException should be thrown.
	 */
	public void testConstructor_nullScheduler() throws Exception {
		try {
			new JobProcessor(null, 1000, log);
			fail("null scheduler, IllegalArgumentException should be thrown.");
		} catch (IllegalArgumentException e) {
			// pass
		}
	}

	/**
	 * Tests the constructor of JobProcessor with negative reloadInterval.
	 * IllegalArgumentException should be thrown.
	 */
	public void testConstructor_negativeReloadInterval() {
		try {
			new JobProcessor(scheduler, -1000, log);
			fail("negative reloadInterval, IllegalArgumentException should be thrown.");
		} catch (IllegalArgumentException e) {
			// pass
		}
	}

	/**
	 * Tests the constructor of JobProcessor with zero reloadInterval.
	 * IllegalArgumentException should be thrown.
	 */
	public void testConstructor_zeroReloadInterval() {
		try {
			new JobProcessor(scheduler, 0, log);
			fail("zero reloadInterval, IllegalArgumentException should be thrown.");
		} catch (IllegalArgumentException e) {
			// pass
		}
	}

	/**
	 * Tests the constructor of JobProcessor with reloadInterval which exceeds
	 * 24 hours. IllegalArgumentException should be thrown.
	 */
	public void testConstructor_reloadIntervalGreaterThan24hours() {
		try {
			new JobProcessor(scheduler, 24 * 60 * 60 * 1000 + 1000, log);
			fail("reloadInterval is greater that 24 hours, IllegalArgumentException should be thrown");
		} catch (IllegalArgumentException e) {
			// pass
		}
	}

	/**
	 * Tests the constructor of JobProcessor with null log,
	 * IllegalArgumentException should be thrown.
	 */
	public void testConstructor_nullLog() {
		try {
			new JobProcessor(scheduler, 1000, null);
			fail("null log, IllegalArgumentException should be thrown.");
		} catch (IllegalArgumentException e) {
			// pass
		}
	}
	
	/**
	 * Tests the metho stopJob with null job name,
	 * IllegalArgumentException should be thrown.
	 */
	public void testStopJob_nullName() {
		try {
			processor.stopJob(null);
			fail("null job name, IllegalArgumentException should be thrown.");
 		} catch (IllegalArgumentException e) {
 			// pass
 		}
	}
	
	/**
	 * Tests the metho stopJob with empty job name,
	 * IllegalArgumentException should be thrown.
	 */
	public void testStopJob_emptyName() {
		try {
			processor.stopJob("   ");
			fail("empty job name, IllegalArgumentException should be thrown.");
 		} catch (IllegalArgumentException e) {
 			// pass
 		}
	}
	
	
}
