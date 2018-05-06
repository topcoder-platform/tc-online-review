/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.security.authenticationfactory;

import java.io.IOException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.security.authenticationfactory.http.HttpResource;
import com.topcoder.security.authenticationfactory.http.basicimpl.HTTPBasicAuthenticator;

/**
 * <p>
 * This class contains the Demo tests for this component.</p>
 *
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class DemoTest extends TestCase {
    /**
     *<p>
     * Creates a test suite of the tests contained in this class.</p>
     *
     * @return a test suite of the tests contained in this class.
     */
    public static Test suite() {
        return new TestSuite(DemoTest.class);
    } // end suite()

    /**
     * Clean up for each test cases.
     */
    protected void tearDown() {
        ConfigHelper.clearAllNamespace();
    }

    /**
     * Do the demo test.
     * @throws AuthenticateException to JUnit
     * @throws IOException to JUnit
     * @throws ConfigurationException to JUnit
     */
    public void testDemo() throws AuthenticateException, IOException, ConfigurationException {

        // load the cofig file
        ConfigHelper.cleanAndLoadConfig("demo.xml");

        // get the singleton instance and reload config file
        AuthenticationFactory factory = AuthenticationFactory.getInstance();
        factory.refresh();

        // get the authenticator defined in the configuration file
        // the HttpBasicAuthenticator will be returned as defined in the
        // example configuration file above.
        Authenticator auth = factory.getAuthenticator("http");

        // create a Principal to authenticate, use the username as the id.
        Principal principal = new Principal("user1");
        principal.addMapping(HTTPBasicAuthenticator.HOST_KEY, "localhost");
        principal.addMapping(HTTPBasicAuthenticator.FILE_KEY, "/webapp1/index.html");
        principal.addMapping(HTTPBasicAuthenticator.PORT_KEY, new Integer(ConfigHelper.getPort()));
        principal.addMapping("UserName", "user1");
        principal.addMapping("Pwd", "pass1".toCharArray());

        // authenticate the principal
        Response response = auth.authenticate(principal);

        // check the authentication fails or not
        if (response.isSuccessful()) {
            System.out.println("authentication succeeds");
        }

        // get the authentication message if there is one
        System.out.println(response.getMessage());

        // get the response details, we should know in advance it is an
        // http authentication implementation
        HttpResource resource = (HttpResource) response.getDetails();

        // print the properties of HttpResource
        System.out.println(resource.getOriginalURL());
        System.out.println(resource.getActualURL());
        System.out.println(resource.getContentType());
        System.out.println(resource.getContent());
        System.out.println(resource.getSetCookie());

        // authenticate it again
        Response r2 = auth.authenticate(principal);
        assertSame("because of cache, these two should be same", r2, response);

        // test with a non-exist user
        Principal p = new Principal("other");
        p.addMapping(HTTPBasicAuthenticator.HOST_KEY, "localhost");
        p.addMapping(HTTPBasicAuthenticator.FILE_KEY, "/webapp1/index.html");
        p.addMapping(HTTPBasicAuthenticator.PORT_KEY, new Integer(ConfigHelper.getPort()));
        p.addMapping("UserName", "Nouser");
        p.addMapping("Pwd", "nopass1".toCharArray());

        // authenticate the principal
        response = auth.authenticate(p);

        // check the authentication fails or not
        if (!response.isSuccessful()) {
            System.out.println("authentication failed");
        }

        // get the authentication message if there is one
        System.out.println(response.getMessage());

        // get the response details, we should know in advance it is an
        // http authentication implementation
        resource = (HttpResource) response.getDetails();
        // print the properties of HttpResource
        System.out.println(resource.getOriginalURL());
        System.out.println(resource.getActualURL());
        System.out.println(resource.getContentType());
        System.out.println(resource.getSetCookie());

        // to turn off the caching functionality
        ConfigHelper.cleanAndLoadConfig("offcache.xml");
        factory.refresh();

        auth = factory.getAuthenticator("http");
        response = auth.authenticate(principal);

        // reauthenticate it again, will get another response
        r2 = auth.authenticate(principal);
        assertNotSame("had turn off cache, these two should not be same", r2, p);
    }
} // end AbstractAuthenticatorTest