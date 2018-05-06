package com.topcoder.util.datavalidator;

    /**
     * This is an extention of the StringValidator which specifically deals with
     * validating strings based on whether they are to be consider a whitespace
     * or not.
     * This is an inner class to StringValidator.
     * This class is thread-safe as it is immutable.
     * @version 1.1
     */
    class WhitespaceCharacterValidator extends CharacterValidator {

        /**
         * Creates a new <code>CaseCharacterValidator</code> that checks
         * whether inputs are whitespace characters.
         */
        public WhitespaceCharacterValidator() {
        }


        /**
         * Creates a new <code>WhitespaceCharacterValidator</code>  and initializes the validator with a specific resource
         * bundle information.
         * @param bundleInfo BundleInfo  resource bundle information
         */
        public WhitespaceCharacterValidator(BundleInfo bundleInfo) {
          super(bundleInfo);
       }

        /**
         * Validates the given <code>char</code> value.
         *
         * @param  value    <code>char</code> to be validated.
         * @return <code>true</code> if <code>value</code> is valid;
         *      <code>false</code> otherwise.
         */
        public boolean valid(char value) {
            return Character.isWhitespace(value);
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
                return "not a whitespace";
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
