package com.topcoder.util.weightedcalculator;

import java.util.List;
import java.util.ArrayList;

/**
 * An abstract class defining and implementing the functionality common 
 * to <code>Item</code>s that can have nested <code>Item</code>s. Also
 * implements <code>Item.getWeightedScore()</code> and <code>
 * Item.getActualScore</code> methods
 * 
 * <p>Copyright &copy 2002, TopCoder, Inc. All rights reserved
 *
 * @author  isv, WishingBone
 * @version 1.0
 */
abstract class Container extends Item {

    /**
     * A <code>List</code> containing nested <code>Item</code>s
     */
    private List items = null;

    /**
     * Total weight to cache up
     */
    protected double totalWeight = 0.0;

    /**
     * Constructs the <code>Container</code> with given description and
     * weight
     *
     * @param  description a <code>String</code> describing the <code>
     *         Container</code>
     * @param  weight a <code>double</code> representing a weight of <code>
     *         Container</code> within the owning element (i.e. <code>Section
     *         </code> or <code>MathMatrix</code>) if any
     * @throws IllegalArgumentException if given <code>weight</code> is
     *         not within the range ( 0 , 1 ] or given description is
     *         <code>null</code> or </code>empty</code>
     */
    protected Container(String description, double weight)
            throws IllegalArgumentException {
        super(description, weight);
        items = new ArrayList();
    }

    /**
     * Adds a specified <code>Item</code> to <code>Container</code>
     *
     * @param  item an <code>Item</code> to add to <code>Container</code>
     * @throws IllegalArgumentException if the sum of <code>weights</code> of
     *         all <code>Item</code>s including given <code>item</code> is 
     *         greater than 1.0  or given <code>item</code> is an instance
     *         of <code>MathMatrix</code>
     * @throws NullPointerException if given <code>item</code> is
     *         <code>null</code>
     */
    public void addItem(Item item) throws IllegalArgumentException,
        NullPointerException {
        if (item == null) {
            throw new NullPointerException();
        }
        Item own = this;
        while (own != null) {
            if (own == item) {
                throw new IllegalArgumentException();
            }
            own = own.getOwner();
        }

        double newTotalWeight = totalWeight + item.getWeight();
        if (newTotalWeight >= 1 + MathMatrix.EPSILON
            || item instanceof MathMatrix) {
            throw new IllegalArgumentException();
        }
        totalWeight = newTotalWeight;

        if (item.getOwner() != null) {
            ((Container) item.getOwner()).getItems().remove(item);
        }
        item.setOwner(this);
        items.add(item);
    }

    /**
     * Removes the <code>Item</code> at the specified position in this 
     * <code>Container</code>. Shifts any subsequent elements to the left 
     * (subtracts one from their indices). Returns the <code>Item</code>
     * that was removed from the <code>Container</code>
     *
     * @param  index the index of the <code>Item</code> to be removed
     * @return removed <code>Item</code>
     * @throws IndexOutOfBoundsException if the index is out of range  
     */
    public Item removeItem(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= items.size()) {
            throw new IndexOutOfBoundsException();
        }
        Item item = (Item) items.remove(index);
        totalWeight -= item.getWeight();
        return item;
    }

    /**
     * Returns the <code>Item</code> at the specified position in this
     * <code>Container</code>
     *
     * @param index index of element to return 
     * @return the <code>Item</code> at the specified position in this 
     *         <code>Container</code>
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public Item getItem(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= items.size()) {
            throw new IndexOutOfBoundsException();
        }
        return (Item) items.get(index);
    }

    /**
     * Returns the <code>List</code> containing all <code>Item</code>s
     * directly placed into this <code>Container</code>
     *
     * @return a <code>List</code> of directly placed <code>Item</code>s
     */
    public List getItems() {
        return new ArrayList(items);
    }

    /**
     * Calculates a <code>weighted</code> score for <code>Container</code>
     * as sum of weighted scores of all <code>Item</code>s contained within
     * it
     *
     * @return a <code>double</code> value representing a weighted score
     *         for <code>Container</code>
     * @throws IllegalStateException if the sum of weights of all directly 
     *         placed <code>Item</code>s is not equal to 1.0 
     */
    public double getWeightedScore() throws IllegalStateException {
        if (totalWeight <= 1 - MathMatrix.EPSILON
            || totalWeight >= 1 + MathMatrix.EPSILON) {
            throw new IllegalStateException();
        }

        double ret = 0;
        for (int i = 0; i < items.size(); ++i) {
            ret += ((Item) items.get(i)).getWeightedScore();
        }

        if (getOwner() == null) {
            double max = getMaximumScore();
            if (ret > max) {
                return max;
            }
        }
        return ret;
    }

    /**
     * Calculates an <code>actual</code> score for <code>Container</code>
     * as sum of actual scores of all <code>Item</code>s contained within
     * it
     *
     * @return a <code>double</code> value representing an actual score
     *         for <code>Container</code>
     * @throws IllegalStateException if the sum of weights of all directly 
     *         placed <code>Item</code>s is not equal to 1.0 
     */
    public double getActualScore() throws IllegalStateException {
        if (totalWeight <= 1 - MathMatrix.EPSILON
            || totalWeight >= 1 + MathMatrix.EPSILON) {
            throw new IllegalStateException();
        }

        double ret = 0;
        for (int i = 0; i < items.size(); ++i) {
            ret += ((Item) items.get(i)).getActualScore();
        }
        return ret;
    }

    /**
     * Returns a <code>maximum score</code> for <code>Container</code> as 
     * sum of maximum scores of all <code>Item</code>s directly nested 
     * within <code>Container</code> 
     * 
     * @return a <code>double</code> value representing a maximum score 
     *         for <code>Container</code>
     * @throws IllegalStateException if the summa of weights of all directly 
     *         placed <code>Items</code>s is not equal to 1.0 
     */
    public double getMaximumScore() throws IllegalStateException {
        if (totalWeight <= 1 - MathMatrix.EPSILON
            || totalWeight >= 1 + MathMatrix.EPSILON) {
            throw new IllegalStateException();
        }

        double ret = 0;
        for (int i = 0; i < items.size(); ++i) {
            ret += ((Item) items.get(i)).getMaximumScore();
        }
        return ret;
    }

    /**
     * Get the index of particular <code>Item</code> within the <code>
     * Container</code>
     *
     * @param item an <code>Item</code>
     * @return index of given <code>Item</code> within the <code>Container
     *         </code> or -1 if <code>Container</code> does not contain
     *         given <code>Item</code> 
     * @throws NullPointerException if given <code>item</code> is <code>
     *         null</code>
     */
    public int indexOf(Item item) throws NullPointerException {
        if (item == null) {
            throw new NullPointerException();
        }
        return items.indexOf(item);
    }

}
