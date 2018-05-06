/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.idgenerator;

import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;

import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.config.UnknownNamespaceException;

import java.math.BigInteger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * <p>
 * This implementation of the IDGenerator interface is backed by an Oracle DB sequence object.&nbsp; This object needs
 * to be created in the backing DB before this class is used. The name of the backing sequence object is derived from
 * the ID name of this generator by appending &quot;_seq&quot; to the name. For example, if the name of this generator
 * is &quot;mygen&quot;, then the Oracle DB should have a sequence object named &quot;mygen_seq&quot;. See Section 4.3
 * in the Component Spec for more details about Oracle sequences.
 * </p>
 *
 * <p>
 * In order to behave like the IDGeneratorImpl class, instances of this class will allocate &quot;blocks&quot; of IDs
 * for themselves by getting the next blockSize values from the backing sequence when its current values have been
 * exhausted.
 * </p>
 *
 * <p>
 * SQL statements used to get information about Oracle sequence or retrieve the next sequence id can be configured 
 * through ConfigManager. See Section 3.2 in the Component Spec.
 * </p>
 *
 * <p>
 * Notes: the default connection name is 'OracleSequence' for DBConnectionFactory component.
 * </p>
 *
 * @author iggy36, gua
 * @version 3.0
 */
public class OracleSequenceGenerator implements IDGenerator {
    /**
     * <p>
     * Represents the namespace used by this component to get the SQL statements.
     * </p>
     */
    private static final String DEFAULT_NAMESPACE = "com.topcoder.util.idgenerator.OracleSequenceGenerator";
    
    /**
     * <p>
     * Represents the namespace used by DBConnection component to get the Connection.
     * </p>
     */
    private static final String DBFACTORY_NAMESPACE = "com.topcoder.db.connectionfactory.DBConnectionFactoryImpl";

    /** Represents the connection name of oracle sequence. */
    private static final String CONNECTION_NAME = "OracleSequence";

    /**
     * The default select SQL statement used for retrieving data from table.
     * Property SQL_SELECT_BLOCK_SIZE set in ConfigManager can override this SQL.
     */
    private static String SELECT_BLOCK_SIZE = "SELECT INCREMENT_BY, MAX_VALUE FROM all_sequences WHERE SEQUENCE_NAME = '?'";
    
    /**
     * The property name of the select SQL statement used for retrieving data from table.
     */
    private static String PROPERTY_SQL_SELECT_BLOCK_SIZE = "SQL_SELECT_BLOCK_SIZE";

    /**
     * The default select SQL statement used for retrieving the next sequence id.
     * Property SQL_NEXT_SEQUENCE_ID set in ConfigManager can override this SQL.
     */
    private static String NEXT_SEQUENCE_ID = "SELECT ?.NEXTVAL FROM DUAL";
    
    /**
     * The property name of the select SQL statement used for retrieving data from table.
     */
    private static String PROPERTY_SQL_NEXT_SEQUENCE_ID = "SQL_NEXT_SEQUENCE_ID";

    /**
     * <p>
     * The default sequence suffix, concatenated to the id name to determine the name of the backing sequence.
     * </p>
     */
    private static final String DEFAULT_SEQ_SUFFFIX = "_SEQ";

    /**
     * <p>
     * The name of the ID sequence which this instance encapsulates. This is set by the constructor and not modified
     * subsequently.
     * </p>
     */
    private String idName;

    /**
     * <p>
     * The sequence name in the backing Oracle DB. Initialized to <em>idName</em> + &quot;_seq&quot;. Users may
     * override this by calling the setSequenceName method.
     * </p>
     */
    private String seqName;

    /**
     * <p>
     * This is the next value that will be generated for this sequence. It is returned and updated by getNextID().
     * </p>
     */
    private long nextID = 0;

    /**
     * <p>
     * Size of blocks to get from backing Oracle sequence.
     * </p>
     */
    private int blockSize = 0;

    /** Indicate the ids left in the current block for the getNextID method. */
    private int idsLeft = 0;

    /** This is the largest ID value that this instance may generate. */
    private long maxID;

