/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.db.connectionfactory.stresstests;

/**
 * <p>
 * Dummy connection interface with additional information used in test. This is not an extension to
 * <code>Connection</code>, but its implementation should always implement <code>Connection</code>.
 * </p>
 *
 * @author magicpig
 * @version 1.0
 */
public interface DummyConnection {
    /**
     * <p>
     * Get the information stored in this conection.
     * </p>
     *
     * @return information stored in this conection
     */
    public String getInformation();
}



