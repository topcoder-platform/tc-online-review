/*
 * TCS LDAP SDK Interface 1.0
 *
 * LDAPSDKConnection.java
 *
 * Copyright (c) 2004, TopCoder Inc. All rights reserved.
 */
package com.topcoder.util.net.ldap.sdkinterface;

import java.util.Hashtable;
import java.util.Iterator;

/**
 * <p>As the <em>Abstract Product</em> of the Abstract Factory Pattern, it represents a connection to an LDAP. &nbsp;
 * &nbsp;It has all the operations (connect, search, add entry, etc) that the implementing classes (plugins for the
 * different SDK) must override.</p>
 *
 * @author  cucu
 * @author  isv
 * @version 1.0 05/18/2004
 */
public interface LDAPSDKConnection {

    /**
     * An <code>int</code> representing the <code>BASE OBJECT</code> scope of search within LDAP entry.
     */
    public static final int SCOPE_BASE = 0;

    /**
     * An <code>int</code> representing the <code>ONE LEVEL</code> scope of search within LDAP entry.
     */
    public static final int SCOPE_ONE = 1;

    /**
     * An <code>int</code> representing the <code>WHOLE SUBTREE</code> scope of search within LDAP entry.
     */
    public static final int SCOPE_SUB = 2;

    /**
     * <p>Connect to the LDAP using the host and port indicated </p>
     * <p>It should catch all the ldap exceptions and throw a new exception created using LDAPSDKFactory with the error
     * code and message retreived from the caugth exception.</p>
     * <p>Throw an LDAPSDKException or one of its inherited exceptions: </p>
     * <ul type="disc">
     *     <li>LDAPSDKCommunicationException:this exception is thrown when an operation was unsuccesfull because of an
     *     error in the communication with the server or in the server</li>
     *     <li>LDAPSDKLimitsExceededException:Processing time or maximum entries found in the server exceeded its
     *     limits.</li>
     *     <li>LDAPSDKCallErrorException:The call to an LDAP service was unsuccesfull due to a protocol difference, a
     *     bad parameter or not supported functionality.</li>
     *     <li>LDAPSDKException:An unknow error was found.</li>
     * </ul>
     *
     * @param host host name
     * @param port port number
     * @throws NullPointerException if host is null or an IllegalArgumentException if it is an empty string.
     * @throws IllegalArgumentException if port is negative or greater than 65535.
     */
    public void connect(String host, int port) throws LDAPSDKException;

    /**
     * <p>Disconnects from the LDAP </p>
     * <p>It should catch all the ldap exceptions and throw a new exception created using LDAPSDKFactory with the error
     * code and message retreived from the caugth exception.</p>
     * <p>Throw an LDAPSDKException or one of its inherited exceptions:</p>
     * <ul type="disc">
     *     <li>LDAPSDKCommunicationException:this exception is thrown when an operation was unsuccesfull because of an
     *     error in the communication with the server or in the server</li>
     *     <li>LDAPSDKCallErrorException:The call to an LDAP service was unsuccesfull due to a protocol difference, a
     *     bad parameter or not supported functionality.</li>
     *     <li>LDAPSDKException:An unknow error was found.</li>
     * </ul>
     *
     *
     */
    public void disconnect() throws LDAPSDKException;

