/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder;


import com.topcoder.search.builder.database.DatabaseSearchStrategy;
import com.topcoder.search.builder.filter.AndFilter;
import com.topcoder.search.builder.filter.BetweenFilter;
import com.topcoder.search.builder.filter.EqualToFilter;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.filter.GreaterThanFilter;
import com.topcoder.search.builder.filter.GreaterThanOrEqualToFilter;
import com.topcoder.search.builder.filter.InFilter;
import com.topcoder.search.builder.filter.LessThanFilter;
import com.topcoder.search.builder.filter.LessThanOrEqualToFilter;
import com.topcoder.search.builder.filter.NotFilter;
import com.topcoder.search.builder.filter.OrFilter;

import com.topcoder.util.datavalidator.IntegerValidator;
import com.topcoder.util.datavalidator.StringValidator;
import com.topcoder.util.sql.databaseabstraction.CustomResultSet;

import junit.framework.TestCase;

import java.util.*;


/**
 * <p>
 * Unit test cases for SearchBundle.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.3
 */
public class SearchBundleTests extends TestCase {
    /**
     * The namespace to load the configration from for the manager.
     *
     */
    private static final String NAMESPACE = "com.topcoder.search.builder";
    /**
     * The namespace for DB.
     */
    private static final String NS = "DBSearchStrategy";
    /**
     * The test SearchBundle name.
     */
    private static final String NAME = "SearchBundle";

    /**
     * The instance of SearchBundle for test database.
     */
    private SearchBundle dbSearchBundle = null;

    /**
     * The instance of SearchBundle for test LDAP.
     */
    private SearchBundle ldSearchBundle = null;
    /**
     * The searchable field.
     *
     */
    private Map fields = null;

    /**
     * The map of alias name and real name.
     *
     */
    private Map aliasMap = null;
    /**
     * The Strategy instance to test.
     */
    private SearchStrategy strategy = null;
    /**
     * setUp.
     *
     * @throws Exception to junit
     */
    protected void setUp() throws Exception {
        //add the configuration
        TestHelper.clearConfig();
        TestHelper.addConfig();

        //makeup the fields
        fields = new HashMap();
        fields.put("age", IntegerValidator.inRange(0, 100));
        fields.put("weight", IntegerValidator.inRange(0, 100));

        //makeup the aliasMap
        aliasMap = new HashMap();
        aliasMap.put("The age", "age");
        aliasMap.put("The weight", "weight");
        aliasMap.put("sb", "searchbuild");
        fields.put("sn",
                StringValidator.hasLength(IntegerValidator.inRange(0, 10)));
        fields.put("sb",
                StringValidator.hasLength(IntegerValidator.inRange(0, 30)));
        //get the ldapSearch_bundle
        SearchBundleManager manager = new SearchBundleManager(NAMESPACE);

        dbSearchBundle = manager.getSearchBundle("FirstSearchBundle");
        ldSearchBundle = manager.getSearchBundle("SecondSearchBundle");
        strategy = new DatabaseSearchStrategy(NS);
        TestHelper.addEntries();
    }

    /**
     * tearDown.
     *
     * @throws Exception to junit
     */
    protected void tearDown() throws Exception {
        TestHelper.clearConfig();
        TestHelper.delEntries();
    }

    /**
     * The accuract test of constructor.
     *
     */
    public void testconstruct_accuracy1() {
        assertNotNull("can not construct the dbSearchBundle", dbSearchBundle);
    }
    /**
     * The accuract test of constructor.
     *
     */
    public void testconstruct_accuracy2() {
        assertNotNull("can not construct the dbSearchBundle", new SearchBundle(NAME, fields, aliasMap, "context"));
    }
    /**
     * The accuract test of constructor.
     *
     */
    public void testconstruct_accuracy3() {
        assertNotNull("can not construct the dbSearchBundle",
                new SearchBundle(NAME, fields, aliasMap, "context", strategy));
    }

