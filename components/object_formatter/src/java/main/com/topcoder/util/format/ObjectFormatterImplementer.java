/*
 * ObjectFormatterImplementer.java
 *
 * Copyright (C) 2002, TopCoder, Inc. All rights reserved
 *
 */

package com.topcoder.util.format;

import java.util.Vector;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Set;
import java.util.Map;

/**
 * A formatter which implements the {@link ObjectFormatter} for non-primitive 
 * types (ie <code>Object</code>s).  The formatter associates certain types 
 * with format method, and will format an <code>Object</code> <code>obj</code> 
 * as a string, using the format method whose associated type most closely 
 * matches <code>obj</code>'s type. <p>
 *
 * @author garyk
 * @version 1.0
 **/
class ObjectFormatterImplementer implements ObjectFormatter {

    /*
     * a HashMap to store the Class types and their associated 
     * ObjectFormatMethods, and their boolean values about whether the 
     * subtypes of them should be formatted with their format methods
     */
    private HashMap classMap;  

    /**
     * Creates a ObjectFormatterImplementer
     **/
    ObjectFormatterImplementer() {
        classMap = new HashMap();
    }

    /**
     * Gets the format method associated with a particular type.  If
     * there is no format method associated with this particular type,
     * this method will return <code>null</code>; it will <i>not</i>
     * return the format method whose associated type is the closest
     * supertype of this particular type.
     *
     * @param type the <code>Class</code> to query for an associated
     *             format method.
     * @return the format method associated with <code>type</code>, or
     *         <code>null</code> if none exists
     * @throws IllegalArgumentException if <code>type</code> is null
     **/
    public ObjectFormatMethod getFormatMethodForClass(Class type)
            throws IllegalArgumentException {
        if (type == null) {
            throw new IllegalArgumentException();
        }

        ObjectFormatMethod ofm = null;

        /*
         * get the type's associated Vector which contains the 
         * associated ObjectFormatMethod and boolean value
         */
        Vector v = (Vector) classMap.get(type);

        /*
         * if the associated Vector exists in the HashMap, get
         * the ObjectFormatMethod
         */
        if (v != null) {
            ofm = (ObjectFormatMethod) v.get(0);
        }
	
        return ofm;
    }
    
    /**
     * Sets the format method associated with a particular type (and
     * possibly subtypes).  If a format method is already set for this
     * type, then that entry is overwritten, even if
     * <code>fFormatSubtypes</code> is different.  Note: this means
     * that calling this method could result in fewer types being
     * formatted by this <code>ObjectFormatter</code>.
     *
     * @param type the <code>Class</code> (and possibly subtypes) to
     *             be associated with this format method
     * @param ofm the format method for this type (and possibly
     *            subtypes)
     * @param fFormatSubtypes <code>true</code> if subtypes of the
     *                        supplied type should be formatted with
     *                        this format method
     * @throws IllegalArgumentException if <code>type</code> is null
     * @throws NullPointerException if <code>ofm</code> is null    
     **/
    public void setFormatMethodForClass(Class type, ObjectFormatMethod ofm,
                                        boolean fFormatSubtypes)
            throws IllegalArgumentException, NullPointerException {
        if (type == null) {
            throw new IllegalArgumentException();
        } else if (ofm == null) {
            throw new NullPointerException();
        }

        /*
         * create a new Vector to store the ObjectFormatMethod and boolean 
         * for the new Class type
         */
        Vector v = new Vector();

        /* add the ObjectFormatMethod and boolean into the Vector */
        v.add(ofm);
        v.add(new Boolean(fFormatSubtypes));

        /*
         * associate the Vector(value) with the Class type(key) 
         * in the HashMap
         */
        classMap.put(type, v);
    }

    /**
     * Removes the associated format method for a particular type.  If
     * this type has no format method associated with it, then an
     * exception is thrown.
     *
     * @param type the type whose associated format method should no
     *             longer apply to it
     * @throws IllegalArgumentException if <code>type</code> has no
     *         format method associated with it
     **/
    public void unsetFormatMethodForClass(Class type)
            throws IllegalArgumentException {
        /* remove the mapping for the Class type if present */
        if (classMap.containsKey(type)) {
            classMap.remove(type);
        } else {
            throw new IllegalArgumentException();
        }
    }
    
