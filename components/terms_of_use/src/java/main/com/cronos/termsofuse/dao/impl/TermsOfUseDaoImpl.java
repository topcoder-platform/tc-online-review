/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.termsofuse.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.cronos.termsofuse.dao.EntityNotFoundException;
import com.cronos.termsofuse.dao.TermsOfUseCyclicDependencyException;
import com.cronos.termsofuse.dao.TermsOfUseDao;
import com.cronos.termsofuse.dao.TermsOfUseDaoConfigurationException;
import com.cronos.termsofuse.dao.TermsOfUseDependencyNotFoundException;
import com.cronos.termsofuse.dao.TermsOfUsePersistenceException;
import com.cronos.termsofuse.model.TermsOfUse;
import com.cronos.termsofuse.model.TermsOfUseAgreeabilityType;
import com.cronos.termsofuse.model.TermsOfUseType;
import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.util.idgenerator.IDGenerationException;
import com.topcoder.util.idgenerator.IDGenerator;
import com.topcoder.util.idgenerator.IDGeneratorFactory;
import com.topcoder.util.log.Log;

/**
 * <p>
 * This class is the default implementation of TermsOfUseDao. It utilizes the DB Connection Factory to get access to
 * the database. The configuration is done by the Configuration API.
 * </p>
 *
 * <p>
 * <em>Sample Configuration:</em>
 * <pre>
 * &lt;CMConfig&gt;
 *     &lt;Config name=&quot;termsOfUseDao&quot;&gt;
 *      &lt;Property name=&quot;dbConnectionFactoryConfig&quot;&gt;
 *        &lt;Property name=&quot;com.topcoder.db.connectionfactory.DBConnectionFactoryImpl&quot;&gt;
 *          &lt;Property name=&quot;connections&quot;&gt;
 *                 &lt;Property name=&quot;default&quot;&gt;
 *                     &lt;Value&gt;InformixJDBCConnection&lt;/Value&gt;
 *                 &lt;/Property&gt;
 *              &lt;Property name=&quot;InformixJDBCConnection&quot;&gt;
 *                  &lt;Property name=&quot;producer&quot;&gt;
 *                      &lt;Value&gt;com.topcoder.db.connectionfactory.producers.JDBCConnectionProducer&lt;/Value&gt;
 *                  &lt;/Property&gt;
 *                  &lt;Property name=&quot;parameters&quot;&gt;
 *                      &lt;Property name=&quot;jdbc_driver&quot;&gt;
 *                      &lt;Value&gt;com.informix.jdbc.IfxDriver&lt;/Value&gt;
 *                      &lt;/Property&gt;
 *                      &lt;Property name=&quot;jdbc_url&quot;&gt;
 *                              &lt;Value&gt;
 *                                  jdbc:informix-sqli://localhost:1526/common_oltp:informixserver=ol_topcoder
 *                              &lt;/Value&gt;
 *                      &lt;/Property&gt;
 *                      &lt;Property name=&quot;user&quot;&gt;
 *                          &lt;Value&gt;informix&lt;/Value&gt;
 *                      &lt;/Property&gt;
 *                      &lt;Property name=&quot;password&quot;&gt;
 *                          &lt;Value&gt;123456&lt;/Value&gt;
 *                      &lt;/Property&gt;
 *                  &lt;/Property&gt;
 *              &lt;/Property&gt;
 *          &lt;/Property&gt;
 *        &lt;/Property&gt;
 *      &lt;/Property&gt;
 *      &lt;Property name=&quot;loggerName&quot;&gt;
 *          &lt;Value&gt;loggerName&lt;/Value&gt;
 *      &lt;/Property&gt;
 *      &lt;Property name=&quot;idGeneratorName&quot;&gt;
 *          &lt;Value&gt;idGenerator&lt;/Value&gt;
 *      &lt;/Property&gt;
 *     &lt;/Config&gt;
 * &lt;/CMConfig&gt;
 * </pre>
 *
 * </p>
 *
 * <p>
 * <em>Sample Code:</em>
 * <pre>
 * // Create the configuration object
 * ConfigurationObject configurationObject = TestsHelper.getConfig(TestsHelper.CONFIG_TERMS);
 * // Instantiate the dao implementation from configuration defined above
 * TermsOfUseDao termsOfUseDao = new TermsOfUseDaoImpl(configurationObject);
 *
 * // Create simple TermsOfUse to persist
 * TermsOfUse terms = new TermsOfUse();
 *
 * terms.setTermsOfUseTypeId(3);
 * terms.setTitle(&quot;t5&quot;);
 * terms.setUrl(&quot;url5&quot;);
 * TermsOfUseAgreeabilityType nonAgreeableType = new TermsOfUseAgreeabilityType();
 * nonAgreeableType.setTermsOfUseAgreeabilityTypeId(1);
 * terms.setAgreeabilityType(nonAgreeableType);
 *
 * // Persist the TermsOfUse
 * terms = termsOfUseDao.createTermsOfUse(terms, &quot;&quot;);
 *
 * // Set terms of use text
 * termsOfUseDao.setTermsOfUseText(terms.getTermsOfUseId(), &quot;text5&quot;);
 *
 * // Get terms of use text. This will return &quot;text5&quot;.
 * String termsOfUseText = termsOfUseDao.getTermsOfUseText(terms.getTermsOfUseId());
 *
 * // Update some information for TermsOfUse
 * TermsOfUseAgreeabilityType electronicallyAgreeableType = new TermsOfUseAgreeabilityType();
 * electronicallyAgreeableType.setTermsOfUseAgreeabilityTypeId(3);
 * terms.setAgreeabilityType(electronicallyAgreeableType);
 *
 * // And update the TermsOfUse
 * terms = termsOfUseDao.updateTermsOfUse(terms);
 *
 * // Retrieve some terms of use. The third row will be returned
 * terms = termsOfUseDao.getTermsOfUse(3);
 * // terms.getAgreeabilityType().getTermsOfUseAgreeabilityTypeId() must be 1
 * // terms.getAgreeabilityType().getName() must be &quot;Non-agreeable&quot;
 *
 * // Delete terms of use
 * termsOfUseDao.deleteTermsOfUse(5);
 *
 * // Retrieve all terms of use. All rows will be returned
 * List&lt;TermsOfUse&gt; allTerms = termsOfUseDao.getAllTermsOfUse();
 *
 * // Create the following dependency relationships between terms of use with the specified IDs:
 * // (1) depends on (2)
 * // (2) depends on (3,4)
 * // (3) depends on (4)
 * termsOfUseDao.createDependencyRelationship(1, 2);
 * termsOfUseDao.createDependencyRelationship(2, 3);
 * termsOfUseDao.createDependencyRelationship(2, 4);
 * termsOfUseDao.createDependencyRelationship(3, 4);
 *
 * try {
 *     // Try to make a loop; TermsOfUseCyclicDependencyException must be thrown
 *     termsOfUseDao.createDependencyRelationship(4, 1);
 * } catch (TermsOfUseCyclicDependencyException e) {
 *     // Good
 * }
 *
 * // Retrieve the dependencies of terms of use with ID=2
 * List&lt;TermsOfUse&gt; termsOfUseList = termsOfUseDao.getDependencyTermsOfUse(2);
 * // termsOfUseList.size() must be 2
 * // termsOfUseList.get(0).getTermsOfUseId() must be 3
 * // termsOfUseList.get(0).getTermsOfUseTypeId() must be 1
 * // termsOfUseList.get(0).getTitle() must be &quot;t3&quot;
 * // termsOfUseList.get(0).getUrl() must be &quot;url3&quot;
 * // termsOfUseList.get(0).getAgreeabilityType().getTermsOfUseAgreeabilityTypeId() must be 1
 * // termsOfUseList.get(0).getAgreeabilityType().getName() must be &quot;Non-agreeable&quot;
 * // termsOfUseList.get(0).getAgreeabilityType().getDescription() must be &quot;Non-agreeable&quot;
 * // termsOfUseList.get(1).getTermsOfUseId() must be 4
 * // termsOfUseList.get(1).getTermsOfUseTypeId() must be 2
 * // termsOfUseList.get(1).getTitle() must be &quot;t4&quot;
 * // termsOfUseList.get(1).getUrl() must be &quot;url4&quot;
 * // termsOfUseList.get(1).getAgreeabilityType().getTermsOfUseAgreeabilityTypeId() must be 3
 * // termsOfUseList.get(1).getAgreeabilityType().getName() must be &quot;Electronically-agreeable&quot;
 * // termsOfUseList.get(1).getAgreeabilityType().getDescription() must be &quot;Electronically-agreeable&quot;
 * // Note: the order of elements in termsOfUseList can vary
 *
 * // Retrieve the dependents of terms of use with ID=2
 * termsOfUseList = termsOfUseDao.getDependentTermsOfUse(2);
 * // termsOfUseList.size() must be 1
 * // termsOfUseList.get(0).getTermsOfUseId() must be 1
 * // termsOfUseList.get(0).getTermsOfUseTypeId() must be 1
 * // termsOfUseList.get(0).getTitle() must be &quot;t1&quot;
 * // termsOfUseList.get(0).getUrl() must be &quot;url1&quot;
 * // termsOfUseList.get(0).getAgreeabilityType().getTermsOfUseAgreeabilityTypeId() must be 2
 * // termsOfUseList.get(0).getAgreeabilityType().getName() must be &quot;Non-electronically-agreeable&quot;
 * // termsOfUseList.get(0).getAgreeabilityType().getDescription() must be &quot;Non-electronically-agreeable&quot;
 *
 * // Delete the dependency relationship between terms of use with IDs=2,4
 * termsOfUseDao.deleteDependencyRelationship(2, 4);
 *
 * // Delete all dependency relationships where terms of use with ID=2 is a dependent
 * termsOfUseDao.deleteAllDependencyRelationshipsForDependent(2);
 *
 * // Delete all dependency relationships where terms of use with ID=4 is a dependency
 * termsOfUseDao.deleteAllDependencyRelationshipsForDependency(4);
 * </pre>
 *
 * </p>
 *
 * <p>
 * <em>Changes in 1.1:</em>
 * <ol>
 * <li>Added methods for managing terms of use dependencies.</li>
 * <li>Updated queries to support removal of memberAgreeable and electronicallySignable properties of TermsOfUse.</li>
 * <li>Updated queries to support adding of TermsOfUse#agreeabilityType property.</li>
 * </ol>
 * </p>
 *
 * <p>
 * <strong>Thread Safety: </strong> The class is immutable and thread - safe. createDependencyRelationship() is made
 * synchronized to avoid creation of cyclic dependency by calling this method from multiple threads at a time.
 * </p>
 *
 * @author faeton, sparemax, saarixx
 * @version 1.1
 */
