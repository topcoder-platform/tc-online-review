/**
 *
 * Copyright � 2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.collection.typesafeenum.stresstests;

import junit.framework.TestCase;

import java.io.PipedInputStream;
import java.io.ObjectInputStream;
import java.io.PipedOutputStream;
import java.io.ObjectOutputStream;

/**
 * <p>This tests the Enum class.
 * Tests will attempt to ensure the requirement that the Enum class should
 * run at speeds comparable to the Integer class for the following operations:
 *   - ordinal value to Enum instance
 *   - Enum instance to ordinal value
 *   - Comparisions (equality and ordinal value ordering)
 *   - Serialization (both reading and writing)
 * This covers all required operations of the Enum class with the exception of
 * the getEnumByString() and getEnumList() methods, and object instantiation.
 *
 * getEnumByString() should be no worse than linear complexity on the number of
 * enumerated values in a class, and getEnumList() should be no worse than
 * linear complexity on the total number of Enum subclasses.
 * Object instantiation should be no worse than the number of Enum subclasses
 * multiplied by the maximum number of enumerated values in any class.
 * To properly stress-test any of these situations, a project with a significant
 * number of Enum subclasses, each having a significant number of values, would
 * be required (on the order of likely (100,100) for instantiation, and on the
 * order of 10,000 of the relevant item in each of the other two).  Needless to
 * say, generating and compiling that many classes would be a time-consuming
 * process at best.  It is probably unreasonably to assume that even the largest
 * project would use more than a few hundred small Enums and a handful of large
 * ones anyway.  We'll go with the component specification and
 * </p>
 * <p>
 * To test the Integer-comparable timings, we'll perform the equivalent
 * operations several times for both Integers and Enums, and assert that the
 * Enum/Integer ratio does not exceed some threshold (3).  That is, ensure that
 * Enums are not significantly slower than Integers.
 * </p>
 * @author TopCoder
 * @version 1.0
 */
public class EnumStressTests extends TestCase {

    /**
     * <p>Number of timing runs used to setup the JIT optimizer.</p>
     */
    static final int OPTIMIZING_RUNS = 2;

    /**
     * <p>Number of timing runs to average out.</p>
     */
    static final int AVERAGING_RUNS = 5;

    /**
     * <p>Length of each timing run.  Note that on slow systems, a high value
     * here could take a long time!</p>
     */
    static final int TIMING_LENGTH = 1000000;

    /**
     * <p>Maximum complexity multiple.</p>
     * NOTE -- changed from 3 - 8 for slower computers
     */
    static final double RATIO_MULTIPLE = 100.0;

    /**
     * <p>I don't know how well Java optimizes big loops that accomplish
     * nothing, but lets give Java a variable that it can't assume will never be
     * used at some point (and thus must compute).</p>
     */
    int k = 0;

    /**
     * <p>Ensure MySuit and IntSuit classes are pre-loaded before running the
     * test.</p>
     */
    public void setUp() {
        k = MySuit.CLUBS.getOrdinal();
        k += IntSuit.CLUBS.getOrdinal();

    }

    /**
     * <p>Tests speed of Enum.getOrdinal().  Compares to an approximately
     * equivalent Integer operation.</p>
     *
     * @throws Throwable any exceptions are passed up to JUnit to report.
     */
    public void testGetOrdinal() throws Throwable {
        long start;
        long enumTime = 0;
        long intTime = 0;
        int i;

        for (int af = 0; af < OPTIMIZING_RUNS + AVERAGING_RUNS; ++af) {
            if (af == OPTIMIZING_RUNS) {
                // Don't count optimizing runs in the total.
                enumTime = 0;
                intTime = 0;

            }

            start = System.currentTimeMillis();
            for (i = 0; i < TIMING_LENGTH; ++i) {
                k += MySuit.CLUBS.getOrdinal();
                k += MySuit.DIAMONDS.getOrdinal();
                k += MySuit.HEARTS.getOrdinal();
                k += MySuit.SPADES.getOrdinal();

            }
            enumTime += System.currentTimeMillis()-start;

            start = System.currentTimeMillis();
            for (i = 0; i < TIMING_LENGTH; ++i) {
                k += IntSuit.CLUBS.getOrdinal();
                k += IntSuit.DIAMONDS.getOrdinal();
                k += IntSuit.HEARTS.getOrdinal();
                k += IntSuit.SPADES.getOrdinal();

            }
            intTime += System.currentTimeMillis()-start;

        }

        double enumAvg = (double)enumTime/AVERAGING_RUNS;
        double intAvg = (double)intTime/AVERAGING_RUNS;

        double ratio = enumAvg/intAvg;

        System.out.println("testGetOrdinal :: Average timings: " +
                           "Enum=" + enumAvg + " / " + "Int=" + intAvg +
                           " ==> ratio=" + ratio);

        assertTrue("Ratio too high. (" + ratio + ")", ratio <= RATIO_MULTIPLE);

    }

