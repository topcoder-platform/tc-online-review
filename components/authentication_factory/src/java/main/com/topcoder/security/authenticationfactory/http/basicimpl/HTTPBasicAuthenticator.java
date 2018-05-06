/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.security.authenticationfactory.http.basicimpl;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.topcoder.security.authenticationfactory.AbstractAuthenticator;
import com.topcoder.security.authenticationfactory.AuthenticateException;
import com.topcoder.security.authenticationfactory.ConfigurationException;
import com.topcoder.security.authenticationfactory.InvalidPrincipalException;
import com.topcoder.security.authenticationfactory.MissingPrincipalKeyException;
import com.topcoder.security.authenticationfactory.Principal;
import com.topcoder.security.authenticationfactory.Response;
import com.topcoder.security.authenticationfactory.http.HttpResource;
import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.config.Property;
import com.topcoder.util.config.UnknownNamespaceException;

/**
 * <p>
 * This class performs HTTP basic authentication. As noted, the HTTP server might ask for, in its
 * response to the client's request, for the client to provide credentials. The client would send
 * another request with the desired information, and the server would then verify the information.
 * This class defines several keys that are used during authentication, and are stored in the map.
 * All keys are Strings,  and all Values are defined to be of specific types. All keynames are
 * case-sensitive. Certain keys are immutable, in  as much as they can't be changed, and no values
 * can be used in their stead. The following details this information:
 * </p>
 *
 * <p>
 * Key: &quot;username&quot;, Type: java.lang.String, Initial Value: null, Immutable: No, Notes:
 * Provided by client.
 * </p>
 *
 * <p>
 * Key: &quot;password&quot;, Type: [C {this is the class name of a char array}, Initial Value:
 * null, Immutable: No, Notes: Provided by client
 * </p>
 *
 * <p>
 * Key: &quot;protocol&quot;, Type: java.lang.String, Initial Value: &quot;http&quot;, Immutable:
 * Yes, Notes: protocol must not be altered.
 * </p>
 *
 * <p>
 * Key: &quot;port&quot;, Type: java.lang.Integer, Initial Value: new Integer(-1), Immutable: No,
 * Notes: a port of value -1, empty, or null means  that the underlying mechanism will used the
 * default HTTP port (which may be 80).
 * </p>
 *
 * <p>
 * Key: &quot;host&quot;, Type: java.lang.String, Initial Value: null, Immutable: No, Notes: the
 * name of the host
 * </p>
 *
 * <p>
 * Key: &quot;file&quot;, Type: java.lang.String, Initial Value: null, Immutable: No, Notes: a file
 * in the realm the client wants to access.
 * </p>
 *
 * <p>
 * Key: &quot;request_properties&quot;, Type: java.util.Map, Initial Value: null, Immutable: No,
 * Notes: provides String to String map of HTTP  request properties and values assigned to them.
 * For example, use may specify &quot;Cookie&quot; and &quot;Content-Type&quot; request properties
 * in this hash.
 * </p>
 *
 * <p>
 * The reason for using a char array for the password is that it can be deleted at anytime. A
 * java.lang.String would have to wait until it is garbage-collected.
 * </p>
 *
 * <ol>
 * <li>
 * host must conform to a syntaxically valid host name using dot notation: Examples
 * &quot;www.yahoo.com&quot; or &quot;127.0.0.1&quot;.  Must contain no backslash and port number.
 * Must not be null or empty.
 * </li>
 * <li>
 * file is a name. No validation performed. Must not be null or empty.
 * </li>
 * <li>
 * port must be a number higher than 0. Can be null, empty, or -1, which would signal to the URL to
 * use the default port for the used protocol  (http here, and the port will probably be 80).
 * </li>
 * <li>
 * username must not be null or empty at the time of validation.
 * </li>
 * <li>
 * request_properties should be java.util.Map
 * </li>
 * </ol>
 *
 * <p>
 * All values passed must be of the types defined above. The details property of the returned
 * response is of <code>Response</code> type
 * </p>
 *
 * @author TCSDESIGNER, TCSDEVELOPER
 * @version 2.0
 */
public class HTTPBasicAuthenticator extends AbstractAuthenticator {
    /**
     * <p>
     * Represents the key used for the protocol.
     * </p>
     */
    public static final String PROTOCOL_KEY = "protocol";

    /**
     * <p>
     * Represents the key used for the port number.
     * </p>
     */
    public static final String PORT_KEY = "port";

    /**
     * <p>
     * Represents the key used for the host.
     * </p>
     */
    public static final String HOST_KEY = "host";

