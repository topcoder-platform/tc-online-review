/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.processor.ipserver.accuracytests;

import com.topcoder.processor.ipserver.message.serializable.SimpleSerializableMessage;


/**
 * A simple subclass of SimpleSerializableMessage class.
 *
 * @author air2cold, WishingBone
 * @version 1.0
 */
class AccuracyMessage extends SimpleSerializableMessage {
    /**
     * The message's name attribute.
     */
    private String name = null;

    /**
     * Constructor.
     *
     * @param handlerId the handler id.
     * @param requestId the request id.
     * @param name the name.
     */
    public AccuracyMessage(String handlerId, String requestId, String name) {
        super(handlerId, requestId);
        this.name = name;
    }

    /**
     * Return message's name attribute.
     *
     * @return message's name attribute.
     */
    public String getName() {
        return name;
    }
}
