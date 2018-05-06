/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.distribution.parsers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.topcoder.util.distribution.DistributionScript;
import com.topcoder.util.distribution.DistributionScriptCommand;
import com.topcoder.util.distribution.DistributionScriptParser;
import com.topcoder.util.distribution.DistributionScriptParsingException;
import com.topcoder.util.distribution.Util;
import com.topcoder.util.distribution.commands.CommandExecutionCondition;
import com.topcoder.util.distribution.commands.ConvertToPDFCommand;
import com.topcoder.util.distribution.commands.CopyFileCommand;
import com.topcoder.util.distribution.commands.CopyFileTemplateCommand;
import com.topcoder.util.distribution.commands.CreateFolderCommand;
import com.topcoder.util.distribution.commands.DefineVariableCommand;
import com.topcoder.util.distribution.commands.ExecuteProcessCommand;
import com.topcoder.util.distribution.commands.UndefineVariableCommand;
import com.topcoder.util.distribution.commands.conditions.VariableDefinedCondition;
import com.topcoder.util.distribution.commands.conditions.VariableNotDefinedCondition;
import com.topcoder.util.log.Log;

/**
 * <p>
 * This class is an implementation of DistributionScriptParser that can read
 * distribution scripts in format described in the section 1.3.1 of CS and
 * supports all commands defined in the com.topcoder.util.distribution.commands
 * package of this component. Currently the following DistributionScriptCommand
 * implementations are supported by this class: DefineVariableCommand,
 * UndefineVariableCommand, CreateFolderCommand, CopyFileCommand,
 * CopyFileTemplateCommand, ExecuteProcessCommand and ConvertToPDFCommand.
 * </p>
 *
 * <p>
 * Thread Safety: This class is immutable and thread safe when the given
 * InputStream and DistributionScript instances are used by the caller in thread
 * safe manner.
 * </p>
 *
 * @author saarixx, TCSDEVELOPER
 * @version 1.0
 */
public class DistributionScriptParserImpl implements DistributionScriptParser {
    /**
     * <p>
     * Creates an instance of DistributionScriptParserImpl.
     * </p>
     */
    public DistributionScriptParserImpl() {
        // empty
    }

