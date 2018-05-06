/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.collection.typesafeenum;

/**
 * <p>
 * Example class for Typesafe Enum pattern implementation.
 * </p>
 * <p>
 * This class should be compileable and public methods for it will be tested in <code>EnumUnitTests</code> class.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.1
 */
public class MyBool extends Enum {

    /**
     * TRUE element for MyBool enum.
     */
    public static final MyBool TRUE = new MyBool();

    /**
     * FALSE element for MyBool enum.
     */
    public static final MyBool FALSE = new MyBool();

    /**
     * The default constructor. It is protected, so it can be called from its sub-class <code>MyTriBool</code>.
     */
    protected MyBool() {
    }
}
