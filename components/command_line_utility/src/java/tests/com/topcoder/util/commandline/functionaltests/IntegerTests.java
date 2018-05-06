package com.topcoder.util.commandline.functionaltests;

import junit.framework.TestCase;
import com.topcoder.util.commandline.* ;
import java.util.* ;

/**
 * <p>Test IntegerValidator</p>
 * @author bokbok
 * @version 1.0
 */

public class IntegerTests extends TestCase {
    private CommandLineUtility util = null ;

    /**
     * <p>Create integer validators for big, small, and negative numbers.</p>
     */
    public void setUp() {
      try {
        util = new CommandLineUtility();
        IntegerValidator bigValidator = new IntegerValidator(new Integer(100), null);
        IntegerValidator smallValidator = new IntegerValidator(new Integer(0),
            new Integer(10));
        IntegerValidator negativeValidator = new IntegerValidator(null,
            new Integer(0));
        util.addSwitch(new Switch("Big", false, 1, 1, bigValidator));
        util.addSwitch(new Switch("Small", false, 1, 1, smallValidator));
        util.addSwitch(new Switch("Neg", false, 1, 1, negativeValidator));
      } catch (IllegalSwitchException e) {
        fail() ;
      }
    }

    /**
     * <p>Test big validator with small number.</p>
     */
    public void testBigValidatorFail() {
        String args[] = {"-Big", "7"} ;
        try {
            util.parse(args);
        } catch (ArgumentValidationException ave) {
            // success
            return ;
        } catch (UsageException ue) {
            fail("Correct usage") ;
        }
        fail("Invalid argument - should have failed") ;

    }

    /**
     * <p>Test big validator with big number.</p>
     */
    public void testBigValidatorSucceed() {
        String args[] = {"-Big", "1007"} ;
        try {
            util.parse(args);
        } catch (ArgumentValidationException ave) {
            fail("Valid number") ;
        } catch (UsageException ue) {
            fail("Correct usage") ;
        }
        Collection switches = util.getValidSwitches() ;
        assertNotNull(switches) ;
        assertEquals(1, switches.size()) ;
        for (Iterator it = switches.iterator() ; it.hasNext() ;) {
            Switch s = (Switch) it.next() ;
            assertEquals("Big", s.getName()) ;
            assertEquals(s.getValue(), "1007") ;
        }

    }
    /**
     * <p>Test small validator with big number.</p>
     */
    public void testSmallValidatorFail() {
        String args[] = {"-Small", "1007"} ;
        try {
            util.parse(args);
        } catch (ArgumentValidationException ave) {
            // success
            return ;
        } catch (UsageException ue) {
            fail("Correct usage") ;
        }
        fail("Invalid argument - should have failed") ;
    }

    /**
     * <p>Test small validator with small number.</p>
     */
    public void testSmallValidatorSucceed() {
        String args[] = {"-Small", "2"} ;
        try {
            util.parse(args);
        } catch (ArgumentValidationException ave) {
            fail("Valid number") ;
        } catch (UsageException ue) {
            fail("Correct usage") ;
        }
        Collection switches = util.getValidSwitches() ;
        assertNotNull(switches) ;
        assertEquals(1, switches.size()) ;
        for (Iterator it = switches.iterator() ; it.hasNext() ;) {
            Switch s = (Switch) it.next() ;
            assertEquals("Small", s.getName()) ;
            assertEquals(s.getValue(), "2") ;
        }

    }
    /**
     * <p>Test negative validator with positive number.</p>
     */
    public void testNegativeValidatorFail() {
        String args[] = {"-Neg", "7"} ;
        try {
            util.parse(args);
        } catch (ArgumentValidationException ave) {
            // success
            return ;
        } catch (UsageException ue) {
            fail("Correct usage") ;
        }
        fail("Invalid argument - should have failed") ;

    }

    /**
     * <p>Test negative validator with negative number.</p>
     */
    public void testNegativeValidatorSucceed() {
        String args[] = {"-Neg", "-100"} ;
        try {
            util.parse(args);
        } catch (ArgumentValidationException ave) {
            fail("Valid number") ;
        } catch (UsageException ue) {
            fail("Correct usage") ;
        }
        Collection switches = util.getValidSwitches() ;
        assertNotNull(switches) ;
        assertEquals(1, switches.size()) ;
        for (Iterator it = switches.iterator() ; it.hasNext() ;) {
            Switch s = (Switch) it.next() ;
            assertEquals("Neg", s.getName()) ;
            assertEquals(s.getValue(), "-100") ;
        }

    }
}