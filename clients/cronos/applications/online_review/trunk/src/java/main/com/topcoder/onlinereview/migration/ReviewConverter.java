/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.topcoder.onlinereview.migration.dto.newschema.deliverable.Submission;
import com.topcoder.onlinereview.migration.dto.newschema.project.ProjectNew;
import com.topcoder.onlinereview.migration.dto.newschema.review.Review;
import com.topcoder.onlinereview.migration.dto.newschema.review.ReviewComment;
import com.topcoder.onlinereview.migration.dto.newschema.review.ReviewItem;
import com.topcoder.onlinereview.migration.dto.newschema.review.ReviewItemComment;
import com.topcoder.onlinereview.migration.dto.oldschema.ProjectOld;
import com.topcoder.onlinereview.migration.dto.oldschema.ScorecardOld;
import com.topcoder.onlinereview.migration.dto.oldschema.deliverable.SubmissionOld;
import com.topcoder.onlinereview.migration.dto.oldschema.review.AggResponse;
import com.topcoder.onlinereview.migration.dto.oldschema.review.AggReview;
import com.topcoder.onlinereview.migration.dto.oldschema.review.AggWorksheet;
import com.topcoder.onlinereview.migration.dto.oldschema.review.Appeal;
import com.topcoder.onlinereview.migration.dto.oldschema.review.FinalReview;
import com.topcoder.onlinereview.migration.dto.oldschema.review.FixItem;
import com.topcoder.onlinereview.migration.dto.oldschema.review.ScorecardQuestion;
import com.topcoder.onlinereview.migration.dto.oldschema.review.SubjectiveResp;
import com.topcoder.onlinereview.migration.dto.oldschema.review.TestcaseQuestion;
import com.topcoder.util.idgenerator.IDGenerationException;
import com.topcoder.util.idgenerator.IDGenerator;
import com.topcoder.util.log.Level;

/**
 * The test of ReviewConverter.
 *
 * @author brain_cn
 * @version 1.0
 */
public class ReviewConverter extends MapUtil {
	private ProjectNew project;
	private SubmissionOld oldSubmission;
	private Submission submission;
	private IDGenerator reviewIdGenerator;
	private IDGenerator reviewCommentIdGenerator;
	private IDGenerator reviewItemIdGenerator;
	private IDGenerator reviewItemCommentIdGenerator;
	private Review worksheetReview = null;
	private Review finalReview = null;	

	private AggWorksheet aggWorksheet = null;

	public ReviewConverter(ProjectOld old, ProjectNew project,SubmissionOld oldSubmission, Submission submission) {
		this.project = project;
		this.oldSubmission = oldSubmission;
		this.submission = submission;
		this.aggWorksheet = old.getAggWorksheet();
	}

	public List convert() throws Exception {
        // Scorecard phase   	
		List list = new ArrayList();
		prepareAggworksheet();

		for (Iterator iter = oldSubmission.getScorecards().iterator(); iter.hasNext();) {
			ScorecardOld old = (ScorecardOld) iter.next();
			Review review = prepareReview(old);
			if (review.getScorecardId() == 0) {
				Util.warn("Failed to find scorecard id for template id: " + old.getTemplateId());
				continue;
			}
			list.add(review);
		}

		if (this.worksheetReview != null) {
			list.add(this.worksheetReview);
		}

		if (this.finalReview != null) {
			list.add(this.finalReview);
		}
		return list;
	}

    private Review prepareReview(ScorecardOld scorecard) throws Exception {
    	Review review = new Review();
    	review.setReviewId((int) reviewIdGenerator.getNextID());
    	review.setCommitted(scorecard.isCompleted());
    	review.setResourceId(getResourceId(project, scorecard.getAuthorId()));
    	if (review.getResourceId() == 0) {
    		// Not found
    		log.log(Level.WARN, "AuthorId not found, AuthorId:" + scorecard.getAuthorId());
    	}
    	review.setScore(scorecard.getScore());
    	review.setScorecardId(getScorecardId(scorecard.getTemplateId()));
    	review.setSubmissionId(submission.getSubmissionId());
    	setBaseDTO(review);

    	for (Iterator iter = scorecard.getScorecardQuestions().iterator(); iter.hasNext();) {
        	createReviewItem(review, (ScorecardQuestion) iter.next(), scorecard.getScorecardType() == 1);
    	}
    	return review;
    }

