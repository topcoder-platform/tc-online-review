/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.accuracytests.database;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Assert;
import com.topcoder.search.builder.database.DatabaseSearchStrategy;
import com.topcoder.search.builder.accuracytests.ConfigHelper;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.util.sql.databaseabstraction.CustomResultSet;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;

/**
 * <p>An accuracy test for {@link com.topcoder.search.builder.database.DatabaseSearchStrategy} class. Tests the methods for proper
 * handling of valid input data and producing accurate results. Passes the valid arguments to the
 * methods and verifies that either the state of the tested instance have been changed appropriately
 * or a correct result is produced by the method.</p>
 *
 * @author isv
 * @version 1.0
 * @since 1.3
 */
public class DatabaseSearchStrategyAccuracyTest extends TestCase {

    /**
     * <p>The instances of {@link DatabaseSearchStrategy} which are tested. These instances are initialized in
     * {@link #setUp()} method and released in {@link #tearDown()} method. Each instance is initialized
     * using a separate constructor provided by the tested class.<p>
     */
    private DatabaseSearchStrategy[] testedInstances = null;

    /**
     * <p>A <code>Connection</code> used by this test case for accesing the target database.</p>
     */
    private Connection connection = null;

    /**
     * <p>Gets the test suite for {@link DatabaseSearchStrategy} class.</p>
     *
     * @return a <code>TestSuite</code> providing the tests for {@link DatabaseSearchStrategy} class.
     */
    public static Test suite() {
        return new TestSuite(DatabaseSearchStrategyAccuracyTest.class);
    }

    /**
     * <p>Sets up the fixture. This method is called before a test is executed.</p>
     *
     * @throws Exception if any error occurs.
     */
    protected void setUp() throws Exception {
        super.setUp();

        ConfigHelper.releaseNamespaces();
        ConfigHelper.loadConfiguration(new File("accuracy/config.xml"));

        DBConnectionFactory connectionFactory
            = new DBConnectionFactoryImpl("com.topcoder.db.connectionfactory.DBConnectionFactoryImpl");
        this.connection = connectionFactory.createConnection("accuracy");
        clearDatabaseTables();
        insertData();

        this.testedInstances = new DatabaseSearchStrategy[3];
        this.testedInstances[0] = new DatabaseSearchStrategy(connectionFactory, "accuracy",
                                                             TestDataFactory.getClassAssociattions());
        this.testedInstances[1] = new DatabaseSearchStrategy("com.topcoder.search.builder.accuracytests.1");
        this.testedInstances[2] = new DatabaseSearchStrategy("com.topcoder.search.builder.accuracytests.2");
    }

    /**
     * <p>Tears down the fixture. This method is called after a test is executed.</p>
     *
     * @throws Exception if any error occurs.
     */
    protected void tearDown() throws Exception {
        this.testedInstances = null;
        this.connection.close();
        ConfigHelper.releaseNamespaces();
        super.tearDown();
    }

    /**
     * <p>Accuracy test. Tests the {@link DatabaseSearchStrategy#search(String, Filter, List, Map)} method for proper
     * behavior.</p>
     *
     * <p>Verifies that the method queries the database tables correctly based on provided filter, content, alias
     * mapping.</p>
     *
     * @throws Exception if an unexpected error occurs. Such an error must be interpreted as test failure.
     */
    /*public void testSearch_String_Filter_List_Map_BetweenFilter_NoAlias_EmptyReturnList() throws Exception {
        for (int i = 0; i < this.testedInstances.length; i++) {
            System.out.println("i: " + i);
            CustomResultSet rs
                = (CustomResultSet) this.testedInstances[i].search("SELECT attr1 FROM accuracytest WHERE ",
                                                                   TestDataFactory.getBetweenFilterWithoutAlias(),
                                                                   new ArrayList(),
                                                                   TestDataFactory.getSearchContextAliasMap());
            Assert.assertTrue("The requested column is not retrieved", rs.findColumn("attr1") > 0);
            Assert.assertFalse("The non-requested column is not retrieved", rs.findColumn("attr2") > 0);
            Assert.assertFalse("The non-requested column is not retrieved", rs.findColumn("attr3") > 0);
            Assert.assertFalse("The non-requested column is not retrieved", rs.findColumn("attr4") > 0);
            Assert.assertFalse("The non-requested column is not retrieved", rs.findColumn("attr5") > 0);
            Assert.assertFalse("The non-requested column is not retrieved", rs.findColumn("attr6") > 0);

            Set values = new HashSet();
            while (rs.next()) {
                values.add(rs.getString("attr1"));
            }

            Assert.assertEquals("Incorrect number of rows is returned", 3, values.size());
            Assert.assertTrue("The matching row is not returned", values.contains("1"));
            Assert.assertTrue("The matching row is not returned", values.contains("11"));
            Assert.assertTrue("The matching row is not returned", values.contains("111"));
        }
    }*/