public class TermsOfUseDaoImpl extends BaseTermsOfUseDao implements TermsOfUseDao {
    /**
     * <p>
     * Represents the class name.
     * </p>
     */
    private static final String CLASS_NAME = TermsOfUseDaoImpl.class.getName();

    /**
     * <p>
     * Represents the property key 'idGeneratorName'.
     * </p>
     */
    private static final String KEY_ID_GENERATOR_NAME = "idGeneratorName";

    /**
     * <p>
     * Represents the SQL string to insert a terms of use entity.
     * </p>
     *
     * <p>
     * <em>Changes in 1.1:</em>
     * <ol>
     * <li>Removed electronically_signable and member_agreeable columns.</li>
     * <li>Added terms_of_use_agreeability_type_id column.</li>
     * </ol>
     * </p>
     */
    private static final String INSERT_TERMS = "INSERT INTO terms_of_use (terms_of_use_id, terms_text,"
        + " terms_of_use_type_id, title, url, terms_of_use_agreeability_type_id) VALUES (?, ?, ?, ?, ?, ?)";

    /**
     * <p>
     * Represents the SQL string to update the terms of use entity.
     * </p>
     *
     * <p>
     * <em>Changes in 1.1:</em>
     * <ol>
     * <li>Removed electronically_signable and member_agreeable columns.</li>
     * <li>Added terms_of_use_agreeability_type_id column.</li>
     * </ol>
     * </p>
     */
    private static final String UPDATE_TERMS = "UPDATE terms_of_use SET terms_of_use_type_id = ?, title = ?,"
        + " url = ?, terms_of_use_agreeability_type_id=? WHERE terms_of_use_id = ?";


