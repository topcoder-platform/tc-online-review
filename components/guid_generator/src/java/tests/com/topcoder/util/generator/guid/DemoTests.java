/**
 * Copyright © 2004, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.generator.guid;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * <p>
 * Demonstrates usage of the component.
 * </p>
 * <ul>
 * <li>The demo provided in the specification</li>
 * <li>Show how they look like the string representation of the 3 different types of UUID </li>
 * <li>Generate UUID's with a false random generator.</li>
 * </ul>
 *
 * @author TCSDEVELOPER
 * @author TCSDESIGNER
 * @version 1.0
 */
public class DemoTests extends TestCase {

    /**
     * return a suite of tests.
     *
     * @return a suite of tests
     */
    public static Test suite() {
        return new TestSuite(DemoTests.class);
    }

    /**
     * Print some UUID's to see in the console how they look like.
     */
    public void testShowUUIDs() {
        System.out.println("\n-----< Demonstration of UUID generation >------");
        System.out.println("Those are 128 bits UUID Version 1:");
        for (int i = 0; i < 5; i++) {
            System.out.println(UUIDUtility.getNextUUID(UUIDType.TYPE1));
        }

        System.out.println("\nThose are 128 bits UUID Version 4:");
        for (int i = 0; i < 5; i++) {
            System.out.println(UUIDUtility.getNextUUID(UUIDType.TYPE4));
        }

        System.out.println("\nThose are INT 32 bits:");
        for (int i = 0; i < 5; i++) {
            System.out.println(UUIDUtility.getNextUUID(UUIDType.TYPEINT32));
        }
    }

    /**
     * Print some UUID's with ZeroRandom class to see in the console how they look like.
     * ZeroRandom returns always 0 when asked for random numbers.
     * 
     * Int32 generated UUID's should'nt show evidence of the fact that ZeroRandom class is used,
     * due to the time involved and the CRC computation
     *
     * Version1 UUID's will show 0's in the node "random" part and clock sequence.
     * 
     * Version4 UUID's will be all equals, because this version is only random based. 
     */
    public void testShowUUIDsZeroRandom() {
        System.out.println("\n-----< Demonstration of UUID generation with ZeroRandom class >------");
        Generator gen;
        
        System.out.println("Those are 128 bits UUID Version 1:");
        gen = new UUIDVersion1Generator(new ZeroRandom());
        for (int i = 0; i < 5; i++) {
            System.out.println(gen.getNextUUID());
        }

        System.out.println("\nThose are 128 bits UUID Version 4:");
        gen = new UUIDVersion4Generator(new ZeroRandom());
        for (int i = 0; i < 5; i++) {
            System.out.println(gen.getNextUUID());
        }

        System.out.println("\nThose are INT 32 bits:");
        gen = new Int32Generator(new ZeroRandom());
        for (int i = 0; i < 5; i++) {
            System.out.println(gen.getNextUUID());
        }
    }

    /**
     * Run the demo provided in the specification 4.3. with some minor changes:
     * - UUIDFactory is actually called UUIDUtility
     * - getUUIDString() replaced by a constant string
     * - added a print of uuid variable
     * - UUID's stored in a Set instead of a database.
     */
    public void testSpecificationDemo() {
        System.out.println("\n-----< Demo provided in the specification >------");
        // Generate a version1 128-bit UUID
        UUID uuid = UUIDUtility.getNextUUID(UUIDType.TYPE1);

        // Get the bytes assigned to the uuid
        byte[] bytes = uuid.toByteArray();

        // Print out the UUID
        System.out.println("Generated a "
                + uuid.getBitCount()
                + "-bit UUID represented as "
                + uuid.toString());

        // Retrieve a UUID from a datasource
        String uuidstring = "ab12cd34"; // getUUIDString();

        // Recreate the UUID
        uuid = AbstractUUID.parse(uuidstring);

        // show the recreated uuid. toString() method will be automatically called.  by TCSDEVELOPER
        System.out.println("Recreated uuid:" + uuid);

        // Get a reference to the UUID and generate a bunch
        Generator generator = UUIDUtility.getGenerator(UUIDType.TYPE4);

        // Generate a bunch and store to a set
        Set values = new HashSet();
        int repeated = 0;
        for (int x = 0; x < 1000; x++) {
            if (!values.add(generator.getNextUUID().toString())) {
                repeated++;
            }
        }
        // show how many repeated items found.
        // It's almost impossible that this value is more than 0 if the implementation is rigth.
        // This is already tested in UUIDVersion1Generator test, so here it is provided only to show the results.
        System.out.println("Generating 1000 UUID's of type 4, found: " + repeated + " UUID's repeated");

        //  Generate a 32-bit UUID
         uuid = UUIDUtility.getNextUUID(UUIDType.TYPEINT32);

        // Get the bytes assigned to the uuid
        bytes = uuid.toByteArray();

        // Print out the UUID
        System.out.println("Generated a " + uuid.getBitCount() + "-bit UUID represented as " + uuid.toString());

        // Use the IDGenerator adapter
        // (Assumes the IDGenerator has been configured to use the adapter)
        // please uncomment that when using a IDGenerator version that enables to plug in classes
        //long id = IDGeneratorFactory.getIDGenerator(IDGeneratorAdapter.getClass().getName());

        //  Create version4 UUID using a custom randomizer
        Random myRandom = new Random();
        Generator gen = new UUIDVersion4Generator(myRandom);

        //   Use it
        uuid = gen.getNextUUID();

        // Print out the UUID
        System.out.println("Generated a " + uuid.getBitCount() + "-bit UUID represented as " + uuid.toString());
    }
}