/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.topcoder.util.generator.guid.Generator;
import com.topcoder.util.generator.guid.UUIDType;
import com.topcoder.util.generator.guid.UUIDUtility;

/**
 * This is the default implementation of the FilePersistence interface that works with files from the file system. The
 * creation of the file is performed in 3 steps: 1. createFile() - the file is created and the user gets a file id to
 * use in the next steps. 2. appendBytes() - this method should be used several times by the user to write all the
 * bytes. 3. closeFile() - the file is closed It is not fully thread safe as the creation of the file should be
 * performed by only one thread, or the file could be corrupted (and no other user should be aware of the file's id in
 * order to try to access the file). The deleteFile and getFileBytesIterator could be accessed by multiple threads, so
 * the methods are synchronized.
 * @author Luca, FireIce
 * @version 1.0
 */
public class FileSystemPersistence implements FilePersistence {

    /**
     * Default Iterator Byte Size.
     */
    private static final int DEFAULT_ITERATORBYTE_SIZE = 1024;

    /**
     * Represents the GUID generator used to generate unique file ids for the files to be created. Initialized in the
     * constructor and never changed later. Not null.
     */
    private final Generator generator = UUIDUtility.getGenerator(UUIDType.TYPEINT32);

    /**
     * Represents the map of output streams for the files that have been created and are not fully written. Initialized
     * in the constructor and never changed later. Not null. Can be empty. The keys are strings (the file creation ids)
     * and values are OutputStreams (the file output streams). A new pair is added by the createFile() method. The
     * output stream is queryied by the appendBytes() method (to write the bytes). The pair is removed by the
     * closeFile() method (and the output stream is closed).
     */
    private final Map openOutputStreams = new HashMap();

    /**
     * Represents the number of bytes each call to the iterators' nextBytes method will return (the bytes iterators
     * created by this instance). Initialized in the constructor and never changed later. Positive.
     */
    private int iteratorByteSize;

    /**
     * Creates the instance with a default value for iteratorByteSize.
     */
    public FileSystemPersistence() {
        this(DEFAULT_ITERATORBYTE_SIZE);
    }

    /**
     * Creates the instance with the given argument.
     * @param iteratorByteSize
     *            the number of bytes each call to the iterators' nextBytes method will return
     * @throws IllegalArgumentException
     *             if the int argument is not positive
     */
    public FileSystemPersistence(int iteratorByteSize) {
        if (iteratorByteSize <= 0) {
            throw new IllegalArgumentException("iteratorByteSize should be positive");
        }
        this.iteratorByteSize = iteratorByteSize;
    }

    /**
     * Creates a new file using the given arguments. It returns the file creation id to be used by the user to append
     * the bytes and to close the file.
     * @param fileLocation
     *            the file location
     * @param persistenceFileName
     *            the file name
     * @return the file creation id
     * @throws NullPointerException
     *             if any object is null
     * @throws IllegalArgumentException
     *             if any string is empty
     * @throws FilePersistenceException
     *             if an exception occurs while performing the operation
     */
    public String createFile(String fileLocation, String persistenceFileName) throws FilePersistenceException {
        if (fileLocation == null) {
            throw new NullPointerException("fileLocation is null");
        } else if (fileLocation.trim().length() == 0) {
            throw new IllegalArgumentException("fileLocation is empty");
        }
        if (persistenceFileName == null) {
            throw new NullPointerException("persistenceFileName is null");
        } else if (persistenceFileName.trim().length() == 0) {
            throw new IllegalArgumentException("persistenceFileName is empty");
        }
        // create an output stream to the file
        OutputStream os;
        try {
            os = new FileOutputStream(new File(fileLocation, persistenceFileName));
        } catch (FileNotFoundException e) {
            throw new FilePersistenceException("create FileOutputStream fails", e);
        }
        // get the next fileCreationId
        String fileCreationId = createUniqueFileId();
        // add the fileId-outputStream pair to the map
        openOutputStreams.put(fileCreationId, os);
        return fileCreationId;
    }

    /**
     * Appends the array of bytes to the file denoted by the fileCreationId argument. If any exception occurs, the
     * output stream is closed and removed from the map.
     * @param fileCreationId
     *            the file creation id
     * @param bytes
     *            the bytes to be appended to the file
     * @throws NullPointerException
     *             if any object is null
     * @throws IllegalArgumentException
     *             if the string is empty
     * @throws FilePersistenceException
     *             if an exception occurs while performing the operation
     */
    public void appendBytes(String fileCreationId, byte[] bytes) throws FilePersistenceException {
        if (fileCreationId == null) {
            throw new NullPointerException("fileCreationId is null");
        } else if (fileCreationId.trim().length() == 0) {
            throw new IllegalArgumentException("fileCreateId is empty");
        }
        if (bytes == null) {
            throw new NullPointerException("bytes is null");
        }
        // get the file output stream
        OutputStream os = (OutputStream) openOutputStreams.get(fileCreationId);
        if (os == null) {
            throw new FilePersistenceException("no output stream associate with fileCreationId " + fileCreationId);
        }
        // append the bytes
        try {
            os.write(bytes);
        } catch (IOException e) {
            try {
                os.close();
            } catch (IOException e1) {
                throw new FilePersistenceException("stream cann't close");
            }
            openOutputStreams.remove(fileCreationId);
            throw new FilePersistenceException("I/O error");
        }
    }

