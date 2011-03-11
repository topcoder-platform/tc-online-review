package com.cronos.onlinereview.phases;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import com.topcoder.management.phase.PhaseManager;
import com.topcoder.management.phase.autopilot.AutoPilotResult;
import com.topcoder.management.phase.autopilot.ConfigurationException;
import com.topcoder.management.phase.autopilot.impl.DefaultProjectPilot;
import com.topcoder.util.log.Level;
import com.topcoder.util.log.Log;

public class OnlineReviewProjectPilot extends DefaultProjectPilot {
	private static final com.topcoder.util.log.Log log = com.topcoder.util.log.LogFactory
			.getLog(OnlineReviewProjectPilot.class.getName());
	
	/** default namespace to read configuration parameters from  */
	private static final String DEFAULT_NAMESPACE = OnlineReviewProjectPilot.class.getName();

	/** Assignment Document status change email template source for managers */
	private String managersEmailTemplateSource = null;
	
	/** Assignment Document status change email template name for managers */
	private String managersEmailTemplateName = null;
	
	/** Sender of the Assignment Document status change email for managers */
	private String managersEmailFromAddress = null;
	
	/** Switch that indicates if AP should check AD status changes */
	private boolean checkAssignmentDocumentsStatus = false;

	/**
	 * <p>
	 * Constructs a new instance of OnlineReviewProjectPilot class. This will initialize the phase manager instance using
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
		log.log(Level.INFO, "Create OnlineReviewProjectPilot instance with namespace:" + DEFAULT_NAMESPACE);
		configure(DEFAULT_NAMESPACE);
	}

	/**
	 * <p>
	 * Constructs a new instance of OnlineReviewProjectPilot class using the given namespace/phaseManagerKey to get
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
		log.log(Level.INFO, "Create OnlineReviewProjectPilot instance with namespace:" + namespace);
		configure(namespace);
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
		log.log(Level.INFO, "Create OnlineReviewProjectPilot instance with namespace:" + DEFAULT_NAMESPACE);
		configure(DEFAULT_NAMESPACE);		
	}
	
	private void configure(String namespace) {		
		try {
		    String propertyValue = PhasesHelper.getPropertyValue(namespace, "CheckAssignmentDocumentsStatus", false);
			checkAssignmentDocumentsStatus = propertyValue!=null && "true".equals(propertyValue.trim());
			if (!checkAssignmentDocumentsStatus) {
				log.log(Level.INFO, "The status check of the Assignment Documents is disabled");
			} else {
				managersEmailTemplateSource = PhasesHelper.getPropertyValue(namespace,
						"AssigmentDocumentEmail.EmailTemplateSource", true);
				managersEmailTemplateName = PhasesHelper.getPropertyValue(namespace,
						"AssigmentDocumentEmail.EmailTemplateName", true);
				managersEmailFromAddress = PhasesHelper.getPropertyValue(namespace,
						"AssigmentDocumentEmail.EmailFromAddress", true);
			}
		} catch (com.cronos.onlinereview.phases.ConfigurationException e) {
			ByteArrayOutputStream s = new ByteArrayOutputStream();
			e.printStackTrace(new PrintStream(s));
			log.log(Level.ERROR, "configuration problem: " + e.getMessage() + 
					"\nstacktrace: " + s.toString());
		}
	}
	
	public AutoPilotResult advancePhases(long projectId, String operator) {
		AutoPilotResult result = null;
		try {
			getLogger().log(Level.DEBUG, "before super.advancePhases");
			result = super.advancePhases(projectId, operator);
			getLogger().log(Level.DEBUG, "after super.advancePhases");
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

	public String getManagersEmailTemplateName() {
		return managersEmailTemplateName;
	}

	public String getManagersEmailTemplateSource() {
		return managersEmailTemplateSource;
	}

	public String getManagersEmailFromAddress() {
		return managersEmailFromAddress;
	}

	public boolean isCheckAssignmentDocumentsStatus() {
		return checkAssignmentDocumentsStatus;
	}

}
