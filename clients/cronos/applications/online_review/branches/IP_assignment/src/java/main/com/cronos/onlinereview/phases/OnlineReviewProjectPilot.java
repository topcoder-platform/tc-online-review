package com.cronos.onlinereview.phases;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.cronos.onlinereview.external.ExternalUser;
import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.management.phase.PhaseManagementException;
import com.topcoder.management.phase.PhaseManager;
import com.topcoder.management.phase.autopilot.AutoPilotResult;
import com.topcoder.management.phase.autopilot.ConfigurationException;
import com.topcoder.management.phase.autopilot.impl.DefaultProjectPilot;
import com.topcoder.management.project.Project;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.resource.ResourceRole;
import com.topcoder.management.resource.search.ResourceFilterBuilder;
import com.topcoder.message.email.EmailEngine;
import com.topcoder.message.email.TCSEmailMessage;
import com.topcoder.search.builder.filter.AndFilter;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.util.file.DocumentGenerator;
import com.topcoder.util.file.Template;
import com.topcoder.util.file.fieldconfig.Field;
import com.topcoder.util.file.fieldconfig.Node;
import com.topcoder.util.file.fieldconfig.TemplateFields;
import com.topcoder.util.log.Level;
import com.topcoder.util.log.Log;
import com.topcoder.web.common.model.AssignmentDocument;
import com.topcoder.web.common.model.AssignmentDocumentStatus;
import com.topcoder.web.ejb.pacts.PactsServices;

public class OnlineReviewProjectPilot extends DefaultProjectPilot {
	private static final com.topcoder.util.log.Log log = com.topcoder.util.log.LogFactory
			.getLog(OnlineReviewProjectPilot.class.getName());

	/** constant for "Project Name" project info. */
	private static final String PROJECT_NAME = "Project Name";

	/** constant for "Project Version" project info. */
	private static final String PROJECT_VERSION = "Project Version";

	private static final AssignmentDocumentStatus AD_PENDING_STATUS = new AssignmentDocumentStatus(
			AssignmentDocumentStatus.PENDING_STATUS_ID);
	private static final AssignmentDocumentStatus AD_AFFIRMED_STATUS = new AssignmentDocumentStatus(
			AssignmentDocumentStatus.AFFIRMED_STATUS_ID);
	private static final String AUTO_PILOT_AD_CHANGE = "AutoPilot AD Change";
	private static final SimpleDateFormat RUN_DATE_FORMATTER = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");
	private static final String DEFAULT_NAMESPACE = OnlineReviewProjectPilot.class.getName();

	/** format for the email timestamp. Will format as "Fri, Jul 28, 2006 01:34 PM EST". */
	private static final SimpleDateFormat EMAIL_TIMESTAMP_FORMAT = new SimpleDateFormat("EEE, MMM d, yyyy hh:mm a z");

	/**
	 * Represents the <code>PactsServices</code> instance.
	 */
	private PactsServices pactsServices = null;
	private ManagerHelper managerHelper = null;

	/**
	 * <p>
	 * Constructs a new instance of DefaultProjectPilot class. This will initialize the phase manager instance using
	 * object factory. The object factory is initialized with this class' full name as its configuration namespace.
	 * Inside this namespace, a property with the key of PhaseManager's full name is used to retrieve phase manager
	 * instance.<br>
	 * {@link #DEFAULT_SCHEDULED_STATUS_NAME} will be used as scheduled status name; {@link #DEFAULT_OPEN_STATUS_NAME}
	 * will be used as open status name; {@link #DEFAULT_LOG_NAME} will be used as log name.
	 * </p>
	 * 
	 * @throws ConfigurationException
	 *             if any error occurs instantiating the object factory or the phase manager instance
	 */
	public OnlineReviewProjectPilot() throws ConfigurationException {
		super();
		createManagerHelper();
	}

	/**
	 * <p>
	 * Constructs a new instance of DefaultProjectPilot class using the given namespace/phaseManagerKey to get
	 * PhaseManager instance with object factory, the given scheduled/open status name, and the given log name. This
	 * will initialize the phase manager instance using object factory. The object factory is initialized with
	 * namespace. Inside this namespace, a property with the key of phaseManagerKey is used to retrieve phase manager
	 * instance. A Log instance with the specified logName is then retrieved.<br>
	 * </p>
	 * 
	 * @param namespace
	 *            the namespace to initialize object factory with
	 * @param phaseManagerKey
	 *            the key defining the PhaseManager instance
	 * @param scheduledStatusName
	 *            A non-null string representing a phase status of scheduled
	 * @param openStatusName
	 *            A non-null string representing a phase status of open
	 * @param logName
	 *            A non-null string representing the log name to use for auditing
	 * @throws IllegalArgumentException
	 *             if any parameter is null or empty (trimmed) string
	 * @throws ConfigurationException
	 *             if any error occurs instantiating the object factory or the phase manager instance or the logger
	 */
	public OnlineReviewProjectPilot(String namespace, String phaseManagerKey, String scheduledStatusName,
			String openStatusName, String logName) throws ConfigurationException {
		super(namespace, phaseManagerKey, scheduledStatusName, openStatusName, logName);
		createManagerHelper();
	}

