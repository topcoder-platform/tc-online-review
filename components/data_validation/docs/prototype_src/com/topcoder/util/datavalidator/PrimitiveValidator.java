/*
 * TCS Data Validation
 *
 * PrimitiveValidator.java
 *
 * Copyright (c) 2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.datavalidator;

/**
 * This is a specific wrapper for all validators that deal with primitive type
 * value validation such a Integer, Boolean, Long, Double, Float, Byte, or Short
 * this is nothing more than a convenience wrapper.
 * This is thread-safe since it is immutable.
 * @version 1.1
 */
public class PrimitiveValidator extends AbstractObjectValidator {

   /**
    * The underlying validator to use. This is initialized through the
    * constructor and is immutable after that. It is expected to be
    * non-null.
    */
    private ObjectValidator validator = null;

    /**
     * Creates a new <code>PrimitiveValidator</code> that uses
     * <code>validator</code> as the underlying <code>ObjectValidator</code>.
     *
     * @param   validator   the underlying <code>ObjectValidator</code> to use.
     * @throws  IllegalArgumentException    if <code>validator</code> is
     * <code>null</code>.
     **/
    public PrimitiveValidator(ObjectValidator validator)
            throws IllegalArgumentException {
        if (validator == null) {
            throw new IllegalArgumentException("validator cannot be null");
        }

        this.validator = validator;
    }

    /**
     * Creates a new <code>PrimitiveValidator</code> that uses
     * <code>validator</code> as the underlying <code>ObjectValidator</code>
     * with a specific resource bundle information.
     *
     * @param   validator   the underlying <code>ObjectValidator</code> to use.
     * @param bundleInfo BundleInfo  resource bundle information
     * @throws  IllegalArgumentException    if <code>validator</code> is
     * <code>null</code>.
     **/
    public PrimitiveValidator(ObjectValidator validator, BundleInfo bundleInfo)
            throws IllegalArgumentException {
          super(bundleInfo);
          if (validator == null) {
            throw new IllegalArgumentException("validator cannot be null");
          }

          this.validator = validator;
   }


    /**
     * Validates the given <code>Object</code>.
     *
     * @param  obj <code>Object</code> to be validated.
     * @return <code>true</code> if <code>obj</code> is valid;
     *      <code>false</code> otherwise.
     */
    public boolean valid(Object obj) {
        return validator.valid(obj);
    }

    /**
     * Validates the given <code>boolean</code> value.
     *
     * @param  value   <code>boolean</code> value to be validated.
     * @return <code>true</code> if <code>value</code> is valid;
     *      <code>false</code> otherwise.
     */
    public boolean valid(boolean value) {
        return valid(new Boolean(value));
    }

    /**
     * Validates the given <code>byte</code> value.
     *
     * @param  value   <code>byte</code> value to be validated.
     * @return <code>true</code> if <code>value</code> is valid;
     *      <code>false</code> otherwise.
     */
    public boolean valid(byte value) {
        return valid(new Byte(value));
    }

    /**
     * Validates the given <code>short</code> value.
     *
     * @param  value   <code>short</code> value to be validated.
     * @return <code>true</code> if <code>value</code> is valid;
     *      <code>false</code> otherwise.
     */
    public boolean valid(short value) {
        return valid(new Short(value));
    }

    /**
     * Validates the given <code>int</code> value.
     *
     * @param  value   <code>int</code> value to be validated.
     * @return <code>true</code> if <code>value</code> is valid;
     *      <code>false</code> otherwise.
     */
    public boolean valid(int value) {
        return valid(new Integer(value));
    }

    /**
     * Validates the given <code>long</code> value.
     *
     * @param  value   <code>long</code> value to be validated.
     * @return <code>true</code> if <code>value</code> is valid;
     *      <code>false</code> otherwise.
     */
    public boolean valid(long value) {
        return valid(new Long(value));
    }

    /**
     * Validates the given <code>float</code> value.
     *
     * @param  value   <code>float</code> value to be validated.
     * @return <code>true</code> if <code>value</code> is valid;
     *      <code>false</code> otherwise.
     */
    public boolean valid(float value) {
        return valid(new Float(value));
    }

    /**
     * Validates the given <code>double</code> value.
     *
     * @param  value   <code>double</code> value to be validated.
     * @return <code>true</code> if <code>value</code> is valid;
     *      <code>false</code> otherwise.
     */
    public boolean valid(double value) {
        return valid(new Double(value));
    }

    /**
     * Validates the given <code>char</code> value.
     *
     * @param  value   <code>char</code> value to be validated.
     * @return <code>true</code> if <code>value</code> is valid;
     *      <code>false</code> otherwise.
     */
    public boolean valid(char value) {
        return valid(new Character(value));
    }

    /**
     * If the given <code>Object</code> is valid, this returns
     * <code>null</code>. Otherwise, it returns an error message.
     *
     * @param   obj  <code>Object</code> to be validated.
     * @return  <code>null</code> if <code>obj</code> is valid. Otherwise an
     *      error message is returned.
     */
    public String getMessage(Object obj) {
        return validator.getMessage(obj);
    }

