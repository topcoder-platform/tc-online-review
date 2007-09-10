package com.topcoder.onlinereview.fixer;

import java.util.Date;

public class SubmitterResult implements Comparable<SubmitterResult> {
    
    private String userId;
    private String handle;
    private long resourceId;
    private String submissionId;
    private double[] reviewScores = new double[8];
    private int reviewScoresIndex = 0;
    private double finalScore;
    private double fixedScore;
    private int fixedRank;
    private int rank;
    private Date creationDate;

    public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public SubmitterResult(String submissionId) {
		setSubmissionId(submissionId);
	}

	public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
    
    public void setSubmissionId(String submissionId) {
        this.submissionId = submissionId;
    }
    
    public String getSubmissionId() {
        return submissionId;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getHandle() {
        return handle;
    }

    public long getResourceId() {
		return resourceId;
	}

	public void setResourceId(long resourceId) {
		this.resourceId = resourceId;
	}
    
    public void addReviewScore(double score) {
        reviewScores[reviewScoresIndex] = score;

        reviewScoresIndex++;
    }
    
    public int getReviewScoresLength() {
        return reviewScoresIndex;
    }

    public double[] getReviewScores() {
        return reviewScores;
    }

    public void setFinalScore(double score) {
        this.finalScore = score;
    }

    public double getFinalScore() {
        return finalScore;
    }
    
    public void setFixedScore(double score) {
        this.fixedScore = score;
    }

    public double getFixedScore() {
        return fixedScore;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getRank() {
        return rank;
    }
    
    public void setFixedRank(int rank) {
        this.fixedRank = rank;
    }

    public int getFixedRank() {
        return fixedRank;
    }

    @Override
	public int hashCode() {
		return submissionId.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		return submissionId.equals(((SubmitterResult) obj).submissionId);
	}

	@Override
	public String toString() {
        String str = "handle:" + getHandle() + " finalScore:" + getFinalScore() + " rank:"
                        + getRank() + "\n";

        for (int i = 0; i < this.reviewScoresIndex; ++i) {
            str = str + "score#" + (i + 1) + ": " + getReviewScores()[i] + "\n";
        }

        return str;
    }

	public int compareTo(SubmitterResult o) {
		Double s1 = getFixedScore();
		Double s2 = o.getFixedScore();
		int ret = s1.compareTo(s2);
		if (ret == 0) {
			return getCreationDate().compareTo(o.getCreationDate()) * -1;
		}
		return ret * -1;
	}
}
