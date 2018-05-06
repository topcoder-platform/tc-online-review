/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.collection.typesafeenum.accuracytests;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

import com.topcoder.util.collection.typesafeenum.Enum;

import junit.framework.TestCase;

/**
 * <p>
 * This class tests all public methods defined in the Enum class.
 * </p>
 *
 * @author georgepf, Yeung
 * @version 1.1
 * @since 1.0
 */
public class EnumAccuracyTests extends TestCase {
    // define several subclasses of Enum for testing; hierarchy follows:
    // Enum <-- Boolean
    // ^ ^----- Desert
    // |------- Island (String name)

    /**
     * The testing Enum type.
     *
     * @author georgepf, Yeung
     * @version 1.1
     * @since 1.0
     */
    public static class Boolean extends Enum {
        /** FALSE element for Boolean enum. */
        public static final Boolean FALSE = new Boolean();

        /** TRUE element for Boolean enum. */
        public static final Boolean TRUE = new Boolean();

        /**
         * The only one constructor thich is private to disallow creation of another items for this Enum.
         */
        private Boolean() {
        }
    }

    /**
     * The testing Enum type.
     *
     * @author georgepf, Yeung
     * @version 1.1
     * @since 1.0
     */
    public static class Desert extends Enum {
        /** ICECREAM element for Desert enum. */
        public static final Desert ICECREAM = new Desert();

        /** PUDDING element for Desert enum. */
        public static final Desert PUDDING = new Desert();

        /** KUR element for Desert enum. */
        public static final Desert KUR = new Desert();

        /**
         * The only one constructor thich is private to disallow creation of another items for this Enum.
         */
        private Desert() {
        }
    }

    /**
     * The testing Enum type.
     *
     * @author georgepf, Yeung
     * @version 1.1
     * @since 1.0
     */
    public static class Island extends Enum {
        /** the island name. */
        private String name;

        /**
         * Constructs an Island instance given a name.
         *
         * @param name
         *            The island's name
         */
        public Island(String name) {
            super(Island.class);
            this.name = name;
        }

        /**
         * Returns the island's name.
         *
         * @return The name of this island
         */
        public String toString() {
            return name;
        }
    }

    /** All Boolean elements. */
    private static Enum[] allBoolean = {Boolean.FALSE, Boolean.TRUE};

    /** All Boolean elements' name. */
    private static String[] allBooleanName = {"FALSE", "TRUE"};

    /** All Desert elements. */
    private static Enum[] allDesert = {Desert.ICECREAM, Desert.PUDDING, Desert.KUR};

    /** All Desert elements' name. */
    private static String[] allDesertName = {"ICECREAM", "PUDDING", "KUR"};

    /** All Island elements' name. */
    private static String[] islandNames = {"Java", "Borneo", "Sumatra", "Bali", "Sulawesi", "Nusa Tenggara",
        "Irian Jaya"};

    /** All Island elements. */
    private static Enum[] allIsland = null;

    /** All elements. */
    private static Enum[] allEnum = null;

    /**
     * Sets up test data.
     */
    protected void setUp() {
        // initialize the allIsland array
        if (allIsland == null) {
            allIsland = new Enum[islandNames.length];
            for (int i = 0; i < islandNames.length; i++) {
                allIsland[i] = new Island(islandNames[i]);
            }
        }
        // initialize the allEnum array
        if (allEnum == null) {
            allEnum = new Enum[allBoolean.length + allDesert.length + allIsland.length];
            Enum[][] all = new Enum[][] {allBoolean, allDesert, allIsland};
            int c = 0;
            for (int i = 0; i < all.length; i++) {
                for (int j = 0; j < all[i].length; j++) {
                    allEnum[c++] = all[i][j];
                }
            }
        }
    }

