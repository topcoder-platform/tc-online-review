/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.naming.jndiutility.stresstests;

import javax.jms.Topic;
import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.Referenceable;
import javax.naming.StringRefAddr;


/**
 * An implementation of the topic interface.
 *
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class TopicImpl implements Referenceable, Topic {
    /** The name of this Object */
    private String name;

    /**
     * Construct a new TopicImpl object
     *
     * @param name the name of the object
     */
    public TopicImpl(String name) {
        this.name = name;
    }

    /**
     * Get the name of this topic
     *
     * @return a String
     */
    public String getTopicName() {
        return name;
    }

    /**
     * Return a String representing this object.
     *
     * @return a String
     */
    public String toString() {
        return name;
    }

    /**
     * Get a Reference for this object
     *
     * @return a Reference
     *
     * @throws NamingException if a NamingException occurs
     */
    public Reference getReference() throws NamingException {
        return new Reference(Topic.class.getName(), new StringRefAddr("topic", name),
            TestObjectFactory.class.getName(), null);
    }
}
