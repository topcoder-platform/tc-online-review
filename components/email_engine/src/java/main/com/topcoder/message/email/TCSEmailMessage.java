/*
 * Copyright (C) 2005-2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.message.email;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.mail.Address;
import javax.mail.internet.InternetAddress;

import com.topcoder.message.TCSMessage;
import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.config.Property;


/**
 * <p>The <code>TCSEmailMessage</code> class represents a single email message.</p>
 *
 * <p>
 * Version 3.0 of <code>TCSEmailMessage</code> have added support for priority of the email message.
 * </p>
 * <p>
 * In version 3.1, this class is updated to include the content type property and an explicit default
 * constructor is added. Other methods aren't changed, even where they don't meet current TC standards.
 * </p>
 * <p>
 * Version 3.2 remove the not used deprecated private field.
 * </p>
 * @author sord, veredox, BEHiker57W, smell, TCSDEVELOPER
 * @created April 17, 2002
 * @version 3.2
 * @internal Log of Changes: $Log: TCSEmailMessage.java,v $
 * @internal Log of Changes: Revision 1.3  2005/10/04 19:54:19  ivern
 * @internal Log of Changes: no message
 * @internal Log of Changes:
 * @internal Log of Changes: Revision 1.2  2005/10/04 14:38:57  mgmg
 * @internal Log of Changes: *** empty log message ***
 * @internal Log of Changes:
 *
 *              Revision 1.1.1.1 2004/03/23 23:03:25 gpaul
 *              no message
 *
 *              Revision 1.3 2003/04/01 18:58:08 marcg
 *              Updates for 2.0
 *
 *              Revision 1.1.1.1 2003/01/09 22:31:19 marcg
 *              no message
 *
 *              Revision 1.7 2002/11/28 15:30 yellow_gecko
 *              added attachments map and corresponding methods
 *
 *              Revision 1.1.1.1 2002/10/28 17:24:47 marcg
 *              no message
 *
 *              Revision 1.6 2002/09/30 16:11:58 seaniswise
 *              added javadocs
 *
 *              Revision 1.5 2002/09/30 15:55:36 seaniswise
 *              no message
 *
 *              Revision 1.4 2002/09/29 19:28:12 marcg
 *              changed
 *
 *              Revision 1.3 2002/06/11 21:27:34 veredox
 *              no message
 *
 *              Revision 1.2 2002/05/21 21:51:48 tcs
 *              change package name
 *
 *              Revision 1.1 2002/05/10 13:14:06 tcs
 *              no message
 *
 *              Revision 1.1.2.2 2002/04/17 12:43:17 apps
 *              Fixed some compiler issues.
 *
 *              Revision 1.1.2.1 2002/04/17 12:34:11 apps
 *              Initial implementation.
 */
public class TCSEmailMessage extends TCSMessage {

    /**
     * Blind Carbon Copy (BCC) field constant.
     */
    public static final int BCC = 2;
    /**
     * Carbon Copy (CC) field constant.
     */
    public static final int CC = 1;
    /**
     * TO field constant.
     */
    public static final int TO = 0;
    /**
     * <p>The map of the default priority headers. The keys to the map the predefined PriorityLevel instances, and the
     * values are maps of the headers for each priority. The key to the second level map is the message header name, and
     * the value is the header content.<p>
     *
     * @since 3.0
     */
    private static final Map DEFAULT_PRIORITY_HEADERS;
    /**
     * <p>The ConfigManager format identifier for the configuration properties for the email message class.</p>
     *
     * <p>For version 3.0, XML format is used.</p>
     *
     * @since 3.0
     */
    private static final String PROPERTIES_FORMAT = ConfigManager.CONFIG_XML_FORMAT;
    /**
     * <p>The file in which configuration properties for the email message class will be found.</p>
     *
     * <p>For version 3.0, this file contains configurable priority header information.</p>
     *
     * @since 3.0
     */
    private static final String PROPERTIES_LOCATION = "com/topcoder/message/email/TCSEmailMessage.xml";