    /**
     * aggregation worksheet phase, Review
     * 
     * @param newProject
     * @param aggWorksheet
     * @param submissionId
     * @return
     * @throws IDGenerationException
     */
    private void prepareWorksheetReview() throws Exception { 
    	worksheetReview = new Review(); 
    	worksheetReview.setReviewId((int) reviewIdGenerator.getNextID());
    	worksheetReview.setCommitted(aggWorksheet.isCompleted());
    	worksheetReview.setResourceId(MapUtil.getResourceId(project, aggWorksheet.getAggregatorId()));
    	if (worksheetReview.getResourceId() == 0) {
    		// Not found
    		log.log(Level.WARN, "AggregatorId not found, AggregatorId:" + aggWorksheet.getAggregatorId());
    	}

    	worksheetReview.setScore(getAggregatorScores(this.oldSubmission));

    	// copied from corresponding review
    	worksheetReview.setSubmissionId(this.submission.getSubmissionId());
		for (Iterator iter = this.aggWorksheet.getAggReviews().iterator(); iter.hasNext();) {
			prepareWorksheetReviewComment((AggReview) iter.next());	
		}
    	setBaseDTO(worksheetReview);
    }

    /**
     * Prepare questions of aggworksheet and final review.
     * 
     * @throws Exception if error occurs while retrieve scorecard id
     */
    private void prepareAggworksheet() throws Exception {
    	if (this.aggWorksheet == null) {
    		return;
    	}
    	prepareWorksheetReview();
    	if (this.aggWorksheet.getFinalReview() == null) {
    		return;
    	}
    	prepareFinalReviewAndComent();
    }

    /**
     * aggregation worksheet phase, Review
     * 
     * @param newProject
     * @param aggWorksheet
     * @param submissionId
     * @return
     * @throws IDGenerationException
     */
    private void prepareFinalReviewAndComent() throws Exception {    
    	finalReview = new Review();	
    	FinalReview fr = this.aggWorksheet.getFinalReview();
    	finalReview.setReviewId((int) reviewIdGenerator.getNextID());
    	finalReview.setCommitted(fr.isCompleted());

    	// TODO final review id from project resource
    	finalReview.setResourceId(getFinalReviewerId(project));
    	if (finalReview.getResourceId() == 0) {
    		// Not found
    		log.log(Level.WARN, "final reviewer not found, use aggregator id:" + this.worksheetReview.getResourceId());
    		finalReview.setResourceId(this.worksheetReview.getResourceId());
    	}

    	// aggregated score
    	finalReview.setScore(worksheetReview.getScore());
    	
    	// copied from corresponding review
    	finalReview.setSubmissionId(this.submission.getSubmissionId());
    	setBaseDTO(finalReview);

    	ReviewComment comment = new ReviewComment();
    	comment.setResourceId(finalReview.getResourceId());
    	comment.setReviewId(finalReview.getReviewId());
    	comment.setReviewCommentId((int) reviewCommentIdGenerator.getNextID());
    	comment.setContent(fr.getComments());
    	// 0 maps to 'Rejected' and 1 maps to 'Accepted'
    	comment.setExtraInfo(fr.isApproved() ? "Accepted" : "Rejected");
    	// always 'Final Review Comment'
    	comment.setCommentTypeId(ReviewItemComment.COMMENT_TYPE_FINAL_REVIEW_COMMENT);
    	setBaseDTO(comment);

    	finalReview.addReviewComment(comment);
    }

    /**
     * aggregation worksheet phase, ReviewComment
     * 
     * @param reviewId
     * @param aggReview
     * @param resourceId
     * @return
     * @throws IDGenerationException
     */
    private void prepareWorksheetReviewComment(AggReview aggReview) throws Exception {
    	ReviewComment comment = new ReviewComment();
    	comment.setResourceId(getResourceId(project, aggReview.getReviewerId()));
    	comment.setReviewId(worksheetReview.getReviewId());
    	comment.setReviewCommentId((int) reviewCommentIdGenerator.getNextID());
    	comment.setContent(aggReview.getAggReviewText());
    	// 1 maps to 'Approved', 2 maps to 'Rejected'
    	comment.setExtraInfo(aggReview.getAggApprovalId() == 1 ? "Approved" : "Rejected");
    	// always 'Aggregation Review Comment'
    	comment.setCommentTypeId(ReviewItemComment.COMMENT_TYPE_AGGREGATION_REVIEW_COMMENT);
    	setBaseDTO(comment);    	
    	this.worksheetReview.addReviewComment(comment);
    }

