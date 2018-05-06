/*
 * TestClassAssociatorMethods.java
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
 * <p>Test ClassAssociator methods.</p>
 *
 * @author TCSDEVELOPER
 * @version 1.0  3-30-2004
 */
public class TestClassAssociatorMethods extends TestCase {

    private ClassAssociator ca;

    protected void setUp() {
        ca = new ClassAssociator();
    }

    public static Test suite() {
        return new TestSuite(TestClassAssociatorMethods.class);
    }

    /**
     * Test addAlgorithm with a valid object
     */
    public void testAddAlgorithm_String_AssocAlgo() {
        try {
            assertTrue (ca.getAlgorithm("testAlgorithm") == null);
            ca.addAlgorithm("testAlgorithm",
                            new DefaultAssociationAlgorithm());
            assertTrue (ca.getAlgorithm("testAlgorithm") != null);
        } catch (Exception ex) {
            fail("An exception should NOT be thrown here.");
        }
    }

    /**
     * Test addAssociation with 2 valid Objects, no restriction.
     */
    public void testAddAssociation_Object_Object() {
        Map associations = null;
        Object value = null;

        try {
            ca.addAssociation("String object", "Test");
            associations = ca.getAssociations();
            value = associations.get(String.class);
            assertTrue(value instanceof String);
            assertTrue(value.equals("Test"));
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            fail("Should not throw an exception");
        }
    }

    /**
     * Test addAssociation with 2 null parameters, no restriction.
     */
    public void testAddAssociation_Null_Null() {

        try {
            ca.addAssociation(null,null);
            fail("An exception should be thrown here.");
        } catch (Exception ex) {
            // SUCCESS
        }
    }

    /**
     * Test addAssociation with 2 valid ObjectsObjects
     * and the Class restriction enabled.
     */
    public void testAddAssociationRestrict_Object_Object() {
        Map associations = null;
        Object value = null;

        try {
            ca.setHandlerRestrictionClass(String.class);
            ca.setHandlerRestriction(true);
            ca.addAssociation("String object", "Test");
            associations = ca.getAssociations();
            value = associations.get(String.class);
            assertTrue(value instanceof String);
            assertTrue(value.equals("Test"));
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            fail("Should not throw an exception");
        }
    }

    /**
     * Test addAssociation with invalid Class and valid Object
     * and the restriction enabled.
     */
    public void testAddAssociationRestrictFail_Object_Object() {
        Map associations = null;
        Object value = null;

        try {
            ca.setHandlerRestrictionClass(String.class);
            ca.setHandlerRestriction(true);
            ca.addAssociation("Test", new Vector());
            fail ("An Exception should be thrown. Class is not restriction class or subclass.");
        } catch (Exception ex) {
            // SUCCESS
        }
    }


    /**
     * Test addClassAssociation with a valid Class and Object, no restriction.
     */
    public void testAddClassAssociation_Class_Object() {
        Map associations = null;
        Object value = null;

        try {
            ca.addClassAssociation(String.class, "Test");
            associations = ca.getAssociations();
            value = associations.get(String.class);
            assertTrue(value instanceof String);
            assertTrue(value.equals("Test"));
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            fail("Should not throw an exception");
        }
    }

    /**
     * Test addClassAssociation with a null Class and Object, no restriction.
     */
    public void testAddClassAssociation_Null_Null() {

        try {
            ca.addClassAssociation(null, null);
            fail("An exception should be thrown here.");
        } catch (Exception ex) {
            // SUCCESS
        }
    }

    /**
     * Test addGroupAssociation with 2 valid Objects, no restriction.
     */
    public void testAddGroupAssociation_Object_Object() {
        Map associations = null;
        Object value = null;

        try {
            ca.addGroupAssociation(new java.awt.Container(), new Vector());
            associations = ca.getGroupAssociations();
            value = associations.get(java.awt.Container.class);
            assertTrue(value instanceof Vector);
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            fail("Should not throw an exception");
        }
    }

    /**
     * Test addGroupAssociation with 2 null parameters, no restriction.
     */
    public void testAddGroupAssociation_Null_Null() {

        try {
            ca.addGroupAssociation(null,null);
            fail("An exception should be thrown here.");
        } catch (Exception ex) {
            // SUCCESS
        }
    }

