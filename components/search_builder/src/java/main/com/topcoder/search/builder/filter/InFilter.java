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
 * This class is a concrete implementor of the Filter interface.
 * It is used to construct IN search criterion.
 * </p>
 *
 * <p>
 * The class is thread-safe since it is immutable.
 * </p>
 *
 * @author ShindouHikaru, TCSDEVELOPER
 * @version 1.3
 */
public class InFilter implements Filter {
    /**
     * It will hold the filed name of the search criterion.
     *
     */
    private final String fieldName;

    /**
     * It will hold a List of Comparable objects, null-elemnt is not allowed.
     *
     */
    private final List values;

    /**
     * <p>Create a new instance,by setting the name and value list.</p>
     *
     *
     * @param name the filed name of the search criterion
     * @param values a List of Comparable objects
     * @throws IllegalArgumentException if any parameter is Null or the items in the list is null
     * @throws IllegalArgumentException if any parameter is empty
     */
    public InFilter(String name, List values) {
        if ((name == null)) {
            throw new IllegalArgumentException(
                "The param should not be null to construct InFilter.");
        }

        if (name.trim().length() == 0) {
            throw new IllegalArgumentException(
                "The param should not be empty to construct InFilter.");
        }

        FilterHelper.checkList(values, "values");

        for (Iterator it = values.iterator(); it.hasNext();) {
            if (!(it.next() instanceof Comparable)) {
                throw new IllegalArgumentException(
                    "The items in the values should be Comparable.");
            }
        }

        this.fieldName = name;
        this.values = new ArrayList(values);
    }

    /**
     * <p>Test to see if the filter is valid according to the rules presented in the validators.</p>
     *
     * @param validators a map containing <code>ObjectValidator</code> Objects
     * @return the <code>ValidationResult</code> object
     * @param alias a Map containing mapping between real names and alias names
     * @throws IllegalArgumentException if any parameter is Null or the keys and values in map is null
     * @throws IllegalArgumentException if any parameter is empty
     */
    public ValidationResult isValid(Map validators, Map alias) {
        if(validators == null) {
            throw new IllegalArgumentException("The validators should not be null.");
        }
        if(alias == null) {
            throw new IllegalArgumentException("The alias should not be null.");
        }

        if ((validators.size() == 0) || (alias.size() == 0)) {
            throw new IllegalArgumentException("The map should not be empty.");
        }

        FilterHelper.checkMap(validators, "validators");
        FilterHelper.checkMap(alias, "alias");

        ObjectValidator rule = (ObjectValidator) validators.get(fieldName);

        //if null,then with alias
        if (rule == null) {
            String aliasName = (String) alias.get(fieldName);

            if (aliasName != null) {
                //get the ObjectValidator
                rule = (ObjectValidator) validators.get(aliasName);
            }
        }

        if (rule == null) {
            //unsearchable field,return invalid
            return ValidationResult.createInvalidResult("InFilter filter fails for there is no rule.",
                (Filter) this);
        }

        if (values == null) {
            return ValidationResult.createValidResult();
        }
        Iterator it = values.iterator();
        while (it.hasNext()) {
            if (rule.getMessage(it.next()) != null) {
                //if one invalid,return invalid
                return ValidationResult.createInvalidResult("InFilter validation fails.",
                    (Filter) this);
            }
        }

        //return valid ValidationResult
        return ValidationResult.createValidResult();
    }

    /**
     * <p>Get the type of the Filter.</p>
     *
     *
     * @return a integer representing the type of the Filter
     * @deprecated This method has been deprecated.
     */
    public final int getFilterType() {
        return Filter.IN_FILTER;
    }

    /**
     * <p>return the name of the field.</p>
     *
     * @return the name of field
     */
    public String getName() {
        return this.fieldName;
    }

    /**
     * <p>return a list of valid field values.</p>
     *
     * @return a list of valid field values
     */
    public List getList() {
        return new ArrayList(values);
    }

    /**
     * <p>return a clone of the object.</p>
     *
     *
     * @return return a clone of the InFilter
     */
    public Object clone() {
        return new InFilter(fieldName, values);
    }
}
