/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.filter;



/**
 * <p>
 * This abstract class is a concrete implementor of the Filter interface.
 * It is used to construct simple  Filter.
 * However, the getFilterType() method and isValid() methods are abstract,
 * which are intended to be  implemented by subclasses differently.
 * This abstract class provides some common functions shared among concrete simple filter classes.
 * </p>
 *
 * @author ShindouHikaru, TCSDEVELOPER
 * @version 1.3
 */
public abstract class AbstractSimpleFilter implements Filter {
    /**
     * It will hold the filed name of the search criterion.
     * Both can be the real name and alias name.
     *
     */
    protected String fieldName;

    /**
     * It will hold a Comparable object. For example, if a LessThan filter is built by the constrcutor,
     * then this attribute is initialized by the Comparable object in the constructor.
     * It is used to set upperThreashold and lowerThreashold attributes
     *
     */
    protected Comparable value;

    /**
     * It will hold a Comparable object representing the upper threshold of this filter.
     *
     */
    protected Comparable upperThreshold = null;

    /**
     * It will hold a Comparable object representing the lower threshold of this filter.
     *
     */
    protected Comparable lowerThreshold = null;

    /**
     * It will hold a boolean indicating if the upper threshold is inclusive.
     *
     */
    protected boolean isUpperThresholdInclusive = false;

    /**
     * It will hold a boolean indicating if the lower threshold is inclusive.
     * It is inclusive if true, exclusive otherwise
     *
     */
    protected boolean isLowerThresholdInclusive = false;

    /**
     * <p>Create a new instance,by providing the name and value.</p>
     *
     *
     * @param name the filed name of the search criterion
     * @param value a Comparable object used to set the upperThreshold and lowerThreshold attributes
     * @throws IllegalArgumentException if any parameter is Null
     * @throws IllegalArgumentException if <code>name</code> parameter is empty
     */
    protected AbstractSimpleFilter(String name, Comparable value) {
        if(name == null) {
            throw new IllegalArgumentException("The name should not be null.");
        }
        if (value == null) {
            throw new IllegalArgumentException(
                "The value should not be null to construct AbstractSimpleFilter");
        }

        if (name.trim().length() == 0) {
            throw new IllegalArgumentException(
                "The name should not empty to construct AbstractSimpleFilter");
        }

        this.fieldName = name;
        this.value = value;
    }

    /**
     * <p>
     * Create a new instance,by providing the name ,upperThreshold and isUpperThresholdInclusive.
     * The constructor if for the betweenFilter.
     * </p>
     *
     * @param name the field name of the simple filter.
     * @param upper the upperThreshold of the simple filter.
     * @param lower the lowerThreshold of the simple filter.
     * @throws IllegalArgumentException if the any param is null.
     * @throws IllegalArgumentException if the name is empty.
     */
    public AbstractSimpleFilter(String name, Comparable upper, Comparable lower) {
        if(name == null) {
            throw new IllegalArgumentException("The name should not be null.");
        }
        if(upper == null) {
            throw new IllegalArgumentException("The upper should not be null.");
        }
        if(lower == null) {
            throw new IllegalArgumentException("The lower should not be null.");
        }

        if (name.length() == 0) {
            throw new IllegalArgumentException(
                "The name should not empty to construct AbstractSimpleFilter");
        }

        this.fieldName = name;
        this.upperThreshold = upper;
        this.lowerThreshold = lower;
        this.isLowerThresholdInclusive = true;
        this.isUpperThresholdInclusive = true;
    }

    /**
     * <p>return the name of the field.</p>
     *
     * @return the name of the field
     */
    public String getName() {
        return this.fieldName;
    }

    /**
     * <p>return the value of the field.</p>
     *
     * @return the value of the field
     */
    public Comparable getValue() {
        return this.value;
    }


    /**
     * the clone of the filter.
     *
     * @return return a clole of the filter.
     */
    public abstract Object clone();
    /**
     * <p>return the upper threshold value of the field.</p>
     *
     * @return a Comparable object representing the upper threshold
     */
    public Comparable getUpperThreshold() {
        return this.upperThreshold;
    }

    /**
     * <p>return the lower threshold value of the field.</p>
     *
     * @return a Comparable object representing the lower threshold
     */
    public Comparable getLowerThreshold() {
        return this.lowerThreshold;
    }

    /**
     * <p>return the upper threshold value of the field.</p>
     *
     *
     * @return a boolean indicating if the upper threshold inclusive
     */
    public boolean isUpperInclusive() {
        return this.isUpperThresholdInclusive;
    }

    /**
     * <p>return the upper threshold value of the field.</p>
     *
     *
     * @return a boolean indicating if the lower threshold inclusive
     */
    public boolean isLowerInclusive() {
        return this.isLowerThresholdInclusive;
    }
}
