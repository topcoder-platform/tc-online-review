/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.processor.ipserver.message;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Iterator;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.UTFDataFormatException;
import com.topcoder.processor.ipserver.ConfigurationException;
import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.config.Property;
import com.topcoder.util.config.ConfigManagerException;
import com.topcoder.util.config.UnknownNamespaceException;

/**
 * <p>This class implements the MessageFactory interface. It has a mapping which maps message names to class
 * names implementing Message interface. The mapping is loaded from the configuration. It can create messages
 * in two ways, either through the name of a message type, or through deserializing a byte array. It also provide
 * a method to serialize/deserialize a message properly.</p>
 *
 * <p>Property 'MessageTypes' is mandatory. It must contain at least one nested property.
 * The names of the nested properties are the names of the messages implementations (the key in mapping),
 * the values of the nested properties are the qualified class names of the message implementations.</p>
 *
 * <p>
 * Sample configuration file:
 * <pre>
 * &lt;?xml version="1.0" encoding="UTF-8"?&gt;
 * &lt;CMConfig&gt;
 *     &lt;!-- The mapping between names and message class names, required --&gt;
 *     &lt;Property name="MessageTypes"&gt;
 *       &lt;!-- Property name is the name, property value is the class name.There must be at
 *          least one of this property. --&gt;
 *       &lt;Property name="simple"&gt;
 *         &lt;Value&gt;com.topcoder.processor.ipserver.message.serializable.SimpleSerializableMessage&lt;/Value&gt;
 *       &lt;/Property&gt;
 *       &lt;Property name="KeepAlive"&gt;
 *         &lt;Value&gt;com.topcoder.processor.ipserver.message.serializable.SimpleSerializableMessage&lt;/Value&gt;
 *       &lt;/Property&gt;
 *     &lt;/Property&gt;
 * &lt;/CMConfig&gt;
 * </pre>
 * </p>
 *
 * <p>This class is thread-safe.</p>
 *
 * @author visualage, zsudraco
 * @version 2.0
 * @copyright Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
public class DefaultMessageFactory implements MessageFactory {

    /**
     * <p>
     * Represents the name of message types property.
     * </p>
     */
    private static final String MESSAGE_TYPES_PROPERTY = "MessageTypes";

    /**
     * <p>Represents the mapping between names (key) and classes of the Message implementations. The names cannot
     * be null or empty string.</p>
     */
    private final Map messageTypeMap = new HashMap();

    /**
     * <p>Represents a mapping of serializers. The keys are qualified class name of serializer implementations, and
     * the values are the instance of this serializer implementation.</p>
     */
    private final Map serializerMap = new HashMap();

    /**
     * <p>Creates a new instance of DefaultMessageFactory class. The configuration values are loaded from
     * the given namespace. Document for this class contains a sample configuration file.</p>
     *
     * @param namespace the namespace to retrieve message types in configuration file.
     * @throws NullPointerException if namespace is null.
     * @throws IllegalArgumentException if namespace is empty string.
     * @throws ConfigurationException if any error occurs when reading configuration; or configuration value is invalid.
     */
    public DefaultMessageFactory(String namespace) throws ConfigurationException {
        if (namespace == null) {
            throw new NullPointerException("The parameter 'namespace' should not be null.");
        }
        if (namespace.trim().length() == 0) {
            throw new IllegalArgumentException("The parameter 'namespace' should not be empty.");
        }

        try {
            // Get the instance of configuration manager.
            ConfigManager cm = ConfigManager.getInstance();

            // Get the property object of 'MessageTypes' in the namespace.
            Property msgTypes = cm.getPropertyObject(namespace, MESSAGE_TYPES_PROPERTY);
            if (msgTypes == null) {
                throw new ConfigurationException("The property " + MESSAGE_TYPES_PROPERTY
                    + " doesn't exist in the namespace " + namespace);
            }
            List properties = msgTypes.list();
            // There should be at least one message type in the 'MessageTypes' property.
            if (properties.size() == 0) {
                throw new ConfigurationException(
                    "There should be at least one message type in the 'MessageTypes' property.");
            }
            // For each nested property, the name and value, create the message type by reflection,
            // then put the name and message type to the message type mapping.
            for (Iterator types = properties.iterator(); types.hasNext();) {
                Property type = (Property) types.next();
                // Result of getName() is a String that is not null nor empty.
                String name = type.getName();
                String value = type.getValue();
                if (value == null) {
                    throw new ConfigurationException(
                        "There is no value for message type property " + name + ".");
                }
                // Construct the message type class by reflection.
                Class msgClass = Class.forName(value);
                // Tests if msgClass is a concrete class that implements Message interface.
                if (!isConcreteMessage(msgClass)) {
                    throw new ConfigurationException("The class " + value
                        + " is not a concrete class that implements Message interface.");
                }
                // Update the message type mapping.
                messageTypeMap.put(name, msgClass);
            }
        } catch (ConfigurationException e) {
            // Rethrow it.
            throw e;
        } catch (UnknownNamespaceException e) {
            throw new ConfigurationException(
                "The namespace " + namespace + " doesn't exist in the configuration manager.", e);
        } catch (ClassNotFoundException e) {
            throw new ConfigurationException(
                "The class of language chooser doesn't exist.", e);
        }
    }

    /**
     * <p>Tests if the given Class instance is a concrete class that implements Message interface.</p>
     *
     * <p>Caller should ensure the given cls is not null.</p>
     *
     * @param cls the class to test, it must not null.
     * @return a boolean value indicating whether the given cls is a concrete class that implements Message interface.
     */
    private static boolean isConcreteMessage(Class cls) {
        // Tests if the class implements Message interface.
        if (!Message.class.isAssignableFrom(cls)) {
            return false;
        }
        int modifier = cls.getModifiers();
        // the cls should not be an interface or abstract class.
        return !Modifier.isAbstract(modifier) && !Modifier.isInterface(modifier);
    }

    /**
     * <p>Creates a message instance according to the given name, handler ID and request ID.</p>
     *
     * @param messageTypeName the name of message type which should be created.
     * @param handlerId the handler ID of the new message instance.
     * @param requestId the request ID of the new message instance.
     * @return a created new message instance.
     * @throws NullPointerException if any argument is null.
     * @throws IllegalArgumentException if messageTypeName is empty string.
     * @throws UnknownMessageException if the messageTypeName cannot be found in the factory.
     * @throws MessageCreationException if any error occurs when creating the new message instance.
     */
    public Message getMessage(String messageTypeName, String handlerId, String requestId)
        throws MessageCreationException {
        if (messageTypeName == null) {
            throw new NullPointerException("The parameter 'messageTypeName' should not be null.");
        }
        if (messageTypeName.trim().length() == 0) {
            throw new IllegalArgumentException("The parameter 'messageTypeName' should not be empty.");
        }
        if (handlerId == null) {
            throw new NullPointerException("The parameter 'handlerId' should not be null.");
        }
        if (requestId == null) {
            throw new NullPointerException("The parameter 'requestId' should not be null.");
        }

        synchronized (messageTypeMap) {
            // Get the class corresponding to the messageTypeName in the messsage type mapping.
            Class msgType = (Class) messageTypeMap.get(messageTypeName);
            if (msgType == null) {
                throw new UnknownMessageException("The messageTypeName cannot be found in the factory.");
            }
            try {
                // Create the message by reflection and return it.
                return (Message) msgType.getConstructor(new Class[]{String.class, String.class}).
                    newInstance(new Object[]{handlerId, requestId});
            } catch (NoSuchMethodException e) {
                throw new MessageCreationException(
                    "The message class doesn't have a constructor accepting a two Strings.", e);
            } catch (SecurityException e) {
                throw new MessageCreationException(
                    "Access to the constructor of message class is denied.", e);
            } catch (IllegalAccessException e) {
                throw new MessageCreationException(
                    "Constructor of message class is inaccessible.", e);
            } catch (InstantiationException e) {
                throw new MessageCreationException(
                    "The message class is an abstract class.", e);
            } catch (InvocationTargetException e) {
                throw new MessageCreationException(
                    "Constructor of message class throws an exception.", e);
            } catch (ExceptionInInitializerError e) {
                throw new MessageCreationException(
                    "Initialization of message class fails.", e);
            } catch (ClassCastException e) {
                throw new MessageCreationException(
                    "The message class does not implement the Message interface.", e);
            }
        }
    }

    /**
     * <p>This is a helper method to get a message serializer instance from the class name.</p>
     *
     * <p>Caller should ensure the parameter className is not null.</p>
     *
     * @param className the qualified serializer class name, it must not be null.
     * @return a new message serializer instance created via reflection from the serializer class name.
     * @throws MessageSerializationException if any error occurs when getting the serializer.
     */
    private MessageSerializer getSerializer(String className) throws MessageSerializationException {
        // Get the serializer from the map
        MessageSerializer serializer = (MessageSerializer) serializerMap.get(className);

        // If the serializer is not in the mapping, create a new instance via reflection and add it in the map.
        if (serializer == null) {
            try {
                serializer = (MessageSerializer) Class.forName(className).newInstance();
            } catch (ClassNotFoundException e) {
                throw new MessageSerializationException("Cannot find the class " + className, e);
            } catch (IllegalAccessException e) {
                throw new MessageSerializationException(
                    "Fails to create serializer, the class or its nullary constructor is not accessible.", e);
            } catch (InstantiationException e) {
                throw new MessageSerializationException(
                    "Fails to create serializer, this Class represents an abstract class, an interface, "
                    + "an array class, a primitive type, or void, or if the class has no nullary constructor,"
                    + " or if the instantiation fails for some other reason.", e);
            } catch (ExceptionInInitializerError e) {
                throw new MessageSerializationException(
                    "Fails to create serializer, the initialization provoked by this method fails.", e);
            } catch (SecurityException e) {
                throw new MessageSerializationException(
                    "Fails to create serializer, there is no permission to create a new instance.", e);
            } catch (ClassCastException e) {
                throw new MessageSerializationException(
                    "Fails to create serializer, the serializer does not implement MessageSerializer interface.",
                    e);
            }

            serializerMap.put(className, serializer);
        }

        return serializer;
    }

    /**
     * <p>Creates a message instance according to the serialized data. This method is able to deserialize the data
     * that is serialized by the serializeMessage method of this class.</p>
     *
     * @param data the byte array representing the serialized data.
     * @return a new message instance created from the serialized data.
     * @throws NullPointerException if data is null.
     * @throws IllegalArgumentException if data is an empty byte array.
     * @throws MessageSerializationException if any error occurs when deserializing the data.
     */
    public Message deserializeMessage(byte[] data) throws MessageSerializationException {
        if (data == null) {
            throw new NullPointerException("The parameter 'data' should not be null.");
        }
        if (data.length == 0) {
            throw new IllegalArgumentException("The parameter 'data' should not be empty.");
        }

        // Create a byte array input stream wrapping the given array.
        InputStream bytesInput = new ByteArrayInputStream(data);
        // Wrap the byte array input stream with DataInputStream.
        DataInputStream input = new DataInputStream(bytesInput);

        // Read the serializer class name from the stream
        String serializerClassName;
        try {
            serializerClassName = input.readUTF();
        } catch (EOFException e) {
            throw new MessageSerializationException(
                "Fails to get class name of serializer, input stream reaches the end before reading all the bytes.",
                e);
        } catch (UTFDataFormatException e) {
            throw new MessageSerializationException(
                "Fails to get class name of serializer, the bytes don't represent a valid UTF-8 encoding string.",
                e);
        } catch (IOException e) {
            throw new MessageSerializationException(
                "Fails to get class name of serializer, an I/O error occurs.", e);
        }

        MessageSerializer serializer;
        synchronized (serializerMap) {
            // Get the serializer from the class name.
            serializer = getSerializer(serializerClassName);
        }

        Message message;
        synchronized (serializer) {
            // Deserialize the message with the serializer,
            // MessageSerializationException may be thrown from the serializer if any error occurs during serialization.
            message = serializer.deserializeMessage(input);
        }

        // Close the stream and return message
        try {
            input.close();
        } catch (IOException e) {
            // Ignore error after the message has been successfully instantiated.
        }
        return message;
    }

    /**
     * <p>
     * Serializes the given Message instance. The serialization is done by the serializer specified in the given
     * Message instance. Any error occurrs during serialization should be wrapped into MessageSerializationException.
     * </p>
     *
     * @param message the Message instance to be serialized.
     * @return the serialized Message.
     * @throws NullPointerException if message is null.
     * @throws MessageSerializationException if any error ocurs when serializing the message.
     */
    public byte[] serializeMessage(Message message) throws MessageSerializationException {
        if (message == null) {
            throw new NullPointerException("The parameter 'message' should not be null.");
        }

        // Get serializer class name from the message.
        String serializerClassName = message.getSerializerType();

        // Create a byte array output stream.
        OutputStream bytesOutput = new ByteArrayOutputStream();
        // Wrap the byte array output stream with DataOutputStream.
        DataOutputStream output = new DataOutputStream(bytesOutput);

        // Write the class name first
        try {
            output.writeUTF(serializerClassName);
        } catch (IOException e) {
            throw new MessageSerializationException(
                "Fails to write class name of serializer, an I/O error occurs.", e);
        }

        MessageSerializer serializer;
        synchronized (serializerMap) {
            // Get the serializer from the class name.
            serializer = getSerializer(serializerClassName);
        }

        synchronized (serializer) {
            // Serialize message, MessageSerializationException may be thrown from the serializer if any error occurs
            // during serialization.
            serializer.serializeMessage(message, output);
        }

        // Close the stream and return byte array data
        try {
            output.close();
        } catch (IOException e) {
            // Ignore error after the message has been successfully instantiated.
        }
        return ((ByteArrayOutputStream) bytesOutput).toByteArray();
    }

    /**
     * <p>Adds a pair of name and message type into the mapping. If the name already exists,
     * the old message type is simply replaced by the new one.</p>
     *
     * @param messageTypeName the name of the message type to be added.
     * @param messageType the message type to be added.
     * @throws NullPointerException if any argument is null.
     * @throws IllegalArgumentException if name is empty string, or the messageType is not an concrete implementation
     * of Message.
     */
    public void add(String messageTypeName, Class messageType) {
        if (messageTypeName == null) {
            throw new NullPointerException("The parameter 'messageTypeName' should not be null.");
        }
        if (messageType == null) {
            throw new NullPointerException("The parameter 'messageType' should not be null.");
        }
        if (messageTypeName.trim().length() == 0) {
            throw new IllegalArgumentException("The parameter 'messageTypeName' should not be empty.");
        }
        if (!isConcreteMessage(messageType)) {
            throw new IllegalArgumentException(
                "The parameter 'messageType': "
                + messageType.getName() + " is not a concrete class that implements Message interface.");
        }

        synchronized (messageTypeMap) {
            messageTypeMap.put(messageTypeName, messageType);
        }
    }

    /**
     * <p>Removes a message type associated with the given name. If the name does not exist,
     * nothing happens.</p>
     *
     * @param messageTypeName the name of the message type to be removed.
     * @throws NullPointerException if name is null.
     * @throws IllegalArgumentException if name is empty string.
     */
    public void remove(String messageTypeName) {
        if (messageTypeName == null) {
            throw new NullPointerException("The parameter 'messageTypeName' should not be null.");
        }
        if (messageTypeName.trim().length() == 0) {
            throw new IllegalArgumentException("The parameter 'messageTypeName' should not be empty.");
        }

        synchronized (messageTypeMap) {
            messageTypeMap.remove(messageTypeName);
        }
    }

    /**
     * <p>Gets the message type associated with the given name. If the name does not exist,
     * null is returned.</p>
     *
     * @param messageTypeName the name to get a message type.
     * @return the message type associated with the given name or null if the name does not exist.
     * @throws NullPointerException if name is null.
     * @throws IllegalArgumentException if name is an empty string.
     */
    public Class get(String messageTypeName) {
        if (messageTypeName == null) {
            throw new NullPointerException("The parameter 'messageTypeName' should not be null.");
        }
        if (messageTypeName.trim().length() == 0) {
            throw new IllegalArgumentException("The parameter 'messageTypeName' should not be empty.");
        }

        synchronized (messageTypeMap) {
            return (Class) messageTypeMap.get(messageTypeName);
        }
    }

    /**
     * <p>Gets a flag indicating whether the factory contains a message type associated with the name.</p>
     *
     * @param messageTypeName the name to be checked.
     * @return true if the name exists in the factory; false otherwise.
     * @throws NullPointerException if name is null.
     * @throws IllegalArgumentException if name is an empty string.
     */
    public boolean contains(String messageTypeName) {
        if (messageTypeName == null) {
            throw new NullPointerException("The parameter 'messageTypeName' should not be null.");
        }
        if (messageTypeName.trim().length() == 0) {
            throw new IllegalArgumentException("The parameter 'messageTypeName' should not be empty.");
        }

        synchronized (messageTypeMap) {
            return messageTypeMap.containsKey(messageTypeName);
        }
    }

    /**
     * <p>Gets a map of names and message types in this factory. Modifying the result Map will not affect
     * the internal data of this class.</p>
     *
     * @return a map of names and message types in this factory.
     */
    public Map getMessageTypes() {
        synchronized (messageTypeMap) {
            return new HashMap(messageTypeMap);
        }
    }

    /**
     * <p>Clears all message types in this factory.</p>
     */
    public void clear() {
        synchronized (messageTypeMap) {
            messageTypeMap.clear();
        }
    }
}
