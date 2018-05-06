/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.failuretests;

import java.util.Iterator;

import com.topcoder.search.builder.SearchBuilderConfigurationException;
import com.topcoder.search.builder.SearchBundleManager;
import com.topcoder.util.config.ConfigManager;

import junit.framework.TestCase;

/**
 * Failure tests for <code>SearchBundleManager</code>.
 *
 * @author assistant
 * @version 1.3
 */
public class SearchBundleManagerTest13 extends TestCase {

    /**
     * Represents the manager to test.
     */
    private SearchBundleManager manager;

    /**
     * Set up the environment.
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        super.setUp();
        ConfigManager cm = ConfigManager.getInstance();
        cm.add("failure/default.xml");
        cm.add("failure/DBSearchStrategy.xml");

        manager = new SearchBundleManager();
    }

    /**
     * Clean up the environment.
     * @throws Exception to JUnit
     */
    protected void tearDown() throws Exception {
        super.tearDown();

        ConfigManager cm = ConfigManager.getInstance();
        Iterator it = cm.getAllNamespaces();
        while (it.hasNext()) {
            cm.removeNamespace(it.next().toString());
        }
    }

    /**
     * Test method for SearchBundleManager(java.lang.String).
     * In this case, the namespace is null.
     * Expected exception : {@link IllegalArgumentException}
     * @throws SearchBuilderConfigurationException to JUnit
     */
    public void testSearchBundleManagerNullNS() throws SearchBuilderConfigurationException {
        try {
            new SearchBundleManager(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for SearchBundleManager(java.lang.String).
     * In this case, the namespace doesn't exist.
     * Expected exception : {@link SearchBuilderConfigurationException}
     * @throws SearchBuilderConfigurationException to JUnit
     */
    public void testSearchBundleManagerNoSuchNS() throws SearchBuilderConfigurationException {
        try {
            new SearchBundleManager("not.exist.namespace");
            fail("SearchBuilderConfigurationException expected.");
        } catch (SearchBuilderConfigurationException e) {
            // should land here
        }
    }

    /**
     * Test method for SearchBundleManager(java.lang.String).
     * In this case, the namespace is empty.
     * Expected exception : {@link IllegalArgumentException}
     * @throws SearchBuilderConfigurationException to JUnit
     */
    public void testSearchBundleManagerEmptyNS() throws SearchBuilderConfigurationException {
        try {
            new SearchBundleManager(" ");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for SearchBundleManager(java.lang.String).
     * In this case, the namespace is empty.
     * Expected exception : {@link SearchBuilderConfigurationException}
     * @throws Exception to JUnit
     */
    public void testSearchBundleManagerMissSearchStrategyFactoryNamespace() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();
        try {
            this.tearDown();
            cm.add("failure/MissSearchStrategyFactoryNS.xml");
            new SearchBundleManager("com.topcoder.search.builder.failure");
            fail("SearchBuilderConfigurationException expected.");
        } catch (SearchBuilderConfigurationException e) {
            // should land here
        }
    }

    /**
     * Test method for SearchBundleManager(java.lang.String).
     * In this case, the search bundles parameter is missed.
     * Expected exception : {@link SearchBuilderConfigurationException}
     * @throws Exception to JUnit
     */
    public void testSearchBundleManagerMissSearchBundles() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();
        try {
            this.tearDown();
            cm.add("failure/MissSearchBundles.xml");
            new SearchBundleManager("com.topcoder.search.builder.failure");
            fail("SearchBuilderConfigurationException expected.");
        } catch (SearchBuilderConfigurationException e) {
            // should land here
        }
    }

    /**
     * Test method for SearchBundleManager(java.lang.String).
     * In this case, the searchable fields is missed.
     * Expected exception : {@link SearchBuilderConfigurationException}
     * @throws Exception to JUnit
     */
    public void testSearchBundleManagerMissSearchableFields() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();
        try {
            this.tearDown();
            cm.add("failure/MissSearchableFields.xml");
            new SearchBundleManager("com.topcoder.search.builder.failure");
            fail("SearchBuilderConfigurationException expected.");
        } catch (SearchBuilderConfigurationException e) {
            // should land here
        }
    }

    /**
     * Test method for SearchBundleManager(java.lang.String).
     * In this case, the searchable fields is bad.
     * Expected exception : {@link SearchBuilderConfigurationException}
     * @throws Exception to JUnit
     */
    public void testSearchBundleManagerBadSearchableFields() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();
        try {
            this.tearDown();
            cm.add("failure/BadSearchableFields.xml");
            new SearchBundleManager("com.topcoder.search.builder.failure");
            fail("SearchBuilderConfigurationException expected.");
        } catch (SearchBuilderConfigurationException e) {
            // should land here
        }
    }

    /**
     * Test method for SearchBundleManager(java.lang.String).
     * In this case, the search strategy is missed.
     * Expected exception : {@link SearchBuilderConfigurationException}
     * @throws Exception to JUnit
     */
    public void testSearchBundleManagerMissSearchStregary() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();
        try {
            this.tearDown();
            cm.add("failure/MissSearchStrategy.xml");
            new SearchBundleManager("com.topcoder.search.builder.failure");
            fail("SearchBuilderConfigurationException expected.");
        } catch (SearchBuilderConfigurationException e) {
            // should land here
        }
    }

    /**
     * Test method for SearchBundleManager(java.lang.String).
     * In this case, the search strategy class is missed.
     * Expected exception : {@link SearchBuilderConfigurationException}
     * @throws Exception to JUnit
     */
    public void testSearchBundleManagerMissSearchStregaryClass() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();
        try {
            this.tearDown();
            cm.add("failure/MissSearchStrategyClass.xml");
            new SearchBundleManager("com.topcoder.search.builder.failure");
            fail("SearchBuilderConfigurationException expected.");
        } catch (SearchBuilderConfigurationException e) {
            // should land here
        }
    }

    /**
     * Test method for SearchBundleManager(java.lang.String).
     * In this case, the search strategy class is missed.
     * Expected exception : {@link SearchBuilderConfigurationException}
     * @throws Exception to JUnit
     */
    public void testSearchBundleManagerMissContext() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();
        try {
            this.tearDown();
            cm.add("failure/MissContext.xml");
            new SearchBundleManager("com.topcoder.search.builder.failure");
            fail("SearchBuilderConfigurationException expected.");
        } catch (SearchBuilderConfigurationException e) {
            // should land here
        }
    }

}
