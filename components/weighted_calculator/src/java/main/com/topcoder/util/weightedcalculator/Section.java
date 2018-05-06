package com.topcoder.util.weightedcalculator;

/**
 * A group of <code>Item</code>s that has a weight associated with it.
 * Consists of <code>LineItem</code> and <code>Section</code> objects.
 *
 * <p>Copyright &copy 2002, TopCoder, Inc. All rights reserved
 *
 * @author  isv, WishingBone
 * @version 1.0
 */
public class Section extends Container {

    /**
     * Constructs a <code>Section</code> with given <code>description
     * </code> and <code>weight</code>
     *
     * @param  description a <code>String</code> describing the <code>
     *         Section</code>
     * @param  weight a <code>double</code> representing a weight of
     *         <code>Section</code> within the owning <code>Item</code>
     *         (i.e. <code>Section</code> or <code>MathMatrix</code>)
     * @throws IllegalArgumentException if given <code>weight</code> is
     *         not within the range ( 0 , 1 ] or given description is 
     *         <code>null</code> or </code>empty</code>
     */
    public Section(String description, double weight) 
            throws IllegalArgumentException {
        super(description, weight);
    }

}
