/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder;

import junit.framework.TestCase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.datavalidator.IntegerValidator;


/**
 * <p>
 * Unit test cases for SearchBundleManager.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.3
 */
public class SearchBundleManagerTests extends TestCase {
    /**
     * The namespace to construct the SearchBundleManager to test.
     */
    private static final String NAMESPACE = "com.topcoder.search.builder";

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
     * setUp.
     *
     * @throws Exception to junit
     */
    protected void setUp() throws Exception {
        //add the configuration
        TestHelper.clearConfig();
        TestHelper.addConfig();
        manager = new SearchBundleManager(NAMESPACE);
    }
    /**
     * tearDown.
     *
     * @throws Exception to junit
     */
    protected void tearDown() throws Exception {
        TestHelper.clearConfig();
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
     * test fail with construct with wrong namespace.
     *
     * SearchBuilderConfigurationException should be throw
     * @throws Exception to junit
     */
    public void testconstruct5() throws Exception {
        try {
            manager = new SearchBundleManager("wrong namespace");
            fail("SearchBuilderConfigurationException should be throw");
        } catch (SearchBuilderConfigurationException e) {
            //success
        }
    }
    /**
     * test fail with construct with wrong namespace.
     * Since the configuration is invalid,
     * SearchBuilderConfigurationException should be throw.
     *
     * @throws Exception to junit
     */
    public void testconstruct_withInvalidNS1() throws Exception {
        ConfigManager.getInstance().add("InvalidConfiguration/Invalid1.xml");
        try {
            manager = new SearchBundleManager("Invalid1");
            fail("SearchBuilderConfigurationException should be throw");
        } catch (SearchBuilderConfigurationException e) {
            //success
        }
    }
    /**
     * test fail with construct with wrong namespace.
     * Since the configuration is invalid,
     * SearchBuilderConfigurationException should be throw.
     *
     * @throws Exception to junit
     */
    public void testconstruct_withInvalidNSw() throws Exception {
        ConfigManager.getInstance().add("InvalidConfiguration/Invalid2.xml");
        try {
            manager = new SearchBundleManager("Invalid2");
            fail("SearchBuilderConfigurationException should be throw");
        } catch (SearchBuilderConfigurationException e) {
            //success
        }
    }
    /**
     * test fail with construct with wrong namespace.
     * Since the configuration is invalid,
     * SearchBuilderConfigurationException should be throw.
     *
     * @throws Exception to junit
     */
    public void testconstruct_withInvalidNS3() throws Exception {
        ConfigManager.getInstance().add("InvalidConfiguration/Invalid3.xml");
        try {
            manager = new SearchBundleManager("Invalid3");
            fail("SearchBuilderConfigurationException should be throw");
        } catch (SearchBuilderConfigurationException e) {
            //success
        }
    }
    /**
     * test fail with construct with wrong namespace.
     * Since the configuration is invalid,
     * SearchBuilderConfigurationException should be throw.
     *
     * @throws Exception to junit
     */
    public void testconstruct_withInvalidNS4() throws Exception {
        ConfigManager.getInstance().add("InvalidConfiguration/Invalid4.xml");
        try {
            manager = new SearchBundleManager("Invalid4");
            fail("SearchBuilderConfigurationException should be throw");
        } catch (SearchBuilderConfigurationException e) {
            //success
        }
    }
    /**
     * test fail with construct with wrong namespace.
     * Since the configuration is invalid,
     * SearchBuilderConfigurationException should be throw.
     *
     * @throws Exception to junit
     */
    public void testconstruct_withInvalidNS5() throws Exception {
        TestHelper.clearConfig();
        ConfigManager.getInstance().add("InvalidConfiguration/Invalid5.xml");
        try {
            manager = new SearchBundleManager("Invalid5");
            fail("SearchBuilderConfigurationException should be throw");
        } catch (SearchBuilderConfigurationException e) {
            //success
        }
    }
    /**
     * test fail with construct with wrong namespace.
     * Since the configuration is invalid,
     * SearchBuilderConfigurationException should be throw.
     *
     * @throws Exception to junit
     */
    public void testconstruct_withInvalidNS6() throws Exception {
        TestHelper.clearConfig();
        ConfigManager.getInstance().add("InvalidConfiguration/Invalid6.xml");
        try {
            manager = new SearchBundleManager("Invalid6");
            fail("SearchBuilderConfigurationException should be throw");
        } catch (SearchBuilderConfigurationException e) {
            //success
        }
    }
    /**
     * test fail with construct with wrong namespace.
     * Since the configuration is invalid,
     * SearchBuilderConfigurationException should be throw.
     *
     * @throws Exception to junit
     */
    public void testconstruct_withInvalidNS7() throws Exception {
        TestHelper.clearConfig();
        ConfigManager.getInstance().add("InvalidConfiguration/Invalid7.xml");
        try {
            manager = new SearchBundleManager("Invalid7");
            fail("SearchBuilderConfigurationException should be throw");
        } catch (SearchBuilderConfigurationException e) {
            //success
        }
    }
    /**
     * test fail with construct with wrong namespace.
     * Since the configuration is invalid,
     * SearchBuilderConfigurationException should be throw.
     *
     * @throws Exception to junit
     */
    public void testconstruct_withInvalidNS8() throws Exception {
        TestHelper.clearConfig();
        ConfigManager.getInstance().add("InvalidConfiguration/Invalid8.xml");
        try {
            manager = new SearchBundleManager("Invalid8");
            fail("SearchBuilderConfigurationException should be throw");
        } catch (SearchBuilderConfigurationException e) {
            //success
        }
    }
    /**
     * test fail with construct with wrong namespace.
     * Since the configuration is invalid,
     * SearchBuilderConfigurationException should be throw.
     *
     * @throws Exception to junit
     */
    public void testconstruct_withInvalidNS9() throws Exception {
        TestHelper.clearConfig();
        ConfigManager.getInstance().add("InvalidConfiguration/Invalid9.xml");
        try {
            manager = new SearchBundleManager("Invalid9");
            fail("SearchBuilderConfigurationException should be throw");
        } catch (SearchBuilderConfigurationException e) {
            //success
        }
    }
    /**
     * test fail with construct with wrong namespace.
     * Since the configuration is invalid,
     * SearchBuilderConfigurationException should be throw.
     *
     * @throws Exception to junit
     */
    public void testconstruct_withInvalidNS10() throws Exception {
        TestHelper.clearConfig();
        ConfigManager.getInstance().add("InvalidConfiguration/Invalid10.xml");
        try {
            manager = new SearchBundleManager("Invalid10");
            fail("SearchBuilderConfigurationException should be throw");
        } catch (SearchBuilderConfigurationException e) {
            //success
        }
    }
    /**
     * test fail with construct with wrong namespace.
     * Since the configuration is invalid,
     * SearchBuilderConfigurationException should be throw.
     *
     * @throws Exception to junit
     */
    public void testconstruct_withInvalidNS11() throws Exception {
        TestHelper.clearConfig();
        ConfigManager.getInstance().add("InvalidConfiguration/Invalid11.xml");
        try {
            manager = new SearchBundleManager("Invalid11");
            fail("SearchBuilderConfigurationException should be throw");
        } catch (SearchBuilderConfigurationException e) {
            //success
        }
    }
    /**
     * test fail with construct with wrong namespace.
     * Since the configuration is invalid,
     * SearchBuilderConfigurationException should be throw.
     *
     * @throws Exception to junit
     */
    public void testconstruct_withInvalidNS12() throws Exception {
        TestHelper.clearConfig();
        ConfigManager.getInstance().add("InvalidConfiguration/Invalid12.xml");
        try {
            manager = new SearchBundleManager("Invalid12");
            fail("SearchBuilderConfigurationException should be throw");
        } catch (SearchBuilderConfigurationException e) {
            //success
        }
    }

    /**
     * test fail for getSearchBundle with name is null.
     *
     */
    public void testgetSearchBundle1() {
        try {
            manager.getSearchBundle(null);
            fail("IllegalArgumentException should be throw");
        } catch (IllegalArgumentException e) {
            //success
        }
    }

    /**
     * test fail for getSearchBundle with name is empty.
     *
     */
    public void testgetSearchBundle2() {
        try {
            manager.getSearchBundle(" ");
            fail("IllegalArgumentException should be throw");
        } catch (IllegalArgumentException e) {
            //success
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
        assertEquals("There should 2 SearchBundle in the manager", 2,
            list.size());
    }

    /**
     * Test the mothed addSearchBundle(fail with SearchBundle is null).
     * @throws Exception to junit
     */
    public void testaddSearchBundle1() throws Exception {
        try {
            manager.addSearchBundle(null);
            fail("IllegalArgumentException should be throw");
        } catch (IllegalArgumentException e) {
            //success
        }
    }

    /**
     * Test the mothed addSearchBundle(fail with exist name).
     *
     * DuplicatedElementsException should throw
     *
     * @throws Exception to junit
     */
    public void testaddSearchBundle2() throws Exception {
        try {
            fields = new HashMap();
            fields.put("name", IntegerValidator.inRange(0, 1));
            alias = new HashMap();
            alias.put("name", "test");
            manager.addSearchBundle(new SearchBundle("FirstSearchBundle",
                    fields, alias, "context"));

            fail("DuplicatedElementsException should be throw");
        } catch (DuplicatedElementsException e) {
            //success
        }
    }

    /**
     * Test the mothed addSearchBundle(success).
     *
     * @throws Exception to junit
     */
    public void testaddSearchBundle3() throws Exception {
        fields = new HashMap();
        fields.put("name", IntegerValidator.inRange(0, 1));
        alias = new HashMap();
        alias.put("name", "test");

        int s = manager.getSearchBundleNames().size();
        manager.addSearchBundle(new SearchBundle("test", fields, alias, "context"));

        assertEquals("The new SearchBundle has not been added", s + 1,
            manager.getSearchBundleNames().size());
    }

    /**
     * test fail for removeSearchBundle with name null.
     *
     */
    public void testremoveSearchBundle1() {
        try {
            manager.removeSearchBundle(null);
            fail("IllegalArgumentException should be throw");
        } catch (IllegalArgumentException e) {
            //success
        }
    }

    /**
     * test fail for removeSearchBundle with name empty.
     *
     */
    public void testremoveSearchBundle2() {
        try {
            manager.removeSearchBundle("");
            fail("IllegalArgumentException should be throw");
        } catch (IllegalArgumentException e) {
            //success
        }
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
        assertEquals("The clear do not success", 0,
            manager.getSearchBundleNames().size());
    }
    /**
     * Test the method setSearchBundle(Map).
     *
     */
    public void testsetSearchBundle() {
        Map map = new HashMap();
        map.put("firstone", manager.getSearchBundle("FirstSearchBundle"));

        manager.setSearchBundle(map);

        assertEquals("The bundles in SearchBundManager now is updated.", 1, manager.getSearchBundleNames().size());
    }
    /**
     * Test the method setSearchBundle(Map),
     * fail for the map is null,
     * IllegalArgumentException should be thrown.
     *
     */
    public void testsetSearchBundle_failure1() {
        try {
            manager.setSearchBundle(null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }
    /**
     * Test the method setSearchBundle(Map),
     * fail for the map is empty,
     * IllegalArgumentException should be thrown.
     *
     */
    public void testsetSearchBundle_failure2() {
        try {
            manager.setSearchBundle(new HashMap());
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }
}
