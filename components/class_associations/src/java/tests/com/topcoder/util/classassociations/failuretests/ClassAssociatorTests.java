/*
 * Class Associations 1.0
 *
 * ClassAssociatorTests.java
 *
 * Copyright (c) 2003 TopCoder, Inc.  All rights reserved
 */
package com.topcoder.util.classassociations.failuretests;

import java.util.HashMap;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.util.classassociations.ClassAssociator;
import com.topcoder.util.classassociations.DefaultAssociationAlgorithm;
import com.topcoder.util.classassociations.IllegalHandlerException;

/**
 * Failure tests for ClassAssociator Class 
 *
 * @author valeriy
 * @version 1.0
 */
public class ClassAssociatorTests extends TestCase {

    /**
     * Tests ClassAssociator(Map, Map) constructor with invalid parameters
     */
    public void testClassAssociatorConstructor() throws Exception {
        try {
            HashMap map = new HashMap(); 
            map.put(null, "abc");
            ClassAssociator test = new ClassAssociator(map, new HashMap());           
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
        } catch (Exception e) {
            fail("IllegalArgumentException expected, but was "+e);
        }
        try {
            HashMap map = new HashMap(); 
            map.put(new Object(), "abc");
            ClassAssociator test = new ClassAssociator(map, new HashMap());           
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
        } catch (Exception e) {
            fail("IllegalArgumentException expected, but was "+e);
        }
        try {
            HashMap map = new HashMap(); 
            map.put(String.class, null);
            ClassAssociator test = new ClassAssociator(map, new HashMap());           
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
        } catch (Exception e) {
            fail("IllegalArgumentException expected, but was "+e);
        }
        try {
            HashMap map = new HashMap(); 
            map.put(null, "abc");
            ClassAssociator test = new ClassAssociator(new HashMap(), map);           
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
        } catch (Exception e) {
            fail("IllegalArgumentException expected, but was "+e);
        }
        try {
            HashMap map = new HashMap(); 
            map.put(new Object(), "abc");
            ClassAssociator test = new ClassAssociator(new HashMap(), map);           
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
        } catch (Exception e) {
            fail("IllegalArgumentException expected, but was "+e);
        }
        try {
            HashMap map = new HashMap(); 
            map.put(String.class, null);
            ClassAssociator test = new ClassAssociator(new HashMap(), map);           
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
        } catch (Exception e) {
            fail("IllegalArgumentException expected, but was "+e);
        }
    }
     
    /**
     * Tests ClassAssociator(Class) constructor with invalid parameters
     */
    public void testClassAssociatorConstructor2() throws Exception {
        try {
            ClassAssociator test = new ClassAssociator(null);           
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
        } catch (Exception e) {
            fail("IllegalArgumentException expected, but was "+e);
        }
    }
    
