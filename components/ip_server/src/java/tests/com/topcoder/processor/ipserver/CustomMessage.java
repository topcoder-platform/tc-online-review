/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.processor.ipserver;

import com.topcoder.processor.ipserver.message.Message;

/**
 * <p>A Message implementation that does not extend java.io.Serializable, it is used for several tests of
 * this component.</p>
 *
 * @author zsudraco
 * @version 2.0
 * @copyright Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
class CustomMessage implements Message {
    /**
     * Default constructor.
     */
    public CustomMessage() {
    }

    /**
     * Get the handler ID.
     * @return the handler ID.
     */
    public String getHandlerId() {
        return "handler id";
    }

    /**
     * Get the request ID.
     * @return the request ID.
     */
    public String getRequestId() {
        return "request id";
    }

    /**
     * Get the serializer type.
     * @return the serializer type.
     */
    public String getSerializerType() {
        return "serializer type";
    }
}
