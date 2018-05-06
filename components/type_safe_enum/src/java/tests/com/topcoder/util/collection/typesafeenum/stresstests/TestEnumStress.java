/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.collection.typesafeenum.stresstests;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.topcoder.util.collection.typesafeenum.Enum;

import junit.framework.TestCase;

/**
 * Test Enum class with multiple threads.
 *
 * @author Chenhong
 * @version 1.1
 */
public class TestEnumStress extends TestCase {

    /**
     * Test case with multiple threads.
     *
     * @throws Exception
     *             to junit.
     */
    public void testThreadSafe_1() throws Exception {
        Thread[] threads = new Thread[10000];

        for (int i = 0; i < threads.length; i++) {
            if (i % 3 == 0) {
                threads[i] = new MyThread(MyEnum.ENUM_Integer);
            } else if (i % 3 == 1) {
                threads[i] = new MyThread(MyEnum.ENUM_String);
            } else {
                threads[i] = new MyThread(MySuit.SPADES);
            }
        }
        long start = System.currentTimeMillis();
        for (int i = 0; i < threads.length; i++) {
            threads[i].run();
            threads[i].join();
        }

        long end = System.currentTimeMillis();

        System.out.println("Calling 10000 threads cost " + (end - start) / 1000.0 + " seconds.");
    }

    /**
     * Test the constructor with multiple threads.
     *
     * @throws Exception
     *             to junit.
     */
    public void testThreadSafe_2() throws Exception {
        Thread[] threads = new Thread[1000];

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new MockThread();
        }

        long start = System.currentTimeMillis();
        for (int i = 0; i < threads.length; i++) {
            threads[i].run();
            threads[i].join();
        }

        long end = System.currentTimeMillis();

        System.out.println("Calling 100 threads cost " + (end - start) / 1000.0 + " seconds.");

        Set set = new HashSet();
        for (int i = 0; i < 1000; ++i) {

            Enum[] enums = ((MockThread) threads[i]).getEnums();

            for (int j = 0; j < enums.length; j++) {
                set.add(new Integer(enums[j].getOrdinal()));
            }
        }

        assertTrue("True is expected.", 200000 == set.size());
    }

    /**
     * A mocked class for testing.
     */
    private class MockThread extends Thread {
        /**
         * Represents the num of Enum instances which will be created.
         */
        private int num = 200;

        /**
         * Represents the Enum instances to be created.
         */
        private Enum[] enums = new Enum[num];

        /**
         * Running
         */
        public void run() {
            for (int i = 0; i < num; ++i) {

                if (i % 2 == 0) {
                    enums[i] = new SimpleEnum();
                } else {
                    enums[i] = new SimpleEnum(SimpleEnum.class);
                }
            }
        }

        /**
         * Get the number of enum instances.
         *
         * @return the number of enums instances.
         */
        public int getNumber() {
            return num;
        }

        /**
         * Get the enum instances.
         *
         * @return Enums.
         */
        public Enum[] getEnums() {
            return enums;
        }

    }

    /**
     * A mocked enum for testing.
     */
    public class SimpleEnum extends Enum {

        /**
         * The default constructor.
         *
         */
        public SimpleEnum() {
            super();
        }

        /**
         * Create a custom Enum with Class as parameter.
         *
         * @param klass
         *            the Class
         */
        public SimpleEnum(Class klass) {
            super(klass);
        }
    }

    /**
     * A custom thread for testing.
     */
    private class MyThread extends Thread {

        /**
         * Represents the Enum instance.
         */
        private Enum enumInstance = null;

        /**
         * Represents the Enum name.
         */
        private String name = null;

        /**
         * Constructor with an Enum instance as parameter
         *
         * @param enum
         *            Enum instance.
         *
         * @throws Exception
         *             to junit.
         */
        public MyThread(Enum enumInstance) throws Exception {
            this.enumInstance = enumInstance;
            this.name = enumInstance.getEnumName();
        }

        /**
         * Running.
         */
        public void run() {
            Class c = enumInstance.getDeclaringClass();

            try {
                Enum e = enumInstance.getEnumByName(this.name, c);
                assertTrue("The Enum should be equal.", e.compareTo(enumInstance) == 0);
            } catch (IllegalAccessException e) {
                fail("NO exception should be raised.");
            }

            List list = enumInstance.getEnumList(c);

            Enum newEnum = enumInstance.getEnumByOrdinal(enumInstance.getOrdinal(), c);
        }
    }
}