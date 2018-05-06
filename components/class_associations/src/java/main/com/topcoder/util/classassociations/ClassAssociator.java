/*
 * ClassAssociator.java
 *
 * Copyright (C) 2004, TopCoder, Inc. All rights reserved
 */

package com.topcoder.util.classassociations;

import com.topcoder.util.classassociations.IllegalHandlerException;
import java.util.*;

/**
 * <p>This is the main class of the Class Associations component. The ClassAssociator
 *      encapsulates the mapping of &quot;Handler&quot; objects to specific
 *      &quot;Target&quot; classes. The ClassAssociator can then retrieve the
 *      appropriate &quot;Handler&quot; for a specified &quot;Target&quot; class
 *      or object. The &quot;Handlers&quot; are retrieved using the default
 *      Association Algorithm, which simply returns the handler which is
 *      &quot;closest&quot; to the class of the &quot;Target&quot; that was
 *      specified (see the DefaultAssociationAlgorithm for more details).
 *      The ClassAssociator also offers support for additional pluggable
 *      association algorithms. </p>
 * <p>The ClassAssociator can optionally restrict Handler objects to a specific
 *      class.&nbsp; This prevents clients from inadvertently adding the wrong
 *      &quot;Handler&quot; types, if so required.</p>
 *
 *
 * @author TCSDEVELOPER
 * @version 1.0  3-30-2004
 */
public class ClassAssociator {


    /**
     * <p>This is the mapping of &quot;Handler&quot; objects to a single class.
     *  The keys of this Map are the &quot;target&quot; Classes, and the values
     *  of this Map are the registered &quot;Handler&quot; objects of those
     *  Classes.</p>
     *
     */
    private Map associations = new HashMap();
    
    /**
     * <p>This is a read-only (unmodifiable) view of the associations member 
     * variable.  This will be returned when retrieving the associations 
     * Map so that the mappings can be returned to a client without
     * jeopardizing the integrity of the internal associations member.</p>
     *
     */
    private Map associationsView = null;

    /**
     * <p>This is the mapping of &quot;Handler&quot; objects to a hiearchy of
     *  classes. The keys of this Map are the parent Classes of the
     *  &quot;target&quot; Class Hiearchy, and the values of this Map are the
     *  registered &quot;Handler&quot; objects of those Classes.</p>
     *
     */
    private Map groupAssociations = new HashMap();

	/**
     * <p>This is a read-only (unmodifiable) view of the groupAssociations member 
     * variable.  This member will be returned when retrieving the 
     * groupAssociations Map so that the mappings can be returned to a client 
     * without jeopardizing the integrity of the internal associations member.</p>
     *
     */
    private Map groupAssociationsView = null;

    /**
     * <p>This is the mapping of Association Algorithms to specified keys. The
     *  keys of this Map are Strings which are the name of specified Association
     *  Algorithm. The default Association Algorithm is mapped to the String with
     *  the value: &quot;default&quot;.</p>
     *
     */
    private Map algorithms = new HashMap();

    /**
     * <p>Determines whether the ClassAssociator would restrict the allowable
     *  &quot;Handler&quot; objects to a specified class. If this is set to true,
     *  then the ClassAssociator would only accept Associations with
     *  &quot;Handlers&quot; which are types or subtypes of the
     *  HandlerRestrictionClass.</p>
     *
     */
    private boolean handlerRestriction = false;

    /**
     * <p>The allowable Handler objects which can be registered with this ClassAssociator.
     * If handlerRestriction is set to true, then the ClassAssociator will only
     * accept instances or subtypes of this class.</p>
     * <p><strong>Note to Developers:</strong> It is not possible specify the
     * default value of this class here (will cause a compile error), but during
     * ClassAssociator construction, the initial value of this field must be
     * the class corresponding to &quot;Object&quot;.</p>
     *
     */
    private Class handlerRestrictionClass = null;

    /**
     * <p>This String represents the key which is used to access the default
     *  Association algorithm of this ClassAssociator. During construction,
     *  the default algorithm that is used is an instance of the
     *  DefaultAssociationAlgorithm. </p>
     *
     * @see DefaultAssociationAlgorithm
     */
    public static final String DEFAULT_ALGORITHM = "default";


    /**
     * <p>Creates a ClassAssociator with the default algorithm registered, no
     *  existing associations, handlerRestriction set to false and
     *  handlerRestriction class as &quot;Object&quot;</p>
     */
    public  ClassAssociator() {
        this(null, null);
    } // end ClassAssociator

