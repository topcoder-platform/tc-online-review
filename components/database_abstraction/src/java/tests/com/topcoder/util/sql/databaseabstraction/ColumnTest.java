/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.sql.databaseabstraction;

import junit.framework.TestCase;

/**
 * Test case for Column.
 *
 * @author justforplay
 * @version 1.1
 * @since 1.1
 */
public class ColumnTest extends TestCase {

    /**
     * Instance of Column for unit test.
     */
    private Column column;

    /**
     * <p>
     * Create new instance of column.
     * </p>
     */
    public void setUp() {
        column = new Column("className", 5, "label", 4, "typeName", 2, 3, true, false, true);
    }

    /**
     * <p>
     * Test Constructor Column(String, int, String, int, String, int, int,
     * boolean, boolean, boolean).
     * </p>
     * <p>
     * Verify:Column can be instantiated correctly.
     * </p>
     */
    public void testColumn() {
        assertNotNull("Unable to instantiate Column", column);
        assertEquals("columnClassName is not initialized correctly.", column.getColumnClassName(), "className");
        assertEquals("columnDisplaySize is not initialized correctly.", column.getColumnDisplaySize(), 5);
        assertEquals("columnLabel is not initialized correctly.", column.getColumnLabel(), "label");
        assertEquals("columnType is not initialized correctly.", column.getColumnType(), 4);
        assertEquals("columnTypeName is not initialized correctly.", column.getColumnTypeName(), "typeName");
        assertEquals("columnPrecision is not initialized correctly.", column.getColumnPrecision(), 2);
        assertEquals("columnScale is not initialized correctly.", column.getColumnScale(), 3);
        assertEquals("autoIncrement is not initialized correctly.", column.isAutoIncrement(), true);
        assertEquals("currency is not initialized correctly.", column.isCurrency(), false);
        assertEquals("signed is not initialized correctly.", column.isSigned(), true);
    }

    /**
     * <p>
     * Test Constructor Column(String, int, String, int, String, int, int,
     * boolean, boolean, boolean,String).
     * </p>
     * <p>
     * Verify:Column can be instantiated correctly.
     * </p>
     */
    public void testColumn2() {
        Column column2 = new Column("className", 5, "label", 4, "typeName", 2, 3, true, false, true, "columnName");
        assertNotNull("Unable to instantiate Column", column2);
    }

    /**
     * <p>
     * Test getColumnClassName().
     * <p>
     * </p>
     * Verify:columnClassName can be retrieved correctly. </p>
     */
    public void testGetColumnClassName() {
        assertEquals("columnClassName can not be retrieved correctly.", column.getColumnClassName(), "className");
    }

    /**
     * <p>
     * Test setColumnClassName(String).
     * </p>
     * <p>
     * Verify:columnClassName can be changed correctly.
     * </p>
     */
    public void testSetColumnClassName() {
        column.setColumnClassName("new_columnClassName");
        assertEquals("columnClassName can not be changed correctly.", column.getColumnClassName(),
                "new_columnClassName");
    }

    /**
     * <p>
     * Test getColumnDisplaySize().
     * </p>
     * <p>
     * Verify: columnDisplaySize can be retrieved correctly.
     * </p>
     */
    public void testGetColumnDisplaySize() {
        assertEquals("columnDisplaySize can not be retrieved correctly.", column.getColumnDisplaySize(), 5);
    }

    /**
     * <p>
     * Test getColumnLabel().
     * </p>
     * <p>
     * Verify:columnLabel can be retrieved correctly.
     * </p>
     */
    public void testGetColumnLabel() {
        assertEquals("columnLabel can not be retrieved correctly.", column.getColumnLabel(), "label");
    }

    /**
     * <p>
     * Test setColumnLabel(String).
     * </p>
     * <p>
     * Verify:columnLabel can be changed correctly.
     * </p>
     */
    public void testSetColumnLabel() {
        column.setColumnLabel("new_columnLabel");
        assertEquals("columnLabel can not be changed correctly.", column.getColumnLabel(), "new_columnLabel");
    }

    /**
     * <p>
     * Test getColumnType().
     * </p>
     * <p>
     * Verify: columnType can be retrieved correctly.
     * </p>
     */
    public void testGetColumnType() {
        assertEquals("columnType can not be retrieved correctly.", column.getColumnType(), 4);
    }

    /**
     * <p>
     * Test getColumnTypeName().
     * </p>
     * <p>
     * Verify: columnTypeName can be retrieved correctly.
     * </p>
     */
    public void testGetColumnTypeName() {
        assertEquals("columnTypeName can not be retrieved correctly.", column.getColumnTypeName(), "typeName");
    }

    /**
     * <p>
     * Test getColumnPrecision().
     * </p>
     * <p>
     * Verify: columnPrecision can be retrieved correctly.
     * </p>
     */
    public void testGetColumnPrecision() {
        assertEquals("columnPrecision can not be retrieved correctly.", column.getColumnPrecision(), 2);
    }

    /**
     * <p>
     * Test getColumnScale().
     * </p>
     * <p>
     * Verify: columnScale can be retrieved correctly.
     * </p>
     */
    public void testGetColumnScale() {
        assertEquals("columnScale can not be retrieved correctly.", column.getColumnScale(), 3);
    }

    /**
     * <p>
     * Test isAutoIncrement().
     * </p>
     * <p>
     * Verify: autoIncrement can be retrieved correctly.
     * </p>
     */
    public void testIsAutoIncrement() {
        assertEquals("autoIncrement can not be retrieved correctly.", column.isAutoIncrement(), true);
    }

    /**
     * <p>
     * Test isCurrency().
     * </p>
     * <p>
     * Verify: currency can be retrieved correctly.
     * </p>
     */
    public void testIsCurrency() {
        assertEquals("currency can not be retrieved correctly.", column.isCurrency(), false);
    }

    /**
     * <p>
     * Test isSigned().
     * </p>
     * <p>
     * Verify: signed can be retrieved correctly.
     * </p>
     */

    public void testIsSigned() {
        assertEquals("signed can not be retrieved correctly.", column.isSigned(), true);
    }

    /**
     * <p>
     * Test getColumnName().
     * </p>
     * <p>
     * Verify:columnName can be retrieved correctly.
     * </p>
     */
    public void testGetColumnNamel() {
        assertNull("columnName can not be retrieved correctly.", column.getColumnName());
    }
}
