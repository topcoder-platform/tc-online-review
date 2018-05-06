/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.servlet.request;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;


/**
 * <p>
 * This class represents the persistent storage of the uploaded files in the local file system. This class works by
 * saving the uploaded files into a default or specified directory (which should be among the allowed directories). In
 * addition, the user can specify whether overwriting of files (based on the remote file name) is desirable. If
 * overwrite is false, a unique file name is generated for each uploaded file and this is used as the file id. If
 * overwrite is true, the remote file name will simply be used as the file id. This will allow uploads with the same
 * remote file name under a request or different requests.
 * </p>
 *
 * <p>
 * <code>LocalFileUpload</code> can be used to store files whose size is moderately large. The advantage is the saving
 * of memory and persistent storage. It cannot be used in the clustered environment however.
 * </p>
 *
 * <p>
 * Note that content type information is not stored permanently in the persistence. When retrieving the uploaded file
 * with the file id, the content type will not be available.
 * </p>
 *
 * <p>
 * This class should be initialized from a configuration namespace which should be preloaded. In addition to the base
 * class, this class supports extra properties:
 *
 * <ul>
 * <li>
 * allowed_dirs (required) - the allowed directories to write uploaded files under. The default or specified directory
 * should be one of the allowed directories.
 * </li>
 * <li>
 * default_dir (optional) - the default directory to write uploaded files under. It is required if the directory is not
 * specified in the constructor.
 * </li>
 * <li>
 * overwrite (optional) - whether overwriting of files is allowed by default. It is required if the overwrite flag is
 * not specified in the constructor.
 * </li>
 * </ul>
 * </p>
 *
 * <p>
 * <b>Thread Safety: </b> This class is thread-safe by being immutable.
 * </p>
 *
 * @author colau, PE
 * @version 2.0
 */
public class LocalFileUpload extends FileUpload {
    /**
     * <p>
     * The property key specifying the allowed directories to write uploaded files under. The default or specified
     * directory should be one of the allowed directories. This property is required.
     * </p>
     */
    public static final String ALLOWED_DIRS_PROPERTY = "allowed_dirs";

    /**
     * <p>
     * The property key specifying the default directory to write uploaded files under. It is required if the directory
     * is not specified in the constructor.
     * </p>
     */
    public static final String DEFAULT_DIR_PROPERTY = "default_dir";

    /**
     * <p>
     * The property key specifying whether overwriting of files is allowed by default. It is required if the overwrite
     * flag is not specified in the constructor.
     * </p>
     */
    public static final String OVERWRITE_PROPERTY = "overwrite";

    /**
     * <p>
     * Represents the allowed directories to write uploaded files under. The default or specified directory should be
     * one of the allowed directories. It cannot be null, but can be empty. Each String should be non-null and
     * non-empty.
     * </p>
     */
    private final String[] allowedDirs;

    /**
     * <p>
     * Represents the directory to write uploaded files under. It cannot be null or empty string.
     * </p>
     */
    private final String dir;

    /**
     * <p>
     * Represents whether overwriting of files is allowed.
     * </p>
     */
    private final boolean overwrite;

    /**
     * <p>
     * Creates a new instance of <code>LocalFileUpload</code> class to load configuration from the specified namespace.
     * The namespace should be preloaded.
     * </p>
     *
     * @param namespace the configuration namespace to use.
     *
     * @throws IllegalArgumentException if namespace is null or empty string.
     * @throws ConfigurationException if any configuration error occurs.
     * @throws DisallowedDirectoryException if the directory is not one of the allowed directories.
     */
    public LocalFileUpload(String namespace) throws ConfigurationException, DisallowedDirectoryException {
        super(namespace);
        this.allowedDirs = FileUploadHelper.getStringArrayPropertyValue(namespace, ALLOWED_DIRS_PROPERTY);
        this.dir = FileUploadHelper.getStringPropertyValue(namespace, DEFAULT_DIR_PROPERTY, true);
        this.overwrite = FileUploadHelper.getBooleanPropertyValue(namespace, OVERWRITE_PROPERTY);

        // Check if the directory is one of the allowed directories.
        checkDirectoryIsAllowedAndValid();
    }

