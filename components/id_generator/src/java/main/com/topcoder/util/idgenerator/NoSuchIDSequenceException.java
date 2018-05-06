/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.idgenerator;

/**
 * Thrown whenever a non-existent ID sequence is requested.
 *
 * @author srowen, iggy36, gua
 * @version 3.0
 */
public class NoSuchIDSequenceException extends IDGenerationException {

    /**
     * Creates a new NoSuchIDSequencException.
     */
    public NoSuchIDSequenceException() {
    }

    /**
     * Creates a new NoSuchIDSequencException with the given message.
     *
     * @param message exception message
     */
    public NoSuchIDSequenceException(String message) {
        super(message);
    }
}



