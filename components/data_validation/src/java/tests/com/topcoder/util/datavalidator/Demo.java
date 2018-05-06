/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.datavalidator;

import junit.framework.TestCase;

import java.util.Locale;


/**
 * <p>
 * This class demonstrates of the usage of this component.
 * </p>
 *
 * @author telly12
 * @version 1.1
 */
public class Demo extends TestCase {
    /**
     * Example of using a validator to obtain a number of messages from the validation process.
     */
    public void testDemo() {
        // Create a bundle info to be used
        BundleInfo bundleInfo = new BundleInfo();
        bundleInfo.setBundle("property", Locale.ENGLISH);
        bundleInfo.setMessageKey("email.field.can.not.be.null");
        bundleInfo.setDefaultMessage("email field cannot be null");

        // Create a aggregating or validator
        AndValidator validator = new AndValidator();

        // Add the first sub validator, true if we have a not null string
        validator.addValidator(new NotValidator(new NullValidator(bundleInfo)));

        bundleInfo.setMessageKey("email.field.is.incorrect.length");
        bundleInfo.setDefaultMessage("email field is of the wrong length");

        // Add the second sub validator, true if the string is in range of 7..35 chars long
        validator.addValidator(StringValidator.hasLength(IntegerValidator.inRange(7, 35, bundleInfo)));

        bundleInfo.setMessageKey("email.field.must.end.with.com");
        bundleInfo.setDefaultMessage("email field doesn＊t end in com");

        // Add the third sub validator, true if the input string end with ".com"
        validator.addValidator(StringValidator.endsWith(".com", bundleInfo));

        bundleInfo.setMessageKey("email.field.must.contain.the.at.char");
        bundleInfo.setDefaultMessage("email field doesn＊t contain the @ character");

        // Add a fourth sub validator, true if the input string contains "@"
        ObjectValidator containsValidator = StringValidator.containsSubstring("@", bundleInfo);
        validator.addValidator(containsValidator);

        //////////////////////////////////////////////////////////////////////////////////
        // Now lets say that we run this validator with the following inputs
        // 1) Input string of "hello" and we run the following:
        //
        // The result will be false, This is because the string is too short
        validator.valid("hello");

        // This will return "Please ensure that the text for email field is between 7 and 35 characters long
        // 每 inclusive"
        // because the AndValidator will short-circuit on the first failed validator and these are done in order in
        // which they were placed. So the first validator to fail this is the inRange validator.
        printResult("validator.getMessage(\"hello\")", validator.getMessage("hello"));

        // This will return "Please ensure that the text for email field is between 7 and 35 characters long
        // 每 inclusive"
        // because the AndValidator will short-circuit on the first failed validator and these are done in order in
        // which they were placed. So the first validator to fail this is the inRange validator and not other
        // validators will be dealt with. This is not very useful.
        printResult("validator.getMessage(\"hello\")", validator.getMessages("hello"));

        // This will return the following messages:
        //        "Please ensure that the text for email field is between 7 and 35 characters long 每 inclusive"
        //        "Please ensure that your email address ends with a .com"
        //        "An email address must contain the @ character."
        // Because the AndValidator in this mode will NOT short-circuit on the first failed validator and will
        // continue doing all validators.
        printResult("getAllMessages(\"hello\")", validator.getAllMessages("hello"));

        // This will only return the following two messages:
        //        "Please ensure that the text for email field is between 7 and 35 characters long 每 inclusive"
        //        "Please ensure that your email address ends with a .com"
        // Because the AndValidator in this mode will NOT short-circuit on the first failed validator but will be
        // limited to only 2 messages.
        printResult("getAllMessages(\"hello\", 2)", validator.getAllMessages("hello", 2));

        //////////////////////////////////////////////////////////////////////////////////
        // 2) Here lets assume that we have the string "ivern#topcoder.org" which is invalid and lets say that we
        // use the non-existent resource bundle of "zh".
        //      Assuming the same code as before but with the difference of this line:
        validator.removeValidator(containsValidator);
        bundleInfo.setBundle("property", Locale.CHINA);
        containsValidator = StringValidator.containsSubstring("@", bundleInfo);
        validator.addValidator(containsValidator);

        // This will return the following messages:
        // "email field doesn＊t end in com"
        // "email field doesn＊t contain the @ character"
        // Because the resource bundle was not found and provided default messages have been used.
        printResult("validator.getAllMessages(\"ivern#topcoder.org\")", validator.getAllMessages("ivern#topcoder.org"));
    }

    /**
     * Print the validation messages.
     *
     * @param desc The validation description
     * @param msgs the validation messages.the validation messages.
     */
    private void printResult(String desc, String[] msgs) {
        System.out.println("The validation result for " + desc + ":");

        if (msgs == null) {
            System.out.println("  >> Validation successfully, no messages return.");

            return;
        }

        for (int i = 0; i < msgs.length; i++) {
            System.out.println("  >> " + msgs[i]);
        }
    }

    /**
     * Print the validation messages.
     *
     * @param desc The validation description
     * @param msg the validation messages.the validation messages.
     */
    private void printResult(String desc, String msg) {
        if (msg == null) {
            printResult(desc, (String[]) null);
        } else {
            printResult(desc, new String[] {msg});
        }
    }
}