    /**
     * <p>Accuracy test. Tests the {@link DatabaseSearchStrategy#search(String, Filter, List, Map)} method for proper
     * behavior.</p>
     *
     * <p>Verifies that the method queries the database tables correctly based on provided filter, content, alias
     * mapping.</p>
     *
     * @throws Exception if an unexpected error occurs. Such an error must be interpreted as test failure.
     */
    /*public void testSearch_String_Filter_List_Map_BetweenFilter_Alias_EmptyReturnList() throws Exception {
        for (int i = 0; i < this.testedInstances.length; i++) {
            CustomResultSet rs
                = (CustomResultSet) this.testedInstances[i].search("SELECT attr1 FROM accuracytest WHERE ",
                                                                   TestDataFactory.getBetweenFilterWithAlias(),
                                                                   new ArrayList(),
                                                                   TestDataFactory.getSearchContextAliasMap());
            Assert.assertTrue("The requested column is not retrieved", rs.findColumn("attr1") > 0);
            Assert.assertFalse("The non-requested column is not retrieved", rs.findColumn("attr2") > 0);
            Assert.assertFalse("The non-requested column is not retrieved", rs.findColumn("attr3") > 0);
            Assert.assertFalse("The non-requested column is not retrieved", rs.findColumn("attr4") > 0);
            Assert.assertFalse("The non-requested column is not retrieved", rs.findColumn("attr5") > 0);
            Assert.assertFalse("The non-requested column is not retrieved", rs.findColumn("attr6") > 0);

            Set values = new HashSet();
            while (rs.next()) {
                values.add(rs.getString("attr1"));
            }

            Assert.assertEquals("Incorrect number of rows is returned", 1, values.size());
            Assert.assertTrue("The matching row is not returned", values.contains("-"));
        }
    }*/

    /**
     * <p>Accuracy test. Tests the {@link DatabaseSearchStrategy#search(String, Filter, List, Map)} method for proper
     * behavior.</p>
     *
     * <p>Verifies that the method queries the database tables correctly based on provided filter, content, alias
     * mapping.</p>
     *
     * @throws Exception if an unexpected error occurs. Such an error must be interpreted as test failure.
     */
    public void testSearch_String_Filter_List_Map_BetweenFilter_NoAlias_NonEmptyReturnList() throws Exception {
        for (int i = 0; i < this.testedInstances.length; i++) {
            if (i ==1) {
                continue;
            }
            CustomResultSet rs
                = (CustomResultSet) this.testedInstances[i].search("SELECT attr1 FROM accuracytest WHERE ",
                                                                   TestDataFactory.getBetweenFilterWithoutAlias(),
                                                                   Arrays.asList(new String[] {"attr2", "attr3"}),
                                                                   TestDataFactory.getSearchContextAliasMap());
            Assert.assertTrue("The requested column is not retrieved", rs.findColumn("attr1") > 0);
            Assert.assertTrue("The requested column is not retrieved", rs.findColumn("attr2") > 0);
            Assert.assertTrue("The requested column is not retrieved", rs.findColumn("attr3") > 0);
            Assert.assertFalse("The non-requested column is not retrieved", rs.findColumn("attr4") > 0);
            Assert.assertFalse("The non-requested column is not retrieved", rs.findColumn("attr5") > 0);
            Assert.assertFalse("The non-requested column is not retrieved", rs.findColumn("attr6") > 0);

            List values1 = new ArrayList();
            List values2 = new ArrayList();
            List values3 = new ArrayList();
            while (rs.next()) {
                values1.add(rs.getString("attr1"));
                values2.add(rs.getString("attr2"));
                values3.add(rs.getString("attr3"));
            }

            Assert.assertEquals("Incorrect number of rows is returned", 3, values1.size());

            Assert.assertTrue("The matching row is not returned", values1.contains("1"));
            Assert.assertTrue("The matching row is not returned", values1.contains("11"));
            Assert.assertTrue("The matching row is not returned", values1.contains("111"));
            Assert.assertTrue("The matching row is not returned", values2.contains("2"));
            Assert.assertTrue("The matching row is not returned", values2.contains("22"));
            Assert.assertTrue("The matching row is not returned", values2.contains("222"));
            Assert.assertTrue("The matching row is not returned", values3.contains("3"));
            Assert.assertTrue("The matching row is not returned", values3.contains("33"));
            Assert.assertTrue("The matching row is not returned", values3.contains("333"));

            int index;
            index = values1.indexOf("1");
            Assert.assertEquals("Incorrect value for the row is returned", "2", values2.get(index));
            Assert.assertEquals("Incorrect value for the row is returned", "3", values3.get(index));
            index = values1.indexOf("11");
            Assert.assertEquals("Incorrect value for the row is returned", "22", values2.get(index));
            Assert.assertEquals("Incorrect value for the row is returned", "33", values3.get(index));
            index = values1.indexOf("111");
            Assert.assertEquals("Incorrect value for the row is returned", "222", values2.get(index));
            Assert.assertEquals("Incorrect value for the row is returned", "333", values3.get(index));
        }
    }

