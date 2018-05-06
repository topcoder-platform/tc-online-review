/*
 * TCS LDAP SDK Interface 1.0
 *
 * LDAPSDK.java
 *
 * Copyright &copy; TopCoder Inc, 2004. All rights reserved.
 */
package com.topcoder.util.net.ldap.sdkinterface;

import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.Iterator;

/**
 * This class represents an entry in the LDAP structure, which is uniquelly identified by its Distinguished Name.
 * It has zero or more attributes, each one being a pair &lt;attributeName, Values&gt; in the attributes Map.</p>
 * <p>It can be used for building entries and passing them to the LDAP, in which case the entry must be built by the
 * user using the constructor and then sent to the LDAP using the addEntry method in LDAPSDKConnection,or it can be
 * used when retrieving data in a readEntry or search (in that case a Iterator of Entry is returned), in which case
 * the user has Entry objects and must use it to retrieve the desired information.
 *
 * @author  cucu
 * @author  isv
 * @version 1.0
 */
public class Entry {

    /**
     * <p>Represents the Distiguished Name of this entry.</p>
     * <p>setDN and getDN methods are accesors for this variable.</p>
     */
    private String dn = null;

    /**
     * Represents the attributes of the entry. The keys are the names of the attributes, and must be of type String,
     * The values are the values of the attributes, and must be of type Values, which contain many Text and Binary
     * values.</p>
     * <p>This variable must be instantiated in the constructor. The set/get/delete Attribute methods will operate on
     * this map.</p>
     */
    private Map attributes = null;

    /**
     * Constructs an empty entry. The entry is initialized with an empty DN and empty list of attributes.
     */
    public Entry() {
        this("");
    }

    /**
     * Constructs an entry with the specified DN. The entry is iniitialized with empty list of attributes.</p>
     *
     * @param  dn a <code>String</code> representing the Distinguished Name of the entry. The empty names are permitted.
     * @throws NullPointerException if specified <code>String</code> is <code>null</code>.
     */
    public Entry(String dn) {
        attributes = new HashMap();
        setDn(dn);
    }

    /**
     * Gets the distiguished name of this entry.
     *
     * @return a <code>String</code> representing the distiguished name of this entry.
     */
    public String getDn() {
        return dn;
    }

    /**
     * Sets the distiguished name of this entry </p>
     *
     * @param  dn a <code>String</code> representing the new Distinguished Name of the entry. The empty names are
     *         permitted.
     * @throws NullPointerException if specified <code>String</code> is <code>null</code>.
     */
    public void setDn(String dn) {
        if (dn == null) {
            throw new NullPointerException("Null DN is not allowed");
        }

        this.dn = dn;
    }

    /**
     * Sets the attribute of this entry with specified value. If the attribute already exists, the attributes values are
     * set to the specified values. If the attribute doesn't exist then the given attribute is added to he entry.</p>
     *
     * @param  attributeName a <code>String</code> representing the name of the attribute.
     * @param  values a <code>Values</code> object containing the values of the attribute.
     * @throws IllegalArgumentException if specified <code>String</code> is empty.
     * @throws NullPointerException if any of given arguments is <code>null</code>.
     */
    public void setAttribute(String attributeName, Values values) {
        checkAttributeName(attributeName);

        if (values == null) {
            throw new NullPointerException("Null Values is prohibited.");
        }
        attributes.put(attributeName, values);
    }

    /**
     * Gets all the attributes that are assigned to this entry.
     *
     * @return a <code>Map</code> with keys of type <code>String</code> and value of type <code>Values</code>,
     *         representing all the attributes of the entry.
     */
    public Map getAttributes() {
        return Collections.unmodifiableMap(attributes);
    }

    /**
     * Sets all the attributes for this entry, deleting all the existing attributes.</p>
     *
     * @param  attributes the <code>Map</code> containing the attributes to be set. Such a <code>Map</code> should map
     *         the <code>String</code> attribute names to <code>Values</code> objects.
     * @throws IllegalArgumentException if specified <code>Map</code> contains at least one null or non-String or empty
     *         String key or null or non-Values value.
     * @throws NullPointerException if given <code>Map</code> is <code>null</code>.
     */
    public void setAttributes(Map attributes) {
        if (attributes == null) {
            throw new NullPointerException("Null map of attribute-values pairs is prohibited.");
        }

        Object key = null;
        Object value = null;
        Map.Entry entry = null;

        Iterator iterator = attributes.entrySet().iterator();
        while (iterator.hasNext()) {
            entry = (Map.Entry) iterator.next();
            key = entry.getKey();
            if ((key == null) || !(key instanceof String) || (((String) key).trim().length() == 0)) {
                throw new IllegalArgumentException("Null, non-String or empty attribute names are prohibited.");
            }

            value = entry.getValue();
            if ((value == null) || !(value instanceof Values)) {
                throw new IllegalArgumentException("Null or non-Values attribute values are prohibited.");
            }
        }

        this.attributes.clear();

        iterator = attributes.entrySet().iterator();

        while (iterator.hasNext()) {
            entry = (Map.Entry) iterator.next();
            String attributeName = (String) entry.getKey();
            Values values = (Values) entry.getValue();
            this.attributes.put(attributeName, values);
        }
    }

    /**
     * Gets all the values for the specified attribute.</p>
     *
     * @param  attributeName a <code>String</code> representing the name of the attribute whose values will be
     *         retrieved.
     * @return a <code>Values</code> object containing the values of the specified attribute.
     * @throws IllegalArgumentException if requested attribute does not exist or is an empty <code>String</code>.
     * @throws NullPointerException if specified <code>String</code> is <code>null</code>.
     */
    public Values getValues(String attributeName) {
        checkAttributeName(attributeName);
        if (!attributes.containsKey(attributeName)) {
            throw new IllegalArgumentException("The specified attribute does not exist : " + attributeName);
        }
        return (Values) attributes.get(attributeName);
    }

    /**
     * Deletes the specified attribute from this entry. Returns silently if requested attribute does not exist.</p>
     *
     * @param  attributeName the name of the attribute whose values will be deleted.
     * @throws IllegalArgumentException if specified <code>String</code> is empty.
     * @throws NullPointerException if specified attribute name is <code>null</code>.
     */
    public void deleteAttribute(String attributeName) {
        checkAttributeName(attributeName);
        attributes.remove(attributeName);
    }

    /**
     * A helper "guard" method rejecting the invalid attribute names throwing the appropriate runtime exception (either
     * <code>NulPointerException</code> or <code>IllegalArgumentException</code>).
     *
     * @param attributeName a <code>String</code> representing the attribute name to check.
     */
    private void checkAttributeName(String attributeName) {
        if (attributeName == null) {
            throw new NullPointerException("Null attribute names are not prohibited.");
        }

        if (attributeName.trim().length() == 0) {
            throw new IllegalArgumentException("Empty attribute names are prohibited.");
        }
    }

}
