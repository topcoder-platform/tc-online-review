package com.topcoder.management.review.scorefixer;

public class ReviewData {
	// r.review_id, riRev.value reviewer, riSub.value submitter, u.project_id
	private long reviewId;
	private long reviewerId;
	private long submitterId;
	private long projectId;

	public long getReviewId() {
		return reviewId;
	}

	public void setReviewId(long reviewId) {
		this.reviewId = reviewId;
	}

	public long getReviewerId() {
		return reviewerId;
	}

	public void setReviewerId(long reviewerId) {
		this.reviewerId = reviewerId;
	}

	public long getSubmitterId() {
		return submitterId;
	}

	public void setSubmitterId(long submitterId) {
		this.submitterId = submitterId;
	}

	public long getProjectId() {
		return projectId;
	}

	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}
	
	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder("{"); 
		return buf.append(getProjectId()).append(", ")
			.append(getReviewId()).append(", ")
			.append(getReviewerId()).append(", ")
			.append(getSubmitterId()).append("}")
			.toString();
	}
}
