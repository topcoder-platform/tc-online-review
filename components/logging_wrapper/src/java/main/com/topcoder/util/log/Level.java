/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.log;

import java.util.HashMap;
import java.util.Map;

import com.topcoder.util.collection.typesafeenum.Enum;

/**
 * <p>
 * The Level class maintains the list of acceptable logging levels. It provides the user this easy access to
 * predefined logging levels thought the constants defined in this class.
 * </p>
 *
 * <p>
 * Extends the Enum class from the Typesafe Enumeration component to make serialization safe to be consistent across
 * the TopCoder Catalog and to emulate an enumeration.
 * </p>
 *
 * <p>
 * The levels in descending order are:
 * <ul>
 * <li>OFF (lowest)</li>
 * <li>FATAL</li>
 * <li>ERROR</li>
 * <li>WARN</li>
 * <li>INFO</li>
 * <li>CONFIG</li>
 * <li>DEBUG</li>
 * <li>TRACE</li>
 * <li>FINEST</li>
 * <li>ALL (highest)</li>
 * </ul>
 * </p>
 *
 * <p>
 * <b>Changes from v2.0: </b>
 * Added a convenient parsing method and made the integer final.
 * </p>
 *
 * <p>
 * This class will be used directly by the application when specifying a logging level.
 * </p>
 *
 * <p>
 * <b>Thread Safety: </b>
 * This enumeration is thread safe from being immutable.
 * </p>
 *
 * @author StinkyCheeseMan, TCSDEVELOPER
 * @author adic, ShindouHikaru
 * @author Pops, TCSDEVELOPER
 * @since 1.2
 * @version 2.0
 */
public final class Level extends Enum {

    /**
     * <p>
     * Constant for the OFF logging level (the int value).
     * </p>
     *
     * @since 1.2
     */
    private static final int OFF_LEVEL = 0;

    /**
     * <p>
     * Constant for the FINEST logging level (the int value).
     * </p>
     *
     * @since 1.2
     */
    private static final int FINEST_LEVEL = 100;

    /**
     * <p>
     * Constant for the TRACE logging level (the int value).
     * </p>
     *
     * @since 1.2
     */
    private static final int TRACE_LEVEL = 200;

    /**
     * <p>
     * Constant for the DEBUG logging level (the int value).
     * </p>
     *
     * @since 1.2
     */
    private static final int DEBUG_LEVEL = 300;

    /**
     * <p>
     * Constant for the CONFIG logging level (the int value).
     * </p>
     *
     * @since 1.2
     */
    private static final int CONFIG_LEVEL = 400;

    /**
     * <p>
     * Constant for the INFO logging level (the int value).
     * </p>
     *
     * @since 1.2
     */
    private static final int INFO_LEVEL = 500;

    /**
     * <p>
     * Constant for the WARN logging level (the int value).
     * </p>
     *
     * @since 1.2
     */
    private static final int WARN_LEVEL = 600;

    /**
     * <p>
     * Constant for the ERROR logging level (the int value).
     * </p>
     *
     * @since 1.2
     */
    private static final int ERROR_LEVEL = 700;

    /**
     * <p>
     * Constant for the FATAL logging level (the int value).
     * </p>
     *
     * @since 1.2
     */
    private static final int FATAL_LEVEL = 800;

    /**
     * <p>
     * Constant for the ALL logging level (the int value).
     * </p>
     *
     * @since 1.2
     */
    private static final int ALL_LEVEL = 900;

    /**
     * <p>
     * Represents the human-readable representations of all the levels, whose sequence must be the same as the LEVELS
     * array.
     * </p>
     *
     * <p>
     * It is only used in <code>parseLevel</code> method.
     * </p>
     *
     * @since 2.0
     */
    private static final String[] LEVEL_NAMES = new String[] {
        "OFF", "FINEST", "TRACE", "DEBUG", "CONFIG", "INFO", "WARN", "ERROR", "FATAL", "ALL"
    };

