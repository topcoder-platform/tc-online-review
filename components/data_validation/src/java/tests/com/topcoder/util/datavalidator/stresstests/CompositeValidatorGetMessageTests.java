/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.datavalidator.stresstests;

import com.topcoder.util.datavalidator.AbstractAssociativeObjectValidator;
import com.topcoder.util.datavalidator.AndValidator;
import com.topcoder.util.datavalidator.DoubleValidator;
import com.topcoder.util.datavalidator.FloatValidator;
import com.topcoder.util.datavalidator.IntegerValidator;
import com.topcoder.util.datavalidator.NotValidator;
import com.topcoder.util.datavalidator.ObjectValidator;
import com.topcoder.util.datavalidator.OrValidator;
import com.topcoder.util.datavalidator.StringValidator;

import junit.framework.TestCase;

/**
 * This tests the speed of getXXXMessages methods using complex validator.
 * 
 * @author Psyho
 * @version 1.1
 * @since 1.1
 */
public class CompositeValidatorGetMessageTests extends TestCase {

    /**
     * Represents the validators used in messages' stress test.
     */
    private ObjectValidator[] validators;

    /**
     * Represents the Objects used in testing.
     */
    private static final String[] TEST_STRINGS = {"  ", "empty", " empty with substring",
            "starting string with 696996969 and substring", "6969699696969 string with end",
            "starting string 69XX969* substring end.", "13", "1.1", "502345"};

    /**
     * Initialiazes the <tt>validator</tt>.
     * 
     * @throws Exception if anything goes wrong
     */
    protected void setUp() throws Exception {
        super.setUp();

        AndValidator validator1 = new AndValidator();
        validator1.addValidator(StringValidator.containsSubstring("substring"));
        validator1.addValidator(StringValidator.startsWith("starting string"));
        validator1.addValidator(StringValidator.matchesRegexp(".*69??969.*end."));

        OrValidator validator2 = new OrValidator();
        validator2.addValidator(IntegerValidator.greaterThan(31));
        validator2.addValidator(validator1);
        validator2.addValidator(new NotValidator(FloatValidator.inExclusiveRange(0.5f, 1.5f)));

        validators = new ObjectValidator[] {validator1, validator2};

    }

    /**
     * Tests the speed of getMessages methods.
     */
    public void testGetMessages() {
        for (int k = 0; k < validators.length; k++) {
            long start = System.currentTimeMillis();
            for (int i = 0; i < 1000; i++) {
                for (int j = 0; j < TEST_STRINGS.length; j++) {
                    validators[k].getMessages(TEST_STRINGS[j]);
                }
            }
            long end = System.currentTimeMillis();
            System.out.println("Test GetMessages" + k + " time cost:" + (end - start) + "ms");
        }
    }

    /**
     * Tests the speed of getAllMessages(Object) methods.
     */
    public void testGetAllMessages() {
        for (int k = 0; k < validators.length; k++) {
            long start = System.currentTimeMillis();
            for (int i = 0; i < 1000; i++) {
                for (int j = 0; j < TEST_STRINGS.length; j++) {
                    validators[k].getAllMessages(TEST_STRINGS[j]);
                }
            }
            long end = System.currentTimeMillis();
            System.out.println("Test GetAllMessages" + k + " time cost:" + (end - start) + "ms");
        }
    }

    /**
     * Tests the speed of getAllMessages(Object, int) methods.
     */
    public void testGetAllMessagesWithLimit() {
        for (int k = 0; k < validators.length; k++) {
            long start = System.currentTimeMillis();
            for (int i = 0; i < 1000; i++) {
                for (int j = 0; j < TEST_STRINGS.length; j++) {
                    validators[k].getAllMessages(TEST_STRINGS[j], i % 4 + 1);
                }
            }
            long end = System.currentTimeMillis();
            System.out.println("Test GetAllMessagesWithLimit" + k + " time cost:" + (end - start) + "ms");
        }
    }

}
