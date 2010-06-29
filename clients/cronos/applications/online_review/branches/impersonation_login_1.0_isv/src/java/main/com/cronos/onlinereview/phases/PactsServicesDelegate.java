/*
 * Created on 06/03/2007
 * Copyright by Refert Argentina 2003-2007
 */
package com.cronos.onlinereview.phases;

import com.topcoder.web.ejb.pacts.PactsClientServices;

/**
 * This class's primary focus was the version 1 assignment document calls.  Since we have
 * moved on to assignment v2 business rules, we don't need to make these calls anymore.  However
 * it's quite likely that we'll need more interaction between online review and PACTS in the future
 * so, i've left the this delegate class for enhancement in the future rather than delete it. 
 *
 * @author Bauna,dok
 * @version 1.0
 */
public class PactsServicesDelegate {
	private static final com.topcoder.util.log.Log log = com.topcoder.util.log.LogFactory
			.getLog(PactsServicesDelegate.class.getName());
	
//	Your nth place <design|development> submission for <component name> <version>
/*
	private final static MessageFormat SUBMISSION_TITLE_FORMATTER =
		new MessageFormat("Your {0} place {1} submission for {2} v{3}");
*/
	private PactsClientServices pactsClientServices;

	public PactsServicesDelegate() throws PactsServicesCreationException {
		try {
			pactsClientServices = ServiceLocator.getInstance().getPactsClientServices();
		} catch (ServiceLocatorNamingException e) {
			throw new PactsServicesCreationException(e);
		} catch (ServiceLocatorCreateException e) {
			throw new PactsServicesCreationException(e);
		} catch (ConfigurationException e) {
			throw new PactsServicesCreationException(e);
		}
	}
	
/*
	private String generateSubmissionTitle(Project project, String position) {
		
		return SUBMISSION_TITLE_FORMATTER.format(new Object[] {position, 
				project.getProjectCategory().getName(), 
				project.getProperty("Project Name"),
				project.getProperty("Project Version")});
	}
*/


    

    /**
	 * Create the AssigmentDocument for the winner and the runner up if they exists. 
	 * 
	 * @param project the project to create the AssignmentDocuments
	 * @return the AssignmentDocuments created
	 * @throws PactsServicesException
	 */
/* [OR-584] we don't do this style of assignment any more
	public AssignmentDocumentResult createAssignmentDocuments(Project project) throws PactsServicesException {
		log.log(Level.INFO, new LoggerMessage("Project", new Long(project.getId()), null, "Create assignment documents."));
		
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
			log.log(Level.ERROR,
					new LoggerMessage("Project", new Long(project.getId()), null,"Fail to create Assignment Documents", e));
			throw new PactsServicesException(e);
		}
	}
*/

/*	[OR-584] we don't do this style of assignment any more
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
			return pactsClientServices.addAssignmentDocument(userAD);
		} catch (Exception e) {
			log.log(Level.ERROR, new LoggerMessage("Project", new Long(projectId), null,
					"Fail to create new Assignment Document for the userId:" + userId + " and submissionTitle:" + submissionTitle, e));
			throw new PactsServicesException(e);
		}
	}*/

	/*[OR-584] we don't do this style of assignment any more
	public void deleteAssignmentDocumentsForProject(long projectId) throws RemoteException {
		List assignmentDocs = getPactsServices().getAssignmentDocumentByProjectId(projectId);
		for (Iterator i = assignmentDocs.iterator(); i.hasNext();) {
			AssignmentDocument ad = (AssignmentDocument) i.next();
			log.log(Level.INFO, "deleting AD: " + ad.getId() + " for Project: " + projectId);
			try {
				getPactsServices().deleteAssignmentDocument(ad);
			} catch (DeleteAffirmedAssignmentDocumentException e) {
				log.log(Level.ERROR,
						new LoggerMessage("Project", new Long(projectId), null, "Fail to delete assignment documents.", e));
			}
		}
	}*/

/*[OR-584] we don't do this style of assignment any more
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
*/

	private PactsClientServices getPactsServices() {
		return pactsClientServices;
	}
}
