/*
 * TestDefaultAssociationAlgorithm.java
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
 * <p>Test DefaultAssociationAlgorithm constructor and methods.</p>
 *
 * @author TCSDEVELOPER
 * @version 1.0  3-30-2004
 */
public class TestDefaultAssociationAlgorithm extends TestCase {

    public static Test suite() {
        return new TestSuite(TestDefaultAssociationAlgorithm.class);
    }

    /**
     * Test DefaultAssociationAlgorithm constructor with no parameters
     */
    public void testDefaultAssociationAlgorithmCtr_NoArgs() {
        try {
            DefaultAssociationAlgorithm aag = new DefaultAssociationAlgorithm();
            assertFalse(aag == null);
        } catch (Exception ex) {
            fail("An exception should NOT be thrown here.");
        }
    }

    /**
     * Test retrieveHandler with valid ClassAssociator and Class.
     * This effectively tests retrieveHandler by using it the way that
     * ClassAssociator will normally use it internally.
     * There should be no exceptions.
     */
    public void testRetrieveHandler_ClassAssoc_Class() {
        HashMap associations            = null;
        HashMap groupAssociations       = null;
        ClassAssociator ca              = null;
        DefaultAssociationAlgorithm aag = null;

        aag = new DefaultAssociationAlgorithm();
        associations = new HashMap();
        groupAssociations = new HashMap();
        associations.put(java.util.Vector.class, new Vector());
        groupAssociations.put(java.awt.Container.class, new JFrame());
        groupAssociations.put(java.awt.MenuContainer.class, new Frame());
        groupAssociations.put(java.util.AbstractCollection.class,
                              new java.util.Vector());

        try {
            // build new ClassAssociator with these associations
            ca = new ClassAssociator(associations, groupAssociations);
            // test a direct match scenario
            assertTrue( aag.retrieveHandler(ca,Vector.class) instanceof Vector );
            // test a group match on Class scenario
            assertTrue( aag.retrieveHandler(ca,Frame.class) instanceof JFrame );
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            fail("Should not throw an exception");
        }
    }
}