    /**
     * <p>
     * Creates a new instance of <code>LocalFileUpload</code> class to load configuration from the specified namespace.
     * The namespace should be preloaded. The directory is given.
     * </p>
     *
     * @param namespace the configuration namespace to use.
     * @param dir the directory to write uploaded files under.
     *
     * @throws IllegalArgumentException if namespace or dir is null or empty string.
     * @throws ConfigurationException if any configuration error occurs.
     * @throws DisallowedDirectoryException if the directory is not one of the allowed directories.
     */
    public LocalFileUpload(String namespace, String dir) throws ConfigurationException, DisallowedDirectoryException {
        super(namespace);
        FileUploadHelper.validateString(dir, "dir");

        this.allowedDirs = FileUploadHelper.getStringArrayPropertyValue(namespace, ALLOWED_DIRS_PROPERTY);
        this.dir = dir;
        this.overwrite = FileUploadHelper.getBooleanPropertyValue(namespace, OVERWRITE_PROPERTY);

        // Check if the directory is one of the allowed directories.
        checkDirectoryIsAllowedAndValid();
    }

    /**
     * <p>
     * Creates a new instance of <code>LocalFileUpload</code> class to load configuration from the specified namespace.
     * The namespace should be preloaded. The directory and overwrite flag are given.
     * </p>
     *
     * @param namespace the configuration namespace to use.
     * @param dir the directory to write uploaded files under.
     * @param overwrite whether overwriting of files is allowed.
     *
     * @throws IllegalArgumentException if namespace or dir is null or empty string.
     * @throws ConfigurationException if any configuration error occurs.
     * @throws DisallowedDirectoryException if the directory is not one of the allowed directories.
     */
    public LocalFileUpload(String namespace, String dir, boolean overwrite)
        throws ConfigurationException, DisallowedDirectoryException {
        super(namespace);
        FileUploadHelper.validateString(dir, "dir");

        this.allowedDirs = FileUploadHelper.getStringArrayPropertyValue(namespace, ALLOWED_DIRS_PROPERTY);
        this.dir = dir;
        this.overwrite = overwrite;

        // Check if the directory is one of the allowed directories and valid.
        checkDirectoryIsAllowedAndValid();
    }

    /**
     * <p>
     * Check if the directory is one of the allowed directories.
     * </p>
     *
     * @throws DisallowedDirectoryException if the directory is not one of the allowed directories.
     * @throws ConfigurationException if the dir is not a correct name of the directory.
     */
    private void checkDirectoryIsAllowedAndValid() throws DisallowedDirectoryException, ConfigurationException {
        FileUploadHelper.validateDirectory(this.dir);

        File directory = new File(dir);
        boolean isAllowed = false;

        for (int i = 0; (i < this.allowedDirs.length) && !isAllowed; i++) {
            File curAllowed = new File(allowedDirs[i]);
            String curAllowedPath = new File(this.allowedDirs[i]).getAbsolutePath();

            if (curAllowed.isDirectory()) {
                if (!curAllowedPath.endsWith(File.separator)) {
                    curAllowedPath += File.separator;
                }

                // sub directory can also be regarded as allowed
                if (curAllowed.equals(directory) || directory.getAbsolutePath().startsWith(curAllowedPath)) {
                    isAllowed = true;
                }
            }
        }

        if (!isAllowed) {
            throw new DisallowedDirectoryException(this.dir);
        }
    }

    /**
     * <p>
     * Gets the allowed directories to write uploaded files under.
     * </p>
     *
     * @return the allowed directories to write uploaded files under.
     */
    public String[] getAllowedDirs() {
        return (String[]) this.allowedDirs.clone();
    }

    /**
     * <p>
     * Gets the directory to write uploaded files under.
     * </p>
     *
     * @return the directory to write uploaded files under.
     */
    public String getDir() {
        return this.dir;
    }

    /**
     * <p>
     * Gets whether overwriting of files is allowed (based on remote file name).
     * </p>
     *
     * @return true if upload can overwrite files, false otherwise.
     */
    public boolean canOverwrite() {
        return this.overwrite;
    }

