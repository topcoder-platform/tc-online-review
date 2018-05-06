/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.processor.ipserver.message.serializable;

import java.io.Serializable;
import com.topcoder.processor.ipserver.message.Message;

/**
 * <p>This class provides a simple implementation of a Message. The message uses Java serialization framework
 * by implementing java.io.Serializable interface. It also uses the default serialization behavior of Java
 * serialization framework. The SerializableMessageSerializer class is used as the serializer. There is no data
 * in this message except the mandatory request ID and handler ID.</p>
 *
 * <p>This class is thread-safe, since it is immutable.</p>
 *
 * @author visualage, zsudraco
 * @version 2.0
 * @copyright Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
public class SimpleSerializableMessage implements Serializable, Message {

    /**
     * <p>The qualified class name of the serializer used to serialize/deserialize this message.</p>
     */
    private static final String SERIALIZER_CLASS_NAME = SerializableMessageSerializer.class.getName();

    /**
     * <p>Represents the request ID used to distinguish different request/response pairs.</p>
     */
    private final String requestId;

    /**
     * <p>Represents the handler ID used to distinguish different services.</p>
     */
    private final String handlerId;

    /**
     * <p>Creates a new instance of SimpleSerializableMessage class with the given handler ID and request ID.</p>
     *
     * @param handlerId the handler ID representing the service requested.
     * @param requestId the request ID identifying the request.
     * @throws NullPointerException if any argument is null.
     */
    public SimpleSerializableMessage(String handlerId, String requestId) {
        if (handlerId == null) {
            throw new NullPointerException("The parameter 'handlerId' should not be null.");
        }
        if (requestId == null) {
            throw new NullPointerException("The parameter 'requestId' should not be null.");
        }

        this.handlerId = handlerId;
        this.requestId = requestId;
    }

    /**
     * <p>Gets the handler ID. It it not null.</p>
     *
     * @return the non-null handler ID.
     */
    public String getHandlerId() {
        return handlerId;
    }

    /**
     * <p>Gets the request ID. It is not null.</p>
     *
     * @return the non-null request ID.
     */
    public String getRequestId() {
        return requestId;
    }

    /**
     * <p>Gets the qualified class name of the serializer used to serialize/deserialize this message.</p>
     *
     * @return the serializer class name used to serialize/deserialize this message.
     */
    public String getSerializerType() {
        return SERIALIZER_CLASS_NAME;
    }
}