    /**
     * <p>
     * Represents the key used for the file.
     * </p>
     */
    public static final String FILE_KEY = "file";

    /**
     * <p>
     * Represents the key used to specify additional request properties sent to HTTP server during
     * session establishment.
     * </p>
     */
    public static final String REQUESTPROPERTIES_KEY = "request_properties";

    /**
     * <p>
     * Represents the key used for the username.
     * </p>
     */
    public static final String USER_NAME_KEY = "username";

    /**
     * <p>
     * Represents the key used for the password.
     * </p>
     */
    public static final String PASSWORD_KEY = "password";

    /**
     * <p>
     * Represents the &quot;default_mappings&quot; property.
     * </p>
     */
    private static final String DEFAULT_MAPPINGS = "default_mappings";

    /**
     * <p>
     * Represents the 'http' protocol.
     * </p>
     */
    private static final String HTTP = "http";

    /**
     * <p>
     * Represents the default request method supported by this authenticator.
     * </p>
     */
    private static final String REQUEST_METHOD = "POST";

    /**
     * <p>
     * Represents the max number of redirections this class checks.
     * The current version only supports 2 directions, since it is not
     * the reality that some http pages will redirect multi-times.
     * <p>
     */
    private static final int MAX_DIRECTIONS = 2;

    /**
     * <p>
     * Create a HttpBasicAuthenticator with the given namespace.
     * </p>
     *
     * @param namespace the namespace to load configuration values.
     *
     * @throws NullPointerException if the namespace is null.
     * @throws IllegalArgumentException if the namespace is empty string.
     * @throws ConfigurationException if fail to load configuration values from the ConfigManager,
     *                                or loaded values are in invalid format.
     */
    public HTTPBasicAuthenticator(String namespace) throws ConfigurationException {
        super(namespace);

        // put the default value
        defaultMappings.put(PORT_KEY, new Integer(-1));
        defaultMappings.put(PROTOCOL_KEY, HTTP);

        ConfigManager cm = ConfigManager.getInstance();
        try {
            Property py = cm.getPropertyObject(namespace, DEFAULT_MAPPINGS);
            if (py == null) {
                // if null, just return, leave defaultMappings empty
                return;
            }

            List sub = py.list();
            for (int i = 0; i < sub.size(); ++i) {
                Property subPy = (Property) sub.get(i);

                // put all the property into defaultMappings, will check its validation later
                if (subPy.getName().equals(PROTOCOL_KEY)) {
                    defaultMappings.put(PROTOCOL_KEY, subPy.getValue());
                } else if (subPy.getName().equals(PORT_KEY)) {
                    Integer integer = new Integer(subPy.getValue());
                    defaultMappings.put(PORT_KEY, integer);
                } else if (subPy.getName().equals(HOST_KEY)) {
                    defaultMappings.put(HOST_KEY, subPy.getValue());
                } else if (subPy.getName().equals(FILE_KEY)) {
                    defaultMappings.put(FILE_KEY, subPy.getValue());
                } else if (subPy.getName().equals(REQUESTPROPERTIES_KEY)) {
                    List subRequrestProperties = subPy.list();
                    Map srpMap = new HashMap();
                    for (int j = 0; j < subRequrestProperties.size(); ++j) {
                        Property srp = (Property) subRequrestProperties.get(j);
                        srpMap.put(srp.getName(), srp.getValue());
                    }
                    defaultMappings.put(REQUESTPROPERTIES_KEY, srpMap);
                }
            }

            // check all the properties's validation
            for (Iterator it = defaultMappings.entrySet().iterator(); it.hasNext();) {
                Entry entry = (Entry) it.next();
                Object key = entry.getKey();
                Object value = entry.getValue();
                validate(key, value);
            }
        } catch (NumberFormatException nfe) {
            throw new ConfigurationException("NumberFormatException caught, please make the port value is valid");
        } catch (UnknownNamespaceException une) {
            throw new ConfigurationException("namespace " + namespace + " is unknown", une);
        }
    }

