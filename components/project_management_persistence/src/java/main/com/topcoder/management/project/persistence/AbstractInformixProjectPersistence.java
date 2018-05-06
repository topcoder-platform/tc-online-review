/*
 * Copyright (C) 2007 - 2012 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.project.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.management.project.ConfigurationException;
import com.topcoder.management.project.FileType;
import com.topcoder.management.project.PersistenceException;
import com.topcoder.management.project.Prize;
import com.topcoder.management.project.PrizeType;
import com.topcoder.management.project.Project;
import com.topcoder.management.project.ProjectCategory;
import com.topcoder.management.project.ProjectPersistence;
import com.topcoder.management.project.ProjectPropertyType;
import com.topcoder.management.project.ProjectStatus;
import com.topcoder.management.project.ProjectStudioSpecification;
import com.topcoder.management.project.ProjectType;
import com.topcoder.management.project.persistence.Helper.DataType;
import com.topcoder.management.project.persistence.logging.LogMessage;
import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.idgenerator.IDGenerationException;
import com.topcoder.util.idgenerator.IDGenerator;
import com.topcoder.util.idgenerator.IDGeneratorFactory;
import com.topcoder.util.log.Level;
import com.topcoder.util.log.Log;
import com.topcoder.util.sql.databaseabstraction.CustomResultSet;
import com.topcoder.util.sql.databaseabstraction.InvalidCursorStateException;
import com.topcoder.util.sql.databaseabstraction.NullColumnValueException;

/**
 * <p>
 * This class contains operations to create/update/retrieve project instances from the Informix database. It also
 * provides methods to retrieve database look up table values. It implements the ProjectPersistence interface to provide
 * a plug-in persistence for Informix database. It is used by the ProjectManagerImpl class. The constructor takes a
 * namespace parameter to initialize database connection.
 * </p>
 * <p>
 * Note that in this class, delete operation is not supported. To delete a project, its status is set to 'Deleted'.
 * Create and update operations here work on the project and its related items as well. It means creating/updating a
 * project would involve creating/updating its properties.
 * </p>
 * <p>
 * This abstract class does not manage the connection itself. It contains three abstract methods which should be
 * implemented by concrete subclass to manage the connection:
 * <ul>
 * <li><code>openConnection()</code> is used to acquire and open the connection.</li>
 * <li><code>closeConnection()</code> is used to dispose the connection if the queries are executed successfully.</li>
 * <li><code>closeConnectionOnError()</code> is used to handle the error if the SQL operation fails.</li>
 * </ul>
 * The transaction handling logic should be implemented in subclasses while the <code>Statement</code>s and
 * <code>ResultSet</code>s are handled in this abstract class.
 * </p>
 * <p>
 * Version 1.1.3 (End Of Project Analysis Release Assembly v1.0)
 * <ul>
 * <li>Updated {@link #getAllProjectTypes(Connection)} method and relevant constant strings to populate project type
 * entities with new <code>generic</code> property.</li>
 * <li>Updated {@link #getAllProjectCategories(Connection)} method and relevant constant strings to populate project
 * type entities with new <code>generic</code> property.</li>
 * </ul>
 * </p>
 * <p>
 * Updated in version 1.2. Add CRUD methods for following entities, it also maintain the relationships between project
 * and following entities.
 * <ul>
 * <li>FileType</li>
 * <li>Prize</li>
 * <li>ProjectStudioSpecification</li>
 * </ul>
 * It also adds a method to get projects by direct project id.
 * </p>
 * <p>
 * Sample Usage: Firstly, you need to set up the configuration according to the CS. Then, you can use the component as
 * the following codes shown:
 *
 * <pre>
 * // get the sample project object
 * Project project = TestHelper.getSampleProject1();
 *
 * persistence.createProject(project, &quot;user&quot;);
 *
 * long projectId = project.getId();
 *
 * // get projects by directProjectId
 * Project[] projects = persistence.getProjectsByDirectProjectId(1);
 *
 * FileType fileType = TestHelper.createFileType(&quot;description 1&quot;, &quot;extension 1&quot;, true, false, 1);
 *
 * // create FileType entity
 * fileType = persistence.createFileType(fileType, &quot;admin&quot;);
 *
 * // update FileType entity
 * fileType.setBundledFile(true);
 * fileType.setSort(2);
 * fileType.setExtension(&quot;extension3&quot;);
 * persistence.updateFileType(fileType, &quot;admin&quot;);
 *
 * // remove FileType entity
 * persistence.removeFileType(fileType, &quot;admin&quot;);
 *
 * fileType = TestHelper.createFileType(&quot;description 2&quot;, &quot;extension 2&quot;, true, false, 1);
 * persistence.createFileType(fileType, &quot;admin&quot;);
 *
 * // get all FileTypes
 * FileType[] fileTypes = persistence.getAllFileTypes();
 *
 * // get all FileTypes by project id
 * fileTypes = persistence.getProjectFileTypes(projectId);
 *
 * // update relationship for project and FileTypes
 * persistence.updateProjectFileTypes(1, Arrays.asList(fileTypes), &quot;admin&quot;);
 *
 * // get all PrizeTypes
 * PrizeType[] prizeTypes = persistence.getPrizeTypes();
 *
 * Prize prize = TestHelper.createPrize(1, 600.00, 5);
 *
 * // create Prize entity
 * prize = persistence.createPrize(prize, &quot;admin&quot;);
 *
 * // update Prize entity
 * prize.setPlace(2);
 * prize.setNumberOfSubmissions(10);
 * persistence.updatePrize(prize, &quot;admin&quot;);
 *
 * // remove Prize entity
 * persistence.removePrize(prize, &quot;admin&quot;);
 *
 * // get all Prizes by project id
 * Prize[] prizes = persistence.getProjectPrizes(projectId);
 *
 * // update relationship for project and Prizes
 * persistence.updateProjectPrizes(1, Arrays.asList(prizes), &quot;admin&quot;);
 *
 * ProjectStudioSpecification spec = TestHelper.
 * createProjectStudioSpecification(&quot;goal1&quot;, &quot;targetAudience&quot;,
 *     &quot;roundOneIntroduction1&quot;);
 * // create ProjectStudioSpecification entity
 * spec = persistence.createProjectStudioSpecification(spec, &quot;admin&quot;);
 *
 * // update ProjectStudioSpecification entity
 * spec.setSubmittersLockedBetweenRounds(false);
 * spec.setGoals(&quot;goal&quot;);
 * persistence.updateProjectStudioSpecification(spec, &quot;admin&quot;);
 *
 * // remove ProjectStudioSpecification entity
 * persistence.removeProjectStudioSpecification(spec, &quot;admin&quot;);
 *
 * // get all ProjectStudioSpecification by project id
 * spec = persistence.getProjectStudioSpecification(1);
 *
 * // update relationship for project and ProjectStudioSpecification
 * persistence.updateStudioSpecificationForProject(spec, 1, &quot;admin&quot;);
 * </pre>
 *
 * </p>
 * <p>
 * Thread Safety: This class is thread safe because it is immutable.
 * </p>
 *
 * <p>
 * Version 1.2.1 (Milestone Support Assembly 1.0) Change notes:
 *   <ol>
 *     <li>Replaced references to <code>prize_type_lu.description</code> column with reference to
 *     <code>prize_type_lu.prize_type_desc</code> column to match actual DB schema.</li>
 *     <li>Updated the logic to support optional file types, prizes and spec for project.</li>
 *     <li>Fixed the issue with connection leaking in {@link #getProjectStudioSpecification(long)} method.</li>

 *   </ol>
 * </p>
 *
 * <p>
 * Version 1.2.2 (Online Review Replatforming Release 2) Change notes:
 * <ol>
 *   <li>Updated {@link #getProjects(long[], Connection)} method to handle the
 *   situation that tc_direct_project_id is null.</li>
 * </ol>
 * </p>
 * 
 *
 * @author tuenm, urtks, bendlund, fuyun, flytoj2ee, VolodymyrK
 * @version 1.2.4
 * @since 1.0
 */
public abstract class AbstractInformixProjectPersistence implements ProjectPersistence {
    /**
     * <p>
     * Represents the default value for Project Id sequence name. It is used to create id generator for project. This
     * value will be overridden by 'ProjectIdSequenceName' configuration parameter if it exist.
     * </p>
     */
    public static final String PROJECT_ID_SEQUENCE_NAME = "project_id_seq";

    /**
     * <p>
     * Represents the default value for project audit id sequence name. It is used to create id generator for project
     * audit. This value will be overridden by 'ProjectAuditIdSequenceName' configuration parameter if it exist.
     * </p>
     */
    public static final String PROJECT_AUDIT_ID_SEQUENCE_NAME = "project_audit_id_seq";

    /**
     * <p>
     * Represents the default value for file type id sequence name. It is used to create id generator for file type.
     * This value will be overridden by 'FileTypeIdGeneratorSequenceName' configuration parameter if it exist.
     * </p>
     *
     * @since 1.2
     */
    private static final String FILE_TYPE_ID_SEQUENCE_NAME = "file_type_id_seq";

    /**
     * <p>
     * Represents the default value for prize id sequence name. It is used to create id generator for prize. This value
     * will be overridden by 'PrizeIdGeneratorSequenceName' configuration parameter if it exist.
     * </p>
     *
     * @since 1.2
     */
    private static final String PRIZE_ID_SEQUENCE_NAME = "prize_id_seq";

    /**
     * <p>
     * Represents the default value for studio spec id sequence name. It is used to create id generator for studio spec.
     * This value will be overridden by 'StudioSpecIdGeneratorSequenceName' configuration parameter if it exist.
     * </p>
     *
     * @since 1.2
     */
    private static final String STUDIO_SPEC_ID_SEQUENCE_NAME = "studio_spec_id_seq";

    /**
     * Represents the name of connection name parameter in configuration.
     */
    private static final String CONNECTION_NAME_PARAMETER = "ConnectionName";

    /**
     * Represents the name of connection factory namespace parameter in configuration.
     */
    private static final String CONNECTION_FACTORY_NAMESPACE_PARAMETER = "ConnectionFactoryNS";

    /**
     * Represents the name of project id sequence name parameter in configuration.
     */
    private static final String PROJECT_ID_SEQUENCE_NAME_PARAMETER = "ProjectIdSequenceName";

    /**
     * Represents the name of project audit id sequence name parameter in configuration.
     */
    private static final String PROJECT_AUDIT_ID_SEQUENCE_NAME_PARAMETER = "ProjectAuditIdSequenceName";

    /**
     * Represents the name of file type id sequence name parameter in configuration.
     *
     * @since 1.2
     */
    private static final String FILE_TYPE_ID_SEQUENCE_NAME_PARAMETER = "FileTypeIdGeneratorSequenceName";

    /**
     * Represents the name of prize id sequence name parameter in configuration.
     *
     * @since 1.2
     */
    private static final String PRIZE_ID_SEQUENCE_NAME_PARAMETER = "PrizeIdGeneratorSequenceName";

    /**
     * Represents the name of studio spec id sequence name parameter in configuration.
     *
     * @since 1.2
     */
    private static final String STUDIO_SPEC_ID_SEQUENCE_NAME_PARAMETER = "StudioSpecIdGeneratorSequenceName";
    /**
     * Represents the sql statement to query all project types.
     */
    private static final String QUERY_ALL_PROJECT_TYPES_SQL = "SELECT "
        + "project_type_id, name, description, is_generic FROM project_type_lu";

    /**
     * Represents the column types for the result set which is returned by executing the sql statement to query all
     * project types.
     */
    private static final DataType[] QUERY_ALL_PROJECT_TYPES_COLUMN_TYPES = new DataType[]{Helper.LONG_TYPE,
        Helper.STRING_TYPE, Helper.STRING_TYPE, Helper.BOOLEAN_TYPE};

    /**
     * Represents the sql statement to query all project categories.
     */
    private static final String QUERY_ALL_PROJECT_CATEGORIES_SQL = "SELECT "
        + "category.project_category_id, category.name, category.description, "
        + "type.project_type_id, type.name, type.description, type.is_generic "
        + "FROM project_category_lu AS category " + "JOIN project_type_lu AS type "
        + "ON category.project_type_id = type.project_type_id";

    /**
     * Represents the column types for the result set which is returned by executing the sql statement to query all
     * project categories.
     */
    private static final DataType[] QUERY_ALL_PROJECT_CATEGORIES_COLUMN_TYPES = new DataType[]{Helper.LONG_TYPE,
        Helper.STRING_TYPE, Helper.STRING_TYPE, Helper.LONG_TYPE, Helper.STRING_TYPE, Helper.STRING_TYPE,
        Helper.BOOLEAN_TYPE};

    /**
     * Represents the sql statement to query all project statuses.
     */
    private static final String QUERY_ALL_PROJECT_STATUSES_SQL = "SELECT "
        + "project_status_id, name, description FROM project_status_lu";

    /**
     * Represents the column types for the result set which is returned by executing the sql statement to query all
     * project statuses.
     */
    private static final DataType[] QUERY_ALL_PROJECT_STATUSES_COLUMN_TYPES = new DataType[]{Helper.LONG_TYPE,
        Helper.STRING_TYPE, Helper.STRING_TYPE};

    /**
     * Represents the sql statement to query all project property types.
     */
    private static final String QUERY_ALL_PROJECT_PROPERTY_TYPES_SQL = "SELECT "
        + "project_info_type_id, name, description FROM project_info_type_lu";

    /**
     * Represents the column types for the result set which is returned by executing the sql statement to query all
     * project property types.
     */
    private static final DataType[] QUERY_ALL_PROJECT_PROPERTY_TYPES_COLUMN_TYPES = new DataType[]{Helper.LONG_TYPE,
        Helper.STRING_TYPE, Helper.STRING_TYPE};

    /**
     * Represents the sql statement to query projects.
     */
    private static final String QUERY_PROJECTS_SQL = "SELECT "
        + "project.project_id, status.project_status_id, status.name, "
        + "category.project_category_id, category.name, type.project_type_id, type.name, "
        + "project.create_user, project.create_date, project.modify_user, project.modify_date, category.description,"
        + " project.tc_direct_project_id, tcdp.name as tc_direct_project_name "
        + "FROM project JOIN project_status_lu AS status ON project.project_status_id=status.project_status_id "
        + "JOIN project_category_lu AS category ON project.project_category_id=category.project_category_id "
        + "JOIN project_type_lu AS type ON category.project_type_id=type.project_type_id "
        + "LEFT OUTER JOIN tc_direct_project AS tcdp ON tcdp.project_id=project.tc_direct_project_id "
        + "WHERE project.project_id IN ";

    /**
     * Represents the column types for the result set which is returned by executing the sql statement to query
     * projects.
     */
    private static final DataType[] QUERY_PROJECTS_COLUMN_TYPES = new DataType[]{Helper.LONG_TYPE, Helper.LONG_TYPE,
        Helper.STRING_TYPE, Helper.LONG_TYPE, Helper.STRING_TYPE, Helper.LONG_TYPE, Helper.STRING_TYPE,
        Helper.STRING_TYPE, Helper.DATE_TYPE, Helper.STRING_TYPE, Helper.DATE_TYPE,
        Helper.STRING_TYPE, Helper.LONG_TYPE, Helper.STRING_TYPE};

