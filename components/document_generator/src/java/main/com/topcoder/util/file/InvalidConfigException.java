/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.file;

import com.topcoder.util.errorhandling.BaseException;

/**
 * <p>
 * Exception thrown to indicate invalid config manager config data:
 * <ol>
 * <li>
 * missing sources property
 * </li>
 * <li>
 * missing "id_class" property for a class
 * </li>
 * <li>
 * instantiation error of a configured class (wraps the actual exception -
 * might be template source implementation specific)
 * </li>
 * <li>
 * propagated from the template source constructor (config errors / missing
 * properties specific to the implementation)
 * </li>
 * </ol>
 * Thrown in the DocumentGenerator class constructor and getInstance() method
 * (see their javadocs for details).
 * </p>
 *
 * <p>
 * Thread Safety : this is immutable and so is thread safe.
 * </p>
 *
 * @author adic, roma
 * @author ShindouHikaru, TCSDEVELOPER
 * @version 2.1
 * @since 2.0
 */
public class InvalidConfigException extends BaseException {
    /**
     * <p>
     * Default constructor.
     * </p>
     * @since 2.0
     */
    public InvalidConfigException() {
        super();
    }

    /**
     * <p>
     * Constructs the exception taking a message as to why it was thrown.
     * </p>
     *
     * <p>
     * The message needs to be meaningful, so that the user will benefit from
     * meaningful messages.
     * </p>
     *
     * @param message A descriptive message of why it was thrown.
     * @since 2.0
     */
    public InvalidConfigException(String message) {
        super(message);
    }

    /**
     * <p>
     * Constructs the exception taking a cause.
     * </p>
     *
     * @param cause A cause why it was thrown.
     * @since 2.0
     */
    public InvalidConfigException(Throwable cause) {
        super(cause);
    }

    /**
     * <p>
     * Constructs the exception taking a message as to why it was thrown,
     * as well as an original cause.
     * </p>
     *
     * <p>
     * The message needs to be meaningful, so that the user will benefit from
     * meaningful messages.
     * </p>
     *
     * <p>
     * The cause is the inner exception.
     * </p>
     *
     * @param message A descriptive message of why it was thrown.
     * @param cause The exception or error that originally caused this to be thrown.
     * @since 2.0
     */
    public InvalidConfigException(String message, Throwable cause) {
        super(message, cause);
    }
}
