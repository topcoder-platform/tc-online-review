/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.naming.jndiutility.stresstests;

import javax.jms.Queue;
import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.Referenceable;
import javax.naming.StringRefAddr;


/**
 * A factory for Queue objects
 *
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class QueueImpl implements Referenceable, Queue {
    String name;

    /**
     * Creates a new QueueImpl object.
     *
     * @param name the Name of the Queue
     */
    public QueueImpl(String name) {
        this.name = name;
    }

    /**
     * Return the name of this Queue
     *
     * @return the name of this Queue
     */
    public String getQueueName() {
        return name;
    }

    /**
     * Return a String describing this Queue
     *
     * @return a String describing this Queue
     */
    public String toString() {
        return name;
    }

    /**
     * Get a Reference fo this Object
     *
     * @return a Reference for this Object
     *
     * @throws NamingException if a NamingException occurs
     */
    public Reference getReference() throws NamingException {
        return new Reference(Queue.class.getName(), new StringRefAddr("queue", name),
            TestObjectFactory.class.getName(), null);
    }
}
