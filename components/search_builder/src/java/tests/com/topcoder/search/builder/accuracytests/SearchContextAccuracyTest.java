/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.accuracytests;

import com.topcoder.search.builder.SearchContext;
import com.topcoder.search.builder.SearchFragmentBuilder;
import com.topcoder.search.builder.ValidationResult;
import com.topcoder.search.builder.filter.NullFilter;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.util.classassociations.ClassAssociator;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.Iterator;
import java.util.Set;
import java.util.Map;
import java.util.List;

/**
 * <p>An accuracy test for {@link SearchContext} class. Tests the methods for proper
 * handling of valid input data and producing accurate results. Passes the valid arguments to the methods and verifies
 * that either the state of the tested instance have been changed appropriately or a correct result is produced by the
 * method.</p>
 *
 * @author isv
 * @version 1.0
 * @since 1.3
 */
public class SearchContextAccuracyTest extends TestCase {

    /**
     * <p>The instances of {@link SearchContext} which are tested. These instances are initialized in {@link #setUp()}
     * method and released in {@link #tearDown()} method. Each instance is initialized using a separate constructor
     * provided by the tested class.<p>
     */
    private SearchContext[] testedInstances = null;

    /**
     * <p>Gets the test suite for {@link SearchContext} class.</p>
     *
     * @return a <code>TestSuite</code> providing the tests for {@link SearchContext} class.
     */
    public static Test suite() {
        return new TestSuite(SearchContextAccuracyTest.class);
    }

    /**
     * <p>Sets up the fixture. This method is called before a test is executed.</p>
     *
     * @throws Exception if any error occurs.
     */
    protected void setUp() throws Exception {
        super.setUp();
        this.testedInstances = new SearchContext[2];
        this.testedInstances[0] = new SearchContext(TestDataFactory.getSearchContextAliasMap());
        this.testedInstances[1] = new SearchContext(TestDataFactory.getSearchContextClassAssociator(),
                                                    TestDataFactory.getSearchContextAliasMap());
    }

    /**
     * <p>Tears down the fixture. This method is called after a test is executed.</p>
     *
     * @throws Exception if any error occurs.
     */
    protected void tearDown() throws Exception {
        this.testedInstances = null;
        super.tearDown();
    }

    /**
     * <p>Accuracy test. Tests the {@link SearchContext#SearchContext(Map)} constructor for proper behavior.</p>
     *
     * <p>Verifies that the internal state of the instance is initialized as expected.</p>
     */
    public void testConstructor_Map() {
        Assert.assertNotNull("The 'searchString' property is not initialized", testedInstances[0].getSearchString());
        Assert.assertNotNull("The 'bindableParameters' property is not initialized",
                             testedInstances[0].getBindableParameters());
        Assert.assertNull("The 'fragmentBuilders' property is not initialized properly",
                          testedInstances[0].getFragmentBuilder(new NullFilter("NullFilter")));
    }

    /**
     * <p>Accuracy test. Tests the {@link SearchContext#SearchContext(ClassAssociator, Map)} constructor for proper
     * behavior.</p>
     *
     * <p>Verifies that the internal state of the instance is initialized as expected.</p>
     */
    public void testConstructor_ClassAssociator_Map() {
        Assert.assertNotNull("The 'searchString' property is not initialized", testedInstances[1].getSearchString());
        Assert.assertNotNull("The 'bindableParameters' property is not initialized",
                             testedInstances[1].getBindableParameters());
        Filter[] filters = TestDataFactory.getSearchContextFilters();
        for (int i = 0; i < filters.length; i++) {
            Assert.assertNotNull("The 'fragmentBuilders' property is not initialized properly",
                                 testedInstances[1].getFragmentBuilder(filters[i]));
        }
    }

    /**
     * <p>Accuracy test. Tests the {@link SearchContext#getFieldName(String)} method for proper behavior.</p>
     *
     * <p>Verifies that the method resolves the field name successfully if the alias is found.</p>
     */
    public void testGetFieldName_String_ExistingAlias() {
        String alias, fieldName;

        Map aliasMap = TestDataFactory.getSearchContextAliasMap();
        Set aliases = aliasMap.keySet();
        for (int i = 0; i < this.testedInstances.length; i++) {
            for (Iterator iterator = aliases.iterator(); iterator.hasNext();) {
                alias = (String) iterator.next();
                fieldName = this.testedInstances[i].getFieldName(alias);
                Assert.assertEquals("Does not return the correct field name", aliasMap.get(alias), fieldName);
            }
        }
    }

    /**
     * <p>Accuracy test. Tests the {@link SearchContext#getFieldName(String)} method for proper behavior.</p>
     *
     * <p>Verifies that the method returns <code>null</code> if the alias is not found.</p>
     */
    public void testGetFieldName_String_NonExistingAlias() {
        String alias, fieldName;

        Map aliasMap = TestDataFactory.getSearchContextAliasMap();
        Set aliases = aliasMap.keySet();
        for (int i = 0; i < this.testedInstances.length; i++) {
            for (Iterator iterator = aliases.iterator(); iterator.hasNext();) {
                alias = (String) iterator.next();
                fieldName = this.testedInstances[i].getFieldName(alias + "?");
                Assert.assertNull("Does not return the correct field name", fieldName);
            }
        }
    }

    /**
     * <p>Accuracy test. Tests the {@link SearchContext#getBindableParameters()} method for proper behavior.</p>
     *
     * <p>Verifies that the method returns the same list on each method call.</p>
     */
    public void testGetBindableParameters_SameList() {
        List[] returned = new List[10];
        for (int i = 0; i < this.testedInstances.length; i++) {
            for (int j = 0; j < returned.length; j++) {
                returned[j] = testedInstances[i].getBindableParameters();
            }
            for (int j = 0; j < returned.length - 1; j++) {
                Assert.assertSame("The method does not return the same List", returned[j], returned[j + 1]);
            }
        }
    }

