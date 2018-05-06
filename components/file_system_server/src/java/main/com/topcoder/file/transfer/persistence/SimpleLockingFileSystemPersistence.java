/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.persistence;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * <p>
 * This is a simple implementation of the FilePersistence interface that decorates other file-system-based
 * FilePersistence implementations with a file-locking mechanism.Specifically, it provides a write block for all
 * writing operations. Reading is not a locking operation to allow multiple readings at the same time.
 *
 * The locking is accomplished by directory creation, as this is an atomic process. A directory will be created in the
 * fileLocation with the name persistenceFileName+"_lock". Once the lock is ready to be released, it will be deleted.
 * </p>
 *
 * <p>
 * <strong>Thread Safety:</strong> This class is mutable but thread-safe. The lack of thread-safety may from the
 * inner persistence classes.
 * </p>
 *
 * @author argolite, TCSDEVELOPER
 * @version 1.1
 * @since 1.1
 */
public class SimpleLockingFileSystemPersistence implements FilePersistence {
    /**
     * <p>
     * The map of file creation IDs to the locks that were created for the file being created.
     *
     * The keys are Strings (fileCreationIs - the IDs of the file creation request). The values are File objects that
     * represents the locks, which in this case are the lock directories.
     * </p>
     *
     * <p>
     * It is initialized to a synchronized map on class construction. It is used, added to and removed by in the
     * methods to createFile, appendBytes, closeFile, and dispose methods.
     *
     * It will not be null. It will contain non-null/empty keys and no null values.
     *
     * It is managed as stated in usage section.
     * </p>
     */
    private final Map creationLockFiles = Collections.synchronizedMap(new HashMap());

    /**
     * <p>
     * The FilePersistence for which this persistence implementation provides locking.
     *
     * It is set in the constructor. It is used in every method.
     *
     * It will not be null.
     *
     * Once set it will not change.
     * </p>
     */
    private final FilePersistence innerPersistence;

    /**
     * <p>
     * Sets the innerPersistence to the namesake member and calls the super constructor with no parameters.
     * </p>
     *
     * @param innerPersistence
     *            the FilePersistence for which this persistence implementation provides locking.
     * @throws IllegalArgumentException
     *             if innerPersistence is null
     */
    public SimpleLockingFileSystemPersistence(FilePersistence innerPersistence) {
        if (innerPersistence == null) {
            throw new IllegalArgumentException("innerPersistence is null");
        }

        this.innerPersistence = innerPersistence;
    }

    /**
     * <p>
     * Creates a new file using the given arguments. It returns the file creation id to be used by the user to append
     * the bytes and to close the file.
     * </p>
     *
     * @param fileLocation
     *            the file location
     * @param persistenceFileName
     *            the file name
     * @return the file creation id
     * @throws NullPointerException
     *             if any object is null
     * @throws IllegalArgumentException
     *             if any string is empty
     * @throws FileAlreadyLockedException
     *             If file with the given location and name is currently locked
     * @throws FilePersistenceException
     *             if an exception occurs while performing the operation
     */
    public String createFile(String fileLocation, String persistenceFileName) throws FileAlreadyLockedException,
            FilePersistenceException {
        File lockFile = getFileLock(fileLocation, persistenceFileName);

        // Perform file creation:
        String fileCreationId = innerPersistence.createFile(fileLocation, persistenceFileName);
        // Add mapping from fileCreationId to lockFile:
        creationLockFiles.put(fileCreationId, lockFile);
        return fileCreationId;
    }

    /**
     * <p>
     * Appends the array of bytes to the file denoted by the fileCreationId argument. If any exception occurs, the
     * output stream is closed and removed from the map.
     * </p>
     *
     * @param fileCreationId
     *            the file creation id
     * @param bytes
     *            the bytes to be appended to the file
     * @throws NullPointerException
     *             if any object is null
     * @throws IllegalArgumentException
     *             if the fileCreationId is empty
     * @throws FileNotYetLockedException
     *             if file with the given fileCreationId is currently not locked
     * @throws FilePersistenceException
     *             if an exception occurs while performing the operation
     */
    public void appendBytes(String fileCreationId, byte[] bytes) throws FileNotYetLockedException,
            FilePersistenceException {
        checkString(fileCreationId, "fileCreationId");
        checkNull(bytes, "bytes");

        // Check if lock exists:
        File lockFile = (File) creationLockFiles.get(fileCreationId);
        // If lockFile is null, throw FileNotYetLockedException
        if (lockFile == null) {
            throw new FileNotYetLockedException("lockFile is null");
        }

        // Perform appending:
        innerPersistence.appendBytes(fileCreationId, bytes);
    }

