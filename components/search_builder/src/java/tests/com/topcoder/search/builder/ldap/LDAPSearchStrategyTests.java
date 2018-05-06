/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.ldap;

import com.topcoder.search.builder.SearchBuilderConfigurationException;
import com.topcoder.search.builder.SearchContext;
import com.topcoder.search.builder.TestHelper;
import com.topcoder.search.builder.UnrecognizedFilterException;
import com.topcoder.search.builder.filter.AndFilter;
import com.topcoder.search.builder.filter.BetweenFilter;
import com.topcoder.search.builder.filter.EqualToFilter;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.filter.GreaterThanFilter;
import com.topcoder.search.builder.filter.GreaterThanOrEqualToFilter;
import com.topcoder.search.builder.filter.InFilter;
import com.topcoder.search.builder.filter.LessThanFilter;
import com.topcoder.search.builder.filter.LessThanOrEqualToFilter;
import com.topcoder.search.builder.filter.NotFilter;
import com.topcoder.search.builder.filter.OrFilter;

import com.topcoder.util.net.ldap.sdkinterface.LDAPSDK;
import com.topcoder.util.net.ldap.sdkinterface.LDAPSDKConnection;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * Unit test cases for LDAPSearchStrategy.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.3
 */
public class LDAPSearchStrategyTests extends TestCase {
    /**
     * The host for test  LDAPConnectionInformation.
     */
    private static final String HOST = "localhost";

    /**
     * The port for test LDAPConnectionInformation.
     */
    private static final int PORT = 8888;

    /**
     * The scope for test LDAPConnectionInformation.
     */
    private static final int SCOPE = LDAPSDKConnection.SCOPE_BASE;

    /**
     * The context.
     */
    private static final String CONTEXT = "dc=guessant,dc=org";

    /**
     * The isSecure value for test LDAPConnectionInformation.
     */
    private static final boolean ISS = false;

    /**
     * The dnroot value for test LDAPConnectionInformation.
     */
    private static final String DNROOT = "dnroot";

    /**
     * The password value for test LDAPConnectionInformation.
     */
    private static final String PASSWORD = "password";

    /**
     * The LDAPSDKFactory class name for test LDAPConnectionInformation.
     */
    private static final String CLASS_NAME = "com.topcoder.util.net.ldap.sdkinterface.netscape.NetscapeFactory";

    /**
     * The namespace.
     */
    private static final String NAMESPACE = "LDAPSearchStrategy";

    /**
     * the instance of LDAPConnectionInformation for test.
     */
    private static LDAPConnectionInformation ldapInfo = null;

    /**
     * LDAPSDK instance for test LDAPConnectionInformation.
     */
    private LDAPSDK factory = null;

    /**
     * The associations Map.
     */
    private Map associations = null;

    /**
     * The returnFields.
     */
    private List returnFields = null;

    /**
     * The map of alias name and real name.
     *
     */
    private Map aliasMap = null;

    /**
     * The LDAPSearchStrategy instance used to test.
     */
    private LDAPSearchStrategy strategy = null;

    /**
     * The setUp of the unit test.
     *
     * @throws Exception to Junit
     */
    protected void setUp() throws Exception {
        //add the configuration
        TestHelper.clearConfig();
        TestHelper.addConfig();

        aliasMap = new HashMap();
        aliasMap.put("sb", "searchbuild");
        aliasMap.put("sn", "sn");
        factory = new LDAPSDK(CLASS_NAME);

        ldapInfo = new LDAPConnectionInformation(factory, HOST, PORT, ISS,
                SCOPE, DNROOT, PASSWORD);

        associations = new HashMap();

        associations.put(EqualToFilter.class, new EqualsFragmentBuilder());

        strategy = new LDAPSearchStrategy(NAMESPACE);

        returnFields = new ArrayList();
        returnFields.add("sn");

        //add the Entries
        TestHelper.addEntries();
    }

    /**
     * The tearDown of the unit test.
     *
     * @throws Exception to JUnit
     */
    protected void tearDown() throws Exception {
        //remove the configuration in tearDown
        TestHelper.clearConfig();

        //delete the entries
        TestHelper.delEntries();
    }

