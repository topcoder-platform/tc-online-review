/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.collection.typesafeenum;

/**
 * Test class for Typesafe Enum pattern implementation without any instances implemented to check how <code>Enum</code>
 * will perform with this condition. It must not fail with unknown errors but return valid results.
 *
 * @author TCSDEVELOPER
 * @version 1.1
 */
public class EmptyEnum extends Enum {

    /**
     * The private default constructor.
     */
    private EmptyEnum() {
    }
}