    /**
     * <p>
     * Represents the SQL string to query a terms of use entity.
     * </p>
     *
     * <p>
     * <em>Changes in 1.1:</em>
     * <ol>
     * <li>Removed electronically_signable and member_agreeable columns.</li>
     * <li>Added JOIN to terms_of_use_agreeability_type_lu table.</li>
     * </ol>
     * </p>
     */
    private static final String QUERY_TERMS = "SELECT terms_of_use_type_id, title, url,"
        + " tou.terms_of_use_agreeability_type_id, touat.name as terms_of_use_agreeability_type_name,"
        + " touat.description as terms_of_use_agreeability_type_description FROM terms_of_use tou"
        + " INNER JOIN terms_of_use_agreeability_type_lu touat ON touat.terms_of_use_agreeability_type_id"
        + " = tou.terms_of_use_agreeability_type_id WHERE terms_of_use_id=?";

    /**
     * <p>
     * Represents the SQL string to delete a terms of use entity.
     * </p>
     */
    private static final String DELETE_TERMS = "DELETE FROM terms_of_use WHERE terms_of_use_id=?";


    /**
     * <p>
     * Represents the SQL string to query terms of use entities by the terms of use type id.
     * </p>
     *
     * <p>
     * <em>Changes in 1.1:</em>
     * <ol>
     * <li>Removed electronically_signable and member_agreeable columns.</li>
     * <li>Added JOIN to terms_of_use_agreeability_type_lu table.</li>
     * </ol>
     * </p>
     */
    private static final String QUERY_TERMS_BY_TYPE_ID =
        "SELECT terms_of_use_id, title, url, tou.terms_of_use_agreeability_type_id,"
        + " touat.name as terms_of_use_agreeability_type_name,"
        + " touat.description as terms_of_use_agreeability_type_description"
        + " FROM terms_of_use tou INNER JOIN terms_of_use_agreeability_type_lu touat"
        + " ON touat.terms_of_use_agreeability_type_id = tou.terms_of_use_agreeability_type_id"
        + " WHERE terms_of_use_type_id=?";


    /**
     * <p>
     * Represents the SQL string to query all terms of use entities.
     * </p>
     *
     * <p>
     * <em>Changes in 1.1:</em>
     * <ol>
     * <li>Removed electronically_signable and member_agreeable columns.</li>
     * <li>Added JOIN to terms_of_use_agreeability_type_lu table.</li>
     * </ol>
     * </p>
     */
    private static final String QUERY_ALL_TERMS = "SELECT terms_of_use_id, terms_of_use_type_id, title, url,"
        + " tou.terms_of_use_agreeability_type_id, touat.name as terms_of_use_agreeability_type_name,"
        + " touat.description as terms_of_use_agreeability_type_description FROM terms_of_use tou"
        + " INNER JOIN terms_of_use_agreeability_type_lu touat ON touat.terms_of_use_agreeability_type_id"
        + " = tou.terms_of_use_agreeability_type_id";

    /**
     * <p>
     * Represents the SQL string to query the terms of use type.
     * </p>
     */
    private static final String QUERY_TERMS_TYPE = "SELECT terms_of_use_type_desc FROM terms_of_use_type"
        + " WHERE terms_of_use_type_id=?";

    /**
     * <p>
     * Represents the SQL string to update the terms of use type.
     * </p>
     */
    private static final String UPDATE_TERMS_TYPE = "UPDATE terms_of_use_type SET terms_of_use_type_desc = ?"
        + " WHERE terms_of_use_type_id = ?";


    /**
     * <p>
     * Represents the SQL string to query the terms text.
     * </p>
     */
    private static final String QUERY_TERMS_TEXT = "SELECT terms_text FROM terms_of_use WHERE terms_of_use_id=?";


    /**
     * <p>
     * Represents the SQL string to update the terms text.
     * </p>
     */
    private static final String UPDATE_TERMS_TEXT = "UPDATE terms_of_use SET terms_text=? WHERE terms_of_use_id=?";
    /**
     * Represents the SQL string to be used for inserting a terms of use dependency record.
     *
     * @since 1.1
     */
    private static final String INSERT_DEPENDENCY =
        "INSERT INTO terms_of_use_dependency(dependency_terms_of_use_id, dependent_terms_of_use_id) VALUES (?, ?)";

    /**
     * Represents the SQL string to query IDs of all dependencies of a list of terms of use.
     *
     * @since 1.1
     */
    private static final String QUERY_DEPENDENCY_IDS =
        "SELECT DISTINCT dependency_terms_of_use_id FROM terms_of_use_dependency"
        + " WHERE dependent_terms_of_use_id IN (%1$s)";

    /**
     * Represents the SQL string to query IDs of all direct dependencies of a list of terms of use.
     *
     * @since 1.1
     */
    private static final String QUERY_DEPENDENCIES =
        "SELECT terms_of_use_id, terms_of_use_type_id, title, url, tou.terms_of_use_agreeability_type_id,"
        + " touat.name as terms_of_use_agreeability_type_name,"
        + " touat.description as terms_of_use_agreeability_type_description FROM terms_of_use_dependency d"
        + " INNER JOIN terms_of_use tou ON tou.terms_of_use_id = d.dependency_terms_of_use_id"
        + " INNER JOIN terms_of_use_agreeability_type_lu touat ON touat.terms_of_use_agreeability_type_id"
        + " = tou.terms_of_use_agreeability_type_id WHERE d.dependent_terms_of_use_id = ?";

