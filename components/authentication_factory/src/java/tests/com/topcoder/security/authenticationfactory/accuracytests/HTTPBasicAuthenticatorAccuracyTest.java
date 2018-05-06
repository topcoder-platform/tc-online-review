package com.topcoder.security.authenticationfactory.accuracytests;

import com.topcoder.security.authenticationfactory.AuthenticateException;
import com.topcoder.security.authenticationfactory.Principal;
import com.topcoder.security.authenticationfactory.Response;
import com.topcoder.security.authenticationfactory.http.basicimpl.HTTPBasicAuthenticator;
import junit.framework.TestCase;

import java.util.Map;


/**
 * Accuracy tests for HTTPBasicAuthenticator class.
 */
public class HTTPBasicAuthenticatorAccuracyTest extends TestCase {
    /**
     * The namespace to load HTTPBasicAuthenticator.
     */
    private static final String NAMESPACE = "com.topcoder.security.http.HTTPBasicAuthenticator";

    /**
     * HTTPBasicAuthenticator is used to test the functionality of AbstractAuthenticator.
     */
    private SubHTTPBasicAuthenticator authenticator = null;
    private Principal p = null;

    /**
     * Setup for each test case.
     *
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        TestUtil.loadConfigFile(TestUtil.ACCURACY_TEST_DIR + "testConfig.xml");
        authenticator = new SubHTTPBasicAuthenticator(NAMESPACE);

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
     *
     * @throws Exception to Junit.
     */
    protected void tearDown() throws Exception {
        TestUtil.clearAllNamespace();
    }

    /**
     * Test if the default mappings is loaded properly from the configuration file in normal case.
     */
    public void testLoadDefaultMapping() {
        Map defaultMappings = authenticator.getDefaultMappings();

        assertTrue("'protocol' key is not loaded", defaultMappings.containsKey("protocol"));
        assertTrue("'port' key is not loaded", defaultMappings.containsKey("port"));
        assertTrue("'host' key is not loaded", defaultMappings.containsKey("host"));
        assertTrue("'file' key is not loaded", defaultMappings.containsKey("file"));
        assertTrue("'request_properties' key is not loaded", defaultMappings.containsKey("request_properties"));

        assertEquals("'protocol' value is not correct", "http", defaultMappings.get("protocol"));
        assertEquals("'port' value is not correct", 8080, ((Integer) defaultMappings.get("port")).intValue());
        assertEquals("'host' value is not correct", "localhost", defaultMappings.get("host"));
        assertEquals("'file' value is not correct", "/webapp1/index.html", defaultMappings.get("file"));

        Map requestProperties = (Map) defaultMappings.get("request_properties");
        assertEquals("'request_properties' values is not correct", "text/html", requestProperties.get("accept"));
    }

    /**
     * Test authentication in normal case.
     *
     * @throws AuthenticateException
     */
    public void testDoAuthenticate() throws AuthenticateException {
        Response res = authenticator.authenticate(p);
        assertEquals(res.isSuccessful(), true);
    }

    /**
     * Test authentication with the principal object contains original keys. That mean the keys does not  need to be
     * converted.
     */
    public void testDoAuthenticateOriginalKey() {
        p = new Principal(new Object());
        p.addMapping(HTTPBasicAuthenticator.USER_NAME_KEY, "user");
        p.addMapping(HTTPBasicAuthenticator.PASSWORD_KEY, "pass".toCharArray());
    }

    /**
     * Test authentication with the converter set to convert 'port' to 'Port'.
     *
     * @throws Exception to Junit.
     */
    public void testDoAuthenticateConvertPort1() throws Exception {
        TestUtil.loadConfigFile(TestUtil.ACCURACY_TEST_DIR + "testConfig_convert_port.xml");
        authenticator = new SubHTTPBasicAuthenticator(NAMESPACE);

        Response r = authenticator.authenticate(p);
        assertEquals("Not the expected authentication result.", true, r.isSuccessful());
    }

    /**
     * Test authentication with the converter set to convert 'port' to 'Port'.
     *
     * @throws Exception to Junit.
     */
    public void testDoAuthenticateConvertPort2() throws Exception {
        TestUtil.loadConfigFile(TestUtil.ACCURACY_TEST_DIR + "testConfig_convert_port.xml");
        authenticator = new SubHTTPBasicAuthenticator(NAMESPACE);
        p.addMapping("Port", new Integer(8080));

        Response r = authenticator.authenticate(p);
        assertEquals("Not the expected authentication result.", true, r.isSuccessful());
    }
}
