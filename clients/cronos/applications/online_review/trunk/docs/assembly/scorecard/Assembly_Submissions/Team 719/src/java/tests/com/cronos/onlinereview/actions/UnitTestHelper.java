package com.cronos.onlinereview.actions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.topcoder.db.connectionfactory.DBConnectionException;
import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.management.scorecard.PersistenceException;
import com.topcoder.management.scorecard.ScorecardManager;
import com.topcoder.management.scorecard.ScorecardManagerImpl;
import com.topcoder.management.scorecard.ScorecardSearchBundle;
import com.topcoder.management.scorecard.data.Scorecard;
import com.topcoder.management.scorecard.validation.ValidationException;
import com.topcoder.search.builder.filter.Filter;

/**
 * <p>
 * Unit test helper.
 * </p>
 * 
 * @version 1.0
 * @author TCSDEVELOPER
 */
public final class UnitTestHelper {
    /** DB connection factory configuration namespace. */
    private static final String DB_CONNECTION_FACTORY_NAMESPACE = "com.topcoder.db.connectionfactory.DBConnectionFactoryImpl";

    /** ScorecardManager instance. */
    private ScorecardManager scorecardManager;

    /** DBConnection factory instance. */
    private DBConnectionFactory connectionFactory;

    /** Singletone instance. */
    private static UnitTestHelper instance = null;

    /**
     * <p>
     * Return the singleton instance.
     * </p>
     * 
     * @return the singleton instance.
     */
    public synchronized static UnitTestHelper getInstance() {
        if (instance == null) {
            instance = new UnitTestHelper();
        }
        return instance;
    }

    /**
     * <p>
     * Private constructor.
     * </p>
     */
    private UnitTestHelper() {
        try {
            this.scorecardManager = new ScorecardManagerImpl();
            this.connectionFactory = new DBConnectionFactoryImpl(
                    DB_CONNECTION_FACTORY_NAMESPACE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * <p>
     * Update the scorecard.
     * </p>
     * 
     * @param scorecard
     *            the scorecard.
     * @throws PersistenceException
     *             from ScorecardManager
     * @throws ValidationException
     *             from ScorecardManager
     */
    public void updateScorecard(Scorecard scorecard)
            throws PersistenceException, ValidationException {
        this.scorecardManager.updateScorecard(scorecard, "tester");
    }

    /**
     * <p>
     * Create the scorecard.
     * </p>
     * 
     * @param scorecard
     *            the scorecard.
     * @throws PersistenceException
     *             from ScorecardManager
     * @throws ValidationException
     *             from ScorecardManager
     */
    public void createScorecard(Scorecard scorecard)
            throws PersistenceException, ValidationException {
        this.scorecardManager.createScorecard(scorecard, "tester");
    }

    /**
     * <p>
     * Return scorecard with ID.
     * </p>
     * 
     * @param id
     *            the id
     * @return the scorecard
     * @throws PersistenceException
     *             from ScorecardManager
     */
    public Scorecard getScorecard(long id) throws PersistenceException {
        return this.scorecardManager.getScorecard(id);
    }

    /**
     * <p>
     * Search scorecards.
     * </p>
     * 
     * @param filter
     *            filter
     * @param complete
     *            if get complete scorecards
     * @return the scorecards
     * @throws PersistenceException
     *             from ScorecardManager
     */
    public Scorecard[] searchScorecards(Filter filter, boolean complete)
            throws PersistenceException {
        return this.scorecardManager.searchScorecards(filter, complete);
    }

    /**
     * <p>
     * Return the scorecard with given name and version.
     * </p>
     * 
     * @param name
     *            name
     * @param version
     *            version
     * @return the scorecard
     * @throws PersistenceException
     *             from ScorecardManager
     */
    public Scorecard getScorecardByNameAndVersion(String name, String version)
            throws PersistenceException {
        Filter filter = ScorecardSearchBundle.buildAndFilter(
                ScorecardSearchBundle.buildNameEqualFilter(name),
                ScorecardSearchBundle.buildVersionEqualFilter(version));
        Scorecard[] scorecards = this.scorecardManager.searchScorecards(filter,
                true);
        if (scorecards != null && scorecards.length > 0) {
            return scorecards[0];
        } else {
            return null;
        }
    }

    /**
     * <p>
     * Return the scorecards with given name.
     * </p>
     * 
     * @param name
     *            name
     * @return the scorecards
     * @throws PersistenceException
     *             from ScorecardManager
     */
    public Scorecard[] getScorecardsByName(String name)
            throws PersistenceException {
        Filter filter = ScorecardSearchBundle.buildNameEqualFilter(name);
        Scorecard[] scorecards = this.scorecardManager.searchScorecards(filter,
                true);
        return scorecards;
    }

    /**
     * <p>
     * Clear all scorecards.
     * </p>
     * 
     * @throws DBConnectionException
     *             from DBConnectionFactory
     * @throws SQLException
     *             if DB operation failed
     */
    public void clearAllScorecards() throws DBConnectionException, SQLException {
        Connection conn = connectionFactory.createConnection();
        conn.setAutoCommit(true);
        PreparedStatement ps = null;
        String[] tables = { "scorecard_question", "scorecard_section",
                "scorecard_group", "scorecard" };
        try {
            for (int i = 0; i < tables.length; i++) {
                ps = conn.prepareStatement("DELETE FROM " + tables[i]);
                ps.executeUpdate();
            }
        } finally {
            ps.close();
        }
    }
}
