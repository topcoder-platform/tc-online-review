package com.topcoder.util.weightedcalculator;

/**
 * A complex matrix intended to evaluate a score based on total score.
 * Provides an ability to calculate a weighted scores for line items
 * or groups(sections) of line items.
 * Contains line items and groups of line items. Every line item and group
 * of line items has a weight associated with it that is greater than zero
 * but not greater than 1.0
 *
 * <p>Copyright &copy 2002, TopCoder, Inc. All rights reserved
 *
 * @author  isv, WishingBone
 * @version 1.0
 */
public class MathMatrix extends Container {

    /**
     * A double representing a deviation to avoid a double rounding
     * problem when comparing sum of maximum scores or weights with 1.0
     */
    public static final double EPSILON = 0.01;

    /**
     * A <code>double</code> value representing a maximum score
     * assigned to <code>MathMatrix</code>
     */
    private double maximumScore = 100;

    /**
     * Constructs a <code>MathMatrix</code> with given <code>description
     * </code> and default <code>maximum score</code>
     *
     * @param  description a <code>String</code> describing the <code>
     *         MathMatrix</code>
     * @throws IllegalArgumentException if given description is <code>null
     *         </code> or </code>empty</code>
     */
    public MathMatrix(String description) 
            throws IllegalArgumentException {
        super(description, 1.00);
    }

    /**
     * Constructs a <code>MathMatrix</code> with given <code>description
     * </code> and <code>maximum score</code>
     *
     * @param  description a <code>String</code> describing the <code>
     *         MathMatrix</code>
     * @param  maximumScore a <code>double</code> representing a maximum 
     *         score assigned to <code>MathMatrix</code>
     * @throws IllegalArgumentException if given <code>maximumScore</code> is
     *         not greater than 0 or given description is <code>null</code>
     *         or </code>empty</code>
     */
    public MathMatrix(String description, double maximumScore) 
            throws IllegalArgumentException {
        super(description, 1.00);
        if (maximumScore <= 0) {
            throw new IllegalArgumentException();
        }
        this.maximumScore = maximumScore;
    }

    /**
     * Returns a <code>maximum score</code> assigned to <code>MathMatrix
     * </code>
     * 
     * @return a <code>double</code> value representing a maximum score 
     *         assigned to <code>MathMatrix</code>
     */
    public double getMaximumScore() {
        return maximumScore;
    }

    /**
     * Assigns given <code>maximum score</code> to <code>MathMatrix</code>
     *
     * @param  maximumScore a <code>double</code> value specifying the maximum
     *         score assigned to <code>MathMatrix</code>
     * @throws IllegalArgumentException if given <code>maximumScore</code>
     *         is not greater than 0 or is less than <code>actual score</code>
     *         for <code>MathMatrix</code>
     */
    public void setMaximumScore(double maximumScore)
        throws IllegalArgumentException {
        if (maximumScore <= 0
            || maximumScore < getActualScore() - EPSILON) {
            throw new IllegalArgumentException();
        }
        this.maximumScore = maximumScore;
    }

    /**
     * Overrides <code>Item.setWeight(double)</code> method in order to 
     * disable weight other than 1.0
     *
     * @param weight the weight to set
     */
    public void setWeight(double weight) {
        // nothing here
    }

}
