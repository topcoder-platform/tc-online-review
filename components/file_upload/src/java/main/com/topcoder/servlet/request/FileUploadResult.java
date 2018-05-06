/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.servlet.request;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


/**
 * <p>
 * A class that encapsulates the file upload information collected during the parsing of the request. This includes
 * uploaded files and form parameters. The APIs for the form parameters resemble the <code>HttpServletRequest</code>
 * for easy grasp of usage.
 * </p>
 *
 * <p>
 * Instances of this class should be created by the <code>FileUpload</code>. Users should retrieve the information in
 * an instance returned from the uploadFiles() method.
 * </p>
 *
 * <p>
 * This class implements the <code>Serializable</code> interface so that it can be stored as session data in applicable
 * scenarios.
 * </p>
 *
 * <p>
 * <b>Thread Safety: </b>This class is thread-safe by being immutable.
 * </p>
 *
 * @author colau, PE
 * @version 2.0
 */
public class FileUploadResult implements Serializable {
    /**
     * <p>
     * Represents the mapping of form parameters. The key should be parameter name (String), the value should be a list
     * (List) of parameter values (String). It can be empty.
     * </p>
     */
    private final Map parameters;

    /**
     * <p>
     * Represents the mapping from form file names (String) to a list (List) of uploaded files (UploadedFile). It can
     * be empty.
     * </p>
     */
    private final Map uploadedFiles;

    /**
     * <p>
     * Creates a new instance of <code>FileUploadResult</code> class. Normally this constructor should be called by the
     * <code>FileUpload</code> class.
     * </p>
     *
     * <p>
     * <code>parameters</code> should be a mapping from parameter names (String) to a list (List) of parameter values
     * (String). <code>uploadedFiles</code> should be a mapping from form file names (String) to a list (List) of
     * uploaded files (UploadedFile).
     * </p>
     *
     * @param parameters a map from parameter names to parameter values.
     * @param uploadedFiles a map from form file names to uploaded files.
     *
     * @throws IllegalArgumentException if parameters or uploadedFiles is null or contains elements of the wrong type.
     */
    public FileUploadResult(Map parameters, Map uploadedFiles) {
        // validate the parameters
        FileUploadHelper.validateNotNull(parameters, "parameters");
        FileUploadHelper.validateNotNull(uploadedFiles, "uploadedFiles");

        for (Iterator iter = parameters.entrySet().iterator(); iter.hasNext();) {
            Entry entry = (Entry) iter.next();

            if (!(entry.getKey() instanceof String)) {
                throw new IllegalArgumentException("The key elements of parameters should be type of String.");
            }

            if (!(entry.getValue() instanceof List)) {
                throw new IllegalArgumentException("The value elements of parameters should be type of List.");
            }

            List list = (List) entry.getValue();

            for (Iterator listIter = list.iterator(); listIter.hasNext();) {
                if (!(listIter.next() instanceof String)) {
                    throw new IllegalArgumentException(
                        "The value elements of parameters should only contain String element.");
                }
            }
        }

        for (Iterator iter = uploadedFiles.entrySet().iterator(); iter.hasNext();) {
            Entry entry = (Entry) iter.next();

            if (!(entry.getKey() instanceof String)) {
                throw new IllegalArgumentException("The key elements of uploadedFiles should be type of String.");
            }

            if (!(entry.getValue() instanceof List)) {
                throw new IllegalArgumentException("The value elements of uploadedFiles should be type of List.");
            }

            List list = (List) entry.getValue();

            for (Iterator listIter = list.iterator(); listIter.hasNext();) {
                if (!(listIter.next() instanceof UploadedFile)) {
                    throw new IllegalArgumentException(
                        "The value elements of uploadedFiles should only contain UploadedFile element.");
                }
            }
        }

        this.parameters = new HashMap(parameters);
        this.uploadedFiles = new HashMap(uploadedFiles);
    }

    /**
     * <p>
     * Gets the array of parameter names.
     * </p>
     *
     * @return the array of parameter names.
     */
    public String[] getParameterNames() {
        return (String[]) parameters.keySet().toArray(new String[parameters.keySet().size()]);
    }

    /**
     * <p>
     * Gets the multiple parameter values with the specified parameter name. If the parameter is not found, an empty
     * array is returned.
     * </p>
     *
     * @param name the name of the parameter.
     *
     * @return the values of the parameter.
     *
     * @throws IllegalArgumentException if name is null or empty string
     */
    public String[] getParameterValues(String name) {
        FileUploadHelper.validateString(name, "name");

        List values = (List) this.parameters.get(name);

        return (values == null) ? new String[0] : (String[]) values.toArray(new String[values.size()]);
    }

    /**
     * <p>
     * Gets the single parameter value with the specified parameter name. If there are multiple parameter values,
     * return the first one. If the parameter is not found, simply return null.
     * </p>
     *
     * @param name the name of the parameter.
     *
     * @return the value of the parameter.
     *
     * @throws IllegalArgumentException if name is null or empty string.
     */
    public String getParameter(String name) {
        FileUploadHelper.validateString(name, "name");

        List values = (List) this.parameters.get(name);

        if (values == null) {
            return null;
        }

        if (values.size() == 0) {
            return null;
        }

        return (String) values.get(0);
    }

    /**
     * <p>
     * Gets the array of the form file names.
     * </p>
     *
     * @return an array of the form file names.
     */
    public String[] getFormFileNames() {
        return (String[]) uploadedFiles.keySet().toArray(new String[uploadedFiles.keySet().size()]);
    }

    /**
     * <p>
     * Gets the multiple uploaded files with the specified form file name. If the form file name is not found, an empty
     * array is returned.
     * </p>
     *
     * @param formFileName the form file name.
     *
     * @return the uploaded files.
     *
     * @throws IllegalArgumentException if formFileName is null or empty string.
     */
    public UploadedFile[] getUploadedFiles(String formFileName) {
        FileUploadHelper.validateString(formFileName, "formFileName");

        List files = (List) this.uploadedFiles.get(formFileName);

        return (files == null) ? new UploadedFile[0] : (UploadedFile[]) files.toArray(new UploadedFile[files.size()]);
    }

    /**
     * <p>
     * Gets the single uploaded file with the specified form file name. If there are multiple uploaded files, return
     * the first one. If the form file name is not found, simply return null.
     * </p>
     *
     * @param formFileName the form file name.
     *
     * @return the uploaded file.
     *
     * @throws IllegalArgumentException if formFileName is null or empty string.
     */
    public UploadedFile getUploadedFile(String formFileName) {
        FileUploadHelper.validateString(formFileName, "formFileName");

        List files = (List) this.uploadedFiles.get(formFileName);

        if (files == null) {
            return null;
        }

        if (files.size() == 0) {
            return null;
        }

        return (UploadedFile) files.get(0);
    }

    /**
     * <p>
     * Gets all the uploaded files contained in this result. If not available, an empty array is returned.
     * </p>
     *
     * @return the uploaded files contained in this result.
     */
    public UploadedFile[] getAllUploadedFiles() {
        List files = new ArrayList();

        for (Iterator iter = this.uploadedFiles.values().iterator(); iter.hasNext();) {
            // aggregate all the elements into a new list
            files.addAll((List) iter.next());
        }

        return (UploadedFile[]) files.toArray(new UploadedFile[files.size()]);
    }
}
