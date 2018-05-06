package com.topcoder.util.commandline.accuracytests;

import com.topcoder.util.commandline.*;

import junit.framework.TestCase;

/**
 * Tests commandline switches for CommandLineUtility
 * 
 * @author farlox
 */
public class ParseNoArgsTest extends TestCase {


    private CommandLineUtility clu;
    
    /**
     * Creates a CommandLineUtility and sets up all switches required.
     * 
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        clu = new CommandLineUtility();

        //not required, no arguments
        clu.addSwitch( new Switch("not", false, 0, 0, null) );

        //required, no values
        clu.addSwitch( new Switch("req",  true, 0, 0, null) );
        clu.addSwitch( new Switch("req2", true, 0, 0, null) );
    }

    /**
     * Tests only required switches
     */
    public void testMin() {
        try {
            clu.parse( new String[] { "-req2", "-req" } );
        } catch (ArgumentValidationException e) {
            fail( e.getMessage() );
        } catch (UsageException e) {
            fail( "Failed with required switches" );
        }
        assertEquals( 2, clu.getValidSwitches().size() );
        assertEquals( 0, clu.getParameters().size() );
    }

    /**
     * Tests both required arguments starting with a parameter
     */
    public void testParamAfter() {
        try {
            clu.parse( new String[] { "-req", "-req2", "parameter"} );
        } catch (ArgumentValidationException e) {
            fail( e.getMessage() );
        } catch (UsageException e) {
            fail( "Failed with parameter after switches" );
        }
        assertEquals( 2, clu.getValidSwitches().size() );
        assertEquals( 1, clu.getParameters().size() );
    }

    /**
     * Tests extra switches
     */
    public void testExtra() {
        try {
            clu.parse( new String[] { "-req", "-req2", "-not" } );
        } catch (ArgumentValidationException e) {
            fail( e.getMessage() );
        } catch (UsageException e) {
            fail( "Failed with extra switches" );
        }
        assertEquals( 3, clu.getValidSwitches().size() );
        assertEquals( 0, clu.getParameters().size() );
    }

    /**
     * Tests extra switches with parameter after switches
     */
    public void testExtraParamAfter() {
        try {
            clu.parse( new String[] { "-req", "-req2", "-not", "param" } );
        } catch (ArgumentValidationException e) {
            fail( e.getMessage() );
        } catch (UsageException e) {
            fail( "Failed with extra switches, param after" );
        }
        assertEquals( 3, clu.getValidSwitches().size() );
        assertEquals( 1, clu.getParameters().size() );
    }

    /**
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

}
