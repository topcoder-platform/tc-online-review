/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.search.builder.SearchBuilderConfigurationException;
import com.topcoder.search.builder.SearchContext;
import com.topcoder.search.builder.TestHelper;
import com.topcoder.search.builder.UnrecognizedFilterException;
import com.topcoder.search.builder.filter.AndFilter;
import com.topcoder.search.builder.filter.BetweenFilter;
import com.topcoder.search.builder.filter.EqualToFilter;
import com.topcoder.search.builder.filter.GreaterThanFilter;
import com.topcoder.search.builder.filter.GreaterThanOrEqualToFilter;
import com.topcoder.search.builder.filter.InFilter;
import com.topcoder.search.builder.filter.LessThanFilter;
import com.topcoder.search.builder.filter.LessThanOrEqualToFilter;
import com.topcoder.search.builder.filter.LikeFilter;
import com.topcoder.search.builder.filter.NotFilter;
import com.topcoder.search.builder.filter.OrFilter;
import com.topcoder.search.builder.ldap.EqualsFragmentBuilder;
import com.topcoder.search.builder.ldap.LDAPSearchStrategy;
import com.topcoder.util.classassociations.ClassAssociator;
import com.topcoder.util.sql.databaseabstraction.CustomResultSet;

import junit.framework.TestCase;
/**
 * <p>
 * Unit test cases for DatabaseSearchStrategy.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.3
 */
