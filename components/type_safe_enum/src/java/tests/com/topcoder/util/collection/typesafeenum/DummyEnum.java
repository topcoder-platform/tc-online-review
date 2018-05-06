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
public class DummyEnum extends Enum {

    /**
     * Represents a public static enum.
     */
    public static final DummyEnum PUBLICSTATIC = new DummyEnum();

    /**
     * Represents a static non-public enum.
     */
    static final DummyEnum NOTPUBLIC = new DummyEnum();

    /**
     * Calls the default constructor of <code>Enum</code>.
     */
    public DummyEnum() {
        super();
    }
}