/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.failuretests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.search.builder.SearchBuilderConfigurationException;
import com.topcoder.search.builder.database.DatabaseSearchStrategy;
import com.topcoder.search.builder.filter.EqualToFilter;
import com.topcoder.search.builder.filter.NullFilter;
import com.topcoder.search.builder.ldap.EqualsFragmentBuilder;
import com.topcoder.util.classassociations.ClassAssociator;
import com.topcoder.util.config.ConfigManager;

import junit.framework.TestCase;

/**
 * Failure tests for <code>DatabaseSearchStrategy</code>.
 *
 * @author assistant
 * @version 1.3
 *
 */
public class DatabaseSearchStrategyTest13 extends TestCase {

    /**
     * Represents the DatabaseSearchStrategy instance to test.
     */
    private DatabaseSearchStrategy strategy = null;

    /**
     * Represents the DBConnectionFactory instance.
     */
    private DBConnectionFactory connectionFactory;

    /**
     * Represents the associations Map.
     */
    private Map associations = new HashMap();

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
     * Set up the environment.
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        super.setUp();

        ConfigManager cm = ConfigManager.getInstance();
        cm.add("failure/default.xml");
        cm.add("failure/DBSearchStrategy.xml");

        connectionFactory = new DBConnectionFactoryImpl("com.topcoder.db.connectionfactory.DBConnectionFactoryImpl");

        strategy = new DatabaseSearchStrategy("com.topcoder.search.builder.failure");
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
            this.tearDown();
            cm.add("failure/default.xml");
            cm.add("failure/MissDBFactory.xml");
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
            this.tearDown();
            cm.add("failure/default.xml");
            cm.add("failure/MissDBFactoryName.xml");
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
            this.tearDown();
            cm.add("failure/default.xml");
            cm.add("failure/MissSearchFragmentFactoryNS.xml");
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
            this.tearDown();
            cm.add("failure/default.xml");
            cm.add("failure/MissTargetFilter.xml");
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
    public void testconstructorMissFragmentName() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();
        try {
            this.tearDown();
            cm.add("failure/default.xml");
            cm.add("failure/MissFragmentName.xml");
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
     * IllegalArgumentException expected.
     *
     * @throws Exception to JUnit
     */
    public void testconstructor2NullNs() throws Exception {
        try {
            new DatabaseSearchStrategy(null, "mysql", associations);
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
    public void testconstructor2NullAssocaition() throws Exception {
        try {
            new DatabaseSearchStrategy(connectionFactory, "mysql", null);
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
    public void testconstructorNullInAssociations() throws Exception {
        associations.put(null, new EqualsFragmentBuilder());
        try {
            new DatabaseSearchStrategy(connectionFactory, "mysql", associations);
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
    public void testconstructorNonStringInAssocations() throws Exception {
        associations.put(new Object(), new EqualsFragmentBuilder());
        try {
            new DatabaseSearchStrategy(connectionFactory, "mysql", associations);
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
    public void testconstructorNullValue() throws Exception {
        associations.put(EqualToFilter.class, null);
        try {
            new DatabaseSearchStrategy(connectionFactory, "mysql", associations);
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
    public void testconstructorNullStringkey() throws Exception {
        associations.put(new Object(), new EqualsFragmentBuilder());
        try {
            new DatabaseSearchStrategy(connectionFactory, "mysql", associations);
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
    public void testconstructorNonStringValue() throws Exception {
        associations.put(EqualToFilter.class, new Object());
        try {
            new DatabaseSearchStrategy(connectionFactory, "mysql", associations);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // should land here
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

}