    /**
     * <p>
     * Parses the script commands from the given input stream and sets them to
     * commands property of the given DistributionScript instance.
     * </p>
     *
     * @param log
     *            the logger to be used by constructed commands (null if logging
     *            in commands is not required)
     * @param script
     *            the distribution script instance to which parsed commands must
     *            be set
     * @param stream
     *            the input stream for reading script
     *
     * @throws IllegalArgumentException
     *             if stream or script is null
     * @throws DistributionScriptParsingException
     *             if some error occurs when parsing a distribution script
     */
    public void parseCommands(InputStream stream, DistributionScript script,
            Log log) throws DistributionScriptParsingException {
        Util.checkNonNull(stream, "stream");
        Util.checkNonNull(script, "script");

        // Create input stream reader:
        InputStreamReader inputStreamReader = new InputStreamReader(stream);
        // Create buffered reader:
        BufferedReader reader = new BufferedReader(inputStreamReader);
        String line = null; // will hold the currently processed line of the
        // script
        // Create new list for script commands:
        List<DistributionScriptCommand> commands = new ArrayList<DistributionScriptCommand>();
        try {
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.length() == 0 || line.startsWith("#")) {
                    continue;
                }

                List<CommandExecutionCondition> conditions = new ArrayList<CommandExecutionCondition>();
                line = parseConditions(conditions, line);

                // Parse line as "commandName:commandData"
                String[] pair = parseCmdNameAndData(line);

                DistributionScriptCommand command;
                if (pair[0].equalsIgnoreCase("define")) {
                    String[] args = pair[1].split("=", 2);
                    if (args.length < 1 || args[0].trim().length() == 0) {
                        throw new DistributionScriptParsingException(
                                "The variable name for 'define' command should be non-empty string.");
                    }
                    args[0] = args[0].trim();
                    String value = args.length > 1 ? args[1].trim() : "";
                    // Create "define variable" command:
                    command = new DefineVariableCommand(log, conditions,
                            args[0], value);
                } else if (pair[0].equalsIgnoreCase("undefine")) {
                    // Create "undefine variable" command:
                    command = new UndefineVariableCommand(log, conditions,
                            pair[1]);
                } else if (pair[0].equalsIgnoreCase("create_folder")) {
                    // Create "create folder" command:
                    command = new CreateFolderCommand(log, conditions, pair[1]);
                } else if (pair[0].equalsIgnoreCase("copy_file")) {
                    String[] args = parseTwoArgsCmd(pair[1], "->", "copy_file");
                    // Create "copy file" command:
                    command = new CopyFileCommand(log, conditions, args[0],
                            args[1]);
                } else if (pair[0].equalsIgnoreCase("file_template")) {
                    String[] args = parseTwoArgsCmd(pair[1], "->",
                            "file_template");
                    // Create "copy template file" command:
                    command = new CopyFileTemplateCommand(log, conditions,
                            args[0], args[1]);
                } else if (pair[0].equalsIgnoreCase("execute")) {
                    String[] args = parseTwoArgsCmd(pair[1], "::", "execute");
                    // Create "execute process" command:
                    command = new ExecuteProcessCommand(log, conditions,
                            args[0], args[1]);
                } else if (pair[0].equalsIgnoreCase("convert_to_pdf")) {
                    String[] args = parseTwoArgsCmd(pair[1], "->", "execute");
                    // Create "covert to pdf" command:
                    command = new ConvertToPDFCommand(log, conditions, args[0],
                            args[1]);
                } else {
                    throw new DistributionScriptParsingException(
                            "Unknown command " + pair[0] + ".");
                }
                // Add command to the list:
                commands.add(command);
            }
        } catch (IOException e) {
            throw new DistributionScriptParsingException(
                    "Error occurs while parsing the commands.");
        }

        // Set parsed commands to the script:
        script.setCommands(commands);
    }

    /**
     * <p>
     * Parse the two arguments of the command in the commandData.
     * </p>
     *
     * @param commandData
     *            the data to parse
     * @param delimiter
     *            the delimiter of the arguments
     * @param commandName
     *            the name of the command, used in the error message when failed
     *            to parse
     * @return the array of the two parsed arguments
     *
     * @throws DistributionScriptParsingException
     *             if couldn't get two arguments or either of the arguments is
     *             empty
     */
    private String[] parseTwoArgsCmd(String commandData, String delimiter,
            String commandName) throws DistributionScriptParsingException {
        String[] args = commandData.split(delimiter, 2);
        if (args.length != 2) {
            throw new DistributionScriptParsingException(
                    "The command data in command " + commandName
                            + " should have two parts.");
        }
        args[0] = args[0].trim();
        args[1] = args[1].trim();
        if (args[0].length() == 0 || args[1].length() == 0) {
            throw new DistributionScriptParsingException(
                    "Both of the two arguments of command " + commandName
                            + " shouldn't be empty.");
        }
        return args;
    }

    /**
     * <p>
     * Parse the command name and data from the line.
     * </p>
     *
     * @param line
     *            the command line to parse
     * @return the command name and data as a array of two elements
     *
     * @throws DistributionScriptParsingException
     *             if the form of the command is invalid
     */
    private String[] parseCmdNameAndData(String line)
        throws DistributionScriptParsingException {
        String[] pair = line.split(":", 2);

        if (pair.length != 2) {
            throw new DistributionScriptParsingException(
                    "The command should have form like commandName:commandData");
        }
        pair[0] = pair[0].trim();
        pair[1] = pair[1].trim();

        if (pair[0].length() == 0 || pair[1].length() == 0) {
            throw new DistributionScriptParsingException(
                    "The command should have form like commandName:commandData");
        }
        return pair;
    }

    /**
     * <p>
     * Parse the conditions from the line.
     * </p>
     *
     * @param conditions
     *            the list of condition to store parsed conditions
     * @param line
     *            the line to parse
     * @return the line after parsing the conditions
     *
     * @throws DistributionScriptParsingException
     *             if error occurs while parsing the conditions
     */
    private String parseConditions(List<CommandExecutionCondition> conditions,
            String line) throws DistributionScriptParsingException {
        while (line.toLowerCase().startsWith("ifdef(")
                || line.toLowerCase().startsWith("ifndef(")) {
            boolean isIfdef = line.toLowerCase().startsWith("ifdef(");
            int index = line.indexOf(')');
            if (index == -1) {
                throw new DistributionScriptParsingException(
                        "No enclosed ')' for condition expression.");
            }
            // extract the var name in condition
            String varName = line.substring(line.indexOf('(') + 1, index)
                    .trim();
            CommandExecutionCondition condition;
            if (isIfdef) {
                // Create new "variable defined" condition
                condition = new VariableDefinedCondition(varName);
            } else {
                condition = new VariableNotDefinedCondition(varName);
            }
            conditions.add(condition);
            // Remove "ifdef(...)" or "ifndef(...)" prefix from line
            line = line.substring(index + 1).trim();
        }
        return line;
    }
}
