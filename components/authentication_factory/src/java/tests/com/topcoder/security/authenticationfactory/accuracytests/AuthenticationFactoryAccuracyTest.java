package com.topcoder.security.authenticationfactory.accuracytests;

import com.topcoder.security.authenticationfactory.AuthenticationFactory;
import com.topcoder.security.authenticationfactory.Authenticator;
import com.topcoder.security.authenticationfactory.http.basicimpl.HTTPBasicAuthenticator;
import junit.framework.TestCase;


/**
 * Accuracy test for AuthenticationFactory class.
 */
public class AuthenticationFactoryAccuracyTest extends TestCase {
    /**
     * HTTPBasicAuthenticator is used to test the functionality of AbstractAuthenticator.
     */
    private AuthenticationFactory factory = null;

    /**
     * Setup for each test case.
     *
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        TestUtil.loadConfigFile(TestUtil.ACCURACY_TEST_DIR + "testConfig.xml");
        factory = AuthenticationFactory.getInstance();
        factory.refresh();
    }

    /**
     * Clean up for each test cases.
     *
     * @throws Exception to Junit.
     */
    protected void tearDown() throws Exception {
        TestUtil.clearAllNamespace();
    }

    /**
     * Test to make sure getInstance() does not reload the configuration file.
     *
     * @throws Exception to Junit.
     */
    public void testGetInstance() throws Exception {
        TestUtil.loadConfigFile(TestUtil.ACCURACY_TEST_DIR + "testConfig1.xml");
        factory = AuthenticationFactory.getInstance();

        Authenticator auth = factory.getAuthenticator("httpTest");
        assertNotNull("The configuration should not be refreshed.", auth);
    }

    /**
     * Test refresh method, make sure it refresh the configuration.
     *
     * @throws Exception to Junit.
     */
    public void testRefresh() throws Exception {
        TestUtil.loadConfigFile(TestUtil.ACCURACY_TEST_DIR + "testConfig1.xml");
        factory.refresh();

        Authenticator auth = factory.getAuthenticator("httpTest");
        assertNull("The configuration is not refreshed.", auth);
    }

    /**
     * Test get authenticator using the factory.
     */
    public void testGetAuthenticator() {
        Authenticator auth = factory.getAuthenticator("http");
        assertTrue("Not the expected authenticator.", auth instanceof HTTPBasicAuthenticator);

        auth = factory.getAuthenticator("httpTest");
        assertTrue("Not the expected authenticator.", auth instanceof SubHTTPBasicAuthenticator);
    }
}
