/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.MessageFormat;

import com.cronos.onlinereview.external.ExternalUser;
import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.deliverable.UploadManager;
import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.management.phase.PhaseManagementException;
import com.topcoder.management.project.Project;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.resource.ResourceRole;
import com.topcoder.management.resource.search.ResourceFilterBuilder;
import com.topcoder.message.email.EmailEngine;
import com.topcoder.message.email.TCSEmailMessage;
import com.topcoder.project.phases.Phase;
import com.topcoder.search.builder.filter.AndFilter;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.util.errorhandling.BaseException;
import com.topcoder.util.file.DocumentGenerator;
import com.topcoder.util.file.Template;
import com.topcoder.util.file.fieldconfig.Field;
import com.topcoder.util.file.fieldconfig.Node;
import com.topcoder.util.file.fieldconfig.TemplateFields;
import com.topcoder.util.log.Level;

/**
 * The extend from AppealsResponsePhaseHandler to add on the logic to push data to project_result.
 *
 * @author brain_cn
 * @version 1.0
 */
public class PRAppealResponsePhaseHandler extends AppealsResponsePhaseHandler {
	private static final com.topcoder.util.log.Log log = com.topcoder.util.log.LogFactory
			.getLog(PRAppealResponsePhaseHandler.class.getName());
	/** constant for "Project Name" project info. */
	private static final String PROJECT_NAME = "Project Name";
	
	/** winners email template source type */
	private String winnersEmailTemplateSource;
	
	/** winners email template name */
	private String winnersEmailTemplateName;
	
	/** subject for the winners email */
	private String winnersEmailSubject;
	
	/** sender address of the winners email */
	private String winnersEmailFromAddress;
	
	/** link to the accept the Assignment Document */
/*[OR-584] we're not doing assignment like this anymore, v2 only now
	private String winnersEmailAssignmentDocumentLink;
*/

	/** helper class for obtaining several managers */
	private ManagerHelper managerHelper;

    /** start date to check or create assigments documents*/
/*[OR-584] we're not doing assignment like this anymore, v2 only now
	private Date assignmentDocumentsStartDate;
*/

    /** switch that indicates if OR have to create Assignment Documents in PACTs*/
/*[OR-584] we're not doing assignment like this anymore, v2 only now
    private boolean createAssignmentDocuments = false;
*/

    /**
     * Create a new instance of AppealsResponsePhaseHandler using the default namespace for loading configuration settings.
     *
     * @throws ConfigurationException if errors occurred while loading configuration settings.
     */
	public PRAppealResponsePhaseHandler() throws ConfigurationException {
		this(DEFAULT_NAMESPACE);
	}

    /**
     * Create a new instance of AppealsResponsePhaseHandler using the given namespace for loading configuration settings.
     *
     * @param namespace the namespace to load configuration settings from.
     * @throws ConfigurationException if errors occurred while loading configuration settings or required properties
     * missing.
     * @throws IllegalArgumentException if the input is null or empty string.
     */
	public PRAppealResponsePhaseHandler(String namespace) throws ConfigurationException {
		super(namespace);
		obtainWinnnersEmailConfigProperties(namespace);
		managerHelper = new ManagerHelper();
	}
	
    private void obtainWinnnersEmailConfigProperties(String namespace) throws ConfigurationException {
/* [OR-584] we're not doing assignment like this anymore, v2 only now
    	this.createAssignmentDocuments = "true".equals(PhasesHelper.getPropertyValue(namespace, "CreateAssignmentDocuments", "false").trim());
    	if (createAssignmentDocuments) {
*/
/*
    		try {
    			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    			sdf.setLenient(false);
    			this.assignmentDocumentsStartDate = sdf.parse(PhasesHelper.getPropertyValue(namespace, "AssignmentDocumentsStartDate", "06/18/2007"));
    		} catch (ParseException e) {
    			throw new ConfigurationException("can not read AssignmentDocumentsStartDate from configuration", e);
    		}
*/
    		this.winnersEmailTemplateSource = PhasesHelper.getPropertyValue(namespace, "WinnersEmail.EmailTemplateSource", true);
    		this.winnersEmailTemplateName = PhasesHelper.getPropertyValue(namespace, "WinnersEmail.EmailTemplateName",  true);
    		this.winnersEmailSubject = PhasesHelper.getPropertyValue(namespace, "WinnersEmail.EmailSubject", true);
/*
    		this.winnersEmailAssignmentDocumentLink = PhasesHelper.getPropertyValue(namespace, "WinnersEmail.AssignmentDocumentLink", true);
*/
    		this.winnersEmailFromAddress = PhasesHelper.getPropertyValue(namespace, "WinnersEmail.EmailFromAddress", true);
/*
    	} else {
    		log.log(Level.INFO, "Creation of Assignment Documents is disabled"); 
    	}
*/
	}

