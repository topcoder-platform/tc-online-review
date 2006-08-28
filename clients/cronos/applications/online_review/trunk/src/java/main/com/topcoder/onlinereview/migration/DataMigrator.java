/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration;

import com.topcoder.db.connectionfactory.ConfigurationException;
import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.db.connectionfactory.UnknownConnectionException;

import com.topcoder.onlinereview.migration.dto.newschema.project.ProjectNew;
import com.topcoder.onlinereview.migration.dto.oldschema.ProjectOld;
import com.topcoder.onlinereview.migration.persistence.ProjectPersistence;
import com.topcoder.onlinereview.migration.persistence.ScorecardPersistence;

import com.topcoder.util.config.ConfigManager;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.Iterator;
import java.util.List;


/**
 * The DataMigrator.
 *
 * @author brain_cn
 * @version 1.0
 */
public class DataMigrator {
    /** The namespace for migration. */
    public static final String NAMESPACE = DataMigrator.class.getName();
    private Connection loaderConn = null;
    private Connection persistConn = null;
    private ScorecardLoader scorecardLoader = null;
    private ScorecardPersistence scorecardPersistence = null;
    private ProjectLoader projectLoader = null;
    private ProjectPersistence projectPersistence = null;
    private ScorecardTransformer scorecardTransformer = null;
    private ProjectTransformer projectTransformer = null;
    private String loaderConnName = "tcs_catalog_old";
    private String persistConnName = "tcs_catalog_new";

    /**
     * Creates a new DataMigrator object.
     *
     * @param loaderConn the connection to load data
     * @param persistConn the connection to persist
     *
     * @throws Exception if error occurs while load or store data
     */
    public DataMigrator(Connection loaderConn, Connection persistConn)
        throws Exception {
        Util.info("Create DataMigrator with two connection");
        this.loaderConn = loaderConn;
        this.persistConn = persistConn;
    }

    /**
     * Creates a new DataMigrator object.
     *
     * @throws Exception if error occurs
     */
    public DataMigrator() throws Exception {
        Util.info("Create DataMigrator that load connection from config file");
        loaderConnName = getString("loader_conn_name", loaderConnName);
        persistConnName = getString("persist_conn_name", persistConnName);
    }

    /**
     * Return loader connection.
     *
     * @return loader connection.
     *
     * @throws Exception if error occurs while create connection
     */
    public Connection getLoaderConnection() throws Exception {
        if (isIdle(this.loaderConn)) {
            this.loaderConn = getDBConnectionFactory().createConnection(loaderConnName);
        }

        return loaderConn;
    }

    private static boolean isIdle(Connection conn) {
        try {
            return (conn == null) || conn.isClosed();
        } catch (SQLException e) {
        	Util.warn("the connection is idel, message: " + e.getMessage());
            return true;
        }
    }

    /**
     * Return persistence connection.
     *
     * @return persistence connection.
     *
     * @throws Exception if error occurs while create connection
     */
    public Connection getPersistenceConnection() throws Exception {
        if (isIdle(this.persistConn)) {
            this.persistConn = getDBConnectionFactory().createConnection(persistConnName);
        }

        return persistConn;
    }

    private String getString(String propertyName, String defaultValue)
        throws Exception {
        ConfigManager cm = ConfigManager.getInstance();
        String value = cm.getString(NAMESPACE, propertyName);

        if ((value == null) || (value.trim().length() == 0)) {
            Util.info(propertyName + " not set, use default " + defaultValue);

            return defaultValue;
        } else {
            Util.info(propertyName + " is set, the value is " + value);

            return value;
        }
    }

    /**
     * Return DBConnectionFactory to create connection.
     *
     * @return DBConnectionFactory
     *
     * @throws UnknownConnectionException to JUnit
     * @throws ConfigurationException to JUnit
     */
    private DBConnectionFactory getDBConnectionFactory()
        throws UnknownConnectionException, ConfigurationException {
        return new DBConnectionFactoryImpl(DBConnectionFactoryImpl.class.getName());
    }

    /**
     * Migrate old online review data to new schema.
     *
     * @throws Exception if error occurs while load or store data
     */
    public void migrate() throws Exception {
        Util.info("start migrate");
        migrateScorecard();
        migrateProject();
        Util.info("end migrate");
    }

    /**
     * Release all resource.
     */
    public void close() {
        Util.info("close connections");
        DatabaseUtils.closeSilently(loaderConn);
        DatabaseUtils.closeSilently(persistConn);
    }

    /**
     * Migrate scorecard data.
     *
     * @throws Exception if error occurs while load or store data
     */
    public void migrateScorecard() throws Exception {
        long startTime = Util.startMain("migrateScorecard");

        // Load data
        List input = getScorecardLoader().loadScorecardTemplate();

        // transform data
        List output = getScorecardTransformer().transformScorecardTemplates(input);

        // store data
        getScorecardPersistence().storeScorecard(output);
        Util.logMainAction(input.size(), "migrateScorecard", startTime);
    }

