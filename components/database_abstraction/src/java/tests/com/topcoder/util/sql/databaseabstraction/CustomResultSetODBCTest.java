/*
 * Copyright (C) 2006, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.sql.databaseabstraction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Comparator;
import java.util.HashMap;

import junit.framework.TestCase;

/**
 * <p>
 * This tests CustomResultSet.
 * </p>
 *
 * @author WishingBone, justforplay, suhugo
 * @version 2.0
 * @since 1.0
 */
public class CustomResultSetODBCTest extends TestCase {

    /**
     * Connection.
     */
    private Connection connection;

    /**
     * Statement.
     */
    private Statement statement;

    /**
     * Set up database connection.
     *
     * @throws Exception
     *             to JUnit
     */
    protected void setUp() throws Exception {
        Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
        connection = DriverManager.getConnection("jdbc:odbc:Tester");
        statement = connection.createStatement();
    }

    /**
     * Close database connection.
     *
     * @throws Exception
     *             to JUnit
     */
    protected void tearDown() throws Exception {
        connection.close();
    }

    /**
     * Test construct from result set.
     */
    public void testCustomResultSet() {
        try {
            ResultSet rs = statement.executeQuery("select * from tests");
            CustomResultSet crs = new CustomResultSet(rs);
            assertTrue(crs.getRecordCount() == 5);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test various navigate method of custom result set.
     */
    public void testCustomResultSetNavigate() {
        try {
            ResultSet rs = statement.executeQuery("select * from tests");
            CustomResultSet crs = new CustomResultSet(rs);
            assertTrue(crs.isBeforeFirst());
            crs.next();
            assertTrue(crs.isFirst());
            assertTrue(crs.first());
            crs.next();
            crs.next();
            crs.next();
            crs.next();
            assertTrue(crs.isLast());
            assertTrue(crs.last());
            crs.previous();
            crs.next();
            crs.next();
            assertTrue(crs.isAfterLast());
            crs.beforeFirst();
            assertTrue(crs.isBeforeFirst());
            crs.afterLast();
            assertTrue(crs.isAfterLast());
            crs.relative(-3);
            assertTrue(crs.getRow() == 3);
            crs.relative(1);
            assertTrue(crs.getRow() == 4);
            crs.absolute(5);
            assertTrue(crs.getRow() == 5);
            crs.absolute(-4);
            assertTrue(crs.getRow() == 2);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test sort on custom result on with natural order or explicit comparator.
     */
    public void testCustomResultSetSort() {
        try {
            ResultSet rs = statement.executeQuery("select * from tests");
            CustomResultSet crs = new CustomResultSet(rs);

            crs.sortAscending(2);
            crs.beforeFirst();
            crs.next();
            assertTrue(crs.getInt("C_Number") == 1);
            crs.next();
            assertTrue(crs.getInt("C_Number") == 2);
            crs.next();
            assertTrue(crs.getInt("C_Number") == 3);
            crs.next();
            assertTrue(crs.getInt("C_Number") == 4);
            crs.next();
            assertTrue(crs.getInt("C_Number") == 5);

            crs.sortAscending("C_Number");
            crs.beforeFirst();
            crs.next();
            assertTrue(crs.getInt("C_Number") == 1);
            crs.next();
            assertTrue(crs.getInt("C_Number") == 2);
            crs.next();
            assertTrue(crs.getInt("C_Number") == 3);
            crs.next();
            assertTrue(crs.getInt("C_Number") == 4);
            crs.next();
            assertTrue(crs.getInt("C_Number") == 5);

            crs.sortDescending(2);
            crs.beforeFirst();
            crs.next();
            assertTrue(crs.getInt("C_Number") == 5);
            crs.next();
            assertTrue(crs.getInt("C_Number") == 4);
            crs.next();
            assertTrue(crs.getInt("C_Number") == 3);
            crs.next();
            assertTrue(crs.getInt("C_Number") == 2);
            crs.next();
            assertTrue(crs.getInt("C_Number") == 1);

            crs.sortDescending("C_Number");
            crs.beforeFirst();
            crs.next();
            assertTrue(crs.getInt("C_Number") == 5);
            crs.next();
            assertTrue(crs.getInt("C_Number") == 4);
            crs.next();
            assertTrue(crs.getInt("C_Number") == 3);
            crs.next();
            assertTrue(crs.getInt("C_Number") == 2);
            crs.next();
            assertTrue(crs.getInt("C_Number") == 1);

            crs.sortAscending(3, new Comparator<Object>() {
                public int compare(Object a, Object b) {
                    return ((String) a).compareTo((String) b);
                }
            });
            crs.beforeFirst();
            crs.next();
            assertTrue(crs.getString("D_Text_To_Number").equals("23"));
            crs.next();
            assertTrue(crs.getString("D_Text_To_Number").equals("45"));
            crs.next();
            assertTrue(crs.getString("D_Text_To_Number").equals("45"));
            crs.next();
            assertTrue(crs.getString("D_Text_To_Number").equals("67"));
            crs.next();
            assertTrue(crs.getString("D_Text_To_Number").equals("89"));

            crs.sortAscending("D_Text_To_Number", new Comparator<Object>() {
                public int compare(Object a, Object b) {
                    return ((String) a).compareTo((String) b);
                }
            });
            crs.beforeFirst();
            crs.next();
            assertTrue(crs.getString("D_Text_To_Number").equals("23"));
            crs.next();
            assertTrue(crs.getString("D_Text_To_Number").equals("45"));
            crs.next();
            assertTrue(crs.getString("D_Text_To_Number").equals("45"));
            crs.next();
            assertTrue(crs.getString("D_Text_To_Number").equals("67"));
            crs.next();
            assertTrue(crs.getString("D_Text_To_Number").equals("89"));

            crs.sortDescending(3, new Comparator<Object>() {
                public int compare(Object a, Object b) {
                    return ((String) a).compareTo((String) b);
                }
            });
            crs.beforeFirst();
            crs.next();
            assertTrue(crs.getString("D_Text_To_Number").equals("89"));
            crs.next();
            assertTrue(crs.getString("D_Text_To_Number").equals("67"));
            crs.next();
            assertTrue(crs.getString("D_Text_To_Number").equals("45"));
            crs.next();
            assertTrue(crs.getString("D_Text_To_Number").equals("45"));
            crs.next();
            assertTrue(crs.getString("D_Text_To_Number").equals("23"));

            crs.sortDescending("D_Text_To_Number", new Comparator<Object>() {
                public int compare(Object a, Object b) {
                    return ((String) a).compareTo((String) b);
                }
            });
            crs.beforeFirst();
            crs.next();
            assertTrue(crs.getString("D_Text_To_Number").equals("89"));
            crs.next();
            assertTrue(crs.getString("D_Text_To_Number").equals("67"));
            crs.next();
            assertTrue(crs.getString("D_Text_To_Number").equals("45"));
            crs.next();
            assertTrue(crs.getString("D_Text_To_Number").equals("45"));
            crs.next();
            assertTrue(crs.getString("D_Text_To_Number").equals("23"));

            crs.sortAscending(new int[] { 2, 3 });
            crs.beforeFirst();
            crs.next();
            assertTrue(crs.getInt("C_Number") == 1);
            crs.next();
            assertTrue(crs.getInt("C_Number") == 2);
            crs.next();
            assertTrue(crs.getInt("C_Number") == 3);
            crs.next();
            assertTrue(crs.getInt("C_Number") == 4);
            crs.next();
            assertTrue(crs.getInt("C_Number") == 5);

            crs.sortAscending(new String[] { "C_Number", "D_Text_To_Number" });
            crs.beforeFirst();
            crs.next();
            assertTrue(crs.getInt("C_Number") == 1);
            crs.next();
            assertTrue(crs.getInt("C_Number") == 2);
            crs.next();
            assertTrue(crs.getInt("C_Number") == 3);
            crs.next();
            assertTrue(crs.getInt("C_Number") == 4);
            crs.next();
            assertTrue(crs.getInt("C_Number") == 5);

            crs.sortDescending(new int[] { 2, 3 });
            crs.beforeFirst();
            crs.next();
            assertTrue(crs.getInt("C_Number") == 5);
            crs.next();
            assertTrue(crs.getInt("C_Number") == 4);
            crs.next();
            assertTrue(crs.getInt("C_Number") == 3);
            crs.next();
            assertTrue(crs.getInt("C_Number") == 2);
            crs.next();
            assertTrue(crs.getInt("C_Number") == 1);

            crs.sortDescending(new String[] { "C_Number", "D_Text_To_Number" });
            crs.beforeFirst();
            crs.next();
            assertTrue(crs.getInt("C_Number") == 5);
            crs.next();
            assertTrue(crs.getInt("C_Number") == 4);
            crs.next();
            assertTrue(crs.getInt("C_Number") == 3);
            crs.next();
            assertTrue(crs.getInt("C_Number") == 2);
            crs.next();
            assertTrue(crs.getInt("C_Number") == 1);

            crs.sortAscending(new int[] { 2, 3 }, new Comparator[] { null, new Comparator<Object>() {
                public int compare(Object a, Object b) {
                    return ((String) a).compareTo((String) b);
                }
            } });
            crs.beforeFirst();
            crs.next();
            assertTrue(crs.getInt("C_Number") == 1);
            crs.next();
            assertTrue(crs.getInt("C_Number") == 2);
            crs.next();
            assertTrue(crs.getInt("C_Number") == 3);
            crs.next();
            assertTrue(crs.getInt("C_Number") == 4);
            crs.next();
            assertTrue(crs.getInt("C_Number") == 5);

            crs.sortAscending(new String[] { "C_Number", "D_Text_To_Number" }, new Comparator[] { null,
                new Comparator<Object>() {
                    public int compare(Object a, Object b) {
                        return ((String) a).compareTo((String) b);
                    }
                } });
            crs.beforeFirst();
            crs.next();
            assertTrue(crs.getInt("C_Number") == 1);
            crs.next();
            assertTrue(crs.getInt("C_Number") == 2);
            crs.next();
            assertTrue(crs.getInt("C_Number") == 3);
            crs.next();
            assertTrue(crs.getInt("C_Number") == 4);
            crs.next();
            assertTrue(crs.getInt("C_Number") == 5);

            crs.sortDescending(new int[] { 2, 3 }, new Comparator[] { null, new Comparator<Object>() {
                public int compare(Object a, Object b) {
                    return ((String) a).compareTo((String) b);
                }
            } });
            crs.beforeFirst();
            crs.next();
            assertTrue(crs.getInt("C_Number") == 5);
            crs.next();
            assertTrue(crs.getInt("C_Number") == 4);
            crs.next();
            assertTrue(crs.getInt("C_Number") == 3);
            crs.next();
            assertTrue(crs.getInt("C_Number") == 2);
            crs.next();
            assertTrue(crs.getInt("C_Number") == 1);

            crs.sortDescending(new String[] { "C_Number", "D_Text_To_Number" }, new Comparator[] { null,
                new Comparator<Object>() {
                    public int compare(Object a, Object b) {
                        return ((String) a).compareTo((String) b);
                    }
                } });
            crs.beforeFirst();
            crs.next();
            assertTrue(crs.getInt("C_Number") == 5);
            crs.next();
            assertTrue(crs.getInt("C_Number") == 4);
            crs.next();
            assertTrue(crs.getInt("C_Number") == 3);
            crs.next();
            assertTrue(crs.getInt("C_Number") == 2);
            crs.next();
            assertTrue(crs.getInt("C_Number") == 1);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test remap data in custom result set.
     */
    public void testCustomResultSetRemap() {
        try {
            ResultSet rs = statement.executeQuery("select * from tests");
            CustomResultSet crs = new CustomResultSet(rs);
            HashMap<String, Converter> map = new HashMap<String, Converter>();
            map.put("varchar", new Converter() {
                public Object convert(Object obj, int column, CustomResultSetMetaData metaData) {
                    return "" + ((String) obj).substring(1) + ((String) obj).substring(0, 1);
                }
            });
            crs.remap(new Mapper(map));
            crs.sortAscending("D_Text_To_Number");
            crs.beforeFirst();
            crs.next();
            assertTrue(crs.getString("D_Text_To_Number").equals("32"));
            crs.next();
            assertTrue(crs.getString("D_Text_To_Number").equals("54"));
            crs.next();
            assertTrue(crs.getString("D_Text_To_Number").equals("54"));
            crs.next();
            assertTrue(crs.getString("D_Text_To_Number").equals("76"));
            crs.next();
            assertTrue(crs.getString("D_Text_To_Number").equals("98"));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