    /**
     * <p>Tests speed of Enum.getEnumByOrdinal().  Compares to an approximately
     * equivalent Integer operation.</p>
     *
     * @throws Throwable any exceptions are passed up to JUnit to report.
     */
    public void testGetEnumByOrdinal() throws Throwable {
        long start;
        long enumTime = 0;
        long intTime = 0;
        int i;
        int enumOrds[] = new int[]{MySuit.CLUBS.getOrdinal(),
                                   MySuit.DIAMONDS.getOrdinal(),
                                   MySuit.HEARTS.getOrdinal(),
                                   MySuit.SPADES.getOrdinal()};
        int intOrds[] = new int[]{IntSuit.CLUBS.getOrdinal(),
                                  IntSuit.DIAMONDS.getOrdinal(),
                                  IntSuit.HEARTS.getOrdinal(),
                                  IntSuit.SPADES.getOrdinal()};

        for (int af = 0; af < OPTIMIZING_RUNS + AVERAGING_RUNS; ++af) {
            if (af == OPTIMIZING_RUNS) {
                // Don't count optimizing runs in the total.
                enumTime = 0;
                intTime = 0;

            }

            start = System.currentTimeMillis();
            for (i = 0; i < TIMING_LENGTH; ++i) {
                assertEquals(MySuit.CLUBS,
                             MySuit.getEnumByOrdinal(enumOrds[0],
                                                     MySuit.class));
                assertEquals(MySuit.DIAMONDS,
                             MySuit.getEnumByOrdinal(enumOrds[1],
                                                     MySuit.class));
                assertEquals(MySuit.HEARTS,
                             MySuit.getEnumByOrdinal(enumOrds[2],
                                                     MySuit.class));
                assertEquals(MySuit.SPADES,
                             MySuit.getEnumByOrdinal(enumOrds[3],
                                                     MySuit.class));

            }
            enumTime += System.currentTimeMillis()-start;

            start = System.currentTimeMillis();
            for (i = 0; i < TIMING_LENGTH; ++i) {
                assertEquals(IntSuit.CLUBS,
                             IntSuit.getIntByOrdinal(intOrds[0]));
                assertEquals(IntSuit.DIAMONDS,
                             IntSuit.getIntByOrdinal(intOrds[1]));
                assertEquals(IntSuit.HEARTS,
                             IntSuit.getIntByOrdinal(intOrds[2]));
                assertEquals(IntSuit.SPADES,
                             IntSuit.getIntByOrdinal(intOrds[3]));

            }
            intTime += System.currentTimeMillis()-start;

        }

        double enumAvg = (double)enumTime/AVERAGING_RUNS;
        double intAvg = (double)intTime/AVERAGING_RUNS;

        double ratio = enumAvg/intAvg;

        System.out.println("testGetEnumByOrdinal :: Average timings: " +
                           "Enum=" + enumAvg + " / " + "Int=" + intAvg +
                           " ==> ratio=" + ratio);

        assertTrue("Ratio too high. (" + ratio + ")", ratio <= RATIO_MULTIPLE);

    }