    /**
     * Represents the SQL string to query IDs of all direct dependents of a list of terms of use.
     *
     * @since 1.1
     */
    private static final String QUERY_DEPENDENTS =
        "SELECT terms_of_use_id, terms_of_use_type_id, title, url, tou.terms_of_use_agreeability_type_id,"
        + " touat.name as terms_of_use_agreeability_type_name,"
        + " touat.description as terms_of_use_agreeability_type_description FROM terms_of_use_dependency d"
        + " INNER JOIN terms_of_use tou ON tou.terms_of_use_id = d.dependent_terms_of_use_id"
        + " INNER JOIN terms_of_use_agreeability_type_lu touat ON touat.terms_of_use_agreeability_type_id"
        + " = tou.terms_of_use_agreeability_type_id WHERE d.dependency_terms_of_use_id = ?";

    /**
     * Represents the SQL string to delete one dependency relationship record.
     *
     * @since 1.1
     */
    private static final String DELETE_DEPENDENCY =
        "DELETE FROM terms_of_use_dependency WHERE dependency_terms_of_use_id = ? AND dependent_terms_of_use_id = ?";

    /**
     * Represents the SQL string to delete all relationships for specific dependent terms of use.
     *
     * @since 1.1
     */
    private static final String DELETE_RELATIONSHIPS_FOR_DEPENDENT =
        "DELETE FROM terms_of_use_dependency WHERE dependent_terms_of_use_id = ?";

    /**
     * Represents the SQL string to delete all relationships for specific dependency terms of use.
     *
     * @since 1.1
     */
    private static final String DELETE_RELATIONSHIPS_FOR_DEPENDENCY =
        "DELETE FROM terms_of_use_dependency WHERE dependency_terms_of_use_id = ?";

    /**
     * <p>
     * The id generator, used to generate ids for the entities.
     * </p>
     *
     * <p>
     * It is initialized in the constructor and never changed afterwards. It is used in the dao operations whenever
     * generation of id is necessary. It can not be null.
     * </p>
     */
    private final IDGenerator idGenerator;

    /**
     * This is the constructor with the ConfigurationObject parameter, which simply delegates the instance
     * initialization to the base class and additionally initializes id generator.
     *
     * @param configurationObject
     *            the configuration object containing the configuration.
     *
     * @throws IllegalArgumentException
     *             if the configurationObject is null.
     * @throws TermsOfUseDaoConfigurationException
     *             if any exception occurs while initializing the instance.
     */
    public TermsOfUseDaoImpl(ConfigurationObject configurationObject) {
        super(configurationObject);

        try {
            idGenerator = IDGeneratorFactory.getIDGenerator(Helper.getRequiredProperty(configurationObject,
                KEY_ID_GENERATOR_NAME));
        } catch (IDGenerationException e) {
            throw new TermsOfUseDaoConfigurationException("Failed to retrieve an id generator.", e);
        }
    }

    /**
     * <p>
     * Creates terms of use entity with the terms text.
     * </p>
     *
     * <p>
     * <em>Changes in 1.1:</em>
     * <ol>
     * <li>Throws IllegalArgumentException when termsOfUse.getAgreeabilityType() is null.</li>
     * <li>Support removal of memberAgreeable and electronicallySignable properties.</li>
     * <li>Support adding of agreeabilityType property.</li>
     * </ol>
     * </p>
     *
     * @param termsOfUse
     *            a TermsOfUse containing required information for creation.
     * @param termsText
     *            the terms text to create.
     *
     * @return a TermsOfUse with created id attribute.
     *
     * @throws IllegalArgumentException
     *             if termsOfUseor or termsOfUse.getAgreeabilityType() is null.
     * @throws TermsOfUsePersistenceException
     *             if any persistence error occurs.
     */
    public TermsOfUse createTermsOfUse(TermsOfUse termsOfUse, String termsText) throws TermsOfUsePersistenceException {
        String signature = CLASS_NAME + ".createTermsOfUse(TermsOfUse termsOfUse, String termsText)";
        Log log = getLog();

        // Log method entry
        Helper.logEntrance(log, signature,
            new String[] {"termsOfUse", "termsText"},
            new Object[] {termsOfUse, termsText});

        try {
            Helper.checkNull(termsOfUse, "termsOfUse");

            TermsOfUseAgreeabilityType agreeabilityType = termsOfUse.getAgreeabilityType();
            Helper.checkNull(agreeabilityType, "termsOfUse.getAgreeabilityType()");

            long termsOfUseId;
            try {
                termsOfUseId = idGenerator.getNextID();
            } catch (IDGenerationException e) {
                throw new TermsOfUsePersistenceException("Failed to generate a new ID.", e);
            }
            termsOfUse.setTermsOfUseId(termsOfUseId);

            Helper.executeUpdate(getDBConnectionFactory(), INSERT_TERMS,
                new Object[] {termsOfUseId, (termsText == null) ? null : termsText.getBytes(),
                    termsOfUse.getTermsOfUseTypeId(), termsOfUse.getTitle(),
                    termsOfUse.getUrl(), agreeabilityType.getTermsOfUseAgreeabilityTypeId()});

            // Log method exit
            Helper.logExit(log, signature, new Object[] {termsOfUse});
            return termsOfUse;
        } catch (IllegalArgumentException e) {
            // Log exception
            throw Helper.logException(log, signature, e);
        } catch (TermsOfUsePersistenceException e) {
            // Log exception
            throw Helper.logException(log, signature, e);
        }
    }

