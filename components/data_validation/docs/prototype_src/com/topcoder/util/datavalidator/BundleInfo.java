package com.topcoder.util.datavalidator;

import java.util.Locale;
import java.util.ResourceBundle;
import java.io.Serializable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * This is a simple bean which represents the resource bundle information for
 * validators. This a serializable bean and is not thread-safe.
 * This doesn't affect the thread-safety of the component though since it will
 * be utilized in a thread-safe manner.
 * @version 1.1
 */
public class BundleInfo implements Serializable{
  /**
   * This represents the resource bundle used by this validator. It is set in
   * one of the constructors to either null (not required) or to a specific
   * named bundle. This is then used to fetch the specific message based on a
   * resource key (which is also initialized in the same constructor)
   * It is transient since it is not serializable. It can be changed upon
   * serialization and subsequent de-serialization.
   */
  private transient ResourceBundle resourceBundle;

  /**
   * This is a name of a resource bundle to load up the resource message from.
   * This is initialized in the setter but since it is not required
   * it can be null. This is used to create a named resource bundle in one of
   * the constructors. It is immutable. It can be null but cannot be an empty
   * string.
   */
  private String bundleName;

  /**
   * This is the locale used to fetch the resource data. It identifies the
   * locale for the message key that will be used to fetch the message for the
   * validator. It will be initialized through a setter and is mutable.
   */
  private Locale bundleLocale;

  /**
   * This is a flag used to remember if the user set up this validator with a
   * current locale. THis will be used by serialization to also use the current
   * locale when being serialized. It will be initialized through a setter and
   * is mutable. By default it is false.
   */
  private boolean currentLocale = false;

  /**
   * This is a message key used to lookup and fetch a message from a resource
   * bundle. It will be initialized through a setter and is mutable. it is an
   * optional variable and this it can be left uninitilized (i.e. as null)
   */
  private String messageKey;

  /**
   * This is a default message that can be used by any validator in case the
   * resource bundle is not availalbe or simply not set. This will also apply
   * to a situation where the bundle doesn't contain the requested message.
   * This is bascially a fail safe message. It will be initialized through a
   * setter and is mutable. It can be a null or an empty message. There
   * are no restrictions.
   */
  private String defaultMessage;

  /**
   * Simple no op constructor
   */
  public BundleInfo() {
  }

  /**
   * Getter for the bundleName property.
   * @return String
   */
  public String getBundleName(){
    return bundleName;
  }

  /**
   * Getter for the locale property.
   * @return String
   */
  public Locale getLocale(){
    return bundleLocale;
  }

  /**
 * Getter for the defaultMessage property.
 * @return String
 */
  public String getDefaultMessage(){
    return defaultMessage;
  }

  /**
 * Getter for the messageKey property.
 * @return String
 */
  public String getMessageKey(){
  return messageKey;
 }

 /**
  * Setter for the messageKey property.
  * @return String
  */
  public void setMessageKey(String messageKey){
    this.messageKey = messageKey;
  }

  /**
   * Setter for the defaultMessage property.
   * @return String
  */
  public void setDefaultMessage(String defaultMessage){
    this.defaultMessage = defaultMessage;
  }

  /**
   * Setter for the bundle property.
   * @return String
  */
  public void setBundle(String bundleName, Locale locale){
   this.bundleName = bundleName;
   this.bundleLocale = locale;
   this.resourceBundle = ResourceBundle.getBundle(bundleName, locale);
 }

 /**
  * Setter for the bundle property with default locale.
  * @return String
  */
 public void setBundle(String bundleName){
   this.bundleName = bundleName;
   this.bundleLocale = Locale.getDefault();
   this.resourceBundle = ResourceBundle.getBundle(bundleName, bundleLocale);
   this.currentLocale = true;
 }


 /**
* Getter for the currentLocale property.
* @return String
 */
 public boolean getCurrentLocale(){
  return currentLocale;
 }

 /**
* Getter for the resourceBundle property.
* @return String
 */
  public ResourceBundle getResourceBundle(){
  return this.resourceBundle;
 }

  /**
   * Specific implementation for de-serialization. Here we will allow for the
   * resource bundle to be read back in on the other end.
   * @param stream ObjectInputStream the serialization stream
   * @throws IOException if there were any issues with reading the stream
   * @throws ClassNotFoundException if there were any issues with JVM trying to
   * find the specific class via class loader.
   */
   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
      stream.defaultReadObject(  );
          // load up the bundle
          if(currentLocale == true){
               bundleLocale = Locale.getDefault();
          }
          resourceBundle = ResourceBundle.getBundle(bundleName, bundleLocale);

   }

  /**
   * Specific implementation for serialization. This is provided mainly as needed
   * by the protocol since we did override the readObject. There is nothing for
   * us to do here since reourceBundle is declared as transient and will thus
   * not be written to the stream.
   * @param stream ObjectOutputStream
  * @throws IOException if there are any io issues in the stream
  */
  private  void writeObject(ObjectOutputStream stream) throws
  IOException {
      stream.defaultWriteObject(  );
}


}