    /**
     * Prepare review item for screening/review scorecard.
     * 
     * @param review
     * @param question
     * @throws IDGenerationException
     */
    private void createReviewItem(Review review, ScorecardQuestion question, boolean screening) throws Exception {
    	ReviewItem item = new ReviewItem();    	
    	item.setReviewId(review.getReviewId());    	
    	item.setReviewItemId((int) reviewItemIdGenerator.getNextID());
    	item.setScorecardQuestionId(getScorecardQuestionId(question.getQTemplateVId()));   
    	item.setAnswer(getAnswer(question));
    	setBaseDTO(item);
    	review.addReviewItem(item);

    	ReviewItem worksheetItem = null;
    	if (!screening && question.hasAggResponse() && this.worksheetReview != null) {
    		this.worksheetReview.setScorecardId(review.getScorecardId());
    		worksheetItem = new ReviewItem();
    		worksheetItem.setReviewId(this.worksheetReview.getReviewId());    	
    		worksheetItem.setReviewItemId((int) reviewItemIdGenerator.getNextID());
    		worksheetItem.setScorecardQuestionId(item.getScorecardQuestionId());
    		worksheetItem.setAnswer(item.getAnswer());
        	setBaseDTO(worksheetItem);
        	worksheetReview.addReviewItem(worksheetItem);
    	}

    	ReviewItem finalReviewItem = null;
    	if (worksheetItem != null && question.hasFinalFix() && this.finalReview != null) {    		
    		// copy to final review
    		this.finalReview.setScorecardId(worksheetReview.getScorecardId());
    		finalReviewItem = new ReviewItem();
    		finalReviewItem.setReviewId(this.finalReview.getReviewId());    	
    		finalReviewItem.setReviewItemId((int) reviewItemIdGenerator.getNextID());
    		finalReviewItem.setScorecardQuestionId(item.getScorecardQuestionId());
    		finalReviewItem.setAnswer(item.getAnswer());
        	setBaseDTO(finalReviewItem);
        	finalReview.addReviewItem(finalReviewItem);
    	}

    	// subjective_resp review_item_comment
    	for (Iterator iter = question.getSubjectiveResps().iterator(); iter.hasNext();) {
    		SubjectiveResp resp = (SubjectiveResp) iter.next();
    		ReviewItemComment ric = createSubjectiveRespReviewItemComment(resp, item, review.getResourceId());

    		AggResponse ar = resp.getAggResponse();
    		if (worksheetItem != null && ar != null) {
    			// copy subjective
    			ReviewItemComment copy = copyReviewItemComment(worksheetItem, ric);
    			ric = prepareAggResponseReviewItemComment(worksheetItem, ar, resp);

	    		FixItem ff = ar.getFixItem();
	    		if (finalReviewItem != null && ff != null) {
	    			// copy subjective and agg_resp
	    			copyReviewItemComment(finalReviewItem, copy);
	    			copyReviewItemComment(finalReviewItem, ric);
	    			prepareFixItemReviewItemComment(finalReviewItem, ff, getContent(ar, resp), getCommentTypeIdByRespId(resp.getResponseTypeId()));
	    		}
    		}
    	}

    	// appeal review_item_comment
    	for (Iterator iter = question.getAppeals().iterator(); iter.hasNext();) {
    		prepareAppealReviewItemComment((Appeal) iter.next(), item, review.getResourceId());
    	}
    }

    private ReviewItemComment copyReviewItemComment(ReviewItem parent, ReviewItemComment old) throws Exception {
    	ReviewItemComment copy = new ReviewItemComment();
    	copy.setReviewItemCommentId((int) reviewItemCommentIdGenerator.getNextID());
    	copy.setCommentTypeId(old.getCommentTypeId());
    	copy.setContent(old.getContent());
    	copy.setResourceId(old.getResourceId());
    	copy.setReviewItemId(parent.getReviewItemId());
    	setBaseDTO(copy);
    	parent.addReviewItemComment(copy);
    	return copy;
    }

    /**
     * Retrieve correct answer for given question according to question type.
     * 
     * @param question
     * @return
     */
    private static String getAnswer(ScorecardQuestion question) {
    	/*
    	question_type,evaluation_id
    	1,null
    	1,5
    	1,6
    	2,null
    	2,1
    	2,2
    	2,3
    	2,4
    	3,null
    	4,null
    	4,7
    	4,8
    	*/

    	// TODO Find scoreacrdquestion id getScorecardQuestionId(question.getQTemplateVId())
    	// decide which question type is
    	// 2 maps to 'Scale 1-4', 3 maps to 'Test Case' and 4 maps to 'Yes/No'
    	if (question.getTestcaseQuestions() != null && question.getTestcaseQuestions().size() > 0) {
    		// maps the evaluation to answer, which is total_pass/total_tests
    		TestcaseQuestion testcase = (TestcaseQuestion) question.getTestcaseQuestions().iterator().next();
    		StringBuffer sb = new StringBuffer();
    		sb.append(testcase.getTotalPass()).append('/').append(testcase.getTotalTests());
    		return sb.toString();
    	} else {
    		// maps the evaludation to answer, which could be 1/2/3/4 or Yes/No
    		int id = question.getEvaluationId();
    		if (id < 5 || id > 8) {
    			return String.valueOf(id);
    		} else {
    			return id == 7 || id == 5 ? "Yes" : "No";
    		}
    	}
    }