    /**
     * <p>
     * Updates terms of use entity.
     * </p>
     *
     * <p>
     * <em>Changes in 1.1:</em>
     * <ol>
     * <li>Throws IllegalArgumentException when termsOfUse.getAgreeabilityType() is null.</li>
     * <li>Support removal of memberAgreeable and electronicallySignable properties.</li>
     * <li>Support adding of agreeabilityType property.</li>
     * </ol>
     * </p>
     *
     * @param termsOfUse
     *            a TermsOfUse containing required information for update.
     *
     * @return a TermsOfUse with updated id attribute.
     *
     * @throws IllegalArgumentException
     *             if termsOfUse or termsOfUse.getAgreeabilityType() is null.
     * @throws EntityNotFoundException
     *             if the entity was not found in the database.
     * @throws TermsOfUsePersistenceException
     *             if any persistence error occurs.
     */
    public TermsOfUse updateTermsOfUse(TermsOfUse termsOfUse) throws TermsOfUsePersistenceException,
        EntityNotFoundException {
        String signature = CLASS_NAME + ".updateTermsOfUse(TermsOfUse termsOfUse)";
        Log log = getLog();

        // Log method entry
        Helper.logEntrance(log, signature,
            new String[] {"termsOfUse"},
            new Object[] {termsOfUse});

        try {
            Helper.checkNull(termsOfUse, "termsOfUse");

            TermsOfUseAgreeabilityType agreeabilityType = termsOfUse.getAgreeabilityType();
            Helper.checkNull(agreeabilityType, "termsOfUse.getAgreeabilityType()");

            Helper.executeUpdate(getDBConnectionFactory(), UPDATE_TERMS,
                new Object[] {termsOfUse.getTermsOfUseTypeId(), termsOfUse.getTitle(),
                    termsOfUse.getUrl(), agreeabilityType.getTermsOfUseAgreeabilityTypeId(),
                    termsOfUse.getTermsOfUseId()},
                Long.toString(termsOfUse.getTermsOfUseId()));

            // Log method exit
            Helper.logExit(log, signature, new Object[] {termsOfUse});
            return termsOfUse;
        } catch (IllegalArgumentException e) {
            // Log exception
            throw Helper.logException(log, signature, e);
        } catch (EntityNotFoundException e) {
            // Log exception
            throw Helper.logException(log, signature, e);
        } catch (TermsOfUsePersistenceException e) {
            // Log exception
            throw Helper.logException(log, signature, e);
        }
    }

    /**
     * Retrieves a terms of use entity from the database.
     *
     * @param termsOfUseId
     *            a long containing the terms of use id to retrieve.
     *
     * @return a TermsOfUse with the requested terms of use or null if not found.
     *
     * @throws TermsOfUsePersistenceException
     *             if any persistence error occurs.
     */
    public TermsOfUse getTermsOfUse(long termsOfUseId) throws TermsOfUsePersistenceException {
        String signature = CLASS_NAME + ".getTermsOfUse(long termsOfUseId)";
        Log log = getLog();

        // Log method entry
        Helper.logEntrance(log, signature,
            new String[] {"termsOfUseId"},
            new Object[] {termsOfUseId});

        try {
            Connection conn = Helper.createConnection(getDBConnectionFactory());
            PreparedStatement ps = null;

            try {
                ps = conn.prepareStatement(QUERY_TERMS);
                ps.setLong(1, termsOfUseId);

                ResultSet rs = ps.executeQuery();
                TermsOfUse terms = null;
                if (rs.next()) {
                    terms = Helper.getTermsOfUse(rs, termsOfUseId, null);
                }

                // Log method exit
                Helper.logExit(log, signature, new Object[] {terms});
                return terms;
            } catch (SQLException e) {
                throw new TermsOfUsePersistenceException("A database access error has occurred.", e);
            } finally {
                // Close the prepared statement
                // (The result set will be closed automatically)
                Helper.closeStatement(ps);
                // Close the connection
                Helper.closeConnection(conn);
            }
        } catch (TermsOfUsePersistenceException e) {
            // Log exception
            throw Helper.logException(log, signature, e);
        }
    }

    /**
     * Deletes a terms of use entity from the database.
     *
     * @param termsOfUseId
     *            a long containing the terms of use id to delete.
     *
     * @throws EntityNotFoundException
     *             if the entity was not found in the database.
     * @throws TermsOfUsePersistenceException
     *             if any persistence error occurs.
     */
    public void deleteTermsOfUse(long termsOfUseId) throws EntityNotFoundException, TermsOfUsePersistenceException {
        String signature = CLASS_NAME + ".deleteTermsOfUse(long termsOfUseId)";
        Log log = getLog();

        // Log method entry
        Helper.logEntrance(log, signature,
            new String[] {"termsOfUseId"},
            new Object[] {termsOfUseId});

        try {
            Helper.executeUpdate(getDBConnectionFactory(), DELETE_TERMS,
                new Object[] {termsOfUseId},
                Long.toString(termsOfUseId));

            // Log method exit
            Helper.logExit(log, signature, null);
        } catch (EntityNotFoundException e) {
            // Log exception
            throw Helper.logException(log, signature, e);
        } catch (TermsOfUsePersistenceException e) {
            // Log exception
            throw Helper.logException(log, signature, e);
        }
    }

    /**
     * Retrieves a terms of use entities by the terms of use type id from the database.
     *
     * @param termsOfUseTypeId
     *            an int containing the terms of use type id to retrieve.
     *
     * @return a list of TermsOfUse entities with the requested terms of use or empty list if not found.
     *
     * @throws TermsOfUsePersistenceException
     *             if any persistence error occurs.
     */
    public List<TermsOfUse> getTermsOfUseByTypeId(int termsOfUseTypeId) throws TermsOfUsePersistenceException {
        String signature = CLASS_NAME + ".getTermsOfUseByTypeId(int termsOfUseTypeId)";
        Log log = getLog();

        // Log method entry
        Helper.logEntrance(log, signature,
            new String[] {"termsOfUseTypeId"},
            new Object[] {termsOfUseTypeId});

        // Delegate to Helper.getTermsOfUse
        return Helper.getTermsOfUse(signature, log, getDBConnectionFactory(), QUERY_TERMS_BY_TYPE_ID, null,
            termsOfUseTypeId);
    }

    /**
     * Retrieves all terms of use entities from the database.
     *
     * @return a list of all TermsOfUse entities.
     *
     * @throws TermsOfUsePersistenceException
     *             if any persistence error occurs.
     */
    public List<TermsOfUse> getAllTermsOfUse() throws TermsOfUsePersistenceException {
        String signature = CLASS_NAME + ".getAllTermsOfUse()";
        Log log = getLog();

        // Log method entry
        Helper.logEntrance(log, signature, null, null);

        // Delegate to Helper.getTermsOfUse
        return Helper.getTermsOfUse(signature, log, getDBConnectionFactory(), QUERY_ALL_TERMS, null, null);
    }

