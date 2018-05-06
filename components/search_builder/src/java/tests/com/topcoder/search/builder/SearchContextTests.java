/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder;

import java.util.HashMap;
import java.util.Map;

import com.topcoder.search.builder.database.EqualsFragmentBuilder;
import com.topcoder.search.builder.filter.EqualToFilter;
import com.topcoder.search.builder.filter.LessThanFilter;
import com.topcoder.util.classassociations.ClassAssociator;

import junit.framework.TestCase;
/**
 * The unit test of <code>SearchContext</code>.
 *
 * @author TCSDEVELOPER
 * @version 1.3
 */
public class SearchContextTests extends TestCase {
    /**
     * The SearchContext instance used to test.
     */
    private SearchContext searchContext = null;
    /**
     * The alias map.
     */
    private Map alias = null;
    /**
     * The class ClassAssociator.
     */
    private ClassAssociator classAssociator = null;
    /**
     * The setUp.
     *
     * @throws Exception to JUnit
     */
    public void setUp() throws Exception {
        alias = new HashMap();
        alias.put("name", "real name");
        alias.put("age", "real age");
        classAssociator = new ClassAssociator();
        classAssociator.addClassAssociation(EqualToFilter.class, new EqualsFragmentBuilder());

        searchContext = new SearchContext(classAssociator, alias);
    }
    /**
     * The accuracy test of the constructor with Map.
     *
     */
    public void testconstructor_accuracy1() {
        assertNotNull("can not construct the SearchContext.", new SearchContext(alias));
    }
    /**
     * The accuracy test of the constructor with Map and ClassAssociator.
     *
     */
    public void testconstructor_accuracy2() {
        assertNotNull("can not construct the SearchContext.", new SearchContext(classAssociator, alias));
    }
    /**
     * The failure test of the constructor with Map,
     * since the alias is invalid,
     * IllegalArgumentException should be thrown.
     *
     */
    public void testconstructor_faulire1() {
        try {
            new SearchContext(null);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }
    /**
     * The failure test of the constructor with Map,
     * since the alias is invalid,
     * IllegalArgumentException should be thrown.
     *
     */
    public void testconstructor_faulire2() {
        alias.put(null, "aa");
        try {
            new SearchContext(alias);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }
    /**
     * The failure test of the constructor with Map,
     * since the alias is invalid,
     * IllegalArgumentException should be thrown.
     *
     */
    public void testconstructor_faulire3() {
        alias.put("  ", "aa");
        try {
            new SearchContext(alias);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }
    /**
     * The failure test of the constructor with Map,
     * since the alias is invalid,
     * IllegalArgumentException should be thrown.
     *
     */
    public void testconstructor_faulire4() {
        alias.put("aa", null);
        try {
            new SearchContext(alias);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }
    /**
     * The failure test of the constructor with Map,
     * since the alias is invalid,
     * IllegalArgumentException should be thrown.
     *
     */
    public void testconstructor_faulire5() {
        alias.put("aa", "   ");
        try {
            new SearchContext(alias);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }
    /**
     * The failure test of the constructor with Map,
     * since the alias is invalid,
     * IllegalArgumentException should be thrown.
     *
     */
    public void testconstructor_faulire6() {
        alias.put("aa", new Object());
        try {
            new SearchContext(alias);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }
    /**
     * The failure test of the constructor with Map,
     * since the alias is invalid,
     * IllegalArgumentException should be thrown.
     *
     */
    public void testconstructor_faulire7() {
        alias.put(new Object(), "aa");
        try {
            new SearchContext(alias);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }
    /**
     * The failure test of the constructor with Map and ClassAssociator,
     * since the alias is invalid,
     * IllegalArgumentException should be thrown.
     *
     */
    public void testconstructor_faulire8() {
        try {
            new SearchContext(classAssociator, null);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }
    /**
     * The failure test of the constructor with Map and ClassAssociator,
     * since the alias is invalid,
     * IllegalArgumentException should be thrown.
     *
     */
    public void testconstructor_faulire9() {
        alias.put(null, "aa");
        try {
            new SearchContext(classAssociator, alias);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }
    /**
     * The failure test of the constructor with Map and ClassAssociator,
     * since the alias is invalid,
     * IllegalArgumentException should be thrown.
     *
     */
    public void testconstructor_faulire10() {
        alias.put("  ", "aa");
        try {
            new SearchContext(classAssociator, alias);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }
    /**
     * The failure test of the constructor with Map and ClassAssociator,
     * since the alias is invalid,
     * IllegalArgumentException should be thrown.
     *
     */
    public void testconstructor_faulire11() {
        alias.put("aa", null);
        try {
            new SearchContext(classAssociator, alias);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }
    /**
     * The failure test of the constructor with Map and ClassAssociator,
     * since the alias is invalid,
     * IllegalArgumentException should be thrown.
     *
     */
    public void testconstructor_faulire12() {
        alias.put("aa", "   ");
        try {
            new SearchContext(classAssociator, alias);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }
    /**
     * The failure test of the constructor with Map and ClassAssociator,
     * since the alias is invalid,
     * IllegalArgumentException should be thrown.
     *
     */
    public void testconstructor_faulire13() {
        alias.put("aa", new Object());
        try {
            new SearchContext(classAssociator, alias);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }
    /**
     * The failure test of the constructor with Map and ClassAssociator,
     * since the alias is invalid,
     * IllegalArgumentException should be thrown.
     *
     */
    public void testconstructor_faulire14() {
        alias.put(new Object(), "aa");
        try {
            new SearchContext(classAssociator, alias);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }
    /**
     * The test of method getSearchString.
     *
     */
    public void testgetSearchString() {
        assertNotNull("should not be null.", searchContext.getSearchString());
    }
    /**
     * The test of method getBindableParameters.
     *
     */
    public void testgetBindableParameters() {
        assertNotNull("should not be null.", searchContext.getBindableParameters());

        searchContext.getBindableParameters().add(new Object());

        assertTrue("The size should be > 0.", searchContext.getBindableParameters().size() > 0);
    }
    /**
     * The failure test of getFragmentBuilder,
     * the filter is null,
     * IllegalArgumentException should be thrown.
     *
     */
    public void testgetFragmentBuilder_failure() {
        try {
            searchContext.getFragmentBuilder(null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }
    /**
     * The accuracy test of getFragmentBuilder,
     * both return null and non_null.
     *
     */
    public void testgetFragmentBuilder_accuracy1() {
        assertNull("No builder can be retrived.",
                searchContext.getFragmentBuilder(new LessThanFilter("The age", new Long(5))));

        assertNotNull("builder can be retrived.",
                searchContext.getFragmentBuilder(new EqualToFilter("The age", new Long(5))));
    }
    /**
     * The accuracy test of getFragmentBuilder,
     * the FragmentBuilder is not supported.
     *
     */
    public void testgetFragmentBuilder_accuracy2() {
        searchContext = new SearchContext(alias);
        assertNull("No builder can be retrived.",
                searchContext.getFragmentBuilder(new LessThanFilter("The age", new Long(5))));

        assertNull("no builder can be retrived.",
                searchContext.getFragmentBuilder(new EqualToFilter("The age", new Long(5))));
    }
    /**
     * The accuracy test of getFieldName,
     * the alias is found.
     *
     */
    public void testgetFieldName_accuracy1() {
        assertEquals("The name should be equals with the got one.", "real age",
                searchContext.getFieldName("age"));
    }
    /**
     * The accuracy test of getFieldName,
     * the alias is not found.
     *
     */
    public void testgetFieldName_accuracy2() {
        assertNull("The name should be null.", searchContext.getFieldName("not found"));
    }
}
