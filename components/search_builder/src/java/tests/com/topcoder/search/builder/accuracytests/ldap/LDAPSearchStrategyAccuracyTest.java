/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.accuracytests.ldap;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Assert;
import com.topcoder.search.builder.ldap.LDAPSearchStrategy;
import com.topcoder.search.builder.ldap.LDAPConnectionInformation;
import com.topcoder.search.builder.accuracytests.ConfigHelper;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.filter.BetweenFilter;
import com.topcoder.search.builder.filter.EqualToFilter;
import com.topcoder.search.builder.filter.AndFilter;
import com.topcoder.search.builder.filter.OrFilter;
import com.topcoder.search.builder.filter.NotFilter;
import com.topcoder.search.builder.filter.GreaterThanFilter;
import com.topcoder.search.builder.filter.GreaterThanOrEqualToFilter;
import com.topcoder.search.builder.filter.InFilter;
import com.topcoder.search.builder.filter.LessThanFilter;
import com.topcoder.search.builder.filter.LessThanOrEqualToFilter;
import com.topcoder.util.net.ldap.sdkinterface.LDAPSDKConnection;
import com.topcoder.util.net.ldap.sdkinterface.LDAPSDK;
import com.topcoder.util.net.ldap.sdkinterface.Entry;
import com.topcoder.util.net.ldap.sdkinterface.Values;
import com.topcoder.util.net.ldap.sdkinterface.LDAPSDKException;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * <p>An accuracy test for {@link com.topcoder.search.builder.ldap.LDAPSearchStrategy} class. Tests the methods for proper
 * handling of valid input data and producing accurate results. Passes the valid arguments to the
 * methods and verifies that either the state of the tested instance have been changed appropriately
 * or a correct result is produced by the method.</p>
 *
 * @author isv
 * @version 1.0
 * @since 1.3
 */
public class LDAPSearchStrategyAccuracyTest extends TestCase {

    /**
     * The namespace.
     */
    private static final String NAMESPACE = "com.topcoder.search.builder.accuracytests.ldap";

    /**
     * <p>The instances of {@link LDAPSearchStrategy} which are tested. These instances are initialized in
     * {@link #setUp()} method and released in {@link #tearDown()} method. Each instance is initialized
     * using a separate constructor provided by the tested class.<p>
     */
    private LDAPSearchStrategy[] testedInstances = null;

    /**
     * <p>A <code>LDAPSDKConnection</code> used by this test case for accesing the target LDAP data store.</p>
     */
    private LDAPConnectionInformation info = null;

    /**
     * <p>A <code>String</code> providing the base DN for LDAP entries.</p>
     */
    private String baseDn = null;

    /**
     * <p>Gets the test suite for {@link LDAPSearchStrategy} class.</p>
     *
     * @return a <code>TestSuite</code> providing the tests for {@link LDAPSearchStrategy} class.
     */
    public static Test suite() {
        return new TestSuite(LDAPSearchStrategyAccuracyTest.class);
    }