    /**
     * <p>The ConfigManager namespace for the configuration properties for the email message class.</p>
     *
     * <p>For version 3.0, the namespace if ConfigManager will contain configurable priority header information.</p>
     *
     * @since 3.0
     */
    private static final String PROPERTIES_NAMESPACE = "com.topcoder.message.email.TCSEmailMessage";

    /**
     * String name constant to retrieve the priorities name.
     *
     * @since 3.0
     */
    private static final String PRIORITIES_NAME = "Priorities";

    /** Represents..  */
    private Map attachments = new Hashtable();

    /** Represents..  */
    private String body = "";

    /** Represents..  */
    private InternetAddress from = null;

    /**
     * <p>It contains the headers of this email message, it is initialized to an empty Map by default. The key of the
     * map is header name (String) and the value is the content of the header (String). Accessors have also been
     * provided for it.</p>
     *
     * <p>For version 3.0, the map will include only the priority headers.</p>
     *
     * @since 3.0
     */
    private Map headersMap = new HashMap();

    /**
     * <p>The priority of this email message, it takes one of the predefined values for the priority of this email
     * message. It is initialized to PriorityLevel.NONE by default. Users can access it via the getPriority and
     * setPriority methods.</p>
     *
     * @since 3.0
     */
    private PriorityLevel priority = PriorityLevel.NONE;

    /**
     * <p>This map contains the priority headers used for this message. It is initialized to an empty map which is
     * equivalent to the default headers for PriorityLevel.NONE.</p>
     */
    private Map priorityHeaders = new HashMap();

    /** Represents..  */
    private String subject = "";

    /** Represents..  */
    private InternetAddress[][] to = new InternetAddress[3][0];
    /**
     * This field indicates the content type for the message body - it is new in version 3.1. The default
     * value is "text/plain". It can be set to any non-null, non-empty string through the setContentType
     * method and retrieved through getContentType.
     */
    private String contentType = "text/plain";

    /**
     * <p>This static block initializes the DEFAULT_PRIORITY_HEADERS to contain the default headers for the
     * priorities.</p>
     */
    static {
        DEFAULT_PRIORITY_HEADERS = new HashMap();

        // Headers for highest priority.
        Map highest = new HashMap();
        highest.put("Priority", "urgent");
        highest.put("X-Priority", "1 (Highest)");
        highest.put("X-MSMail-Priority", "High");
        DEFAULT_PRIORITY_HEADERS.put(PriorityLevel.HIGHEST, highest);

        // Headers for high priority.
        Map high = new HashMap();
        high.put("Priority", "urgent");
        high.put("X-Priority", "2 (High)");
        high.put("X-MSMail-Priority", "High");
        DEFAULT_PRIORITY_HEADERS.put(PriorityLevel.HIGH, high);

        // Headers for normal priority.
        Map normal = new HashMap();
        normal.put("Priority", "normal");
        normal.put("X-Priority", "3 (Normal)");
        normal.put("X-MSMail-Priority", "Normal");
        DEFAULT_PRIORITY_HEADERS.put(PriorityLevel.NORMAL, normal);

        // Headers for low priority.
        Map low = new HashMap();
        low.put("Priority", "non-urgent");
        low.put("X-Priority", "4 (Low)");
        low.put("X-MSMail-Priority", "Low");
        DEFAULT_PRIORITY_HEADERS.put(PriorityLevel.LOW, low);

        // Headers for lowest priority.
        Map lowest = new HashMap();
        lowest.put("Priority", "non-urgent");
        lowest.put("X-Priority", "5 (Lowest)");
        lowest.put("X-MSMail-Priority", "Low");
        DEFAULT_PRIORITY_HEADERS.put(PriorityLevel.LOWEST, lowest);

        // Headers for no priority, the headers map for it is empty.
        DEFAULT_PRIORITY_HEADERS.put(PriorityLevel.NONE, Collections.EMPTY_MAP);
    }
    /**
     * the default ctor does nothing.
     * @since 3.1
     */
    public TCSEmailMessage() {
        //does nothing
    }

