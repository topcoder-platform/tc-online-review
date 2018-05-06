/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.accuracytests;

import com.topcoder.db.connectionfactory.ConnectionProducer;
import com.topcoder.db.connectionfactory.producers.JDBCConnectionProducer;

import com.topcoder.search.builder.*;
import com.topcoder.search.builder.database.DatabaseSearchStrategy;
import com.topcoder.search.builder.filter.AndFilter;
import com.topcoder.search.builder.filter.EqualToFilter;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.filter.GreaterThanFilter;
import com.topcoder.search.builder.filter.InFilter;
import com.topcoder.search.builder.filter.NotFilter;
import com.topcoder.search.builder.filter.OrFilter;

import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.config.Property;
import com.topcoder.util.datavalidator.IntegerValidator;
import com.topcoder.util.sql.databaseabstraction.CustomResultSet;

import junit.framework.TestCase;

import java.util.*;
import java.io.File;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Connection;


/**
 * <p>
 * Accuracy test cases for ValidationResult.
 * </p>
 *
 * @author zjq, isv
 * @version 1.3
 */
public class SearchBundleAccuracyTests extends TestCase {
    /**
     * The UPP number.
     */
    private static final int UPP = 100;

    /**
     * The test SearchBundle name.
     */
    private static final String NAME = "SearchBundle";

    /**
     * The namespace to load the datebase configration.
     */
    private static final String DBNAMESPACE = "DataBase.Connection";
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
     * The instance of SearchBundle for test database.
     */
    private SearchBundle dbSearchBundle = null;

    /**
     * The instance of SearchBundle for test LDAP.
     */
    private SearchBundle ldSearchBundle = null;

    /**
     * <p>A <code>SearchStrategy</code> to be used for testing.</p>
     *
     * @since 1.3
     */
    private SearchStrategy searchStrategy = null;

    /**
     * <p>A <code>Connection</code> used by this test case for accesing the target database.</p>
     *
     * @since 1.3
     */
    private Connection connection = null;

    /**
     * <p>A <code>String</code> providing the context to be used for testing purposes.</p>
     *
     * @since 1.3
     */
    private String context = "SELECT * FROM people WHERE ";

    /**
     * setUp.
     *
     * @throws Exception to junit
     */
    protected void setUp() throws Exception {
        ConfigHelper.releaseNamespaces();
        ConfigHelper.loadConfiguration(new File("accuracy/config.xml"));

        //makeup the fields
        fields = new HashMap();
        fields.put("age", IntegerValidator.inRange(0, UPP));
        fields.put("weight", IntegerValidator.inRange(0, UPP));

        //makeup the aliasMap
        aliasMap = new HashMap();
        aliasMap.put("The age", "age");
        aliasMap.put("The weight", "weight");

        //makeup the ConnectionStrategy
        ConfigManager configManager = ConfigManager.getInstance();
        Property pro = configManager.getPropertyObject(DBNAMESPACE, "parameters");
        ConnectionProducer producer = new JDBCConnectionProducer(pro);

        this.connection = producer.createConnection();
        clearDatabaseTables();
        insertData();

        //makeup the dbbuilder
        this.searchStrategy = new DatabaseSearchStrategy("com.topcoder.search.builder.accuracytests.2");
        dbSearchBundle = new SearchBundle(NAME, fields, aliasMap, "SELECT * FROM people WHERE ");
        dbSearchBundle.setSearchStrategy(this.searchStrategy);
    }

    /**
     * tearDown.
     *
     * @throws Exception to junit
     */
    protected void tearDown() throws Exception {
        ConfigManager configManager = ConfigManager.getInstance();
        if (configManager.existsNamespace(DBNAMESPACE)) {
            configManager.removeNamespace(DBNAMESPACE);
        }
        ConfigHelper.releaseNamespaces();
    }
    /**
     * test construct.
     *
     */
    public void testconstruct1() {
        assertNotNull("can not construct the dbSearchBundle", dbSearchBundle);
    }

    /**
     * test construct with exception.
     *
     * @since 1.3
     */
    public void testconstruct2() {
        try {
            dbSearchBundle = new SearchBundle(NAME, fields, aliasMap, context);
        } catch (Exception e) {
            fail("No Exception should be throw");
        }
    }

    /**
     * test construct with exception.
     *
     * @since 1.3
     */
    public void testconstruct11() {
        try {
            dbSearchBundle = new SearchBundle(NAME, aliasMap, context);
        } catch (Exception e) {
            fail("No Exception should be throw");
        }
    }

    /**
     * test construct with exception.
     *
     * @since 1.3
     */
    public void testconstruct3() {
        try {
            dbSearchBundle = new SearchBundle(NAME, fields, aliasMap, context, searchStrategy);
        } catch (Exception e) {
            fail("No Exception should be throw");
        }
    }