    /**
     * test fail for construct SearchBundle with param is null.
     *
     */
    public void testconstruct3() {
        try {
            dbSearchBundle = new SearchBundle(null, fields, aliasMap, "context");
            fail("IllegalArgumentException should be throw");
        } catch (IllegalArgumentException e) {
            //success
        }
    }

    /**
     * test fail for construct SearchBundle with param is null.
     *
     */
    public void testconstruct4() {
        try {
            dbSearchBundle = new SearchBundle(NAME, null, aliasMap, "context");
            fail("IllegalArgumentException should be throw");
        } catch (IllegalArgumentException e) {
            //success
        }
    }

    /**
     * test fail for construct SearchBundle with param is null.
     *
     */
    public void testconstruct5() {
        try {
            dbSearchBundle = new SearchBundle(NAME, fields, null, "context");
            fail("IllegalArgumentException should be throw");
        } catch (IllegalArgumentException e) {
            //success
        }
    }

    /**
     * test fail for construct SearchBundle with param is empty.
     *
     */
    public void testconstruct9() {
        try {
            dbSearchBundle = new SearchBundle(NAME, new HashMap(), aliasMap, "context");
            fail("IllegalArgumentException should be throw");
        } catch (IllegalArgumentException e) {
            //success
        }
    }

    /**
     * test fail for construct SearchBundle with param is empty.
     *
     */
    public void testconstruct10() {
        try {
            dbSearchBundle = new SearchBundle("", fields, aliasMap, "context");
            fail("IllegalArgumentException should be throw");
        } catch (IllegalArgumentException e) {
            //success
        }
    }

    /**
     * test construct with exception.
     *
     */
    public void testconstruct11() {
        try {
            dbSearchBundle = new SearchBundle(NAME, aliasMap, "context");
        } catch (Exception e) {
            fail("No Exception should be throw");
        }
    }

    /**
     * test fail for construct SearchBundle with param is null.
     *
     */
    public void testconstruct12() {
        try {
            dbSearchBundle = new SearchBundle(null, aliasMap, "context");
            fail("IllegalArgumentException should be throw");
        } catch (IllegalArgumentException e) {
            //success
        }
    }

    /**
     * test fail for construct SearchBundle with param is null.
     *
     */
    public void testconstruct13() {
        try {
            dbSearchBundle = new SearchBundle(NAME, null, "context");
            fail("IllegalArgumentException should be throw");
        } catch (IllegalArgumentException e) {
            //success
        }
    }

    /**
     * test fail for construct SearchBundle with param is null.
     *
     */
    public void testconstruct14() {
        try {
            dbSearchBundle = new SearchBundle(NAME, aliasMap, null, "context");
            fail("IllegalArgumentException should be throw");
        } catch (IllegalArgumentException e) {
            //success
        }
    }

    /**
     * test fail for construct SearchBundle with param is empty.
     *
     */
    public void testconstruct15() {
        try {
            dbSearchBundle = new SearchBundle("", aliasMap, "context");
            fail("IllegalArgumentException should be throw");
        } catch (IllegalArgumentException e) {
            //success
        }
    }
    /**
     * test construct with exception.
     *
     */
    public void testconstruct16() {
        try {
            dbSearchBundle = new SearchBundle(NAME, fields, aliasMap, "context", strategy);
        } catch (Exception e) {
            fail("No Exception should be throw");
        }
    }

    /**
     * test fail for construct SearchBundle with param is null.
     *
     */
    public void testconstruct17() {
        try {
            dbSearchBundle = new SearchBundle(null, fields, aliasMap, "context", strategy);
            fail("IllegalArgumentException should be throw");
        } catch (IllegalArgumentException e) {
            //success
        }
    }

    /**
     * test fail for construct SearchBundle with param is null.
     *
     */
    public void testconstruct18() {
        try {
            dbSearchBundle = new SearchBundle(NAME, null, aliasMap, "context", strategy);
            fail("IllegalArgumentException should be throw");
        } catch (IllegalArgumentException e) {
            //success
        }
    }