    /**
     * Test addGroupAssociation with 2 valid ObjectsObjects
     * and the Class restriction enabled.
     */
    public void testAddGroupAssociationRestrict_Object_Object() {
        Map associations = null;
        Object value = null;

        try {
            ca.setHandlerRestrictionClass(java.util.AbstractList.class);
            ca.setHandlerRestriction(true);
            ca.addGroupAssociation(new java.awt.Container(), new Vector());
            associations = ca.getGroupAssociations();
            value = associations.get(java.awt.Container.class);
            assertTrue(value instanceof Vector);
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            fail("Should not throw an exception");
        }
    }

    /**
     * Test addGroupAssociation with invalid Class and valid Object
     * and the restriction enabled.
     */
    public void testAddGroupAssociationRestrictFail_Object_Object() {
        Map associations = null;
        Object value = null;

        try {
            ca.setHandlerRestrictionClass(String.class);
            ca.setHandlerRestriction(true);
            ca.addGroupAssociation(new java.awt.Container(), new Vector());
            fail ("An Exception should be thrown. Class is not restriction class or subclass.");
        } catch (Exception ex) {
            // SUCCESS
        }
    }

    /**
     * Test addGroupClassAssociation with a valid Class and Object, no restriction.
     */
    public void testAddGroupClassAssociation_Class_Object() {
        Map associations = null;
        Object value = null;

        try {
            ca.addGroupClassAssociation(java.awt.Container.class, "Test");
            associations = ca.getGroupAssociations();
            value = associations.get(java.awt.Container.class);
            assertTrue(value instanceof String);
            assertTrue(value.equals("Test"));
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            fail("Should not throw an exception");
        }
    }

    /**
     * Test addGroupClassAssociation with a null Class and Object, no restriction.
     */
    public void testAddGroupClassAssociation_Null_Null() {

        try {
            ca.addGroupClassAssociation(null, null);
            fail("An exception should be thrown here.");
        } catch (Exception ex) {
            // SUCCESS
        }
    }

    /**
     * Test getAlgorithm with a valid String.
     */
    public void testGetAlgorithm_String() {

        try {
            AssociationAlgorithm aag = new DefaultAssociationAlgorithm();
            ca.addAlgorithm("testAlgorithm",aag);
            assertTrue (ca.getAlgorithm("testAlgorithm") == aag);
        } catch (Exception ex) {
             fail("An exception should NOT be thrown here.");
        }
    }

    /**
     * Test getAssociations.
     * There should be no exceptions.
     */
    public void testGetAssociations() {
        HashMap associations = null;

        associations = new HashMap();
        associations.put(String.class, "String object");
        associations.put(java.util.Vector.class, new Vector());

        try {
            ca = new ClassAssociator(associations, null);
            assertTrue(ca.getAssociations().size() == 2);
            assertTrue(((ca.getAssociations()).get(java.util.Vector.class)) instanceof Vector);
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            fail("Should not throw an exception");
        }
    }

    /**
     * Test getDefaultAlgorithm
     * There should be no exceptions.
     */
    public void testGetDefaultAlgorithm() {
        try {
            assertTrue(ca.getDefaultAlgorithm() instanceof DefaultAssociationAlgorithm);
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            fail("Should not throw an exception");
        }
    }

    /**
     * Test GetGroupAssociations.
     * There should be no exceptions.
     */
    public void testGetGroupAssociations() {
        HashMap groupAssociations = null;

        groupAssociations = new HashMap();
        groupAssociations.put(java.awt.Container.class, new JFrame());
        groupAssociations.put(java.util.AbstractList.class, new java.util.Vector());

        try {
            ca = new ClassAssociator(null, groupAssociations);
            assertTrue(ca.getGroupAssociations().size() == 2);
            assertTrue(((ca.getGroupAssociations()).get(java.util.AbstractList.class))
                    instanceof Vector);
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            fail("Should not throw an exception");
        }
    }

    /**
     * Test getHandlerRestriction / setHandlerRestriction
     * There should be no exceptions.
     */
    public void testGetSetHandlerRestriction() {
        try {
            ca.setHandlerRestriction(true);
            assertTrue(ca.getHandlerRestriction());
            ca.setHandlerRestriction(false);
            assertFalse(ca.getHandlerRestriction());
        } catch (Exception ex) {
            fail("An exception should NOT be thrown here.");
        }
    }

    /**
     * Test getHandlerRestrictionClass / setHandlerRestrictionClass
     * There should be no exceptions.
     */
    public void testGetSetHandlerRestrictionClass() {
        try {
            ca.setHandlerRestrictionClass(java.util.Vector.class);
            assertTrue(ca.getHandlerRestrictionClass() == java.util.Vector.class );
        } catch (Exception ex) {
            fail("An exception should NOT be thrown here.");
        }
    }

