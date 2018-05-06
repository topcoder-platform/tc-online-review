/*
 * TCS LDAP SDK Interface 1.0
 *
 * LDAPSDKExceptionFactory.java
 *
 * Copyright (c) 2004, TopCoder Inc. All rights reserved.
 */
package com.topcoder.util.net.ldap.sdkinterface;

/**
 * This class has two statics members (is a kind of singleton), that create Exceptions of diferent kind depending on the
 * code received.</p>
 * <p>Both methods are similar, being the only difference the cause parameter that only one of them receives.</p>
 * <p>It must be used when a LDAP exception is caugth (e.g. in methods in NetscapeConnection) to create the Exception
 * that matches the error code.
 *
 * @author  cucu
 * @author  isv
 * @version 1.0 05/18/2004
 */
public final class LDAPSDKExceptionFactory {

    /**
     * An <code>int</code> containing the error code corresponding to
     */
    private final static int OPERATION_ERROR = 1;

    /**
     * An <code>int</code> containing the error code corresponding to
     */
    private final static int BUSY = 51;

    /**
     * An <code>int</code> containing the error code corresponding to
     */
    private final static int UNAVAILABLE = 52;

    /**
     * An <code>int</code> containing the error code corresponding to
     */
    private final static int UNWILLING_TO_PERFORM = 53;

    /**
     * An <code>int</code> containing the error code corresponding to
     */
    private final static int SERVER_DOWN = 81;

    /**
     * An <code>int</code> containing the error code corresponding to
     */
    private final static int CONNECT_ERROR = 91;

    /**
     * An <code>int</code> containing the error code corresponding to
     */
    private final static int AUTH_METHOD_NOT_SUPPORTED = 7;

    /**
     * An <code>int</code> containing the error code corresponding to
     */
    private final static int STRONG_AUTH_REQUIRED = 8;

    /**
     * An <code>int</code> containing the error code corresponding to
     */
    private final static int CONFIDENTIALITY_REQUIRED = 13;

    /**
     * An <code>int</code> containing the error code corresponding to
     */
    private final static int SASL_BIND_IN_PROGRESS = 14;

    /**
     * An <code>int</code> containing the error code corresponding to
     */
    private final static int INAPPROPRIATE_AUTHENTICATION = 48;

    /**
     * An <code>int</code> containing the error code corresponding to
     */
    private final static int INVALID_CREDENTIALS = 49;

    /**
     * An <code>int</code> containing the error code corresponding to
     */
    private final static int INSUFFICIENT_ACCESS_RIGHTS = 50;

    /**
     * An <code>int</code> containing the error code corresponding to
     */
    private final static int TIME_LIMIT_EXCEEDED = 3;

    /**
     * An <code>int</code> containing the error code corresponding to
     */
    private final static int SIZE_LIMIT_EXCEEDED = 4;

    /**
     * An <code>int</code> containing the error code corresponding to
     */
    private final static int ADMIN_LIMIT_EXCEEDED = 11;

    /**
     * An <code>int</code> containing the error code corresponding to
     */
    private final static int LDAP_TIMEOUT = 85;

    /**
     * An <code>int</code> containing the error code corresponding to
     */
    private final static int UNDEFINED_ATTRIBUTE_TYPE = 17;

    /**
     * An <code>int</code> containing the error code corresponding to
     */
    private final static int CONSTRAINT_VIOLATION = 19;

    /**
     * An <code>int</code> containing the error code corresponding to
     */
    private final static int ATTRIBUTE_OR_VALUE_EXISTS = 20;

    /**
     * An <code>int</code> containing the error code corresponding to
     */
    private final static int NAMING_VIOLATION = 64;

    /**
     * An <code>int</code> containing the error code corresponding to
     */
    private final static int OBJECT_CLASS_VIOLATION = 65;

    /**
     * An <code>int</code> containing the error code corresponding to
     */
    private final static int NOT_ALLOWED_ON_RDN = 67;

    /**
     * An <code>int</code> containing the error code corresponding to
     */
    private final static int ENTRY_ALREADY_EXISTS = 68;

    /**
     * An <code>int</code> containing the error code corresponding to
     */
    private final static int INAPPROPRIATE_MATCHING = 18;

    /**
     * An <code>int</code> containing the error code corresponding to
     */
    private final static int NO_SUCH_ATTRIBUTE = 16;

    /**
     * An <code>int</code> containing the error code corresponding to
     */
    private final static int NO_SUCH_OBJECT = 32;

    /**
     * An <code>int</code> containing the error code corresponding to
     */
    private final static int INVALID_ATTRIBUTE_SYNTAX = 21;

    /**
     * An <code>int</code> containing the error code corresponding to
     */
    private final static int INVALID_DN_SYNTAX = 34;

    /**
     * An <code>int</code> containing the error code corresponding to
     */
    private final static int PROTOCOL_ERROR = 2;