    /**
     * Represents the sql statement to query project properties.
     */
    private static final String QUERY_PROJECT_PROPERTIES_SQL = "SELECT "
        + "info.project_id, info_type.name, info.value " + "FROM project_info AS info "
        + "JOIN project_info_type_lu AS info_type " + "ON info.project_info_type_id=info_type.project_info_type_id "
        + "WHERE info.project_id IN ";

    /**
     * Represents the sql statement to query project properties.
     */
    private static final String QUERY_ONE_PROJECT_PROPERTIES_SQL = "SELECT "
        + "info.project_id, info_type.name, info.value " + "FROM project_info AS info "
        + "JOIN project_info_type_lu AS info_type " + "ON info.project_info_type_id=info_type.project_info_type_id "
        + "WHERE info.project_id = ?";

    /**
     * Represents the column types for the result set which is returned by executing the sql statement to query project
     * properties.
     */
    private static final DataType[] QUERY_PROJECT_PROPERTIES_COLUMN_TYPES = new DataType[]{Helper.LONG_TYPE,
        Helper.STRING_TYPE, Helper.STRING_TYPE};

    /**
     * Represents the sql statement to query project property ids.
     */
    private static final String QUERY_PROJECT_PROPERTY_IDS_SQL = "SELECT "
        + "project_info_type_id FROM project_info WHERE project_id=?";

    /**
     * Represents the sql statement to query project property ids and values.
     */
    private static final String QUERY_PROJECT_PROPERTY_IDS_AND_VALUES_SQL = "SELECT "
        + "project_info_type_id, value FROM project_info WHERE project_id=?";

    /**
     * Represents the column types for the result set which is returned by executing the sql statement to query project
     * property ids.
     */
    private static final DataType[] QUERY_PROJECT_PROPERTY_IDS_COLUMN_TYPES = new DataType[]{Helper.LONG_TYPE};

    /**
     * Represents the column types for the result set which is returned by executing the sql statement to query project
     * property ids and values.
     */
    private static final DataType[] QUERY_PROJECT_PROPERTY_IDS_AND_VALUES_COLUMN_TYPES = new DataType[]{
        Helper.LONG_TYPE, Helper.STRING_TYPE};

    /**
     * Represents the sql statement to create project.
     */
    private static final String CREATE_PROJECT_SQL = "INSERT INTO project "
        + "(project_id, project_status_id, project_category_id, "
        + "create_user, create_date, modify_user, modify_date, tc_direct_project_id) "
        + "VALUES (?, ?, ?, ?, CURRENT, ?, CURRENT, ?)";

    /**
     * Represents the sql statement to create project property.
     */
    private static final String CREATE_PROJECT_PROPERTY_SQL = "INSERT INTO project_info "
        + "(project_id, project_info_type_id, value, " + "create_user, create_date, modify_user, modify_date) "
        + "VALUES (?, ?, ?, ?, CURRENT, ?, CURRENT)";

    /**
     * Represents the sql statement to create project audit.
     */
    private static final String CREATE_PROJECT_AUDIT_SQL = "INSERT INTO project_audit "
        + "(project_audit_id, project_id, update_reason, " + "create_user, create_date, modify_user, modify_date) "
        + "VALUES (?, ?, ?, ?, CURRENT, ?, CURRENT)";

    /**
     * Represents the sql statement to update project.
     */
    private static final String UPDATE_PROJECT_SQL = "UPDATE project "
        + "SET project_status_id=?, project_category_id=?, modify_user=?, modify_date=?, tc_direct_project_id=? "
        + "WHERE project_id=?";

    /**
     * Represents the sql statement to update project property.
     */
    private static final String UPDATE_PROJECT_PROPERTY_SQL = "UPDATE project_info "
        + "SET value=?, modify_user=?, modify_date=CURRENT " + "WHERE project_id=? AND project_info_type_id=?";

    /**
     * Represents the sql statement to delete project properties.
     */
    private static final String DELETE_PROJECT_PROPERTIES_SQL = "DELETE FROM project_info "
        + "WHERE project_id=? AND project_info_type_id IN ";

    /**
     * <p>
     * Represents the audit creation type.
     * </p>
     *
     * @since 1.1.2
     */
    private static final int AUDIT_CREATE_TYPE = 1;

    /**
     * <p>
     * Represents the audit deletion type.
     * </p>
     *
     * @since 1.1.2
     */
    private static final int AUDIT_DELETE_TYPE = 2;

    /**
     * <p>
     * Represents the audit update type.
     * </p>
     *
     * @since 1.1.2
     */
    private static final int AUDIT_UPDATE_TYPE = 3;

    /**
     * Represents the SQL statement to audit project info.
     *
     * @since 1.1.2
     */
    private static final String PROJECT_INFO_AUDIT_INSERT_SQL = "INSERT INTO project_info_audit "
        + "(project_id, project_info_type_id, value, audit_action_type_id, action_date, action_user_id) "
        + "VALUES (?, ?, ?, ?, ?, ?)";

    /**
     * Represents the sql statement to query file types.
     *
     * @since 1.2
     */
    private static final String QUERY_FILE_TYPES_SQL = "SELECT "
        + "type.file_type_id, type.description, type.sort, type.image_file," + " type.extension, type.bundled_file "
        + "FROM file_type_lu AS type " + "JOIN project_file_type_xref AS xref "
        + "ON type.file_type_id=xref.file_type_id " + "WHERE xref.project_id=";

    /**
     * Represents the column types for the result set which is returned by executing the sql statement to query file
     * types.
     *
     * @since 1.2
     */
    private static final DataType[] QUERY_FILE_TYPES_COLUMN_TYPES = {Helper.LONG_TYPE, Helper.STRING_TYPE,
        Helper.LONG_TYPE, Helper.BOOLEAN_TYPE, Helper.STRING_TYPE, Helper.BOOLEAN_TYPE};

    /**
     * Represents the sql statement to query prizes.
     *
     * @since 1.2
     */
    private static final String QUERY_PRIZES_SQL = "SELECT "
        + "prize.prize_id, prize.place, prize.prize_amount, prize.number_of_submissions, "
        + "prize_type.prize_type_id, prize_type.prize_type_desc " + "FROM prize AS prize "
        + "JOIN prize_type_lu AS prize_type ON prize.prize_type_id=prize_type.prize_type_id "
        + "WHERE prize.project_id=";

    /**
     * Represents the column types for the result set which is returned by executing the sql statement to query prizes.
     *
     * @since 1.2
     */
    private static final DataType[] QUERY_PRIZES_COLUMN_TYPES = {Helper.LONG_TYPE, Helper.LONG_TYPE,
        Helper.Double_TYPE, Helper.LONG_TYPE, Helper.LONG_TYPE, Helper.STRING_TYPE};

    /**
     * Represents the sql statement to insert prize to the prize table.
     *
     * @since 1.2
     */
    private static final String CREATE_PRIZE_SQL = "INSERT INTO prize "
        + "(prize_id, project_id, place, prize_amount, prize_type_id, number_of_submissions, "
        + "create_user, create_date, modify_user, modify_date) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    /**
     * Represents the sql statement to update prize to the prize table.
     *
     * @since 1.2
     */
    private static final String UPDATE_PRIZE_SQL = "UPDATE prize "
        + "SET place=?, prize_amount=?, prize_type_id=?, number_of_submissions=?, modify_user=?, modify_date=?, "
        + "project_id=? WHERE prize_id=";

    /**
     * Represents the sql statement to delete the prize by the specified prize id.
     *
     * @since 1.2
     */
    private static final String DELETE_PRIZE_SQL = "DELETE FROM prize WHERE prize_id=?";
    /**
     * Represents the sql statement to query prize types from the prize_type_lu table.
     *
     * @since 1.2
     */
    private static final String QUERY_ALL_PRIZE_TYPES_SQL = "SELECT prize_type_id, prize_type_desc FROM prize_type_lu";
    /**
     * Represents the column types for the result set which is returned by querying prize types from the prize_type_lu
     * table.
     *
     * @since 1.2
     */
    private static final DataType[] QUERY_ALL_PRIZE_TYPES_COLUMN_TYPES = {Helper.LONG_TYPE, Helper.STRING_TYPE};
    /**
     * Represents the sql statement to query file types from the file_type_lu table.
     *
     * @since 1.2
     */
    private static final String QUERY_ALL_FILE_TYPES_SQL = "SELECT file_type_id, description, sort, image_file, "
        + "extension, bundled_file FROM file_type_lu";
    /**
     * Represents the column types for the result set which is returned by querying file types from the file_type_lu
     * table.
     *
     * @since 1.2
     */
    private static final DataType[] QUERY_ALL_FILE_TYPES_COLUMN_TYPES = {Helper.LONG_TYPE, Helper.STRING_TYPE,
        Helper.LONG_TYPE, Helper.BOOLEAN_TYPE, Helper.STRING_TYPE, Helper.BOOLEAN_TYPE};

    /**
     * Represents the sql statement to delete the project file types reference by the specified file type id.
     *
     * @since 1.2
     */
    private static final String DELETE_PROJECT_FILE_TYPE_XREF_SQL = "DELETE FROM project_file_type_xref "
        + "WHERE file_type_id=?";
    /**
     * Represents the sql statement to delete the file types by the specified file type id.
     *
     * @since 1.2
     */
    private static final String DELETE_FILE_TYPE_SQL = "DELETE FROM file_type_lu WHERE file_type_id=?";
    /**
     * Represents the sql statement to update the file types by the specified file type id.
     *
     * @since 1.2
     */
    private static final String UPDATE_FILE_TYPE_SQL = "UPDATE file_type_lu "
        + "SET description=?, sort=?, image_file=?, extension=?, bundled_file=?, modify_user=?, modify_date=? "
        + "WHERE file_type_id=";
    /**
     * Represents the sql statement to create the file type.
     *
     * @since 1.2
     */
    private static final String CREATE_FILE_TYPE_SQL = "INSERT INTO file_type_lu "
        + "(file_type_id, description, sort, image_file, extension, bundled_file, "
        + "create_user, create_date, modify_user, modify_date) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    /**
     * Represents the sql statement to insert the project file types reference with the provided project id.
     *
     * @since 1.2
     */
    private static final String DELETE_PROJECT_FILE_TYPES_XREF__WITH_PROJECT_ID_SQL
        = "DELETE FROM project_file_type_xref " + "WHERE project_id=?";
    /**
     * Represents the sql statement to insert the project file types reference data.
     *
     * @since 1.2
     */
    private static final String INSERT_PROJECT_FILE_TYPES_XREF_SQL
        = "INSERT INTO project_file_type_xref (project_id, file_type_id) VALUES (?, ?)";

    /**
     * Represents the sql statement to create studio specification data.
     *
     * @since 1.2
     */
    private static final String CREATE_STUDIO_SPEC_SQL
        = "INSERT INTO project_studio_specification (project_studio_spec_id, "
            + "goals, target_audience, branding_guidelines, disliked_design_websites, other_instructions, "
            + "winning_criteria, submitters_locked_between_rounds, round_one_introduction, round_two_introduction, "
            + "colors, fonts, layout_and_size, create_user, create_date, modify_user, modify_date)"
            + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    /**
     * Represents the sql statement to update studio specification data.
     *
     * @since 1.2
     */
    private static final String UPDATE_STUDIO_SPEC_SQL = "UPDATE project_studio_specification "
        + "SET goals=?, target_audience=?, branding_guidelines=?, disliked_design_websites=?, "
        + "other_instructions=?, winning_criteria=?, submitters_locked_between_rounds=?, "
        + "round_one_introduction=?, round_two_introduction=?, colors=?, fonts=?, "
        + "layout_and_size=?, modify_user=?, modify_date=? " + "WHERE project_studio_spec_id=";

    /**
     * Represents the sql statement to delete studio specification data with the specified project studio specification
     * id.
     *
     * @since 1.2
     */
    private static final String DELETE_STUDIO_SPEC_SQL = "DELETE FROM project_studio_specification "
        + "WHERE project_studio_spec_id=?";

    /**
     * Represents the sql statement to set studio specification id to null for project table with the specified project
     * studio specification id.
     *
     * @since 1.2
     */
    private static final String SET_PROJECT_STUDIO_SPEC_SQL = "UPDATE project SET project_studio_spec_id="
        + "NULL WHERE project_studio_spec_id=?";
    /**
     * Represents the sql statement to query studio specification data with the specified project id.
     *
     * @since 1.2
     */
    private static final String QUERY_STUDIO_SPEC_SQL = "SELECT "
        + "spec.project_studio_spec_id, spec.goals, spec.target_audience, "
        + "spec.branding_guidelines, spec.disliked_design_websites, spec.other_instructions, "
        + "spec.winning_criteria, spec.submitters_locked_between_rounds, "
        + "spec.round_one_introduction, spec.round_two_introduction, spec.colors, "
        + "spec.fonts, spec.layout_and_size " + "FROM project_studio_specification AS spec JOIN project AS project "
        + "ON project.project_studio_spec_id=spec.project_studio_spec_id " + "WHERE project.project_id=";
    /**
     * Represents the data types for the result set by querying studio specification data with the specified project id.
     *
     * @since 1.2
     */
    private static final DataType[] QUERY_STUDIO_SPEC_COLUMN_TYPES = new DataType[]{Helper.LONG_TYPE,
        Helper.STRING_TYPE, Helper.STRING_TYPE, Helper.STRING_TYPE, Helper.STRING_TYPE, Helper.STRING_TYPE,
        Helper.STRING_TYPE, Helper.BOOLEAN_TYPE, Helper.STRING_TYPE, Helper.STRING_TYPE, Helper.STRING_TYPE,
        Helper.STRING_TYPE, Helper.STRING_TYPE};

    /**
     * Represents the sql statement to set studio specification id for project table with the specified project id.
     *
     * @since 1.2
     */
    private static final String SET_PROJECT_STUDIO_SPEC_WITH_PROJECT_SQL = "UPDATE project SET project_studio_spec_id="
        + "? WHERE project.project_id=";

    /**
     * Represents the sql statement to query project id for project table with the specified tc direct project id.
     *
     * @since 1.2
     */
    private static final String QUERY_PROJECT_IDS_SQL
        = "SELECT DISTINCT project_id FROM project WHERE tc_direct_project_id=";

    /**
     * Represents the data types for the result set by querying project id for project table.
     *
     * @since 1.2
     */
    private static final DataType[] QUERY_PROJECT_IDS__COLUMN_TYPES = new DataType[]{Helper.LONG_TYPE};

    /**
     * Represents the sql statement to query project id for project table with the specified studio specification id.
     *
     * @since 1.2
     */
    private static final String QUERY_PROJECT_IDS_WITH_STUDIO_SPEC_SQL
        = "SELECT DISTINCT project_id FROM project WHERE project_studio_spec_id=";

