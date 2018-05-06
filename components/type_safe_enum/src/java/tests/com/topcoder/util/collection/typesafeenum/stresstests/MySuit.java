/**
 *
 * Copyright © 2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.collection.typesafeenum.stresstests;

import com.topcoder.util.collection.typesafeenum.Enum;
import java.io.Serializable;

/*
 * <p>
 * This is annoying.  The specs layout the Suit class, but the reference
 * implementation doesn't contain the class.  This gives me the option of either
 * assuming that we'll never compile against the reference implementation (and
 * implicitly assuming all submissions implemented the class), or doing it
 * myself (and not being able to test a submission's Suit implementation for
 * lack of the previous assumption).  Since the Suit is just an example class,
 * I'm taking the latter option.
 * </p>
 * @author Altrag
 * @version 1.0
 */
public class MySuit extends Enum implements Comparable, Serializable {
    
    public static final MySuit CLUBS = new MySuit("Clubs", "Black");
    public static final MySuit DIAMONDS = new MySuit("Diamonds", "Red");
    public static final MySuit HEARTS = new MySuit("Hearts", "Red");
    public static final MySuit SPADES = new MySuit("Spades", "Black");
    
    private String name;
    private String color;
    
    private MySuit(String name, String color) {
        this.name = name;
        this.color = color;
        
    }
    
    public String getName() {
        return name;
        
    }
    
    public String getColor() {
        return color;
        
    }
    
    public String toString() {
        return name;
        
    }
    
}
