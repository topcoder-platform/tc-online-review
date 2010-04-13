/*
 * Copyright (C) 2006 TopCoder, Inc. All Rights Reserved.
 */
package com.cronos.onlinereview.project;

import com.gargoylesoftware.htmlunit.BrowserVersion;

/**
 * This class provides the target browser feature to test. 
 *
 * @author TCSTESTER
 * @version 1.0
 */
public class BrowserSupport {

    /**
     * The configuration interface.
     */
    private static Configuration config = new Configuration(BrowserSupport.class.getName());

    /**
     * Private constructor.
     */
    private BrowserSupport() {
    }

    /**
     * Look up the target browser.
     *
     * @return the target browser.
     */
    public static BrowserVersion getTargetBrowser() {
        if ("Internet Explorer 6.0".equals(config.getProperty("target_browser"))) {
            return BrowserVersion.INTERNET_EXPLORER_6_0;
        } else if ("Mozilla 1.0".equals(config.getProperty("target_browser"))) {
            return BrowserVersion.MOZILLA_1_0;
        } else if ("Netscape 4.7.9".equals(config.getProperty("target_browser"))) {
            return BrowserVersion.NETSCAPE_4_7_9;
        } else if ("Netscape 6.2.3".equals(config.getProperty("target_browser"))) {
            return BrowserVersion.NETSCAPE_6_2_3;
        } else {
            return BrowserVersion.FULL_FEATURED_BROWSER;
        }
    }
}