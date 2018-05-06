/**
 *
 * Copyright © 2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.collection.typesafeenum.stresstests;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/*
 * <p>
 * This class implements as (close as I can come up with) equivalent methods
 * for the operations that should be approximately Integer-speed, for comparison
 * purposes.
 * </p>
 * @author Altrag
 * @version 1.0
 */
public class IntSuit implements Comparable, Serializable {
    public static final IntSuit CLUBS = new IntSuit(0);
    public static final IntSuit DIAMONDS = new IntSuit(1);
    public static final IntSuit HEARTS = new IntSuit(2);
    public static final IntSuit SPADES = new IntSuit(3);

    static List iArray =
        Arrays.asList(new IntSuit[]{CLUBS, DIAMONDS, HEARTS, SPADES});
    Integer i;

    private IntSuit(int i) {
        this.i = new Integer(i);
        
    }
    
    public int getOrdinal() {
        return i.intValue();
        
    }
    
    public static IntSuit getIntByOrdinal(int ord) {
        return (IntSuit)iArray.get(ord);
        
    }
    
    Object readResolve() {
        return iArray.get(i.intValue());
        
    }
    
    public boolean equals(Object obj) {
        return (this == obj);
        
    }
    
    public int compareTo(Object obj) {
        return i.compareTo(((IntSuit)obj).i);
        
    }
    
}
