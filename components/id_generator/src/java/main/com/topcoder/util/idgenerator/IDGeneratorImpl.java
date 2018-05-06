/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.idgenerator;

import com.topcoder.util.config.ConfigManagerException;

import java.math.BigDecimal;
import java.math.BigInteger;

import java.sql.SQLException;
import java.util.Map;


/**
 * <p>
 * This class is the core of the component and actually generates the IDs. It is also used by IDGeneratorBean. This
 * class queries a database to find the current state of the sequence -- the lowest ID not yet generated, and the
 * current block size. It allocates to itself the next &quot;block&quot; of IDs and then updates the ID sequence state
 * in the database accordingly, then allocates IDs from this block until exhausted. Note that larger block sizes are
 * more efficient, as this class has to make fewer database updates. However also note that any IDs not yet assigned
 * when this instance is destroyed are simply lost, so large block sizes may also result in larger &quot;gaps&quot; in
 * the ID sequence. This class requires a javax.sql.DataSource instance to be made available under the JNDI name
 * &quot;java:comp/env/jdbc/com/topcoder/util/idgenerator/IDGeneratorDataSource&quot;; this DataSource is used to
 * access the ID table. This class is synchronized for thread-safety.
 * </p>
 * 
 * <p>
 * Notes: the default connection name is 'DefaultSequence' for DBConnectionFactory component.
 * </p>
 *
 * @author srowen, iggy36, gua
 * @version 3.0
 */
public class IDGeneratorImpl implements IDGenerator {
    /** The default next_block_start field name of table id_sequences. */
    private static final String DEFAULT_NEXT_BLOCK_START = "next_block_start";

    /** The default block_size field name of table id_sequences. */
    private static final String DEFAULT_BLOCK_SIZE = "block_size";

    /** The default exausted field name of table id_sequences. */
    private static final String DEFAULT_EXHAUSTED = "exhausted";

    /** The key to the next_block_start field name of table id_sequences in configuration. */
    private static final String NEXT_BLOCK_START_KEY = "next_block_start_field";

    /** The key to the block_size field name of table id_sequences in configuration. */
    private static final String BLOCK_SIZE_KEY = "block_size_field";

    /** The key to the exausted field name of table id_sequences in configuration. */
    private static final String EXHAUSTED_KEY = "exhausted_field";

    /** The default next_block_start field name of table id_sequences. */
    private final String nextBlockStartField;

    /** The default block_size field name of table id_sequences. */
    private final String blockSizeField;

    /** The default exausted field name of table id_sequences. */
    private final String exhaustedField;

    /** The database helper used to perform db operations. */
    private DBHelper helper;

    /**
     * The name of the ID sequence which this instance encapsulates. This is set by the constructor and so is not given
     * an initial value in the model.
     */
    private final String idName;

    /** This is the next value that will be generated for this sequence. It is returned and updated by getNextID(). */
    private long nextID;

    /** Indicate the ids left in the current block for the getNextID method. */
    private int idsLeft = 0;
    
    /** the block size */
    private int blockSize = -1;

    /**
     * Creates a new IDGeneratorImpl for the named ID sequence.
     *
     * @param idName name of the ID sequence encapsulated by this instance.
     *
     * @throws IDGenerationException if an error occurs while retrieving ID  sequence configuration (for example,
     *         database errors)
     * @throws NoSuchIDSequenceException if name is null, or no such ID  sequence is configured in the database
     */
    public IDGeneratorImpl(String idName) throws IDGenerationException {
        if (idName == null) {
            throw new NoSuchIDSequenceException("The specified IDName is null");
        }

        this.idName = idName;
        nextBlockStartField = DEFAULT_NEXT_BLOCK_START;
        blockSizeField = DEFAULT_BLOCK_SIZE;
        exhaustedField = DEFAULT_EXHAUSTED;

        helper = new DBHelper();
        checkIDName();
    }

    /**
     * Creates a new IDGeneratorImpl for the named ID sequence with the configuration namespace.
     *
     * @param namespace the configuration namespace for the ID generator instance
     * @param idName name of the ID sequence encapsulated by this instance.
     *
     * @throws IDGenerationException if an error occurs while retrieving ID sequence configuration (for example,
     *         database errors)
     * @throws NoSuchIDSequenceException if name is null, or no such ID sequence is configured in the database
     */
    public IDGeneratorImpl(String namespace, String idName)
        throws IDGenerationException {
        if (idName == null) {
            throw new NoSuchIDSequenceException("The specified IDName is null");
        }

        this.idName = idName;

        try {
            nextBlockStartField = IDGeneratorHelper.getConfigurationSetting(namespace, NEXT_BLOCK_START_KEY,
                    DEFAULT_NEXT_BLOCK_START);
            blockSizeField = IDGeneratorHelper.getConfigurationSetting(namespace, BLOCK_SIZE_KEY, DEFAULT_BLOCK_SIZE);
            exhaustedField = IDGeneratorHelper.getConfigurationSetting(namespace, EXHAUSTED_KEY, DEFAULT_EXHAUSTED);

            helper = new DBHelper(namespace);
        } catch (ConfigManagerException e) {
            throw new IDGenerationException("Failed to create the id generator instance!", e);
        }

        checkIDName();
    }

