/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.processor.ipserver.message.serializable;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.Serializable;
import com.topcoder.processor.ipserver.message.MessageSerializer;
import com.topcoder.processor.ipserver.message.Message;
import com.topcoder.processor.ipserver.message.MessageSerializationException;

/**
 * <p>This class implements MessageSerializer interface to provide features to serialize/deserialize messages
 * using Java serialization framework by ObjectInputStream/ObjectOutputStream. Only Message implementations implement
 * java.io.Serializable interface can use this serializer.</p>
 *
 * <p>Thread safety: this class is thread safe.</p>
 *
 * @author visualage, zsudraco
 * @version 2.0
 * @copyright Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
public class SerializableMessageSerializer implements MessageSerializer {

    /**
     * <p>Creates an instance of SerializableMessageSerializer.</p>
     */
    public SerializableMessageSerializer() {
        // Do nothing
    }

    /**
     * <p>Deserializes the byte array to obtain a Message instance. The serialized data is read from the given
     * input stream. If the data cannot be deserialized by any reason, or any error occurrs during deserialization,
     * the error is wrapped into MessageSerializationException.</p>
     * <p>This serializer can only deserialize messages serialized by this class.</p>
     *
     * @param input the input stream where the serialized Message is read from.
     * @return the Message instance which is deserialized from the given data from input stream.
     * @throws NullPointerException if input is null.
     * @throws MessageSerializationException if data cannot be deserialized, or any error occurs when deserializing
     * the data from input.
     */
    public Message deserializeMessage(InputStream input) throws MessageSerializationException {
        if (input == null) {
            throw new NullPointerException("The parameter 'input' should not be null.");
        }

        try {
            // Create an object input stream wrapping the input stream.
            ObjectInputStream ois = new ObjectInputStream(input);
            // Read an object from the object input stream, cast it into Message, and return it.
            return (Message) ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new MessageSerializationException("The class of the message in the input stream is not found.", e);
        } catch (ClassCastException e) {
            throw new MessageSerializationException("The message is not an instance of Message interface.", e);
        } catch (IOException e) {
            throw new MessageSerializationException("An I/O error occurs while deserializing message "
                + e.getMessage(), e);
        } catch (SecurityException e) {
            throw new MessageSerializationException(
                "Fails to deserialize message, untrusted subclass illegally overrides security-sensitive methods.",
                e);
        }
    }

    /**
     * <p>Serializes the given Message instance. The serialized data is written into the given output stream.
     * Any error occurred during serialization is wrapped into MessageSerializationException.</p>
     * <p>This serializer can only serialize messages implementing Serializable interface.</p>
     *
     * @param message the Message instance to be serialized.
     * @param output the output stream where serialized data is written to.
     * @throws NullPointerException if any argument is null.
     * @throws MessageSerializationException if the given message cannot be serialized by this serializer,
     * or any error occurs when serializing the message.
     */
    public void serializeMessage(Message message, OutputStream output) throws MessageSerializationException {
        if (message == null) {
            throw new NullPointerException("The parameter 'message' should not be null.");
        }
        if (output == null) {
            throw new NullPointerException("The parameter 'output' should not be null.");
        }

        // Check if the message implements java.io.Serializable.
        if (!(message instanceof Serializable)) {
            throw new MessageSerializationException("The message does not implement the Serializable interface.");
        }

        try {
            // Create an object output stream wrapping the output stream.
            ObjectOutputStream oos = new ObjectOutputStream(output);
            // Write the message object into the object output stream.
            oos.writeObject(message);
        } catch (IOException e) {
            throw new MessageSerializationException("An I/O error occurs while serializing message "
                + e.getMessage(), e);
        } catch (SecurityException e) {
            throw new MessageSerializationException(
                "Fails to serialize message, untrusted subclass illegally overrides security-sensitive methods.",
                e);
        }
    }
}
