/*
 * Copyright (c) 2005, TopCoder, Inc. All rights reserved.
 */
package com.topcoder.processor.ipserver.failuretests;

import com.topcoder.processor.ipserver.message.Message;

/**
 * MockMessage implementation without Serializable.
 *
 * @author brain_cn
 * @version 2.0
 */
public class MockMessageNotSerializable implements Message {
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
    public MockMessageNotSerializable() {
    }
    /**
     * The contructor with handlerId, requestId.
     *
     * @param handlerId The handler id
     * @param requestId the request id
     */
    public MockMessageNotSerializable(String handlerId, String requestId) {
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
