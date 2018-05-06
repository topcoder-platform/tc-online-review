/*
 * AssociationAlgorithm.java
 *
 * Copyright (C) 2004, TopCoder, Inc. All rights reserved
 */

package com.topcoder.util.classassociations;

import java.util.*;

/**
 * <p>An object that can retrieve &quot;Handlers&quot; for specific targets. Each
 *  implementation offers different logic of determining which is the appropriate
 *  &quot;Handler&quot; to return. </p>
 *
 *
 * @author TCSDEVELOPER
 * @version 1.0  3-28-2004
 */
public interface AssociationAlgorithm {


    /**
     * <p>Retrieves a &quot;handler&quot; object for specified &quot;target&quot;
     *  object. The associations that were specified in ClassAssociator will be used
     *  to determine the appropriate &quot;Handler&quot; to return.</p>
     * <p>Valid: associator = any non-null ClassAssociator </p>
     * <p>Valid: target = any non-null Class </p>
     * <p>Invalid: associator = null (throws IllegalArgumentException) </p>
     * <p>Invalid: target = null (throws IllegalArgumentException) </p>
     *
     *
     * @return A "Handler" object that is determined by the implementation of this
     *  interface. Or <code>null</code> if no appropriate handler can be found.
     * @param associator A ClassAssociator which contains the associations which
     *  are to be used by the algorithm.
     * @param target The Class whose "Handler" you want to retrieve.
     * @throws IllegalArgumentException if any of the given parameters is null.
     */
    public Object retrieveHandler(ClassAssociator associator, Class target);

} // end AssociationAlgorithm





