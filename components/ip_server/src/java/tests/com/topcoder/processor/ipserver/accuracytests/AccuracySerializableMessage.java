/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.processor.ipserver.accuracytests;

import com.topcoder.processor.ipserver.message.serializable.SimpleSerializableMessage;

/**
 * A test extension to see if it really deals with all serializable messages.
 *
 * @author WishingBone
 * @version 2.0
 */
public class AccuracySerializableMessage extends SimpleSerializableMessage {

    /**
     * A magic number to verify.
     */
    private long magicNumber = 0;

    /**
     * Contract constructor.
     *
     * @param handlerId the handler id.
     * @param requestId the request id.
     */
    public AccuracySerializableMessage(String handlerId, String requestId) {
        super(handlerId, requestId);
    }

    /**
     * Set the magic number.
     *
     * @param magicNumber the magic number.
     */
    public void setMagicNumber(long magicNumber) {
        this.magicNumber = magicNumber;
    }

    /**
     * Get the magic number.
     *
     * @return the magic number.
     */
    public long getMagicNumber() {
        return this.magicNumber;
    }

}
