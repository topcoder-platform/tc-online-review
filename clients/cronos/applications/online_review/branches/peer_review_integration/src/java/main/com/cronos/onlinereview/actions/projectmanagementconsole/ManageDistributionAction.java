/*
 * Copyright (C) 2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions.projectmanagementconsole;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cronos.onlinereview.Constants;
import com.cronos.onlinereview.model.DynamicModel;
import com.cronos.onlinereview.model.FormFile;
import com.cronos.onlinereview.util.ActionsHelper;
import com.cronos.onlinereview.util.AuthorizationHelper;
import com.cronos.onlinereview.util.ConfigHelper;
import com.cronos.onlinereview.util.CorrectnessCheckResult;
import com.cronos.onlinereview.util.LoggingHelper;
import com.topcoder.management.project.Project;
import com.topcoder.util.distribution.DistributionTool;
import com.topcoder.util.distribution.DistributionToolException;

/**
 * This class is the struts action class which is used to manage distributions for the project.
 * <p>
 * <b>Thread Safety:</b>Struts 2 Action objects are instantiated for each request, so there are no thread-safety issues.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class ManageDistributionAction extends BaseProjectManagementConsoleAction {

    /**
     * Represents the serial version id.
     */
    private static final long serialVersionUID = 1869262448529833439L;

    /**
     * Creates a new instance of the <code>ManageDistributionAction</code> class.
     */
    public ManageDistributionAction() {
    }

    /**
     * <p>
     * Creates a project design distribution file, upload it to the server or return it to the user (or both).
     * </p>
     *
     * @return a <code>String</code> referencing the next view to be displayed to user.
     * @throws Exception if an unexpected error occurs.
     */
    public String execute() throws Exception {

        LoggingHelper.logAction(request);

        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false, request, this);
        if (!verification.isSuccessful()) {
            return verification.getResult();
        }

        // set the active tab index to able to re-render the page with the correct selected tab in
        // case an error occurs
        request.setAttribute("activeTabIdx", 2);

        // Gather the roles the user has for current request
        AuthorizationHelper.gatherUserRoles(request);

        // Check whether the user has the permission to perform this action. If not then redirect the request
        // to log-in page or report about the lack of permissions. Also check that current user is granted a
        // permission to access the details for requested project
        verification
            = ActionsHelper.checkForCorrectProjectId(this, request,
                                                     Constants.PROJECT_MANAGEMENT_PERM_NAME, false);

        if (!verification.isSuccessful()) {
            return verification.getResult();
        }

        // Get the current project
        Project project = verification.getProject();

        // Validate form data
        validateCreateDistributionForm(project, getModel(), request);

        // Check if there were any validation errors identified and return appropriate forward
        if (ActionsHelper.isErrorsPresent(request)) {
            initProjectManagementConsole(request, project);
            return INPUT;
        }

        // Creates the distribution file using the distribution tool component
        File outputDirFile = createDistributionFile(project, getModel(), request);

        try {
            File distributionFile = null;

            if (outputDirFile != null) {
                // Returns the distribution file from the output dir
                distributionFile = getDistributionFile(outputDirFile);
            }

            if (distributionFile == null) {
                ActionsHelper.addErrorToRequest(request,
                    "error.com.cronos.onlinereview.actions.manageProject.Distributions.DistTool.CannotFind");

                initProjectManagementConsole(request, project);
                return INPUT;
            }

            // Check what to do with the file
            boolean uploadToServer = getBooleanFromForm(getModel(), "upload_to_server");
            boolean returnDistribution = getBooleanFromForm(getModel(), "return_distribution");

            if (uploadToServer) {
                // A temporary final variable to be used by an anonymous class.
                final File tempFile = distributionFile;

                DistributionFileDescriptor descriptor = new DistributionFileDescriptor() {
                    public String getFileName() {
                        return tempFile.getName();
                    }

                    public InputStream getInputStream() throws IOException {
                        return new FileInputStream(tempFile);
                    }
                };

                // Saves the distribution file into the catalog directory (it will always be a design distribution)
                if (!saveDistributionFileToCatalog(project, descriptor, request, true)) {
                    initProjectManagementConsole(request, project);
                    return INPUT;
                }
            }

            if (returnDistribution) {

                // Return the distribution file - write it to the response object
                writeDistributionFileToResponse(response, distributionFile);
                return null;

            } else {

                request.getSession().setAttribute("success_upload",
                    this.getText("manageProject.Distributions.Successful_upload"));

                setPid(project.getId());
                return Constants.SUCCESS_FORWARD_NAME;
            }
        } finally {
            if (outputDirFile != null) {
                // cleans output directory file
                cleanupDirectory(outputDirFile);
            }
        }
    }

    /**
     * <p>
     * Writes the distribution file that was recently created to the HTTP response object.
     * </p>
     *
     * @param response an <code>HttpServletResponse</code> representing response outgoing to client.
     * @param distributionFile the distribution file.
     * @throws IOException if any error occurs while reading from the distribution file or writing to the response.
     */
    private void writeDistributionFileToResponse(HttpServletResponse response, File distributionFile)
        throws IOException {

        response.setHeader("Content-Type", "application/octet-stream");
        response.setStatus(HttpServletResponse.SC_OK);
        response.setIntHeader("Content-Length", (int) distributionFile.length());
        response.setHeader("Content-Disposition", "attachment; filename=\"" + distributionFile.getName() + "\"");

        response.flushBuffer();

        copyStream(new FileInputStream(distributionFile), response.getOutputStream());
    }

    /**
     * <p>
     * Creates the distribution file using the distribution tool component.
     * </p>
     *
     * @param project the current project.
     * @param model provides the form parameters mapped to specified request.
     * @param request an <code>HttpServletRequest</code> representing incoming request from the client.
     * @return the distribution file (or null if unable to create the output file or find the script to use).
     * @throws IOException if any error occurs while copying uploaded files.
     */
    private File createDistributionFile(Project project, DynamicModel model, HttpServletRequest request)
        throws IOException {

        String outputDir = getOutputDir(project);

        if (outputDir == null) {
            // Problem creating the output dir
            ActionsHelper.addErrorToRequest(request,
                "error.com.cronos.onlinereview.actions.manageProject.Distributions.OutputDir");
            return null;
        }

        File outputDirFile = new File(outputDir);

        // Create Java design distribution
        Map<String, String> parameters = new HashMap<String, String>();

        // At this point, Project Name and Version are guaranteed to be not null
        String projectName = (String) project.getProperty("Project Name");
        String version = (String) project.getProperty("Project Version");

        parameters.put(DistributionTool.VERSION_PARAM_NAME, version.trim());
        parameters.put(DistributionTool.COMPONENT_NAME_PARAM_NAME, projectName.trim());

        String packageName = (String) getModel().get("distribution_package_name");
        if (!isEmpty(packageName)) {
            parameters.put(DistributionTool.PACKAGE_NAME_PARAM_NAME, packageName.trim());
        }

        parameters.put("output_dir", outputDir);

        // Requirements Specification (validate already to be not null);
        FormFile rsFormFile = (FormFile) getModel().get("distribution_rs");
        File rsFile = createTempFile(outputDir, rsFormFile);

        parameters.put("rs", rsFile.getAbsolutePath());

        // Add additional documents
        int j = 1;
        for (int i = 1; i <= 3; ++i) {
            FormFile additionalFormFile = (FormFile) getModel().get("distribution_additional" + i);

            // Only use additional file if it is uploaded and well set
            if ((additionalFormFile != null) && (additionalFormFile.getFileSize() > 0)
                && !isEmpty(additionalFormFile.getFileName())) {

                File additionalFile = createTempFile(outputDir, additionalFormFile);

                parameters.put("additional_doc" + j, additionalFile.getAbsolutePath());
                ++j;
            }
        }

        // Determines the script that will be used
        String rootCatalogID = (String) project.getProperty("Root Catalog ID");
        try {
            DISTRIBUTION_TOOL.createDistribution(ConfigHelper.getDistributionScript(rootCatalogID), parameters);
        } catch (DistributionToolException ex) {
            ActionsHelper.addErrorToRequest(request, ActionsHelper.GLOBAL_MESSAGE,
                "error.com.cronos.onlinereview.actions.manageProject.Distributions.DistTool.Failure", ex.getMessage());

            return null;
        }

        return outputDirFile;
    }

    /**
     * <p>
     * Returns the generate distribution file. Looks for the file in the output directory. A safe process will leave a
     * single file in there. If more than one is found, return the file with the latest modified date (since the file
     * will be the last one to be generated).
     * </p>
     *
     * @param outputDirFile the output distribution directory.
     * @return the distribution file.
     */
    private File getDistributionFile(File outputDirFile) {
        File distFile = null;
        long lastModified = Long.MIN_VALUE;

        // Should be a single file only, but there might a problem deleting files, so, to be safe,
        // check for the latest file - which should be the distribution file
        File[] files = outputDirFile.listFiles();
        for (File file : files) {
            if (file.isFile() && (file.lastModified() > lastModified)) {
                distFile = file;
                lastModified = file.lastModified();
            }
        }

        return distFile;
    }

    /**
     * <p>
     * Deletes a directory. Traverse deleting all files and sub directories.
     * </p>
     *
     * @param dirFile the directory to clear and delete.
     */
    private void cleanupDirectory(File dirFile) {

        for (File file : dirFile.listFiles()) {
            if (file.isDirectory()) {
                cleanupDirectory(file);
            }

            if (file.isFile()) {
                file.delete();
            }
        }

        dirFile.delete();
    }

    /**
     * <p>
     * Saves the uploaded file to disk and returns its absolute path name.
     * </p>
     *
     * @param outputdir the base directory to save the file.
     * @param formFile the uploaded file.
     * @return the absolute path of the temporary file.
     * @throws IOException if any error occurs while uploading a file.
     */
    private File createTempFile(String outputdir, FormFile formFile)
        throws IOException {

        String dir = outputdir + File.separator + UPLOADED_ARTIFACTS_DIR;
        File output = new File(dir + File.separator + formFile.getFileName());
        FileOutputStream out = new FileOutputStream(output);
        InputStream in = formFile.getInputStream();

        copyStream(in, out);

        return output;
    }

    /**
     * <p>
     * Creates the output directory based on the project ID. This directory will be used to generated the distribution
     * file.
     * </p>
     *
     * @param project the current project.
     * @return the output directory based on the project ID (null if unable to create the directories).
     */
    private String getOutputDir(Project project) {

        String baseDir = ConfigHelper.getDistributionToolOutputDir() + File.separator;
        baseDir = baseDir + project.getId() + "_" + System.currentTimeMillis();

        File dir = new File(baseDir + File.separator + UPLOADED_ARTIFACTS_DIR);

        // Temp directory should not exist, otherwise other files will be considered (this can happen
        // in extremely odd cases of two people submitting files at the same millisecond
        if (dir.exists()) {
            return null;
        }

        if (!dir.mkdirs()) {
            return null;
        }

        return baseDir;
    }
}

