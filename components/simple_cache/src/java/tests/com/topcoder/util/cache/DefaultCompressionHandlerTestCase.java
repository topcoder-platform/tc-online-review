/*
 * Copyright (c) 2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.cache;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;

/**
 * Provides unit test cases for DefaultCompressionHandler class.
 *
 * @author  rem
 * @version 2.0
 * @since   2.0
 */
public class DefaultCompressionHandlerTestCase extends TestCase {

    /**
     * Returns the test suite for this class.
     *
     * @return a new TestSuite representing this class.
     */
    public static Test suite() {
        return new TestSuite(DefaultCompressionHandlerTestCase.class);
    }

    /**
     * Tests no-arg constructor.
     * <ul>Check that this ctor successfully creates instance.
     * <li>Check that compress method will accept object of any type. Try to compress and decompress
     *     instance of SomeSerializableClass class.
     * </ul>
     *
     * @throws Exception if any error occurs.
     */
    public void testConstructor1() throws Exception {
        DefaultCompressionHandler dch = new DefaultCompressionHandler();
        SomeSerializableClass obj = new SomeSerializableClass(7);
        byte[] compressedObj = dch.compress(obj);
        Object decompressedObj = dch.decompress(compressedObj);
        assertTrue("decompressedObj should be instance of SomeSerializableClass and should contain the same data"
                + " as obj", (decompressedObj instanceof SomeSerializableClass)
                && ((SomeSerializableClass) decompressedObj).getData() == obj.getData());
    }

    /**
     * Tests parameters typeMatchRegex and acceptedTypes of constructor.
     * <ul>
     * <li>Try to provide regex ".*Some.*" and null acceptedTypes list. Check that method compress will
     *     accept instance of SomeSerializableClass and will not accept instance of AnotherSerializable class.
     * <li>Try to provide regex ".*Some.*" and acceptedTypes containing element "AnotherSerializableClass".
     *     Check that method compress will accept both instances.
     * <li>Try to provide empty regex and acceptedTypes containing element "AnotherSerializableClass".
     *     Check that method compress will accept instance of AnotherSerializableClass and will not accept
     *     instance of SomeSerializableClass.
     * </ul>
     *
     * @throws Exception if any error occurs.
     */
    public void testConstructorTypeMatchRegexAndAcceptedTypes() throws Exception {
        DefaultCompressionHandler dch;

        dch = new DefaultCompressionHandler(null, ".*Some.*", null, null);
        dch.compress(new SomeSerializableClass(7));
        try {
            dch.compress(new AnotherSerializableClass(7));
            fail("compress should throw TypeNotMatchedException when calling with unaccepted type.");
        } catch (TypeNotMatchedException ex) {
            // expected exception.
        }

        List list = new ArrayList();
        list.add("com.topcoder.util.cache.DefaultCompressionHandlerTestCase$AnotherSerializableClass");

        dch = new DefaultCompressionHandler(null, ".*Some.*", null, list);
        dch.compress(new SomeSerializableClass(7));
        dch.compress(new AnotherSerializableClass(7));

        dch = new DefaultCompressionHandler(null, "  ", null, list);
        try {
            dch.compress(new SomeSerializableClass(7));
            fail("compress should throw TypeNotMatchedException when calling with unaccepted type.");
        } catch (TypeNotMatchedException ex) {
            // expected exception.
        }
        dch.compress(new AnotherSerializableClass(7));
    }

    /**
     * Tests parameter converter of constructor.
     * <ul>
     * <li>Try to provide instance of ObjectByteConverterImpl as converter and make sure that compression handler
     *     will use this instance for object-byte and byte-object convertions.
     * </ul>
     *
     * @throws Exception if any error occurs.
     */
    public void testConstructorConverter() throws Exception {
        ObjectByteConverter obc = new ObjectByteConverterImpl();
        DefaultCompressionHandler dch = new DefaultCompressionHandler(null, null, obc, null);
        dch.compress(new Object());
        dch.decompress(new byte[0]);
    }

    /**
     * Tests second constructor with invalid arguments.
     * <ul>
     * <li>Make sure IllegalArgumentException is thrown if typeMatchRegex contains invalid regular expression.
     * <li>Make sure IllegalArgumentException is thrown if acceptedTypes contains not-String value.
     * </ul>
     *
     * @throws Exception if any error occurs.
     */
    public void testConstructorFail() throws Exception {
        try {
            new DefaultCompressionHandler(null, "[", null, null);
            fail("IllegalArgumentException should be thrown when regular expression is invalid.");
        } catch (IllegalArgumentException ex) {
            // expected exception.
        }

        List list = new ArrayList();
        list.add(new Object());
        try {
            new DefaultCompressionHandler(null, null, null, list);
            fail("IllegalArgumentException should be thrown when acceptedTypes contains not-String value.");
        } catch (IllegalArgumentException ex) {
            // expected exception.
        }
    }

