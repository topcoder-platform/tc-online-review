/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.project;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITableMetaData;
import org.dbunit.dataset.Column;

import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * <p>A helper class providing the details for the particluar project which is currently used for testing purposes.</p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class Project {

    /**
     * <p>A <code>Map</code> mapping the <code>String</code> name of the column to <code>String</code> value of the
     * column.</p>
     */
    private Map data = new HashMap();

    /**
     * <p>Constructs new <code>Project</code> instance.</p>
     */
    public Project() {
    }

    public static Map loadAllProjects(IDataSet dataSet) throws Exception {
        Map projects = new LinkedHashMap();
        ITable projectsTable = dataSet.getTable("project");
        for (int i = 0; i < projectsTable.getRowCount(); i++) {
            Project project = loadProject(dataSet, String.valueOf(projectsTable.getValue(i, "project_id")));
            projects.put(project.getId(), project);
        }
        return projects;
    }

    /**
     * <p>Loads the data for requested resource from the data set.</p>
     *
     * @param dataSet an <code>IDataSet</code> providing the data.
     * @param id a <code>String</code> providing the ID of requested project.
     * @return a <code>Project</code> providing the details for requested project.
     * @throws Exception if an unexpected error occurs.
     */
    public static Project loadProject(IDataSet dataSet, String id) throws Exception {
        Project project = new Project();
        ITable projectTable = dataSet.getTable("project");
        for (int i = 0; i < projectTable.getRowCount(); i++) {
            if (String.valueOf(projectTable.getValue(i, "project_id")).equals(id)) {
                fill(project.data, projectTable, i);
                // project_info
                LinkedHashMap projectInfos = new LinkedHashMap();
                project.data.put("infos", projectInfos);
                ITable projectInfosTable = dataSet.getTable("project_info");
                for (int j = 0; j < projectInfosTable.getRowCount(); j++) {
                    if (String.valueOf(projectInfosTable.getValue(j, "project_id")).equals(id)) {
                        Map projectInfoData = new HashMap();
                        fill(projectInfoData, projectInfosTable, j);
                        projectInfos.put(projectInfoData.get("project_info_type_id"), projectInfoData);
                    }
                }
                // phase
                LinkedHashMap projectPhases = new LinkedHashMap();
                project.data.put("phases", projectPhases);
                ITable projectPhasesTable = dataSet.getTable("project_phase");
                for (int j = 0; j < projectPhasesTable.getRowCount(); j++) {
                    if (String.valueOf(projectPhasesTable.getValue(j, "project_id")).equals(id)) {
                        Map phaseData = new HashMap();
                        fill(phaseData, projectPhasesTable, j);
                        projectPhases.put(phaseData.get("project_phase_id"), phaseData);
                    }
                }
                // submissions
                LinkedHashMap projectSubmissions = new LinkedHashMap();
                project.data.put("submissions", projectSubmissions);
                if (containsTable(dataSet, "resource")) {
                    ITable resourcesTable = dataSet.getTable("resource");
                    ITable resourceSubmissionsTable = dataSet.getTable("resource_submission");
                    ITable submissionsTable = dataSet.getTable("submission");
                    for (int j = 0; j < resourcesTable.getRowCount(); j++) {
                        if (String.valueOf(resourcesTable.getValue(j,"project_id")).equals(id)) {
                            String resourceId = String.valueOf(resourcesTable.getValue(j,"resource_id"));
                            for (int k = 0; k < resourceSubmissionsTable.getRowCount(); k++) {
                                if (String.valueOf(resourceSubmissionsTable.getValue(k, "resource_id")).equals(resourceId)) {
                                    for (int m = 0; m < submissionsTable.getRowCount(); m++) {
                                        if (String.valueOf(submissionsTable.getValue(m, "submission_id")).equals(
                                            String.valueOf(resourceSubmissionsTable.getValue(k, "submission_id")))) {
                                            Map submissionData = new LinkedHashMap();
                                            fill(submissionData, submissionsTable, m);
                                            projectSubmissions.put(submissionData.get("submission_id"), submissionData);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return project;
    }

    /**
     * <p>Fills the specified <code>Map</code> with data from the specified table and row.</p>
     *
     * @param data a <code>Map</code> to be filled with row data.
     * @param table an <code>ITable</code> providing the data from current table.
     * @param row an <code>int</code> providing the index of current row to load data from.
     * @throws DataSetException if an unexpected error occurs.
     */
    private static void fill(Map data, ITable table, int row) throws DataSetException {
        ITableMetaData tableMetaData = table.getTableMetaData();
        Column[] columns = tableMetaData.getColumns();
        for (int i = 0; i < columns.length; i++) {
            data.put(columns[i].getColumnName(), String.valueOf(table.getValue(row, columns[i].getColumnName())));
        }
    }

    /**
     * <p>Gets the ID of this project.</p>
     *
     * @return a <code>String</code> providing the ID of this project.
     */
    public String getId() {
        return (String) this.data.get("project_id");
    }

    /**
     * <p>Gets the ID of the category for this project.</p>
     *
     * @return a <code>String</code> providing the ID of category for this project.
     */
    public String getCategoryId() {
        return (String) this.data.get("project_category_id");
    }

    /**
     * <p>Gets the project name.</p>
     *
     * @return a <code>String</code> providing the project name.
     */
    public String getName() {
        Map infos = (Map) this.data.get("infos");
        Map handleInfo = (Map) infos.get("6");
        return (String) handleInfo.get("value");
    }

    /**
     * <p>Gets the project version.</p>
     *
     * @return a <code>String</code> providing the project version.
     */
    public String getVersion() {
        Map infos = (Map) this.data.get("infos");
        Map handleInfo = (Map) infos.get("7");
        return (String) handleInfo.get("value");
    }

    /**
     * <p>Gets the project submissions.</p>
     *
     * @return a <code>Map</code> mapping the <code>String</code> submission ID to <code>Map</code> of submission
     *         properties.
     */
    public Map getSubmissions() {
        return (Map) this.data.get("submissions");
    }

    /**
     * <p>Gets the external ID for the winner of this project.</p>
     *
     * @return a <code>String</code> providing the external ID for the winner of this project.
     */
    public String getWinnerExternalId() {
        Map infos = (Map) this.data.get("infos");
        Map handleInfo = (Map) infos.get("23");
        return (String) handleInfo.get("value");
    }

    private static boolean containsTable(IDataSet dataSet, String tableName) throws DataSetException {
        String[] tableNames = dataSet.getTableNames();
        for (int i = 0; i < tableNames.length; i++) {
            if (tableNames[i].equalsIgnoreCase(tableName)) {
                return true;
            }
        }
        return false;
    }
}
