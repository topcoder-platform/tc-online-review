package com.topcoder.util.datavalidator;

    /**
     * This is an extention of the IntegerValidator which specifically deals with
     * validating numbers based on their parity (i.e. if the number is odd or
     * even) Which parity should be validated against is set as a flag when
     * creating/initializing this validator and is bascially odd (true) or
     * even (false)
     * As an example, if we create an instance of this validator set
     * with a parity of odd (true) which woudl then accpet only integers that
     * are odd.
     * This is an inner class to IntegerValidator.
     * This class is thread-safe as it is immutable.
     * @version 1.1
     */
    class ParityIntegerValidator extends IntegerValidator {

       /**
        * This represnts whether inputs must be odd or even. This is the
        * validator initilization value which is set in the constructor and once
        * set is immutable.
        */
        private final boolean isOdd;

        /**
         * Creates a new <code>ParityIntegerValidator</code> that checks whether
         * inputs are odd or even.
         *
         * @param   isOdd   <code>true</code> if inputs must be odd;
         *      <code>false</code> if inputs must be even.
         */
        public ParityIntegerValidator(boolean isOdd) {
            this.isOdd = isOdd;
        }

        /**
         * Creates a new <code>ParityIntegerValidator</code> that checks whether
         * inputs are odd or even and initializes the validator with a specific
         * resource bundle information.
         *
         * @param   isOdd   <code>true</code> if inputs must be odd;
         *      <code>false</code> if inputs must be even.
         * @param bundleInfo BundleInfo  resource bundle information
         */
        public ParityIntegerValidator(boolean isOdd, BundleInfo bundleInfo) {
            super(bundleInfo);
            this.isOdd = isOdd;
        }

        /**
         * Validates the given <code>int</code> value.
         *
         * @param  value   <code>int</code> value to be validated.
         * @return <code>true</code> if <code>value</code> is valid;
         *      <code>false</code> otherwise.
         */
        public boolean valid(int value) {
            return (value % 2 == 0) != isOdd;
        }

        /**
         * If the given <code>int</code> value is valid, this returns
         * <code>null</code>. Otherwise, it returns an error message.
         *
         * @param   value  <code>int</code> value to be validated.
         * @return  <code>null</code> if <code>value</code> is valid. Otherwise
         * an error message is returned.
         */
        public String getMessage(int value) {
            String message = null;
            if (valid(value)) {
                return null;
            } else {
                return "not " + (isOdd ? "odd" : "even");
            }
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
