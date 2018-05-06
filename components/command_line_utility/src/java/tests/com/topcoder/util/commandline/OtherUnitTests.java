package com.topcoder.util.commandline;

import junit.framework.TestCase;
import com.topcoder.util.commandline.*;
import java.util.*;

/**
 * <p>Test specifics for valid parameters.</p>
 * @author TCSDEVELOPER
 * @version 1.0
 */

public class OtherUnitTests extends TestCase {
    private CommandLineUtility util = null ;
    private Switch sw_d;
    private Switch sw_f;
    private Switch sw_roo;

    public void setUp() {
        util = new CommandLineUtility();
        String d_howto = new String("U1");
        String f_howto = new String("U2");
        String roo_howto = new String("U3");
        
        try {    
            sw_d = new Switch("D", false, 0, -1, null, d_howto);
            util.addSwitch(sw_d);
            sw_f = new Switch("F", true, 1, 2, null, f_howto);
            util.addSwitch(sw_f);
            sw_roo = new Switch("Roo", false, 3, 3, null, roo_howto);
            util.addSwitch(sw_roo);
        } catch (IllegalSwitchException e) {
            fail() ;
        }
    }

    /**
     * <p>Usage string should display properly</p>
     */
    public void testUsageStrings() {
        String args[] = {"-D", "arg1","arg2","arg3","arg4","arg5","-F","arg1",
            "-Roo","arg1","arg2","arg3","param1"} ;
        try {
            util.parse(args);
        } catch (ArgumentValidationException ave) {
            fail("Should not throw an argument validation") ;
        } catch (UsageException ue) {
            fail("Correct usage");
        }
        assertTrue(util.getUsageString().startsWith("Usage:\n"));
        assertTrue(util.getUsageString().indexOf("\t-D\tU1\n") != -1);
        assertTrue(util.getUsageString().indexOf("\t-F\tU2\n") != -1);
        assertTrue(util.getUsageString().indexOf("\t-Roo\tU3\n") != -1);
    }
    
    /**
     * <p>getSwitches should find all switches</p>
     */
    public void testGetSwitches() {
        String args[] = {"-D", "arg1","arg2","arg3","arg4","arg5","-F","arg1",
            "-Roo","arg1","arg2","arg3","param1"} ;
        try {
            util.parse(args);
        } catch (ArgumentValidationException ave) {
            fail("Should not throw an argument validation") ;
        } catch (UsageException ue) {
            fail("Correct usage");
        }
        assertTrue(util.getSwitches().contains(sw_d));
        assertTrue(util.getSwitches().contains(sw_f));
        assertTrue(util.getSwitches().contains(sw_roo));
        
        assertTrue(util.getSwitch("D") == sw_d);
    }
}