/*
 * Copyright (C) 2009 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

import java.io.Serializable;

/**
 * <p>A simple <code>DTO</code> representing a single project link.</p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 * @since OR Project Linking assembly
 */
public class ProjectLink implements Serializable {

    /**
     * <p>A <code>ProjectLinkType</code> referencing the type of this project link.</p>
     */
    private ProjectLinkType type = null;

    /**
     * <p>A <code>long</code> providing the ID of a referring project.</p>
     */
    private long sourceProjectId = 0;

    /**
     * <p>A <code>String</code> providing the name of a referring project.</p>
     */
    private String sourceProjectName = null;

    /**
     * <p>A <code>String</code> providing the version of a referring project.</p>
     */
    private String sourceProjectVersion = null;

    /**
     * <p>A <code>long</code> providing the ID of a referred project.</p>
     */
    private long targetProjectId = 0;

    /**
     * <p>A <code>String</code> providing the name of a referred project.</p>
     */
    private String targetProjectName = null;

    /**
     * <p>A <code>String</code> providing the version of a referred project.</p>
     */
    private String targetProjectVersion = null;

    /**
     * <p>Constructs new <code>ProjectLink</code> instance. This implementation does nothing.</p>
     */
    public ProjectLink() {
    }

    /**
     * <p>Constructs new <code>ProjectLink</code> instance with specified type and id/names for source and target
     * projects.</p>
     *
     * @param type a <code>ProjectLinkType</code> referencing the project link type.
     * @param sourceProjectId a <code>long</code> providing the ID of a referring project.
     * @param sourceProjectName a <code>String</code> providing the name of a referring project.
     * @param sourceProjectVersion a <code>String</code> providing the version of a referring project.
     * @param targetProjectId a <code>long</code> providing the ID of a referred project.
     * @param targetProjectName a <code>String</code> providing the name of a referred project.
     * @param targetProjectVersion a <code>String</code> providing the version of a referred project.
     */
    public ProjectLink(ProjectLinkType type, long sourceProjectId, String sourceProjectName,
                       String sourceProjectVersion, long targetProjectId, String targetProjectName,
                       String targetProjectVersion) {
        this.type = type;
        this.sourceProjectId = sourceProjectId;
        this.sourceProjectName = sourceProjectName;
        this.sourceProjectVersion = sourceProjectVersion;
        this.targetProjectId = targetProjectId;
        this.targetProjectName = targetProjectName;
        this.targetProjectVersion = targetProjectVersion;
    }

    /**
     * <p>Gets the type for this project link.</p>
     *
     * @return a <code>ProjectLinkType</code> referencing the project link type.
     */
    public ProjectLinkType getType() {
        return this.type;
    }

    /**
     * <p>Sets the type for this project link.</p>
     *
     * @param type a <code>ProjectLinkType</code> referencing the project link type.
     */
    public void setType(ProjectLinkType type) {
        this.type = type;
    }

    /**
     * <p>Gets the ID for referring project.</p>
     *
     * @return a <code>long</code> providing the ID of a referring project.
     */
    public long getSourceProjectId() {
        return this.sourceProjectId;
    }

    /**
     * <p>Sets the ID for referring project.</p>
     *
     * @param sourceProjectId a <code>long</code> providing the ID of a referring project.
     */
    public void setSourceProjectId(long sourceProjectId) {
        this.sourceProjectId = sourceProjectId;
    }

    /**
     * <p>Gets the name for referring project.</p>
     *
     * @return a <code>String</code> providing the name of a referring project.
     */
    public String getSourceProjectName() {
        return this.sourceProjectName;
    }

    /**
     * <p>Sets the name for referring project.</p>
     *
     * @param sourceProjectName a <code>String</code> providing the name of a referring project.
     */
    public void setSourceProjectName(String sourceProjectName) {
        this.sourceProjectName = sourceProjectName;
    }

    /**
     * <p>Gets the version for referring project.</p>
     *
     * @return a <code>String</code> providing the version of a referring project.
     */
    public String getSourceProjectVersion() {
        return this.sourceProjectVersion;
    }

    /**
     * <p>Sets the version for referring project.</p>
     *
     * @param sourceProjectVersion a <code>String</code> providing the version of a referring project.
     */
    public void setSourceProjectVersion(String sourceProjectVersion) {
        this.sourceProjectVersion = sourceProjectVersion;
    }

    /**
     * <p>Gets the ID for referred project.</p>
     *
     * @return a <code>long</code> providing the ID of a referred project.
     */
    public long getTargetProjectId() {
        return this.targetProjectId;
    }

    /**
     * <p>Sets the ID for referred project.</p>
     *
     * @param targetProjectId a <code>long</code> providing the ID of a referred project.
     */
    public void setTargetProjectId(long targetProjectId) {
        this.targetProjectId = targetProjectId;
    }

    /**
     * <p>Gets the name for referred project.</p>
     *
     * @return a <code>String</code> providing the name of a referred project.
     */
    public String getTargetProjectName() {
        return this.targetProjectName;
    }

    /**
     * <p>Sets the name for referred project.</p>
     *
     * @param targetProjectName a <code>String</code> providing the name of a referred project.
     */
    public void setTargetProjectName(String targetProjectName) {
        this.targetProjectName = targetProjectName;
    }

    /**
     * <p>Gets the version for referred project.</p>
     *
     * @return a <code>String</code> providing the version of a referred project.
     */
    public String getTargetProjectVersion() {
        return this.targetProjectVersion;
    }

    /**
     * <p>Sets the version for referred project.</p>
     *
     * @param targetProjectVersion a <code>String</code> providing the version of a referred project.
     */
    public void setTargetProjectVersion(String targetProjectVersion) {
        this.targetProjectVersion = targetProjectVersion;
    }
}
