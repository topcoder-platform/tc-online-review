/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder;

import com.topcoder.util.datavalidator.ObjectValidator;

/**
 * <p>
 * This validator is always true for any provided value. It is used as a
 * substitute value for null constraints on searchable field maps. This is done
 * as a convenient alternative to encapsulating null-handling behavior into all
 * filter classes.
 * </p>
 *
 * <p>
 * Thread Safety: This class is stateless and the methods can handle concurrent
 * requests, so its therefore thread-safe.
 * </p>
 *
 * @author ShindouHikaru, TCSDEVELOPER
 * @version 1.3
 */
class AlwaysTrueValidator implements ObjectValidator {

    /**
     * The id.
     */
    private String id = null;
    /**
     * <p>
     * Default Cosntructor.
     * </p>
     *
     */
    public AlwaysTrueValidator() {
    }
    /**
     * <p>
     * Cosntructor with Id.
     * </p>
     *
     * @throws IllegalArgumentException if the <code>id</code> is null
     */
    public AlwaysTrueValidator(String id) {
        if(id == null) {
            throw new IllegalArgumentException("The id can not be null.");
        }
        this.id = id;
    }

    /**
     * <p>
     * Always returns null, since this validator considers any object to be
     * valid.
     * </p>
     *
     * @return null always.
     * @param obj
     *            The object to validate (this parameter is ignored)
     */
    public String getMessage(Object obj) {
        return null;
    }

    /**
     * <p>
     * Always returns true, since this validator considers any object to be
     * valid.
     * </p>
     *
     * @return True always.
     * @param obj
     *            The object to validate (this parameter is ignored)
     */
    public boolean valid(Object obj) {
        return true;
    }

    /**
     * <p>
     * Gives error information about the given object being validated. This method will return possibly a number of
     * messages produced for this object by a number of validators if the validator is composite. In general a
     * composite <code>OrValidator</code> should produce quite a few messages (when constituent validators keep on
     * failing - since all have to fail for the OR or fail) but for an <code>AndValidator</code> we would only see a
     * single message (of course as we would go deeper this would be changing if there were <code>OrValidators</code>
     * under the <code>AndValidator</code> in question) This validation will only return enough error messages leading
     * to the moment in traversing the composite validator tree to determine that the validator has failed. in other
     * words this method is short-circuited.
     * </p>
     *
     * @param object the object to validate
     *
     * @return String[] a non-empty but possibly null array of failure messages
     */
    public String[] getMessages(Object object) {
        return getAllMessages(object);
    }
    /**
     * <p>
     * This is the same method concept as the <code>getMessage()</code> except that this method will evaluate the whole
     * validator tree and return all the messages from any validators that failed the object. In other words this is a
     * non-short-circuited version of the method. This method should be used with caution as for large trees it might
     * have to go deep and thus spend quite a bit of time doing it. The alternative to this is the getAllMessages
     * overload that takes a limiting max that will force the method to return after so many errors were found.
     * </p>
     *
     * @param object the object to validate
     *
     * @return String[] a non-empty but possibly null array of failure messages
     */
    public String[] getAllMessages(Object object) {
        return new String [] {getMessage(object)};
    }
    /**
     * <p>
     * This is an overloaded version of the <code>getAllMessages()</code> method which accepts a limit on how many
     * messages at most will be requested. This means that the traversal of the validator tree will stop the moment
     * messageLimit has been met. This is provided so that the user can limit the number of messages coming back and
     * thus limit the traversal time but still get a reasonable number of messages back to the user.
     * </p>
     *
     * @param object the object to validate
     * @param messageLimit the max number of messages. This must be greater-than-or-equal to 0.
     *
     * @return String[] always return null since this is a AlwaysTrueValidator.
     *
     * @throws IllegalArgumentException if messageLimit is not positive
     */
    public String[] getAllMessages(Object object, int messageLimit) {
        if(messageLimit <= 0) {
            throw new IllegalArgumentException("The messageLimit is not positive.");
        }
        return getAllMessages(object);
    }

    /**
     * <p>
     * A unique identifier of this validator. This can be used to identify validators with specific resources that they
     * validate.
     * </p>
     *
     * @return the identifier of this validator
     */
    public String getId() {
        return id;
    }
    /**
     * <p>
     * This is a setter for the validator's id. This will be used to set a specific id for the validator so that later
     * on we can map the validator for a message for example. The value of id can be anything except null.
     * </p>
     *
     * @param id the id of validator
     *
     * @throws IllegalArgumentException if the <code>id</code> is null
     */
    public void setId(String id) {
        if(id == null) {
            throw new IllegalArgumentException("The id can not be null.");
        }
        this.id = id;
    }
}