    /**
     * Represents the sql statement to query project id for project table with the specified file type id.
     *
     * @since 1.2
     */
    private static final String QUERY_PROJECT_IDS_WITH_FILE_TYPE_SQL
        = "SELECT DISTINCT project_id FROM project_file_type_xref WHERE file_type_id=";
    /**
     * Represents the sql statement to query project id for project table with the specified prize id.
     *
     * @since 1.2
     */
    private static final String QUERY_PROJECT_IDS_WITH_PRIZE_SQL
        = "SELECT project_id FROM prize WHERE prize_id=";

    /**
     * <p>
     * The factory instance used to create connection to the database. It is initialized in the constructor using
     * DBConnectionFactory component and never changed after that. It will be used in various persistence methods of
     * this project.
     * </p>
     */
    private final DBConnectionFactory factory;

    /**
     * <p>
     * Represents the database connection name that will be used by DBConnectionFactory. This variable is initialized in
     * the constructor and never changed after that. It will be used in create/update/retrieve method to create
     * connection. This variable can be null, which mean connection name is not defined in the configuration namespace
     * and default connection will be created.
     * </p>
     */
    private final String connectionName;

    /**
     * <p>
     * Represents the IDGenerator for project table. This variable is initialized in the constructor and never change
     * after that. It will be used in createProject() method to generate new Id for project..
     * </p>
     */
    private final IDGenerator projectIdGenerator;

    /**
     * <p>
     * Represents the IDGenerator for project audit table. This variable is initialized in the constructor and never
     * change after that. It will be used in updateProject() method to store update reason.
     * </p>
     */
    private final IDGenerator projectAuditIdGenerator;

    /**
     * <p>
     * Represents the IDGenerator for file_type table. This variable is initialized in the constructor and never change
     * after that. It will be used in createFileType() method to generate new Id for project. It could not be null after
     * initialized.
     * </p>
     *
     * @since 1.2
     */
    private final IDGenerator fileTypeIdGenerator;
    /**
     * <p>
     * Represents the IDGenerator for prize table. This variable is initialized in the constructor and never change
     * after that. It will be used in createPrize() method to generate new Id for project. It could not be null after
     * initialized.
     * </p>
     *
     * @since 1.2
     */
    private final IDGenerator prizeIdGenerator;
    /**
     * <p>
     * Represents the IDGenerator for project_studio_specification table. This variable is initialized in the
     * constructor and never change after that. It will be used in createProjectStudioSpecification() method to generate
     * new Id for project. It could not be null after initialized.
     * </p>
     *
     * @since 1.2
     */
    private final IDGenerator studioSpecIdGenerator;

    /**
     * <p>
     * Creates a new instance of <code>AbstractProjectPersistence</code> from the settings in configuration.
     * </p>
     * <p>
     * The following parameters are configured.
     * <ul>
     * <li>ConnectionFactoryNS: The namespace that contains settings for DB Connection Factory. It is required.</li>
     * <li>ConnectionName: The name of the connection that will be used by DBConnectionFactory to create connection. If
     * missing, default connection will be created. It is optional.</li>
     * <li>ProjectIdSequenceName: The sequence name used to create Id generator for project Id. If missing default value
     * (project_id_seq) is used. It is optional.</li>
     * <li>ProjectAuditIdSequenceName: The sequence name used to create Id generator for project audit Id. If missing,
     * default value (project_audit_id_seq) is used. It is optional.</li>
     * </ul>
     * </p>
     * <p>
     * Configuration sample:
     *
     * <pre>
     * &lt;Config name="InformixProjectPersistence.CustomNamespace"&gt;
     *     &lt;!-- The DBConnectionFactory class name used to create DB Connection Factory, required --&gt;
     *     &lt;Property name="ConnectionFactoryNS"&gt;
     *         &lt;Value&gt;com.topcoder.db.connectionfactory.DBConnectionFactoryImpl&lt;/Value&gt;
     *     &lt;/Property&gt;
     *     &lt;!-- the connection name to retrieve connection in DB Connection Factory, required --&gt;
     *     &lt;Property name="ConnectionName"&gt;
     *         &lt;Value&gt;informix_connection&lt;/Value&gt;
     *     &lt;/Property&gt;
     *     &lt;Property name="ProjectIdSequenceName"&gt;
     *         &lt;Value&gt;project_id_seq&lt;/Value&gt;
     *     &lt;/Property&gt;
     *     &lt;Property name="ProjectAuditIdSequenceName"&gt;
     *         &lt;Value&gt;project_audit_id_seq&lt;/Value&gt;
     *     &lt;/Property&gt;
     *     &lt;Property name="FileTypeIdGeneratorSequenceName"&gt;
     *         &lt;Value&gt;file_type_id_seq&lt;/Value&gt;
     *     &lt;/Property&gt;
     *     &lt;Property name="PrizeIdGeneratorSequenceName"&gt;
     *         &lt;Value&gt;prize_id_seq&lt;/Value&gt;
     *     &lt;/Property&gt;
     *     &lt;Property name="StudioSpecIdGeneratorSequenceName"&gt;
     *         &lt;Value&gt;studio_spec_id_seq&lt;/Value&gt;
     *     &lt;/Property&gt;
     * &lt;/Config&gt;
     * </pre>
     *
     * </p>
     * <p>
     * Updated in version 1.2: get sequence name and create following IdGenerators.
     * <ul>
     * <li>fileTypeIdGenerator</li>
     * <li>prizeIdGenerator</li>
     * <li>studioSpecIdGenerator</li>
     * </ul>
     * </p>
     *
     * @param namespace The namespace to load configuration setting.
     * @throws IllegalArgumentException if the input is null or empty string.
     * @throws ConfigurationException if error occurs while loading configuration settings, or required configuration
     *             parameter is missing.
     * @throws PersistenceException if cannot initialize the connection to the database.
     *
     * @since 1.0
     */
    @SuppressWarnings("deprecation")
    protected AbstractInformixProjectPersistence(String namespace) throws ConfigurationException, PersistenceException {
        Helper.assertStringNotNullNorEmpty(namespace, "namespace");

        // get the instance of ConfigManager
        ConfigManager cm = ConfigManager.getInstance();

        // get db connection factory namespace
        String factoryNamespace = Helper.getConfigurationParameterValue(cm, namespace,
            CONNECTION_FACTORY_NAMESPACE_PARAMETER, true, getLogger());

        // try to create a DBConnectionFactoryImpl instance from
        // factoryNamespace
        try {
            factory = new DBConnectionFactoryImpl(factoryNamespace);
        } catch (Exception e) {
            throw new ConfigurationException("Unable to create a new instance of DBConnectionFactoryImpl class"
                + " from namespace [" + factoryNamespace + "].", e);
        }

        // get the connection name
        connectionName = Helper.getConfigurationParameterValue(cm, namespace, CONNECTION_NAME_PARAMETER, false,
            getLogger());

        // try to get project id sequence name
        String projectIdSequenceName = Helper.getConfigurationParameterValue(cm, namespace,
            PROJECT_ID_SEQUENCE_NAME_PARAMETER, false, getLogger());
        // use default name if project id sequence name is not provided
        if (projectIdSequenceName == null) {
            projectIdSequenceName = PROJECT_ID_SEQUENCE_NAME;
        }

        // try to get project audit id sequence name
        String projectAuditIdSequenceName = Helper.getConfigurationParameterValue(cm, namespace,
            PROJECT_AUDIT_ID_SEQUENCE_NAME_PARAMETER, false, getLogger());
        // use default name if project audit id sequence name is not provided
        if (projectAuditIdSequenceName == null) {
            projectAuditIdSequenceName = PROJECT_AUDIT_ID_SEQUENCE_NAME;
        }

        // try to get file type id sequence name
        String fileTypeIdGeneratorSequenceName = Helper.getConfigurationParameterValue(cm, namespace,
            FILE_TYPE_ID_SEQUENCE_NAME_PARAMETER, false, getLogger());
        // use default name if file type id sequence name is not provided
        if (fileTypeIdGeneratorSequenceName == null) {
            fileTypeIdGeneratorSequenceName = FILE_TYPE_ID_SEQUENCE_NAME;
        }

        // try to get prize id sequence name
        String prizeIdGeneratorSequenceName = Helper.getConfigurationParameterValue(cm, namespace,
            PRIZE_ID_SEQUENCE_NAME_PARAMETER, false, getLogger());
        // use default name if prize id sequence name is not provided
        if (prizeIdGeneratorSequenceName == null) {
            prizeIdGeneratorSequenceName = PRIZE_ID_SEQUENCE_NAME;
        }

        // try to get studio spec id sequence name
        String studioSpecIdGeneratorSequenceName = Helper.getConfigurationParameterValue(cm, namespace,
            STUDIO_SPEC_ID_SEQUENCE_NAME_PARAMETER, false, getLogger());
        // use default name if studio spec id sequence name is not provided
        if (studioSpecIdGeneratorSequenceName == null) {
            studioSpecIdGeneratorSequenceName = STUDIO_SPEC_ID_SEQUENCE_NAME;
        }
        // try to get the IDGenerators
        try {
            projectIdGenerator = IDGeneratorFactory.getIDGenerator(projectIdSequenceName);
        } catch (IDGenerationException e) {
            getLogger().log(Level.ERROR, "The projectIdSequence [" + projectIdSequenceName + "] is invalid.");
            throw new PersistenceException("Unable to create IDGenerator for '" + projectIdSequenceName + "'.", e);
        }
        try {
            projectAuditIdGenerator = IDGeneratorFactory.getIDGenerator(projectAuditIdSequenceName);
        } catch (IDGenerationException e) {
            getLogger().log(Level.ERROR, "The projectAuditIdSequence [" + projectAuditIdSequenceName + "] is invalid.");
            throw new PersistenceException("Unable to create IDGenerator for '" + projectAuditIdSequenceName + "'.", e);
        }
        try {
            fileTypeIdGenerator = IDGeneratorFactory.getIDGenerator(fileTypeIdGeneratorSequenceName);
        } catch (IDGenerationException e) {
            getLogger().log(Level.ERROR,
                "The fileTypeIdGeneratorSequence [" + fileTypeIdGeneratorSequenceName + "] is invalid.");
            throw new PersistenceException("Unable to create IDGenerator for '" + fileTypeIdGeneratorSequenceName
                + "'.", e);
        }
        try {
            prizeIdGenerator = IDGeneratorFactory.getIDGenerator(prizeIdGeneratorSequenceName);
        } catch (IDGenerationException e) {
            getLogger().log(Level.ERROR,
                "The prizeIdGeneratorSequence [" + prizeIdGeneratorSequenceName + "] is invalid.");
            throw new PersistenceException("Unable to create IDGenerator for '" + prizeIdGeneratorSequenceName + "'.",
                e);
        }
        try {
            studioSpecIdGenerator = IDGeneratorFactory.getIDGenerator(studioSpecIdGeneratorSequenceName);
        } catch (IDGenerationException e) {
            getLogger().log(Level.ERROR,
                "The studioSpecIdGeneratorSequence [" + studioSpecIdGeneratorSequenceName + "] is invalid.");
            throw new PersistenceException("Unable to create IDGenerator for '" + studioSpecIdGeneratorSequenceName
                + "'.", e);
        }
    }

    /**
     * <p>
     * Creates the project in the database using the given project instance.
     * </p>
     * <p>
     * The project information is stored to 'project' table, while its properties are stored in 'project_info' table.
     * The project's associating scorecards are stored in 'project_scorecard' table. For the project, its properties
     * and associating scorecards, the operator parameter is used as the creation/modification user and the creation
     * date and modification date will be the current date time when the project is created.
     * </p>
     *
     * <p>
     * Updated in version 1.2 (TC - Studio Replatforming Project): get sequence name and create following IdGenerators.
     * <ul>
     * <li>fileTypeIdGenerator</li>
     * <li>prizeIdGenerator</li>
     * <li>studioSpecIdGenerator</li>
     * </ul>
     * </p>
     *
     * @param project The project instance to be created in the database.
     * @param operator The creation user of this project.
     * @throws IllegalArgumentException if any input is null or the operator is empty string.
     * @throws PersistenceException if error occurred while accessing the database.
     *
     * @since 1.0
     */
    public void createProject(Project project, String operator) throws PersistenceException {
        Helper.assertObjectNotNull(project, "project");
        Helper.assertStringNotNullNorEmpty(operator, "operator");

        Connection conn = null;

        // newId will contain the new generated Id for the project
        Long newId;

        getLogger().log(Level.DEBUG,
            new LogMessage(null, operator, "creating new project: " + project.getAllProperties()));

        try {
            // create the connection
            conn = openConnection();

            // check whether the project id is already in the database
            if (project.getId() > 0) {
                if (Helper.checkEntityExists("project", "project_id", project.getId(), conn)) {
                    throw new PersistenceException("The project with the same id [" + project.getId()
                        + "] already exists.");
                }
            }

            try {
                // generate id for the project
                newId = projectIdGenerator.getNextID();
                getLogger().log(Level.DEBUG, new LogMessage(newId, operator, "generate id for new project"));
            } catch (IDGenerationException e) {
                throw new PersistenceException("Unable to generate id for the project.", e);
            }

            // create the project
            createProject(newId, project, operator, conn);

            // set the file types
            createOrUpdateProjectFileTypes(newId, project.getProjectFileTypes(), conn, operator, false);

            // set the prizes
            createOrUpdateProjectPrizes(newId, project.getPrizes(), conn, operator, false);
            // set the project studio specification
            createOrUpdateProjectStudioSpecification(newId, project.getProjectStudioSpecification(), conn, operator);

            closeConnection(conn);
        } catch (PersistenceException e) {
            getLogger().log(Level.ERROR,
                new LogMessage(null, operator, "Fails to create project " + project.getAllProperties(), e));
            if (conn != null) {
                closeConnectionOnError(conn);
            }
            throw e;
        }

        // set the newId when no exception occurred
        project.setId(newId);
    }

