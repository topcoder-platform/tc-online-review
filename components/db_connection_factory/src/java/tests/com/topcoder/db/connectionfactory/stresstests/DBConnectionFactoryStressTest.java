package com.topcoder.db.connectionfactory.stresstests;

import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

import java.util.Iterator;
import java.util.Hashtable;

import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.util.config.ConfigManager;

import javax.naming.Context;


 /** <p>
 * This class test the behavior of <code>DBConnectionFactory </code> when used to read
  * configuration information via Config Manager.
 * </p>
 *
 * @author AleaActaEst, TCDEVELOPER
 * @version 1.0
 */
public class DBConnectionFactoryStressTest extends TestCase {

    /**
     * Represents the configuration file with 1 entry
     */
    private String CONNECTION_CONFIG_FILE_LOW_ENTRY = "FactoryStressTest_low.xml";

   /**
    * Represents the configuration file with 5 entries
    */
    private String CONNECTION_CONFIG_FILE_MED_ENTRIES = "FactoryStressTest_Med.xml";

    /**
     * Represents the configuration file with 15 entry
     */
    private String CONNECTION_CONFIG_FILE_HIGH_ENTRIES = "FactoryStressTest_High.xml";

    /**
     * <p>
     * Configuration namespace used in this test.
     * </p>
     */
    private static final String CONFIG_NAMESPACE = "test_namespace";

    /**
     * <p>
     * <code>DBConnectionFactoryImpl</code> instance used in this test.
     * </p>
     */
    private DBConnectionFactoryImpl connFactory = null;

    /**
     * <p>
     * <code>Configuration Manager </code> instance used in this test.
     * </p>
     */
     private ConfigManager cm = null;

     /**
     * <p>
     * This method aggragates all tests in this class.
     * </p>
     *
     * @return test suite aggragating all tests
     */
    public static Test suite() {
        return new TestSuite(DBConnectionFactoryStressTest.class);
    }
    /**
     * <p>
     * Set up the test environment.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    protected void setUp() throws Exception {
        cm = ConfigManager.getInstance();

        if (cm.existsNamespace(CONFIG_NAMESPACE)) {
            cm.removeNamespace(CONFIG_NAMESPACE);
        }

    }

    /**
     * <p>
     * Clean up the test environment.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    protected void tearDown() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();

        if (cm.existsNamespace(CONFIG_NAMESPACE)) {
            cm.removeNamespace(CONFIG_NAMESPACE);
        }
        connFactory = null;
    }


    /**
     * <p>
     * Generic load test utilized by the other test drivers
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void genericFactoryLoad(String nameSpace, String testFile, int count) throws Exception{
        long startTime = 0;
        long endTime = 0;

        // load the proper test file
        cm.add(testFile);

        // load context
        Context context = new DummyContext(new Hashtable());
        context.bind("datasource", new DummyDataSource("From JNDI"));

        // test the actuall loading
        startTime = System.currentTimeMillis();
        connFactory = new DBConnectionFactoryImpl(nameSpace);
        endTime = System.currentTimeMillis();
        // output the stats
        System.out.println("\t Test took "+ (endTime -startTime) +" ms");
        // count entries
        Iterator it = connFactory.listConnectionProducerNames();
        int total = 0;
        while(it.hasNext()){
            total++;
            it.next();
        }
         System.out.println(" Expected: "+ count + " and retrieved "+total + " records");

     }

    /**
     * Test the following:
     * Load 1 connection provider
     */
    public void testLoad_One_Entry() throws Exception{
      System.out.println("[DBConnectionFactory stress test]");
      System.out.println("Testing the loading of 1 configured connection provider...");
      genericFactoryLoad(CONFIG_NAMESPACE, CONNECTION_CONFIG_FILE_LOW_ENTRY, 1);
      System.out.println();

    }

    /**
     * Test the following:
     * Load 5 connection provider
     */
    public void t2estLoad_Five_Entry() throws Exception{
      System.out.println("[DBConnectionFactory stress test]");
      System.out.println("Testing the loading of 5 configured connection providers...");
      genericFactoryLoad(CONFIG_NAMESPACE, CONNECTION_CONFIG_FILE_MED_ENTRIES, 5);
      System.out.println();

    }

    /**
     * Test the following:
     * Load 15 connection provider
     */
    public void t2estLoad_Fifteen_Entry() throws Exception{
      System.out.println("[DBConnectionFactory stress test]");
      System.out.println("Testing the loading of 15 configured connection providers...");
      genericFactoryLoad(CONFIG_NAMESPACE, CONNECTION_CONFIG_FILE_HIGH_ENTRIES, 15);
      System.out.println();

    }

}


