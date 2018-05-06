/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.ldap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.topcoder.search.builder.SearchBuilderConfigurationException;
import com.topcoder.search.builder.database.DatabaseSearchStrategy;
import com.topcoder.search.builder.filter.NullFilter;
import com.topcoder.util.config.ConfigManager;

import junit.framework.TestCase;

/**
 * Failure tests for <code>LDAPSearchStrategy</code>.
 *
 * @author assistant
 * @version 1.3
 */
public class LDAPSearchStrategyTest extends TestCase {

    /**
     * Represents the returnFields.
     */
    private List returnFields = new ArrayList();

    /**
     * Represents the map of alias name and real name.
     *
     */
    private Map aliasMap = new HashMap();

    /**
     * Represents the strategy to test.
     */
    private LDAPSearchStrategy strategy;

    /**
     * Set up the environment.
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        super.setUp();

        ConfigManager cm = ConfigManager.getInstance();
        cm.add("failure/LDAPSearchStrategy.xml");


        strategy = new LDAPSearchStrategy("com.topcoder.search.builder.failure");
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
     * The failure test of the constructor.
     * IllegalArgumentException expected.
     *
     * @throws Exception to JUnit
     */
    public void testconstructor1NullNS() throws Exception {
        try {
            new DatabaseSearchStrategy(null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }
    /**
     * The failure test of the constructor.
     * IllegalArgumentException expected.
     *
     * @throws Exception to JUnit
     */
    public void testconstructorEmptyNS() throws Exception {
        try {
            new DatabaseSearchStrategy("  ");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * The failure test of the constructor.
     * namespace unknown,
     * SearchBuilderConfigurationException expected.
     *
     * @throws Exception to JUnit
     */
    public void testconstructorNotExistNS() throws Exception {
        try {
            new DatabaseSearchStrategy("not.exist.namespace");
            fail("SearchBuilderConfigurationException expected");
        } catch (SearchBuilderConfigurationException e) {
            // should land here
        }
    }

    /**
     * The failure test of the constructor.
     * SearchBuilderConfigurationException expected.
     *
     * @throws Exception to JUnit
     */
    public void testconstructorMissDBFactory() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();
        try {
            cm.removeNamespace("com.topcoder.search.builder.failure");
            cm.add("failure/MissLDAPFactory.xml");
            new DatabaseSearchStrategy("com.topcoder.search.builder.failure");
            fail("SearchBuilderConfigurationException expected");
        } catch (SearchBuilderConfigurationException e) {
            // should land here
        } finally {
            cm.removeNamespace("com.topcoder.search.builder.failure");
        }
    }

    /**
     * The failure test of the constructor.
     * SearchBuilderConfigurationException expected.
     *
     * @throws Exception to JUnit
     */
    public void testconstructorMissDBFactoryName() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();
        try {
            cm.removeNamespace("com.topcoder.search.builder.failure");
            cm.add("failure/MissLDAPConnectionFactoryName.xml");
            new DatabaseSearchStrategy("com.topcoder.search.builder.failure");
            fail("SearchBuilderConfigurationException expected");
        } catch (SearchBuilderConfigurationException e) {
            // should land here
        } finally {
            cm.removeNamespace("com.topcoder.search.builder.failure");
        }
    }

    /**
     * The failure test of the constructor.
     * SearchBuilderConfigurationException expected.
     *
     * @throws Exception to JUnit
     */
    public void testconstructorMissSearchFragmentFactoryNS() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();
        try {
            cm.removeNamespace("com.topcoder.search.builder.failure");
            cm.add("failure/MissLDAPFragmentClassName.xml");
            new DatabaseSearchStrategy("com.topcoder.search.builder.failure");
            fail("SearchBuilderConfigurationException expected");
        } catch (SearchBuilderConfigurationException e) {
            // should land here
        } finally {
            cm.removeNamespace("com.topcoder.search.builder.failure");
        }
    }

    /**
     * The failure test of the constructor.
     * SearchBuilderConfigurationException expected.
     *
     * @throws Exception to JUnit
     */
    public void testconstructorMissTargetFilter() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();
        try {
            cm.removeNamespace("com.topcoder.search.builder.failure");
            cm.add("failure/MissLDAPTargetFilter.xml");
            new DatabaseSearchStrategy("com.topcoder.search.builder.failure");
            fail("SearchBuilderConfigurationException expected");
        } catch (SearchBuilderConfigurationException e) {
            // should land here
        } finally {
            cm.removeNamespace("com.topcoder.search.builder.failure");
        }
    }