    /**
     * <p>
     * Represents the constant int values of all the levels, whose sequence must be the same as the LEVELS
     * array.
     * </p>
     *
     * <p>
     * It is only used in <code>parseLevel</code> method.
     * </p>
     *
     * @since 2.0
     */
    private static final int[] LEVEL_VALUES = new int[] {
        OFF_LEVEL, FINEST_LEVEL, TRACE_LEVEL, DEBUG_LEVEL, CONFIG_LEVEL, INFO_LEVEL, WARN_LEVEL, ERROR_LEVEL,
        FATAL_LEVEL, ALL_LEVEL
    };

    /**
     * <p>
     * Public constant for the OFF logging level.
     * </p>
     *
     * @since 1.2
     */
    public static final Level OFF = new Level(OFF_LEVEL);

    /**
     * <p>
     * Public constant for the FINEST logging level.
     * </p>
     *
     * @since 1.2
     */
    public static final Level FINEST = new Level(FINEST_LEVEL);

    /**
     * <p>
     * Public constant for the TRACE logging level.
     * </p>
     *
     * @since 1.2
     */
    public static final Level TRACE = new Level(TRACE_LEVEL);

    /**
     * <p>
     * Public constant for the DEBUG logging level.
     * </p>
     *
     * @since 1.2
     */
    public static final Level DEBUG = new Level(DEBUG_LEVEL);

    /**
     * <p>
     * Public constant for the CONFIG logging level.
     * </p>
     *
     * @since 1.2
     */
    public static final Level CONFIG = new Level(CONFIG_LEVEL);

    /**
     * <p>
     * Public constant for the INFO logging level.
     * </p>
     *
     * @since 1.2
     */
    public static final Level INFO = new Level(INFO_LEVEL);

    /**
     * <p>
     * Public constant for the WARN logging level.
     * </p>
     *
     * @since 1.2
     */
    public static final Level WARN = new Level(WARN_LEVEL);

    /**
     * <p>
     * Public constant for the ERROR logging level.
     * </p>
     *
     * @since 1.2
     */
    public static final Level ERROR = new Level(ERROR_LEVEL);

    /**
     * <p>
     * Public constant for the FATAL logging level.
     * </p>
     *
     * @since 1.2
     */
    public static final Level FATAL = new Level(FATAL_LEVEL);

    /**
     * <p>
     * Public constant for the ALL logging level.
     * </p>
     *
     * @since 1.2
     */
    public static final Level ALL = new Level(ALL_LEVEL);

    /**
     * <p>
     * Represents an array containing all the levels.
     * </p>
     *
     * <p>
     * It is only used in <code>parseLevel</code> method.
     * </p>
     *
     * @since 2.0
     */
    private static final Level[] LEVELS = new Level[] {
        OFF, FINEST, TRACE, DEBUG, CONFIG, INFO, WARN, ERROR, FATAL, ALL
    };

    /**
     * <p>
     * Represents a Map used to search the level according the integer value of the level(trim'ed), as returned by
     * intValue() method, or the string equivalent(trim'ed and case insensitive), as returned by the toString() method..
     * </p>
     */
    private static final Map LEVEL_MAP = new HashMap();

    static {
        // initialize the map, note for integer value, the key of the map should not use the String representation,
        // for example "800", but should use new Integer(800), because users may query the level according 800.0
        for (int i = 0; i < LEVEL_VALUES.length; i++) {
            LEVEL_MAP.put(new Integer(LEVEL_VALUES[i]), LEVELS[i]);
            LEVEL_MAP.put(LEVEL_NAMES[i], LEVELS[i]);
        }
    }

    /**
     * <p>
     * Member field to indicate the value of the level of this instance.
     * </p>
     *
     * <p>
     * This value is set by the constructor, is immutable and will only be one of the XXX_LEVEL constants.
     * This variable is referenced in all the methods.
     * </p>
     *
     * <p>
     * <b>Changes in v2.0: </b>
     * This field was made final.
     * </p>
     *
     * @since 1.2
     */
    private final int level;

