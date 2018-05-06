package com.topcoder.util.commandline.functionaltests;

import junit.framework.TestCase;
import com.topcoder.util.commandline.* ;
import java.util.* ;

/**
 * <p>Test multiple value separator and switch delimiter</p>
 * @author bokbok
 * @version 1.0
 */

public class DelimiterTests extends TestCase {
    private CommandLineUtility util = null ;

    public void setUp() {
      try {
        util = new CommandLineUtility();
        util.addSwitch(new Switch("D", false, 0, -1, null));
        util.addSwitch(new Switch("F", true, 1, 2, null));
        util.addSwitch(new Switch("Roo", false, 3, -1, null));
        util.setMultipleValueSeparator(";");
        util.setSwitchDesignator("/");
      } catch (IllegalSwitchException e) {
        fail() ;
      }
    }

    /**
     * <p>Uses commas to delimit - should fail.
     *    Note: this is MultipleValueTests.testCorrectUsageAllArguments</p>
     */
    public void testCommaDelimiter() {
        String args[] = {"/D", "test", "/F=blah1,", "blah2", "/Roo", "yada1,", "yada2,yada3"} ;
        try {
            util.parse(args);
        } catch (ArgumentValidationException ave) {
            fail("Argument validation not enabled") ;
        } catch (UsageException ue) {
            // success - Roo doesn't have enough arguments
            return ;
        }
        fail("Incorrect usage - should have failed") ;
    }

    /**
     * <p>Uses semicolons and slashes to delimit - should succeed.
     *    Note: this is MultipleValueTests.testCorrectUsageAllArguments</p>
     */
    public void testSemicolonSlashDelimiter() {
        String args[] = {"/D", "test", "/F=blah1;", "blah2", "/Roo", "yada1;", "yada2;yada3"} ;
        try {
            util.parse(args);
        } catch (ArgumentValidationException ave) {
            fail("Argument validation not enabled") ;
        } catch (UsageException ue) {
            fail("Correct usage") ;
        }
        Collection switches = util.getValidSwitches() ;
        assertNotNull(switches) ;
        assertEquals(3, switches.size()) ;
    }

    /**
     * <p>Uses semicolons and dashes to delimit - should fail.
     *    Note: this is MultipleValueTests.testCorrectUsageAllArguments</p>
     */
    public void testSemicolonDashDelimiter() {
        String args[] = {"-D", "test", "-F=blah1;", "blah2", "-Roo", "yada1;", "yada2;yada3"} ;
        try {
            util.parse(args);
			fail(); // should throw usage exception
        } catch (ArgumentValidationException ave) {
            fail("Argument validation not enabled") ;
        } catch (UsageException ue) {
			// success
		}
    }
}