	/**
	 * <p>
	 * Constructs a new instance of DefaultProjectPilot class using the given PhaseManager instance and parameters.
	 * </p>
	 * 
	 * @param phaseManager
	 *            the PhaseManager instance to use
	 * @param scheduledStatusName
	 *            A non-null string representing a phase status of scheduled
	 * @param openStatusName
	 *            A non-null string representing a phase status of open
	 * @param logger
	 *            the Logger instance to use for auditing
	 * @throws IllegalArgumentException
	 *             if any of the parameter is null or any of the string parameters are empty (trimmed) string
	 */
	public OnlineReviewProjectPilot(PhaseManager phaseManager, String scheduledStatusName, String openStatusName,
			Log logger) {
		super(phaseManager, scheduledStatusName, openStatusName, logger);
		createManagerHelper();
	}

	private void createManagerHelper() {
		try {
			managerHelper = new ManagerHelper();
		} catch (com.cronos.onlinereview.phases.ConfigurationException e) {
			throw new IllegalStateException("error creating the ManagerHelper", e);
		}
	}

	@Override
	public AutoPilotResult advancePhases(long projectId, String operator) {
		AutoPilotResult result = super.advancePhases(projectId, operator);
		try {
			Project project = managerHelper.getProjectManager().getProject(projectId);
			log.log(Level.DEBUG, "check AD for projectId: " + projectId);
			checkAssignmentDocumentStatusChange(project, operator);

		} catch (Throwable e) {
			ByteArrayOutputStream s = new ByteArrayOutputStream();
			e.printStackTrace(new PrintStream(s));
			getLogger().log(
					Level.ERROR,
					"Error occurs while trying to check Assignment Documents of project with id " + projectId
							+ ", stack trace : " + s.toString());
		}
		return result;
	}

	/**
	 * @return
	 * @throws ServiceLocatorNamingException
	 * @throws ServiceLocatorCreateException
	 * @throws com.cronos.onlinereview.phases.ConfigurationException
	 */
	private PactsServices getPactsServices() throws ServiceLocatorNamingException, ServiceLocatorCreateException,
			com.cronos.onlinereview.phases.ConfigurationException {
		if (pactsServices == null) {
			pactsServices = ServiceLocator.getInstance().getPactsServices();
		}
		return pactsServices;
	}

	private void checkAssignmentDocumentStatusChange(Project project, String operator) throws PhaseManagementException {
		try {
			List assignmentsDocuments = getPactsServices().getAssignmentDocumentByProjectId(project.getId());
			log.log(Level.DEBUG, "projectId: " + project.getId() + " ADs: " + assignmentsDocuments);
			for (Iterator i = assignmentsDocuments.iterator(); i.hasNext();) {
				AssignmentDocument ad = (AssignmentDocument) i.next();
				String runDateValue = (String) project.getProperty(AUTO_PILOT_AD_CHANGE);
				Date runDate = (runDateValue == null) ? null : RUN_DATE_FORMATTER.parse(runDateValue);
				log.log(Level.DEBUG, "projectId: " + project.getId() + " Autopilot date: " + runDateValue
						+ " ad modify date: " + RUN_DATE_FORMATTER.format(ad.getModifyDate()));
				if ((runDate == null || runDate.compareTo(ad.getModifyDate()) < 0) && !ad.getStatus().getDescription().equals(AD_AFFIRMED_STATUS.getDescription())
						 && !ad.getStatus().getDescription().equals(AD_PENDING_STATUS.getDescription())) {
					informAssignmentDocumentStatusChange(project, ad);
					project.setProperty(AUTO_PILOT_AD_CHANGE, RUN_DATE_FORMATTER.format(new Date()));
					managerHelper.getProjectManager().updateProject(project, AUTO_PILOT_AD_CHANGE, operator);
				}
			}
		} catch (Exception e) {
			throw new PhaseManagementException(e.getMessage(), e);
		}
	}