    /**
     * Tests ClassAssociator.addAssociation(Object, Object) method with invalid parameters
     */
    public void testClassAssociatorAddAssociation() throws Exception {
        ClassAssociator test = new ClassAssociator();           
        try {
            test.addAssociation(null, "abc");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
        } catch (Exception e) {
            fail("IllegalArgumentException expected, but was "+e);
        }
        try {
            test.addAssociation("abc", null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
        } catch (Exception e) {
            fail("IllegalArgumentException expected, but was "+e);
        }
        ClassAssociator test2 = new ClassAssociator(String.class);           
        try {
            test2.addAssociation("abc", new Object());
            fail("IllegalHandlerException expected");
        } catch (IllegalHandlerException e) {
        } catch (Exception e) {
            fail("IllegalHandlerException expected, but was "+e);
        }
    }
    
    /**
     * Tests ClassAssociator.addClassAssociation(Class, Object) method with invalid parameters
     */
    public void testClassAssociatorAddClassAssociation() throws Exception {
        ClassAssociator test = new ClassAssociator();           
        try {
            test.addClassAssociation(null, "abc");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
        } catch (Exception e) {
            fail("IllegalArgumentException expected, but was "+e);
        }
        try {
            test.addClassAssociation("abc".getClass(), null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
        } catch (Exception e) {
            fail("IllegalArgumentException expected, but was "+e);
        }
        ClassAssociator test2 = new ClassAssociator(String.class);           
        try {
            test2.addClassAssociation("abc".getClass(), new Object());
            fail("IllegalHandlerException expected");
        } catch (IllegalHandlerException e) {
        } catch (Exception e) {
            fail("IllegalHandlerException expected, but was "+e);
        }
    }
    
    /**
     * Tests ClassAssociator.addGroupAssociation(Object, Object) method with invalid parameters
     */
    public void testClassAssociatorAddGroupAssociation() throws Exception {
        ClassAssociator test = new ClassAssociator();           
        try {
            test.addGroupAssociation(null, "abc");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
        } catch (Exception e) {
            fail("IllegalArgumentException expected, but was "+e);
        }
        try {
            test.addGroupAssociation("abc", null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
        } catch (Exception e) {
            fail("IllegalArgumentException expected, but was "+e);
        }
        ClassAssociator test2 = new ClassAssociator(String.class);           
        try {
            test2.addGroupAssociation("abc", new Object());
            fail("IllegalHandlerException expected");
        } catch (IllegalHandlerException e) {
        } catch (Exception e) {
            fail("IllegalHandlerException expected, but was "+e);
        }
    }
    
    /**
     * Tests ClassAssociator.addGroupClassAssociation(Class, Object) method with invalid parameters
     */
    public void testClassAssociatorAddGroupClassAssociation() throws Exception {
        ClassAssociator test = new ClassAssociator();           
        try {
            test.addGroupClassAssociation(null, "abc");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
        } catch (Exception e) {
            fail("IllegalArgumentException expected, but was "+e);
        }
        try {
            test.addGroupClassAssociation("abc".getClass(), null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
        } catch (Exception e) {
            fail("IllegalArgumentException expected, but was "+e);
        }
        ClassAssociator test2 = new ClassAssociator(String.class);           
        try {
            test2.addGroupClassAssociation("abc".getClass(), new Object());
            fail("IllegalHandlerException expected");
        } catch (IllegalHandlerException e) {
        } catch (Exception e) {
            fail("IllegalHandlerException expected, but was "+e);
        }
    }
    
    /**
     * Tests ClassAssociator.removeAssociation(Object) method with invalid parameters
     */
    public void testClassAssociatorRemoveAssociation() throws Exception {
        ClassAssociator test = new ClassAssociator();           
        try {
            test.removeAssociation(null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
        } catch (Exception e) {
            fail("IllegalArgumentException expected, but was "+e);
        }
    }
    
    /**
     * Tests ClassAssociator.removeClassAssociation(Class) method with invalid parameters
     */
    public void testClassAssociatorRemoveClassAssociation() throws Exception {
        ClassAssociator test = new ClassAssociator();           
        try {
            test.removeClassAssociation(null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
        } catch (Exception e) {
            fail("IllegalArgumentException expected, but was "+e);
        }
    }
    
    /**
     * Tests ClassAssociator.removeGroupAssociation(Object) method with invalid parameters
     */
    public void testClassAssociatorRemoveGroupAssociation() throws Exception {
        ClassAssociator test = new ClassAssociator();           
        try {
            test.removeGroupAssociation(null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
        } catch (Exception e) {
            fail("IllegalArgumentException expected, but was "+e);
        }
    }
    
    /**
     * Tests ClassAssociator.removeGroupClassAssociation(Class) method with invalid parameters
     */
    public void testClassAssociatorRemoveGroupClassAssociation() throws Exception {
        ClassAssociator test = new ClassAssociator();           
        try {
            test.removeGroupClassAssociation(null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
        } catch (Exception e) {
            fail("IllegalArgumentException expected, but was "+e);
        }
    }
    
    /**
     * Tests ClassAssociator.setAssociations(Map) method with invalid parameters
     */
    public void testClassAssociatorSetAssociations() throws Exception {
        ClassAssociator test = new ClassAssociator();           
        try {
            HashMap map = new HashMap(); 
            map.put(null, "abc");
            test.setAssociations(map);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
        } catch (Exception e) {
            fail("IllegalArgumentException expected, but was "+e);
        }
        try {
            HashMap map = new HashMap(); 
            map.put(new Object(), "abc");
            test.setAssociations(map);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
        } catch (Exception e) {
            fail("IllegalArgumentException expected, but was "+e);
        }
        try {
            HashMap map = new HashMap(); 
            map.put(String.class, null);
            test.setAssociations(map);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
        } catch (Exception e) {
            fail("IllegalArgumentException expected, but was "+e);
        }
        ClassAssociator test2 = new ClassAssociator(String.class);           
        try {
            HashMap map = new HashMap(); 
            map.put("abc".getClass(), new Object());
            test2.setAssociations(map);
            fail("IllegalHandlerException expected");
        } catch (IllegalHandlerException e) {
        } catch (Exception e) {
            fail("IllegalHandlerException expected, but was "+e);
        }
    }
     
    /**
     * Tests ClassAssociator.setGroupAssociations(Map) method with invalid parameters
     */
    public void testClassAssociatorSetGroupAssociations() throws Exception {
        ClassAssociator test = new ClassAssociator();           
        try {
            HashMap map = new HashMap(); 
            map.put(null, "abc");
            test.setGroupAssociations(map);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
        } catch (Exception e) {
            fail("IllegalArgumentException expected, but was "+e);
        }
        try {
            HashMap map = new HashMap(); 
            map.put(new Object(), "abc");
            test.setGroupAssociations(map);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
        } catch (Exception e) {
            fail("IllegalArgumentException expected, but was "+e);
        }
        try {
            HashMap map = new HashMap(); 
            map.put(String.class, null);
            test.setGroupAssociations(map);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
        } catch (Exception e) {
            fail("IllegalArgumentException expected, but was "+e);
        }
        ClassAssociator test2 = new ClassAssociator(String.class);           
        try {
            HashMap map = new HashMap(); 
            map.put("abc".getClass(), new Object());
            test2.setGroupAssociations(map);
            fail("IllegalHandlerException expected");
        } catch (IllegalHandlerException e) {
        } catch (Exception e) {
            fail("IllegalHandlerException expected, but was "+e);
        }
    }
     
    /**
     * Tests ClassAssociator.addAlgorithm(String, AssociationAlgorithm) method with invalid parameters
     */
    public void testClassAssociatorAddAlgorithm() throws Exception {
        ClassAssociator test = new ClassAssociator();           
        try {
            test.addAlgorithm(null, new DefaultAssociationAlgorithm());
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
        } catch (Exception e) {
            fail("IllegalArgumentException expected, but was "+e);
        }
        try {
            test.addAlgorithm("abc", null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
        } catch (Exception e) {
            fail("IllegalArgumentException expected, but was "+e);
        }
    }
    
    /**
     * Tests ClassAssociator.removeAlgorithm(String) method with invalid parameters
     */
    public void testClassAssociatorRemoveAlgorithm() throws Exception {
        ClassAssociator test = new ClassAssociator();           
        try {
            test.removeAlgorithm(null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
        } catch (Exception e) {
            fail("IllegalArgumentException expected, but was "+e);
        }
        try {
            test.removeAlgorithm(ClassAssociator.DEFAULT_ALGORITHM);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
        } catch (Exception e) {
            fail("IllegalArgumentException expected, but was "+e);
        }
    }
    
    /**
     * Tests ClassAssociator.setDefaultAlgorithm(String) method with invalid parameters
     */
    public void testClassAssociatorSetDefaultAlgorithm() throws Exception {
        ClassAssociator test = new ClassAssociator();           
        try {
            test.setDefaultAlgorithm(null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
        } catch (Exception e) {
            fail("IllegalArgumentException expected, but was "+e);
        }
    }
    
    /**
     * Tests ClassAssociator.setHandlerRestriction(boolean) method with invalid parameters
     */
    public void testClassAssociatorSetHandlerRestriction() throws Exception {
        ClassAssociator test = new ClassAssociator(String.class);
        test.setHandlerRestriction(false);           
        HashMap map = new HashMap(); 
        map.put("abc".getClass(), new Object());
        test.setAssociations(map);
        try {
            test.setHandlerRestriction(true);
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
        } catch (Exception e) {
            fail("IllegalStateException expected, but was "+e);
        }
        ClassAssociator test2 = new ClassAssociator(String.class);
        test2.setHandlerRestriction(false);           
        test2.setGroupAssociations(map);
        try {
            test2.setHandlerRestriction(true);
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
        } catch (Exception e) {
            fail("IllegalStateException expected, but was "+e);
        }
    }
     
    /**
     * Tests ClassAssociator.setHandlerRestrictionClass(Class) method with invalid parameters
     */
    public void testClassAssociatorSetHandlerRestrictionClass() throws Exception {
        ClassAssociator test = new ClassAssociator();
        try {
            test.setHandlerRestrictionClass(null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
        } catch (Exception e) {
            fail("IllegalArgumentException expected, but was "+e);
        }
        HashMap map = new HashMap(); 
        map.put("abc".getClass(), new Object());
        test.setAssociations(map);
        test.setHandlerRestriction(true);
        try {
            test.setHandlerRestrictionClass(String.class);
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
        } catch (Exception e) {
            fail("IllegalStateException expected, but was "+e);
        }
        ClassAssociator test2 = new ClassAssociator();
        test2.setGroupAssociations(map);
        test2.setHandlerRestriction(true);
        try {
            test2.setHandlerRestrictionClass(String.class);
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
        } catch (Exception e) {
            fail("IllegalStateException expected, but was "+e);
        }
    }

    /**
     * Tests ClassAssociator.retrieveHandler(Object) method with invalid parameters
     */
    public void testClassAssociatorRetrieveHandler() throws Exception {
        ClassAssociator test = new ClassAssociator();           
        try {
            test.retrieveHandler(null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
        } catch (Exception e) {
            fail("IllegalArgumentException expected, but was "+e);
        }
    }
    
    /**
     * Tests ClassAssociator.retrieveClassHandler(Class) method with invalid parameters
     */
    public void testClassAssociatorRetrieveClassHandler() throws Exception {
        ClassAssociator test = new ClassAssociator();           
        try {
            test.retrieveClassHandler(null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
        } catch (Exception e) {
            fail("IllegalArgumentException expected, but was "+e);
        }
    }
    
    /**
     * Tests ClassAssociator.retrieveHandler(Object, String) method with invalid parameters
     */
    public void testClassAssociatorRetrieveHandler2() throws Exception {
        ClassAssociator test = new ClassAssociator();           
        try {
            test.retrieveHandler(null, ClassAssociator.DEFAULT_ALGORITHM);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
        } catch (Exception e) {
            fail("IllegalArgumentException expected, but was "+e);
        }
        try {
            test.retrieveHandler(new Object(), null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
        } catch (Exception e) {
            fail("IllegalArgumentException expected, but was "+e);
        }
        try {
            test.retrieveHandler(new Object(), "abc");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
        } catch (Exception e) {
            fail("IllegalArgumentException expected, but was "+e);
        }
    }
    
    /**
     * Tests ClassAssociator.retrieveClassHandler(Class, String) method with invalid parameters
     */
    public void testClassAssociatorRetrieveClassHandler2() throws Exception {
        ClassAssociator test = new ClassAssociator();           
        try {
            test.retrieveClassHandler(null, ClassAssociator.DEFAULT_ALGORITHM);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
        } catch (Exception e) {
            fail("IllegalArgumentException expected, but was "+e);
        }
        try {
            test.retrieveClassHandler(String.class, null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
        } catch (Exception e) {
            fail("IllegalArgumentException expected, but was "+e);
        }
        try {
            test.retrieveClassHandler(String.class, "abc");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
        } catch (Exception e) {
            fail("IllegalArgumentException expected, but was "+e);
        }
    }
    
    /**
     * Tests ClassAssociator.getAlgorithm(String) method with invalid parameters
     */
    public void testClassAssociatorGetAlgorithm() throws Exception {
        ClassAssociator test = new ClassAssociator();           
        try {
            test.getAlgorithm(null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
        } catch (Exception e) {
            fail("IllegalArgumentException expected, but was "+e);
        }
    }
    
    public static Test suite() {
        return new TestSuite(ClassAssociatorTests.class);
    }
    
}
