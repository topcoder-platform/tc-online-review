/*
 * TCS Data Validation
 *
 * TypeValidator.java
 *
 * Copyright (c) 2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.datavalidator;

import java.util.*;

/**
 * This is a simple ObjectValidator which specifically deals with validating
 * the input type of a value. It diregards all primitive based class types
 * (such as Double for example) and considered any input that is primitive
 * based wrapper to be invalid.
 * user can create this validator with a specific class type and then it (or
 * its descendants) woudl be considered as the valid type to be used by this
 * validator.
 * This class is thread-safe as it is immutable.
 * @version 1.1
 */
public class TypeValidator extends AbstractObjectValidator {

   /**
    * The underlying validator to use. This is initialized through the
    * constructor and is immutable after that. It is expected to be
    * non-null.
    */
    private final ObjectValidator validator;

    /** Inputs must be instances of this type. This is initialized in the
     * constructor and is immutable after that. Cannot be null.
     */
    private final Class type;

    /** This array represents invalid types. */
    private static Collection invalidTypes = new ArrayList();
    {
        invalidTypes.add(byte.class);
        invalidTypes.add(short.class);
        invalidTypes.add(int.class);
        invalidTypes.add(long.class);
        invalidTypes.add(float.class);
        invalidTypes.add(double.class);
        invalidTypes.add(char.class);
        invalidTypes.add(boolean.class);
    }

    /**
     * Creates a new <code>TypeValidator</code> that ensures inputs are
     * instances of <code>type</code>.
     *
     * @param   type    ensure inputs are instances of this class.
     * @throws  IllegalArgumentException    if <code>type</code> is
     * <code>null</code>.
     */
    public TypeValidator(Class type)
            throws IllegalArgumentException {
        if (type == null) {
            throw new IllegalArgumentException("type cannot be null");
        }

        if (invalidTypes.contains(type)) {
            throw new IllegalArgumentException("type cannot be a primitive "
                    + "data type (" + type.getName() + ")");
        }

        this.validator = null;
        this.type = type;
    }

    /**
     * Creates a new <code>TypeValidator</code> that ensures inputs are
     * instances of <code>type</code> and is initializaed with a resource bundle
     * information.
     *
     * @param   type    ensure inputs are instances of this class.
     * @throws  IllegalArgumentException    if <code>type</code> is
     * <code>null</code>.
     * @param bundleInfo BundleInfo  resource bundle information
     */
    public TypeValidator(Class type, BundleInfo bundleInfo)
        throws IllegalArgumentException {
      super(bundleInfo);
      if (type == null) {
        throw new IllegalArgumentException("type cannot be null");
      }

      if (invalidTypes.contains(type)) {
        throw new IllegalArgumentException("type cannot be a primitive "
                                           + "data type (" + type.getName() + ")");
      }

      this.validator = null;
      this.type = type;
    }


    /**
     * Creates a new <code>TypeValidator</code> that ensures inputs are
     * instances of <code>type</code>, and uses <code>validator</code> as the
     * underlying <code>ObjectValidator</code>.
     *
     * @param   validator   the underlying <code>ObjectValidator</code> to use.
     * @param   type    ensure inputs are instances of this class.
     * @throws  IllegalArgumentException if <code>validator</code> or
     * <code>type</code> is <code>null</code>.
     */
    public TypeValidator(ObjectValidator validator, Class type)
            throws IllegalArgumentException {
        if (validator == null) {
            throw new IllegalArgumentException("validator cannot be null");
        }

        if (type == null) {
            throw new IllegalArgumentException("type cannot be null");
        }

        if (invalidTypes.contains(type)) {
            throw new IllegalArgumentException("type cannot be a primitive "
                    + "data type (" + type.getName() + ")");
        }

        this.validator = validator;
        this.type = type;
    }


    /**
     * Creates a new <code>TypeValidator</code> that ensures inputs are
     * instances of <code>type</code>, and uses <code>validator</code> as the
     * underlying <code>ObjectValidator</code>.
     *
     * @param   validator   the underlying <code>ObjectValidator</code> to use.
     * @param   type    ensure inputs are instances of this class.
     * @throws  IllegalArgumentException if <code>validator</code> or
     * <code>type</code> is <code>null</code>.
     * @param bundleInfo BundleInfo  resource bundle information
     */
    public TypeValidator(ObjectValidator validator, Class type, BundleInfo bundleInfo)
            throws IllegalArgumentException {
          super(bundleInfo);
          if (validator == null) {
            throw new IllegalArgumentException("validator cannot be null");
          }

          if (type == null) {
            throw new IllegalArgumentException("type cannot be null");
          }

          if (invalidTypes.contains(type)) {
            throw new IllegalArgumentException("type cannot be a primitive "
                                               + "data type (" + type.getName() + ")");
          }

          this.validator = validator;
          this.type = type;
    }

    /**
     * Validates the given <code>Object</code>.
     *
     * @param  obj <code>Object</code> to be validated.
     * @return <code>true</code> if <code>obj</code> is valid;
     *      <code>false</code> otherwise.
     */
    public boolean valid(Object obj) {
        if (obj == null) {
            return false;
        }

        // obj is not of valid type...
        if (!type.isAssignableFrom(obj.getClass())) {
            return false;

        // obj type ok, no more validations...
        } else if (validator == null) {
            return true;

        // obj type ok, use underlying validator...
        } else {
            return validator.valid(obj);
        }
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
        String message = null;
        if (obj == null) {
            return "object is null";
        }

        // obj is not of valid type...
        if (!type.isAssignableFrom(obj.getClass())) {
            return "not instance of " + type.getName();

        // obj type ok, no more validations...
        } else if (validator == null) {
            return null;

        // obj type ok, use underlying validator...
        } else {
            return validator.getMessage(obj);
        }
    }
    /**
 * Gives error information about the given object being validated. This
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
