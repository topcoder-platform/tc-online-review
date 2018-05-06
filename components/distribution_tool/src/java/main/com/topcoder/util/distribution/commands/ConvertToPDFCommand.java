/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.distribution.commands;

import java.io.File;
import java.net.ConnectException;
import java.util.List;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeException;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;
import com.topcoder.util.distribution.DistributionScriptCommandExecutionException;
import com.topcoder.util.distribution.DistributionScriptExecutionContext;
import com.topcoder.util.distribution.InputFileNotFoundException;
import com.topcoder.util.distribution.PDFConversionException;
import com.topcoder.util.distribution.Util;
import com.topcoder.util.log.Log;

/**
 * <p>
 * This class is an implementation of DistributionScriptCommand that converts the given input file to PDF format and
 * copies it to the specified location on the file system. It extends BaseDistributionScriptCommand that provides common
 * functionality for all commands defined in this component. This class uses OpenOffice server and JODConverter
 * component to convert files to PDF format, thus it supports source files of DOC, RTF, HTML and some other formats.
 * When the source file has PDF extension, the file is simply copied without any conversion performed.
 * </p>
 *
 * <p>
 * Thread Safety: This class is immutable and thread safe when DistributionScriptExecutionContext instance is used by
 * the caller in thread safe manner. It uses thread safe Log instance.
 * </p>
 *
 * @author saarixx, TCSDEVELOPER
 * @version 1.0
 */
public class ConvertToPDFCommand extends BaseDistributionScriptCommand {
    /**
     * <p>
     * Represents the listening port.
     * </p>
     */
    private static final int OPPENOFFICE_PORT = 8100;

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
     * The destination PDF file path (can contain variable fields in format
     * "%{variable_name}"). Is initialized in the constructor and never changed
     * after that. Cannot be null or empty. Is used in executeCommand().
     * </p>
     */
    private final String destPath;

    /**
     * <p>
     * Creates an instance of ConvertToPDFCommand with the given paths.
     * </p>
     *
     * @param log
     *            the logger used for logging executed command description and
     *            warnings (null if logging is not required)
     * @param destPath
     *            the destination PDF file path (can contain variable fields in
     *            format "%{variable_name}")
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
    public ConvertToPDFCommand(Log log,
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
     * Executes this command. Converts the source file to PDF format and saves
     * it to the specified destination. When the source file has PDF extension,
     * the file is simply copied without any conversion performed.
     * </p>
     *
     * @param context
     *            the distribution script execution context
     *
     * @throws IllegalArgumentException
     *             if context is null
     * @throws InputFileNotFoundException
     *             if the source file cannot be found
     * @throws PDFConversionException
     *             if some error occurred when converting the source file to PDF
     *             format
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
        // Replace keywords {FILENAME} and {EXT} in the curDestPath if any
        curDestPath = CommandsUtil.createDestPath(curSourcePath, curDestPath);
        String sourceFileExt = CommandsUtil.getExtension(curSourcePath);


        if (sourceFileExt.equalsIgnoreCase("pdf")) {
            Util.logInfo(getLog(), "Copying file <" + curSourcePath + "> to <"
                    + curDestPath + ">.");
            CommandsUtil.copyFile(curSourcePath, curDestPath);
            return;
        }

        Util.logInfo(getLog(), "Converting file <" + curSourcePath + "> to <"
                + curDestPath + ">.");
        // convert the source file to pdf using jodconverter
        File inputFile = new File(curSourcePath);
        File outputFile = new File(curDestPath);

        // connect to an OpenOffice.org instance running on port 8100
        OpenOfficeConnection connection = new SocketOpenOfficeConnection(OPPENOFFICE_PORT);
        try {
            connection.connect();
            // converter
            DocumentConverter converter = new OpenOfficeDocumentConverter(
                    connection);
            converter.convert(inputFile, outputFile);
        } catch (ConnectException e) {
            throw new PDFConversionException(
                    "Fail to connect to OpenOffice.org instance.", e);
        } catch (IllegalArgumentException e) {
            // the javadoc from jodconverter is not clear to know
            // what exception types can be thrown.
            // (from the source code, I see that at least IAE may happen)
            throw new PDFConversionException("Fail to convert the file "
                    + curSourcePath + " to pdf format.", e);
        } catch (OpenOfficeException e) {
            // the javadoc from jodconverter is not clear to know
            // what exception types can be thrown.
            // (from the source code, I see that at least OpenOfficeException may happen)
            throw new PDFConversionException("Fail to convert the file "
                    + curSourcePath + " to pdf format.", e);
        } finally {
            if (connection != null) {
                // close the connection
                connection.disconnect();
            }
        }
    }
}
