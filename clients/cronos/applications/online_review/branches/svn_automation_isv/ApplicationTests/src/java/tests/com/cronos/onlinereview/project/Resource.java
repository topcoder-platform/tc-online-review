/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.project;

import org.dbunit.dataset.Column;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITableMetaData;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.NoSuchTableException;

import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * <p></p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class Resource {

    /**
     * <p>A <code>Map</code> mapping the <code>String</code> name of the column to <code>String</code> value of the
     * column.</p>
     */
    private Map data = new HashMap();

    /**
     * <p>Constructs new <code>Resource</code> instance.</p>
     */
    public Resource() {
    }

    /**
     * <p>Loads the data for requested resource from the data set.</p>
     *
     * @param dataSet an <code>IDataSet</code> providing the data.
     * @param id a <code>String</code> providing the ID of requested resource.
     * @return a <code>Resource</code> providing the details for requested resource.
     * @throws Exception if an unexpected error occurs.
     */
    public static Resource loadResource(IDataSet dataSet, String id) throws Exception {
        Resource resource = new Resource();
        ITable resourceTable = dataSet.getTable("resource");
        for (int i = 0; i < resourceTable.getRowCount(); i++) {
            if (String.valueOf(resourceTable.getValue(i, "resource_id")).equals(id)) {
                fill(resource.data, resourceTable, i);
                LinkedHashMap resourceInfos = new LinkedHashMap();
                resource.data.put("infos", resourceInfos);
                ITable resourceInfosTable = dataSet.getTable("resource_info");
                for (int j = 0; j < resourceInfosTable.getRowCount(); j++) {
                    if (String.valueOf(resourceInfosTable.getValue(j, "resource_id")).equals(id)) {
                        Map resourceInfoData = new HashMap();
                        fill(resourceInfoData, resourceInfosTable, j);
                        resourceInfos.put(resourceInfoData.get("resource_info_type_id"), resourceInfoData);
                    }
                }
            }
        }
        return resource;
    }

    /**
     * <p>Loads the data for requested resource referenced by handle from the data set.</p>
     *
     * @param dataSet an <code>IDataSet</code> providing the data.
     * @param handle a <code>String</code> providing the ID of requested resource.
     * @return a <code>Resource</code> providing the details for requested resource.
     * @throws Exception if an unexpected error occurs.
     */
    public static Resource loadResourceByHandle(IDataSet dataSet, String handle) throws Exception {
        ITable resourceTable = dataSet.getTable("resource");
        for (int i = 0; i < resourceTable.getRowCount(); i++) {
            Resource resource = new Resource();
            fill(resource.data, resourceTable, i);
            LinkedHashMap resourceInfos = new LinkedHashMap();
            resource.data.put("infos", resourceInfos);
            ITable resourceInfosTable = dataSet.getTable("resource_info");
            boolean found = false;
            for (int j = 0; j < resourceInfosTable.getRowCount(); j++) {
                if (String.valueOf(resourceInfosTable.getValue(j, "resource_id"))
                    .equals(resource.data.get("resource_id"))) {
                    Map resourceInfoData = new HashMap();
                    fill(resourceInfoData, resourceInfosTable, j);
                    resourceInfos.put(resourceInfoData.get("resource_info_type_id"), resourceInfoData);
                    if (resourceInfoData.get("resource_info_type_id").equals("2")) {
                      if (resourceInfoData.get("value").equals(handle)) {
                          found = true;
                      }
                    }
                }
            }
            if (found) {
                return resource;
            }
        }
        throw new IllegalStateException("The resource matching the handle [" + handle + "] is not found");
    }

    /**
     * <p>Loads the data for requested resource referenced by handle from the data set.</p>
     *
     * @param dataSet an <code>IDataSet</code> providing the data.
     * @param id a <code>String</code> providing the ID of requested resource.
     * @return a <code>Resource</code> providing the details for requested resource.
     * @throws Exception if an unexpected error occurs.
     */
    public static Resource[] loadResourceByExternalId(IDataSet dataSet, String id) throws Exception {
        List result = new ArrayList();
        ITable resourceTable = dataSet.getTable("resource");
        for (int i = 0; i < resourceTable.getRowCount(); i++) {
            Resource resource = new Resource();
            fill(resource.data, resourceTable, i);
            LinkedHashMap resourceInfos = new LinkedHashMap();
            resource.data.put("infos", resourceInfos);
            ITable resourceInfosTable = dataSet.getTable("resource_info");
            boolean found = false;
            for (int j = 0; j < resourceInfosTable.getRowCount(); j++) {
                if (String.valueOf(resourceInfosTable.getValue(j, "resource_id"))
                    .equals(resource.data.get("resource_id"))) {
                    Map resourceInfoData = new HashMap();
                    fill(resourceInfoData, resourceInfosTable, j);
                    resourceInfos.put(resourceInfoData.get("resource_info_type_id"), resourceInfoData);
                    if (resourceInfoData.get("resource_info_type_id").equals("1")) {
                      if (resourceInfoData.get("value").equals(id)) {
                          found = true;
                      }
                    }
                }
            }
            if (found) {
                LinkedHashMap resourceSubmissions = new LinkedHashMap();
                resource.data.put("submissions", resourceSubmissions);
                try {
                    ITable resourceSubmissionsTable = dataSet.getTable("resource_submission");
                    ITable submissionsTable = dataSet.getTable("resource_submission");
                    for (int j = 0; j < resourceSubmissionsTable.getRowCount(); j++) {
                        if (String.valueOf(resourceSubmissionsTable.getValue(j, "resource_id")).equals(resource.getId())) {
                            for (int k = 0; k < submissionsTable.getRowCount(); k++) {
                                if (String.valueOf(submissionsTable.getValue(k, "submission_id")).equals(String.valueOf(resourceSubmissionsTable.getValue(j, "submission_id")))) {
                                    Map submissionData = new LinkedHashMap();
                                    fill(submissionData, submissionsTable, k);
                                    resourceSubmissions.put(submissionData.get("submission_id"), submissionData);
                                }
                            }
                        }
                    }

                } catch (NoSuchTableException e) {
                    e.printStackTrace();
                }

                result.add(resource);
            }
        }
        if (result.isEmpty()) {
            throw new IllegalStateException("The resource matching the handle [" + id + "] is not found");
        }
        return (Resource[]) result.toArray(new Resource[result.size()]);
    }

    /**
     * <p>Loads the resources assigned to specified project.</p>
     *
     * @param dataSet an <code>IDataSet</code> providing the data.
     * @param projectId a <code>String</code> providing the ID of requested project.
     * @return a <code>Resource</code> array providing the details for resources assigned to specified project.
     * @throws Exception if an unexpected error occurs.
     */
    public static Resource[] loadProjectResources(IDataSet dataSet, String projectId) throws Exception {
        List result = new ArrayList();
        ITable resourceTable = dataSet.getTable("resource");
        for (int i = 0; i < resourceTable.getRowCount(); i++) {
            if (String.valueOf(resourceTable.getValue(i, "project_id")).equals(projectId)) {
                result.add(loadResource(dataSet, String.valueOf(resourceTable.getValue(i, "resource_id"))));
            }
        }

        return (Resource[]) result.toArray(new Resource[result.size()]);
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
     * <p>Gets the ID of this resource.</p>
     *
     * @return a <code>String</code> providing the ID of this resource.
     */
    public String getId() {
        return (String) this.data.get("resource_id");
    }

    /**
     * <p>Gets the ID of the project for this resource.</p>
     *
     * @return a <code>String</code> providing the ID of the project for this resource.
     */
    public String getProjectId() {
        return (String) this.data.get("project_id");
    }

    /**
     * <p>Gets the resource handle.</p>
     *
     * @return a <code>String</code> providing the resource handle.
     */
    public String getHandle() {
        Map infos = (Map) this.data.get("infos");
        Map handleInfo = (Map) infos.get("2");
        return (String) handleInfo.get("value");
    }

    /**
     * <p>Gets the resource email.</p>
     *
     * @return a <code>String</code> providing the resource email.
     */
    public String getEmail() {
        Map infos = (Map) this.data.get("infos");
        Map handleInfo = (Map) infos.get("3");
        return (String) handleInfo.get("value");
    }

    /**
     * <p>Gets the resource rating.</p>
     *
     * @return a <code>String</code> providing the resource rating.
     */
    public String getRating() {
        Map infos = (Map) this.data.get("infos");
        Map handleInfo = (Map) infos.get("4");
        return (String) handleInfo.get("value");
    }

    /**
     * <p>Gets the resource reliability.</p>
     *
     * @return a <code>String</code> providing the resource reliability.
     */
    public String getReliability() {
        Map infos = (Map) this.data.get("infos");
        Map handleInfo = (Map) infos.get("5");
        return (String) handleInfo.get("value");
    }

    /**
     * <p>Gets the resource payment.</p>
     *
     * @return a <code>String</code> providing the resource payment.
     */
    public String getPayment() {
        Map infos = (Map) this.data.get("infos");
        Map handleInfo = (Map) infos.get("7");
        if (handleInfo != null) {
            return (String) handleInfo.get("value");
        } else {
            return "N/A";
        }
    }

    /**
     * <p>Gets the resource payment status.</p>
     *
     * @return a <code>String</code> providing the resource payment status.
     */
    public String getPaymentStatus() {
        Map infos = (Map) this.data.get("infos");
        Map handleInfo = (Map) infos.get("8");
        if (handleInfo != null) {
            return (String) handleInfo.get("value");
        } else {
            return "N/A";
        }
    }

    /**
     * <p>Gets the resource submissions.</p>
     *
     * @return a <code>Map</code> mapping the <code>String</code> submission ID to <code>Map</code> of submission
     *         properties.
     */
    public Map getSubmissions() {
        return (Map) this.data.get("submissions");
    }

    /**
     * <p>Gets the most recent submission for this resource.</p>
     *
     * @return a <code>Map</code> providing the details for most recent submission.
     */
    public Map getMostRecentSubmission() {
        Map submissions = (Map) this.data.get("submissions");
        Iterator iteator = submissions.entrySet().iterator();
        Map.Entry entry;
        while (iteator.hasNext()) {
            entry = (Map.Entry) iteator.next();
            Map submission = (Map) entry.getValue();
            if ("1".equals(submission.get("submission_status_id"))) {
                return submission;
            }
        }
        return null;
    }
}
