package com.topcoder.util.commandline.functionaltests;

import junit.framework.TestCase;
import com.topcoder.util.commandline.* ;
import java.util.* ;

/**
 * <p></p>
 * @author bokbok
 * @version 1.0
 */

public class ParameterTests extends TestCase {
    private CommandLineUtility util = null ;

    /**
     * Require 2 parameters.
     */
    public void setUp() {
      try {
        util = new CommandLineUtility(false, 2, 2);
        util.addSwitch(new Switch("D", true, 1, 1, null));
        util.addSwitch(new Switch("F", false, 1, 1, null));
        util.addSwitch(new Switch("Roo", false, 1, 1, null));
      } catch (IllegalSwitchException e) {
        fail() ;
      }
    }

    /**
     * <p>Test where no parameters are provided - should fail.</p>
     */
    public void testNoParameters() {
        String args[] = {"-D", "test"} ;
        try {
            util.parse(args) ;
        } catch (ArgumentValidationException ave) {
            fail("Argument validation not enabled") ;
        } catch (UsageException ue) {
            // success (two parameters required)
            return ;
        }
        fail("Incorrect usage - should have failed") ;
    }

    /**
     * <p>Test where three parameters are provided - should fail.</p>
     */
    public void testThreeParameters() {
        String args[] = {"-D", "test", "param1", "param2", "param3",} ;
        try {
            util.parse(args) ;
        } catch (ArgumentValidationException ave) {
            fail("Argument validation not enabled") ;
        } catch (UsageException ue) {
            // success (two parameters required)
            return ;
        }
        fail("Incorrect usage - should have failed") ;
    }

    /**
     * <p>Test where two parameters are provided - should succeed.</p>
     */
    public void testTwoParameters() {
        String args[] = {"-D", "test", "param1", "param2"} ;
        try {
            util.parse(args) ;
        } catch (ArgumentValidationException ave) {
            fail("Argument validation not enabled") ;
        } catch (UsageException ue) {
            fail("Correct usage.") ;
        }
        List params = util.getParameters() ;
        assertNotNull(params) ;
        assertEquals(2, params.size()) ;
        assertEquals("param1", params.get(0)) ;
        assertEquals("param2", params.get(1)) ;
    }
}