    /**
     * <p>
     * Update the given project instance into the database.
     * </p>
     * <p>
     * The project information is stored to 'project' table, while its properties are stored in 'project_info' table.
     * The project's associating scorecards are stored in 'project_scorecard' table. All related items in these tables
     * will be updated. If items are removed from the project, they will be deleted from the persistence. Likewise, if
     * new items are added, they will be created in the persistence. For the project, its properties and associating
     * scorecards, the operator parameter is used as the modification user and the modification date will be the
     * current date time when the project is updated. An update reason is provided with this method. Update reason
     * will be stored in the persistence for future references.
     * </p>
     *
     * <p>
     * Updated in version 1.2 (TC - Studio Replatforming Project): It needs to maintain the relationships
     * between project and following entities.
     * <ul>
     * <li>FileType</li>
     * <li>Prize</li>
     * <li>ProjectStudioSpecification</li>
     * </ul>
     * </p>
     *
     * @param project The project instance to be updated into the database.
     * @param reason The update reason. It will be stored in the persistence for future references.
     * @param operator The modification user of this project.
     * @throws IllegalArgumentException if any input is null or the operator is empty string. Or project id is zero.
     * @throws PersistenceException if error occurred while accessing the database.
     *
     * @since 1.0
     */
    public void updateProject(Project project, String reason, String operator) throws PersistenceException {
        Helper.assertObjectNotNull(project, "project");
        Helper.assertLongPositive(project.getId(), "project id");
        Helper.assertObjectNotNull(reason, "reason");
        Helper.assertStringNotNullNorEmpty(operator, "operator");

        // modifyDate will contain the modify_date retrieved from database.
        Date modifyDate;

        Connection conn = null;

        getLogger().log(Level.DEBUG,
            new LogMessage(project.getId(), operator, "updating project: " + project.getAllProperties()));
        try {
            // create the connection
            conn = openConnection();

            // check whether the project id is already in the database
            if (!Helper.checkEntityExists("project", "project_id", project.getId(), conn)) {
                throw new PersistenceException("The project id [" + project.getId()
                    + "] does not exist in the database.");
            }

            // update the project
            updateProject(project, reason, operator, conn);

            getLogger().log(
                Level.DEBUG,
                new LogMessage(project.getId(), operator, "execute sql:" + "SELECT modify_date "
                    + "FROM project WHERE project_id=?"));
            // get the modification date.
            modifyDate = (Date) Helper.doSingleValueQuery(conn, "SELECT modify_date "
                + "FROM project WHERE project_id=?", new Object[]{project.getId()}, Helper.DATE_TYPE);

            // set the file types
            createOrUpdateProjectFileTypes(project.getId(), project.getProjectFileTypes(), conn, operator, true);

            // set the prizes
            createOrUpdateProjectPrizes(project.getId(), project.getPrizes(), conn, operator, true);

            // set the project studio specification
            createOrUpdateProjectStudioSpecification(project.getId(), project.getProjectStudioSpecification(), conn,
                operator);

            closeConnection(conn);
        } catch (PersistenceException e) {
            getLogger().log(Level.ERROR,
                new LogMessage(null, operator, "Fails to update project " + project.getAllProperties(), e));
            if (conn != null) {
                closeConnectionOnError(conn);
            }
            throw e;
        }

        // set the modification user and date when no exception
        // occurred.
        project.setModificationUser(operator);
        project.setModificationTimestamp(modifyDate);
    }

    /**
     * Retrieves the project instance from the persistence given its id. The project instance is retrieved with its
     * related items, such as properties and scorecards.
     *
     * @return The project instance.
     * @param id The id of the project to be retrieved.
     * @throws IllegalArgumentException if the input id is less than or equal to zero.
     * @throws PersistenceException if error occurred while accessing the database.
     */
    public Project getProject(long id) throws PersistenceException {
        Helper.assertLongPositive(id, "id");

        Project[] projects = getProjects(new long[]{id});
        return projects.length == 0 ? null : projects[0];
    }

    /**
     * <p>
     * Retrieves an array of project instance from the persistence given their ids. The project instances are
     * retrieved with their properties.
     * </p>
     *
     * @param ids ids The ids of the projects to be retrieved.
     * @return An array of project instances.
     * @throws IllegalArgumentException if the input id is less than or equal to zero.
     * @throws PersistenceException if error occurred while accessing the database.
     */
    public Project[] getProjects(long[] ids) throws PersistenceException {
        Helper.assertObjectNotNull(ids, "ids");

        // check if ids is empty
        if (ids.length == 0) {
            throw new IllegalArgumentException("Array 'ids' should not be empty.");
        }

        String idstring = "";
        // check if ids contains an id that is <= 0
        for (long id : ids) {
            idstring += id + ",";
            if (id <= 0) {
                throw new IllegalArgumentException("Array 'ids' contains an id that is <= 0.");
            }
        }

        Connection conn = null;

        getLogger().log(Level.DEBUG, "get projects with the ids: " + idstring.substring(0, idstring.length() - 1));
        try {
            // create the connection
            conn = openConnection();

            // get the project objects
            Project[] projects = getProjects(ids, conn);
            closeConnection(conn);
            return projects;
        } catch (PersistenceException e) {
            getLogger().log(
                Level.ERROR,
                new LogMessage(null, null, "Fails to retrieving projects with ids: "
                    + idstring.substring(0, idstring.length() - 1), e));
            if (conn != null) {
                closeConnectionOnError(conn);
            }
            throw e;
        }
    }

    /**
     * Gets an array of all project types in the persistence. The project types are stored in 'project_type_lu' table.
     *
     * @return An array of all project types in the persistence.
     * @throws PersistenceException if error occurred while accessing the database.
     */
    public ProjectType[] getAllProjectTypes() throws PersistenceException {
        Connection conn = null;

        getLogger().log(Level.DEBUG, new LogMessage(null, null, "Enter getAllProjectTypes method."));
        try {
            // create the connection
            conn = openConnection();

            // get all the project types
            ProjectType[] projectTypes = getAllProjectTypes(conn);
            closeConnection(conn);
            return projectTypes;
        } catch (PersistenceException e) {
            getLogger().log(Level.ERROR, new LogMessage(null, null, "Fail to getAllProjectTypes.", e));
            if (conn != null) {
                closeConnectionOnError(conn);
            }
            throw e;
        }
    }

    /**
     * Gets an array of all project categories in the persistence. The project categories are stored in
     * 'project_category_lu' table.
     *
     * @return An array of all project categories in the persistence.
     * @throws PersistenceException if error occurred while accessing the database.
     */
    public ProjectCategory[] getAllProjectCategories() throws PersistenceException {
        Connection conn = null;
        getLogger().log(Level.DEBUG, new LogMessage(null, null, "Enter getAllProjectCategories method."));
        try {
            // create the connection
            conn = openConnection();

            // get all categories
            ProjectCategory[] projectCategories = getAllProjectCategories(conn);

            closeConnection(conn);
            return projectCategories;
        } catch (PersistenceException e) {
            getLogger().log(Level.ERROR, new LogMessage(null, null, "Fail to getAllProjectCategories.", e));
            if (conn != null) {
                closeConnectionOnError(conn);
            }
            throw e;
        }
    }

    /**
     * Gets an array of all project statuses in the persistence. The project statuses are stored in
     * 'project_status_lu' table.
     *
     * @return An array of all project statuses in the persistence.
     * @throws PersistenceException if error occurred while accessing the database.
     */
    public ProjectStatus[] getAllProjectStatuses() throws PersistenceException {
        Connection conn = null;
        getLogger().log(Level.DEBUG, new LogMessage(null, null, "Enter getAllProjectStatuses method."));
        try {
            // create the connection
            conn = openConnection();

            // get all the project statuses
            ProjectStatus[] projectStatuses = getAllProjectStatuses(conn);
            closeConnection(conn);
            return projectStatuses;
        } catch (PersistenceException e) {
            getLogger().log(Level.ERROR, new LogMessage(null, null, "Fail to getAllProjectStatuses.", e));
            if (conn != null) {
                closeConnectionOnError(conn);
            }
            throw e;
        }
    }

    /**
     * Gets an array of all project property type in the persistence. The project property types are stored in
     * 'project_info_type_lu' table.
     *
     * @return An array of all scorecard assignments in the persistence.
     * @throws PersistenceException if error occurred while accessing the database.
     */
    public ProjectPropertyType[] getAllProjectPropertyTypes() throws PersistenceException {
        getLogger().log(Level.DEBUG, new LogMessage(null, null, "Enter getAllProjectPropertyTypes method."));
        Connection conn = null;

        try {
            // create the connection
            conn = openConnection();

            ProjectPropertyType[] propertyTypes = getAllProjectPropertyTypes(conn);
            closeConnection(conn);
            return propertyTypes;
        } catch (PersistenceException e) {
            getLogger().log(Level.ERROR, new LogMessage(null, null, "Fail to getAllProjectPropertyTypes.", e));
            if (conn != null) {
                closeConnectionOnError(conn);
            }
            throw e;
        }
    }

    /**
     * Gets Project entities by given directProjectId.
     *
     * @param directProjectId
     *            the given directProjectId to find the Projects.
     * @return the found Project entities, return empty if cannot find any.
     * @throws IllegalArgumentException
     *             if the argument is non-positive
     * @throws PersistenceException
     *             if there are any exceptions.
     * @since 1.2
     */
    public Project[] getProjectsByDirectProjectId(long directProjectId) throws PersistenceException {
        Helper.assertLongPositive(directProjectId, "directProjectId");

        Connection conn = null;

        getLogger().log(Level.DEBUG, "get projects with the direct project id: " + directProjectId);
        try {
            // create the connection
            conn = openConnection();

            // find all the project ids with the specified direct project id in the table
            Object[][] rows = Helper.doQuery(conn, QUERY_PROJECT_IDS_SQL + directProjectId, new Object[]{},
                QUERY_PROJECT_IDS__COLUMN_TYPES);

            if (0 == rows.length) {
                return new Project[0];
            }

            long[] ids = new long[rows.length];

            for (int i = 0; i < rows.length; i++) {
                ids[i] = (Long) rows[i][0];
            }

            // get the project objects
            Project[] projects = getProjects(ids, conn);
            closeConnection(conn);
            return projects;
        } catch (PersistenceException e) {
            getLogger().log(
                Level.ERROR,
                new LogMessage(null, null, "Fails to retrieving projects with the direct project id: "
                    + directProjectId, e));
            if (conn != null) {
                closeConnectionOnError(conn);
            }
            throw e;
        }
    }

    /**
     * Gets Project FileType entities by given projectId.
     *
     * @param projectId
     *            the given projectId to find the entities.
     * @return the found FileType entities, return empty if cannot find any.
     * @throws IllegalArgumentException
     *             if the argument is non-positive
     * @throws PersistenceException
     *             if there are any exceptions.
     * @since 1.2
     */
    public FileType[] getProjectFileTypes(long projectId) throws PersistenceException {
        Helper.assertLongPositive(projectId, "projectId");

        Connection conn = null;

        getLogger().log(Level.DEBUG, "get Project file types with the project id: " + projectId);
        try {
            // create the connection
            conn = openConnection();

            // check whether the project id is already in the database
            if (!Helper.checkEntityExists("project", "project_id", projectId, conn)) {
                throw new PersistenceException("The project with id " + projectId + " does not exist in the database.");
            }

            // find file types in the table
            Object[][] rows = Helper.doQuery(conn, QUERY_FILE_TYPES_SQL + projectId, new Object[]{},
                QUERY_FILE_TYPES_COLUMN_TYPES);

            FileType[] fileTypes = new FileType[rows.length];

            // enumerate each row
            for (int i = 0; i < rows.length; ++i) {
                Object[] row = rows[i];

                FileType fileType = new FileType();
                fileType.setId((Long) row[0]);
                fileType.setDescription((String) row[1]);
                fileType.setSort(((Long) row[2]).intValue());
                fileType.setImageFile((Boolean) row[3]);
                fileType.setExtension((String) row[4]);
                fileType.setBundledFile((Boolean) row[5]);

                fileTypes[i] = fileType;
            }

            closeConnection(conn);
            return fileTypes;
        } catch (PersistenceException e) {
            getLogger().log(Level.ERROR,
                new LogMessage(null, null, "Fails to retrieving project file types with project id: " + projectId, e));
            if (conn != null) {
                closeConnectionOnError(conn);
            }
            throw e;
        }
    }

    /**
     * Updates FileType entities by given projectId, it also updates the relationship between project and FileType.
     *
     * @param projectId
     *            the given projectId to update the fileTypes entities.
     * @param fileTypes
     *            the given fileTypes entities to update.
     * @param operator
     *            the given audit user.
     * @throws IllegalArgumentException
     *             if projectId is non-positive or fileTypes contains null, or if operator is null or empty.
     * @throws PersistenceException
     *             if there are any exceptions.
     * @since 1.2
     */
    public void updateProjectFileTypes(long projectId, List<FileType> fileTypes, String operator)
        throws PersistenceException {
        Helper.assertLongPositive(projectId, "projectId");
        Helper.assertObjectNotNull(fileTypes, "fileTypes");
        for (int i = 0; i < fileTypes.size(); i++) {
            Helper.assertObjectNotNull(fileTypes.get(i), "fileTypes[" + i + "]");
        }
        Helper.assertStringNotNullNorEmpty(operator, "operator");

        Connection conn = null;

        getLogger().log(Level.DEBUG,
            new LogMessage(null, operator, "updating the file types for the project with id: " + projectId));

        try {
            // create the connection
            conn = openConnection();

            // check whether the project id is already in the database
            if (!Helper.checkEntityExists("project", "project_id", projectId, conn)) {
                throw new PersistenceException("The project with id " + projectId + " does not exist in the database.");
            }

            createOrUpdateProjectFileTypes(projectId, fileTypes, conn, operator, true);

            // create project audit record into project_audit table
            createProjectAudit(projectId, "Updates the project file types", operator, conn);

            closeConnection(conn);
        } catch (PersistenceException e) {
            getLogger()
                .log(
                    Level.ERROR,
                    new LogMessage(null, operator, "Fails to update the file types for the project with id "
                        + projectId, e));
            if (conn != null) {
                closeConnectionOnError(conn);
            }
            throw e;
        }
    }

    /**
     * Creates or updates project file types.
     *
     * @param projectId
     *            the project id
     * @param fileTypes
     *            the file types for the project
     * @param conn
     *            the database connection
     * @param operator
     *            the given audit user.
     * @param update
     *            true to update the project file types reference; otherwise, just create the project file types
     *            reference
     * @throws PersistenceException
     *             if any error occurs
     * @since 1.2
     */
    private void createOrUpdateProjectFileTypes(long projectId, List<FileType> fileTypes, Connection conn,
        String operator, boolean update) throws PersistenceException {
        if (fileTypes == null) {
            return;
        }
        Object[] queryArgs;

        if (update) {

            getLogger().log(Level.DEBUG,
                "delete the project file typs reference from database with the specified project id: " + projectId);
            // delete the project file types reference from database with the specified project id
            queryArgs = new Object[]{projectId};
            Helper.doDMLQuery(conn, DELETE_PROJECT_FILE_TYPES_XREF__WITH_PROJECT_ID_SQL, queryArgs);
        }

        for (FileType fileType : fileTypes) {
            // the file type with the specified file type id exists, just update it
            if (fileType.getId() > 0
                && Helper.checkEntityExists("file_type_lu", "file_type_id", fileType.getId(), conn)) {
                updateFileType(fileType, operator);
            } else { // the file type with the specified file types id does not exist, insert it to the database
                createFileType(fileType, operator);
            }

            // insert projectId and file type id into project_file_type_xref table
            getLogger().log(
                Level.DEBUG,
                "insert projectId: " + projectId + " and file type id: " + fileType.getId()
                    + " into project_file_type_xref table");

            queryArgs = new Object[]{projectId, fileType.getId()};
            Helper.doDMLQuery(conn, INSERT_PROJECT_FILE_TYPES_XREF_SQL, queryArgs);
        }
    }

