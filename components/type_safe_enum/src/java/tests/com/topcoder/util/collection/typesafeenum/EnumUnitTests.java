/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.collection.typesafeenum;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.List;

import junit.framework.TestCase;

/**
 * Unit test for the <code>Enum</code> class.
 *
 * @author TCSDEVELOPER
 * @version 1.1
 */
public class EnumUnitTests extends TestCase {

    /**
     * Test for constructor accessibility. All of them must be protected.
     */
    public void testConstructorAccesibility() {
        Constructor[] allConstructors = Enum.class.getConstructors();
        for (int i = 0; i < allConstructors.length; i++) {
            assertTrue("All constructors in Enum must be protected",
                Modifier.isProtected(allConstructors[i].getModifiers()));
        }
    }

    /**
     * Accuracy test for the default constructor <code>Enum()</code>. Instance should be created successfully.
     */
    public void testDefaultConstructor() {
        MockEnum enum1 = new MockEnum();
        MockEnum enum2 = new MockEnum();
        assertNotNull("Instance should be created successfully.", enum1);
        assertTrue("The ordinal should be set correctly.", enum1.getOrdinal() == 0);
        assertTrue("The declaringClass should be set correctly.",
            enum1.getDeclaringClass() == MockEnum.class);
        assertNotNull("Instance should be created successfully.", enum2);
        assertTrue("The ordinal should be set correctly.", enum2.getOrdinal() == 1);
        assertTrue("The declaringClass should be set correctly.",
            enum2.getDeclaringClass() == MockEnum.class);
    }

