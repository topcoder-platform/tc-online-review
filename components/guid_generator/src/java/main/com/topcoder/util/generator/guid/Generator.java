/**
 * Copyright (C) 2004, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.generator.guid;

/**
 * <p>
 * defines the contract that Generators must obey. A generator will need to
 * implement a getNextUUID() method using whatever algorithm it defines
 * </p>
 * <p>
 * <strong>Thread Safety: </strong> Implementors of this interface should be thread safe.
 * </p>
 * 
 * @author TCSDEVELOPER
 * @author TCSDESIGNER 
 * @version 1.0
 */
public interface Generator {

    /**
     * <p>
     * implementors of this interface will generate the next UUID based on it's individual logic
     * </p>
     *
     * @return a non-null UUID
     */
    public UUID getNextUUID();

}