    /**
     * <p>Creates a ClassAssociator with the default algorithm registered,
     *  specified associations, handlerRestriction set to false and
     *  handlerRestriction class
     * as &quot;Object&quot;</p>
     * <p>If any of the specified Maps is null, a new HashMap is initialized for
     * that parameter.</p>
     * <p>Valid Input: associations =
     *      new HashMap().add(Class.forName(&quot;java.io.FileInputStream&quot;),
     *      new FileReader())</p>
     * <p>Valid Input: groupAssns =
     *      new HashMap().add(Class.forName(&quot;java.io.FileInputStream&quot;),
     *      new FileReader())</p>
     * <p>Valid Input: associations = null</p>
     * <p>Valid Input: groupAssns = null</p>
     * <p>Invalid Input: associations =
     *      new HashMap().add(new String(&quot;java.io.FileInputStream&quot;),
     *      new FileReader()) (throws IllegalArgumentException)</p>
     * <p>Invalid Input: groupAssns =
     *      new HashMap().add(new String(&quot;java.io.FileInputStream&quot;),
     *      new FileReader()) (throws IllegalArgumentException)</p>
     * <p>Invalid Input: associations =
     *      new HashMap().add(Class.forName(&quot;java.io.FileInputStream&quot;),
     *      null) (throws IllegalArgumentException)</p>
     * <p>Invalid Input: groupAssns =
     *      new HashMap().add(Class.forName(&quot;java.io.FileInputStream&quot;),
     *      null) (throws IllegalArgumentException)</p>
     *
     * @param associations A mapping of "Target" Classes to their specified
     *      "Handler" objects. May be null.
     * @param groupAssns A mapping of "Target" Class hiearchies to their specified
     *      "Handler" objects. May be null.
     * @throws IllegalArgumentException if any of the given keys are null, or
     *      any of the given keys are not instances of the "Class" object, or
     *      any of the given values within the Map are null.
     */
    public  ClassAssociator(Map associations, Map groupAssns) {
        Object key = null;     // used for parameter validation

		// set/copy the new associations Map
		try {
	        // if an associations Map was provided, validate it and store a copy
	        if (associations != null) {
	            this.setAssociations(associations);
	        } else {
	        	// since no new associations will be made, a read-only Map copy of
	        	// associations must be made.
	        	this.associationsView = Collections.unmodifiableMap(this.associations);
	        }
	    } catch (IllegalHandlerException ihe) {
	    	// translate this exception to one declared and specified by the constructor
        	throw new IllegalArgumentException(ihe.getMessage());
        }
	
		// set/copy the new group associations Map
		try {
	        // if an groupAssns Map was provided, validate it and store a copy
	        if (groupAssns != null) {
	           this.setGroupAssociations(groupAssns);
	        } else {
	        	// since no new group associations will be made, a read-only Map copy of
	        	// groupAssociations must be made.
	        	this.groupAssociationsView = Collections.unmodifiableMap(this.groupAssociations);
	        }
        } catch (IllegalHandlerException ihe) {
        	// translate this exception to one declared and specified by the constructor
        	throw new IllegalArgumentException(ihe.getMessage());
        }

        // set handlerRestrictionClass = Object
        this.handlerRestrictionClass = Object.class;

        // disable the handler restriction; user must explicitly enable it
        this.handlerRestriction = false;

        // setup DefaultAssociationAlgoritm as default algorithm
        this.setDefaultAlgorithm(new DefaultAssociationAlgorithm());

    } // end ClassAssociator

    /**
     * <p>Creates a ClassAssociator with the default algorithm registered, no
     *      existing associations, handlerRestriction set to true and
     *      handlerRestriction class as specified.</p>
     * <p>Valid: _handlerRestrictionClass = Any non-null Class.</p>
     * <p>Invalid: _handlerRestrictionClass = null (throws IllegalArgumentException) </p>
     *
     * @param _handlerRestrictionClass The Class of all allowable Handler objects
     * @throws IllegalArgumentException if the given argument is null.
     */
    public  ClassAssociator(Class _handlerRestrictionClass) {
        // Use the other constructor to perform all other construction tasks.
        this (null, null);

        // Set handler Restriction Class if not null
        if (_handlerRestrictionClass != null) {
            this.handlerRestrictionClass = _handlerRestrictionClass;
            this.handlerRestriction = true;
        } else {
            throw new IllegalArgumentException ("null Class argument not allowed.");
        }

    } // end ClassAssociator

    /**
     * <p>Registers the specified handler with the Class of specified object.</p>
     * <p>This is equivalent to calling addClassAssociation(target.getClass(), handler)</p>
     * <p><em>(If handlerRestriction is false)</em></p>
     * <p>Valid: target = any non-null Object</p>
     * <p>Valid: handler = any non-null Object</p>
     * <p>Invalid: target = null (throws IllegalArgumentException)</p>
     * <p>Invalid: handler = null (throws IllegalArgumentException)</p>
     * <p><em>(if handlerRestriction is true) </em></p>
     * <p>Valid: handler = any Object that is an instance of the Class indicated by
     *  handlerRestrictionClass</p>
     * <p>Invalid: handler = any Object that is not an instance of the Class
     *  indicated by handlerRestrictionClass (throws IllegalHandlerException)</p>
     *
     *
     * @param target an Object whose class is specified as a "target" for the given handler.
     * @param handler The "Handler" object for the class of specified object.
     * @throws IllegalArgumentException if any of the given parameters is null.
     * @throws IllegalHandlerException if handler does not belong to handlerRestrictionClass
     *  and handlerRestriction is true.
     */
    public void addAssociation(Object target, Object handler) throws IllegalHandlerException {
        // target is the only parameter that must be tested for null here since the
        // class must be retrieved before forwarding the message to the proper method.
        if (target == null) {
            throw new IllegalArgumentException ("Target object is null. This is not allowed. ");
        }

        // forward this messge to the proper method so associations will be added uniformly
        this.addClassAssociation(target.getClass(), handler);
    } // end addAssociation

