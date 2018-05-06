/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.errorhandling;

import java.util.ResourceBundle;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogManager;
import junit.framework.TestCase;

/**
 * <p>
 * The Base Exception 2.0 component builds on the central idea to the version
 * 1.0 component - to provide a family of exception classes that can be extended
 * later by custom exceptions, allowing the exceptions to contain more
 * information than Java built-in exceptions.
 * <p>
 * This class provides the demo usage of this component.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class ComponentDemo extends TestCase {

    /**
     * <p>
     * This demo shows how to create base exceptions with their constructors.
     * </p>
     * <p>
     * Since the constructors are similar, this demo will only focus on
     * <code>BaseException</code>.
     * </p>
     */
    public void testExceptionCreation() {
        // create the ExceptionData instance used to construct exceptions
        ExceptionData data = new ExceptionData();
        // create the Throwable instance used to construct exceptions
        Throwable throwable = new NullPointerException();
        // create a BaseException instance with its constructors
        BaseException baseException = new BaseException();
        assertNotNull("A BaseException instance should be created.", baseException);
        baseException = new BaseException("test");
        baseException = new BaseException(throwable);
        baseException = new BaseException(data);
        baseException = new BaseException("test", throwable);
        baseException = new BaseException("test", data);
        baseException = new BaseException(throwable, data);
        baseException = new BaseException("test", throwable, data);
        assertEquals("Return value should be 'test'", "test", baseException.getMessage());
        assertEquals("Cause should be set correctly", throwable, baseException.getCause());

        // create a BaseNonCriticalException instance with its default constructor
        BaseNonCriticalException nonCriticalException = new BaseNonCriticalException();
        assertNotNull("A BaseNonCriticalException instance should be created.", nonCriticalException);
        // create a BaseCriticalException instance with its default constructor
        BaseCriticalException criticalException = new BaseCriticalException();
        assertNotNull("A BaseCriticalException instance should be created.", criticalException);
        // create a BaseError instance with its default constructor
        BaseError error = new BaseError();
        assertNotNull("A BaseError instance should be created.", error);
        // create a BaseRuntimeException instance with its default constructor
        BaseRuntimeException runtimeException = new BaseRuntimeException();
        assertNotNull("A BaseRuntimeException instance should be created.", runtimeException);
    }

    /**
     * <p>
     * This demo shows how to use a custom exception within an application.
     * </p>
     */
    public void testCustomException() {
        // call throwMethod(String name) with name = "baduser"
        try {
            throwMethod("baduser");
        } catch (CustomException e) {
            // log the exception if not already:
            if (!e.isLogged()) {
                e.setLogged(true);
                assertEquals("Message should be 'bad'", "bad", e.getMessage());
                assertEquals("Error code should be 'e1421_m'", "e1421_m", e.getErrorCode());
                assertEquals("Module code should be 'MY_MODULE'", "MY_MODULE", e.getModuleCode());
            }
        }
        // call throwMethod(String name) with name = "spaced name"
        try {
            throwMethod("spaced name");
        } catch (CustomException ce) {
            System.out.println("Logged on thread " + ce.getThreadName());
            System.out.println("Logged at time " + ce.getCreationDate());

            String bad = (String) ce.getInformation("attempted name");
            if (bad != null) {
                // perform some other logic on the exception
                ce.setInformation("suggested name", bad.replaceAll(" ", "_"));
                assertEquals("Suggested name should be 'spaced_space'", "spaced_name",
                        (String) ce.getInformation("suggested name"));
            }
        }
    }

    /**
     * <p>
     * A private method used to throw <code>CustomException</code> according to the <code>name</code>.
     * </p>
     *
     * @param name the name to indicate whether exception should be thrown
     * @throws CustomException if the name is equal to 'baduser'/ contains blank spaces
     */
    private void throwMethod(String name) throws CustomException {
        // Check for valid name, throws IAE if string is empty or null.
        ExceptionUtils.checkNullOrEmpty(name, null, null, "No empty name");
        // obtain resource bundle
        // The following ResourceBundle is defined in the same package
        // public class MyResourceBundle extends ListResourceBundle {
        //    private static final Object[][] contents = {
        //        {"key", "value"}, {"doubleKey", new Double(0.0)}, {"emptyKey", ""}, {"trimemptyKey", "  "},
        //        {"sameKey","sameKey"}, {"app-module-error","value1"}, {"badname", "bad"},
        //        {"nospace","space space"}, {"appCode-SmartEx-e617", "message"}
        //    };
        //    public Object[][] getContents() {
        //        return contents;
        //    }
        // }


        ResourceBundle bund = ResourceBundle.getBundle("com.topcoder.util.errorhandling.MyResourceBundle");

        // The following CustomException is defined in the same package
        // public class CustomException extends BaseNonCriticalException {
        //    public static final String APPCODE = "UserHandler"; // application code
        //
        //    public static final String MODCODE = "MY_MODULE"; // module code
        //
        //    public static final String ERRCODE = "e1421_m"; // error code
        //
        //    public CustomException(String message) {
        //        this(message, null, new ExceptionData()); // call generic constructor
        //    }
        //
        //    public CustomException(String message, Throwable cause) {
        //        this(message, cause, new ExceptionData()); // call generic constructor
        //    }
        //
        //    public CustomException(String message, Throwable cause, ExceptionData data) {
        //        // call super constructor after setting the module and error codes
        //        super(message, cause, getData(data));
        //    }
        //
        //    private static ExceptionData getData(ExceptionData data) {
        //        if (data == null) {
        //            return new ExceptionData();
        //        } else {
        //            return data.setApplicationCode(APPCODE).setModuleCode(MODCODE).setErrorCode(ERRCODE);
        //        }
        //    }
        // }
        // use simple constructor, similar to Base Exception 1.0
        if (name.equals("baduser")) {
            throw new CustomException(ExceptionUtils.getMessage(bund, "badname", "Bad user!"));
        }

        // use data constructor to attach more information
        if (name.indexOf(" ") != -1) {
            System.out.println("Encounter a name containing blank spaces");
            throw new CustomException(ExceptionUtils.getMessage(bund, "nospace", "No spaces"), null,
                    new ExceptionData().setInformation("attempted name", name).setInformation("size",
                            new Long(name.length())).setLogged(true));
        }
    }

    /**
     * <p>
     * This demo shows how to use <code>SmartException</code> which makes use of the given utilities to provide
     * exception message.
     * </p>
     *
     */
    public void testSmartException() {
        // The following AppUtils class should be defined in the same package
        // public class AppUtils {
        //    public static String getAppCode() {
        //        return "appCode";
        //    }
        //    public static ResourceBundle getResourceBundle() {
        //        return ResourceBundle.getBundle("com.topcoder.util.errorhandling.MyResourceBundle");
        //    }
        // }

        // Also, the following SmartException should be defined in the same package
        // public class SmartException extends BaseCriticalException {
        //    public static final String MODCODE = "SmartEx"; // module code
        //    public SmartException(String error, Throwable cause, ExceptionData data, Log logger) {
        //        super(buildMessage(error), cause, getData(data, error));
        //        if (logger != null) {
        //            logger.log(Level.ERROR, getMessage());
        //            setLogged(true);
        //        }
        //    }
        //    private static ExceptionData getData(ExceptionData data, String error) {
        //        if (data == null) {
        //            return new ExceptionData();
        //        } else {
        //            return data.setApplicationCode(AppUtils.getAppCode()).setModuleCode(MODCODE).setErrorCode(error);
        //        }
        //    }
        //    private static String buildMessage(String errCode) {
        //        String appCode = AppUtils.getAppCode();
        //        ResourceBundle bundle = AppUtils.getResourceBundle();
        //        return ExceptionUtils.getMessage(bundle, appCode, MODCODE, errCode);
        //    }
        // }

        // get a Log instance
        Log myLogger = LogManager.getLog();
        // SmartException can be thrown directly
        try {
            throw new SmartException("e617", null, new ExceptionData(), myLogger);
        } catch (SmartException se) {
            assertEquals("Message should be 'appCode-SmartEx-e617: message", "appCode-SmartEx-e617: message",
                        se.getMessage());
        }
    }

    /**
     * <p>
     * This demo shows the usage of <code>ExceptionUtils</code>.
     * </p>
     *
     */
    public void testExceptionUtils() {
        // obtain resource bundle
        ResourceBundle bund = ResourceBundle.getBundle("com.topcoder.util.errorhandling.MyResourceBundle");
        // get localized message string
        assertEquals("Return value should be 'value'", "value", ExceptionUtils.getMessage(bund, "key", "default"));
        assertEquals("Return value should be 'default'", "default", ExceptionUtils.getMessage(null, null, "default"));
        // get an exception message automatically from the exception¡¯s codes
        assertEquals("Return value should be 'app-module-error: value1'", "app-module-error: value1",
                        ExceptionUtils.getMessage(bund, "app", "module", "error"));
        // check null argument, IllegalArgumentException is thrown
        try {
            ExceptionUtils.checkNull(null, null, null, "null value");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // good
        }
        // check empty String, IllegalArgumentException is thrown
        try {
            ExceptionUtils.checkNullOrEmpty(" ", null, null, "empty value");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // good
        }

    }

}
