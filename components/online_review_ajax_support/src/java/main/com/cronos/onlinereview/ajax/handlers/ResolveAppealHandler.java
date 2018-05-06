/*
 * Copyright (C) 2006-2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ajax.handlers;

import java.util.Iterator;

import com.cronos.onlinereview.ajax.AjaxRequest;
import com.cronos.onlinereview.ajax.AjaxResponse;
import com.cronos.onlinereview.ajax.AjaxSupportHelper;
import com.cronos.onlinereview.ajax.ConfigurationException;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.review.data.Comment;
import com.topcoder.management.review.data.CommentType;
import com.topcoder.management.review.data.Item;
import com.topcoder.management.review.data.Review;
import com.topcoder.management.review.scorecalculator.CalculationManager;
import com.topcoder.management.scorecard.ScorecardManager;
import com.topcoder.management.scorecard.data.Scorecard;
import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.PhaseStatus;
import com.topcoder.project.phases.PhaseType;

/**
 * <p>
 * Ajax request handler class which handles the "Resolve Appeal" Ajax operation.
 * this class extends the ReviewCommonHandler abstract class,
 * and uses the managers defined in its parents classes in order to implement the "Resolve Appeal" operation.
 *
 * Any successful or failed operation is logged using the Logging Wrapper component.
 * </p>
 *
 * <p>
 * <strong>Thread Safety:</strong>
 * This class is immutable an thread safe. Any manager class used by this handler is supposed to be thread safe.
 * </p>
 *
 * @author topgear
 * @author assistant
 * @version 1.1.1
 */
public class ResolveAppealHandler extends ReviewCommonHandler {

    /**
     * The magic string for status open.
     */
    private static final String STATUS_OPEN = "Open";

    /**
     * The magic string for type review.
     */
    private static final String TYPE_REVIEW = "Review";

    /**
     * The magic string for type appeals response.
     */
    private static final String TYPE_APPEALS_RESPONSE = "Appeals Response";

    /**
     * The magic string for type comment.
     */
    private static final String COMMENT_TYPE_COMMENT = "Comment";

    /**
     * The magic string for type recommended.
     */
    private static final String COMMENT_TYPE_RECOMMENDED = "Recommended";

    /**
     * The magic string for type required.
     */
    private static final String COMMENT_TYPE_REQUIRED = "Required";

    /**
     * The magic string for type appeal response.
     */
    private static final String COMMENT_TYPE_APPEAL_RESPONSE = "Appeal Response";

    /**
     * Represents the status of success.
     */
    private static final String SUCCESS = "Success";

    /**
     * Represents the status of business error.
     */
    private static final String BUSINESS_ERROR = "Business error";

    /**
     * Represents the status of role error.
     */
    private static final String ROLE_ERROR = "Role error";

    /**
     * Represents the status of login error.
     */
    private static final String LOGIN_ERROR = "Login error";

    /**
     * Represents the status of invalid parameter error.
     */
    private static final String INVALID_PARAMETER_ERROR = "Invalid parameter error";

    /**
     * Represents the status of phase error.
     */
    private static final String PHASE_ERROR = "Phase error";

    /**
     * Represents the status of invalid item error.
     */
    private static final String INVALID_ITEM_ERROR = "Invalid item error";

    /**
     * Represents the status of invalid comment error.
     */
    private static final String INVALID_COMMENT_ERROR = "Invalid comment error";

    /**
     * Represents the status of invalid review error.
     */
    private static final String INVALID_REVIEW_ERROR = "Invalid review error";

    /**
     * <p>
     * Represents comment type with name "Comment", it is used to modify the original comment type.
     * This variable is immutable, it is initialized by the constructor to a not null CommentType
     * obtained from the ReviewManager of the parent class, and used by the service method.
     * </p>
     */
    private final CommentType commentCommentType;

    /**
     * <p>
     * Represents comment type with name "Recommended", it is used to modify the original comment type.
     * This variable is immutable, it is initialized by the constructor to a not null CommentType
     * obtained from the ReviewManager of the parent class, and used by the service method.
     * </p>
     */
    private final CommentType recommendedCommentType;

    /**
     * <p>
     * Represents comment type with name "Required", it is used to modify the original comment type.
     * This variable is immutable, it is initialized by the constructor to a not null CommentType
     * obtained from the ReviewManager of the parent class, and used by the service method.
     * </p>
     */
    private final CommentType requiredCommentType;