    /**
     * Gets Project Prize entities by given projectId.
     *
     * @param projectId
     *            the given projectId to find the entities.
     * @return the found Prize entities, return empty if cannot find any.
     * @throws IllegalArgumentException
     *             if projectId is non-positive
     * @throws PersistenceException
     *             if there are any exceptions.
     * @since 1.2
     */
    public Prize[] getProjectPrizes(long projectId) throws PersistenceException {
        Helper.assertLongPositive(projectId, "projectId");

        Connection conn = null;

        getLogger().log(Level.DEBUG, "get project prizes with the project id: " + projectId);
        try {
            // create the connection
            conn = openConnection();

            // check whether the project id is already in the database
            if (!Helper.checkEntityExists("project", "project_id", projectId, conn)) {
                throw new PersistenceException("The project with id " + projectId + " does not exist in the database.");
            }

            // find prizes in the table
            Object[][] rows = Helper.doQuery(conn, QUERY_PRIZES_SQL + projectId, new Object[]{},
                QUERY_PRIZES_COLUMN_TYPES);

            Prize[] prizes = new Prize[rows.length];
            // enumerate each row
            for (int i = 0; i < rows.length; ++i) {
                Object[] row = rows[i];

                Prize prize = new Prize();
                prize.setId((Long) row[0]);
                prize.setProjectId(projectId);
                prize.setPlace(((Long) row[1]).intValue());
                prize.setPrizeAmount((Double) row[2]);
                prize.setNumberOfSubmissions(((Long) row[3]).intValue());
                PrizeType prizeType = new PrizeType();
                prizeType.setId((Long) row[4]);
                prizeType.setDescription((String) row[5]);
                prize.setPrizeType(prizeType);

                prizes[i] = prize;
            }

            closeConnection(conn);
            return prizes;
        } catch (PersistenceException e) {
            getLogger().log(Level.ERROR,
                new LogMessage(null, null, "Fails to retrieving project prizes with project id: " + projectId, e));
            if (conn != null) {
                closeConnectionOnError(conn);
            }
            throw e;
        }
    }

    /**
     * Updates Prize entities by given projectId.
     *
     * @param projectId
     *            the given projectId to update the prizes entities.
     * @param prizes
     *            the given prizes entities to update.
     * @param operator
     *            the given audit user.
     * @throws IllegalArgumentException
     *             if projectId is non-positive, prizes contains null, or if operator is null or empty.
     * @throws PersistenceException
     *             if there are any exceptions.
     * @since 1.2
     */
    public void updateProjectPrizes(long projectId, List<Prize> prizes, String operator) throws PersistenceException {
        Helper.assertLongPositive(projectId, "projectId");
        Helper.assertObjectNotNull(prizes, "prizes");
        for (int i = 0; i < prizes.size(); i++) {
            Helper.assertObjectNotNull(prizes.get(i), "prizes[" + i + "]");
        }
        Helper.assertStringNotNullNorEmpty(operator, "operator");

        Connection conn = null;

        getLogger().log(Level.DEBUG,
            new LogMessage(null, operator, "updating the prizes for the project with id: " + projectId));

        try {
            // create the connection
            conn = openConnection();

            // check whether the project id is already in the database
            if (!Helper.checkEntityExists("project", "project_id", projectId, conn)) {
                throw new PersistenceException("The project with id " + projectId + " does not exist in the database.");
            }

            createOrUpdateProjectPrizes(projectId, prizes, conn, operator, true);

            // create project audit record into project_audit table
            createProjectAudit(projectId, "Updates the project prizes", operator, conn);

            closeConnection(conn);
        } catch (PersistenceException e) {
            getLogger().log(Level.ERROR,
                new LogMessage(null, operator, "Fails to update the prizes for the project with id " + projectId, e));
            if (conn != null) {
                closeConnectionOnError(conn);
            }
            throw e;
        }
    }

    /**
     * Creates or updates project prizes.
     *
     * @param projectId
     *            the project id
     * @param prizes
     *            the prizes for the project
     * @param conn
     *            the database connection
     * @param operator
     *            the given audit user.
     * @param update
     *            true to update the project prizes reference; otherwise, just create the project prizes reference
     * @throws PersistenceException
     *             if any error occurs
     * @since 1.2
     */
    private void createOrUpdateProjectPrizes(long projectId, List<Prize> prizes, Connection conn, String operator,
        boolean update) throws PersistenceException {
        if (prizes == null) {
            return;
        }

        for (Prize prize : prizes) {
            // the prize with the specified prize id exists, just update it
            if (prize.getId() > 0 && Helper.checkEntityExists("prize", "prize_id", prize.getId(), conn)) {
                updatePrize(prize, operator);
            } else { // the prize with the specified prize id does not exist, insert it to the database
                createPrize(prize, operator);
            }
        }
    }

    /**
     * Creates the given FileType entity.
     *
     * @param fileType
     *            the given fileType entity to create.
     * @param operator
     *            the given audit user.
     * @return the created fileType entity.
     * @throws IllegalArgumentException
     *             if fileType is null, or if operator is null or empty.
     * @throws PersistenceException
     *             if there are any exceptions.
     * @since 1.2
     */
    public FileType createFileType(FileType fileType, String operator) throws PersistenceException {
        Helper.assertObjectNotNull(fileType, "fileType");
        Helper.assertStringNotNullNorEmpty(operator, "operator");

        Connection conn = null;

        // newId will contain the new generated Id for the file type
        Long newId = null;

        getLogger().log(Level.DEBUG, new LogMessage(null, operator, "creating new file type: " + fileType));

        try {
            // create the connection
            conn = openConnection();

            // check whether the file type id is already in the database
            if (fileType.getId() > 0) {
                if (Helper.checkEntityExists("file_type_lu", "file_type_id", fileType.getId(), conn)) {
                    throw new PersistenceException("The file type with the same id [" + fileType.getId()
                        + "] already exists.");
                }
            }

            try {
                // generate id for the file type
                newId = fileTypeIdGenerator.getNextID();
                getLogger().log(Level.DEBUG, new LogMessage(newId, operator, "generate id for new file type"));
            } catch (IDGenerationException e) {
                throw new PersistenceException("Unable to generate id for the file type.", e);
            }

            // create the file type
            getLogger().log(Level.DEBUG, "insert record into file type with id:" + newId);

            Timestamp createDate = new Timestamp(System.currentTimeMillis());

            // insert the file type into database
            Object[] queryArgs = new Object[]{newId, fileType.getDescription(), fileType.getSort(),
                convertBooleanToString(fileType.isImageFile()), fileType.getExtension(),
                convertBooleanToString(fileType.isBundledFile()), operator, createDate, operator, createDate};
            Helper.doDMLQuery(conn, CREATE_FILE_TYPE_SQL, queryArgs);

            closeConnection(conn);

            fileType.setCreationUser(operator);
            fileType.setCreationTimestamp(createDate);
            fileType.setModificationUser(operator);
            fileType.setModificationTimestamp(createDate);

            // set the newId when no exception occurred
            fileType.setId(newId);
        } catch (PersistenceException e) {
            getLogger().log(Level.ERROR, new LogMessage(null, operator, "Fails to create file type " + newId, e));
            if (conn != null) {
                closeConnectionOnError(conn);
            }
            throw e;
        }

        return fileType;
    }

    /**
     * Converts the boolean value to a string representation. For true, we use 't'; For false, we use 'f'.
     *
     * @param booleanVal
     *            the boolean value to convert
     * @return 't' if the paramter is true; otherwise, returns 'f'
     * @since 1.2
     */
    private Object convertBooleanToString(boolean booleanVal) {
        return booleanVal ? "t" : "f";
    }

    /**
     * Updates the given FileType entity.
     *
     * @param fileType
     *            the given fileType entity to update.
     * @param operator
     *            the given audit user.
     * @throws IllegalArgumentException
     *             if fileType is null, or if operator is null or empty.
     * @throws PersistenceException
     *             if there are any exceptions.
     * @since 1.2
     */
    public void updateFileType(FileType fileType, String operator) throws PersistenceException {
        Helper.assertObjectNotNull(fileType, "fileType");
        Helper.assertStringNotNullNorEmpty(operator, "operator");

        Connection conn = null;

        getLogger().log(Level.DEBUG,
            new LogMessage(null, operator, "updating the file type with id: " + fileType.getId()));

        // modifyDate will contain the modify_date retrieved from database.
        Timestamp modifyDate = new Timestamp(System.currentTimeMillis());
        try {
            // create the connection
            conn = openConnection();

            // check whether the file type id is already in the database
            if (!Helper.checkEntityExists("file_type_lu", "file_type_id", fileType.getId(), conn)) {
                throw new PersistenceException("The file type id [" + fileType.getId()
                    + "] does not exist in the database.");
            }

            // update the file type into database
            Object[] queryArgs = new Object[]{fileType.getDescription(), fileType.getSort(),
                convertBooleanToString(fileType.isImageFile()), fileType.getExtension(),
                convertBooleanToString(fileType.isBundledFile()), operator, modifyDate};
            Helper.doDMLQuery(conn, UPDATE_FILE_TYPE_SQL + fileType.getId(), queryArgs);

            closeConnection(conn);
        } catch (PersistenceException e) {
            getLogger().log(Level.ERROR, new LogMessage(null, operator, "Fails to update file type " + fileType, e));
            if (conn != null) {
                closeConnectionOnError(conn);
            }
            throw e;
        }

        fileType.setModificationUser(operator);
        fileType.setModificationTimestamp(modifyDate);
    }

    /**
     * Removes the given FileType entity.
     *
     * @param fileType
     *            the given fileType entity to update.
     * @param operator
     *            the given audit user.
     * @throws IllegalArgumentException
     *             if fileType is null, or if operator is null or empty.
     * @throws PersistenceException
     *             if there are any exceptions.
     * @since 1.2
     */
    public void removeFileType(FileType fileType, String operator) throws PersistenceException {
        Helper.assertObjectNotNull(fileType, "fileType");
        Helper.assertStringNotNullNorEmpty(operator, "operator");

        Connection conn = null;

        getLogger().log(Level.DEBUG,
            new LogMessage(null, operator, "deleting the file type with id: " + fileType.getId()));

        try {
            // create the connection
            conn = openConnection();

            // check whether the file type id is already in the database
            if (!Helper.checkEntityExists("file_type_lu", "file_type_id", fileType.getId(), conn)) {
                throw new PersistenceException("The file type id [" + fileType.getId()
                    + "] does not exist in the database.");
            }

            // delete the project file type reference from database
            Object[] queryArgs = new Object[]{fileType.getId()};

            Object[][] rows = Helper.doQuery(conn, QUERY_PROJECT_IDS_WITH_FILE_TYPE_SQL + fileType.getId(),
                new Object[]{}, QUERY_PROJECT_IDS__COLUMN_TYPES);

            // create project audit record into project_audit table
            auditProjects(rows, conn, "Removes the project file type", operator);

            Helper.doDMLQuery(conn, DELETE_PROJECT_FILE_TYPE_XREF_SQL, queryArgs);

            // delete the file type from database
            Helper.doDMLQuery(conn, DELETE_FILE_TYPE_SQL, queryArgs);

            closeConnection(conn);
        } catch (PersistenceException e) {
            getLogger().log(Level.ERROR, new LogMessage(null, operator, "Fails to delete file type " + fileType, e));
            if (conn != null) {
                closeConnectionOnError(conn);
            }
            throw e;
        }
    }

    /**
     * Gets all FileType entities.
     *
     * @return the found FileType entities, return empty if cannot find any.
     * @throws PersistenceException
     *             if there are any exceptions.
     * @since 1.2
     */
    public FileType[] getAllFileTypes() throws PersistenceException {
        Connection conn = null;

        getLogger().log(Level.DEBUG, new LogMessage(null, null, "Enter getAllFileTypes method."));
        try {
            // create the connection
            conn = openConnection();

            // get all the file types
            Object[][] rows = Helper.doQuery(conn, QUERY_ALL_FILE_TYPES_SQL, new Object[]{},
                QUERY_ALL_FILE_TYPES_COLUMN_TYPES);

            closeConnection(conn);

            // create the FileType array.
            FileType[] fileTypes = new FileType[rows.length];

            for (int i = 0; i < rows.length; ++i) {
                Object[] row = rows[i];

                // create a new instance of FileType class
                fileTypes[i] = new FileType();
                fileTypes[i].setId((Long) row[0]);
                fileTypes[i].setDescription((String) row[1]);
                fileTypes[i].setSort(((Long) row[2]).intValue());
                fileTypes[i].setImageFile((Boolean) row[3]);
                fileTypes[i].setExtension((String) row[4]);
                fileTypes[i].setBundledFile((Boolean) row[5]);
            }

            return fileTypes;
        } catch (PersistenceException e) {
            getLogger().log(Level.ERROR, new LogMessage(null, null, "Fail to getAllFileTypes.", e));
            if (conn != null) {
                closeConnectionOnError(conn);
            }
            throw e;
        }
    }

    /**
     * Gets all PrizeType entities.
     *
     * @return the found PrizeType entities, return empty if cannot find any.
     * @throws PersistenceException
     *             if there are any exceptions.
     * @since 1.2
     */
    public PrizeType[] getPrizeTypes() throws PersistenceException {
        Connection conn = null;

        getLogger().log(Level.DEBUG, new LogMessage(null, null, "Enter getPrizeTypes method."));
        try {
            // create the connection
            conn = openConnection();

            // get all the prize types
            Object[][] rows = Helper.doQuery(conn, QUERY_ALL_PRIZE_TYPES_SQL, new Object[]{},
                QUERY_ALL_PRIZE_TYPES_COLUMN_TYPES);

            // create the PrizeType array.
            PrizeType[] prizeTypes = new PrizeType[rows.length];

            for (int i = 0; i < rows.length; ++i) {
                Object[] row = rows[i];

                // create a new instance of PrizeType class
                prizeTypes[i] = new PrizeType();
                prizeTypes[i].setId((Long) row[0]);
                prizeTypes[i].setDescription((String) row[1]);
            }

            closeConnection(conn);
            return prizeTypes;
        } catch (PersistenceException e) {
            getLogger().log(Level.ERROR, new LogMessage(null, null, "Fail to getPrizeTypes.", e));
            if (conn != null) {
                closeConnectionOnError(conn);
            }
            throw e;
        }
    }

