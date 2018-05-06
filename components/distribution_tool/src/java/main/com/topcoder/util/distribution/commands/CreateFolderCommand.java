/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.distribution.commands;

import java.io.File;
import java.util.List;

import com.topcoder.util.distribution.DistributionScriptCommandExecutionException;
import com.topcoder.util.distribution.DistributionScriptExecutionContext;
import com.topcoder.util.distribution.Util;
import com.topcoder.util.log.Log;

/**
 * <p>
 * This class is an implementation of DistributionScriptCommand that creates a
 * folder on the file system. It extends BaseDistributionScriptCommand that
 * provides common functionality for all commands defined in this component.
 * Note that this command creates all parent folders recursively if required.
 * </p>
 *
 * <p>
 * Thread Safety: This class is immutable and thread safe when
 * DistributionScriptExecutionContext instance is used by the caller in thread
 * safe manner. It uses thread safe Log instance.
 * </p>
 *
 * @author saarixx, TCSDEVELOPER
 * @version 1.0
 */
public class CreateFolderCommand extends BaseDistributionScriptCommand {
    /**
     * <p>
     * The full path of the folder to be created (can contain variable fields in
     * format "%{variable_name}"). Is initialized in the constructor and never
     * changed after that. Cannot be null or empty. Is used in executeCommand().
     * </p>
     */
    private final String folderPath;

    /**
     * <p>
     * Creates an instance of CreateFolderCommand.
     * </p>
     *
     * @param log
     *            the logger used for logging executed command description and
     *            warnings (null if logging is not required)
     * @param folderPath
     *            the full path of the folder to be created (can contain
     *            variable fields in format "%{variable_name}")
     * @param conditions
     *            the conditions that indicate when this command must be
     *            executed (all conditions are ANDed, empty list means that the
     *            command must be executed ALWAYS)
     *
     * @throws IllegalArgumentException
     *             if folderPath is null or empty, or if conditions is null or
     *             contains null
     */
    public CreateFolderCommand(Log log,
            List<CommandExecutionCondition> conditions, String folderPath) {
        super(log, conditions);

        Util.checkNonNullNonEmpty(folderPath, "folderPath");
        this.folderPath = folderPath;
    }

    /**
     * <p>
     * Executes this command. Creates a folder with specific path.
     * </p>
     *
     * @param context
     *            the distribution script execution context
     *
     * @throws IllegalArgumentException
     *             if context is null
     * @throws DistributionScriptCommandExecutionException
     *             if some other error occurred
     */
    protected void executeCommand(DistributionScriptExecutionContext context)
        throws DistributionScriptCommandExecutionException {
        Util.checkNonNull(context, "context");

        // Replace variable fields in the string:
        String currentPath = replaceVariableFields(folderPath, context);
        Util.logInfo(getLog(), "Creating folder <" + currentPath + ">.");

        try {
            // Get File instance for folder to be created:
            File folder = new File(currentPath);
            // Create all folders recursively:
            folder.mkdirs();
        } catch (SecurityException e) {
            throw new DistributionScriptCommandExecutionException("Error occurs while creating the folders.", e);
        }
    }
}
