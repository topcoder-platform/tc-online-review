/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.security.authenticationfactory.http;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * <p>
 * This class represents a cookie provided by HTTP server to client in the
 * 'Set-Cookie' header field.
 * </p>
 *
 * @author TCSDESIGNER, TCSDEVELOPER
 * @version 2.0
 * @since 1.1
 */
public class HttpCookie {
    /**
     * <p>
     * Represents the attribute name of 'path' in the cookie.
     * </p>
     */
    private static final String PATH = "path";

    /**
     * <p>
     * Represents the attribute name of 'domain' in the cookie.
     * </p>
     */
    private static final String DOMAIN = "domain";

    /**
     * <p>
     * Represents the attribute name of 'max-age' in the cookie.
     * </p>
     */
    private static final String MAX_AGE = "max-age";

    /**
     * <p>
     * Represents the attribute name of 'Secure' in the cookie.
     * </p>
     */
    private static final String SECURE = "secure";

    /**
     * <p>
     * Represents the attribute name of 'comment' in the cookie.
     * </p>
     */
    private static final String COMMENT = "comment";

    /**
     * <p>
     * Represents the attribute name of 'version' in the cookie.
     * </p>
     */
    private static final String VERSION = "version";

    /**
     * <p>
     * A map holds the attribute-value couples parsed from the cookie.
     * All the attribute names are list above.
     * </p>
     */
    private  Map cookieMap = new HashMap();

    /**
     * <p>
     * This field stores cookie name. Each cookie should have a name.
     * </p>
     */
    private String name = null;

    /**
     * <p>
     * This field stores cookie value. Each cookie should have a value.
     * </p>
     */
    private String value = null;

