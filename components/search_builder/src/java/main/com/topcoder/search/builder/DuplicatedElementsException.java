/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder;


/**
 * The items insert into the DataSource should not exist before.
 * Which try to add the existed element, this Exception should be thrown.
 * It is thrown if the element to be added already exists.
 *
 * @author ShindouHikaru, TCSDEVELOPER
 * @version 1.3
 */
public class DuplicatedElementsException extends SearchBuilderException {

/**
 * <p>
 * Creates a new instance of this custom exception.
 * This constructor has one argument: message-adescriptive message
 * </p>
 *
 * @param message a descriptive message to describe why this exception is generated
 */
    public  DuplicatedElementsException(String message) {
        super(message);
    }
}