    /**
     * test fail for construct SearchBundle with param is null.
     *
     */
    public void testconstruct19() {
        try {
            dbSearchBundle = new SearchBundle(NAME, fields, null, "context", strategy);
            fail("IllegalArgumentException should be throw");
        } catch (IllegalArgumentException e) {
            //success
        }
    }

    /**
     * test fail for construct SearchBundle with param is empty.
     *
     */
    public void testconstruct20() {
        try {
            dbSearchBundle = new SearchBundle(NAME, new HashMap(), aliasMap, "context", strategy);
            fail("IllegalArgumentException should be throw");
        } catch (IllegalArgumentException e) {
            //success
        }
    }

    /**
     * test fail for construct SearchBundle with param is empty.
     *
     */
    public void testconstruct21() {
        try {
            dbSearchBundle = new SearchBundle("", fields, aliasMap, "context", strategy);
            fail("IllegalArgumentException should be throw");
        } catch (IllegalArgumentException e) {
            //success
        }
    }

    /**
     * test construct with exception.
     *
     */
    public void testconstruct22() {
        try {
            dbSearchBundle = new SearchBundle(NAME, fields, "context", strategy);
        } catch (Exception e) {
            fail("No Exception should be throw");
        }
    }

    /**
     * test fail for construct SearchBundle with param is null.
     *
     */
    public void testconstruct23() {
        try {
            dbSearchBundle = new SearchBundle(null, aliasMap, "context", strategy);
            fail("IllegalArgumentException should be throw");
        } catch (IllegalArgumentException e) {
            //success
        }
    }

    /**
     * test fail for construct SearchBundle with param is null.
     *
     */
    public void testconstruct24() {
        try {
            dbSearchBundle = new SearchBundle(NAME, null, "context", strategy);
            fail("IllegalArgumentException should be throw");
        } catch (IllegalArgumentException e) {
            //success
        }
    }

    /**
     * test fail for construct SearchBundle with param is null.
     *
     */
    public void testconstruct25() {
        try {
            dbSearchBundle = new SearchBundle(NAME, aliasMap, null, "context", strategy);
            fail("IllegalArgumentException should be throw");
        } catch (IllegalArgumentException e) {
            //success
        }
    }

    /**
     * test fail for construct SearchBundle with param is empty.
     *
     */
    public void testconstruct26() {
        try {
            dbSearchBundle = new SearchBundle("", aliasMap, "context", strategy);
            fail("IllegalArgumentException should be throw");
        } catch (IllegalArgumentException e) {
            //success
        }
    }
    /**
     * test fail for construct SearchBundle with param is empty.
     *
     */
    public void testconstruct27() {
        try {
            dbSearchBundle = new SearchBundle(NAME, aliasMap, "context", null);
            fail("IllegalArgumentException should be throw");
        } catch (IllegalArgumentException e) {
            //success
        }
    }

    /**
     * test fail for search with null.
     *
     * @throws Exception to Junit
     */
    public void testsearch1() throws Exception {
        try {
            dbSearchBundle.search(null);
            fail("IllegalArgumentException should be throw");
        } catch (IllegalArgumentException e) {
            //success
        }
    }

    /**
     * test fail with invalid filter.
     *
     * @throws Exception to junit
     */
    public void testsearch2() throws Exception {
        try {
            EqualToFilter equalToFilter = new EqualToFilter("unsearchable",
                    new Integer(10));
            dbSearchBundle.search(equalToFilter);
            fail("SearchBuilderException should be throw");
        } catch (SearchBuilderException e) {
            //success
        }
    }

    /**
     * search from the table who's age = 10.
     *
     * @throws Exception to Junit
     */
    public void testsearch3() throws Exception {
        EqualToFilter equalToFilter = new EqualToFilter("The age",
                new Integer(10));

        //search depend the filter
        CustomResultSet result = (CustomResultSet) dbSearchBundle.search(equalToFilter);

        List list = new ArrayList();

        //get the result
        while (result.next()) {
            list.add(result.getString("peopleName"));
        }

        assertEquals("There are 2 prople with age = 10", 2, list.size());
    }

