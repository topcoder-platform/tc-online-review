package com.topcoder.util.weightedcalculator;

/**
 * A single line item that is an analog of requirement in scorecard.
 * It is a basic unit when building Sections or MathMatrix.
 * Has a maximum available score and weight associated with it. Also
 * can have an actual score that is used to calculate a weighted score
 * in accordance with given maximum score and weight. 
 *
 * <p>Copyright &copy 2002, TopCoder, Inc. All rights reserved
 *
 * @author  isv, WishingBone
 * @version 1.0
 */
public class LineItem extends Item {

    /**
     * A <code>double</code> value representing an actual score assigned 
     * to <code>LineItem</code>. Can't be greater than <code>maximumScore
     * </code>
     */
    private double actualScore = 0;

    /**
     * A <code>double</code> value representing a maximum allowable score
     * that can be assigned to <code>LineItem</code>
     */
    private double maximumScore = 0;

    /**
     * Constructs a <code>LineItem</code> with given <code>description</code>,
     * <code>weight</code> and <code>maximum score</code>
     *
     * @param  description a <code>String</code> describing the <code>LineItem
     *         </code>
     * @param  weight a <code>double</code> representing a weight of <code>
     *         LineItem</code> within the owning <code>Item</code>
     *         (i.e. <code>Section</code> or <code>MathMatrix</code>)
     * @param  maximumScore a <code>double</code> representing a maximum score
     *         that can be assigned to <code>LineItem</code>
     * @throws IllegalArgumentException if given <code>weight</code> is
     *         not within the range ( 0 , 1 ] or <code>maximumScore</code>
     *         is not greater than 0 or given description is <code>null</code>
     *         or </code>empty</code>
     */
    public LineItem(String description, double weight, double maximumScore) 
            throws IllegalArgumentException {
        super(description, weight);
        if (maximumScore <= 0) {
            throw new IllegalArgumentException();
        }
        this.maximumScore = maximumScore;
    }

    /**
     * Constructs a <code>LineItem</code> with given <code>description</code>,
     * <code>weight</code>, <code>maximum score</code> and <code>actualScore
     * </code>
     *
     * @param  description a <code>String</code> describing the <code>LineItem
     *         </code>
     * @param  weight a <code>double</code> representing a weight of <code>
     *         LineItem</code> within the owning <code>Item</code>element
     *         (i.e. <code>Section</code> or <code>MathMatrix</code>)
     * @param  maximumScore a <code>double</code> representing a maximum score
     *         that can be assigned to <code>LineItem</code>
     * @param  actualScore a <code>double</code> representing an actual score
     *         assigned to LineItem
     * @throws IllegalArgumentException if given <code>weight</code> is
     *         not within the range ( 0 , 1 ] or <code>maximumScore</code>
     *         is not greater than 0 or <code>actualScore</code> is greater
     *         than <code>maximumScore</code> or is less than zero or given 
     *         description is <code>null</code> or </code>empty</code>
     */
    public LineItem(String description, double weight, double maximumScore, 
                    double actualScore) throws IllegalArgumentException {
        super(description, weight);
        if (maximumScore <= 0 || actualScore < 0
            || actualScore > maximumScore + MathMatrix.EPSILON) {
            throw new IllegalArgumentException();
        }
        this.maximumScore = maximumScore;
        this.actualScore = actualScore;
    }

    /**
     * Calculates a <code>weighted</code> score for <code>LineItem</code>
     *
     * @return a <code>double</code> value representing a weighted score
     *         for <code>LineItem</code>
     */
    public double getWeightedScore() {
        return actualScore / maximumScore * getTotalScore();
    }

    /**
     * Returns a <code>maximum score</code> that can be assigned to 
     * <code>LineItem</code> as <code>double</code> value
     * 
     * @return a <code>double</code> value representing a maximum score 
     *         that can be assigned to <code>LineItem</code>
     */
    public double getMaximumScore() {
        return maximumScore;
    }

    /**
     * Returns an <code>actual score</code> assigned to <code>LineItem</code>
     *         as <code>double</code> value
     * 
     * @return an <code>int</code> value representing an actual score 
     *         assigned to <code>LineItem</code>
     */
    public double getActualScore() {
        return actualScore;
    }

    /**
     * Specifies the <code>maximum score</code> that can be assigned to 
     * <code>LineItem</code>
     *
     * @param  maximumScore a <code>double</code> value specifying the maximum
     *         score that can be assigned to <code>LineItem</code>
     * @throws IllegalArgumentException if given <code>maximumScore</code>
     *         is not greater than 0 or is less than <code>actual score</code>
     *         assigned to <code>LineItem</code>
     */
    public void setMaximumScore(double maximumScore) 
        throws IllegalArgumentException {
        if (maximumScore <= 0
            || maximumScore < actualScore - MathMatrix.EPSILON) {
            throw new IllegalArgumentException();
        }
        this.maximumScore = maximumScore;
    }

    /**
     * Assigns a given <code>actual score</code> to <code>LineItem</code>
     *
     * @param  actualScore a <code>double</code> value that should be
     *         assigned to <code>LineItem</code>
     * @throws IllegalArgumentException if given <code>actualScore</code>
     *         is less than 0 or is greater than <code>maximum score</code>
     *         assigned to <code>LineItem</code>
     */
    public void setActualScore(double actualScore) 
        throws IllegalArgumentException {
        if (actualScore < 0
            || actualScore > maximumScore + MathMatrix.EPSILON) {
            throw new IllegalArgumentException();
        }
        this.actualScore = actualScore;
    }

}
