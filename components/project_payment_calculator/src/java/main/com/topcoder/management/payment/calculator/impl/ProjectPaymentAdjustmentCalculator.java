/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.payment.calculator.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topcoder.commons.utils.LoggingWrapperUtility;
import com.topcoder.commons.utils.ParameterCheckUtility;
import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.management.payment.calculator.ProjectPaymentCalculator;
import com.topcoder.management.payment.calculator.ProjectPaymentCalculatorConfigurationException;
import com.topcoder.management.payment.calculator.ProjectPaymentCalculatorException;
import com.topcoder.util.log.Log;
import com.topcoder.util.objectfactory.InvalidClassSpecificationException;
import com.topcoder.util.objectfactory.ObjectFactory;
import com.topcoder.util.objectfactory.SpecificationFactory;
import com.topcoder.util.objectfactory.impl.ConfigurationObjectSpecificationFactory;
import com.topcoder.util.objectfactory.impl.IllegalReferenceException;
import com.topcoder.util.objectfactory.impl.SpecificationConfigurationException;


/**
 * <p>
 * This class acts as a project payment adjuster which wraps/decorates the <code>ProjectPaymentCalculator</code>
 * implementation instance.
 * </p>
 * <p>
 * It extends <code>BaseProjectPaymentCalculator</code> to gain access to the logger to be used to perform logging,
 * and to create the connection to be used to connect to the database using {@link #createConnection()}. It is a
 * decorating implementation of the <code>ProjectPaymentCalculator</code> interface. It holds a reference to the
 * <code>ProjectPaymentCalculator</code> implementation instance to which it delegates the calls. After delegating
 * the call to the underlying implementation it will optionally scale the resulting payment values by a multiplier
 * or will replace them with a fixed amount. The scaling multiplier and fixed amount values depend on the project
 * id and resource role and will be read from the database.
 * </p>
 * <p>
 * This class is configured using Configuration API <code>ConfigurationObject</code> either from configuration file
 * or directly via <code>ConfigurationObject</code> instance. In addition of the configuration parameters described
 * in {@link BaseProjectPaymentCalculator}, the <code>ProjectPaymentAdjustmentCalculator</code> needs the
 * configuration parameters defined in the following table (angle brackets are used for identifying child
 * configuration objects):
 * </p>
 * <table border="1" cellspacing="2" cellpadding="3">
 * <tr>
 * <th>Parameter name</th>
 * <th>Description</th>
 * <th>Value</th>
 * </tr>
 * <tr>
 * <td>&lt;object_factory_config&gt;</td>
 * <td>This section contains configuration of Object Factory used by this class for creating pluggable object
 * instances. It is required to provide this configuration parameter when the caller will use any one of the
 * constructors that do not accept a <code>ProjectPaymentCalculator</code> input parameter.</td>
 * <td><code>ConfigurationObject</code>. Required</td>
 * </tr>
 * <tr>
 * <td>project_payment_calculator_key</td>
 * <td>The Object Factory key that is used for creating an instance of <code>ProjectPaymentCalculator</code>
 * underlying implementation to be used by this class for payments calculation. It is required to provide this
 * configuration parameter when the caller will use any one of the constructors that do not accept a
 * <code>ProjectPaymentCalculator</code> input parameter.</td>
 * <td>String, Not Empty. Required.</td>
 * </tr>
 * </table>
 * <p>
 * <b>Sample configuration:</b><br/>
 * File <code>SampleConfig.xml</code>
 *
 * <pre>
 * &lt;?xml version="1.0"?&gt;
 * &lt;CMConfig&gt;
 *     &lt;!-- Project payment adjustment calculator --&gt;
 *     &lt;Config
 *         name="com.topcoder.management.payment.calculator.impl.ProjectPaymentAdjustmentCalculator"&gt;
 *         &lt;!-- configuration for ObjectFactory --&gt;
 *         &lt;Property name="object_factory_config"&gt;
 *             &lt;Property name="DefaultProjectPaymentCalculator"&gt;
 *                 &lt;Property name="type"&gt;
 *           &lt;Value&gt;com.topcoder.management.payment.calculator.impl.DefaultProjectPaymentCalculator&lt;/Value&gt;
 *                 &lt;/Property&gt;
 *             &lt;/Property&gt;
 *         &lt;/Property&gt;
 *         &lt;Property name="project_payment_calculator_key"&gt;
 *             &lt;Value&gt;DefaultProjectPaymentCalculator&lt;/Value&gt;
 *         &lt;/Property&gt;
 *         &lt;Property name="logger_name"&gt;
 *             &lt;Value&gt;adjustment_calculator_logger&lt;/Value&gt;
 *         &lt;/Property&gt;
 *         &lt;Property name="connection_name"&gt;
 *             &lt;Value&gt;my_connection&lt;/Value&gt;
 *         &lt;/Property&gt;
 *         &lt;!-- configuration for DBConnectionFactoryImpl --&gt;
 *         &lt;Property name="db_connection_factory_config"&gt;
 *             &lt;Property name="com.topcoder.db.connectionfactory.DBConnectionFactoryImpl"&gt;
 *                 &lt;Property name="connections"&gt;
 *                     &lt;Property name="default"&gt;
 *                         &lt;Value&gt;my_connection&lt;/Value&gt;
 *                     &lt;/Property&gt;
 *
 *                     &lt;Property name="my_connection"&gt;
 *                         &lt;Property name="producer"&gt;
 *                        &lt;Value&gt;com.topcoder.db.connectionfactory.producers.JDBCConnectionProducer&lt;/Value&gt;
 *                         &lt;/Property&gt;
 *                         &lt;Property name="parameters"&gt;
 *                             &lt;Property name="jdbc_driver"&gt;
 *                                 &lt;Value&gt;com.informix.jdbc.IfxDriver&lt;/Value&gt;
 *                             &lt;/Property&gt;
 *                             &lt;Property name="jdbc_url"&gt;
 *            &lt;Value&gt;jdbc:informix-sqli://localhost:9088/tcs_catalog:informixserver=ol_informix1170&lt;/Value&gt;
 *                             &lt;/Property&gt;
 *                             &lt;Property name="user"&gt;
 *                                 &lt;Value&gt;informix&lt;/Value&gt;
 *                             &lt;/Property&gt;
 *                             &lt;Property name="password"&gt;
 *                                 &lt;Value&gt;dbadmin&lt;/Value&gt;
 *                             &lt;/Property&gt;
 *                         &lt;/Property&gt;
 *                     &lt;/Property&gt;
 *                 &lt;/Property&gt;
 *             &lt;/Property&gt;
 *         &lt;/Property&gt;
 *     &lt;/Config&gt;
 * &lt;/CMConfig&gt;
 *
 * </pre>
 *
 * The configuration file above is loaded into <code>ConfigurationObject</code> instance via Configuration
 * Persistence component by providing a configuration properties file as following:<br/>
 * File <code>config.properties</code>
 *
 * <pre>
 * com.topcoder.management.payment.calculator.impl.DefaultProjectPaymentCalculator = SampleConfig.xml
 * </pre>
 *
 * It contains the mapping between namespace and the XML configuration file.
 * </p>
 * <p>
 * <b>Sample usage:</b><br/>
 * Let's say the current records in the database are shown by these following tables:
 * <p>
 * Table <code>default_project_payment</code>
 *
 * <pre>
 * ------------------------------------------------------------------------------------------------------
 * | project_category_id | resource_role_id | fixed_amount | base_coefficient | incremental_coefficient |
 * ------------------------------------------------------------------------------------------------------
 * |          1          |         2        |       0      |       0.00       |           0.01          |
 * |          1          |         4        |       0      |       0.12       |           0.05          |
 * |          1          |         8        |      10      |       0.00       |           0.00          |
 * |          2          |         9        |       0      |       0.05       |           0.00          |
 * ------------------------------------------------------------------------------------------------------
 * </pre>
 *
 * </p>
 * <p>
 * Table <code>project</code>
 *
 * <pre>
 * ------------------------------------
 * | project_id | project_category_id |
 * ------------------------------------
 * |    230     |          1          |
 * |    231     |          2          |
 * |    232     |          3          |
 * |    233     |          4          |
 * ------------------------------------
 * </pre>
 *
 * </p>
 * <p>
 * Table <code>prize</code>
 *
 * <pre>
 * -----------------------------------------------------
 * | project_id | prize_type_id | place | prize_amount |
 * -----------------------------------------------------
 * |     230    |      15       |   1   |      500     |
 * |     231    |      15       |   1   |      400     |
 * |     232    |      15       |   1   |      650     |
 * |     233    |      15       |   1   |     1000     |
 * -----------------------------------------------------
 * </pre>
 *
 * </p>
 * <p>
 * Table <code>submission</code>
 *
 * <pre>
 * ---------------------------------------------------------
 * | submission_type_id | upload_id | submission_status_id |
 * ---------------------------------------------------------
 * |          1         |    500    |          1           |
 * |          1         |    623    |          2           |
 * |          1         |     46    |          7           |
 * ---------------------------------------------------------
 * </pre>
 *
 * </p>
 * <p>
 * Table <code>upload</code>
 *
 * <pre>
 * -------------------------------------------
 * | project_id | upload_type_id | upload_id |
 * -------------------------------------------
 * |    230     |       1        |    500    |
 * |    230     |       1        |    623    |
 * |    233     |       1        |     46    |
 * -------------------------------------------
 * </pre>
 *
 * </p>
 * <p>
 * Table <code>project_payment_adjustment</code>
 *
 * <pre>
 * -------------------------------------------------------------
 * | project_id | resource_role_id | fixed_amount | multiplier |
 * -------------------------------------------------------------
 * |    230     |         2        |    14.00     |    NULL    |
 * |    230     |         4        |    22.00     |    NULL    |
 * |    230     |         5        |    NULL      |    3.00    |
 * |    230     |         8        |    NULL      |    2.00    |
 * |    230     |         9        |    NULL      |    NULL    |
 * -------------------------------------------------------------
 * </pre>
 *
 * </p>
 * <p>
 *
 * <pre>
 * // create an instance of DefaultProjectPaymentCalculator using the default configuration file
 * // found in com/topcoder/management/payment/calculator/impl/ProjectPaymentAdjustmentCalculator.properties
 * ProjectPaymentCalculator adjuster = new ProjectPaymentAdjustmentCalculator();
 *
 * // create a new ProjectPaymentAdjustmentCalculator with to wrap the calculator created above using the same
 * // configuration file.
 * String namespace1 = &quot;com.topcoder.management.payment.calculator.impl.ProjectPaymentAdjustmentCalculator&quot;;
 * adjuster = new ProjectPaymentAdjustmentCalculator(calculator, configFile, namespace1);
 *
 * // Get the adjusted default payments for primary screener, reviewer and accuracy reviewer for the
 * // project with project_id = 230.
 * List&lt;Long&gt; resourceRoleIDs = new ArrayList&lt;Long&gt;();
 * resourceRoleIDs.add(DefaultProjectPaymentCalculator.PRIMARY_SCREENER_RESOURCE_ROLE_ID);
 * resourceRoleIDs.add(DefaultProjectPaymentCalculator.REVIEWER_RESOURCE_ROLE_ID);
 * resourceRoleIDs.add(DefaultProjectPaymentCalculator.AGGREGATOR_RESOURCE_ROLE_ID);
 *
 * Map&lt;Long, BigDecimal&gt; adjustedPayments = adjuster.getDefaultPayments(230, resourceRoleIDs);
 *
 * // The adjustedPayments map will contain the following 3 elements
 * // first element : key = 2, value = 14 (adjusted to the value of fixed_amount)
 * // second element : key = 4, value = 22 (adjusted to the value of fixed_amount).
 * // third element : key = 8, value = (10 + (0.00 + 0.00*0)*500 ) * 2 = 20 (multiplier == 2)
 * for (Long roleId : adjustedPayments.keySet()) {
 *     BigDecimal payment = adjustedPayments.get(roleId);
 *     System.out.println(&quot;Adjusted Payment for role &quot; + roleId + &quot; = &quot; + payment);
 * }
 * </pre>
 *
 * </p>
 * <p>
 * <b>Thread Safety:</b><br/>
 * This class is thread safe since its base class is thread safe and all its fields are initialized in the
 * constructor and never changed after that and the aggregated <code>ProjectPaymentCalculator</code> implementation
 * instance is expected to be thread safe.
 * </p>
 *
 * @author Schpotsky, TCSDEVELOPER
 * @version 1.0
 */
