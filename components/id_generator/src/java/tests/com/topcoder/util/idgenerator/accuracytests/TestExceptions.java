/**
 * Copyright (C) 2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.idgenerator.accuracytests;

import junit.framework.TestCase;

import com.topcoder.util.idgenerator.*;
import com.topcoder.util.errorhandling.*;

/**
 * This class tests the exception classes (IDGenerationException, IDsExhaustedException, NoSuchIDSequenceException).
 *
 * The following aspects are tested for each of these classes:
 * 1). Is its parent class correct?
 * 2). Are the parameters passed to ctors (if any) actually set to its internal state?
 *
 * @author RoyItaqi
 * @version 3.0
 */
public class TestExceptions extends TestCase {
    
    /** The message to be passed to the ctors. */
    private final String MESSAGE = "this is a message";
    
    /** The cause to be passed to the ctors. */
    private final Throwable CAUSE = new Throwable("this is a cause");
    
    
    /** The IDGenerationException object used for testing. */
    private IDGenerationException myIDGenerationException = new IDGenerationException();

    /** The IDsExhaustedException object used for testing. */
    private IDsExhaustedException myIDsExhaustedException = new IDsExhaustedException();

    /** The NoSuchIDSequenceException object used for testing. */
    private NoSuchIDSequenceException myNoSuchIDSequenceException = new NoSuchIDSequenceException();


    public void testIDGenerationException_parent() {
        assertTrue("IDGenerationException should be child of BaseException",
                   myIDGenerationException instanceof BaseException);
    }
    
    public void testIDsExhaustedException_parent() {
        assertTrue("IDsExhaustedException should be child of IDGenerationException",
                   myIDsExhaustedException instanceof IDGenerationException);
    }
    
    public void testNoSuchIDSequenceExceptionn_parent() {
        assertTrue("NoSuchIDSequenceException should be child of IDGenerationException",
                   myNoSuchIDSequenceException instanceof IDGenerationException);
    }

    
    public void testIDGenerationException_ctors() {
        myIDGenerationException = new IDGenerationException(MESSAGE, CAUSE);
        assertTrue("Message should be set correctly", myIDGenerationException.getMessage().startsWith(MESSAGE));
        assertEquals("Cause should be set correctly", CAUSE, myIDGenerationException.getCause());
        
        myIDGenerationException = new IDGenerationException(MESSAGE);
        assertEquals("Message should be set correctly", MESSAGE, myIDGenerationException.getMessage());
        
        myIDGenerationException = new IDGenerationException(CAUSE);
        assertEquals("Cause should be set correctly", CAUSE, myIDGenerationException.getCause());
    }

    public void testIDsExhaustedException_ctors() {
        myIDsExhaustedException = new IDsExhaustedException(MESSAGE);
        assertEquals("Message should be set correctly", MESSAGE, myIDsExhaustedException.getMessage());
    }
    
    public void testNoSuchIDSequenceException_ctors() {
        myNoSuchIDSequenceException = new NoSuchIDSequenceException(MESSAGE);
        assertEquals("Message should be set correctly", MESSAGE, myNoSuchIDSequenceException.getMessage());
    }

}