    /**
     * <p>Sets up the fixture. This method is called before a test is executed.</p>
     *
     * @throws Exception if any error occurs.
     */
    protected void setUp() throws Exception {
        super.setUp();
        ConfigHelper.releaseNamespaces();

        ConfigHelper.loadConfiguration(new File("accuracy/config.xml"));
        this.baseDn = ConfigHelper.getProperty(NAMESPACE, "baseDN");

        this.info
            = new LDAPConnectionInformation(new LDAPSDK("com.topcoder.util.net.ldap.sdkinterface.netscape.NetscapeFactory"),
                                            ConfigHelper.getProperty(NAMESPACE, "host"),
                                            Integer.parseInt(ConfigHelper.getProperty(NAMESPACE, "port")),
                                            false,
                                            LDAPSDKConnection.SCOPE_ONE,
                                            ConfigHelper.getProperty(NAMESPACE, "user"),
                                            ConfigHelper.getProperty(NAMESPACE, "password"));
        LDAPSDKConnection connection
            = new LDAPSDK("com.topcoder.util.net.ldap.sdkinterface.netscape.NetscapeFactory").createConnection();
        try {
            connection.connect(info.getHost(), info.getPort());

            connection.authenticate(3, info.getDnroot(), info.getPassword());

            deleteEntries(connection);
            insertData(connection);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        this.testedInstances = new LDAPSearchStrategy[2];
        this.testedInstances[0] = new LDAPSearchStrategy(info, TestDataFactory.getClassAssociattions());
        this.testedInstances[1]
            = new LDAPSearchStrategy("com.topcoder.search.builder.accuracytests.LDAPSearchStrategy");
    }

    /**
     * <p>Tears down the fixture. This method is called after a test is executed.</p>
     *
     * @throws Exception if any error occurs.
     */
    protected void tearDown() throws Exception {
        LDAPSDKConnection connection = null;
        try {
            this.testedInstances = null;
            ConfigHelper.releaseNamespaces();
            connection = new LDAPSDK("com.topcoder.util.net.ldap.sdkinterface.netscape.NetscapeFactory").createConnection();
            connection.connect(info.getHost(), info.getPort());
            connection.authenticate(3, info.getDnroot(), info.getPassword());
            deleteEntries(connection);
            super.tearDown();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * <p>Accuracy test. Tests the {@link LDAPSearchStrategy#search(String, Filter, List, Map)} method for proper
     * behavior.</p>
     *
     * <p>Verifies that the method queries the database tables correctly based on provided filter, content, alias
     * mapping.</p>
     *
     * @throws Exception if an unexpected error occurs. Such an error must be interpreted as test failure.
     */
    /*public void testSearch_String_Filter_List_Map_BetweenFilter_NoAlias_EmptyReturnList() throws Exception {
        for (int i = 0; i < this.testedInstances.length; i++) {

            Iterator iterator
                = (Iterator) this.testedInstances[i].search(this.baseDn,
                                                            new BetweenFilter("cn", "cn3", "cn1"),
                                                            new ArrayList(),
                                                            TestDataFactory.getLDAPPersonAliasMap());
            Entry[] entries = collectEntries(iterator);

            Assert.assertEquals("Incorrect number of entries is returned", 3, entries.length);
            Assert.assertNotNull("The matching entry is not returned", findEndtry("cn=cn1," + this.baseDn, entries));
            Assert.assertNotNull("The matching entry is not returned", findEndtry("cn=cn2," + this.baseDn, entries));
            Assert.assertNotNull("The matching entry is not returned", findEndtry("cn=cn3," + this.baseDn, entries));
            Assert.assertTrue("All attributes must be returned",
                              findEndtry("cn=cn1," + this.baseDn, entries).getAttributes().size() >= 2);
            Assert.assertTrue("All attributes must be returned",
                              findEndtry("cn=cn2," + this.baseDn, entries).getAttributes().size() >= 2);
            Assert.assertTrue("All attributes must be returned",
                              findEndtry("cn=cn3," + this.baseDn, entries).getAttributes().size() >= 2);
            Assert.assertEquals("The requested attribute must be returned", "cn1",
                                findEndtry("cn=cn1," + this.baseDn, entries).getValues("cn").getTextValues().get(0));
            Assert.assertEquals("The requested attribute must be returned", "cn2",
                                findEndtry("cn=cn2," + this.baseDn, entries).getValues("cn").getTextValues().get(0));
            Assert.assertEquals("The requested attribute must be returned", "cn3",
                                findEndtry("cn=cn3," + this.baseDn, entries).getValues("cn").getTextValues().get(0));
            Assert.assertEquals("The requested attribute must be returned", "sn1",
                                findEndtry("cn=cn1," + this.baseDn, entries).getValues("sn").getTextValues().get(0));
            Assert.assertEquals("The requested attribute must be returned", "sn2",
                                findEndtry("cn=cn2," + this.baseDn, entries).getValues("sn").getTextValues().get(0));
            Assert.assertEquals("The requested attribute must be returned", "sn3",
                                findEndtry("cn=cn3," + this.baseDn, entries).getValues("sn").getTextValues().get(0));
        }
    }*/

    /**
     * <p>Accuracy test. Tests the {@link LDAPSearchStrategy#search(String, Filter, List, Map)} method for proper
     * behavior.</p>
     *
     * <p>Verifies that the method queries the database tables correctly based on provided filter, content, alias
     * mapping.</p>
     *
     * @throws Exception if an unexpected error occurs. Such an error must be interpreted as test failure.
     */
    /*public void testSearch_String_Filter_List_Map_BetweenFilter_NoAlias_NonEmptyReturnList() throws Exception {
        for (int i = 0; i < this.testedInstances.length; i++) {

            Iterator iterator
                = (Iterator) this.testedInstances[i].search(this.baseDn,
                                                            new BetweenFilter("cn", "cn3", "cn1"),
                                                            Arrays.asList(new String[] {"sn"}),
                                                            TestDataFactory.getLDAPPersonAliasMap());
            Entry[] entries = collectEntries(iterator);

            Assert.assertEquals("Incorrect number of entries is returned", 3, entries.length);
            Assert.assertNotNull("The matching entry is not returned", findEndtry("cn=cn1," + this.baseDn, entries));
            Assert.assertNotNull("The matching entry is not returned", findEndtry("cn=cn2," + this.baseDn, entries));
            Assert.assertNotNull("The matching entry is not returned", findEndtry("cn=cn3," + this.baseDn, entries));
            Assert.assertTrue("Only requested attributes must be returned",
                              findEndtry("cn=cn1," + this.baseDn, entries).getAttributes().size() == 1);
            Assert.assertTrue("Only requested attributes must be returned",
                              findEndtry("cn=cn2," + this.baseDn, entries).getAttributes().size() == 1);
            Assert.assertTrue("Only requested attributes must be returned",
                              findEndtry("cn=cn3," + this.baseDn, entries).getAttributes().size() == 1);
            Assert.assertTrue("The requested attribute must be returned",
                              findEndtry("cn=cn1," + this.baseDn, entries).getAttributes().containsKey("sn"));
            Assert.assertTrue("The requested attribute must be returned",
                              findEndtry("cn=cn2," + this.baseDn, entries).getAttributes().containsKey("sn"));
            Assert.assertTrue("The requested attribute must be returned",
                              findEndtry("cn=cn3," + this.baseDn, entries).getAttributes().containsKey("sn"));
            Assert.assertEquals("The requested attribute must be returned", "sn1",
                                findEndtry("cn=cn1," + this.baseDn, entries).getValues("sn").getTextValues().get(0));
            Assert.assertEquals("The requested attribute must be returned", "sn2",
                                findEndtry("cn=cn2," + this.baseDn, entries).getValues("sn").getTextValues().get(0));
            Assert.assertEquals("The requested attribute must be returned", "sn3",
                                findEndtry("cn=cn3," + this.baseDn, entries).getValues("sn").getTextValues().get(0));
        }
    }*/

    /**
     * <p>Accuracy test. Tests the {@link LDAPSearchStrategy#search(String, Filter, List, Map)} method for proper
     * behavior.</p>
     *
     * <p>Verifies that the method queries the database tables correctly based on provided filter, content, alias
     * mapping.</p>
     *
     * @throws Exception if an unexpected error occurs. Such an error must be interpreted as test failure.
     */
    /*public void testSearch_String_Filter_List_Map_BetweenFilter_Alias_EmptyReturnList() throws Exception {
        for (int i = 0; i < this.testedInstances.length; i++) {

            Iterator iterator
                = (Iterator) this.testedInstances[i].search(this.baseDn,
                                                            new BetweenFilter("CommonName", "cn3", "cn1"),
                                                            new ArrayList(),
                                                            TestDataFactory.getLDAPPersonAliasMap());
            Entry[] entries = collectEntries(iterator);

            Assert.assertEquals("Incorrect number of entries is returned", 3, entries.length);
            Assert.assertNotNull("The matching entry is not returned", findEndtry("cn=cn1," + this.baseDn, entries));
            Assert.assertNotNull("The matching entry is not returned", findEndtry("cn=cn2," + this.baseDn, entries));
            Assert.assertNotNull("The matching entry is not returned", findEndtry("cn=cn3," + this.baseDn, entries));
            Assert.assertTrue("All attributes must be returned",
                              findEndtry("cn=cn1," + this.baseDn, entries).getAttributes().size() >= 2);
            Assert.assertTrue("All attributes must be returned",
                              findEndtry("cn=cn2," + this.baseDn, entries).getAttributes().size() >= 2);
            Assert.assertTrue("All attributes must be returned",
                              findEndtry("cn=cn3," + this.baseDn, entries).getAttributes().size() >= 2);
            Assert.assertEquals("The requested attribute must be returned", "cn1",
                                findEndtry("cn=cn1," + this.baseDn, entries).getValues("cn").getTextValues().get(0));
            Assert.assertEquals("The requested attribute must be returned", "cn2",
                                findEndtry("cn=cn2," + this.baseDn, entries).getValues("cn").getTextValues().get(0));
            Assert.assertEquals("The requested attribute must be returned", "cn3",
                                findEndtry("cn=cn3," + this.baseDn, entries).getValues("cn").getTextValues().get(0));
            Assert.assertEquals("The requested attribute must be returned", "sn1",
                                findEndtry("cn=cn1," + this.baseDn, entries).getValues("sn").getTextValues().get(0));
            Assert.assertEquals("The requested attribute must be returned", "sn2",
                                findEndtry("cn=cn2," + this.baseDn, entries).getValues("sn").getTextValues().get(0));
            Assert.assertEquals("The requested attribute must be returned", "sn3",
                                findEndtry("cn=cn3," + this.baseDn, entries).getValues("sn").getTextValues().get(0));
        }
    }*/

    /**
     * <p>Accuracy test. Tests the {@link LDAPSearchStrategy#search(String, Filter, List, Map)} method for proper
     * behavior.</p>
     *
     * <p>Verifies that the method queries the database tables correctly based on provided filter, content, alias
     * mapping.</p>
     *
     * @throws Exception if an unexpected error occurs. Such an error must be interpreted as test failure.
     */
    /*public void testSearch_String_Filter_List_Map_BetweenFilter_Alias_NonEmptyReturnList() throws Exception {
        for (int i = 0; i < this.testedInstances.length; i++) {

            Iterator iterator
                = (Iterator) this.testedInstances[i].search(this.baseDn,
                                                            new BetweenFilter("CommonName", "cn3", "cn1"),
                                                            Arrays.asList(new String[] {"sn"}),
                                                            TestDataFactory.getLDAPPersonAliasMap());
            Entry[] entries = collectEntries(iterator);

            Assert.assertEquals("Incorrect number of entries is returned", 3, entries.length);
            Assert.assertNotNull("The matching entry is not returned", findEndtry("cn=cn1," + this.baseDn, entries));
            Assert.assertNotNull("The matching entry is not returned", findEndtry("cn=cn2," + this.baseDn, entries));
            Assert.assertNotNull("The matching entry is not returned", findEndtry("cn=cn3," + this.baseDn, entries));
            Assert.assertTrue("Only requested attributes must be returned",
                              findEndtry("cn=cn1," + this.baseDn, entries).getAttributes().size() == 1);
            Assert.assertTrue("Only requested attributes must be returned",
                              findEndtry("cn=cn2," + this.baseDn, entries).getAttributes().size() == 1);
            Assert.assertTrue("Only requested attributes must be returned",
                              findEndtry("cn=cn3," + this.baseDn, entries).getAttributes().size() == 1);
            Assert.assertTrue("The requested attribute must be returned",
                              findEndtry("cn=cn1," + this.baseDn, entries).getAttributes().containsKey("sn"));
            Assert.assertTrue("The requested attribute must be returned",
                              findEndtry("cn=cn2," + this.baseDn, entries).getAttributes().containsKey("sn"));
            Assert.assertTrue("The requested attribute must be returned",
                              findEndtry("cn=cn3," + this.baseDn, entries).getAttributes().containsKey("sn"));
            Assert.assertEquals("The requested attribute must be returned", "sn1",
                                findEndtry("cn=cn1," + this.baseDn, entries).getValues("sn").getTextValues().get(0));
            Assert.assertEquals("The requested attribute must be returned", "sn2",
                                findEndtry("cn=cn2," + this.baseDn, entries).getValues("sn").getTextValues().get(0));
            Assert.assertEquals("The requested attribute must be returned", "sn3",
                                findEndtry("cn=cn3," + this.baseDn, entries).getValues("sn").getTextValues().get(0));
        }
    }*/

    /**
     * <p>Accuracy test. Tests the {@link LDAPSearchStrategy#search(String, Filter, List, Map)} method for proper
     * behavior.</p>
     *
     * <p>Verifies that the method queries the database tables correctly based on provided filter, content, alias
     * mapping.</p>
     *
     * @throws Exception if an unexpected error occurs. Such an error must be interpreted as test failure.
     */
    public void testSearch_String_Filter_List_Map_AndFilter_NoAlias_EmptyReturnList() throws Exception {
        Filter f1 = new EqualToFilter("cn", "cn1");
        Filter f2 = new EqualToFilter("sn", "sn1");
        AndFilter andFilter1 = new AndFilter(f1, f2);
        AndFilter andFilter2 = new AndFilter(f1, f2);
        AndFilter andFilter3 = new AndFilter(andFilter1, andFilter2);

        for (int i = 0; i < this.testedInstances.length; i++) {

            Iterator iterator
                = (Iterator) this.testedInstances[i].search(this.baseDn,
                                                            andFilter3,
                                                            new ArrayList(),
                                                            TestDataFactory.getLDAPPersonAliasMap());
            Entry[] entries = collectEntries(iterator);

            Assert.assertEquals("Incorrect number of entries is returned", 1, entries.length);
            Assert.assertNotNull("The matching entry is not returned", findEndtry("cn=cn1," + this.baseDn, entries));
            Assert.assertTrue("All attributes must be returned",
                              findEndtry("cn=cn1," + this.baseDn, entries).getAttributes().size() >= 2);
            Assert.assertEquals("The requested attribute must be returned", "cn1",
                                findEndtry("cn=cn1," + this.baseDn, entries).getValues("cn").getTextValues().get(0));
            Assert.assertEquals("The requested attribute must be returned", "sn1",
                                findEndtry("cn=cn1," + this.baseDn, entries).getValues("sn").getTextValues().get(0));
        }
    }

    /**
     * <p>Accuracy test. Tests the {@link LDAPSearchStrategy#search(String, Filter, List, Map)} method for proper
     * behavior.</p>
     *
     * <p>Verifies that the method queries the database tables correctly based on provided filter, content, alias
     * mapping.</p>
     *
     * @throws Exception if an unexpected error occurs. Such an error must be interpreted as test failure.
     */
    public void testSearch_String_Filter_List_Map_OrFilter_NoAlias_EmptyReturnList() throws Exception {
        Filter f1 = new EqualToFilter("cn", "cn1");
        Filter f2 = new EqualToFilter("sn", "sn2");
        OrFilter orFilter = new OrFilter(f1, f2);

        for (int i = 0; i < this.testedInstances.length; i++) {

            Iterator iterator
                = (Iterator) this.testedInstances[i].search(this.baseDn,
                                                            orFilter,
                                                            new ArrayList(),
                                                            TestDataFactory.getLDAPPersonAliasMap());
            Entry[] entries = collectEntries(iterator);

            Assert.assertEquals("Incorrect number of entries is returned", 2, entries.length);
            Assert.assertNotNull("The matching entry is not returned", findEndtry("cn=cn1," + this.baseDn, entries));
            Assert.assertNotNull("The matching entry is not returned", findEndtry("cn=cn2," + this.baseDn, entries));
            Assert.assertTrue("All attributes must be returned",
                              findEndtry("cn=cn1," + this.baseDn, entries).getAttributes().size() >= 2);
            Assert.assertTrue("All attributes must be returned",
                              findEndtry("cn=cn2," + this.baseDn, entries).getAttributes().size() >= 2);
            Assert.assertEquals("The requested attribute must be returned", "cn1",
                                findEndtry("cn=cn1," + this.baseDn, entries).getValues("cn").getTextValues().get(0));
            Assert.assertEquals("The requested attribute must be returned", "sn1",
                                findEndtry("cn=cn1," + this.baseDn, entries).getValues("sn").getTextValues().get(0));
            Assert.assertEquals("The requested attribute must be returned", "cn2",
                                findEndtry("cn=cn2," + this.baseDn, entries).getValues("cn").getTextValues().get(0));
            Assert.assertEquals("The requested attribute must be returned", "sn2",
                                findEndtry("cn=cn2," + this.baseDn, entries).getValues("sn").getTextValues().get(0));
        }
    }

    /**
     * <p>Accuracy test. Tests the {@link LDAPSearchStrategy#search(String, Filter, List, Map)} method for proper
     * behavior.</p>
     *
     * <p>Verifies that the method queries the database tables correctly based on provided filter, content, alias
     * mapping.</p>
     *
     * @throws Exception if an unexpected error occurs. Such an error must be interpreted as test failure.
     */
    public void testSearch_String_Filter_List_Map_NotFilter_NoAlias_EmptyReturnList() throws Exception {
        Filter f1 = new EqualToFilter("cn", "cn1");
        NotFilter notFilter = new NotFilter(f1);

        for (int i = 0; i < this.testedInstances.length; i++) {

            Iterator iterator
                = (Iterator) this.testedInstances[i].search(this.baseDn,
                                                            notFilter,
                                                            new ArrayList(),
                                                            TestDataFactory.getLDAPPersonAliasMap());
            Entry[] entries = collectEntries(iterator);

            Assert.assertEquals("Incorrect number of entries is returned", 5, entries.length);
            Assert.assertNotNull("The matching entry is not returned", findEndtry("cn=cn2," + this.baseDn, entries));
            Assert.assertNotNull("The matching entry is not returned", findEndtry("cn=cn3," + this.baseDn, entries));
            Assert.assertNotNull("The matching entry is not returned", findEndtry("cn=cn4," + this.baseDn, entries));
            Assert.assertNotNull("The matching entry is not returned", findEndtry("cn=cn5," + this.baseDn, entries));
            Assert.assertNotNull("The matching entry is not returned", findEndtry("cn=cn6," + this.baseDn, entries));

            Assert.assertTrue("All attributes must be returned",
                              findEndtry("cn=cn2," + this.baseDn, entries).getAttributes().size() >= 2);
            Assert.assertTrue("All attributes must be returned",
                              findEndtry("cn=cn3," + this.baseDn, entries).getAttributes().size() >= 2);
            Assert.assertTrue("All attributes must be returned",
                              findEndtry("cn=cn4," + this.baseDn, entries).getAttributes().size() >= 2);
            Assert.assertTrue("All attributes must be returned",
                              findEndtry("cn=cn5," + this.baseDn, entries).getAttributes().size() >= 2);
            Assert.assertTrue("All attributes must be returned",
                              findEndtry("cn=cn6," + this.baseDn, entries).getAttributes().size() >= 2);

            Assert.assertEquals("The requested attribute must be returned", "cn2",
                                findEndtry("cn=cn2," + this.baseDn, entries).getValues("cn").getTextValues().get(0));
            Assert.assertEquals("The requested attribute must be returned", "sn2",
                                findEndtry("cn=cn2," + this.baseDn, entries).getValues("sn").getTextValues().get(0));
            Assert.assertEquals("The requested attribute must be returned", "cn3",
                                findEndtry("cn=cn3," + this.baseDn, entries).getValues("cn").getTextValues().get(0));
            Assert.assertEquals("The requested attribute must be returned", "sn3",
                                findEndtry("cn=cn3," + this.baseDn, entries).getValues("sn").getTextValues().get(0));
            Assert.assertEquals("The requested attribute must be returned", "cn4",
                                findEndtry("cn=cn4," + this.baseDn, entries).getValues("cn").getTextValues().get(0));
            Assert.assertEquals("The requested attribute must be returned", "sn4",
                                findEndtry("cn=cn4," + this.baseDn, entries).getValues("sn").getTextValues().get(0));
            Assert.assertEquals("The requested attribute must be returned", "cn5",
                                findEndtry("cn=cn5," + this.baseDn, entries).getValues("cn").getTextValues().get(0));
            Assert.assertEquals("The requested attribute must be returned", "sn5",
                                findEndtry("cn=cn5," + this.baseDn, entries).getValues("sn").getTextValues().get(0));
            Assert.assertEquals("The requested attribute must be returned", "cn6",
                                findEndtry("cn=cn6," + this.baseDn, entries).getValues("cn").getTextValues().get(0));
            Assert.assertEquals("The requested attribute must be returned", "sn6",
                                findEndtry("cn=cn6," + this.baseDn, entries).getValues("sn").getTextValues().get(0));
        }
    }

    /**
     * <p>Accuracy test. Tests the {@link LDAPSearchStrategy#search(String, Filter, List, Map)} method for proper
     * behavior.</p>
     *
     * <p>Verifies that the method queries the database tables correctly based on provided filter, content, alias
     * mapping.</p>
     *
     * @throws Exception if an unexpected error occurs. Such an error must be interpreted as test failure.
     */
    /*public void testSearch_String_Filter_List_Map_GreaterFilter_NoAlias_EmptyReturnList() throws Exception {
        Filter filter = new GreaterThanFilter("cn", "cn4");

        for (int i = 0; i < this.testedInstances.length; i++) {

            Iterator iterator
                = (Iterator) this.testedInstances[i].search(this.baseDn,
                                                            filter,
                                                            new ArrayList(),
                                                            TestDataFactory.getLDAPPersonAliasMap());
            Entry[] entries = collectEntries(iterator);

            Assert.assertEquals("Incorrect number of entries is returned", 2, entries.length);
            Assert.assertNotNull("The matching entry is not returned", findEndtry("cn=cn5," + this.baseDn, entries));
            Assert.assertNotNull("The matching entry is not returned", findEndtry("cn=cn6," + this.baseDn, entries));
            Assert.assertTrue("All attributes must be returned",
                              findEndtry("cn=cn5," + this.baseDn, entries).getAttributes().size() >= 2);
            Assert.assertTrue("All attributes must be returned",
                              findEndtry("cn=cn6," + this.baseDn, entries).getAttributes().size() >= 2);
            Assert.assertEquals("The requested attribute must be returned", "cn5",
                                findEndtry("cn=cn5," + this.baseDn, entries).getValues("cn").getTextValues().get(0));
            Assert.assertEquals("The requested attribute must be returned", "sn5",
                                findEndtry("cn=cn5," + this.baseDn, entries).getValues("sn").getTextValues().get(0));
            Assert.assertEquals("The requested attribute must be returned", "cn6",
                                findEndtry("cn=cn6," + this.baseDn, entries).getValues("cn").getTextValues().get(0));
            Assert.assertEquals("The requested attribute must be returned", "sn6",
                                findEndtry("cn=cn6," + this.baseDn, entries).getValues("sn").getTextValues().get(0));
        }
    }
*/
    /**
     * <p>Accuracy test. Tests the {@link LDAPSearchStrategy#search(String, Filter, List, Map)} method for proper
     * behavior.</p>
     *
     * <p>Verifies that the method queries the database tables correctly based on provided filter, content, alias
     * mapping.</p>
     *
     * @throws Exception if an unexpected error occurs. Such an error must be interpreted as test failure.
     */
    /*public void testSearch_String_Filter_List_Map_GreaterOrEqualFilter_NoAlias_EmptyReturnList() throws Exception {
        Filter filter = new GreaterThanOrEqualToFilter("cn", "cn5");

        for (int i = 0; i < this.testedInstances.length; i++) {

            Iterator iterator
                = (Iterator) this.testedInstances[i].search(this.baseDn,
                                                            filter,
                                                            new ArrayList(),
                                                            TestDataFactory.getLDAPPersonAliasMap());
            Entry[] entries = collectEntries(iterator);

            Assert.assertEquals("Incorrect number of entries is returned", 2, entries.length);
            Assert.assertNotNull("The matching entry is not returned", findEndtry("cn=cn5," + this.baseDn, entries));
            Assert.assertNotNull("The matching entry is not returned", findEndtry("cn=cn6," + this.baseDn, entries));
            Assert.assertTrue("All attributes must be returned",
                              findEndtry("cn=cn5," + this.baseDn, entries).getAttributes().size() >= 2);
            Assert.assertTrue("All attributes must be returned",
                              findEndtry("cn=cn6," + this.baseDn, entries).getAttributes().size() >= 2);
            Assert.assertEquals("The requested attribute must be returned", "cn5",
                                findEndtry("cn=cn5," + this.baseDn, entries).getValues("cn").getTextValues().get(0));
            Assert.assertEquals("The requested attribute must be returned", "sn5",
                                findEndtry("cn=cn5," + this.baseDn, entries).getValues("sn").getTextValues().get(0));
            Assert.assertEquals("The requested attribute must be returned", "cn6",
                                findEndtry("cn=cn6," + this.baseDn, entries).getValues("cn").getTextValues().get(0));
            Assert.assertEquals("The requested attribute must be returned", "sn6",
                                findEndtry("cn=cn6," + this.baseDn, entries).getValues("sn").getTextValues().get(0));
        }
    }
*/
    /**
     * <p>Accuracy test. Tests the {@link LDAPSearchStrategy#search(String, Filter, List, Map)} method for proper
     * behavior.</p>
     *
     * <p>Verifies that the method queries the database tables correctly based on provided filter, content, alias
     * mapping.</p>
     *
     * @throws Exception if an unexpected error occurs. Such an error must be interpreted as test failure.
     */
    /*public void testSearch_String_Filter_List_Map_LessFilter_NoAlias_EmptyReturnList() throws Exception {
        Filter filter = new LessThanFilter("cn", "cn3");

        for (int i = 0; i < this.testedInstances.length; i++) {

            Iterator iterator
                = (Iterator) this.testedInstances[i].search(this.baseDn,
                                                            filter,
                                                            new ArrayList(),
                                                            TestDataFactory.getLDAPPersonAliasMap());
            Entry[] entries = collectEntries(iterator);

            Assert.assertEquals("Incorrect number of entries is returned", 2, entries.length);
            Assert.assertNotNull("The matching entry is not returned", findEndtry("cn=cn1," + this.baseDn, entries));
            Assert.assertNotNull("The matching entry is not returned", findEndtry("cn=cn2," + this.baseDn, entries));
            Assert.assertTrue("All attributes must be returned",
                              findEndtry("cn=cn1," + this.baseDn, entries).getAttributes().size() >= 2);
            Assert.assertTrue("All attributes must be returned",
                              findEndtry("cn=cn2," + this.baseDn, entries).getAttributes().size() >= 2);
            Assert.assertEquals("The requested attribute must be returned", "cn1",
                                findEndtry("cn=cn1," + this.baseDn, entries).getValues("cn").getTextValues().get(0));
            Assert.assertEquals("The requested attribute must be returned", "sn1",
                                findEndtry("cn=cn1," + this.baseDn, entries).getValues("sn").getTextValues().get(0));
            Assert.assertEquals("The requested attribute must be returned", "cn2",
                                findEndtry("cn=cn2," + this.baseDn, entries).getValues("cn").getTextValues().get(0));
            Assert.assertEquals("The requested attribute must be returned", "sn2",
                                findEndtry("cn=cn2," + this.baseDn, entries).getValues("sn").getTextValues().get(0));
        }
    }*/

    /**
     * <p>Accuracy test. Tests the {@link LDAPSearchStrategy#search(String, Filter, List, Map)} method for proper
     * behavior.</p>
     *
     * <p>Verifies that the method queries the database tables correctly based on provided filter, content, alias
     * mapping.</p>
     *
     * @throws Exception if an unexpected error occurs. Such an error must be interpreted as test failure.
     */
    /*public void testSearch_String_Filter_List_Map_LessOrEqualFilter_NoAlias_EmptyReturnList() throws Exception {
        Filter filter = new LessThanOrEqualToFilter("cn", "cn2");

        for (int i = 0; i < this.testedInstances.length; i++) {

            Iterator iterator
                = (Iterator) this.testedInstances[i].search(this.baseDn,
                                                            filter,
                                                            new ArrayList(),
                                                            TestDataFactory.getLDAPPersonAliasMap());
            Entry[] entries = collectEntries(iterator);

            Assert.assertEquals("Incorrect number of entries is returned", 2, entries.length);
            Assert.assertNotNull("The matching entry is not returned", findEndtry("cn=cn1," + this.baseDn, entries));
            Assert.assertNotNull("The matching entry is not returned", findEndtry("cn=cn2," + this.baseDn, entries));
            Assert.assertTrue("All attributes must be returned",
                              findEndtry("cn=cn1," + this.baseDn, entries).getAttributes().size() >= 2);
            Assert.assertTrue("All attributes must be returned",
                              findEndtry("cn=cn2," + this.baseDn, entries).getAttributes().size() >= 2);
            Assert.assertEquals("The requested attribute must be returned", "cn1",
                                findEndtry("cn=cn1," + this.baseDn, entries).getValues("cn").getTextValues().get(0));
            Assert.assertEquals("The requested attribute must be returned", "sn1",
                                findEndtry("cn=cn1," + this.baseDn, entries).getValues("sn").getTextValues().get(0));
            Assert.assertEquals("The requested attribute must be returned", "cn2",
                                findEndtry("cn=cn2," + this.baseDn, entries).getValues("cn").getTextValues().get(0));
            Assert.assertEquals("The requested attribute must be returned", "sn2",
                                findEndtry("cn=cn2," + this.baseDn, entries).getValues("sn").getTextValues().get(0));
        }
    }*/

    /**
     * <p>Accuracy test. Tests the {@link LDAPSearchStrategy#search(String, Filter, List, Map)} method for proper
     * behavior.</p>
     *
     * <p>Verifies that the method queries the database tables correctly based on provided filter, content, alias
     * mapping.</p>
     *
     * @throws Exception if an unexpected error occurs. Such an error must be interpreted as test failure.
     */
    public void testSearch_String_Filter_List_Map_InFilter_NoAlias_EmptyReturnList() throws Exception {
        Filter filter = new InFilter("cn", Arrays.asList(new String[] {"cn1", "cn2", "cn5", "cn6"}));

        for (int i = 0; i < this.testedInstances.length; i++) {

            Iterator iterator
                = (Iterator) this.testedInstances[i].search(this.baseDn,
                                                            filter,
                                                            new ArrayList(),
                                                            TestDataFactory.getLDAPPersonAliasMap());
            Entry[] entries = collectEntries(iterator);

            Assert.assertEquals("Incorrect number of entries is returned", 4, entries.length);
            Assert.assertNotNull("The matching entry is not returned", findEndtry("cn=cn1," + this.baseDn, entries));
            Assert.assertNotNull("The matching entry is not returned", findEndtry("cn=cn2," + this.baseDn, entries));
            Assert.assertNotNull("The matching entry is not returned", findEndtry("cn=cn5," + this.baseDn, entries));
            Assert.assertNotNull("The matching entry is not returned", findEndtry("cn=cn6," + this.baseDn, entries));
            Assert.assertTrue("All attributes must be returned",
                              findEndtry("cn=cn1," + this.baseDn, entries).getAttributes().size() >= 2);
            Assert.assertTrue("All attributes must be returned",
                              findEndtry("cn=cn2," + this.baseDn, entries).getAttributes().size() >= 2);
            Assert.assertTrue("All attributes must be returned",
                              findEndtry("cn=cn5," + this.baseDn, entries).getAttributes().size() >= 2);
            Assert.assertTrue("All attributes must be returned",
                              findEndtry("cn=cn6," + this.baseDn, entries).getAttributes().size() >= 2);
            Assert.assertEquals("The requested attribute must be returned", "cn1",
                                findEndtry("cn=cn1," + this.baseDn, entries).getValues("cn").getTextValues().get(0));
            Assert.assertEquals("The requested attribute must be returned", "sn1",
                                findEndtry("cn=cn1," + this.baseDn, entries).getValues("sn").getTextValues().get(0));
            Assert.assertEquals("The requested attribute must be returned", "cn2",
                                findEndtry("cn=cn2," + this.baseDn, entries).getValues("cn").getTextValues().get(0));
            Assert.assertEquals("The requested attribute must be returned", "sn2",
                                findEndtry("cn=cn2," + this.baseDn, entries).getValues("sn").getTextValues().get(0));
            Assert.assertEquals("The requested attribute must be returned", "cn5",
                                findEndtry("cn=cn5," + this.baseDn, entries).getValues("cn").getTextValues().get(0));
            Assert.assertEquals("The requested attribute must be returned", "sn5",
                                findEndtry("cn=cn5," + this.baseDn, entries).getValues("sn").getTextValues().get(0));
            Assert.assertEquals("The requested attribute must be returned", "cn6",
                                findEndtry("cn=cn6," + this.baseDn, entries).getValues("cn").getTextValues().get(0));
            Assert.assertEquals("The requested attribute must be returned", "sn6",
                                findEndtry("cn=cn6," + this.baseDn, entries).getValues("sn").getTextValues().get(0));
        }
    }

    /**
     * <p>Finds an entry matching the specified DN.</p>
     *
     * @param dn a <code>String</code> providing the DN for requested entry.
     * @param entries an <code>Entry</code> array providing the entires returned by the search.
     * @return an <code>Entry</code> matching the specified DN or <code>null</code> if such an entry is not found.
     */
    private Entry findEndtry(String dn, Entry[] entries) {
        for (int i = 0; i < entries.length; i++) {
            if (entries[i].getDn().equals(dn)) {
                return entries[i];
            }
        }
        return null;
    }

    /**
     * <p>Gets the entries provided by the specified iterator.</p>
     *
     * @param iterator an <code>Iterator</code> over the entries.
     * @return an <code>Entry</code> array providing the entries.
     */
    private Entry[] collectEntries(Iterator iterator) {
        List result = new ArrayList();
        while (iterator.hasNext()) {
            result.add(iterator.next());
            System.out.println(((Entry) result.get(result.size() - 1)).getDn());
        }
        return (Entry[]) result.toArray(new Entry[0]);
    }

    /**
     * <p>Populates the LDAP data store with sample entries.</p>
     *
     * @throws Exception if an SQL error occurs.
     */
    private void insertData(LDAPSDKConnection connection) throws Exception {
        Entry entry;
        entry = new Entry("cn=cn1," + this.baseDn);
        entry.setAttribute("objectClass", new Values("person"));
        entry.setAttribute("cn", new Values("cn1"));
        entry.setAttribute("sn", new Values("sn1"));
        connection.addEntry(entry);
        entry = new Entry("cn=cn2," + this.baseDn);
        entry.setAttribute("objectClass", new Values("person"));
        entry.setAttribute("cn", new Values("cn2"));
        entry.setAttribute("sn", new Values("sn2"));
        connection.addEntry(entry);
        entry = new Entry("cn=cn3," + this.baseDn);
        entry.setAttribute("objectClass", new Values("person"));
        entry.setAttribute("cn", new Values("cn3"));
        entry.setAttribute("sn", new Values("sn3"));
        connection.addEntry(entry);
        entry = new Entry("cn=cn4," + this.baseDn);
        entry.setAttribute("objectClass", new Values("person"));
        entry.setAttribute("cn", new Values("cn4"));
        entry.setAttribute("sn", new Values("sn4"));
        connection.addEntry(entry);
        entry = new Entry("cn=cn5," + this.baseDn);
        entry.setAttribute("objectClass", new Values("person"));
        entry.setAttribute("cn", new Values("cn5"));
        entry.setAttribute("sn", new Values("sn5"));
        connection.addEntry(entry);
        entry = new Entry("cn=cn6," + this.baseDn);
        entry.setAttribute("objectClass", new Values("person"));
        entry.setAttribute("cn", new Values("cn6"));
        entry.setAttribute("sn", new Values("sn6"));
        connection.addEntry(entry);
    }

    /**
     * <p>Deletes the sample entries from LDAP data store.</p>
     *
     * @throws Exception if an SQL error occurs.
     */
    private void deleteEntries(LDAPSDKConnection connection) throws Exception {
        try {
            connection.deleteEntry("cn=cn1," + this.baseDn);
        } catch (LDAPSDKException e) {
        }
        try {
            connection.deleteEntry("cn=cn2," + this.baseDn);
        } catch (LDAPSDKException e) {
        }
        try {
            connection.deleteEntry("cn=cn3," + this.baseDn);
        } catch (LDAPSDKException e) {
        }
        try {
            connection.deleteEntry("cn=cn4," + this.baseDn);
        } catch (LDAPSDKException e) {
        }
        try {
            connection.deleteEntry("cn=cn5," + this.baseDn);
        } catch (LDAPSDKException e) {
        }
        try {
            connection.deleteEntry("cn=cn6," + this.baseDn);
        } catch (LDAPSDKException e) {
        }
    }
}
