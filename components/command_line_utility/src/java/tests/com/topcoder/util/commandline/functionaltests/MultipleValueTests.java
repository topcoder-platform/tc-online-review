package com.topcoder.util.commandline.functionaltests;

import junit.framework.TestCase;
import com.topcoder.util.commandline.* ;
import java.util.* ;

/**
 * <p>Test multiple value parameters.</p>
 * @author bokbok
 * @version 1.0
 */

public class MultipleValueTests extends TestCase {
    private CommandLineUtility util = null ;

    public void setUp() {
      try {
        util = new CommandLineUtility();
        util.addSwitch(new Switch("D", false, 0, -1, null));
        util.addSwitch(new Switch("F", true, 1, 2, null));
        util.addSwitch(new Switch("Roo", false, 3, -1, null));
      } catch (IllegalSwitchException e) {
        fail() ;
      }
    }

    /**
     * <p>Provides too few arguments - should fail.</p>
     */
    public void testInsufficentArguments() {
        String args[] = {"-F"} ;
        try {
            util.parse(args);
        } catch (ArgumentValidationException ave) {
            fail("Argument validation not enabled") ;
        } catch (UsageException ue) {
            // success (switch "F" requires one argument)
            return ;
        }
        fail("Incorrect usage - should have failed") ;
    }

    /**
     * <p>Provides too many arguments - should fail.</p>
     */
    public void testTooManyArguments() {
        String args[] = {"-F", "blah,blah,blah"} ;
        try {
            util.parse(args);
        } catch (ArgumentValidationException ave) {
            fail("Argument validation not enabled") ;
        } catch (UsageException ue) {
            // success (switch "F" requires two or less arguments)
            return ;
        }
        fail("Incorrect usage - should have failed") ;
    }

    /**
     * <p>Provides too many arguments across two params - should fail.</p>
     */
    public void testTooManyArgumentsAgain() {
        String args[] = {"-F", "blah,", "blah,blah"} ;
        try {
            util.parse(args);
        } catch (ArgumentValidationException ave) {
            fail("Argument validation not enabled") ;
        } catch (UsageException ue) {
            // success (switch "F" requires two or less arguments)
            return ;
        }
        fail("Incorrect usage - should have failed") ;
    }

    /**
     * <p>Provides correct arguments - should succeed.</p>
     */
    public void testCorrectUsageWithSpace() {
        String args[] = {"-F", "blah1,", "blah2"} ;
        try {
            util.parse(args);
        } catch (ArgumentValidationException ave) {
            fail("Argument validation not enabled") ;
        } catch (UsageException ue) {
            fail("Correct usage") ;
        }
        Collection switches = util.getValidSwitches() ;
        assertNotNull(switches) ;
        assertEquals(1, switches.size()) ;
        for (Iterator it = switches.iterator() ; it.hasNext() ;) {
            Switch s = (Switch) it.next() ;
            assertEquals("F", s.getName()) ;
            assertNotNull(s.getValues()) ;

            int i = 1 ;
            for (Iterator lit = s.getValues().iterator() ; lit.hasNext() ; ) {
                String val = (String)lit.next() ;
                String testVal = "blah" + i ;
                i++ ;
                assertEquals(testVal, val) ;
            }
        }
    }

    /**
     * <p>Provides correct arguments with equals - should succeed.</p>
     */
    public void testCorrectUsageWithEquals() {
        String args[] = {"-F=blah1,blah2"} ;
        try {
            util.parse(args);
        } catch (ArgumentValidationException ave) {
            fail("Argument validation not enabled") ;
        } catch (UsageException ue) {
            fail("Correct usage") ;
        }
        Collection switches = util.getValidSwitches() ;
        assertNotNull(switches) ;
        assertEquals(1, switches.size()) ;
        for (Iterator it = switches.iterator() ; it.hasNext() ;) {
            Switch s = (Switch) it.next() ;
            assertEquals("F", s.getName()) ;
            assertNotNull(s.getValues()) ;

            int i = 1 ;
            for (Iterator lit = s.getValues().iterator() ; lit.hasNext() ; ) {
                String val = (String)lit.next() ;
                String testVal = "blah" + i ;
                i++ ;
                assertEquals(testVal, val) ;
            }
        }
    }

    /**
     * <p>Provides correct arguments with equals and space - should succeed.</p>
     */
    public void testCorrectUsageWithSpaceAndEquals() {
        String args[] = {"-F=blah1,", "blah2"} ;
        try {
            util.parse(args);
        } catch (ArgumentValidationException ave) {
            fail("Argument validation not enabled") ;
        } catch (UsageException ue) {
            fail("Correct usage") ;
        }
        Collection switches = util.getValidSwitches() ;
        assertNotNull(switches) ;
        assertEquals(1, switches.size()) ;
        for (Iterator it = switches.iterator() ; it.hasNext() ;) {
            Switch s = (Switch) it.next() ;
            assertEquals("F", s.getName()) ;
            assertNotNull(s.getValues()) ;

            int i = 1 ;
            for (Iterator lit = s.getValues().iterator() ; lit.hasNext() ; ) {
                String val = (String)lit.next() ;
                String testVal = "blah" + i ;
                i++ ;
                assertEquals(testVal, val) ;
            }
        }
    }

    /**
     * <p>Tests all arguments - should succeed.</p>
     */
    public void testCorrectUsageAllArguments() {
        String args[] = {"-D", "test", "-F=blah1,", "blah2", "-Roo", "yada1,", "yada2,yada3"} ;
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
        for (Iterator it = switches.iterator() ; it.hasNext() ;) {
            Switch s = (Switch) it.next() ;
            if ("F".equals(s.getName())) {
                assertNotNull(s.getValues()) ;

                int i = 1 ;
                for (Iterator lit = s.getValues().iterator() ; lit.hasNext() ; ) {
                    String val = (String)lit.next() ;
                    String testVal = "blah" + i ;
                    i++ ;
                    assertEquals(testVal, val) ;
                }
            }
            if ("Roo".equals(s.getName())) {
                assertNotNull(s.getValues()) ;

                int i = 1 ;
                for (Iterator lit = s.getValues().iterator() ; lit.hasNext() ; ) {
                    String val = (String)lit.next() ;
                    String testVal = "yada" + i ;
                    i++ ;
                    assertEquals(testVal, val) ;
                }
            }
            if ("D".equals(s.getName())) {
                assertNotNull(s.getValue()) ;
                assertEquals(s.getValue(), "test") ;
            }
        }
    }

}