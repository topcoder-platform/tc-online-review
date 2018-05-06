/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.processor.ipserver;

import java.io.IOException;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import com.topcoder.processor.ipserver.message.Message;
import com.topcoder.processor.ipserver.message.MessageFactory;
import com.topcoder.processor.ipserver.message.MessageSerializationException;

/**
 * This is a helper class used for reading and writing Message object from SocketChannel. In this IPServer
 * implementation. We write the Message object size into channel first, then write the object self into channel. While
 * reading, We read the size first, then read the bytes from channel to deserialize the Message object.
 *
 * @author visualage, zsudraco, FireIce
 * @version 2.0.1
 * @copyright Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
class IOHelper {
    /** The constant length for valid message. */
    static final int VALID_MESSAGE_LENGTH = 65535;

    /** The constant of max port. */
    static final int MAX_PORT = 0xffff;

    /** The number of bytes of message length. */
    static final int MESSAGE_LENGTH = 4;

    /**
     * Prevent this utility class to be constructed.
     */
    private IOHelper() {
    }

    /**
     * Wrap message object with the size of this object into ByteBuffer format. Write size first, then object
     * bytes.
     *
     * @param message The message object to be wrapped.
     * @param messageFactory the message factory to serialize the request into a byte array.
     *
     * @return ByteBuffer array that contains size and bytes content of wrapped message object.
     *
     * @throws IOException if error occurs while writing request to outputStream.
     * @throws MessageSerializationException if the message factory fails to serialize the message.
     */
    static ByteBuffer[] wrapMessage(Message message, MessageFactory messageFactory)
        throws IOException, MessageSerializationException {
        // Use message factory to serialize the message into a byte array
        byte[] bytes = messageFactory.serializeMessage(message);

        // Wrap Length of the bytes
        ByteBuffer lengthBuffer = (ByteBuffer) ByteBuffer.allocate(MESSAGE_LENGTH).putInt(bytes.length).flip();

        // Wrap request bytes
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        return new ByteBuffer[] {lengthBuffer, buffer};
    }

    /**
     * Reading message object from underlying SocketChannel, with size int, message bytes order.
     *
     * @param channel The SocketChannel reading message from.
     * @param messageFactory the message factory to deserialize message from a byte array.
     *
     * @return a Message from the channel.
     *
     * @throws IOException if error occurs while reading from SocketChannel.
     */
    static Message readMessage(SocketChannel channel, MessageFactory messageFactory)
        throws IOException {
        // Read four bytes for the size of this message bytes array
        ByteBuffer buffer = ByteBuffer.allocate(MESSAGE_LENGTH);
        int size = channel.read(buffer);

        if (size == -1) {
            throw new IOException("Reach the end of stream which means connection broken.");
        }
        // Bad format for the protocol of IPServer implementation
        if (buffer.hasRemaining()) {
            return null;
        }

        buffer.flip();
        // Parse the size of message bytes array from these four bytes
        size = buffer.getInt();

        // Impossible size for a message object
        if (size <= 0) {
            return null;
        }

        // This prevent Malicious attack
        if (size > VALID_MESSAGE_LENGTH) {
            size = VALID_MESSAGE_LENGTH;
        }

        // Allocate actual size for the message object
        buffer = ByteBuffer.allocate(size);

        // Read real bytes array from channel to buffer
        size = channel.read(buffer);

        if (size == -1) {
            throw new IOException("Reach the end of stream which means connection broken.");
        }
        // Fails to read from channel for message bytes array
        if (buffer.hasRemaining()) {
            return null;
        }

        buffer.flip();

        try {
            // Use the message factory to deserialize message from the byte array
            return messageFactory.deserializeMessage(buffer.array());
        } catch (MessageSerializationException e) {
            return null;
        }
    }
}