    /**
     * <p>
     * Authenticates using the underlying service, using keys from principal, and if those are not
     * enough, using keys already present in the defaultMappings. Any internal non-runtime
     * exception is chained to the AuthenticateException, and can be retrieved. The internal
     * authentication scheme will throw IOException if there are any I/O errors, or
     * ProtocolException if the authetication fails (this is the most significant exception).
     * </p>
     *
     * @param principal the principal to authenticate.
     * @return a Resource object with a resource if the service returns one, otherwise null.
     *
     * @throws MissingPrincipalKeyException if certain key is missing in the given principal.
     * @throws InvalidPrincipalException if the principal is invalid, e.g. the type of a
     *         certain key's value is invalid.
     * @throws AuthenticateException if error occurs during the authentication.
     */
    protected Response doAuthenticate(Principal principal) throws AuthenticateException {
        if (principal == null) {
            throw new NullPointerException("principal is null");
        }

        // check if the required key exist in principal, if can't find it in principal
        // if still can't find it, throw MissingPrincipalException

        checkAllKeysExist(principal);

        // get the protocol, host, port, file from the principal and defaultMappings
        String protocol = (String) getRequiredValue(principal, PROTOCOL_KEY);
        String host = (String) getRequiredValue(principal, HOST_KEY);
        String file = (String) getRequiredValue(principal, FILE_KEY);

        int port = -1;
        Integer pt = (Integer) getRequiredValue(principal, PORT_KEY);
        if (pt != null) {
            port = pt.intValue();
        }

        URL url = null;
        HttpURLConnection httpConn = null;
        String originalURL = null;
        int resCode = 0;
        try {
            // open the connection to the http server
            url = new URL(protocol, host, port, file);
            originalURL = url.toExternalForm();
            httpConn = (HttpURLConnection) url.openConnection();

            // connect to the http server
            this.configHttp(httpConn, principal);
            httpConn.connect();

            // check whether the location field exists in the http header
            // if that, reconnect. Note, we only reconnect once.
            String location = httpConn.getHeaderField("location");
            int index = 0;
            while (location != null) {
                url = new URL(location);
                httpConn.disconnect();
                httpConn = (HttpURLConnection) url.openConnection();
                this.configHttp(httpConn, principal);
                httpConn.connect();
                location = httpConn.getHeaderField("location");

                index++;
                if (index >= MAX_DIRECTIONS) {
                    break;
                }
            }
            resCode = httpConn.getResponseCode();
        } catch (MalformedURLException mfe) {
            throw new AuthenticateException(
                    "Creating url fails", mfe);
        } catch (IOException ioe) {
            System.out.println(ioe);
            throw new AuthenticateException(
                    "Opening connection to url fails", ioe);

        }

        HttpResource resource = new HttpResourceImpl(httpConn, originalURL);

        // if responseCode is in [200, 300), means successed
        if (resCode >= 200 && resCode < 300) {
            return new Response(true, "successful", resource);
        } else {
            return new Response(false, "failed", resource);
        }
    }

    /**
     * Get the value of <tt>key</tt> from <tt>principal</tt>, if can't find it in <tt>principal</tt>,
     * we will get it from <tt>defaultMappings</tt>, then check its validation.
     *
     * @param principal the <code>Pricipal</code>
     * @param key the key
     * @return the required value
     *
     * @throws InvalidPrincipalException if the value of <tt>key</tt> is not valid
     */
    private Object getRequiredValue(Principal principal, String key) {
        Object value = principal.getValue(getConverter().convert(key));
        if (value == null) {
            value = defaultMappings.get(key);
        }

        if (value != null) {
            validate(key, value);
        }

        return value;
    }

