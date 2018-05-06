/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ajax;

import com.cronos.onlinereview.ajax.handlers.LoadTimelineTemplateHandler;
import com.cronos.onlinereview.ajax.handlers.PlaceAppealHandler;
import com.cronos.onlinereview.ajax.handlers.ResolveAppealHandler;
import com.cronos.onlinereview.ajax.handlers.SetScorecardStatusHandler;
import com.cronos.onlinereview.ajax.handlers.SetTimelineNotificationHandler;
import com.topcoder.management.phase.MockPhaseManager;
import com.topcoder.management.phase.PhaseManagementException;
import com.topcoder.management.project.MockProjectManager;
import com.topcoder.management.resource.MockResourceManager;
import com.topcoder.management.resource.persistence.ResourcePersistenceException;
import com.topcoder.management.review.MockReviewManager;
import com.topcoder.management.review.ReviewManagementException;
import com.topcoder.management.scorecard.MockScorecardManager;
import com.topcoder.management.scorecard.PersistenceException;
import com.topcoder.management.deliverable.persistence.UploadPersistenceException;
import com.topcoder.management.deliverable.MockUploadManager;
import com.topcoder.project.phases.template.MockPhaseTemplate;
import com.topcoder.project.phases.template.PhaseTemplateException;
import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.log.Level;
import junit.framework.Assert;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * <p>This test case contains the tests to be run to verify that the identified bugs have been fixed.</p>
 *
 * @author  isv
 * @version 1.0
 */
public class BugResolutionTestCase extends TestCase {

    /**
     * <p>A <code>PlaceAppealHandler</code> instance to be used for testing purposes.</p>
     */
    private PlaceAppealHandler placeAppealHandler;

    /**
     * <p>A <code>ResolveAppealHandler</code> instance to be used for testing purposes.</p>
     */
    private ResolveAppealHandler resolveAppealHandler;

    /**
     * <p>A <code>LoadTimelineTemplateHandler</code> instance to be used for testing purposes.</p>
     */
    private LoadTimelineTemplateHandler loadTimelineTemplateHandler;

    /**
     * <p>A <code>SetScorecardStatusHandler</code> instance to be used for testing purposes.</p>
     */
    private SetScorecardStatusHandler setScorecardStatusHandler;

    /**
     * <p>A <code>SetTimelineNotificationHandler</code> instance to be used for testing purposes.</p>
     */
    private SetTimelineNotificationHandler setTimelineNotificationHandler;

    /**
     * Set up the environment.
     * @throws Exception to JUnit.
     */
    protected void setUp() throws Exception {
        super.setUp();
        // load the configurations
        ConfigManager cm = ConfigManager.getInstance();
        if (!cm.existsNamespace("com.cronos.onlinereview.ajax")) {
            cm.add("default.xml");
            cm.add("objectfactory.xml");
            cm.add("scorecalculator.xml");
        }

        this.placeAppealHandler = new PlaceAppealHandler();
        this.resolveAppealHandler = new ResolveAppealHandler();
        this.loadTimelineTemplateHandler = new LoadTimelineTemplateHandler();
        this.setScorecardStatusHandler = new SetScorecardStatusHandler();
        this.setTimelineNotificationHandler = new SetTimelineNotificationHandler();
        MockLog.releaseState();
        MockLog.init();
        MockResourceManager.releaseState();
        MockResourceManager.init();
        MockScorecardManager.releaseState();
        MockScorecardManager.init();
        MockPhaseTemplate.releaseState();
        MockPhaseTemplate.init();
        MockProjectManager.releaseState();
        MockProjectManager.init();
    }

    /**
     * Clean up the environment.
     *
     * @throws Exception to JUnit
     */
    protected void tearDown() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();
        Iterator it = cm.getAllNamespaces();
        while (it.hasNext()) {
            cm.removeNamespace(it.next().toString());
        }
        this.placeAppealHandler = null;
        this.resolveAppealHandler = null;
        this.loadTimelineTemplateHandler = null;
        this.setScorecardStatusHandler = null;
        this.setTimelineNotificationHandler = null;
        MockLog.releaseState();
        MockResourceManager.releaseState();
        MockPhaseTemplate.releaseState();
        MockProjectManager.releaseState();
        MockScorecardManager.releaseState();
    }

