/*
 * Copyright (C) 2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions.projectmanagementconsole;


import java.io.IOException;
import java.io.InputStream;

import com.cronos.onlinereview.Constants;
import com.cronos.onlinereview.model.FormFile;
import com.cronos.onlinereview.util.ActionsHelper;
import com.cronos.onlinereview.util.AuthorizationHelper;
import com.cronos.onlinereview.util.CorrectnessCheckResult;
import com.cronos.onlinereview.util.LoggingHelper;
import com.topcoder.management.project.Project;

/**
 * This class is the struts action class which is used for uploading distribution page.
 * <p>
 * <b>Thread Safety:</b>Struts 2 Action objects are instantiated for each request, so there are no thread-safety issues.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class UploadDistributionAction extends BaseProjectManagementConsoleAction {

    /**
     * Represents the serial version id.
     */
    private static final long serialVersionUID = 6635034302518938963L;

    /**
     * Creates a new instance of the <code>UploadDistributionAction</code> class.
     */
    public UploadDistributionAction() {
    }

    /**
     * <p>
     * Uploads a project distribution file (design or development).
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

        Project project = verification.getProject();
        long projectCategoryId = project.getProjectCategory().getId();

        // Validate project type - must be a Design or Development
        if ((projectCategoryId != DESIGN_PROJECT_ID) && (projectCategoryId != DEVELOPMENT_PROJECT_ID)) {
            ActionsHelper.addErrorToRequest(request,
                "error.com.cronos.onlinereview.actions.manageProject.Distributions.Upload.ProjectCategory");

            initProjectManagementConsole(request, project);
            return INPUT;
        }

        final FormFile distributionFormFile = (FormFile) getModel().get("distribution_file");

        if (distributionFormFile == null || distributionFormFile.getFileSize() == 0) {
            ActionsHelper.addErrorToRequest(request, "distribution_file",
                "error.com.cronos.onlinereview.actions.manageProject.Distributions.Distribution.Empty");

            initProjectManagementConsole(request, project);
            return INPUT;
        }

        DistributionFileDescriptor descriptor = new DistributionFileDescriptor() {
            public String getFileName() {
                return distributionFormFile.getFileName();
            }

            public InputStream getInputStream() throws IOException {
                return distributionFormFile.getInputStream();
            }
        };

        boolean isDesign = (projectCategoryId == DESIGN_PROJECT_ID);

        // Saves the distribution file into the catalog directory
        if (!saveDistributionFileToCatalog(project, descriptor, request, isDesign)) {

            initProjectManagementConsole(request, project);
            return INPUT;
        }

        request.getSession().setAttribute("success_upload",
            getText("manageProject.Distributions.Successful_upload"));

        this.setPid(project.getId());
        return Constants.SUCCESS_FORWARD_NAME;
    }
}

