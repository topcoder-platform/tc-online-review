/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer;

import com.topcoder.util.collection.typesafeenum.Enum;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * This is used to test FileUploadCheckStatus for correctness.
 * @author FireIce
 * @version 1.0
 */
public class FileUploadCheckStatusTestCase extends TestCase {
    /**
     * Test inheritence.
     */
    public void testInheritence() {
        assertTrue("this class should extends Enum class", FileUploadCheckStatus.UPLOAD_ACCEPTED instanceof Enum);
    }

    /**
     * Test getValue() method.
     */
    public void testGetValue() {
        assertEquals("the value should be \"UPLOAD_ACCEPTED\"", FileUploadCheckStatus.UPLOAD_ACCEPTED.getValue(),
                "UPLOAD_ACCEPTED");
        assertEquals("the value should be \"UPLOAD_NOT_ACCEPTED\"", FileUploadCheckStatus.UPLOAD_NOT_ACCEPTED
                .getValue(), "UPLOAD_NOT_ACCEPTED");
    }

    /**
     * <p>
     * Aggragates all tests in this class.
     * </p>
     * @return test suite aggragating all tests.
     */
    public static Test suite() {
        return new TestSuite(FileUploadCheckStatusTestCase.class);
    }
}
