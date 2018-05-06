package com.topcoder.util.datavalidator;

    /**
     * This is a specific type of CharacterValidator which will validate an input
     * character as a digit or a letter (but not both)
     * It can act in one of those modes:
     *    1. Check of the input character is a digit
     *    2. Check if the input character is letter
     * This is determined by the initialization done in the constructor which
     * allows the user to choose either a digit validation (isDigit - true) or
     * letter validation (idDigit - false)
     * This class is thread-safe since it is immutable.
     * Note that this is an inner class of the CharacterValidator and is not
     * part of the public API.
     * @version 1.1
     */
    class AlphanumCharacterValidator extends CharacterValidator {

        /** Whether inputs must be digits or letters. This is intialized in the
         * constructor and is used during validation to determine the mode of
         * validation (true - must be digit, false - must be letter)
         * */
        private final boolean isDigit;

        /**
         * Creates a new <code>AlphanumCharacterValidator</code> that checks
         * whether inputs are letters or digits.
         *
         * @param   isDigit <code>true</code> if inputs must be digits;
         *      <code>false</code> if inputs must be letters.
         */
        public AlphanumCharacterValidator(boolean isDigit) {
            this.isDigit = isDigit;
        }

        /**
         * Creates a new <code>AlphanumCharacterValidator</code> that checks
         * whether inputs are letters or digits and is set with a specific
         * resource bundle info
         * @param bundleInfo BundleInfo  resource bundle information
         * @param   isDigit <code>true</code> if inputs must be digits;
         *      <code>false</code> if inputs must be letters.
         */
        public AlphanumCharacterValidator(boolean isDigit, BundleInfo bundleInfo) {
          super(bundleInfo);
          this.isDigit = isDigit;
        }


        /**
         * Validates the given <code>char</code> value.
         *
         * @param  value    <code>char</code> to be validated.
         * @return <code>true</code> if <code>value</code> is valid;
         *      <code>false</code> otherwise.
         */
        public boolean valid(char value) {
            if (isDigit) {
                return Character.isDigit(value);
            } else {
                return Character.isLetter(value);
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
                // get the message from resource bundle if it exisist.
                message = this.getValidationMessage();
                if(message != null){
                  return message;
                }
                return "not a " + (isDigit ? "digit" : "letter");
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