    /**
     * <p>Registers the specified handler with the specified class.</p>
     * <p>Valid: target = any non-null Class</p>
     * <p>Valid: handler = any non-null Object</p>
     * <p>Invalid: target = null (throws IllegalArgumentException)</p>
     * <p>Invalid: handler = null (throws IllegalArgumentException)</p>
     * <p><em>(if handlerRestriction is true) </em></p>
     * <p>Valid: handler = any Object that is an instance of the Class indicated by
     *      handlerRestrictionClass</p>
     * <p>Invalid: handler = any Object that is not an instance of the Class indicated by
     *     handlerRestrictionClass(throws IllegalHandlerException)</p>
     *
     * @param target The target Class.
     * @param handler The handler to be associated with target Class.
     * @throws IllegalArgumentException if any of the given parameters is null.
     * @throws IllegalHandlerException if handler does not belong to handlerRestrictionClass
     *      and handlerRestriction is true.
     */
    public void addClassAssociation(Class target, Object handler) throws IllegalHandlerException {
        // validate parameters for null values
        if ((target == null) && (handler == null)) {
            throw new IllegalArgumentException ("target cannot be null. handler cannot be null");
        } else if (target == null) {
            throw new IllegalArgumentException ("target object is null.");
        } else if (handler == null) {
            throw new IllegalArgumentException ("handler object is null.");
        }

        // validate handler further if handlerRestriction is true
        // if true, handler's class must be the same class or a subclass of
        // this.handlerRestrictionClass
        if ((this.handlerRestriction == true)
            && (!(this.handlerRestrictionClass.isAssignableFrom(handler.getClass())))) {
                throw new IllegalHandlerException ("Handler restriction is active and "
                    + "handler is not of the same class as the handler restriction class.");
        }

        // associate the handler to the target Class
        this.associations.put(target, handler);
    } // end addClassAssociation

    /**
     * <p>Registers the specified handler with the Class hierarchy of specified object.
     *    This means that the handler is associated with the given Class, as well as all
     *      the sub-classes of the given Class.</p>
     * <p>This is equivalent to calling addGroupClassAssociation(target.getClass(), handler)</p>
     * <p>Valid: target = any non-null Object</p>
     * <p>Valid: handler = any non-null Object</p>
     * <p>Invalid: target = null (throws IllegalArgumentException)</p>
     * <p>Invalid: handler = null (throws IllegalArgumentException)</p>
     * <p><em>(if handlerRestriction is true) </em></p>
     * <p>Valid: handler = any Object that is an instance of the Class indicated
     *      by handlerRestrictionClass</p>
     * <p>Invalid: handler = any Object that is not an instance of the Class indicated by
     *  handlerRestrictionClass (throws IllegalHandlerException)</p>
     *
     * @param target an Object whose class is specified as a "target" for the given handler.
     * @param handler The "Handler" object for the class of specified object.
     * @throws IllegalArgumentException if any of the given parameters is null.
     * @throws IllegalHandlerException if handler does not belong to handlerRestrictionClass and
     *  handlerRestriction is true.
     */
    public void addGroupAssociation(Object target, Object handler) throws IllegalHandlerException {
        // target is the only parameter that must be tested for null here since the
        // class must be retrieved before forwarding the message to the proper method.
        if (target == null) {
            throw new IllegalArgumentException ("target object is null.");
        }

        // forward this messge to the proper method so associations will be added uniformly
        this.addGroupClassAssociation(target.getClass(), handler);
    } // end addGroupAssociation

    /**
     * <p>Registers the specified handler with the specified class. This means
     *      that the handler is associated with the given Class, as well as all
     *      the sub-classes of the given Class.</p>
     * <p>Valid: target = any non-null Class</p>
     * <p>Valid: handler = any non-null Object</p>
     * <p>Invalid: target = null (throws IllegalArgumentException)</p>
     * <p>Invalid: handler = null (throws IllegalArgumentException)</p>
     * <p><em>(if handlerRestriction is true) </em></p>
     * <p>Valid: handler = any Object that is an instance of the Class indicated
     *      by handlerRestrictionClass</p>
     * <p>Invalid: handler = any Object that is not an instance of the Class
     *      indicated by handlerRestrictionClass (throws IllegalHandlerException)</p>
     *
     * @param target The target Class.
     * @param handler The handler to be associated with target Class.
     * @throws IllegalArgumentException if any of the given parameters is null.
     * @throws IllegalHandlerException if handler does not belong to
     *      handlerRestrictionClass and handlerRestriction is true.
     */
    public void addGroupClassAssociation(Class target, Object handler) throws IllegalHandlerException {
        // validate parameters for null values
        if ((target == null) && (handler == null)) {
            throw new IllegalArgumentException ("target cannot be null. handler cannot be null");
        } else if (target == null) {
            throw new IllegalArgumentException ("target object is null.");
        } else if (handler == null) {
            throw new IllegalArgumentException ("handler object is null.");
        }

        // validate handler further if handlerRestriction is true
        // if true, handler's class must be the same class or a subclass of
        // this.handlerRestrictionClass
        if ((this.handlerRestriction == true)
            && (!(this.handlerRestrictionClass.isAssignableFrom(handler.getClass())))) {
                throw new IllegalHandlerException ("Handler restriction is active and "
                    + "handler is not of the same class as the handler restriction class.");
        }

        // associate the handler to the target Class
        this.groupAssociations.put(target, handler);
    } // end addGroupClassAssociation