public class DatabaseSearchStrategyTests extends TestCase {
    /**
     * The connection name.
     */
    private static final String CONNECTIONNAME = "MysqlJDBCConnection";
    /**
     * The namespace for DB.
     */
    private static final String NSDB = "com.topcoder.db.connectionfactory.DBConnectionFactoryImpl";
    /**
     * The context.
     */
    private static final String CONTEXT = "Select * from people where";
    /**
     * The namespace for DB.
     */
    private static final String NS = "DBSearchStrategy";
    /**
     * The DatabaseSearchStrategy instance to test.
     */
    private DatabaseSearchStrategy strategy = null;
    /**
     * The DBConnectionFactory instance.
     */
    private DBConnectionFactory connectionFactory;
    /**
     * The associations Map.
     */
    private Map associations = null;
    /**
     * The returnFields.
     */
    private List returnFields = null;
    /**
     * The map of alias name and real name.
     *
     */
    private Map aliasMap = null;
    /**
     * The ClassAssociator instance used to test.
     */
    private ClassAssociator fragmentBuilders;
    /**
     * The setUp of the unit test.
     *
     * @throws Exception to Junit
     */
    protected void setUp() throws Exception {
        //add the configuration
        TestHelper.clearConfig();
        TestHelper.addConfig();

        aliasMap = new HashMap();
        aliasMap.put("The age", "age");
        aliasMap.put("The weight", "weight");

        associations = new HashMap();

        associations.put(EqualToFilter.class, new EqualsFragmentBuilder());

        strategy = new DatabaseSearchStrategy(NS);

        returnFields = new ArrayList();
        returnFields.add("peopleID");
        connectionFactory = new DBConnectionFactoryImpl(NSDB);
    }
    /**
     * The tearDown of the unit test.
     *
     * @throws Exception to JUnit
     */
    protected void tearDown() throws Exception {
        //remove the configuration in tearDown
        TestHelper.clearConfig();
    }
    /**
     * The accuracy test of the constructor.
     *
     */
    public void testconstructor_accuracy1() {
        assertNotNull("can not construct the DatabaseSearchStrategy.", strategy);
    }
    /**
     * The accuracy test of the constructor.
     *
     * @throws Exception to JUnit
     */
    public void testconstructor_accuracy2() throws Exception {
        assertNotNull("can not construct the DatabaseSearchStrategy.",
                new DatabaseSearchStrategy(connectionFactory, CONNECTIONNAME, associations));
    }
    /**
     * The failure test of the constructor.
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testconstructor_failure1() throws Exception {
        try {
            new DatabaseSearchStrategy(null);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }
    /**
     * The failure test of the constructor.
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testconstructor_failure2() throws Exception {
        try {
            new DatabaseSearchStrategy("  ");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }
    /**
     * The failure test of the constructor.
     * namespace unknown,
     * SearchBuilderConfigurationException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testconstructor_failure3() throws Exception {
        try {
            new DatabaseSearchStrategy("not exists");
            fail("SearchBuilderConfigurationException should be thrown");
        } catch (SearchBuilderConfigurationException e) {
            //pass
        }
    }
    /**
     * The failure test of the constructor.
     * namespace unknown,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testconstructor_failure4() throws Exception {
        try {
            new DatabaseSearchStrategy(null, CONNECTIONNAME, associations);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }
    /**
     * The failure test of the constructor.
     * namespace unknown,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testconstructor_failure5() throws Exception {
        try {
            new DatabaseSearchStrategy(connectionFactory, CONNECTIONNAME, null);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }
    /**
     * The failure test of the constructor.
     * namespace unknown,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testconstructor_failure6() throws Exception {
        associations.put(null, new EqualsFragmentBuilder());
        try {
            new DatabaseSearchStrategy(connectionFactory, CONNECTIONNAME, associations);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }
    /**
     * The failure test of the constructor.
     * namespace unknown,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testconstructor_failure7() throws Exception {
        associations.put(new Object(), new EqualsFragmentBuilder());
        try {
            new DatabaseSearchStrategy(connectionFactory, CONNECTIONNAME, associations);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }
    /**
     * The failure test of the constructor.
     * namespace unknown,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testconstructor_failure8() throws Exception {
        associations.put(EqualToFilter.class, null);
        try {
            new DatabaseSearchStrategy(connectionFactory, CONNECTIONNAME, associations);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }
    /**
     * The failure test of the constructor.
     * namespace unknown,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testconstructor_failure9() throws Exception {
        associations.put(new Object(), new EqualsFragmentBuilder());
        try {
            new DatabaseSearchStrategy(connectionFactory, CONNECTIONNAME, associations);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }
    /**
     * The failure test of the constructor.
     * namespace unknown,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testconstructor_failure10() throws Exception {
        associations.put(EqualToFilter.class, new Object());
        try {
            new DatabaseSearchStrategy(connectionFactory, CONNECTIONNAME, associations);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }
    /**
     * The failure test of the constructor.
     * namespace unknown,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testconstructor_failure11() throws Exception {
        associations.put(Object.class, new EqualsFragmentBuilder());
        try {
            new DatabaseSearchStrategy(connectionFactory, CONNECTIONNAME, associations);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }
    /**
     * Test the method SearchContext.
     *
     * @throws Exception to JUnit
     */
    public void testbuildSearchContext() throws Exception {
        SearchContext searchContext = strategy.buildSearchContext(CONTEXT, new EqualToFilter("age", new Integer(10)),
                new ArrayList(), aliasMap);

        assertEquals("The searchString should be same.",
                "Select * from people where age = ?", searchContext.getSearchString().toString());
    }
    /**
     * Test failure test of the method SearchContext,
     * UnrecognizedFilterException will be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testbuildSearchContext_failure() throws Exception {
        strategy = new DatabaseSearchStrategy(connectionFactory, CONNECTIONNAME, associations);
        try {
            strategy.buildSearchContext(CONTEXT, new LessThanFilter("age", new Integer(100)),
                new ArrayList(), aliasMap);
            fail("UnrecognizedFilterException should be thrown.");
        } catch (UnrecognizedFilterException e) {
            //pass
        }
    }
    /**
     * search from the table who's age = 10.
     *
     * @throws Exception to Junit
     */
    public void testsearch_accuracy1() throws Exception {
        EqualToFilter equalToFilter = new EqualToFilter("The age",
                new Integer(10));

        //search depend the filter
        CustomResultSet result = (CustomResultSet) strategy.search(CONTEXT, equalToFilter, new ArrayList(), aliasMap);

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
    public void testsearch_accuracy2() throws Exception {
        EqualToFilter equalToFilter = new EqualToFilter("The age",
                new Integer(10));
        GreaterThanFilter greaterThanFilter = new GreaterThanFilter("The weight",
                new Integer(30));
        AndFilter andFilter = new AndFilter(equalToFilter, greaterThanFilter);

        //search depend the filter
        CustomResultSet result = (CustomResultSet) strategy.search(CONTEXT, andFilter, new ArrayList(), aliasMap);

        List list = new ArrayList();

        //get the result
        while (result.next()) {
            list.add(result.getString("peopleName"));
        }

        assertEquals("There are 1 prople with age = 10 and weight > 30", 1,
            list.size());
    }
    /**
     * search from the table who's age = 10 or weight > 30.
     *
     * @throws Exception to Junit
     */
    public void testsearch_accuracy3() throws Exception {
        EqualToFilter equalToFilter = new EqualToFilter("The age",
                new Integer(10));
        GreaterThanFilter greaterThanFilter = new GreaterThanFilter("The weight",
                new Integer(30));
        OrFilter orFilter = new OrFilter(equalToFilter, greaterThanFilter);

        //search depend the filter
        CustomResultSet result = (CustomResultSet) strategy.search(CONTEXT, orFilter, new ArrayList(), aliasMap);

        List list = new ArrayList();

        //get the result
        while (result.next()) {
            list.add(result.getString("peopleName"));
        }

        assertEquals("There are 6 prople with age = 10 or weight > 30", 6,
            list.size());
    }
    /**
     * search from the table who's age = 10 or weight > 30.
     *
     * @throws Exception to Junit
     */
    public void testsearch_accuracy4() throws Exception {
        EqualToFilter equalToFilter = new EqualToFilter("The age",
                new Integer(10));
        GreaterThanFilter greaterThanFilter = new GreaterThanFilter("The weight",
                new Integer(30));
        OrFilter orFilter = new OrFilter(equalToFilter, greaterThanFilter);

        NotFilter filter = new NotFilter(orFilter);
        //search depend the filter
        CustomResultSet result = (CustomResultSet) strategy.search(CONTEXT, filter, new ArrayList(), aliasMap);

        List list = new ArrayList();

        //get the result
        while (result.next()) {
            list.add(result.getString("peopleName"));
        }

        assertEquals("There are 3 prople with !(age = 10 or weight > 30)", 3,
            list.size());
    }
    /**
     * search from the table who's weight < 31.
     *
     * @throws Exception to Junit
     */
    public void testsearch_accuracy5() throws Exception {
        LessThanFilter lessThanFilter = new LessThanFilter("The weight",
                new Integer(31));

        //search depend the filter
        CustomResultSet result = (CustomResultSet) strategy.search(CONTEXT, lessThanFilter, new ArrayList(), aliasMap);

        List list = new ArrayList();

        //get the result
        while (result.next()) {
            list.add(result.getString("peopleName"));
        }

        assertEquals("There are 4 prople with weight < 31", 4,
            list.size());
    }
    /**
     * search from the table who's weight <= 31.
     *
     * @throws Exception to Junit
     */
    public void testsearch_accuracy6() throws Exception {
        LessThanOrEqualToFilter lessThanFilter = new LessThanOrEqualToFilter("The weight",
                new Integer(31));

        //search depend the filter
        CustomResultSet result = (CustomResultSet) strategy.search(CONTEXT, lessThanFilter, new ArrayList(), aliasMap);

        List list = new ArrayList();

        //get the result
        while (result.next()) {
            list.add(result.getString("peopleName"));
        }

        assertEquals("There are 5 prople with weight < 31", 5,
            list.size());
    }
    /**
     * search from the table who's weight >= 31.
     *
     * @throws Exception to Junit
     */
    public void testsearch_accuracy7() throws Exception {
        GreaterThanOrEqualToFilter filter = new GreaterThanOrEqualToFilter("The weight",
                new Integer(31));

        //search depend the filter
        CustomResultSet result = (CustomResultSet) strategy.search(CONTEXT, filter, new ArrayList(), aliasMap);

        List list = new ArrayList();

        //get the result
        while (result.next()) {
            list.add(result.getString("peopleName"));
        }

        assertEquals("There are 5 prople with weight < 31", 5,
            list.size());
    }
    /**
     * search from the table who's name in 'p1' or 'p9'.
     *
     * @throws Exception to Junit
     */
    public void testsearch_accuracy8() throws Exception {
        List values = new ArrayList();
        values.add("p1");
        values.add("p9");
        InFilter filter = new InFilter("peopleName",
                values);

        //search depend the filter
        CustomResultSet result = (CustomResultSet) strategy.search(CONTEXT, filter, new ArrayList(), aliasMap);

        List list = new ArrayList();

        //get the result
        while (result.next()) {
            list.add(result.getString("peopleName"));
        }

        assertEquals("There are 2 prople with Infilter", 2,
            list.size());
    }

    /**
     * search from the table who's name BetweenFilter in 'p1' or 'p9'.
     *
     * @throws Exception to Junit
     */
    public void testsearch_accuracy9() throws Exception {
        BetweenFilter filter = new BetweenFilter("peopleName",
                "p9", "p1");

        //search depend the filter
        CustomResultSet result = (CustomResultSet) strategy.search(CONTEXT, filter, new ArrayList(), aliasMap);

        List list = new ArrayList();

        //get the result
        while (result.next()) {
            list.add(result.getString("peopleName"));
        }

        assertEquals("There are 9 prople with BetweenFilter",9,
            list.size());
    }
    /**
     * search from the table who's name like p%.
     *
     * @throws Exception to Junit
     */
    public void testsearch_accuracy10() throws Exception {
        LikeFilter filter = new LikeFilter("peopleName",
                "SW:p", '!');

        //search depend the filter
        CustomResultSet result = (CustomResultSet) strategy.search(CONTEXT, filter, new ArrayList(), aliasMap);

        List list = new ArrayList();

        //get the result
        while (result.next()) {
            list.add(result.getString("peopleName"));
        }

        assertEquals("There are 9 prople with BetweenFilter",9,
            list.size());
    }
    /**
     * The failure test of search,
     * since the parameters are invalid,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to Junit
     */
    public void testsearch_failure1() throws Exception {
        EqualToFilter filter = new EqualToFilter("sn", "2");
        try {
            strategy.search(null, filter, returnFields, aliasMap);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }
    /**
     * The failure test of search,
     * since the parameters are invalid,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to Junit
     */
    public void testsearch_failure2() throws Exception {
        EqualToFilter filter = new EqualToFilter("sn", "2");
        try {
            strategy.search("  ", filter, returnFields, aliasMap);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }
    /**
     * The failure test of search,
     * since the parameters are invalid,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to Junit
     */
    public void testsearch_failure3() throws Exception {
        EqualToFilter filter = new EqualToFilter("sn", "2");
        try {
            strategy.search(CONTEXT, null, returnFields, aliasMap);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }
    /**
     * The failure test of search,
     * since the parameters are invalid,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to Junit
     */
    public void testsearch_failure4() throws Exception {
        EqualToFilter filter = new EqualToFilter("sn", "2");
        try {
            strategy.search(CONTEXT, filter, null, aliasMap);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }
    /**
     * The failure test of search,
     * since the parameters are invalid,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to Junit
     */
    public void testsearch_failure5() throws Exception {
        EqualToFilter filter = new EqualToFilter("sn", "2");
        returnFields.add(null);
        try {
            strategy.search(CONTEXT, filter, returnFields, aliasMap);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }
    /**
     * The failure test of search,
     * since the parameters are invalid,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to Junit
     */
    public void testsearch_failure6() throws Exception {
        EqualToFilter filter = new EqualToFilter("sn", "2");
        returnFields.add("  ");
        try {
            strategy.search(CONTEXT, filter, returnFields, aliasMap);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }
    /**
     * The failure test of search,
     * since the parameters are invalid,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to Junit
     */
    public void testsearch_failure7() throws Exception {
        EqualToFilter filter = new EqualToFilter("sn", "2");
        try {
            strategy.search(CONTEXT, filter, returnFields, null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }
    /**
     * The failure test of search,
     * since the parameters are invalid,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to Junit
     */
    public void testsearch_failure8() throws Exception {
        EqualToFilter filter = new EqualToFilter("sn", "2");
        aliasMap.put(null, "aa");
        try {
            strategy.search(CONTEXT, filter, returnFields, aliasMap);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }
    /**
     * The failure test of search,
     * since the parameters are invalid,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to Junit
     */
    public void testsearch_failure9() throws Exception {
        EqualToFilter filter = new EqualToFilter("sn", "2");
        aliasMap.put("  ", "aa");
        try {
            strategy.search(CONTEXT, filter, returnFields, aliasMap);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }
    /**
     * The failure test of search,
     * since the parameters are invalid,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to Junit
     */
    public void testsearch_failure10() throws Exception {
        EqualToFilter filter = new EqualToFilter("sn", "2");
        aliasMap.put(new Object(), "aa");
        try {
            strategy.search(CONTEXT, filter, returnFields, aliasMap);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }
    /**
     * The failure test of search,
     * since the parameters are invalid,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to Junit
     */
    public void testsearch_failure11() throws Exception {
        EqualToFilter filter = new EqualToFilter("sn", "2");
        aliasMap.put("aa", null);
        try {
            strategy.search(CONTEXT, filter, returnFields, aliasMap);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }
    /**
     * The failure test of search,
     * since the parameters are invalid,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to Junit
     */
    public void testsearch_failure12() throws Exception {
        EqualToFilter filter = new EqualToFilter("sn", "2");
        aliasMap.put("aa", "  ");
        try {
            strategy.search(CONTEXT, filter, returnFields, aliasMap);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }
    /**
     * The failure test of search,
     * since the parameters are invalid,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to Junit
     */
    public void testsearch_failure13() throws Exception {
        EqualToFilter filter = new EqualToFilter("sn", "2");
        aliasMap.put("aa", new Object());
        try {
            strategy.search(CONTEXT, filter, returnFields, aliasMap);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }
}