	/**
     * Provides additional logic to execute a phase. this exetension will update valid_submission_ind, submit_timestamp
     * field of project_result table.</p>
     *
     * @param phase The input phase to check.
     * @param operator The operator that execute the phase.
     *
     * @throws PhaseNotSupportedException if the input phase type is not "Submission" type.
     * @throws PhaseHandlingException if there is any error occurred while processing the phase.
     * @throws IllegalArgumentException if the input parameters is null or empty string.
     */
    public void perform(Phase phase, String operator) throws PhaseHandlingException {
    	super.perform(phase, operator);
    	long projectId = phase.getProject().getId();
        boolean toStart = PhasesHelper.checkPhaseStatus(phase.getPhaseStatus());
    	// Only will perform while submission phase is ended
    	Connection conn = this.createConnection();
    	try {
    		processPR(projectId, conn, toStart);
            //send winner email as appropriate here
            if (!toStart) {
                sendMailForWinners(getManagerHelper().getProjectManager().getProject(projectId));
            }
/* [OR-584] don't need to do this anymore, we only accept v2 documents now
            if (!toStart && isCreateAssignmentDocuments()) {
                createAssignmentDocuments(projectId);
            }
*/
    	} catch (Throwable e) {
			throw new PhaseHandlingException(e.getMessage(), e);
		} finally {
    		PRHelper.close(conn);
    	}
    }

    /**
     * Verifies if assignment documents need to be created for a project.
     * 
     * @param project the project
     * @return true if assignment documents must to be created.
     */
/* [OR-584] don't nee this anymore, we're only doing v2 assignment now
    private boolean verifyCreateAssignmentDocument(Project project) {
    	ProjectCategory category = project.getProjectCategory();
    
        return project.getCreationTimestamp().after(assignmentDocumentsStartDate)
        		&& (category.getId() == 1 || category.getId() == 2);
    }
*/

    /**
     * Creates the Assigment Documents for the winners of the project if there are any.
     * 
     * @throws Exception
     */
/* [OR-584] don't need this anymore, we're only doing v2 assignment
    private void createAssignmentDocuments(long projectId) throws Exception {
    	Project project = getManagerHelper().getProjectManager().getProject(projectId);
    	if (verifyCreateAssignmentDocument(project)) {
    		AssignmentDocumentResult adResult = new PactsServicesDelegate().createAssignmentDocuments(project); 	
    		sendMailForWinners(project, adResult);
    	} else {
    		log.log(Level.INFO, "Don't creating AD for project: " + projectId + " because it's creation date is: " + project.getCreationTimestamp());
    	}
	}
*/

	private void sendMailForWinners(Project project) throws Exception {
        log.log(Level.DEBUG, "we're in the send email method");
       String winnerId = (String) project.getProperty("Winner External Reference ID");
       String runnerUpId = (String) project.getProperty("Runner-up External Reference ID");

        if (winnerId!=null) {
			sendWinnersEmailForUser(project,
                    managerHelper.getUserRetrieval().retrieveUser(Long.parseLong(winnerId)), "1st");
		}
		if (runnerUpId!=null) {
			sendWinnersEmailForUser(project,
                    managerHelper.getUserRetrieval().retrieveUser(Long.parseLong(runnerUpId)), "2nd");
		}
	}

	private void sendWinnersEmailForUser(Project project, ExternalUser user, String position) throws Exception {
		DocumentGenerator docGenerator = DocumentGenerator.getInstance();
		Template template = getEmailTemplate();
		log.log(Level.DEBUG, "sending winner email for projectId: " + project.getId() + " handle: " + user.getHandle() + " position: " + position);
		TemplateFields root = setTemplateFieldValues(docGenerator.getFields(template), project, user, position);
		
		String emailContent = docGenerator.applyTemplate(root);
		TCSEmailMessage message = new TCSEmailMessage();
		message.setSubject(MessageFormat.format(winnersEmailSubject, new Object[] {"Online Review", project.getProperty(PROJECT_NAME)}));
		message.setBody(emailContent);
		message.setFromAddress(winnersEmailFromAddress);
		message.setToAddress(user.getEmail(), TCSEmailMessage.TO);
		EmailEngine.send(message);
	}
	
