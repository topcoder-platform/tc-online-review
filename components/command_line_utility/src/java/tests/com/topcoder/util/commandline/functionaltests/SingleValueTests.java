package com.topcoder.util.commandline.functionaltests;

import junit.framework.TestCase;
import com.topcoder.util.commandline.* ;
import java.util.Collection;
import java.util.Iterator;

/**
 * <p>Tests </p>
 * @author bokbok
 * @version 1.0
 */

public class SingleValueTests extends TestCase {
    private CommandLineUtility util = null ;

    public void setUp() {
      try {
        util = new CommandLineUtility();
        util.addSwitch(new Switch("D", false, 1, 1, null));
        util.addSwitch(new Switch("F", true, 1, 1, null));
        util.addSwitch(new Switch("Roo", false, 1, 1, null));
      } catch (IllegalSwitchException e) {
        fail() ;
      }
    }

    /**
     * <p>Test where no valid switches are provided.</p>
     */
    public void testNoSwitches() {
        String args[] = {"D", "blah"} ;
        try {
            util.parse(args);
        } catch (ArgumentValidationException ave) {
            fail("Argument validation not enabled") ;
        } catch (UsageException ue) {
            // success (switch "F" is required)
            return ;
        }
        fail("Incorrect usage - should have failed") ;
    }

    /**
     * <p>Test a switch set for a minimum of 1 value but is submitted with
     *    none - should fail.</p>
     */
    public void testNoValue() {
        String args[] = {"-D", "-F", "blah", "-Roo", "blah"} ;
        // this should be parsed with -F as the value for -D,
        // which means that -F will not have the required value
        // -or- -D should flag having no arguments (preferred - see note in Switch)
        try {
            util.parse(args) ;
        } catch (ArgumentValidationException ave) {
            fail("Argument validation not enabled") ;
        } catch (UsageException ue) {
            // success (switch "F" is required)
            return ;
        }
        fail("Incorrect usage - should have failed") ;
    }

    /**
     * <p>Test a switch set for a maximum of 1 value with 2 values - should fail.</p>
     */
    public void testTwoValues() {
        String args[] = {"-D", "test", "test2", "-F", "blah", "-Roo", "blah"} ;
        // this should be parsed with -F as the value for -D,
        // which means that -F will not have the required value
        try {
            util.parse(args) ;
        } catch (ArgumentValidationException ave) {
            fail("Argument validation not enabled") ;
        } catch (UsageException ue) {
            // success (switch "F" is required)
            return ;
        }
        fail("Incorrect usage - should have failed") ;
    }

    /**
     * <p>Test a single switch - should succeed.</p>
     */
    public void testOneSwitch() {
        String args[] = {"-F", "test"} ;
        try {
            util.parse(args) ;
        } catch (ArgumentValidationException ave) {
            fail("Argument validation not enabled") ;
        } catch (UsageException ue) {
            fail("Correct usage threw UsageException") ;
        }
        Collection switches = util.getValidSwitches() ;
        assertNotNull(switches) ;
        assertEquals(1, switches.size()) ;
        for (Iterator it = switches.iterator() ; it.hasNext() ;) {
            Switch s = (Switch) it.next() ;
            assertEquals("F", s.getName()) ;
            assertEquals(s.getValue(), "test") ;
        }
    }

    /**
     * <p>Test all switches with different single value formats - should succeed.</p>
     */
    public void testAllSwitches() {
        String args[] = {"-D", "test", "-F=test2", "-Rootest3"} ;
        try {
            util.parse(args) ;
        } catch (ArgumentValidationException ave) {
            fail("Argument validation not enabled") ;
        } catch (UsageException ue) {
            fail("Correct usage threw UsageException") ;
        }
        Collection switches = util.getValidSwitches() ;
        assertNotNull(switches) ;
        assertEquals(3, switches.size()) ;
        for (Iterator it = switches.iterator() ; it.hasNext() ;) {
            Switch s = (Switch) it.next() ;
            if ("D".equals(s.getName())) {
                assertEquals("D", s.getName()) ;
                assertEquals(s.getValue(), "test") ;
            }
            if ("F".equals(s.getName())) {
                assertEquals("F", s.getName()) ;
                assertEquals(s.getValue(), "test2") ;
            }
            if ("Roo".equals(s.getName())) {
                assertEquals("Roo", s.getName()) ;
                assertEquals(s.getValue(), "test3") ;
            }
        }
    }
}