/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.assignment.accuracytests;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.persistence.ConfigurationFileManager;
import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.management.phase.PhaseManager;
import com.topcoder.management.project.Project;
import com.topcoder.management.project.ProjectManager;
import com.topcoder.management.project.ProjectManagerImpl;
import com.topcoder.management.resource.ResourceManager;
import com.topcoder.management.review.application.ReviewApplication;
import com.topcoder.management.review.application.ReviewApplicationManager;
import com.topcoder.management.review.application.ReviewAuction;
import com.topcoder.management.review.application.ReviewAuctionManager;
import com.topcoder.management.review.application.impl.ReviewApplicationManagerImpl;
import com.topcoder.management.review.application.impl.ReviewAuctionManagerImpl;
import com.topcoder.management.review.application.search.ReviewApplicationFilterBuilder;
import com.topcoder.management.review.assignment.algorithm.BruteForceBasedReviewAssignmentAlgorithm;
import com.topcoder.management.review.assignment.algorithm.MaxSumOfRatingReviewAssignmentAlgorithm;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.util.errorhandling.ExceptionData;
import com.topcoder.util.objectfactory.ObjectFactory;
import com.topcoder.util.objectfactory.impl.ConfigurationObjectSpecificationFactory;

import java.sql.Connection;
import java.util.List;

/**
 * <p>A helper class providing the data to be used for testing.</p>
 * 
 * @author isv
 * @version 1.0
 */
public class TestDataFactory {

    /**
     * <p>Constructs new <code>TestDataFactory</code> instance. This implementation does nothing.</p>
     */
    private TestDataFactory() {
    }

    /**
     * <p>Gets the message to be passed to constructors of the tested exception classes.</p>
     * 
     * @return a <code>String</code> providing the message for exceptions.
     */
    public static String getExceptionMessage() {
        return "Exception message";
    }

    /**
     * <p>Gets the cause to be passed to constructors of the tested exception classes.</p>
     *
     * @return a <code>Throwable</code> providing the message for exceptions.
     */
    public static Throwable getExceptionCause() {
        return new IllegalArgumentException("Test");
    }

    /**
     * <p>Gets the data to be passed to constructors of the tested exception classes.</p>
     *
     * @return a <code>ExceptionData</code> providing the data for exceptions.
     */
    public static ExceptionData getExceptionData() {
        ExceptionData data = new ExceptionData();
        data.setApplicationCode("Review Assignment");
        data.setErrorCode("01");
        data.setModuleCode("Component");
        data.setLogged(true);
        return data;
    }

    /**
     * <p>Gets the review auction manager.</p>
     * 
     * @return a <code>ReviewAuctionManager</code> to be used for getting review auction details from database.  
     */
    public static ReviewAuctionManager getReviewAuctionManager() {
        ReviewAuctionManager manager 
                = new ReviewAuctionManagerImpl("accuracy/ReviewAuctionManagerImpl.properties", 
                                               ReviewAuctionManagerImpl.DEFAULT_CONFIG_NAMESPACE);
        return manager;
    }

    /**
     * <p>Gets the review application manager.</p>
     *
     * @return a <code>ReviewApplicationManager</code> to be used for getting review application details from database.
     */
    public static ReviewApplicationManager getReviewApplicationManager() {
        ReviewApplicationManager manager =
                new ReviewApplicationManagerImpl("accuracy/ReviewApplicationManagerImpl.properties",
                                                 ReviewApplicationManagerImpl.DEFAULT_CONFIG_NAMESPACE);
        return manager;
    }

    /**
     * <p>Gets the review auction matching the specified ID.</p>
     * 
     * @param auctionId a <code>long</code> providing the review auction ID.
     * @return a <code>ReviewAuction</code> providing the details for requested review auction. 
     * @throws Exception if an unexpected error occurs.
     */
    public static ReviewAuction getReviewAuction(long auctionId) throws Exception {
        ReviewAuctionManager reviewAuctionManager = TestDataFactory.getReviewAuctionManager();
        ReviewAuction auction = reviewAuctionManager.getAuction(auctionId);
        return auction;
    }

