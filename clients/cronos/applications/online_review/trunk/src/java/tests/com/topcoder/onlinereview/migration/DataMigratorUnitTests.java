/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration;

import com.topcoder.db.connectionfactory.ConfigurationException;
import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.db.connectionfactory.UnknownConnectionException;

import com.topcoder.onlinereview.migration.dto.newschema.project.ProjectNew;
import com.topcoder.onlinereview.migration.dto.newschema.scorecard.Scorecard;
import com.topcoder.onlinereview.migration.dto.newschema.scorecard.ScorecardGroup;
import com.topcoder.onlinereview.migration.dto.newschema.scorecard.ScorecardQuestionNew;
import com.topcoder.onlinereview.migration.dto.newschema.scorecard.ScorecardSectionNew;
import com.topcoder.onlinereview.migration.dto.oldschema.ProjectOld;
import com.topcoder.onlinereview.migration.dto.oldschema.scorecard.QuestionTemplate;
import com.topcoder.onlinereview.migration.dto.oldschema.scorecard.ScSectionGroup;
import com.topcoder.onlinereview.migration.dto.oldschema.scorecard.ScorecardSectionOld;
import com.topcoder.onlinereview.migration.dto.oldschema.scorecard.ScorecardTemplate;

import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.log.Level;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogFactory;

import junit.framework.TestCase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


/**
 * The test of DataMigrator.
 *
 * @author brain_cn
 * @version 1.0
 */
public class DataMigratorUnitTests extends TestCase {
    private static final String OLD_CONNECTION_NAME = "tcs_catalog_old";
    private static final String NEW_CONNECTION_NAME = "tcs_catalog_new";

    /** Represents the configuration file. */
    private static final String CONFIG_FILE = "data_migration.xml";

    /** Represents the all namespaces. */
    private static final String[] NAMESPACES = new String[] {
            "com.topcoder.util.log", "com.topcoder.db.connectionfactory.DBConnectionFactoryImpl",
            "com.topcoder.util.idgenerator.IDGeneratorImpl",
            "com.topcoder.onlinereview.migration.DataMigrator"
        };
    private Log logger =  LogFactory.getLog();
    private Connection loaderConn = null;
    private Connection persistConn = null;
    private DataMigrator migrator = null;

    /**
     * Load namespace for testing.
     *
     * @throws Exception to JUnit
     */
    public void setUp() throws Exception {
        DBConnectionFactory dbf = getDBConnectionFactory();
        loaderConn = dbf.createConnection(OLD_CONNECTION_NAME);
        persistConn = dbf.createConnection(NEW_CONNECTION_NAME);
        migrator = new DataMigrator(loaderConn, persistConn);
    }

    /**
     * Clears the configuration namespaces.
     *
     * @throws Exception if any unexpected exception occurs.
     */
    public void tearDown() throws Exception {
        migrator.close();
        DatabaseUtils.closeSilently(loaderConn);
        DatabaseUtils.closeSilently(persistConn);
    }

    /**
     * Test method for 'migrate()'
     *
     * @throws Exception to JUnit
     */
    public void atestMigrate() throws Exception {
    }

    /**
     * Test method for 'migrate()'
     *
     * @throws Exception to JUnit
     */
    public void testProjectLoader() throws Exception {
        List list = migrator.getProjectLoader().loadProjects();

        for (Iterator iter = list.iterator(); iter.hasNext();) {
            ProjectOld project = (ProjectOld) iter.next();
            LogDebug.log(project);
        }

        logger.log(Level.DEBUG, " ================= ");
        List results = migrator.getProjectTransformer().transformProjects(list);

        for (Iterator iter = results.iterator(); iter.hasNext();) {
            ProjectNew project = (ProjectNew) iter.next();
            LogDebug.log(project);
        }
        migrator.getProjectPersistence().storeProjects(results);
    }

    /**
     * Test method for 'migrate()'
     *
     * @throws Exception to JUnit
     */
    public void atestScorecardLoader() throws Exception {
        List list = migrator.getScorecardLoader().loadScorecardTemplate();

        for (Iterator iter = list.iterator(); iter.hasNext();) {
            ScorecardTemplate template = (ScorecardTemplate) iter.next();
            logger.log(Level.DEBUG, "template: " + template.getTemplateName() + " size: " + template.getGroups().size());

            Collection groups = template.getGroups();

            for (Iterator gIter = groups.iterator(); gIter.hasNext();) {
                ScSectionGroup group = (ScSectionGroup) gIter.next();
                logger.log(Level.DEBUG, "group: " + group.getGroupName() + " size: " + group.getSections().size());

                Collection sections = group.getSections();

                for (Iterator sIter = sections.iterator(); sIter.hasNext();) {
                    ScorecardSectionOld section = (ScorecardSectionOld) sIter.next();
                    logger.log(Level.DEBUG, "section: " + section.getSectionName() + " size: " + section.getQuestions().size());

                    Collection questions = section.getQuestions();

                    for (Iterator qIter = questions.iterator(); qIter.hasNext();) {
                        QuestionTemplate question = (QuestionTemplate) qIter.next();
                      //  logger.log(Level.DEBUG, "question: " + question.getQuestionText());
                    }
                }
            }
        }

        logger.log(Level.DEBUG, " ================= ");
        List results = migrator.getScorecardTransformer().transformScorecardTemplate(list);

        for (Iterator iter = results.iterator(); iter.hasNext();) {
        	Scorecard scorecard = (Scorecard) iter.next();
            logger.log(Level.DEBUG, "    scorecard: " + scorecard.getScorecardId() + " size: " + scorecard.getGroups().size());

            Collection groups = scorecard.getGroups();

            for (Iterator gIter = groups.iterator(); gIter.hasNext();) {
            	ScorecardGroup group = (ScorecardGroup) gIter.next();
                logger.log(Level.DEBUG, "       group: " + group.getScorecardGroupId() + " size: " + group.getSections().size());

                Collection sections = group.getSections();

                for (Iterator sIter = sections.iterator(); sIter.hasNext();) {
                	ScorecardSectionNew section = (ScorecardSectionNew) sIter.next();
                    logger.log(Level.DEBUG, "          section: " + section.getScorecardSectionId() + " size: " + section.getQuestions().size());

                    Collection questions = section.getQuestions();

                    for (Iterator qIter = questions.iterator(); qIter.hasNext();) {
                    	ScorecardQuestionNew question = (ScorecardQuestionNew) qIter.next();
                        logger.log(Level.DEBUG, "          question: " + question.getScorecardQuestionId());
                    }
                }
            }
        }

        migrator.getScorecardPersistence().storeScorecard(results);
    }

    public void atestTemplateId() throws Exception {
    	Set ids = new HashSet();
    	File file = new File("log", "q_template_id.log");
    	BufferedReader reader = new BufferedReader(new FileReader(file));
    	String line = null;
    	while((line = reader.readLine()) != null) {
    		if (ids.contains(line)) {
    			System.out.println("Exist, line: " + line);
    		} else {
    			ids.add(line);
    		}
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
     * Clears all the namespaces.
     *
     * @throws Exception to JUnit
     */
    private static void releaseNamespaces() throws Exception {
        ConfigManager config = ConfigManager.getInstance();

        for (int i = 0; i < NAMESPACES.length; i++) {
            if (config.existsNamespace(NAMESPACES[i])) {
                config.removeNamespace(NAMESPACES[i]);
            }
        }
    }
}
