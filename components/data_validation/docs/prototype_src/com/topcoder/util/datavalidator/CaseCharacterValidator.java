package com.topcoder.util.datavalidator;

    /**
     * This is a specific type of CharacterValidator which ensures either the
     * upper case or lower case aspect of input (i.e. of the character). Thus
     * this validator works in one of two modes:
     *     1. Test for upper case
     *     2. Test for lower case
     * User will choose which mode it operates in through the constructor flag.
     * This is thread-safe since it is immutable.
     * This is an inner class of CharacterValidator
     * @version 1.1
     */
    class CaseCharacterValidator extends CharacterValidator {

        /**
         * Whether inputs must be upper or lower case letters. This is
         * initialized in the constructor and denotes upper-case validation (true)
         * or lower-case validation (false)
         * Once initialized it is immutable.
         */
        private final boolean isUpper;

        /**
         * Creates a new <code>CaseCharacterValidator</code> that checks
         * whether inputs are upper or lower case letters.
         *
         * @param   isUpper <code>true</code> if inputs must be upper case;
         *      <code>false</code> if inputs must be lower case.
         */
        public CaseCharacterValidator(boolean isUpper) {
            this.isUpper = isUpper;
        }

        /**
         * Creates a new <code>CaseCharacterValidator</code> that checks
         * whether inputs are upper or lower case letters and is initialized with
         * resource bundle information.
         *
         * @param   isUpper <code>true</code> if inputs must be upper case;
         *      <code>false</code> if inputs must be lower case.
         * @param bundleInfo BundleInfo  resource bundle information
         */
        public CaseCharacterValidator(boolean isUpper, BundleInfo bundleInfo) {
          super(bundleInfo);
          this.isUpper = isUpper;
        }


        /**
         * Validates the given <code>char</code> value.
         *
         * @param  value    <code>char</code> to be validated.
         * @return <code>true</code> if <code>value</code> is valid;
         *      <code>false</code> otherwise.
         */
        public boolean valid(char value) {
            if (isUpper) {
                return Character.isUpperCase(value);
            } else {
                return Character.isLowerCase(value);
            }
        }

        /**
         * If the given <code>char</code> value is valid, this returns
         * <code>null</code>. Otherwise, it returns an error message.
         *
         * @param   value   <code>char</code> value to be validated.
         * @return  <code>null</code> if <code>value</code> is valid. Otherwise
         * an error message is returned.
         */
        public String getMessage(char value) {
            String message = null;
            if (valid(value)) {
                return null;
            } else {
              // get the message from resource bundle if it exists.
              message = this.getValidationMessage();
              if(message != null){
                return message;
              }
              return "not " + (isUpper ? "an upper" : "a lower")
                        + " case letter";
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
