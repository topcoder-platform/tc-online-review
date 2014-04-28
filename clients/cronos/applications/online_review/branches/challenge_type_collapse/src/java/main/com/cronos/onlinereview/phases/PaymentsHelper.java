/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.topcoder.db.connectionfactory.DBConnectionException;
import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.payment.ProjectPayment;
import com.topcoder.management.payment.ProjectPaymentManagementException;
import com.topcoder.management.payment.ProjectPaymentManager;
import com.topcoder.management.payment.ProjectPaymentType;
import com.topcoder.management.payment.calculator.ProjectPaymentCalculator;
import com.topcoder.management.payment.calculator.ProjectPaymentCalculatorException;
import com.topcoder.management.payment.search.ProjectPaymentFilterBuilder;
import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.management.project.PersistenceException;
import com.topcoder.management.project.Project;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.resource.ResourceManager;
import com.topcoder.management.resource.persistence.ResourcePersistenceException;
import com.topcoder.management.resource.search.ResourceFilterBuilder;
import com.topcoder.management.review.data.Review;
import com.topcoder.search.builder.SearchBuilderConfigurationException;
import com.topcoder.search.builder.SearchBuilderException;
import com.topcoder.search.builder.filter.AndFilter;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.config.Property;

/**
 * <p>
 * This class is the helper class used to process the payments for the resources in automatic payments mode.
 * </p>
 *
 * <p>
 * Thread-safety: This class is thread-safe as it does not contain any mutable inner state.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public final class PaymentsHelper {
    /**
     * The submitter resource roles.
     */
    private static final Set<String> SUBMITTER_ROLES = new HashSet<String>(Arrays.asList(Constants.ROLE_SUBMITTER));
    /**
     * The reviewer resource roles.
     */
    private static final Set<String> REVIEWER_ROLES = new HashSet<String>(Arrays.asList(
            Constants.ROLE_SPECIFICATION_REVIEWER,
            Constants.ROLE_CHECKPOINT_SCREENER,
            Constants.ROLE_CHECKPOINT_REVIEWER,
            Constants.ROLE_PRIMARY_SCREENER,
            Constants.ROLE_REVIEWER,
            Constants.ROLE_ACCURACY_REVIEWER,
            Constants.ROLE_FAILURE_REVIEWER,
            Constants.ROLE_STRESS_REVIEWER,
            Constants.ROLE_AGGREGATOR,
            Constants.ROLE_FINAL_REVIEWER,
            Constants.ROLE_POST_MORTEM_REVIEWER,
            Constants.ROLE_ITERATIVE_REVIEWER));
    /**
     * The copilot resource roles.
     */
    private static final Set<String> COPILOT_ROLES = new HashSet<String>(Arrays.asList(Constants.ROLE_COPILOT));
    /**
     * The resource roles which belongs to submission domain.
     */
    private static final Set<String> SUBMISSION_DOMAIN = new HashSet<String>(Arrays.asList(Constants.ROLE_SUBMITTER));
    /**
     * The resource roles which belongs to resource domain.
     */
    private static final Set<String> RESOURCE_DOMAIN = new HashSet<String>(Arrays.asList(
            Constants.ROLE_CHECKPOINT_SCREENER,
            Constants.ROLE_CHECKPOINT_REVIEWER,
            Constants.ROLE_PRIMARY_SCREENER,
            Constants.ROLE_REVIEWER,
            Constants.ROLE_ACCURACY_REVIEWER,
            Constants.ROLE_FAILURE_REVIEWER,
            Constants.ROLE_STRESS_REVIEWER,
            Constants.ROLE_AGGREGATOR,
            Constants.ROLE_POST_MORTEM_REVIEWER,
            Constants.ROLE_COPILOT,
            Constants.ROLE_ITERATIVE_REVIEWER
    ));
    /**
     * The resource roles which belongs to resource role domain.
     */
    private static final Set<String> RESOURCE_ROLE_DOMAIN = new HashSet<String>(Arrays.asList(
            Constants.ROLE_SPECIFICATION_REVIEWER,
            Constants.ROLE_FINAL_REVIEWER
    ));
    /**
     * This constant for the "Contest Payment" project payment type id.
     */
    private static final Long CONTEST_PAYMENT_TYPE_ID = 1L;
    /**
     * This constant for the "Contest Checkpoint Payment" project payment type id.
     */
    private static final Long CONTEST_CHECKPOINT_PAYMENT_TYPE_ID = 2L;
    /**
     * This constant for the "Review Payment" project payment type id.
     */
    private static final Long REVIEW_PAYMENT_TYPE_ID = 3L;
    /**
     * This constant for the "Copilot Payment" project payment type id.
     */
    private static final Long COPILOT_PAYMENT_TYPE_ID = 4L;
    /**
     * Property name constant for ProjectPaymentManager class name.
     */
    private static final String PROP_PROJECT_PAYMENT_MGR_CLASS_NAME = "ProjectPaymentManager.ClassName";
    /**
     * Property name constant for namespace to be passed to ProjectPaymentManager implementation constructor.
     */
    private static final String PROP_PROJECT_PAYMENT_MGR_NAMESPACE = "ProjectPaymentManager.Namespace";
    /**
     * Property name constant for configuration file location to be passed to ProjectPaymentManager
     * implementation constructor.
     */
    private static final String PROP_PROJECT_PAYMENT_MGR_CONFIG_FILE = "ProjectPaymentManager.ConfigFile";
    /**
     * Property name constant for connection factory namespace.
     */
    private static final String PROP_CONNECTION_FACTORY_NS = "ConnectionFactoryNS";
    /**
     * Property name constant for connection name.
     */
    private static final String PROP_CONNECTION_NAME = "ConnectionName";
    /**
     * The default configuration namespace of this class. It is used in the default constructor.
     */
    private static final String DEFAULT_NAMESPACE = "com.cronos.onlinereview.phases.PRHelper";
    /**
     * A <code>ProjectPaymentCalculator</code> to be used for calculating the payments for reviewer roles for desired
     * projects.
     */
    private static final ProjectPaymentCalculator projectPaymentCalculator = createProjectPaymentCalculator();
    /**
     * A <code>ProjectPaymentManager</code> to be used for managing project payment.
     */
    private static final ProjectPaymentManager projectPaymentManager;
    /**
     * A <code>ManagerHelper</code> to be used for creating the manager instances.
     */
    private static final ManagerHelper managerHelper;
    /**
     * The factory instance used to create connection to the database. It is initialized in the constructor using
     * DBConnectionFactory component and never changed after that. It will be used in various persistence methods of
     * this project. This field is never null.
     */
    private static final DBConnectionFactory factory;

    /**
     * Represents the connection name used to create connection to the database using DBConnectionFactory. This
     * variable can be null. When it is null, default connection is be created. This variable can be initialized in
     * the constructor and never change after that.
     */
    private static final String connectionName;

    /**
     * Static section.
     */
    static {
        try {
            projectPaymentManager = createProjectPaymentManager();
            managerHelper = new ManagerHelper();

            factory = PhasesHelper.createDBConnectionFactory(DEFAULT_NAMESPACE, PROP_CONNECTION_FACTORY_NS);
            String connName = PhasesHelper.getPropertyValue(DEFAULT_NAMESPACE, PROP_CONNECTION_NAME, false);
            if (!PhasesHelper.isStringNullOrEmpty(connName)) {
                connectionName = connName;
            } else {
                connectionName = null;
            }
        } catch (ConfigurationException e) {
            throw new RuntimeException("Error occurs when creating ProjectPaymentManager", e);
        }
    }

    /**
     * Empty private constructor.
     */
    private PaymentsHelper() {
    }

    /**
     * This method will scan all project resources and update all payments for resources in the
     * automatic payment mode. This method will be used by the Online Review action classes and the
     * phase handlers by autopilot.
     *
     * @param projectId the project id.
     * @param operator the operator
     * @throws PhaseHandlingException if any error occurs.
     */
    public static void processAutomaticPayments(long projectId, String operator)
            throws PhaseHandlingException {
        try {
            Project project = managerHelper.getProjectManager().getProject(projectId);
            // get all resources
            Filter projectIdFilter = ResourceFilterBuilder.createProjectIdFilter(projectId);
            Resource[] resources = managerHelper.getResourceManager().searchResources(projectIdFilter);
            Map<Long, Resource> resourceLookup = new HashMap<Long, Resource>();
            for (Resource resource : resources) {
                resourceLookup.put(resource.getId(), resource);
            }

            // get all non-deleted submitters' submissions
            Submission[] allSubmissions = PRHelper.getNonDeletedProjectSubmitterSubmissions(
                    managerHelper.getUploadManager(), projectId);
            // checks whether there is an Active contest submission which was granted a prize
            boolean hasPrizedContestSubmission = false;
            for (Submission submission : allSubmissions) {
                boolean isActive = Constants.SUBMISSION_STATUS_ACTIVE.equalsIgnoreCase(
                        submission.getSubmissionStatus().getName());
                if (submission.getPrize() != null && isActive &&
                        Constants.SUBMISSION_TYPE_CONTEST_SUBMISSION.equalsIgnoreCase(
                                submission.getSubmissionType().getName())) {
                    hasPrizedContestSubmission = true;
                    break;
                }
            }

            // get all reviews
            Review[] allReviews = PRHelper.searchReviewsForProject(managerHelper, projectId, false);

            // get all existing project payments
            List<ProjectPayment> existingPayments = projectPaymentManager.search(
                    ProjectPaymentFilterBuilder.createProjectIdFilter(projectId));

            // key->resource role id, value->the project payments of that resource role
            Map<Long, List<ProjectPayment>> needToCalculatePayments = new HashMap<Long, List<ProjectPayment>>();
            // This list contains all the new project payments
            List<ProjectPayment> allPayments = new ArrayList<ProjectPayment>();

            for (Resource resource : resources) {
                if (isManualPayment(resource)) {
                    // we only consider resources in auto payment mode
                    continue;
                }
                String resourceRoleName = resource.getResourceRole().getName();
                Long resourceRoleId = resource.getResourceRole().getId();
                if (SUBMITTER_ROLES.contains(resourceRoleName)) {
                    List<ProjectPayment> submissionPayments =
                            getSubmitterPayments(resource, allSubmissions,project);
                    if (submissionPayments != null) {
                        allPayments.addAll(submissionPayments);
                    }
                } else if (REVIEWER_ROLES.contains(resourceRoleName) || COPILOT_ROLES.contains(resourceRoleName)) {
                    ProjectPayment payment;
                    if (REVIEWER_ROLES.contains(resourceRoleName)) {
                        payment = getReviewerPayment(resource, allReviews, hasPrizedContestSubmission, project);
                    } else {
                        payment = getCopilotPayment(resource, hasPrizedContestSubmission, project);
                    }
                    if (payment != null) {
                        if (!needToCalculatePayments.containsKey(resourceRoleId)) {
                            needToCalculatePayments.put(resourceRoleId, new ArrayList<ProjectPayment>());
                        }
                        needToCalculatePayments.get(resourceRoleId).add(payment);
                        allPayments.add(payment);
                    }
                }
            }

            // calculate the reviewer payments
            calculatePayments(projectId, needToCalculatePayments);

            // group the new project payments by domain
            Map<Domain, List<ProjectPayment>> newPaymentsByDomain = getPaymentsByDomain(allPayments, resourceLookup);
            // group the existing project payments by domain
            Map<Domain, List<ProjectPayment>> existingPaymentsByDomain =
                    getPaymentsByDomain(existingPayments, resourceLookup);
            // Now we update the project payments separately for each domain
            for (Map.Entry<Domain, List<ProjectPayment>> entry : newPaymentsByDomain.entrySet()) {
                Domain domain = entry.getKey();
                domain.processPayments(entry.getValue(), existingPaymentsByDomain.get(domain),
                        projectPaymentManager, operator);
            }

            // If there are no new payment in a domain, delete all existing non-paid payments in the domain
            Set<Domain> noPaymentsDomains = existingPaymentsByDomain.keySet();
            noPaymentsDomains.removeAll(newPaymentsByDomain.keySet());
            // Now noPaymentsDomains only contains the domains which has no new payments
            for (Domain domain : noPaymentsDomains) {
                for (ProjectPayment payment : existingPaymentsByDomain.get(domain)) {
                    Resource resource = resourceLookup.get(payment.getResourceId());
                    if (payment.getPactsPaymentId() == null &&
                            !isManualPayment(resource)) {
                        projectPaymentManager.delete(payment.getProjectPaymentId());
                    }
                }
            }
        } catch (PersistenceException e) {
            throw new PhaseHandlingException("Error occurs when retrieving project", e);
        } catch (SearchBuilderConfigurationException e) {
            throw new PhaseHandlingException("Problem with search builder configuration", e);
        } catch (ResourcePersistenceException e) {
            throw new PhaseHandlingException("There was a resource retrieval error", e);
        } catch (SearchBuilderException e) {
            throw new PhaseHandlingException("Problem with search builder", e);
        } catch (ProjectPaymentCalculatorException e) {
            throw new PhaseHandlingException("Problem with project payment calculator", e);
        } catch (ProjectPaymentManagementException e) {
            throw new PhaseHandlingException("Problem with project payment manager", e);
        }
    }

    /**
     * Update the project_result.payment field for all submitters for a given project.
     *
     * @param projectId the id of the given project.
     * @throws PhaseHandlingException if any error occurs.
     */
    public static void updateProjectResultPayments(long projectId) throws PhaseHandlingException {
        // get all resources
        Connection conn = createConnection();
        PreparedStatement ps = null;
        try {
            ResourceManager resourceManager = managerHelper.getResourceManager();
            Filter projectIdFilter = ResourceFilterBuilder.createProjectIdFilter(projectId);
            Filter resourceRoleFilter = ResourceFilterBuilder.createResourceRoleIdFilter(
                    LookupHelper.getResourceRole(resourceManager, Constants.ROLE_SUBMITTER).getId());
            Resource[] submitterResources = resourceManager.searchResources(
                    new AndFilter(projectIdFilter, resourceRoleFilter));
            List<ProjectPayment> payments = projectPaymentManager.search(
                    ProjectPaymentFilterBuilder.createProjectIdFilter(projectId));

            ps = conn.prepareStatement("UPDATE project_result SET payment = ? WHERE project_id = ? AND user_id = ?");
            ps.setLong(2, projectId);
            for (Resource resource : submitterResources) {
                double totalPayment = 0;
                for (ProjectPayment payment : payments) {
                    if (payment.getResourceId().equals(resource.getId())) {
                        totalPayment += payment.getAmount().doubleValue();
                    }
                }
                if (totalPayment == 0) {
                    ps.setNull(1, Types.DOUBLE);
                } else {
                    ps.setDouble(1, totalPayment);
                }
                ps.setLong(3, Long.parseLong((String) resource.getProperty("External Reference ID")));
                ps.executeUpdate();
            }
        } catch (SearchBuilderConfigurationException e) {
            throw new PhaseHandlingException("Problem with search builder configuration", e);
        } catch (ResourcePersistenceException e) {
            throw new PhaseHandlingException("There was a resource retrieval error", e);
        } catch (SearchBuilderException e) {
            throw new PhaseHandlingException("Problem with search builder", e);
        } catch (ProjectPaymentManagementException e) {
            throw new PhaseHandlingException("Problem with project payment manager", e);
        } catch (SQLException e) {
            throw new PhaseHandlingException("Error occurs when updating project result", e);
        } finally {
            PRHelper.close(ps);
            PRHelper.close(conn);
        }
    }

    /**
     * This method is used to create the connection to access database. The connection needs to be
     * closed after use.
     *
     * @return The database connection.
     * @throws PhaseHandlingException if connection could not be created.
     */
    private static Connection createConnection() throws PhaseHandlingException {
        try {
            if (connectionName == null) {
                return factory.createConnection();
            } else {
                return factory.createConnection(connectionName);
            }
        } catch (DBConnectionException ex) {
            throw new PhaseHandlingException("Could not create connection", ex);
        }
    }

    /**
     * Calculate the payments for the resource roles for a given project.
     *
     * @param projectId the id of the given project.
     * @param paymentsByResourceRole the map from resource role to the <code>ProjectPayment</code>s instances belongs
     *                               to this role. Basically we call calculate payment for each resource role using
     *                               <code>projectPaymentCalculator</code> to assign the payment to each
     *                               <code>ProjectPayment</code> instance of this resource role.
     * @throws ProjectPaymentCalculatorException if any error occurs when calculating the project payment.
     */
    private static void calculatePayments(long projectId, Map<Long, List<ProjectPayment>> paymentsByResourceRole)
            throws ProjectPaymentCalculatorException {
        if (paymentsByResourceRole.keySet().size() == 0) {
            return;
        }
        Map<Long, BigDecimal> resourceRolePayments = projectPaymentCalculator.getDefaultPayments(
                projectId, new ArrayList<Long>(paymentsByResourceRole.keySet()));
        for (Map.Entry<Long, List<ProjectPayment>> entry : paymentsByResourceRole.entrySet()) {
            BigDecimal amount = resourceRolePayments.get(entry.getKey());
            if (amount == null) {
                amount = BigDecimal.ZERO;
            }
            for (ProjectPayment payment : entry.getValue()) {
                payment.setAmount(amount);
            }
        }
    }

    /**
     * This method will compute submitter payments.
     *
     * @param resource the submitter resource.
     * @param allSubmissions all non-deleted submissions of the project.
     * @param project the project which the submitter resource belongs to.
     * @return all the submission payments of the submitter.
     */
    private static List<ProjectPayment> getSubmitterPayments(Resource resource, Submission[] allSubmissions,
                                                                  Project project) {
        if (!isMemberPaymentEligible(project)) {
            return null;
        }
        long resourceId = resource.getId();
        List<ProjectPayment> payments = new ArrayList<ProjectPayment>();
        for (Submission submission : allSubmissions) {
            if (submission.getUpload().getOwner() == resourceId && submission.getPrize() != null) {
                ProjectPayment projectPayment = new ProjectPayment();
                projectPayment.setResourceId(resourceId);
                projectPayment.setSubmissionId(submission.getId());
                projectPayment.setAmount(BigDecimal.valueOf(submission.getPrize().getPrizeAmount()));
                ProjectPaymentType type = new ProjectPaymentType();
                type.setProjectPaymentTypeId(submission.getSubmissionType().getName().equalsIgnoreCase(
                        Constants.SUBMISSION_TYPE_CONTEST_SUBMISSION)
                        ? CONTEST_PAYMENT_TYPE_ID : CONTEST_CHECKPOINT_PAYMENT_TYPE_ID);
                projectPayment.setProjectPaymentType(type);
                payments.add(projectPayment);
            }
        }
        return payments;
    }

    /**
     * This method will compute reviewer payment.
     *
     * @param resource the reviewer resource.
     * @param allReviews all reviews of the project.
     * @param hasPrizedContestSubmission true if there is at least one submission of &quote;Contest Submission&quote;
     *                                   type in &quote;Action&quote; status that was granted a prize, false otherwise.
     * @param project the project which the reviewer resource belongs to.
     * @return the project payment of the reviewer resource.
     */
    private static ProjectPayment getReviewerPayment(Resource resource, Review[] allReviews,
                                                     boolean hasPrizedContestSubmission, Project project) {
        if (!isMemberPaymentEligible(project)) {
            return null;
        }
        boolean hasCommittedReview = false;
        for (Review review : allReviews) {
            if (review.getAuthor() == resource.getId() && review.isCommitted()) {
                hasCommittedReview = true;
                break;
            }
        }
        if (!hasCommittedReview) {
            // This resource has no committed review
            return null;
        }

        // For a non-studio project, if the resource is a specific reviewer, there must be an Active contest submission
        // which was granted a prize
        if (!PhasesHelper.isStudio(project)
                && Constants.ROLE_SPECIFICATION_REVIEWER.equalsIgnoreCase(resource.getResourceRole().getName())
                && !hasPrizedContestSubmission) {
            return null;
        }
        ProjectPayment projectPayment = new ProjectPayment();
        projectPayment.setResourceId(resource.getId());
        ProjectPaymentType type = new ProjectPaymentType();
        type.setProjectPaymentTypeId(REVIEW_PAYMENT_TYPE_ID);
        projectPayment.setProjectPaymentType(type);
        // amount will be set later by calculatePayments method
        return projectPayment;
    }

    /**
     * This method compute copilot payment.
     *
     * @param resource the copilot resource.
     * @param hasPrizedContestSubmission true if there is at least one submission of &quote;Contest Submission&quote;
     *                                   type in &quote;Action&quote; status that was granted a prize, false otherwise.
     * @param project the project which the copilot resource belongs to.
     * @return the project payment of the copilot resource.
     */
    private static ProjectPayment getCopilotPayment(Resource resource, boolean hasPrizedContestSubmission,
                                                    Project project) {
        if (!isMemberPaymentEligible(project)) {
            return null;
        }
        if (!hasPrizedContestSubmission) {
            // There is no Active contest submission which was granted a prize
            return null;
        }
        ProjectPayment projectPayment = new ProjectPayment();
        projectPayment.setResourceId(resource.getId());
        ProjectPaymentType type = new ProjectPaymentType();
        type.setProjectPaymentTypeId(COPILOT_PAYMENT_TYPE_ID);
        projectPayment.setProjectPaymentType(type);
        // amount will be set later by calculatePayments method
        return projectPayment;
    }

    /**
     * Checks whether the project is member payment eligible.
     *
     * @param project the given project.
     * @return true if the project is member payment eligible, false otherwise.
     */
    private static boolean isMemberPaymentEligible(Project project) {
        return "true".equalsIgnoreCase((String) project.getProperty("Member Payments Eligible"));
    }

    /**
     * Checks whether the resource is in manual payment mode.
     *
     * @param resource the given resource.
     * @return true if the resource is in manual payment mode, false otherwise.
     */
    private static boolean isManualPayment(Resource resource) {
        return "true".equalsIgnoreCase((String) resource.getProperty("Manual Payments"));
    }

    /**
     * Creates new instance of calculator for the project payments based on the available application configuration.
     *
     * @return a <code>ProjectPaymentCalculator</code> to be used for calculating the payments for reviewer roles for
     *         desired projects.
     */
    private static ProjectPaymentCalculator createProjectPaymentCalculator() {
        String className = null;
        try {
            ConfigManager cfgMgr = ConfigManager.getInstance();
            Property config = cfgMgr.getPropertyObject("com.cronos.OnlineReview", "ProjectPaymentConfig");
            className = config.getValue("CalculatorClass");
            Class clazz = Class.forName(className);
            return (ProjectPaymentCalculator) clazz.newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to instantiate the project payment calculator of type: "
                    + className, e);
        }
    }

    /**
     * Creates new instance of project payment manager based on the available application configuration.
     *
     * @return a <code>ProjectPaymentManager</code> to be used for managing project payments.
     * @throws ConfigurationException if any error occurs when creating the instance.
     */
    private static ProjectPaymentManager createProjectPaymentManager() throws ConfigurationException {
        String namespace = "com.cronos.OnlineReview";
        String mgrClassName = PhasesHelper.getPropertyValue(namespace, PROP_PROJECT_PAYMENT_MGR_CLASS_NAME, true);
        String mgrNamespace = PhasesHelper.getPropertyValue(namespace, PROP_PROJECT_PAYMENT_MGR_NAMESPACE, true);
        String mgrConfigFile = PhasesHelper.getPropertyValue(namespace, PROP_PROJECT_PAYMENT_MGR_CONFIG_FILE, true);

        Object[] params = new Object[]{mgrConfigFile, mgrNamespace};
        Class<?>[] paramsTypes = new Class<?>[]{String.class, String.class};

        // instantiate using reflection.
        try {
            Class<? extends ProjectPaymentManager> clazz = Class.forName(mgrClassName).asSubclass(ProjectPaymentManager.class);
            return clazz.getConstructor(paramsTypes).newInstance(params);
        } catch (ClassNotFoundException e) {
            throw new ConfigurationException("Could not find class:" + mgrClassName, e);
        } catch (IllegalArgumentException e) {
            throw new ConfigurationException("The object could not be instantiated.", e);
        } catch (SecurityException e) {
            throw new ConfigurationException("The object could not be instantiated.", e);
        } catch (InstantiationException e) {
            throw new ConfigurationException("The object could not be instantiated.", e);
        } catch (IllegalAccessException e) {
            throw new ConfigurationException("The object could not be instantiated.", e);
        } catch (InvocationTargetException e) {
            throw new ConfigurationException("The object could not be instantiated.", e);
        } catch (NoSuchMethodException e) {
            throw new ConfigurationException("The object could not be instantiated.", e);
        } catch (ClassCastException e) {
            throw new ConfigurationException(mgrClassName + " must be of type " +
                    ProjectPaymentManager.class.getName());
        }
    }

    /**
     * Group the project payments by the domain.
     *
     * @param payments the project payments.
     * @param resourceLookup the map from resource id to <code>Resource</code> instance.
     * @return the project payments grouped by the domain, the key is the domain, and the value is the
     *          project payments belongs to the domain.
     */
    private static Map<Domain, List<ProjectPayment>> getPaymentsByDomain(List<ProjectPayment> payments,
                                                                         Map<Long, Resource> resourceLookup) {
        Map<Domain, List<ProjectPayment>> paymentsByDomain = new HashMap<Domain, List<ProjectPayment>>();

        for (ProjectPayment payment : payments) {
            Resource resource = resourceLookup.get(payment.getResourceId());
            String resourceRoleName = resource.getResourceRole().getName();
            Domain domain = null;
            if (SUBMISSION_DOMAIN.contains(resourceRoleName)) {
                domain = new SubmissionDomain(payment.getSubmissionId());
            } else if (RESOURCE_DOMAIN.contains(resourceRoleName)) {
                domain = new ResourceDomain(payment.getResourceId());
            } else if (RESOURCE_ROLE_DOMAIN.contains(resourceRoleName)) {
                domain = new ResourceRoleDomain(resource.getResourceRole().getName(), resourceLookup);
            }
            if (domain == null) {
                continue;
            }
            if (!paymentsByDomain.containsKey(domain)) {
                paymentsByDomain.put(domain, new ArrayList<ProjectPayment>());
            }
            paymentsByDomain.get(domain).add(payment);
        }
        return paymentsByDomain;
    }

    /**
     * This class represents the domain of the project payments. Each resource can have at most one automatic payment
     * per domain. We will process automatic payments separately for each domain. Within each domain there can be
     * only one payment. Currently there are 3 domains: Submission domain, Resource domain and Resource Role domain.
     *
     * @author TCSASSEMBLER
     * @version 2.0
     */
    static abstract class Domain {
        /**
         * Represents the identifier of the domain. Two domains with the same identifier will be considered
         * to be equal.
         */
        private final String key;

        /**
         * Create a <code>Domain</code> instance with the identifier.
         *
         * @param key the identifier.
         */
        protected Domain(String key) {
            this.key = key;
        }

        /**
         * Returns a hash code value for the object. It will just return the hash code of {@link #key}.
         *
         * @return the hash code.
         */
        public int hashCode() {
            return key.hashCode();
        }

        /**
         * Indicates whether some other object is equal to this one. Two domains are equal if and only if their keys
         * are equal.
         *
         * @param o the reference object with which to compare.
         * @return true if other object is {@link Domain} and the key of which is equal to this one, false otherwise.
         */
        public boolean equals(Object o) {
            return (o instanceof Domain) && key.equals(((Domain) o).key);
        }

        /**
         * Process the project payments for this domain. It will first check whether there is an existing paid payment,
         * if yes all non-paid existing paid payment will be deleted. Otherwise it will update the old existing
         * payments to the current new project payment. Since each domain can only contain one automatic payment,
         * only the first element of current new project payments will be used. The payments updating rules are:
         * <ol>
         *     <li>If there are multiple existing payments in the domain, delete all the existing payment. Then
         *     add the new payment to the domain.</li>
         *     <li>If these are no existing payment in the domain, just add the new payment to the domain.</li>
         *     <li>If there is only one existing payment in the domain, update the existing payment if the amount
         *     or the payment type is different.</li>
         *     <li>All the payments with zero amount will be deleted/ignored.</li>
         * </ol>
         *
         * @param current the current new project payments in the domain, it will only contain automatic payments. Only
         *                the first element will be used since each domain can only contain one automatic payment.
         * @param existing the existing project payments in the domain, can be null.
         * @param projectPaymentManager the instance of project payment manager.
         * @param operator the operator
         * @throws ProjectPaymentManagementException if any error occurs.
         */
        void processPayments(List<ProjectPayment> current, List<ProjectPayment> existing,
                             ProjectPaymentManager projectPaymentManager, String operator)
                throws ProjectPaymentManagementException {
            if (existing != null) {
                // checks whether there is an existing paid payment
                boolean paid = false;
                for (ProjectPayment payment : existing) {
                    if (payment.getPactsPaymentId() != null) {
                        paid = true;
                        break;
                    }
                }
                if (paid) {
                    // there is an existing paid payment in the domain, so delete all existing non-paid payments
                    for (ProjectPayment payment : existing) {
                        if (payment.getPactsPaymentId() == null) {
                            projectPaymentManager.delete(payment.getProjectPaymentId());
                        }
                    }
                    return;
                }
            }

            // the new payment in the domain
            ProjectPayment now = current.get(0);

            // Update the existing payments to the new payment in the domain
            if (existing != null && existing.size() > 1) {
                // there are multiple existing payments in the domain, so delete all the existing payments first
                for (ProjectPayment payment : existing) {
                    projectPaymentManager.delete(payment.getProjectPaymentId());
                }
                existing.clear();
            }
            if (existing == null || existing.size() == 0) {
                if (now.getAmount().compareTo(BigDecimal.ZERO) != 0) {
                    // add the new payment to the domain
                    projectPaymentManager.create(now, operator);
                }
            } else {
                // There is only one existing payment in the domain
                ProjectPayment toUpdate = existing.get(0);
                if (now.getAmount().compareTo(BigDecimal.ZERO) == 0) {
                    // delete the payment since the payment amount is zero
                    projectPaymentManager.delete(toUpdate.getProjectPaymentId());
                } else if (!toUpdate.getAmount().equals(now.getAmount()) ||
                        !toUpdate.getProjectPaymentType().getProjectPaymentTypeId().equals(
                                now.getProjectPaymentType().getProjectPaymentTypeId())) {
                    // amount or payment type is different, so update the existing payment
                    toUpdate.setAmount(now.getAmount());
                    toUpdate.setProjectPaymentType(now.getProjectPaymentType());
                    projectPaymentManager.update(toUpdate, operator);
                }
            }
        }
    }

    /**
     * This class represents the &quote;Submission Domain&quote; of the project payments. It's used for Submitter
     * resource. Every Submitter resource can have at most one payment per submission. So the domain is the submission.
     *
     * @author TCSASSEMBLER
     * @version 2.0
     */
    static class SubmissionDomain extends Domain {
        /**
         * Create a new <code>SubmissionDomain</code> with the submission id.
         *
         * @param submissionId the submission id.
         */
        SubmissionDomain(long submissionId) {
            super("submission_" + submissionId);
        }
    }

    /**
     * This class represents the &quote;Resource Domain&quote; of the project payments. It's used for the resources
     * who can have at most payment per resource. So the domain is the resource.
     *
     * @author TCSASSEMBLER
     * @version 2.0
     */
    static class ResourceDomain extends Domain {
        /**
         * Create a new <code>ResourceDomain</code> with the resource id.
         *
         * @param resourceId the resource id.
         */
        ResourceDomain(long resourceId) {
            super("resource_" + resourceId);
        }
    }

    /**
     * This class represents the &quote;Resource Role Domain&quote; of the project payments. It's used for the resources
     * who can have at most one payment per resource role. So the domain is all the project resources of that resource
     * role.
     *
     * @author TCSASSEMBLER
     * @version 2.0
     */
    static class ResourceRoleDomain extends Domain {
        /**
         * Represents the map from resource id to <code>Resource</code> instance.
         */
        private final Map<Long, Resource> resourceLookup;

        /**
         * Create a new <code>ResourceRoleDomain</code> with the resource role name and
         * the map from resource id to <code>Resource</code> instance.
         *
         * @param resourceRoleName the resource role name.
         * @param resourceLookup the map from resource id to <code>Resource</code> instance.
         */
        ResourceRoleDomain(String resourceRoleName, Map<Long, Resource> resourceLookup) {
            super("resourceRole_" + resourceRoleName);
            this.resourceLookup = resourceLookup;
        }

        /**
         * Process the project payments for this domain. Since all project resources of the resource role can have at
         * most one payment. This method will first check whether there is an existing payment owned by a resource
         * in manual payment mode, if yes, it will delete all non-paid payments owned by a resource in automatic payment
         * mode. Otherwise it will call {@link Domain#processPayments(List, List, ProjectPaymentManager, String)} to
         * assign the new payment to the resource with the least resource id.
         *
         * @param current the current project payments in the domain, it will only contain automatic payments.
         * @param existing the existing project payments in the domain, can be null.
         * @param projectPaymentManager the instance of project payment manager.
         * @param operator the operator
         * @throws ProjectPaymentManagementException if any error occurs.
         * @see Domain#processPayments(List, List, ProjectPaymentManager, String)
         */
        void processPayments(List<ProjectPayment> current, List<ProjectPayment> existing,
                             ProjectPaymentManager projectPaymentManager, String operator)
                throws ProjectPaymentManagementException {
            if (existing != null) {
                // checks whether there is an existing payment owned by a resource in manual payment mode
                boolean hasManualPayment = false;
                for (ProjectPayment payment : existing) {
                    Resource resource = resourceLookup.get(payment.getResourceId());
                    if (isManualPayment(resource)) {
                        hasManualPayment = true;
                        break;
                    }
                }

                if (hasManualPayment) {
                    // there is an existing payment owned by a resource in manual payment mode,
                    // so delete all non-paid payments owned by a resource in automatic payment mode
                    for (ProjectPayment payment : existing) {
                        Resource resource = resourceLookup.get(payment.getResourceId());
                        if (!isManualPayment(resource) && payment.getPactsPaymentId() == null) {
                            projectPaymentManager.delete(payment.getProjectPaymentId());
                        }
                    }
                    return;
                }
            }

            // at here "existing" only contains automatic payments

            // When there are multiple resources need to assign prize, we assign the prize to the resource
            // with least resource id. So first we sort the new payments by resource id, then the payment with
            // least resource id will be used when calling super.processPayments
            Collections.sort(current, new Comparator<ProjectPayment>() {
                public int compare(ProjectPayment projectPayment1, ProjectPayment projectPayment2) {
                    long diff = projectPayment1.getResourceId() - projectPayment2.getResourceId();
                    return diff < 0 ? -1 : (diff == 0 ? 0 : 1);
                }
            });

            super.processPayments(current, existing, projectPaymentManager, operator);
        }
    }
}