    /**
     * Creates the given Prize entity.
     *
     * @param prize
     *            the given prize entity to create.
     * @param operator
     *            the given audit user.
     * @return the created prize entity.
     * @throws IllegalArgumentException
     *             if prize is null, or if operator is null or empty.
     * @throws PersistenceException
     *             if there are any exceptions.
     * @since 1.2
     */
    public Prize createPrize(Prize prize, String operator) throws PersistenceException {
        Helper.assertObjectNotNull(prize, "prize");
        Helper.assertObjectNotNull(prize.getPrizeType(), "prize.prizeType");
        Helper.assertStringNotNullNorEmpty(operator, "operator");

        Connection conn = null;

        // newId will contain the new generated Id for the prize
        Long newId = null;

        getLogger().log(Level.DEBUG, new LogMessage(null, operator, "creating new prize: " + prize));

        try {
            // create the connection
            conn = openConnection();

            // check whether the prize id is already in the database
            if (prize.getId() > 0) {
                if (Helper.checkEntityExists("prize", "prize_id", prize.getId(), conn)) {
                    throw new PersistenceException(
                        "The prize with the same id [" + prize.getId() + "] already exists.");
                }
            }

            try {
                // generate id for the prize
                newId = prizeIdGenerator.getNextID();
                getLogger().log(Level.DEBUG, new LogMessage(newId, operator, "generate id for new prize"));
            } catch (IDGenerationException e) {
                throw new PersistenceException("Unable to generate id for the prize.", e);
            }

            // create the prize
            getLogger().log(Level.DEBUG, "insert record into prize with id:" + newId);

            Timestamp createDate = new Timestamp(System.currentTimeMillis());

            // insert the prize into database
            Object[] queryArgs = new Object[]{newId, prize.getProjectId(), (long) prize.getPlace(),
                    prize.getPrizeAmount(), prize.getPrizeType().getId(),
                prize.getNumberOfSubmissions(), operator, createDate, operator, createDate};
            Helper.doDMLQuery(conn, CREATE_PRIZE_SQL, queryArgs);

            closeConnection(conn);

            prize.setCreationUser(operator);
            prize.setCreationTimestamp(createDate);
            prize.setModificationUser(operator);
            prize.setModificationTimestamp(createDate);

            // set the newId when no exception occurred
            prize.setId(newId);
        } catch (PersistenceException e) {
            getLogger().log(Level.ERROR, new LogMessage(null, operator, "Fails to create prize " + newId, e));
            if (conn != null) {
                closeConnectionOnError(conn);
            }
            throw e;
        }

        return prize;
    }

    /**
     * Updates the given prize entity.
     *
     * @param prize
     *            the given prize entity to create.
     * @param operator
     *            the given audit user.
     * @throws IllegalArgumentException
     *             if prize is null, or if operator is null or empty.
     * @throws PersistenceException
     *             if there are any exceptions.
     * @since 1.2
     */
    public void updatePrize(Prize prize, String operator) throws PersistenceException {
        Helper.assertObjectNotNull(prize, "prize");
        Helper.assertStringNotNullNorEmpty(operator, "operator");

        Connection conn = null;

        getLogger().log(Level.DEBUG, new LogMessage(null, operator, "updating the prize with id: " + prize.getId()));

        // modifyDate will contain the modify_date retrieved from database.
        // modifyDate will contain the modify_date retrieved from database.
        Timestamp modifyDate = new Timestamp(System.currentTimeMillis());
        try {
            // create the connection
            conn = openConnection();

            // check whether the prize id is already in the database
            if (!Helper.checkEntityExists("prize", "prize_id", prize.getId(), conn)) {
                throw new PersistenceException("The prize id [" + prize.getId() + "] does not exist in the database.");
            }

            // insert the prize into database
            Object[] queryArgs = new Object[]{(long) prize.getPlace(), prize.getPrizeAmount(),
                    prize.getPrizeType().getId(), prize.getNumberOfSubmissions(), operator, modifyDate,
                    prize.getProjectId()};
            Helper.doDMLQuery(conn, UPDATE_PRIZE_SQL + prize.getId(), queryArgs);

            closeConnection(conn);
        } catch (PersistenceException e) {
            getLogger().log(Level.ERROR, new LogMessage(null, operator, "Fails to update prize " + prize, e));
            if (conn != null) {
                closeConnectionOnError(conn);
            }
            throw e;
        }

        prize.setModificationUser(operator);
        prize.setModificationTimestamp(modifyDate);
    }

    /**
     * Removes the given prize entity.
     *
     * @param prize
     *            the given prize entity to create.
     * @param operator
     *            the given audit user.
     * @throws IllegalArgumentException
     *             if prize is null, or if operator is null or empty.
     * @throws PersistenceException
     *             if there are any exceptions.
     * @since 1.2
     */
    public void removePrize(Prize prize, String operator) throws PersistenceException {
        Helper.assertObjectNotNull(prize, "prize");
        Helper.assertStringNotNullNorEmpty(operator, "operator");

        Connection conn = null;

        getLogger().log(Level.DEBUG, new LogMessage(null, operator, "deleting the prize with id: " + prize.getId()));

        try {
            // create the connection
            conn = openConnection();

            // check whether the prize id is already in the database
            if (!Helper.checkEntityExists("prize", "prize_id", prize.getId(), conn)) {
                throw new PersistenceException("The prize id [" + prize.getId() + "] does not exist in the database.");
            }

            // delete the project prize reference from database
            Object[] queryArgs = new Object[]{prize.getId()};

            Object[][] rows = Helper.doQuery(conn, QUERY_PROJECT_IDS_WITH_PRIZE_SQL + prize.getId(), new Object[]{},
                QUERY_PROJECT_IDS__COLUMN_TYPES);

            // create project audit record into project_audit table
            auditProjects(rows, conn, "Removes the project prize", operator);

            // delete the prize from database
            Helper.doDMLQuery(conn, DELETE_PRIZE_SQL, queryArgs);

            closeConnection(conn);
        } catch (PersistenceException e) {
            getLogger().log(Level.ERROR, new LogMessage(null, operator, "Fails to delete prize " + prize, e));
            if (conn != null) {
                closeConnectionOnError(conn);
            }
            throw e;
        }
    }

    /**
     * Creates the given ProjectStudioSpecification entity.
     *
     * @param spec
     *            the given ProjectStudioSpecification entity to create.
     * @param operator
     *            the given audit user.
     * @return the created spec entity
     * @throws IllegalArgumentException
     *             if spec is null, or if operator is null or empty.
     * @throws PersistenceException
     *             if there are any exceptions.
     * @since 1.2
     */
    public ProjectStudioSpecification createProjectStudioSpecification(ProjectStudioSpecification spec, String operator)
        throws PersistenceException {
        Helper.assertObjectNotNull(spec, "spec");
        Helper.assertStringNotNullNorEmpty(operator, "operator");

        Connection conn = null;

        // newId will contain the new generated Id for the project studio specification
        Long newId = null;

        getLogger().log(Level.DEBUG,
            new LogMessage(null, operator, "creating new project studio specification: " + spec));

        try {
            // create the connection
            conn = openConnection();

            // check whether the project studio specification id is already in the database
            if (spec.getId() > 0) {
                if (Helper.checkEntityExists("project_studio_specification", "project_studio_spec_id", spec.getId(),
                    conn)) {
                    throw new PersistenceException("The project studio specification with the same id [" + spec.getId()
                        + "] already exists.");
                }
            }

            try {
                // generate id for the project studio specification
                newId = studioSpecIdGenerator.getNextID();
                getLogger().log(Level.DEBUG,
                    new LogMessage(newId, operator, "generate id for new project studio specification"));
            } catch (IDGenerationException e) {
                throw new PersistenceException("Unable to generate id for the project studio specification.", e);
            }

            // create the project studio specification
            getLogger().log(Level.DEBUG, "insert record into project studio specification with id:" + newId);

            Timestamp createDate = new Timestamp(System.currentTimeMillis());

            // insert the project studio specification into database
            Object[] queryArgs = new Object[]{newId, spec.getGoals(), spec.getTargetAudience(),
                spec.getBrandingGuidelines(), spec.getDislikedDesignWebSites(), spec.getOtherInstructions(),
                spec.getWinningCriteria(), spec.isSubmittersLockedBetweenRounds(), spec.getRoundOneIntroduction(),
                spec.getRoundTwoIntroduction(), spec.getColors(), spec.getFonts(), spec.getLayoutAndSize(), operator,
                createDate, operator, createDate};
            Helper.doDMLQuery(conn, CREATE_STUDIO_SPEC_SQL, queryArgs);

            closeConnection(conn);

            spec.setCreationUser(operator);
            spec.setCreationTimestamp(createDate);
            spec.setModificationUser(operator);
            spec.setModificationTimestamp(createDate);

            // set the newId when no exception occurred
            spec.setId(newId);
        } catch (PersistenceException e) {
            getLogger().log(Level.ERROR,
                new LogMessage(null, operator, "Fails to create project studio specification " + newId, e));
            if (conn != null) {
                closeConnectionOnError(conn);
            }
            throw e;
        }

        return spec;
    }

    /**
     * Updates the given ProjectStudioSpecification entity.
     *
     * @param spec
     *            the given ProjectStudioSpecification entity to create.
     * @param operator
     *            the given audit user.
     * @throws IllegalArgumentException
     *             if spec is null, or if operator is null or empty.
     * @throws PersistenceException
     *             if there are any exceptions.
     * @since 1.2
     */
    public void updateProjectStudioSpecification(ProjectStudioSpecification spec, String operator)
        throws PersistenceException {
        Helper.assertObjectNotNull(spec, "spec");
        Helper.assertStringNotNullNorEmpty(operator, "operator");

        Connection conn = null;

        getLogger().log(Level.DEBUG,
            new LogMessage(null, operator, "updating the project studio specification with id: " + spec.getId()));

        // modifyDate will contain the modify_date retrieved from database.
        Timestamp modifyDate = new Timestamp(System.currentTimeMillis());
        try {
            // create the connection
            conn = openConnection();

            // check whether the project studio specification id is already in the database
            if (!Helper.checkEntityExists("project_studio_specification",
                "project_studio_spec_id", spec.getId(), conn)) {
                    throw new PersistenceException("The project studio specification id [" + spec.getId()
                        + "] does not exist in the database.");
            }

            // insert the project studio specification into database
            Object[] queryArgs = new Object[]{spec.getGoals(), spec.getTargetAudience(), spec.getBrandingGuidelines(),
                spec.getDislikedDesignWebSites(), spec.getOtherInstructions(), spec.getWinningCriteria(),
                spec.isSubmittersLockedBetweenRounds(), spec.getRoundOneIntroduction(), spec.getRoundTwoIntroduction(),
                spec.getColors(), spec.getFonts(), spec.getLayoutAndSize(), operator, modifyDate};
            Helper.doDMLQuery(conn, UPDATE_STUDIO_SPEC_SQL + spec.getId(), queryArgs);

            closeConnection(conn);
        } catch (PersistenceException e) {
            getLogger().log(Level.ERROR,
                new LogMessage(null, operator, "Fails to update project studio specification " + spec.getId(), e));
            if (conn != null) {
                closeConnectionOnError(conn);
            }
            throw e;
        }

        spec.setModificationUser(operator);
        spec.setModificationTimestamp(modifyDate);
    }

    /**
     * Removes the given ProjectStudioSpecification entity.
     *
     * @param spec
     *            the given ProjectStudioSpecification entity to create.
     * @param operator
     *            the given audit user.
     * @throws IllegalArgumentException
     *             if spec is null, or if operator is null or empty.
     * @throws PersistenceException
     *             if there are any exceptions.
     * @since 1.2
     */
    public void removeProjectStudioSpecification(ProjectStudioSpecification spec, String operator)
        throws PersistenceException {
        Helper.assertObjectNotNull(spec, "spec");
        Helper.assertStringNotNullNorEmpty(operator, "operator");

        Connection conn = null;

        getLogger().log(Level.DEBUG,
            new LogMessage(null, operator, "deleting the project studio specification with id: " + spec.getId()));

        try {
            // create the connection
            conn = openConnection();

            // check whether the project studio specification id is already in the database
            if (!Helper.checkEntityExists("project_studio_specification",
                "project_studio_spec_id", spec.getId(), conn)) {
                    throw new PersistenceException("The project studio specification id [" + spec.getId()
                        + "] does not exist in the database.");
            }

            // delete the project project studio specification reference from database
            Object[] queryArgs = new Object[]{spec.getId()};

            Object[][] rows = Helper.doQuery(conn, QUERY_PROJECT_IDS_WITH_STUDIO_SPEC_SQL + spec.getId(),
                new Object[]{}, QUERY_PROJECT_IDS__COLUMN_TYPES);

            // create project audit record into project_audit table
            auditProjects(rows, conn, "Removes the project studio specification", operator);

            Helper.doDMLQuery(conn, SET_PROJECT_STUDIO_SPEC_SQL, queryArgs);

            // delete the project studio specification from database
            Helper.doDMLQuery(conn, DELETE_STUDIO_SPEC_SQL, queryArgs);

            closeConnection(conn);
        } catch (PersistenceException e) {
            getLogger().log(Level.ERROR,
                new LogMessage(null, operator, "Fails to delete project studio specification " + spec.getId(), e));
            if (conn != null) {
                closeConnectionOnError(conn);
            }
            throw e;
        }
    }

    /**
     * Audits the projects.
     *
     * @param rows
     *            the rows containing the projects ids
     * @param conn
     *            the database connection
     * @param reason
     *            the reason to audit
     * @param operator the audit user
     * @throws PersistenceException
     *             if any error occurs
     * @since 1.2
     */
    private void auditProjects(Object[][] rows, Connection conn, String reason, String operator)
        throws PersistenceException {
        if (0 != rows.length) {
            for (Object[] row : rows) {
                createProjectAudit((Long) row[0], reason, operator, conn);
            }
        }
    }

    /**
     * Gets ProjectStudioSpecification entity by given projectId.
     *
     * @param projectId
     *            the given projectId to find the entities.
     * @return the found ProjectStudioSpecification entities, return null if cannot find any.
     * @throws IllegalArgumentException
     *             if projectId is non-positive
     * @throws PersistenceException
     *             if there are any exceptions.
     * @since 1.2
     */
    public ProjectStudioSpecification getProjectStudioSpecification(long projectId) throws PersistenceException {
        Helper.assertLongPositive(projectId, "projectId");

        Connection conn = null;

        getLogger().log(Level.DEBUG, "get project studio specification with the project id: " + projectId);
        try {
            // create the connection
            conn = openConnection();

            // check whether the project id is already in the database
            if (!Helper.checkEntityExists("project", "project_id", projectId, conn)) {
                throw new PersistenceException("The project with id " + projectId + " does not exist in the database.");
            }

            // find project studio specification in the table
            Object[][] rows = Helper.doQuery(conn, QUERY_STUDIO_SPEC_SQL + projectId, new Object[]{},
                QUERY_STUDIO_SPEC_COLUMN_TYPES);

            if (rows.length == 0) { // no project studio specification is found, return null
                closeConnection(conn);
                return null;
            }

            ProjectStudioSpecification studioSpec = new ProjectStudioSpecification();

            // sets the properties for the studio specification
            studioSpec.setId((Long) rows[0][0]);
            studioSpec.setGoals((String) rows[0][1]);
            studioSpec.setTargetAudience((String) rows[0][2]);
            studioSpec.setBrandingGuidelines((String) rows[0][3]);
            studioSpec.setDislikedDesignWebSites((String) rows[0][4]);
            studioSpec.setOtherInstructions((String) rows[0][5]);
            studioSpec.setWinningCriteria((String) rows[0][6]);
            studioSpec.setSubmittersLockedBetweenRounds((Boolean) rows[0][7]);
            studioSpec.setRoundOneIntroduction((String) rows[0][8]);
            studioSpec.setRoundTwoIntroduction((String) rows[0][9]);
            studioSpec.setColors((String) rows[0][10]);
            studioSpec.setFonts((String) rows[0][11]);
            studioSpec.setLayoutAndSize((String) rows[0][12]);

            closeConnection(conn);
            return studioSpec;
        } catch (PersistenceException e) {
            getLogger().log(
                Level.ERROR,
                new LogMessage(null, null, "Fails to retrieving project studio specification with project id: "
                    + projectId, e));
            if (conn != null) {
                closeConnectionOnError(conn);
            }
            throw e;
        }
    }

