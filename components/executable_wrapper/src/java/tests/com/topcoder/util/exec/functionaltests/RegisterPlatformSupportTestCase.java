package com.topcoder.util.exec.functionaltests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import com.topcoder.util.exec.*;

/**
 * <p>This test case tests registering new platform support.</p>
 *
 * @author srowen
 * @version 1.0
 */
public class RegisterPlatformSupportTestCase extends TestCase {

    public void testRegisterPlatformSupport() {
        final String osName = "Windows 2010"; //System.getProperty("os.name");
        PlatformSupport.registerPlatformSupport(osName, new FooOSPlatformSupport());
    }

    public void testIllegalArguments() {
        try {
            PlatformSupport.registerPlatformSupport(null, new FooOSPlatformSupport());
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // good
        }
        try {
            PlatformSupport.registerPlatformSupport("fooOS", null);
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // good
        }
    }

    public static Test suite() {
        return new TestSuite(RegisterPlatformSupportTestCase.class);
    }

    public static class FooOSPlatformSupport extends PlatformSupport {
        protected String[] makeShellCommand(final String[] command) {
            return null;
        }
    }
}