    /**
     * <p>Adds an entry to the LDAP </p>
     * <p>Throw an NullPointerException if entry is null</p>
     * <p>It should catch all the ldap exceptions and throw a new exception created using LDAPSDKFactory with the error
     * code and message retreived from the caugth exception.</p>
     * <p>Throw an LDAPSDKException or one of its inherited exceptions:</p>
     * <ul type="disc">
     *     <li>LDAPSDKCommunicationException:this exception is thrown when an operation was unsuccesfull because of an
     *     error in the communication with the server or in the server</li>
     *     <li>LDAPSDKAccesDeniedException: the operation could not be completed due insuficient access rights.</li>
     *     <li>LDAPSDKLimitsExceededException:Processing time or maximum entries found in the server exceeded its
     *     limits.</li>
     *     <li>LDAPSDKSchemaViolationException:The operation could no be completed because it would violate the LDAP
     *     schema.</li>
     *     <li>LDAPSDKInvalidAttributeSyntaxException:The attribute sytnax is invalid</li>
     *     <li>LDAPSDKInvalidDNSyntaxException:The DN sytnax of the Entry is invalid</li>
     *     <li>LDAPSDKCallErrorException:The call to an LDAP service was unsuccesfull due to a protocol difference, a
     *     bad parameter or not supported functionality.</li>
     *     <li>LDAPSDKException:An unknow error was found.</li>
     * </ul>
     *
     * @param entry the entry to be added
     */
    public void addEntry(Entry entry) throws LDAPSDKException;

    /**
     * <p>Deletes an entry from the LDAP </p>
     * <p>Throw an NullPointerException if dn is null or an IllegalArgumentException if it is an empty string</p>
     * <p>It should catch all the ldap exceptions and throw a new exception created using LDAPSDKFactory with the error
     * code and message retreived from the caugth exception.</p>
     * <p>Throw an LDAPSDKException or one of its inherited exceptions:</p>
     * <ul type="disc">
     *     <li>LDAPSDKCommunicationException:this exception is thrown when an operation was unsuccesfull because of an
     *     error in the communication with the server or in the server</li>
     *     <li>LDAPSDKAccesDeniedException: the operation could not be completed due insuficient access rights.</li>
     *     <li>LDAPSDKLimitsExceededException:Processing time or maximum entries found in the server exceeded its
     *     limits.</li>
     *     <li>LDAPSDKSchemaViolationException:The operation could no be completed because it would violate the LDAP
     *     schema.</li>
     *     <li>LDAPSDKNoSuchObjectException:The entry was not found.</li>
     *     <li>LDAPSDKInvalidDNSyntaxException:The DN sytnax is invalid</li>
     *     <li>LDAPSDKCallErrorException:The call to an LDAP service was unsuccesfull due to a protocol difference, a
     *     bad parameter or not supported functionality.</li>
     *     <li>LDAPSDKOperationNotAllowedException:The operation is not allowed in the entry specified. For example, a
     *     non leaf entry can not be deleted</li>
     *     <li>LDAPSDKException:An unknow error was found.</li>
     * </ul>
     *
     * @param dn the DN of the entry to be deleted
     */
    public void deleteEntry(String dn) throws LDAPSDKException;

    /**
     * <p>Renames an entry in the LDAP. The entry at dn will be now at newDN, preserving all his attributes </p>
     * <p>Throw an NullPointerException if dn or newDN ar null or an IllegalArgumentException if one or both parameters
     * are an empty string</p>
     * <p>It should catch all the ldap exceptions and throw a new exception created using LDAPSDKFactory with the error
     * code and message retreived from the caugth exception.</p>
     * <p>Throw an LDAPSDKException or one of its inherited exceptions:</p>
     * <ul type="disc">
     *     <li>LDAPSDKCommunicationException:this exception is thrown when an operation was unsuccesfull because of an
     *     error in the communication with the server or in the server</li>
     *     <li>LDAPSDKAccesDeniedException: the operation could not be completed due insuficient access rights.</li>
     *     <li>LDAPSDKLimitsExceededException:Processing time or maximum entries found in the server exceeded its
     *     limits.</li>
     *     <li>LDAPSDKSchemaViolationException:The operation could no be completed because it would violate the LDAP
     *     schema.</li>
     *     <li>LDAPSDKNoSuchObjectException:The entry was not found.</li>
     *     <li>LDAPSDKInvalidDNSyntaxException:The DN sytnax is invalid for dn or newDN</li>
     *     <li>LDAPSDKCallErrorException:The call to an LDAP service was unsuccesfull due to a protocol difference, a
     *     bad parameter or not supported functionality.</li>
     *     <li>LDAPSDKOperationNotAllowedException:The operation is not allowed in the entry specified.</li>
     *     <li>LDAPSDKException:An unknow error was found.</li>
     * </ul>
     *
     * @param dn the DN of the entry to be renamed
     * @param newDN the new DN for the entry
     * @param deleteOldDN a <code>boolean</code> indicating whether the old name is not retained as an attribute value
     *        or not. If false, the old name is retained as an attribute value (for example, the entry might now have
     *        two values for the cn attribute: "cn=oldName" and "cn=newName").
     */
    public void renameEntry(String dn, String newDN, boolean deleteOldDN) throws LDAPSDKException;

