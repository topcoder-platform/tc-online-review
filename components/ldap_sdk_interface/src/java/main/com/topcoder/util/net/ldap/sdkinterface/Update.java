/*
 * TCS LDAP SDK Interface 1.0
 *
 * Update.java
 *
 * Copyright (c) 2004, TopCoder Inc. All rights reserved.
 */
package com.topcoder.util.net.ldap.sdkinterface;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Represents an Update in an LDAP entry</p>
 * <p>An update consists in many modifications, each one represented by a Modification object</p>
 * <p>The modifications are not executed when the methods are called. The only thing this class does is to store
 * modifications that will be executed with the updateEntry method from the connection. Because of that, none of the
 * methods can produce an error; if the data provided is wrong the error will appear when the update is
 * &quot;commited&quot; </p>
 * <p>There are 4 types of modifications</p>
 * <ul type="disc">
 * <li>add an attribute or values to an attribute (add method)</li>
 * <li>delete an attribute (delete method with one parameter)</li>
 * <li>delete values from an attribute (delete method with two parameters)</li>
 * <li>replace the attribute values (replace method)</li>
 * </ul>
 * <p>Each time one of the 4 methods is called, a new Modification is created and stored in a collection </p>
 * <p>There is no way to delete or change a Modification, because this will not be usually needed. If this is needed,
 * the collection of Modification is protected, so this class can be inherited and the necesary methods provided</p>
 * <p>All the modifications can be retrieved using the getModifications method</p>
 * <p>After generating an update, it should be executed using the updateEntry method of the LDAPSDKConnection.
 *
 * @author  cucu
 * @author  isv
 * @version 1.0 05/18/2004
 */
public class Update {

    /**
     * A <code>List</code> of <code>Modification</code> objects representing the modifications containing within this
     * <code>Update</code> object.
     */
    protected List modifications = new ArrayList();

    /**
     * Add an attribute (if the attribute name doesn't exist) or values to an attribute in an entry</p>
     *
     * @param  attributeName the attribute name.
     * @param  values the values to be added.
     * @throws NullPointerException if attributeName or values are null.
     * @throws IllegalArgumentException if attributeName is an empty string.
     */
    public void add(String attributeName, Values values) {
        checkValues(values);
        Modification modification = new Modification(Modification.ADD, attributeName, values);
        modifications.add(modification);
    }

    /**
     * Delete an attribute from an Entry.</p>
     *
     * @param  attributeName the attribute name.
     * @throws NullPointerException if attributeName is null.
     * @throws IllegalArgumentException if attibuteName is an empty string.
     */
    public void delete(String attributeName) {
        Modification modification = new Modification(Modification.DELETE_ATTRIBUTE, attributeName, null);
        modifications.add(modification);
    }

    /**
     * Delete values from an attribute.</p>
     *
     * @param attributeName the attribute name.
     * @param values the values to be deleted.
     * @throws NullPointerException if attributeName or values is null.
     * @throws IllegalArgumentException if attibuteName is an empty string.
     */
    public void delete(String attributeName, Values values) {
        checkValues(values);
        Modification modification = new Modification(Modification.DELETE_VALUE, attributeName, values);
        modifications.add(modification);
    }

    /**
     * Replace the values of an attribute with the specified values.</p>
     *
     * @param attributeName the attribute name.
     * @param values the new values of the attribute.
     * @throws NullPointerException if attributeName or values are null.
     * @throws IllegalArgumentException if attributeName is an empty string.
     */
    public void replace(String attributeName, Values values) {
        checkValues(values);
        Modification modification = new Modification(Modification.REPLACE, attributeName, values);
        modifications.add(modification);
    }

    /**
     * Get all the modifications.
     *
     * @return a List of Modification objects.
     */
    public List getModifications() {
        return new ArrayList(modifications);
    }

    /**
     * A helper "guard" method rejecting the invalid values of modification operations added to this collection throwing
     * the appropriate runtime exception (namely, <code>NulPointerException</code>).
     *
     * @param  values a <code>Values</code> object containing the values that must be used to configure the modifiaction
     *         operation.
     * @throws NullPointerException if specified argument is <code>null</code>.
     */
    private void checkValues(Values values) {
        if (values == null) {
            throw new NullPointerException("Null values are prohibited");
        }
    }
}