    /**
     * The resourceId should be the same as Review's
     * 
     * @param resp
     * @param reviewItem
     * @param resourceId
     * @throws IDGenerationException
     */
    private ReviewItemComment createSubjectiveRespReviewItemComment(SubjectiveResp resp, ReviewItem reviewItem, int resourceId) throws IDGenerationException {
    	ReviewItemComment itemComment = new ReviewItemComment();
    	itemComment.setReviewItemId(reviewItem.getReviewItemId());
    	itemComment.setResourceId(resourceId);
    	itemComment.setReviewItemCommentId((int) reviewItemCommentIdGenerator.getNextID());
    	itemComment.setContent(resp.getResponseText());
    	
    	itemComment.setCommentTypeId(getCommentTypeIdByRespId(resp.getResponseTypeId()));
    	setBaseDTO(itemComment);
    	reviewItem.addReviewItemComment(itemComment);
    	return itemComment;
    }

    /**
     * Return comment type id from resp id.
     * 
     * @param respId
     * @return
     */
    private static int getCommentTypeIdByRespId(int respId) {
    	// 1 maps to 'Required', 2 maps to 'Recommended', 3 maps to 'Comment'
    	int commentTypeId = ReviewItemComment.COMMENT_TYPE_COMMENT;
    	if (respId == 1) {
    		commentTypeId = ReviewItemComment.COMMENT_TYPE_REQUIRED;
    	} else if (respId == 2) {
    		commentTypeId = ReviewItemComment.COMMENT_TYPE_RECOMMENDED;
    	}
    	return commentTypeId;
    }
    
    private void prepareAppealReviewItemComment(Appeal appeal, ReviewItem reviewItem, int resourceId) throws IDGenerationException {
    	ReviewItemComment itemComment = new ReviewItemComment();
    	itemComment.setReviewItemId(reviewItem.getReviewItemId());
    	itemComment.setResourceId(resourceId);
    	itemComment.setReviewItemCommentId((int) reviewItemCommentIdGenerator.getNextID());
    	itemComment.setContent(appeal.getAppealText());
    	    	
    	// if present, 'Succeeded' or 'Failed'
    	itemComment.setExtraInfo(appeal.isSuccessful() ? "Succeeded" : "Failed");
    	// always 'Appeal'
    	itemComment.setCommentTypeId(ReviewItemComment.COMMENT_TYPE_APPEAL);
    	setBaseDTO(itemComment);
    	reviewItem.addReviewItemComment(itemComment);
    	
    	// Appeal response
    	itemComment = new ReviewItemComment();
    	itemComment.setReviewItemId(reviewItem.getReviewItemId());
    	itemComment.setResourceId(resourceId);
    	itemComment.setReviewItemCommentId((int) reviewItemCommentIdGenerator.getNextID());
    	itemComment.setContent(appeal.getAppealResponse());

    	// always 'Appeal Response'
    	itemComment.setCommentTypeId(ReviewItemComment.COMMENT_TYPE_APPEAL_RESPONSE);
    	
    	// raw_evaluation_id/ raw_total_tests/ raw_total_pass
    	if (appeal.getRawTotalTests() > 0) {
    		StringBuffer sb = new StringBuffer();
    		sb.append(appeal.getRawTotalPass()).append('/').append(appeal.getRawTotalTests());
        	itemComment.setExtraInfo(appeal.isSuccessful() ? "Succeeded" : "Failed");
    	} else {
        	itemComment.setExtraInfo(String.valueOf(appeal.getRawEvaluationId()));
    	}
    	setBaseDTO(itemComment);
    	reviewItem.addReviewItemComment(itemComment);
    }

    /**
     * Return content from aggResponse and SubjectiveResp.
     * @param aggResponse
     * @param resp
     * @return
     */
    private static String getContent(AggResponse aggResponse, SubjectiveResp resp) {
    	String respText = aggResponse.getResponseText();
    	if (respText == null || respText.trim().length() == 0) {
    		return resp.getResponseText() + " Aggregation Comment:" + respText;
    	} else {
    		return resp.getResponseText();
    	}
    }

