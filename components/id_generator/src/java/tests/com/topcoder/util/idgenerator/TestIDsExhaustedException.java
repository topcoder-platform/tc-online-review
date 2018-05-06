/**
 * Copyright &copy; 2004, TopCoder Inc. All rights reserved.
 */
package com.topcoder.util.idgenerator;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Test;

/**
 * Test the behaviour of IDsExhaustedException. The situation
 * when the exception should be thrown resides in the test of
 * corresponding class.
 *
 * @author gua
 * @version 2.0
 */
public class TestIDsExhaustedException extends TestCase {

    /**
     * Return the test suite of this class.
     *
     * @return the test suite of this class.
     */
    public static Test suite() {
        return new TestSuite(TestIDsExhaustedException.class);
    }

    /**
     * Test the behaviour of constructor.
     */
    public void testConstructor1() {
        Exception excp = new IDsExhaustedException();
        assertNull("No message", excp.getMessage());
    }

    /**
     * Test the behaviour of constructor.
     */
    public void testConstructor2() {
        Exception excp = new IDsExhaustedException("Fail");
        assertTrue("Fail message",
            excp.getMessage().indexOf("Fail") >= 0);
    }

    /**
     * <p>Inheritance test.</p>
     *
     * <p>Verifies IDsExhaustedException subclasses IDGenerationException.</p>
     */
    public void testIDsExhaustedExceptionInheritance() {
        assertTrue("IDsExhaustedException does not subclass IDGenerationException.",
                new IDsExhaustedException() instanceof IDGenerationException);
    }

    /**
     * <p>Inheritance test.</p>
     *
     * <p>Verifies IDsExhaustedException subclasses IDGenerationException.</p>
     */
    public void testIDsExhaustedExceptionInheritance2() {
        assertTrue("IDsExhaustedException does not subclass IDGenerationException.",
                new IDsExhaustedException("error") instanceof IDGenerationException);
    }
}
