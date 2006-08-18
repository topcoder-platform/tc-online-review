/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration;

import com.topcoder.util.log.Level;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogFactory;

/**
 * The test of Util.
 *
 * @author brain_cn
 * @version 1.0
 */
public class Util {
	private static Log logger = LogFactory.getLog();
	public static void info(Object info) {
		logger.log(Level.INFO, info);
	}
	public static void debug(Object debug) {
		logger.log(Level.DEBUG, debug);
	}
	public static void warn(Object warn) {
		if (warn instanceof Exception) {
			((Exception) warn).printStackTrace();
		}
		logger.log(Level.WARN, warn);
	}

	public static long start(String name) {
		long startTime = System.currentTimeMillis();
		Util.debug("start " + name);
		return startTime;
	}

	public static long startMain(String name) {
		long startTime = System.currentTimeMillis();
		Util.info("start " + name);
		return startTime;
	}

	public static void logAction(int count, String action, long startTime) {
		Util.debug(action + " " + count + " records in " + (System.currentTimeMillis() - startTime) / 1000 + " seconds");
	}

	public static void logMainAction(int count, String action, long startTime) {
		Util.info(action + " " + count + " records in " + (System.currentTimeMillis() - startTime) / 1000 + " seconds");
	}

	public static void logAction(String action, long startTime) {
		Util.debug(action + " in " + (System.currentTimeMillis() - startTime) / 1000 + " seconds");
	}

	public static void logMainAction(String action, long startTime) {
		Util.info(action + " in " + (System.currentTimeMillis() - startTime) / 1000 + " seconds");
	}
}
