/*
 * ObjectFormatterFactoryTest.java
 *
 * Copyright © 2002, TopCoder, Inc. All rights reserved
 *
 */

package com.topcoder.util.format;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.util.*;

/**
 * A test class for <code>ObjectFormatterFactory</code>.<p>
 *
 * @author garyk
 * @version 1.0
 **/
public class ObjectFormatterFactoryTest extends TestCase { 

    /**
     * Creates a empty object formatter
     **/
    private ObjectFormatter createEmptyObjectFormatter() {
        return ObjectFormatterFactory.getEmptyFormatter();
    }

    /**
     * Creates a plain object formatter
     **/
    private ObjectFormatter createPlainObjectFormatter() {
        return ObjectFormatterFactory.getPlainFormatter();
    }

    /**
     * Creates a pretty object formatter
     **/
    private ObjectFormatter createPrettyObjectFormatter() {
        return ObjectFormatterFactory.getPrettyFormatter();
    }
    
    /**
     * Creates a object formatter method for Integer
     **/
    private ObjectFormatMethod createTestObjectFormatMethod() {
        return new ObjectFormatMethod() {
                    public String format(Object obj) {
                       return "Bob! " + ((Integer)obj).intValue();
                    }
               };
    }

    /**
     * Tests creating <code>ObjectFormatter</code>s
     **/
    public void testCreateObjectFormatter() {
        ObjectFormatter of = createEmptyObjectFormatter();
        assertTrue("Creating empty ObjectFormatter", of != null);

        of = createPlainObjectFormatter();
        assertTrue("Creating plain ObjectFormatter", of != null);

        of = createPrettyObjectFormatter();
        assertTrue("Creating pretty ObjectFormatter", of != null);
    }
    
    /**
     * Tests formatting a <code>Byte</code> 
     **/ 
    public void testFormatByteWithPrettyObjectFormatter() {
        Byte b = new Byte((byte)8);

        ObjectFormatter of = createPrettyObjectFormatter();
        assertEquals("8", of.format(b));
    }

    /**
     * Tests formatting a <code>Character</code> 
     **/ 
	public void testFormatCharWithPrettyObjectFormatter() {
        Character c = new Character('c');

        ObjectFormatter of = createPrettyObjectFormatter();
        assertEquals("99", of.format(c));
    }

    /**
     * Tests formatting a <code>Short</code> 
     **/ 
    public void testFormatShortWithPrettyObjectFormatter() {
        Short s = new Short((short)-12);

        ObjectFormatter of = createPrettyObjectFormatter();
        assertEquals("-12", of.format(s));
    }

    /**
     * Tests formatting a <code>Integer/code> 
     **/ 
    public void testFormatIntegerWithPrettyObjectFormatter() {
        Integer i = new Integer(1234);

        ObjectFormatter of = createPrettyObjectFormatter();
        assertEquals("1,234", of.format(i));
    }

    /**
     * Tests formatting a <code>Long</code> 
     **/ 
    public void testFormatLongWithPrettyObjectFormatter() {
        Long l = new Long((long)12345);

        ObjectFormatter of = createPrettyObjectFormatter();
        assertEquals("12,345", of.format(l));
    }

    /**
     * Tests formatting a <code>Float</code> 
     **/ 
    public void testFormatFloatWithPrettyObjectFormatter() {
        Float f = new Float((float)12345.678);

        ObjectFormatter of = createPrettyObjectFormatter();
        assertEquals("12,345.68", of.format(f));
    }

    /**
     * Tests formatting a <code>Double</code> 
     **/ 
    public void testFormatDoubleWithPrettyObjectFormatter() {
        Double d = new Double(-1234567.890);

        ObjectFormatter of = createPrettyObjectFormatter();
        assertEquals("-1,234,567.89", of.format(d));
    }

    /**
     * Tests formatting a <code>Date</code> 
     **/ 
    public void testFormatDateWithPrettyObjectFormatter() {
        Calendar c = Calendar.getInstance();
        /* December 25, 2002, 00:00:00 */
        c.set(2002, Calendar.DECEMBER, 25, 0, 0, 0);   
        Date d = c.getTime(); 

        ObjectFormatter of = createPrettyObjectFormatter();
        assertEquals("12/25/2002", of.format(d));
    }

    /**
     * Tests adding a <code>ObjectFormatMethod</code> to 
     * <code>ObjectFormatter</code>
     **/ 
    public void testAddFormatMethodToObjectFormatter() {
        ObjectFormatter of = createEmptyObjectFormatter();
        ObjectFormatMethod ofm = createTestObjectFormatMethod();
        of.setFormatMethodForClass(Integer.class, ofm, true);
        assertEquals("Using empty ObjectFormatter", ofm,
                of.getFormatMethodForClass(Integer.class));

        of = createPlainObjectFormatter();
        of.setFormatMethodForClass(Integer.class, ofm, true);
        assertEquals("Using plain ObjectFormatter", ofm, 
                of.getFormatMethodForClass(Integer.class));

        of = createPrettyObjectFormatter();
        of.setFormatMethodForClass(Integer.class, ofm, true);
        assertEquals("Using pretty ObjectFormatter", ofm,     
                of.getFormatMethodForClass(Integer.class));
    }    

