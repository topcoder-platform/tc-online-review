/*
 * Copyright (C) 2007, 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.file;

import com.topcoder.util.errorhandling.BaseNonCriticalException;
import com.topcoder.util.errorhandling.ExceptionData;

/**
 * <p>
 * Exception thrown to signal an invalid template in
 * DocumentGenerator.getTemplate, parseTemplate, applyTemplate, getFields and
 * in the Template implementations.
 * <ol>
 * <li>
 * can be signaled by the template processing class (Template instance)
 * </li>
 * <li>
 * other implementation specific exception indicating a template format problem
 * (wrapped in this exception - such as an XSLT exception)
 * </li>
 * </ol>
 * </p>
 *
 * <p>
 * Thread Safety : this is not thread safe, since base class is not thread safe.
 * </p>
 *
 * @author adic, roma
 * @author ShindouHikaru, biotrail
 * @author gniuxiao, TCSDEVELOPER
 * @version 3.0
 * @since 2.1
 */
public class TemplateFormatException extends BaseNonCriticalException {
    /**
     * Creates an instance with the specified detail message.
     * @param message
     *            the exception message
     * @since 2.0
     */
    public TemplateFormatException(String message) {
        super(message);
    }

    /**
     * Creates an instance with the inner cause.
     * @param cause
     *            the cause of the exception
     * @since 2.0
     */
    public TemplateFormatException(Throwable cause) {
        super(cause);
    }

    /**
     * Creates an instance with the specified detail message and inner cause.
     * @param message
     *            the exception message
     * @param cause
     *            the cause of the exception
     * @since 2.0
     */
    public TemplateFormatException(String message, Throwable cause) {
        super(message, cause);
    }
    /**
     * Creates an instance with the specified detail message, the inner cause, and the additional data to
     * attach to the exception.
     * @param data
     *            the additional data to attach to the exception
     * @param message
     *            the exception message
     * @param cause
     *            the cause of the exception
     * @since 3.0
     */
    public TemplateFormatException(String message, Throwable cause, ExceptionData data) {
        super(message, cause, data);
    }
}
