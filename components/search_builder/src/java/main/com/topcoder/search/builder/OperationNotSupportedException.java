/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder;


/**
 * <p>
 * It is thrown if the intended operation is not supported.
 * For example, if SearchBundle is bounded to LDAP,
 * but user want to call compileStatement() (which is used to compile statement for database),
 * then this exception is thrown.
 * Most mothed in this component will wrap the exception occurs by this Exception
 * </p>
 *
 * @author ShindouHikaru, TCSDEVELOPER
 * @version 1.3
 */
public class OperationNotSupportedException extends SearchBuilderException {
    /**
     * <p>
     * Creates a new instance of this custom exception. This constructor has one argument: message-a
     * descriptive message, the message should include the reason of why the operation is not supported
     * </p>
     *
     *
     *
     * @param message a descriptive message to describe why this exception is generated,
     * it should include the name of unsearchable field
     */
    public OperationNotSupportedException(String message) {
        super(message);
    }

    /**
     * <p>
     * Creates a new instance of this custom exception. This constructor has two arguments: message-a
     * descriptive message(the message should include the reason of why the operation is not supported);
     * cause - the exception(or a chain of exceptions) that generated this exception.
     * </p>
     *
     *
     *
     * @param message a descriptive message to describe why this exception is generated,
     * it should include the name of unsearchable field
     * @param cause the exception(or a chain of exceptions) that generated this exception.
     */
    public OperationNotSupportedException(String message, Throwable cause) {
        super(message, cause);
    }
}
