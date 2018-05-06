/**
 * Copyright &copy; 2004, TopCoder Inc. All rights reserved.
 */
package com.topcoder.util.idgenerator;

import com.topcoder.util.errorhandling.BaseException;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Test;

/**
 * Test the behaviour of IDGenerationException. The situation
 * when the exception should be thrown resides in the test of
 * corresponding class.
 *
 * @author gua
 * @version 2.0
 */
public class TestIDGenerationException extends TestCase {
    /**
     * Return the test suite of this class.
     *
     * @return the test suite of this class.
     */
    public static Test suite() {
        return new TestSuite(TestIDGenerationException.class);
    }

    /**
     * Test the behaviour of constructor.
     */
    public void testConstructor1() {
        Exception excp = new IDGenerationException();
        assertNull("No message", excp.getMessage());
    }

    /**
     * Test the behaviour of constructor.
     */
    public void testConstructor2() {
        Exception excp = new IDGenerationException("Fail");
        assertTrue("Fail message",
            excp.getMessage().indexOf("Fail") >= 0);
    }

    /**
     * <p>Inheritance test.</p>
     *
     * <p>Verifies IDGenerationException subclasses BaseException.</p>
     */
    public void testIDGenerationExceptionInheritance() {
        assertTrue("IDGenerationException does not subclass BaseException.",
                new IDGenerationException() instanceof BaseException);
    }

    /**
     * <p>Inheritance test.</p>
     *
     * <p>Verifies IDGenerationException subclasses BaseException.</p>
     */
    public void testIDGenerationExceptionInheritance2() {
        assertTrue("IDGenerationException does not subclass BaseException.",
                new IDGenerationException("error") instanceof BaseException);
    }

    /**
     * <p>Inheritance test.</p>
     *
     * <p>Verifies IDGenerationException subclasses BaseException.</p>
     */
    public void testIDGenerationExceptionInheritance3() {
        assertTrue("IDGenerationException does not subclass BaseException.",
                new IDGenerationException(new Exception()) instanceof BaseException);
    }

    /**
     * <p>Inheritance test.</p>
     *
     * <p>Verifies IDGenerationException subclasses BaseException.</p>
     */
    public void testIDGenerationExceptionInheritance4() {
        assertTrue("IDGenerationException does not subclass BaseException.",
                new IDGenerationException("error", new Exception()) instanceof BaseException);
    }
}