    /**
     * <p>
     * Represents DBConnectionFactory instance. this variable will be instantiate lazily in the first invoking
     * getConnection() method.
     * </p>
     */
    private DBConnectionFactory factory = null;

    /** If the given seqName sequence exist or not. */
    private boolean sequenceExist = false;

    /**
     * <p>
     * Instantiates an OracleSequenceGenerator with a given ID name. This will also initialize the expected sequence
     * name in the Oracle DB to be the result of appending the suffix &quot;_seq&quot; to this name. This can be
     * overridden by calling the setSequenceName method.
     * </p>
     *
     * @param idName ID name
     *
     * @throws IDGenerationException if idName is null
     */
    public OracleSequenceGenerator(String idName) throws IDGenerationException {
        if (idName == null) {
            throw new NoSuchIDSequenceException("The given idName is null.");
        }

        this.idName = idName;
        this.setSequenceName(idName + DEFAULT_SEQ_SUFFFIX);
        this.configureSQL();
    }

    /**
     * <p>
     * Returns the name of the ID sequence which this instance encapsulates.
     * </p>
     *
     * @return the name of the ID sequence which this instance encapsulates
     */
    public String getIDName() {
        return idName;
    }

    /**
     * <p>
     * Returns the next ID in the ID sequence encapsulated by this instance.
     * </p>
     *
     * @return the generated ID
     *
     * @throws IDGenerationException if there is a problem with the backing DB
     * @throws IDsExhaustedException if the IDs have been exhausted
     * @throws NoSuchIDSequenceException if there is no appropriate sequence defined in the Oracle DB
     */
    public synchronized long getNextID() throws IDGenerationException {
        if (idsLeft <= 0) {
            // if no ids left,
            // acquire a new block
            synchronized (OracleSequenceGenerator.class) {
                getNextBlock();
            }
        }

        --idsLeft;

        return nextID++;
    }

    /**
     * Reading the database for the next new start id.
     *
     * @throws IDsExhaustedException if all possible values in the ID sequence have been assigned and no more can be
     *         assigned
     * @throws IDGenerationException if an error occurs while generating the ID (for example, error while connecting to
     *         the database)
     * @throws NoSuchIDSequenceException if there is no appropriate sequence defined in the backing DB
     */
    private synchronized void getNextBlock() throws IDGenerationException {
        // check sequence exist or not lazily
        if (!sequenceExist) {
            if (!setupSequence()) {
                throw new NoSuchIDSequenceException("sequence: " + seqName + " does not exist.");
            }
        }

        Statement stmt = null;
        ResultSet rs = null;
        Connection connection = null;

        try {
            connection = getConnection();

            // query next val for this sequence
            stmt = connection.createStatement();
            rs = stmt.executeQuery(OracleSequenceGenerator.NEXT_SEQUENCE_ID.replaceAll("\\?", this.seqName));

            if (rs.next()) {
                nextID = rs.getLong(1);
                idsLeft = blockSize;

                // maxID - nextID is less than blockSize
                if (((nextID + idsLeft) - 1) > this.maxID) {
                    throw new IDsExhaustedException("The ids left are not sufficient to make a block.");
                }
            }
        } catch (SQLException e) {
            if (e.getMessage().indexOf("exceeds MAXVALUE") > 0) {
                throw new IDsExhaustedException("The ids left are not sufficient to make a block.");
            }

            throw new IDGenerationException("Failed to get next sequence id.", e);
        } finally {
            IDGeneratorHelper.closeStatement(stmt);
            IDGeneratorHelper.close(connection);
        }
    }

    /**
     * <p>
     * Wraps the value that would be returned by getNextID() in a BigInteger instance and returns it.
     * </p>
     *
     * @return BigInteger containing the generated ID
     *
     * @throws IDGenerationException if there is a problem with the backing DB
     * @throws IDsExhaustedException if the IDs have been exhausted
     * @throws NoSuchIDSequenceException if there is no appropriate sequence defined in the backing DB
     */
    public synchronized BigInteger getNextBigID() throws IDGenerationException {
        return BigInteger.valueOf(getNextID());
    }

