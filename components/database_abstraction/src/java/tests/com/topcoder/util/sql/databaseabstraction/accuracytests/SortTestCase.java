package com.topcoder.util.sql.databaseabstraction.accuracytests;

import com.topcoder.util.sql.databaseabstraction.*;

import java.sql.ResultSet;
import java.util.Comparator;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * <p>This class test the methods of the <code>
 * Column</code> class.</p>
 *
 * @author Tomson
 * @version 1.0
 */

public class SortTestCase extends TestUtils {

    public void setUp() {
        super.setUp();
    }

    /**
     * test the variant sort methods of <code>CustomResultSet
     * </code>
     */
    /** test the single sort methods */
    public void testSingleSort1_des() throws Exception {
        ResultSet rs = state.executeQuery("select D_Text_TO_Number from tests");
        CustomResultSet crs = new CustomResultSet(rs);
        crs.sortDescending("D_Text_TO_Number");

        String expectedResult[] = {"89","67","45","45","23"};

        int count = 0;
        crs.beforeFirst();
        while(crs.next()){
            assertEquals(crs.getString("D_Text_TO_Number"),expectedResult[count]);
            count++;
        }
    }
    public void testSingleSort1_as() throws Exception {
        ResultSet rs = state.executeQuery("select D_Text_TO_Number from tests");
        CustomResultSet crs = new CustomResultSet(rs);
        crs.sortAscending("D_Text_TO_Number");

        String expectedResult[] = {"23","45","45","67","89"};

        int count = 0;
        crs.beforeFirst();
        while(crs.next()){
            assertEquals(crs.getString("D_Text_TO_Number"),expectedResult[count]);
            count++;
        }
    }
    public void testSingleSort2_as() throws Exception {
        ResultSet rs = state.executeQuery("select D_Text_TO_Number from tests");
        CustomResultSet crs = new CustomResultSet(rs);
        crs.sortAscending("D_Text_TO_Number");

        String expectedResult[] = {"23","45","45","67","89"};

        int count = 0;
        crs.beforeFirst();
        while(crs.next()){
            assertEquals(crs.getString("D_Text_TO_Number"),expectedResult[count]);
            count++;
        }
    }

    /** test using the custom comparator to sort */
    public void testSingleSort3() throws Exception {
    	ResultSet rs = state.executeQuery("select D_Text_TO_Number from tests");
        CustomResultSet crs = new CustomResultSet(rs);
        crs.sortAscending("D_Text_TO_Number",
                          new Comparator() {
            public int compare(Object a, Object b) {
            	return ((String)a).substring(1).compareTo(((String)b).substring(1));
            }
        });
        String expectedResult[] = {"23","45","45","67","89"};

        int count = 0;
        crs.beforeFirst();
        while(crs.next()){
            assertEquals(crs.getString("D_Text_TO_Number"),expectedResult[count]);
            count++;
        }
    }
    /*
    public void testMultipleSort1() {
    	try {
    	    ResultSet rs = state.executeQuery("select C_Number, D_Text_To_Number from tests");
            CustomResultSet crs = new CustomResultSet(rs);
            crs.sortAscending(new int[]{2,1});
            crs.beforeFirst();
            crs.next();
            assertEquals(
                crs.getInt("C_Number"),
                4);
            crs.next();
            assertEquals(
                crs.getInt("C_Number"),
                1);
            crs.next();
            assertEquals(
                crs.getInt("C_Number"),
                3);
            crs.next();
            assertEquals(
                crs.getInt("C_Number"),
                2);
            crs.next();
            assertEquals(
                crs.getInt("C_Number"),
                5);
        } catch(Exception e) {
            e.printStackTrace();
            fail("Exception thrown");
        }
    }
    */
   /* public void testMultipleSort2() {
    	try {
    	    ResultSet rs = state.executeQuery("select * from tests");
            CustomResultSet crs = new CustomResultSet(rs);
            crs.sortAscending(new String[]{"D_Text_TO_Number", "C_Number"});
            crs.beforeFirst();
            crs.next();
            assertEquals(
                crs.getInt("C_Number"),
                4);
            crs.next();
            assertEquals(
                crs.getInt("C_Number"),
                1);
            crs.next();
            assertEquals(
                crs.getInt("C_Number"),
                3);
            crs.next();
            assertEquals(
                crs.getInt("C_Number"),
                2);
            crs.next();
            assertEquals(
                crs.getInt("C_Number"),
                5);
        } catch(Exception e) {
            fail("Exception throwed");
        }
    }*/
    public static Test suite() {
        return new TestSuite(SortTestCase.class);
    }
}