    /**
     * If the given <code>boolean</code> value is valid, this returns
     * <code>null</code>. Otherwise, it returns an error message.
     *
     * @param   value  <code>boolean</code> value to be validated.
     * @return  <code>null</code> if <code>value</code> is valid. Otherwise an
     *      error message is returned.
     */
    public String getMessage(boolean value) {
        return getMessage(new Boolean(value));
    }

    /**
     * If the given <code>byte</code> value is valid, this returns
     * <code>null</code>. Otherwise, it returns an error message.
     *
     * @param   value  <code>byte</code> value to be validated.
     * @return  <code>null</code> if <code>value</code> is valid. Otherwise an
     *      error message is returned.
     */
    public String getMessage(byte value) {
        return getMessage(new Byte(value));
    }

    /**
     * If the given <code>short</code> value is valid, this returns
     * <code>null</code>. Otherwise, it returns an error message.
     *
     * @param   value  <code>short</code> value to be validated.
     * @return  <code>null</code> if <code>value</code> is valid. Otherwise an
     *      error message is returned.
     */
    public String getMessage(short value) {
        return getMessage(new Short(value));
    }

    /**
     * If the given <code>int</code> value is valid, this returns
     * <code>null</code>. Otherwise, it returns an error message.
     *
     * @param   value  <code>int</code> value to be validated.
     * @return  <code>null</code> if <code>value</code> is valid. Otherwise an
     *      error message is returned.
     */
    public String getMessage(int value) {
        return getMessage(new Integer(value));
    }

    /**
     * If the given <code>long</code> value is valid, this returns
     * <code>null</code>. Otherwise, it returns an error message.
     *
     * @param   value  <code>long</code> value to be validated.
     * @return  <code>null</code> if <code>value</code> is valid. Otherwise an
     *      error message is returned.
     */
    public String getMessage(long value) {
        return getMessage(new Long(value));
    }

    /**
     * If the given <code>float</code> value is valid, this returns
     * <code>null</code>. Otherwise, it returns an error message.
     *
     * @param   value  <code>float</code> value to be validated.
     * @return  <code>null</code> if <code>value</code> is valid. Otherwise an
     *      error message is returned.
     */
    public String getMessage(float value) {
        return getMessage(new Float(value));
    }

    /**
     * If the given <code>double</code> value is valid, this returns
     * <code>null</code>. Otherwise, it returns an error message.
     *
     * @param   value  <code>double</code> value to be validated.
     * @return  <code>null</code> if <code>value</code> is valid. Otherwise an
     *      error message is returned.
     */
    public String getMessage(double value) {
        return getMessage(new Double(value));
    }

    /**
     * If the given <code>char</code> value is valid, this returns
     * <code>null</code>. Otherwise, it returns an error message.
     *
     * @param   value  <code>char</code> value to be validated.
     * @return  <code>null</code> if <code>value</code> is valid. Otherwise an
     *      error message is returned.
     */
    public String getMessage(char value) {
        return getMessage(new Character(value));
    }

    /** Gives error information about the given object being validated. This
 * method will return posibly a number of messages produced for this object
 * by a number of validators if the validator is composite. Since this is
 * a primitive validator the result will always be equivalent to getting
 * a simgle message but as an array.
 * @param object Object the object to validate
 * @return String[] a non-empty but possibly null array of failure messages
 */
public String[] getMessages(Object object){
  String[] result = null;
  String message = getMessage(object);
  if(message != null){
    result = new String[1];
    result[0] = message;
  }
  return result;
}

/**
 * This is the same method concept as the getMessage except taht this method
 * will evalulat the whole validator tree and return all the messages from any
 * validators that failed the object. In other words this is a
 * non-short-circuited version of the method. Since this is
 * a primitive validator the result will always be equivalent to getting
 * a single message but as an array.
 * @param object Object the object to validate
 * @return String[] a non-empty but possibly null array of failure messages
 */
public String[] getAllMessages(Object object){
  return getMessages(object);
}


/**
 * This is a helper method to the getAllMessages method with a message limit.
 * User will need to implement this to implement the validator tree traversal.
 * The most important aspect of this method is the fact that the currently
 * accumulated message count has to be ferried as the traversal is happening.
 * To ensure thread-safety this is done on the stack with modification by all
 * the callers in the call chain.
 * @param object Object the object to validate
 * @param messageLimit int int the max number of messages. This must be
 * greater-than-or-equal to 0
 * @param currentCount int the number of messages so far accumulated.
 * @return String[] a non-empty but possibly null array of failure messages with
 * a most messagelimit messages.
 */
protected String[] getAllMessages(Object object, int messageLimit, int currentCount){
  String[] result = getMessages(object);
  if(result == null){
    // no op
  }else{
    currentCount+= result.length;
  }
  return result;
}


}