    /**
     * test construct with exception.
     *
     * @since 1.3
     */
    public void testconstruct4() {
        try {
            dbSearchBundle = new SearchBundle(NAME, fields, context, searchStrategy);
        } catch (Exception e) {
            fail("No Exception should be throw");
        }
    }

    /**
     * test set/getSearchStrategy methods.
     *
     * @since 1.3
     */
    public void testSetGetSearchStrategy() {
        assertSame("The search strategy is not saved", this.searchStrategy, dbSearchBundle.getSearchStrategy());
    }

    /**
     * search from the table who's age = 10.
     *
     * @throws Exception to Junit
     */
    public void testsearch3() throws Exception {
        EqualToFilter equalToFilter = new EqualToFilter("The age", new Integer(10));

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
        EqualToFilter equalToFilter = new EqualToFilter("The age", new Integer(10));
        GreaterThanFilter greaterThanFilter = new GreaterThanFilter("The weight", new Integer(30));
        AndFilter andFilter = new AndFilter(equalToFilter, greaterThanFilter);

        //search depend the filter
        CustomResultSet result = (CustomResultSet) dbSearchBundle.search(andFilter);

        List list = new ArrayList();

        //get the result
        while (result.next()) {
            list.add(result.getString("peopleName"));
        }

        assertEquals("There are 1 prople with age = 10 and weight > 30", 1, list.size());
    }

    /**
     * test setSearchableFields success.
     *
     */
    public void testsetSearchableFields3() {
        try {
            Map map = new HashMap();
            map.put("test", IntegerValidator.greaterThan(1));
            dbSearchBundle.setSearchableFields(map);
        } catch (Exception e) {
            fail("no Exception should be throw");
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
     * test validateFilter(return invalid).
     *
     */
    public void testvalidateFilter2() {
        //101 out of rang 0-100
        Filter f1 = new EqualToFilter("The age", new Integer(UPP + 1));
        ValidationResult result = dbSearchBundle.validateFilter(f1);

        //invalid
        assertFalse("The validateFilter result is wrong", result.isValid());
    }

    /**
     * test the mothed getname().
     *
     */
    public void testgetname() {
        assertEquals("The get name mothed is wrong", "SearchBundle", dbSearchBundle.getName());
    }

    /**
     * test search with provide the return field.
     * @throws Exception to junit
     */
    public void testsearchwithlist3() throws Exception {
        //make up fields list
        List list = new ArrayList();
        list.add("peopleName");

        CustomResultSet result = (CustomResultSet) dbSearchBundle.search(new EqualToFilter("The age",
                                                                                           new Integer(10)), list);
        result.next();
        assertNotNull("The field 'peopleName' should be return", result.getString("peopleName"));
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
     * <p>Removes all data from the database tables which are affected by this test case.</p>
     *
     * @throws SQLException if an SQL error occurs.
     */
    private void clearDatabaseTables() throws SQLException {
       /* String[] tables = new String[] {"people"};

        PreparedStatement stmt = null;
        try {
            for (int i = 0; i < tables.length; i++) {
                stmt = this.connection.prepareStatement("DELETE FROM " + tables[i]);
                stmt.executeUpdate();
                stmt.close();
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }*/
    }

    /**
     * <p>Populates the database with sample data.</p>
     *
     * @throws SQLException if an SQL error occurs.
     */
    private void insertData() throws SQLException {
       /* Statement stmt = null;
        try {
            stmt = this.connection.createStatement();
            stmt.executeUpdate("INSERT INTO people (peopleID, peopleName, age, weight) VALUES (1, 'p1', 3, 10)");
            stmt.executeUpdate("INSERT INTO people (peopleID, peopleName, age, weight) VALUES (2, 'p2', 5, 15)");
            stmt.executeUpdate("INSERT INTO people (peopleID, peopleName, age, weight) VALUES (3, 'p3', 10, 31)");
            stmt.executeUpdate("INSERT INTO people (peopleID, peopleName, age, weight) VALUES (4, 'p4', 20, 60)");
            stmt.executeUpdate("INSERT INTO people (peopleID, peopleName, age, weight) VALUES (5, 'p5', 40, 65)");
            stmt.executeUpdate("INSERT INTO people (peopleID, peopleName, age, weight) VALUES (6, 'p6', 76, 55)");
            stmt.executeUpdate("INSERT INTO people (peopleID, peopleName, age, weight) VALUES (7, 'p7', 55, 70)");
            stmt.executeUpdate("INSERT INTO people (peopleID, peopleName, age, weight) VALUES (8, 'p8', 10, 27)");
            stmt.executeUpdate("INSERT INTO people (peopleID, peopleName, age, weight) VALUES (9, 'p9', 3, 12)");
            stmt.close();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }*/
    }
}
