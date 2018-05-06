/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.phase;

/**
 * <p>
 * This class is a container for validation result returned by canStart() and canEnd() methods of PhaseManager and
 * canPerform() method of PhaseHandler. It contains not only a boolean value indicating whether the requested operation
 * can be performed or not, but additionally a message indicating why the operation cannot be performed.
 * </p>
 * <p>
 * Implementations of PhaseManager and PhaseHandler should follow the following rules to make the code clearer:
 * <ol>
 * <li>OperationCheckResult.SUCCESS should be returned by canXXX() methods to indicate that operation can be performed
 * for the given phase;</li>
 * <li>new OperationCheckResult(&lt;&lt;explanation&gt;&gt;) should be returned by canXXX() methods to indicate that
 * operation cannot be performed for the phase and to provide a valid reasoning.</li>
 * </ol>
 * </p>
 * <p>
 * Thread Safety: This class is immutable and thread safe.
 * </p>
 * @author saarixx, sokol
 * @version 1.1
 * @since 1.1
 */
public class OperationCheckResult {

    /**
     * <p>
     * Represents an instance of OperationCheckResult representing a successful check result (indicating that operation
     * can be performed).
     * </p>
     * <p>
     * It is provided to avoid useless instantiations of OperationCheckResult and to make API easier. Is initialized
     * during class loading and never changed after that. Cannot be null.
     * </p>
     */
    public static final OperationCheckResult SUCCESS = new OperationCheckResult(true, null);

    /**
     * <p>
     * Represents the value indicating whether validation was successful (i.e. operation can be performed) or not.
     * </p>
     * <p>
     * Is initialized in the constructor and never changed after that. Has a getter.
     * </p>
     */
    private final boolean success;

    /**
     * <p>
     * Represents the message indicating why the operation cannot be performed.
     * </p>
     * <p>
     * Is null if success is true. Is initialized in the constructor and never changed after that.
     * </p>
     * <p>
     * Cannot be empty. Cannot be null if success is false. Has a getter.
     * </p>
     */
    private final String message;

    /**
     * <p>
     * Creates an instance of OperationCheckResult.
     * </p>
     * @param message the message indicating why the operation cannot be performed (must be null if check is
     *            successful)
     * @param success the value indicating whether validation was successful (i.e. operation can be performed) or not
     * @throws IllegalArgumentException if (success and message != null) or (not success and message == null) or
     *             message is empty
     */
    public OperationCheckResult(boolean success, String message) {
        if (success) {
            Helper.checkState(message != null, "Message should be null, when operation check result is success.");
            // message is null here, no need to check on empty
        } else {
            Helper.checkState(message == null,
                    "Message should be not null, when operation check result is not success.");
            // check message is empty
            Helper.checkState(message.trim().length() == 0,
                    "Message should not be empty, when operation check result is not success.");
        }
        this.success = success;
        this.message = message;
    }

    /**
     * <p>
     * Creates an instance of OperationCheckResult indicating that operation cannot be performed with the failure
     * description message.
     * </p>
     * @param message the message indicating why the operation cannot be performed
     * @throws IllegalArgumentException if message is null or empty
     */
    public OperationCheckResult(String message) {
        this(false, message);
    }

    /**
     * <p>
     * Retrieves the value indicating whether validation was successful (i.e. operation can be performed) or not.
     * </p>
     * @return the value indicating whether validation was successful (i.e. operation can be performed) or not
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * <p>
     * Retrieves the message indicating why the operation cannot be performed.
     * </p>
     * @return the message indicating why the operation cannot be performed
     */
    public String getMessage() {
        return message;
    }
}
