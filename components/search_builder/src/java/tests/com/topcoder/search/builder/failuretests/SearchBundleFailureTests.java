/**
 * Copyright (c) 2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.search.builder.failuretests;

import com.topcoder.search.builder.*;
import com.topcoder.search.builder.database.*;
import com.topcoder.search.builder.filter.*;
import com.topcoder.search.builder.ldap.*;

import com.topcoder.util.datavalidator.IntegerValidator;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Failure test cases for SearchBundle.
 *
 * @author WishingBone
 * @version 1.1
 */
public class SearchBundleFailureTests extends TestCase {
//    /**
//     * Create with null name.
//     */
//    public void testCreate_NullName() {
//        try {
//            new SearchBundle(null, new HashMap());
//            fail("Should have thrown NullPointerException.");
//        } catch (NullPointerException npe) {
//        }
//    }
//
//    /**
//     * Create with empty name.
//     */
//    public void testCreate_EmptyName() {
//        try {
//            new SearchBundle("", new HashMap());
//            fail("Should have thrown IllegalArgumentException.");
//        } catch (IllegalArgumentException iae) {
//        }
//    }
//
//    /**
//     * Create with null alias.
//     */
//    public void testCreate_NullAliad() {
//        try {
//            new SearchBundle("bundle", null);
//            fail("Should have thrown NullPointerException.");
//        } catch (NullPointerException npe) {
//        }
//    }
//
//    /**
//     * Create with invalid alias key.
//     */
//    public void testCreate_InvalidAliadKey() {
//        Map alias = new HashMap();
//        alias.put(new Object(), "field");
//
//        try {
//            new SearchBundle("bundle", alias);
//            fail("Should have thrown IllegalArgumentException.");
//        } catch (IllegalArgumentException iae) {
//        }
//    }
//
//    /**
//     * Create with invalid alias value.
//     */
//    public void testCreate_InvalidAliadValue() {
//        Map alias = new HashMap();
//        alias.put("field", new Object());
//
//        try {
//            new SearchBundle("bundle", alias);
//            fail("Should have thrown IllegalArgumentException.");
//        } catch (IllegalArgumentException iae) {
//        }
//    }
//
//    /**
//     * Create with null name.
//     */
//    public void testCreate_NullName2() {
//        try {
//            new SearchBundle(null, new HashMap(), new HashMap());
//            fail("Should have thrown NullPointerException.");
//        } catch (NullPointerException npe) {
//        }
//    }
//
//    /**
//     * Create with empty name.
//     */
//    public void testCreate_EmptyName2() {
//        try {
//            new SearchBundle("", new HashMap(), new HashMap());
//            fail("Should have thrown IllegalArgumentException.");
//        } catch (IllegalArgumentException iae) {
//        }
//    }
//
//    /**
//     * Create with null fields.
//     */
//    public void testCreate_NullFields() {
//        try {
//            new SearchBundle("bundle", null, new HashMap());
//            fail("Should have thrown NullPointerException.");
//        } catch (NullPointerException npe) {
//        }
//    }
//
//    /**
//     * Create with invalid field key.
//     */
//    public void testCreate_InvalidFieldKey() {
//        Map fields = new HashMap();
//        fields.put(new Object(), IntegerValidator.greaterThan(12));
//
//        try {
//            new SearchBundle("bundle", fields, new HashMap());
//            fail("Should have thrown IllegalArgumentException.");
//        } catch (IllegalArgumentException iae) {
//        }
//    }
//
//    /**
//     * Create with invalid field value.
//     */
//    public void testCreate_InvalidFieldValue() {
//        Map fields = new HashMap();
//        fields.put("field", new Object());
//
//        try {
//            new SearchBundle("bundle", fields, new HashMap());
//            fail("Should have thrown IllegalArgumentException.");
//        } catch (IllegalArgumentException iae) {
//        }
//    }

