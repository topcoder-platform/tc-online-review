/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.errorhandling;

import java.util.ResourceBundle;
import com.topcoder.util.log.Level;
import com.topcoder.util.log.Log;

/**
 * <p>
 * <code>SmartException</code> is a custom exception which extends
 * <code>BaseCriticalException</code>. It makes use of the give utilities to
 * provide message for it. The <code>Logging Wrapper 2.0</code> component is
 * used to log.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 2.0
 */

public class SmartException extends BaseCriticalException {
    /**
     * Represents the module code.
     */
    public static final String MODCODE = "SmartEx";

    /**
     * <p>
     * Build exception from <code>error</code> code, <code>cause</code>, as
     * well as the additional <code>data</code> to attach to the exception.
     * The <code>logger</code> is used to log message.
     * </p>
     *
     * @param error the error code attached to the exception
     * @param cause The initial throwable reason which triggered this exception
     *            to be thrown
     * @param data the additional data to attach to the exception
     * @param logger the <code>Log</code> instance to log message.
     */
    public SmartException(String error, Throwable cause, ExceptionData data, Log logger) {

        // call super constructor, statically building all information
        super(buildMessage(error), cause, getData(data, error));

        // now log if needed
        if (logger != null) {
            logger.log(Level.ERROR, getMessage());
            setLogged(true);
        }
    }

    /**
     * <p>
     * A private static method used to get the <code>ExceptionData</code>
     * instance after setting the app, module and error codes. If
     * <code>data</code> parameter is null, a new <code>ExceptionData()</code>
     * is returned. <code>AppUtils</code> class is used to get application
     * code.
     * </p>
     *
     * @param data the <code>ExceptionData</code> instance to be set
     * @param error the error code to attach to the exception
     * @return the data after setting its app, module and error codes, or a new
     *         <code>ExceptionData()</code> if parameter data is null
     */
    private static ExceptionData getData(ExceptionData data, String error) {
        if (data == null) {
            return new ExceptionData();
        } else {
            return data.setApplicationCode(AppUtils.getAppCode()).setModuleCode(MODCODE).setErrorCode(error);
        }
    }

    /**
     * <p>
     * A private static method used to build message with the given
     * <code>errCode</code>. <code>AppUtils</code> class is used to get
     * application code.
     * </p>
     *
     * @param errCode the error code attached to the exceptioin
     * @return the built message
     */
    private static String buildMessage(String errCode) {
        // get application data (from fictitious static AppUtils)
        String appCode = AppUtils.getAppCode();
        // get resource bundle (from fictitious static AppUtils)
        ResourceBundle bundle = AppUtils.getResourceBundle();

        // parse statically from utilities
        return ExceptionUtils.getMessage(bundle, appCode, MODCODE, errCode);
    }
}
