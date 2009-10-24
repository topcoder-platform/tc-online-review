/*
 * Created on 13/03/2007
 * Copyright by Refert Argentina 2003-2007
 */
package com.cronos.onlinereview.phases;

import com.topcoder.web.ejb.pacts.assignmentdocuments.AssignmentDocument;

/**
 * Holds the <code>AssignmentDocument</code>'s created
 * 
 * @version 1.0
 * @author Bauna
 */
public class AssignmentDocumentResult {
	/** holds the AD for the winner */
	private AssignmentDocument winnerAssignmentDocument;
	
	/** holds the AD for the runner up */
	private AssignmentDocument runnerUpAssignmentDocument;

	public AssignmentDocument getRunnerUpAssignmentDocument() {
		return runnerUpAssignmentDocument;
	}

	public void setRunnerUpAssignmentDocument(AssignmentDocument runnerUpAssignmentDocument) {
		this.runnerUpAssignmentDocument = runnerUpAssignmentDocument;
	}

	public AssignmentDocument getWinnerAssignmentDocument() {
		return winnerAssignmentDocument;
	}

	public void setWinnerAssignmentDocument(AssignmentDocument winnerAssignmentDocument) {
		this.winnerAssignmentDocument = winnerAssignmentDocument;
	}
	
	public boolean hasWinner() {
		return getWinnerAssignmentDocument() != null;
	}
	
	public boolean hasRunnerUp() {
		return getRunnerUpAssignmentDocument() != null;
	}
}
