/* Copyright (C) 2007 TopCoder Inc., All Rights Reserved. */

package com.cronos.onlinereview;

import com.gargoylesoftware.htmlunit.BrowserVersion;

public class Configuration
{
    private static Configuration instance = new Configuration();
    
    private BrowserVersion browserVersion = BrowserVersion.FULL_FEATURED_BROWSER;
    private String adminUrl = "http://63.118.154.175/scorecard_admin";
    private String reviewUrl = "http://63.118.154.175/review";
    private String httpAuthUsername = "alexdelarge";
    private String httpAuthPassword = "cl0ckw0rk";
    
    private Configuration()
    {
    }
    
    public static BrowserVersion getBrowserVersion()
    {
        return instance.browserVersion;
    }
    
    public static String getAdminUrl()
    {
        return instance.adminUrl;
    }
    
    public static String getReviewUrl()
    {
        return instance.reviewUrl;
    }
    
    public static String getHttpAuthUsername()
    {
        return instance.httpAuthUsername;
    }
    
    public static String getHttpAuthPassword()
    {
        return instance.httpAuthPassword;
    }
}
