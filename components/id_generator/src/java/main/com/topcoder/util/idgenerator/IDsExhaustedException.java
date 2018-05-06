/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.idgenerator;

/**
 * Thrown whenever this component cannot generate an ID because a new block
 * of IDs is needed, but there are not enough IDs left in the sequence to
 * allocate another block.
 *
 * @author srowen, iggy36, gua
 * @version 3.0
 */
public class IDsExhaustedException extends IDGenerationException {

    /**
     * Creates a new IDsExhaustedException.
     */
    public IDsExhaustedException() {
    }

    /**
     * Creates a new IDGenerationException with the given message.
     *
     * @param message exception message
     */
    public IDsExhaustedException(String message) {
        super(message);
    }

}