    /**
     * <p>Removes any &quot;handlers&quot; that are currently associated with
     *      the Class of the specified object. </p>
     * <p>This is equivalent to calling removeClassAssociation(target.getClass())</p>
     * <p>Valid: target = any non-null Object</p>
     * <p>Invalid: target = null (throws IllegalArgumentException)</p>
     *
     * @return The handler object that was unregistered, or null if there was no handler registered.
     * @param target An Object whose whose registered Handlers you wish to remove.
     * @throws IllegalArgumentException if any of the given parameters are null.
     */
    public Object removeAssociation(Object target) {
        // must validate target because the class must be retrieved before
        // forwarding the message to the proper method.
        if (target == null) {
            throw new IllegalArgumentException ("target object is null.");
        }

        // forward this messge to the proper method so associations will be removed uniformly
        return this.removeClassAssociation(target.getClass());
    } // end removeAssociation

    /**
     * <p>Removes any &quot;handlers&quot; that are currently associated with the specified Class. </p>
     * <p>Valid: target = any non-null Class</p>
     * <p>Invalid: target = null (throws IllegalArgumentException)</p>
     *
     * @return The handler object that was unregistered, or null if there was no handler registered.
     * @param target The target Class whose handler you wish to unregister.
     * @throws IllegalArgumentException if any of the given parameters are null.
     */
    public Object removeClassAssociation(Class target) {
        // validate parameters for null values
        if (target == null) {
            throw new IllegalArgumentException ("target object is null.");
        }

        // remove the handler of the target Class
        return this.associations.remove(target);
    } // end removeClassAssociation

    /**
     * <p>Removes any &quot;handlers&quot; that are currently associated with the Class
     *      hiearchy of the specified object.</p>
     * <p><strong>Note:</strong> This will only unregister handlers of hiearchies of
     *      which the target class is the &quot;root&quot; (topmost superclass).
     *      This is done to prevent any inadvertent removal of handlers in different
     *      levels of the same hiearchy.</p>
     * <p>This is equivalent to calling removeGroupClassAssociation(target.getClass())</p>
     * <p>Valid: target = any non-null Object</p>
     * <p>Invalid: target = null (throws IllegalArgumentException)</p>
     *
     *
     *
     * @return The handler object that was unregistered, or null if there was no handler registered.
     * @param target An Object whose whose registered Handlers you wish to remove.
     * @throws IllegalArgumentException if any of the given parameters are null.
     */
    public Object removeGroupAssociation(Object target) {
        // must validate target because the class must be retrieved before
        // forwarding the message to the proper method.
        if (target == null) {
            throw new IllegalArgumentException ("target object is null.");
        }

        // forward this messge to the proper method so associations will be removed uniformly
        return this.removeGroupClassAssociation(target.getClass());
    } // end removeGroupAssociation

    /**
     * <p>Removes any &quot;handlers&quot; that are currently associated with the specified Class. </p>
     * <p><strong>Note:</strong> This will only unregister handlers of hiearchies of which the target
     *  class is the &quot;root&quot; (topmost superclass). This is done to prevent any inadvertent
     *  removal of handlers in different levels of the same hiearchy.</p>
     * <p>Valid: target = any non-null Class</p>
     * <p>Invalid: target = null (throws IllegalArgumentException)</p>
     *
     *
     *
     * @return The handler object that was unregistered, or null if there was no handler registered.
     * @param target The target Class whose handler you wish to unregister.
     * @throws IllegalArgumentException if any of the given parameters are null.
     */
    public Object removeGroupClassAssociation(Class target) {
        // validate parameters for null values
        if (target == null) {
            throw new IllegalArgumentException ("target object is null.");
        }

        // remove the handler of the target Class

        return this.groupAssociations.remove(target);
    } // end removeGroupClassAssociation

    /**
     * <p>Returns the mapping of &quot;Handler&quot; objects to a single class.
     *      The keys of this Map are the &quot;target&quot; Classes, and the values
     *      of this Map are the registered &quot;Handler&quot; objects of those
     *      Classes.&nbsp; For safety purposes, a read-only copy of the Map is returned so
     *      that illegal &quot;handlers&quot; and data types will not inadvertently
     *      make their way into the map.&nbsp; Any changes made to the map will have
     *      to be updated using the setAssociations() method.</p>
     *
     * @return A read-only mapping of "Target" Classes to their specified "Handler" objects.
     */
    public Map getAssociations() {
        // return the read-only Map copy/view.
        return this.associationsView;
    } // end getAssociations