    /**
     * <p>
     * Creates HTTP cookie from information contained in the 'Set-Cookie'
     * header field provided by the server.
     * </p>
     *
     * <p>
     * Parsing is performed according to basic cookie structure defined in the
     * RFC2109 (HTTP State Management Mechanism) specification. Note, that
     * this constructor doesn't guarantee full cookie validation conformant
     * to any RFC specification but just provides basic cookie parsing
     * capabilities. It also doesn't support parsing of multiple
     * cookies provided in the single Set-Cookie string.
     * </p>
     *
     * <p>
     * Parsed string is matched with the following syntax definition
     * </p>
     *
     * <ol>
     * <li>
     * <em>cookie&nbsp;=&nbsp;NAME &quot;=&quot; VALUE (&quot;;&quot;
     * cookie-av)</em>
     * </li>
     * <li>
     * <em>NAME&nbsp;&nbsp;=&nbsp;attr</em>
     * </li>
     * <li>
     * <em>VALUE&nbsp;=&nbsp;value</em>
     * </li>
     * <li>
     * <em>cookie-av&nbsp;=&nbsp;</em>
     * </li>
     * </ol>
     *
     *
     * <ul type="disc">
     * <li>
     * <em>&quot;Comment&quot; &quot;=&quot; value |</em>
     * </li>
     * <li>
     * <em>&quot;Domain&quot; &quot;=&quot; value | </em>
     * </li>
     * <li>
     * <em>&quot;Max-Age&quot; &quot;=&quot; value |</em>
     * </li>
     * <li>
     * <em>&quot;Path&quot; &quot;=&quot; value |</em>
     * </li>
     * <li>
     * <em>&quot;Secure&quot; |</em>
     * </li>
     * <li>
     * <em>&quot;Version&quot; &quot;=&quot; 1DIGIT</em>
     * </li>
     * </ul>
     *
     * <p>
     * Each cookie should begin with a NAME=VALUE pair, followed by zero or
     * more&nbsp;&nbsp;&nbsp; semi-colon-separated attribute-value
     * pairs.&nbsp; The syntax for attribute-value pairs was shown
     * earlier.&nbsp; The specific attributes and the semantics of their
     * values follows.&nbsp; The NAME=VALUE attribute-value pair must come
     * first in each cookie.&nbsp; The others, if present, can occur in any
     * order.&nbsp; No attribute should appear more than once in a cookie.
     * </p>
     *
     * <p>
     * Below is description of each attribute and validation/parsing rules for
     * them (if applicable).
     * </p>
     *
     * <ol>
     * <li>
     * <strong>NAME=VALUE</strong>. Required.&nbsp;The name of the state
     * information (&quot;cookie&quot;) is NAME and its value is VALUE.
     * </li>
     * <li>
     * <strong>Comment=comment</strong>. Optional.
     * </li>
     * <li>
     * <strong>Domain=domain</strong>. Optional.
     * </li>
     * <li>
     * <strong>Max-Age=delta-seconds</strong>. Optional. Delta-seconds should
     * be a decimal non-negative integer.
     * </li>
     * <li>
     * <strong>Path=path</strong>. Optional.
     * </li>
     * <li>
     * <strong>Secure</strong>. Optional. This is the only attribute which
     * should have no value.
     * </li>
     * <li>
     * <strong>Version=version. </strong>Though required by RFC2109 this field
     * should be considered optional for the scope of this document. This
     * field should be a decimal integer.
     * </li>
     * </ol>
     *
     * <p>
     * If the input string is null, IllegalArgumentException will be thrown by
     * this constructor.
     * </p>
     *
     * <p>
     * In the following cases input string is considered invalid and
     * IllegalArgumentException is thrown
     * </p>
     *
     * <ol>
     * <li>
     * String doesn't begin from <strong>NAME=VALUE </strong>pair.
     * </li>
     * <li>
     * String contains multiple cookies.
     * </li>
     * <li>
     * String contains unknown or incorrectly formatted attributes.
     * </li>
     * <li>
     * String contains duplicate attributes.
     * </li>
     * <li>
     * Max-Age attribute is present and its value is not a decimal
     * non-negative integer.
     * </li>
     * <li>
     * Secure attribute has some value assigned.
     * </li>
     * <li>
     * Version attribute is prsent and its not a decimal integer.
     * </li>
     * </ol>
     *
     * <p>
     * If parsing is successfull, cookie attrbiutes will be correctly
     * assigned.
     * </p>
     *
     * @since 1.1
     *
     * @param setCookie parsed 'Set-Cookie' header field
     *
     * @throws NullPointerException if setCookie is null
     * @throws IllegalArgumentException if fail to parse
     *         the setCookie
     *
     */
    public HttpCookie(String setCookie) {
        if (setCookie == null) {
            throw new NullPointerException("The setCookie is null.");
        }

        // check whether the setCookie represents a multi-cookies
        if (setCookie.indexOf("#") != -1) {
            throw new IllegalArgumentException(
                "The setCookie string(" + setCookie + ") is a multi-cookies.");
        }

        // initialize the map, the initial values of all the attributes are null
        cookieMap.put(COMMENT, null);
        cookieMap.put(DOMAIN, null);
        cookieMap.put(MAX_AGE, null);
        cookieMap.put(PATH, null);
        cookieMap.put(SECURE, null);
        cookieMap.put(VERSION, null);

        // pasre the setCookie string and set the various variables
        // via the parsed result
        this.initialCookie(setCookie);
    } // end HttpCookie

    /**
     * <p>
     * Returns cookie name for this cookie. Each valid cookie has a
     * name.
     * </p>
     *
     * @return cookie name
     */
    public String getName() {
        return this.name;
    } // end getName

    /**
     * <p>
     * Returns cookie value for this cookie.&nbsp; Each valid cookie has a
     * value.
     * </p>
     *
     * @return cookie value
     */
    public String getValue() {
        return this.value;
    } // end getValue

    /**
     * <p>
     * Returns optional path attribute for this cookie or null if path was not
     * configured. The Path attribute specifies the subset of URLs to which
     * this cookie applies.
     * </p>
     *
     * @return cookie path or null if this cookie attribute was not specifed
     *         by server
     */
    public String getPath() {
        return (String) cookieMap.get(PATH);
    } // end getPath

    /**
     * <p>
     * Returns optional domain attribute for this cookie or null if domain was
     * not configured. The Domain attribute specifies the domain for which
     * the cookie is valid.
     * </p>
     *
     * @return cookie domain or null if this cookie attribute was not specifed
     *         by server
     */
    public String getDomain() {
        return (String) cookieMap.get(DOMAIN);
    } // end getDomain

