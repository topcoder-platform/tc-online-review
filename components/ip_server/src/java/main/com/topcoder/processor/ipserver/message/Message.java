/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.processor.ipserver.message;

/**
 * <p>This interface defines a message which will be transmitted between client and server. It represents both
 * the request and response, and contains only very fundemantal properties, i.e. Request ID, which is used to
 * distinguish different request/response pair, Handler ID, which is used to distinguish different services, and
 * the MessageSerializer type, which is used to serialize/deserialize this message.</p>
 *
 * <p>In order to be used by MessageFactory, implementations should provide a constructor with two String arguments.
 * The first argument is handler ID, and the second argument is request ID. And this constructor can be accessed
 * by MessageFactory instance.</p>
 *
 * <p>Implementations of this interface need not to be thread-safe.</p>
 *
 * @author visualage, zsudraco
 * @version 2.0
 * @copyright Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
public interface Message {
    /**
     * <p>Gets the request ID. It it not null.</p>
     *
     * @return the request ID.
     */
    String getRequestId();

    /**
     * <p>Gets the handler ID. It is not null.</p>
     *
     * @return the handler ID.
     */
    String getHandlerId();

    /**
     * <p>Gets the qualified class name of the serializer used to serialize/deserialize this message.</p>
     *
     * @return the serializer class name used to serialize/deserialize this message.
     */
    String getSerializerType();
}
