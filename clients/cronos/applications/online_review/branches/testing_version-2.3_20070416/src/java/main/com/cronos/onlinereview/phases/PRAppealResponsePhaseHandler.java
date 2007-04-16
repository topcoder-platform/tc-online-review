/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.MessageFormat;

import com.cronos.onlinereview.external.ExternalUser;
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
import com.topcoder.web.common.model.AssignmentDocument;

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
	private String winnersEmailAssignmentDocumentLink;
	
	/** helper class for obtaining several managers */
	private ManagerHelper managerHelper;

	/** switch that indicates if OR have to create Assignment Documents in PACTs*/
	private boolean createAssignmentDocuments = false;
	
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
    	this.createAssignmentDocuments = "true".equals(PhasesHelper.getPropertyValue(namespace, "CreateAssignmentDocuments", "false").trim());
    	if (createAssignmentDocuments) {
    		this.winnersEmailTemplateSource = PhasesHelper.getPropertyValue(namespace, "WinnersEmail.EmailTemplateSource", true);
    		this.winnersEmailTemplateName = PhasesHelper.getPropertyValue(namespace, "WinnersEmail.EmailTemplateName",  true);
    		this.winnersEmailSubject = PhasesHelper.getPropertyValue(namespace, "WinnersEmail.EmailSubject", true);
    		this.winnersEmailAssignmentDocumentLink = PhasesHelper.getPropertyValue(namespace, "WinnersEmail.AssignmentDocumentLink", true);
    		this.winnersEmailFromAddress = PhasesHelper.getPropertyValue(namespace, "WinnersEmail.EmailFromAddress", true);	
    	} else {
    		log.log(Level.INFO, "Creation of Assignment Documents is disabled"); 
    	}
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
    		if (!toStart && isCreateAssignmentDocuments()) {
    			createAssignmentDocuments(projectId);
    		}
    	} catch (Throwable e) {
			throw new PhaseHandlingException(e.getMessage(), e);
		} finally {
    		PRHelper.close(conn);
    	}
    }

    /**
     * Creates the Assigment Documents for the winners of the project if there are any.
     * 
     * @param projectId the project id.
     * @throws Exception 
     */
    private void createAssignmentDocuments(long projectId) throws Exception {
    	
    	Project project = getManagerHelper().getProjectManager().getProject(projectId);
    	AssignmentDocumentResult adResult = new PactsServicesDelegate().createAssignmentDocuments(project);
    	
    	sendMailForWinners(project, adResult);
	}

	private void sendMailForWinners(Project project, AssignmentDocumentResult adResult) throws Exception {
		if (adResult.hasWinner()) {
			AssignmentDocument winnerAD = adResult.getWinnerAssignmentDocument();
			sendWinnersEmailForUser(project, winnerAD, managerHelper.getUserRetrieval().retrieveUser(
					winnerAD.getUser().getId().longValue()), "1st");
		}
		if (adResult.hasRunnerUp()) {
			AssignmentDocument runnerUpAD = adResult.getWinnerAssignmentDocument();
			sendWinnersEmailForUser(project, runnerUpAD, managerHelper.getUserRetrieval().retrieveUser(
					runnerUpAD.getUser().getId().longValue()), "2nd");
		}
	}

	private void sendWinnersEmailForUser(Project project, AssignmentDocument ad, ExternalUser user, String position) throws Exception {
		DocumentGenerator docGenerator = DocumentGenerator.getInstance();
		Template template = getEmailTemplate();
		log.log(Level.DEBUG, "sending winner email for projectId: " + project.getId() + " handle: " + user.getHandle() + " position: " + position);
		TemplateFields root = setTemplateFieldValues(docGenerator.getFields(template), project, user, ad, position);
		
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
			for (int i = 0; i < roles.length; i++) {
				ResourceRole role = roles[i];
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
	
	private TemplateFields setTemplateFieldValues(TemplateFields root, Project project, ExternalUser user, AssignmentDocument ad, String position) throws BaseException {
		Node[] nodes = root.getNodes();

		for (int i = 0; i < nodes.length; i++) {
			if (nodes[i] instanceof Field) {
				Field field = (Field) nodes[i];

				if ("PROJECT_TYPE".equals(field.getName())) {
					field.setValue(project.getProjectCategory().getDescription());
				} else if ("PROJECT_NAME".equals(field.getName())) {
					field.setValue((String) project.getProperty(PROJECT_NAME));
				} else if ("SCORE".equals(field.getName())) {
					field.setValue((String) getResourceForProjectAndUser(project, 
							String.valueOf(user.getId())).getProperty("Final Score")); 
				} else if ("PLACE".equals(field.getName())) {
					field.setValue(position);
				} else if ("ASSIGNMET_DOCUMENT_LINK".equals(field.getName())) {
					field.setValue(MessageFormat.format(winnersEmailAssignmentDocumentLink, new Object[] {ad.getId().toString()}));
				} 
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

	public boolean isCreateAssignmentDocuments() {
		return createAssignmentDocuments;
	}
}
