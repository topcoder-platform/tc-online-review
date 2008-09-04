/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.project;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSet;

import java.io.InputStream;

/**
 * <p></p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class Lookup {

    /**
     * <p>A <code>String</code> providing the name of the XML file providing the initial test data to be inserted into
     * database lookup tables prior to executing any test.</p>
     */
    public static final String LOOKUPS_TEST_DATA_FILE_NAME = "data/split/Lookups.xml";

    private static Lookup instance = null;

    private IDataSet dataSet = null;

    public Lookup(IDataSet dataSet) {
        this.dataSet = dataSet;
    }

    public static Lookup getInstance() throws Exception {
        if (instance == null) {
            InputStream lookupsDataStream
                = AbstractTestCase.class.getClassLoader().getResourceAsStream(LOOKUPS_TEST_DATA_FILE_NAME);
            instance = new Lookup(new FlatXmlDataSet(lookupsDataStream));
        }
        return instance;
    }


    public String getPhaseName(String phaseTypeId) throws Exception {
        return getValue("phase_type_lu", "phase_type_id", phaseTypeId, "name");
    }

    public String getProjectCategoryName(String categoryId) throws Exception {
        return getValue("project_category_lu", "project_category_id", categoryId, "name");
    }

    private String getValue(String tableName, String lookupColumn, String lookupValue, String returnColumn)
        throws Exception {
        ITable table = this.dataSet.getTable(tableName);
        for (int i = 0; i < table.getRowCount(); i++) {
            if (lookupValue.equals(String.valueOf(table.getValue(i, lookupColumn)))) {
                return String.valueOf(table.getValue(i, returnColumn));
            }
        }
        return null;
    }
}
