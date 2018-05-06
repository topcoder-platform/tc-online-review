/*
 * Windows95PlatformSupport.java
 *
 * Copyright (C) 2002, TopCoder, Inc. All rights reserved
 *
 */

package com.topcoder.util.exec;

/**
 * <p>Encapsulates support for Windows 95 and derived platforms 
 * (Windows 98, ME).</p>
 *
 * @author srowen
 * @author garyk
 * @version 1.0
 */
public class Windows95PlatformSupport extends WindowsPlatformSupport {

    /* names of the Windows 95 and derived platforms */
    public static final String WINDOWS_98 = "Windows 98";
    public static final String WINDOWS_95 = "Windows 95";

    /**
     * <p>This method returns <code>{"command.com", "/C"}</code>.</p>
     *
     * @return <code>{"command.com", "/C"}</code>
     */
    protected String[] getOSShell() {
        return new String[] {"command.com", "/C"};
    }
}