    /**
     * Test removeAlgorithm with a valid String
     */
    public void testRemoveAlgorithm_String() {
        try {
            assertTrue (ca.getAlgorithm("testAlgorithm") == null);
            ca.addAlgorithm("testAlgorithm",
                            new DefaultAssociationAlgorithm());
            assertTrue (ca.getAlgorithm("testAlgorithm") != null);
            ca.removeAlgorithm("testAlgorithm");
            assertTrue (ca.getAlgorithm("testAlgorithm") == null);
        } catch (Exception ex) {
            fail("An exception should NOT be thrown here.");
        }
    }

    /**
     * Test removeAssociation with a valid object
     */
    public void testRemoveAssociation_Object() {
        try {
            ca.addAssociation("String target object", "String value object");
            ca.removeAssociation("String object");
            assertTrue(ca.getAssociations().size() == 0);

        } catch (Exception ex) {
            fail("An exception should NOT be thrown here.");
        }
    }

    /**
     * Test removeClassAssociation with a valid Class
     */
    public void testRemoveClassAssociation_Class() {
        try {
            ca.addClassAssociation(String.class, "Test");
            ca.removeClassAssociation(String.class);
            assertTrue(ca.getAssociations().size() == 0);

        } catch (Exception ex) {
            fail("An exception should NOT be thrown here.");
        }
    }

    /**
     * Test removeGroupAssociation with a valid object
     */
    public void testRemoveGroupAssociation_Object() {
        try {
            ca.addGroupClassAssociation(
                    java.awt.Container.class, "String value object");
            ca.removeGroupAssociation(new java.awt.Container());
            assertTrue(ca.getGroupAssociations().size() == 0);

        } catch (Exception ex) {
            fail("An exception should NOT be thrown here.");
        }
    }

    /**
     * Test removeGroupClassAssociation with a valid Class
     */
    public void testRemoveGroupClassAssociation_Class() {
        try {
            ca.addGroupClassAssociation(
                    java.awt.Container.class, "String value object");
            ca.removeGroupClassAssociation(java.awt.Container.class);
            assertTrue(ca.getGroupAssociations().size() == 0);
        } catch (Exception ex) {
            fail("An exception should NOT be thrown here.");
        }
    }

    /**
     * Test setAssociations with a valid Map
     */
    public void testSetAssociations_Map() {
        HashMap associations = null;

        associations = new HashMap();
        associations.put(String.class, "String object");
        associations.put(java.util.Vector.class, new Vector());

        try {
            ca.setAssociations(associations);
            // since a copy was made, modification to local Map should be change
            // the associations Map in the ClassAssociator
            associations.put(java.util.LinkedList.class, new LinkedList());
            assertTrue(ca.getAssociations().size()
                        == (associations.size() - 1));
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            fail("Should not throw an exception");
        }
    }

    /**
     * Test setDefaultAlgorithm with a AssociationAlgorithm
     */
    public void testSetDefaultAlgorithm_AssocAlgo() {
        try {
            // Verify that the instance of the default algorithm
            // is different after setting the default algorithm
            AssociationAlgorithm oldaag = ca.getDefaultAlgorithm();
            AssociationAlgorithm aag = new DefaultAssociationAlgorithm();
            ca.setDefaultAlgorithm(aag);
            assertTrue(ca.getDefaultAlgorithm() != oldaag);
        } catch (Exception ex) {
            fail("An exception should NOT be thrown here.");
        }
    }

    /**
     * Test setGroupAssociations with a valid Map.
     * There should be no exceptions.
     */
    public void testSetGroupAssociations_Map() {
        HashMap groupAssociations = null;

        groupAssociations = new HashMap();
        groupAssociations.put(java.awt.Container.class, new JFrame());
        groupAssociations.put(java.util.AbstractList.class, new java.util.Vector());

        try {
            ca.setGroupAssociations(groupAssociations);
            // since a copy was made, modification to local Map should be change
            // the groupAssociations Map in the ClassAssociator
            groupAssociations.put(java.util.AbstractCollection.class, new LinkedList());
            assertTrue(ca.getGroupAssociations().size()
                        == (groupAssociations.size() - 1));
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            fail("Should not throw an exception");
        }
    }

    // *********************************************************

