/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.processor.ipserver;

import com.topcoder.processor.ipserver.message.Message;
import com.topcoder.processor.ipserver.message.serializable.SimpleSerializableMessage;

/**
 * <p>A custom serializable message.</p>
 *
 * @author zsudraco
 * @version 2.0
 * @copyright Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
public class CustomSerializableMessage extends SimpleSerializableMessage {
    /**
     * <p>Represents the custom data.</p>
     */
    private byte customData;

    /**
     * <p>Creates a new instance of CustomSerializableMessage class with the given handler ID and request ID.</p>
     *
     * @param handlerId the handler ID representing the service requested.
     * @param requestId the request ID identifying the request.
     */
    public CustomSerializableMessage(String handlerId, String requestId) {
        super(handlerId, requestId);
        this.customData = 0;
    }

    /**
     * Get the custom data.
     * @return the custom data.
     */
    public byte getData() {
        return customData;
    }

    /**
     * Set the custom data.
     * @param data the data to set
     */
    public void setData(byte data) {
        customData = data;
    }

    /**
     * Get the serializer type.
     * @return the serializer type.
     */
    public String getSerializerType() {
        return CustomSerializableMessageSerializer.class.getName();
    }
}