    /**
     * <p>
     * Constructor is private to prevent instantiation outside of the class.
     * </p>
     *
     * <p>
     * <b>Changes to v2.0: </b>
     * No changes were made.
     * </p>
     *
     * @param level The value of this logging level.
     *
     * @since 1.2
     */
    private Level(int level) {
        this.level = level;
    }

    /**
     * <p>
     * This method returns the integer representation of the level.
     * </p>
     *
     * <p>
     * <b>Changes to v2.0: </b>
     * No changes were made.
     * </p>
     *
     * @return the integer representation of the level
     *
     * @since 1.2
     */
    public final int intValue() {
        return level;
    }

    /**
     * <p>
     * Overrides the equals method to allow Level objects to be compared for
     * equality.
     * </p>
     *
     * <p>
     * <b>Changes to v2.0: </b>
     * No changes were made.
     * </p>
     *
     * @param level the level object to be compared to this object for equality
     *
     * @return true if the Level objects are equal, false otherwise
     *
     * @since 1.2
     */
    public final boolean equals(Object level) {
        // if it is this, simply return true
        if (level == this) {
            return true;
        }
        // check if the level is of Level, return false if it is false
        if (!(level instanceof Level)) {
            return false;
        }

        Level comparisonLevel = (Level) level;
        // compare them with their int value, return true if they are equal
        return (comparisonLevel.intValue() == this.level);
    }

    /**
     * <p>
     * Overrides the hashCode method (required because equals is overridden).
     * </p>
     *
     * <p>
     * <b>Changes to v2.0: </b>
     * No changes were made.
     * </p>
     *
     * @return the hash code for this instance (the value of the level attribute)
     *
     * @since 1.2
     */
    public final int hashCode() {
        return level;
    }

    /**
     * <p>
     * Overrides the toString method to return a human-readable representation of the Level object.
     * </p>
     *
     * <p>
     * <b>Changes to v2.0: </b>
     * No changes were made.
     * </p>
     *
     * @return String - human-readable representation of the Level
     *
     * @since 1.2
     */
    public final String toString() {
        if (this == OFF) {
            return "OFF";
        } else if (this == FINEST) {
            return "FINEST";
        } else if (this == TRACE) {
            return "TRACE";
        } else if (this == DEBUG) {
            return "DEBUG";
        } else if (this == CONFIG) {
            return "CONFIG";
        } else if (this == INFO) {
            return "INFO";
        } else if (this == WARN) {
            return "WARN";
        } else if (this == ERROR) {
            return "ERROR";
        } else if (this == FATAL) {
            return "FATAL";
        } else if (this == ALL) {
            return "ALL";
        } else {
            return "INVALID LEVEL";
        }
    }

    /**
     * <p>
     * Parses the string into a level or returns null if the string could not be parsed.
     * </p>
     *
     * <p>
     * The string can either be the integer value of the level(trim'ed), as returned by intValue() method, or the
     * string equivalent(trim'ed and case insensitive), as returned by the toString() method.
     * </p>
     *
     * <p>
     * <b>Changes for v2.0: </b>
     * This is a new method for this class.
     * </p>
     *
     * <p>
     * Not that null will be returned if the level is not found.
     * </p>
     *
     * @param level A possibly null, possibly empty (trim'd) string representing the level (either it's string or
     *  integer representation)
     *
     * @return A non-null if the string was parsed to a Level or null if it is not found
     *
     * @since 2.0
     */
    public static Level parseLevel(String level) {
        // if the level is null, simply return null
        if (level == null) {
            return null;
        }
        // trim the level
        level = level.trim();
        // return null if it is empty
        if (level.length() == 0) {
            return null;
        }
        try {
            // firstly try to parse it to an Integer
            int value = Integer.parseInt(level);
            // search it from the level map
            return (Level) LEVEL_MAP.get(new Integer(value));
        } catch (NumberFormatException e) {
            // if the level string could not be parsed to an Integer, try to parse it as the name of the level
            return (Level) LEVEL_MAP.get(level.toUpperCase());
        }
    }
}