    /**
     * <p>Adds an attachment to the attachments <code>Map</code> variable using the InputStream of the file to be
     * attached as the key and the name of the file as the value.</p>
     *
     * @param attachment    an InputStream containing file to be attached
     * @param name          the name of the file to attached
     */
    public void addAttachment(InputStream attachment, String name) {
        checkNull(attachment, "attachment");
        checkNull(name, "name");

        attachments.put(attachment, name);
    }

    /**
     * <p>Adds an additional "to" address of the specified type (TO, CC, BCC).</p>
     *
     * @param addr  the new "to" address
     * @param type  the new "to" address type (TO, CC, or BCC)
     *
     * @throws NullPointerException     if addr is <code>null</code>
     * @throws IllegalArgumentException if type is not TO, CC, nor BCC
     * @throws AddressException         if addr is not a valid address according to RFC822
     */
    public void addToAddress(String addr, int type) throws AddressException {
        checkNull(addr, "addr");

        if (type < 0 || type >= to.length) {
            throw new IllegalArgumentException("Invalid type");
        }

        try {
            to[type] = addAddress(to[type], new InternetAddress(addr));
        } catch (javax.mail.internet.AddressException e) {
            throw new AddressException("Invalid Address", e);
        }
    }

    /**
     * <p>Adds an additional to address for the specified type (TO, CC, BCC).</p>
     *
     * @param addr      the new "to" address
     * @param personal  the new personal name
     * @param type      the new "to" address type TO, CC, or BCC)
     *
     * @throws NullPointerException     if either addr or personal is <code>null</code>
     * @throws IllegalArgumentException if type is not TO, CC, nor BCC
     * @throws AddressException         if  1) address is not a valid address according to RFC822
     *                                      2) encoding of the personal is not supported
     */
    public void addToAddress(String addr, String personal, int type) throws AddressException {
        checkNull(addr, "addr");
        checkNull(personal, "personal");

        if (type < 0 || type >= to.length) {
            throw new IllegalArgumentException("Invalid type");
        }

        to[type] = addAddress(to[type], createAddress(addr, personal));
    }

    /**
     * Clears the attachments <code>Map</code> variable.
     */
    public void clearAttachments() {
        attachments.clear();
    }

    /**
     * Returns the attachments <code>Map</code> variable.
     *
     * @return the attachments <code>Map</code>
     */
    public Map getAttachments() {
        return attachments;
    }

    /**
     * Returns the body of the message.
     *
     * @return the body of the message or a 0 length string if no body has been set.
     */
    public String getBody() {
        return body;
    }

    /**
     * Returns the formatted from address for the message.
     *
     * @return the formatted from address that has been set for the message or null if the address has not been set.
     */
    public Address getFromAddress() {
        return from;
    }

    /**
     * <p>Gets the value for a header with the specified key. If the value with the key does not exist,
     * <code>null</code> is returned.</p>
     *
     * @param key   the key of header to get
     *
     * @return value for the key argument, <code>null</code> if the value does not exist
     *
     * @throws NullPointerException if key is <code>null</code>
     * @throws IllegalArgumentException if key is empty string after being trimmed.
     *
     * @since 3.0
     */
    public String getHeader(String key) {
        checkNull(key, "key");
        if (key.trim().length() == 0) {
            throw new IllegalArgumentException("key should not be empty string after being trimmed");
        }
        return (String) headersMap.get(key);
    }

    /**
     * <p>Return an unmodifiable copy of the headers map. The returned <code>Map</code> contains the headers used for
     * this <code>TCSEmailMessage</code>.</p>
     *
     * @return unmodifiable copy of the headers map
     *
     * @since 3.0
     */
    public Map getHeadersMap() {
        return Collections.unmodifiableMap(headersMap);
    }

    /**
     * <p>Returns the priority level of this <code>TCSEmailMessage</code>.</p>
     *
     * @return the priority level
     *
     * @since 3.0
     */
    public PriorityLevel getPriority() {
        return priority;
    }

    /**
     * Returns the subject of the message.
     *
     * @return the subject of the message or a 0 length string if no subject has been set.
     */
    public String getSubject() {
        return subject;
    }

