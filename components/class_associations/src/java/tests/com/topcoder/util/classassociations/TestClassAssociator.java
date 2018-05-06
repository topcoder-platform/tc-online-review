/*
 * TestClassAssociator.java
 *
 * Copyright © 2004, TopCoder, Inc. All rights reserved
 */

package com.topcoder.util.classassociations;

import com.topcoder.util.classassociations.*;
import java.awt.*;
import java.util.*;
import javax.swing.JFrame;
import junit.framework.*;


/**
 * <p>Test ClassAssociator constructors</p>
 *
 *
 * @author TCSDEVELOPER
 * @version 1.0  3-30-2004
 */
public class TestClassAssociator extends TestCase {

    public static Test suite() {
        return new TestSuite(TestClassAssociator.class);
    }

    /**
     * Test basic construction with no args.
     */
    public void testClassAssociatorCtrNoArgs() {
        // Create a ClassAssociator with no arguments.
        // There must be no exceptions.
        try {
            ClassAssociator ca = new ClassAssociator();
            assertFalse(ca == null);       // SUCCESS
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            fail("Construction here should not raise an exception");
        }
    }

    /**
     * Test constructor with handlerRestriction parameter
     */
    public void testClassAssociatorCtrHandlerRestriction() {
        // Create instance with a valid argument. There must be no exceptions.
        try {
            ClassAssociator ca = new ClassAssociator(String.class);
            assertFalse(ca == null);       // SUCCESS
        } catch (Exception ex) {
            fail("Construction should not raise an exception here.");
        }
    }

    /**
     * Test constructor with handlerRestriction parameter with a null parameter.
     * An exception should be thrown.
     */
    public void testClassAssociatorCtrHandlerRestrictionNullParm() {
        try {
            ClassAssociator ca = new ClassAssociator(null);
            fail("Construction should raise an exception because of null parameter.");
        } catch (Exception ex) {
            // SUCCESS
        }
    }

    /**
     * Test constructor with two null Map arguments.
     * There should be no exceptions.
     */
    public void testClassAssociatorCtrTwoArgs_Null_Null() {
        try {
            ClassAssociator ca = new ClassAssociator(null, null);
            assertFalse(ca == null);       // SUCCESS
            assertTrue(ca.getAssociations().size() == 0);
            assertTrue(ca.getGroupAssociations().size() == 0);
            assertTrue(Object.class.isAssignableFrom(
                    ca.getHandlerRestrictionClass()));
        } catch (Exception ex) {
            fail("Construction should not raise an exception here.");
        }
    }

    /**
     * Test constructor with a pre-defined associations Map and a null
     * groupAssocations Map.
     * There should be no exceptions.
     */
    public void testClassAssociatorCtrTwoArgs_Map_Null() {
        HashMap associations      = null;

        // Prepare Maps for the next two tests
        associations      = new HashMap();
        associations.put(String.class, "String object");
        associations.put(java.util.Vector.class, new Vector());

        try {
            ClassAssociator ca = new ClassAssociator(associations, null);
            assertFalse(ca == null);       // SUCCESS
            assertTrue(ca.getAssociations().size() == 2);
            assertTrue(ca.getGroupAssociations().size() == 0);
            assertTrue(Object.class.isAssignableFrom(
                    ca.getHandlerRestrictionClass()));
        } catch (Exception ex) {
            fail("Construction should not raise an exception here.");
        }
    }

    /**
     * Test constructor with a null associations Map and a pre-defined
     * groupAssocations Map.
     * There should be no exceptions.
     */
    public void testClassAssociatorCtrTwoArgs_Null_Map() {
        HashMap groupAssociations = null;

        // Prepare Maps for the next two tests
        groupAssociations = new HashMap();
        groupAssociations.put(java.awt.Container.class, new JFrame());
        groupAssociations.put(java.util.AbstractList.class, new java.util.Vector());

        try {
            ClassAssociator ca = new ClassAssociator(null, groupAssociations);
            assertFalse(ca == null);       // SUCCESS
            assertTrue(ca.getAssociations().size() == 0);
            assertTrue(ca.getGroupAssociations().size() == 2);
            assertTrue(Object.class.isAssignableFrom(
                    ca.getHandlerRestrictionClass()));
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            fail("Construction should not raise an exception here.");
        }
    }

    /**
     * Test constructor with a pre-defined associations Map groupAssocations Map.
     * There should be no exceptions.
     */
    public void testClassAssociatorCtrTwoArgs_Map_Map() {
        HashMap associations      = null;
        HashMap groupAssociations = null;

        // Prepare Maps for the next two tests
        associations      = new HashMap();
        associations.put(String.class, "String object");
        associations.put(java.util.Vector.class, new Vector());
        groupAssociations = new HashMap();
        groupAssociations.put(java.awt.Container.class, new JFrame());
        groupAssociations.put(java.util.AbstractList.class, new java.util.Vector());

        try {
            ClassAssociator ca = new ClassAssociator(associations, groupAssociations);
            assertFalse(ca == null);       // SUCCESS
            assertTrue(ca.getAssociations().size() == 2);
            assertTrue(ca.getGroupAssociations().size() == 2);
            assertTrue(Object.class.isAssignableFrom(
                    ca.getHandlerRestrictionClass()));
        } catch (Exception ex) {
            fail("Construction should not raise an exception here.");
        }
    }

    /**
     * Test constructor with a associations Map with a null key.
     * An There should be no exceptions.
     */
    public void testClassAssociatorCtrTwoArgs_MapNullKey_Null() {
        HashMap associations      = new HashMap();

        associations.put(null, "String object");
        try {
            ClassAssociator ca = new ClassAssociator(associations, null);
            fail("Construction raise an exception here for null association key.");
        } catch (Exception ex) {
            // SUCCESS
        }
    }

    /**
     * Test constructor with a associations Map with a null key.
     * An There should be no exceptions.
     */
    public void testClassAssociatorCtrTwoArgs_MapNullValue_Null() {
        HashMap associations      = new HashMap();

        associations.put(String.class, null);
        try {
            ClassAssociator ca = new ClassAssociator(associations, null);
            fail("Construction should raise an exception here for a null association key.");
        } catch (Exception ex) {
            // SUCCESS
        }
    }
}