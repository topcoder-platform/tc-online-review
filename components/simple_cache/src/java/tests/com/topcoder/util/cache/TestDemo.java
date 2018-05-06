/*
 * Copyright (c) 2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.cache;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.io.Serializable;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

/**
 * First demo from Component Specification document.
 *
 * @author  AleaActaEst, rem
 * @version 2.0
 * @since   2.0
 */
public class TestDemo extends TestCase {

    /**
     * Returns the test suite for this class.
     *
     * @return a new TestSuite representing this class.
     */
    public static Test suite() {
        return new TestSuite(TestDemo.class);
    }

    /**
     * Tests demo.
     */
    public void testDemo() {
        // Here we will simply create a simple out–of-the-box cache with unlimited number of
        // records but with limited memory consumption with an upper bound of 2 Mb, which will be
        // refreshed every 3 hours and which will use the BOF eviction strategy. We will also use
        // default compression on the entries.

        // create a new SimpleCache instance
        SimpleCache recordCache = new SimpleCache(
                SimpleCache.NO_MAX_SIZE,    // no size limit
                3 * 60 * 60 * 1000,         // 3 hour timeout
                new BOFCacheEvictionStrategy(), // eviction strategy
                2 * 1024 * 1024,            // capacity of 2 Mb
                null,                       // default memory handler
                null,                       // default compression
                true);                      // compression is on


         // Here we will simulate some work done on the database in the sense that we will
        // read a number of records. Here we will assume that we have 1000 records, which will be
        // read from the database.

        // Populate the cache with 1000 expensive to get records. We will use the
        // record id as the key for the object.
        for (int i = 1; i <= 1000; i++) {
            Record rec = Record.readFromDB(i);
            recordCache.put("" + i, rec);
        }

        // Lets check that all records have been successfully entered. This of
        // course assumes that the combined size of all compressed entries is
        // less than 2 Mb
        // We should get 1000 entries
        assertEquals("Cache should contain 1000 entries", 1000, recordCache.getSize());

        // Lets check how much memory this takes up
        assertTrue("Total size of all objects should be less than 2 Mb", recordCache.getByteSize() < 2000000);

        // Lets check if compression is on. It should be on
        assertTrue("Compression should be on", recordCache.getCompressionFlag());

        // lets view the keyset of this cache. We should get keys from 1 to 1000
        boolean[] valueExists = new boolean[1001];
        Set keyset = recordCache.keySet();
        Iterator it = keyset.iterator();
        while (it.hasNext()) {
            int k = Integer.parseInt(it.next().toString());
            assertTrue("Key should be valid", k >= 1 && k <= 1000 && !valueExists[k]);
            valueExists[k] = true;
        }
        assertEquals("Size of keyset should be 1000", 1000, keyset.size());

        // lets get all the values in the cache. For this demo we should get
        // values from "Record 1" to "Record 1000"
        valueExists = new boolean[1001];
        Iterator recordIterator = recordCache.values();
        int cnt = 0;
        while (recordIterator.hasNext()) {
            ++cnt;
            String st = ((Record) recordIterator.next()).getRecordInfo();
            assertTrue(st.startsWith("Record "));
            st = st.substring(7, st.length());
            int k = Integer.parseInt(st);
            assertTrue("Key should be valid", k >= 1 && k <= 1000 && !valueExists[k]);
            valueExists[k] = true;
        }
        assertEquals("Number of values should be 1000", 1000, cnt);

        // Lets get a specific record (id=1000) from the cache
        // this should return "Record 1000"
        Record rec = (Record) (recordCache.get("1000"));
        assertEquals("Record should be 'Record 1000'", "Record 1000", rec.getRecordInfo());


        // Removal functionality

        // Remove the first record
        recordCache.remove("1");


        // Remove records "2" through "99"
        Set keysToRemove = new HashSet();
        for (int key = 2; key <= 99; key++) {
            keysToRemove.add("" + key);
        }
        recordCache.removeSet(keysToRemove);
        assertTrue(setsAreEqual(recordCache.keySet(), getSet(100, 1000)));

        // Remove records whose keys match the regular expression
        // "1[0-9]{2}" which should remove all the values whose keys
        // match any string from "100" through "199"
        recordCache.removeByPattern("1[0-9]{2}");
        assertTrue(setsAreEqual(recordCache.keySet(), getSet(200, 1000)));

        // remove records older than the records with key "99"
        // Since the records have been added from "1" to "1000" in a timed
        // sequence this means that any entry whose id is < "99" is older.
        recordCache.removeOlder("99");
        assertTrue(setsAreEqual(recordCache.keySet(), getSet(200, 1000)));

        // Here we will simulate adding some rouge objects of type Vector
        recordCache.put("rouge001", new Vector());
        recordCache.put("rouge002", new Vector());
        // Now we remove these object based on their class type
        recordCache.removeLike(new Vector());
        assertTrue(setsAreEqual(recordCache.keySet(), getSet(200, 1000)));

        // We will clear the cache completely
        recordCache.clear();
        assertEquals("keySet should be empty.", 0, recordCache.keySet().size());

        // We will turn off compression
        recordCache.setCompressionFlag(false);
        assertFalse("compression flag should be set to false.", recordCache.getCompressionFlag());

        // We will repopulate the cache with records
        for (int key = 1; key <= 1000; key++) {
            rec = Record.readFromDB(key);
            recordCache.put("" + key , rec);
        }

        // We will switch the eviction strategy mid-stream
        recordCache.setEvictionStrategy(new FIFOCacheEvictionStrategy());
    }

    /**
     * Utility method. Checks that two sets are equal.
     *
     * @param set1 first set.
     * @param set2 second set.
     * @return true iif sets are equal.
     */
    private boolean setsAreEqual(Set set1, Set set2) {
        if (set1.size() != set2.size()) {
            return false;
        }
        for (Iterator it = set1.iterator(); it.hasNext();) {
            if (!set2.contains(it.next())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Utility method. Returns set containing string representation of numbers from k1 to k2.
     *
     * @param k1 lower limit.
     * @param k2 upper limit.
     * @return Set set of numbers from k1 to k2.
     */
    private Set getSet(int k1, int k2) {
        Set result = new HashSet();
        for (int i = k1; i <= k2; ++i) {
            result.add(new Integer(i).toString());
        }
        return result;
    }
}

/*
 * Simulated data base record. This will also simulate a variable but
 * predictable memory load since each record will be driven by its id for
 * memory consumption.
 */
class Record implements Serializable {
    private int id;
    private String record;
    private int[] memoryLoad = null;

    public Record(int id, String record) {
        this.id = id;
        this.record = record;   // simulate record content
        memoryLoad = new int[id];   // simulate memory load
    }

    // a simulated db reader
    public static Record readFromDB(int id) {
        return new Record(id, "Record " + id);
    }

    public int getID() {
        return id;
    }

    public String getRecordInfo() {
        return record;
    }
}
