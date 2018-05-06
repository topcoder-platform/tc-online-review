package com.topcoder.util.commandline;

import junit.framework.TestCase;
import com.topcoder.util.commandline.*;
import java.util.*;

/**
 * <p>Test specifics for valid parameters.</p>
 * @author TCSDEVELOPER
 * @version 1.0
 */

public class SpecificUnitTestIgnore extends TestCase {
    private CommandLineUtility util = null ;
    private Switch sw_d;
    private Switch sw_f;
    private Switch sw_roo;

    public void setUp() {
        util = new CommandLineUtility(true, 1, -1);
        
        String[] args = {"arg1","arg2","arg3","arg4","arg5"};
        EnumValidator argsValidator = new EnumValidator(Arrays.asList(args)) ;
        
        try {    
            sw_d = new Switch("D", false, 0, -1, argsValidator);
            util.addSwitch(sw_d);
            sw_f = new Switch("F", true, 1, 2, argsValidator);
            util.addSwitch(sw_f);
            sw_roo = new Switch("Roo", false, 3, -1, argsValidator);
            util.addSwitch(sw_roo);
        } catch (IllegalSwitchException e) {
            fail() ;
        }
    }

    /**
     * <p>Should find all arguments that were passed in.</p>
     */
    public void testReturnsIgnore() {
        String args[] = {"-D", "badarg1", "badarg2", "badarg3", "badarg4",
            "badarg5", "-F", "badarg1", "-Roo", "badarg1", "badarg2","badarg3", 
            "param1"} ;
        try {
            util.parse(args);
        } catch (ArgumentValidationException ave) {
            fail("Should not throw an argument validation") ;
        } catch (UsageException ue) {
            fail("Correct usage");
        }
        List invalid = util.getInvalidSwitches();
        assertTrue(invalid.contains(sw_d));        
        assertTrue(invalid.contains(sw_f));
        assertTrue(invalid.contains(sw_roo));
        
        Switch sw_d_info = (Switch) invalid.get(invalid.indexOf(sw_d));
        Switch sw_f_info = (Switch) invalid.get(invalid.indexOf(sw_f));
        Switch sw_roo_info = (Switch) invalid.get(invalid.indexOf(sw_roo));
        assertTrue(sw_d_info.getValues().contains("badarg1"));
        assertTrue(sw_d_info.getValues().contains("badarg2"));
        assertTrue(sw_d_info.getValues().contains("badarg3"));
        assertTrue(sw_d_info.getValues().contains("badarg4"));
        assertTrue(sw_d_info.getValues().contains("badarg5"));
        assertTrue(sw_f_info.getValues().contains("badarg1"));
        assertTrue(sw_roo_info.getValues().contains("badarg1"));
        assertTrue(sw_roo_info.getValues().contains("badarg2"));
        assertTrue(sw_roo_info.getValues().contains("badarg3"));
    
        List params = util.getParameters();
        assertTrue(params.contains("param1"));
    }
}