	private Resource getResourceForProjectAndUser(Project project, String userId) throws PhaseManagementException {
		try {
			ResourceRole submitterRole = null;
			ResourceRole[] roles = managerHelper.getResourceManager().getAllResourceRoles();
			log.log(Level.DEBUG, "roles size: " + roles.length);
			for (int i = 0; i < roles.length; i++) {
				ResourceRole role = roles[i];
				log.log(Level.DEBUG, "roles id: " + roles[i].getId() + ", name: " + roles[i].getName());
				if ("Submitter".equals(role.getName())) {
					submitterRole = role;
					break;
				}
			}
			if (submitterRole == null) {
				throw new PhaseHandlingException("can't find submitter role id");
			}
			// Create filter to filter only the resources for the project in question
			Filter filterProject = ResourceFilterBuilder.createProjectIdFilter(project.getId());
			Filter filterManager = ResourceFilterBuilder.createResourceRoleIdFilter(submitterRole.getId());
			// Create combined final filter
			Filter filter = new AndFilter(filterProject, filterManager);

			// Perform search for resources
			Resource[] submitters = managerHelper.getResourceManager().searchResources(filter);
			for (int i = 0; i < submitters.length; i++) {
				Resource resource = submitters[i];
				if (userId.equals(resource.getProperty("External Reference ID"))) {
					return resource;
				}
			}
		} catch (Exception e) {
			throw new PhaseManagementException(e.getMessage(), e);
		}
		throw new PhaseHandlingException("couldn't found the resource for userId: " + userId + " projectId: " + project.getId());
	}
	
	private TemplateFields setTemplateFieldValues(TemplateFields root, Project project, ExternalUser user, String position) throws BaseException {
		Node[] nodes = root.getNodes();

		for (int i = 0; i < nodes.length; i++) {
			if (nodes[i] instanceof Field) {
				Field field = (Field) nodes[i];

				if ("PROJECT_TYPE".equals(field.getName())) {
					field.setValue(project.getProjectCategory().getDescription());
				} else if ("PROJECT_NAME".equals(field.getName())) {
					field.setValue((String) project.getProperty(PROJECT_NAME));
				} else if ("SCORE".equals(field.getName())) {
					// get all the submissions for the user in the project
                    Long[] submissions = getResourceForProjectAndUser(project, 
                            String.valueOf(user.getId())).getSubmissions();
                    int placement = position.equals("1st") ? 1 : 2;
                    UploadManager uploadManager = managerHelper.getUploadManager();
                    for (Long submissionId : submissions) {
                        // get the score for the placement depending on the position
                        Submission submission = uploadManager.getSubmission(submissionId);
                        if(submission.getPlacement() != null && submission.getPlacement() == placement) {
                            field.setValue(submission.getFinalScore()+"");
                            break;
                        }
                    }
				} else if ("PLACE".equals(field.getName())) {
					field.setValue(position);
				} /* [OR-584] don't need this link anymore
				else if ("ASSIGNMET_DOCUMENT_LINK".equals(field.getName())) {
					field.setValue(MessageFormat.format(winnersEmailAssignmentDocumentLink, new Object[] {ad.getId().toString()}));
				} */
			}
		}

		return root;
	}

	private Template getEmailTemplate() throws Exception {
		return DocumentGenerator.getInstance().getTemplate(winnersEmailTemplateSource, winnersEmailTemplateName);
	}

	/**
     * Pull data to project_result.
     * 
     * @param projectId the projectId
     * @throws PhaseHandlingException if error occurs
     */
    public void processPR(long projectId, Connection conn, boolean toStart) throws PhaseHandlingException {
    	try {
        	PRHelper.processAppealResponsePR(projectId, conn, toStart);
    	} catch(SQLException e) {
    		throw new PhaseHandlingException("Failed to push data to project_result", e);
    	}
    }

/* [OR-584] we're not doing assignment like this anymore, v2 only now
	public boolean isCreateAssignmentDocuments() {
		return createAssignmentDocuments;
	}
*/
}
