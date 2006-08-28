/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;

/**
 * This class contains utility methods used only by the database package of this component. This class is
 * package-visible as all methods are solely used from this package.
 *
 * @author brain_cn
 * @version 1.0
 */
public class DatabaseUtils {
	/** TODO Used for test purpose. */
	public static final boolean IS_TEST = false;

    /**
     * This method closes the given result set silently.
     *
     * @param resultSet the resultSet to be closed
     */
	public static void closeResultSetSilently(final ResultSet resultSet) {
        try {
            resultSet.close();
        } catch (Exception e) {
        }
    }
    /**
     * This method closes the given Statement silently.
     *
     * @param statement the statement to be closed
     */
    public static void closeStatementSilently(final Statement statement) {
        try {
        	statement.close();
        } catch (Exception e) {
        }
    }

    /**
     * This method silently closes the given connection.
     * <p/>
     *
     * @param connection the connection to be closed
     */
    public static void closeSilently(final Connection connection) {
        try {
            connection.close();
        } catch (Exception e) {
        }
    }

    /**
     * This method silently rolls back the given connection.
     * <p/>
     *
     * @param connection the connection to be rolled back
     */
    public static void rollbackSilently(final Connection connection) {
        try {
            connection.rollback();
        } catch (Exception e) {
        }
    }

    /**
     * Make insert sql statement from given fieldnames
     *
     * @param tablename tablename
     * @param fieldnames fieldnames of insert sql statement
     *
     * @return the insert statement
     */
    public static String makeInsertSql(String tablename, String[] fieldnames) {
        StringBuffer sb = new StringBuffer();

        sb.append("INSERT INTO ").append(tablename).append(" (");

        for (int i = 0; i < fieldnames.length; i++) {
            sb.append(fieldnames[i]).append(',');
        }

        // Remove the last , character
        sb.setLength(sb.length() - 1);
        sb.append(") VALUES (");

        // Append ? for every field
        for (int i = 0; i < fieldnames.length; i++) {
            sb.append("?,");
        }

        // Remove the last , character
        sb.setLength(sb.length() - 1);
        sb.append(')');

        return sb.toString();
    }
}
