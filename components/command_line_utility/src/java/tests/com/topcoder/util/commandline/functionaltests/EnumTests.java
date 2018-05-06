package com.topcoder.util.commandline.functionaltests;

import junit.framework.TestCase;
import com.topcoder.util.commandline.* ;
import java.util.* ;

/**
 * <p>Test EnumValidator</p>
 * @author bokbok
 * @version 1.0
 */

public class EnumTests extends TestCase {
    private CommandLineUtility util = null ;

    /**
     * <p>Create integer validators for big, small, and negative numbers.</p>
     */
    public void setUp() {
        util = new CommandLineUtility() ;
        String pets[] = {"dog", "cat", "rat", "fish"} ;
        String colors[] = {"red", "blue", "green"} ;
        String empty[] = {} ;
        EnumValidator petValidator = new EnumValidator(Arrays.asList(pets)) ;
        EnumValidator colorValidator = new EnumValidator(Arrays.asList(colors)) ;
        EnumValidator emptyValidator = new EnumValidator(Collections.EMPTY_LIST) ;

        try {
          util.addSwitch(new Switch("pet", false, 1, 1, petValidator));
          util.addSwitch(new Switch("color", false, 1, 1, colorValidator));
          util.addSwitch(new Switch("empty", false, 1, 1, emptyValidator));
        } catch (IllegalSwitchException e) {
          fail() ;
        }
    }

    /**
     * Test pets list with an invalid value.
     */
    public void testPetFail() {
        String args[] = {"-pet", "mongoose"} ;
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
     * Test pets list with an valid value.
     */
    public void testPetSuccess() {
        String args[] = {"-pet", "dog"} ;
        try {
           util.parse(args);
       } catch (ArgumentValidationException ave) {
           fail("Valid argument") ;
       } catch (UsageException ue) {
           fail("Correct usage") ;
       }
       Collection switches = util.getValidSwitches() ;
       assertNotNull(switches) ;
       assertEquals(1, switches.size()) ;
       for (Iterator it = switches.iterator() ; it.hasNext() ;) {
           Switch s = (Switch) it.next() ;
           assertEquals("pet", s.getName()) ;
           assertEquals(s.getValue(), "dog") ;
       }

    }

    /**
     * Test colors list with an invalid value.
     */
    public void testColorFail() {
        String args[] = {"-color", "maroon"} ;
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
     * Test colors list with an valid value.
     */
    public void testColorSuccess() {
        String args[] = {"-color", "red"} ;
        try {
           util.parse(args);
       } catch (ArgumentValidationException ave) {
           fail("Valid argument") ;
       } catch (UsageException ue) {
           fail("Correct usage") ;
       }
       Collection switches = util.getValidSwitches() ;
       assertNotNull(switches) ;
       assertEquals(1, switches.size()) ;
       for (Iterator it = switches.iterator() ; it.hasNext() ;) {
           Switch s = (Switch) it.next() ;
           assertEquals("color", s.getName()) ;
           assertEquals(s.getValue(), "red") ;
       }
    }

    /**
     * Test empty list with an invalid value.
     */
    public void testEmptyFail() {
        String args[] = {"-empty", "anyvalue"} ;
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
}