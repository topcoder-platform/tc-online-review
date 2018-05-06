/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 *
 * @(#)PriorityLevel.java
 */
package com.topcoder.message.email;

import com.topcoder.util.collection.typesafeenum.Enum;


/**
 * <p>Type Safe Enumeration of the values that priority can be. All valid priority values if provided by the static
 * instances.</p>
 *
 * <p>This class is thread-safe, since it is immutable.</p>
 *
 * @see TCSEmailMessage#setPriority(PriorityLevel)
 *
 * @since   3.0
 *
 * @author  BEHiker57W
 * @author  smell
 * @version 3.0
 */
public class PriorityLevel extends Enum {

    /**
     * <p>No priority.</p>
     */
    public static final PriorityLevel NONE = new PriorityLevel("none");

    /**
     * <p>Highest priority.</p>
     */
    public static final PriorityLevel HIGHEST = new PriorityLevel("highest");

    /**
     * <p>High priority.</p>
     */
    public static final PriorityLevel HIGH = new PriorityLevel("high");

    /**
     * <p>Normal priority.</p>
     */
    public static final PriorityLevel NORMAL = new PriorityLevel("normal");

    /**
     * <p>Low priority.</p>
     */
    public static final PriorityLevel LOW = new PriorityLevel("low");

    /**
     * <p>Lowest priority.</p>
     */
    public static final PriorityLevel LOWEST = new PriorityLevel("lowest");

    /**
     * <p>String name of the priority. Immutable once created.</p>
     *
     * <p>The purpose of this property is to allow lookups in the configuration file that will show the custom headers
     * associated with the name.</p>
     */
    private final String name;

    /**
     * <p>Private Constructor to prevent outside instantiation. It is called to instantiate the static instances.</p>
     *
     * @param name  name for this instance, this parameter is not check since the constructor is used inside only
     */
    private PriorityLevel(String name) {
        // No argument check, since this method is invoked internally only.
        this.name = name;
    }

    /**
     * <p>Returns name the name of this priority, it corresponds to custom config value for priority.</p>
     *
     * @return the name value for this instance of <code>PriorityLevel</code>
     */
    public String getName() {
        return name;
    }

    /**
     * <p>Returns a string representation of the object. This overrides the default toString to return the name as the
     * string representation. It is equivalent to <code>getName()</code>.</p>
     *
     * @return The name of the <code>PriorityLevel</code>
     */
    public String toString() {
        return name;
    }

}
