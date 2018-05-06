/*
 * Copyright (c) 2005, TopCoder, Inc. All rights reserved.
 */
package com.topcoder.processor.ipserver.failuretests;

import java.io.Serializable;

import com.topcoder.processor.ipserver.message.Message;


/**
 * MockMessage implementation.
 *
 * @author brain_cn
 * @version 2.0
 */
public class MockMessage implements Message, Serializable {
    /**
     * The handler id.
     */
    private String handlerId = "mock handler id";

    /**
     * The request id.
     */
    private String requestId = "mock request id";

    /**
     * Default Constructor without any arguments.
     *
     */
    public MockMessage() {        
    }
    /**
     * The contructor with handlerId, requestId.
     *
     * @param handlerId The handler id
     * @param requestId the request id
     */
    public MockMessage(String handlerId, String requestId) {
            this.handlerId = handlerId;
            this.requestId = requestId;
    }
    /**
     * Mock implementation.
     *
     * @return requestId
     */
    public String getRequestId() {
        return requestId;
    }

    /**
     * Mock implementation.
     *
     * @return the handler id
     */
    public String getHandlerId() {
        return handlerId;
    }

    /**
     * Mock implementation.
     *
     * @return the serializer type.
     */
    public String getSerializerType() {
        return "Mock Type";
    }
}
