/*
 * TCS LDAP SDK Interface 1.0
 *
 * LDAPSDKSchemaViolationExcpetion.java
 *
 * Copyright (c) 2004, TopCoder Inc. All rights reserved.
 */
package com.topcoder.util.net.ldap.sdkinterface;

/**
 * <p>This exception is thrown when the operation could no be completed because it would violate the LDAP schema.</p>
 * <p>It encapsulates the following LDAP result codes: </p>
 * <ul type="disc">
 *     <li>(17) UNDEFINED_ATTRIBUTE_TYPE: Indicates that the attribute specified in the modify or add operation does
 *     not exist in the LDAP server's schema</li>
 *     <li>(19) CONSTRAINT_VIOLATION: An attribute value specified or an operation started violates some server-side
 *     constraint </li>
 *     <li>(20) ATTRIBUTE_OR_VALUE_EXISTS: The value that you are adding to an attribute already exists in the
 *     attribute.</li>
 *     <li>(64) NAMING_VIOLATION: Indicates that the add or modify DN operation violates the schema's structure rules.
 *     </li>
 *     <li>(65) OBJECT_CLASS_VIOLATION: Indicates that the add, modify, or modify DN operation violates the object
 *     class rules for the entry. </li>
 *     <li>(67) NOT_ALLOWED_ON_RDN: Indicates that the modify operation attempted to remove an attribute value that
 *     forms the entry's relative distinguished name.</li>
 *     <li>(68) ENTRY_ALREADY_EXISTS: Indicates that the add operation attempted to add an entry that already exists,
 *     or that the modify operation attempted to rename an entry to the name of an entry that already exists.</li>
 * </ul>
 *
 * @author  cucu
 * @author  isv
 * @version 1.0 05/18/2004
 */
public class LDAPSDKSchemaViolationException extends LDAPSDKException {

    /**
     * Constructs new <code>LDAPSDKSchemaViolationException</code> with specified message and error code.
     *
     * @param message the error message.
     * @param code result code from the LDAP specification.
     */
    public LDAPSDKSchemaViolationException(String message, int code) {
        super(message, code);
    }

    /**
     * Constructs new <code>LDAPSDKSchemaViolationException</code> with specified message, error code and an original
     * exception thrown by the underlying LDAP implementation.
     *
     * @param message the error message.
     * @param code result code from the LDAP specification.
     * @param cause the exception that was caugth before throwing this one.
     */
    public LDAPSDKSchemaViolationException(String message, int code, Throwable cause) {
        super(message, code, cause);
    }
}