    /**
     * Gets the format method that would be used to format the given
     * object.  If the given object cannot be formatted by this
     * formatter (either because it has no format method that applies
     * to it, or because it has two or more equally applicable format
     * methods), then <code>null</code> is returned.  See the class
     * javadoc above for details on determining the appropriate
     * format method.
     *
     * @param obj an <code>Object</code> that might be formatted
     * @return the format method that would be used to format
     *         <code>obj</code>, or <code>null</code> if none exists
     * @throws IllegalArgumentException if <code>obj</code> is null
     **/
    public ObjectFormatMethod getFormatMethodForObject(Object obj)
            throws IllegalArgumentException {
        if (obj == null) {
            throw new IllegalArgumentException();
        }

        /* a vector to store the superTypes of obj */
        Vector superTypes = new Vector(); 
        int superTypesCount; // count of elements in superTypes
        int count = 0; // count of super classes of a specific type
        Class curType = null; // current Class type
        Class objType = obj.getClass(); //the Class type of obj

        /* get the ObjectFormatMethod for the Class type of obj */
        ObjectFormatMethod ofm = getFormatMethodForClass(objType);

        /*
         * if there is no format method for the Class type of object,
         * get the closest super type of the type of object
         */
        if (ofm == null) {
            /* get all the entries in the HashMap into a set */
            Set entries = classMap.entrySet();

            /* get all the super types of the type of object */
            for (Iterator it = entries.iterator(); it.hasNext(); ) {
                /* get the next entry in the entry set */
                Map.Entry entry = (Map.Entry) it.next();

                /* get the Class type */
                curType = (Class) entry.getKey();

                /*
                 * get the type's associated Vector which contains the 
                 * associated ObjectFormatMethod and boolean value
                 */
                Vector v = (Vector) entry.getValue();

                /*
                 * get the boolean value about whether the sub types of 
                 * the current type should be formatted with this format method
                 */
                boolean fFormatSubtypes = ((Boolean) v.get(1)).booleanValue();

                /*
                 * If curType is super type of objType and sub types of curType
                 * can use its format method, then add that type's name into the 
                 * superTypes Vector
                 */
                if (curType.isAssignableFrom(objType) && fFormatSubtypes) {
                    superTypes.add(curType);
                }
            }

            /* get the size of the superTypes Vector */
            superTypesCount = superTypes.size();

            /*
             * check if there is a Class type, which is sub type of all the  
             * others
             */
            for (int i = 0; (i < superTypesCount) && (count != superTypesCount);
                    i++) {
                curType = (Class)superTypes.get(i);  // get a Class type
                count = 0; // count for the current type
	
                /*
                 * get the number of types, which are super types of the 
                 * current type
                 */
                for (int j = 0; j < superTypesCount; j++) {
                    Class tempType = (Class)superTypes.get(j);
                    if (tempType.isAssignableFrom(curType)) {
                        count++;
                    }
                }
            }

            /*
             * if count is equals to the number of elements in superTypes, and 
             * the count is not 0, then the closest type exists and that type
             * is curType
             */
            if ((count != 0) && (count == superTypesCount)) {
                ofm = getFormatMethodForClass(curType);
            }
        }

        return ofm;
    }
    
    /**
     * Converts the given <code>Object</code> into an appropriate
     * string representation.  The format method used is the one that
     * would be returned by <code>getFormatMethodForObject</code>.
     * See the class javadoc above for details on determining the
     * appropriate format method.
     *
     * @param obj an <code>Object</code> to be formatted
     * @return a formatted string representing <code>obj</code>
     * @throws IllegalArgumentException if <code>obj</code> is null, or 
     *         there is no format method, or two or more equally applicable 
     *         format methods to use to format <code>obj</code>
     **/
    public String format(Object obj) throws IllegalArgumentException {
        ObjectFormatMethod ofm = getFormatMethodForObject(obj);

        // if ofm is not null, format the obj using its format
        // format method, otherwise throw a IllegalArgumentException
        if(ofm != null)
            return ofm.format(obj);
        else
            throw new IllegalArgumentException();
    }
}