    /**
     * <p>Accuracy test. Tests the {@link SearchContext#getBindableParameters()} method for proper behavior.</p>
     *
     * <p>Verifies that the method returns the direct reference to a list of parameters on each method call so any
     * modifications to returned list are reflected in the state of the <code>SearchContext</code> instance.</p>
     */
    public void testGetBindableParameters_InternalList() {
        Integer item;
        List parameters;
        for (int i = 0; i < this.testedInstances.length; i++) {
            for (int j = 0; j < 10; j++) {
                this.testedInstances[i].getBindableParameters().add(new Integer(j));
                parameters = this.testedInstances[i].getBindableParameters();
                Assert.assertEquals("The list of parameters is not updated correctly", j + 1, parameters.size());
                for (int k = 0; k < parameters.size(); k++) {
                    item = (Integer) parameters.get(k);
                    Assert.assertEquals("The list of parameters is not updated correctly", k , item.intValue());
                }
            }
            parameters = this.testedInstances[i].getBindableParameters();
            parameters.clear();
            parameters = this.testedInstances[i].getBindableParameters();
            Assert.assertEquals("The list of parameters is not cleared", 0, parameters.size());
        }
    }

    /**
     * <p>Accuracy test. Tests the {@link SearchContext#getSearchString()} method for proper behavior.</p>
     *
     * <p>Verifies that the method returns the same buffer on each method call.</p>
     */
    public void testGetSearchString_SameBuffer() {
        StringBuffer[] returned = new StringBuffer[10];
        for (int i = 0; i < this.testedInstances.length; i++) {
            for (int j = 0; j < returned.length; j++) {
                returned[j] = testedInstances[i].getSearchString();
            }
            for (int j = 0; j < returned.length - 1; j++) {
                Assert.assertSame("The method does not return the same StringBuffer", returned[j], returned[j + 1]);
            }
        }
    }

    /**
     * <p>Accuracy test. Tests the {@link SearchContext#getSearchString()} method for proper behavior.</p>
     *
     * <p>Verifies that the method returns the direct reference to a search string on each method call so any
     * modifications to returned buffer are reflected in the state of the <code>SearchContext</code> instance.</p>
     */
    public void testGetSearchString_InternalBuffer() {
        char ch;
        StringBuffer buffer;
        for (int i = 0; i < this.testedInstances.length; i++) {
            for (int j = 0; j < 10; j++) {
                this.testedInstances[i].getSearchString().append(String.valueOf(j));
                buffer = this.testedInstances[i].getSearchString();
                Assert.assertEquals("The search string is not updated correctly", j + 1, buffer.length());
                for (int k = 0; k < buffer.length(); k++) {
                    ch = buffer.toString().charAt(k);
                    Assert.assertEquals("The search string is not updated correctly",
                                        String.valueOf(k) , String.valueOf(ch));
                }
            }
            buffer = this.testedInstances[i].getSearchString();
            buffer.setLength(0);
            buffer = this.testedInstances[i].getSearchString();
            Assert.assertEquals("The search string is not cleared", 0, buffer.length());
        }
    }

    /**
     * <p>Accuracy test. Tests the {@link SearchContext#getFragmentBuilder(Filter)} method for proper behavior.</p>
     *
     * <p>Verifies that the method returns NULL on each method call if it was instantiated without
     * {@link ClassAssociator} instance provided.</p>
     */
    public void testGetFragmentBuilder_Filter_WithoutClassAssociator() {
        Filter[] filters = TestDataFactory.getSearchContextFilters();
        for (int i = 0; i < filters.length; i++) {
            Assert.assertNull("A fragment builder is returned by mistake",
                              this.testedInstances[0].getFragmentBuilder(filters[i]));
        }
    }

    /**
     * <p>Accuracy test. Tests the {@link SearchContext#getFragmentBuilder(Filter)} method for proper behavior.</p>
     *
     * <p>Verifies that the method returns appropriate {@link SearchFragmentBuilder} on each method call if it was
     * instantiated with {@link ClassAssociator} instance provided and the provided filter is mapped to some builder.
     * </p>
     */
    public void testGetFragmentBuilder_Filter_WithClassAssociator() {
        Filter[] filters = TestDataFactory.getSearchContextFilters();
        SearchFragmentBuilder[] builders = TestDataFactory.getSearchContextFragmentBuilders();
        SearchFragmentBuilder builder;
        for (int i = 0; i < filters.length; i++) {
            builder = this.testedInstances[1].getFragmentBuilder(filters[i]);
            Assert.assertNotNull("A fragment builder is not returned", builder);
            Assert.assertEquals("Incorrect fragment builder is returned",
                                builders[i].getClass().getName(), builder.getClass().getName());
        }
    }

    /**
     * <p>Accuracy test. Tests the {@link SearchContext#getFragmentBuilder(Filter)} method for proper behavior.</p>
     *
     * <p>Verifies that the method returns <code>null</code> on each method call if it was* instantiated with
     * {@link ClassAssociator} instance provided and the provided filter is not mapped to some builder.</p>
     */
    public void testGetFragmentBuilder_Filter_UnknownFilter() {
        SearchFragmentBuilder builder = this.testedInstances[1].getFragmentBuilder(new Filter() {
            public ValidationResult isValid(Map validators, Map alias) { return null; }
            public int getFilterType() { return 0; }
            public Object clone() {return null;}
        });
        Assert.assertNull("A fragment builder is returned by mistake", builder);
    }
}
