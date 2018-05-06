/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.distribution.commands;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import com.topcoder.util.distribution.DistributionScriptCommandExecutionException;
import com.topcoder.util.distribution.DistributionScriptExecutionContext;
import com.topcoder.util.distribution.InputFileNotFoundException;
import com.topcoder.util.distribution.Util;
import com.topcoder.util.log.Log;

/**
 * <p>
 * This class is an implementation of DistributionScriptCommand that copies a
 * text file from one location to another on the file system and replaces all
 * variable fields in format "%{variable_name}" in the destination file with
 * variable values from the execution context. It extends
 * BaseDistributionScriptCommand that provides common functionality for all
 * commands defined in this component. This command can rename the file if
 * required. Destination path can contain "{FILENAME}" and "{EXT}" keywords that
 * are replaced with the source file name (without extension) and the source
 * file extension accordingly.
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
public class CopyFileTemplateCommand extends BaseDistributionScriptCommand {
    /**
     * <p>
     * The source template file path (can contain variable fields in format
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
     * Creates an instance of CopyFileTemplateCommand with the given paths.
     * </p>
     *
     * @param log
     *            the logger used for logging executed command description and
     *            warnings (null if logging is not required)
     * @param destPath
     *            the destination file path (can contain variable fields in
     *            format "%{variable_name}", "{FILENAME}" and "{EXT}" keywords)
     * @param sourcePath
     *            the source template file path (can contain variable fields in
     *            format "%{variable_name}")
     * @param conditions
     *            the conditions that indicate when this command must be
     *            executed (all conditions are ANDed, empty list means that the
     *            command must be executed ALWAYS)
     *
     * @throws IllegalArgumentException
     *             if sourcePath or destPath is null or empty, or if conditions
     *             is null or contains null
     */
    public CopyFileTemplateCommand(Log log,
            List<CommandExecutionCondition> conditions, String sourcePath,
            String destPath) {
        super(log, conditions);

        Util.checkNonNullNonEmpty(sourcePath, "sourcePath");
        Util.checkNonNullNonEmpty(destPath, "destPath");

        this.sourcePath = sourcePath;
        this.destPath = destPath;
    }

    /**
     * <p>
     * Executes this command. Copies a text file from the configured source path
     * to the specified destination and replaces all known variable fields (in
     * format "%{variable_name}") in that file with actual variable values taken
     * from the given DistributionScriptExecutionContext instance. Destination
     * path can contain "{FILENAME}" and "{EXT}" keywords that are replaced with
     * the source file name (without extension) and the source file extension
     * accordingly.
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

        Util.logInfo(getLog(), "Copying template file <" + curSourcePath
                + "> to <" + curDestPath + ">.");

        InputStream input = null;
        OutputStream output = null;
        // Create input file reader:
        try {
            input = new BufferedInputStream(new FileInputStream(curSourcePath));
            // Create output file writer:
            output = new BufferedOutputStream(new FileOutputStream(curDestPath));
            StringBuilder line = new StringBuilder();
            byte[] bytes = new byte[1024];
            int readed;
            while ((readed = input.read(bytes)) > 0) {
                String str = new String(bytes, 0, readed, "ISO-8859-1");
                int pos = str.lastIndexOf("\n");
                if (pos < 0) {
                    line.append(str);
                } else {
                    line.append(str.substring(0, pos + 1));
                    output.write(replaceVariableFields(line.toString(), context).getBytes("ISO-8859-1"));
                    line = new StringBuilder();
                    line.append(str.substring(pos + 1));
                }
            }
            if (line.length() > 0) {
                output.write(replaceVariableFields(line.toString(), context).getBytes("ISO-8859-1"));
            }
        } catch (IOException e) {
            throw new DistributionScriptCommandExecutionException(
                    "Error occurs while copying template " + curSourcePath
                            + "to " + curDestPath, e);
        } finally {
            Util.safeClose(input);
            Util.safeClose(output);
        }
    }
}