    /**
     * <p>Sets the mapping of &quot;Handler&quot; objects to a single class. The
     *      keys of this Map are the &quot;target&quot; Classes, and the values of this
     *      Map are the registered &quot;Handler&quot; objects of those Classes.</p>
     * <p><em>(If handlerRestriction is false)</em></p>
     * <p>Valid Input: new HashMap().add(Class.forName(&quot;java.io.FileInputStream&quot;),
     *                                   new FileReader()) </p>
     * <p>Invalid Input: new HashMap().add(new String(&quot;java.io.FileInputStream&quot;),
     *                                     new FileReader()) (throws IllegalArgumentException) </p>
     * <p>Invalid Input: new HashMap().add(Class.forName(&quot;java.io.FileInputStream&quot;), null)
     *      (throws IllegalArgumentException)</p>
     * <p><em>(if handlerRestriction is true) </em></p>
     * <p>Invalid Input: new HashMap().add(Class.forName(&quot;java.io.FileInputStream&quot;),
     *      new String()) (if the RestrictionClass was, for example, of the java.io.Reader class)
     *      (throws IllegalHandlerException) </p>
     *
     * @param _associations A mapping of "Target" Classes to their specified "Handler" objects.
     * @throws IllegalArgumentException if any of the given keys are null, or any
     *    of the given keys are not instances of the "Class" object, or any of
     *    the given values are null.
     * @throws IllegalHandlerException if the map contains handlers that do not
     *    belong to handlerRestrictionClass and handlerRestriction is true.
     */
    public void setAssociations(Map _associations) throws IllegalHandlerException {
        Map.Entry entry       = null;     // used for parameter validation

        // validate the argument for null value
        if (_associations == null) {
            throw new IllegalArgumentException("null arguments not allowed. _associations is null");
        }

        // validate that associations has only [Class]->[non-null object] associations;
        // if handlerRestriction is true, verify that the class of all handlers
        // matches handlerRestrictionClass.
        //   Note: 1 if and 2 for loops created instead of embedding the
        //   restrictionHandler check inside 1 for loop to prevent the condition
        //   from being checked for each item in the map.  Conditionals are expensive
        //   process-wise, for a little extra code, you get 1 conditional test for
        //   handlerRestriction instead of N tests.
        if (this.handlerRestriction == true) {          
            for (Iterator i = _associations.entrySet().iterator(); i.hasNext(); ) {
                entry = (Map.Entry) i.next();
                if (!(entry.getKey() instanceof Class)) {
                    throw new IllegalArgumentException("All '_associations' keys must be Class objects.");
                } else if (entry.getValue() == null) {
                    throw new IllegalArgumentException("All '_associations' values must be non-null.");
                } else if (! this.handlerRestrictionClass.isAssignableFrom(entry.getValue().getClass())) {
                    throw new IllegalHandlerException("handlerRestriction is enabled"
                            + "and the class of one of the handler object is invalid.");
                }
            } // end for
            
        } else {
            // handlerRestriction == false ; 
            for (Iterator i = _associations.entrySet().iterator(); i.hasNext(); ) {
                entry = (Map.Entry) i.next();
                if (!(entry.getKey() instanceof Class)) {
                    throw new IllegalArgumentException("All '_associations' keys must be Class objects.");
                } else if (entry.getValue() == null) {
                    throw new IllegalArgumentException("All '_associations' values must be non-null.");
                }
            } // end for
        }

        // assign a copy of _associations to this.associations
        this.associations = new HashMap();
        this.associations.putAll(_associations);
        this.associationsView = Collections.unmodifiableMap(this.associations);
    } // end setAssociations

    /**
     * <p>Returns the mapping of &quot;Handler&quot; objects to a hiearchy of classes.
     *      The keys of this Map are the parent Classes of the &quot;target&quot;
     *      Class Hiearchy, and the values of this Map are the registered &quot;Handler&quot;
     *      objects of those Classes.&nbsp; </p>
     *
     * @return A read-only mapping of "Target" Classes to their specified "Handler" objects.
     */
    public Map getGroupAssociations() {
        // return the read-only Map copy/view
        return this.groupAssociationsView;
    } // end getGroupAssociations

    /**
     * <p>Sets the mapping of &quot;Handler&quot; objects to a hiearchy of classes.
     *      The keys of this Map are the parent Classes of the &quot;target&quot;
     *      Class Hiearchy, and the values of this Map are the registered
     *      &quot;Handler&quot; objects of those Classes.</p>
     * <p><em>(If handlerRestriction is false)</em></p>
     * <p>Valid Input: new HashMap().add(Class.forName(&quot;java.io.FileInputStream&quot;), new FileReader())</p>
     * <p>Invalid Input: new HashMap().add(new String(&quot;java.io.FileInputStream&quot;), new FileReader())
     *      (throws IllegalArgumentException)</p>
     * <p>Invalid Input: new HashMap().add(Class.forName(&quot;java.io.FileInputStream&quot;), null)
     *      (throws IllegalArgumentException)</p>
     * <p><em>(if handlerRestriction is true)</em></p>
     * <p>Invalid Input: new HashMap().add(Class.forName(&quot;java.io.FileInputStream&quot;), new String())
     *      (if the RestrictionClass was, for example, of the java.io.Reader class)
     *      (throws IllegalHandlerException)</p>
     *
     * @param _groupAssociations A mapping of "Target" Class hiearchies to their
     *      specified "Handler" objects.
     * @throws IllegalArgumentException if any of the given keys are null, or any
     *      of the given keys are not instances of the "Class" object, or any of
     *      the given values within the Map are null.
     * @throws IllegalHandlerException if the map contains handlers that do not
     *      belong to handlerRestrictionClass and handlerRestriction is true.
     */
    public void setGroupAssociations(Map _groupAssociations) throws IllegalHandlerException {
        Map.Entry entry       = null;     // used for parameter validation

        // validate the argument for null value
        if (_groupAssociations == null) {
            throw new IllegalArgumentException("null arguments not allowed. _groupAssociations is null");
        }

        // validate that associations has only [Class]->[non-null object] associations;
        // if handlerRestriction is true, verify that the class of all handlers
        // matches handlerRestrictionClass.
        //   Note: 1 if and 2 for loops created instead of embedding the
        //   restrictionHandler check inside 1 for loop to prevent the condition
        //   from being checked for each item in the map.  Conditionals are expensive
        //   process-wise, for a little extra code, you get 1 conditional test for
        //   handlerRestriction instead of N tests.
        if (this.handlerRestriction == true) {
            for (Iterator i = _groupAssociations.entrySet().iterator(); i.hasNext(); ) {
                entry = (Map.Entry) i.next();
                if (!(entry.getKey() instanceof Class)) {
                    throw new IllegalArgumentException("All '_groupAssociations' keys must be Class objects.");
                } else if (entry.getValue() == null) {
                    throw new IllegalArgumentException("All '_groupAssociations' values must be non-null.");
                } else if (! this.handlerRestrictionClass.isAssignableFrom(entry.getValue().getClass())) {
                    throw new IllegalHandlerException("handlerRestriction is enabled"
                            + "and the class of one of the handler object is invalid.");
                }
            } // end for
        } else {
            // handlerRestriction == false           
            for (Iterator i = _groupAssociations.entrySet().iterator(); i.hasNext(); ) {
                entry = (Map.Entry) i.next();
                if (!(entry.getKey() instanceof Class)) {
                    throw new IllegalArgumentException("All '_groupAssociations' keys must be Class objects.");
                } else if (entry.getValue() == null) {
                    throw new IllegalArgumentException("All '_groupAssociations' values must be non-null.");
                }
            } // end for
        }

        // assign a copy of _groupAssociations to this.groupAssociations
        // must make a manual copy since Map interface does not implement Cloneable
        this.groupAssociations = new HashMap();
        this.groupAssociations.putAll(_groupAssociations);
        this.groupAssociationsView = Collections.unmodifiableMap(this.groupAssociations);
    } // end setGroupAssociations