    /**
     * <p>Tests speed of Enum.compareTo() and Enum.equals().  Compares to an
     * approximately equivalent Integer operation.</p>
     *
     * @throws Throwable any exceptions are passed up to JUnit to report.
     */
    public void testComparisons() throws Throwable {
        long start;
        long enumTime = 0;
        long intTime = 0;
        int i;

        for (int af = 0; af < OPTIMIZING_RUNS + AVERAGING_RUNS; ++af) {
            if (af == OPTIMIZING_RUNS) {
                // Don't count optimizing runs in the total.
                enumTime = 0;
                intTime = 0;

            }

            start = System.currentTimeMillis();
            for (i = 0; i < TIMING_LENGTH; ++i) {
                assertTrue(MySuit.CLUBS.compareTo(MySuit.DIAMONDS) < 0);
                assertTrue(MySuit.HEARTS.compareTo(MySuit.SPADES) < 0);
                assertTrue(MySuit.SPADES.compareTo(MySuit.DIAMONDS) > 0);
                assertTrue(MySuit.CLUBS.equals(MySuit.CLUBS));

            }
            enumTime += System.currentTimeMillis()-start;

            start = System.currentTimeMillis();
            for (i = 0; i < TIMING_LENGTH; ++i) {
                assertTrue(IntSuit.CLUBS.compareTo(IntSuit.DIAMONDS) < 0);
                assertTrue(IntSuit.HEARTS.compareTo(IntSuit.SPADES) < 0);
                assertTrue(IntSuit.SPADES.compareTo(IntSuit.DIAMONDS) > 0);
                assertTrue(IntSuit.CLUBS.equals(IntSuit.CLUBS));

            }
            intTime += System.currentTimeMillis()-start;

        }

        double enumAvg = (double)enumTime/AVERAGING_RUNS;
        double intAvg = (double)intTime/AVERAGING_RUNS;

        double ratio = enumAvg/intAvg;

        System.out.println("testComparisons :: Average timings: " +
                           "Enum=" + enumAvg + " / " + "Int=" + intAvg +
                           " ==> ratio=" + ratio);

        assertTrue("Ratio too high. (" + ratio + ")", ratio <= RATIO_MULTIPLE);

    }

    /**
     * <p>Tests speed of serialization operations on an Enum.  Compares to an
     * approximately equivalent Integer operation.</p>
     *
     * @throws Throwable any exceptions are passed up to JUnit to report.
     */
    public void testSerialize() throws Throwable {
        long start;
        long enumTime = 0;
        long intTime = 0;
        int i;

        PipedOutputStream pos = new PipedOutputStream();
        PipedInputStream pis = new PipedInputStream(pos);
        ObjectOutputStream os = new ObjectOutputStream(pos);
        ObjectInputStream is = new ObjectInputStream(pis);

        for (int af = 0; af < OPTIMIZING_RUNS + AVERAGING_RUNS; ++af) {
            if (af == OPTIMIZING_RUNS) {
                // Don't count optimizing runs in the total.
                enumTime = 0;
                intTime = 0;

            }

            start = System.currentTimeMillis();
            for (i = 0; i < TIMING_LENGTH; ++i) {
                os.writeObject(MySuit.CLUBS);
                os.writeObject(MySuit.DIAMONDS);
                os.writeObject(MySuit.HEARTS);
                os.writeObject(MySuit.SPADES);

                assertEquals(MySuit.CLUBS, is.readObject());
                assertEquals(MySuit.DIAMONDS, is.readObject());
                assertEquals(MySuit.HEARTS, is.readObject());
                assertEquals(MySuit.SPADES, is.readObject());

            }
            enumTime += System.currentTimeMillis()-start;

            start = System.currentTimeMillis();
            for (i = 0; i < TIMING_LENGTH; ++i) {
                os.writeObject(IntSuit.CLUBS);
                os.writeObject(IntSuit.DIAMONDS);
                os.writeObject(IntSuit.HEARTS);
                os.writeObject(IntSuit.SPADES);

                assertEquals(IntSuit.CLUBS, is.readObject());
                assertEquals(IntSuit.DIAMONDS, is.readObject());
                assertEquals(IntSuit.HEARTS, is.readObject());
                assertEquals(IntSuit.SPADES, is.readObject());

            }
            intTime += System.currentTimeMillis()-start;

        }

        double enumAvg = (double)enumTime/AVERAGING_RUNS;
        double intAvg = (double)intTime/AVERAGING_RUNS;

        double ratio = enumAvg/intAvg;

        System.out.println("testSerialize :: Average timings: " +
                           "Enum=" + enumAvg + " / " + "Int=" + intAvg +
                           " ==> ratio=" + ratio);

        assertTrue("Ratio too high. (" + ratio + ")", ratio <= RATIO_MULTIPLE);

    }

}