    /**
     * <p>Returns a list of formatted "to" addresses of the specified type. If no addresses of the given type have been
     * set, the returned array will be of length 0.</p>
     *
     * @param type  the address type (TO, CC, or BCC)
     *
     * @return a list of formatted addresses.
     *
     * @throws IllegalArgumentException  if the address type is not defined
     */
    public Address[] getToAddress(int type) {
        if (type < 0 || type >= to.length) {
            throw new IllegalArgumentException("Invalid type");
        }

        return dup(to[type]);
    }

    /**
     * <p>Removes this header with the specified key from the email entirely. If the specified header is not present,
     * nothing happens.</p>
     *
     * @param key   the key of header to remove
     *
     * @return old value of header for key, or <code>null</code> if the header is not present
     *
     * @throws NullPointerException if the key is <code>null</code>
     *
     * @since 3.0
     */
    public String removeHeader(String key) {
        checkNull(key, "key");
        if (key.trim().length() == 0) {
            throw new IllegalArgumentException("key should not be empty string after being trimmed");
        }
        return (String) headersMap.remove(key);
    }

    /**
     * <p>Sets the body of the message.</p>
     *
     * @param body the new body text
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * <p>Sets the from address of this message. The address should be in RFC822 format, otherwise
     * <code>AddressException</code> will be thrown.</p>
     *
     * @param addr  the "from" address in RFC822 format
     *
     * @throws NullPointerException if addr is <code>null</code>
     * @throws AddressException     if addr is not a valid address according to RFC822
     */
    public void setFromAddress(String addr) throws AddressException {
        checkNull(addr, "addr");

        try {
            from = new InternetAddress(addr);
        } catch (javax.mail.internet.AddressException e) {
            throw new AddressException("Invalid Address", e);
        }
    }

    /**
     * <p>Sets the from address with a personal name.</p>
     *
     * @param addr      the new "from" address in RFC822 format
     * @param personal  the personal name
     *
     * @throws NullPointerException if either argument is <code>null</code>
     * @throws AddressException     if  1) address is not a valid address according to RFC822
     *                                  2) encoding of the personal is not supported
     */
    public void setFromAddress(String addr, String personal) throws AddressException {
        checkNull(addr, "addr");
        checkNull(personal, "personal");

        from = createAddress(addr, personal);
    }

    /**
     * <p>Sets a new header value. If an old value exists for the key, it is overwritten.</p>
     *
     * @param key   key for header to set.
     * @param value value to set header to
     *
     * @throws NullPointerException     if either key or value is <code>null</code>
     * @throws IllegalArgumentException if key is empty string after being trimmed.
     *
     * @since 3.0
     */
    public void setHeader(String key, String value) {
        checkNull(key, "key");
        checkNull(value, "value");

        if (key.trim().length() == 0) {
            throw new IllegalArgumentException("key should not be empty string after being trimmed");
        }
        headersMap.put(key, value);
    }

    /**
     * <p>Sets the value for the priority variable. Also sets or resets the value of the various priority headers for
     * the newly set priority.</p>
     *
     * <p>It first clears the old headers for the priority, then add the new headers. To find the new headers for the
     * given priority, it tries to search the configuration under the default namespace, it also tries to add the
     * configuration file if the namespace does not present. If there is no configuration for the headers of the given
     * priority or any error occurs when searching the configuration, the default headers of this priority are used
     * instead.</p>
     *
     * @param priority  the new priority to set.
     *
     * @throws NullPointerException if priority is <code>null</code>
     *
     * @since 3.0
     */
    public void setPriority(PriorityLevel priority) {
        checkNull(priority, "priority");

        // Removes the old headers from headersMap.
        headersMap.entrySet().removeAll(priorityHeaders.entrySet());

        loadPriorityHeaders(priority);
        this.priority = priority;

        // Adds the new headers to headersMap.
        headersMap.putAll(priorityHeaders);
    }

