/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.collection.typesafeenum;

/**
 * Represents the Enum class which is ONLY used for test.
 *
 * @author TCSDEVELOPER
 * @version 1.1
 */
public class MockEnum extends Enum {
    /**
     * Calls the default constructor of <code>Enum</code>.
     */
    public MockEnum() {
        super();
    }

    /**
     * Calls the constructor of <code>Enum</code>.
     *
     * @param declaringClass
     *            the declaring class of this enum
     * @throws IllegalArgumentException
     *             if <code>declaringClass</code> is null or is not a sub-class of Enum
     */
    public MockEnum(Class declaringClass) {
        super(declaringClass);
    }
}