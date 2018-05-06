/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder;


/**
 * <p>
 * This interface includes all the information needed to connect to a data store, such as LDAP and database.
 * It defines APIs to retrieve those information.
 * </p>
 *
 * @author ShindouHikaru, TCSDEVELOPER
 * @version 1.3
 * @deprecated This public class is not used any more
 */
public interface ConnectionInformation {
/**
 * get the factory object used to create a data store connection.
 *
 * @return an object used to create a data store connection
 */
    Object getFactory();
}