    /**
     * <p>Gets the list of reviewer applications for specified review auction.</p>
     *
     * @param auctionId a <code>long</code> providing the review auction ID.
     * @return a <code>List</code> listing the applications for specified review auction. 
     * @throws Exception if an unexpected error occurs.
     */
    public static List<ReviewApplication> getReviewApplications(long auctionId) throws Exception {
        ReviewApplicationManager reviewApplicationManager = TestDataFactory.getReviewApplicationManager();
        Filter auctionIdFilter = ReviewApplicationFilterBuilder.createAuctionIdFilter(auctionId);
        List<ReviewApplication> reviewApplications = reviewApplicationManager.searchApplications(auctionIdFilter);
        return reviewApplications;
    }

    /**
     * <p>Gets the details for the project related to specified review auction.</p>
     * 
     * @param auctionId a <code>long</code> providing the review auction ID.
     * @return a <code>Project</code> providing the details for the project. 
     * @throws Exception if an unexpected error occurs.
     */
    public static Project getRelatedProject(long auctionId) throws Exception {
        ReviewAuction reviewAuction = getReviewAuction(auctionId);
        ProjectManager m = new ProjectManagerImpl();
        return m.getProject(reviewAuction.getProjectId());
    }

    /**
     * <p>Gets the project resource manager.</p>
     * 
     * @return a <code>ResourceManager</code> to be used for accessing project resources. 
     * @throws Exception if an unexpected error occurs.
     */
    public static ResourceManager getResourceManager() throws Exception {
        ConfigurationObject config = getReviewAssignmentProjectManagerConfig(false, false);
        String resourceManagerKey = (String) config.getPropertyValue("resourceManagerKey");
        ObjectFactory objectFactory = getObjectFactory(config);
        ResourceManager resourceManager = (ResourceManager) objectFactory.createObject(resourceManagerKey);
        return resourceManager;
    }

    /**
     * <p>Gets the project phase manager.</p>
     *
     * @return a <code>PhaseManager</code> to be used for accessing project phases.
     * @throws Exception if an unexpected error occurs.
     */
    public static PhaseManager getPhaseManager() throws Exception {
        ConfigurationObject config = getReviewAssignmentProjectManagerConfig(false, false);
        String phaseManagerKey = (String) config.getPropertyValue("phaseManagerKey");
        ObjectFactory objectFactory = getObjectFactory(config);
        PhaseManager phaseManager = (PhaseManager) objectFactory.createObject(phaseManagerKey);
        return phaseManager;
    }


    /**
     * <p>Gets the configuration for algorithm implementation.</p>
     * 
     *
     * @param unSetLog <code>true</code> if logger configuration is to be unset; <code>false</code> otherwise.
     * @param unSetMinFeedbacks <code>true</code> if minimum feedbacks is to be unset; <code>false</code> otherwise.
     * @param unSetDefaultRating <code>true</code> if default rating is to be unset; <code>false</code> otherwise.
     * @param unSetConnectionName <code>true</code> if connection name is to be unset; <code>false</code> otherwise.
     * @return a <code>ConfigurationObject</code> to be used for configuring the algorithm implementation. 
     * @throws Exception if an unexpected error occurs.
     */
    public static ConfigurationObject getAlgorithmConfig(boolean unSetLog, boolean unSetMinFeedbacks,
                                                         boolean unSetDefaultRating, boolean unSetConnectionName) 
            throws Exception {
        ConfigurationFileManager manager = new ConfigurationFileManager("test_files/accuracy/Config.properties");
        ConfigurationObject globalConfig 
                = manager.getConfiguration("com.topcoder.management.review.assignment.accuracy");
        ConfigurationObject config = globalConfig.getChild("com.topcoder.management.review.assignment");
        if (unSetLog) {
            config.removeProperty("loggerName");
        }
        if (unSetMinFeedbacks) {
            config.removeProperty("minimumFeedbacks");
        }
        if (unSetDefaultRating) {
            config.removeProperty("defaultRating");
        }
        if (unSetConnectionName) {
            config.removeProperty("connectionName");
        }
        return config;
    }