    /**
     * search from the table who's age = 10 and weight > 30.
     *
     * @throws Exception to Junit
     */
    public void testsearch4() throws Exception {
        EqualToFilter equalToFilter = new EqualToFilter("The age",
                new Integer(10));
        GreaterThanFilter greaterThanFilter = new GreaterThanFilter("The weight",
                new Integer(30));
        AndFilter andFilter = new AndFilter(equalToFilter, greaterThanFilter);

        //search depend the filter
        CustomResultSet result = (CustomResultSet) dbSearchBundle.search(andFilter);

        List list = new ArrayList();

        //get the result
        while (result.next()) {
            list.add(result.getString("peopleName"));
        }

        assertEquals("There are 1 prople with age = 10 and weight > 30", 1,
            list.size());
    }

    /**
     * test the mothed buildGreaterThanFilter.
     *
     */
    public void testbuildGreaterThanFilter() {
        Filter filter = null;

        try {
            filter = new GreaterThanFilter("test", new Integer(1));
        } catch (Exception e) {
            fail("no Exception should be throw");
        }

        assertTrue("The buildGreaterThanFilter create wrong instance",
            filter instanceof GreaterThanFilter);
    }

    /**
     * test the mothed buildLessThanFilter.
     *
     */
    public void testbuildLessThanFilter() {
        Filter filter = null;
        Filter a = null;
        try {
            filter = new LessThanFilter("test", new Integer(1));
            a = new LessThanFilter("seller", "c");
        } catch (Exception e) {
            fail("no Exception should be throw");
        }

        assertTrue("The buildLessThanFilter create wrong instance",
            filter instanceof LessThanFilter);
        assertTrue("The buildLessThanFilter create wrong instance",
                a instanceof LessThanFilter);
        assertEquals("lalal", ((LessThanFilter)a).getValue().toString(),"c");
    }

    /**
     * test the mothed buildGreaterThanOrEqualToFilter.
     *
     */
    public void testbuildGreaterThanOrEqualToFilter() {
        Filter filter = null;

        try {
            filter = new GreaterThanOrEqualToFilter("test",
                    new Integer(1));
        } catch (Exception e) {
            fail("no Exception should be throw");
        }

        assertTrue("The buildGreaterThanOrEqualToFilter create wrong instance",
            filter instanceof GreaterThanOrEqualToFilter);
    }

    /**
     * test the mothed buildLessThanOrEqualToFilter.
     *
     */
    public void testbuildLessThanOrEqualToFilter() {
        Filter filter = null;

        try {
            filter = new LessThanOrEqualToFilter("test",
                    new Integer(1));
        } catch (Exception e) {
            fail("no Exception should be throw");
        }

        assertTrue("The buildLessThanOrEqualToFilter create wrong instance",
            filter instanceof LessThanOrEqualToFilter);
    }

    /**
     * test the mothed buildEqualToFilter.
     *
     */
    public void testbuildEqualToFilter() {
        Filter filter = null;

        try {
            filter =new EqualToFilter("test", new Integer(1));
        } catch (Exception e) {
            fail("no Exception should be throw");
        }

        assertTrue("The buildEqualToFilter create wrong instance",
            filter instanceof EqualToFilter);
    }

    /**
     * test the mothed buildBetweenFilter.
     *
     */
    public void testbuildBetweenFilter() {
        Filter filter = null;

        try {
            filter = new BetweenFilter("test", new Integer(10),
                    new Integer(10));
        } catch (Exception e) {
            fail("no Exception should be throw");
        }

        assertTrue("The buildBetweenFilter create wrong instance",
            filter instanceof BetweenFilter);
    }

    /**
     * test the mothed buildInFilter.
     *
     */
    public void testbuildInFilter() {
        Filter filter = null;

        try {
            List list = new ArrayList();
            list.add(new Integer(1));
            list.add(new Integer(10));
            filter = new InFilter("test", list);
        } catch (Exception e) {
            fail("no Exception should be throw");
        }

        assertTrue("The buildInFilter create wrong instance",
            filter instanceof InFilter);
    }

