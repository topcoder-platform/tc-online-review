/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.distribution;

import java.io.InputStream;

import com.topcoder.util.log.Log;

/**
 * <p>
 * This interface represents a distribution script parser. It defines a single
 * method for parsing the list of distribution script commands from the given
 * input stream and setting them to the given DistributionScript instance.
 * </p>
 *
 * <p>
 * Thread Safety: Implementations of this interface must be thread safe assuming
 * that the given InputStream and DistributionScript instances are used by the
 * caller in thread safe manner.
 * </p>
 *
 * @author saarixx, TCSDEVELOPER
 * @version 1.0
 */
public interface DistributionScriptParser {
    /**
     * <p>
     * Parses the script commands from the given input stream and sets them to
     * commands property of the given DistributionScript instance.
     * </p>
     *
     * @param log
     *            the logger to be used by constructed commands (null if logging
     *            in commands is not required)
     * @param script
     *            the distribution script instance to which parsed commands must
     *            be set
     * @param stream
     *            the input stream for reading script
     *
     * @throws IllegalArgumentException
     *             if stream or script is null
     * @throws DistributionScriptParsingException
     *             if some error occurs when parsing a distribution script
     */
    public void parseCommands(InputStream stream, DistributionScript script,
            Log log) throws DistributionScriptParsingException;
}
