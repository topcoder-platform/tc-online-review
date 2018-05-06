/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.phase;

/**
 * <p>
 * This class contains helper methods for this component.
 * </p>
 * @author sokol
 * @version 1.1
 * @since 1.1
 */
public final class Helper {

    /**
     * <p>
     * Prevents from instantiating.
     * </p>
     */
    private Helper() {
    }

    /**
     * <p>
     * Checks whether given state is true.
     * </p>
     * @param state the state to check
     * @param message the exception message
     * @throws IllegalArgumentException if given state is true
     */
    static void checkState(boolean state, String message) {
        if (state) {
            throw new IllegalArgumentException(message);
        }
    }
}