    /**
     * <p>Adds a pluggable algorithm to this Class Associator, under the given
     *     algorithm name. The algorithm name will be used to specify that this
     *     AssociationAlgorithm is to be used when retrieving a Handler. </p>
     * <p>Valid: name = Any non-null String. </p>
     * <p>Valid: algorithm = Any non-null algorithm. </p>
     * <p>Invalid: name = null (throws IllegalArgumentException) </p>
     * <p>Invalid: algorithm = null (throws IllegalArgumentException) </p>
     *
     * @param name A String containing the name of the given algorithm.
     * @param algorithm The AssociationAlgorithm that implements some association
     *      logic to return the appropriate "Handler" object.
     * @throws IllegalArgumentException if any of the given parameters is null.
     */
    public void addAlgorithm(String name, AssociationAlgorithm algorithm) {
        // validate parameters for null values
        if ((name == null) && (algorithm == null)) {
            throw new IllegalArgumentException("name and algorithm are null. null values are not allowed");
        } else if (name == null) {
            throw new IllegalArgumentException("name is null. null values are not allowed");
        } else if (algorithm == null) {
            throw new IllegalArgumentException("algorithm is null. null values are not allowed");
        }

        // add algorithm to map
        this.algorithms.put(name, algorithm);
    } // end addAlgorithm

    /**
     * <p>Retrieves the pluggable algorithm to this Class Associator, under the
     *      given algorithm name.</p>
     * <p>Valid: name = Any non-null String.</p>
     * <p>Invalid: name = null (throws IllegalArgumentException)</p>

     *
     * @return The AssociationAlgorithm that implements some association logic to
     *     return the appropriate "Handler" object, or null if no
     *     AssociationAlgorithm registered under the given algorithm name.
     * @param name A String containing the name of the given algorithm.
     * @throws IllegalArgumentException if any of the given parameters is null.
     */
    public AssociationAlgorithm getAlgorithm(String name) {
        // validate parameters
        if (name == null) {
            throw new IllegalArgumentException("name is null. null values are not allowed");
        }

        // return the AssociationAlgorithm
        return (AssociationAlgorithm)this.algorithms.get(name);
    } // end getAlgorithm

    /**
     * <p>Removes the AssociationAlgorithm of the given name.</p>
     * <p>Valid: name = &quot;FooAlgorithm&quot; </p>
     * <p>Invalid: name = null (throws IllegalArgumentException) </p>
     * <p>Invalid: name = DEFAULT_ALGORITHM (throws IllegalArgumentException) </p>
     *
     * @return The AssociationAlgorithm that was removed, or null if no
     *      AssociationAlgorithm was registered under given algorithm name.
     * @param name A String containing the name of the AssociationAlgorithm to be removed.
     * @throws IllegalArgumentException if given String is null, or if given
     *      String is equal to the default algorithm.
     * @see #addAlgorithm(String name, AssociationAlgorithm algorithm)
     */
    public AssociationAlgorithm removeAlgorithm(String name) {
        // validate parameters
        if (name == null) {
            throw new IllegalArgumentException("name is null. null values are not allowed");
        } else if (name.equals(DEFAULT_ALGORITHM)) {
            throw new IllegalArgumentException("Cannot remove default algorithm.");
        }

        // remove and return the algorithm
        return (AssociationAlgorithm)this.algorithms.remove(name);
    } // end removeAlgorithm

