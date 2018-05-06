package com.topcoder.util.commandline.accuracytests;

import com.topcoder.util.commandline.*;

import junit.framework.TestCase;

/**
 * Tests commandline switches with multiple values for CommandLineUtility
 * 
 * @author farlox
 */
public class MultipleArgsTest extends TestCase {


    private CommandLineUtility clu;
    
    /**
     * Creates a CommandLineUtility and sets up the switch.
     * 
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();

        //CommandLine with syntax /param=value1:value2:...
        clu = new CommandLineUtility();
        clu.setSwitchDesignator( "/" );
        clu.setMultipleValueSeparator( ":" );

        //not required, no arguments
        clu.addSwitch( new Switch("mult", false, 1, 3, null) );
    }

    /**
     * Tests only required switches
     */
    public void testMin() {
        try {
            clu.parse( new String[] { "/mult=one" } );
        } catch (ArgumentValidationException e) {
            fail( e.getMessage() );
        } catch (UsageException e) {
            fail( "Failed with one value" );
        }
        assertEquals( 1, clu.getValidSwitches().size() );
        Switch s = (Switch) clu.getValidSwitches().get(0);
        assertEquals( 1, s.getValues().size() );
    }

    /**
     * Tests two values for switch
     */
    public void testTwoVals() {
        try {
            clu.parse( new String[] { "/mult=one:two" } );
        } catch (ArgumentValidationException e) {
            fail( e.getMessage() );
        } catch (UsageException e) {
            fail( "Failed with parameter before switches" );
        }
        assertEquals( 1, clu.getValidSwitches().size() );
        Switch s = (Switch) clu.getValidSwitches().get(0);
        assertEquals( 2, s.getValues().size() );
    }

    /**
     * Tests three values for switch
     */
    public void testThreeVals() {
        try {
            clu.parse( new String[] { "/mult=one:two:threeve" } );
        } catch (ArgumentValidationException e) {
            fail( e.getMessage() );
        } catch (UsageException e) {
            fail( "Failed with parameter after switches" );
        }
        assertEquals( 1, clu.getValidSwitches().size() );
        Switch s = (Switch) clu.getValidSwitches().get(0);
        assertEquals( 3, s.getValues().size() );
    }

    /**
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

}