    /**
     * Create with null alias.
     */
    public void testCreate_NullAliad2() {
        try {
            new SearchBundle("bundle", new HashMap(), null);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException pe) {
        }
    }

//    /**
//     * Create with invalid alias key.
//     */
//    public void testCreate_InvalidAliadKey2() {
//        Map alias = new HashMap();
//        alias.put(new Object(), "field");
//
//        try {
//            new SearchBundle("bundle", new HashMap(), alias);
//            fail("Should have thrown IllegalArgumentException.");
//        } catch (IllegalArgumentException iae) {
//        }
//    }
//
//    /**
//     * Create with invalid alias value.
//     */
//    public void testCreate_InvalidAliadValue2() {
//        Map alias = new HashMap();
//        alias.put("field", new Object());
//
//        try {
//            new SearchBundle("bundle", new HashMap(), alias);
//            fail("Should have thrown IllegalArgumentException.");
//        } catch (IllegalArgumentException iae) {
//        }
//    }
//
//    /**
//     * search() with null filter.
//     *
//     * @throws Exception to JUnit.
//     */
//    public void testSearch_NullFilter() throws Exception {
//        try {
//            new SearchBundle("bundle", new HashMap()).search(null);
//            fail("Should have thrown NullPointerException.");
//        } catch (NullPointerException npe) {
//        }
//    }
//
//    /**
//     * search() with invalid filter.
//     *
//     * @throws Exception to JUnit.
//     */
//    public void testSearch_InvalidFilter() throws Exception {
//        Map fields = new HashMap();
//        fields.put("quantity", IntegerValidator.greaterThan(12));
//
//        try {
//            Map map = new HashMap();
//            map.put("test", "test");
//            new SearchBundle("bundle", fields, map).search(new EqualToFilter(
//                    "quantity", new Integer(12)));
//            fail("Should have thrown PersistenceOperationException.");
//        } catch (SearchBuilderException poe) {
//        }
//    }
//
//    /**
//     * search() with invalid persistence.
//     *
//     * @throws Exception to JUnit.
//     */
//    public void testSearch_InvalidPersistence() throws Exception {
//        try {
//            Map map = new HashMap();
//            map.put("test", "test");
//            new SearchBundle("bundle", map).search(new EqualToFilter(
//                    "quantity", new Integer(12)));
//            fail("Should have thrown PersistenceOperationException.");
//        } catch (PersistenceOperationException poe) {
//        }
//    }
//
//    /**
//     * search() with null filter.
//     *
//     * @throws Exception to JUnit.
//     */
//    public void testSearch_NullFilter2() throws Exception {
//        List returnFields = new ArrayList();
//        returnFields.add("coder");
//
//        try {
//            Map map = new HashMap();
//            map.put("test", "test");
//            new SearchBundle("bundle", map).search(null, returnFields);
//            fail("Should have thrown NullPointerException.");
//        } catch (NullPointerException npe) {
//        }
//    }
//
//    /**
//     * search() with invalid filter.
//     *
//     * @throws Exception to JUnit.
//     */
//    public void testSearch_InvalidFilter2() throws Exception {
//        List returnFields = new ArrayList();
//        returnFields.add("coder");
//
//        Map fields = new HashMap();
//        fields.put("quantity", IntegerValidator.greaterThan(12));
//
//        try {
//            Map map = new HashMap();
//            map.put("test", "test");
//            new SearchBundle("bundle", fields, map).search(new EqualToFilter(
//                    "quantity", new Integer(12)), returnFields);
//            fail("Should have thrown PersistenceOperationException.");
//        } catch (SearchBuilderException poe) {
//        }
//    }
//
//    /**
//     * search() with invalid persistence.
//     *
//     * @throws Exception to JUnit.
//     */
//    public void testSearch_InvalidPersistence2() throws Exception {
//        List returnFields = new ArrayList();
//        returnFields.add("coder");
//
//        try {
//            Map map = new HashMap();
//            map.put("test", "test");
//            new SearchBundle("bundle", map).search(new EqualToFilter(
//                    "quantity", new Integer(12)), returnFields);
//            fail("Should have thrown PersistenceOperationException.");
//        } catch (PersistenceOperationException poe) {
//        }
//    }
//
//    /**
//     * search() with null return fields.
//     *
//     * @throws Exception to JUnit.
//     */
//    public void testSearch_NullReturnFields() throws Exception {
//        try {
//            Map map = new HashMap();
//            map.put("test", "test");
//            new SearchBundle("bundle", map).search(new EqualToFilter(
//                    "quantity", new Integer(12)), null);
//            fail("Should have thrown NullPointerException.");
//        } catch (NullPointerException npe) {
//        }
//    }
//
//    /**
//     * search() with null return field.
//     *
//     * @throws Exception to JUnit.
//     */
//    public void testSearch_NullReturnField() throws Exception {
//        List returnFields = new ArrayList();
//        returnFields.add(null);
//
//        try {
//            Map map = new HashMap();
//            map.put("test", "test");
//            new SearchBundle("bundle", map).search(new EqualToFilter(
//                    "quantity", new Integer(12)), returnFields);
//            fail("Should have thrown PersistenceOperationException.");
//        } catch (PersistenceOperationException iae) {
//        }
//    }
//
//    /**
//     * search() with invalid return field.
//     *
//     * @throws Exception to JUnit.
//     */
//    public void testSearch_InvalidReturnField() throws Exception {
//        List returnFields = new ArrayList();
//        returnFields.add(new Object());
//
//        try {
//            Map map = new HashMap();
//            map.put("test", "test");
//            new SearchBundle("bundle", map).search(new EqualToFilter(
//                    "quantity", new Integer(12)), returnFields);
//            fail("Should have thrown PersistenceOperationException.");
//        } catch (PersistenceOperationException iae) {
//        }
//    }