    /**
     * <p>Sets the default algorithm to the specified algorithm. This method is
     *      equivalent to making the method call
     *      &quot;addAlgorithm(DEFAULT_ALGORITHM, fooAlgorithm)&quot;.</p>
     * <p>Valid: algorithm = Any non-null algorithm. </p>
     * <p>Invalid: algorithm = null (throws IllegalArgumentException) </p>
     *
     * @param algorithm The default AssociationAlgorithm that implements some
     *      association logic to return the appropriate "Handler" object.
     * @throws IllegalArgumentException if given parameter is null.
     */
    public void setDefaultAlgorithm(AssociationAlgorithm algorithm) {
       // validate parameters
       if (algorithm == null) {
           throw new IllegalArgumentException("algorithm is null. null values are not allowed.");
       }

       // add default algorithm
       this.addAlgorithm(DEFAULT_ALGORITHM, algorithm);
    } // end setDefaultAlgorithm

    /**
     * <p>Retrives the default algorithm for this ClassAssociator. This method
     *      is equivalent to making the method call &quot;getAlgorithm(DEFAULT_ALGORITHM)&quot;.</p>
     *
     * @return The default AssociationAlgorithm that implements some association
     *      logic to return the appropriate "Handler" object.
     */
    public AssociationAlgorithm getDefaultAlgorithm() {
        return this.getAlgorithm(DEFAULT_ALGORITHM);
    } // end getDefaultAlgorithm

    /**
     * <p>Determines whether the ClassAssociator would restrict the allowable
     *      &quot;Handler&quot; objects to a specified class. If this is set to
     *      true, then the ClassAssociator would only accept Associations with
     *      &quot;Handlers&quot; which are types or subtypes of the HandlerRestrictionClass.</p>
     *
     * @return A boolean that indicates  whether this ClassAssociator must
     *      restrict Handlers to a specified class.
     */
    public boolean getHandlerRestriction() {
        return handlerRestriction;
    } // end getHandlerRestriction

    /**
     * <p>Determines whether the ClassAssociator would restrict the allowable
     *     &quot;Handler&quot; objects to a specified class. If this is set to
     *     true, then the ClassAssociator would only accept Associations with
     *     &quot;Handlers&quot; which are types or subtypes of the HandlerRestrictionClass.</p>
     *
     * @param _handlerRestriction A boolean to determine whether this ClassAssociator
     *      must restrict Handlers to a specified class.
     * @throws IllegalStateException if the ClassAssociator already has some
     *     registered "Handler" objects which are not instances of the HandlerRestrictionClass.
     */
    public void setHandlerRestriction(boolean _handlerRestriction) {

        // if parameter value is true, validate current associations
        // and groupAssociations
        if (_handlerRestriction == true) {
            // validate current associations values and then group associations values against
            // the current handlerRestrictionClass
            //  ---
            // validate associations
            for (Iterator i = this.associations.values().iterator(); i.hasNext(); ) {
                if (! this.handlerRestrictionClass.isAssignableFrom(i.next().getClass())) {
                    throw new IllegalStateException("associations map already has "
                            + "handlers that are invalid for the restriction class.");
                }
            }
            // validate group associations
            for (Iterator i = this.groupAssociations.values().iterator(); i.hasNext(); ) {
                if (! this.handlerRestrictionClass.isAssignableFrom(i.next().getClass())) {
                    throw new IllegalStateException("group associations map already has "
                            + "handlers that are invalid for the restriction class.");
                }
            }
        }

        // set handlerRestriction
        handlerRestriction = _handlerRestriction;
    } // end setHandlerRestriction

    /**
     * <p>The allowable Handler objects which can be registered with this ClassAssociator.
     *      If handlerRestriction is set to true, then the ClassAssociator will
     *      only accept instances or subtypes of this class.</p>
     * <p><strong>Note to Developers:</strong> It is not possible specify the default
     *      value of this class here (will cause a compile error), but during
     *      ClassAssociator construction, the initial value of this field must
     *      be the class corresponding to &quot;Object&quot;.</p>
     *
     * @return A Class which restricts all registered Handler objects.
     */
    public Class getHandlerRestrictionClass() {
        return this.handlerRestrictionClass;
    } // end getHandlerRestrictionClass

    /**
     * <p>The allowable Handler objects which can be registered with this ClassAssociator.
     *      If handlerRestriction is set to true, then the ClassAssociator will
     *      only accept instances or subtypes of this class.</p>
     * <p><strong>Note to Developers:</strong> It is not possible specify the
     *      default value of this class here (will cause a compile error), but
     *      during ClassAssociator construction, the initial value of this field
     *      must be the class corresponding to &quot;Object&quot;.</p>
     *
     * @param _handlerRestrictionClass A Class which restricts all registered Handler objects.
     * @throws IllegalStateException if the ClassAssociator already has some
     *      registered "Handler" objects which are not instances of the HandlerRestrictionClass.
     * @throws IllegalArgumentException if given argument is null.
     */
    public void setHandlerRestrictionClass(Class _handlerRestrictionClass) {
        // validate parameter for null value
        if (_handlerRestrictionClass == null) {
            throw new IllegalArgumentException("_handlerRestrictionClass is null. null values are not allowed.");
        }

        // if handlerRestriction is true, validate associations and group associations
        if (this.handlerRestriction == true) {
            // validate current associations and then group associations against the
            // new handlerRestrictionClass
            // ---
            // associations
            for (Iterator i = this.associations.values().iterator(); i.hasNext(); ) {
                if (! _handlerRestrictionClass.isAssignableFrom(i.next().getClass())) {
                    throw new IllegalStateException("associations map already has "
                            + "handlers that are invalid for the restriction class.");
                }
            }
            // group associations
            for (Iterator i = this.groupAssociations.values().iterator(); i.hasNext(); ) {
                if (! _handlerRestrictionClass.isAssignableFrom(i.next().getClass())) {
                    throw new IllegalStateException("group associations map already has "
                            + "handlers that are invalid for the restriction class.");
                }
            }
            
        }

        // set the handler restriction class
        handlerRestrictionClass = _handlerRestrictionClass;
    } // end setHandlerRestrictionClass

