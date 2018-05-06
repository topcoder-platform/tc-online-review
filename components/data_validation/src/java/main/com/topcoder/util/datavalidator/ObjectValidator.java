/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.datavalidator;

import java.io.Serializable;


/**
 * <p>
 * This is the main contract for object validation.
 * </p>
 *
 * <p>
 * Here we have the ability for simple validation (true/false) which will tell us if the object input is valid or not.
 * We also have the ability to specify a validation that will produce a message if the validation fails telling us
 * something about the cause. For complex validation where we could have multiple failure points we have the ability
 * to fetch all the messages that come from the validators that failed the object. This can further be refined into a
 * short-circuited evaluation or the non-short-circuted evaluation. In short-circuited evaluation we have a scenario
 * where the validator will return a failed result (with associated) messages, the moment it can determine that the
 * validation has failed.
 * </p>
 *
 * <p>
 * For example and <code>AndValidator</code> need only fail the first of its constituent validators to know that the
 * whole validation fails. Thus there is no need to invoke any further validators in the tree. Thus we have
 * short-circuited <code>AndValidators</code> for failure. The same way the <code>OrValidator</code> is
 * short-circuited for success. In non-short-circuted evaluation we keep on traversing the validator tree regardless
 * of the fact that the composite outcome is already determined. This is good for situations where we would like to
 * return to the user as much as possible information so that they can correct the issues as in a web page input for
 * example. There is also a variation on this with being able to limit the number of messages that are returned to the
 * caller so as to put an upper limit on the validator tree traversal.
 * </p>
 *
 * <p>
 * <b>Thread Safety:</b> Implementations of this interface are required to be thread-safe.
 * </p>
 *
 * @author WishingBone, zimmy, AleaActaEst, telly12
 * @version 1.1
 */
public interface ObjectValidator extends Serializable {
    /**
     * <p>
     * Determines if the given <code>Object</code> is valid.
     * </p>
     *
     * @param obj <code>Object</code> to validate.
     *
     * @return <code>true</code> if <code>obj</code> is valid; <code>false</code> otherwise.
     */
    boolean valid(Object obj);

    /**
     * <p>
     * Gives error information about the given <code>Object</code>.
     * </p>
     *
     * @param obj <code>Object</code> to validate.
     *
     * @return <code>null</code> if <code>obj</code> is valid. Otherwise, an error message is returned.
     */
    String getMessage(Object obj);

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
     *
     * @since 1.1
     */
    public String[] getMessages(Object object);

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
    public abstract String[] getAllMessages(Object object);

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
     * @return String[] a non-empty but possibly null array of failure messages with a most messagelimit messages.
     *
     * @throws IllegalArgumentException if messageLimit is not positive
     *
     * @since 1.1
     */
    public String[] getAllMessages(Object object, int messageLimit);

    /**
     * <p>
     * A unique identifier of this validator. This can be used to identify validators with specific resources that they
     * validate.
     * </p>
     *
     * @return the identifier of this validator
     *
     * @since 1.1
     */
    public String getId();

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
    public void setId(String id);
}
