/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.log;

import com.topcoder.util.errorhandling.BaseException;

/**
 * <p>
 * Exception class for all logging exceptions thrown from this API. It provides the ability to reference the underlying
 * exception via the <code>getCause</code> method, inherited from <code>BaseException</code>.
 * </p>
 *
 * <p>
 * <b>Changes from v2.0: </b>
 * As per forum, new loggers may throw this exception so new constructors were added to allow unknown loggers to
 * effectively use this exception. It inherits from <code>BaseException</code> now.
 * </p>
 *
 * <p>
 * This exception will be created by a {@link LogFactory} implementation if it encounters an exception creating a log.
 * Currently, no implementation throws this exception.
 * </p>
 *
 * <p>
 * <b>Thread safety: </b>
 * This exception is thread safe by having no mutable state.
 * </p>
 *
 * @author StinkyCheeseMan, TCSDEVELOPER
 * @author adic, ShindouHikaru
 * @author Pops, TCSDEVELOPER
 * @since 1.2
 * @version 2.0
 */
public class LogException extends BaseException {

    /**
     * <p>
     * Constructor takes a <tt>throwable</tt> as the underlying exception.
     * </p>
     *
     * @param throwable the underlying exception
     *
     * @since 1.2
     */
    public LogException(Throwable throwable) {
        super(throwable);
    }

    /**
     * <p>
     * Constructs the exception from the specified message.
     * </p>
     *
     * <p>
     * <b>Changes from V2.0: </b>
     * This is a new constructor.
     * </p>
     *
     * @param msg A possibly null, possibly empty (trim'd) string exception message
     *
     * @since 2.0
     */
    public LogException(String msg) {
        super(msg);
    }

    /**
     * <p>
     * Constructs the exception from the specified message and throwable.
     * </p>
     *
     * <p>
     * <b>Changes from V2.0: </b>
     * This is a new constructor.
     * </p>
     *
     * @param msg A possibly null, possibly empty (trim'd) string exception message
     * @param throwable A possibly null underlying exception
     *
     * @since 2.0
     */
    public LogException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}

