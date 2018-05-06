/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.util.memoryusage;

import com.topcoder.util.errorhandling.BaseException;

/**
 * Exception thrown to signal there is no analyzer for the running JVM.
 *
 * <p><b>Thread safety:</b> This class is thread safe since it is immutable.</p>
 *
 * @author BryanChen
 * @author AleaActaEst, TexWiller
 * @version 2.0
 */
public class JVMNotSupportedException extends BaseException {

    /**
     * Constructor with no message.
     */
    public JVMNotSupportedException() {
        super();
    }

    /**
     * Constructor with message.
     *
     * @param message The message of the exception. Can be <code>null</code>
     * or empty.
     */
    public JVMNotSupportedException(String message) {
        super(message);
    }
}

