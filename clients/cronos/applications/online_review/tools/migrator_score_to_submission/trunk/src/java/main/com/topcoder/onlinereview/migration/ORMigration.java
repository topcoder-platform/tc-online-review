package com.topcoder.onlinereview.migration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.topcoder.db.connectionfactory.ConfigurationException;
import com.topcoder.db.connectionfactory.DBConnectionException;
import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.db.connectionfactory.UnknownConnectionException;
import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.config.ConfigManagerException;

/**
 * <p>
 * This is the migration package which handles all the migration stuff. Please refer individual method docs for
 * what it does.
 * </p>
 * 
 * @author evilisneo
 * @version 1.0
 */
public class ORMigration {

    /**
     * The config file for the DBConnectionFactory to create the connection.
     */
    public static final String CONFIG_PATH = "DBConfig_Migration.xml";

    /**
     * Alter statements for the subimission table.
     */
    private static final String[] ALTER_STAMENTS = new String[] {
            "ALTER TABLE submission ADD screening_score DECIMAL(5,2)",
            "ALTER TABLE submission ADD initial_score DECIMAL(5,2)",
            "ALTER TABLE submission ADD final_score DECIMAL(5,2)", 
						"ALTER TABLE submission ADD placement DECIMAL(3,0)" };

    /**
     * Selects the required columns from the resource info table.
     */
    private static final String SELECT_RESOURCE_INFO = "select submission.submission_id, resource_info.resource_info_type_id, "
            + "resource_info.value from resource_submission inner join submission on "
            + "resource_submission.submission_id = submission.submission_id inner join resource_info "
            + "on resource_info.resource_id = resource_submission.resource_id "
            + "where resource_info.resource_info_type_id in (9, 10, 11, 12) "
            + "and resource_info.value is not null order by 1, 2";

    /**
     * Final deletion of the resource info.
     */
    private static final String DELETE_RESOURCE_INFO = "delete from resource_info where resource_info.resource_info_type_id in (9, 10, 11, 12)";

    private Map columnNameToResourceTypeId = new HashMap();

    /**
     * Constructor. Initializes the columnNameToResourceTypeId Map.
     */
    public ORMigration() {
        columnNameToResourceTypeId.put(new Long(9), " screening_score = ");
        columnNameToResourceTypeId.put(new Long(10), " initial_score = ");
        columnNameToResourceTypeId.put(new Long(11), " final_score = ");
        columnNameToResourceTypeId.put(new Long(12), " placement = ");
    }

    /**
     * This method does the following. <br>
     * 1. Alters the submission table to include the following columns.
     * 
     * <pre>
     * ALTER TABLE submission ADD (screening_score DECIMAL(5,2)); 
     * ALTER TABLE submission ADD (initial_score DECIMAL(5,2)); 
     * ALTER TABLE submission ADD (final_score DECIMAL(5,2)); 
     * ALTER TABLE submission ADD (placement DECIMAL(3,0));
     * </pre>
     * 
     * 2. For each row in the submission table, refers the resource_info table to get the four fields and loads.
     * Using the following ids as reference.
     * 
     * <pre>
     * ----------------------------------------
     * resource_info_type_id | name           |
     * ----------------------------------------
     * 9                     | Screening Score|
     * 10                    | Initial Score  |
     * 11                    | Final Score    |
     * 12                    | Placement      |
     * ---------------------------------------- 
     * </pre>
     * 
     * 3. It verifies whether all the scores are updated properly from the resource_info table to submission by
     * doing a reverse check, and if anything does not match, the entire transaction is rolled back.<br>
     * <br>
     * 4. Finally, the rows corresponding to the four fields in the resource_info table is cleared and the
     * transaction is committed.<br>
     */
    public void modifyAndLoadSubmissionTable() {
        Connection connection = null;
        try {
            Map submissionIdMap = new HashMap();
            connection = getConnection();
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = null;

            // Depending on the DB Change can be commented out.
            // alter the table
            // for (int i = 0; i < ALTER_STAMENTS.length; i++) {
            // preparedStatement = connection.prepareStatement(ALTER_STAMENTS[i]);
            // preparedStatement.execute();
            // }
            preparedStatement = connection.prepareStatement(SELECT_RESOURCE_INFO);
            ResultSet resultSet = preparedStatement.executeQuery();

            // from the result set, construct a map
            while (resultSet.next()) {
                long subId = resultSet.getLong(1);
                submissionIdMap.put(new Long(subId), getString(resultSet.getLong(2), resultSet.getString(3),
                        (String) submissionIdMap.get(new Long(subId))));
            }

            Set set = submissionIdMap.entrySet();
            StringBuffer update = new StringBuffer();
            List batchStatements = new ArrayList();

            // prepare batch statements from the map
            for (Iterator iter = set.iterator(); iter.hasNext();) {
                Entry element = (Entry) iter.next();
                update.append("Update submission set");
                update.append(element.getValue());
                update.append(" where submission_id = ");
                update.append(element.getKey());
                update.append(";\n");
                // batch statement limit size is 65535
                if (update.length() >= 65000) {
                    batchStatements.add(update.toString());
                    update = new StringBuffer();
                }
            }
            

            // add the final update statement
            batchStatements.add(update.toString());

            // execute each batch statement
            for (Iterator iter = batchStatements.iterator(); iter.hasNext();) {
                preparedStatement = connection.prepareStatement((String) iter.next());
                preparedStatement.executeUpdate();
            }

            // verify db
            verifyDB(connection);

            // remove the rows
            preparedStatement = connection.prepareStatement(DELETE_RESOURCE_INFO);
            preparedStatement.execute();

            // everything is fine commit now
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException e1) {
                    System.out.println("Roll back trace.");
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * Creates a new DB connection from the config file.
     * 
     * @return the created connection
     * @throws DBConnectionException
     *             if any.
     * @throws ConfigManagerException
     *             if any.
     * @throws ConfigurationException
     *             if any.
     */
    private Connection getConnection() throws ConfigManagerException, ConfigurationException, DBConnectionException {
        ConfigManager configManager = ConfigManager.getInstance();
        configManager.add(CONFIG_PATH);
        DBConnectionFactory connectionFactory = new DBConnectionFactoryImpl(
                "com.topcoder.db.connectionfactory.DBConnectionFactoryImpl");
        return connectionFactory.createConnection();
    }

    /**
     * Gets the set values.
     * 
     * @param type_id
     *            the resource info type id
     * @param value
     *            the value
     * @param setter
     *            the previous setter statements if any
     * @return the concatenated statement.
     */
    private String getString(long type_id, String value, String setter) {
        String in = columnNameToResourceTypeId.get(new Long(type_id)).toString() + value;
        return setter == null ? in : setter + "," + in;
    }

    /**
     * This can be included whether the migration was clean and successful.
     * 
     * @param connection
     *            the current connection.
     */
    private void verifyDB(Connection connection) {
        // complete this
    }

    /**
     * The starting point.
     * 
     * @param args
     *            not used.
     */
    public static void main(String args[]) {
        new ORMigration().modifyAndLoadSubmissionTable();
    }
}
