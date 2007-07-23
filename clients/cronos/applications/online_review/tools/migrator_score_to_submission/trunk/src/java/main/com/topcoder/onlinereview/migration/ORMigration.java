package com.topcoder.onlinereview.migration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.topcoder.db.connectionfactory.ConfigurationException;
import com.topcoder.db.connectionfactory.DBConnectionException;
import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.config.ConfigManagerException;
import com.topcoder.util.idgenerator.IDGenerator;
import com.topcoder.util.idgenerator.IDGeneratorFactory;

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
    private static final String[] ALTER_STATEMENTS = new String[] {
            "ALTER TABLE submission ADD screening_score DECIMAL(5,2) BEFORE create_user",
            "ALTER TABLE submission ADD initial_score DECIMAL(5,2) BEFORE create_user",
            "ALTER TABLE submission ADD final_score DECIMAL(5,2) BEFORE create_user", 
            "ALTER TABLE submission ADD placement DECIMAL(3,0) BEFORE create_user" };

    /**
     * Selects the required columns from the resource info table.
     */
    private static final String SELECT_RESOURCE_INFO = "select s.submission_id, ri.resource_info_type_id, "
            + "ri.value from resource_info ri inner join upload u on "
            + "u.resource_id = ri.resource_id inner join submission s "
            + "on s.upload_id = u.upload_id  where ri.resource_info_type_id "
            + "in (9, 10, 11, 12) and u.upload_type_id = 1 and ri.value is not null "
            + "order by s.submission_id, ri.resource_info_type_id";

    /**
     * Selects the data from the upload based on the upload status as deleted and the upload id not present in the
     * submission table.
     */
    private static final String SELECT_UPLOAD = "select u.upload_id from upload u where "
            + "u.upload_type_id = 1 and not exists (select s.submission_id from "
            + "submission s where s.upload_id = u.upload_id)";

    /**
     * Represents the sql statement to insert submission.
     */
    private static final String INSERT_SUBMISSION = "INSERT INTO submission "
            + "(submission_id, upload_id, submission_status_id, create_user, create_date, modify_user, modify_date, "
            + " screening_score, initial_score, final_score, placement)" + " VALUES (";

    /**
     * Select query for getting all the missed rows in the resource_submission table.
     */
    private static final String RESOURCE_SUBMISSION_QUERY = "select s.submission_id, u.resource_id from submission s, "
            + "upload u where s.upload_id = u.upload_id and u.upload_type_id = 1 "
            + "and not exists (select * from resource_submission rs where "
            + "s.submission_id = rs.submission_id and rs.resource_id = u.resource_id)";

    /**
     * Represents the sql statement to insert resource submission.
     */
    private static final String INSERT_RESOURCE_SUBMISSION = "INSERT INTO resource_submission "
            + "(submission_id, resource_id, create_user, create_date, modify_user, modify_date"
            + ")" + " VALUES (";

    /**
     * The operator name for the created_user and modified_user.
     */
    private static final String OPERATOR = "StudioConv";

    /**
     * Final deletion of the resource info.
     */
    private static final String DELETE_RESOURCE_INFO = "delete from resource_info where resource_info.resource_info_type_id in (9, 10, 11, 12)";

    /**
     * The column Name to resource info type id map.
     */
    private Map<Long, String> columnNameToResourceTypeId = new HashMap<Long, String>();

    /**
     * Constructor. Initializes the columnNameToResourceTypeId Map.
     */
    public ORMigration() {
    	ConfigManager configManager = ConfigManager.getInstance();
        try {
			configManager.add(CONFIG_PATH);
		} catch (ConfigManagerException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
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
    	System.out.println("start modifyAndLoadSubmissionTable");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            Map<Long, String> submissionIdMap = new LinkedHashMap<Long, String>();
            connection = getConnection();
            connection.setAutoCommit(false);

            // Depending on the DB Change can be commented out.
            // alter the table
//            for (int i = 0; i < ALTER_STATEMENTS.length; i++) {
//            	preparedStatement = connection.prepareStatement(ALTER_STATEMENTS[i]);
//            	preparedStatement.execute();
//            }
            preparedStatement = connection.prepareStatement(SELECT_RESOURCE_INFO);
            ResultSet resultSet = preparedStatement.executeQuery();

            // from the result set, construct a map
            while (resultSet.next()) {
                long subId = resultSet.getLong(1);
                submissionIdMap.put(subId, 
                		getString(resultSet.getLong(2), resultSet.getString(3),
                				submissionIdMap.get(subId)));
            }

            Set<Entry<Long, String>> set = submissionIdMap.entrySet();
            StringBuffer update = new StringBuffer();
            List<String> batchStatements = new ArrayList<String>();

            // prepare batch statements from the map
            for (Entry<Long, String> entry : set) {
                update.append("Update submission set");
                update.append(entry.getValue());
                update.append(" where submission_id = ");
                update.append(entry.getKey());
                update.append(";\n");
                // batch statement limit size is 65535
                if (update.length() >= 65000) {
                    batchStatements.add(update.toString());
                    update = new StringBuffer();
                }
            }

            // add the final update statement
            batchStatements.add(update.toString());

            execBatchStatements(batchStatements, connection, preparedStatement);

            // verify db
            verifyDB(connection);

            // remove the rows
            // It will be maintained for a while
            //preparedStatement = connection.prepareStatement(DELETE_RESOURCE_INFO);
            //preparedStatement.execute();

            // everything is fine commit now
            connection.commit();
            System.out.println("commit");
        } catch (Exception e) {
            e.printStackTrace();
            if (connection != null) {
                try {
                    connection.rollback();
                    System.out.println("rollback");
                } catch (SQLException e1) {
                    System.out.println("Roll back trace.");
                    e1.printStackTrace();
                }

            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e1) {
                    // ignore
                }
            }
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e1) {
                    // ignore
                }
            }
        }
        System.out.println("end modifyAndLoadSubmissionTable");
    }

    /**
     * Executes the given List of batch statements.
     * 
     * @param batchStatements
     *            the statements
     * @param connection
     *            the connection to be used
     * @param preparedStatement
     *            the prepared statment to be used
     * @throws Exception
     *             if any.
     */
    private void execBatchStatements(List<String> batchStatements, Connection connection,
            PreparedStatement preparedStatement) throws Exception {
        // execute each batch statement
        for (String string : batchStatements) {
            preparedStatement = connection.prepareStatement(string);
            preparedStatement.executeUpdate();
        }
    }

    /**
     * It takes all the uploads from the upload table with status as deleted, and load them to the submission table
     * with submission status as deleted. <br>
     * It also inserts the missing rows in the resource_submission table.
     */
    public void loadSubmissionTableWithUploads() {
    	System.out.println("start loadSubmissionTableWithUploads");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = getConnection();
            connection.setAutoCommit(false);
            IDGenerator submissionIdGenerator = IDGeneratorFactory.getIDGenerator("submission_id_seq");

            preparedStatement = connection.prepareStatement(SELECT_UPLOAD);
            ResultSet rs = preparedStatement.executeQuery();
            String insertEnd = ", 5, '" + OPERATOR + "', CURRENT, '" + OPERATOR + "', CURRENT, -1, -1, -1, -1);\n";
            StringBuffer insert = new StringBuffer();
            List<String> batchStatements = new ArrayList<String>();
            while (rs.next()) {
                insert.append(INSERT_SUBMISSION);
                insert.append(submissionIdGenerator.getNextID());
                insert.append(", ");
                insert.append(rs.getLong(1));
                insert.append(insertEnd);
                // batch statement limit size is 65535
                if (insert.length() >= 30000) {
                    batchStatements.add(insert.toString());
                    insert = new StringBuffer();
                    System.out.println("In here "+batchStatements.size());
                }
            }
            batchStatements.add(insert.toString());
            System.out.println(insert.toString());
            System.out.println("inserts submissions");
            execBatchStatements(batchStatements, connection, preparedStatement);
            
            int resource_submission_quantity = connection.createStatement().executeUpdate("insert into resource_submission (resource_id, submission_id, create_user, create_date, modify_user, modify_date)"
            		+ " select u.resource_id, s.submission_id, '" + OPERATOR + "', CURRENT,  '" + OPERATOR + "', CURRENT"
            		+ " from submission s, upload u"
            		+ " where s.upload_id = u.upload_id and u.upload_type_id = 1"
            		+ " and not exists (select * from resource_submission rs where" 
            		+ " s.submission_id = rs.submission_id and rs.resource_id = u.resource_id)");
            System.out.println("resource_submission_quantity: " + resource_submission_quantity);
            
            /*
            preparedStatement = connection.prepareStatement(RESOURCE_SUBMISSION_QUERY);
            rs = preparedStatement.executeQuery();
            insertEnd = ", '" + operator + "', CURRENT, '" + operator + "', CURRENT);\n";
            insert = new StringBuffer();
            batchStatements.clear();
            while (rs.next()) {
                insert.append(INSERT_RESOURCE_SUBMISSION);
                insert.append(rs.getLong(1));
                insert.append(", ");
                insert.append(rs.getLong(2));
                insert.append(insertEnd);
                // batch statement limit size is 65535
                if (insert.length() >= 30000) {
                    batchStatements.add(insert.toString());
                    insert = new StringBuffer();
                    System.out.println("In here "+batchStatements.size());
                }
            }
            batchStatements.add(insert.toString());
            System.out.println(insert.toString());
            System.out.println("inserts resource_submission");
            execBatchStatements(batchStatements, connection, preparedStatement);
            */
            // everything is fine commit now
            System.out.println("commit");
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (connection != null) {
                try {
                    connection.rollback();
                    System.out.println("Rollback");
                } catch (SQLException e1) {
                    System.out.println("Roll back trace.");
                    e1.printStackTrace();
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e1) {
                    // ignore
                }
            }
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e1) {
                    // ignore
                }
            }
        }
        System.out.println("start loadSubmissionTableWithUploads");
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
    private Connection getConnection() throws DBConnectionException, ConfigManagerException, ConfigurationException {
        
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
        ORMigration migration = new ORMigration();
        migration.modifyAndLoadSubmissionTable();
        migration.loadSubmissionTableWithUploads();
    }
}