    /**
     * Gets terms of use type by id.
     *
     * @param termsOfUseTypeId
     *            terms of use type id.
     *
     * @return terms of use type.
     *
     * @throws TermsOfUsePersistenceException
     *             if any persistence error occurs.
     */
    public TermsOfUseType getTermsOfUseType(int termsOfUseTypeId) throws TermsOfUsePersistenceException {
        String signature = CLASS_NAME + ".getTermsOfUseType(int termsOfUseTypeId)";
        Log log = getLog();

        // Log method entry
        Helper.logEntrance(log, signature,
            new String[] {"termsOfUseTypeId"},
            new Object[] {termsOfUseTypeId});

        try {
            Connection conn = Helper.createConnection(getDBConnectionFactory());
            PreparedStatement ps = null;

            try {
                ps = conn.prepareStatement(QUERY_TERMS_TYPE);
                ps.setLong(1, termsOfUseTypeId);

                ResultSet rs = ps.executeQuery();
                TermsOfUseType termsType = null;
                if (rs.next()) {
                    termsType = new TermsOfUseType();
                    termsType.setTermsOfUseTypeId(termsOfUseTypeId);
                    termsType.setDescription(rs.getString("terms_of_use_type_desc"));
                }

                // Log method exit
                Helper.logExit(log, signature, new Object[] {termsType});
                return termsType;
            } catch (SQLException e) {
                throw new TermsOfUsePersistenceException("A database access error has occurred.", e);
            } finally {
                // Close the prepared statement
                // (The result set will be closed automatically)
                Helper.closeStatement(ps);
                // Close the connection
                Helper.closeConnection(conn);
            }
        } catch (TermsOfUsePersistenceException e) {
            // Log exception
            throw Helper.logException(log, signature, e);
        }
    }

    /**
     * Update terms of use type.
     *
     * @param termsType
     *            the terms of use type to be updated.
     *
     * @return updated terms of use type.
     *
     * @throws IllegalArgumentException
     *             if termsType is null.
     * @throws EntityNotFoundException
     *             if the entity was not found in the database.
     * @throws TermsOfUsePersistenceException
     *             if any persistence error occurs.
     */
    public TermsOfUseType updateTermsOfUseType(TermsOfUseType termsType) throws TermsOfUsePersistenceException,
        EntityNotFoundException {
        String signature = CLASS_NAME + ".updateTermsOfUseType(TermsOfUseType termsType)";
        Log log = getLog();

        // Log method entry
        Helper.logEntrance(log, signature,
            new String[] {"termsType"},
            new Object[] {termsType});

        try {
            Helper.checkNull(termsType, "termsType");

            Helper.executeUpdate(getDBConnectionFactory(), UPDATE_TERMS_TYPE,
                new Object[] {termsType.getDescription(), termsType.getTermsOfUseTypeId()},
                Long.toString(termsType.getTermsOfUseTypeId()));

            // Log method exit
            Helper.logExit(log, signature, new Object[] {termsType});
            return termsType;
        } catch (IllegalArgumentException e) {
            // Log exception
            throw Helper.logException(log, signature, e);
        } catch (EntityNotFoundException e) {
            // Log exception
            throw Helper.logException(log, signature, e);
        } catch (TermsOfUsePersistenceException e) {
            // Log exception
            throw Helper.logException(log, signature, e);
        }
    }

