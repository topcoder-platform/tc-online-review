/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.datavalidator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * <p>
 * This is a specific type of a composite validator which validates based on a boolean OR outcome of all the associated
 * validators. This is basically a composite OR validator. by default it works in a short-circuited mode which means
 * that the validator will return an 'valid' result the moment the first of the validators returns a true (or null for
 * getMessage())
 * </p>
 *
 * <p>
 * <b>Thread Safety:</b>This class is thread safe since it adds no shared/mutable data.
 * </p>
 *
 * @author WishingBone, zimmy, AleaActaEst, telly12
 * @version 1.1
 */
public class OrValidator extends AbstractAssociativeObjectValidator {
    /**
     * <p>
     * Creates a new <code>OrValidator</code>, with initially no associated validators.
     * </p>
     */
    public OrValidator() {
    }

    /**
     * <p>
     * Creates a new <code>OrValidator</code>, with 2 initial <code>ObjectValdiators</code>.
     * </p>
     *
     * @param validator1 the first <code>ObjectValidator</code> to use.
     * @param validator2 the second <code>ObjectValidator</code> to use.
     *
     * @throws IllegalArgumentException if <code>validator1</code> or <code>validator2</code> is <code>null</code>.
     */
    public OrValidator(ObjectValidator validator1, ObjectValidator validator2) {
        super(validator1, validator2);
    }

    /**
     * <p>
     * Validates the given <code>Object</code>. Note: if the list of associated validators is empty, this returns
     * false.
     * </p>
     *
     * @param obj <code>Object</code> to be validated.
     *
     * @return <code>true</code> if <code>obj</code> is valid; <code>false</code> otherwise.
     */
    public boolean valid(Object obj) {
        // Iterate all validators
        for (Iterator itr = getValidators(); itr.hasNext();) {
            if (((ObjectValidator) itr.next()).valid(obj)) {
                return true;
            }
        }

        return false;
    }

    /**
     * <p>
     * If the given <code>Object</code> is valid, this returns <code>null</code>. Otherwise, it returns an error
     * message.
     * </p>
     *
     * @param obj <code>Object</code> to be validated.
     *
     * @return <code>null</code> if <code>obj</code> is valid. Otherwise an error message is returned.
     */
    public String getMessage(Object obj) {
        StringBuffer allmsgs = new StringBuffer();
        boolean isFirst = true;

        // Iterate all validators
        for (Iterator itr = getValidators(); itr.hasNext();) {
            String msg = ((ObjectValidator) itr.next()).getMessage(obj);

            if (msg == null) {
                return null;
            } else {
                // ensures we don't add " AND " before the first message
                if (isFirst) {
                    isFirst = false;
                } else {
                    allmsgs.append(" AND ");
                }

                allmsgs.append(msg);
            }
        }

        return allmsgs.toString();
    }

    /**
     * <p>
     * Gives error information about the given object being validated. This method will return possibly a number of
     * messages produced for this object by a number of validators if the validator is composite. In general a
     * composite <code>OrValidator</code> should produce quite a few messages (when constituent validators keep on
     * failing - since all have to fail for the OR or fail) but for an <code>AndValidator</code> we would only see a
     * single message (of course as we would go deeper this would be changing if there were <code>OrValidator</code>
     * under the <code>AndValidator</code> in question) This validation will only return enough error messages leading
     * to the moment in traversing the composite validator tree to determine that the validator has failed. in other
     * words this method is short-circuited.
     * </p>
     *
     * @param object the object to validate
     *
     * @return String[] a non-empty but possibly null array of failure messages
     *
     * @since 1.1
     */
    public String[] getMessages(Object object) {
        List allMessages = new ArrayList();

        // Iterate all validators
        for (Iterator itr = getValidators(); itr.hasNext();) {
            String[] messages = ((ObjectValidator) itr.next()).getMessages(object);

            if (messages == null) {
                // Validation successful
                return null;
            } else {
                for (int i = 0; i < messages.length; i++) {
                    allMessages.add(messages[i]);
                }
            }
        }

        return (String[]) (allMessages.toArray(new String[0]));
    }

    /**
     * <p>
     * This is another version of the <code>getAllMessages()</code> method which accepts a limit on how many messages
     * at most will be requested. This means that the traversal of the validator tree will stop the moment
     * messageLimit has been met. This is provided so that the user can limit the number of messages coming back and
     * thus limit the traversal time but still get a reasonable number of messages back to the user.
     * </p>
     *
     * @param object the object to validate
     * @param messageLimit the max number of messages. This must be greater-than-or-equal to 0.
     *
     * @return String[] a non-empty but possibly null array of failure messages with a most messageLimit messages.
     *
     * @throws IllegalArgumentException if messageLimit is not positive
     *
     * @since 1.1
     */
    public String[] getAllMessages(Object object, int messageLimit) {
        if (messageLimit <= 0) {
            throw new IllegalArgumentException("The argument 'messageLimit' must be greater than 0");
        }

        List allMessages = new ArrayList();

        // Iterate all validators, only validate if current number of messages is less than messageLimit
        for (Iterator itr = getValidators(); itr.hasNext() && (allMessages.size() < messageLimit);) {
            String[] messages = ((ObjectValidator) itr.next()).getAllMessages(object,
                    messageLimit - allMessages.size());

            if (messages == null) {
                return null;
            } else {
                // Only add if current number of messages is less than messageLimit
                for (int i = 0; (i < messages.length) && (allMessages.size() < messageLimit); i++) {
                    allMessages.add(messages[i]);
                }
            }
        }

        return (String[]) (allMessages.toArray(new String[0]));
    }
}
