/*
 * Created on 06/03/2007
 * Copyright by Refert Argentina 2003-2007
 */
package com.cronos.onlinereview.phases;

import java.rmi.RemoteException;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;

import com.topcoder.management.project.Project;
import com.topcoder.util.log.Level;
import com.topcoder.web.common.model.AssignmentDocument;
import com.topcoder.web.common.model.AssignmentDocumentStatus;
import com.topcoder.web.common.model.AssignmentDocumentType;
import com.topcoder.web.common.model.ComponentProject;
import com.topcoder.web.common.model.User;
import com.topcoder.web.ejb.pacts.DeleteAffirmedAssignmentDocumentException;
import com.topcoder.web.ejb.pacts.PactsServices;

/**
 * 
 * @author Bauna
 * @version 1.0
 */
public class PactsServicesDelegate {
	private static final com.topcoder.util.log.Log log = com.topcoder.util.log.LogFactory
			.getLog(PactsServicesDelegate.class.getName());
	
//	Your nth place <design|development> submission for <component name> <version>
	private final static MessageFormat SUBMISSION_TITLE_FORMATTER = 
		new MessageFormat("Your {0} place {1} submission for {2} v{3}");
	private PactsServices pactsServices;

	public PactsServicesDelegate() throws PactsServicesCreationException {
		try {
			pactsServices = ServiceLocator.getInstance().getPactsServices();
		} catch (ServiceLocatorNamingException e) {
			throw new PactsServicesCreationException(e);
		} catch (ServiceLocatorCreateException e) {
			throw new PactsServicesCreationException(e);
		} catch (ConfigurationException e) {
			throw new PactsServicesCreationException(e);
		}
	}
	
	private String generateSubmissionTitle(Project project, String position) {
		
		return SUBMISSION_TITLE_FORMATTER.format(new Object[] {position, 
				project.getProjectCategory().getName(), 
				project.getProperty("Project Name"),
				project.getProperty("Project Version")});
	}
	
	/**
	 * Create the AssigmentDocument for the winner and the runner up if they exists. 
	 * 
	 * @param project the project to create the AssignmentDocuments
	 * @return the AssignmentDocuments created
	 * @throws PactsServicesException
	 */
	public AssignmentDocumentResult createAssignmentDocuments(Project project) throws PactsServicesException {
		long projectId = project.getId();
		String winnerId = (String) project.getProperty("Winner External Reference ID");
		String runnerUpId = (String) project.getProperty("Runner-up External Reference ID");

		try {
			AssignmentDocumentResult result = new AssignmentDocumentResult();
			deleteAssignmentDocumentsForProject(projectId);
			if (winnerId != null) {
				result.setWinnerAssignmentDocument(createNewAssignmentDocument(projectId, winnerId, generateSubmissionTitle(project, "first")));
			}
			if (runnerUpId != null) {
				result.setRunnerUpAssignmentDocument(createNewAssignmentDocument(projectId, runnerUpId, generateSubmissionTitle(project, "second")));
			}
			return result;
		} catch (Exception e) {
			throw new PactsServicesException(e);
		}
	}
		
	public AssignmentDocument createNewAssignmentDocument(long projectId, String userId, String submissionTitle) throws PactsServicesException {
		log.log(Level.INFO, "Creating AD for User: " + userId + " Project: " + projectId);
		log.log(Level.DEBUG, "Submission Title: " + submissionTitle);
		AssignmentDocument userAD = new AssignmentDocument();
		userAD.setType(new AssignmentDocumentType(AssignmentDocumentType.COMPONENT_COMPETITION_TYPE_ID));
		userAD.setStatus(new AssignmentDocumentStatus(AssignmentDocumentStatus.PENDING_STATUS_ID));

		ComponentProject cp = new ComponentProject();
		cp.setId(new Long(projectId));
		userAD.setComponentProject(cp);

		User user = new User();
		user.setId(Long.valueOf(userId));
		userAD.setUser(user);

		userAD.setSubmissionTitle(submissionTitle);
		try {
			return pactsServices.addAssignmentDocument(userAD);
		} catch (Exception e) {
			throw new PactsServicesException(e);
		}
	}

	public void deleteAssignmentDocumentsForProject(long projectId) throws RemoteException {
		List assignmentDocs = getPactsServices().getAssignmentDocumentByProjectId(projectId);
		for (Iterator i = assignmentDocs.iterator(); i.hasNext();) {
			AssignmentDocument ad = (AssignmentDocument) i.next();
			log.log(Level.INFO, "deleting AD: " + ad.getId() + " for Project: " + projectId);
			try {
				getPactsServices().deleteAssignmentDocument(ad);
			} catch (DeleteAffirmedAssignmentDocumentException e) {
				log.log(Level.WARN, "");
			}
		}
	}

	public boolean getAllWinnersAcceptedAssignmentDocuments(Project project) throws RemoteException {
		
		Long winnerId = (project.getProperty("Winner External Reference ID") != null) ? 
				new Long((String) project.getProperty("Winner External Reference ID")) : (Long) null;
		Long runnerUpId = project.getProperty("Runner-up External Reference ID") != null ? 
				new Long((String) project.getProperty("Runner-up External Reference ID")) : null;

		List assignmentDocs = getPactsServices().getAssignmentDocumentByProjectId(project.getId());
		boolean winnerAccept = false;
		boolean runnerUpAccept = runnerUpId == null;
		for (Iterator i = assignmentDocs.iterator(); i.hasNext();) {
			AssignmentDocument ad = (AssignmentDocument) i.next();
			if (ad.getUser().getId().equals(winnerId)) {
				winnerAccept = ad.getStatus().equals(AssignmentDocumentStatus.AFFIRMED_STATUS_ID);
			} else if (ad.getUser().getId().equals(runnerUpId)) {
				runnerUpAccept = ad.getStatus().equals(AssignmentDocumentStatus.AFFIRMED_STATUS_ID);
			}
		}
		return winnerAccept && runnerUpAccept; 
	}

	private PactsServices getPactsServices() {
		return pactsServices;
	}
}