    /**
     * <p>Sets the subject of the message.</p>
     *
     * @param subject   the new subject text
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * <p>Sets the address of the specified delivery type (TO, CC, or BCC). Any to addresses this type that have already
     * been set will be discarded.</p>
     *
     * @param addr  the new address in RFC822 format
     * @param type  the type of the address (TO, CC, or BCC)
     *
     * @throws NullPointerException     if addr is <code>null</code>
     * @throws IllegalArgumentException if type is not TO, CC, nor BCC
     * @throws AddressException         if address is not a valid address according to RFC822
     */
    public void setToAddress(String addr, int type) throws AddressException {
        checkNull(addr, "addr");

        // Check type.
        if (type < 0 || type >= to.length) {
            throw new IllegalArgumentException("Invalid type");
        }

        to[type] = new InternetAddress[0];
        addToAddress(addr, type);
    }

    /**
     * <p>Sets the address of the specified type (TO, CC, or BCC). Any to addresses this type that have already been set
     * will be discarded.</p>
     *
     * @param addr      the new address in RFC822 format
     * @param personal  the new personal name
     * @param type      the type of the address (TO, CC, or BCC)
     *
     * @throws NullPointerException     if either addr or personal is <code>null</code>
     * @throws IllegalArgumentException if type is not TO, CC, nor BCC
     * @throws AddressException         if  1) address is not a valid address according to RFC822
     *                                      2) encoding of the personal is not supported
     */
    public void setToAddress(String addr, String personal, int type) throws AddressException {
        checkNull(addr, "addr");
        checkNull(personal, "personal");

        if (type < 0 || type >= to.length) {
            throw new IllegalArgumentException("Invalid type");
        }

        to[type] = new InternetAddress[0];
        addToAddress(addr, personal, type);
    }

    /**
     * <p>Adds an address to the list of addresses using a shallow copy.</p>
     *
     * @param src   an array of addresses
     * @param add   The feature to be added to the Address attribute
     *
     * @return the new array of addresses
     *
     * @throws NullPointerException if either src or add is <code>null</code>
     */
    private InternetAddress[] addAddress(InternetAddress[] src, InternetAddress add) {
        checkNull(src, "src");
        checkNull(add, "add");

        InternetAddress dst[] = new InternetAddress[src.length + 1];
        for (int i = 0; i < src.length; i++) {
            dst[i] = src[i];
        }
        dst[src.length] = add;
        return dst;
    }

    /**
     * <p>Checks whether the given key is a valid RFC822 header string.
     *
     * @param key  the key to check
     *
     * @throws IllegalArgumentException if the given key is invalid message header key
     */
    private void checkKey(String key) {
        if (key.length() < 1) {
            throw new IllegalArgumentException(key + " is invalid message header key");
        }
        for (int i = 0; i < key.length(); ++i) {
            char c = key.charAt(i);
            if (Character.isISOControl(c) || Character.isWhitespace(c) || c == ':') {
                throw new IllegalArgumentException(key + " is invalid message header key");
            }
        }
    }

    /**
     * <p>Checks whether object is <code>null</code>, <code>NullPointerException</code> is thrown when it is.<p>
     *
     * @param object    the <code>Object</code> to check
     * @param name      the name of the object
     *
     * @throws NullPointerException if object is <code>null</code>
     *
     * @since 3.0
     */
    private void checkNull(Object object, String name) {
        if (object == null) {
            throw new NullPointerException(name + " should not be null");
        }
    }

    /**
     * <p>Creates <code>InternetAddress</code> with the specified address and personal. This method helps this class to
     * create <code>InternetAddress</code>.</p>
     *
     * <p>This method exists because the implementation of version 2.0 did not validate the address when setting the
     * "from" and "to" address with personal, since the constructor InternetAddress(String address, String personal) of
     * InternetAddress assumes the address passed in is a syntactically valid RFC822 address. This method validates the
     * address and returns the created InternetAddress, if the address fails the validation, AddressException will be
     * throw.</p>
     *
     * @param address   the address in RFC822 format
     * @param personal  the personal name
     *
     * @return the created <code>InternetAddress</code>
     *
     * @throws AddressException if  1) address is not a valid address according to RFC822
     *                              2) encoding of the personal is not supported
     */
    private InternetAddress createAddress(String address, String personal) throws AddressException {
        try {
            InternetAddress ret = new InternetAddress(address);
            ret.setPersonal(personal);
            return ret;
        } catch (javax.mail.internet.AddressException e) {
            throw new AddressException("Invalid address: " + address, e);
        } catch (UnsupportedEncodingException e) {
            throw new AddressException("Encoding if not supported for personal: " + personal, e);
        }
    }