    /**
     * <p>Accuracy test. Tests the {@link DatabaseSearchStrategy#search(String, Filter, List, Map)} method for proper
     * behavior.</p>
     *
     * <p>Verifies that the method queries the database tables correctly based on provided filter, content, alias
     * mapping.</p>
     *
     * @throws Exception if an unexpected error occurs. Such an error must be interpreted as test failure.
     */
    public void testSearch_String_Filter_List_Map_BetweenFilter_Alias_NonEmptyReturnList() throws Exception {
        for (int i = 0; i < this.testedInstances.length; i++) {
            if (i ==1) {
                continue;
            }
            CustomResultSet rs
                = (CustomResultSet) this.testedInstances[i].search("SELECT attr1 FROM accuracytest WHERE ",
                                                                   TestDataFactory.getBetweenFilterWithAlias(),
                                                                   Arrays.asList(new String[] {"attr4", "attr5"}),
                                                                   TestDataFactory.getSearchContextAliasMap());
            Assert.assertTrue("The requested column is not retrieved", rs.findColumn("attr1") > 0);
            Assert.assertTrue("The requested column is not retrieved", rs.findColumn("attr4") > 0);
            Assert.assertTrue("The requested column is not retrieved", rs.findColumn("attr5") > 0);
            Assert.assertFalse("The non-requested column is not retrieved", rs.findColumn("attr2") > 0);
            Assert.assertFalse("The non-requested column is not retrieved", rs.findColumn("attr3") > 0);
            Assert.assertFalse("The non-requested column is not retrieved", rs.findColumn("attr6") > 0);

            List values1 = new ArrayList();
            List values4 = new ArrayList();
            List values5 = new ArrayList();
            while (rs.next()) {
                values1.add(rs.getString("attr1"));
                values4.add(rs.getString("attr4"));
                values5.add(rs.getString("attr5"));
            }

            Assert.assertEquals("Incorrect number of rows is returned", 1, values1.size());

            Assert.assertTrue("The matching row is not returned", values1.contains("-"));
            Assert.assertTrue("The matching row is not returned", values4.contains("d"));
            Assert.assertTrue("The matching row is not returned", values5.contains("8"));
        }
    }