    /**
     * Closes the file denoted by the fileCreationId argument.
     * @param fileCreationId
     *            the file creation id
     * @throws NullPointerException
     *             if the object is null
     * @throws IllegalArgumentException
     *             if the string is empty
     * @throws FilePersistenceException
     *             if an exception occurs while performing the operation
     */
    public void closeFile(String fileCreationId) throws FilePersistenceException {
        if (fileCreationId == null) {
            throw new NullPointerException("fileCreationId is null");
        } else if (fileCreationId.trim().length() == 0) {
            throw new IllegalArgumentException("fileCreationId is empty");
        }
        // get the output stream
        OutputStream os = (OutputStream) openOutputStreams.get(fileCreationId);
        if (os != null) {
            try {
                os.close();
            } catch (IOException e) {
                throw new FilePersistenceException("stream failed to close");
            } finally {
                openOutputStreams.remove(fileCreationId);
            }
        } else {
            throw new FilePersistenceException("no open output stream associate with id " + fileCreationId);
        }
    }

    /**
     * Deletes the file denoted by the given arguments.
     * @param fileLocation
     *            the file location
     * @param persistenceFileName
     *            the file name
     * @throws NullPointerException
     *             if any object is null
     * @throws IllegalArgumentException
     *             if any string is empty
     * @throws FilePersistenceException
     *             if an exception occurs while performing the operation
     */
    public synchronized void deleteFile(String fileLocation, String persistenceFileName)
        throws FilePersistenceException {
        if (fileLocation == null) {
            throw new NullPointerException("fileLocation is null");
        } else if (fileLocation.trim().length() == 0) {
            throw new IllegalArgumentException("fileLocation is empty");
        }
        if (persistenceFileName == null) {
            throw new NullPointerException("persistenceFileName is null");
        } else if (persistenceFileName.trim().length() == 0) {
            throw new IllegalArgumentException("persistenceFileName is empty");
        }
        File fileToDel = new File(fileLocation, persistenceFileName);
        if (fileToDel.exists()) {
            if (fileToDel.isDirectory()) {
                throw new FilePersistenceException("file " + fileToDel.getAbsolutePath() + " is a directory");
            } else if (!fileToDel.delete()) {
                throw new FilePersistenceException("file cannot delete");
            }
        } else {
            throw new FilePersistenceException("file does not exist");
        }
    }

    /**
     * Returns a BytesIterator instance that will iterate over the bytes of the file denoted by the given arguments.
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
    public synchronized BytesIterator getFileBytesIterator(String fileLocation, String persistenceFileName)
        throws FilePersistenceException {
        if (fileLocation == null) {
            throw new NullPointerException("fileLocation is null");
        } else if (fileLocation.trim().length() == 0) {
            throw new IllegalArgumentException("fileLocation is empty");
        }
        if (persistenceFileName == null) {
            throw new NullPointerException("persistenceFileName is null");
        } else if (persistenceFileName.trim().length() == 0) {
            throw new IllegalArgumentException("persistenceFileName is empty");
        }
        try {
            return new InputStreamBytesIterator(new FileInputStream(new File(fileLocation, persistenceFileName)),
                    iteratorByteSize);
        } catch (FileNotFoundException e) {
            throw new FilePersistenceException("file not exist", e);
        }
    }

    /**
     * Returns the size of the file denoted by the given arguments.
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
        if (fileLocation == null) {
            throw new NullPointerException("fileLocation is null");
        } else if (fileLocation.trim().length() == 0) {
            throw new IllegalArgumentException("fileLocation is empty");
        }
        if (persistenceFileName == null) {
            throw new NullPointerException("persistenceFileName is null");
        } else if (persistenceFileName.trim().length() == 0) {
            throw new IllegalArgumentException("persistenceFileName is empty");
        }
        return new File(fileLocation, persistenceFileName).length();
    }

    /**
     * Disposes this instance. This method is needed as the connection between the client and server could be closed or
     * some exception could occur, and the resources have to be disposed.
     * @throws FilePersistenceException
     *             if an exception occurs while performing the operation
     */
    public void dispose() throws FilePersistenceException {
        for (Iterator iter = openOutputStreams.values().iterator(); iter.hasNext();) {
            OutputStream os = (OutputStream) iter.next();
            try {
                os.close();
            } catch (IOException e) {
                throw new FilePersistenceException("fail to close stream");
            }
        }
        openOutputStreams.clear();
    }

    /**
     * Creates a unique String id using the GUID Generator. It is used by the createFile method.
     * @return a unique String id
     */
    private String createUniqueFileId() {
        return generator.getNextUUID().toString();
    }
}