    /**
     * buildGreaterThanFilter() with null name.
     */
    public void testBuildGreaterThanFilter_NullName() {
        try {
            SearchBundle.buildGreaterThanFilter(null, new Integer(12));
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException npe) {
        }
    }

    /**
     * buildGreaterThanFilter() with null value.
     */
    public void testBuildGreaterThanFilter_NullValue() {
        try {
            SearchBundle.buildGreaterThanFilter("quantity", null);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException npe) {
        }
    }

    /**
     * buildGreaterThanFilter() with empty name.
     */
    public void testBuildGreaterThanFilter_EmptyName() {
        try {
            SearchBundle.buildGreaterThanFilter("", new Integer(12));
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException iae) {
        }
    }

    /**
     * buildLessThanFilter() with null name.
     */
    public void testBuildLessThanFilter_NullName() {
        try {
            SearchBundle.buildLessThanFilter(null, new Integer(12));
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException npe) {
        }
    }

    /**
     * buildLessThanFilter() with null value.
     */
    public void testBuildLessThanFilter_NullValue() {
        try {
            SearchBundle.buildLessThanFilter("quantity", null);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException npe) {
        }
    }

    /**
     * buildLessThanFilter() with empty name.
     */
    public void testBuildLessThanFilter_EmptyName() {
        try {
            SearchBundle.buildLessThanFilter("", new Integer(12));
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException iae) {
        }
    }

    /**
     * buildGreaterThanOrEqualToFilter() with null name.
     */
    public void testBuildGreaterThanOrEqualToFilter_NullName() {
        try {
            SearchBundle.buildGreaterThanOrEqualToFilter(null, new Integer(12));
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException npe) {
        }
    }

    /**
     * buildGreaterThanOrEqualToFilter() with null value.
     */
    public void testBuildGreaterThanOrEqualToFilter_NullValue() {
        try {
            SearchBundle.buildGreaterThanOrEqualToFilter("quantity", null);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException npe) {
        }
    }

    /**
     * buildGreaterThanOrEqualToFilter() with empty name.
     */
    public void testBuildGreaterThanOrEqualToFilter_EmptyName() {
        try {
            SearchBundle.buildGreaterThanOrEqualToFilter("", new Integer(12));
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException iae) {
        }
    }

    /**
     * buildLessThanOrEqualToFilter() with null name.
     */
    public void testBuildLessThanOrEqualToFilter_NullName() {
        try {
            SearchBundle.buildLessThanOrEqualToFilter(null, new Integer(12));
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException npe) {
        }
    }

    /**
     * buildLessThanOrEqualToFilter() with null value.
     */
    public void testBuildLessThanOrEqualToFilter_NullValue() {
        try {
            SearchBundle.buildLessThanOrEqualToFilter("quantity", null);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException npe) {
        }
    }

    /**
     * buildLessThanOrEqualToFilter() with empty name.
     */
    public void testBuildLessThanOrEqualToFilter_EmptyName() {
        try {
            SearchBundle.buildLessThanOrEqualToFilter("", new Integer(12));
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException iae) {
        }
    }

    /**
     * buildEqualToFilter() with null name.
     */
    public void testBuildEqualToFilter_NullName() {
        try {
            SearchBundle.buildEqualToFilter(null, new Integer(12));
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException npe) {
        }
    }

    /**
     * buildEqualToFilter() with null value.
     */
    public void testBuildEqualToFilter_NullValue() {
        try {
            SearchBundle.buildEqualToFilter("quantity", null);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException npe) {
        }
    }

    /**
     * buildEqualToFilter() with empty name.
     */
    public void testBuildEqualToFilter_EmptyName() {
        try {
            SearchBundle.buildEqualToFilter("", new Integer(12));
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException iae) {
        }
    }

    /**
     * buildBetweenFilter() with null name.
     */
    public void testBuildBetweenFilter_NullName() {
        try {
            SearchBundle.buildBetweenFilter(null, new Integer(15),
                new Integer(12));
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException npe) {
        }
    }

    /**
     * buildBetweenFilter() with null upper.
     */
    public void testBuildBetweenFilter_NullUpper() {
        try {
            SearchBundle.buildBetweenFilter("quantity", null, new Integer(12));
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException npe) {
        }
    }

    /**
     * buildBetweenFilter() with null lower.
     */
    public void testBuildBetweenFilter_NullLower() {
        try {
            SearchBundle.buildBetweenFilter("quantity", new Integer(15), null);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException npe) {
        }
    }

    /**
     * buildBetweenFilter() with empty name.
     */
    public void testBuildBetweenFilter_EmptyName() {
        try {
            SearchBundle.buildBetweenFilter("", new Integer(15), new Integer(12));
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException iae) {
        }
    }