    /**
     * <p>Accuracy test. Tests the {@link DatabaseSearchStrategy#search(String, Filter, List, Map)} method for proper
     * behavior.</p>
     *
     * <p>Verifies that the method queries the database tables correctly based on provided filter, content, alias
     * mapping.</p>
     *
     * @throws Exception if an unexpected error occurs. Such an error must be interpreted as test failure.
     */
    public void testSearch_String_Filter_List_Map_BetweenFilter_NoAlias_AllColumns() throws Exception {
        for (int i = 0; i < this.testedInstances.length; i++) {
            if (i == 1) {
                continue;
            }
            CustomResultSet rs
                = (CustomResultSet) this.testedInstances[i].search("SELECT * FROM accuracytest WHERE ",
                                                                   TestDataFactory.getBetweenFilterWithoutAlias(),
                                                                   new ArrayList(),
                                                                   TestDataFactory.getSearchContextAliasMap());
            Assert.assertTrue("The requested column is not retrieved", rs.findColumn("attr1") > 0);
            Assert.assertTrue("The requested column is not retrieved", rs.findColumn("attr2") > 0);
            Assert.assertTrue("The requested column is not retrieved", rs.findColumn("attr3") > 0);
            Assert.assertTrue("The requested column is not retrieved", rs.findColumn("attr4") > 0);
            Assert.assertTrue("The requested column is not retrieved", rs.findColumn("attr5") > 0);
            Assert.assertTrue("The requested column is not retrieved", rs.findColumn("attr6") > 0);

            List values1 = new ArrayList();
            List values2 = new ArrayList();
            List values3 = new ArrayList();
            List values4 = new ArrayList();
            List values5 = new ArrayList();
            List values6 = new ArrayList();
            while (rs.next()) {
                values1.add(rs.getString("attr1"));
                values2.add(rs.getString("attr2"));
                values3.add(rs.getString("attr3"));
                values4.add(rs.getString("attr4"));
                values5.add(rs.getString("attr5"));
                values6.add(rs.getString("attr6"));
            }

            Assert.assertEquals("Incorrect number of rows is returned", 3, values1.size());
            Assert.assertTrue("The matching row is not returned", values1.contains("1"));
            Assert.assertTrue("The matching row is not returned", values1.contains("11"));
            Assert.assertTrue("The matching row is not returned", values1.contains("111"));

            int index;
            index = values1.indexOf("1");
            Assert.assertEquals("Incorrect value for the row is returned", "2", values2.get(index));
            Assert.assertEquals("Incorrect value for the row is returned", "3", values3.get(index));
            Assert.assertEquals("Incorrect value for the row is returned", "4", values4.get(index));
            Assert.assertEquals("Incorrect value for the row is returned", "5", values5.get(index));
            Assert.assertEquals("Incorrect value for the row is returned", "6", values6.get(index));
            index = values1.indexOf("11");
            Assert.assertEquals("Incorrect value for the row is returned", "22", values2.get(index));
            Assert.assertEquals("Incorrect value for the row is returned", "33", values3.get(index));
            Assert.assertEquals("Incorrect value for the row is returned", "44", values4.get(index));
            Assert.assertEquals("Incorrect value for the row is returned", "55", values5.get(index));
            Assert.assertEquals("Incorrect value for the row is returned", "66", values6.get(index));
            index = values1.indexOf("111");
            Assert.assertEquals("Incorrect value for the row is returned", "222", values2.get(index));
            Assert.assertEquals("Incorrect value for the row is returned", "333", values3.get(index));
            Assert.assertEquals("Incorrect value for the row is returned", "444", values4.get(index));
            Assert.assertEquals("Incorrect value for the row is returned", "555", values5.get(index));
            Assert.assertEquals("Incorrect value for the row is returned", "666", values6.get(index));
        }
    }

    /**
     * <p>Accuracy test. Tests the {@link DatabaseSearchStrategy#search(String, Filter, List, Map)} method for proper
     * behavior.</p>
     *
     * <p>Verifies that the method queries the database tables correctly based on provided filter, content, alias
     * mapping.</p>
     *
     * @throws Exception if an unexpected error occurs. Such an error must be interpreted as test failure.
     */
    public void testSearch_String_Filter_List_Map_BetweenFilter_Alias_AllColumns() throws Exception {
        for (int i = 0; i < this.testedInstances.length; i++) {
            if (i == 1) {
                continue;
            }
            CustomResultSet rs
                = (CustomResultSet) this.testedInstances[i].search("SELECT * FROM accuracytest WHERE ",
                                                                   TestDataFactory.getBetweenFilterWithAlias(),
                                                                   new ArrayList(),
                                                                   TestDataFactory.getSearchContextAliasMap());
            Assert.assertTrue("The requested column is not retrieved", rs.findColumn("attr1") > 0);
            Assert.assertTrue("The requested column is not retrieved", rs.findColumn("attr2") > 0);
            Assert.assertTrue("The requested column is not retrieved", rs.findColumn("attr3") > 0);
            Assert.assertTrue("The requested column is not retrieved", rs.findColumn("attr4") > 0);
            Assert.assertTrue("The requested column is not retrieved", rs.findColumn("attr5") > 0);
            Assert.assertTrue("The requested column is not retrieved", rs.findColumn("attr6") > 0);

            List values1 = new ArrayList();
            List values2 = new ArrayList();
            List values3 = new ArrayList();
            List values4 = new ArrayList();
            List values5 = new ArrayList();
            List values6 = new ArrayList();
            while (rs.next()) {
                values1.add(rs.getString("attr1"));
                values2.add(rs.getString("attr2"));
                values3.add(rs.getString("attr3"));
                values4.add(rs.getString("attr4"));
                values5.add(rs.getString("attr5"));
                values6.add(rs.getString("attr6"));
            }

            Assert.assertEquals("Incorrect number of rows is returned", 1, values1.size());
            int index = values1.indexOf("-");
            Assert.assertEquals("Incorrect value for the row is returned", "4", values2.get(index));
            Assert.assertEquals("Incorrect value for the row is returned", "c", values3.get(index));
            Assert.assertEquals("Incorrect value for the row is returned", "d", values4.get(index));
            Assert.assertEquals("Incorrect value for the row is returned", "8", values5.get(index));
            Assert.assertEquals("Incorrect value for the row is returned", "9", values6.get(index));
        }
    }