    /**
     * <p>Update the entry in the Distinguished Name specified with the modifications provided in the update field </p>
     * <p>Throw an NullPointerException if dn is null or an IllegalArgumentException if it is an empty string</p>
     * <p>Throw an NullPointerException if update is null </p>
     * <p>It should catch all the ldap exceptions and throw a new exception created using LDAPSDKFactory with the error
     * code and message retreived from the caugth exception.</p>
     * <p>Throw an LDAPSDKException or one of its inherited exceptions:</p>
     * <ul type="disc">
     *     <li>LDAPSDKCommunicationException:this exception is thrown when an operation was unsuccesfull because of an
     *     error in the communication with the server or in the server</li>
     *     <li>LDAPSDKAccesDeniedException: the operation could not be completed due insuficient access rights.</li>
     *     <li>LDAPSDKLimitsExceededException:Processing time or maximum entries found in the server exceeded its
     *     limits.</li>
     *     <li>LDAPSDKSchemaViolationException:The operation could no be completed because it would violate the LDAP
     *     schema.</li>
     *     <li>LDAPSDKNoSuchObjectException:The entry to be updated was not found.</li>
     *     <li>LDAPSDKInvalidDNSyntaxException:The DN sytnax is invalid</li>
     *     <li>LDAPSDKCallErrorException:The call to an LDAP service was unsuccesfull due to a protocol difference, a
     *     bad parameter or not supported functionality.</li>
     *     <li>LDAPSDKException:An unknow error was found.</li>
     * </ul>
     *
     * @param dn the DN of the entry to be updated
     * @param update the changes to be applied
     */
    public void updateEntry(String dn, Update update) throws LDAPSDKException;

    /**
     * <p>Read an entry from the LDAP and retreive all the attributes </p>
     * <p>Throw an NullPointerException if dn is null or an IllegalArgumentException if it is an empty string</p>
     * <p>It should catch all the ldap exceptions and throw a new exception created using LDAPSDKFactory with the error
     * code and message retreived from the caugth exception.</p>
     * <p>Throw an LDAPSDKException or one of its inherited exceptions:</p>
     * <ul type="disc">
     *     <li>LDAPSDKCommunicationException:this exception is thrown when an operation was unsuccesfull because of an
     *     error in the communication with the server or in the server</li>
     *     <li>LDAPSDKAccesDeniedException: the operation could not be completed due insuficient access rights.</li>
     *     <li>LDAPSDKLimitsExceededException:Processing time or maximum entries found in the server exceeded its
     *     limits.</li>
     *     <li>LDAPSDKNoSuchObjectException:The entry dn was not found.</li>
     *     <li>LDAPSDKInvalidDNSyntaxException:The DN sytnax is invalid</li>
     *     <li>LDAPSDKCallErrorException:The call to an LDAP service was unsuccesfull due to a protocol difference, a
     *     bad parameter or not supported functionality.</li>
     *     <li>LDAPSDKException:An unknow error was found.</li>
     * </ul>
     *
     * @param dn the DN of the entry to be read
     * @return the entry read
     */
    public Entry readEntry(String dn) throws LDAPSDKException;

