package com.topcoder.onlinereview.fixer;

import java.util.LinkedHashSet;
import java.util.Set;

public class ProjectResult {
    private String projectId;
    private String projectName;
    private String projectType;
    private Set<SubmitterResult> submitterResults = new LinkedHashSet<SubmitterResult>();

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
        submitterResults.add(sResult);
    }

    public Set<SubmitterResult> getSubmitterResults() {
        return submitterResults;
    }

    public String getProjectInfo() {
        return getProjectName() + "-" + getProjectType() + "-" + getProjectId();
    }

    public String getSubmissionsResult() {
        StringBuilder result = new StringBuilder();

        for (SubmitterResult sResult : getSubmitterResults()) {
			result.append(sResult);
		}

        return result.toString();
    }

    public String toString() {
        return new StringBuilder(getProjectInfo())
        	.append("\n---------------------------\n")
        	.append(getSubmissionsResult())
        	.toString();
    }
}