    /**
     * An <code>int</code> containing the error code corresponding to
     */
    private final static int PARAM_ERROR = 89;

    /**
     * An <code>int</code> containing the error code corresponding to
     */
    private final static int LDAP_NOT_SUPPORTED = 92;

    /**
     * An <code>int</code> containing the error code corresponding to
     */
    private final static int IS_LEAF = 35;

    /**
     * An <code>int</code> containing the error code corresponding to
     */
    private final static int NOT_ALLOWED_ON_NONLEAF = 66;

    /**
     * An <code>int</code> containing the error code corresponding to
     */
    private final static int OTHER = 80;

    /**
     * This method must instantiate and return an object from LDAPSDKException or from a descendent class based on the
     * code. The message and code received in this method will be passed as parameters in the constructor. </p>
     * <p>The exception that must be thrown for each code can be seen in the other method from this class (not copied
     * here for saving some space).
     *
     * @param message the error message.
     * @param code result code from the LDAP specification.
     * @return an Exception object of a type depending on the error code.
     */
    public static final LDAPSDKException createException(String message, int code) {
        return createException(message, code, null);
    }

    /**
     * <p>This method must instantiate and return an object from LDAPSDKException or from a descendent class based on the code. The message, code and cause received in this method will be passed as parameters in the constructor. </p>
     * <p>The exception that must be thrown for each code is detailed next: </p>
     * <p><strong>LDAPSDKCommunicationException</strong></p>
     * <ul type="disc">
     *     <li>(1) OPERATION_ERROR</li>
     *     <li>(51) BUSY</li>
     *     <li>(52) UNAVAILABLE</li>
     *     <li>(53) UNWILLING_TO_PERFORM</li>
     *     <li>(81) SERVER_DOWN</li>
     *     <li>(91) CONNECT_ERROR</li>
     * </ul>
     * <p><strong>LDAPSDKAuthenticationMethodException</strong></p>
     * <ul type="disc">
     *     <li>(7) AUTH_METHOD_NOT_SUPPORTED</li>
     *     <li>(8) STRONG_AUTH_REQUIRED</li>
     *     <li>(13) CONFIDENTIALITY_REQUIRED</li>
     *     <li>(14) SASL_BIND_IN_PROGRESS</li>
     *     <li>(48) INAPPROPRIATE_AUTHENTICATION</li>
     * </ul>
     * <p><strong>LDAPSDKAccesDeniedException</strong></p>
     * <ul type="disc">
     *     <li>(49) INVALID_CREDENTIALS</li>
     *     <li>(50) INSUFFICIENT_ACCESS_RIGHTS</li>
     * </ul>
     * <p><strong>LDAPSDKLimitsExceededException</strong></p>
     * <ul type="disc">
     *     <li>(3) TIME_LIMIT_EXCEEDED</li>
     *     <li>(4) SIZE_LIMIT_EXCEEDED</li>
     *     <li>(11) ADMIN_LIMIT_EXCEEDED</li>
     *     <li>(85) LDAP_TIMEOUT</li>
     * </ul>
     * <p><strong>LDAPSDKSchemaViolationException</strong></p>
     * <ul type="disc">
     *     <li>(17) UNDEFINED_ATTRIBUTE_TYPE</li>
     *     <li>(19) CONSTRAINT_VIOLATION</li>
     *     <li>(20) ATTRIBUTE_OR_VALUE_EXISTS</li>
     *     <li>(64) NAMING_VIOLATION</li>
     *     <li>(65) OBJECT_CLASS_VIOLATION</li>
     *     <li>(67) NOT_ALLOWED_ON_RDN</li>
     *     <li>(68) ENTRY_ALREADY_EXISTS</li>
     * </ul>
     * <p><strong>LDAPSDKInvalidFilterException</strong></p>
     * <ul type="disc">
     *     <li>(18) INAPPROPRIATE_MATCHING</li>
     * </ul>
     * <p><strong>LDAPSDKNoSuchAttributeException</strong></p>
     * <ul type="disc">
     *     <li>(16) NO_SUCH_ATTRIBUTE</li>
     * </ul>
     * <p><strong>LDAPSDKNoSuchObjectException</strong></p>
     * <ul type="disc">
     *     <li>(32) NO_SUCH_OBJECT</li>
     * </ul>
     * <p><strong>LDAPSDKInvalidAttributeSyntaxException</strong></p>
     * <ul type="disc">
     *     <li>(21) INVALID_ATTRIBUTE_SYNTAX</li>
     * </ul>
     * <p><strong>LDAPSDKInvalidDNSyntaxException</strong></p>
     * <ul type="disc">
     *     <li>(34) INVALID_DN_SYNTAX</li>
     * </ul>
     * <p><strong>LDAPSDKCallErrorException</strong></p>
     * <ul type="disc">
     *     <li>(2) PROTOCOL_ERROR</li>
     *     <li>(89) PARAM_ERROR</li>
     *     <li>(92) LDAP_NOT_SUPPORTED</li>
     * </ul>
     * <p><strong>LDAPSDKOperationNotAllowedException</strong></p>
     * <ul type="disc">
     *     <li>(35) IS_LEAF</li>
     *     <li>(66) NOT_ALLOWED_ON_NONLEAF</li>
     * </ul>
     * <p><strong>LDAPSDKException</strong></p>
     * <ul type="disc">
     *     <li>(80) OTHER</li>
     *     <li>Any other number that wasn't in any list</li>
     * </ul>
     *
     * @param message the error message
     * @param code result code from the LDAP specification
     * @param cause the exception that was caugth before throwing this one
     * @return an Exception object of a type depending on the error code
     */
    public static final LDAPSDKException createException(String message, int code, Throwable cause) {
        LDAPSDKException exception = null;

        switch (code) {
            // creating LDAPSDKCommunicationException
            case (OPERATION_ERROR):
            case (BUSY):
            case (UNAVAILABLE):
            case (SERVER_DOWN):
            case (CONNECT_ERROR):
            case (UNWILLING_TO_PERFORM):
                {
                    exception = new LDAPSDKCommunicationException(message, code, cause);
                    break;
                }
            // creating LDAPSKAuthenticationMethodException
            case (AUTH_METHOD_NOT_SUPPORTED):
            case (STRONG_AUTH_REQUIRED):
            case (CONFIDENTIALITY_REQUIRED):
            case (SASL_BIND_IN_PROGRESS):
            case (INAPPROPRIATE_AUTHENTICATION):
                {
                    exception = new LDAPSDKAuthenticationMethodException(message, code, cause);
                    break;
                }
            // creating LDAPSDKAccessDeniedException
            case (INVALID_CREDENTIALS):
            case (INSUFFICIENT_ACCESS_RIGHTS):
                {
                    exception = new LDAPSDKAccessDeniedException(message, code, cause);
                    break;
                }
            // creating LDAPSDKLimitsExceededException
            case (TIME_LIMIT_EXCEEDED):
            case (SIZE_LIMIT_EXCEEDED):
            case (ADMIN_LIMIT_EXCEEDED):
            case (LDAP_TIMEOUT):
                {
                    exception = new LDAPSDKLimitsExceededException(message, code, cause);
                    break;
                }
            // creating LDSPSDKSchemaViolationException
            case (UNDEFINED_ATTRIBUTE_TYPE):
            case (CONSTRAINT_VIOLATION):
            case (ATTRIBUTE_OR_VALUE_EXISTS):
            case (NAMING_VIOLATION):
            case (OBJECT_CLASS_VIOLATION):
            case (NOT_ALLOWED_ON_RDN):
            case (ENTRY_ALREADY_EXISTS):
                {
                    exception = new LDAPSDKSchemaViolationException(message, code, cause);
                    break;
                }
            // creating LDSPSDKInvalidFilterException
            case (INAPPROPRIATE_MATCHING):
                {
                    exception = new LDAPSDKInvalidFilterException(message, code, cause);
                    break;
                }
            // creating LDSPSDKNoSuchAttributeExcpetion
            case (NO_SUCH_ATTRIBUTE):
                {
                    exception = new LDAPSDKNoSuchAttributeException(message, code, cause);
                    break;
                }
            // creating LDSPSDKNoSuchAttributeExcpetion
            case (NO_SUCH_OBJECT):
                {
                    exception = new LDAPSDKNoSuchObjectException(message, code, cause);
                    break;
                }
            // creating LDSPSDKInvalidAttributeSyntaxException
            case (INVALID_ATTRIBUTE_SYNTAX):
                {
                    exception = new LDAPSDKInvalidAttributeSyntaxException(message, code, cause);
                    break;
                }
            // creating LDSPSDKInvalidDNSyntaxException
            case (INVALID_DN_SYNTAX):
                {
                    exception = new LDAPSDKInvalidDNSyntaxException(message, code, cause);
                    break;
                }
            // creating LDSPSDKCallErrorException
            case (PROTOCOL_ERROR):
            case (PARAM_ERROR):
            case (LDAP_NOT_SUPPORTED):
                {
                    exception = new LDAPSDKCallErrorException(message, code, cause);
                    break;
                }
            // creating LDSPSDKOperationNotAllowedException
            case (IS_LEAF):
            case (NOT_ALLOWED_ON_NONLEAF):
                {
                    exception = new LDAPSDKOperationNotAllowedException(message, code, cause);
                    break;
                }
            // creating LDSPSDKException
            case (OTHER):
            default :
                {
                    exception = new LDAPSDKException(message, code, cause);
                    break;
                }
        }

        return exception;
    }
}
