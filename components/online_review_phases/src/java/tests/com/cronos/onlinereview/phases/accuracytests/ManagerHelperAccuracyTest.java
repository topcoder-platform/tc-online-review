package com.cronos.onlinereview.phases.accuracytests;

import com.cronos.onlinereview.external.UserRetrieval;
import com.cronos.onlinereview.phases.ManagerHelper;
import com.topcoder.management.deliverable.UploadManager;
import com.topcoder.management.phase.PhaseManager;
import com.topcoder.management.project.ProjectManager;
import com.topcoder.management.resource.ResourceManager;
import com.topcoder.management.review.ReviewManager;
import com.topcoder.management.review.scoreaggregator.ReviewScoreAggregator;
import com.topcoder.management.scorecard.ScorecardManager;
import com.topcoder.util.idgenerator.IDGeneratorFactory;

/**
 * Accuracy test for ManagerHelper class.
 *
 * @author tuenm
 * @version 1.0
 */
public class ManagerHelperAccuracyTest extends BaseAccuracyTest {

    /**
     * <p/>
     * Sets up the test environment.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    protected void setUp() throws Exception {
        super.setUp();

        AccuracyTestHelper.releaseSingletonInstance(IDGeneratorFactory.class, "generators");

        AccuracyTestHelper.loadTestConfiguration();
    }

    /**
     * <p/>
     * Cleans up the test environment.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit.
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        AccuracyTestHelper.clearAllConfigNS();
    }

    /**
     * Tests ManagerHelper constructor. No exception is expected.
     *
     * @throws Exception to JUnit.
     */
    public void testConstructor() throws Exception {
        ManagerHelper helper = new ManagerHelper();
        assertNotNull("The instance must be created", helper);
    }

    /**
     * Tests get PhaseManager from ManagerHelper. The returned value is expected to be non null.
     *
     * @throws Exception to JUnit.
     */
    public void testGetPhaseManager() throws Exception {
        ManagerHelper helper = new ManagerHelper();
        PhaseManager manager = helper.getPhaseManager();
        assertNotNull("Cannot get PhaseManager using ManagerHelper.", manager);

        assertEquals("Hanlders array should be empty.", 0, manager.getAllHandlers().length);
        assertTrue("Phase statuses should be initialized.", manager.getAllPhaseStatuses().length > 0);
        assertTrue("Phase types should be initialized.", manager.getAllPhaseTypes().length > 0);
    }

    /**
     * Tests get ProjectManager from ManagerHelper. The returned value is expected to be non null.
     *
     * @throws Exception to JUnit.
     */
    public void testGetProjectManager() throws Exception {
        ManagerHelper helper = new ManagerHelper();
        ProjectManager manager = helper.getProjectManager();
        assertNotNull("Cannot get ProjectManager using ManagerHelper.", manager);

        assertTrue("Project types should be initialized.", manager.getAllProjectTypes().length > 0);
        assertTrue("Project statuses should be initialized.", manager.getAllProjectStatuses().length > 0);
        assertTrue("Project categories should be initialized.", manager.getAllProjectCategories().length > 0);
    }

    /**
     * Tests get ScorecardManager from ManagerHelper. The returned value is expected to be non null.
     *
     * @throws Exception to JUnit.
     */
    public void testGetScorecardManager() throws Exception {
        ManagerHelper helper = new ManagerHelper();
        ScorecardManager manager = helper.getScorecardManager();
        assertNotNull("Cannot get ScorecardManager using ManagerHelper.", manager);

        assertTrue("Scorecard types should be initialized.", manager.getAllScorecardTypes().length > 0);
        assertTrue("Scorecard statuses should be initialized.", manager.getAllScorecardStatuses().length > 0);
        assertTrue("Question types should be initialized.", manager.getAllQuestionTypes().length > 0);
    }

    /**
     * Tests get ReviewManager from ManagerHelper. The returned value is expected to be non null.
     *
     * @throws Exception to JUnit.
     */
    public void testGetReviewManager() throws Exception {
        ManagerHelper helper = new ManagerHelper();
        ReviewManager manager = helper.getReviewManager();
        assertNotNull("Cannot get ReviewManager using ManagerHelper.", manager);
    }

    /**
     * Tests get ResourceManager from ManagerHelper. The returned value is expected to be non null.
     *
     * @throws Exception to JUnit.
     */
    public void testGetResourceManager() throws Exception {
        ManagerHelper helper = new ManagerHelper();
        ResourceManager manager = helper.getResourceManager();
        assertNotNull("Cannot get ResourceManager using ManagerHelper.", manager);

        assertTrue("Notification types should be initialized.", manager.getAllNotificationTypes().length > 0);
        assertTrue("Resource roles should be initialized.", manager.getAllResourceRoles().length > 0);
    }

    /**
     * Tests get UploadManager from ManagerHelper. The returned value is expected to be non null.
     *
     * @throws Exception to JUnit.
     */
    public void testGetUploadManager() throws Exception {
        ManagerHelper helper = new ManagerHelper();
        UploadManager manager = helper.getUploadManager();
        assertNotNull("Cannot get UploadManager using ManagerHelper.", manager);

        assertTrue("Upload types should be initialized.", manager.getAllUploadTypes().length > 0);
        assertTrue("Upload statuses should be initialized.", manager.getAllUploadStatuses().length > 0);

    }

    /**
     * Tests get UserRetrieval from ManagerHelper. The returned value is expected to be non null.
     *
     * @throws Exception to JUnit.
     */
    public void testGetUserRetrieval() throws Exception {
        ManagerHelper helper = new ManagerHelper();
        UserRetrieval userRetrieval = helper.getUserRetrieval();
        assertNotNull("Cannot get UserRetrieval using ManagerHelper.", userRetrieval);
    }

    /**
     * Tests get ReviewScoreAggregator from ManagerHelper. The returned value is expected to be non null.
     *
     * @throws Exception to JUnit.
     */
    public void testReviewScoreAggregator() throws Exception {
        ManagerHelper helper = new ManagerHelper();
        ReviewScoreAggregator agg = helper.getScorecardAggregator();
        assertNotNull("Cannot get ReviewScoreAggregator using ManagerHelper.", agg);

        assertNotNull("Score aggregation algorithm should be initialized.", agg.getScoreAggregationAlgorithm());
        assertNotNull("Placement algorithm should be initialized.", agg.getPlaceAssignmentAlgorithm());
        assertNotNull("Tie breaker should be initialized.", agg.getTieBreaker());
        assertNotNull("Tie detector should be initialized.", agg.getTieDetector());
    }
}
