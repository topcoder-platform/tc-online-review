/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.log;

import java.util.Date;

import com.topcoder.util.format.ObjectFormatter;
import com.topcoder.util.format.ObjectFormatterFactory;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * <p>
 * Unit tests for {@link AbstractLog} class. It uses MockAbstractLog to test.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class AbstractLogTest extends TestCase {
    /**
     * <p>
     * Represents the name of the log used for testing.
     * </p>
     */
    private static final String LOG_NAME = "logName";

    /**
     * <p>
     * Represents the message to log.
     * </p>
     */
    private static final Object MESSAGE = new Integer(123456789);

    /**
     * <p>
     * Represents the message string to log.
     * </p>
     */
    private static final String MESSAGE_STRING = "message";

    /**
     * <p>
     * MockAbstractLog instance used for testing.
     * </p>
     */
    private MockAbstractLog log;

    /**
     * <p>
     * Throwable instance used for testing.
     * </p>
     */
    private Throwable throwable;

    /**
     * <p>
     * Returns the test suite of this class.
     * </p>
     *
     * @return the test suite of this class.
     */
    public static Test suite() {
        return new TestSuite(AbstractLogTest.class);
    }

    /**
     * <p>
     * Sets up the test environment.
     * </p>
     */
    protected void setUp() {
        throwable = new Throwable();
        log = new MockAbstractLog(LOG_NAME);
    }

    /**
     * <p>
     * Tears down the test environment.
     * </p>
     */
    protected void tearDown() {
        log = null;
        throwable = null;
    }

    /**
     * <p>
     * Tests constructor for AbstractLog(String) for accuracy.
     * </p>
     *
     * <p>
     * Verify that AbstractLog(String) is correct.
     * </p>
     */
    public void testAbstractLog() {
        assertNotNull("The AbstractLog should not be null.", log);
    }

    /**
     * <p>
     * Tests method for getName() for accuracy.
     * </p>
     *
     * <p>
     * Verify that getName() is correct.
     * </p>
     */
    public void testGetName() {
        assertEquals("The name is incorrect.", LOG_NAME, log.getName());
    }

    /**
     * <p>
     * Tests method for log(Level, Object) for accuracy.
     * </p>
     *
     * <p>
     * Verify that log(Level, Object) is correct.
     * </p>
     */
    public void testLogLevelObject() {
        log.log(Level.ALL, MESSAGE);
        assertEquals("The level is incorrect.", Level.ALL, log.getLevel());
        assertEquals("The message to log is incorrect.", MESSAGE.toString(), log.getMessage());
        assertNull("The cause should be null.", log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, Object) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the message is null and verifies log(Level, Object) is correct.
     * </p>
     */
    public void testLogLevelObjectWithNullMessage() {
        log.log(Level.ALL, null);
        assertEquals("The level is incorrect.", Level.ALL, log.getLevel());
        assertEquals("The message to log is incorrect.", "null", log.getMessage());
        assertNull("The cause should be null.", log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, Object) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the level is lower that INFO and verifies that no message will be logged.
     * </p>
     */
    public void testLogLevelObjectWithLowerLevel() {
        log.log(Level.DEBUG, MESSAGE);
        assertNull("The level should be null.", log.getLevel());
        assertNull("The message should be null.", log.getMessage());
        assertNull("The cause should be null.", log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, Object) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the level is null and verifies nothing will be logged.
     * </p>
     */
    public void testLogLevelObjectWithNullLevel() {
        log.log(null, MESSAGE);
        assertNull("The formatted message should be null.", log.getMessage());
        assertNull("The level should be null.", log.getLevel());
        assertNull("The cause should be null.", log.getCause());
    }


    /**
     * <p>
     * Tests method for log(Level, Object, ObjectFormatter) for accuracy.
     * </p>
     *
     * <p>
     * Verify that log(Level, Object, ObjectFormatter) is correct.
     * </p>
     */
    public void testLogLevelObjectObjectFormatter() {
        ObjectFormatter objectFormatter = ObjectFormatterFactory.getPrettyFormatter();
        log.log(Level.ALL, MESSAGE, objectFormatter);
        assertEquals("The formatted message is incorrect.", objectFormatter.format(MESSAGE), log.getMessage());
        assertEquals("The level is incorrect.", Level.ALL, log.getLevel());
        assertNull("The cause should be null.", log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, Object, ObjectFormatter) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the level is null and verifies nothing will be logged.
     * </p>
     */
    public void testLogLevelObjectObjectFormatterWithNullLevel() {
        ObjectFormatter objectFormatter = ObjectFormatterFactory.getPrettyFormatter();
        log.log(null, MESSAGE, objectFormatter);
        assertNull("The formatted message should be null.", log.getMessage());
        assertNull("The level should be null.", log.getLevel());
        assertNull("The cause should be null.", log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, Object, ObjectFormatter) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the level is lower than INFO and verifies nothing will be logged.
     * </p>
     */
    public void testLogLevelObjectObjectFormatterWithLowerLevel() {
        ObjectFormatter objectFormatter = ObjectFormatterFactory.getPrettyFormatter();
        log.log(Level.DEBUG, MESSAGE, objectFormatter);
        assertNull("The formatted message should be null.", log.getMessage());
        assertNull("The level should be null.", log.getLevel());
        assertNull("The cause should be null.", log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, Object, ObjectFormatter) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the message is null and verifies "null" message will be logged.
     * </p>
     */
    public void testLogLevelObjectObjectFormatterWithNullMessage() {
        ObjectFormatter objectFormatter = ObjectFormatterFactory.getPrettyFormatter();
        log.log(Level.ALL, (Object) null, objectFormatter);
        assertEquals("The formatted message should be 'null'.", "null", log.getMessage());
        assertEquals("The level is incorrect.", Level.ALL, log.getLevel());
        assertNull("The cause should be null.", log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, Object, ObjectFormatter) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the ObjectFormatter is invalid and verifies message.toString() will be logged.
     * </p>
     */
    public void testLogLevelObjectObjectFormatterWithInvalidObjectFormatter() {
        ObjectFormatter objectFormatter = ObjectFormatterFactory.getPrettyFormatter();
        Object object = new Object();
        log.log(Level.ALL, object, objectFormatter);
        assertEquals("The formatted message should be the same as message.toString().",
                object.toString(), log.getMessage());
        assertEquals("The level is incorrect.", Level.ALL, log.getLevel());
        assertNull("The cause should be null.", log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, Object, ObjectFormatter) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the ObjectFormatter is null and verifies message.toString() will be logged.
     * </p>
     */
    public void testLogLevelObjectObjectFormatterWithNullObjectFormatter() {
        Object object = new Object();
        log.log(Level.ALL, object, null);
        assertEquals("The formatted message should be the same as message.toString().",
                object.toString(), log.getMessage());
        assertEquals("The level is incorrect.", Level.ALL, log.getLevel());
        assertNull("The cause should be null.", log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, String, Object) for accuracy.
     * </p>
     *
     * <p>
     * Verify that log(Level, String, Object) is correct.
     * </P>
     */
    public void testLogLevelStringObject() {
        log.log(Level.ALL, MESSAGE_STRING + "{0}", MESSAGE_STRING);
        assertEquals("The formatted message is incorrect.", MESSAGE_STRING + MESSAGE_STRING, log.getMessage());
        assertEquals("The level is incorrect.", Level.ALL, log.getLevel());
        assertNull("The cause should be null.", log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, String, Object) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the level is null and verifies nothing will be logged.
     * </P>
     */
    public void testLogLevelStringObjectWithNullLevel() {
        log.log(null, MESSAGE_STRING + "{0}", MESSAGE_STRING);
        assertNull("The formatted message should be null.", log.getMessage());
        assertNull("The level should be null.", log.getLevel());
        assertNull("The cause should be null.", log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, String, Object) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the level is lower than INFO and verifies nothing will be logged.
     * </P>
     */
    public void testLogLevelStringObjectWithLowerLevel() {
        log.log(Level.DEBUG, MESSAGE_STRING + "{0}", MESSAGE_STRING);
        assertNull("The formatted message should be null.", log.getMessage());
        assertNull("The level should be null.", log.getLevel());
        assertNull("The cause should be null.", log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, String, Object) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the messageFormat is null and verifies nothing will be logged.
     * </P>
     */
    public void testLogLevelStringObjectWithNullMessageFormat() {
        log.log(Level.ALL, (String) null, MESSAGE_STRING);
        assertNull("The formatted message should be null.", log.getMessage());
        assertNull("The level should be null.", log.getLevel());
        assertNull("The cause should be null.", log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, String, Object) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the objects is null and verifies log(Level, Throwable, String, Object)
     * is correct.
     * </P>
     */
    public void testLogLevelStringObjectWithNullObjects() {
        log.log(Level.ALL,  MESSAGE_STRING + "{0}", (Object) null);
        assertEquals("The formatted message is incorrect.", MESSAGE_STRING + "null", log.getMessage());
        assertEquals("The level is incorrect.", Level.ALL, log.getLevel());
        assertNull("The cause should be null.", log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, String, Object) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the messageFormat is invalid and verifies nothing will be logged.
     * </P>
     */
    public void testLogLevelStringObjectWithInvalidMessageFormat() {
        log.log(Level.ALL, MESSAGE_STRING + " {0, invalid}", new Date());
        assertNull("The formatted message should be null.", log.getMessage());
        assertNull("The level should be null.", log.getLevel());
        assertNull("The cause should be null.", log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, String, Object) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the objects are invalid and verifies nothing will be logged.
     * </P>
     */
    public void testLogLevelStringObjectWithInvalidObjects() {
        log.log(Level.ALL, MESSAGE_STRING + " {0, time}", MESSAGE_STRING);
        assertNull("The formatted message should be null.", log.getMessage());
        assertNull("The level should be null.", log.getLevel());
        assertNull("The cause should be null.", log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, String, Object, Object) for accuracy.
     * </p>
     *
     * <p>
     * Verify that log(Level, String, Object, Object) is correct.
     * </P>
     */
    public void testLogLevelStringObjectObject() {
        log.log(Level.ALL, MESSAGE_STRING + "{0}{1}", MESSAGE_STRING, MESSAGE_STRING);
        assertEquals("The formatted message is incorrect.",
                MESSAGE_STRING + MESSAGE_STRING + MESSAGE_STRING, log.getMessage());
        assertEquals("The level is incorrect.", Level.ALL, log.getLevel());
        assertNull("The cause should be null.", log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, String, Object, Object) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the level is null and verifies nothing will be logged.
     * </P>
     */
    public void testLogLevelStringObjectObjectWithNullLevel() {
        log.log(null, MESSAGE_STRING + "{0}{1}", MESSAGE_STRING, MESSAGE_STRING);
        assertNull("The formatted message should be null.", log.getMessage());
        assertNull("The level should be null.", log.getLevel());
        assertNull("The cause should be null.", log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, String, Object, Object) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the level is lower than INFO and verifies nothing will be logged.
     * </P>
     */
    public void testLogLevelStringObjectObjectWithLowerLevel() {
        log.log(Level.DEBUG, MESSAGE_STRING + "{0}{1}", MESSAGE_STRING, MESSAGE_STRING);
        assertNull("The formatted message should be null.", log.getMessage());
        assertNull("The level should be null.", log.getLevel());
        assertNull("The cause should be null.", log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, String, Object, Object) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the messageFormat is null and verifies nothing will be logged.
     * </P>
     */
    public void testLogLevelStringObjectObjectWithNullMessageFormat() {
        log.log(Level.ALL, (String) null, MESSAGE_STRING, MESSAGE_STRING);
        assertNull("The formatted message should be null.", log.getMessage());
        assertNull("The level should be null.", log.getLevel());
        assertNull("The cause should be null.", log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, String, Object, Object) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the objects is null and verifies log(Level, Throwable, String, Object, Object)
     * is correct.
     * </P>
     */
    public void testLogLevelStringObjectObjectWithNullObjects() {
        log.log(Level.ALL, MESSAGE_STRING + "{0}{1}", null, null);
        assertEquals("The formatted message is incorrect.", MESSAGE_STRING + "nullnull", log.getMessage());
        assertEquals("The level is incorrect.", Level.ALL, log.getLevel());
        assertNull("The cause should be null.", log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, String, Object, Object) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the messageFormat is invalid and verifies nothing will be logged.
     * </P>
     */
    public void testLogLevelStringObjectObjectWithInvalidMessageFormat() {
        log.log(Level.ALL, MESSAGE_STRING + " {0, invalid}, {1, time}", new Date(), new Date());
        assertNull("The formatted message should be null.", log.getMessage());
        assertNull("The level should be null.", log.getLevel());
        assertNull("The cause should be null.", log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, String, Object, Object) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the objects are invalid and verifies nothing will be logged.
     * </P>
     */
    public void testLogLevelStringObjectObjectWithInvalidObjects() {
        log.log(Level.ALL, MESSAGE_STRING + " {0, time}, {1, time}", MESSAGE_STRING, MESSAGE_STRING);
        assertNull("The formatted message should be null.", log.getMessage());
        assertNull("The level should be null.", log.getLevel());
        assertNull("The cause should be null.", log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, String, Object, Object, Object) for accuracy.
     * </p>
     *
     * <p>
     * Verify that log(Level, String, Object, Object, Object) is correct.
     * </P>
     */
    public void testLogLevelStringObjectObjectObject() {
        log.log(Level.ALL, MESSAGE_STRING + "{0}{1}{2}", MESSAGE_STRING, MESSAGE_STRING, MESSAGE_STRING);
        assertEquals("The formatted message is incorrect.",
                MESSAGE_STRING + MESSAGE_STRING + MESSAGE_STRING + MESSAGE_STRING, log.getMessage());
        assertEquals("The level is incorrect.", Level.ALL, log.getLevel());
        assertNull("The cause should be null.", log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, String, Object, Object, Object) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the level is null and verifies nothing will be logged.
     * </P>
     */
    public void testLogLevelStringObjectObjectObjectWithNullLevel() {
        log.log(null, MESSAGE_STRING + "{0}{1}{2}", MESSAGE_STRING, MESSAGE_STRING, MESSAGE_STRING);
        assertNull("The formatted message should be null.", log.getMessage());
        assertNull("The level should be null.", log.getLevel());
        assertNull("The cause should be null.", log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, String, Object, Object, Object) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the level is lower than INFO and verifies nothing will be logged.
     * </P>
     */
    public void testLogLevelStringObjectObjectObjectWithLowerLevel() {
        log.log(Level.DEBUG, MESSAGE_STRING + "{0}{1}{2}", MESSAGE_STRING, MESSAGE_STRING, MESSAGE_STRING);
        assertNull("The formatted message should be null.", log.getMessage());
        assertNull("The level should be null.", log.getLevel());
        assertNull("The cause should be null.", log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, String, Object, Object, Object) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the messageFormat is null and verifies nothing will be logged.
     * </P>
     */
    public void testLogLevelStringObjectObjectObjectWithNullMessageFormat() {
        log.log(Level.ALL, (String) null, MESSAGE_STRING, MESSAGE_STRING, MESSAGE_STRING);
        assertNull("The formatted message should be null.", log.getMessage());
        assertNull("The level should be null.", log.getLevel());
        assertNull("The cause should be null.", log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, String, Object, Object, Object) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the objects is null and verifies log(Level, Throwable, String, Object, Object, Object)
     * is correct.
     * </P>
     */
    public void testLogLevelStringObjectObjectObjectWithNullObjects() {
        log.log(Level.ALL, MESSAGE_STRING + "{0}{1}{2}", null, null, null);
        assertEquals("The formatted message is incorrect.", MESSAGE_STRING + "nullnullnull", log.getMessage());
        assertEquals("The level is incorrect.", Level.ALL, log.getLevel());
        assertNull("The cause should be null.", log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level,String, Object, Object, Object) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the messageFormat is invalid and verifies nothing will be logged.
     * </P>
     */
    public void testLogLevelStringObjectObjectObjectWithInvalidMessageFormat() {
        log.log(Level.ALL, MESSAGE_STRING + " {0, invalid}, {1, time}, {2, time}",
                new Date(), new Date(), new Date());
        assertNull("The formatted message should be null.", log.getMessage());
        assertNull("The level should be null.", log.getLevel());
        assertNull("The cause should be null.", log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, String, Object, Object, Object) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the objects are invalid and verifies nothing will be logged.
     * </P>
     */
    public void testLogLevelStringObjectObjectObjectWithInvalidObjects() {
        log.log(Level.ALL, MESSAGE_STRING + " {0, time}, {1, time}, {2, time}",
                MESSAGE_STRING, MESSAGE_STRING, MESSAGE_STRING);
        assertNull("The formatted message should be null.", log.getMessage());
        assertNull("The level should be null.", log.getLevel());
        assertNull("The cause should be null.", log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, String, Object[]) for accuracy.
     * </p>
     *
     * <p>
     * Verify that log(Level, String, Object[]) is correct.
     * </P>
     */
    public void testLogLevelStringObjectArray() {
        log.log(Level.ALL, MESSAGE_STRING + " {0}, {1}", new Object[] {new Integer(12), new Double(123.1)});
        assertEquals("The formatted message is incorrect.", MESSAGE_STRING + " 12, 123.1", log.getMessage());
        assertEquals("The level is incorrect.", Level.ALL, log.getLevel());
        assertNull("The cause should be null.", log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, String, Object[]) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the level is null and verifies nothing will be logged.
     * </P>
     */
    public void testLogLevelStringObjectArrayWithNullLevel() {
        log.log(null, MESSAGE_STRING + " {0}, {1}", new Object[] {new Integer(12), new Double(123.1)});
        assertNull("The formatted message should be null.", log.getMessage());
        assertNull("The level should be null.", log.getLevel());
        assertNull("The cause should be null.", log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, String, Object[]) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the level is lower than INFO and verifies nothing will be logged.
     * </P>
     */
    public void testLogLevelStringObjectArrayWithLowerLevel() {
        log.log(Level.DEBUG, MESSAGE_STRING + " {0}, {1}", new Object[] {new Integer(12), new Double(123.1)});
        assertNull("The formatted message should be null.", log.getMessage());
        assertNull("The level should be null.", log.getLevel());
        assertNull("The cause should be null.", log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, String, Object[]) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the messageFormat is null and verifies nothing will be logged.
     * </P>
     */
    public void testLogLevelStringObjectArrayWithNullMessageFormat() {
        log.log(Level.ALL, (String) null, new Object[] {new Integer(12), new Double(123.1)});
        assertNull("The formatted message should be null.", log.getMessage());
        assertNull("The level should be null.", log.getLevel());
        assertNull("The cause should be null.", log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, String, Object[]) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the args is null and verifies the original messageFormat will be logged.
     * </P>
     */
    public void testLogLevelStringObjectArrayWithNullObjectArray() {
        log.log(Level.ALL, MESSAGE_STRING + " {0}, {1}", (Object[]) null);
        assertEquals("The formatted message is incorrect.", MESSAGE_STRING + " {0}, {1}", log.getMessage());
        assertEquals("The level is incorrect.", Level.ALL, log.getLevel());
        assertNull("The cause should be null.", log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, String, Object[]) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the messageFormat is invalid and verifies nothing will be logged.
     * </P>
     */
    public void testLogLevelStringObjectArrayWithInvalidMessageFormat() {
        log.log(Level.ALL, MESSAGE_STRING + " {0, invalid}", new Object[] {new Integer(10)});
        assertNull("The formatted message should be null.", log.getMessage());
        assertNull("The level should be null.", log.getLevel());
        assertNull("The cause should be null.", log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, String, Object[]) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the object array is invalid and verifies nothing will be logged.
     * </P>
     */
    public void testLogLevelStringObjectArrayWithInvalidObjectArray() {
        log.log(Level.ALL, MESSAGE_STRING + " {0, time}", new Object[] {MESSAGE_STRING});
        assertNull("The formatted message should be null.", log.getMessage());
        assertNull("The level should be null.", log.getLevel());
        assertNull("The cause should be null.", log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, Throwable, Object) for accuracy.
     * </p>
     *
     * <p>
     * Verify that log(Level, Throwable, Object) is correct.
     * </p>
     */
    public void testLogLevelThrowableObject() {
        log.log(Level.ALL, throwable, MESSAGE);
        assertEquals("The formatted message is incorrect.", MESSAGE.toString(), log.getMessage());
        assertEquals("The level is incorrect.", Level.ALL, log.getLevel());
        assertSame("The cause is incorrect.", throwable, log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, Throwable, Object) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the level is null and verifies nothing will be logged.
     * </p>
     */
    public void testLogLevelThrowableObjectWithNullLevel() {
        log.log(null, throwable, MESSAGE);
        assertNull("The formatted message should be null.", log.getMessage());
        assertNull("The level should be null.", log.getLevel());
        assertNull("The cause should be null.", log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, Throwable, Object) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the level is lower than INFO and verifies nothing will be logged.
     * </p>
     */
    public void testLogLevelThrowableObjectWithLowerLevel() {
        log.log(Level.DEBUG, throwable, MESSAGE);
        assertNull("The formatted message should be null.", log.getMessage());
        assertNull("The level should be null.", log.getLevel());
        assertNull("The cause should be null.", log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, Throwable, Object) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the cause is null and verifies log(Level, Throwable, Object) is correct.
     * </p>
     */
    public void testLogLevelThrowableObjectWithNullCause() {
        log.log(Level.ALL, (Throwable) null, MESSAGE);
        assertEquals("The formatted message is incorrect.", MESSAGE.toString(), log.getMessage());
        assertEquals("The level is incorrect.", Level.ALL, log.getLevel());
        assertNull("The cause should be null.", log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, Throwable, Object) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the message is null and verifies "null" message will be logged.
     * </p>
     */
    public void testLogLevelThrowableObjectWithNullMessage() {
        log.log(Level.ALL, throwable, (Object) null);
        assertEquals("The formatted message should be 'null'.", "null", log.getMessage());
        assertEquals("The level is incorrect.", Level.ALL, log.getLevel());
        assertSame("The cause is incorrect.", throwable, log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, Throwable, Object, ObjectFormatter) for accuracy.
     * </p>
     *
     * <p>
     * Verify that log(Level, Throwable, Object, ObjectFormatter) is correct.
     * </p>
     */
    public void testLogLevelThrowableObjectObjectFormatter() {
        ObjectFormatter objectFormatter = ObjectFormatterFactory.getPrettyFormatter();
        log.log(Level.ALL, throwable, MESSAGE, objectFormatter);
        assertEquals("The formatted message is incorrect.", objectFormatter.format(MESSAGE), log.getMessage());
        assertEquals("The level is incorrect.", Level.ALL, log.getLevel());
        assertSame("The cause is incorrect.", throwable, log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, Throwable, Object, ObjectFormatter) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the level is null and verifies nothing will be logged.
     * </p>
     */
    public void testLogLevelThrowableObjectObjectFormatterWithNullLevel() {
        ObjectFormatter objectFormatter = ObjectFormatterFactory.getPrettyFormatter();
        log.log(null, throwable, MESSAGE, objectFormatter);
        assertNull("The formatted message should be null.", log.getMessage());
        assertNull("The level should be null.", log.getLevel());
        assertNull("The cause should be null.", log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, Throwable, Object, ObjectFormatter) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the level is lower than INFO and verifies nothing will be logged.
     * </p>
     */
    public void testLogLevelThrowableObjectObjectFormatterWithLowerLevel() {
        ObjectFormatter objectFormatter = ObjectFormatterFactory.getPrettyFormatter();
        log.log(Level.DEBUG, throwable, MESSAGE, objectFormatter);
        assertNull("The formatted message should be null.", log.getMessage());
        assertNull("The level should be null.", log.getLevel());
        assertNull("The cause should be null.", log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, Throwable, Object, ObjectFormatter) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the cause is null and verifies log(Level, Throwable, Object, ObjectFormatter) is correct.
     * </p>
     */
    public void testLogLevelThrowableObjectObjectFormatterWithNullCause() {
        ObjectFormatter objectFormatter = ObjectFormatterFactory.getPrettyFormatter();
        log.log(Level.ALL, (Throwable) null, MESSAGE, objectFormatter);
        assertEquals("The formatted message is incorrect.", objectFormatter.format(MESSAGE), log.getMessage());
        assertEquals("The level is incorrect.", Level.ALL, log.getLevel());
        assertNull("The cause should be null.", log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, Throwable, Object, ObjectFormatter) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the message is null and verifies "null" message will be logged.
     * </p>
     */
    public void testLogLevelThrowableObjectObjectFormatterWithNullMessage() {
        ObjectFormatter objectFormatter = ObjectFormatterFactory.getPrettyFormatter();
        log.log(Level.ALL, throwable, (Object) null, objectFormatter);
        assertEquals("The formatted message should be 'null'.", "null", log.getMessage());
        assertEquals("The level is incorrect.", Level.ALL, log.getLevel());
        assertSame("The cause is incorrect.", throwable, log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, Throwable, Object, ObjectFormatter) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the ObjectFormatter is invalid and verifies message.toString() will be logged.
     * </p>
     */
    public void testLogLevelThrowableObjectObjectFormatterWithInvalidObjectFormatter() {
        ObjectFormatter objectFormatter = ObjectFormatterFactory.getPrettyFormatter();
        Object object = new Object();
        log.log(Level.ALL, throwable, object, objectFormatter);
        assertEquals("The formatted message should be the same as message.toString().",
                object.toString(), log.getMessage());
        assertEquals("The level is incorrect.", Level.ALL, log.getLevel());
        assertSame("The cause is incorrect.", throwable, log.getCause());
    }


    /**
     * <p>
     * Tests method for log(Level, Throwable, String, Object) for accuracy.
     * </p>
     *
     * <p>
     * Verify that log(Level, Throwable, String, Object) is correct.
     * </P>
     */
    public void testLogLevelThrowableStringObject() {
        log.log(Level.ALL, throwable, MESSAGE_STRING + "{0}", MESSAGE_STRING);
        assertEquals("The formatted message is incorrect.", MESSAGE_STRING + MESSAGE_STRING, log.getMessage());
        assertEquals("The level is incorrect.", Level.ALL, log.getLevel());
        assertSame("The cause is incorrect.", throwable, log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, Throwable, String, Object) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the level is null and verifies nothing will be logged.
     * </P>
     */
    public void testLogLevelThrowableStringObjectWithNullLevel() {
        log.log(null, throwable, MESSAGE_STRING + "{0}", MESSAGE_STRING);
        assertNull("The formatted message should be null.", log.getMessage());
        assertNull("The level should be null.", log.getLevel());
        assertNull("The cause should be null.", log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, Throwable, String, Object) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the level is lower than INFO and verifies nothing will be logged.
     * </P>
     */
    public void testLogLevelThrowableStringObjectWithLowerLevel() {
        log.log(Level.DEBUG, throwable, MESSAGE_STRING + "{0}", MESSAGE_STRING);
        assertNull("The formatted message should be null.", log.getMessage());
        assertNull("The level should be null.", log.getLevel());
        assertNull("The cause should be null.", log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, Throwable, String, Object) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the messageFormat is null and verifies nothing will be logged.
     * </P>
     */
    public void testLogLevelThrowableStringObjectWithNullMessageFormat() {
        log.log(Level.ALL, throwable, null, MESSAGE_STRING);
        assertNull("The formatted message should be null.", log.getMessage());
        assertNull("The level should be null.", log.getLevel());
        assertNull("The cause should be null.", log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, Throwable, String, Object) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the throwable is null and verifies throwable will not be logged.
     * </P>
     */
    public void testLogLevelThrowableStringObjectithNullThrowable() {
        log.log(Level.ALL, (Throwable) null, MESSAGE_STRING + "{0}", MESSAGE_STRING);
        assertEquals("The formatted message is incorrect.",
                MESSAGE_STRING + MESSAGE_STRING, log.getMessage());
        assertEquals("The level is incorrect.", Level.ALL, log.getLevel());
        assertNull("The cause should be null.", log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, Throwable, String, Object) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the objects is null and verifies log(Level, Throwable, String, Object)
     * is correct.
     * </P>
     */
    public void testLogLevelThrowableStringObjectWithNullObjects() {
        log.log(Level.ALL, throwable, MESSAGE_STRING + "{0}", (Object) null);
        assertEquals("The formatted message is incorrect.", MESSAGE_STRING + "null", log.getMessage());
        assertEquals("The level is incorrect.", Level.ALL, log.getLevel());
        assertSame("The cause is incorrect.", throwable, log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, Throwable, String, Object) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the messageFormat is invalid and verifies nothing will be logged.
     * </P>
     */
    public void testLogLevelThrowableStringObjectWithInvalidMessageFormat() {
        log.log(Level.ALL, throwable, MESSAGE_STRING + " {0, invalid}", new Date());
        assertNull("The formatted message should be null.", log.getMessage());
        assertNull("The level should be null.", log.getLevel());
        assertNull("The cause should be null.", log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, Throwable, String, Object) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the objects are invalid and verifies nothing will be logged.
     * </P>
     */
    public void testLogLevelThrowableStringObjectWithInvalidObjects() {
        log.log(Level.ALL, throwable, MESSAGE_STRING + " {0, time}", MESSAGE_STRING);
        assertNull("The formatted message should be null.", log.getMessage());
        assertNull("The level should be null.", log.getLevel());
        assertNull("The cause should be null.", log.getCause());
    }


    /**
     * <p>
     * Tests method for log(Level, Throwable, String, Object, Object) for accuracy.
     * </p>
     *
     * <p>
     * Verify that log(Level, Throwable, String, Object, Object) is correct.
     * </P>
     */
    public void testLogLevelThrowableStringObjectObject() {
        log.log(Level.ALL, throwable, MESSAGE_STRING + "{0}{1}", MESSAGE_STRING, MESSAGE_STRING);
        assertEquals("The formatted message is incorrect.",
                MESSAGE_STRING + MESSAGE_STRING + MESSAGE_STRING, log.getMessage());
        assertEquals("The level is incorrect.", Level.ALL, log.getLevel());
        assertSame("The cause is incorrect.", throwable, log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, Throwable, String, Object, Object) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the level is null and verifies nothing will be logged.
     * </P>
     */
    public void testLogLevelThrowableStringObjectObjectWithNullLevel() {
        log.log(null, throwable, MESSAGE_STRING + "{0}{1}", MESSAGE_STRING, MESSAGE_STRING);
        assertNull("The formatted message should be null.", log.getMessage());
        assertNull("The level should be null.", log.getLevel());
        assertNull("The cause should be null.", log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, Throwable, String, Object, Object) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the level is lower than INFO and verifies nothing will be logged.
     * </P>
     */
    public void testLogLevelThrowableStringObjectObjectWithLowerLevel() {
        log.log(Level.DEBUG, throwable, MESSAGE_STRING + "{0}{1}", MESSAGE_STRING, MESSAGE_STRING);
        assertNull("The formatted message should be null.", log.getMessage());
        assertNull("The level should be null.", log.getLevel());
        assertNull("The cause should be null.", log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, Throwable, String, Object, Object) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the messageFormat is null and verifies nothing will be logged.
     * </P>
     */
    public void testLogLevelThrowableStringObjectObjectWithNullMessageFormat() {
        log.log(Level.ALL, throwable, null, MESSAGE_STRING, MESSAGE_STRING);
        assertNull("The formatted message should be null.", log.getMessage());
        assertNull("The level should be null.", log.getLevel());
        assertNull("The cause should be null.", log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, Throwable, String, Object, Object) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the throwable is null and verifies throwable will not be logged.
     * </P>
     */
    public void testLogLevelThrowableStringObjectObjectWithNullThrowable() {
        log.log(Level.ALL, (Throwable) null, MESSAGE_STRING + "{0}{1}", MESSAGE_STRING, MESSAGE_STRING);
        assertEquals("The formatted message is incorrect.",
                MESSAGE_STRING + MESSAGE_STRING + MESSAGE_STRING, log.getMessage());
        assertEquals("The level is incorrect.", Level.ALL, log.getLevel());
        assertNull("The cause should be null.", log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, Throwable, String, Object, Object) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the objects is null and verifies log(Level, Throwable, String, Object, Object)
     * is correct.
     * </P>
     */
    public void testLogLevelThrowableStringObjectObjectWithNullObjects() {
        log.log(Level.ALL, throwable, MESSAGE_STRING + "{0}{1}", null, null);
        assertEquals("The formatted message is incorrect.", MESSAGE_STRING + "nullnull", log.getMessage());
        assertEquals("The level is incorrect.", Level.ALL, log.getLevel());
        assertSame("The cause is incorrect.", throwable, log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, Throwable, String, Object, Object) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the messageFormat is invalid and verifies nothing will be logged.
     * </P>
     */
    public void testLogLevelThrowableStringObjectObjectWithInvalidMessageFormat() {
        log.log(Level.ALL, throwable, MESSAGE_STRING + " {0, invalid}, {1, time}", new Date(), new Date());
        assertNull("The formatted message should be null.", log.getMessage());
        assertNull("The level should be null.", log.getLevel());
        assertNull("The cause should be null.", log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, Throwable, String, Object, Object) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the objects are invalid and verifies nothing will be logged.
     * </P>
     */
    public void testLogLevelThrowableStringObjectObjectWithInvalidObjects() {
        log.log(Level.ALL, throwable, MESSAGE_STRING + " {0, time}, {1, time}", MESSAGE_STRING, MESSAGE_STRING);
        assertNull("The formatted message should be null.", log.getMessage());
        assertNull("The level should be null.", log.getLevel());
        assertNull("The cause should be null.", log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, Throwable, String, Object, Object, Object) for accuracy.
     * </p>
     *
     * <p>
     * Verify that log(Level, Throwable, String, Object, Object, Object) is correct.
     * </P>
     */
    public void testLogLevelThrowableStringObjectObjectObject() {
        log.log(Level.ALL, throwable, MESSAGE_STRING + "{0}{1}{2}",
                MESSAGE_STRING, MESSAGE_STRING, MESSAGE_STRING);
        assertEquals("The formatted message is incorrect.",
                MESSAGE_STRING + MESSAGE_STRING + MESSAGE_STRING + MESSAGE_STRING, log.getMessage());
        assertEquals("The level is incorrect.", Level.ALL, log.getLevel());
        assertSame("The cause is incorrect.", throwable, log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, Throwable, String, Object, Object, Object) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the level is null and verifies nothing will be logged.
     * </P>
     */
    public void testLogLevelThrowableStringObjectObjectObjectWithNullLevel() {
        log.log(null, throwable, MESSAGE_STRING + "{0}{1}{2}",
                MESSAGE_STRING, MESSAGE_STRING, MESSAGE_STRING);
        assertNull("The formatted message should be null.", log.getMessage());
        assertNull("The level should be null.", log.getLevel());
        assertNull("The cause should be null.", log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, Throwable, String, Object, Object, Object) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the level is lower than INFO and verifies nothing will be logged.
     * </P>
     */
    public void testLogLevelThrowableStringObjectObjectObjectWithLowerLevel() {
        log.log(Level.DEBUG, throwable, MESSAGE_STRING + "{0}{1}{2}",
                MESSAGE_STRING, MESSAGE_STRING, MESSAGE_STRING);
        assertNull("The formatted message should be null.", log.getMessage());
        assertNull("The level should be null.", log.getLevel());
        assertNull("The cause should be null.", log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, Throwable, String, Object, Object, Object) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the messageFormat is null and verifies nothing will be logged.
     * </P>
     */
    public void testLogLevelThrowableStringObjectObjectObjectWithNullMessageFormat() {
        log.log(Level.ALL, throwable, null, MESSAGE_STRING, MESSAGE_STRING, MESSAGE_STRING);
        assertNull("The formatted message should be null.", log.getMessage());
        assertNull("The level should be null.", log.getLevel());
        assertNull("The cause should be null.", log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, Throwable, String, Object, Object, Object) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the throwable is null and verifies throwable will not be logged.
     * </P>
     */
    public void testLogLevelThrowableStringObjectObjectObjectWithNullThrowable() {
        log.log(Level.ALL, (Throwable) null, MESSAGE_STRING + "{0}{1}{2}",
                MESSAGE_STRING, MESSAGE_STRING, MESSAGE_STRING);
        assertEquals("The formatted message is incorrect.",
                MESSAGE_STRING + MESSAGE_STRING + MESSAGE_STRING + MESSAGE_STRING, log.getMessage());
        assertEquals("The level is incorrect.", Level.ALL, log.getLevel());
        assertNull("The cause should be null.", log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, Throwable, String, Object, Object, Object) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the objects is null and verifies log(Level, Throwable, String, Object, Object, Object)
     * is correct.
     * </P>
     */
    public void testLogLevelThrowableStringObjectObjectObjectWithNullObjects() {
        log.log(Level.ALL, throwable, MESSAGE_STRING + "{0}{1}{2}", null, null, null);
        assertEquals("The formatted message is incorrect.", MESSAGE_STRING + "nullnullnull", log.getMessage());
        assertEquals("The level is incorrect.", Level.ALL, log.getLevel());
        assertSame("The cause is incorrect.", throwable, log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, Throwable, String, Object, Object, Object) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the messageFormat is invalid and verifies nothing will be logged.
     * </P>
     */
    public void testLogLevelThrowableStringObjectObjectObjectWithInvalidMessageFormat() {
        log.log(Level.ALL, throwable, MESSAGE_STRING + " {0, invalid}, {1, time}, {2, time}",
                new Date(), new Date(), new Date());
        assertNull("The formatted message should be null.", log.getMessage());
        assertNull("The level should be null.", log.getLevel());
        assertNull("The cause should be null.", log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, Throwable, String, Object, Object, Object) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the objects are invalid and verifies nothing will be logged.
     * </P>
     */
    public void testLogLevelThrowableStringObjectObjectObjectWithInvalidObjects() {
        log.log(Level.ALL, throwable, MESSAGE_STRING + " {0, time}, {1, time}, {2, time}",
                MESSAGE_STRING, MESSAGE_STRING, MESSAGE_STRING);
        assertNull("The formatted message should be null.", log.getMessage());
        assertNull("The level should be null.", log.getLevel());
        assertNull("The cause should be null.", log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, Throwable, String, Object[]) for accuracy.
     * </p>
     *
     * <p>
     * Verify that log(Level, Throwable, String, Object[]) is correct.
     * </P>
     */
    public void testLogLevelThrowableStringObjectArray() {
        log.log(Level.ALL, throwable, MESSAGE_STRING + " {0}, {1}", new Object[] {new Integer(12), new Double(123.1)});
        assertEquals("The formatted message is incorrect.", MESSAGE_STRING + " 12, 123.1", log.getMessage());
        assertEquals("The level is incorrect.", Level.ALL, log.getLevel());
        assertSame("The cause is incorrect.", throwable, log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, Throwable, String, Object[]) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the level is null and verifies nothing will be logged.
     * </P>
     */
    public void testLogLevelThrowableStringObjectArrayWithNullLevel() {
        log.log(null, throwable, MESSAGE_STRING + " {0}, {1}", new Object[] {new Integer(12), new Double(123.1)});
        assertNull("The formatted message should be null.", log.getMessage());
        assertNull("The level should be null.", log.getLevel());
        assertNull("The cause should be null.", log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, Throwable, String, Object[]) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the level is lower than INFO and verifies nothing will be logged.
     * </P>
     */
    public void testLogLevelThrowableStringObjectArrayWithLowerLevel() {
        log.log(Level.DEBUG, throwable, MESSAGE_STRING + " {0}, {1}",
                new Object[] {new Integer(12), new Double(123.1)});
        assertNull("The formatted message should be null.", log.getMessage());
        assertNull("The level should be null.", log.getLevel());
        assertNull("The cause should be null.", log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, Throwable, String, Object[]) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the messageFormat is null and verifies nothing will be logged.
     * </P>
     */
    public void testLogLevelThrowableStringObjectArrayWithNullMessageFormat() {
        log.log(Level.ALL, throwable, null, new Object[] {new Integer(12), new Double(123.1)});
        assertNull("The formatted message should be null.", log.getMessage());
        assertNull("The level should be null.", log.getLevel());
        assertNull("The cause should be null.", log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, Throwable, String, Object[]) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the throwable is null and verifies throwable will not be logged.
     * </P>
     */
    public void testLogLevelThrowableStringObjectArrayWithNullThrowable() {
        log.log(Level.ALL, (Throwable) null, MESSAGE_STRING + " {0}, {1}",
                new Object[] {new Integer(12), new Double(123.1)});
        assertEquals("The formatted message is incorrect.", MESSAGE_STRING + " 12, 123.1", log.getMessage());
        assertEquals("The level is incorrect.", Level.ALL, log.getLevel());
        assertNull("The cause should be null.", log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, Throwable, String, Object[]) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the args is null and verifies the original messageFormat will be logged.
     * </P>
     */
    public void testLogLevelThrowableStringObjectArrayWithNullObjectArray() {
        log.log(Level.ALL, throwable, MESSAGE_STRING + " {0}, {1}", (Object[]) null);
        assertEquals("The formatted message is incorrect.", MESSAGE_STRING + " {0}, {1}", log.getMessage());
        assertEquals("The level is incorrect.", Level.ALL, log.getLevel());
        assertSame("The cause is incorrect.", throwable, log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, Throwable, String, Object[]) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the messageFormat is invalid and verifies nothing will be logged.
     * </P>
     */
    public void testLogLevelThrowableStringObjectArrayWithInvalidMessageFormat() {
        log.log(Level.ALL, throwable, MESSAGE_STRING + " {0, invalid}", new Object[] {new Integer(10)});
        assertNull("The formatted message should be null.", log.getMessage());
        assertNull("The level should be null.", log.getLevel());
        assertNull("The cause should be null.", log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, Throwable, String, Object[]) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that the object array is invalid and verifies nothing will be logged.
     * </P>
     */
    public void testLogLevelThrowableStringObjectArrayWithInvalidObjectArray() {
        log.log(Level.ALL, throwable, MESSAGE_STRING + " {0, time}", new Object[] {MESSAGE_STRING});
        assertNull("The formatted message should be null.", log.getMessage());
        assertNull("The level should be null.", log.getLevel());
        assertNull("The cause should be null.", log.getCause());
    }

    /**
     * <p>
     * Tests method for log(Level, Throwable, String) for accuracy.
     * </p>
     *
     * <p>
     * Verify that log(Level, Throwable, String) is correct and note that it is an abstract method.
     * </p>
     */
    public void testLogLevelThrowableString() {
        log.log(Level.ALL, throwable, MESSAGE_STRING);
        assertEquals("The formatted message is incorrect.", MESSAGE_STRING, log.getMessage());
        assertEquals("The level is incorrect.", Level.ALL, log.getLevel());
        assertSame("The cause is incorrect.", throwable, log.getCause());
    }
}