    /**
     * test the mothed buildAndFilter.
     *
     */
    public void testbuildAndFilter() {
        Filter filter = null;

        try {
            Filter f1 = new EqualToFilter("test", new Integer(1));
            Filter f2 = new EqualToFilter("test", new Integer(10));
            filter = new AndFilter(f1, f2);
        } catch (Exception e) {
            fail("no Exception should be throw");
        }

        assertTrue("The buildAndFilter create wrong instance",
            filter instanceof AndFilter);
    }

    /**
     * test the mothed buildOrFilter.
     *
     */
    public void testbuildOrFilter() {
        Filter filter = null;

        try {
            Filter f1 = new EqualToFilter("test", new Integer(1));
            Filter f2 = new EqualToFilter("test", new Integer(10));
            filter = new OrFilter(f1, f2);
        } catch (Exception e) {
            fail("no Exception should be throw");
        }

        assertTrue("The buildOrFilter create wrong instance",
            filter instanceof OrFilter);
    }

    /**
     * test the mothed buildNotFilter.
     *
     */
    public void testbuildNotFilter() {
        Filter filter = null;

        try {
            Filter f1 = new EqualToFilter("test", new Integer(1));
            filter = new NotFilter(f1);
        } catch (Exception e) {
            fail("no Exception should be throw");
        }

        assertTrue("The buildNotFilter create wrong instance",
            filter instanceof NotFilter);
    }

    /**
     * test fail for  setSearchableFields with param is null.
     *
     */
    public void testsetSearchableFields1() {
        try {
            dbSearchBundle.setSearchableFields(null);
            fail("IllegalArgumentException should be throw");
        } catch (IllegalArgumentException e) {
            //success
        }
    }

    /**
     * test fail for  setSearchableFields with param is empty.
     *
     */
    public void testsetSearchableFields2() {
        try {
            dbSearchBundle.setSearchableFields(new HashMap());
            fail("IllegalArgumentException should be throw");
        } catch (IllegalArgumentException e) {
            //success
        }
    }

    /**
     * test setSearchableFields fail with the invalid values in map.
     * IllegalArgumentException should be throw
     *
     */
    public void testsetSearchableFields3() {
        try {
            Map map = new HashMap();
            map.put("test", "test");
            dbSearchBundle.setSearchableFields(map);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            //success
        }
    }

    /**
     * test validateFilter(return valid).
     *
     */
    public void testvalidateFilter1() {
        Filter f1 = new EqualToFilter("The age", new Integer(1));
        ValidationResult result = dbSearchBundle.validateFilter(f1);

        //valid
        assertTrue("The validateFilter result is wrong", result.isValid());
    }

    /**
     * test the mothed getname().
     *
     */
    public void testgetname() {
        assertEquals("The get name mothed is wrong", "FirstSearchBundle",
            dbSearchBundle.getName());
    }

    /**
     * test fail for search with param null.
     * @throws Exception to junit
     */
    public void testsearchwithlist1() throws Exception {
        try {
            dbSearchBundle.search(null, new ArrayList());
            fail("IllegalArgumentException should be throw");
        } catch (IllegalArgumentException e) {
            //success
        }
    }

    /**
     * test fail for search with param null.
     * @throws Exception to junit
     */
    public void testsearchwithlist2() throws Exception {
        try {
            dbSearchBundle.search(new EqualToFilter("age", new Integer(10)),
                null);
            fail("IllegalArgumentException should be throw");
        } catch (IllegalArgumentException e) {
            //success
        }
    }

    /**
     * test search with provide the return field.
     * @throws Exception to junit
     */
    public void testsearchwithlist3() throws Exception {
        //make up fields list
        List list = new ArrayList();
        list.add("peopleName");

        CustomResultSet result = (CustomResultSet) dbSearchBundle.search(new EqualToFilter(
                    "The age", new Integer(10)), list);
        result.next();
        assertNotNull("The field 'peopleName' should be return",
            result.getString("peopleName"));
    }