    /**
     * <p>Read an entry from the LDAP and retreive the specified attributes </p>
     * <p>Throw an NullPointerException if dn or attrs are null or an IllegalArgumentException if dn is an empty
     * string.</p>
     * <p>It should catch all the ldap exceptions and throw a new exception created using LDAPSDKFactory with the error
     * code and message retreived from the caugth exception.</p>
     * <p>Throw an LDAPSDKException or one of its inherited exceptions:</p>
     * <ul type="disc">
     *     <li>LDAPSDKCommunicationException:this exception is thrown when an operation was unsuccesfull because of an
     *     error in the communication with the server or in the server</li>
     *     <li>LDAPSDKAccesDeniedException: the operation could not be completed due insuficient access rights.</li>
     *     <li>LDAPSDKLimitsExceededException:Processing time or maximum entries found in the server exceeded its
     *     limits.</li>
     *     <li>LDAPSDKNoSuchAttributeException:The attribute was not found.</li>
     *     <li>LDAPSDKNoSuchObjectException:The entry was not found.</li>
     *     <li>LDAPSDKInvalidAttributeSyntaxException:The attribute sytnax is invalid</li>
     *     <li>LDAPSDKInvalidDNSyntaxException:The DN sytnax is invalid</li>
     *     <li>LDAPSDKCallErrorException:The call to an LDAP service was unsuccesfull due to a protocol difference, a
     *     bad parameter or not supported functionality.</li>
     *     <li>LDAPSDKException:An unknow error was found.</li>
     * </ul>
     *
     * @param dn the DN of the entry to be read
     * @param attrs the attributes to be retreived
     * @return the entry read
     */
    public Entry readEntry(String dn, String[] attrs) throws LDAPSDKException;

    /**
     * <p>Search the LDAP for Entries. Many entries can be found, and they can be retrieved iterating through the
     * Iterator returned. The scope must be one of:</p>
     * <ul type="disc">
     *     <li>SCOPE_BASE. Only the entry specified as the search base is include in the search. This is used when you
     *     already know the DN of the object and you would like to read its attributes. (The read method may also be
     *     used to read the values of a single entry).</li>
     *     <li>SCOPE_ONE. Objects one level below the base (but not including the base) are included in the search.</li>
     *     <li>SCOPE_SUB. All objects below the base, including the base itself, are included in the search.</li>
     * </ul>
     * <p>The filter string must have the format specified in rfc2254 (http://www.faqs.org/rfcs/rfc2254.html) <br/>
     * For example, some posible filters are: </p>
     * <ul type="disc">
     *     <li>(cn=Babs Jensen)</li>
     *     <li>(!(cn=Tim Howes))</li>
     *     <li>(&amp;(objectClass=Person)(|(sn=Jensen)(cn=Babs J*)))</li>
     *     <li>(o=univ*of*mich*)</li>
     * </ul>
     * <p>This method throws a NullPointerException if base or filter are null or an IllegalArgumentException if one or
     * both strings are empty or the scope is not one of the described above.</p>
     * <p>It should catch all the ldap exceptions and throw a new exception created using LDAPSDKFactory with the error
     * code and message retreived from the caugth exception.</p>
     * <p>Throw an LDAPSDKException or one of its inherited exceptions:</p>
     * <ul type="disc">
     *     <li>LDAPSDKCommunicationException:this exception is thrown when an operation was unsuccesfull because of an
     *     error in the communication with the server or in the server</li>
     *     <li>LDAPSDKAccesDeniedException: the operation could not be completed due insuficient access rights.</li>
     *     <li>LDAPSDKLimitsExceededException:Processing time or maximum entries found in the server exceeded its
     *     limits.</li>
     *     <li>LDAPSDKInvalidFilterException:The filter for the search is invalid.</li>
     *     <li>LDAPSDKInvalidDNSyntaxException:The DN sytnax for the base is invalid</li>
     *     <li>LDAPSDKCallErrorException:The call to an LDAP service was unsuccesfull due to a protocol difference, a
     *     bad parameter or not supported functionality.</li>
     *     <li>LDAPSDKException:An unknow error was found.</li>
     * </ul>
     *
     * @param base the base DN to search
     * @param scope scope of the search.
     * @param filter filter for the search
     * @return an Iterator to a collection of Entry objects
     * @throws IllegalArgumentException if the scope is not one of the constants defined for this purpouse
     */
    public Iterator search(String base, int scope, String filter) throws LDAPSDKException;