    /**
     * <p>Accuracy test. Tests the {@link DatabaseSearchStrategy#search(String, Filter, List, Map)} method for proper
     * behavior.</p>
     *
     * <p>Verifies that the method queries the database tables correctly based on provided filter, content, alias
     * mapping.</p>
     *
     * @throws Exception if an unexpected error occurs. Such an error must be interpreted as test failure.
     */
    public void testSearch_String_Filter_List_Map_BetweenFilter_NoAlias_AllColumns_NonEmptyReturnList()
        throws Exception {
        for (int i = 0; i < this.testedInstances.length; i++) {
            if (i ==1) {
                continue;
            }
            CustomResultSet rs
                = (CustomResultSet) this.testedInstances[i].search("SELECT * FROM accuracytest WHERE ",
                                                                   TestDataFactory.getBetweenFilterWithoutAlias(),
                                                                   Arrays.asList(new String[] {"attr2", "attr3"}),
                                                                   TestDataFactory.getSearchContextAliasMap());
            Assert.assertTrue("The requested column is not retrieved", rs.findColumn("attr1") > 0);
            Assert.assertTrue("The requested column is not retrieved", rs.findColumn("attr2") > 0);
            Assert.assertTrue("The requested column is not retrieved", rs.findColumn("attr3") > 0);
            Assert.assertTrue("The requested column is not retrieved", rs.findColumn("attr4") > 0);
            Assert.assertTrue("The requested column is not retrieved", rs.findColumn("attr5") > 0);
            Assert.assertTrue("The requested column is not retrieved", rs.findColumn("attr6") > 0);

            List values1 = new ArrayList();
            List values2 = new ArrayList();
            List values3 = new ArrayList();
            List values4 = new ArrayList();
            List values5 = new ArrayList();
            List values6 = new ArrayList();
            while (rs.next()) {
                values1.add(rs.getString("attr1"));
                values2.add(rs.getString("attr2"));
                values3.add(rs.getString("attr3"));
                values4.add(rs.getString("attr4"));
                values5.add(rs.getString("attr5"));
                values6.add(rs.getString("attr6"));
            }

            Assert.assertEquals("Incorrect number of rows is returned", 3, values1.size());

            int index;
            index = values1.indexOf("1");
            Assert.assertEquals("Incorrect value for the row is returned", "2", values2.get(index));
            Assert.assertEquals("Incorrect value for the row is returned", "3", values3.get(index));
            Assert.assertEquals("Incorrect value for the row is returned", "4", values4.get(index));
            Assert.assertEquals("Incorrect value for the row is returned", "5", values5.get(index));
            Assert.assertEquals("Incorrect value for the row is returned", "6", values6.get(index));
            index = values1.indexOf("11");
            Assert.assertEquals("Incorrect value for the row is returned", "22", values2.get(index));
            Assert.assertEquals("Incorrect value for the row is returned", "33", values3.get(index));
            Assert.assertEquals("Incorrect value for the row is returned", "44", values4.get(index));
            Assert.assertEquals("Incorrect value for the row is returned", "55", values5.get(index));
            Assert.assertEquals("Incorrect value for the row is returned", "66", values6.get(index));
            index = values1.indexOf("111");
            Assert.assertEquals("Incorrect value for the row is returned", "222", values2.get(index));
            Assert.assertEquals("Incorrect value for the row is returned", "333", values3.get(index));
            Assert.assertEquals("Incorrect value for the row is returned", "444", values4.get(index));
            Assert.assertEquals("Incorrect value for the row is returned", "555", values5.get(index));
            Assert.assertEquals("Incorrect value for the row is returned", "666", values6.get(index));
        }
    }

