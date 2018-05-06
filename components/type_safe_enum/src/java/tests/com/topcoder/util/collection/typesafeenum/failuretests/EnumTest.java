/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.collection.typesafeenum.failuretests;

import com.topcoder.util.collection.typesafeenum.Enum;

import junit.framework.TestCase;

/**
 * Failure test for <code>Enum</code>.
 * 
 * @author enefem21
 * @version 1.1
 */
public class EnumTest extends TestCase {

	/**
	 * Test constructor for failure. Condition: declaringClass is null. Expect:
	 * <code>IllegalArgumentException</code>.
	 */
	public void testEnumClassNull() {
		try {
			new EnumPublic(null);
			fail("Should throw IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			// expected
		}
	}

	/**
	 * Test constructor for failure. Condition: declaringClass is not
	 * subclassing Enum. Expect: <code>IllegalArgumentException</code>.
	 */
	public void testEnumClassNotSubclass() {
		try {
			new EnumPublic(String.class);
			fail("Should throw IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			// expected
		}
	}

	/**
	 * Enum constructor with public constructor.
	 */
	class EnumPublic extends Enum {

		/** The constructor. */
		public EnumPublic(Class declaringClass) {
			super(declaringClass);
		}
	}

	/**
	 * Test <code>getEnumByOrdinal</code> for failure. Condition: enumClass is
	 * null. Expect: <code>IllegalArgumentException</code>.
	 */
	public void testGetEnumByOrdinalNull() {
		try {
			Enum.getEnumByOrdinal(1, null);
			fail("Should throw IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			// expected
		}
	}

	/**
	 * Test <code>getEnumByOrdinal</code> for failure. Condition: enumClass is
	 * not an enum. Expect: <code>IllegalArgumentException</code>.
	 */
	public void testGetEnumByOrdinalNotEnum() {
		try {
			Enum.getEnumByOrdinal(1, String.class);
			fail("Should throw IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			// expected
		}
	}

	/**
	 * Test <code>getEnumByStringValue</code> for failure. Condition: string
	 * value is null. Expect: <code>IllegalArgumentException</code>.
	 */
	public void testGetEnumByStringValueNullStringValue() {
		try {
			Enum.getEnumByStringValue(null, EnumPublic.class);
			fail("Should throw IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			// expected
		}
	}

	/**
	 * Test <code>getEnumByStringValue</code> for failure. Condition: enum
	 * class is null. Expect: <code>IllegalArgumentException</code>.
	 */
	public void testGetEnumByStringValueNullEnumClass() {
		try {
			Enum.getEnumByStringValue("ENUM", null);
			fail("Should throw IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			// expected
		}
	}

	/**
	 * Test <code>getEnumByStringValue</code> for failure. Condition: enum
	 * class is not subclassing Enum. Expect:
	 * <code>IllegalArgumentException</code>.
	 */
	public void testGetEnumByStringValueNotEnum() {
		try {
			Enum.getEnumByStringValue("ENUM", String.class);
			fail("Should throw IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			// expected
		}
	}

	/**
	 * Test <code>getEnumByStringValue</code> for failure. Condition: name is
	 * null. Expect: <code>IllegalArgumentException</code>.
	 * 
	 * @throws Exception
	 *             to JUnit
	 */
	public void testGetEnumByNameNullName() throws Exception {
		try {
			Enum.getEnumByName(null, EnumPublic.class);
			fail("Should throw IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			// expected
		}
	}

	/**
	 * Test <code>getEnumByStringValue</code> for failure. Condition: enum
	 * class is null. Expect: <code>IllegalArgumentException</code>.
	 * 
	 * @throws Exception
	 *             to JUnit
	 */
	public void testGetEnumByNameNullEnumClass() throws Exception {
		try {
			Enum.getEnumByName("ENUM", null);
			fail("Should throw IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			// expected
		}
	}

	/**
	 * Test <code>getEnumByStringValue</code> for failure. Condition: enum
	 * class is not an Enum. Expect: <code>IllegalArgumentException</code>.
	 * 
	 * @throws Exception
	 *             to JUnit
	 */
	public void testGetEnumByNameNotEnum() throws Exception {
		try {
			Enum.getEnumByName("ENUM", String.class);
			fail("Should throw IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			// expected
		}
	}

	/**
	 * Test <code>getEnumList</code> for failure. Condition: enum class is
	 * null. Expect: <code>IllegalArgumentException</code>.
	 * 
	 * @throws Exception
	 *             to JUnit
	 */
	public void testGetEnumListNull() {
		try {
			Enum.getEnumList(null);
			fail("Should throw IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			// expected
		}
	}

	/**
	 * Test <code>getEnumList</code> for failure. Condition: enum class is not
	 * an Enum. Expect: <code>IllegalArgumentException</code>.
	 * 
	 * @throws Exception
	 *             to JUnit
	 */
	public void testGetEnumListNotEnum() {
		try {
			Enum.getEnumList(String.class);
			fail("Should throw IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			// expected
		}
	}

}
