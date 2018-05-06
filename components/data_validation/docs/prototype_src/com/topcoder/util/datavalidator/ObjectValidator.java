/*
 * TCS Data Validation
 *
 * ObjectValidator.java
 *
 * Copyright (c) 2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.datavalidator;

import java.io.Serializable;

/**
 * This is the main contract for object validation. Here we have the ability for
 * simple validation (true/false) which will tell us if the object input
 * is valid or not. We also have the ability to specify a validation that will
 * produce a message if the valiadation fails telling us something about the
 * cause. For complex validation where we could have multiple failure points
 * we have the ability to fetch all the messages that come from the validators
 * that failed the object. This can further be refined into a short-circuited
 * evaluation or the non-short-circuted evaluation.
 * In short-circuited evaluation we have a scenario where the validator will
 * return a failed result (with associated) messages, the moment it can
 * determine that the validation has failed. For example and AndValidator need
 * only fail the first of its constituent validators to know that the whole
 * validation fails. Thus there is no need to invoke any further validators in
 * the tree. Thus we have short-circuited AndValidators for failure. The same
 * way the OrValidator is short-circuited for success.
 * In non-short-circuted evaluation we keep on traversing the validator tree
 * regardless of the fact that the composite outcome is already determined.
 * This is good for situations where we woudl like to return to the user as
 * much as possible information so that they can correct the issues as in
 * a web page input for example. There is also a variation on this with being
 * able to limit the numbner of messages that are returned to the caller
 * so as to put an upper limit on the validator tree traversal.
 * Implementations of this are required to be thread-safe.
 *
 * @version 1.1
 */
public interface ObjectValidator extends Serializable{

    /**
     * Determines if the given <code>Object</code> is valid.
     *
     * @param   obj <code>Object</code> to validate.
     * @return  <code>true</code> if <code>obj</code> is valid;
     *      <code>false</code> otherwise.
     */
    boolean valid(Object obj);

    /**
     * Gives error information about the given <code>Object</code>.
     *
     * @param   obj <code>Object</code> to validate.
     * @return  <code>null</code> if <code>obj</code> is valid. Otherwise,
     *      an error message is returned.
     */
    String getMessage(Object obj);

    /**
     * Gives error information about the given object being validated. This
     * method will return posibly a number of messages produced for this object
     * by a number of validators if the validator is composite. In general a
     * composite OrValidator should produce quite a few messages (when constituent
     * validators keep on failing - since all have to fail for the OR ro fail) but
     * for an AndValidator we would only see a single message (of course as we would
     * go deeper this would be changing if there were OrValidators under the
     * AndValidator in question)
     * This validation will only return enough error messages leading to the moment
     * in traversing the composite validator tree to determine that the validator
     * has failed. in other words this method is short-circuited.
     * @param object Object the object to validate
     * @return String[] a non-empty but possibly null array of failure messages
     */
    public String[] getMessages(Object object);

    /**
   * This is the same method concept as the getMessage except taht this method
   * will evalulat the whole validator tree and return all the messages from any
   * validators that failed the object. In other words this is a
   * non-short-circuited version of the method.
   * This method should be used with caution as for large trees it might have to
   * go deep and thus spend quite a bit oif time doing it. The alternative to
   * this is the getAllMessages overload that takes a limiting max that will
   * force the method to return after so many errors were found.
   * @param object Object the object to validate
   * @return String[] a non-empty but possibly null array of failure messages
   */
  public abstract String[] getAllMessages(Object object);

  /**
   * This is an overloaded version of the getAllMessages method which accepts
   * a limit on how many messages at most will be requested. This means that
   * the traversal of the validator tree will stop the moment messageLimit
   * has been met. This is provided so that the user can limit the number
   * of messages coming back and thus limit the traversal time but still get
   * a reasonable number of messages back to the user.
   * @param object Object the object to validate
   * @param messageLimit int the max number of messages. This must be
   * greater-than-or-equal to 0
   * User who want to implement their own traversal routine should implement the
   * helper method that this one calls which is the getAllMessages with the 3
   * parameters
   * @return String[] a non-empty but possibly null array of failure messages with
   * a most messagelimit messages.
   */
  public String[] getAllMessages(Object object, int messageLimit) throws IllegalArgumentException;

  /**
   * A unique identifier of this validator. This can be used to identify validators
   * with specific resources that they validate.
   * @return String
   */
  public String getId();

  /**
   * This is a setter for the validator's id. This will be used to set a specific
   * id for the validator so that later on we can map the validator for a
   * message for example. There are no restrictions on the value and it can be
   * anything.
   * @param id String
   */
  public void setId(String id);



}
