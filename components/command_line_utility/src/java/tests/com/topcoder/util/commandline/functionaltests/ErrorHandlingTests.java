package com.topcoder.util.commandline.functionaltests;

import junit.framework.TestCase;
import com.topcoder.util.commandline.* ;
import java.util.* ;

/**
 * <p>Test EnumValidator</p>
 * @author bokbok
 * @version 1.0
 */

public class ErrorHandlingTests extends TestCase {
    private CommandLineUtility util = null ;

    /**
     * <p>Create integer validators for big, small, and negative numbers.</p>
     */
    public void setUp() {
        util = new CommandLineUtility(true, 0, 1) ;
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
     * Test multiple validation failures
     */
    public void testMultipleFail() {
        String args[] = {"-pet", "mongoose", "-color", "maroon","-empty", "anyvalue", "param1"} ;
        try {
            util.parse(args);
        } catch (ArgumentValidationException ave) {
            fail("Error handling is off") ;
        } catch (UsageException ue) {
            fail("Correct usage") ;
        }
        Collection validSwitches = util.getValidSwitches() ;
        assertEquals(0, validSwitches.size()) ;
        Collection switches = util.getSwitches() ;
        assertNotNull(switches) ;
        assertEquals(3, switches.size()) ;
        for (Iterator it = switches.iterator() ; it.hasNext() ;) {
            Switch s = (Switch) it.next() ;
            assertNotNull(s.getErrors()) ;
            assertEquals(1, s.getErrors().size()) ;
        }
    }
}
