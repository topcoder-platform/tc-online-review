/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.project;

import org.dbunit.dataset.ITable;
import org.dbunit.dataset.Column;
import org.dbunit.dataset.ITableMetaData;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.NoSuchTableException;

import java.util.HashMap;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Iterator;

/**
 * <p></p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class Review {

    /**
     * <p>A <code>Map</code> mapping the <code>String</code> name of the column to <code>String</code> value of the
     * column.</p>
     */
    private Map data = new HashMap();

    /**
     * <p>Constructs new <code>Review</code> instance.</p>
     */
    public Review() {
    }

    /**
     * <p>Loads the data for requested reveiw from the data set.</p>
     *
     * @param dataSet an <code>IDataSet</code> providing the data.
     * @param id a <code>String</code> providing the ID of requested review.
     * @return a <code>Review</code> providing the details for requested review.
     * @throws Exception if an unexpected error occurs.
     */
    public static Review loadReview(IDataSet dataSet, String id) throws Exception {
        Review review = new Review();
        ITable reviewTable = dataSet.getTable("review");
        for (int i = 0; i < reviewTable.getRowCount(); i++) {
            if (String.valueOf(reviewTable.getValue(i, "review_id")).equals(id)) {
                fill(review.data, reviewTable, i);
                LinkedHashMap reviewItems = new LinkedHashMap();
                LinkedHashMap reviewComments = new LinkedHashMap();
                review.data.put("items", reviewItems);
                review.data.put("comments", reviewComments);
                ITable reviewItemsTable = dataSet.getTable("review_item");
                for (int j = 0; j < reviewItemsTable.getRowCount(); j++) {
                    if (String.valueOf(reviewItemsTable.getValue(j, "review_id")).equals(id)) {
                        Map reviewItemData = new HashMap();
                        fill(reviewItemData, reviewItemsTable, j);
                        Map reviewItemComments = new LinkedHashMap();
                        reviewItemData.put("comments", reviewItemComments);
                        ITable reviewItemComentsTable = dataSet.getTable("review_item_comment");
                        for (int k = 0; k < reviewItemComentsTable.getRowCount(); k++) {
                            if (String.valueOf(reviewItemsTable.getValue(j, "review_item_id")).
                                equals(reviewItemData.get("review_item_id"))) {
                                Map reviewItemCommentData = new HashMap();
                                fill(reviewItemCommentData, reviewItemComentsTable, k);
                                reviewItemComments.put(reviewItemCommentData.get("review_item_comment_id"),
                                                       reviewItemCommentData);
                            }
                        }
                        reviewItems.put(reviewItemData.get("review_item_id"), reviewItemData);
                    }
                }

                try {
                    ITable reviewCommentsTable = dataSet.getTable("review_comment");
                    for (int j = 0; j < reviewCommentsTable.getRowCount(); j++) {
                        if (String.valueOf(reviewCommentsTable.getValue(j, "review_id")).equals(id)) {
                            Map reviewCommentData = new HashMap();
                            fill(reviewCommentData, reviewCommentsTable, j);
                            reviewComments.put(reviewCommentData.get("review_comment_id"), reviewCommentData);
                        }
                    }
                } catch (NoSuchTableException e) {
                    e.printStackTrace();
                }
            }
        }
        return review;
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
     * <p>Gets the score for the review item corresponding to specified scorecard question.</p>
     *
     * @param scorecardQuestionId a <code>String</code> providing the ID of a scorecard question.
     * @return a <code>String</code> providing the score of the respective review item.
     */
    public String getQuestionScore(String scorecardQuestionId) {
        Map item = getReviewItem(scorecardQuestionId);
        return (String) item.get("answer");
    }

    /**
     * <p>Checks if the specified question has been appealed by the submitter.</p>
     *
     * @param scorecardQuestionId a <code>String</code> providing the ID of a scorecard question.
     * @return <code>true</code> if there is an appeal for the specified question; <code>false</code> otherwise.
     */
    public boolean isAppealed(String scorecardQuestionId) {
        Map item = getReviewItem(scorecardQuestionId);
        Map comments = (Map) item.get("comments");
        Iterator commentsIterator = comments.entrySet().iterator();
        Map comment;
        while (commentsIterator.hasNext()) {
            comment = (Map) commentsIterator.next();
            if (comment.get("comment_type_id").equals("4")) {
                return true;
            }
        }
        return false;
    }

    /**
     * <p>Checks if the appeal for specified question has been resolved by the reviewer.</p>
     *
     * @param scorecardQuestionId a <code>String</code> providing the ID of a scorecard question.
     * @return <code>true</code> if an appeal for the specified question has been resolved; <code>false</code>
     *         otherwise.
     */
    public boolean isAppealResolved(String scorecardQuestionId) {
        Map item = getReviewItem(scorecardQuestionId);
        Map comments = (Map) item.get("comments");
        Iterator commentsIterator = comments.entrySet().iterator();
        Map comment;
        while (commentsIterator.hasNext()) {
            comment = (Map) commentsIterator.next();
            if (comment.get("comment_type_id").equals("5")) {
                return true;
            }
        }
        return false;
    }

    /**
     * <p>Gets the review item corresponding to specified scorecard question.</p>
     *
     * @param scorecardQuestionId a <code>String</code> providing the ID of a scorecard question.
     * @return a <code>Map</code> providing the details for review item corresponding to specified scorecard question.
     */
    public Map getReviewItem(String scorecardQuestionId) {
        Map reviewItems = (Map) this.data.get("items");
        Iterator iterator = reviewItems.entrySet().iterator();
        Map.Entry entry;
        while (iterator.hasNext()) {
            entry = (Map.Entry) iterator.next();
            Map item = (Map) entry.getValue();
            if (item.get("scorecard_question_id").equals(scorecardQuestionId)) {
                return item;
            }
        }
        throw new IllegalStateException("No review item matching the scorecard question [" + scorecardQuestionId + "]");
    }
}
