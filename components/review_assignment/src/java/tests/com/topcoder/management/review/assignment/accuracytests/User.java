/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.assignment.accuracytests;

/**
 * <p>An enumeration over the pre-defined user accounts.</p>
 *
 * @author isv
 */
public enum User {

    DOK_TESTER(20, "dok_tester"),

    DOK_TESTER1(21, "dok_tester1"),

    HUNG(124764, "Hung"),

    TWIGHT(124766, "twight"),

    PARTHA(124772, "Partha"),

    SANDKING(124776, "sandking"),

    LIGHTSPEED(124834, "lightspeed"),

    REASSEMBLER(124835, "reassembler"),

    ANNEJ9NY(124836, "annej9ny"),

    PLINEHAN(124852, "plinehan"),

    CHELSEASIMON(124853, "chelseasimon"),

    WYZMO(124856, "wyzmo"),

    CARTAJS(124857, "cartajs"),

    KSMITH(124861, "ksmith"),

    YOSHI(124916, "Yoshi"),

    HEFFAN(132456, "heffan"),

    SUPER(132457, "super"),

    USER(132458, "user");

    /**
     * <p>A <code>long</code> providing the ID of this user.</p>
     */
    private long userId;

    /**
     * <p>A <code>String</code> providing the name of this user.</p>
     */
    private String name;

    /**
     * <p>Constructs new <code>User</code> instance with specified ID and name.</p>
     *
     * @param userId a <code>long</code> providing the ID of this user.
     * @param name a <code>String</code> providing the name of this user.
     */
    private User(long userId, String name) {
        this.userId = userId;
        this.name = name;
    }

    /**
     * <p>Gets the name of this user.</p>
     *
     * @return a <code>String</code> providing the name of this user.
     */
    public String getName() {
        return this.name;
    }

    /**
     * <p>Gets the ID of this user.</p>
     *
     * @return a <code>long</code> providing the ID of this user.
     */
    public long getUserId() {
        return this.userId;
    }

    /**
     * <p>Gets the user matching the specified ID.</p>
     * 
     * @param userId a <code>long</code> providing the user ID.
     * @return a <code>User</code> providing the details for user. 
     */
    public static User getByUserId(long userId) {
        User[] users = values();
        for (User user : users) {
            if (user.getUserId() == userId) {
                return user;
            }
        }
        return null;
    }
}