    /**
     * aggregation worksheet phase, ReviewItemComment
     * 
     * @param aggResponse
     * @param reviewItemId
     * @param resourceId
     * @return
     * @throws IDGenerationException
     */
    private ReviewItemComment prepareAggResponseReviewItemComment(ReviewItem reviewItem, AggResponse aggResponse, SubjectiveResp resp) throws IDGenerationException {
    	ReviewItemComment itemComment = new ReviewItemComment();
    	itemComment.setReviewItemId(reviewItem.getReviewItemId());
    	itemComment.setResourceId(this.worksheetReview.getResourceId());
    	itemComment.setReviewItemCommentId((int) reviewItemCommentIdGenerator.getNextID());

    	// copied from review, if present new comment of 'Aggregation Comment' is added
    	itemComment.setContent(getContent(aggResponse, resp));

    	// copied from review 	
    	itemComment.setCommentTypeId(getCommentTypeIdByRespId(resp.getResponseTypeId()));
    	
    	// 1 maps to 'Accepted', 2 maps to 'Rejected', 3 maps to 'Duplicate'
    	String extraInfo = "Accepted";
    	if (aggResponse.getAggRespStatId() == 2) {
    		extraInfo = "Rejected";
    	} else if (aggResponse.getAggRespStatId() == 3) {
    		extraInfo = "Duplicate";
    	}
    	itemComment.setExtraInfo(extraInfo);
    	setBaseDTO(itemComment);
    	reviewItem.addReviewItemComment(itemComment);
    	return itemComment;
    }

    /**
     * aggregation worksheet phase, ReviewItemComment
     * 
     * @param fixItem
     * @param reviewItemId
     * @param resourceId
     * @return
     * @throws IDGenerationException
     */
    private void prepareFixItemReviewItemComment(ReviewItem reviewItem, FixItem fixItem, String content, int commentTypeId) throws IDGenerationException {
    	ReviewItemComment itemComment = new ReviewItemComment();
    	itemComment.setReviewItemId(reviewItem.getReviewItemId());
    	itemComment.setResourceId(this.finalReview.getResourceId());
    	itemComment.setReviewItemCommentId((int) reviewItemCommentIdGenerator.getNextID());
    	itemComment.setContent(content);

    	// copied from review 	
    	itemComment.setCommentTypeId(commentTypeId);

    	// 1 maps to 'Not Fixed', 2 maps to 'Fixed'
    	itemComment.setExtraInfo(fixItem.getFinalFixSId() == 1 ? "Not Fixed" : "Fixed");
    	setBaseDTO(itemComment);
    	reviewItem.addReviewItemComment(itemComment);
    }

	/**
	 * @return Returns the reviewCommentIdGenerator.
	 */
	public IDGenerator getReviewCommentIdGenerator() {
		return reviewCommentIdGenerator;
	}

	/**
	 * @param reviewCommentIdGenerator The reviewCommentIdGenerator to set.
	 */
	public void setReviewCommentIdGenerator(IDGenerator reviewCommentIdGenerator) {
		this.reviewCommentIdGenerator = reviewCommentIdGenerator;
	}

	/**
	 * @return Returns the reviewIdGenerator.
	 */
	public IDGenerator getReviewIdGenerator() {
		return reviewIdGenerator;
	}

	/**
	 * @param reviewIdGenerator The reviewIdGenerator to set.
	 */
	public void setReviewIdGenerator(IDGenerator reviewIdGenerator) {
		this.reviewIdGenerator = reviewIdGenerator;
	}

	/**
	 * @return Returns the reviewItemCommentIdGenerator.
	 */
	public IDGenerator getReviewItemCommentIdGenerator() {
		return reviewItemCommentIdGenerator;
	}

	/**
	 * @param reviewItemCommentIdGenerator The reviewItemCommentIdGenerator to set.
	 */
	public void setReviewItemCommentIdGenerator(IDGenerator reviewItemCommentIdGenerator) {
		this.reviewItemCommentIdGenerator = reviewItemCommentIdGenerator;
	}

	/**
	 * @return Returns the reviewItemIdGenerator.
	 */
	public IDGenerator getReviewItemIdGenerator() {
		return reviewItemIdGenerator;
	}

	/**
	 * @param reviewItemIdGenerator The reviewItemIdGenerator to set.
	 */
	public void setReviewItemIdGenerator(IDGenerator reviewItemIdGenerator) {
		this.reviewItemIdGenerator = reviewItemIdGenerator;
	}
}
