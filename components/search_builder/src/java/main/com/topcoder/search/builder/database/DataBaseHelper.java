/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.database;

import java.util.Iterator;
import java.util.List;


/**
 * <p>
 * This helper Class of the package com.topcoder.search.builder.database.
 * </p>
 *
 * @author victor_lxd, haozhangr
 * @version 1.1
 */
class DataBaseHelper {
    /**
     * The private construct.
     *
     */
    private DataBaseHelper() {
        //empty
    }

    /**
     * Check the list valid.The list should not be null also should not be empty.
     * The mothed also check the items in the list which also should not be null.
     *
     * @param list The list to be checked
     * @param listName the Name of the list, which denote the usage of the list
     * @throws NullPointerException if the list is null
     * @throws IllegalArgumentException if the <code>list</code> is empty  or the items in it is null
     */
    public static void checkList(List list, String listName) {
        if (list == null) {
            throw new NullPointerException("The list " + listName + " should not be null.");
        }

        if (list.size() == 0) {
            throw new IllegalArgumentException("The list " + listName + " should not be empty.");
        }
        //all the items in the list should not be null
        for (Iterator it = list.iterator(); it.hasNext();) {
            if (it.next() == null) {
                throw new IllegalArgumentException("The items in the list " + listName + " should not be null.");
            }
        }
    }

}
