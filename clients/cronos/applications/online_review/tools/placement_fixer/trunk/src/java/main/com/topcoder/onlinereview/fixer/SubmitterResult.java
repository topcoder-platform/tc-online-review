package com.topcoder.onlinereview.fixer;

public class SubmitterResult {
    
    private String userId;

    private String handle;
    
    private String submissionId;

    private double[] reviewScores = new double[8];

    private int reviewScoresIndex = 0;

    private double finalScore;
    
    private double fixedScore;
    
    private int fixedRank;

    private int rank;

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

    public String toString() {
        String str = "handle:" + getHandle() + " finalScore:" + getFinalScore() + " rank:"
                        + getRank() + "\n";

        for (int i = 0; i < this.reviewScoresIndex; ++i) {
            str = str + "score#" + (i + 1) + ": " + getReviewScores()[i] + "\n";
        }

        return str;
    }
}
