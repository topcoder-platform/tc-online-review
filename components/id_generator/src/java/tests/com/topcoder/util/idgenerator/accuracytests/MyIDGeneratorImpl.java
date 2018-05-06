/**
 * Copyright (C) 2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.idgenerator.accuracytests;

import com.topcoder.util.idgenerator.*;

import java.math.BigInteger;


/**
 * A dummy implementation of IDGenerator interface.  This class is used for IDGeneratorFactory tests.
 *
 * @author RoyItaqi
 * @version 3.0
 */
public class MyIDGeneratorImpl implements IDGenerator {
    
    /** Dummy ID */
    private final String DUMMY_ID = "my_dummy_implementation";
    
    /** Dummy ctor which does nothing. */
    public MyIDGeneratorImpl() {
        // nothing
    }
    
    /** Dummy ctor which does nothing. */
    public MyIDGeneratorImpl(String name) {
        // nothing
    }
    
    /**
     * Dummy operation which does nothing.
     */
    public String getIDName() {
        return DUMMY_ID;
    }

    /**
     * Dummy operation which does nothing.
     */
    public long getNextID() throws IDGenerationException {
        return 0;
    }

    /**
     * Dummy operation which does nothing.
     */
    public BigInteger getNextBigID() throws IDGenerationException {
        return null;
    }

    /**
     * Do nothing
     */
    public void dispose() {
    }
}