/*
    public void testUserId_PlaceAppealHandler() throws Exception {
        // Test for handling valid request parameter name
        // prepare the ajax request
        Map map = new HashMap();
        map.put("ReviewId", "1");
        map.put("ItemId", "2");
        map.put("Text", "appeal text");

        // Test for handling the valid request parameter name
        AjaxRequest request = new AjaxRequest("PlaceAppeal", map);
        AjaxResponse ajaxResponse = this.placeAppealHandler.service(request, new Long(3));
        Assert.assertEquals("Should have responded with response of ROLE_ERROR status if user is not owner of the " +
                            "submission", "Success", ajaxResponse.getStatus());
    }
*/

    /**
     * <p>Tests the resolution of bug caused by the <code>PlaceAppealHandler</code> using an incorrect name for the
     * request parameter providing the appeal text. The orignial handler code expects the parameter to be named <code>
     * "text"</code> while <code>"Text"</code> is a correct name of parameter.</p>
     *
     * @throws Exception if an unexpected error occurs.
     */
    public void testTextParameterHandling_PlaceAppealHandler() throws Exception {
        // prepare the ajax request
        Map map = new HashMap();
        map.put("ReviewId", "1");
        map.put("ItemId", "2");
        map.put("text", "appeal text");
        map.put("tExt", "appeal text");
        map.put("TEXT", "appeal text");
        map.put("TEXt", "appeal text");
        map.put("Text", "");
        map.put("Text", "           ");

        // Test for handling the invalid request parameter name
        AjaxRequest request = new AjaxRequest("PlaceAppeal", map);
        AjaxResponse ajaxResponse = this.placeAppealHandler.service(request, new Long(2));
        Assert.assertEquals("Should have responded with response of INVALID_PARAMETER_ERROR status if 'Text' parameter "
                            + "is missing from request", "The appeal text must be provided.", ajaxResponse.getStatus());

        // Test for handling valid request parameter name
        // prepare the ajax request
        map = new HashMap();
        map.put("ReviewId", "1");
        map.put("ItemId", "2");
        map.put("Text", "appeal text");
        map.put("TextLength", "" + ((String)map.get("Text")).length());

        // Test for handling the valid request parameter name
        request = new AjaxRequest("PlaceAppeal", map);
        ajaxResponse = this.placeAppealHandler.service(request, new Long(2));
        Assert.assertEquals("Should have responded with response of SUCCESS status if 'Text' parameter is provided by "
                            + "request", "Success", ajaxResponse.getStatus());
    }

    /**
     * <p>Tests the resolution of bug caused by the <code>ResolveAppealHandler</code> using an incorrect name for the
     * request parameter providing the appeal text. The orignial handler code expects the parameter to be named <code>
     * "text"</code> while <code>"Text"</code> is a correct name of parameter.</p>
     *
     * @throws Exception if an unexpected error occurs.
     */
    public void testTextParameterHandling_ResolveAppealHandler() throws Exception {
        // prepare the ajax request
        Map map = new HashMap();
        map.put("ReviewId", "1");
        map.put("ItemId", "1");
        map.put("Status", "Succeeded");
        map.put("Answer", "4");
        map.put("text", "appeal text");
        map.put("tExt", "appeal text");
        map.put("TEXT", "appeal text");
        map.put("TEXt", "appeal text");
        map.put("Text", "");
        map.put("Text", "           ");

        // Test for handling the invalid request parameter name
        AjaxRequest request = new AjaxRequest("ResolveAppeal", map);
        AjaxResponse ajaxResponse = this.resolveAppealHandler.service(request, new Long(2));
        Assert.assertEquals("Should have responded with response of INVALID_PARAMETER_ERROR status if 'Text' parameter "
                            + "is missing from request", "The appeal response text must be provided.", ajaxResponse.getStatus());

        // Test for handling valid request parameter name
        // prepare the ajax request
        map = new HashMap();
        map.put("ReviewId", "1");
        map.put("ItemId", "1");
        map.put("Status", "Succeeded");
        map.put("Answer", "4");
        map.put("Text", "appeal resolution text");

        // Test for handling the valid request parameter name
        request = new AjaxRequest("ResolveAppeal", map);
        ajaxResponse = this.resolveAppealHandler.service(request, new Long(2));
        Assert.assertEquals("Should have responded with response of SUCCESS status if 'Text' parameter is provided by "
                            + "request", "Success", ajaxResponse.getStatus());
    }

    /**
     * <p>Tests the resolution of issue caused by the existing production handlers not logging the stack traces for
     * encountered exceptions causing the handler to return an error response.</p>
     *
     * <p>This test passes various requests to tested <code>LoadTimelineTemplateHandler</code> instance. Each request is
     * expected to cause an exception while processing it. The test verifies that the exception stack trace is logged
     * for each of request.</p>
     *
     * @throws Exception if an unexpected error occurs.
     */
    public void testErrorLog_LoadTimelineTemplateHandler() throws Exception {
        final String requestType = "LoadTimelineTemplate";
        final List requestParams = new ArrayList();

        // Test when data can not be parsed successfully
        Map map;

        map = new HashMap();
        map.put("TemplateName", "Screening");
        map.put("StartDate", "NonDateObject");
        requestParams.add(map);

        map = new HashMap();
        map.put("TemplateName", "Review");
        map.put("StartDate", "01/01/2006 01:01");
        requestParams.add(map);

        for (int i = 0; i < requestParams.size(); i++) {
            MockLog.releaseState();
            Map params = (Map) requestParams.get(i);
            AjaxRequest request = new AjaxRequest(requestType, params);
            this.loadTimelineTemplateHandler.service(request, new Long(2));
            assertExceptionStackTraceLogged(requestType);
            MockLog.releaseState();
        }

        // Create valid request
        map = new HashMap();
        map.put("TemplateName", "Appeal");
        map.put("StartDate", "01.01.2006 01:01 AM");

        // Test when ResourceManager reports an error
        MockLog.releaseState();
        MockResourceManager.throwGlobalException(new ResourcePersistenceException("Test Exception"));
        this.loadTimelineTemplateHandler.service(new AjaxRequest(requestType, map), new Long(2));
        assertExceptionStackTraceLogged(requestType);
        MockResourceManager.releaseState();

        // Test when PhaseTemplate reports an error
        MockLog.releaseState();
        MockPhaseTemplate.throwGlobalException(new PhaseTemplateException("Test Exception"));
        this.loadTimelineTemplateHandler.service(new AjaxRequest(requestType, map), new Long(2));
        assertExceptionStackTraceLogged(requestType);
        MockPhaseTemplate.releaseState();
        MockLog.releaseState();
    }

    /**
     * <p>Tests the resolution of issue caused by the existing production handlers not logging the stack traces for
     * encountered exceptions causing the handler to return an error response.</p>
     *
     * <p>This test passes various requests to tested <code>LoadTimelineTemplateHandler</code> instance. Each request is
     * expected to cause an exception while processing it. The test verifies that the exception stack trace is logged
     * for each of request.</p>
     *
     * @throws Exception if an unexpected error occurs.
     */
    public void testErrorLog_SetScorecardStatusHandler() throws Exception {
        final String requestType = "SetScorecardStatus";
        final List requestParams = new ArrayList();

        // Test when data can not be parsed successfully
        Map map;

        map = new HashMap();
        map.put("ScorecardId", "fsvgsvdv");
        map.put("Status", "Active");
        requestParams.add(map);

        map = new HashMap();
        map.put("ScorecardId", "1111111111111111111111111111111111111111111111111111111111111111");
        map.put("Status", "Active");
        requestParams.add(map);

        for (int i = 0; i < requestParams.size(); i++) {
            MockLog.releaseState();
            Map params = (Map) requestParams.get(i);
            AjaxRequest request = new AjaxRequest(requestType, params);
            this.setScorecardStatusHandler.service(request, new Long(2));
            assertExceptionStackTraceLogged(requestType);
            MockLog.releaseState();
        }

        // Create valid request
        map = new HashMap();
        map.put("ScorecardId", "1");
        map.put("Status", "Active");

        // Test when ResourceManager reports an error
        MockLog.releaseState();
        MockResourceManager.throwGlobalException(new ResourcePersistenceException("Test Exception"));
        this.setScorecardStatusHandler.service(new AjaxRequest(requestType, map), new Long(2));
        assertExceptionStackTraceLogged(requestType);
        MockResourceManager.releaseState();

        // Test when ScorecardManager reports an error
        MockLog.releaseState();
        MockScorecardManager.throwGlobalException(new PersistenceException("Test Exception"));
        this.setScorecardStatusHandler.service(new AjaxRequest(requestType, map), new Long(2));
        assertExceptionStackTraceLogged(requestType);
        MockScorecardManager.releaseState();
    }

    /**
     * <p>Tests the resolution of issue caused by the existing production handlers not logging the stack traces for
     * encountered exceptions causing the handler to return an error response.</p>
     *
     * <p>This test passes various requests to tested <code>SetTimelineNotificationHandler</code> instance. Each request
     * is expected to cause an exception while processing it. The test verifies that the exception stack trace is logged
     * for each of request.</p>
     *
     * @throws Exception if an unexpected error occurs.
     */
    public void testErrorLog_SetTimelineNotificationHandler() throws Exception {
        final String requestType = "SetTimelineNotification";
        final List requestParams = new ArrayList();

        // Test when data can not be parsed successfully
        Map map;

        map = new HashMap();
        map.put("ProjectId", "fsvgsvdv");
        map.put("Status", "On");
        requestParams.add(map);

        map = new HashMap();
        map.put("ProjectId", "111111111111111111111111111111111111111111111111111111111111111111111");
        map.put("Status", "On");
        requestParams.add(map);

        for (int i = 0; i < requestParams.size(); i++) {
            MockLog.releaseState();
            Map params = (Map) requestParams.get(i);
            AjaxRequest request = new AjaxRequest(requestType, params);
            this.setTimelineNotificationHandler.service(request, new Long(2));
            assertExceptionStackTraceLogged(requestType);
            MockLog.releaseState();
        }

        // Create valid request
        map = new HashMap();
        map.put("ProjectId", "1");
        map.put("Status", "On");

        // Test when ResourceManager reports an error
        MockLog.releaseState();
        MockResourceManager.throwGlobalException(new ResourcePersistenceException("Test Exception"));
        this.setTimelineNotificationHandler.service(new AjaxRequest(requestType, map), new Long(2));
        assertExceptionStackTraceLogged(requestType);
        MockResourceManager.releaseState();

        // Test when projectManager reports an error
        MockLog.releaseState();
        MockProjectManager.throwGlobalException(new com.topcoder.management.project.PersistenceException("Test Exception"));
        this.setTimelineNotificationHandler.service(new AjaxRequest(requestType, map), new Long(2));
        assertExceptionStackTraceLogged(requestType);
        MockProjectManager.releaseState();
    }

    /**
     * <p>Tests the resolution of issue caused by the existing production handlers not logging the stack traces for
     * encountered exceptions causing the handler to return an error response.</p>
     *
     * <p>This test passes various requests to tested <code>SetTimelineNotificationHandler</code> instance. Each request
     * is expected to cause an exception while processing it. The test verifies that the exception stack trace is logged
     * for each of request.</p>
     *
     * @throws Exception if an unexpected error occurs.
     */
    public void testErrorLog_PlaceAppealHandler() throws Exception {
        final String requestType = "PlaceAppeal";
        final List requestParams = new ArrayList();

        // Test when data can not be parsed successfully
        Map map;

        map = new HashMap();
        map.put("ReviewId", "fsvgsvdv");
        map.put("ItemId", "2");
        map.put("Text", "Appeal text");
        requestParams.add(map);

        map = new HashMap();
        map.put("ReviewId", "11111111111111111111111111111111111111111111111111111111111111111");
        map.put("ItemId", "2");
        map.put("Text", "Appeal text");
        requestParams.add(map);

        map = new HashMap();
        map.put("ReviewId", "1");
        map.put("ItemId", "ffffffff");
        map.put("Text", "Appeal text");
        requestParams.add(map);

        map = new HashMap();
        map.put("ReviewId", "1");
        map.put("ItemId", "11111111111111111111111111111111111111111111111111111111111111111111");
        map.put("Text", "Appeal text");
        requestParams.add(map);

        for (int i = 0; i < requestParams.size(); i++) {
            MockLog.releaseState();
            Map params = (Map) requestParams.get(i);
            AjaxRequest request = new AjaxRequest(requestType, params);
            this.placeAppealHandler.service(request, new Long(2));
            assertExceptionStackTraceLogged(requestType);
            MockLog.releaseState();
        }

        // Create valid request
        map = new HashMap();
        map.put("ReviewId", "1");
        map.put("ItemId", "2");
        map.put("Text", "Appeal text");

        // Test when ResourceManager reports an error
        MockLog.releaseState();
        MockResourceManager.throwGlobalException(new ResourcePersistenceException("Test Exception"));
        this.placeAppealHandler.service(new AjaxRequest(requestType, map), new Long(2));
        assertExceptionStackTraceLogged(requestType);
        MockResourceManager.releaseState();

        // Test when UploadManager reports an error
        MockLog.releaseState();
        MockUploadManager.throwGlobalException(new UploadPersistenceException("Test Exception"));
        this.placeAppealHandler.service(new AjaxRequest(requestType, map), new Long(2));
        assertExceptionStackTraceLogged(requestType);
        MockUploadManager.releaseState();

        // Test when PhaseManager reports an error
        MockLog.releaseState();
        MockPhaseManager.throwGlobalException(new PhaseManagementException("Test Exception"));
        this.placeAppealHandler.service(new AjaxRequest(requestType, map), new Long(2));
        assertExceptionStackTraceLogged(requestType);
        MockPhaseManager.releaseState();

        // Test when ReviewManager reports an error
        MockLog.releaseState();
        MockReviewManager.throwGlobalException(new ReviewManagementException("Test Exception"));
        this.placeAppealHandler.service(new AjaxRequest(requestType, map), new Long(2));
        assertExceptionStackTraceLogged(requestType);
        MockReviewManager.releaseState();
    }

    /**
     * <p>Tests the resolution of issue caused by the existing production handlers not logging the stack traces for
     * encountered exceptions causing the handler to return an error response.</p>
     *
     * <p>This test passes various requests to tested <code>SetTimelineNotificationHandler</code> instance. Each request
     * is expected to cause an exception while processing it. The test verifies that the exception stack trace is logged
     * for each of request.</p>
     *
     * @throws Exception if an unexpected error occurs.
     */
    public void testErrorLog_ResolveAppealHandler() throws Exception {
        final String requestType = "ResolveAppeal";
        final List requestParams = new ArrayList();

        // Test when data can not be parsed successfully
        Map map;

        map = new HashMap();
        map.put("ReviewId", "fsvgsvdv");
        map.put("ItemId", "2");
        map.put("Text", "Appeal text");
        map.put("Status", "Failed");
        map.put("Answer", "4/4");
        requestParams.add(map);

        map = new HashMap();
        map.put("ReviewId", "11111111111111111111111111111111111111111111111111111111111111111");
        map.put("ItemId", "2");
        map.put("Text", "Appeal text");
        map.put("Status", "Failed");
        map.put("Answer", "4/4");
        requestParams.add(map);

        map = new HashMap();
        map.put("ReviewId", "1");
        map.put("ItemId", "ffffffff");
        map.put("Text", "Appeal text");
        map.put("Status", "Failed");
        map.put("Answer", "4/4");
        requestParams.add(map);

        map = new HashMap();
        map.put("ReviewId", "1");
        map.put("ItemId", "11111111111111111111111111111111111111111111111111111111111111111111");
        map.put("Text", "Appeal text");
        map.put("Status", "Failed");
        map.put("Answer", "4/4");
        requestParams.add(map);

        for (int i = 0; i < requestParams.size(); i++) {
            MockLog.releaseState();
            Map params = (Map) requestParams.get(i);
            AjaxRequest request = new AjaxRequest(requestType, params);
            this.resolveAppealHandler.service(request, new Long(3));
            assertExceptionStackTraceLogged(requestType);
            MockLog.releaseState();
        }

        // Create valid request
        map = new HashMap();
        map.put("ReviewId", "1");
        map.put("ItemId", "1");
        map.put("Text", "Appeal text");
        map.put("Status", "Failed");
        map.put("Answer", "Yes");

        // Test when ResourceManager reports an error
        MockLog.releaseState();
        MockResourceManager.throwGlobalException(new ResourcePersistenceException("Test Exception"));
        this.resolveAppealHandler.service(new AjaxRequest(requestType, map), new Long(3));
        assertExceptionStackTraceLogged(requestType);
        MockResourceManager.releaseState();

        // Test when PhaseManager reports an error
        MockLog.releaseState();
        MockPhaseManager.throwGlobalException(new PhaseManagementException("Test Exception"));
        this.resolveAppealHandler.service(new AjaxRequest(requestType, map), new Long(3));
        assertExceptionStackTraceLogged(requestType);
        MockPhaseManager.releaseState();

        // Test when ReviewManager reports an error
        MockLog.releaseState();
        MockReviewManager.throwGlobalException(new ReviewManagementException("Test Exception"));
        this.resolveAppealHandler.service(new AjaxRequest(requestType, map), new Long(3));
        assertExceptionStackTraceLogged(requestType);
        MockReviewManager.releaseState();

        // Test when ScorecardManager reports an error
        MockLog.releaseState();
        MockScorecardManager.throwGlobalException(new PersistenceException("Test Exception"));
        this.resolveAppealHandler.service(new AjaxRequest(requestType, map), new Long(3));
        assertExceptionStackTraceLogged(requestType);
        MockScorecardManager.releaseState();
    }

    /**
     * <p>Tests the resolution of bug caused by the <code>PlaceAppealHandler</code> validating the reviewers against
     * <code>Reviewer</code> role only. In fact the reviewer resolving the appeal may be assigned any of <code>Reviewer
     * </code>, <code>Accuracy Reviewer</code>, <code>Failure Reviewer</code>, <code>Stress Reviewer</code> role.</p>
     *
     * @throws Exception if an unexpected error occurs.
     */
    public void testReviewerRoles_ResolveAppealHandler() throws Exception {
        final String requestType = "ResolveAppeal";
        AjaxResponse ajaxResponse;

        // Create valid request
        Map map = new HashMap();
        map.put("ItemId", "1");
        map.put("Text", "Appeal text");
        map.put("Status", "Failed");
        map.put("Answer", "Yes");

        // Test when reviewer is granted a Reviewer role
        map.put("ReviewId", "3");
        ajaxResponse = this.resolveAppealHandler.service(new AjaxRequest(requestType, map), new Long(3));
        Assert.assertEquals("Should have responded with response of 'Success' status for reviewer greanted 'Reviewer' "
                            + "role", "Success", ajaxResponse.getStatus());

        // Test when reviewer is granted an Accuracy Reviewer role
        map.put("ReviewId", "4");
        ajaxResponse = this.resolveAppealHandler.service(new AjaxRequest(requestType, map), new Long(4));
        Assert.assertEquals("Should have responded with response of 'Success' status for reviewer greanted 'Accuracy "
                             + "Reviewer' role", "Success", ajaxResponse.getStatus());

        // Test when reviewer is granted a Failure Reviewer role
        map.put("ReviewId", "5");
        ajaxResponse = this.resolveAppealHandler.service(new AjaxRequest(requestType, map), new Long(5));
        Assert.assertEquals("Should have responded with response of 'Success' status for reviewer greanted 'Failure "
                             + "Reviewer' role", "Success", ajaxResponse.getStatus());

        // Test when reviewer is granted a Stress Reviewer role
        map.put("ReviewId", "6");
        ajaxResponse = this.resolveAppealHandler.service(new AjaxRequest(requestType, map), new Long(6));
        Assert.assertEquals("Should have responded with response of 'Success' status for reviewer greanted 'Stress "
                             + "Reviewer' role", "Success", ajaxResponse.getStatus());
    }

    /**
     * <p>Verifies that the exception stack trace is logged for the request of specified type.</p>
     *
     * @param requestType a <code>String</code> specifying the type of the request.
     */
    private void assertExceptionStackTraceLogged(String requestType) {
        List methodArguments = MockLog.getMethodArguments("log_Level_Object");
        Assert.assertEquals("The method must log the exception stack trace", 4, methodArguments.size());
        Map args = (Map) methodArguments.get(3);
        Level exceptionLevel = (Level) args.get("1");
        Assert.assertSame("The exception stack trace is not logged at ERROR level", Level.ERROR, exceptionLevel);
        String exceptionLog = (String) args.get("2");
        Assert.assertTrue("The exception stack trace is not logged correctly",
                          exceptionLog.startsWith(requestType + " : Exception : \n"));
    }
}