    /**
     * <p>Gets the configuration for email notification manager.</p>
     *
     * @param unSetLog <code>true</code> if logger configuration is to be unset; <code>false</code> otherwise.
     * @return a <code>ConfigurationObject</code> to be used for configuring the algorithm implementation.
     * @throws Exception if an unexpected error occurs.
     */
    public static ConfigurationObject getNotificationManagerConfig(boolean unSetLog)
            throws Exception {
        ConfigurationFileManager manager = new ConfigurationFileManager("test_files/accuracy/Config.properties");
        ConfigurationObject globalConfig
                = manager.getConfiguration("com.topcoder.management.review.assignment.accuracy");
        ConfigurationObject config = globalConfig.getChild("com.topcoder.management.review.assignment");
        if (unSetLog) {
            config.removeProperty("loggerName");
        }
        return config;
    }

    /**
     * <p>Gets the algorithm for generating assignments.</p>
     * 
     * @return a <code>BruteForceBasedReviewAssignmentAlgorithm</code> to be used for generating assignments. 
     * @throws Exception if an unexpected error occurs.
     */
    public static BruteForceBasedReviewAssignmentAlgorithm getAlgorithm() throws Exception {
        MaxSumOfRatingReviewAssignmentAlgorithm algo = new MaxSumOfRatingReviewAssignmentAlgorithm();
        algo.configure(getAlgorithmConfig(false, false, false, false));
        return algo;
    }

    /**
     * <p>Gets the configuration for review assignment project manager.</p>
     *
     *
     * @param unSetLog <code>true</code> if logger configuration is to be unset; <code>false</code> otherwise.
     * @param unSetRegDateFormat <code>true</code> if registration date format configuration is to be unset; 
     *        <code>false</code> otherwise.
     * @return a <code>ConfigurationObject</code> to be used for configuring the algorithm implementation.
     * @throws Exception if an unexpected error occurs.
     */
    public static ConfigurationObject getReviewAssignmentProjectManagerConfig(boolean unSetLog,
                                                                              boolean unSetRegDateFormat)
            throws Exception {
        ConfigurationFileManager manager = new ConfigurationFileManager("test_files/accuracy/Config.properties");
        ConfigurationObject globalConfig
                = manager.getConfiguration("com.topcoder.management.review.assignment.accuracy");
        ConfigurationObject config = globalConfig.getChild("com.topcoder.management.review.assignment");
        if (unSetLog) {
            config.removeProperty("loggerName");
        }
        if (unSetRegDateFormat) {
            config.removeProperty("registrationDateFormatString");
        }
        return config;
    }

    /**
     * <p>Gets the configuration for review assignment manager.</p>
     *
     * @param unSetLog <code>true</code> if logger configuration is to be unset; <code>false</code> otherwise.
     * @return a <code>ConfigurationObject</code> to be used for configuring the algorithm implementation.
     * @throws Exception if an unexpected error occurs.
     */
    public static ConfigurationObject getReviewAssignmentManagerConfig(boolean unSetLog)
            throws Exception {
        ConfigurationFileManager manager = new ConfigurationFileManager("test_files/accuracy/Config.properties");
        ConfigurationObject globalConfig
                = manager.getConfiguration("com.topcoder.management.review.assignment.accuracy");
        ConfigurationObject config = globalConfig.getChild("com.topcoder.management.review.assignment");
        if (unSetLog) {
            config.removeProperty("loggerName");
        }
        return config;
    }

    /**
     * <p>Creates an <code>ObjectFactory</code> instance from given <code>ConfigurationObject</code>.</p>
     *
     * @param config a <code>ConfigurationObject</code> providing the configuration for object factory. 
     * @return an <code>ObjectFactory</code> instance.
     * @throws Exception if an unexpected error occurs.
     */
    public static ObjectFactory getObjectFactory(ConfigurationObject config) throws Exception {
        ConfigurationObjectSpecificationFactory specFactory 
                = new ConfigurationObjectSpecificationFactory(config.getChild("objectFactoryConfig"));
        return new ObjectFactory(specFactory);
    }

    /**
     * <p>Gets connection to target database.</p>
     * 
     * @return a <code>Connection</code> providing connection to target database. 
     * @throws Exception if an unexpected error occurs.
     */
    public static Connection getDatabaseConnection() throws Exception {
        ConfigurationObject config = getReviewAssignmentProjectManagerConfig(false, false);
        ObjectFactory objectFactory = getObjectFactory(config);
        DBConnectionFactory connectionFactory 
                = (DBConnectionFactory) objectFactory.createObject("dbConnectionFactoryObjectFactoryKey");
        return connectionFactory.createConnection();
    }
}
