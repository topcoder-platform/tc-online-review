/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.processor.ipserver;

/**
 * Mock message class only used for testing.
 *
 * @author zsudraco
 * @version 1.0
 */
class MyMessage extends Message {
    /** The data attribute. */
    private String data = null;

    /**
     * The contructor with handlerId, requestId, data.
     *
     * @param handlerId The handler id
     * @param requestId the request id
     * @param data the data of this message
     */
    MyMessage(String handlerId, String requestId, String data) {
        super(handlerId, requestId);
        this.data = data;
    }

    /**
     * Return the date of this message.
     *
     * @return the date of this message
     */
    public String getData() {
        return data;
    }
}
