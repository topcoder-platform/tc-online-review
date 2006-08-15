/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration.dto.oldschema.project;

/**
 * The CompVersions DTO.
 *
 * @author brain_cn
 * @version 1.0
 */
public class CompVersions {
    /** Represents the comp_versions table name. */
    public static final String TABLE_NAME = "comp_versions";

    /** Represents comp_vers_id field name. */
    public static final String COMP_VERS_ID_NAME = "comp_vers_id";

    /** Represents component_id field name. */
    public static final String COMPONENT_ID_NAME = "component_id";

    /** Represents version field name. */
    public static final String VERSION_NAME = "version";

    /** Represents version_text field name. */
    public static final String VERSION_TEXT_NAME = "version_text";

    /** Represents phase_id field name. */
    public static final String PHASE_ID_NAME = "phase_id";
    private int componentId;
    private int version;
    private String versionText;
    private int compVersId;
    private int phaseId;

    /**
     * Returns the componentId.
     *
     * @return Returns the componentId.
     */
    public int getComponentId() {
        return componentId;
    }

    /**
     * Set the componentId.
     *
     * @param componentId The componentId to set.
     */
    public void setComponentId(int componentId) {
        this.componentId = componentId;
    }

    /**
     * Returns the version.
     *
     * @return Returns the version.
     */
    public int getVersion() {
        return version;
    }

    /**
     * Set the version.
     *
     * @param version The version to set.
     */
    public void setVersion(int version) {
        this.version = version;
    }

    /**
     * Returns the versionText.
     *
     * @return Returns the versionText.
     */
    public String getVersionText() {
        return versionText;
    }

    /**
     * Set the versionText.
     *
     * @param versionText The versionText to set.
     */
    public void setVersionText(String versionText) {
        this.versionText = versionText;
    }

    /**
     * Returns the compVersId.
     *
     * @return Returns the compVersId.
     */
    public int getCompVersId() {
        return compVersId;
    }

    /**
     * Set the compVersId.
     *
     * @param compVersId The compVersId to set.
     */
    public void setCompVersId(int compVersId) {
        this.compVersId = compVersId;
    }

    /**
     * Returns the phaseId.
     *
     * @return Returns the phaseId.
     */
    public int getPhaseId() {
        return phaseId;
    }

    /**
     * Set the phaseId.
     *
     * @param phaseId The phaseId to set.
     */
    public void setPhaseId(int phaseId) {
        this.phaseId = phaseId;
    }
}
