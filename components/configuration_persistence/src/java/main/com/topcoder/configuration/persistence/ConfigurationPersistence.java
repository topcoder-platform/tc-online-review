/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.configuration.persistence;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import com.topcoder.configuration.ConfigurationObject;

/**
 * <p>
 * This interface defines the contract for classes that save and load ConfigurationObjects to and
 * from files.
 * </p>
 * <p>
 * Two implementations of this interface are included with this component, one for xml files and one
 * for property files. If a user needs support for some other format, a new implementation of this
 * interface is needed and FileType should be extended.
 * </p>
 * <p>
 * The existing implementations of this interface are thread safe. It is not required that future
 * implementations be thread safe.
 * </p>
 *
 * @author bendlund, rainday
 * @version 1.0
 *
 */
public interface ConfigurationPersistence extends Serializable {
    /**
     * <p>
     * Returns a ConfigurationObject with the specified name containing the configuration stored in
     * the specified file.
     * </p>
     *
     * @param name
     *            name of the ConfigurationObject to return
     * @param file
     *            an abstract path name for the configuration file to read.  The file is sought first among the
     *            resources accessible to the context CLassLoader, and if not found there then the file system is
     *            checked.
     * @return the generated ConfigurationObject for the file
     *
     * @throws IOException
     *             if an I/O problem occured in reading from the specified file
     * @throws IllegalArgumentException
     *             if name is empty or null, or file is null
     * @throws ConfigurationParserException
     *             the file could not be parsed by this ConfigurationPersistence implementation
     */
    public ConfigurationObject loadFile(String name, File file) throws IOException, ConfigurationParserException;

    /**
     * <p>
     * Saves the given ConfigurationObject to the specified file. Any data currently in the
     * specified file will be overwritten.
     * </p>
     *
     * @param file
     *            an abstract path name for the configuration file to update.  The file is sought first among the
     *            resources accessible to the context CLassLoader, and if not found there then the file system is
     *            checked.  Saving will fail if the file is not writable; in particular, it will fail if the file is
     *            inside an archive file.
     * @param config
     *            a ConfigurationObject containing string properties that can be written to a file
     *
     * @throws IOException
     *             indicates that an I/O problem occured in reading from the specified file
     * @throws IllegalArgumentException
     *             if either argument is null
     * @throws ConfigurationParserException
     *             if any problem occured in retrieving value from the configuration object
     */
    public void saveFile(File file, ConfigurationObject config) throws IOException, ConfigurationParserException;
}
