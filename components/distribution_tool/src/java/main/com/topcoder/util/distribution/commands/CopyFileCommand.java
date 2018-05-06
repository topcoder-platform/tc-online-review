/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.distribution.commands;

import java.io.File;
import java.util.List;

import com.topcoder.util.distribution.DistributionScriptCommandExecutionException;
import com.topcoder.util.distribution.DistributionScriptExecutionContext;
import com.topcoder.util.distribution.InputFileNotFoundException;
import com.topcoder.util.distribution.Util;
import com.topcoder.util.log.Log;

/**
 * <p>
 * This class is an implementation of DistributionScriptCommand that copies a
 * file from one location to another on the file system. It extends
 * BaseDistributionScriptCommand that provides common functionality for all
 * commands defined in this component. This command doesn't change the content
 * of the file, but can rename the file if required. Destination path can
 * contain "{FILENAME}" and "{EXT}" keywords that are replaced with the source
 * file name (without extension) and the source file extension accordingly.
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
public class CopyFileCommand extends BaseDistributionScriptCommand {
    /**
     * <p>
     * The source file path (can contain variable fields in format
     * "%{variable_name}"). Is initialized in the constructor and never changed
     * after that. Cannot be null or empty. Is used in executeCommand().
     * </p>
     */
    private final String sourcePath;

    /**
     * <p>
     * The destination file path (can contain variable fields in format
     * "%{variable_name}", "{FILENAME}" and "{EXT}" keywords). Is initialized in
     * the constructor and never changed after that. Cannot be null or empty. Is
     * used in executeCommand().
     * </p>
     */
    private final String destPath;

    /**
     * <p>
     * Creates an instance of CopyFileCommand with the given paths.
     * </p>
     *
     * @param log
     *            the logger used for logging executed command description and
     *            warnings (null if logging is not required)
     * @param destPath
     *            the destination file path (can contain variable fields in
     *            format "%{variable_name}", "{FILENAME}" and "{EXT}" keywords)
     * @param sourcePath
     *            the source file path (can contain variable fields in format
     *            "%{variable_name}")
     * @param conditions
     *            the conditions that indicate when this command must be
     *            executed (all conditions are ANDed, empty list means that the
     *            command must be executed ALWAYS)
     *
     * @throws IllegalArgumentException
     *             if sourcePath or destPath is null or empty, or if conditions
     *             is null or contains null
     */
    public CopyFileCommand(Log log, List<CommandExecutionCondition> conditions,
            String sourcePath, String destPath) {
        super(log, conditions);

        Util.checkNonNullNonEmpty(sourcePath, "sourcePath");
        Util.checkNonNullNonEmpty(destPath, "destPath");

        this.sourcePath = sourcePath;
        this.destPath = destPath;
    }

    /**
     * <p>
     * Executes this command. Copies a file from the configured source path to
     * the specified destination. Destination path can contain "{FILENAME}" and
     * "{EXT}" keywords that are replaced with the source file name (without
     * extension) and the source file extension accordingly.
     * </p>
     *
     * @param context
     *            the distribution script execution context
     *
     * @throws IllegalArgumentException
     *             if context is null
     * @throws InputFileNotFoundException
     *             if the source file cannot be found
     * @throws DistributionScriptCommandExecutionException
     *             if some other error occurred
     */
    protected void executeCommand(DistributionScriptExecutionContext context)
        throws DistributionScriptCommandExecutionException {
        Util.checkNonNull(context, "context");

        // Replace variable fields in the string:
        String curSourcePath = replaceVariableFields(sourcePath, context);

        if (!new File(curSourcePath).isFile()) {
            throw new InputFileNotFoundException(
                    "Couldn't find the source file " + curSourcePath);
        }

        // Replace variable fields in the string:
        String curDestPath = replaceVariableFields(destPath, context);

        curDestPath = CommandsUtil.createDestPath(curSourcePath, curDestPath);

        Util.logInfo(getLog(), "Copying file <" + curSourcePath + "> to <"
                + curDestPath + ">.");

        CommandsUtil.copyFile(curSourcePath, curDestPath);
    }
}