    /**
     * Tests removing a <code>ObjectFormatMethod</code> from 
     * <code>ObjectFormatter</code> 
     **/ 
    public void testRemoveFormatMethodFromObjectFormatter() 
    {
        ObjectFormatter of = createEmptyObjectFormatter();
        ObjectFormatMethod ofm = createTestObjectFormatMethod();
        of.setFormatMethodForClass(Integer.class, ofm, true);
        of.unsetFormatMethodForClass(Integer.class);
        assertEquals("Using empty ObjectFormatter", null, 
                of.getFormatMethodForClass(Integer.class));

        of = createPlainObjectFormatter();
        of.setFormatMethodForClass(Integer.class, ofm, true);
        of.unsetFormatMethodForClass(Integer.class);
        assertEquals("Using plain ObjectFormatter", null, 
                of.getFormatMethodForClass(Integer.class));

        of = createPrettyObjectFormatter();
        of.setFormatMethodForClass(Integer.class, ofm, true);
        of.unsetFormatMethodForClass(Integer.class);
        assertEquals("Using pretty ObjectFormatter", null, 
                of.getFormatMethodForClass(Integer.class));
    }
    
    /**
     * Tests formatting an <code>Object</code>, which doesn't have 
     * associated ObjectFormatMethod in the ObjectFormatter, but some 
     * of its supertypes are in the ObjectFormatter. There are two
     * possible cases: 
     * 1. There is a supertype in the ObjectFormatter, which is sub type 
     *    of all the other supertypes
     * 2. This kind of supertype doesn't exist, and then this Object 
     *    can't be formatted.
     **/
    public void testFormatObject() 
    {
        ObjectFormatter of = createPrettyObjectFormatter();
        
        ObjectFormatMethod ofmCollection = new ObjectFormatMethod() {
            public String format(Object obj) {
                if (!(obj instanceof Collection)) {
                    throw new IllegalArgumentException("expected Collection");
                }

                return "Collection! " + obj;
            }
        };

        ObjectFormatMethod ofmList = new ObjectFormatMethod() {
            public String format(Object obj) {
                if (!(obj instanceof List)) {
                    throw new IllegalArgumentException("expected List");
                }

                return "List! " + obj;
            }
        };

        ObjectFormatMethod ofmRandomAccess = new ObjectFormatMethod() {
            public String format(Object obj) {
                if (!(obj instanceof RandomAccess)) {
                    throw new IllegalArgumentException("expected RandomAccess");
                }

                return "RandomAccess! " + obj;
            }
        };
        
        of.setFormatMethodForClass(Collection.class,   ofmCollection,   true);
        of.setFormatMethodForClass(List.class,         ofmList,         true);
        of.setFormatMethodForClass(RandomAccess.class, ofmRandomAccess, true);
        
        LinkedList ll = new LinkedList();
        ll.add(new Integer(1));
        ll.add(new Integer(2));
        ll.add(new Integer(3));

        /* 
         *LinkedList should be formatted with the format method of List class,
         * which is the closest supertype of LinkedList
         */
        assertEquals("LinkedList should be formatted", ofmList.format(ll), 
                of.format(ll));

        ArrayList al = new ArrayList(ll);
        try {
            String bob = of.format(al);

            /* 
             * ArrayList cannot be formatted, because List and RandomAccess 
             * are both super-types of ArrayList, but neither is a sub-type 
             * of the other.
             */
            fail("ArrayList should not be formatted");
        } catch (IllegalArgumentException iae) {
        } catch (Exception e) {
            fail("Only IllegalArgumentException should be throuwn");
        }

        /*
         * Set the fFormatSubtypes of the RandomAccess class to be false so
         * that the subtypes of RandomAccess can't be formatted with its 
         * format method
         */
        of.setFormatMethodForClass(RandomAccess.class, ofmRandomAccess, false);
        assertEquals("ArrayList should be formatted now", ofmList.format(ll), 
                of.format(al));
    }

    /**
     * Assembles and returns a test suite for
     * all the test methods of this test case.
     *
     * @return A non-null test suite.
     */
    public static Test suite() {
        /*
         * Reflection is used here to add all
         * the testXXX() methods to the suite.
         */
        TestSuite suite = new TestSuite(ObjectFormatterFactoryTest.class);

        return suite;
    }
}