    /**
     * The accuracy test of the constructor with namespace.
     *
     */
    public void testconstructor_withNamespace() {
        assertNotNull("can not create the LDAPSearchStrategy.", strategy);
    }

    /**
     * The accuracy test of the constructor with Parameters.
     *
     */
    public void testconstructor_withParameter() {
        assertNotNull("can not create the LDAPSearchStrategy.",
            new LDAPSearchStrategy(ldapInfo, associations));
    }

    /**
     * The failure test for the constructor,
     * fail for invalid parameter,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testconstructor_failure1() throws Exception {
        try {
            new LDAPSearchStrategy(null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * The failure test for the constructor,
     * fail for invalid parameter,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testconstructor_failure2() throws Exception {
        try {
            new LDAPSearchStrategy("   ");
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * The failure test for the constructor,
     * fail for invalid parameter,
     * SearchBuilderConfigurationException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testconstructor_failure11() throws Exception {
        try {
            new LDAPSearchStrategy("unknown");
            fail("SearchBuilderConfigurationException should be thrown.");
        } catch (SearchBuilderConfigurationException e) {
            //pass
        }
    }

    /**
     * The failure test for the constructor,
     * fail for invalid parameter,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testconstructor_failure3() throws Exception {
        try {
            new LDAPSearchStrategy(null, associations);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * The failure test for the constructor,
     * fail for invalid parameter,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testconstructor_failure4() throws Exception {
        try {
            new LDAPSearchStrategy(ldapInfo, null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * The failure test for the constructor,
     * fail for invalid parameter,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testconstructor_failure5() throws Exception {
        associations.put(null, new EqualsFragmentBuilder());

        try {
            new LDAPSearchStrategy(ldapInfo, associations);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * The failure test for the constructor,
     * fail for invalid parameter,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testconstructor_failure6() throws Exception {
        associations.put(new Object(), new EqualsFragmentBuilder());

        try {
            new LDAPSearchStrategy(ldapInfo, associations);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * The failure test for the constructor,
     * fail for invalid parameter,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testconstructor_failure7() throws Exception {
        associations.put(EqualToFilter.class, null);

        try {
            new LDAPSearchStrategy(ldapInfo, associations);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * The failure test for the constructor,
     * fail for invalid parameter,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testconstructor_failure8() throws Exception {
        associations.put(EqualToFilter.class, new Object());

        try {
            new LDAPSearchStrategy(ldapInfo, associations);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * The failure test for the constructor,
     * fail for invalid parameter,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testconstructor_failure9() throws Exception {
        associations.put(Object.class, new EqualsFragmentBuilder());

        try {
            new LDAPSearchStrategy(ldapInfo, associations);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * Test the method connect.
     *
     * @throws Exception to JUnit
     */
    public void testconnect() throws Exception {
        LDAPSDKConnection connection = null;

        try {
            connection = strategy.connect();
            assertNotNull("can not connect.", connection);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * Test the method SearchContext.
     *
     * @throws Exception to JUnit
     */
    public void testbuildSearchContext() throws Exception {
        SearchContext searchContext = strategy.buildSearchContext(CONTEXT,
                new EqualToFilter("sb", "aa"), new ArrayList(), aliasMap);

        assertEquals("The searchString should be same.", "(searchbuild=aa)",
            searchContext.getSearchString().toString());
    }

    /**
     * Test failure test of the method SearchContext,
     * UnrecognizedFilterException will be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testbuildSearchContext_failure() throws Exception {
        strategy = new LDAPSearchStrategy(ldapInfo, associations);

        try {
            strategy.buildSearchContext(CONTEXT,
                new LessThanFilter("sb", "aa"), new ArrayList(), aliasMap);
            fail("UnrecognizedFilterException should be thrown.");
        } catch (UnrecognizedFilterException e) {
            //pass
        }
    }

    /**
     * Test search the ldap with the EqualToFilter.
     * @throws Exception tp junit
     */
    public void testLDAPSearchEqualToFilter() throws Exception {
        EqualToFilter filter = new EqualToFilter("sn", "2");
        Iterator it = (Iterator) strategy.search(CONTEXT, filter, returnFields,
                aliasMap);

        //1 person with the sn = "2"
        assertEquals("1 person under the filter", 1,
            TestHelper.getItemNumber(it));
    }

    /**
     * Test search the ldap with the AndFilter.
     * @throws Exception to junit
     */
    public void testLDAPSearchAndFilter() throws Exception {
        Filter filter1 = new EqualToFilter("sn", "2");
        Filter filter2 = new EqualToFilter("sn", "1");
        Filter filter = new AndFilter(filter1, filter2);
        Iterator it = (Iterator) strategy.search(CONTEXT, filter, returnFields,
                aliasMap);

        //0 person with the sn = "2" & "1"
        assertTrue("0 person under the filter",
            0 == TestHelper.getItemNumber(it));
    }

    /**
     * Test search the ldap with the OrFilter.
     * @throws Exception to junit
     */
    public void testLDAPSearchOrFilter() throws Exception {
        Filter filter1 = new EqualToFilter("sn", "2");
        Filter filter2 = new EqualToFilter("sn", "1");
        OrFilter filter = new OrFilter(filter1, filter2);
        Iterator it = (Iterator) strategy.search(CONTEXT, filter, returnFields,
                aliasMap);

        //2 person with the sn = "2" | "1"
        assertEquals("2 person under the filter", 2,
            TestHelper.getItemNumber(it));
    }

    /**
     * Test search the ldap with the NotFilter.
     * @throws Exception to junit
     */
    public void testLDAPSearchNotFilter() throws Exception {
        Filter filter1 = new EqualToFilter("sn", "2");
        Filter filter2 = new EqualToFilter("sn", "1");
        Filter filter3 = new OrFilter(filter1, filter2);
        Filter filter = new NotFilter(filter3);
        Iterator it = (Iterator) strategy.search(CONTEXT, filter, returnFields,
                aliasMap);

        //7 items with the sn != "2" | "1"
        assertEquals("7 items under the filter", 7, TestHelper.getItemNumber(it));
    }

    /**
     * Test search the ldap with the InFilter.
     * @throws Exception to junit
     */
    public void testLDAPSearchInFilter() throws Exception {
        List values = new ArrayList();
        values.add("2");
        values.add("5");
        values.add("6");

        Filter filter = new InFilter("sn", values);
        Iterator it = (Iterator) strategy.search(CONTEXT, filter, returnFields,
                aliasMap);

        //2 person with the sn in ("2", "5", "6")
        assertEquals("2 person under the filter", 2,
            TestHelper.getItemNumber(it));
    }

    /**
     * Test search the ldap with the LessThanFilter.
     * @throws Exception to junit
     */
    public void testLDAPSearchLessThanFilter() throws Exception {
        Filter filter = new LessThanFilter("sb", "sb1");
        Iterator it = (Iterator) strategy.search(CONTEXT, filter, returnFields,
                aliasMap);

        //no role with the searchbuilder lessthan "sb1"
        assertEquals("0 role under the filter", 0, TestHelper.getItemNumber(it));
    }

    /**
     * Test search the ldap with the LessThanFilter.
     * @throws Exception to junit
     */
    public void testLDAPSearchLessThanOrEqualtoFilter()
        throws Exception {
        Filter filter = new LessThanOrEqualToFilter("sb", "sb1");
        Iterator it = (Iterator) strategy.search(CONTEXT, filter, returnFields,
                aliasMap);

        //1 role with the searchbuilder lessthan "sb1"
        assertEquals("1 role under the filter", 1, TestHelper.getItemNumber(it));
    }

    /**
     * Test search the ldap with the GreaterThanFilter.
     * @throws Exception to junit
     */
    public void testLDAPSearchGreatThanFilter() throws Exception {
        Filter filter = new GreaterThanFilter("sb", "sb2");
        Iterator it = (Iterator) strategy.search(CONTEXT, filter, returnFields,
                aliasMap);

        //2 role with the searchbuilder name greater than "sb2"
        assertEquals("2 roles under the filter", 2, TestHelper.getItemNumber(it));
    }

    /**
     * Test search the ldap with the GreaterThanFilter.
     * @throws Exception to junit
     */
    public void testLDAPSearchGreatThanOrEqualtoFilter()
        throws Exception {
        Filter filter = new GreaterThanOrEqualToFilter("sb", "sb2");
        Iterator it = (Iterator) strategy.search(CONTEXT, filter, returnFields,
                aliasMap);

        //3 role with the searchbuilder name greater than "sb2"
        assertEquals("3 roles under the filter", 3, TestHelper.getItemNumber(it));
    }

    /**
     * Test search the ldap with the BetweenFilter.
     * @throws Exception to junit
     */
    public void testLDAPSearchBetweenFilter() throws Exception {
        Filter filter = new BetweenFilter("sb", "sb3", "sb1");
        Iterator it = (Iterator) strategy.search(CONTEXT, filter, returnFields,
                aliasMap);

        //3 role with the searchbuilder name between than "sb3" and "sb1"
        assertEquals("3 roles under the filter", 3, TestHelper.getItemNumber(it));
    }

    /**
     * The failure test of search,
     * since the parameters are invalid,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testsearch_failure1() throws Exception {
        EqualToFilter filter = new EqualToFilter("sn", "2");

        try {
            strategy.search(null, filter, returnFields, aliasMap);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * The failure test of search,
     * since the parameters are invalid,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testsearch_failure2() throws Exception {
        EqualToFilter filter = new EqualToFilter("sn", "2");

        try {
            strategy.search("  ", filter, returnFields, aliasMap);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * The failure test of search,
     * since the parameters are invalid,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testsearch_failure3() throws Exception {
        EqualToFilter filter = new EqualToFilter("sn", "2");

        try {
            strategy.search(CONTEXT, null, returnFields, aliasMap);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * The failure test of search,
     * since the parameters are invalid,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testsearch_failure4() throws Exception {
        EqualToFilter filter = new EqualToFilter("sn", "2");

        try {
            strategy.search(CONTEXT, filter, null, aliasMap);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * The failure test of search,
     * since the parameters are invalid,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testsearch_failure5() throws Exception {
        EqualToFilter filter = new EqualToFilter("sn", "2");
        returnFields.add(null);

        try {
            strategy.search(CONTEXT, filter, returnFields, aliasMap);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * The failure test of search,
     * since the parameters are invalid,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testsearch_failure6() throws Exception {
        EqualToFilter filter = new EqualToFilter("sn", "2");
        returnFields.add("  ");

        try {
            strategy.search(CONTEXT, filter, returnFields, aliasMap);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * The failure test of search,
     * since the parameters are invalid,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testsearch_failure7() throws Exception {
        EqualToFilter filter = new EqualToFilter("sn", "2");

        try {
            strategy.search(CONTEXT, filter, returnFields, null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * The failure test of search,
     * since the parameters are invalid,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testsearch_failure8() throws Exception {
        EqualToFilter filter = new EqualToFilter("sn", "2");
        aliasMap.put(null, "aa");

        try {
            strategy.search(CONTEXT, filter, returnFields, aliasMap);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * The failure test of search,
     * since the parameters are invalid,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testsearch_failure9() throws Exception {
        EqualToFilter filter = new EqualToFilter("sn", "2");
        aliasMap.put("  ", "aa");

        try {
            strategy.search(CONTEXT, filter, returnFields, aliasMap);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * The failure test of search,
     * since the parameters are invalid,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testsearch_failure10() throws Exception {
        EqualToFilter filter = new EqualToFilter("sn", "2");
        aliasMap.put(new Object(), "aa");

        try {
            strategy.search(CONTEXT, filter, returnFields, aliasMap);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * The failure test of search,
     * since the parameters are invalid,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testsearch_failure11() throws Exception {
        EqualToFilter filter = new EqualToFilter("sn", "2");
        aliasMap.put("aa", null);

        try {
            strategy.search(CONTEXT, filter, returnFields, aliasMap);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * The failure test of search,
     * since the parameters are invalid,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testsearch_failure12() throws Exception {
        EqualToFilter filter = new EqualToFilter("sn", "2");
        aliasMap.put("aa", "  ");

        try {
            strategy.search(CONTEXT, filter, returnFields, aliasMap);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * The failure test of search,
     * since the parameters are invalid,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testsearch_failure13() throws Exception {
        EqualToFilter filter = new EqualToFilter("sn", "2");
        aliasMap.put("aa", new Object());

        try {
            strategy.search(CONTEXT, filter, returnFields, aliasMap);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }
}
