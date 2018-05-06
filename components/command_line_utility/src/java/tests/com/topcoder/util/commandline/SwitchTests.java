package com.topcoder.util.commandline;

import junit.framework.TestCase;
import com.topcoder.util.commandline.*;
import java.util.*;

/**
 * <p>Tests to make sure switches work.</p>
 * @author TCSDEVELOPER
 * @version 1.0
 */

public class SwitchTests extends TestCase {
    private Switch sw_d;
    private Switch sw_f;
    private Switch sw_roo;

    public void setUp() {
        try {    
            sw_d = new Switch("D", false, 0, -1, null, "U1");
            sw_f = new Switch("F", true, 1, 2, null, "U2");
            sw_roo = new Switch("Roo", false, 3, 3, null, "U3");
        } catch (IllegalSwitchException e) {
            fail() ;
        }
    }
    
    /**
     * <p>Test min and max arguments.</p>
     */
    public void testMinMax() {
        assertTrue(sw_d.getMinimumArguments() == 0);
        assertTrue(sw_d.getMaximumArguments() == -1);
        
        assertTrue(sw_f.getMinimumArguments() == 1);
        assertTrue(sw_f.getMaximumArguments() == 2);
        
        assertTrue(sw_roo.getMinimumArguments() == 3);
        assertTrue(sw_roo.getMaximumArguments() == 3);
    }
    
    /**
     * <p>Test to make sure value function work for switches.</p>
     */
    public void testGetValues() {
        try {
            sw_d.setValue("ThisValue", 0, false);
            sw_f.setValue("ThatValue", 0, false);
        
            List theseValues = new ArrayList();
            theseValues.add("ThisValue");
            theseValues.add("ThatValue");
            sw_roo.setValues(theseValues, 0, false);
        } catch (ArgumentValidationException e) {
            fail();
        }
        assertTrue(sw_d.getValue().equals("ThisValue"));
        assertTrue(sw_f.getValue().equals("ThatValue"));
        assertTrue(sw_roo.getValue().equals("ThisValue"));
        assertTrue(sw_roo.getValues().contains("ThatValue"));
    }
    
    /**
     * <p>Test all naming functions.</p>
     */
    public void testNamingFunctions() {
        assertTrue(sw_d.getUsage() == "U1");
        assertTrue(sw_f.getUsage() == "U2");
        assertTrue(sw_roo.getUsage() == "U3");
        
        assertTrue(sw_d.getName().equals("D"));
        assertTrue(sw_f.getName().equals("F"));
        assertTrue(sw_roo.getName().equals("Roo"));
    }
}