    /**
     * <p>
     * Returns the name of the expected sequence in the backing Oracle DB. Initialized to <em>idName</em> +
     * &quot;_seq&quot; at construction but may be manually set afterwards by using the setSequenceName method.
     * </p>
     *
     * @return name of the expected sequence in the Oracle DB
     */
    public synchronized String getSequenceName() {
        return this.seqName;
    }

    /**
     * <p>
     * Sets the name of the expected Oracle DB sequence. This should be used in cases where it is not possible to
     * create the backing sequence name to be getIDName() + &quot;_seq&quot;, which is the default sequence name after
     * construction. Calling this method will override this default.
     * </p>
     *
     * @param seqName Expected Oracle DB sequence name
     *
     * @throws NullPointerException if seqName is null
     */
    public synchronized void setSequenceName(String seqName) {
        if (seqName == null) {
            throw new NullPointerException("The given seqName is null.");
        }

        if (!seqName.equals(this.seqName)) {
            this.seqName = seqName;

            // check if the sequence exist or not
            setupSequence();
        }
    }

    /**
     * Disposes the id generator instance.
     */
    public void dispose() {
        //do nothing
    }

    /**
     * Get connection from DBFactory. Will instaniate all required resource here.
     *
     * @return the connection from DBFactory
     *
     * @throws IDGenerationException if an error occurs while retrieving ID sequence configuration (for example,
     *         database errors)
     */
    private Connection getConnection() throws IDGenerationException {
        try {
            // Instantiate the factory lazily
            if (factory == null) {
                factory = new DBConnectionFactoryImpl(DBFACTORY_NAMESPACE);
            }

            // This implementation only retrieve nextval from oracle sequence, No update is needed
            return factory.createConnection(CONNECTION_NAME);
        } catch (Exception e) {
            throw new IDGenerationException("Failed to get connection fro oracle-sequence from db factory.", e);
        }
    }

    /**
     * Check if the sequence exist or not. If existing, retrieve increment_by as block size.
     *
     * @return true if the seqName sequence exist, false otherwise.
     */
    private boolean setupSequence() {
        // set sequenceExist to initial value
        sequenceExist = false;
        Statement stmt = null;
        ResultSet rs = null;
        Connection connection = null;

        try {
            // Query increment by field from SEQ table (Oracle system table used for storing all sequence object)
            connection = getConnection();
            stmt = connection.createStatement();
            rs = stmt.executeQuery(OracleSequenceGenerator.SELECT_BLOCK_SIZE.replaceAll("\\?", this.seqName));

            if (rs.next()) {
                // set the blocksize with increment by
                this.blockSize = rs.getInt(1);
                this.maxID = rs.getBigDecimal(2).longValue();
                sequenceExist = true;
                return true;
            }
            return false;
        } catch (IDGenerationException e) {
            return false;
        } catch (SQLException e) {
            return false;
        } finally {
            IDGeneratorHelper.closeResultSet(rs);
            IDGeneratorHelper.closeStatement(stmt);
            IDGeneratorHelper.close(connection);
        }
    }

    /**
     * Read SQL configuration from ConfigManager. If any error occurs, use the default SQL instead.
     */
    private void configureSQL() {
        try {
            ConfigManager cm = ConfigManager.getInstance();
            String s = cm.getString(
                    OracleSequenceGenerator.DEFAULT_NAMESPACE, 
                    OracleSequenceGenerator.PROPERTY_SQL_SELECT_BLOCK_SIZE);
            if (s != null) {
                OracleSequenceGenerator.SELECT_BLOCK_SIZE = s;
            }
            s = cm.getString(
                    OracleSequenceGenerator.DEFAULT_NAMESPACE, 
                    OracleSequenceGenerator.PROPERTY_SQL_NEXT_SEQUENCE_ID);
            if (s != null) {
                OracleSequenceGenerator.NEXT_SEQUENCE_ID = s;
            }
        } catch (UnknownNamespaceException e) {
            // if any error occurs, just ignore it and use default SQL statements
        }
    }
}