    /**
     * Creates a new IDGeneratorImpl for the named ID sequence with the configuration namespace and a specified block
     * size.
     *
     * @param namespace the configuration namespace for the ID generator instance
     * @param idName name of the ID sequence encapsulated by this instance.
     * @param blockSize the block size
     *
     * @throws IDGenerationException if an error occurs while retrieving ID sequence configuration (for example,
     *         database errors)
     * @throws IllegalArgumentException if the block size is negative.
     */
    public IDGeneratorImpl(String namespace, String idName, int blockSize)
        throws IDGenerationException {
        this(namespace, idName);

        if (blockSize < 0) {
            throw new IllegalArgumentException("The block size should not be negative.");
        }

        this.blockSize = blockSize;
    }

    /**
     * Return the name of the ID sequence which this instance encapsulates.
     *
     * @return the name of the ID sequence which this instance encapsulates
     */
    public String getIDName() {
        return idName;
    }

    /**
     * Returns the next ID in the ID sequence encapsulated by this instance. Internal state is updated so that this ID
     * is not returned again from this method.
     *
     * @return the next ID in the ID sequence
     *
     * @throws IDGenerationException if an error occurs while generating the ID (for example, error while connecting to
     *         the database)
     */
    public synchronized long getNextID() throws IDGenerationException {
        if (idsLeft <= 0) {
            // if no ids left,
            // acquire a new block
            getNextBlock();
        }

        --idsLeft;

        return nextID++;
    }

    /**
     * <p>
     * Wraps the value that would be returned by getNextID() in a BigInteger instance and returns it.
     * </p>
     *
     * @return next value that would be returned by getNextID() as a BigInteger
     *
     * @throws IDGenerationException if an error occurs while generating the ID (for example, error while connecting to
     *         the database)
     */
    public synchronized BigInteger getNextBigID() throws IDGenerationException {
        return BigInteger.valueOf(getNextID());
    }

    /**
     * <p>
     * Disposes the id generator instance.
     * </p>
     */
    public void dispose() {
        helper.releaseDatabaseResources(true);
    }

    /**
     * Reading the database for the next new start id.
     *
     * @throws IDGenerationException if an error occurs while generating the ID (for example, error while connecting to
     *         the database)
     */
    private synchronized void getNextBlock() throws IDGenerationException {
        try {
            Map[] result = (Map[]) helper.execute(DBHelper.SELECT_NEXT_BLOCK_KEY, new Object[] { idName });

            if (result.length == 0) {
                throw new NoSuchIDSequenceException("The specified IDName does not exist in the database.");
            }

            // if the ids are exausted yet, simply throw exception
            Object exhaustedFieldObject = result[0].get(exhaustedField);
            if (exhaustedFieldObject instanceof BigDecimal) {
                // For old version, before 3.0.1
                if (((BigDecimal) exhaustedFieldObject).intValue() != 0) {
                    throw new IDsExhaustedException("The ids of specified IDName are exausted yet.");
                }
            } else {
                if (((Integer) exhaustedFieldObject).intValue() != 0) {
                    throw new IDsExhaustedException("The ids of specified IDName are exausted yet.");
                }
            }
            

            // otherwise, read the new block and update this id
            Object nextBlockObject = result[0].get(nextBlockStartField);
            long myNextID = 0;
            if (nextBlockObject instanceof BigDecimal) {
                myNextID = ((BigDecimal) nextBlockObject).longValue();
            } else {
                myNextID =  ((Long) nextBlockObject).longValue();
            }
             

            if (blockSize < 0) {
                Object blockSizeObject = result[0].get(blockSizeField);
                if (blockSizeObject instanceof BigDecimal) {
                    blockSize = ((BigDecimal) blockSizeObject).intValue();
                } else {
                    blockSize = ((Integer) blockSizeObject).intValue();
                }
            }

            // if the ids left are not sufficient to make a full block,
            // throw exception
            if ((myNextID - 1) > (Long.MAX_VALUE - blockSize)) {
                throw new IDsExhaustedException("The ids left are not sufficient to make a block.");
            }

            // From here, we need to consider the rollback problem while error occurs
            // if the ids are exausted, set the flag
            if ((myNextID - 1) >= (Long.MAX_VALUE - blockSize)) {
                helper.execute(DBHelper.UPDATE_EXHAUSTED_KEY, new Object[] { idName });
            }

            long myMaxBlockID = (myNextID + blockSize) - 1;

            // update the next block start
            helper.execute(DBHelper.UPDATE_NEXT_BLOCK_START_KEY, new Object[] { new Long(myMaxBlockID + 1), idName });
            helper.commit();

            // it is safe to assign all the value now
            idsLeft = blockSize;
            nextID = myNextID;
        } catch (SQLException e) {
            // rollback for SQL error
            // IDGenerationException will be thrown only while try to get connection. in this case we needn't rollback
            // while thrown IDsExhaustedException, no any updating at the underlying persistence.
            // Selection operation needn't to be rollback while error occurs. So it needn't to rollback too
            helper.rollback();
            throw new IDGenerationException("Failed to get next block.", e);
        } finally {
            helper.releaseDatabaseResources(false);
        }
    }

    /**
     * Checks whether the id sequence exists.
     *
     * @throws IDGenerationException if the id sequence does not exist.
     */
    private void checkIDName() throws IDGenerationException {
        // Check if the given id generator exist on the underlying persistence
        try {
            Map[] result = (Map[]) helper.execute(DBHelper.SELECT_NEXT_BLOCK_KEY, new Object[] { idName });

            if (result.length == 0) {
                throw new NoSuchIDSequenceException(
                    "The specified IDName does not exist in the underlying persistence.");
            }
        } catch (SQLException e) {
            throw new IDGenerationException("Error occurs while accessing the underlying persistence.", e);
        } finally {
            helper.releaseDatabaseResources(false);
        }
    }
}
