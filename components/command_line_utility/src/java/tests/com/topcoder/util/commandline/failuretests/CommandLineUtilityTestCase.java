package com.topcoder.util.commandline.failuretests;

import com.topcoder.util.commandline.* ;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>This class test the correct behavior when
 * providing invalid input to the <code>EnumValidator
 * </code>
 *
 * @author Tomson
 * @version 1.0
 */
public class CommandLineUtilityTestCase extends TestCase {
    private CommandLineUtility util;
    /**
     * <p>test the parse(X) method when the given argument
     * is invalid</p>
     */
    /** test Switch not found */
    public void testParse1() {
        util = new CommandLineUtility();
        String arg[] = {"-D" , "a"};
        try {
            util.parse(arg);
            fail("should throw an exception");
        } catch (UsageException e) {
            // correct
        } catch(Exception e) {
            fail("should throw UsageException");
        }
    }
    public void testParse2() {
        util = new CommandLineUtility(true , 2 , 3);
        assertNotNull(
                "construct failed",
                util);
        String arg[] = {"-D" , "a"};
        try {
            util.parse(arg);
        } catch(Exception e) {
            fail("should not throw exception");
        }
    }
    public void testParse31() {
        util = new CommandLineUtility(false , 0 , -1);
        String arg[] = { "a" };
        try {
            util.parse(arg);
        } catch (Exception e) {
            fail("should not throw exception");
        }
    }
    /** test too many / few  parameters */
    public void testParse3() {
        String arg[] = { "a" };
        util = new CommandLineUtility(false , 2 , 3);
        try {
            util.parse(arg);
            fail("should throw an exception");
        } catch(UsageException e) {
            // correct
        } catch(Exception e) {
            fail("should throw an UsageException");
        }
        arg = new String[] {"a" , "b" , "c" , "d" };
        try {
            util.parse(arg);
            fail("should throw an exception");
        } catch(UsageException e) {
            // correct
        } catch(Exception e) {
            fail("should throw an UsageException");
        }
    }
    /** test too many / few values of a switch */
    public void testParse4() throws IllegalSwitchException {
        util = new CommandLineUtility(false , 0 , -1);
        util.addSwitch(new Switch("D", false, 1 , 1, null));

        String arg[] = {"-Da"};
        try {
            util.parse(arg);
        } catch(Exception e) {
            fail("should not throw exception");
        }
        arg = new String[] {"-Da" , ",b" };
        try {
            util.parse(arg);
            fail("should throw an exception");
        } catch(UsageException e) {
            // correct
        } catch(Exception e) {
            fail("should throw an UsageException");
        }
        arg = new String[] {"-Da" , "," , "b" };
        try {
            util.parse(arg);
            fail("should throw an exception");
        } catch(UsageException e) {
            // correct
        } catch(Exception e) {
            fail("should throw an UsageException");
        }
        arg = new String[] {"-D"};
        try {
            util.parse(arg);
            fail("should throw an exception");
        } catch(UsageException e) {
            // correct
        } catch(Exception e) {
            fail("should throw an UsageException");
        }
    }
    /** test switches must precede all parameters */
    public void testParse5() throws IllegalSwitchException {
        util = new CommandLineUtility(false , 1 , 1);
        util.addSwitch(new Switch("D", false, 1 , 1, null));

        String arg[] = {"-D" , "-54" , "a"};
        try {
            util.parse(arg);
        } catch(Exception e) {
            fail("should not throw exception");
        }
        arg = new String[] {"a" , "-D" , "dir"};
        try {
            util.parse(arg);
            fail("should throw an exception");
        } catch(UsageException e) {
            // correct
        } catch(Exception e) {
            fail("should throw an UsageException");
        }
    }
    /** Switches may not be used more than once in the same command line */
    public void testParse6() throws IllegalSwitchException {
        util = new CommandLineUtility(false , 0 , -1);
        util.addSwitch(new Switch("D", false, 0 , -1, null));

        String arg[] = {"-D" , "dir1" , "-D" , "dir2"};
        try {
            util.parse(arg);
            fail("should throw an exception");
        } catch(UsageException e) {
            // correct
        } catch(Exception e) {
            fail("should throw an UsageException");
        }
    }

    /** the -Ddir switch could be interpreted as D=dir, Dd=ir,
     *  and Ddi=r. If more than one of these values is a valid
     *  switch, then an error will be thrown.
     */
    public void testParse7() throws IllegalSwitchException {
        util = new CommandLineUtility(false , 0 , -1);
        util.addSwitch(new Switch("D", false, 0 , -1, null));

        String arg[] = {"-Ddir"};
        try {
            util.parse(arg);
        } catch(Exception e) {
            fail("should not throw exception");
        }
        util.addSwitch(new Switch("Dd" , false , 0 , -1 , null));
        try {
            util.parse(arg);
            fail("should throw an exception");
        } catch(UsageException e) {
            // correct
        } catch(Exception e) {
            fail("should throw an UsageException");
        }
    }

    /** normal test */
    public void testParse8() throws IllegalSwitchException {
        util = new CommandLineUtility(false , 0 , 2);
        util.addSwitch(new Switch("D", false, 2 , 2, null));
        String arg[] = {"-Da" , "," , "b"};
        try {
            util.parse(arg);
        } catch(Exception e) {
            fail("should not throw exception");
        }
        arg = new String[] {"-D,,", ","};
        try {
            util.parse(arg);
        } catch(Exception e) {
            fail("should not throw exception");
        }
        arg = new String[] {"-Da," , "b" , "," , ","};
        try {
            util.parse(arg);
        } catch(Exception e) {
            fail("should not throw exception");
        }
    }
    /** test throw ArgumentValidationException */
    public void testParse9() throws IllegalSwitchException {
        util = new CommandLineUtility(false , 0 , 2);
        util.addSwitch(
                new Switch(
                        "D",
                        false,
                        2 ,
                        2,
                        new ExceptionValidator()));
        String[] arg = new String[]{"-Da" , "b" };
        try {
            util.parse(arg);
            fail("should throw an exception");
        } catch(ArgumentValidationException e) {
            // correct
        } catch(Exception e) {
            fail("should throw an UsageException");
        }
    }
    private class ExceptionValidator implements ArgumentValidator {
        public void validate(String s)
                throws ArgumentValidationException {
            throw new ArgumentValidationException(s,"PIG");
        }
    }
    public static Test suite() {
        return new TestSuite(CommandLineUtilityTestCase.class);
    }
}
