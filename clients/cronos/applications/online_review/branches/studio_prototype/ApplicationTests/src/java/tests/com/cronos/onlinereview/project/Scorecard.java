/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.project;

import java.io.InputStream;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.ITableMetaData;
import org.dbunit.dataset.Column;

/**
 * <p></p>
 *
 * @author  TCSDEVELOPER
 * @version 1.0
 */
public class Scorecard {

    /**
     * <p>A <code>String</code> providing the name of the XML file providing the initial test data to be inserted into
     * database scorecard tables prior to executing any test.</p>
     */
    public static final String SCORECARDS_TEST_DATA_FILE_NAME = "data/split/Scorecards.xml";

    /**
     * <p>A <code>Scorecard</code> providing details for design screening scorecard.</p>
     */
    public static final Scorecard DESIGN_SCREENING = loadScorecard("1");

    /**
     * <p>A <code>Scorecard</code> providing details for design review scorecard.</p>
     */
    public static final Scorecard DESIGN_REVIEW = loadScorecard("2");

    /**
     * <p>A <code>Scorecard</code> providing details for design review scorecard.</p>
     */
    public static final Scorecard DESIGN_APPROVAL = loadScorecard("3");

    /**
     * <p>A <code>Map</code> mapping the <code>String</code> name of the column to <code>String</code> value of the
     * column.</p>
     */
    private Map data = new HashMap();

    /**
     * <p>Loads the data for requested scorecard from the data store.</p>
     *
     * @param id a <code>String</code> providing the ID of desired scorecard.
     * @return a <code>Scorecard</code> providing the details for requested scorecard.
     */
    private static Scorecard loadScorecard(String id) {
        Scorecard scorecard = new Scorecard();
        try {
            InputStream dataStream
                = Scorecard.class.getClassLoader().getResourceAsStream(SCORECARDS_TEST_DATA_FILE_NAME);
            FlatXmlDataSet dataSet = new FlatXmlDataSet(dataStream);
            ITable scorecardTable = dataSet.getTable("scorecard");
            for (int i = 0; i < scorecardTable.getRowCount(); i++) {
                if (String.valueOf(scorecardTable.getValue(i, "scorecard_id")).equals(id)) {
                    Map scorecardGroups = new LinkedHashMap();
                    scorecard.data.put("groups", scorecardGroups);
                    fill(scorecard.data, scorecardTable, i);
                    ITable scorecardGroupTable = dataSet.getTable("scorecard_group");
                    for (int j = 0; j < scorecardGroupTable.getRowCount(); j++) {
                        if (String.valueOf(scorecardGroupTable.getValue(j, "scorecard_id")).equals(id)) {
                            Map groupData = new HashMap();
                            Map groupSections = new LinkedHashMap();
                            groupData.put("sections", groupSections);
                            fill(groupData, scorecardGroupTable, j);
                            ITable scorecardSectionTable = dataSet.getTable("scorecard_section");
                            for (int k = 0; k < scorecardSectionTable.getRowCount(); k++) {
                                if (String.valueOf(scorecardSectionTable.getValue(k, "scorecard_group_id")).
                                    equals(groupData.get("scorecard_group_id"))) {
                                    Map sectionData = new HashMap();
                                    Map sectionQuestions = new LinkedHashMap();
                                    sectionData.put("questions", sectionQuestions);
                                    fill(sectionData, scorecardSectionTable, k);
                                    ITable scorecardQuestionTable = dataSet.getTable("scorecard_question");
                                    for (int m = 0; m < scorecardQuestionTable.getRowCount(); m++) {
                                        if (String.valueOf(scorecardQuestionTable.getValue(m, "scorecard_section_id")).
                                            equals(sectionData.get("scorecard_section_id"))) {
                                            Map questionData = new HashMap();
                                            fill(questionData, scorecardQuestionTable, m);
                                            sectionQuestions.put(questionData.get("scorecard_question_id"),
                                                                 questionData);
                                        }
                                    }
                                    groupSections.put(sectionData.get("scorecard_section_id"), sectionData);
                                }
                            }
                            scorecardGroups.put(groupData.get("scorecard_group_id"), groupData);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return scorecard;
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
     * <p>Gets the groups existing in this scorecard.</p>
     *
     * @return a <code>Map</code> array providing the details for existing groups.
     */
    public Map[] getGroups() {
        LinkedHashMap groups = (LinkedHashMap) this.data.get("groups");
        return (Map[]) groups.values().toArray(new Map[0]);
    }
}
