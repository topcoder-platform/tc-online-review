package com.topcoder.util.sql.databaseabstraction.accuracytests;

import com.topcoder.util.sql.databaseabstraction.Column;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>This class test the methods of the <code>
 * Column</code> class.</p>
 *
 * @author Tomson
 * @version 1.0
 */
public class ColumnTestCase extends TestCase {

    /**
     * test all the functions of the <code>Column</code> 
     */
    public void testColumn() {
        try {
            Column c = new Column(
                    "PIG",          // class name
                     10,            // display size
                     "$p- i _g",    // column label
                     2,             // column type
                     "tcp ip",      // column type name
                     3,             // column precision
                     4,             // column scale
                     true,          // auto increment
                     false,         // is a Currency
                     true);         // is a Signed number
            assertNotNull(
                "construct failed",
                c);                                     
            assertEquals(
                "construct failed",
                c.getColumnClassName(),
                "PIG");
                
            c.setColumnClassName("pig_1234");
            assertEquals(
                "modify failed",
                c.getColumnClassName(),
                "pig_1234");
            assertEquals(
                "construct failed",
                c.getColumnDisplaySize(),
                10);
            assertEquals(
                "construct failed",
                c.getColumnLabel(),
                "$p- i _g");
                
            c.setColumnLabel("zc x1");
            assertEquals(
                "modifiy failed",
                c.getColumnLabel(),
                "zc x1");
            assertEquals(
                "construct failed",
                c.getColumnType(),
                2);
            assertEquals(
                "construct failed",
                c.getColumnTypeName(),
                "tcp ip");
            assertEquals(
                "construct failed",
                c.getColumnPrecision(),
                3);
            assertEquals(
                "construct failed",
                c.getColumnScale(),
                4);
            assertTrue(
                "construct failed",
                c.isAutoIncrement());
            assertFalse(
                "construct failed",
                c.isCurrency());
            assertTrue(
                "construct failed",
                c.isSigned());            
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    
    public static Test suite() {
        return new TestSuite(ColumnTestCase.class);
    }	
}