    /**
     * <p>Search the LDAP for Entries, and retrieve the specified attributes for each matching Entry. Many entries can
     * be found, and they can be retrieved iterating through the Iterator returned. </p>
     * <p>The scope must be one of:</p>
     * <ul type="disc">
     *     <li>SCOPE_BASE. Only the entry specified as the search base is include in the search. This is used when you
     *     already know the DN of the object and you would like to read its attributes. (The read method may also be
     *     used to read the values of a single entry).</li>
     *     <li>SCOPE_ONE. Objects one level below the base (but not including the base) are included in the search.</li>
     *     <li>SCOPE_SUB. All objects below the base, including the base itself, are included in the search.</li>
     * </ul>
     * <p>The filter string must have the format specified in rfc2254 (http://www.faqs.org/rfcs/rfc2254.html)<br>
     * For example, some posible filters are: </p>
     * <ul type="disc">
     *     <li>(cn=Babs Jensen)</li>
     *     <li>(!(cn=Tim Howes))</li>
     *     <li>(&amp;(objectClass=Person)(|(sn=Jensen)(cn=Babs J*)))</li>
     *     <li>(o=univ*of*mich*)</li>
     * </ul>
     * <p>This method throws a NullPointerException if base or filter are null or an IllegalArgumentException if one or
     * both strings are empty or the scope is not one of the described above.</p>
     * <p>It should catch all the ldap exceptions and throw a new exception created using LDAPSDKFactory with the error
     * code and message retreived from the caugth exception.</p>
     * <p>Throw an LDAPSDKException or one of its inherited exceptions:</p>
     * <ul type="disc">
     *     <li>LDAPSDKCommunicationException:this exception is thrown when an operation was unsuccesfull because of an
     *     error in the communication with the server or in the server</li>
     *     <li>LDAPSDKAccesDeniedException: the operation could not be completed due insuficient access rights.</li>
     *     <li>LDAPSDKLimitsExceededException:Processing time or maximum entries found in the server exceeded its
     *     limits.</li>
     *     <li>LDAPSDKNoSuchAttributeException:One of the attribute specified was not found.</li>
     *     <li>LDAPSDKInvalidFilterException:The filter for the search is invalid.</li>
     *     <li>LDAPSDKInvalidDNSyntaxException:The DN sytnax for the base is invalid</li>
     *     <li>LDAPSDKCallErrorException:The call to an LDAP service was unsuccesfull due to a protocol difference, a
     *     bad parameter or not supported functionality.</li>
     *     <li>LDAPSDKInvalidAttributeSyntaxException:The attribute syntax of one of at least one of the attributes is
     *     invalid </li>
     *     <li>LDAPSDKException:An unknow error was found.</li>
     * </ul>
     *
     * @param base the base DN to search
     * @param scope scope of the search
     * @param filter filter for the search
     * @param attrs the attributes to be retreived
     * @return an Iterator to a collection of Entry objects
     * @throws IllegalArgumentException if the scope is not one of the constants defined for this purpouse
     */
    public Iterator search(String base, int scope, String filter, String[] attrs) throws LDAPSDKException;

