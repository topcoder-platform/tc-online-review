/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.log.jdk14;

import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogFactory;

/**
 * <p>
 * This is the implementation of the <code>LogFactory</code> interface that will create Jdk14Log instances based on
 * the java logger for the given name.
 * </p>
 *
 * <p>
 * <b>Changes for version 2.0</b>
 * This is a new class.
 * </p>
 *
 * <p>
 * This <code>LogFactory</code> implementation will be used by the application to produce Log(s) that will write the
 * logging information to the java logging system.  This <code>LogFactory</code> implementation can be specified in
 * the LogManager.configure method.  The <code>LogManager</code> will then call the <code>createLog</code> method to
 * create a log (in this case, a <code>Jdk14Log</code>) when a Log implementation is needed.
 * </p>
 *
 * <p>
 * <b>Thread Safety: </b>
 * This class is thread safe by having no mutable state information.
 * </p>
 *
 * @author Pops, TCSDEVELOPER
 * @version 2.0
 */
public class Jdk14LogFactory implements LogFactory {

    /**
     * <p>
     * Constructs the Jdk14LogFactory.
     * </p>
     */
    public Jdk14LogFactory() {
        // empty
    }

    /**
     * <p>
     * Creates a Log implementation and returns it.
     * </p>
     *
     * <p>
     * This factory will create and return a Jdk14Log with the given name.
     * </p>
     *
     * @param name A possibly null, possibly empty (trim'd) string representing the name assigned to the log
     *
     * @return A non-null log implementation (in this case a Jdk14Log)
     */
    public Log createLog(String name) {
        return new Jdk14Log(name);
    }
}
