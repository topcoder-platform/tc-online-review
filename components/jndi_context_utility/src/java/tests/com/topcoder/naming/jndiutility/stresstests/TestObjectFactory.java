/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.naming.jndiutility.stresstests;

import java.util.Hashtable;

import javax.jms.Queue;
import javax.jms.Topic;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.spi.ObjectFactory;
import javax.sql.DataSource;


/**
 * A factory used to create Topic, Queue an DataSource objects.
 *
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class TestObjectFactory implements ObjectFactory {
    /**
     * Get an object instance.
     *
     * @param obj an object(a Reference)
     * @param name the name of the obj
     * @param nameCtx the context for the nam
     * @param environment the environment.
     *
     * @return an Object null if an Object can not be created.
     *
     * @throws Exception if any Exception occurs
     */
    public Object getObjectInstance(Object obj, Name name, Context nameCtx, Hashtable environment)
        throws Exception {
        //If obj is not a reference just return null
        if (!(obj instanceof Reference)) {
            return null;
        }

        Reference reference = (Reference) obj;
        String className = reference.getClassName();
        RefAddr address = reference.get(0);

        if (className.equals(Topic.class.getName())) {
            return new TopicImpl((String) address.getContent());
        } else if (className.equals(Queue.class.getName())) {
            return new QueueImpl((String) address.getContent());
        } else if (className.equals(DataSource.class.getName())) {
            return new DataSourceImpl((String) address.getContent());
        }

        return null;
    }
}
