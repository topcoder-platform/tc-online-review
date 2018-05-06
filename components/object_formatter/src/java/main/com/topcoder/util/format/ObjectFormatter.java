/*
 * ObjectFormatter.java
 *
 * Copyright (C) 2002, TopCoder, Inc. All rights reserved
 *
 */

package com.topcoder.util.format;


/**
 * A formatter for non-primitive types (ie <code>Object</code>s).  The
 * formatter associates certain types with format method, and will
 * format an <code>Object</code> <code>obj</code> as a string, using
 * the format method whose associated type most closely matches
 * <code>obj</code>'s type. <p>
 *
 * To initialize an <code>ObjectFormatter</code> for use, it should be
 * given <code>ObjectFormatMethod</code>s for the types it will have
 * to format.  Note that an <code>ObjectFormatMethod</code> can be
 * used to format not only a specific given type, but also all
 * sub-types.  There is no default formatting applied to objects that
 * have no applicable format method; if this behavior is desired, a
 * format method should be added for <code>Object</code>. <p>
 *
 * If an <code>Object</code> <code>obj</code> could be formatted by
 * more than one format method, then the format method whose
 * associated type is closest to <code>obj</code>'s type will be used.
 * This closest type is determined by first determining all format
 * methods (a) whose associated type is a super-type of
 * <code>obj</code> (that is, it is an interface that <code>obj</code>
 * implements, or it is a superclass of <code>obj</code>), and (b)
 * that actually could format <code>obj</code> (because either the
 * type matches exactly, or because the type is a super-type and is
 * allowed to format sub-types).  This set of types
 * <i>type<sub>1</sub>, type<sub>2</sub>, ... type<sub>n</sub></i> is
 * then scanned to determine if there is any <i>type<sub>i</sub></i>
 * that is a sub-type of all the others.  If so, then the format
 * method associated with <i>type<sub>i</sub></i> is used to format
 * <code>obj</code>; if not, an exception is thrown.  Note that this
 * formatting algorithm ensures that if there is a format method
 * associated with <code>obj</code>'s exact run-time type, then that
 * format method will be used to format <code>obj</code>. <p>
 *
 * As an example, suppose an <code>ObjectFormatter</code> has format
 * methods associated with <code>Object</code>,
 * <code>Collection</code>, <code>List</code>, and
 * <code>RandomAccess</code> types, and suppose that all of these
 * format methods are allowed to format sub-types.  If a
 * <code>LinkedList</code> were to be formatted, then the format
 * method for <code>List</code>s would be used, because
 * <code>Object</code>, <code>Collection</code>, and <code>List</code>
 * are super-types of </code>LinkedList</code>, and <code>List</code>
 * is a sub-type of the other two.  If, however, an
 * <code>ArrayList</code> were to be formatted, then the
 * <code>ObjectFormatter</code> would throw an exception, because
 * <code>List</code> and <code>RandomAccess</code> are both
 * super-types if <code>ArrayList</code>, but neither is a sub-type of
 * the other. <p>
 *
 * @author KurtSteinkraus
 * @author garyk
 * @version 1.0
 **/
public interface ObjectFormatter {
    
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
     **/
    public ObjectFormatMethod getFormatMethodForClass(Class type);
    
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
     **/
    public void setFormatMethodForClass(Class type, ObjectFormatMethod ofm,
                                        boolean fFormatSubtypes);
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
            throws IllegalArgumentException;
    
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
    public ObjectFormatMethod getFormatMethodForObject(Object obj);
    
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
    public String format(Object obj) throws IllegalArgumentException;
}
