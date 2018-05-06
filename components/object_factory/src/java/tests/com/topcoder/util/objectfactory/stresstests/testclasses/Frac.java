/*
 * Copyright (c) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.objectfactory.stresstests.testclasses;


/**
 * A class used for testing.
 *
 * @author Wendell
 * @version 1.0
 */
public class Frac {
    /** The int value of this Frac. */
    private int iv;

    /** The Bar value of this Frac. */
    private Bar bv;

    /** The String value of this Frac. */
    private String sv;

    /**
     * Creates a new Frac with default values.
     */
    public Frac() {
        setIv(0);
        setBv(new Bar(1.0f, new StringBuffer()));
        setSv("");
    }

    /**
     * Creates a new Frac with the given arguments.
     *
     * @param iv the int value.
     * @param sv the String value.
     * @param bv the Bar value.
     */
    public Frac(int iv, String sv, Bar bv) {
        setIv(iv);
        setBv(bv);
        setSv(sv);
    }

    /**
     * Gets the Bar value of this Frac.
     *
     * @return the Bar value of this Frac.
     */
    public Bar getBv() {
        return bv;
    }

    /**
     * Sets the Bar value of this Frac.
     *
     * @param bv the Bar value of this Frac.
     */
    public void setBv(Bar bv) {
        this.bv = bv;
    }

    /**
     * Gets the int value of this Frac.
     *
     * @return the int value of this Frac.
     */
    public int getIv() {
        return iv;
    }

    /**
     * Sets the int value of this Frac.
     *
     * @param iv the int value of this Frac.
     */
    public void setIv(int iv) {
        this.iv = iv;
    }

    /**
     * Gets the String value of this Frac.
     *
     * @return the String value of this Frac.
     */
    public String getSv() {
        return sv;
    }

    /**
     * Sets the String value of this Frac.
     *
     * @param sv the String value of this Frac.
     */
    public void setSv(String sv) {
        this.sv = sv;
    }
}