    /**
     * Failure test for the constructor <code>Enum(String)</code> with null declaringClass. IllegalArgumentException
     * should be thrown.
     */
    public void testConstructorNullDeclaringClass() {
        try {
            new MockEnum(null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Failure test for the constructor <code>Enum(String)</code> with invalid declaringClass which is not a sub-class
     * of Enum. IllegalArgumentException should be thrown.
     */
    public void testConstructorInvalidDeclaringClass() {
        try {
            new MockEnum(Object.class);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Accuracy test for the constructor <code>Enum(String)</code>. Instance should be created successfully.
     */
    public void testConstructorWithDeclaringClass() {
        MockEnum enum1 = new MockEnum(AnotherEnum.class);
        MockEnum enum2 = new MockEnum(AnotherEnum.class);
        assertNotNull("Instance should be created successfully.", enum1);
        assertTrue("The ordinal should be set correctly.", enum1.getOrdinal() == 0);
        assertTrue("The declaringClass should be set correctly.",
            enum1.getDeclaringClass() == AnotherEnum.class);
        assertNotNull("Instance should be created successfully.", enum2);
        assertTrue("The ordinal should be set correctly.", enum2.getOrdinal() == 1);
        assertTrue("The declaringClass should be set correctly.",
            enum2.getDeclaringClass() == AnotherEnum.class);
    }

    /**
     * Accuracy test for the method <code>getOrdinal()</code>. Ordinal must be non negative.
     */
    public void testGetOrdinalNonNegative() {
        assertTrue("Ordinal must be non negative for MyBool.TRUE", MyBool.TRUE.getOrdinal() >= 0);
        assertTrue("Ordinal must be non negative for MyBool.FALSE", MyBool.FALSE.getOrdinal() >= 0);
    }

    /**
     * Accuracy test for the method <code>getOrdinal()</code>.  Ordinal must start from zero.
     */
    public void testGetOrdinalStartFromZero() {
        assertTrue("Ordinal must start from zero", (MyBool.TRUE.getOrdinal() == 0) || (MyBool.FALSE.getOrdinal() == 0));
    }

    /**
     * Accuracy test for the method <code>getOrdinal()</code>. Ordinal value must be unique.
     */
    public void testGetOrdinalUnique() {
        assertTrue("Ordinal value must be unique for MyBool", MyBool.TRUE.getOrdinal() != MyBool.FALSE.getOrdinal());
    }

    /**
     * Accuracy test for the method <code>getDeclaringClass()</code> when the enum is created by default constructor.
     * Default declaring class (this.getClass()) should be returned.
     */
    public void testGetDeclaringClassWhenDefault() {
        MockEnum enumObj = new MockEnum();
        assertTrue("Declaring class should be MockEnum", enumObj.getDeclaringClass() == MockEnum.class);
    }

    /**
     * Accuracy test for the method <code>getDeclaringClass()</code> when the enum is created by the constructor with
     * specified declaring class. That declaring class (specified in constructor) should be returned.
     */
    public void testGetDeclaringClassWhenSpecified() {
        MockEnum enumObj = new MockEnum(AnotherEnum.class);
        assertTrue("Declaring class should be MockEnum", enumObj.getDeclaringClass() == AnotherEnum.class);
    }

    /**
     * Accuracy test for the method <code>compareTo()</code>. For the same object must return 0.
     */
    public void testCompareToEqual() {
        assertTrue("Must return zero for the same Enum", MyBool.TRUE.compareTo(MyBool.TRUE) == 0);
    }

    /**
     * Accuracy test for the method <code>compareTo()</code>. For the different objects must not return 0.
     */
    public void testCompareToNotEqual() {
        assertTrue("Must return nonzero for different Enums", MyBool.TRUE.compareTo(MyBool.FALSE) != 0);
    }

    /**
     * Accuracy test for the method <code>compareTo()</code>. Checks the positive, negative values returned by
     * <code>compareTo()</code> based on ordinal values. It's not guaranteed that order of static members
     * initialisation will be the same for all VM's so we have to check it with <code>getOrdinal()</code> values.
     */
    public void testCompareToOrder() {
        if (MyBool.TRUE.getOrdinal() > MyBool.FALSE.getOrdinal()) {
            assertTrue("Must return positive", MyBool.TRUE.compareTo(MyBool.FALSE) > 0);
            assertTrue("Must return negative", MyBool.FALSE.compareTo(MyBool.TRUE) < 0);
        } else {
            assertTrue("Must return negative", MyBool.TRUE.compareTo(MyBool.FALSE) < 0);
            assertTrue("Must return positive", MyBool.FALSE.compareTo(MyBool.TRUE) > 0);
        }
    }

    /**
     * Failure test for the method <code>compareTo()</code> with null. ClassCastException should be thrown.
     */
    public void testCompareToNull() {
        try {
            MyBool.TRUE.compareTo(null);
            fail("ClassCastException should be thrown");
        } catch (ClassCastException e) {
            // success
        }
    }

    /**
     * Failure test for the method <code>compareTo()</code> with non-Enum classes. ClassCastException should be
     * thrown.
     */
    public void testCompareToNonEnumClasses() {
        try {
            MyBool.TRUE.compareTo("I am not an Enum");
            fail("ClassCastException should be thrown");
        } catch (ClassCastException e) {
            // success
        }
    }

    /**
     * Failure test for the method <code>compareTo()</code> with different Enum classes. ClassCastException should be
     * thrown.
     */
    public void testCompareToDifferentEnumClasses() {
        try {
            MyBool.TRUE.compareTo(Suit.DIAMONDS);
            fail("ClassCastException should be thrown");
        } catch (ClassCastException e) {
            // success
        }
    }

    /**
     * Failure test for the method <code>compareTo()</code> with subclass/super Enum classes. ClassCastException
     * should be thrown.
     */
    public void testCompareToDifferentEnumClassesSubclass() {
        try {
            MyTriBool.UNKNOWN.compareTo(MyBool.FALSE);
            fail("ClassCastException should be thrown");
        } catch (ClassCastException cce) {
            // success
        }
        try {
            MyBool.FALSE.compareTo(MyTriBool.UNKNOWN);
            fail("ClassCastException should be thrown");
        } catch (ClassCastException cce) {
            // success
        }
    }

    /**
     * Accuracy test for <code>getEnumByOrdinal()</code>. The enum instance with the given ordinal should be returned.
     */
    public void testGetEnumByOrdinalReverse() {
        Enum testTrue = Enum.getEnumByOrdinal(MyBool.TRUE.getOrdinal(), MyBool.class);
        Enum testTalse = Enum.getEnumByOrdinal(MyBool.FALSE.getOrdinal(), MyBool.class);
        assertTrue("Reverse test failed for MyBool.TRUE", testTrue == MyBool.TRUE);
        assertTrue("Reverse test failed for MyBool.FALSE", testTalse == MyBool.FALSE);
    }

    /**
     * Accuracy test for <code>getEnumByOrdinal()</code>. Null should be returned if the ordinal is too large.
     */
    public void testGetEnumByOrdinalNoSuchTooLarge() {
        Enum testNull = Enum.getEnumByOrdinal(2, MyBool.class);
        assertNull("Must return null for non-existing element", testNull);
    }

    /**
     * Accuracy test for <code>getEnumByOrdinal()</code>. Null should be returned if the ordinal is negative.
     */
    public void testGetEnumByOrdinalNoSuchNegative() {
        Enum testNull = Enum.getEnumByOrdinal(-1, MyBool.class);
        assertNull("Must return null even for negative values of ordinal", testNull);
    }

    /**
     * Accuracy test for <code>getEnumByOrdinal()</code>. In case if class is a subclass of Enum but does not define
     * any values yet, it must not crash our application and return null.
     */
    public void testGetEnumByOrdinalEmptyEnum() {
        Enum testNull = Enum.getEnumByOrdinal(0, EmptyEnum.class);
        assertNull("Must return null for EmptyEnum", testNull);
    }

    /**
     * Failure test for <code>getEnumByOrdinal()</code> with null enumClass. IllegalArgumentException should be thrown.
     */
    public void testGetEnumByOrdinalNullEnumClass() {
        try {
            Enum.getEnumByOrdinal(0, null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Failure test for <code>getEnumByOrdinal()</code> with invalid enumClass which is not a subclass of Enum.
     * IllegalArgumentException should be thrown.
     */
    public void testGetEnumByOrdinalNotEnumClass() {
        try {
            Enum.getEnumByOrdinal(0, Object.class);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Accuracy test for <code>getEnumList()</code>. This test case will check whether valid elements are returned.
     */
    public void testGetEnumListValid() {
        final List testMybool = Enum.getEnumList(MyBool.class);
        assertTrue("Must return exactly 2 items in List for MyBool", testMybool.size() == 2);
        assertTrue("Must be only MyBool values in List", testMybool.get(0) instanceof MyBool);
        assertTrue("Must be only MyBool values in List", testMybool.get(1) instanceof MyBool);
    }

    /**
     * Accuracy test for <code>getEnumList()</code>. This test case will check whether the returned elements are sorted.
     */
    public void testGetEnumListSorted() {
        final List testMybool = Enum.getEnumList(MyBool.class);
        assertTrue("Must return at least 2 items in List for MyBool", testMybool.size() >= 2);
        Enum item0 = (Enum) testMybool.get(0);
        Enum item1 = (Enum) testMybool.get(1);
        assertTrue("Items must be sorted according to CompareTo()", item0.compareTo(item1) < 0);
    }

    /**
     * Accuracy test for <code>getEnumList()</code>. In case if class is a subclass of Enum but have not define any
     * instances, this must not crash and return empty List.
     */
    public void testGetEnumListEmptyEnum() {
        final List testEmpty = Enum.getEnumList(EmptyEnum.class);
        assertNotNull("Must return not-null empty List for EmptyEnum", testEmpty);
        assertTrue("Must return empty List for EmptyEnum", testEmpty.size() == 0);
    }

    /**
     * Accuracy test for <code>getEnumList()</code>. This test case will check whether the returned list is
     * unmodifiable.
     */
    public void testGetEnumListUnmodifiable() {
        List testMybool = Enum.getEnumList(MyBool.class);
        try {
            testMybool.add(MyBool.FALSE);
            fail("List returned for MyBool is allowed to modify");
        } catch (UnsupportedOperationException uoe) {
            // success
        }

        List testEmpty = Enum.getEnumList(EmptyEnum.class);
        try {
            testEmpty.add(new Object());
            fail("List returned for EmptyEnum is allowed to modify");
        } catch (UnsupportedOperationException uoe) {
            // success
        }
    }

    /**
     * Failure test for <code>getEnumList()</code> with null enumClass. IllegalArgumentException should be thrown.
     */
    public void testGetEnumListNullEnumClass() {
        try {
            Enum.getEnumList(null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Failure test for <code>getEnumList()</code> with invalid enumClass which is not a subclass of Enum.
     * IllegalArgumentException should be thrown.
     */
    public void testGetEnumListNotEnumClass() {
        try {
            Enum.getEnumList(Object.class);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Accuracy test for <code>getEnumByStringValue()</code>. The enum instance with the given string value should be
     * returned.
     */
    public void testGetEnumByStringValueValid() {
        Enum stringDiamonds = Enum.getEnumByStringValue(Suit.DIAMONDS.toString(), Suit.class);
        Enum stringClubs = Enum.getEnumByStringValue("Clubs", Suit.class);
        assertTrue("Reverse lookup by string failed for Suit.DIAMONDS", stringDiamonds == Suit.DIAMONDS);
        assertTrue("'Clubs' lookup failed for Suit.CLUBS", stringClubs == Suit.CLUBS);
    }

    /**
     * Accuracy test for <code>getEnumByStringValue()</code>. This test case will check whether it return null for
     * elements not found.
     */
    public void testGetEnumByStringValueNoSuch() {
        Enum notFound = Enum.getEnumByStringValue("Something nobody can imagine", Suit.class);
        assertNull("Must return null if not found for Suit", notFound);
    }

    /**
     * Accuracy test for <code>getEnumByStringValue()</code>. This test case will check whether it correctly separate
     * string values from different classes.
     */
    public void testGetEnumByStringValueNotFromThisClass() {
        Enum notFound = Enum.getEnumByStringValue(Suit.DIAMONDS.toString(), MyBool.class);
        assertNull("Must return null if valid toString() value is not from this class", notFound);
    }

    /**
     * Accuracy test for <code>getEnumByStringValue()</code>. This test case will check it dose not result in errors if
     * subclass of Enum has not defined any values yet.
     */
    public void testGetEnumByStringValueEmptyEnum() {
        Enum notFound = Enum.getEnumByStringValue("Clubs", EmptyEnum.class);
        assertNull("Must always return null without errors for EmptyEnum class", notFound);
    }

    /**
     * Failure test for <code>getEnumByStringValue()</code> with null stringValue. IllegalArgumentException should be
     * thrown.
     */
    public void testGetEnumByStringValueNullStringValue() {
        try {
            Enum.getEnumByStringValue(null, Suit.class);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Failure test for <code>getEnumByStringValue()</code> with null enumClass. IllegalArgumentException should be
     * thrown.
     */
    public void testGetEnumByStringValueNullEnumClass() {
        try {
            Enum.getEnumByStringValue("Clubs", null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Failure test for <code>getEnumByStringValue()</code> with invalid enumClass which is not a subclass of Enum.
     * IllegalArgumentException should be thrown.
     */
    public void testGetEnumByStringValueNotEnumClass() {
        try {
            Enum.getEnumByStringValue("Clubs", Object.class);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Check if our readResolve() method will work correctly with serialization process as designed. Write object to
     * some OutputStream (in out case this is ByteArrayOutputStream) and read it back. This must result in the same
     * objects, not simply the same values.
     *
     * @throws Exception
     *             does not expect to throw exception if everything is ok
     */
    public void testReadResolve() throws Exception {
        // Write serialized classed to some stream, memory stream here
        ByteArrayOutputStream outBytes = new ByteArrayOutputStream();
        ObjectOutputStream objectOut = new ObjectOutputStream(outBytes);
        objectOut.writeObject(MyBool.TRUE);
        objectOut.writeObject(MyBool.FALSE);
        objectOut.writeObject(Suit.DIAMONDS);
        objectOut.flush();
        objectOut.close();

        // Try to read them back
        // By default ObjectInputStream doing readResolve() if SecurityManager allow
        ByteArrayInputStream inBytes = new ByteArrayInputStream(outBytes.toByteArray());
        ObjectInputStream objectIn = new ObjectInputStream(inBytes);
        MyBool readTrue = (MyBool) objectIn.readObject();
        MyBool readFalse = (MyBool) objectIn.readObject();
        Suit readDiamonds = (Suit) objectIn.readObject();
        objectIn.close();
        assertTrue("MyBool.TRUE failed to readResolve()", readTrue == MyBool.TRUE);
        assertTrue("MyBool.FALSE failed to readResolve()", readFalse == MyBool.FALSE);
        assertTrue("Suit.DIAMONDS failed to readResolve()", readDiamonds == Suit.DIAMONDS);
    }

    /**
     * Accuracy test for the <code>getEnumName()</code> with non-public enum field. Null should be returned.
     *
     * @throws Exception
     *             does not expect to throw exception if everything is ok
     */
    public void testGetEnumNameWithNonPublicEnum() throws Exception {
        assertNull("Null should be returned.", DummyEnum.NOTPUBLIC.getEnumName());
    }

    /**
     * Accuracy test for the <code>getEnumName()</code> with non-static enum field. Null should be returned.
     *
     * @throws Exception
     *             does not expect to throw exception if everything is ok
     */
    public void testGetEnumNameWithNonStaticEnum() throws Exception {
        assertNull("Null should be returned.", new DummyEnum().getEnumName());
    }

    /**
     * Accuracy test for the <code>getEnumName()</code> with public static field. Correct enum name should be
     * returned.
     *
     * @throws Exception
     *             does not expect to throw exception if everything is ok
     */
    public void testGetEnumNameWithPublicStaticEnum() throws Exception {
        assertTrue("'PUBLICSTATIC' should be returned.", DummyEnum.PUBLICSTATIC.getEnumName().equals("PUBLICSTATIC"));
    }

    /**
     * Accuracy test for <code>getEnumByName()</code>. The enum instance with the given enum name should be returned.
     *
     * @throws Exception
     *             does not expect to throw exception if everything is ok
     */
    public void testGetEnumByNameValid() throws Exception {
        Enum nameClubs = Enum.getEnumByName("CLUBS", Suit.class);
        assertTrue("'CLUBS' lookup failed for Suit.CLUBS", nameClubs == Suit.CLUBS);
        Enum nameDiamonds = Enum.getEnumByName("DIAMONDS", Suit.class);
        assertTrue("'DIAMONDS' lookup failed for Suit.DIAMONDS", nameDiamonds == Suit.DIAMONDS);
    }

    /**
     * Accuracy test for <code>getEnumByName()</code>. This test case will check whether it return null for elements
     * not found.
     *
     * @throws Exception
     *             does not expect to throw exception if everything is ok
     */
    public void testGetEnumByNameNoSuch() throws Exception {
        Enum notFound = Enum.getEnumByName("Something nobody can imagine", Suit.class);
        assertNull("Must return null if not found for Suit", notFound);
    }

    /**
     * Failure test for <code>getEnumByName()</code> with empty name. IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             does not expect to throw exception if everything is ok
     */
    public void testGetEnumByNameWithEmptyName() throws Exception {
        try {
            Enum.getEnumByName(" ", Suit.class);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Accuracy test for <code>getEnumByName()</code>. This test case will check whether it correctly separate enum
     * names from different classes.
     *
     * @throws Exception
     *             does not expect to throw exception if everything is ok
     */
    public void testGetEnumByNameNotFromThisClass() throws Exception {
        Enum notFound = Enum.getEnumByName("DIAMONDS", MyBool.class);
        assertNull("Must return null if enum name is not from this class", notFound);
    }

    /**
     * Accuracy test for <code>getEnumByName()</code>. This test case will check it dose not result in errors if
     * subclass of Enum has not defined any values yet.
     *
     * @throws Exception
     *             does not expect to throw exception if everything is ok
     */
    public void testGetEnumByNameEmptyEnum() throws Exception {
        Enum notFound = Enum.getEnumByName("CLUBS", EmptyEnum.class);
        assertNull("Must always return null without errors for EmptyEnum class", notFound);
    }

    /**
     * Failure test for <code>getEnumByName()</code> with null name. IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             does not expect to throw exception if everything is ok
     */
    public void testGetEnumByNameNullStringValue() throws Exception {
        try {
            Enum.getEnumByName(null, Suit.class);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Failure test for <code>getEnumByName()</code> with null enumClass. IllegalArgumentException should be
     * thrown.
     *
     * @throws Exception
     *             does not expect to throw exception if everything is ok
     */
    public void testGetEnumByNameNullEnumClass() throws Exception {
        try {
            Enum.getEnumByName("CLUBS", null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Failure test for <code>getEnumByName()</code> with invalid enumClass which is not a subclass of Enum.
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             does not expect to throw exception if everything is ok
     */
    public void testGetEnumByNameNotEnumClass() throws Exception {
        try {
            Enum.getEnumByName("CLUBS", Object.class);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }
}