    /**
     * Test retrieveHandler with a valid target Object.
     * There should be no exceptions.
     */
    public void testRetrieveHandler_Object() {
        HashMap associations = null;
        HashMap groupAssociations = null;

        associations = new HashMap();
        groupAssociations = new HashMap();
        associations.put(java.util.Vector.class, new Vector());
        groupAssociations.put(java.awt.Container.class, new JFrame());
        groupAssociations.put(java.util.AbstractCollection.class,
                              new java.util.Vector());

        try {
            // build new ClassAssociator with these associations
            ca = new ClassAssociator(associations, groupAssociations);
            // test a direct match scenario
            assertTrue( ca.retrieveHandler(new Vector()) instanceof Vector );
            // test a group match on Class scenario
            assertTrue( ca.retrieveHandler(new Container()) instanceof JFrame );
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            fail("Should not throw an exception");
        }
    }

    /**
     * Test retrieveHandler with a valid target Object and algorithm name.
     * The default algorithm will still be use here, but it will be explicitly
     * named. When explicitly naming the default algorithm, the lookup and
     * execution of the method is no different than if a different algorithm
     * were used.
     * There should be no exceptions.
     */
    public void testRetrieveHandler_Object_String() {
        HashMap associations = null;
        HashMap groupAssociations = null;

        associations = new HashMap();
        groupAssociations = new HashMap();
        associations.put(java.util.Vector.class, new Vector());
        groupAssociations.put(java.awt.Container.class, new JFrame());
        groupAssociations.put(java.util.AbstractCollection.class,
                              new java.util.Vector());

        try {
            // build new ClassAssociator with these associations
            ca = new ClassAssociator(associations, groupAssociations);
            // test a direct match scenario while specifying the algorithm
            assertTrue( ca.retrieveHandler(new Vector(), "default") instanceof Vector );
            // test a group match on Class scenario
            assertTrue( ca.retrieveHandler(new Container(), "default") instanceof JFrame );
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            fail("Should not throw an exception");
        }
    }

    /**
     * Test retrieveClassHandler with a valid target Class.
     * There should be no exceptions.
     */
    public void testRetrieveClassHandler_Class() {
        HashMap associations = null;
        HashMap groupAssociations = null;

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
            assertTrue( ca.retrieveClassHandler(Vector.class) instanceof Vector );
            // test a group match on Class scenario
            assertTrue( ca.retrieveClassHandler(Frame.class) instanceof JFrame );
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            fail("Should not throw an exception");
        }
    }

    /**
     * Test retrieveHandler with a valid target Object and algorithm name.
     * The default algorithm will still be use here, but it will be explicitly
     * named. When explicitly naming the default algorithm, the lookup and
     * execution of the method is no different than if a different algorithm
     * were used.
     * There should be no exceptions.
     */
    public void testRetrieveClassHandler_Class_String() {
        HashMap associations = null;
        HashMap groupAssociations = null;

        associations = new HashMap();
        groupAssociations = new HashMap();
        associations.put(Vector.class, new Vector());
        groupAssociations.put(Container.class, new JFrame());
        groupAssociations.put(AbstractCollection.class, new Vector());

        try {
            // build new ClassAssociator with these associations
            ca = new ClassAssociator(associations, groupAssociations);
            // test a direct match scenario while specifying the algorithm
            assertTrue( ca.retrieveClassHandler(Vector.class, "default") instanceof Vector );
            // test a group match on Class scenario
            assertTrue( ca.retrieveClassHandler(Container.class, "default") instanceof JFrame );
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            fail("Should not throw an exception");
        }
    }

    /**
     * Test retrieveClassHandler with a valid target Class.
     * There should be no exceptions.
     */
    public void testRetrieveClassHandlerInterface_Class() {
        HashMap associations = null;
        HashMap groupAssociations = null;

        associations = new HashMap();
        groupAssociations = new HashMap();
        associations.put(java.util.Vector.class, new Vector());
        groupAssociations.put(Vector.class, new JFrame());
        groupAssociations.put(MenuContainer.class, new Frame());
        groupAssociations.put(AbstractCollection.class,new Vector());

        try {
            // build new ClassAssociator with these associations
            ca = new ClassAssociator(associations, groupAssociations);
            // test a group match scenario where match is an interface
            assertTrue(ca.retrieveClassHandler(MenuContainer.class)
                       instanceof Frame);
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            fail("Should not throw an exception");
        }
    }

}