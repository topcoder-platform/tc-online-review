/*
 * DefaultAssociationAlgorithm.java
 *
 * Copyright (C) 2004, TopCoder, Inc. All rights reserved
 */

package com.topcoder.util.classassociations;

import com.topcoder.util.classassociations.*;
import java.util.*;

/**
 * <p>Default implementation of the AssociationAlgorithm. This implementation would
 *  return the handler that is directly associated with the class of the &quot;target&quot;.
 *  If there is no handler that is directly associated with the &quot;target&quot;,
 *  then it will return the handler of specified target such that:</p>
 * <ul>
 * <li>The handler is registered to a superclass of specified target.</li>
 * <li>No other handlers exist which are registered to a subclass of specified superclass.
 *  (ie, it is the most direct parent of specified target).</li>
 * <li>If the class associated with the handler is an interface, then there must be
 *  no &quot;non-interface&quot; classes which are supertypes of the target that have registered handlers.</li>
 * <li>See Component Specification for a more lengthy explanation. </li>
 * </ul>
 * <p>Thus, the &quot;Closest&quot; handler would be returned. </p>
 * <p>If no handlers meet the said criteria, then a <code>null</code> object would be returned.</p>
 *
 *
 * @author TCSDEVELOPER
 * @version 1.0  3-26-2004
 */
public class DefaultAssociationAlgorithm implements AssociationAlgorithm {

    /**
     * <p>Constructor for the DefaultAssociationAlgorithm class. </p>
     */
    public  DefaultAssociationAlgorithm() {
        // coded in case this ever becomes a subclass of some other class
        super();
    } // end DefaultAssociationAlgorithm

    /**
     * <p>Retrieves a &quot;handler&quot; object for specified &quot;target&quot; object.
     *      The associations that were specified in ClassAssociator will be used to
     *      determine the appropriate &quot;Handler&quot; to return.</p>
     * <p>See Class Documentation for details on the implementation of this algorithm.</p>
     * <p>Valid: associator = any non-null ClassAssociator </p>
     * <p>Valid: target = any non-null Class </p>
     * <p>Invalid: associator = null (throws IllegalArgumentException) </p>
     * <p>Invalid: target = null (throws IllegalArgumentException) </p>
     *
     *
     *
     * @return A "closest" handler associated with the given class.  See class
     *      documentation for more details.
     * @param associator A ClassAssociator which contains the associations which
     *      are to be used by the algorithm.
     * @param target The Class  whose "Handler" you want to retrieve.
     * @throws IllegalArgumentException if any of the given parameters is null.
     */
    public Object retrieveHandler(ClassAssociator associator, Class target) {
        Map groupAssociations  = null;   // target/handler group associations
        Object handler         = null;   // handler found for target parameter
        Class key              = null;   // used to iterate over group associations
        Class nearestClass     = null;   // used to search for class handlers
        Class nearestInterface = null;   // used to search for class handlers

		// validate arguments
		if (associator == null) {
			throw new IllegalArgumentException("associator argument is null");
		} else if (target == null) {
			throw new IllegalArgumentException("associator argument is null");
		}

        // find handler objects whose associated class is the type of the target
        // and return if present
        handler = associator.getAssociations().get(target);
        if (handler != null) {
            return handler;
        }
        
        // get association Map from group associator
        groupAssociations = associator.getGroupAssociations();

        // if there is no association with actual type of target, search for associations
        // (that can handle subtypes) that are associated with the supertype of the target
        // which is lowest in the hierarchy (assuming Object is the highest).
        // If the handler is associated with a class that is an interface, the handler
        // must NOT be returned unless there are no handlers associated with "true"
        // or "non-interface" classes. Otherwise, return the handler associated
        // with the Class.
        // If two or more interface handlers are found, then return an interface
        // handler such that there are no other interface handlers which are
        // registered as subtypes of the returned interface handler.
        // Any candidate is suitable.

        // find the nearest Class; if no superclasses exist, look for nearest
        // interface. If no interface found, there is no handler.
        for (Iterator i = groupAssociations.keySet().iterator(); i.hasNext(); ) {
            key = (Class)i.next();
            if (key.isAssignableFrom(target)) {
                if (key.isInterface()) {
                    // key in an interface
                    if (nearestInterface == null) {
                        nearestInterface = key;
                    } else if (nearestInterface.isAssignableFrom(key)) {
                        // interface is a subinterface of nearestClass and
                        // closest superinterface to target so far
                        nearestInterface = key;
                    }
                } else {
                    // key is a Class
                    if (nearestClass == null) {
                        nearestClass = key;
                    } else if (nearestClass.isAssignableFrom(key)) {
                        // key is a subclass of nearestClass and
                        // closest superclass of target so far
                        nearestClass = key;
                    }
                } // end if
            } // end if
        } // end for


        // determine which class or interface was found;
        // if nearestClass and nearestInterface are null, there is no handler for target
        if (nearestClass != null) {
            handler = groupAssociations.get(nearestClass);
        } else if (nearestInterface != null) {
            handler = groupAssociations.get(nearestInterface);
        }

        // return handler or null
        return handler;
    } // end retrieveHandler

 } // end DefaultAssociationAlgorithm