    /**
     * test search with in filter.
     *
     *@throws Exception to junit
     */
    public void testsearchInFilter() throws Exception {
        //makeup values
        List values = new ArrayList();
        values.add(new Integer(3));
        values.add(new Integer(5));
        values.add(new Integer(10));

        //construct infilter
        InFilter infilter = new InFilter("age", values);
        CustomResultSet result = (CustomResultSet) dbSearchBundle.search(infilter);
        int siz = 0;

        while (result.next()) {
            siz++;
        }

        assertEquals("5 items are live up to the infilter.", 5, siz);
    }

    /**
     * test search with in filter.
     *
     *@throws Exception to junit
     */
    public void testsearchNotFilter() throws Exception {
        //makeup values
        List values = new ArrayList();
        values.add(new Integer(3));
        values.add(new Integer(5));
        values.add(new Integer(10));

        //construct infilter
        InFilter infilter = new InFilter("age", values);
        NotFilter notFilter = new NotFilter(infilter);
        CustomResultSet result = (CustomResultSet) dbSearchBundle.search(notFilter);
        int siz = 0;

        while (result.next()) {
            siz++;
        }

        assertEquals("4 items are live up to the infilter.", 4, siz);
    }

    /**
     * test search with in filter.
     *
     *@throws Exception to junit
     */
    public void testsearchAndFilter() throws Exception {
        //makeup values
        List values = new ArrayList();
        values.add(new Integer(3));
        values.add(new Integer(5));
        values.add(new Integer(10));

        //construct infilter
        InFilter infilter = new InFilter("age", values);
        NotFilter notFilter = new NotFilter(infilter);
        AndFilter andFilter = new AndFilter(infilter, notFilter);
        CustomResultSet result = (CustomResultSet) dbSearchBundle.search(andFilter);
        int siz = 0;

        while (result.next()) {
            siz++;
        }
        assertEquals("0 items are live up to the infilter.", 0, siz);
    }

