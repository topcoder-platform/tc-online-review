/*
 * UnixPlatformSupport.java
 *
 * Copyright (C) 2002, TopCoder, Inc. All rights reserved
 *
 */

package com.topcoder.util.exec;

/**
 * <p>Encapsulates support for Unix-like platforms 
 * (Linux, OS X, Solaris, etc.).</p>
 *
 * @author srowen
 * @author garyk
 * @version 1.0
 */
public class UnixPlatformSupport extends DefaultPlatformSupport {

    /* names of all the Unix-like platforms */
    public static final String LINUX = "Linux";
    public static final String SOLARIS_1 = "SunOS";
    public static final String SOLARIS_2 = "Solaris";
    public static final String MAC_OS_X = "Mac OS X";
    public static final String HP_UX = "HP-UX";
    public static final String FREE_BSD = "FreeBSD";
    public static final String AIX = "AIX";
    public static final String IRIX = "IRIX";
    public static final String OS_2 = "OS/2";

    /**
     * <p>For Unix-like systems, use "sh" to execute commands. This method
     * produces a command like:</p>
     *
     * <p><code>sh -c "command arg1 arg2 ..."</code></p>
     *
     * <p>Note that it needs to "join" the components of the given command
     * into one String, separated by spaces, and use that as the third
     * component of the result command, after "sh" and "-c".</p>
     *
     * @return the shell command
     */
    protected String[] makeShellCommand(final String[] command) {
        String[] shellCommand = new String[3];
              
        /* add the os shell to the shell command */
        shellCommand[0] = "sh";
        shellCommand[1] = "-c";
        
        /* add the command to the shell command as the third component */
        shellCommand[2] = command[0];
        for (int i = 1; i < command.length; i++) {
            shellCommand[2] += " " + command[i];
        }
        return shellCommand;
    }
}
