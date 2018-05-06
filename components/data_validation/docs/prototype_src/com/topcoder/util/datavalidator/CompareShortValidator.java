package com.topcoder.util.datavalidator;

    /**
     * This is an extention of the ShortValidator which specifically deals with
     * validating comparisons based criteria such equal, greater-than-or-equal,
     * less-than, less-than-or-equal, greater-than. Thus we will have a
     * validation based on a specific initilization value (that is set with the
     * validator) and then have this validator compare the input value with the
     * set value using one of the specific comparison directions.
     * This is an inner class to ShortValidator.
     * This class is thread-safe as it is immutable.
     * @version 1.1
     */
    class CompareShortValidator extends ShortValidator {

       /**
        * The value to compare inputs with. This is the validator initilization
        * value which is set in the constructor and once set is immutable.
        * This cannot be null and represents the value against which the input
        * values will be compared.
        */
        private final short value;

        /**
         * The direction for comparing inputs. This is initialized in the
         * constructor and is immutable therafter. It cannot be null.
         */
        private final CompareDirection direction;

        /**
         * Creates a new <code>CompareShortValidator</code> that compares
         * inputs to <code>value</code>, in the direction determined by
         * <code>direction</code>.
         *
         * @param   value   inputs are compared to this.
         * @param   direction   the comparison direction.
         */
        public CompareShortValidator(short value, CompareDirection direction) {
            this.value = value;
            this.direction = direction;
        }

        /**
         * Creates a new <code>CompareShortValidator</code> that compares
         * inputs to <code>value</code>, in the direction determined by
         * <code>direction</code> and initializes the validator with a specific
         * resource bundle information.
         * @param bundleInfo BundleInfo  resource bundle information
         * @param   value   inputs are compared to this.
         * @param   direction   the comparison direction.
         */
        public CompareShortValidator(short value, CompareDirection direction, BundleInfo bundleInfo) {
          super(bundleInfo);
            this.value = value;
            this.direction = direction;
        }

        /**
         * Validates the given <code>short</code> value.
         *
         * @param  value   <code>short</code> value to be validated.
         * @return <code>true</code> if <code>value</code> is valid;
         *      <code>false</code> otherwise.
         */
        public boolean valid(short value) {
            if (direction == CompareDirection.GREATER) {
                return value > this.value;

            } else if (direction == CompareDirection.GREATER_OR_EQUAL) {
                return value >= this.value;

            } else if (direction == CompareDirection.LESS) {
                return value < this.value;

            } else if (direction == CompareDirection.LESS_OR_EQUAL) {
                return value <= this.value;

            }else if (direction == CompareDirection.EQUAL) {
                  return value == this.value;
            }


            // should not reach here because all cases are handled
            return false;
        }

        /**
         * If the given <code>short</code> value is valid, this returns
         * <code>null</code>. Otherwise, it returns an error message.
         *
         * @param   value  <code>short</code> value to be validated.
         * @return  <code>null</code> if <code>value</code> is valid. Otherwise
         * an error message is returned.
         */
        public String getMessage(short value) {
            String message = null;
            if (valid(value)) {
                return null;
            } else {
                return "not " + direction + " " + this.value;
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
