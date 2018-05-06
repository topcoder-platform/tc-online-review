/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.filter;

import com.topcoder.search.builder.ValidationResult;

import com.topcoder.util.datavalidator.ObjectValidator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * This helper Class of the package com.topcoder.search.builder.filter,
 * which offer some function to check valid of the param.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.3
 */
final class FilterHelper {
    /**
     * The private construct.
     *
     */
    private FilterHelper() {
        //empty
    }

    /**
     * Check the list valid.The list should not be null also should not be empty.
     * The mothed also check the items in the list which also should not be null.
     *
     * @param list The list to be checked
     * @param listName the Name of the list, which denote the usage of the list
     * @throws IllegalArgumentException if the list is null
     * @throws IllegalArgumentException if the list is empty or the items in it is null
     */
    static void checkList(List list, String listName) {
        if (list == null) {
            throw new IllegalArgumentException("The list " + listName
                + " should not be null");
        }

        if (list.size() == 0) {
            throw new IllegalArgumentException("The list " + listName
                + " should not be empty");
        }

        //all the items in the list should not be null
        for (Iterator it = list.iterator(); it.hasNext();) {
            if (it.next() == null) {
                throw new IllegalArgumentException("The items in the list "
                    + listName + " should not be null");
            }
        }
    }

    /**
     * Check the map valid.The map should not be null also should not be empty.
     * The mothed also check the key and value in the map which also should not be null.
     *
     * Note: The map checked in the filters can be empty,eg. the alias is empty means
     * there is no fieldName has aliasName.
     * @param map The map to be checked
     * @param mapName the Name of the map, which denote the usage of the map
     * @throws IllegalArgumentException if the map is null or the key and value in it is null
     * @throws IllegalArgumentException if the map is empty
     */
    public static void checkMap(Map map, String mapName) {
        for (Iterator it = map.entrySet().iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            Object key = entry.getKey();

            //the keys in the map should not be null
            if (key == null) {
                throw new IllegalArgumentException("The key in the list "
                    + mapName + " should not be null");
            }
            Object value = entry.getValue();
            //the values in the map should not be null
            if (value == null) {
                throw new IllegalArgumentException("The value in the list "
                    + mapName + " should not be null");
            }
        }
    }

    /**
     * Check the SimpleFilter whether it is legal for rule which is stored in the map validators.
     * The rule is got by the name or the aliasName according it.
     *
     * @param validators a map containing <code>ObjectValidator</code> Objects
     * @param alias a Map containing mapping between real names and alias names
     * @param name the filedName to get the rule
     * @param value the Comparable object to be checked valid according the above rule
     * @param filter the filter to be checked
     * @return the <code>ValidationResult</code> object
     * @throws IllegalArgumentException if any <code>param</code> is nul
     * @throws IllegalArgumentException if the map is illegal
     */
    public static ValidationResult isValidSimpleFilter(Map validators,
        Map alias, String name, Comparable value, Filter filter) {
        if(validators == null) {
            throw new IllegalArgumentException("The validators should not be null.");
        }
        if(alias == null) {
            throw new IllegalArgumentException("The alias should not be null.");
        }

        if (validators.size() == 0) {
            throw new IllegalArgumentException(
                "The validators map should not be empty");
        }

        checkMap(validators, "validators");
        checkMap(alias, "alias");

        if(name == null) {
            throw new IllegalArgumentException("The name should not be null.");
        }
        if(value == null) {
            throw new IllegalArgumentException("The value should not be null.");
        }
        if(filter == null) {
            throw new IllegalArgumentException("The filter should not be null.");
        }
        ObjectValidator rule = null;
        if (validators.get(name) != null && validators.get(name) instanceof ObjectValidator) {
            rule = (ObjectValidator) validators.get(name);
        }

        //if null,then with alias
        if (rule == null) {
            String aliasName = (String) alias.get(name);

            if (aliasName != null) {
                if (validators.get(aliasName) != null && validators.get(aliasName) instanceof ObjectValidator) {
                    rule = (ObjectValidator) validators.get(aliasName);
                }
            }
        }

        //if not searchable,no rule exist for the name, return invalid
        if (rule == null) {
            throw new IllegalArgumentException("The map validators is invalid to get the check rule.");
        }

        //if null return invalid
        if (rule.getMessage(value) != null) {
            return ValidationResult.createInvalidResult("Filter check valid fails for "
                    + rule.getMessage(value) + ".",
                filter);
        }

        //return valid object
        return ValidationResult.createValidResult();
    }

    /**
     * Check the AssociativeFilter whether it is legal for rule which is stored in the map validators.
     * The rule is got by the name or the aliasName according it.
     *
     * @param validators a map containing <code>ObjectValidator</code> Objects
     * @param alias a Map containing mapping between real names and alias names
     * @param filters the list of filter in the AssociativeFilter
     * @return the <code>ValidationResult</code> object
     * @throws IllegalArgumentException if any <code>param</code> is nul
     * @throws IllegalArgumentException if the map is illegal
     */
    public static ValidationResult isValidAssociativeFilter(Map validators,
        Map alias, List filters) {
        if(validators == null) {
            throw new IllegalArgumentException("The validators should not be null.");
        }
        if(alias == null) {
            throw new IllegalArgumentException("The alias should not be null.");
        }

        if (validators.size() == 0) {
            throw new IllegalArgumentException(
                "The validators map should not be empty");
        }
        checkMap(validators, "validators");
        checkMap(alias, "alias");
        checkList(filters, "filters");
        Iterator it = filters.iterator();
        while (it.hasNext()) {
            Filter filter = (Filter) it.next();

            ValidationResult v = filter.isValid(validators, alias);

            //unsuccess then return
            if (!v.isValid()) {
                return v;
            }
        }

        //valid success
        return ValidationResult.createValidResult();
    }
    /**
     * Clone a AbstractAssociativeFilter, include both AndFilter and OrFilter.
     *
     * @param filter the filter to be cloned
     * @return the clone of the filter
     */
    public static AbstractAssociativeFilter associativeFilterclone(AbstractAssociativeFilter filter) {
        List newList = new ArrayList();
        List filters = filter.getFilters();
        Iterator it = filters.iterator();
        while (it.hasNext()) {
            //clone every filter in the filters
            newList.add(((Filter) it.next()).clone());
        }
        if (filter instanceof AndFilter) {
            return new AndFilter(newList);
        }

        return new OrFilter(newList);
    }
}