    /**
     * <p>
     * Check if a key-value pair is legal according to rules specified.
     * in the class document.
     * </p>
     *
     * @param key the key
     * @param value the value
     *
     * @throws InvalidPrincipalException if the value of key is not valid
     */
    private void validate(Object key, Object value) {
        if (key.equals(USER_NAME_KEY)) {
            // username must not be null or empty at the time of validation.
            if (value == null) {
                throw new InvalidPrincipalException(USER_NAME_KEY + "'s value is null.");
            } else if (value.toString().trim().length() == 0) {
                throw new InvalidPrincipalException(USER_NAME_KEY + "'s value is the empty string");
            }
        } else if (key.equals(PASSWORD_KEY)) {
            if (!(value instanceof char[])) {
                // password must be of char[] type
                throw new InvalidPrincipalException(PASSWORD_KEY + "'s value is not of char[] type.");
            }
        } else if (key.equals(PORT_KEY)) {
            // port must be a positive integer or -1
            if (value instanceof Integer) {
                int portNum = ((Integer) value).intValue();
                if ((portNum == 0) || (portNum < -1)) {
                    throw new InvalidPrincipalException(
                        PORT_KEY + "'s port " + portNum + " must be positive or -1");
                }
            } else if ((value != null) && !(value instanceof Integer)) {
                throw new InvalidPrincipalException(
                        PORT_KEY + "'s value must be of Integer type");
            }
        } else if (key.equals(HOST_KEY)) {
            // host must be an invalid host name. Must contain no backslash
            // and port number. Must not be null or empty.
            if (value == null) {
                throw new InvalidPrincipalException(
                        HOST_KEY + "'s value is null");
            } else if (!(value instanceof String)) {
                throw new InvalidPrincipalException(
                        HOST_KEY + "'s value is not of String type.");
            } else {
                String tmp = (String) value;
                if (tmp.trim().equals("") || (tmp.indexOf(":") != -1)
                        || (tmp.indexOf("/") != -1) || (tmp.indexOf("\\") != -1)) {
                    throw new InvalidPrincipalException(
                            "The host name " + value + " is invalid.");
                }
            }
        } else if (key.equals(FILE_KEY)) {
            // file is a name. No validation performed.
            // Must not be null or empty.
            if (value == null) {
                throw new InvalidPrincipalException(
                        FILE_KEY + "'s value is null");
            } else if (value.toString().trim().length() == 0) {
                throw new InvalidPrincipalException(
                        FILE_KEY + "'s value is the empty string");
            }
        } else if (key.equals(REQUESTPROPERTIES_KEY) && (value != null)) {
            // request_properties must be null or java.util.Map
            // the key-value in map must be of String type
            if (!(value instanceof java.util.Map)) {
                throw new InvalidPrincipalException(
                        REQUESTPROPERTIES_KEY + "'s value must be of java.util.Map type.");
            }

            Map tmp = (Map) value;
            for (Iterator it = tmp.entrySet().iterator(); it.hasNext();) {
                Entry entry = (Entry) it.next();
                if (!(entry.getKey() instanceof String)
                    || !(entry.getValue() instanceof String)) {
                    throw new InvalidPrincipalException(
                            "The key-value in the map value must be of String type.");
                }
            }
        } else if (key.equals(PROTOCOL_KEY)
                && (!(value instanceof String) || !(value.equals(HTTP)))) {
            throw new InvalidPrincipalException(
                "The protocol must be 'http', not " + value);
        }
    } // end validate

    /**
     * <p>
     * This private method sets the httpheaderfield with extra http
     * request-property mappings, such as "Accept-text/html", user name and
     * password simply by calling HttpURLConnection.setRequestProperty().
     * And set the request method, note, we use 'POST' as default.
     * </p>
     *
     * @param httpConn a httpURlConnection to be set
     * @param principal a <code>Pricipal</code> containing http request-properties mappings
     * @throws ProtocolException if the request method cannot be reset or if
     *                           the requested method isn't valid for HTTP.
     */
    private void configHttp(HttpURLConnection httpConn, Principal principal)
        throws ProtocolException {

        // set comman httpheaderfields such as "Accept-text/html"
        Map resProps = (Map) getRequiredValue(principal, REQUESTPROPERTIES_KEY);
        if (resProps != null) {
            for (Iterator it = resProps.entrySet().iterator(); it.hasNext();) {
                Entry entry = (Entry) it.next();
                String key = (String) entry.getKey();
                String value = (String) entry.getValue();
                httpConn.setRequestProperty(key, value);
            }
        }

        // set the username and password for authentication
        String userName = (String) getRequiredValue(principal, USER_NAME_KEY);
        char[] pwd = (char[]) getRequiredValue(principal, PASSWORD_KEY);

        String namePwd = userName + ":" + new String(pwd);
        httpConn.setRequestProperty("Authorization", "Basic " + Base64.encode(namePwd.getBytes()));

        // set the request method
        httpConn.setRequestMethod(REQUEST_METHOD);
    }

    /**
     * This private method check whether the principal and defaultMappings contains all
     * the keys specified in this class. If not, throw MissingPrincipalKeyException.
     *
     * @param principal to be checked
     *
     * @throws MissingPrincipalKeyException if specified principal and defaultMappings
     *         does not contain all required keys in this class.
     */
    private void checkAllKeysExist(Principal principal) {
        if (getRequiredValue(principal, PASSWORD_KEY) == null) {
            throw new MissingPrincipalKeyException(PASSWORD_KEY);
        }
        if (getRequiredValue(principal, USER_NAME_KEY) == null) {
            throw new MissingPrincipalKeyException(USER_NAME_KEY);
        }
        if (getRequiredValue(principal, HOST_KEY) == null) {
            throw new MissingPrincipalKeyException(HOST_KEY);
        }
        if (getRequiredValue(principal, PORT_KEY) == null) {
            throw new MissingPrincipalKeyException(PORT_KEY);
        }
        if (getRequiredValue(principal, FILE_KEY) == null) {
            throw new MissingPrincipalKeyException(FILE_KEY);
        }
    }
}