    /**
     * <p>
     * Closes the file denoted by the fileCreationId argument.
     * </p>
     *
     * @param fileCreationId
     *            the file creation id
     * @throws NullPointerException
     *             if the object is null
     * @throws IllegalArgumentException
     *             if the string is empty
     * @throws FileNotYetLockedException
     *             If file with the given fileCreationId is currently not locked
     * @throws FilePersistenceException
     *             if an exception occurs while performing the operation
     */
    public void closeFile(String fileCreationId) throws FileNotYetLockedException, FilePersistenceException {
        checkString(fileCreationId, "fileCreationId");

        // Check if lock exists:
        File lockFile = (File) creationLockFiles.get(fileCreationId);
        // If lockFile is null, throw FileNotYetLockedException
        if (lockFile == null) {
            throw new FileNotYetLockedException("lockFile is null");
        }

        // Perform closing:
        innerPersistence.closeFile(fileCreationId);
        // Remove file lock
        // Remove mapping
        creationLockFiles.remove(fileCreationId);
        // Remove directory:
        lockFile.delete();
    }

    /**
     * <p>
     * Deletes the file denoted by the given arguments.
     *
     * This method is synchronized.
     * </p>
     *
     * @param fileLocation
     *            the file location
     * @param persistenceFileName
     *            the file name
     * @throws NullPointerException
     *             if any object is null
     * @throws IllegalArgumentException
     *             if any string is empty
     * @throws FileAlreadyLockedException
     *             If file with the given location and name is currently locked
     * @throws FilePersistenceException
     *             if an exception occurs while performing the operation
     */
    public synchronized void deleteFile(String fileLocation, String persistenceFileName)
            throws FileAlreadyLockedException, FilePersistenceException {
        File lockFile = getFileLock(fileLocation, persistenceFileName);

        // Perform deletion:
        innerPersistence.deleteFile(fileLocation, persistenceFileName);
        // Remove file lock:
        lockFile.delete();
    }

    /**
     * <p>
     * Checks the file denoted by the given arguments.
     * </p>
     *
     * @param fileLocation
     *            the file location
     * @param persistenceFileName
     *            the file name
     * @return the file lock
     * @throws NullPointerException
     *             if any object is null
     * @throws IllegalArgumentException
     *             if any string is empty
     * @throws FileAlreadyLockedException
     *             If file with the given location and name is currently locked
     */
    private File getFileLock(String fileLocation, String persistenceFileName) throws FileAlreadyLockedException {
        checkString(fileLocation, "fileLocation");
        checkString(persistenceFileName, "persistenceFileName");

        // Attempt a file lock:
        File lockFile = new File(fileLocation, persistenceFileName + "_lock");
        if (!lockFile.mkdir()) {
            // If it returns false, then the directory already exists: throw FileAlreadyLockedException
            throw new FileAlreadyLockedException("the directory already exists.");
        }

        return lockFile;
    }

    /**
     * <p>
     * Returns a BytesIterator instance that will iterate over the bytes of the file denoted by the given arguments.
     * </p>
     *
     * @param fileLocation
     *            the file location
     * @param persistenceFileName
     *            the file name
     * @return the bytes iterator
     * @throws NullPointerException
     *             if any object is null
     * @throws IllegalArgumentException
     *             if any string is empty
     * @throws FilePersistenceException
     *             if an exception occurs while performing the operation
     */
    public BytesIterator getFileBytesIterator(String fileLocation, String persistenceFileName)
            throws FilePersistenceException {
        return innerPersistence.getFileBytesIterator(fileLocation, persistenceFileName);
    }

    /**
     * <p>
     * Returns the size of the file denoted by the given arguments.
     * </p>
     *
     * @param fileLocation
     *            the file location
     * @param persistenceFileName
     *            the file name
     * @return the size of the file
     * @throws NullPointerException
     *             if any object is null
     * @throws IllegalArgumentException
     *             if any string is empty
     * @throws FilePersistenceException
     *             if an exception occurs while performing the operation
     */
    public long getFileSize(String fileLocation, String persistenceFileName) throws FilePersistenceException {
        return innerPersistence.getFileSize(fileLocation, persistenceFileName);
    }

    /**
     * <p>
     * Disposes this instance. This method is needed as the connection between the client and server could be closed
     * or some exception could occur, and the resources have to be disposed.
     * </p>
     *
     * @throws FilePersistenceException
     *             if an exception occurs while performing the operation
     */
    public void dispose() throws FilePersistenceException {
        // Dispose of writing streams:
        innerPersistence.dispose();

        // Remove file lock:
        // For each entry in creationLockFiles
        for (Iterator itor = creationLockFiles.values().iterator(); itor.hasNext();) {
            // Remove directory:
            ((File) itor.next()).delete();
        }

        // Clear creationLockFiles:
        creationLockFiles.clear();
    }

    /**
     * <p>
     * Checks whether the given Object is null.
     * </p>
     *
     * @param arg
     *            the argument to check
     * @param name
     *            the name of the argument to check
     * @throws NullPointerException
     *             if the given Object is null
     */
    private static void checkNull(Object arg, String name) {
        if (arg == null) {
            throw new NullPointerException(name + " should not be null.");
        }
    }

    /**
     * <p>
     * Checks whether the given String is null or empty.
     * </p>
     *
     * @param arg
     *            the String to check
     * @param name
     *            the name of the String argument to check
     * @throws NullPointerException
     *             if the given string is null
     * @throws IllegalArgumentException
     *             if the given string is empty
     */
    private static void checkString(String arg, String name) {
        checkNull(arg, name);

        if (arg.trim().length() == 0) {
            throw new IllegalArgumentException(name + " should not be empty.");
        }
    }
}
