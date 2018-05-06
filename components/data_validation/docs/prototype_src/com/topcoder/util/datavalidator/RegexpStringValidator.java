package com.topcoder.util.datavalidator;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * This is an extention of the StringValidator which specifically deals with
 * validating strings based on a regular expression.
 * This is an inner class to StringValidator.
 * This class is thread-safe as it is immutable.
 * @version 1.1
 */
class RegexpStringValidator extends StringValidator {

    /** Inputs must conform to this regexp string. This is initialized in the
     * constructor and is used to initialize the regexp pattern (below)
     * It cannot be null and cannot be an empty string.
     */
    private final String regexpStr;

    /** The regular expression as a pattern. This is initialized in the
     * constructor. It cannot be null. It is initialized from the regexpStr
     * that the user inputs.
     */
    private final Pattern regexpPattern;

    /**
     * Creates a new <code>StartsWithStringValidator</code> that checks
     * whether inputs start with <code>str</code>.
     *
     * @param   regexpStr inputs must conform to this regexp.
     * @throws  IllegalArgumentException    if <code>str</code> is
     * <code>null</code> or is an empty string. It will also be thrown if there
     * are issues with creating the regular expression pattern.
     */
    public RegexpStringValidator(String regexpStr)
            throws IllegalArgumentException {
        if (regexpStr == null) {
            throw new IllegalArgumentException("regexpStr cannot be null");
        }
        if (regexpStr.trim().length() == 0) {
            throw new IllegalArgumentException("regexpStr cannot be an empty string");
        }


        this.regexpStr = regexpStr;
        this.regexpPattern = Pattern.compile(regexpStr);

    }

    /**
     * Creates a new <code>StartsWithStringValidator</code> that checks
     * whether inputs start with <code>str</code> with a specific resource
     * bundle information.
     *
     * @param   regexpStr inputs must conform to this regexp.
     * @throws  IllegalArgumentException    if <code>str</code> is
     * <code>null</code> or is an empty string. It will also be thrown if there
     * are issues with creating the regular expression pattern.
     * @param bundleInfo BundleInfo  resource bundle information
     */
    public RegexpStringValidator(String regexpStr, BundleInfo bundleInfo)
            throws IllegalArgumentException {
          super(bundleInfo);
          if (regexpStr == null) {
            throw new IllegalArgumentException("regexpStr cannot be null");
          }
          if (regexpStr.trim().length() == 0) {
            throw new IllegalArgumentException("regexpStr cannot be an empty string");
          }


          this.regexpStr = regexpStr;
          this.regexpPattern = Pattern.compile(regexpStr);

    }
    /**
     * Validates the given <code>String</code> to see if it matches the regular
     * expression that this validator has been initialized with.
     *
     * @param  str <code>String</code> to be validated.
     * @return <code>true</code> if <code>str</code> is valid;
     *      <code>false</code> otherwise.
     */
    public boolean valid(String str) {
      Matcher matcher = regexpPattern.matcher(str);
        return matcher.matches();
    }

    /**
     * If the given <code>String</code> is valid , this returns
     * <code>null</code>. Otherwise, it returns an error message.
     * To test validity this method will test to see if it matches the regular
     * expression that this validator has been initialized with.
     *
     * @param   str <code>String</code> value to be validated.
     * @return  <code>null</code> if <code>str</code> is valid. Otherwise an
     *      error message is returned.
     */
    public String getMessage(String str) {
        String message = null;
        if (valid(str)) {
            return null;
        } else {
                return "does not match the following regular expression\"" + this.regexpStr;
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