    /**
     * Gets terms of use text by terms of use id.
     *
     * @param termsOfUseId
     *            terms of use id.
     *
     * @return text of terms of use.
     *
     * @throws EntityNotFoundException
     *             if the entity was not found in the database.
     * @throws TermsOfUsePersistenceException
     *             if any persistence error occurs.
     */
    public String getTermsOfUseText(long termsOfUseId) throws TermsOfUsePersistenceException, EntityNotFoundException {
        String signature = CLASS_NAME + ".getTermsOfUseText(long termsOfUseId)";
        Log log = getLog();

        // Log method entry
        Helper.logEntrance(log, signature,
            new String[] {"termsOfUseId"},
            new Object[] {termsOfUseId});

        try {
            Connection conn = Helper.createConnection(getDBConnectionFactory());
            PreparedStatement ps = null;

            try {
                ps = conn.prepareStatement(QUERY_TERMS_TEXT);
                ps.setLong(1, termsOfUseId);

                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    throw new EntityNotFoundException("The entity was not found for id  (" + termsOfUseId + ").");
                }

                byte[] termsTextBytes = rs.getBytes(1);
                String termsText = (termsTextBytes == null) ? null : new String(termsTextBytes);

                // Log method exit
                Helper.logExit(log, signature, new Object[] {termsText});
                return termsText;
            } catch (SQLException e) {
                throw new TermsOfUsePersistenceException("A database access error has occurred.", e);
            } finally {
                // Close the prepared statement
                // (The result set will be closed automatically)
                Helper.closeStatement(ps);
                // Close the connection
                Helper.closeConnection(conn);
            }
        } catch (EntityNotFoundException e) {
            // Log exception
            throw Helper.logException(log, signature, e);
        } catch (TermsOfUsePersistenceException e) {
            // Log exception
            throw Helper.logException(log, signature, e);
        }
    }

    /**
     * Sets terms of use text.
     *
     * @param termsOfUseId
     *            terms of use id.
     * @param text
     *            text of terms of use.
     *
     * @throws TermsOfUsePersistenceException
     *             if any persistence error occurs.
     * @throws EntityNotFoundException
     *             if the entity was not found in the database.
     */
    public void setTermsOfUseText(long termsOfUseId, String text) throws TermsOfUsePersistenceException,
        EntityNotFoundException {
        String signature = CLASS_NAME + ".setTermsOfUseText(long termsOfUseId, String text)";
        Log log = getLog();

        // Log method entry
        Helper.logEntrance(log, signature,
            new String[] {"termsOfUseId", "text"},
            new Object[] {termsOfUseId, text});

        try {
            Helper.executeUpdate(getDBConnectionFactory(), UPDATE_TERMS_TEXT,
                new Object[] {(text == null) ? null : text.getBytes(), termsOfUseId},
                Long.toString(termsOfUseId));

            // Log method exit
            Helper.logExit(log, signature, null);
        } catch (EntityNotFoundException e) {
            // Log exception
            throw Helper.logException(log, signature, e);
        } catch (TermsOfUsePersistenceException e) {
            // Log exception
            throw Helper.logException(log, signature, e);
        }
    }
    /**
     * <p>
     * Creates a dependency relationship between two terms of use. This method ensures that a dependency loop is not
     * created. This method must be synchronized to avoid creation of cyclic dependency by calling this method from
     * multiple threads at a time (e.g. when dependency A->B exists, and two dependencies B->C and C->A are created at
     * the same time).
     * </p>
     *
     * <p>
     * This method throws TermsOfUseCyclicDependencyException when dependencyTermsOfUseId is equal to
     * dependentTermsOfUseId or if any direct/indirect dependency of dependencyTermsOfUseId depends on
     * dependentTermsOfUseId.
     * </p>
     *
     * @param dependencyTermsOfUseId
     *            the ID of the dependency terms of use
     * @param dependentTermsOfUseId
     *            the ID of the dependent terms of use
     *
     * @throws TermsOfUseCyclicDependencyException
     *             if creation of the specified relationship will lead to a dependency loop.
     * @throws TermsOfUsePersistenceException
     *             if any persistence error occurs.
     *
     * @since 1.1
     */
    public synchronized void createDependencyRelationship(long dependentTermsOfUseId, long dependencyTermsOfUseId)
        throws TermsOfUseCyclicDependencyException, TermsOfUsePersistenceException {
        String signature = CLASS_NAME
            + ".createDependencyRelationship(long dependentTermsOfUseId, long dependencyTermsOfUseId)";
        Log log = getLog();

        // Log method entry
        Helper.logEntrance(log, signature,
            new String[] {"dependentTermsOfUseId", "dependencyTermsOfUseId"},
            new Object[] {dependentTermsOfUseId, dependencyTermsOfUseId});

        try {

            if (dependencyTermsOfUseId == dependentTermsOfUseId) {
                throw new TermsOfUseCyclicDependencyException(
                    "Creation of the specified relationship will lead to a dependency loop.");
            }
            // Create connection:
            Connection conn = Helper.createConnection(getDBConnectionFactory());
            try {
                // Check dependency loop
                checkDependencyLoop(conn, dependentTermsOfUseId, dependencyTermsOfUseId);

                // Insert the dependency
                Helper.executeUpdate(conn, INSERT_DEPENDENCY,
                    new Object[] {dependencyTermsOfUseId, dependentTermsOfUseId});
            } finally {
                Helper.closeConnection(conn);
            }

            // Log method exit
            Helper.logExit(log, signature, null);
        } catch (TermsOfUseCyclicDependencyException e) {
            // Log exception
            throw Helper.logException(log, signature, e);
        } catch (TermsOfUsePersistenceException e) {
            // Log exception
            throw Helper.logException(log, signature, e);
        }
    }

    /**
     * Retrieves all dependency terms of use for the dependent terms of use with the given ID. Returns an empty list
     * if none found.
     *
     * @param dependentTermsOfUseId
     *            the ID of the dependent terms of use
     * @return the retrieved dependency terms of use (not null; doesn't contain null)
     *
     * @throws TermsOfUsePersistenceException
     *             if any persistence error occurs.
     *
     * @since 1.1
     */
    public List<TermsOfUse> getDependencyTermsOfUse(long dependentTermsOfUseId) throws TermsOfUsePersistenceException {
        String signature = CLASS_NAME + ".getDependencyTermsOfUse(long dependentTermsOfUseId)";
        Log log = getLog();

        // Log method entry
        Helper.logEntrance(log, signature,
            new String[] {"dependentTermsOfUseId"},
            new Object[] {dependentTermsOfUseId});

        // Delegate to Helper.getTermsOfUse
        return Helper.getTermsOfUse(signature, log, getDBConnectionFactory(), QUERY_DEPENDENCIES,
            dependentTermsOfUseId, null);
    }

    /**
     * Retrieves all dependent terms of use for the dependency terms of use with the given ID. Returns an empty list
     * if none found.
     *
     * @param dependencyTermsOfUseId
     *            the ID of the dependency terms of use
     * @return the retrieved dependent terms of use (not null; doesn't contain null)
     *
     * @throws TermsOfUsePersistenceException
     *             if any persistence error occurs.
     *
     * @since 1.1
     */
    public List<TermsOfUse> getDependentTermsOfUse(long dependencyTermsOfUseId) throws TermsOfUsePersistenceException {
        String signature = CLASS_NAME + ".getDependentTermsOfUse(long dependencyTermsOfUseId)";
        Log log = getLog();

        // Log method entry
        Helper.logEntrance(log, signature,
            new String[] {"dependencyTermsOfUseId"},
            new Object[] {dependencyTermsOfUseId});

        // Delegate to Helper.getTermsOfUse
        return Helper.getTermsOfUse(signature, log, getDBConnectionFactory(), QUERY_DEPENDENTS,
            dependencyTermsOfUseId, null);
    }

    /**
     * Deletes the dependency relationship between the dependent and dependency terms of use with the specified IDs.
     *
     * @param dependencyTermsOfUseId
     *            the ID of the dependency terms of use
     * @param dependentTermsOfUseId
     *            the ID of the dependent terms of use
     *
     * @throws TermsOfUseDependencyNotFoundException
     *             if dependency relationship between the specified dependency and dependent terms of use doesn't
     *             exist.
     * @throws TermsOfUsePersistenceException
     *             if any persistence error occurs.
     *
     * @since 1.1
     */
    public void deleteDependencyRelationship(long dependentTermsOfUseId, long dependencyTermsOfUseId)
        throws TermsOfUseDependencyNotFoundException, TermsOfUsePersistenceException {
        String signature = CLASS_NAME
            + ".deleteDependencyRelationship(long dependentTermsOfUseId, long dependencyTermsOfUseId)";
        Log log = getLog();

        // Log method entry
        Helper.logEntrance(log, signature,
            new String[] {"dependentTermsOfUseId", "dependencyTermsOfUseId"},
            new Object[] {dependentTermsOfUseId, dependencyTermsOfUseId});

        try {
            int deletedNum = Helper.executeUpdate(getDBConnectionFactory(), DELETE_DEPENDENCY, new Object[] {
                dependencyTermsOfUseId, dependentTermsOfUseId});
            if (deletedNum == 0) {
                // Log exception
                throw Helper.logException(log, signature, new TermsOfUseDependencyNotFoundException(
                    "The dependency relationship between the specified dependency"
                        + " and dependent terms of use doesn't exist."));
            }

            // Log method exit
            Helper.logExit(log, signature, null);
        } catch (TermsOfUsePersistenceException e) {
            // Log exception
            throw Helper.logException(log, signature, e);
        }
    }
    /**
     * Deletes all dependency relationships in which terms of use with the specified ID is a dependent.
     *
     * @param dependentTermsOfUseId
     *            the ID of the dependent terms of use
     *
     * @throws TermsOfUsePersistenceException
     *             if any persistence error occurs.
     *
     * @since 1.1
     */
    public void deleteAllDependencyRelationshipsForDependent(long dependentTermsOfUseId)
        throws TermsOfUsePersistenceException {
        String signature = CLASS_NAME + ".deleteAllDependencyRelationshipsForDependent(long dependentTermsOfUseId)";
        Log log = getLog();

        // Log method entry
        Helper.logEntrance(log, signature,
            new String[] {"dependentTermsOfUseId"},
            new Object[] {dependentTermsOfUseId});

        try {
            Helper.executeUpdate(getDBConnectionFactory(), DELETE_RELATIONSHIPS_FOR_DEPENDENT,
                new Object[] {dependentTermsOfUseId});

            // Log method exit
            Helper.logExit(log, signature, null);
        } catch (TermsOfUsePersistenceException e) {
            // Log exception
            throw Helper.logException(log, signature, e);
        }
    }

    /**
     * Deletes all dependent relationships in which terms of use with the specified ID is a dependency.
     *
     * @param dependencyTermsOfUseId
     *            the ID of the dependency terms of use
     *
     * @throws TermsOfUsePersistenceException
     *             if any persistence error occurs.
     *
     * @since 1.1
     */
    public void deleteAllDependencyRelationshipsForDependency(long dependencyTermsOfUseId)
        throws TermsOfUsePersistenceException {
        String signature = CLASS_NAME + ".deleteAllDependencyRelationshipsForDependency(long dependencyTermsOfUseId)";
        Log log = getLog();

        // Log method entry
        Helper.logEntrance(log, signature,
            new String[] {"dependencyTermsOfUseId"},
            new Object[] {dependencyTermsOfUseId});

        try {
            Helper.executeUpdate(getDBConnectionFactory(), DELETE_RELATIONSHIPS_FOR_DEPENDENCY,
                new Object[] {dependencyTermsOfUseId});

            // Log method exit
            Helper.logExit(log, signature, null);
        } catch (TermsOfUsePersistenceException e) {
            // Log exception
            throw Helper.logException(log, signature, e);
        }
    }

    /**
     * Checks the dependency loop.
     *
     * @param conn
     *            the connection
     * @param dependencyTermsOfUseId
     *            the ID of the dependency terms of use
     * @param dependentTermsOfUseId
     *            the ID of the dependent terms of use
     *
     * @throws TermsOfUseCyclicDependencyException
     *             if creation of the specified relationship will lead to a dependency loop.
     * @throws TermsOfUsePersistenceException
     *             if any persistence error occurs.
     */
    private static void checkDependencyLoop(Connection conn, long dependentTermsOfUseId, long dependencyTermsOfUseId)
        throws TermsOfUseCyclicDependencyException, TermsOfUsePersistenceException {
        // Create a set for deep (direct and indirect) dependencies of dependency terms of use,
        // including the dependency terms of use itself;
        // this set contains all dependencies that are already checked:
        Set<Long> deepDependencyTermsOfUseIds = new HashSet<Long>();
        // Create a set for IDs of dependency terms of use to be checked:
        Set<Long> termsOfUseIdsToCheck = new HashSet<Long>();
        // Add dependencyTermsOfUseId to the set:
        termsOfUseIdsToCheck.add(dependencyTermsOfUseId);

        PreparedStatement ps;

        try {
            while (!termsOfUseIdsToCheck.isEmpty()) {
                // Create string builder for dynamic part of the query:
                StringBuilder sb = new StringBuilder();
                // Get the number of terms of use for which dependency IDs must be retrieved:
                int termsOfUseIdsToCheckNum = termsOfUseIdsToCheck.size();
                for (int i = 0; i < termsOfUseIdsToCheckNum; i++) {
                    if (i != 0) {
                        // Append a comma:
                        sb.append(", ");
                    }
                    // Append a parameter placeholder:
                    sb.append("?");
                }
                ps = conn.prepareStatement(String.format(QUERY_DEPENDENCY_IDS, sb.toString()));

                try {
                    int index = 1;
                    for (long termsOfUseIdsToCheckId : termsOfUseIdsToCheck) {
                        // Set ID to the prepared statement:
                        ps.setLong(index++, termsOfUseIdsToCheckId);
                    }

                    // Add all terms of use IDs to be checked to the set with checked terms of use IDs:
                    deepDependencyTermsOfUseIds.addAll(termsOfUseIdsToCheck);
                    // Clear the set with terms of use to be checked:
                    termsOfUseIdsToCheck.clear();

                    // Execute the query:
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        // Get the next dependency ID:
                        long nextDependencyId = rs.getLong("dependency_terms_of_use_id");
                        if (nextDependencyId == dependentTermsOfUseId) {
                            throw new TermsOfUseCyclicDependencyException(
                                "Creation of the specified relationship will lead to a dependency loop.");
                        }
                        if (!deepDependencyTermsOfUseIds.contains(nextDependencyId)) {
                            // Add ID to the set of terms of use IDs that need to be checked:
                            termsOfUseIdsToCheck.add(nextDependencyId);
                        }
                    }
                } finally {
                    // Close the prepared statement:
                    // The result set will be closed automatically.
                    Helper.closeStatement(ps);
                }
            }
        } catch (SQLException e) {
            throw new TermsOfUsePersistenceException("A database access error has occurred.", e);
        }
    }
}