public class ProjectPaymentAdjustmentCalculator extends BaseProjectPaymentCalculator implements
    ProjectPaymentCalculator {
    /**
     * <p>
     * The default configuration file path of this class. It refers to the file path
     * <code>com/topcoder/management/payment/calculator/impl/ProjectPaymentAdjustmentCalculator.properties</code>.
     * </p>
     * <p>
     * It is an immutable static constant. It is used in the {@link #ProjectPaymentAdjustmentCalculator()} and
     * {@link #ProjectPaymentAdjustmentCalculator(ProjectPaymentCalculator)} constructors.
     * </p>
     */
    public static final String DEFAULT_CONFIG_FILENAME =
        "com/topcoder/management/payment/calculator/impl/ProjectPaymentAdjustmentCalculator.properties";

    /**
     * <p>
     * The default configuration namespace of this class. It refers to the fully qualified name of this class,
     * <code>com.topcoder.management.payment.calculator.impl.ProjectPaymentAdjustmentCalculator</code>.
     * </p>
     * <p>
     * It is an immutable static constant. It is used in the {@link #ProjectPaymentAdjustmentCalculator()} and
     * {@link #ProjectPaymentAdjustmentCalculator(ProjectPaymentCalculator)} constructors.
     * </p>
     */
    public static final String DEFAULT_CONFIG_NAMESPACE =
        "com.topcoder.management.payment.calculator.impl.ProjectPaymentAdjustmentCalculator";

    /**
     * <p>
     * The multiplier column in project_payment_adjustment table.
     * </p>
     */
    private static final String MULTIPLIER_COLUMN = "multiplier";

    /**
     * <p>
     * The fixed_amount column in project_payment_adjustment table.
     * </p>
     */
    private static final String FIXED_AMOUNT_COLUMN = "fixed_amount";

    /**
     * <p>
     * The resource_role_id column in project_payment_adjustment table.
     * </p>
     */
    private static final String RESOURCE_ROLE_ID_COLUMN = "resource_role_id";

    /**
     * <p>
     * The prefix of the SQL query to retrieve adjustment values from project_payment_adjustment.
     * </p>
     * <p>
     * It will be appended in getDefaultPayments() method with given resourceRoleIds to complete the IN clause.
     * </p>
     */
    private static final String GET_ADJUSTMENT_QUERY_PREFIX = "SELECT resource_role_id, fixed_amount, multiplier "
        + "FROM project_payment_adjustment " + "WHERE project_id=? AND " + "resource_role_id IN (?";

    /**
     * <p>
     * The instance of the underlying implementation of the ProjectPaymentCalculator interface which is
     * decorated/wrapped by this payment adjuster.
     * </p>
     * <p>
     * It is initialized in the constructor and never changed after that. Can not be null after initialization.
     * </p>
     * <p>
     * It is used in the getDefaultPayments() method, the call is delegated to it before payments adjustment.
     * </p>
     */
    private final ProjectPaymentCalculator projectPaymentCalculator;

    /**
     * <p>
     * Creates a new instance of this class using the default configuration file {@link #DEFAULT_CONFIG_FILENAME}
     * and default namespace {@link #DEFAULT_CONFIG_NAMESPACE}. When this constructor is used to create an instance
     * of this class, the configuration file should contain the Object Factory configuration to be used to
     * instantiate the underlying implementation of the <code>ProjectPaymentCalculator</code>.
     * </p>
     *
     * @throws ProjectPaymentCalculatorConfigurationException
     *             If any error occurred during the configuration. (Required configuration parameter not found or
     *             has invalid value or the configuration file can not be opened for some reason).
     */
    public ProjectPaymentAdjustmentCalculator() {
        this(DEFAULT_CONFIG_FILENAME, DEFAULT_CONFIG_NAMESPACE);
    }

    /**
     * <p>
     * Creates a new instance of this class loading the configuration parameters using the specified configuration
     * file and namespace. When this constructor is used to create an instance of this class, the configuration
     * file should contain the Object Factory configuration to be used to instantiate the underlying implementation
     * of the <code>ProjectPaymentCalculator</code>.
     * </p>
     *
     * @param configFilePath
     *            The configuration file path.
     * @param namespace
     *            The configuration namespace.
     * @throws IllegalArgumentException
     *             If any of input parameters is <code>null</code> or is an empty String.
     * @throws ProjectPaymentCalculatorConfigurationException
     *             If any error occurred during the configuration. (Required configuration parameter not found or
     *             has invalid value or the configuration file can not be opened for some reason).
     */
    public ProjectPaymentAdjustmentCalculator(String configFilePath, String namespace) {
        this(getConfigurationObject(configFilePath, namespace));
    }

    /**
     * <p>
     * Creates a new instance of this class using the provided configuration object. When this constructor is used
     * to create an instance of this class, the configuration file should contain the Object Factory configuration
     * to be used to instantiate the underlying implementation of the <code>ProjectPaymentCalculator</code>.
     * </p>
     *
     * @param configurationObject
     *            The ConfigurationObject instance holding this class configuration parameters.
     * @throws IllegalArgumentException
     *             If the input parameter is <code>null</code>.
     * @throws ProjectPaymentCalculatorConfigurationException
     *             If any error occurred during the configuration. (Required configuration parameter not found or
     *             has invalid value).
     */
    public ProjectPaymentAdjustmentCalculator(ConfigurationObject configurationObject) {
        super(configurationObject);

        // Get Object Factory configuration
        ConfigurationObject objectFactoryConfig =
            Helper.getChildConfiguration(configurationObject, "object_factory_config");

        // create object factory
        ObjectFactory objectFactory = ProjectPaymentAdjustmentCalculator.createObjectFactory(objectFactoryConfig);

        // get the object factory key for ProjectPaymentCalculator
        String projectPaymentCalculatorKey =
            Helper.getStringProperty(configurationObject, "project_payment_calculator_key", true);

        // create the ProjectPaymentCalculator instance with ObjectFactory
        projectPaymentCalculator =
            ProjectPaymentAdjustmentCalculator.createCalculator(objectFactory, projectPaymentCalculatorKey);
    }

    /**
     * <p>
     * Creates a new instance of this class with the given <code>ProjectPaymentCalculator</code> instance and using
     * the default configuration file {@link #DEFAULT_CONFIG_FILENAME} and default namespace
     * {@link #DEFAULT_CONFIG_NAMESPACE}.
     * </p>
     *
     * @param projectPaymentCalculator
     *            The <code>ProjectPaymentCalculator</code> underlying implementation instance to set.
     * @throws IllegalArgumentException
     *             If <code>projectPaymentCalculator</code> input parameter is <code>null</code>.
     * @throws ProjectPaymentCalculatorConfigurationException
     *             If any error occurred during the configuration. (Required configuration parameter not found or
     *             has invalid value or the configuration file can not be opened for some reason).
     */
    public ProjectPaymentAdjustmentCalculator(ProjectPaymentCalculator projectPaymentCalculator) {
        this(projectPaymentCalculator, DEFAULT_CONFIG_FILENAME, DEFAULT_CONFIG_NAMESPACE);
    }

    /**
     * <p>
     * Creates a new instance of this class using the the given <code>ProjectPaymentCalculator</code> instance and
     * by loading the configuration parameters using the specified configuration file and namespace.
     * </p>
     *
     * @param projectPaymentCalculator
     *            The <code>ProjectPaymentCalculator</code> underlying implementation to be decorated by this
     *            class.
     * @param configFilePath
     *            The configuration file path.
     * @param namespace
     *            The configuration namespace.
     * @throws IllegalArgumentException
     *             If any of input parameters is <code>null</code> or if any String input parameter is an empty
     *             String.
     * @throws ProjectPaymentCalculatorConfigurationException
     *             If any error occurred during the configuration. (Required configuration parameter not found or
     *             has invalid value or the configuration file can not be opened for some reason).
     */
    public ProjectPaymentAdjustmentCalculator(ProjectPaymentCalculator projectPaymentCalculator, String configFilePath,
        String namespace) {
        this(projectPaymentCalculator, getConfigurationObject(configFilePath, namespace));
    }

    /**
     * <p>
     * Creates a new instance of this class using the specified ProjectPaymentCalculator implementation instance
     * and the provided configuration object.
     * </p>
     *
     * @param projectPaymentCalculator
     *            The <code>ProjectPaymentCalculator</code> underlying implementation to be decorated by this
     *            class.
     * @param configurationObject
     *            The <code>ConfigurationObject</code> instance holding this class configuration parameters.
     * @throws IllegalArgumentException
     *             If any of input parameters is <code>null</code>.
     * @throws ProjectPaymentCalculatorConfigurationException
     *             If any error occurred during the configuration. (Required configuration parameter not found or
     *             has invalid value).
     */
    public ProjectPaymentAdjustmentCalculator(ProjectPaymentCalculator projectPaymentCalculator,
        ConfigurationObject configurationObject) {
        super(configurationObject);
        ParameterCheckUtility.checkNotNull(projectPaymentCalculator, "projectPaymentCalculator");
        this.projectPaymentCalculator = projectPaymentCalculator;
    }

    /**
     * <p>
     * This method is a decorating implementation method of the namesake method in the
     * <code>ProjectPaymentCalculator</code> interface.
     * </p>
     * <p>
     * It provides a concrete implementation responsible of calculating and optionally adjusting the default
     * payments of the resource roles identified by the given IDs for the project identified by the given
     * <code>projectId</code>.
     * </p>
     * <p>
     * This method calculates the default payments using the aggregated <code>ProjectPaymentCalculator</code>
     * underlying implementation and optionally adjusts the calculated payments based on the fixed amount and
     * multiplier values for the given project and resource roles, the multiplier and the fixed amount are
     * retrieved from the database.
     * </p>
     * <p>
     * If no record is found in the database, then an empty Map is returned.
     * </p>
     *
     * @param projectId
     *            The id of the project for which to calculate the default payments per resource role.
     * @param resourceRoleIDs
     *            The list of the resource roles ids for which to get and optionally adjust the default payments.
     * @return The mapping between the resource role IDs and the default payment (that were optionally adjusted).
     *         (can be empty if there is no record found in the database.)
     * @throws IllegalArgumentException
     *             If <code>projectId</code> is not positive or if <code>resourceRoleIDs</code> list is
     *             <code>null</code> or is an empty list or contains <code>null</code> elements.
     * @throws ProjectPaymentCalculatorException
     *             If any error occurred during the operation.
     */
    public Map<Long, BigDecimal> getDefaultPayments(long projectId, List<Long> resourceRoleIDs)
        throws ProjectPaymentCalculatorException {
        Log logger = getLogger();
        String signature = DEFAULT_CONFIG_NAMESPACE + "#getDefaultPayments(long, List<Long>)";
        LoggingWrapperUtility.logEntrance(logger, signature, new String[] {"projectId", "resourceRoleIDs"},
            new Object[] {projectId, resourceRoleIDs});

        // arguments checking
        try {
            ParameterCheckUtility.checkPositive(projectId, "projectId");
            ParameterCheckUtility.checkNotNullNorEmpty(resourceRoleIDs, "resourceRoleIDs");
            ParameterCheckUtility.checkNotNullElements(resourceRoleIDs, "resourceRoleIDs");
        } catch (IllegalArgumentException e) {
            throw LoggingWrapperUtility.logException(logger, signature, e);
        }

        // Create the parameterized query to be used to get the fixed amount and multiplier for each one of the
        // specific resource role id from the database
        StringBuilder query = new StringBuilder(GET_ADJUSTMENT_QUERY_PREFIX);

        // Optionally concatenate the "?" placeholders for the other resource roles IDs (The above query is created
        // assuming that there will be at least on resource role id in the list, otherwise IAE is thrown ).
        for (int i = 1; i < resourceRoleIDs.size(); ++i) {
            query.append(",?");
        }
        // add the closing parenthesis to the query
        query.append(")");

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        Connection connection = null;
        try {
            // Create a connection to the database.
            connection = createConnection();

            // Create the prepared statement.
            preparedStatement = connection.prepareStatement(query.toString());

            // Set the prepared statement parameters.
            preparedStatement.setLong(1, projectId);
            for (int i = 0; i < resourceRoleIDs.size(); ++i) {
                preparedStatement.setLong(i + 2, resourceRoleIDs.get(i));
            }

            // Execute the query and get the result.
            resultSet = preparedStatement.executeQuery();

            // call the underlying implementation to calculate the default payments.
            Map<Long, BigDecimal> defaultPayments =
                projectPaymentCalculator.getDefaultPayments(projectId, resourceRoleIDs);

            // update the default payments for each resource role id based on the fixed amount and multiplier
            // values
            // please note that it is possible that some of resource role IDs will not be present
            // in the defaultPayments Map returned by the underlying implementation of the ProjectPaymentCalculator
            // this is taken into consideration in this method implementation.
            while (resultSet.next()) {
                // adjust the default payments
                adjustPayment(defaultPayments, resultSet);
            }

            LoggingWrapperUtility.logExit(getLogger(), signature, new Object[] {defaultPayments});

            return defaultPayments;
        } catch (SQLException e) {
            throw LoggingWrapperUtility.logException(getLogger(), signature, new ProjectPaymentCalculatorException(
                "Fails to query adjustment values from database", e));
        } finally {
            Helper.closeDatabaseResources(getLogger(), signature, resultSet, preparedStatement, connection);
        }
    }

    /**
     * <p>
     * Adjust the default payment of a resource role. If fixed amount is specified then it will be used as the
     * adjusted payment. If it wasn't, then default payment will be adjusted by multiplying it with multiplier.
     * </p>
     *
     * @param defaultPayments
     *            The map containing default payments to be adjusted.
     * @param resultSet
     *            The result set that hold adjustment values from database.
     * @throws SQLException
     *             If any error occurs.
     */
    private void adjustPayment(Map<Long, BigDecimal> defaultPayments, ResultSet resultSet) throws SQLException {
        long resourceRoleId = resultSet.getLong(RESOURCE_ROLE_ID_COLUMN);
        BigDecimal fixedAmount = resultSet.getBigDecimal(FIXED_AMOUNT_COLUMN);
        if (fixedAmount != null) {
            // put directly into the map since fixed amount is available
            defaultPayments.put(resourceRoleId, fixedAmount.setScale(2, RoundingMode.HALF_UP));
        } else {
            // i.e fixedAmount is null
            // get the multiplier
            float multiplier = resultSet.getFloat(MULTIPLIER_COLUMN);
            if (!resultSet.wasNull()) {
                // check if resource role id has default payment to be adjusted
                BigDecimal defaultPayment = defaultPayments.get(resourceRoleId);

                if (defaultPayment != null) {
                    // multiply the default payment with the multiplier
                    BigDecimal payment =
                        defaultPayment.multiply(BigDecimal.valueOf(multiplier)).setScale(2, RoundingMode.HALF_UP);

                    defaultPayments.put(resourceRoleId, payment);
                }
            }
        }
    }

    /**
     * <p>
     * Creates an instance of ProjectPaymentCalculator with ObjectFactory using specified key.
     * </p>
     *
     * @param objectFactory
     *            The object factory.
     * @param key
     *            The key of the object to instantiate.
     * @return An instance of ProjectPaymentCalculator.
     * @throws ProjectPaymentCalculatorConfigurationException
     *             If any error occurs while reading key's value or creating object
     */
    private static ProjectPaymentCalculator createCalculator(ObjectFactory objectFactory, String key) {
        try {
            Object obj = objectFactory.createObject(key);

            if (!ProjectPaymentCalculator.class.isInstance(obj)) {
                throw new ProjectPaymentCalculatorConfigurationException("The type returned should be "
                    + ProjectPaymentCalculator.class.getName() + ", but is " + obj.getClass().getName() + ".");
            }
            return (ProjectPaymentCalculator) obj;
        } catch (InvalidClassSpecificationException e) {
            throw new ProjectPaymentCalculatorConfigurationException("Fails to create object for '" + key + "'", e);
        }
    }

    /**
     * <p>
     * Creates a new instance of <code>ObjectFactory</code> with given configuration that provides object
     * specification.
     * </p>
     *
     * @param config
     *            The configuration that provides object specification.
     * @return An object factory.
     * @throws ProjectPaymentCalculatorConfigurationException
     *             If any error occurs while creating specification factory for object factory.
     */
    private static ObjectFactory createObjectFactory(ConfigurationObject config) {
        return new ObjectFactory(createObjectFactorySpecificationFactory(config));
    }

    /**
     * <p>
     * Creates a new instance of <code>ConfigurationObjectSpecificationFactory</code> with given configuration.
     * </p>
     *
     * @param config
     *            The configuration that provides object specification.
     * @return A specification factory.
     * @throws ProjectPaymentCalculatorConfigurationException
     *             If any error occurs while creating specification factory.
     */
    private static SpecificationFactory createObjectFactorySpecificationFactory(ConfigurationObject config) {
        try {
            return new ConfigurationObjectSpecificationFactory(config);
        } catch (IllegalReferenceException e) {
            throw new ProjectPaymentCalculatorConfigurationException(
                "Fails to create object factory specification factory. " + "Found invalid configuration object", e);
        } catch (SpecificationConfigurationException e) {
            throw new ProjectPaymentCalculatorConfigurationException(
                "Fails to create object factory specification factory. "
                    + "An error occurs while accessing configuration", e);
        }
    }
}
