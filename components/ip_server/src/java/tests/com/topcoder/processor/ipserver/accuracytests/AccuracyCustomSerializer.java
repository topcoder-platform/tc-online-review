/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.processor.ipserver.accuracytests;

import com.topcoder.processor.ipserver.message.Message;
import com.topcoder.processor.ipserver.message.MessageSerializer;
import com.topcoder.processor.ipserver.message.MessageSerializationException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

/**
 * Accuracy implementation of a serializer that writes handler and request id directly to the stream.
 *
 * @author WishingBone
 * @version 2.0
 */
public class AccuracyCustomSerializer implements MessageSerializer {

    /**
     * Serialize a message.
     *
     * @param message the message to serialize.
     * @param output the stream to write to.
     *
     * @throws MessageSerializationException if serialization fails.
     */
    public void serializeMessage(Message message, OutputStream output) throws MessageSerializationException {
        try {
            ObjectOutputStream objectStream = new ObjectOutputStream(output);
            objectStream.writeObject(message.getHandlerId());
            objectStream.writeObject(message.getRequestId());
            objectStream.flush();
        } catch (IOException ioe) {
            throw new MessageSerializationException("Serialization fails.", ioe);
        }
    }

    /**
     * Deserialize a message.
     *
     * @param input the stream to read from.
     *
     * @return the deserialized message.
     *
     * @throws MessageSerializationException if deserialization fails.
     */
    public Message deserializeMessage(InputStream input) throws MessageSerializationException {
        try {
            ObjectInputStream objectStream = new ObjectInputStream(input);
            return new AccuracyCustomMessage(
                    (String) objectStream.readObject(),
                    (String) objectStream.readObject());
        } catch (IOException ioe) {
            throw new MessageSerializationException("Deserialization fails.", ioe);
        } catch (ClassNotFoundException cnfe) {
            throw new MessageSerializationException("Deserialization fails.", cnfe);
        }
    }

}
