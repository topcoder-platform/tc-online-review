/*
 * ClassAssociatorAccuracyTests.java vesion 1.0 Created on Apr 2, 2004
 * 
 * Copyright © 2004, TopCoder, Inc. All rights reserved
 * */
package com.topcoder.util.classassociations.accuracytests;

import com.topcoder.util.classassociations.*;
import junit.framework.TestCase;
import java.util.Map;
import java.util.HashMap;

/**
 * TODO - add full javadoc description
 * 
 * @author TCSDEVELOPER
 * @version 1.0
 */
// some example classes 
class Aaa { }
class Bbb extends Aaa { }
class Ccc extends Aaa { }

public class ClassAssociatorAccuracyTests extends TestCase {
    ClassAssociator ca = null; 
    Aaa aa = null;
    Bbb bb = null;
    public void setUp() {
        ca = new ClassAssociator();  
        aa = new Aaa();
        bb = new Bbb();
    }
    
    public void testAddAssociation() throws IllegalHandlerException {
        Object handler = new Integer(1);
        assertNull("no yet handler for Aaa", ca.retrieveHandler(aa));
        // adding Aaa association
        ca.addAssociation(aa, handler);
        assertEquals("now we have handler", handler, ca.retrieveHandler(aa));
        // no handler for Bbb yet
        assertNull("no yet handler for Bbb", ca.retrieveClassHandler(bb.getClass()));
        assertNull("no yet handler for Bbb", ca.retrieveHandler(bb));
    }
    
    public void testRemoveAssociation() throws IllegalHandlerException {
        Object handler = new Integer(2);
        // adding Aaa association
        ca.addAssociation(aa, handler);
        assertEquals("now we have handler", handler, ca.retrieveHandler(aa));
        ca.removeAssociation(aa);
        assertNull("handler for Aaa removed", ca.retrieveHandler(aa));
    }
    
    public void testAddClassAssociation() throws IllegalHandlerException {
        Object handler = new Integer(1);
        assertNull("no yet handler for Aaa", ca.retrieveHandler(aa.getClass()));
        // adding Aaa association
        ca.addClassAssociation(aa.getClass(), handler);
        assertEquals("now we have handler", handler, ca.retrieveClassHandler(aa.getClass()));
        assertEquals("now we have handler", handler, ca.retrieveHandler(aa));
        // no handler for Bbb yet
        assertNull("no yet handler for Bbb", ca.retrieveClassHandler(bb.getClass()));
        assertNull("no yet handler for Bbb", ca.retrieveHandler(bb));
    }
    
    public void testRemoveClassAssociation() throws IllegalHandlerException {
        Object handler = new Integer(2);
        // adding Aaa association
        ca.addClassAssociation(aa.getClass(), handler);
        assertEquals("now we have handler", handler, ca.retrieveClassHandler(aa.getClass()));
        assertEquals("now we have handler", handler, ca.retrieveHandler(aa));
        ca.removeClassAssociation(aa.getClass());
        assertNull("handler for Aaa removed", ca.retrieveClassHandler(aa.getClass()));
        assertNull("handler for Aaa removed", ca.retrieveHandler(aa));
    }    
    public void testAddGroupAssociation() throws IllegalHandlerException {
        Object handler = new Integer(1);
        assertNull("no yet handler for Aaa", ca.retrieveHandler(bb));
        // adding Aaa association
        ca.addGroupAssociation(aa, handler);
        assertEquals("now we have handler", handler, ca.retrieveClassHandler(bb.getClass()));
        assertEquals("now we have handler", handler, ca.retrieveHandler(bb));
    }
    
    public void testRemoveGroupAssociation() throws IllegalHandlerException {
        Object handler = new Integer(2);
        // adding Aaa association
        ca.addGroupAssociation(aa, handler);
        assertEquals("now we have handler", handler, ca.retrieveClassHandler(bb.getClass()));
        assertEquals("now we have handler", handler, ca.retrieveHandler(bb));
        ca.removeGroupAssociation(aa);
        assertNull("handler for Aaa and Bbb removed", ca.retrieveClassHandler(bb.getClass()));
        assertNull("handler for Aaa and Bbb removed", ca.retrieveHandler(bb));
    } 
    
    public void testSetAssociations() throws Exception {
        Object handler = new Integer(3);
        Map ass = new HashMap();
        ass.put(aa.getClass(), handler);
        ca.setAssociations(ass);
        assertEquals("now we have handler", handler, ca.retrieveClassHandler(aa.getClass()));
        assertEquals("now we have handler", handler, ca.retrieveHandler(aa));
        assertNull("no yet handler for Bbb", ca.retrieveHandler(bb));        
    }

    public void testSetGroupAssociations() throws Exception {
        Object handler = new Integer(3);
        Map ass = new HashMap();
        ass.put(aa.getClass(), handler);
        ca.setGroupAssociations(ass);
        assertEquals("now we have handler", handler, ca.retrieveClassHandler(bb.getClass()));
        assertEquals("now we have handler for Aaa", handler, ca.retrieveHandler(bb));
        assertEquals("now we have group handler for Bbb", handler, ca.retrieveHandler(bb));
    }
}
