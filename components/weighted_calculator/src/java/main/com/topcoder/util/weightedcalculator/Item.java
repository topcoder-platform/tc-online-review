package com.topcoder.util.weightedcalculator;

/**
 * An abstract class defining and partially implementing the basic 
 * functionality common to items that can be stored in 
 * <code>MathMatrix</code>
 * 
 * <p>Copyright &copy 2002, TopCoder, Inc. All rights reserved
 *
 * @author  isv, WishingBone
 * @version 1.0
 */
public abstract class Item {

    /**
     * A <code>String</code> describing the <code>Item</code>
     */
    private String description = null;

    /**
     * A <code>double</code> value representing a weight of <code>Item
     * </code> within the context of owning element(i.e.<code>MathMatrix
     * </code> or <code>Section</code>)
     */
    private double weight = 0.00;

    /**
     * A reference to <code>Item</code> that owns the original <code>
     * Item</code>
     */
    private Item owner = null;   

    /**
     * Constructs an <code>Item</code> with given <code>description</code>,
     * and <code>weight</code>
     *
     * @param  description a <code>String</code> describing the <code>Item
     *         </code>
     * @param  weight a <code>double</code> representing a weight
     *         of <code>Item</code> within the owning element
     *         (i.e. <code>Section</code> or <code>MathMatrix</code>)
     * @throws IllegalArgumentException if given <code>weight</code> is
     *         not within the range ( 0 , 1 ] or given description is
     *         <code>null</code> or </code>empty</code>
     */
    protected Item(String description, double weight) 
        throws IllegalArgumentException {
        if (description == null || description.length() == 0
            || weight <= 0 || weight > 1) {
            throw new IllegalArgumentException();
        }
        this.description = description;
        this.weight = weight;
    }

    /**
     * Returns a <code>weight</code> associated with <code>Item</code> 
     *         as <code>double</code> value
     * 
     * @return a <code>double</code> value representing a weight associated 
     *         with <code>Item</code>
     */
    public double getWeight() {
        return weight;
    }

    /**
     * Assigns given <code>weight</code> to <code>Item</code> instance
     *
     * @param  weight a <code>double</code> value to be assigned with
     *         <code>Item</code>
     * @throws IllegalArgumentException if given <code>weight</code>
     *         is not within the range ( 0 , 1 ]
     */
    public void setWeight(double weight) throws IllegalArgumentException {
        if (weight <= 0 || weight > 1) {
            throw new IllegalArgumentException();
        }
        if (owner != null) {
            ((Container) owner).totalWeight -= this.weight;
        }
        this.weight = weight;
        if (owner != null) {
            ((Container) owner).totalWeight += this.weight;
        }
    }

    /**
     * Get the description of <code>Item</code>
     *
     * @return a <code>String</code> describing the <code>Item</code>
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the description of <code>Item</code>
     *
     * @param  description a <code>String</code> describing the <code>
     *         Item</code>
     * @throws IllegalArgumentException if <code>description</code> is
     *         <code>null</code> or <code>empty</code>
     */
    public void setDescription(String description)
        throws IllegalArgumentException {
        if (description == null || description.length() == 0) {
            throw new IllegalArgumentException();
        }
        this.description = description;
    }

    /**
     * Returns a <code>maximum score</code> available for <code>Item</code>
     *         as <code>int</code> value
     * 
     * @return an <code>int</code> value representing a maximum score 
     *         available for <code>Item</code>
     */
    public abstract double getMaximumScore();

    /**
     * Returns an <code>actual score</code> assigned to <code>Item</code>
     *         as <code>int</code> value
     * 
     * @return an <code>int</code> value representing an actual score 
     *         assigned to <code>Item</code>
     */
    public abstract double getActualScore();

    /**
     * Calculates a <code>weighted</code> score for <code>Item</code>
     *
     * @return a <code>double</code> value representing a weighted score
     *         for <code>Item</code>
     */
    public abstract double getWeightedScore();

    /**
     * Get the reference to <code>Item</code> that owns this <code>
     * Item</code>
     *
     * @return an owning <code>Item</code>
     */
    protected Item getOwner() {
        return owner;
    }

    /**
     * Set the reference to <code>Item</code> that owns this <code>
     * Item</code>
     *
     * @param owner an owning <code>Item</code>
     */
    protected void setOwner(Item owner) {
        this.owner = owner;
    }

    /**
     * Get the total score assigned to on-top element of matrix(i.e. 
     * <code>MathMatrix</code>), multiples the weight of this item
     * on the whole.
     * If <code>owner</code> of <code>Item</code> is <code>null</code> 
     * returns the <code>Item.getMaximumScore() * this.weight</code>
     * otherwise returns <code>owner.getTotalScore() * this.weight</code>
     *
     * @return a score
     */
    protected double getTotalScore() {
        if (owner == null) {
            return getMaximumScore() * getWeight();
        }
        return owner.getTotalScore() * getWeight();
    }

}