    /**
     * Updates the given ProjectStudioSpecification entity for specified project id.
     *
     * @param spec
     *            the given ProjectStudioSpecification entity to update.
     * @param projectId
     *            the given project id to update.
     * @param operator
     *            the given audit user.
     * @throws IllegalArgumentException
     *             if spec is null, or projectId is non-positive or if operator is null or empty.
     * @throws PersistenceException
     *             if there are any exceptions.
     * @since 1.2
     */
    public void updateStudioSpecificationForProject(ProjectStudioSpecification spec, long projectId, String operator)
        throws PersistenceException {
        Helper.assertObjectNotNull(spec, "spec");
        Helper.assertLongPositive(projectId, "projectId");
        Helper.assertStringNotNullNorEmpty(operator, "operator");

        Connection conn = null;

        getLogger().log(Level.DEBUG,
            new LogMessage(null, operator, "updating the studio specification for the project with id: " + projectId));

        try {
            // create the connection
            conn = openConnection();

            // check whether the project id is already in the database
            if (!Helper.checkEntityExists("project", "project_id", projectId, conn)) {
                throw new PersistenceException("The project with id " + projectId + " does not exist in the database.");
            }

            createOrUpdateProjectStudioSpecification(projectId, spec, conn, operator);

            // create project audit record into project_audit table
            createProjectAudit(projectId, "Updates the project studion specification", operator, conn);

            closeConnection(conn);
        } catch (PersistenceException e) {
            getLogger().log(
                Level.ERROR,
                new LogMessage(null, operator, "Fails to update the studio specification for the project with id "
                    + projectId, e));
            if (conn != null) {
                closeConnectionOnError(conn);
            }
            throw e;
        }
    }

    /**
     * Creates or updates project studio specification.
     *
     * @param projectId
     *            the project id
     * @param spec
     *            the studio specification for the project
     * @param conn
     *            the database connection
     * @param operator
     *            the given audit user
     * @throws PersistenceException
     *             if any error occurs
     * @since 1.2
     */
    private void createOrUpdateProjectStudioSpecification(long projectId, ProjectStudioSpecification spec,
        Connection conn, String operator) throws PersistenceException {
        if (spec == null) {
            return;
        }
        
        // the studio specification with the specified id exists, just update it
        if (spec.getId() > 0
            && Helper.checkEntityExists("project_studio_specification", "project_studio_spec_id", spec.getId(), conn)) {
            updateProjectStudioSpecification(spec, operator);
        } else { // the studio specification with the specified id does not exist, insert it to the database
            createProjectStudioSpecification(spec, operator);
        }

        // update the project studio specification reference for the project table
        Object[] queryArgs = new Object[]{spec.getId()};
        Helper.doDMLQuery(conn, SET_PROJECT_STUDIO_SPEC_WITH_PROJECT_SQL + projectId, queryArgs);
    }

    /**
     * Returns the database connection name that will be used by DB Connection Factory.
     *
     * @return a possibly <code>null</code> string representing the connection name used in DB Connection Factory.
     */
    protected String getConnectionName() {
        return connectionName;
    }

    /**
     * returns the <code>DBConnectionFactory</code> instance used to create connection to the database.
     *
     * @return the <code>DBConnectionFactory</code> instance used to create connection to the database.
     */
    protected DBConnectionFactory getConnectionFactory() {
        return factory;
    }

    /**
     * <p>
     * Return the getLogger().
     * </p>
     *
     * @return the <code>Log</code> instance used to take the log message
     */
    protected abstract Log getLogger();

    /**
     * <p>
     * Abstract method used to get an open connection from which to perform DB operations.
     * </p>
     * <p>
     * The implementations in subclasses will get a connection and properly prepare it for use, depending on the
     * transaction management strategy used in the subclass.
     * </p>
     *
     * @return an open connection used for DB operation.
     * @throws PersistenceException if there's a problem getting the Connection
     */
    protected abstract Connection openConnection() throws PersistenceException;

    /**
     * <p>
     * Abstract method used to commit any transaction (if necessary in the subclass) and close the given connection
     * after an operation completes successfully.
     * </p>
     * <p>
     * It is used by all public methods after the successful execution of DB operation.
     * </p>
     * <p>
     * The implementations in subclasses will close the given connection. Depending on the transaction management
     * strategy used in the subclass, a transaction on the connection may be committed.
     * </p>
     *
     * @param connection connection to close
     * @throws PersistenceException if any problem occurs trying to close the connection
     * @throws IllegalArgumentException if the argument is <code>null</code>
     */
    protected abstract void closeConnection(Connection connection) throws PersistenceException;

    /**
     * <p>
     * Abstract method used to rollback any transaction (if necessary in the subclass) and close the given connection
     * when an error occurs.
     * </p>
     * <p>
     * It is used by all public methods just before any exception is thrown if fails to do DB operation..
     * </p>
     * <p>
     * The implementations in subclasses will close the given connection. Depending on the transaction management
     * strategy used in the subclass, a transaction on the connection may be rolled back.
     * </p>
     *
     * @param connection connection to close
     * @throws PersistenceException if any problem occurs closing the Connection
     * @throws IllegalArgumentException if the argument is <code>null</code>
     */
    protected abstract void closeConnectionOnError(Connection connection) throws PersistenceException;

    /**
     * Creates the project in the database using the given project instance.
     *
     * @param projectId The new generated project id
     * @param project The project instance to be created in the database.
     * @param operator The creation user of this project.
     * @param conn The database connection
     * @throws PersistenceException if error occurred while accessing the database.
     *
     * @since 1.0
     */
    @SuppressWarnings("unchecked")
    private void createProject(Long projectId, Project project, String operator, Connection conn)
        throws PersistenceException {

        getLogger().log(Level.DEBUG, "insert record into project with id:" + projectId);

        // insert the project into database
        Object[] queryArgs = new Object[]{projectId, project.getProjectStatus().getId(),
                project.getProjectCategory().getId(), operator, operator, project.getTcDirectProjectId()};
        Helper.doDMLQuery(conn, CREATE_PROJECT_SQL, queryArgs);

        // get the creation date.
        Date createDate = (Date) Helper.doSingleValueQuery(conn, "SELECT create_date FROM project WHERE project_id=?",
            new Object[]{projectId}, Helper.DATE_TYPE);

        // set the creation/modification user and date when no exception
        // occurred
        project.setCreationUser(operator);
        project.setCreationTimestamp(createDate);
        project.setModificationUser(operator);
        project.setModificationTimestamp(createDate);

        // get the property id - property value map from the project.
        Map idValueMap = makePropertyIdPropertyValueMap(project.getAllProperties(), conn);

        // create the project properties
        createProjectProperties(projectId, project, idValueMap, operator, conn);
    }

    /**
     * Update the given project instance into the database.
     *
     * @param project The project instance to be updated into the database.
     * @param reason The update reason. It will be stored in the persistence for future references.
     * @param operator The modification user of this project.
     * @param conn the database connection
     * @throws PersistenceException if error occurred while accessing the database.
     */
    @SuppressWarnings("unchecked")
    private void updateProject(Project project, String reason, String operator, Connection conn)
        throws PersistenceException {
        Long projectId = project.getId();

        getLogger().log(Level.DEBUG, new LogMessage(projectId, operator, "update project with projectId:" + projectId));

        Timestamp modifyDate = new Timestamp(System.currentTimeMillis());

        // update the project type and project category
        Object[] queryArgs = new Object[]{project.getProjectStatus().getId(),
                project.getProjectCategory().getId(), operator, modifyDate, project.getTcDirectProjectId() == 0 ? null : project.getTcDirectProjectId(),
            projectId};
        Helper.doDMLQuery(conn, UPDATE_PROJECT_SQL, queryArgs);

        // update the project object so this data's correct for audit purposes
        project.setModificationUser(operator);
        project.setModificationTimestamp(modifyDate);

        // get the property id - property value map from the new project object.
        Map idValueMap = makePropertyIdPropertyValueMap(project.getAllProperties(), conn);

        // update the project properties
        updateProjectProperties(project, idValueMap, operator, conn);

        // create project audit record into project_audit table
        createProjectAudit(projectId, reason, operator, conn);
    }

    /**
     * Makes a property id-property value(String) map from property name-property value map.
     *
     * @param nameValueMap the property name-property value map
     * @param conn the database connection
     * @return a property id-property value map
     * @throws PersistenceException if error occurred while accessing the database.
     */
    @SuppressWarnings("unchecked")
    private Map makePropertyIdPropertyValueMap(Map nameValueMap, Connection conn) throws PersistenceException {
        Map idValueMap = new HashMap();

        // get the property name-property id map
        Map nameIdMap = makePropertyNamePropertyIdMap(getAllProjectPropertyTypes(conn));

        // enumerate each property
        for (Object item : nameValueMap.entrySet()) {
            Entry entry = (Entry) item;

            // find the property id from property name
            Object propertyId = nameIdMap.get(entry.getKey());
            // check if the property id can be found
            if (propertyId == null) {
                throw new PersistenceException("Unable to find ProjectPropertyType name [" + entry.getKey()
                        + "] in project_info_type_lu table.");
            }

            // put the property id-property value(String) pair into the map
            idValueMap.put(propertyId, entry.getValue().toString());
        }
        return idValueMap;
    }

    /**
     * Gets an array of all project property type in the persistence. The project property types are stored in
     * 'project_info_type_lu' table.
     *
     * @param conn the database connection
     * @return An array of all scorecard assignments in the persistence.
     * @throws PersistenceException if error occurred while accessing the database.
     */
    private ProjectPropertyType[] getAllProjectPropertyTypes(Connection conn) throws PersistenceException {
        // find all project property types in the table.
        Object[][] rows = Helper.doQuery(conn, QUERY_ALL_PROJECT_PROPERTY_TYPES_SQL, new Object[]{},
            QUERY_ALL_PROJECT_PROPERTY_TYPES_COLUMN_TYPES);

        // create the ProjectPropertyType array.
        ProjectPropertyType[] propertyTypes = new ProjectPropertyType[rows.length];

        for (int i = 0; i < rows.length; ++i) {
            Object[] row = rows[i];

            // create a new instance of ProjectPropertyType class
            propertyTypes[i] = new ProjectPropertyType((Long) row[0], (String) row[1], (String) row[2]);
        }

        return propertyTypes;
    }

