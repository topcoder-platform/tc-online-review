/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.collection.typesafeenum.stresstests;

import com.topcoder.util.collection.typesafeenum.Enum;

/**
 * This class will extend Enum for testing the new feature of version 1.1
 *
 * @author Chenhong
 * @version 1.1
 */
public class MyEnum extends Enum {
    /**
     * Represents the Class.
     */
    private Class Class = null;

    /**
     * An Enum with Integer class as parameter.
     */
    public static final MyEnum ENUM_Integer = new MyEnum(Integer.class);

    /**
     * An Enum with String class as parameter.
     */
    public static final MyEnum ENUM_String = new MyEnum(String.class);

    /**
     * Constructor.
     *
     * @param class
     *            parameter
     */
    private MyEnum(Class class1) {
        this.Class = class1;
    }
}