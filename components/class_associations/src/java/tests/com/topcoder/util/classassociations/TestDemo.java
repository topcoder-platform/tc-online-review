/*
 * TestDemo.java
 *
 * Copyright © 2004, TopCoder, Inc. All rights reserved
 */

package com.topcoder.util.classassociations;

import com.topcoder.util.classassociations.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import junit.framework.*;

/**
 * <p>Provide a test demonstration of how the component should be used.</p>
 *
 * @author TCSDEVELOPER
 * @version 1.0  3-30-2004
 */
public class TestDemo extends TestCase {

    public static Test suite() {
        return new TestSuite(TestDemo.class);
    }

    /**
     * Demonstration of how to use the ClassAssociator. Will provide a sample
     * demonstration and prove that the results are as expected.
     */
    public void testDemo() {
        ClassAssociator ca = null;
        Object handler     = null;

        try {
            // build new ClassAssociator with no arguments.
            ca = new ClassAssociator();

            // *****************************************************************
            // Here we will make use of a set of "Handlers" that belong to
            // the java.awt.Component class hierarchy.
            // *****************************************************************

            try {
                // Setup Restriction Handler Class for safer use.
                ca.setHandlerRestrictionClass(java.awt.Container.class);

                // Enable the use of the Restriction Handler
                ca.setHandlerRestriction(true);
            } catch(IllegalStateException ise) {
                // associations already exist that violate the hander restriction.
                System.err.println(ise.getMessage());
            }


            // *****************************************************************
            // Add assocations
            // *****************************************************************

            // Add direct match Object association
            ca.addAssociation(new Frame(), new Frame());

            // Add direct match Class association
            ca.addClassAssociation(JLabel.class, new JLabel());

            // Add group match Object association
            ca.addGroupAssociation(new java.awt.Container(), new JFrame());

            // Add group match Class association using a superclass
            ca.addGroupClassAssociation(java.awt.Window.class, new JEditorPane());

            // Add group match Class association using an interface for the Class
            ca.addGroupClassAssociation(java.awt.MenuContainer.class, new JList());

            // *****************************************************************
            // Lookup assocations
            // *****************************************************************

            // When we are ready to retrieve a handler, we use
            // retrieveHandler(...) and retrieveHandlerClass(...)

            // Lookup a direct handler match by object
            handler = ca.retrieveHandler(new Frame());
            assertTrue(handler instanceof Frame);

            // Lookup a direct handler match by Class
            handler = ca.retrieveClassHandler(JLabel.class);
            assertTrue(handler instanceof JLabel);

            // Lookup a hierarchy handler match by Object
            handler = ca.retrieveHandler(new java.awt.Container());
            assertTrue(handler instanceof JFrame);

            // Lookup a hierarchy handler match by Class
            handler = ca.retrieveClassHandler(java.awt.Window.class);
            assertTrue(handler instanceof JEditorPane);

            // Lookup a hierarchy handler match using an interface
            handler = ca.retrieveClassHandler(java.awt.MenuContainer.class);
            assertTrue(handler instanceof JList);

        } catch (IllegalArgumentException ex) {
            System.err.println(ex.getMessage());
            fail("Should not throw an exception");
        } catch (IllegalHandlerException ihe) {
            // The handlers specified
            System.err.println(ihe.getMessage());
        }
    }
}