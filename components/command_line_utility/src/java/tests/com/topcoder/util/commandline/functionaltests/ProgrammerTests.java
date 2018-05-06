package com.topcoder.util.commandline.functionaltests;

import junit.framework.TestCase;
import com.topcoder.util.commandline.* ;
import java.util.* ;

/**
 * <p></p>
 * @author bokbok
 * @version 1.0
 */

public class ProgrammerTests extends TestCase {
    private CommandLineUtility util = null ;

    public void testDuplicateSwitches() {
      try {
        util = new CommandLineUtility(false, 2, 2);
        util.addSwitch(new Switch("D", false, 1, 1, null));
        util.addSwitch(new Switch("Roo", true, 1, 1, null));
        util.addSwitch(new Switch("Roo", false, 1, 1, null));
       // should bomb
        fail() ;
      } catch (IllegalSwitchException e) {
        // success
      }
    }

    public void testInvalidMinMax() {
      try {
        util = new CommandLineUtility(false, 2, 2);
        util.addSwitch(new Switch("D", false, 4, 0, null));
        util.addSwitch(new Switch("Roo", true, -1, -1, null));
        util.addSwitch(new Switch("Boo", false, 3, 1, null));
       // should bomb
        fail() ;
      } catch (IllegalSwitchException e) {
        // success
      }
    }

    public void testInvalidCLUMinMax() {
      try {
        util = new CommandLineUtility(false, 3, 1);
        util.addSwitch(new Switch("D", false, 0, 0, null));
        util.addSwitch(new Switch("Roo", true, 0, 0, null));
        util.addSwitch(new Switch("Boo", false, 0, 0, null));
       // should bomb
        fail() ;
      } catch (IllegalSwitchException e) {
        // success
      }
    }

    public void testInvalidSwitchNameSingle() {
      try {
        util = new CommandLineUtility(false, 2, 2);
        util.setSwitchDesignator("/");
        util.addSwitch(new Switch("D/", false, 0, 0, null));
        util.addSwitch(new Switch("Roo", true, 0, 0, null));
        util.addSwitch(new Switch("Boo", false, 0, 0, null));
       // should bomb
        fail() ;
      } catch (IllegalSwitchException e) {
        // success
      }
    }

    public void testInvalidSwitchNameMultiple() {
      try {
        util = new CommandLineUtility(false, 2, 2);
        util.setMultipleValueSeparator(";");
        util.addSwitch(new Switch("D;", false, 0, 0, null));
        util.addSwitch(new Switch("Roo", true, 0, 0, null));
        util.addSwitch(new Switch("Boo", false, 0, 0, null));
       // should bomb
        fail() ;
      } catch (IllegalSwitchException e) {
        // success
      }
    }

    public void testNullSwitch() {
      try {
        util = new CommandLineUtility(false, 2, 2);
        util.addSwitch(null);
       // should bomb
        fail() ;
      } catch (IllegalSwitchException e) {
        // success
      }
    }

    public void testNullSingleDelimiter() {
      try {
        util = new CommandLineUtility(false, 2, 2);
        util.setSwitchDesignator(null);
       // should bomb
        fail() ;
      } catch (NullPointerException e) {
        // success
      }
    }

    public void testNullMultipleDelimiter() {
      try {
        util = new CommandLineUtility(false, 2, 2);
        util.setMultipleValueSeparator(null);
       // should bomb
        fail() ;
      } catch (NullPointerException e) {
        // success
      }
    }

}