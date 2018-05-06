package com.topcoder.util.commandline;

import junit.framework.TestCase;
import com.topcoder.util.commandline.*;
import java.util.*;

/**
 * <p>Test specifics for valid parameters.</p>
 * @author TCSDEVELOPER
 * @version 1.0
 */

public class SpecificUnitTest extends TestCase {
    private CommandLineUtility util = null ;
    private Switch sw_d;
    private Switch sw_f;
    private Switch sw_roo;

    public void setUp() {
        util = new CommandLineUtility();
            
        try {    
            sw_d = new Switch("D", false, 0, -1, null);
            util.addSwitch(sw_d);
            sw_f = new Switch("F", true, 1, 2, null);
            util.addSwitch(sw_f);
            sw_roo = new Switch("Roo", false, 3, 3, null);
            util.addSwitch(sw_roo);
        } catch (IllegalSwitchException e) {
            fail() ;
        }
    }

    /**
     * <p>Should find all arguments that were passed in.</p>
     */
    public void testReturnsNormal() {
        String args[] = {"-D", "arg1","arg2","arg3","arg4","arg5","-F","arg1",
            "-Roo","arg1","arg2","arg3","param1"} ;
        try {
            util.parse(args);
        } catch (ArgumentValidationException ave) {
            fail("Should not throw an argument validation") ;
        } catch (UsageException ue) {
            fail("Correct usage");
            return ;
        }
        List valid = util.getValidSwitches();
        assertTrue(valid.contains(sw_d));        
        assertTrue(valid.contains(sw_f));
        assertTrue(valid.contains(sw_roo));
        
        Switch sw_d_info = (Switch) valid.get(valid.indexOf(sw_d));
        Switch sw_f_info = (Switch) valid.get(valid.indexOf(sw_f));
        Switch sw_roo_info = (Switch) valid.get(valid.indexOf(sw_roo));
        assertTrue(sw_d_info.getValues().contains("arg1"));
        assertTrue(sw_d_info.getValues().contains("arg2"));
        assertTrue(sw_d_info.getValues().contains("arg3"));
        assertTrue(sw_d_info.getValues().contains("arg4"));
        assertTrue(sw_d_info.getValues().contains("arg5"));
        assertTrue(sw_f_info.getValues().contains("arg1"));
        assertTrue(sw_roo_info.getValues().contains("arg1"));
        assertTrue(sw_roo_info.getValues().contains("arg2"));
        assertTrue(sw_roo_info.getValues().contains("arg3"));
    
        List params = util.getParameters();
        assertTrue(params.contains("param1"));
    }
}