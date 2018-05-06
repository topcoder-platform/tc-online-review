package com.topcoder.security.authenticationfactory.accuracytests;

import com.topcoder.security.authenticationfactory.AuthenticateException;
import com.topcoder.security.authenticationfactory.Principal;
import com.topcoder.security.authenticationfactory.Response;
import com.topcoder.security.authenticationfactory.http.basicimpl.HTTPBasicAuthenticator;
import com.topcoder.util.config.ConfigManager;
import junit.framework.TestCase;


/**
 * Accuracy tests for AbstractAuthenticator class. The test cases test the common functions that AbstractAuthenticator
 * provides.
 */
public class AbstractAuthenticatorAccuracyTest extends TestCase {
    /**
     * The namespace to load HTTPBasicAuthenticator.
     */
    private static final String NAMESPACE = "com.topcoder.security.http.HTTPBasicAuthenticator";

    /**
     * HTTPBasicAuthenticator is used to test the functionality of AbstractAuthenticator.
     */
    private HTTPBasicAuthenticator authenticator = null;
    private Principal p = null;

    /**
     * Setup for each test case.
     *
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();
        TestUtil.clearAllNamespace();

        TestUtil.loadConfigFile(TestUtil.ACCURACY_TEST_DIR + "testConfig.xml");
        authenticator = new HTTPBasicAuthenticator(NAMESPACE);

        // add the username and password, note that "UserName" and "Pwd" is the key defined in the property
        // "mappings" in the configuration namespace of DefaultKeyConverter
        // HTTPBasicAuthenticator will use DefaultKeyConverter to convert principal key. The converted
        // key will be used to get value fomr principal
        p = new Principal(new Object());
        p.addMapping("UserName", "user");
        p.addMapping("Pwd", "pass".toCharArray());
    }

    /**
     * Clean up for each test cases.
     */
    protected void tearDown() {
        TestUtil.clearAllNamespace();
    }

    /**
     * Test if the converter is loaded properly from the configuration file in normal case.
     *
     * @throws AuthenticateException to Junit.
     */
    public void testConverter() throws AuthenticateException {
        Response res = authenticator.authenticate(p);
        assertEquals(true, res.isSuccessful());
    }

    /**
     * Test if the converter is loaded properly from the configuration file with a configuration file that does not
     * contains 'principal_key_converter' property. The converter must be DefaultKeyConverter and the namespace to
     * load must be the same as the namespace passed in AbstractAuthenticator constructor.
     *
     * @throws Exception to Junit.
     */
    public void testConverterMissingConverterSetting()
        throws Exception {
        TestUtil.loadConfigFile(TestUtil.ACCURACY_TEST_DIR + "testConfig_missing_principal.xml");
        authenticator = new HTTPBasicAuthenticator(NAMESPACE);

        Response res = authenticator.authenticate(p);
        assertEquals("Not the expected authentication result.", true, res.isSuccessful());
    }

    /**
     * Test if cache is loaded properly from the configuration file in normal case.
     *
     * @throws Exception to Junit.
     */
    public void testCache() throws Exception {
        Response r = authenticator.authenticate(p);
        Response r1 = authenticator.authenticate(p);
        assertSame("The resposonse should be the same object since cache is enabled.", r, r1);
    }

    /**
     * Test if cache is loaded properly from the configuration file with a configuration file that does not contains
     * catch settings. The authenticator should be loaded normally with no cache.
     *
     * @throws Exception to Junit.
     */
    public void testCacheNoCache() throws Exception {
        TestUtil.loadConfigFile(TestUtil.ACCURACY_TEST_DIR + "testConfig_missing_cache.xml");
        authenticator = new HTTPBasicAuthenticator(NAMESPACE);

        Response r = authenticator.authenticate(p);
        Response r1 = authenticator.authenticate(p);
        assertFalse("The resposonse should not be the same object since cache is enabled.", r == r1);
    }
}