    /**
     * test search with in filter.
     *
     *@throws Exception to junit
     */
    public void testsearchOrFilter() throws Exception {
        //makeup values
        List values = new ArrayList();
        values.add(new Integer(3));
        values.add(new Integer(5));
        values.add(new Integer(10));

        //construct infilter
        InFilter infilter = new InFilter("age", values);
        NotFilter notFilter = new NotFilter(infilter);
        OrFilter orFilter = new OrFilter(infilter, notFilter);
        CustomResultSet result = (CustomResultSet) dbSearchBundle.search(orFilter);
        int siz = 0;

        while (result.next()) {
            siz++;
        }

        assertEquals("all items are live up to the infilter.", 9, siz);
    }
    /**
     * Test search the ldap with the EqualToFilter.
     * @throws Exception tp junit
     */
    public void testLDAPSearchEqualToFilter() throws Exception {
        Filter filter = new EqualToFilter("sn", "2");
        Iterator it = (Iterator) ldSearchBundle.search(filter);
        //1 person with the sn = "2"
        assertTrue("1 person under the filter", 1 == TestHelper.getItemNumber(it));
    }
    /**
     * Test search the ldap with the AndFilter.
     * @throws Exception to junit
     */
    public void testLDAPSearchAndFilter() throws Exception {
        Filter filter1 = new EqualToFilter("sn", "2");
        Filter filter2 = new EqualToFilter("sn", "1");
        Filter filter = new AndFilter(filter1, filter2);
        Iterator it = (Iterator) ldSearchBundle.search(filter);
        //0 person with the sn = "2" & "1"
        assertTrue("0 person under the filter", 0 == TestHelper.getItemNumber(it));
    }
    /**
     * Test search the ldap with the OrFilter.
     * @throws Exception to junit
     */
    public void testLDAPSearchOrFilter() throws Exception {
        Filter filter1 = new EqualToFilter("sn", "2");
        Filter filter2 = new EqualToFilter("sn", "1");
        Filter filter = new OrFilter(filter1, filter2);
        Iterator it = (Iterator) ldSearchBundle.search(filter);
        //2 person with the sn = "2" | "1"
        assertTrue("2 person under the filter", 2 == TestHelper.getItemNumber(it));
    }
    /**
     * Test search the ldap with the NotFilter.
     * @throws Exception to junit
     */
    public void testLDAPSearchNotFilter() throws Exception {
        Filter filter1 = new EqualToFilter("sn", "2");
        Filter filter2 = new EqualToFilter("sn", "1");
        Filter filter3 = new OrFilter(filter1, filter2);
        Filter filter = new NotFilter(filter3);
        Iterator it = (Iterator) ldSearchBundle.search(filter);
        //7 items with the sn != "2" | "1"
        assertTrue("7 items under the filter", 7 == TestHelper.getItemNumber(it));
    }
    /**
     * Test search the ldap with the InFilter.
     * @throws Exception to junit
     */
    public void testLDAPSearchInFilter() throws Exception {
        List values = new ArrayList();
        values.add("2");
        values.add("5");
        values.add("6");
        Filter filter = new InFilter("sn", values);
        Iterator it = (Iterator) ldSearchBundle.search(filter);
        //2 person with the sn in ("2", "5", "6")
        assertTrue("2 person under the filter", 2 == TestHelper.getItemNumber(it));
    }
    /**
     * Test search the ldap with the LessThanFilter.
     * @throws Exception to junit
     */
    public void testLDAPSearchLessThanFilter() throws Exception {
        Filter filter = new LessThanFilter("sb", "sb1");
        Iterator it = (Iterator) ldSearchBundle.search(filter);
        //no role with the searchbuilder lessthan "sb1"
        assertTrue("0 role under the filter", 0 == TestHelper.getItemNumber(it));
    }
    /**
     * Test search the ldap with the LessThanFilter.
     * @throws Exception to junit
     */
    public void testLDAPSearchLessThanOrEqualtoFilter() throws Exception {
        Filter filter = new LessThanOrEqualToFilter("sb", "sb1");
        Iterator it = (Iterator) ldSearchBundle.search(filter);
        //1 role with the searchbuilder lessthan "sb1"
        assertTrue("1 role under the filter", 1 == TestHelper.getItemNumber(it));
    }
    /**
     * Test search the ldap with the GreaterThanFilter.
     * @throws Exception to junit
     */
    public void testLDAPSearchGreatThanFilter() throws Exception {
        Filter filter = new GreaterThanFilter("sb", "sb2");
        Iterator it = (Iterator) ldSearchBundle.search(filter);
        //2 role with the searchbuilder name greater than "sb2"
        assertTrue("2 roles under the filter", 2 == TestHelper.getItemNumber(it));
    }
    /**
     * Test search the ldap with the GreaterThanFilter.
     * @throws Exception to junit
     */
    public void testLDAPSearchGreatThanOrEqualtoFilter() throws Exception {
        Filter filter = new GreaterThanOrEqualToFilter("sb", "sb2");
        Iterator it = (Iterator) ldSearchBundle.search(filter);
        //3 role with the searchbuilder name greater than "sb2"
        assertTrue("3 roles under the filter", 3 == TestHelper.getItemNumber(it));
    }
    /**
     * Test search the ldap with the BetweenFilter.
     * @throws Exception to junit
     */
    public void testLDAPSearchBetweenFilter() throws Exception {
        Filter filter = new BetweenFilter("sb", "sb3", "sb1");
        Iterator it = (Iterator) ldSearchBundle.search(filter);
        //3 role with the searchbuilder name between than "sb3" and "sb1"
        assertTrue("3 roles under the filter", 3 == TestHelper.getItemNumber(it));
    }
    /**
     * The failure test for setSearchStrategy,
     * fail for null,
     * IllegalArgumentException should be thrown.
     *
     */
    public void testsetSearchStrategy_failure() {
        try {
            ldSearchBundle.setSearchStrategy(null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            
        }
    }
    /**
     * The accuracy test for setSearchStrategy and getSearchStrategy.
     *
     */
    public void testsetSearchStrategy_getSearchStrategy() {
        ldSearchBundle.setSearchStrategy(strategy);
        assertEquals("The strategy should be the set one before.", strategy, ldSearchBundle.getSearchStrategy());
    }

}
