/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.message.email.accuracytests;

import com.topcoder.message.email.PriorityLevel;

import junit.framework.TestCase;


/**
 * Test <code>PriorityLevel</code> class for accuracy.
 *
 * @author fairytale
 * @version 3.0
 */
public class AccuracyPriorityLevelTest extends TestCase {
    /**
     * Test NONE field for correct name.
     */
    public void testNONE() {
        assertEquals(
            "NONE field should be properly set.", "none", PriorityLevel.NONE.getName());
    }

    /**
     * Test HIGHEST field for correct name.
     */
    public void testHIGHEST() {
        assertEquals(
            "HIGHEST field should be properly set.", "highest",
            PriorityLevel.HIGHEST.getName());
    }

    /**
     * Test HIGH field for correct name.
     */
    public void testHIGH() {
        assertEquals(
            "HIGH field should be properly set.", "high", PriorityLevel.HIGH.getName());
    }

    /**
     * Test NORMAL field for correct name.
     */
    public void testNORMAL() {
        assertEquals(
            "NORMAL field should be properly set.", "normal",
            PriorityLevel.NORMAL.getName());
    }

    /**
     * Test LOW field for correct name.
     */
    public void testLOW() {
        assertEquals(
            "LOW field should be properly set.", "low", PriorityLevel.LOW.getName());
    }

    /**
     * Test LOWEST field for correct name.
     */
    public void testLOWEST() {
        assertEquals(
            "LOWEST field should be properly set.", "lowest",
            PriorityLevel.LOWEST.getName());
    }

    /**
     * Print out message get from toString() method.
     */
    public void testAccuracyHIGHESTToString() {
        System.out.println("HIGHEST: " + PriorityLevel.HIGHEST.toString());
    }

    /**
     * Print out message get from toString() method.
     */
    public void testAccuracyHIGHToString() {
        System.out.println("HIGH: " + PriorityLevel.HIGH.toString());
    }

    /**
     * Print out message get from toString() method.
     */
    public void testAccuracyNORMALToString() {
        System.out.println("NORMAL: " + PriorityLevel.NORMAL.toString());
    }

    /**
     * Print out message get from toString() method.
     */
    public void testAccuracyLOWToString() {
        System.out.println("LOW: " + PriorityLevel.LOW.toString());
    }

    /**
     * Print out message get from toString() method.
     */
    public void testAccuracyLOWESTToString() {
        System.out.println("LOWEST: " + PriorityLevel.LOWEST.toString());
    }

    /**
     * Print out message get from toString() method.
     */
    public void testAccuracyNONEToString() {
        System.out.println("NONE: " + PriorityLevel.NONE.toString());
    }
}
