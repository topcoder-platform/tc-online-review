package com.topcoder.onlinereview.fixer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProjectResult {
    private String projectId;

    private String projectName;

    private String projectType;

    private List submitterResults = new ArrayList();

    private Set duplicateResultChecker = new HashSet();

    public void setProjectId(String projectId) {
        if (projectId == null) {
            throw new IllegalArgumentException("projectId cannot be null");
        }
        this.projectId = projectId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectName(String projectName) {
        if (projectName == null) {
            throw new IllegalArgumentException("name cannot be null.");
        }

        this.projectName = projectName;
    }

    public String getProjectName() {
        return this.projectName;
    }

    public void setProjectType(String projectType) {
        if (projectType == null) {
            throw new IllegalArgumentException("type cannot be null.");
        }

        this.projectType = projectType;
    }

    public String getProjectType() {
        return projectType;
    }

    public void addSubmitterResult(SubmitterResult sResult) {
        if (sResult == null) {
            throw new IllegalArgumentException("sResult cannot be null.");
        }

        if (duplicateResultChecker.contains(new Integer(sResult.getSubmissionId()))) {
            return;
        } else {
            duplicateResultChecker.add(new Integer(sResult.getSubmissionId()));
        }

        submitterResults.add(sResult);
    }

    public List getSubmitterResults() {
        return submitterResults;
    }

    public String getProjectInfo() {
        return getProjectName() + "-" + getProjectType() + "-" + getProjectId();
    }

    public String getSubmissionsResult() {
        String result = "";
        
        List sResults = getSubmitterResults();

        for (int i = 0; i < sResults.size(); ++i) {
            result += sResults.get(i).toString();
        }

        return result;
    }

    public String toString() {
        String result = getProjectInfo() + "\n";

        result += "---------------------------\n";

        result += getSubmissionsResult();

        return result;
    }
}
