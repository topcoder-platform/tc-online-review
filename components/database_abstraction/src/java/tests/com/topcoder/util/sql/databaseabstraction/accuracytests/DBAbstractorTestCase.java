package com.topcoder.util.sql.databaseabstraction.accuracytests;

import com.topcoder.util.sql.databaseabstraction.*;

import java.sql.ResultSet;
import java.sql.Date;
import java.util.HashMap;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * <p>This class test the methods of the <code>
 * Column</code> class.</p>
 *
 * @author Tomson
 * @version 1.0
 */
public class DBAbstractorTestCase extends TestUtils {

    private ResultSet rs;

    public void setUp() {
        super.setUp();
        try {
    	    rs = state.executeQuery("SELECT * FROM tests");
    	} catch(Exception e) {
    	    fail(e.toString());
    	}
    }

    /**
     * test the constructor of <code>DatabaseAbstractor</code>
     */
    public void testConstructor() {
        DatabaseAbstractor dba = new DatabaseAbstractor();
        assertNotNull(
            "construct failed",
            dba);
        dba = new DatabaseAbstractor((Mapper)null);
        assertNotNull(
            "construct failed",
            dba);
        dba = new DatabaseAbstractor(new Mapper());
        assertNotNull(
            "construct failed",
            dba);
    }

    /**
     * test the convert method of <code>DatabaseAbstractor</code>
     */
    public void testConverter() {
        DatabaseAbstractor dba = new DatabaseAbstractor();
        try {
            dba.convertResultSet(rs);
        }catch(Exception e) {
            fail(e.toString());
        }
        try {
            dba.convertResultSet(rs, new Mapper());
        }catch(Exception e) {
            fail(e.toString());
        }
    }

    /**
     * test the remap method of <code>DatabaseAbstractior</code>
     */
    /** normal test */
    public void testRemap1() throws Exception {
        HashMap map = new HashMap();
        map.put("INTEGER", new Converter() {
            public Object convert(
                Object obj,
                int column,
                CustomResultSetMetaData metaData) {
                    return new Integer(((Integer)obj).intValue() + 1);
                }
            });
        DatabaseAbstractor dba = new DatabaseAbstractor(new Mapper(map));
        CustomResultSet crs = dba.convertResultSet(rs);
        crs.beforeFirst();
        crs.next();
        assertEquals(
            "should be 2",
            crs.getInt("C_Number"),
            2);
        crs.next();
        assertEquals(
            "should be 3",
            crs.getInt("C_Number"),
            3);
        crs.next();
        assertEquals(
            "Should be 4",
            crs.getInt("C_Number"),
            4);
        crs.next();
        assertEquals(
            "should be 5",
            crs.getInt("C_Number"),
            5);
        crs.next();
        assertEquals(
            "should be 6",
            crs.getInt("C_Number"),
            6);
    }
    /** exception test */
    /*
    public void testRemap2() {
        HashMap map = new HashMap();
        map.put(
            "TIMESTAMP",
            new ImplConverter());
        try {
            DatabaseAbstractor dba = new DatabaseAbstractor(new Mapper(map));
            CustomResultSet crs = dba.convertResultSet(rs);
            fail("should throw an exception");
        } catch(IllegalMappingException e) {
            // correct
        } catch(Exception e) {
            fail("shouldn throw an IllegalMappingException");
        }
    }
    */

    public static Test suite() {
        return new TestSuite(DBAbstractorTestCase.class);
    }
}