    /**
     * Tests the class hierarchy of <code>Enum</code> objects.
     */
    public void testClassHierarchy() {
        for (int i = 0; i < allBoolean.length; i++) {
            String s = "object " + i + " should be an instance of ";
            assertTrue(s + "Boolean", allBoolean[i] instanceof Boolean);
            assertTrue(s + "Enum", allBoolean[i] instanceof Enum);
            assertTrue(s + "Comparable", allBoolean[i] instanceof Comparable);
            assertTrue(s + "Serializable", allBoolean[i] instanceof Serializable);
            assertTrue(s + "DeclaringClass", allBoolean[i].getDeclaringClass() == Boolean.class);
        }

        for (int i = 0; i < allDesert.length; i++) {
            String s = "object " + i + " should be an instance of ";
            assertTrue(s + "Desert", allDesert[i] instanceof Desert);
            assertTrue(s + "Enum", allDesert[i] instanceof Enum);
            assertTrue(s + "Comparable", allDesert[i] instanceof Comparable);
            assertTrue(s + "Serializable", allDesert[i] instanceof Serializable);
            assertTrue(s + "DeclaringClass", allDesert[i].getDeclaringClass() == Desert.class);
        }

        for (int i = 0; i < allIsland.length; i++) {
            String s = "object " + i + " should be an instance of ";
            assertTrue(s + "Island", allIsland[i] instanceof Island);
            assertTrue(s + "Enum", allIsland[i] instanceof Enum);
            assertTrue(s + "Comparable", allIsland[i] instanceof Comparable);
            assertTrue(s + "Serializable", allIsland[i] instanceof Serializable);
            assertTrue(s + "DeclaringClass", allIsland[i].getDeclaringClass() == Island.class);
        }
    }

    /**
     * Tests the <code>getOrdinal</code> method.
     */
    public void testGetOrdinal() {
        // check whether different objects of the same class are assigned different ordinals
        for (int i = 0; i < allBoolean.length; i++) {
            for (int j = 0; j < allBoolean.length; j++) {
                if (i == j) {
                    assertTrue("the ordinals of objects " + i + " and " + j + " should be the same", allBoolean[i]
                            .getOrdinal() == allBoolean[j].getOrdinal());
                } else {
                    assertTrue("the ordinals of objects " + i + " and " + j + " should not be the same", allBoolean[i]
                            .getOrdinal() != allBoolean[j].getOrdinal());
                }
            }
        }

        for (int i = 0; i < allDesert.length; i++) {
            for (int j = 0; j < allDesert.length; j++) {
                if (i == j) {
                    assertTrue("the ordinals of objects " + i + " and " + j + " should be the same", allDesert[i]
                            .getOrdinal() == allDesert[j].getOrdinal());
                } else {
                    assertTrue("the ordinals of objects " + i + " and " + j + " should not be the same", allDesert[i]
                            .getOrdinal() != allDesert[j].getOrdinal());
                }
            }
        }

        for (int i = 0; i < allIsland.length; i++) {
            for (int j = 0; j < allIsland.length; j++) {
                if (i == j) {
                    assertTrue("the ordinals of objects " + i + " and " + j + " should be the same", allIsland[i]
                            .getOrdinal() == allIsland[j].getOrdinal());
                } else {
                    assertTrue("the ordinals of objects " + i + " and " + j + " should not be the same", allIsland[i]
                            .getOrdinal() != allIsland[j].getOrdinal());
                }
            }
        }
    }

