package com.topcoder.util.datavalidator;

    /**
     * This is an extention of the DoubleValidator which specifically deals with
     * validating a range. In other words we are creating a validator that will
     * validate if a given input number is in range (inclusive of the extremes
     * as an option) Note that an acceptable comparison error can be set as
     * well (i.e. the epsilon) which woudl be taken into account when deciding
     * if the in-range criteria has been met.
     * As an example, if we create an instance of this validator set
     * with a range of 2.5 - 7.88 with an epsilon of 1e-12 then a number like
     * 5.0 would be in range and evene a number like 7.88000000045 woudl be in
     * range but a number like 7.89 would not be valid.
     * This is an inner class to DoubleValidator.
     * This class is thread-safe as it is immutable.
     * @version 1.1
     */
    class RangeDoubleValidator extends DoubleValidator {

       /**
        * The underlying lower validator to use. This is initialized through the
        * constructor and is immutable after that. It is expected to be
        * non-null.
        */
        private final DoubleValidator lowerValidator;

        /**
         * The underlying upper validator to use. This is initialized through the
         * constructor and is immutable after that. It is expected to be
         * non-null.
         */
        private final DoubleValidator upperValidator;

        /**
         * The underlying composite validator which will combine the upper and
         * lower validators. This is initialized in the the constructor and is
         * immutable after that. it cannot be null but since it is not
         * initialized by the user there is no danger of that. This will always
         * be an AndValidator which will compbine the upper and lower validators.
         */
        private ObjectValidator validator;

        /**
         * Creates a new <code>RangeDoubleValidator</code> that creates a range
         * comparator which ensures that input is with the specified range
         * and if the exclusive flag is true then we exclude the extremes.
         *
         * @param   lower   lower bound of valid values.
         * @param   upper   upper bound of valid values.
         * @param   exclusive   whether the bounds are exclusive.
         * @param bundleInfo BundleInfo  resource bundle information
         * @throws  IllegalArgumentException    when <code>exclusive</code> is
         *      false, this is thrown if upper is less than lower; when
         *      <code>exclusive</code> is true, this is thrown if upper is less
         *      than or equal to lower.
         */
        public RangeDoubleValidator(double lower, double upper,
                boolean exclusive, BundleInfo bundleInfo)
                throws IllegalArgumentException {
              super(bundleInfo);
              if (!exclusive && upper < lower) {
                throw new IllegalArgumentException("upper is less than lower");
              }

              if (exclusive && upper <= lower) {
                throw new IllegalArgumentException("upper is less than or "
                                                   + "equal to lower");
              }

              if (exclusive) {
                lowerValidator = DoubleValidator.greaterThan(lower, bundleInfo);
                upperValidator = DoubleValidator.lessThan(upper, bundleInfo);
              } else {
                lowerValidator = DoubleValidator.greaterThanOrEqualTo(lower, bundleInfo);
                upperValidator = DoubleValidator.lessThanOrEqualTo(upper, bundleInfo);
              }

              validator = new AndValidator(lowerValidator, upperValidator);
        }

        /**
         * Creates a new <code>RangeDoubleValidator</code> that creates a range
         * comparator which ensures that input is with the specified range
         * and if the exclusive flag is true then we exclude the extremes.
         * This is also initialized with a specific resource bundle information
         * used to fetch error messages.
         *
         * @param   lower   lower bound of valid values.
         * @param   upper   upper bound of valid values.
         * @param   exclusive   whether the bounds are exclusive.
         * @throws  IllegalArgumentException    when <code>exclusive</code> is
         *      false, this is thrown if upper is less than lower; when
         *      <code>exclusive</code> is true, this is thrown if upper is less
         *      than or equal to lower.
         */
        public RangeDoubleValidator(double lower, double upper,
                boolean exclusive)
                throws IllegalArgumentException {
            if (!exclusive && upper < lower) {
                throw new IllegalArgumentException("upper is less than lower");
            }

            if (exclusive && upper <= lower) {
                throw new IllegalArgumentException("upper is less than or "
                        + "equal to lower");
            }

            if (exclusive) {
                lowerValidator = DoubleValidator.greaterThan(lower);
                upperValidator = DoubleValidator.lessThan(upper);
            } else {
                lowerValidator = DoubleValidator.greaterThanOrEqualTo(lower);
                upperValidator = DoubleValidator.lessThanOrEqualTo(upper);
            }

            validator = new AndValidator(lowerValidator, upperValidator);
        }


        /**
         * Sets the epsilon value to be used with this validator.
         *
         * @param   eps epsilon value to use.
         * @throws  IllegalArgumentException    if <code>eps</code> is NaN.
         */
        public void setEpsilon(double eps)
                throws IllegalArgumentException {
            super.setEpsilon(eps);

            lowerValidator.setEpsilon(eps);
            upperValidator.setEpsilon(eps);
        }

        /**
         * Validates the given <code>double</code> value.
         *
         * @param  value   <code>double</code> value to be validated.
         * @return <code>true</code> if <code>value</code> is valid;
         *      <code>false</code> otherwise.
         */
        public boolean valid(double value) {
            return validator.valid(new Double(value));
        }

        /**
         * If the given <code>double</code> value is valid, this returns
         * <code>null</code>. Otherwise, it returns an error message.
         *
         * @param   value  <code>double</code> value to be validated.
         * @return  <code>null</code> if <code>value</code> is valid. Otherwise
         * an error message is returned.
         */
        public String getMessage(double value) {
            return validator.getMessage(new Double(value));
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