    /**
     * <p>Authenticate to the LDAP server using the specified dn and password</p>
     * <p>Throw an NullPointerException if dn or password are null or an IllegalArgumentException if one or both are
     * empty strings</p>
     * <p>It should catch all the ldap exceptions and throw a new exception created using LDAPSDKFactory with the error
     * code and message retreived from the caugth exception.</p>
     * <p>Throw an LDAPSDKException or one of its inherited exceptions:</p>
     * <ul type="disc">
     *     <li>LDAPSDKCommunicationException:this exception is thrown when an operation was unsuccesfull because of an
     *     error in the communication with the server or in the server</li>
     *     <li>LDAPSDKAuthenticationMethodException:The server is expecting a different authentication method or there
     *     is an error in the way of authenticating.</li>
     *     <li>LDAPSDKAccesDeniedException: the operation could not be completed due insuficient access rights.</li>
     *     <li>LDAPSDKLimitsExceededException:Processing time or maximum entries found in the server exceeded its
     *     limits.</li>
     *     <li>LDAPSDKInvalidDNSyntaxException:The DN sytnax is invalid</li>
     *     <li>LDAPSDKCallErrorException:The call to an LDAP service was unsuccesfull due to a protocol difference, a
     *     bad parameter or not supported functionality.</li>
     *     <li>LDAPSDKException:An unknow error was found.</li>
     * </ul>
     *
     * @param dn the DN to be authenticated with
     * @param password the password for authenticating to that DN
     */
    public void authenticate(String dn, String password) throws LDAPSDKException;

    /**
     * <p>Authenticate to the LDAP server using the specified version, dn and password</p>
     * <p>Throw an NullPointerException if dn or password are null or an IllegalArgumentException if one or both are
     * empty strings</p>
     * <p>It should catch all the ldap exceptions and throw a new exception created using LDAPSDKFactory with the error
     * code and message retreived from the caugth exception.</p>
     * <p>Throw an LDAPSDKException or one of its inherited exceptions:</p>
     * <ul type="disc">
     *     <li>LDAPSDKCommunicationException:this exception is thrown when an operation was unsuccesfull because of an
     *     error in the communication with the server or in the server</li>
     *     <li>LDAPSDKAuthenticationMethodException:The server is expecting a different authentication method or there
     *     is an error in the way of authenticating.</li>
     *     <li>LDAPSDKAccesDeniedException: the operation could not be completed due insuficient access rights.</li>
     *     <li>LDAPSDKLimitsExceededException:Processing time or maximum entries found in the server exceeded its
     *     limits.</li>
     *     <li>LDAPSDKInvalidDNSyntaxException:The DN sytnax is invalid</li>
     *     <li>LDAPSDKCallErrorException:The call to an LDAP service was unsuccesfull due to a protocol difference, a
     *     bad parameter or not supported functionality.</li>
     *     <li>LDAPSDKException:An unknow error was found.</li>
     * </ul>
     *
     * @param  version the LDAP protocol version
     * @param dn the DN to be authenticated with
     * @param password the password for authenticating to that DN
     */
    public void authenticate(int version, String dn, String password) throws LDAPSDKException;

    /**
     * <p>Authenticate as anonymous to the LDAP server </p>
     * <p>It should catch all the ldap exceptions and throw a new exception created using LDAPSDKFactory with the error
     * code and message retreived from the caugth exception.</p>
     * <p>Throw an LDAPSDKException or one of its inherited exceptions:</p>
     * <ul type="disc">
     *     <li>LDAPSDKCommunicationException:this exception is thrown when an operation was unsuccesfull because of an
     *     error in the communication with the server or in the server</li>
     *     <li>LDAPSDKAuthenticationMethodException:The server is expecting a different authentication method or there
     *     is an error in the way of authenticating.</li>
     *     <li>LDAPSDKAccesDeniedException: the operation could not be completed due insuficient access rights.</li>
     *     <li>LDAPSDKLimitsExceededException:Processing time or maximum entries found in the server exceeded its
     *     limits.</li>
     *     <li>LDAPSDKCallErrorException:The call to an LDAP service was unsuccesfull due to a protocol difference,
     *     a bad parameter or not supported functionality.</li>
     *     <li>LDAPSDKException:An unknow error was found.</li>
     * </ul>
     *
     *
     */
    public void authenticateAnonymous() throws LDAPSDKException;


