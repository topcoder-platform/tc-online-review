/* Copyright (C) 2007 TopCoder Inc., All Rights Reserved. */

package com.cronos.onlinereview.tests;

public class User
{
    final public static User SUBMITTER = new User("submitter", "password");
    final public static User MANAGER = new User("manager", "password");
    final public static User OBSERVER = new User("observer", "password");
    
    private String userName;
    private String password;
    
    public User(String userName, String password)
    {
        this.setUserName(userName);
        this.setPassword(password);
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
    
    
}
