/*
 * PlatformSupport.java
 *
 * Copyright (C) 2002, TopCoder, Inc. All rights reserved
 *
 */

package com.topcoder.util.exec;

import java.util.HashMap;
import java.util.Properties;

/**
 * <p>Subclasses of this class encapsulate platform-specific behavior that
 * is needed when executing a command. Currently it encapsulates the 
 * platform's notion of a shell, which is used to execute commands.</p>
 *
 * @author srowen
 * @author garyk
 * @version 1.0     
 */
public abstract class PlatformSupport {

    /* 
     * the name of the parameter to obtain the OS name from the property
     * of the System
     */
    private static final String OS_NAME = "os.name";
    /*  the PlatformSupport instance */
    private static PlatformSupport instance;  
    /* the Map to store all the PlatformSupports */
    private static HashMap platformMap;  
    /* the flag to decide whether a new platform is registered */
    private static boolean isNewPlatformRegistered;

    /*
     * Initialization of variables
     */
    static {
        isNewPlatformRegistered = false;

        /*
         * initialize the HashMap for the platforms
         */
        platformMap = new HashMap();

        /* modern Windows platforms */
        WindowsPlatformSupport winSupport = new WindowsPlatformSupport();
        platformMap.put(WindowsPlatformSupport.WINDOWS_2003, winSupport);
        platformMap.put(WindowsPlatformSupport.WINDOWS_VISTA, winSupport);
        platformMap.put(WindowsPlatformSupport.WINDOWS_XP, winSupport);
        platformMap.put(WindowsPlatformSupport.WINDOWS_2000, winSupport);
        platformMap.put(WindowsPlatformSupport.WINDOWS_NT, winSupport);

        /* Windows 95 platforms */
        Windows95PlatformSupport win95Support = new Windows95PlatformSupport();
        platformMap.put(Windows95PlatformSupport.WINDOWS_98, win95Support);
        platformMap.put(Windows95PlatformSupport.WINDOWS_95, win95Support);

        /* Unix-like platforms */
        UnixPlatformSupport unixSupport = new UnixPlatformSupport();
        platformMap.put(UnixPlatformSupport.LINUX, unixSupport);
        platformMap.put(UnixPlatformSupport.SOLARIS_1, unixSupport);
        platformMap.put(UnixPlatformSupport.SOLARIS_2, unixSupport);
        platformMap.put(UnixPlatformSupport.MAC_OS_X, unixSupport);
        platformMap.put(UnixPlatformSupport.HP_UX, unixSupport);
        platformMap.put(UnixPlatformSupport.FREE_BSD, unixSupport);
        platformMap.put(UnixPlatformSupport.AIX, unixSupport);
        platformMap.put(UnixPlatformSupport.IRIX, unixSupport);
        platformMap.put(UnixPlatformSupport.OS_2, unixSupport);
    }

    /**
     * <p>Should return an instance of PlatformSupport appropriate for the
     * operating system on which the JVM is running. The current operating
     * system should be determined by checking the value of system property
     * "os.name". The following tables details which subclass of
     * PlatformSupport should be returned for various values of this
     * property:</p>
     *
     * <table>
     *  <tr>
     *   <th>os.name</th><th>instance</th>
     *   <td>Windows XP</td><td>WindowsPlatformSupport</td>
     *   <td>Windows 2000</td><td>WindowsPlatformSupport</td>
     *   <td>Windows NT</td><td>WindowsPlatformSupport</td>
     *   <td>Windows 95</td><td>Windows95PlatformSupport</
     *   <td>Windows 98</td><td>Windows95PlatformSupport</td>>
     *   <td>Linux</td><td>UnixPlatformSupport</td>
     *   <td>Solaris</td><td>UnixPlatformSupport</td>
     *   <td>Mac OS X</td><td>UnixPlatformSupport</td>
     *   <td>HP-UX</td><td>UnixPlatformSupport</td>
     *   <td>FreeBSD</td><td>UnixPlatformSupport</td>
     *   <td>AIX</td><td>UnixPlatformSupport</td>
     *   <td>Irix</td><td>UnixPlatformSupport</td>
     *   <td>OS/2</td><td>UnixPlatformSupport*</td>
     *  </tr>
     * </table>
     *
     * <p>* This may actually need WindowsPlatformSupport?</p>
     *
     * <p>This should be correct, but has only been tested so far on
     * Windows 2000, Linux, and Mac OS X.</p>
     *
     * <p>Note: some of this info was gathered from this useful page:
     * <a href="http://www.tolstoy.com/samizdat/sysprops.html">
     * http://www.tolstoy.com/samizdat/sysprops.html</a>.</p>
     *
     * <p>If "os.name" is anything else, this returns an instance of
     * DefaultPlatformSupport.</p>
     *
     * @return a PlatformSupport instance associated with the current OS
     */
    protected static PlatformSupport getInstance() {
        /* 
         * if the instance is null or a new platform is registered, 
         * it might need to get a new instance
         */
        if (instance == null || isNewPlatformRegistered) {
            /* obtain the name of the current OS */
            Properties properties = System.getProperties();
            String osName = (String) properties.get(OS_NAME);

            /* get the instance from the platform Map */
            instance = (PlatformSupport) platformMap.get(osName);

            /* 
             * if no PlatformSupport is associated with the current OS name,
             * use the default PlatformSupport
             */
            if (instance == null) {
                instance = new DefaultPlatformSupport();
            }

            isNewPlatformRegistered = false;
        }

        return instance;
    }

    /**
     * <p>This component executes commands using the underlying operating
     * system's shell, so that commands generally work as the user expects
     * them to. This method is overridden in subclasses, and it transforms a
     * command into a command invocation through a shell, in a manner
     * appropriate to a particular operating system.</p>
     *
     * @param command the original execution command
     * @return the shell command
     */
    protected abstract String[] makeShellCommand(final String[] command);
    
    /**
     * <p>Registers platform support for a specific OS.</p>
     *
     * @param osString the name of an OS
     * @param platformSupport the PlatformSupport for the OS
     * @throws IllegalArgumentException if either argument is null
     */
    public static void registerPlatformSupport(final String osString, 
            final PlatformSupport platformSupport) 
            throws IllegalArgumentException{
        if (osString == null || platformSupport == null) {
            throw new IllegalArgumentException();
        }

        /* 
         * insert the system name and its associated PlatformSupport into the 
         * HashMap
         */
        platformMap.put(osString, platformSupport);

        isNewPlatformRegistered = true;  
    }
}