    /**
     * Get projects from sql result.
     *
     * @param result result set from sql query
     * @return An array with project found in persistence.
     * @throws PersistenceException if error occurred while accessing the database.
     */
    public Project[] getProjects(CustomResultSet result) throws PersistenceException {
        Connection conn = null;
        try {
            conn = openConnection();
            PreparedStatement ps = conn.prepareStatement(QUERY_ONE_PROJECT_PROPERTIES_SQL);
            int size = result.getRecordCount();
            Project[] projects = new Project[size];
            for (int i = 0; i < size; i++) {
                result.absolute(i + 1);

                // create the ProjectStatus object
                ProjectStatus status = new ProjectStatus(result.getLong(2), result.getString(3));

                // create the ProjectType object
                ProjectType type = new ProjectType(result.getLong(6), result.getString(7));

                // create the ProjectCategory object
                ProjectCategory category = new ProjectCategory(result.getLong(4), result.getString(5), type);

                // create a new instance of ProjectType class
                projects[i] = new Project(result.getLong(1), category, status);

                // assign the audit information
                projects[i].setCreationUser(result.getString(8));
                projects[i].setCreationTimestamp(result.getDate(9));
                projects[i].setModificationUser(result.getString(10));
                projects[i].setModificationTimestamp(result.getDate(11));

                ps.setLong(1, projects[i].getId());
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    projects[i].setProperty(rs.getString(2), rs.getString(3));
                }
            }
            return projects;
        } catch (NullColumnValueException ncve) {
            throw new PersistenceException("Null value retrieved.", ncve);
        } catch (InvalidCursorStateException icse) {
            throw new PersistenceException("Cursor state is invalid.", icse);
        } catch (SQLException e) {
            throw new PersistenceException(e.getMessage(), e);
        } finally {
            if (conn != null) {
                closeConnection(conn);
            }
        }
    }

    /**
     * <p>
     * Retrieves an array of project instance from the persistence given their ids. The project instances are
     * retrieved with their properties.
     * </p>
     *
     * @param ids The ids of the projects to be retrieved.
     * @param conn the database connection
     * @return An array of project instances.
     * @throws PersistenceException if error occurred while accessing the database.
     */
    @SuppressWarnings("unchecked")
    private Project[] getProjects(long ids[], Connection conn) throws PersistenceException {
        // build the id list string
        StringBuilder idListBuffer = new StringBuilder();
        idListBuffer.append('(');
        for (int i = 0; i < ids.length; ++i) {
            if (i != 0) {
                idListBuffer.append(',');
            }
            idListBuffer.append(ids[i]);
        }
        idListBuffer.append(')');
        // get the id list string
        String idList = idListBuffer.toString();

        // find projects in the table.
        Object[][] rows = Helper
            .doQuery(conn, QUERY_PROJECTS_SQL + idList, new Object[]{}, QUERY_PROJECTS_COLUMN_TYPES);

        // create the Project array.
        Project[] projects = new Project[rows.length];

        for (int i = 0; i < rows.length; ++i) {
            Object[] row = rows[i];

            // create the ProjectStatus object
            ProjectStatus status = new ProjectStatus((Long) row[1], (String) row[2]);

            // create the ProjectType object
            ProjectType type = new ProjectType((Long) row[5], (String) row[6]);

            // create the ProjectCategory object
            ProjectCategory category = new ProjectCategory((Long) row[3], (String) row[4], type);
            category.setDescription((String) row[11]);

            long projectId = (Long) row[0];
            // create a new instance of Project class
            projects[i] = new Project(projectId, category, status);

            // assign the audit information
            projects[i].setCreationUser((String) row[7]);
            projects[i].setCreationTimestamp((Date) row[8]);
            projects[i].setModificationUser((String) row[9]);
            projects[i].setModificationTimestamp((Date) row[10]);

            // set the tc direct project id and name
            projects[i].setTcDirectProjectId(row[12] == null ? 0 : ((Long) row[12]).intValue());
            projects[i].setTcDirectProjectName((String) row[13]);

            // set the file types
            projects[i].setProjectFileTypes(Arrays.asList(getProjectFileTypes(projectId)));

            // set the prizes
            projects[i].setPrizes(Arrays.asList(getProjectPrizes(projectId)));

            // set the studio specification
            projects[i].setProjectStudioSpecification(getProjectStudioSpecification(projectId));
        }

        // get the Id-Project map
        Map projectMap = makeIdProjectMap(projects);

        // find project properties in the table.
        rows = Helper.doQuery(conn, QUERY_PROJECT_PROPERTIES_SQL + idList, new Object[]{},
            QUERY_PROJECT_PROPERTIES_COLUMN_TYPES);

        // enumerate each row
        for (Object[] row : rows) {
            // get the corresponding Project object
            Project project = (Project) projectMap.get(row[0]);

            // set the property to project
            project.setProperty((String) row[1], row[2]);
        }
        return projects;
    }

    /**
     * Gets an array of all project types in the persistence. The project types are stored in 'project_type_lu' table.
     *
     * @param conn the database connection
     * @return An array of all project types in the persistence.
     * @throws PersistenceException if error occurred while accessing the database.
     */
    private ProjectType[] getAllProjectTypes(Connection conn) throws PersistenceException {
        // find all project types in the table.
        Object[][] rows = Helper.doQuery(conn, QUERY_ALL_PROJECT_TYPES_SQL, new Object[]{},
            QUERY_ALL_PROJECT_TYPES_COLUMN_TYPES);

        // create the ProjectType array.
        ProjectType[] projectTypes = new ProjectType[rows.length];

        for (int i = 0; i < rows.length; ++i) {
            Object[] row = rows[i];

            // create a new instance of ProjectType class
            projectTypes[i] = new ProjectType((Long) row[0], (String) row[1], (String) row[2],
                (Boolean) row[3]);
        }

        return projectTypes;
    }

    /**
     * Create the project properties in the database.
     *
     * @param projectId the project's Id
     * @param project The new generated project
     * @param idValueMap The property id - property value map
     * @param operator The creation user of this project
     * @param conn The database connection
     * @throws PersistenceException if error occurred while accessing the database.
     */
    @SuppressWarnings("unchecked")
    private void createProjectProperties(Long projectId, Project project, Map idValueMap, String operator,
        Connection conn) throws PersistenceException {

        getLogger().log(Level.DEBUG,
            new LogMessage(projectId, operator, "insert record into project_info with project id" + projectId));
        PreparedStatement preparedStatement = null;
        try {
            // prepare the statement.
            preparedStatement = conn.prepareStatement(CREATE_PROJECT_PROPERTY_SQL);

            // enumerator each property in the idValueMap
            for (Object item : idValueMap.entrySet()) {
                Entry entry = (Entry) item;

                // insert the project property into database
                Object[] queryArgs = new Object[]{projectId, entry.getKey(), entry.getValue(), operator, operator};
                Helper.doDMLQuery(preparedStatement, queryArgs);

                auditProjectInfo(conn, projectId, project, AUDIT_CREATE_TYPE, (Long) entry.getKey(), (String) entry
                        .getValue());
            }

        } catch (SQLException e) {
            throw new PersistenceException(
                "Unable to create prepared statement [" + CREATE_PROJECT_PROPERTY_SQL + "].", e);
        } finally {
            Helper.closeStatement(preparedStatement);
        }
    }

    /**
     * Make an Id-Project map from Project[].
     *
     * @param projects the Id-Project array
     * @return an Id-Project map
     */
    @SuppressWarnings("unchecked")
    private Map makeIdProjectMap(Project[] projects) {
        Map map = new HashMap();

        for (Project project : projects) {
            map.put(project.getId(), project);
        }
        return map;
    }

    /**
     * Update the project properties into the database.
     *
     * @param project the project object
     * @param idValueMap the property id - property value map
     * @param operator the modification user of this project
     * @param conn the database connection
     * @throws PersistenceException if error occurred while accessing the database.
     */
    @SuppressWarnings("unchecked")
    private void updateProjectProperties(Project project, Map idValueMap, String operator, Connection conn)
        throws PersistenceException {

        Long projectId = project.getId();

        // get old property ids and values from database
        Map<Long, String> propertyMap = getProjectPropertyIdsAndValues(projectId, conn);

        // create a property id-property value map that contains the properties
        // to insert
        Map createIdValueMap = new HashMap();

        PreparedStatement preparedStatement = null;
        try {
            getLogger().log(Level.DEBUG,
                new LogMessage(projectId, operator, "update project, update project_info with projectId:" + projectId));

            // prepare the statement.
            preparedStatement = conn.prepareStatement(UPDATE_PROJECT_PROPERTY_SQL);

            // enumerator each property id in the project object
            for (Object item : idValueMap.entrySet()) {
                Entry entry = (Entry) item;

                Long propertyId = (Long) entry.getKey();

                // check if the property in the project object already exists in
                // the database
                if (propertyMap.containsKey(propertyId)) {
                    // if the value hasn't been changed, we don't need to update anything
                    if (!propertyMap.get(propertyId).equals((String) entry.getValue())) {
                        // update the project property
                        Object[] queryArgs = new Object[]{entry.getValue(), operator, projectId, propertyId};
                        Helper.doDMLQuery(preparedStatement, queryArgs);

                        auditProjectInfo(conn, project, AUDIT_UPDATE_TYPE, propertyId, (String) entry.getValue());
                    }
                    propertyMap.remove(propertyId);
                } else {
                    // add the entry to the createIdValueMap
                    createIdValueMap.put(propertyId, entry.getValue());
                }
            }
        } catch (SQLException e) {
            throw new PersistenceException(
                "Unable to create prepared statement [" + UPDATE_PROJECT_PROPERTY_SQL + "].", e);
        } finally {
            Helper.closeStatement(preparedStatement);
        }

        // create the new properties
        createProjectProperties(project.getId(), project, createIdValueMap, operator, conn);

        // delete the remaining property ids that are not in the project object
        // any longer
        deleteProjectProperties(project, propertyMap.keySet(), conn);
    }

    /**
     * Gets all the property ids associated to this project.
     *
     * @param projectId The id of this project
     * @param conn The database connection
     * @return A set that contains the property ids
     * @throws PersistenceException if error occurred while accessing the database.
     */
    @SuppressWarnings({"unused", "unchecked"})
    private Set getProjectPropertyIds(Long projectId, Connection conn) throws PersistenceException {
        Set idSet = new HashSet();

        // find projects in the table.
        Object[][] rows = Helper.doQuery(conn, QUERY_PROJECT_PROPERTY_IDS_SQL, new Object[]{projectId},
            QUERY_PROJECT_PROPERTY_IDS_COLUMN_TYPES);

        // enumerator each row
        for (Object[] row : rows) {
            // add the id to the set
            idSet.add(row[0]);
        }
        return idSet;
    }

    /**
     * Gets all the property ids and values associated to this project.
     *
     * @param projectId The id of this project
     * @param conn The database connection
     * @return A map that contains the property values, keyed by id
     *
     * @throws PersistenceException if error occurred while accessing the database.
     */
    private Map<Long, String> getProjectPropertyIdsAndValues(Long projectId, Connection conn)
        throws PersistenceException {

        Map<Long, String> idMap = new HashMap<Long, String>();

        // find projects in the table.
        Object[][] rows = Helper.doQuery(conn, QUERY_PROJECT_PROPERTY_IDS_AND_VALUES_SQL, new Object[] {projectId},
            QUERY_PROJECT_PROPERTY_IDS_AND_VALUES_COLUMN_TYPES);

        // enumerator each row
        for (Object[] row : rows) {
            // add the id to the map
            idMap.put((Long) row[0], (String) row[1]);
        }

        return idMap;
    }

    /**
     * Delete the project properties from the database.
     *
     * @param project the project object
     * @param propertyIdSet the Set that contains the property ids to delete
     * @param conn the database connection
     * @throws PersistenceException if error occurred while accessing the database.
     */
    private void deleteProjectProperties(Project project, Set<Long> propertyIdSet, Connection conn)
        throws PersistenceException {

        Long projectId = project.getId();

        // check if the property id set is empty
        // do nothing if property id set is empty
        if (!propertyIdSet.isEmpty()) {

            // build the id list string
            StringBuilder idListBuffer = new StringBuilder();
            idListBuffer.append('(');
            int idx = 0;
            for (Long id : propertyIdSet) {
                if (idx++ != 0) {
                    idListBuffer.append(',');
                }
                idListBuffer.append(id);
            }
            idListBuffer.append(')');

            getLogger().log(Level.DEBUG,
                new LogMessage(projectId, null, "delete records from project_info with projectId:" + projectId));

            // delete the properties whose id is in the set
            Helper.doDMLQuery(conn, DELETE_PROJECT_PROPERTIES_SQL + idListBuffer.toString(), new Object[]{projectId});

            for (Long id : propertyIdSet) {
                auditProjectInfo(conn, project, AUDIT_DELETE_TYPE, id, null);
            }
        }
    }

    /**
     * Create a project audit record into the database.
     *
     * @param projectId The id of the project
     * @param reason The update reason
     * @param operator the modification user of this project
     * @param conn the database connection
     * @throws PersistenceException if error occurred while accessing the database.
     */
    private void createProjectAudit(Long projectId, String reason, String operator, Connection conn)
        throws PersistenceException {

        Long auditId;
        try {
            // generate a new audit id
            auditId = projectAuditIdGenerator.getNextID();
            getLogger().log(Level.DEBUG, new LogMessage(projectId, operator, "generate new id for the project audit."));
        } catch (IDGenerationException e) {
            throw new PersistenceException("Unable to generate id for project.", e);
        }
        getLogger().log(Level.DEBUG, "insert record into project_audit with projectId:" + projectId);
        // insert the update reason information to project_audit table
        Object[] queryArgs = new Object[]{auditId, projectId, reason, operator, operator};
        Helper.doDMLQuery(conn, CREATE_PROJECT_AUDIT_SQL, queryArgs);
    }

    /**
     * Make a property name - property id map from ProjectPropertyType[].
     *
     * @param propertyTypes the ProjectPropertyType array
     * @return a property name - property id map
     * @throws PersistenceException if duplicate property type names are found
     */
    @SuppressWarnings("unchecked")
    private Map makePropertyNamePropertyIdMap(ProjectPropertyType[] propertyTypes) throws PersistenceException {
        Map map = new HashMap();

        // enumerate each property type
        for (ProjectPropertyType propertyType : propertyTypes) {

            // check if the property name already exists
            if (map.containsKey(propertyType.getName())) {
                throw new PersistenceException("Duplicate project property type names [" + propertyType.getName()
                        + "] found in project_info_type_lu table.");
            }

            // put the name-id pair to the map
            map.put(propertyType.getName(), propertyType.getId());
        }
        return map;
    }

    /**
     * Gets an array of all project categories in the persistence. The project categories are stored in
     * 'project_category_lu' table.
     *
     * @param conn the database connection
     * @return An array of all project categories in the persistence.
     * @throws PersistenceException if error occurred while accessing the database.
     */
    private ProjectCategory[] getAllProjectCategories(Connection conn) throws PersistenceException {
        // find all project categories in the table.
        Object[][] rows = Helper.doQuery(conn, QUERY_ALL_PROJECT_CATEGORIES_SQL, new Object[] {},
            QUERY_ALL_PROJECT_CATEGORIES_COLUMN_TYPES);

        // create the ProjectCategory array.
        ProjectCategory[] projectCategories = new ProjectCategory[rows.length];

        for (int i = 0; i < rows.length; ++i) {
            Object[] row = rows[i];

            // create the ProjectType object
            ProjectType type = new ProjectType((Long) row[3], (String) row[4], (String) row[5], (Boolean) row[6]);

            // create a new instance of ProjectCategory class
            projectCategories[i] = new ProjectCategory((Long) row[0], (String) row[1], (String) row[2], type);
        }

        return projectCategories;
    }

    /**
     * Gets an array of all project statuses in the persistence. The project statuses are stored in
     * 'project_status_lu' table.
     *
     * @param conn the database connection
     * @return An array of all project statuses in the persistence.
     * @throws PersistenceException if error occurred while accessing the database.
     */
    private ProjectStatus[] getAllProjectStatuses(Connection conn) throws PersistenceException {
        // find all project statuses in the table.
        Object[][] rows = Helper.doQuery(conn, QUERY_ALL_PROJECT_STATUSES_SQL, new Object[]{},
            QUERY_ALL_PROJECT_STATUSES_COLUMN_TYPES);

        // create the ProjectStatus array.
        ProjectStatus[] projectStatuses = new ProjectStatus[rows.length];

        for (int i = 0; i < rows.length; ++i) {
            Object[] row = rows[i];

            // create a new instance of ProjectStatus class
            projectStatuses[i] = new ProjectStatus((Long) row[0], (String) row[1], (String) row[2]);
        }

        return projectStatuses;
    }

    /**
     * This method will audit project information. This information is generated when most project properties are
     * inserted, deleted, or edited.
     *
     * @param connection the connection to database
     * @param projectId the id of the project being audited
     * @param project the project being audited
     * @param auditType the audit type. Can be AUDIT_CREATE_TYPE, AUDIT_DELETE_TYPE, or AUDIT_UPDATE_TYPE
     * @param projectInfoTypeId the project info type id
     * @param value the project info value that we're changing to
     *
     * @throws PersistenceException if validation error occurs or any error occurs in the underlying layer
     *
     * @since 1.1.2
     */
    private void auditProjectInfo(Connection connection, Long projectId, Project project, int auditType,
        long projectInfoTypeId, String value) throws PersistenceException {

        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(PROJECT_INFO_AUDIT_INSERT_SQL);

            int index = 1;
            statement.setLong(index++, projectId);
            statement.setLong(index++, projectInfoTypeId);
            statement.setString(index++, value);
            statement.setInt(index++, auditType);
            statement.setTimestamp(index++, new Timestamp(project.getModificationTimestamp().getTime()));
            statement.setString(index++, project.getModificationUser());

            if (statement.executeUpdate() != 1) {
                throw new PersistenceException("Audit information was not successfully saved.");
            }
        } catch (SQLException e) {
            closeConnectionOnError(connection);
            throw new PersistenceException("Unable to insert project_info_audit record.", e);
        } finally {
            Helper.closeStatement(statement);
        }
    }

    /**
     * This method will audit project information. This information is generated when most project properties are
     * inserted, deleted, or edited.
     *
     * @param connection the connection to database
     * @param project the project being audited
     * @param auditType the audit type. Can be AUDIT_CREATE_TYPE, AUDIT_DELETE_TYPE, or AUDIT_UPDATE_TYPE
     * @param projectInfoTypeId the project info type id
     * @param value the project info value that we're changing to
     *
     * @throws PersistenceException if validation error occurs or any error occurs in the underlying layer
     *
     * @since 1.1.2
     */
    private void auditProjectInfo(Connection connection, Project project, int auditType, long projectInfoTypeId,
        String value) throws PersistenceException {
        auditProjectInfo(connection, project.getId(), project, auditType, projectInfoTypeId, value);
    }

}
