/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ajax.accuracytests;

import com.cronos.onlinereview.ajax.AjaxRequest;
import com.cronos.onlinereview.ajax.AjaxResponse;
import com.cronos.onlinereview.ajax.handlers.LoadTimelineTemplateHandler;

import junit.framework.TestCase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * Tests for LoadTimelineTemplateHandler class.
 *
 * @author assistant
 * @version 1.0
 */
public class TestLoadTimelineTemplateHandler extends TestCase {
    /** LoadTimelineTemplateHandler instance used for test. */
    private LoadTimelineTemplateHandler handler = null;

    /** Map instance used for test. */
    private Map params = null;

    /**
     * Setup the test environment.
     *
     * @throws Exception to JUnit.
     */
    protected void setUp() throws Exception {
        super.setUp();

        TestHelper.clearNamespaces();
        TestHelper.loadFile("test_files/accuracy/accuracy.xml");
        handler = new LoadTimelineTemplateHandler();
        params = new HashMap();
    }

    /**
     * Setup the test environment.
     *
     * @throws Exception to JUnit.
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        TestHelper.clearNamespaces();
    }

    /**
     * Tests LoadTimelineTemplateHandler() method with accuracy state.
     */
    public void testLoadTimelineTemplateHandlerAccuracy() {
        assertNotNull("creating LoadTimelineTemplateHandler fails", handler);
    }

    /**
     * Tests service(AjaxRequest request, Long userId) method with accuracy state. The
     * TemplateName is empty.
     */
    public void testServiceAccuracy1() {
        params.put("TemplateName", " ");
        SimpleDateFormat formatter = new SimpleDateFormat("MM.dd.yyyy h:mm");

        params.put("StartDate", formatter.format(new Date()));

        AjaxRequest request = new AjaxRequest("LoadTimelineTemplateHandler", params);

        AjaxResponse response = handler.service(request, new Long(999990));

        assertEquals("service fails - testServiceAccuracy2.", "Invalid parameter error",
            response.getStatus());
        assertEquals("service fails - testServiceAccuracy2.",
            "LoadTimelineTemplateHandler", response.getType());
    }

    /**
     * Tests service(AjaxRequest request, Long userId) method with accuracy state. The
     * start date is invalid.
     */
    public void testServiceAccuracy2() {
        params.put("TemplateName", "scorecard");
        params.put("StartDate", "invalid start date");

        AjaxRequest request = new AjaxRequest("LoadTimelineTemplateHandler", params);

        AjaxResponse response = handler.service(request, new Long(999990));


        assertEquals("service fails.", "Invalid parameter error", response.getStatus());
        assertEquals("service fails.", "LoadTimelineTemplateHandler", response.getType());
    }

    /**
     * Tests service(AjaxRequest request, Long userId) method with accuracy state. The
     * userId is null.
     */
    public void testServiceAccuracy3() {
        params.put("TemplateName", "scorecard");
        SimpleDateFormat formatter = new SimpleDateFormat("MM.dd.yyyy h:mm a");

        params.put("StartDate", formatter.format(new Date()));

        AjaxRequest request = new AjaxRequest("LoadTimelineTemplate", params);

        AjaxResponse response = handler.service(request, null);


        assertEquals("service fails.", "Login error", response.getStatus());
        assertEquals("service fails.", "LoadTimelineTemplate", response.getType());
    }

    /**
     * Tests service(AjaxRequest request, Long userId) method with accuracy state. The
     * userId is invalid.
     */
    public void testServiceAccuracy4() {
        params.put("TemplateName", "appeal");
        SimpleDateFormat formatter = new SimpleDateFormat("MM.dd.yyyy h:mm a");

        params.put("StartDate", formatter.format(new Date()));

        AjaxRequest request = new AjaxRequest("LoadTimelineTemplate", params);

        AjaxResponse response = handler.service(request, new Long(-1));


        assertEquals("service fails.", "Role error", response.getStatus());
        assertEquals("service fails.", "LoadTimelineTemplate", response.getType());
    }

    /**
     * Tests timelineToXml(Project project) method with accuracy state.
     */
    public void testTimelineToXmlAccuracy() {
        MockPhaseProject project = new MockPhaseProject();
        LoadTimelineTemplateHandler.timelineToXml(project);
    }
}