    /**
     * <p>
     * Represents comment type with name "Appeal Response", it is used to create an appeal response comment.
     * This variable is immutable, it is initialized by the constructor to a not null CommentType
     * obtained from the ReviewManager of the parent class, and used by the service method.
     * </p>
     */
    private final CommentType appealResponseCommentType;

    /**
     * <p>
     * The id of the phase type with name "Review". it is used to check that a phase has the "Review" phase type.
     * This variable is immutable, it is initialized by the constructor to a negative/0/positive long number,
     * and used by the service method.
     * </p>
     */
    private final long reviewPhaseTypeId;

    /**
     * <p>
     * The id of the phase type with name "Appeals Response".
     * It is used to check that a phase has the "Appeals Response" phase type.
     * This variable is immutable, it is initialized by the constructor to a negative/0/positive long number,
     * and used by the service method.
     * </p>
     */
    private final long appealsResponsePhaseTypeId;

    /**
     * <p>
     * The id of the phase status with name "Open". it is used to check that a phase has the "Open" status.
     * This variable is immutable, it is initialized by the constructor to a negative/0/positive long number,
     * and used by the service method.
     * </p>
     */
    private final long openPhaseStatusId;

    /**
     * <p>
     * The calculation manager used to calculate review score.
     * </p>
     */
    private final CalculationManager calculationManager;

    /**
     * <p>
     * The score card manager used to calculate review score.
     * </p>
     */
    private final ScorecardManager scorecardManager;

    /**
     * <p>
     * <strong>Description : </strong>
     * Creates an instance of this class and initialize its internal state.
     * </p>
     *
     * @throws ConfigurationException if there is a configuration exception
     */
    public ResolveAppealHandler() throws ConfigurationException {

        try {
            calculationManager = (CalculationManager) AjaxSupportHelper.createObject(CalculationManager.class);
            scorecardManager = (ScorecardManager) AjaxSupportHelper.createObject(ScorecardManager.class);

            // get all comment types and find the one with name "Appeal Response"
            CommentType[] commentTypes = getReviewManager().getAllCommentTypes();
            CommentType comment = null;
            CommentType recommended = null;
            CommentType required = null;
            CommentType appealResponse = null;
            for (CommentType commentType : commentTypes) {
                if (commentType.getName().equals(COMMENT_TYPE_COMMENT)) {
                    comment = commentType;
                } else if (commentType.getName().equals(COMMENT_TYPE_RECOMMENDED)) {
                    recommended = commentType;
                } else if (commentType.getName().equals(COMMENT_TYPE_REQUIRED)) {
                    required = commentType;
                } else if (commentType.getName().equals(COMMENT_TYPE_APPEAL_RESPONSE)) {
                    appealResponse = commentType;
                }
            }
            if (comment != null) {
                this.commentCommentType = comment;
            } else {
                throw new ConfigurationException("No comment type Comment found.");
            }
            if (recommended != null) {
                this.recommendedCommentType = recommended;
            } else {
                throw new ConfigurationException("No comment type Recommended found.");
            }
            if (required != null) {
                this.requiredCommentType = required;
            } else {
                throw new ConfigurationException("No comment type Required found.");
            }
            if (appealResponse != null) {
                this.appealResponseCommentType = appealResponse;
            } else {
                throw new ConfigurationException("No comment type Appeal Response found.");
            }

            // get all phase types and find the review phase type id and appeals phase type id
            PhaseType[] phaseTypes = getPhaseManager().getAllPhaseTypes();
            long reviewTypeId = 0;
            long appealResponseTypeId = 0;
            boolean foundReview = false;
            boolean foundAppeal = false;
            for (PhaseType phaseType : phaseTypes) {
                if (phaseType.getName().equals(TYPE_REVIEW)) {
                    reviewTypeId = phaseType.getId();
                    foundReview = true;
                }
                if (phaseType.getName().equals(TYPE_APPEALS_RESPONSE)) {
                    appealResponseTypeId = phaseType.getId();
                    foundAppeal = true;
                }
            }

            if (!foundReview) {
                throw new ConfigurationException("The review phase type id can't be found.");
            }
            if (!foundAppeal) {
                throw new ConfigurationException("The appeal response phase type id can't be found.");
            }
            this.reviewPhaseTypeId = reviewTypeId;
            this.appealsResponsePhaseTypeId = appealResponseTypeId;

            // get the open phase status id
            PhaseStatus[] statuses = getPhaseManager().getAllPhaseStatuses();
            boolean found = false;
            long id = 0;
            for (PhaseStatus status : statuses) {
                if (status.getName().equals(STATUS_OPEN)) {
                    id = status.getId();
                    found = true;
                    break;
                }
            }
            if (!found) {
                throw new ConfigurationException("The open phase status id can't be found.");
            }
            this.openPhaseStatusId = id;
        } catch (Exception e) {
            if (e instanceof ConfigurationException) {
                throw (ConfigurationException) e;
            }
            throw new ConfigurationException("Something error when loading configurations.", e);
        }
    }

