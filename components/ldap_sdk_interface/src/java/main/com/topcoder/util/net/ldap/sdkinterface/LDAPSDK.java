/**
 * TCS LDAP SDK Interface 1.0
 *
 * LDAPSDK.java
 *
 * Copyright &copy; TopCoder Inc, 2004. All rights reserved.
 */
package com.topcoder.util.net.ldap.sdkinterface;

/**
 * As the <em>Creator</em> in the Factory Method Pattern, this class creates a concrete product (for example a
 * NetscapeFactory) and stores the instance in a private member variable. Note that because this class implements
 * LDAPSDKFactory, it provides a set of common methods with the instantiated factory (methods createConnection () and
 * createSSLConnection ())</p>
 * <p>As a <em>Concrete Creator</em> in the Factory Method Pattern, this class provides concrete products from the
 * factory chosen.&nbsp;</p>
 * <p>This design makes the use of these classes quite easy, saving the user to deal with intermediate factory classes.
 * &nbsp;&nbsp;</p>
 * <p>This piece of code demonstrates how to work with it:</p>
 * <p></p>
 * <p>LDAPSDK ldap=new LDAPSDK (&quot;com.topcoder.util.net.ldap.sdkinterface.netscape.NetscapeFactory&quot;); <br/>
 * LDAPSDKConnection con=ldap.createConnection ();
 *
 * @author  cucu
 * @author  isv
 * @version 1.0 05/18/2004
 */
public class LDAPSDK implements LDAPSDKFactory {

    /**
     * A factory producing the connections to LDAP servers.
     */
    private LDAPSDKFactory lDAPSDKFactory = null;

    /**
     * Create an LDAPSDK of the specified plugin for asking him connections. </p>
     * <p>This constructor should create an instance of the specified class, probably using reflection, and store it on
     * the lDAPSDKFactory member variable. If the class has not been found, a ClassNotFoundException should be raised.
     *
     * @param  className The name of the plugin class
     * @throws ClassNotFoundException if specified class can not be located.
     * @throws ClassCastException if specified class does not implement the <code>LDAPSDKFactory</code> interface.
     * @throws NullPointerException if specified <code>String</code> is <code>null</code>.
     * @throws IllegalArgumentException if specified <code>String</code> is empty or instance of specified class can not
     *         be successfully created.
     */
    public LDAPSDK(String className) throws ClassNotFoundException {
        if (className == null) {
            throw new NullPointerException("Null class names are prohibited");
        }

        if (className.trim().length() == 0) {
            throw new IllegalArgumentException("Empty class names are prohibited");
        }

        try {
            Class clazz = Class.forName(className);
            lDAPSDKFactory = (LDAPSDKFactory) clazz.newInstance();
        } catch(IllegalAccessException e ) {
            throw new IllegalArgumentException("The SDK instance can not be created : " + e);
        } catch(InstantiationException e ) {
            throw new IllegalArgumentException("The SDK instance can not be created : " + e);
        }
    }

    /**
     * Create a connection using the plugin specified in the constructor. The constructor must have built a
     * LDAPSDKFactory of the specified plugin and stored on the lDAPSDKFactory member variable, so this method should
     * ask that object a connection and return it.
     *
     * @return a connection to the plugin specified in the constructor
     */
    public LDAPSDKConnection createConnection() {
        return lDAPSDKFactory.createConnection();
    }

    /**
     * Create a connection using the plugin specified in the constructor. The constructor must have built a
     * LDAPSDKFactory of the specified plugin and stored on the lDAPSDKFactory member variable, so this method should
     * ask that object a SSL connection and return it.
     *
     * @return a SSL connection to the plugin specified in the constructor
     */
    public LDAPSDKConnection createSSLConnection() {
        return lDAPSDKFactory.createSSLConnection();
    }
}
