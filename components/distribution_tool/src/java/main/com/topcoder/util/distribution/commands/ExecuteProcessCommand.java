/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.distribution.commands;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import com.topcoder.util.distribution.DistributionScriptExecutionContext;
import com.topcoder.util.distribution.ProcessExecutionException;
import com.topcoder.util.distribution.Util;
import com.topcoder.util.log.Log;

/**
 * <p>
 * This class is an implementation of DistributionScriptCommand that executes an
 * external process using the specified command line and waits until this
 * process is terminated. It extends BaseDistributionScriptCommand that provides
 * common functionality for all commands defined in this component. This command
 * uses a specified working directory when starting a process.
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
public class ExecuteProcessCommand extends BaseDistributionScriptCommand {
    /**
     * <p>
     * The external command to be executed (can contain variable fields in
     * format "%{variable_name}"). Is initialized in the constructor and never
     * changed after that. Cannot be null or empty. Is used in executeCommand().
     * </p>
     */
    private final String command;

    /**
     * <p>
     * The working directory path for the executed process (can contain variable
     * fields in format "%{variable_name}"). Is initialized in the constructor
     * and never changed after that. Cannot be null or empty. Is used in
     * executeCommand().
     * </p>
     */
    private final String workingPath;

    /**
     * <p>
     * Creates an instance of ExecuteProcessCommand with the given parameters.
     * </p>
     *
     * @param log
     *            the logger used for logging executed command description and
     *            warnings (null if logging is not required)
     * @param command
     *            the external command to be executed (can contain variable
     *            fields in format "%{variable_name}")
     * @param workingPath
     *            the working directory path for the executed process (can
     *            contain variable fields in format "%{variable_name}")
     * @param conditions
     *            the conditions that indicate when this command must be
     *            executed (all conditions are ANDed, empty list means that the
     *            command must be executed ALWAYS)
     *
     * @throws IllegalArgumentException
     *             if command or workingPath is null or empty, or if conditions
     *             is null or contains null
     */
    public ExecuteProcessCommand(Log log,
            List<CommandExecutionCondition> conditions, String command,
            String workingPath) {
        super(log, conditions);

        Util.checkNonNullNonEmpty(command, "command");
        Util.checkNonNullNonEmpty(workingPath, "workingPath");

        this.command = command;
        this.workingPath = workingPath;
    }

    /**
     * <p>
     * Executes this command. Executes the specific command line in a separate
     * process and waits until this process is terminated.
     * </p>
     *
     * @param context
     *            the distribution script execution context
     *
     * @throws IllegalArgumentException
     *             if context is null
     * @throws ProcessExecutionException
     *             if some error occurred when starting an external process
     */
    protected void executeCommand(DistributionScriptExecutionContext context)
        throws ProcessExecutionException {
        Util.checkNonNull(context, "context");

        // Replace variable fields in the string:
        String curCommand = replaceVariableFields(command, context);
        // Replace variable fields in the string:
        String curWorkingPath = replaceVariableFields(workingPath, context);
        // Get runtime instance:
        Runtime runtime = Runtime.getRuntime();
        Util.logInfo(getLog(), "Executing command <" + curCommand
                + "> using working path <" + curWorkingPath + ">");

        try {
            Process process = runtime.exec(curCommand, null, new File(
                    curWorkingPath));

            // any error message?
            StreamGobbler errorGobbler = new
                StreamGobbler(process.getErrorStream(), "ERROR");

            // any output?
            StreamGobbler outputGobbler = new
                StreamGobbler(process.getInputStream(), "OUTPUT");

            // The two thread are created to empty the streams for the external
            // process, to avoid the problem in class java.lang.Process
            // kick them off
            errorGobbler.start();
            outputGobbler.start();

            // Wait until the process is terminated:
            process.waitFor();
        } catch (IOException e) {
            throw new ProcessExecutionException(
                    "I/O error occurs while executing the command " + curCommand, e);
        } catch (InterruptedException e) {
            throw new ProcessExecutionException(
                    "Error occurs while executing the command " + curCommand, e);
        }
    }

    /**
     * <p>
     * A inner class that used to empty the output stream or error stream for the external
     * process. We need empty the streams if any in different threads.
     * </p>
     *
     * @author TCSDEVELOPER
     * @version 1.0
     */
    class StreamGobbler extends Thread
    {
        /**
         * <p>
         * The input stream.
         * </p>
         */
        private InputStream is;

        /**
         * <p>
         * Represents the type of the stream(stdout or stderr).
         * </p>
         */
        private String type;

        /**
         * <p>
         * The constructor.
         * </p>
         *
         * @param is
         *            the input stream
         * @param type
         *            the stream type
         */
        StreamGobbler(InputStream is, String type) {
            this.is = is;
            this.type = type;
        }

        /**
         * <p>
         * Empty the stream.
         * </p>
         */
        public void run() {
            try {
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String line = null;
                while ((line = br.readLine()) != null)
                    // just read the content, no operation on it
                    line = null;
            } catch (IOException ioe) {
                // ignore
            }
        }
    }
}
