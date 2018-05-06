/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable;

import junit.framework.TestCase;

import java.util.Date;

/**
 * Unit test for AuditedDeliverableStructure.
 *
 * @author singlewood
 * @version 1.1
 */
public class AuditedDeliverableStructureTests extends TestCase {

    /**
     * The test AuditedDeliverableStructure instance.
     */
    private AuditedDeliverableStructure auditedDeliverable = null;

    /**
     * The test Date instance.
     */
    private Date date = null;

    /**
     * Create the test instance.
     *
     * @throws Exception exception to JUnit.
     */
    public void setUp() throws Exception {
        auditedDeliverable = new AuditedDeliverableStructureExtends();
        date = new Date();
    }

    /**
     * Clean the config.
     *
     * @throws Exception exception to JUnit.
     */
    public void tearDown() throws Exception {
        auditedDeliverable = null;
        date = null;
    }

    /**
     * The default constructor should set id to UNSET_ID. So check if id is set properly. No exception should be
     * thrown.
     */
    public void testConstructor1_Accuracy() {
        assertEquals("the constructor doesn't set id properly", AuditedDeliverableStructure.UNSET_ID,
                auditedDeliverable.getId());
    }

    /**
     * Tests constructor2 with invalid parameters. The argument will be set to 0. IllegalArgumentException should be
     * thrown.
     */
    public void testConstructor2_InvalidLong1() {
        try {
            new AuditedDeliverableStructureExtends(0);
            fail("IllegalArgumentException should be thrown because of the invalid parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Tests constructor2 with invalid parameters. The argument will be set to -2. IllegalArgumentException should be
     * thrown.
     */
    public void testConstructor2_InvalidLong2() {
        try {
            new AuditedDeliverableStructureExtends(-2);
            fail("IllegalArgumentException should be thrown because of invalid parameters.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Tests constructor2 with valid parameter. Check if the constructor set the id fields properly. No exception should
     * be thrown.
     */
    public void testConstructor2_Accuracy1() {
        auditedDeliverable = new AuditedDeliverableStructureExtends(123);
        assertEquals("constructor doesn't work properly.", 123, auditedDeliverable.getId());
    }

    /**
     * Tests the behavior of setId. Set the id field with 0. IllegalArgumentException should be thrown.
     */
    public void testSetId_Invalid1() {
        try {
            auditedDeliverable.setId(0);
            fail("IllegalArgumentException should be thrown because of the invalid parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Tests the behavior of setId. Set the id field when it was already set. IllegalArgumentException should be thrown.
     */
    public void testSetId_Invalid2() {
        try {
            auditedDeliverable.setId(1);
            auditedDeliverable.setId(1);
            fail("IdAlreadySetException should be thrown because id was set ");
        } catch (IdAlreadySetException ise) {
            // expected.
        }
    }

    /**
     * Tests the behavior of setId. Set the id field, see if the getId method can get the correct value. No exception
     * should be thrown.
     */
    public void testSetId_Accuracy1() {
        auditedDeliverable.setId(111);
        assertEquals("id is not set properly.", 111, auditedDeliverable.getId());
    }

    /**
     * Tests the behavior of getId. Set the id field, see if the getId method can get the correct value. No exception
     * should be thrown.
     */
    public void testGetId_Accuracy1() {
        auditedDeliverable.setId(222);
        assertEquals("getId doesn't work properly.", 222, auditedDeliverable.getId());
    }

    /**
     * Tests the behavior of setCreationUser. Set the creationUser field, see if the getCreationUser method can get the
     * correct value. No exception should be thrown.
     */
    public void testSetCreationUser_Accuracy1() {
        auditedDeliverable.setCreationUser("user1");
        assertEquals("creationUser is not set properly.", "user1", auditedDeliverable.getCreationUser());
    }

    /**
     * Tests the behavior of getCreationUser. Set the creationUser field, see if the getCreationUser method can get the
     * correct value. No exception should be thrown.
     */
    public void testGetCreationUser_Accuracy1() {
        auditedDeliverable.setCreationUser("user2");
        assertEquals("getCreationUser doesn't work properly.", "user2", auditedDeliverable.getCreationUser());
    }

    /**
     * Tests the behavior of setCreationTimestamp. Set the creationTimestamp field, see if the getCreationTimestamp
     * method can get the correct value. No exception should be thrown.
     */
    public void testSetCreationTimestamp_Accuracy1() {
        auditedDeliverable.setCreationTimestamp(date);
        assertEquals("creationTimestamp is not set properly.", date, auditedDeliverable
                .getCreationTimestamp());
    }

    /**
     * Tests the behavior of getCreationTimestamp. Set the creationTimestamp field, see if the getCreationTimestamp
     * method can get the correct value. No exception should be thrown.
     */
    public void testGetCreationTimestamp_Accuracy1() {
        auditedDeliverable.setCreationTimestamp(date);
        assertEquals("getCreationTimestamp doesn't work properly.", date, auditedDeliverable
                .getCreationTimestamp());
    }

    /**
     * Tests the behavior of setModificationUser. Set the modificationUser field, see if the getModificationUser method
     * can get the correct value. No exception should be thrown.
     */
    public void testSetModificationUser_Accuracy1() {
        auditedDeliverable.setModificationUser("user1");
        assertEquals("modificationUser is not set properly.", "user1", auditedDeliverable
                .getModificationUser());
    }

    /**
     * Tests the behavior of getModificationUser. Set the modificationUser field, see if the getModificationUser method
     * can get the correct value. No exception should be thrown.
     */
    public void testGetModificationUser_Accuracy1() {
        auditedDeliverable.setModificationUser("user2");
        assertEquals("getModificationUser doesn't work properly.", "user2", auditedDeliverable
                .getModificationUser());
    }

    /**
     * Tests the behavior of setModificationTimestamp. Set the modificationTimestamp field, see if the
     * getModificationTimestamp method can get the correct value. No exception should be thrown.
     */
    public void testSetModificationTimestamp_Accuracy1() {
        auditedDeliverable.setModificationTimestamp(date);
        assertEquals("modificationTimestamp is not set properly.", date, auditedDeliverable
                .getModificationTimestamp());
    }

    /**
     * Tests the behavior of getModificationTimestamp. Set the modificationTimestamp field, see if the
     * getModificationTimestamp method can get the correct value. No exception should be thrown.
     */
    public void testGetModificationTimestamp_Accuracy1() {
        auditedDeliverable.setModificationTimestamp(date);
        assertEquals("getModificationTimestamp doesn't work properly.", date, auditedDeliverable
                .getModificationTimestamp());
    }

    /**
     * Tests the behavior of isValidToPersist. Set the creationUser field to null, see if the isValidToPersist returns
     * false. No exception should be thrown.
     */
    public void testIsValidToPersist_Accuracy1() {
        auditedDeliverable.setCreationUser(null);
        auditedDeliverable.setCreationTimestamp(date);
        auditedDeliverable.setModificationUser("ModificationUser");
        auditedDeliverable.setModificationTimestamp(date);
        assertEquals("isValidToPersist doesn't work properly.", false, auditedDeliverable.isValidToPersist());
    }

    /**
     * Tests the behavior of isValidToPersist. Set the creationTimestamp field to null, see if the isValidToPersist
     * returns false. No exception should be thrown.
     */
    public void testIsValidToPersist_Accuracy2() {
        auditedDeliverable.setCreationUser("CreationUser");
        auditedDeliverable.setCreationTimestamp(null);
        auditedDeliverable.setModificationUser("ModificationUser");
        auditedDeliverable.setModificationTimestamp(date);
        assertEquals("isValidToPersist doesn't work properly.", false, auditedDeliverable.isValidToPersist());
    }

    /**
     * Tests the behavior of isValidToPersist. Set the modificationUser field to null, see if the isValidToPersist
     * returns false. No exception should be thrown.
     */
    public void testIsValidToPersist_Accuracy3() {
        auditedDeliverable.setCreationUser("CreationUser");
        auditedDeliverable.setCreationTimestamp(date);
        auditedDeliverable.setModificationUser(null);
        auditedDeliverable.setModificationTimestamp(date);
        assertEquals("isValidToPersist doesn't work properly.", false, auditedDeliverable.isValidToPersist());
    }

    /**
     * Tests the behavior of isValidToPersist. Set the modificationTimestamp field to null, see if the isValidToPersist
     * returns false. No exception should be thrown.
     */
    public void testIsValidToPersist_Accuracy4() {
        auditedDeliverable.setCreationUser("CreationUser");
        auditedDeliverable.setCreationTimestamp(date);
        auditedDeliverable.setModificationUser("ModificationUser");
        auditedDeliverable.setModificationTimestamp(null);
        assertEquals("isValidToPersist doesn't work properly.", false, auditedDeliverable.isValidToPersist());
    }

    /**
     * Tests the behavior of isValidToPersist. Set the modificationTimestamp <= creationTimestamp, see if the
     * isValidToPersist returns false. No exception should be thrown.
     */
    public void testIsValidToPersist_Accuracy5() {
        auditedDeliverable.setCreationUser("CreationUser");
        auditedDeliverable.setCreationTimestamp(new Date(2000));
        auditedDeliverable.setModificationUser("ModificationUser");
        auditedDeliverable.setModificationTimestamp(new Date(1000));
        assertEquals("isValidToPersist doesn't work properly.", false, auditedDeliverable.isValidToPersist());
    }

    /**
     * Tests the behavior of isValidToPersist. Set all the field with non-null values, see if the isValidToPersist
     * returns true. No exception should be thrown.
     */
    public void testIsValidToPersist_Accuracy6() {
        auditedDeliverable.setCreationUser("CreationUser");
        auditedDeliverable.setCreationTimestamp(date);
        auditedDeliverable.setModificationUser("ModificationUser");
        auditedDeliverable.setModificationTimestamp(date);
        assertEquals("isValidToPersist doesn't work properly.", true, auditedDeliverable.isValidToPersist());
    }

}