    /**
     * <p>Authenticate as anonymous to the LDAP server using the specified version.</p>
     * <p>It should catch all the ldap exceptions and throw a new exception created using LDAPSDKFactory with the error
     * code and message retreived from the caugth exception.</p>
     * <p>Throw an LDAPSDKException or one of its inherited exceptions:</p>
     * <ul type="disc">
     *     <li>LDAPSDKCommunicationException:this exception is thrown when an operation was unsuccesfull because of an
     *     error in the communication with the server or in the server</li>
     *     <li>LDAPSDKAuthenticationMethodException:The server is expecting a different authentication method or there
     *     is an error in the way of authenticating.</li>
     *     <li>LDAPSDKAccesDeniedException: the operation could not be completed due insuficient access rights.</li>
     *     <li>LDAPSDKLimitsExceededException:Processing time or maximum entries found in the server exceeded its
     *     limits.</li>
     *     <li>LDAPSDKCallErrorException:The call to an LDAP service was unsuccesfull due to a protocol difference,
     *     a bad parameter or not supported functionality.</li>
     *     <li>LDAPSDKException:An unknow error was found.</li>
     * </ul>
     *
     * @param  version the LDAP protocol version
     */
    public void authenticateAnonymous(int version) throws LDAPSDKException;


    /**
     * <p>Authenticate to the LDAP server using the specified dn, the configuration properties in the props hashtable
     * and the callback object cbh</p>
     * <p>Throw an NullPointerException if dn, props or cbh are null or an IllegalArgumentException if dn is an empty
     * strings</p>
     * <p>It should catch all the ldap exceptions and throw a new exception created using LDAPSDKFactory with the error
     * code and message retreived from the caugth exception.</p>
     * <p>Throw an LDAPSDKException or one of its inherited exceptions:</p>
     * <ul type="disc">
     *     <li>LDAPSDKCommunicationException:this exception is thrown when an operation was unsuccesfull because of an
     *     error in the communication with the server or in the server</li>
     *     <li>LDAPSDKAuthenticationMethodException:The server is expecting a different authentication method or there
     *     is an error in the way of authenticating.</li>
     *     <li>LDAPSDKAccesDeniedException: the operation could not be completed due insuficient access rights.</li>
     *     <li>LDAPSDKLimitsExceededException:Processing time or maximum entries found in the server exceeded its
     *     limits.</li>
     *     <li>LDAPSDKInvalidDNSyntaxException:The DN sytnax is invalid</li>
     *     <li>LDAPSDKCallErrorException:The call to an LDAP service was unsuccesfull due to a protocol difference, a
     *     bad parameter or not supported functionality.</li>
     *     <li>LDAPSDKException:An unknow error was found.</li>
     * </ul>
     *
     * @param dn the DN to be authenticated with
     * @param mechanisms the SASL mechanisms to be used
     * @param props properties for the SASL
     * @param cbh a class which the SASL framework can call to obtain additional required information
     */
    public void authenticateSASL(String dn, String[] mechanisms, Hashtable props, Object cbh) throws LDAPSDKException;

