package com.topcoder.util.datavalidator;

import java.util.Locale;
import java.util.ResourceBundle;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * This is an abstract ObjectValidator implementation which gives the ability
 * for resource bundled message fetching for the validation messages.
 * The user will be able to set up which bundle should be used and what key should
 * be used to fetch the message. We also have the ability to specify the locale.
 * But if one os not provided then the current locale will be used.
 * Note that the bundle functionality is fail safe in the sense the user must
 * provide a default message in case the bundle functionality fails. This way the
 * validator is always guaranteed to be able to produce a message.
 * The user is not required though to utilize the resource bundle functionality
 * and can directly supply just the message that will be then used by the
 * implementing validator.
 * This is a thread-safe implementation since it is immutable.
 * @version 1.1
 */
public abstract class AbstractObjectValidator implements ObjectValidator{

  /**
   * This is just a dummy synchronization object for resource bundle based thread
   * safety.
   */
  private final String dummySynchronizationObjectforBundle = "dummy_1";

  /**
   * This is the id of this validator and is used to uniquely identify this
   * validator amongst other validators in a composite validator for example.
   * This is set through a dedicated setter and is mutable. We can also read it
   * through a dedicated getter. Access to this must be synchronized for
   * thread-safety.
   */
  private String id;

  /**
   * This represents the resource bundle information used by this validator.
   * It is set in one of the constructors to either null (not required) or to a specific
   * named bundle. This is then used to fetch the specific message based on a
   * resource key (which is also initialized in the same constructor)
   * It is transient since it is not serializable. It can be changed upon
   * serialization and subsequent de-serialization.
   */
  private BundleInfo bundleInfo;

  /**
   * This is a simple constrcutor which initializes the defaultMessage to the
   * input string. This can be null or empty and it is up to the user to make
   * this decision.
   * @param validationMessage String This is the validation message to use for
   * the underlying validator.
   * @throws IllegalArgumentException if the input parameter is null or empty
   */
  protected AbstractObjectValidator(String validationMessage) {
      bundleInfo = new BundleInfo();
      bundleInfo.setBundle(null, null);
      bundleInfo.setMessageKey(null);
      bundleInfo.setDefaultMessage(validationMessage);

  }

  /**
   * This is a simple constrcutor which initializes alal the internal variables
   * to their default values.
   * @param validationMessage String This is the validation message to use for
   * the underlying validator.
   */
  protected AbstractObjectValidator() {
    bundleInfo = new BundleInfo();
    bundleInfo.setBundle(null, null);
    bundleInfo.setMessageKey(null);
    bundleInfo.setDefaultMessage(null);
}

  /**
   * This constructor will initialize the resource bundle and the remaining
   * variables based on the input parameters.
   * @param bundleName String name of the bundle to use
   * @param locale Locale Locale to utilize when fetching the message resources
   * @param messageKey String the resource byndle message key to fetch the vadliation
   * message with.
   * We ensure that we copy the actual info to a new instance.
   * @param defaultMessage String This is a fail safe message to use in case the
   * bundle cannot be used (failed creation) or the provided key has no associated
   * message in the bundle.
   * @throws IllegalArgumentException if any of the parameters (except defaultMessage)
   * is null or an empty string (for string parameters)
   */
  protected AbstractObjectValidator(BundleInfo bundleInfo) throws IllegalArgumentException{
    if(bundleInfo == null){
      throw new IllegalArgumentException("null parameters are not allowed");
    }
    this.bundleInfo = copyBundleInfo(bundleInfo);

  }

   /**
    * Gives error information about the given <code>Object</code>.
    *
    * @param   obj <code>Object</code> to validate.
    * @return  <code>null</code> if <code>obj</code> is valid. Otherwise,
    *      an error message is returned.
    */
  public abstract String getMessage(Object object);


  /**
   * Determines if the given <code>Object</code> is valid.
   *
   * @param   obj <code>Object</code> to validate.
   * @return  <code>true</code> if <code>obj</code> is valid;
   *      <code>false</code> otherwise.
   */
  public abstract boolean valid(Object object);

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
  public abstract String[] getMessages(Object object);

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
  public String[] getAllMessages(Object object, int messageLimit) throws IllegalArgumentException{
    if(messageLimit < 0){
      throw new IllegalArgumentException("messagelimit must be greater than or equal to 0");
    }
    // invoke the helper method
    return getAllMessages(object, messageLimit, 0);
  }

  /**
   * This is a helper method to the getAllMessages method with a mesage limit.
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
  protected abstract String[] getAllMessages(Object object, int messageLimit, int currentCount);

  /**
   * This is a simple method which returns the currently set validation message
   * for this validator. This will be either the message obtained from
   * resource bundle or a default message. Please note that this could return
   * null.
   * @return String a possibly null validation message.
   */
  public String getValidationMessage(){
    synchronized(dummySynchronizationObjectforBundle){
      try {
        return bundleInfo.getResourceBundle().getString(bundleInfo.getMessageKey());
      }
      catch (Exception e) {
        // we choke it off
      }
      return bundleInfo.getDefaultMessage();
    }
  }

  /**
   * This is a simple method which returns the currently set validation message
   * for this validator. This will be either the message obtained from
   * resource bundle or a default message. Please note that this could return
   * null.
   * @return String a possibly null validation message.
   */
  public String getValidationMessage(String messageKey, String defaultMessage){
    synchronized(dummySynchronizationObjectforBundle){
      try {
        return bundleInfo.getResourceBundle().getString(messageKey);
      }
      catch (Exception e) {
        // we choke it off
      }
      return bundleInfo.getDefaultMessage();
    }
  }


  /**
     * A unique identifier of this validator. This can be used to identify validators
     * with specific resources that they validate.
     * @return String
     */
    public String getId(){
      synchronized(this.id){
        return id;
      }
    }

    /**
     * This is a setter for the validator's id. This will be used to set a specific
     * id for the validator so that later on we can map the validator for a
     * message for example. There are no restrictions on the value and it can be
     * anything.
     * @param id String
     */
  public void setId(String id){
    synchronized(this.id){
      this.id = id;
    }
  }

  /**
   * A setter of resource bundle information used by this validator.
   * @param bundleName String
   * @param locale Locale
   * @param messageKey String
   * @param defaultMessage String
   * @throws IllegalArgumentException if any parameter is null (except for
   * defaultMessage) or if any string is empty.
   */
  public void setResourceBundleInfo(BundleInfo bundleInfo)throws IllegalArgumentException{
    synchronized(dummySynchronizationObjectforBundle){
      if (bundleInfo == null) {
        throw new IllegalArgumentException("null parameters are not allowed");
      }
      this.bundleInfo = copyBundleInfo(bundleInfo);
    }
  }

  /**
   * This is a getter for the bundle information contained in this validator
   * @return BundleInfo current bundle info, possibly null.
   * This should be thread-safe
   */
  public BundleInfo getBundleInfo(){
    synchronized(dummySynchronizationObjectforBundle){
      return copyBundleInfo(bundleInfo);
    }
  }

  /**
   * Helper method to make a copy of the bundle
   * @param bundleInfo BundleInfo the one to copy
   * @return BundleInfo the copy
   */
  private BundleInfo copyBundleInfo(BundleInfo bundleInfo){
    if(bundleInfo == null){
      return null;
    }
    BundleInfo copy = new BundleInfo();
    copy.setBundle(bundleInfo.getBundleName(), bundleInfo.getLocale());
    copy.setDefaultMessage(bundleInfo.getDefaultMessage());
    copy.setMessageKey(bundleInfo.getMessageKey());

    return copy;
  }

}