	private long[] getManagersForProject(Project project) throws PhaseManagementException {
		try {
			ResourceRole managerRole = null;
			ResourceRole[] roles = managerHelper.getResourceManager().getAllResourceRoles();
			for (int i = 0; i < roles.length; i++) {
				ResourceRole role = roles[i];
				if ("Manager".equals(role.getName())) {
					managerRole = role;
					break;
				}
			}
			if (managerRole == null) {
				throw new PhaseHandlingException("can't find manager role id");
			}
			// Create filter to filter only the resources for the project in question
			Filter filterProject = ResourceFilterBuilder.createProjectIdFilter(project.getId());
			Filter filterManager = ResourceFilterBuilder.createResourceRoleIdFilter(managerRole.getId());
			// Create combined final filter
			Filter filter = new AndFilter(filterProject, filterManager);

			// Perform search for resources
			Resource[] managers = managerHelper.getResourceManager().searchResources(filter);
			if (managers.length == 0) {
				return new long[0];
			}
			long[] usersId = new long[managers.length];
			for (int i = 0; i < managers.length; i++) {
				usersId[i] = Long.parseLong((String) managers[i].getProperty("External Reference ID"));
			}
			return usersId;
		} catch (Exception e) {
			throw new PhaseManagementException(e.getMessage(), e);
		}
	}

	private Template getEmailTemplate() throws Exception {

		String templateSource = PhasesHelper.getPropertyValue(DEFAULT_NAMESPACE,
				"AssigmentDocumentEmail.EmailTemplateSource", true);
		String templateName = PhasesHelper.getPropertyValue(DEFAULT_NAMESPACE,
				"AssigmentDocumentEmail.EmailTemplateName", true);

		return DocumentGenerator.getInstance().getTemplate(templateSource, templateName);
	}

	private void informAssignmentDocumentStatusChange(Project project, AssignmentDocument ad)
			throws PhaseManagementException {
		try {
			long[] usersId = getManagersForProject(project);
			if (usersId.length == 0) {
				return;
			}

			DocumentGenerator docGenerator = DocumentGenerator.getInstance();
			Template template = getEmailTemplate();
			String fromAddress = PhasesHelper.getPropertyValue(DEFAULT_NAMESPACE,
					"AssigmentDocumentEmail.EmailFromAddress", true);
			ExternalUser[] users = managerHelper.getUserRetrieval().retrieveUsers(usersId);
			for (int i = 0; i < users.length; i++) {
				ExternalUser user = users[i];
				log.log(Level.DEBUG, "sending email for: " + user.getHandle() + " - email: " + user.getEmail());
				// for each external user, set field values
				TemplateFields root = setTemplateFieldValues(docGenerator.getFields(template), user, project, ad);

				String emailContent = docGenerator.applyTemplate(root);

				TCSEmailMessage message = new TCSEmailMessage();
				message.setSubject("[Assignment Document] - " + project.getProperty(PROJECT_NAME));
				message.setBody(emailContent);
				message.setFromAddress(fromAddress);
				message.setToAddress(user.getEmail(), TCSEmailMessage.TO);
				EmailEngine.send(message);
			}

		} catch (Exception e) {
			throw new PhaseManagementException(e.getMessage(), e);
		}
	}

	/**
	 * This method sets the values of the template fields with user and project information based on bStart variable
	 * which is true if phase is to start, false if phase is to end.
	 * 
	 * @param root
	 *            template fields.
	 * @param user
	 *            the user to be notified.
	 * @param project
	 *            project instance.
	 * 
	 * @return template fields with data set.
	 */
	private TemplateFields setTemplateFieldValues(TemplateFields root, ExternalUser user, Project project,
			AssignmentDocument ad) {
		Node[] nodes = root.getNodes();

		for (int i = 0; i < nodes.length; i++) {
			if (nodes[i] instanceof Field) {
				Field field = (Field) nodes[i];

				if ("TIMESTAMP".equals(field.getName())) {
					field.setValue(EMAIL_TIMESTAMP_FORMAT.format(new Date()));
				} else if ("USER_FIRST_NAME".equals(field.getName())) {
					field.setValue(user.getFirstName());
				} else if ("USER_LAST_NAME".equals(field.getName())) {
					field.setValue(user.getLastName());
				} else if ("USER_HANDLE".equals(field.getName())) {
					field.setValue(user.getHandle());
				} else if ("PROJECT_NAME".equals(field.getName())) {
					field.setValue((String) project.getProperty(PROJECT_NAME));
				} else if ("PROJECT_VERSION".equals(field.getName())) {
					field.setValue((String) project.getProperty(PROJECT_VERSION));
				} else if ("ASSIGNMENT_DOCUMENT".equals(field.getName())) {
					field.setValue(ad.getSubmissionTitle());
				} else if ("ASSIGNMENT_DOCUMENT_STATUS".equals(field.getName())) {
					field.setValue(ad.getStatus().getDescription());
				}
			}
		}

		return root;
	}

}