    /**
     * Tests the <code>getEnumByOrdinal</code> method.
     */
    public void testGetEnumByOrdinal() {
        for (int i = 0; i < allBoolean.length; i++) {
            assertTrue("the object returned should match object " + i, Enum.getEnumByOrdinal(
                    allBoolean[i].getOrdinal(), allBoolean[i].getDeclaringClass()) == allBoolean[i]);
            assertTrue("the object returned should match object " + i, Enum.getEnumByOrdinal(
                    allBoolean[i].getOrdinal(), Boolean.class) == allBoolean[i]);
        }

        for (int i = 0; i < allDesert.length; i++) {
            assertTrue("the object returned should match object " + i, Enum.getEnumByOrdinal(allDesert[i].getOrdinal(),
                    allDesert[i].getDeclaringClass()) == allDesert[i]);
            assertTrue("the object returned should match object " + i, Enum.getEnumByOrdinal(allDesert[i].getOrdinal(),
                    Desert.class) == allDesert[i]);
        }

        for (int i = 0; i < allIsland.length; i++) {
            assertTrue("the object returned should match object " + i, Enum.getEnumByOrdinal(allIsland[i].getOrdinal(),
                    allIsland[i].getDeclaringClass()) == allIsland[i]);
            assertTrue("the object returned should match object " + i, Enum.getEnumByOrdinal(allIsland[i].getOrdinal(),
                    Island.class) == allIsland[i]);
        }
    }

    /**
     * Tests the <code>getEnumName</code> method and the <code>getEnumByName</code> method.
     *
     * @throws Exception
     *             to junit
     */
    public void testEnumName() throws Exception {
        for (int i = 0; i < allBoolean.length; i++) {
            assertTrue("the object returned should match object " + i,
                    allBoolean[i].getEnumName() == allBooleanName[i]);
            assertTrue("the object returned should match object " + i, Enum.getEnumByName(allBooleanName[i],
                    Boolean.class) == allBoolean[i]);
        }

        for (int i = 0; i < allDesert.length; i++) {
            assertTrue("the object returned should match object " + i, allDesert[i].getEnumName() == allDesertName[i]);
            assertTrue("the object returned should match object " + i, Enum.getEnumByName(allDesertName[i],
                    Desert.class) == allDesert[i]);
        }

        for (int i = 0; i < allIsland.length; i++) {
            assertTrue("the object returned should match object " + i, allIsland[i].getEnumName() == null);
        }
    }

    /**
     * Tests the <code>getEnumByStringValue</code> method.
     */
    public void testGetEnumByStringValue() {
        for (int i = 0; i < allBoolean.length; i++) {
            assertTrue("the object returned should match object " + i, Enum.getEnumByStringValue(allBoolean[i]
                    .toString(), allBoolean[i].getDeclaringClass()) == allBoolean[i]);
            assertTrue("the object returned should match object " + i, Enum.getEnumByStringValue(allBoolean[i]
                    .toString(), Boolean.class) == allBoolean[i]);
        }

        for (int i = 0; i < allDesert.length; i++) {
            assertTrue("the object returned should match object " + i, Enum.getEnumByStringValue(allDesert[i]
                    .toString(), allDesert[i].getDeclaringClass()) == allDesert[i]);
            assertTrue("the object returned should match object " + i, Enum.getEnumByStringValue(allDesert[i]
                    .toString(), Desert.class) == allDesert[i]);
        }

        for (int i = 0; i < allIsland.length; i++) {
            assertTrue("the object returned should match object " + i, Enum.getEnumByStringValue(allIsland[i]
                    .toString(), allIsland[i].getDeclaringClass()) == allIsland[i]);
            assertTrue("the object returned should match object " + i, Enum.getEnumByStringValue(allIsland[i]
                    .toString(), Island.class) == allIsland[i]);
        }
    }

    /**
     * Tests the <code>getEnumList</code> method.
     */
    public void testGetEnumList() {
        List l;

        l = Enum.getEnumList(Boolean.class);
        assertTrue("the returned list should not be null", l != null);
        assertTrue("the length of the returned list should be " + allBoolean.length + " and not " + l.size(),
                l.size() == allBoolean.length);
        for (int i = 0; i < l.size(); i++) {
            assertTrue("objects should be the same at index " + i, l.get(i) == allBoolean[i]);
        }

        l = Enum.getEnumList(Desert.class);
        assertTrue("the returned list should not be null", l != null);
        assertTrue("the length of the returned list should be " + allDesert.length + " and not " + l.size(),
                l.size() == allDesert.length);
        for (int i = 0; i < l.size(); i++) {
            assertTrue("objects should be the same at index " + i, l.get(i) == allDesert[i]);
        }

        l = Enum.getEnumList(Island.class);
        assertTrue("the returned list should not be null", l != null);
        assertTrue("the length of the returned list should be " + allIsland.length + " and not " + l.size(),
                l.size() == allIsland.length);
        for (int i = 0; i < l.size(); i++) {
            assertTrue("objects should be the same at index " + i, l.get(i) == allIsland[i]);
        }
    }

