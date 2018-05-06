/*
 * WindowsPlatformSupport.java
 *
 * Copyright (C) 2002, TopCoder, Inc. All rights reserved
 *
 */

package com.topcoder.util.exec;

/**
 * <p>Encapsulates support for modern versions of the Windows platforms and
 * its derivatives (Windows 2000, XP, etc.).</p>
 *
 * @author srowen
 * @author garyk
 * @version 1.0
 **/
public class WindowsPlatformSupport extends DefaultPlatformSupport {

    /* names of all the modern versions of the Windows platforms */
    public static final String WINDOWS_XP = "Windows XP";
    public static final String WINDOWS_2000 = "Windows 2000";
    public static final String WINDOWS_2003 = "Windows 2003";
    public static final String WINDOWS_VISTA = "Windows Vista";
    public static final String WINDOWS_NT = "Windows NT";

    /**
     * <p>Returns the OS shell components to be prepended to the beginning of
     * the command. It is a separate method so that subclasses can override
     * this to accomodate different Windows versions.</p>
     *
     * <p>This method returns <code>{"cmd", "/C"}</code>.</p>
     *
     * @return <code>{"cmd", "/C"}</code>
     */
    protected String[] getOSShell() {
        return new String[] {"cmd", "/C"};
    }

    /**
     * <p>For Windows systems, use "cmd" to execute commands. This method
     * produces a command like:</p>
     *
     * <p><code>cmd /C command arg1 arg2 ...</code></p>
     *
     * <p>It prepnds additional components to the beginning of the command.</p>
     *
     * @param command the original execution command
     * @return the shell command
     */
    protected String[] makeShellCommand(final String[] command) {
        String[] shellCommand = new String[command.length + 2];
        String[] osShell = getOSShell();
           
        /* add the os shell to the shell command */        
        shellCommand[0] = osShell[0];
        shellCommand[1] = osShell[1];

        /* add the command to the shell command */
        for (int i = 0; i < command.length; i++) {
            shellCommand[i + 2] = command[i];
        }

        return shellCommand;
    }
}