    /**
     * The failure test of the constructor.
     * SearchBuilderConfigurationException expected.
     *
     * @throws Exception to JUnit
     */
    public void testconstructorMissConnectionInfo() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();
        try {
            cm.removeNamespace("com.topcoder.search.builder.failure");
            cm.add("failure/MissConnectionInfo.xml");
            new DatabaseSearchStrategy("com.topcoder.search.builder.failure");
            fail("SearchBuilderConfigurationException expected");
        } catch (SearchBuilderConfigurationException e) {
            // should land here
        } finally {
            cm.removeNamespace("com.topcoder.search.builder.failure");
        }
    }

    /**
     * Test method for search(java.lang.String, com.topcoder.search.builder.filter.Filter,
     * java.util.List, java.util.Map).
     * @throws Exception to JUnit
     */
    public void testSearchNullContext() throws Exception {
        try {
            strategy.search(null, new NullFilter("a"), returnFields, aliasMap);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for search(java.lang.String, com.topcoder.search.builder.filter.Filter,
     * java.util.List, java.util.Map).
     * @throws Exception to JUnit
     */
    public void testSearchNullFilter() throws Exception {
        try {
            strategy.search("context", null, returnFields, aliasMap);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for search(java.lang.String, com.topcoder.search.builder.filter.Filter,
     * java.util.List, java.util.Map).
     * @throws Exception to JUnit
     */
    public void testSearchNullFields() throws Exception {
        try {
            strategy.search("context", new NullFilter("a"), null, aliasMap);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for search(java.lang.String, com.topcoder.search.builder.filter.Filter,
     * java.util.List, java.util.Map).
     * @throws Exception to JUnit
     */
    public void testSearchNullInFields() throws Exception {
        try {
            List list = new ArrayList();
            list.add(null);
            strategy.search("context", new NullFilter("a"), list, aliasMap);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for search(java.lang.String, com.topcoder.search.builder.filter.Filter,
     * java.util.List, java.util.Map).
     * @throws Exception to JUnit
     */
    public void testSearchNonStringInFields() throws Exception {
        try {
            List list = new ArrayList();
            list.add(new Object());
            strategy.search("context", new NullFilter("a"), list, aliasMap);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for search(java.lang.String, com.topcoder.search.builder.filter.Filter,
     * java.util.List, java.util.Map).
     * @throws Exception to JUnit
     */
    public void testSearchEmptyStringInFields() throws Exception {
        try {
            List list = new ArrayList();
            list.add(" ");
            strategy.search("context", new NullFilter("a"), list, aliasMap);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for search(java.lang.String, com.topcoder.search.builder.filter.Filter,
     * java.util.List, java.util.Map).
     * @throws Exception to JUnit
     */
    public void testSearchNullInAlias() throws Exception {
        try {
            Map map = new HashMap();
            map.put("a", null);
            strategy.search("context", new NullFilter("a"), new ArrayList(), map);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for search(java.lang.String, com.topcoder.search.builder.filter.Filter,
     * java.util.List, java.util.Map).
     * @throws Exception to JUnit
     */
    public void testSearchNonStringKeyInAlias() throws Exception {
        try {
            Map map = new HashMap();
            map.put(new Object(), "value");
            strategy.search("context", new NullFilter("a"), new ArrayList(), map);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for search(java.lang.String, com.topcoder.search.builder.filter.Filter,
     * java.util.List, java.util.Map).
     * @throws Exception to JUnit
     */
    public void testSearchEmptyStringKeyInAlias() throws Exception {
        try {
            Map map = new HashMap();
            map.put(" ", "value");
            strategy.search("context", new NullFilter("a"), new ArrayList(), map);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for search(java.lang.String, com.topcoder.search.builder.filter.Filter,
     * java.util.List, java.util.Map).
     * @throws Exception to JUnit
     */
    public void testSearchEmptyStringValueInAlias() throws Exception {
        try {
            Map map = new HashMap();
            map.put("jey", " ");
            strategy.search("context", new NullFilter("a"), new ArrayList(), map);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for search(java.lang.String, com.topcoder.search.builder.filter.Filter,
     * java.util.List, java.util.Map).
     * @throws Exception to JUnit
     */
    public void testSearchNonStringValueInAlias() throws Exception {
        try {
            Map map = new HashMap();
            map.put("jey", new Object());
            strategy.search("context", new NullFilter("a"), new ArrayList(), map);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for search(java.lang.String, com.topcoder.search.builder.filter.Filter,
     * java.util.List, java.util.Map).
     * @throws Exception to JUnit
     */
    public void testbuildSearchContextNullContext() throws Exception {
        try {
            strategy.buildSearchContext(null, new NullFilter("a"), returnFields, aliasMap);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for buildSearchContext(java.lang.String, com.topcoder.search.builder.filter.Filter,
     * java.util.List, java.util.Map).
     * @throws Exception to JUnit
     */
    public void testbuildSearchContextNullFilter() throws Exception {
        try {
            strategy.buildSearchContext("context", null, returnFields, aliasMap);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for buildSearchContext(java.lang.String, com.topcoder.search.builder.filter.Filter,
     * java.util.List, java.util.Map).
     * @throws Exception to JUnit
     */
    public void testbuildSearchContextNullFields() throws Exception {
        try {
            strategy.buildSearchContext("context", new NullFilter("a"), null, aliasMap);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for buildSearchContext(java.lang.String, com.topcoder.search.builder.filter.Filter,
     * java.util.List, java.util.Map).
     * @throws Exception to JUnit
     */
    public void testbuildSearchContextNullInFields() throws Exception {
        try {
            List list = new ArrayList();
            list.add(null);
            strategy.buildSearchContext("context", new NullFilter("a"), list, aliasMap);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for buildSearchContext(java.lang.String, com.topcoder.search.builder.filter.Filter,
     * java.util.List, java.util.Map).
     * @throws Exception to JUnit
     */
    public void testbuildSearchContextNonStringInFields() throws Exception {
        try {
            List list = new ArrayList();
            list.add(new Object());
            strategy.buildSearchContext("context", new NullFilter("a"), list, aliasMap);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for buildSearchContext(java.lang.String, com.topcoder.search.builder.filter.Filter,
     * java.util.List, java.util.Map).
     * @throws Exception to JUnit
     */
    public void testbuildSearchContextEmptyStringInFields() throws Exception {
        try {
            List list = new ArrayList();
            list.add(" ");
            strategy.buildSearchContext("context", new NullFilter("a"), list, aliasMap);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for buildSearchContext(java.lang.String, com.topcoder.search.builder.filter.Filter,
     * java.util.List, java.util.Map).
     * @throws Exception to JUnit
     */
    public void testbuildSearchContextNullInAlias() throws Exception {
        try {
            Map map = new HashMap();
            map.put("a", null);
            strategy.buildSearchContext("context", new NullFilter("a"), new ArrayList(), map);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for buildSearchContext(java.lang.String, com.topcoder.search.builder.filter.Filter,
     * java.util.List, java.util.Map).
     * @throws Exception to JUnit
     */
    public void testbuildSearchContextNonStringKeyInAlias() throws Exception {
        try {
            Map map = new HashMap();
            map.put(new Object(), "value");
            strategy.buildSearchContext("context", new NullFilter("a"), new ArrayList(), map);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for buildSearchContext(java.lang.String, com.topcoder.search.builder.filter.Filter,
     * java.util.List, java.util.Map).
     * @throws Exception to JUnit
     */
    public void testbuildSearchContextEmptyStringKeyInAlias() throws Exception {
        try {
            Map map = new HashMap();
            map.put(" ", "value");
            strategy.buildSearchContext("context", new NullFilter("a"), new ArrayList(), map);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for buildSearchContext(java.lang.String, com.topcoder.search.builder.filter.Filter,
     * java.util.List, java.util.Map).
     * @throws Exception to JUnit
     */
    public void testbuildSearchContextEmptyStringValueInAlias() throws Exception {
        try {
            Map map = new HashMap();
            map.put("jey", " ");
            strategy.buildSearchContext("context", new NullFilter("a"), new ArrayList(), map);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for buildSearchContext(java.lang.String, com.topcoder.search.builder.filter.Filter,
     * java.util.List, java.util.Map).
     * @throws Exception to JUnit
     */
    public void testbuildSearchContextNonStringValueInAlias() throws Exception {
        try {
            Map map = new HashMap();
            map.put("jey", new Object());
            strategy.buildSearchContext("context", new NullFilter("a"), new ArrayList(), map);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }
}
