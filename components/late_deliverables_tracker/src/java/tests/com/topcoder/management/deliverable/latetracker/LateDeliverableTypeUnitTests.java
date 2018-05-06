/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.latetracker;

import junit.framework.TestCase;

/**
 * <p>
 * Unit tests for {@link LateDeliverableType} enumeration.
 * </p>
 *
 * @author sparemax
 * @version 1.3
 * @since 1.3
 */
public class LateDeliverableTypeUnitTests extends TestCase {

    /**
     * <p>
     * Accuracy test for the constant <code>MISSED_DEADLINE</code>.<br>
     * The value should be correct.
     * </p>
     */
    public void test_MISSED_DEADLINE() {
        assertEquals("'MISSED_DEADLINE' should be correct.",
            "Missed Deadline", LateDeliverableType.MISSED_DEADLINE.toString());
    }

    /**
     * <p>
     * Accuracy test for the constant <code>REJECTED_FINAL_FIX</code>.<br>
     * The value should be correct.
     * </p>
     */
    public void test_REJECTED_FINAL_FIX() {
        assertEquals("'REJECTED_FINAL_FIX' should be correct.",
            "Rejected Final Fix", LateDeliverableType.REJECTED_FINAL_FIX.toString());
    }

    /**
     * <p>
     * Accuracy test for the method <code>toString()</code>.<br>
     * The result should be correct.
     * </p>
     */
    public void test_toString() {
        assertEquals("'toString' should be correct.",
            "Missed Deadline", LateDeliverableType.MISSED_DEADLINE.toString());
    }

    /**
     * <p>
     * Accuracy test for the method <code>fromString(String value)</code> with value is 'Missed Deadline'.<br>
     * The result should be correct: LateDeliverableType.MISSED_DEADLINE.
     * </p>
     */
    public void test_fromString_1() {
        assertEquals("'fromString' should be correct.",
            LateDeliverableType.MISSED_DEADLINE, LateDeliverableType.fromString("Missed Deadline"));
    }

    /**
     * <p>
     * Accuracy test for the method <code>fromString(String value)</code> with value is 'Rejected Final Fix'.<br>
     * The result should be correct: LateDeliverableType.REJECTED_FINAL_FIX.
     * </p>
     */
    public void test_fromString_2() {
        assertEquals("'fromString' should be correct.",
            LateDeliverableType.REJECTED_FINAL_FIX, LateDeliverableType.fromString("Rejected Final Fix"));
    }

    /**
     * <p>
     * Accuracy test for the method <code>fromString(String value)</code> with value is 'not exist'.<br>
     * The result should be correct: null.
     * </p>
     */
    public void test_fromString_3() {
        assertNull("'fromString' should be correct.", LateDeliverableType.fromString("not exist"));
    }
}
