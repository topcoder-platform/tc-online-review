/*
 * DefaultAlgorithmAccuracyTests.java vesion 1.0 Created on Apr 2, 2004
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
interface Ia {}
interface Ib {}
class Aaaa { }
class ABbb extends Aaaa implements Ib { }
class ACcc extends Aaaa implements Ia { }
class ACDdd extends ACcc {}
class Eee {}
public class DefaultAlgorithmAccuracyTests extends TestCase {
    ClassAssociator ca = null; 
    Aaaa aa = null;
    ABbb bb = null;
    ACcc cc = new ACcc();
    ACDdd dd = new ACDdd();
    Eee ee = new Eee();
    public void setUp() {
        ca = new ClassAssociator();  
        aa = new Aaaa();
        bb = new ABbb();
    }
    
    public void testGroupAndClassHandlers() throws Exception {
        Object groupHandler = new Integer(1);
        Object classHandler = new Integer(2);
        ca.addGroupAssociation(aa, groupHandler);
        ca.addAssociation(bb, classHandler);
        assertEquals("aa", groupHandler, ca.retrieveHandler(aa));
        assertEquals("bb", classHandler, ca.retrieveHandler(bb));
        assertNull("ee", ca.retrieveHandler(ee));        
    }
    
    public void testHeirarchy() throws Exception {
        Object groupHandler = new Integer(1);
        Object classHandler = new Integer(2);
        ca.addGroupAssociation(aa, groupHandler);
        ca.addAssociation(cc, classHandler);
        assertEquals("aa", groupHandler, ca.retrieveHandler(aa));
        assertEquals("cc", classHandler, ca.retrieveHandler(cc));
        assertEquals("dd", groupHandler, ca.retrieveHandler(dd));
        assertNull("ee", ca.retrieveHandler(ee));        
    }
    
    public void testInterfaces() throws Exception {
        Object groupHandler = new Integer(1);
        Object classHandler = new Integer(2);
        Object interfaceHandler = new Integer(3);
        
        // interface can be returned
        ca.addGroupClassAssociation(Ib.class, interfaceHandler);        
        assertEquals("bb", interfaceHandler, ca.retrieveHandler(bb));
        ca.addClassAssociation(ABbb.class, classHandler);        
        assertEquals("bb", classHandler, ca.retrieveHandler(bb));
    }
    
    public void testGroupAndInterfaces() throws Exception {
        Object groupHandler = new Integer(1);
        Object classHandler = new Integer(2);
        Object interfaceHandler = new Integer(3);
        
        // interface can be returned
        ca.addGroupClassAssociation(Ia.class, interfaceHandler);        
        assertEquals("dd", interfaceHandler, ca.retrieveHandler(dd));
        ca.addGroupAssociation(aa, groupHandler);        
        assertEquals("dd", groupHandler, ca.retrieveHandler(dd));
    }
}