    /**
     * <p>
     * Performs the "Resolve Appeal" operation and returns the score of the review
     * if the operation is successful wrapped in an AjaxResponse,
     * if the operation fails this method returns a failure Ajax response.
     * </p>
     *
     * @return the response to the request
     * @param request the request to service
     * @param userId the id of user who issued this request
     * @throws IllegalArgumentException if the request is null
     */
    public AjaxResponse service(AjaxRequest request, Long userId) {

        if (request == null) {
            throw new IllegalArgumentException("The request should not be null.");
        }
        // check all the required parameters
        long reviewId, itemId;
        String status, answer, text;

        // ReviewID
        try {
            reviewId = request.getParameterAsLong("ReviewId");
        } catch (NumberFormatException e) {
            return AjaxSupportHelper.createAndLogError(request.getType(), INVALID_PARAMETER_ERROR,
                    "The review id should be a long value.", userId, e);
        }
        // ItemId
        try {
            itemId = request.getParameterAsLong("ItemId");
        } catch (NumberFormatException e) {
            return AjaxSupportHelper.createAndLogError(request.getType(), INVALID_PARAMETER_ERROR,
                    "The review id should be a long value.", userId, e);
        }

        // status
        status = request.getParameter("Status");
        if (status == null || (!status.equals("Succeeded") && !status.equals("Failed"))) {
            return AjaxSupportHelper.createAndLogError(request.getType(), INVALID_PARAMETER_ERROR,
                    "The status must be Succeeded or Failed.", status);
        }
        // answer and text
        answer = request.getParameter("Answer");
        // ISV : Appeal text is required and must be provided by "Text" parameter but not "text"
        text = request.getParameter("Text");
        if ((text == null) || (text.trim().length() == 0)) {
            return AjaxSupportHelper.createAndLogError(request.getType(), "The appeal response text must be provided.",
                    "The appeal response text must be provided.", "ResolveAppeal. " + "User id : " + userId);
        }
        if (text != null && text.length() > 4096) {
            return AjaxSupportHelper.createAndLogError(request.getType(), "The appeal response text must contain less than or equal to 4096 characters.",
                    "The appeal response text must contain less than or equal to 4096 characters.", "ResolveAppeal. User id : " + userId);
        }

        // check the userId for validation
        if (userId == null) {
            return AjaxSupportHelper.createAndLogError(request.getType(), LOGIN_ERROR,
                    "Doesn't login or expired.", userId);
        }

        // check the user is the author of the review
        Review review;
        try {
            review = getReviewManager().getReview(reviewId);
        } catch (Exception e) {
            return AjaxSupportHelper.createAndLogError(request.getType(),
                    BUSINESS_ERROR, "Can't get the review : " + e.getMessage(),
                    "User id : " + userId + "\treview id : " + reviewId, e);
        }
        if (review == null) {
            return AjaxSupportHelper.createAndLogError(request.getType(), INVALID_REVIEW_ERROR,
                    "Can't get the review",
                    "User id : " + userId + "\treview id : " + reviewId);
        }

        // get the reviewer resource
        Resource reviewerResource;
        try {
            reviewerResource = getResourceManager().getResource(review.getAuthor());
        } catch (Exception e) {
            return AjaxSupportHelper.createAndLogError(request.getType(),
                    BUSINESS_ERROR, "Can't get reviewer resource.",
                    "User id : " + userId + "\treview id : " + reviewId
                    + "\treviwer id :" + review.getAuthor(), e);
        }
        // validate the review resource
        if (reviewerResource == null) {
            return AjaxSupportHelper.createAndLogError(request.getType(),
                    BUSINESS_ERROR, "Error when finding the resource.", "Review ID : " + reviewId);
        }
        if (reviewerResource.getPhase() == null) {
            return AjaxSupportHelper.createAndLogError(request.getType(),
                    PHASE_ERROR, "The reviewerResource should have a phase.",
                    "User id : " + userId + "\treview id : " + reviewId
                    + "\treviwer id :" + review.getAuthor());
        }

        // Check that the logged user is actually the review author.
        if (!checkResourceAssignedToUser(reviewerResource, userId)) {
            return AjaxSupportHelper.createAndLogError(request.getType(),
                    ROLE_ERROR, "The user is wrong.",
                    "User id : " + userId + "\tReview ID : " + reviewId);
        }

        // check the user has the role of "Reviewer"
        if (!(checkResourceHasRole(reviewerResource, "Reviewer")
              || checkResourceHasRole(reviewerResource, "Accuracy Reviewer")
              || checkResourceHasRole(reviewerResource, "Failure Reviewer")
              || checkResourceHasRole(reviewerResource, "Stress Reviewer"))) {
            return AjaxSupportHelper.createAndLogError(request.getType(),
                    ROLE_ERROR, "The user should be a reviewer.",
                    "User id : " + userId + "\treview id : " + reviewId);
        }

        Phase[] phases;
        try {
            phases = getPhaseManager().getPhases(reviewerResource.getProject()).getAllPhases();
        } catch (Exception e) {
            return AjaxSupportHelper.createAndLogError(request.getType(),
                    BUSINESS_ERROR, "Can't get phases.",
                    "User id : " + userId + "\treview id : " + reviewId
                    + "\tproject id :" + reviewerResource.getProject(), e);
        }

        // get the review phase
        Phase reviewPhase = null;
        for (Phase phase : phases) {
            if (phase.getPhaseType().getId() == reviewPhaseTypeId) {
                reviewPhase = phase;
                break;
            }
        }
        if (reviewPhase == null) {
            return AjaxSupportHelper.createAndLogError(request.getType(),
                    BUSINESS_ERROR, "Can't get review phase.",
                    "User id : " + userId + "\treview id : " + reviewId);
        }

        // validate the phase
        if (reviewPhase.getId() != reviewerResource.getPhase()) {
            return AjaxSupportHelper.createAndLogError(request.getType(),
                    ROLE_ERROR, "The reviewerResource should have a phase the same with the review phase.",
                    "User id : " + userId + "\treview id : " + reviewId
                    + "\texpected phase id :" + reviewPhase.getId() + "\tactual id : " + reviewerResource.getPhase());
        }

        // get the appeal response phase
        Phase appealResponsePhase = null;
        for (Phase phase : phases) {
            if (phase.getPhaseType().getId() == appealsResponsePhaseTypeId) {
                appealResponsePhase = phase;
                break;
            }
        }

        // validate the phase
        if (appealResponsePhase == null) {
            return AjaxSupportHelper.createAndLogError(request.getType(),
                    BUSINESS_ERROR, "Can't get appeal response phase.",
                    "User id : " + userId + "\treview id : " + reviewId);
        }

        if (appealResponsePhase.getPhaseStatus().getId() != openPhaseStatusId) {
            return AjaxSupportHelper.createAndLogError(request.getType(),
                    PHASE_ERROR, "The phase should be open.",
                    "User id : " + userId + "\treview id : " + reviewId + "\tphase id :" + appealResponsePhase.getId());
        }

        // get all the review items
        Item[] items = review.getAllItems();

        // find the item with itemId
        Item item = null;
        for (Item itemTmp : items) {
            if (itemTmp.getId() == itemId) {
                item = itemTmp;
                break;
            }
        }
        if (item == null) {
            return AjaxSupportHelper.createAndLogError(request.getType(),
                    INVALID_ITEM_ERROR, "The item can't be found.",
                    "User id : " + userId + "\treview id : " + reviewId + "\titem id :" + itemId);
        }

        // get all the comments
        Comment[] comments = item.getAllComments();
        Comment appealResponseComment = null;

        // find the one with type appealResponseCommentType
        for (Comment comment : comments) {
            if (comment.getCommentType().getId() == this.appealResponseCommentType.getId()) {
                appealResponseComment = comment;
                break;
            }
        }

        // if the comment doesn't exist
        if (appealResponseComment == null) {
            // create a new one and add it to the item
            appealResponseComment = new Comment();
            appealResponseComment.setCommentType(appealResponseCommentType);
            appealResponseComment.setComment(text);
            appealResponseComment.setExtraInfo(item.getAnswer());
            appealResponseComment.setAuthor(reviewerResource.getId());
            item.addComment(appealResponseComment);
        } else {
            // just set the comment text
            appealResponseComment.setComment(text);
        }

        // find the comment with appeal comment type
        Comment appealComment = null;
        for (Comment comment : comments) {
            if (comment.getCommentType().getName().equals("Appeal")) {
                appealComment = comment;
                break;
            }
        }
        if (appealComment == null) {
            return AjaxSupportHelper.createAndLogError(request.getType(),
                    PHASE_ERROR, "There should be an appeal comment.",
                    "User id : " + userId + "\treview id : " + reviewId + "\titem id :" + itemId);
        }
        appealComment.setExtraInfo(status);

        // update the item
        item.setAnswer(answer);

        // WB - allow modify the type of the original comments
        for (Object param : request.getAllParameterNames()) {
            String paramName = (String) param;
            if (!paramName.startsWith("CommentType")) {
                continue;
            }
            String commentId = paramName.substring("CommentType".length());
            String newType = request.getParameter(paramName);

            // find the original comment
            Comment originalComment = null;
            for (Comment comment : comments) {
                if (commentId.equals(String.valueOf(comment.getId()))) {
                    originalComment = comment;
                    break;
                }
            }
            if (originalComment == null) {
                return AjaxSupportHelper.createAndLogError(request.getType(),
                        INVALID_COMMENT_ERROR, "The original comment with id " + commentId + " can not be found.",
                        "User id : " + userId + "\treview id : " + reviewId + "\titem id :" + itemId);
            }

            // verify the comment is of one of the three types
            if (originalComment.getCommentType().getId() != commentCommentType.getId()
                    && originalComment.getCommentType().getId() != recommendedCommentType.getId()
                    && originalComment.getCommentType().getId() != requiredCommentType.getId()) {
                return AjaxSupportHelper.createAndLogError(request.getType(),
                        INVALID_COMMENT_ERROR, "The original comment with id " + commentId + " can not be modified.",
                        "User id : " + userId + "\treview id : " + reviewId + "\titem id :" + itemId);
            }

            // verify the new comment can be set
            if (COMMENT_TYPE_COMMENT.equals(newType)) {
                originalComment.setCommentType(commentCommentType);
            } else if (COMMENT_TYPE_RECOMMENDED.equals(newType)) {
                originalComment.setCommentType(recommendedCommentType);
            } else if (COMMENT_TYPE_REQUIRED.equals(newType)) {
                originalComment.setCommentType(requiredCommentType);
            } else {
                return AjaxSupportHelper.createAndLogError(request.getType(),
                        INVALID_COMMENT_ERROR, "The new comment type " + newType + " can not be recognized.",
                        "User id : " + userId + "\treview id : " + reviewId + "\titem id :" + itemId);
            }
        }

        // calculate the review score
        // get the scorecard id from the review
        try {
            long scorecardId = review.getScorecard();
            // get scorecard
            Scorecard card = scorecardManager.getScorecard(scorecardId);
            // calculate the score
            double score = calculationManager.getScore(card, review);

            review.setScore(score);
        } catch (Exception e) {
            return AjaxSupportHelper.createAndLogError(request.getType(),
                    BUSINESS_ERROR, "Error in calculating score.",
                    "User id : " + userId + "\treview id : " + reviewId, e);
        }
        // update the item
        try {
            getReviewManager().updateReview(review, userId.toString());
        } catch (Exception e) {
            return AjaxSupportHelper.createAndLogError(request.getType(),
                    BUSINESS_ERROR, "Can't update review.",
                    "User id : " + userId + "\treview id : " + reviewId, e);
        }

        // succeed
        return AjaxSupportHelper.createAndLogSuccess(request.getType(), SUCCESS,
                "Suceeded to response appeal.", review.getScore(), "ResponseAppeal."
                + "\tuser id : " + userId + "review id :" + review.getId()
                + "\titem id : " + item.getId());
    }
}