    /**
     * <p>Retrieves the handler for the class of specified target object. The
     *      Default association algorithm that is registered with this Class
     *      Associator is used. </p>
     * <p>This is equivalent to calling retrieveClassHandler(target.getClass())</p>
     * <p>Valid: target = any non-null Object </p>
     * <p>Invalid: target = null (throws IllegalArgumentException) </p>
     *
     * @return The handler object associated with the class of target Object,
     *      as determined by the default algorithm.
     * @param target The target object whose "handler" you wish to retrieve.
     * @throws IllegalArgumentException if given parameter is null.
     */
    public Object retrieveHandler(Object target) {
        // validate parameter for null value
        if (target == null) {
            throw new IllegalArgumentException("target is null. null values are not allowed.");
        }

        // forward this message to the proper method
        return this.retrieveClassHandler(target.getClass());
    } // end retrieveHandler

    /**
     * <p>Retrieves the handler for the specified class. The Default association
     *     algorithm that is registered with this Class Associator is used. </p>
     * <p>Valid: target = any non-null Class </p>
     * <p>Invalid: target = null (throws IllegalArgumentException) </p>
     *
     * @return The handler object associated with the target class, as determined
     *     by the default algorithm.
     * @param target a Class whose handler you wish to retrieve.
     * @throws IllegalArgumentException if given parameter is null.
     */
    public Object retrieveClassHandler(Class target) {
        // validate parameter for null value
        if (target == null) {
            throw new IllegalArgumentException("target is null. null values are not allowed.");
        }

        // forward this message to the fully qualified method.
        return retrieveClassHandler(target, DEFAULT_ALGORITHM);
    } // end retrieveClassHandler

    /**
     * <p>Retrieves the handler for the class of specified target object. The
     *      algorithm specified with Algorithm Name is used. </p>
     * <p>This is equivalent to calling
     *      retrieveClassHandler(target.getClass(), algorithmname)</p>
     * <p>Valid: target = any non-null Object </p>
     * <p>Valid: algorithmname = a String name that is registered using the
     *      addAlgorithm(String name, AssociationAlgorithm algorithm) method</p>
     * <p>Invalid: target = null (throws IllegalArgumentException) </p>
     * <p>Invalid: algorithmname = &quot;non-existent algorithm name&quot;
     *      (throws IllegalArgumentException) </p>
     *
     * @return The handler object associated with the class of target Object,
     *      as determined by the specified algorithm.
     * @param target The target object whose "handler" you wish to retrieve.
     * @param algorithmname A String containing the name of the given algorithm.
     * @throws IllegalArgumentException if given parameter is null, or if given
     *      algorithmname has not been registered with the ClassAssociator.
     */
    public Object retrieveHandler(Object target, String algorithmname) {
        // must validate target because the class must be retrieved before
        // forwarding the message to the proper method.
        if (target == null) {
            throw new IllegalArgumentException ("target object is null. null values are not allowed.");
        }

        // all remainging validation will occur in the method that this message
        // is passed to in the return statement
        // retrieve Handler and return it.
        return this.retrieveClassHandler(target.getClass(), algorithmname);
    } // end retrieveHandler

    /**
     * <p>Retrieves the handler for the specified class. The algorithm specified
     *      with Algorithm Name is used. </p>
     * <p>Valid: target = any non-null Class </p>
     * <p>Valid: algorithmname = a String name that is registered using the
     *      addAlgorithm(String name, AssociationAlgorithm algorithm) method </p>
     * <p>Invalid: target = null (throws IllegalArgumentException) </p>
     * <p>Invalid: algorithmname = &quot;non-existent algorithm name&quot;
     *      (throws NoSuchAlgorithmException) </p>
     *
     * @return The handler object associated with the target class, as
     *      determined by the specified algorithm.
     * @param target a Class whose handler you wish to retrieve.
     * @param algorithmname A String containing the name of the given algorithm.
     * @throws IllegalArgumentException if given parameter is null, or if given
     *      algorithmname has not been registered with the ClassAssociator.
     */
    public Object retrieveClassHandler(Class target, String algorithmname) {
        // validate target and algorithmname for null values
        /*if ((target == null) && (algorithmname == null)) {
            throw new IllegalArgumentException("target and algorithmname are null. null values are not allowed");
        } else*/ if (target == null) {
            throw new IllegalArgumentException("target is null. null values are not allowed");
        } else if (algorithmname == null) {
            throw new IllegalArgumentException("algorithmname is null. null values are not allowed");
        }

        // retreive algorithm for algorithmname
        AssociationAlgorithm alg = this.getAlgorithm(algorithmname);
        // validate algorithm
        if (alg == null) {
            throw new IllegalArgumentException("algorithm name provided - "
                + algorithmname + " - does not have a registered AssociationAlgorithm.");
        }

        // retrieve handler using the AssociationAlgorithm.
        return alg.retrieveHandler(this, target);
    } // end retrieveClassHandler

 } // end ClassAssociator