    /**
     * Migrate Project/ProjectAudit data.
     *
     * @throws Exception if error occurs while generate id
     */
    public void migrateProject() throws Exception {
        // Load all project ids
        List input = getProjectLoader().loadProjectIds();

        // Remove migrated project
        input.removeAll(MapUtil.getMigratedProjectIds());

        migrateProjects(input);
    }

    public void updateSQLS() throws Exception {
    	
    }

    /**
     * Migrate Project/ProjectAudit data.
     *
     * @param input project ids
     *
     * @throws Exception if error occurs while generate id
     */
    public void migrateProjects(List input) throws Exception {
        long startTime = Util.startMain("migrateProjects");

        for (Iterator iter = input.iterator(); iter.hasNext();) {
            migrateProject(Integer.parseInt(iter.next().toString()));
        }

        Util.logMainAction("migrateProjects", startTime);
    }

    public void migrateProject(int projectId) throws Exception {
        try {
            long startTime = Util.startMain("migrateProject");

            ProjectOld oldProject = getProjectLoader().loadProject(projectId);
            ProjectNew newProject = getProjectTransformer().transformProject(oldProject);

            if (getProjectPersistence().storeProject(newProject)) {
                MapUtil.storeMigratedProjectId(oldProject.getProjectId(), newProject.getProjectId());
            } else {
                Util.warn("Failed to store project, project_id: " + oldProject.getProjectId());
            }

            Util.logMainAction("migrateProject", startTime);
        } catch (Exception e) {
            Util.warn(e);
            Util.warn("Failed to migrate project, projectId: " + projectId);
        }
    	
    }

    /**
     * Returns the projectLoader.
     *
     * @return Returns the projectLoader.
     */
    public ProjectLoader getProjectLoader() throws Exception {
    	if (this.projectLoader == null) {
            this.projectLoader = new ProjectLoader(this);
    	}
        return projectLoader;
    }

    /**
     * Set the projectLoader.
     *
     * @param projectLoader The projectLoader to set.
     */
    public void setProjectLoader(ProjectLoader projectLoader) {
        this.projectLoader = projectLoader;
    }

    /**
     * Returns the projectPersistence.
     *
     * @return Returns the projectPersistence.
     */
    public ProjectPersistence getProjectPersistence() throws Exception {
    	if (this.projectPersistence == null) {
            this.projectPersistence = new ProjectPersistence(this);
    	}
        return projectPersistence;
    }

    /**
     * Set the projectPersistence.
     *
     * @param projectPersistence The projectPersistence to set.
     */
    public void setProjectPersistence(ProjectPersistence projectPersistence) {
        this.projectPersistence = projectPersistence;
    }

    /**
     * Returns the scorecardLoader.
     *
     * @return Returns the scorecardLoader.
     */
    public ScorecardLoader getScorecardLoader() throws Exception {
    	if (this.scorecardLoader == null) {
            this.scorecardLoader = new ScorecardLoader(this);
    	}
        return scorecardLoader;
    }

    /**
     * Set the scorecardLoader.
     *
     * @param scorecardLoader The scorecardLoader to set.
     */
    public void setScorecardLoader(ScorecardLoader scorecardLoader) {
        this.scorecardLoader = scorecardLoader;
    }

    /**
     * Returns the scorecardPersistence.
     *
     * @return Returns the scorecardPersistence.
     */
    public ScorecardPersistence getScorecardPersistence()throws Exception {
    	if (this.scorecardPersistence == null) {
            this.scorecardPersistence = new ScorecardPersistence(this);
    	}
        return scorecardPersistence;
    }

    /**
     * Set the scorecardPersistence.
     *
     * @param scorecardPersistence The scorecardPersistence to set.
     */
    public void setScorecardPersistence(ScorecardPersistence scorecardPersistence) {
        this.scorecardPersistence = scorecardPersistence;
    }

    /**
     * Returns the projectTransformer.
     *
     * @return Returns the projectTransformer.
     */
    public ProjectTransformer getProjectTransformer() throws Exception {
    	if (this.projectTransformer == null) {
            this.projectTransformer = new ProjectTransformer();
    	}
        return projectTransformer;
    }

    /**
     * Set the projectTransformer.
     *
     * @param projectTransformer The projectTransformer to set.
     */
    public void setProjectTransformer(ProjectTransformer projectTransformer) {
        this.projectTransformer = projectTransformer;
    }

    /**
     * Returns the scorecardTransformer.
     *
     * @return Returns the scorecardTransformer.
     */
    public ScorecardTransformer getScorecardTransformer() throws Exception {
    	if (this.scorecardTransformer == null) {
            this.scorecardTransformer = new ScorecardTransformer();
    	}
        return scorecardTransformer;
    }

    /**
     * Set the scorecardTransformer.
     *
     * @param scorecardTransformer The scorecardTransformer to set.
     */
    public void setScorecardTransformer(ScorecardTransformer scorecardTransformer) {
        this.scorecardTransformer = scorecardTransformer;
    }
}