    /**
     * Tests the <code>Comparable</code> interface (i.e., the <code>compareTo</code> method).
     */
    public void testComparable() {
        // for accuracy testing only test between objects of the same enumeration class
        for (int i = 0; i < allBoolean.length - 1; i++) {
            assertTrue("incorrect comparison result for object " + i, allBoolean[i].compareTo(allBoolean[i + 1]) < 0);
            assertTrue("incorrect comparison result for object " + i, allBoolean[i + 1].compareTo(allBoolean[i]) > 0);
            assertTrue("incorrect comparison result for object " + i, allBoolean[i].compareTo(allBoolean[i]) == 0);
        }

        for (int i = 0; i < allDesert.length - 1; i++) {
            assertTrue("incorrect comparison result for object " + i, allDesert[i].compareTo(allDesert[i + 1]) < 0);
            assertTrue("incorrect comparison result for object " + i, allDesert[i + 1].compareTo(allDesert[i]) > 0);
            assertTrue("incorrect comparison result for object " + i, allDesert[i].compareTo(allDesert[i]) == 0);
        }

        for (int i = 0; i < allIsland.length - 1; i++) {
            assertTrue("incorrect comparison result for object " + i, allIsland[i].compareTo(allIsland[i + 1]) < 0);
            assertTrue("incorrect comparison result for object " + i, allIsland[i + 1].compareTo(allIsland[i]) > 0);
            assertTrue("incorrect comparison result for object " + i, allIsland[i].compareTo(allIsland[i]) == 0);
        }
    }

    /**
     * Tests the <code>Serializable</code> interface.
     *
     * @throws Exception
     *             to junit
     */
    public void testSerializable() throws Exception {
        ByteArrayOutputStream output;
        ObjectOutputStream objOutput;
        ByteArrayInputStream input;
        ObjectInputStream objInput;

        // write the objects to a stream twice

        output = new ByteArrayOutputStream();
        objOutput = new ObjectOutputStream(output);
        // first write them in normal order
        for (int i = 0; i < allEnum.length; i++) {
            objOutput.writeObject(allEnum[i]);
        }

        // and then in reverse order
        for (int i = allEnum.length - 1; i >= 0; i--) {
            objOutput.writeObject(allEnum[i]);
        }
        objOutput.close();

        input = new ByteArrayInputStream(output.toByteArray());
        objInput = new ObjectInputStream(input);
        // first read object once, expect them in normal order
        for (int i = 0; i < allEnum.length; i++) {
            Object testObj = objInput.readObject();
            Class c = allEnum[i].getClass();
            assertTrue("incorrect object type for object " + i, c.isInstance(testObj));
            assertTrue("wrong compareTo result for object " + i, ((Enum) testObj).compareTo(allEnum[i]) == 0);
            assertTrue("wrong == result for object " + i, testObj == allEnum[i]);
        }
        // then read object once more, expect them in reverse order
        for (int i = allEnum.length - 1; i >= 0; i--) {
            Object testObj = objInput.readObject();
            Class c = allEnum[i].getClass();
            assertTrue("incorrect object type for object " + i, c.isInstance(testObj));
            assertTrue("wrong compareTo result for object " + i, ((Enum) testObj).compareTo(allEnum[i]) == 0);
            assertTrue("wrong == result for object " + i, testObj == allEnum[i]);
        }
        objInput.close();

    }
}