    /**
     * Tests that compression and decompression process.
     *
     * <ul>
     * <li>Try to compress and decompress HashSet containing Integer and String values.
     *     Make sure that after decompression set contains same values.
     * </ul>
     *
     * @throws Exception if any error occurs.
     */
    public void testCompressDecompress() throws Exception {
        // initialize set.
        HashSet set = new HashSet();
        for (int i = 0; i < 1000; ++i) {
            set.add(new Integer(i));
        }
        for (char c1 = 'A'; c1 <= 'Z'; ++c1) {
            for (char c2 = 'A'; c2 <= 'Z'; ++c2) {
                String st = new String();
                st += c1;
                st += c2;
                set.add(st);
            }
        }

        // compress and decompress it.
        DefaultCompressionHandler dch = new DefaultCompressionHandler();
        byte[] compressedSet = dch.compress(set);
        Object result = dch.decompress(compressedSet);

        // check that sets are equal. Their sizes should be equal and each element of first set should
        // exist in second set.
        assertTrue("After decompression object should have same type as before.", result instanceof HashSet);
        assertEquals("Set should contain same values as before compression.", ((HashSet) result).size(), set.size());
        for (int i = 0; i < 1000; ++i) {
            assertTrue("Set should contain same values as before compression.",
                    ((HashSet) result).contains(new Integer(i)));
        }
        for (char c1 = 'A'; c1 <= 'Z'; ++c1) {
            for (char c2 = 'A'; c2 <= 'Z'; ++c2) {
                String st = new String();
                st += c1;
                st += c2;
                assertTrue("Set should contain same values as before compression.",
                        ((HashSet) result).contains(st));
            }
        }
    }

    /**
     * Tests method compress with wrong arguments.
     * <ul>
     * <li>Try to provide non-serializable object and make sure CompressionException is thrown.
     * <li>Try to provide object of unacceptable type and make sure TypeNotMatchedException is thrown.
     * </ul>
     *
     * @throws Exception if any error occurs.
     */
    public void testCompressFail() throws Exception {
        DefaultCompressionHandler dch = new DefaultCompressionHandler();
        try {
            dch.compress(new Object());
            fail("CompressionException should be thrown.");
        } catch (CompressionException ex) {
            // expected exception.
        }

        dch = new DefaultCompressionHandler(null, "abc", null, null);
        try {
            dch.compress(new Object());
            fail("TypeNotMatchedException should be thrown.");
        } catch (TypeNotMatchedException ex) {
            // expected exception.
        }
    }

    /**
     * Tests method decompress with wrong arguments.
     * <ul>
     * <li>Make sure CompressionException is thrown when deserialization fails.
     * </ul>
     *
     * @throws Exception if any error occurs.
     */
    public void testDecompressFail() throws Exception {
        DefaultCompressionHandler dch = new DefaultCompressionHandler();
        try {
            // deserialization should fail with this argument.
            dch.decompress(new byte[] {1, 2, 3, 4, 5});
            fail("CompressionException should be thrown.");
        } catch (CompressionException ex) {
            // expected exception.
        }
    }

    /**
     * Tests methods compress and decompress with null and empty parameters.
     * <ul>
     * <li>Make sure compress returns empty array for null parameter.
     * <li>Make sure decompress returns null for null parameter.
     * <li>Make sure decompress returns null for empty array parameter.
     * </ul>
     *
     * @throws Exception if any error occurs.
     */
    public void testNull() throws Exception {
        DefaultCompressionHandler dch = new DefaultCompressionHandler();
        assertTrue("compress should return empty array for null parameter", dch.compress(null).length == 0);
        assertNull("decompress should return null for null parameter", dch.decompress(null));
        assertNull("decompress should return null for empty array parameter", dch.decompress(new byte[0]));
    }

    /**
     * Utility class for testing.
     */
    private static class SomeSerializableClass implements Serializable {

        /**
         * Some data.
         */
        private int data;

        /**
         * Constructor.
         *
         * @param data some data.
         */
        public SomeSerializableClass(int data) {
            this.data = data;
        }

        /**
         * Returns data stored in object.
         *
         * @return some data.
         */
        public int getData() {
            return data;
        }
    }

    /**
     * Another utility class for testing.
     */
    private static class AnotherSerializableClass implements Serializable {
        /**
         * Some data.
         */
        private int data;

        /**
         * Constructor.
         *
         * @param data some data.
         */
        public AnotherSerializableClass(int data) {
            this.data = data;
        }

        /**
         * Returns data stored in object.
         *
         * @return some data.
         */
        public int getData() {
            return data;
        }
    }

    /**
     * Implementation of ObjectByteConverter interface used for testing DefaultCompressionHandler.
     */
    private class ObjectByteConverterImpl implements ObjectByteConverter {

        /**
         * Contains true iif method getBytes was called at least once.
         */
        private boolean getBytesCalled;

        /**
         * Contains true iif method getObject was called at least once.
         */
        private boolean getObjectCalled;

        /**
         * Returns byte representation of object.
         *
         * @param  value some data.
         * @return byte representation of value.
         */
        public byte[] getBytes(Object value) {
            getBytesCalled = true;
            return new byte[123];
        }

        /**
         * Returns original object.
         *
         * @param  compressedValue byte representation of object.
         * @return original object.
         */
        public Object getObject(byte[] compressedValue) {
            getObjectCalled = true;
            return new Object();
        }

        /**
         * Checks that getBytes and getObject were called at least once.
         *
         * @return true if and only if both getBytes and getObject methods were called at least once.
         */
        public boolean methodsWereCalled() {
            return getBytesCalled && getObjectCalled;
        }
    }
}

