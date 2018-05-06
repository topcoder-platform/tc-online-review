/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.collection.typesafeenum;

/**
 * Test class for Typesafe Enum pattern implementation. It extends from <code>MyBool</code> class.
 *
 * @author TCSDEVELOPER
 * @version 1.1
 */
public class MyTriBool extends MyBool {

    /**
     * UNKNOWN element for MyTriBool enum.
     */
    public static final MyTriBool UNKNOWN = new MyTriBool();

    /**
     * The private default constructor.
     */
    private MyTriBool() {
    }
}
