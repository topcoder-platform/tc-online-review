/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration;

import java.io.File;
import java.util.List;

import com.topcoder.util.commandline.ArgumentValidationException;
import com.topcoder.util.commandline.ArgumentValidator;
import com.topcoder.util.commandline.CommandLineUtility;
import com.topcoder.util.commandline.IllegalSwitchException;
import com.topcoder.util.commandline.Switch;
import com.topcoder.util.commandline.UsageException;
import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.config.ConfigManagerException;

/**
 * The MigratorCommandLine.
 *
 * @author brain_cn
 * @version 1.0
 */
public class MigratorCommandLine {

    /**
     * <p>
     * This constructor is private to prevent this class from being
     * instantiated.
     * </p>
     */
    private MigratorCommandLine() {
        // Empty constructor.
    }

    /**
     * <p>
     * The entry-point of the Event Engine command-line interface. This method
     * parses the command-line arguments and starts the Event Engine.
     * </p>
     *
     * @param   args the command-line arguments
     * @throws  IllegalSwitchException (this should never happen)
     */
    public static final void main(String[] args) throws IllegalSwitchException {
        CommandLineUtility cmdLineUtil = new CommandLineUtility();

        // Add the "config" switch to the command-line utility.
        cmdLineUtil.addSwitch(new Switch("config", false, 1, -1, new ArgumentValidator() {
            public void validate(String argument) throws ArgumentValidationException {
                File confFile = new File(argument);
                if (!confFile.exists()) {
                    throw new ArgumentValidationException(argument,
                            "The configuration file '" + argument + "' does not exist.");
                }
            }
        }, "One or more configuration filenames that will be loaded before the event engine is started."));

        // Add the "namespace" switch to the command-line utility.
        cmdLineUtil.addSwitch(new Switch("process", false, 1, -1, null,
                "The configuration process to indicates which migration processes should executed"));

        // Add the "namespace" switch to the command-line utility.
        cmdLineUtil.addSwitch(new Switch("ids", false, 1, -1, null,
                "The configuration process to indicates which migration processes should executed"));

        // Parse the command-line arguments.
        try {
            cmdLineUtil.parse(args);
        } catch (ArgumentValidationException e) {
            printExceptionAndExit(e);
        } catch (UsageException e) {
            printExceptionAndExit(e);
        }

        // Add the values of the "config" switch to the ConfigManager.
        Switch configSwitch = cmdLineUtil.getSwitch("config");
        List configSwitchValues = configSwitch.getValues();
        if (configSwitchValues != null) {
            for (int i = 0; i < configSwitchValues.size(); i++) {
                String configFilename = (String) configSwitchValues.get(i);
                try {
                	if (!ConfigManager.getInstance().existsNamespace("com.topcoder.onlinereview.migration.DataMigrator")) {
                    	ConfigManager.getInstance().add(configFilename);
                    }
                } catch (ConfigManagerException e) {
                	e.printStackTrace();
                }
            }
        }

        // Get the value of the "namespace" switch.

        DataMigrator migrator = null;
        try {
        	migrator = new DataMigrator();
            Switch processSwitch = cmdLineUtil.getSwitch("process");
            List processes = processSwitch.getValues();
            if (processes != null && processes.size() > 0) {
                for (int i = 0; i < processes.size(); i++) {
                    String process = (String) processes.get(i);
                    if ("project".equals(process)) {
                    	migrator.migrateProject();
                    } else if ("scorecard".equals(process)) {
                    	migrator.migrateScorecard();
                    } else if ("specificed".equals(process)) {
                        Switch idsSwitch = cmdLineUtil.getSwitch("ids");
                        List ids = idsSwitch.getValues();
                        migrator.migrateProjects(ids);
                    }
                }
            } else {
            	migrator.migrate();
            }
        } catch(Exception e) {
        	e.printStackTrace();
        } finally {
        	if (migrator != null) {
        		migrator.close();
        	}
        }
    }

    /**
     * <p>
     * Prints the given exception to <code>System.err</code> and exists the
     * application with exit code -1.
     * </p>
     *
     * @param   e the exception to print
     */
    private static void printExceptionAndExit(Exception e) {
        // The stack trace is printed to System.err.
        e.printStackTrace();

        // Exit
        System.exit(-1);
    }
}
