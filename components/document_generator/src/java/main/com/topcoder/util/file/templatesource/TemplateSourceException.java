/*
 * Copyright (C) 2007, 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.file.templatesource;

import com.topcoder.util.errorhandling.BaseNonCriticalException;
import com.topcoder.util.errorhandling.ExceptionData;

/**
 * <p>
 * Exception thrown in the DocumentGenerator#getTemplate methods to indicate an error related to
 * getting a template from a template source:
 * <ol>
 * <li>
 * there is no template source with a given id.
 * </li>
 * <li>
 * there is no template with the given name in the template source.
 * </li>
 * <li>
 * other template source implementation specific exception (wrapped in this
 * exception - such as <code>SQLException</code>, <code>IOException</code>).
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
public class TemplateSourceException extends BaseNonCriticalException {

    /**
     * Creates an instance with the specified detail message.
     * @param message
     *            the exception message
     * @since 2.0
     */
    public TemplateSourceException(String message) {
        super(message);
    }

    /**
     * Creates an instance with the inner cause.
     * @param cause
     *            the cause of the exception
     * @since 2.0
     */
    public TemplateSourceException(Throwable cause) {
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
    public TemplateSourceException(String message, Throwable cause) {
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
    public TemplateSourceException(String message, Throwable cause, ExceptionData data) {
        super(message, cause, data);
    }
}
