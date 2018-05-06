/**
 * Copyright (c) 2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.search.builder.failuretests;

import com.topcoder.search.builder.*;
import com.topcoder.search.builder.filter.*;
import com.topcoder.search.builder.database.*;
import com.topcoder.search.builder.ldap.*;
import junit.framework.TestCase;
import java.util.HashMap;
import java.util.Map;

/**
 * Failure test cases for SearchBundleManager.
 *
 * @author WishingBone
 * @version 1.1
 */
public class SearchBundleManagerFailureTests extends TestCase {

    /**
     * Create with null namespace.
     *
     * @throws Exception to JUnit.
     */
    public void testCreate_NullNamespace() throws Exception {
        try {
            new SearchBundleManager(null);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException npe) {
        }
    }

    /**
     * Create with empty namespace.
     *
     * @throws Exception to JUnit.
     */
    public void testCreate_EmptyNamespace() throws Exception {
        try {
            new SearchBundleManager("");
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException iae) {
        }
    }

    /**
     * Create with invalid namespace.
     */
    public void testCreate_InvalidNamespace() {
        try {
            new SearchBundleManager("invalid");
            fail("Should have thrown SearchBuilderConfigurationException.");
        } catch (SearchBuilderConfigurationException sbce) {
        }
    }

    /**
     * getSearchBundle() with null name.
     */
    public void testGetSearchBundle_NullName() {
        try {
            new SearchBundleManager().getSearchBundle(null);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException npe) {
        }
    }

    /**
     * getSearchBundle() with empty name.
     */
    public void testGetSearchBundle_EmptyName() {
        try {
            new SearchBundleManager().getSearchBundle("");
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException iae) {
        }
    }

    /**
     * addSearchBundle() with null bundle.
     *
     * @throws Exception to JUnit.
     */
    public void testAddSearchBundle_NullBundle() throws Exception {
        try {
            new SearchBundleManager().addSearchBundle(null);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException npe) {
        }
    }

//    /**
//     * addSearchBundle() with duplicate bundle.
//     *
//     * @throws Exception to JUnit.
//     */
//    public void testAddSearchBundle_DuplicateBundle() throws Exception {
//        SearchBundleManager manager = new SearchBundleManager();
//        Map map = new HashMap();
//        map.put("test", "test");
//        SearchBundle bundle = new SearchBundle("bundle", map);
//        manager.addSearchBundle(bundle);
//        try {
//            manager.addSearchBundle(bundle);
//            fail("Should have thrown DuplicatedElementsException.");
//        } catch (DuplicatedElementsException dee) {
//        }
//    }

    /**
     * removeSearchBundle() with null name.
     */
    public void testRemoveSearchBundle_NullName() {
        try {
            new SearchBundleManager().removeSearchBundle(null);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException npe) {
        }
    }

    /**
     * removeSearchBundle() with empty name.
     */
    public void testRemoveSearchBundle_EmptyName() {
        try {
            new SearchBundleManager().removeSearchBundle("");
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException iae) {
        }
    }

    /**
     * setSearchBundle() with null bundles.
     */
    public void testSetSearchBundle_NullBundles() {
        try {
            new SearchBundleManager().removeSearchBundle(null);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException npe) {
        }
    }

//    /**
//     * setSearchBundle() with invalid name.
//     */
//    public void testSetSearchBundle_InvalidName() {
//        Map bundles = new HashMap();
//        Map map = new HashMap();
//        map.put("test", "test");
//        bundles.put(new Object(), new SearchBundle("bundle", map));
//        try {
//            new SearchBundleManager().setSearchBundle(bundles);
//            fail("Should have thrown IllegalArgumentException.");
//        } catch (IllegalArgumentException iae) {
//        }
//    }

    /**
     * setSearchBundle() with invalid bundle.
     */
    public void testSetSearchBundle_InvalidBundle() {
        Map bundles = new HashMap();
        bundles.put("bundle", new Object());
        try {
            new SearchBundleManager().setSearchBundle(bundles);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException iae) {
        }
    }

}