    /**
     * <p>
     * Returns optional Max-Age attribute for this cookie or -1 if Max-Age was
     * not configured. The Max-Age attribute defines the lifetime of the
     * cookie, in seconds;After Max-Age seconds elapse, the client
     * should discard the cookie; A value of zero means the cookie
     * should be discarded immediately.
     * </p>
     *
     * @return cookie domain or -1 if this cookie attribute was not specifed
     *         by server
     */
    public int getMaxAge() {
        Integer integer = (Integer) cookieMap.get(MAX_AGE);
        if (integer == null) {
            return -1;
        }

        return integer.intValue();
    } // end getMaxAge

    /**
     * <p>
     * Returns true is the cookie is secure. Secure cookie requires client to
     * use secure means to contact the origin server whenever it sends back
     * this cookie. Cooki secure if and only if 'secure' attribute with no
     * value was provided by server.
     * </p>
     *
     * @return true if cookie is secure, otherwise false
     */
    public boolean isSecure() {
        return cookieMap.get(SECURE) != null;
    } // end isSecure

    /**
     * <p>
     * Returns optional comment for this cookie or null if comment attribute
     * was not provided by server.
     * </p>
     *
     * @return cookie domain or null if this cookie attribute was not specifed
     *         by server
     */
    public String getComment() {
        return (String) cookieMap.get(COMMENT);
    } // end getComment

    /**
     * <p>
     * Identifies which version of state management specification this cookie
     * conforms.
     * </p>
     *
     * @return cookie version
     */
    public int getVersion() {
        Integer integer = (Integer) cookieMap.get(VERSION);
        if (integer == null) {
            return 1;
        }

        return integer.intValue();
    } // end getVersion

    /**
     * <p>
     * This private method parses the content ofa  cookie, and sets the
     * 'name', 'value', 'comment', 'domain', 'maxAge', 'path', 'secure',
     * 'version' attributes according to rfc2109.
     * Note: 'comment', 'domain', 'maxAge', 'path', 'secure',
     * 'version' are optional in a cookie
     *
     * <p>
     *  The content of the cookie is retrieved from the HttpHeaderField,
     * it is almost always valid, since the syntax of a set-cookie attribute
     * is really liberal. This function only provide the basic parsing of a
     * single set-cookie.
     * </p>
     *
     * <p>
     * Note:
     * Many cookies sent by common sites such as www.yahoo.com are invalid
     * according to RFC2109. However, this method simply accepts them as
     * valid ones. It's meaningless to create a cookie string manually,
     * and create a HttpCookie instance from this cookie string, since this
     * class only accepts cookie string sent by actual sites.
     * </p>
     *
     * @param cookie a string represents the content of a cookie
     *
     * @throws IllegalArgumentException if fail to parse the cookie sting
     */
    private void initialCookie(String cookie) {
        StringTokenizer tokenizer = new StringTokenizer(cookie, ";");
        if (tokenizer.countTokens() < 1) {
            throw new IllegalArgumentException(
                    "The cookie content(" + cookie + ") is invalid.");
        }

        // parse the name=value mapping first
        String token = tokenizer.nextToken();
        String[] nameVal = this.getAttrValue(token, cookie);
        this.name = nameVal[0].trim();
        this.value = nameVal[1].trim();
        // we only check wether the name and value are empty
        if ((name.length() == 0) || (value.length() == 0)) {
            throw new IllegalArgumentException(
                    "The content of this cookie(" + cookie + ") is invalid.");
        }
        checkNameOfCookie(name);

        // parse the optinal attribute-value mappings. If there is
        // duplicated mappings, throws IAE
        token = null;
        while (tokenizer.hasMoreTokens()) {
            // parse the attribute-value mapping from the nextToken
            nameVal = this.getAttrValue(tokenizer.nextToken(), cookie);

            // Note, the attribute-value mappings are case inseneitive
            if (nameVal[0].equalsIgnoreCase(COMMENT)) {
                // parse comment
                if ((nameVal[1].length() == 0) || cookieMap.get(COMMENT) != null) {
                    throw new IllegalArgumentException(
                        "The content of this cookie(" + cookie + ") is invalid near comment.");
                }
                cookieMap.put(COMMENT, nameVal[1]);
            } else if (nameVal[0].equalsIgnoreCase(DOMAIN)) {
                // parse domain, a domain must starts with "."
                if ((nameVal[1].length() == 0)
                        || !nameVal[1].startsWith(".")
                        || (cookieMap.get(DOMAIN) != null)) {
                    throw new IllegalArgumentException(
                        "The content of this cookie(" + cookie + ") is invalid near domain.");
                }
                cookieMap.put(DOMAIN, nameVal[1]);
            } else if (nameVal[0].equalsIgnoreCase(PATH)) {
                // parse path
                if ((nameVal[1].length() == 0) || cookieMap.get(PATH) != null) {
                    throw new IllegalArgumentException(
                        "The content of this cookie(" + cookie + ") is invalid near path.");
                }
                cookieMap.put(PATH, nameVal[1]);
            } else if (nameVal[0].equalsIgnoreCase(MAX_AGE)) {
                // pasrse max-age
                int maxAge = 1;
                try {
                    maxAge = Integer.parseInt(nameVal[1]);
                } catch (NumberFormatException nfe) {
                    throw new IllegalArgumentException(
                            "The content of this cookie(" + cookie + ") is invalid near maxAge.");
                }
                if ((maxAge <= 0) || (cookieMap.get(MAX_AGE) != null)) {
                    throw new IllegalArgumentException(
                            "The content of this cookie(" + cookie + ") is invalid near max-Age.");
                }
                cookieMap.put(MAX_AGE, new Integer(maxAge));
            } else if (nameVal[0].equalsIgnoreCase(VERSION)) {
                // parse version
                int version = 1;
                try {
                    version = Integer.parseInt(nameVal[1]);
                } catch (NumberFormatException nfe) {
                    throw new IllegalArgumentException("The content of this cookie(" + cookie
                            + ") is invalid near version.");
                }
                if ((version <= 0) || (cookieMap.get(VERSION) != null)) {
                    throw new IllegalArgumentException(
                            "The content of this cookie(" + cookie + ") is invalid near version.");
                }
                cookieMap.put(VERSION, new Integer(version));
            } else if (nameVal[0].equalsIgnoreCase(SECURE)) {
                // parse secure
                if ((nameVal[1].length() != 0) || (cookieMap.get(SECURE) != null)) {
                    throw new IllegalArgumentException(
                            "The content of this cookie(" + cookie + ") is invalid near Secure.");
                }
                cookieMap.put(SECURE, new Boolean(true));
            }
        }
    }