    /**
     * <p>Accuracy test. Tests the {@link DatabaseSearchStrategy#search(String, Filter, List, Map)} method for proper
     * behavior.</p>
     *
     * <p>Verifies that the method queries the database tables correctly based on provided filter, content, alias
     * mapping.</p>
     *
     * @throws Exception if an unexpected error occurs. Such an error must be interpreted as test failure.
     */
    public void testSearch_String_Filter_List_Map_BetweenFilter_Alias_AllColumns_NonEmptyReturnList() throws Exception {
        for (int i = 0; i < this.testedInstances.length; i++) {
            if (i ==1) {
                continue;
            }
            CustomResultSet rs
                = (CustomResultSet) this.testedInstances[i].search("SELECT * FROM accuracytest WHERE ",
                                                                   TestDataFactory.getBetweenFilterWithAlias(),
                                                                   Arrays.asList(new String[] {"attr4", "attr5"}),
                                                                   TestDataFactory.getSearchContextAliasMap());
            Assert.assertTrue("The requested column is not retrieved", rs.findColumn("attr1") > 0);
            Assert.assertTrue("The requested column is not retrieved", rs.findColumn("attr2") > 0);
            Assert.assertTrue("The requested column is not retrieved", rs.findColumn("attr3") > 0);
            Assert.assertTrue("The requested column is not retrieved", rs.findColumn("attr4") > 0);
            Assert.assertTrue("The requested column is not retrieved", rs.findColumn("attr5") > 0);
            Assert.assertTrue("The requested column is not retrieved", rs.findColumn("attr6") > 0);

            List values1 = new ArrayList();
            List values2 = new ArrayList();
            List values3 = new ArrayList();
            List values4 = new ArrayList();
            List values5 = new ArrayList();
            List values6 = new ArrayList();
            while (rs.next()) {
                values1.add(rs.getString("attr1"));
                values2.add(rs.getString("attr2"));
                values3.add(rs.getString("attr3"));
                values4.add(rs.getString("attr4"));
                values5.add(rs.getString("attr5"));
                values6.add(rs.getString("attr6"));
            }

            Assert.assertEquals("Incorrect number of rows is returned", 1, values1.size());

            int index = values1.indexOf("-");
            Assert.assertEquals("Incorrect value for the row is returned", "4", values2.get(index));
            Assert.assertEquals("Incorrect value for the row is returned", "c", values3.get(index));
            Assert.assertEquals("Incorrect value for the row is returned", "d", values4.get(index));
            Assert.assertEquals("Incorrect value for the row is returned", "8", values5.get(index));
            Assert.assertEquals("Incorrect value for the row is returned", "9", values6.get(index));
        }
    }

    /**
     * <p>Removes all data from the database tables which are affected by this test case.</p>
     *
     * @throws SQLException if an SQL error occurs.
     */
    private void clearDatabaseTables() throws SQLException {
        String[] tables = new String[] {"accuracytest"};

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
        }
    }

    /**
     * <p>Populates the database with sample data.</p>
     *
     * @throws SQLException if an SQL error occurs.
     */
    private void insertData() throws SQLException {
        Statement stmt = null;
        try {
            stmt = this.connection.createStatement();
            stmt.executeUpdate("INSERT INTO accuracytest VALUES ('1', '2', '3', '4', '5', '6')");
            stmt.executeUpdate("INSERT INTO accuracytest VALUES ('11', '22', '33', '44', '55', '66')");
            stmt.executeUpdate("INSERT INTO accuracytest VALUES ('111', '222', '333', '444', '555', '666')");
            stmt.executeUpdate("INSERT INTO accuracytest VALUES ('a', 'b', 'c', 'd', 'e', 'f')");
            stmt.executeUpdate("INSERT INTO accuracytest VALUES ('-', '4', 'c', 'd', '8', '9')");
            stmt.executeUpdate("INSERT INTO accuracytest VALUES ('0', '6', 'c', 'd', 'e', 'f')");
            stmt.close();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }
}
