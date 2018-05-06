/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.accuracytests;

import com.topcoder.search.builder.*;
import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.datavalidator.IntegerValidator;

import junit.framework.TestCase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.Connection;
import java.io.File;


/**
 * <p>
 * Accuracy test cases for SearchBundleManager.
 * </p>
 *
 * @author zjq, isv
 * @version 1.3
 */
public class SearchBundleManagerAccuracyTests extends TestCase {
    /**
     * The namespace to construct the SearchBundleManager to test.
     */
    private static final String NAMESPACE = "com.topcoder.search.builder";
    /**
     * The namespace to factory the database datasource.
     */
    private static final String DBFACTORY = "com.topcoder.db.connectionfactory.DBConnectionFactoryImpl";

    /**
     * The instance of SearchBundleManager for test.
     */
    private SearchBundleManager manager = null;

    /**
     * The map of the searchable fileds.
     */
    private Map fields = null;

    /**
     * The map of alias name and real name.
     */
    private Map alias = null;

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

        manager = new SearchBundleManager(NAMESPACE);
    }
    /**
     * tearDown.
     *
     * @throws Exception to junit
     */
    protected void tearDown() throws Exception {
        ConfigHelper.releaseNamespaces();
    }

    /**
     * test construct with exception throw.
     *
     * @throws Exception to junit
     */
    public void testconstruct1() throws Exception {
        try {
            manager = new SearchBundleManager(NAMESPACE);
        } catch (Exception e) {
            fail("No Exception should be throw");
        }
    }

    /**
     * test the construct seccess.
     */
    public void testconstruct2() {
        assertNotNull("can not construct SearchBundleManager", manager);
    }

    /**
     * test the construct seccess.
     */
    public void testconstruct3() {
        manager = new SearchBundleManager();
        assertNotNull("can not construct SearchBundleManager", manager);
    }

    /**
     * test construct with exception throw.
     *
     * @throws Exception to junit
     */
    public void testconstruct4() throws Exception {
        try {
            manager = new SearchBundleManager();
        } catch (Exception e) {
            fail("No Exception should be throw");
        }
    }

    /**
     * test success getSearchBundle.
     *
     */
    public void testgetSearchBundle3() {
        SearchBundle bd = manager.getSearchBundle("FirstSearchBundle");
        assertNotNull("The name with FirstSearchBundle should exist", bd);
    }

    /**
     * test the mothed getSearchBundleNames.
     *
     */
    public void testgetSearchBundleNames() {
        List list = manager.getSearchBundleNames();
        assertNotNull("The list should not be null", list);

        //2 SearchBundle from config file
        assertEquals("There should 2 SearchBundle in the manager", 2, list.size());
    }

    /**
     * Test the mothed addSearchBundle(success).
     *
     * @throws Exception to junit
     */
    public void testaddSearchBundle3() throws Exception {
        fields = new HashMap();
        fields.put("name", IntegerValidator.greaterThan(1));
        alias = new HashMap();
        alias.put("name", "test");

        int s = manager.getSearchBundleNames().size();
        manager.addSearchBundle(new SearchBundle("test", fields, alias, "SELECT * FROM people WHERE "));

        assertEquals("The new SearchBundle has not been added", s + 1, manager.getSearchBundleNames().size());
    }

    /**
     * test removeSearchBundle with unexist name.
     *
     * no exception should throw
     *
     */
    public void testremoveSearchBundle3() {
        try {
            manager.removeSearchBundle("unexist");
        } catch (Exception e) {
            fail("No Exception should be throw");
        }
    }

    /**
     * test removeSearchBundle successfully.
     *
     */
    public void testremoveSearchBundle4() {
        int s = manager.getSearchBundleNames().size();
        manager.removeSearchBundle("FirstSearchBundle");

        assertEquals("The FirstSearchBundle has not been removed", s - 1,
                     manager.getSearchBundleNames().size());
    }

    /**
     * test the mothed clear.
     *
     */
    public void testclear() {
        manager.clear();

        //all has been cleared
        assertEquals("The clear do not success", 0, manager.getSearchBundleNames().size());
    }
}