    /**
     * buildBetweenFilter() with invalid range.
     */
    public void testBuildBetweenFilter_InvalidRange() {
        try {
            SearchBundle.buildBetweenFilter("", new Integer(12), new Integer(15));
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException iae) {
        }
    }

    /**
     * buildInFilter() with null name.
     */
    public void testBuildInFilter_NullName() {
        List values = new ArrayList();
        values.add(new Integer(12));

        try {
            SearchBundle.buildInFilter(null, values);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException npe) {
        }
    }

    /**
     * buildInFilter() with null values.
     */
    public void testBuildInFilter_NullValues() {
        try {
            SearchBundle.buildInFilter("quantity", null);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException npe) {
        }
    }

    /**
     * buildInFilter() with null value.
     */
    public void testBuildInFilter_NullValue() {
        List values = new ArrayList();
        values.add(null);

        try {
            SearchBundle.buildInFilter("quantity", values);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException iae) {
        }
    }

    /**
     * buildInFilter() with invalid value.
     */
    public void testBuildInFilter_InvalidValue() {
        List values = new ArrayList();
        values.add(new Object());

        try {
            SearchBundle.buildInFilter("quantity", values);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException iae) {
        }
    }

    /**
     * buildInFilter() with empty name.
     */
    public void testBuildInFilter_EmptyName() {
        List values = new ArrayList();
        values.add(new Integer(12));

        try {
            SearchBundle.buildInFilter("", values);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException iae) {
        }
    }

    /**
     * buildAndFilter() with null filter1.
     */
    public void testBuildAndFilter_NullFilter1() {
        try {
            SearchBundle.buildAndFilter(null,
                new EqualToFilter("quantity", new Integer(12)));
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException npe) {
        }
    }

    /**
     * buildAndFilter() with null filter2.
     */
    public void testBuildAndFilter_NullFilter2() {
        try {
            SearchBundle.buildAndFilter(new EqualToFilter("quantity",
                    new Integer(12)), null);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException npe) {
        }
    }

    /**
     * buildOrFilter() with null filter1.
     */
    public void testBuildOrFilter_NullFilter1() {
        try {
            SearchBundle.buildOrFilter(null,
                new EqualToFilter("quantity", new Integer(12)));
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException npe) {
        }
    }

    /**
     * buildOrFilter() with null filter2.
     */
    public void testBuildOrFilter_NullFilter2() {
        try {
            SearchBundle.buildOrFilter(new EqualToFilter("quantity",
                    new Integer(12)), null);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException npe) {
        }
    }

    /**
     * buildNotFilter() with null filter.
     */
    public void testBuildNotFilter_NullFilter() {
        try {
            SearchBundle.buildNotFilter(null);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException npe) {
        }
    }

//    /**
//     * setSearchableFields() with null fields.
//     */
//    public void testSetSearchableFields_NullFields() {
//        try {
//            Map map = new HashMap();
//            map.put("test", "test");
//            new SearchBundle("bundle", map).setSearchableFields(null);
//            fail("Should have thrown NullPointerException.");
//        } catch (NullPointerException npe) {
//        }
//    }
//
//    /**
//     * setSearchableFields() with invalid field value.
//     */
//    public void testSetSearchableFields_InvalidFieldValue() {
//        Map fields = new HashMap();
//        fields.put("quantity", new Object());
//
//        try {
//            new SearchBundle("bundle", new HashMap()).setSearchableFields(fields);
//            fail("Should have thrown IllegalArgumentException.");
//        } catch (IllegalArgumentException iae) {
//        }
//    }
//
//    /**
//     * validateFilter() with null filter.
//     */
//    public void testValidateFilter_NullFilter() {
//        try {
//            Map map = new HashMap();
//            map.put("test", "test");
//            new SearchBundle("bundle", map).validateFilter(null);
//            fail("Should have thrown NullPointerException.");
//        } catch (NullPointerException npe) {
//        }
//    }
//
//    /**
//     * setSearchStringBuilder() with null builder.
//     */
//    public void testSetSearchStringBuilder_NullBuilder() {
//        try {
//        	Map map = new HashMap();
//            map.put("test", "test");
//            new SearchBundle("bundle", map).setSearchStringBuilder(null);
//            fail("Should have thrown NullPointerException.");
//        } catch (NullPointerException npe) {
//        }
//    }
//
//    /**
//     * setConnectionStrategy() with null strategy.
//     *
//     * @throws Exception to JUnit.
//     */
//    public void testSetConnectionStrategy_NullStrategy()
//        throws Exception {
//        try {
//        	Map map = new HashMap();
//            map.put("test", "test");
//            new SearchBundle("bundle", map).setConnectionStrategy(null);
//            fail("Should have thrown NullPointerException.");
//        } catch (NullPointerException npe) {
//        }
//    }
}
