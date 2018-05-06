package com.topcoder.management.deliverable.late.accuracytests;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.management.deliverable.late.impl.LateDeliverableManagerImpl;
import com.topcoder.management.deliverable.late.impl.persistence.DatabaseLateDeliverablePersistence;
import org.junit.After;
import org.junit.Before;

import java.sql.Connection;

public class PersistenceTestBase {

    /**
     * <p> Instance of <code>DatabaseLateDeliverablePersistence</code> used for test. </p>
     */
    protected DatabaseLateDeliverablePersistence lateDeliverablePersistence;

    /**
     * Configuration object used for test.
     */
    protected ConfigurationObject persistenceConfig;

    /**
     * Connection used for test.
     */
    protected Connection connection;

    /**
     * Instance of <code>LateDeliverableManagerImpl</code> used for test.
     */
    protected LateDeliverableManagerImpl lateDeliverableManager;

    /**
     * Configuration object for <code>LateDeliverableManagerImpl</code>.
     */
    protected ConfigurationObject managerConfig;

    /**
     * <p> Sets up test environment. </p>
     *
     * @throws Exception to jUnit.
     */
    @Before
    public void setUp() throws Exception {
        TestHelper.resetConfigurationManager();
        TestHelper.initializeConfigurationManager("accuracy/SearchBundleManager.xml");

        connection = TestHelper.createConnection();

        TestHelper.clearTable(connection);
        TestHelper.initializeData(connection);

        managerConfig = TestHelper.getManagerConfig();
        persistenceConfig = managerConfig.getChild("persistenceConfig");

        lateDeliverablePersistence = new DatabaseLateDeliverablePersistence();
        lateDeliverablePersistence.configure(persistenceConfig);

        lateDeliverableManager = new LateDeliverableManagerImpl(managerConfig);
        lateDeliverablePersistence = new DatabaseLateDeliverablePersistence();
    }

    /**
     * <p> Tears down test environment. </p>
     *
     * @throws Exception to jUnit.
     */
    @After
    public void tearDown() throws Exception {
        TestHelper.clearTable(connection);
        TestHelper.closeConnection(connection);
        connection = null;
        lateDeliverablePersistence = null;
        
        TestHelper.resetConfigurationManager();
    }
}