    /**
     * <p>Returns a deep copy of the Address list.</p>
     *
     * @param src   an array of addresses
     *
     * @return the new array of addresses
     *
     * @throws NullPointerException if src is <code>null</code>
     */
    private InternetAddress[] dup(InternetAddress[] src) {
        checkNull(src, "src");

        InternetAddress dst[] = new InternetAddress[src.length];
        for (int i = 0; i < dst.length; i++) {
            try {
                dst[i] = new InternetAddress(src[i].getAddress(), src[i].getPersonal());
            } catch (Exception e) {
                dst[i] = src[i];
            }
        }
        return dst;
    }

    /**
     * <p>Loads the headers into priorityHeaders for the specified priority level.</p>
     *
     * <p>It first tries to get the configuration for priority headers under the namespace in ConfigManager, if the
     * namespace does not present, it tries to add it from the default file. Then, it parses the configuration to get
     * the headers. If the configuration file is not present or the property for the specified priority does not exist,
     * the default headers are used instead.</p>
     *
     * @param priorityLevel the specified priority level
     *
     * @throws IllegalArgumentException if the configuration is invalid
     *
     * @since 3.0
     */
    private void loadPriorityHeaders(PriorityLevel priorityLevel) {
        String[] headers = null;
        try {
            ConfigManager configManager = ConfigManager.getInstance();
            if (configManager.existsNamespace(PROPERTIES_NAMESPACE)) {
                configManager.refresh(PROPERTIES_NAMESPACE);
            } else {
                // Try to add the namespace of configuration.
                configManager.add(PROPERTIES_NAMESPACE, PROPERTIES_LOCATION, PROPERTIES_FORMAT);
            }

            // The property for custom priorities.
            Property property = configManager.getPropertyObject(PROPERTIES_NAMESPACE, PRIORITIES_NAME);
            // Headers array of the priority
            headers = property.getValues(priorityLevel.getName());
            if (headers == null) {
                // The property does not present.
                priorityHeaders = (Map) DEFAULT_PRIORITY_HEADERS.get(priorityLevel);
                return;
            }
        } catch (Exception ignore) {
            // Cannot load headers via configuration, use the default instead.
            priorityHeaders = (Map) DEFAULT_PRIORITY_HEADERS.get(priorityLevel);
            return;
        }

        /*
         * Iterate to parse and add the headers. We cannot operate directly on the original map, since configuration may
         * be invalid, which would leave the original one in invalid status.
         */
        Map newHeaders = new HashMap();
        for (int i = 0; i < headers.length; ++i) {
            if (headers[i].length() == 0) {
                // Do not use header for placeholder.
                continue;
            }

            int delim = headers[i].indexOf(':');
            if (delim == -1) {
                throw new IllegalArgumentException("the headers value for priority " + priorityLevel + " is invalid");
            }
            String key = headers[i].substring(0, delim);
            String value = headers[i].substring(delim + 1);
            checkKey(key);
            newHeaders.put(key, value);
        }
        priorityHeaders = newHeaders;
    }

    /**
     * get the contextType of this email message.if not set the default value is "text/plain" will return.
     * @return the contentType of this email message.
     * @since 3.1
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * get the contextType of this email message.
     * @param contentType
     *            the contentType to set
     * @throws IllegalArgumentException
     *             if contextType is null or empty.
     * @since 3.1
     */
    public void setContentType(String contentType) {
        if (contentType == null || contentType.trim().length() == 0) {
            throw new IllegalArgumentException("the contextType should not be null or empty");
        }
        this.contentType = contentType;
    }

}