    /**
     * <p>
     * This private method parse the attribute-value mapping contained in the
     * attrValue argument. If parse successfully, this method will put the
     * attirbute in the first position of the returned String array, and put
     * the value in the second position. If the parameter attrValue is empty
     * throw IllegalArgumentException wrapping the cookie argument.
     * </p>
     *
     * <p>
     * Note: Parsing the set-cookie content stringent against RFC2109 is out
     * of the scope of this component.
     * </p>
     *
     * @param attrValue contains the attribute-value string to be parsed
     * @param cookie where the attrValue extracted from.
     * @return String[] contains the attribute-value mapping
     *
     * @throws IllegalArgumentException if fail to parse the attrValue
     */
    private String[] getAttrValue(String attrValue, String cookie) {
        String copy = attrValue.trim();
        if (copy.length() == 0) {
            throw new IllegalArgumentException(
                    "The content of this cookie(" + cookie + ") is invalid near " + attrValue);
        }
        // if the attrValue is name-value mapping, just ignore the string part
        // after the '&' and  '$' according to rfc2109 on page 4
        if (copy.indexOf("&") != -1) {
            copy = copy.substring(0, copy.indexOf("&"));
        } else if (copy.indexOf("$") != -1) {
            copy = copy.substring(0, copy.indexOf("$"));
        }


        StringTokenizer tokenizer = new StringTokenizer(copy, "=");
        String attr = tokenizer.nextToken();
        String valueOfAttr = "";
        if (tokenizer.hasMoreTokens()) {
            valueOfAttr = tokenizer.nextToken();
        }

        return new String[]{attr.trim(), valueOfAttr.trim()};
    }

    /**
     * <p>
     * If the name is any one of 'comment', 'max-age', 'domain', 'path',
     * 'secure', or 'version'.
     * </p>
     *
     * @param name the name
     * @throws IllegalArgumentException if name is a value list above.
     */
    private void checkNameOfCookie(String name) {
        // we only check whethe the name is one of the attributes name list above
        if (cookieMap.keySet().contains(name.toLowerCase())) {
            throw new IllegalArgumentException(
                    "The setCookie must contain name=value mapping.");
        }
    }

} // end HttpCookie