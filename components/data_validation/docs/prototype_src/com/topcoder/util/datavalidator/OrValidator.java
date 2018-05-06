/*
 * TCS Data Validation
 *
 * OrValidator.java
 *
 * Copyright (c) 2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.datavalidator;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

/**
 * This is a specific type of a compsite validator which validates based on a
 * boolean OR outcome of all the associated validators. This is basically a
 * composite OR validator. by default it works ina short-circuited mode which
 * means that the validator will return an 'valid' result the moment the
 * first of the validators returns a true (or null for getMessage())
 * This class is thread safe since it adds no shared/mutable data
 * @version 1.1
 */
public class OrValidator extends AbstractAssociativeObjectValidator {

    /**
     * Creates a new <code>OrValidator</code>, with initially no associated
     * validators.
     */
    public OrValidator() {
    }

    /**
     * Creates a new <code>OrValidator</code>, with 2 initial
     * <code>ObjectValdiators</code>.
     *
     * @param   validator1  the first <code>ObjectValidator</code> to use.
     * @param   validator2  the second <code>ObjectValidator</code> to use.
     * @throws  IllegalArgumentException    if <code>validator1</code> or
     *      <code>validator2</code> is <code>null</code>.
     */
    public OrValidator(ObjectValidator validator1, ObjectValidator validator2)
            throws IllegalArgumentException {
        super(validator1, validator2);
    }

    /**
     * Validates the given <code>Object</code>. Note: if the list of associated
     * validators is empty, this returns false.
     *
     * @param  obj <code>Object</code> to be validated.
     * @return <code>true</code> if <code>obj</code> is valid;
     *      <code>false</code> otherwise.
     */
    public boolean valid(Object obj) {
        for (Iterator itr = getValidators(); itr.hasNext();) {
            if (((ObjectValidator) itr.next()).valid(obj)) {
                return true;
            }
        }

        return false;
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
        StringBuffer allmsgs = new StringBuffer();
        boolean first = true;

        for (Iterator itr = getValidators(); itr.hasNext();) {
            String msg = ((ObjectValidator) itr.next()).getMessage(obj);

            if (msg == null) {
                return null;
            } else {

                // ensures we don't add " AND " before the 1st msg
                if (first) {
                    first = false;
                } else {
                    allmsgs.append(" AND ");
                }

                allmsgs.append(msg);
            }
        }

        return allmsgs.toString();
    }

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
    public String[] getMessages(Object object){
      List allMessages = new ArrayList();
      for (Iterator itr = getValidators(); itr.hasNext();) {
        String[] messages = ((ObjectValidator) itr.next()).getMessages(object);
        if (messages == null){
          if(allMessages.size() > 0){
            return (String[]) (allMessages.toArray());
          }
          return null;
        }else{
          for(int i=0; i< messages.length; i++){
            allMessages.add(messages[i]);
          }
        }
      }
      return null;
    }

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
    public String[] getAllMessages(Object object){
      List allMessages = new ArrayList();
      for (Iterator itr = getValidators(); itr.hasNext(); ) {
        String[] messages = ( (ObjectValidator) itr.next()).getMessages(object);
        if (messages == null) {
          // no-op
        }
        else {
          for (int i = 0; i < messages.length; i++) {
            allMessages.add(messages[i]);
          }
        }
        if (allMessages.size() > 0) {
          return (String[]) (allMessages.toArray());
        }
        else {
          return null;
        }
      }
      return null;
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
    protected String[] getAllMessages(Object object, int messageLimit, int currentCount){
      List allMessages = new ArrayList();
      for (Iterator itr = getValidators(); itr.hasNext(); ) {
        String[] messages = ( (ObjectValidator) itr.next()).getMessages(object);
        if (messages == null) {
          // no-op
        }
        else {
          for (int i = 0; i < messages.length; i++) {
            allMessages.add(messages[i]);
            currentCount++;
            if (currentCount >= messageLimit) {
              return (String[]) (allMessages.toArray());
            }
          }
        }
        if (allMessages.size() > 0) {
          return (String[]) (allMessages.toArray());
        }
        else {
          return null;
        }
      }
      return null;
    }
}
