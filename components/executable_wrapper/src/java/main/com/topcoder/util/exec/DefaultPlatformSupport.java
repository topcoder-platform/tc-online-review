package com.topcoder.util.exec;

/**
 * <p>Provides default method implementations that are typically overridden
 * by subclasses. This implementation is used for unknown platforms, and so
 * it should try to provide behavior that would work in most situations on
 * most platforms.</p>
 */
public class DefaultPlatformSupport extends PlatformSupport {

    /**
     * Default implementation - just returns its input unchanged.
     */
    protected String[] makeShellCommand(final String[] command) {
        return command;
    }

}