    /**
     * <p>
     * Parses the uploaded files and parameters from the given request and parser. It saves each uploaded file under
     * the directory using a unique file name if overwrite is false, or remote file name if overwrite is true.
     * </p>
     *
     * <p>
     * Note that if there is any failure, the file just written will be deleted. All the previously written files are
     * kept.
     * </p>
     *
     * @param request the servlet request to be parsed.
     * @param parser the parser to use.
     *
     * @return the <code>FileUploadResult</code> containing uploaded files and parameters information.
     *
     * @throws IllegalArgumentException if request or parser is null.
     * @throws RequestParsingException if any I/O error occurs during parsing, or the parsing violates the constraints,
     *         e.g. file size limit is exceeded.
     * @throws PersistenceException if it fails to upload the files to the persistence.
     */
    public FileUploadResult uploadFiles(ServletRequest request, RequestParser parser)
        throws RequestParsingException, PersistenceException {
        FileUploadHelper.validateNotNull(request, "request");
        FileUploadHelper.validateNotNull(parser, "parser");

        synchronized (parser) {
            // parse the request with the parse
            parser.parseRequest(request);

            Map uploadedFiles = new HashMap();
            long currentTotalSize = 0;

            while (parser.hasNextFile()) {
                // get the unique file name (if overwrite is false) or simply use the remote file name
                // (if overwrite is true).
                String fileName = this.overwrite ? ("overwrite_" + parser.getRemoteFileName())
                                                 : getUniqueFileName(parser.getRemoteFileName());

                // get the form file name
                String formFileName = parser.getFormFileName();

                // get the content type
                String contentType = parser.getContentType();

                File uploadedFile = null;
                OutputStream output = null;

                try {
                    uploadedFile = new File(dir, fileName);

                    output = new FileOutputStream(uploadedFile);
                    parser.writeNextFile(output,
                            Math.min(getSingleFileLimit(), getTotalFileLimit() - currentTotalSize));

                    // add the new updated file
                    List files = (List) uploadedFiles.get(formFileName);

                    if (files == null) {
                        files = new ArrayList();
                        uploadedFiles.put(formFileName, files);
                    }

                    files.add(new LocalUploadedFile(uploadedFile, contentType));

                } catch (IOException e) {
                    // if there is any failure, the file just written will be deleted.
                    uploadedFile.delete();
                    throw new PersistenceException("Fails to upload the files to the persistence.", e);
                } catch (FileSizeLimitExceededException e) {
                    uploadedFile.delete();
                    throw e;
                } finally {
                    FileUploadHelper.closeOutputStream(output);
                }

                // update the current total size of the uploaded files
                currentTotalSize += uploadedFile.length();
            }

            return new FileUploadResult(parser.getParameters(), uploadedFiles);
        }
    }

    /**
     * <p>
     * Retrieves the uploaded file from the persistence with the specified file id. In this case, retrieve the file
     * from the local file system. The file id should act the name of the saved file.
     * </p>
     *
     * <p>
     * Note that content type information is not stored permanently in the persistence. When retrieving the uploaded
     * file with the file id, the content type will not be available.
     * </p>
     *
     * @param fileId the id of the file to retrieve.
     * @param refresh whether to refresh the cached file copy, ignored here.
     *
     * @return the uploaded file.
     *
     * @throws IllegalArgumentException if fileId is null or empty string.
     * @throws FileDoesNotExistException if the file does not exist in persistence.
     */
    public UploadedFile getUploadedFile(String fileId, boolean refresh) throws FileDoesNotExistException {
        FileUploadHelper.validateString(fileId, "fileId");

        File file = new File(dir, fileId);

        if (!file.exists()) {
            throw new FileDoesNotExistException(fileId);
        }

        return new LocalUploadedFile(file, null);
    }

    /**
     * <p>
     * Removes the uploaded file from the persistence with the specified file id. Once removed the file contents are
     * lost. In this case, simply remove the file from the local file system. The file id should act the name of the
     * saved file.
     * </p>
     *
     * @param fileId the id of the file to remove.
     *
     * @throws IllegalArgumentException if fileId is null or empty string.
     * @throws FileDoesNotExistException if the file does not exist in persistence.
     */
    public void removeUploadedFile(String fileId) throws FileDoesNotExistException {
        FileUploadHelper.validateString(fileId, "fileId");

        File file = new File(dir, fileId);

        if (!file.exists()) {
            throw new FileDoesNotExistException(fileId);
        }

        file.delete();
    }
}
