/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.processor.ipserver.accuracytests;

import com.topcoder.processor.ipserver.message.Message;

/**
 * Accuracy implementation of a message which is not serializable.
 *
 * @author WishingBone
 * @version 2.0
 */
public class AccuracyCustomMessage implements Message {

    /**
     * The handler id.
     */
    private String handlerId = null;

    /**
     * The request id.
     */
    private String requestId = null;

    /**
     * Contract constructor.
     *
     * @param handlerId the handler id.
     * @param requestId the request id.
     */
    public AccuracyCustomMessage(String handlerId, String requestId) {
        this.handlerId = handlerId;
        this.requestId = requestId;
    }

    /**
     * Get the handler id.
     *
     * @return the handler id.
     */
    public String getHandlerId() {
        return this.handlerId;
    }

    /**
     * Get the request id.
     *
     * @return the request id.
     */
    public String getRequestId() {
        return this.requestId;
    }

    /**
     * Return the serializer type.
     *
     * @return the serializer type.
     */
    public String getSerializerType() {
        return AccuracyCustomSerializer.class.getName();
    }

}
