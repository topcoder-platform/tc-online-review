/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.log;

import java.beans.PropertyChangeEvent;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.util.format.ObjectFormatMethod;
import com.topcoder.util.format.ObjectFormatter;
import com.topcoder.util.format.ObjectFormatterFactory;
import com.topcoder.util.log.basic.BasicLogFactory;
import com.topcoder.util.log.log4j.Log4jLogFactory;

/**
 * <p>
 * Component demonstration for Logging Wrapper Component 2.0.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class DemoTests extends TestCase {


    /**
     * <p>
     * Returns the test suite of this class.
     * </p>
     *
     * @return the test suite of this class.
     */
    public static Test suite() {
        return new TestSuite(DemoTests.class);
    }

    /**
     * <p>
     * Sets up test environment.
     * </p>
     */
    protected void setUp() {
        LogManager.setLogFactory(new BasicLogFactory());
        Logger.getRootLogger().removeAllAppenders();
        // configure the root log4j log
        Logger logger = Logger.getRootLogger();
        ConsoleAppender appender = new ConsoleAppender(new SimpleLayout(), "System.out");
        logger.removeAllAppenders();
        // add the appender
        logger.addAppender(appender);
        // set the level
        logger.setLevel(org.apache.log4j.Level.INFO);

        // configure the server logger
        java.util.logging.Logger jdkLogger = java.util.logging.Logger.getLogger("nioserver");
        // clear the handlers
        Handler[] handlers = jdkLogger.getHandlers();
        for (int i = 0; i < handlers.length; i++) {
            jdkLogger.removeHandler(handlers[i]);
        }
        // add the appender
        jdkLogger.addHandler(new ConsoleHandler());
        // set the level
        jdkLogger.setLevel(java.util.logging.Level.INFO);
    }

    /**
     * <p>
     * This method demonstrates how to change when upgrading from v1.2 to v2.0.
     * </p>
     *
     * <p>
     * There are two breakage points when upgrading from v1.2 to v2.0: <br>
     *  a. The loss of specifying the logger implementation via the configuration file. <br>
     *  b. The main class name that changed.<br>
     * </p>
     *
     * <p>
     * Fortunately, both breakage points can be rectified with one simple change.
     * </p>
     */
    public void testUpgradingFromV12() {
        // In v1.2 you would load the configuration with the following line:
        // LogFactory.loadConfiguration();
        // To convert this to v2.0, you simply replace the line with the following (using the appropriate logging
        // factory): This corrects both the name change and specification of the underlying logging system.
        LogManager.setLogFactory(new Log4jLogFactory());
    }

    /**
     * <p>
     * This method demonstrates how to setup up for the Logging Factory.
     * </p>
     *
     * <p>
     * Before any logging is done, the Logging Wrapper component should be setup.
     * Setup involves two steps - specification of the underlying logging factory and specification of the message
     * object formatting (if needed).
     * </p>
     *
     * <p>
     * Both steps are optional. If the underlying logging factory is not specified, logging to the console
     * (System.out) will be used by default. If the message object formatting is not specified, a simply
     * object.toString() will be used for formatting.
     * </p>
     */
    public void testLoggingSetupForLoggingFactory() {
        // set the underlying logging factory to use the Log4jLog
        // the logging configuration of log4j must be specified according to log4j requirements
        LogManager.setLogFactory(new Log4jLogFactory());
        // set the message object formatting to use PrimitiveFormatter
        ObjectFormatter objectFormatter = ObjectFormatterFactory.getPrettyFormatter();
        // get the Log instance, it should be Log4jLog
        Log log = LogManager.getLog();
        // log the object using PrimitiveFormatter
        log.log(Level.INFO, new Integer(123456789), objectFormatter);
    }

    /**
     * <p>
     * This method demonstrates how to specify the Logging Factory.
     * </p>
     *
     * <p>
     * In version 2.0, the component provides three basic logging systems:<br>
     *  a) A Basic Log Factory that will log to a specified print stream.<br>
     *  b) A Java Log Factory that will log to the Java 1.4+ logging API.<br>
     *  c) A Log4J Log Factory that will log to the Log4J logging API.<br>
     * Although not required, it's highly recommended that the logging factory be specified as early as possible and
     * only be specified once.
     * </p>
     *
     * <p>
     * Here is an example of using the Basic Log Factory to write logging to a file.
     * </p>
     *
     * @throws Exception to Junit
     */
    public void testSpecificationOfLoggingFactory() throws Exception {
        // create a print stream to the file with auto flushing
        PrintStream ps = new PrintStream(new FileOutputStream("test_files/log.txt"), true);
        // specify the basic logger with the above print stream
        // it's highly recommended that the logging factory is only specified once
        LogManager.setLogFactory(new BasicLogFactory(ps));
        // application code ...
        // any logging from this point on will go to the "test_files/log.txt" file
        // get the Log instance, it should be BasicLog
        Log log = LogManager.getLog();
        // log the object using PrimitiveFormatter
        log.log(Level.INFO, new Integer(123456789));
    }

    /**
     * <p>
     * This method demonstrates how to specify the Object Formatting.
     * </p>
     *
     * <p>
     * Before any logging is done, the Logging Wrapper component should be setup.
     * Setup involves two steps -? specification of the underlying logging factory and specification of the message
     * object formatting (if needed).
     * </p>
     *
     * <p>
     * Both steps are optional. If the underlying logging factory is not specified, logging to the console
     * (System.out) will be used by default. If the message object formatting is not specified, a simply
     * object.toString() will be used for formatting.
     * </p>
     *
     * <p>
     * This component makes use of the Object Formatter component to separate the formatting of an object from the
     * object itself or from the logging line itself.
     * </p>
     * <p>
     * If an application needed to log property change events in a GUI application, the logging code would look
     * something like:
     * </p>
     * <pre>
     *  logger.log(Level.DEBUG, event.getPropertyName() + " changed to a new value of " + event.getNewValue());
     * </pre>
     * <p>
     * Or like:
     * </p>
     * <pre>
     *  logger.log(Level.DEBUG, event.toString());
     * </pre>
     * <p>
     * Using the new message format API, you could change this to:
     * </p>
     * <pre>
     *  logger.log(Level.DEBUG, "{0} changed to a new value of {1}", event.getPropertyName(), event.getNewValue());
     * </pre>
     * <p>
     * The problem with any of those is that the formatting of the event is duplicated in every logging line
     * (especially across the application) or the formatting of the event is left up to its toString method (which
     * ties the formatting directly to the object or provides more/less information than you need). If you wanted to
     * add in the old value from the event, you have to modify every location.
     * </p>
     *
     * <p>
     * Here is an example of using the Basic Log Factory to write logging to a file.
     * </p>
     *
     * <p>
     * If you ever needed to change the format of the logging, you just have to change the ObjectFormatMethod above
     * and all instances of the event logging would change automatically.  This provides separation of formatting code
     * from both the object itself and the logging locations.
     * </p>
     */
    public void testSpecificationOfObjectFormatting() {
        // specify Log4J logging
        LogManager.setLogFactory(new Log4jLogFactory());

        // specify a format method for all PropertyChangeEvent classes
        LogManager.getObjectFormatter().setFormatMethodForClass(
                PropertyChangeEvent.class,
                new ObjectFormatMethod() {
                    public String format(Object o) {
                        PropertyChangeEvent e = (PropertyChangeEvent) o;
                        return e.getPropertyName() + " changed from " + e.getOldValue() + " to " + e.getNewValue();
                    }
                },
                true);
        // application code...
        // get the Log instance, it should be Log4jLog
        Log log = LogManager.getLog();
        // then the logging line would be simple:
        log.log(Level.INFO, new PropertyChangeEvent(new Object(), "propertyName", "oldValue", "newValue"));
    }

    /**
     * <p>
     * This method demonstrates how to log the traditional message with exception stack trace.
     * </p>
     *
     * <p>
     * Logging a message with or without an exception is fairly straight forward. You simply acquire a Log then use
     * the API to log message and/or errors.
     * </p>
     */
    public void testLoggingMessageWithException() {
        Log log = LogManager.getLog("acme");
        // other codes ...
        // Log the entry into the message
        log.log(Level.DEBUG, "Entering doUpdate");
        try {
          // some SQL based updates
            doSQLUpdates();
        } catch (SQLException e) {
            // Log the exception with a full stack trace
            log.log(Level.ERROR, e, "Update failed");
        }
    }

    /**
     * <p>
     * This method demonstrates how to log the message formatting.
     * </p>
     *
     * <p>
     * The performance aspect of logging is an area that needs constant attention.
     * </p>
     *
     * <p>
     * Example: the following logging code has a fairly high overhead because it constructs a String regardless if the
     * message will be logged or not:
     * </p>
     * <pre>
     *  log.log(Level.DEBUG, someString + " happened at " + System.currentMillis());
     * </pre>
     * <p>
     * If only Level.INFO and above is enabled, the above line will waste time by constructing the string first then
     * determining nothing should be logged.
     * </p>
     * <p>
     * Traditionally, you could get around that issue by writing the code like:
     * </p>
     * <pre>
     *  if (log.isEnabled(Level.DEBUG)) {
     *       log.log(Level.DEBUG, someString + " happened at " + System.currentMillis());
     *  }
     * </pre>
     * <p>
     * However, this is fairly involved code for logging and programmers tend to forget the performance aspect of that.
     * </p>
     * <p>
     * Instead, the message format API can be used:
     * </p>
     * <pre>
     *  log.log(Level.DEBUG, "{0} happened at {1}", someString, System.currentMillis());
     * </pre>
     * <p>
     * This minimizes the overhead by allowing the formatting to happen as late as possible (certainly after a check
     * to see if logging is enabled).  This allows the code to be more compact and easier to write (and less
     * performance error prone).
     * </p>
     * <p>
     * The API provides overloaded "log" methods for specification of up to three arguments directly.  More arguments
     * can be specified by using the object array API.
     * </p>
     */
    public void testLoggingMessageFormatting() {
        // get the BasicLog
        Log log = LogManager.getLog();
        String tradeDesc = "tradeDesc";
        Integer numOfShares = new Integer(3);
        Double pricePerShare = new Double(1.5);
        Date tradeTime = new Date();
        // log the message formatting
        log.log(Level.DEBUG, "Trade {0} for {1} shares at {2} occurred at {3}",
                new Object[] {tradeDesc, numOfShares, pricePerShare, tradeTime});
    }

    /**
     * <p>
     * This method demonstrates how to log the message formatting.
     * </p>
     *
     * <p>
     * The following is an example of the usage of the Logging Wrapper component in a NIO Server.  This example
     * demonstrates the setup and API usage of the component.
     * </p>
     */
    public void testNIOSeverExample() {
        NioServer.main(new String[] {"127.0.0.1", "6789"});
        // delay to let the NioServer run for some time
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // ignore
        }
    }

    /**
     * <p>
     * Does some SQL based updates used for testing.
     * </p>
     *
     * <p>
     * It will always throw a SQLException.
     * </p>
     *
     * @throws SQLException always thrown
     */
    private void doSQLUpdates() throws SQLException {
        throw new SQLException("For testing.");
    }
}