    /**
     * <p>Bind to the LDAP server using the specified dn and password</p>
     * <p>Throw an NullPointerException if dn or password are null or an IllegalArgumentException if one or both are
     * empty strings</p>
     * <p>It should catch all the ldap exceptions and throw a new exception created using LDAPSDKFactory with the error
     * code and message retreived from the caugth exception.</p>
     * <p>Throw an LDAPSDKException or one of its inherited exceptions:</p>
     * <ul type="disc">
     *     <li>LDAPSDKCommunicationException:this exception is thrown when an operation was unsuccesfull because of an
     *     error in the communication with the server or in the server</li>
     *     <li>LDAPSDKAuthenticationMethodException:The server is expecting a different authentication method or there
     *     is an error in the way of authenticating.</li>
     *     <li>LDAPSDKAccesDeniedException: the operation could not be completed due insuficient access rights.</li>
     *     <li>LDAPSDKLimitsExceededException:Processing time or maximum entries found in the server exceeded its
     *     limits.</li>
     *     <li>LDAPSDKInvalidDNSyntaxException:The DN sytnax is invalid</li>
     *     <li>LDAPSDKCallErrorException:The call to an LDAP service was unsuccesfull due to a protocol difference, a
     *     bad parameter or not supported functionality.</li>
     *     <li>LDAPSDKException:An unknow error was found.</li>
     * </ul>
     *
     * @param dn the DN to be authenticated with
     * @param password the password for authenticating to that DN
     */
    public void bind(String dn, String password) throws LDAPSDKException;

    /**
     * <p>Bind as anonymous to the LDAP server </p>
     * <p>It should catch all the ldap exceptions and throw a new exception created using LDAPSDKFactory with the error
     * code and message retreived from the caugth exception.</p>
     * <p>Throw an LDAPSDKException or one of its inherited exceptions:</p>
     * <ul type="disc">
     *     <li>LDAPSDKCommunicationException:this exception is thrown when an operation was unsuccesfull because of an
     *     error in the communication with the server or in the server</li>
     *     <li>LDAPSDKAuthenticationMethodException:The server is expecting a different authentication method or there
     *     is an error in the way of authenticating.</li>
     *     <li>LDAPSDKAccesDeniedException: the operation could not be completed due insuficient access rights.</li>
     *     <li>LDAPSDKLimitsExceededException:Processing time or maximum entries found in the server exceeded its
     *     limits.</li>
     *     <li>LDAPSDKCallErrorException:The call to an LDAP service was unsuccesfull due to a protocol difference, a
     *     bad parameter or not supported functionality.</li>
     *     <li>LDAPSDKException:An unknow error was found.</li>
     * </ul>
     */
    public void bindAnonymous() throws LDAPSDKException;

    /**
     * <p>Bind to the LDAP server using the specified dn, the configuration properties in the props hashtable and the
     * callback object cbh</p>
     * <p>Throw an NullPointerException if dn, props or cbh are null or an IllegalArgumentException if dn is an empty
     * strings</p>
     * <p>It should catch all the ldap exceptions and throw a new exception created using LDAPSDKFactory with the error
     * code and message retreived from the caugth exception.</p>
     * <p>Throw an LDAPSDKException or one of its inherited exceptions:</p>
     * <ul type="disc">
     *     <li>LDAPSDKCommunicationException:this exception is thrown when an operation was unsuccesfull because of an
     *     error in the communication with the server or in the server</li>
     *     <li>LDAPSDKAuthenticationMethodException:The server is expecting a different authentication method or there
     *     is an error in the way of authenticating.</li>
     *     <li>LDAPSDKAccesDeniedException: the operation could not be completed due insuficient access rights.</li>
     *     <li>LDAPSDKLimitsExceededException:Processing time or maximum entries found in the server exceeded its
     *     limits.</li>
     *     <li>LDAPSDKInvalidDNSyntaxException:The DN sytnax is invalid</li>
     *     <li>LDAPSDKCallErrorException:The call to an LDAP service was unsuccesfull due to a protocol difference, a
     *     bad parameter or not supported functionality.</li>
     *     <li>LDAPSDKException:An unknow error was found.</li>
     * </ul>
     *
     * @param dn the DN to be authenticated with
     * @param mechanisms the SASL mechanisms to be used
     * @param props properties for the SASL
     * @param cbh a class which the SASL framework can call to obtain additional required information
     */
    public void bindSASL(String dn, String[] mechanisms, Hashtable props, Object cbh) throws LDAPSDKException;
}
