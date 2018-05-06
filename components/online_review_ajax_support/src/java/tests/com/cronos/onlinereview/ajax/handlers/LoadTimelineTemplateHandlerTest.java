/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ajax.handlers;

import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.TestCase;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.cronos.onlinereview.ajax.AjaxRequest;
import com.cronos.onlinereview.ajax.AjaxResponse;
import com.cronos.onlinereview.ajax.AjaxSupportHelper;
import com.cronos.onlinereview.ajax.TestHelper;
import com.topcoder.date.workdays.DefaultWorkdays;
import com.topcoder.project.phases.MockPhase;
import com.topcoder.project.phases.MockPhaseType;
import com.topcoder.project.phases.MockProject;
import com.topcoder.project.phases.Project;
import com.topcoder.project.phases.template.MockPhaseTemplate;
import com.topcoder.project.phases.template.PhaseTemplate;
import com.topcoder.util.config.ConfigManager;

/**
 * Test the class <code>LoadTimelineTemplateHandler</code>.
 *
 *
 * @author assistant
 * @version 1.0
 */
public class LoadTimelineTemplateHandlerTest extends TestCase {

    /**
     * Represents the handler to test.
     */
    private LoadTimelineTemplateHandler handler;

    /**
     * Set up the environment.
     * @throws Exception to JUnit
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

        handler = new LoadTimelineTemplateHandler();
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
    }

    /**
     * Test method for LoadTimelineTemplateHandler().
     * @throws Exception to JUnit
     */
    public void testLoadTimelineTemplateHandler() throws Exception {
        assertNotNull("The constructor failed.", handler);

        PhaseTemplate tem = (PhaseTemplate) TestHelper.getPrivateFieldValue(
                LoadTimelineTemplateHandler.class, "phaseTemplate", handler);

        assertTrue("The type should be MockPhaseTemplate.", tem instanceof MockPhaseTemplate);
    }

    /**
     * Test method for service(com.cronos.onlinereview.ajax.AjaxRequest, java.lang.Long).
     * In this case, the request is ok and we should get a successful response.
     * And the start date is set.
     * @throws Exception to JUnit
     */
    public void testServiceAccuracy1() throws Exception {

        // prepare the parameters
        Map parameters = new HashMap();
        parameters.put("TemplateName", "Review");
        parameters.put("StartDate", AjaxSupportHelper.dateToString(TestHelper.DATE_1));

        // create the request
        AjaxRequest request = new AjaxRequest("LoadTimelineTemplate", parameters);

        // serve the request
        AjaxResponse response = handler.service(request, new Long(1));

        // verify the response
        assertEquals("The status should be success.", "Success", response.getStatus());
        assertEquals("The type should be LoadTimelineTemplate.", "LoadTimelineTemplate", response.getType());

        // parse and verify it
        String xml = response.getData().toString();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes("iso-8859-1")));

        // verify the result
        Element root = doc.getDocumentElement();
        assertEquals("The name should be timeline", "timeline", root.getNodeName());

        // verify the phase
        Node phases = root.getElementsByTagName("phases").item(0);
        Node phase = ((Element) phases).getElementsByTagName("phase").item(0);
        Node startDateNode = ((Element) phase).getElementsByTagName("start-date").item(0);
        Node endDateNode = ((Element) phase).getElementsByTagName("end-date").item(0);
        Node lengthNode = ((Element) phase).getElementsByTagName("length").item(0);
        assertEquals("The length is not right.", "1000", lengthNode.getFirstChild().getNodeValue());
    }

    /**
     * Test method for service(com.cronos.onlinereview.ajax.AjaxRequest, java.lang.Long).
     * In this case, the request is not ok for the user id is null.
     * We should get the response with "login error".
     * @throws Exception to JUnit
     */
    public void testServiceAccuracy2() throws Exception {

        // prepare the parameters
        Map parameters = new HashMap();
        parameters.put("TemplateName", "Review");
        parameters.put("StartDate", AjaxSupportHelper.dateToString(TestHelper.DATE_1));

        // create the request
        AjaxRequest request = new AjaxRequest("LoadTimelineTemplate", parameters);

        // serve the request
        AjaxResponse response = handler.service(request, null);

        // verify the response
        assertEquals("The status should be Login error.", "Login error", response.getStatus());
        assertEquals("The type should be LoadTimelineTemplate.", "LoadTimelineTemplate", response.getType());
    }

    /**
     * Test method for service(com.cronos.onlinereview.ajax.AjaxRequest, java.lang.Long).
     * In this case, the request is not ok for there is no such template name.
     * We should get the response with "Invalid template name error".
     * @throws Exception to JUnit
     */
    public void testServiceAccuracy3() throws Exception {

        // prepare the parameters
        Map parameters = new HashMap();
        parameters.put("TemplateName", "Not.A.Phase");
        parameters.put("StartDate", AjaxSupportHelper.dateToString(TestHelper.DATE_1));

        // create the request
        AjaxRequest request = new AjaxRequest("LoadTimelineTemplate", parameters);

        // serve the request
        AjaxResponse response = handler.service(request, new Long(1));

        // verify the response
        assertEquals("The status should be Invalid template name error.",
                "Invalid template name error", response.getStatus());
        assertEquals("The type should be LoadTimelineTemplate.", "LoadTimelineTemplate", response.getType());
    }

    /**
     * Test method for service(com.cronos.onlinereview.ajax.AjaxRequest, java.lang.Long).
     * In this case, the request is null.
     * Expected exception : {@link IllegalArgumentException}.
     * @throws Exception to JUnit
     */
    public void testServiceNullRequest() throws Exception {
        try {
            handler.service(null, null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for timelineToXml(com.topcoder.project.phases.Project).
     * In this case, the fixed date is not null so no dependencies will be renderered.
     * @throws Exception to JUnit
     */
    public void testTimelineToXmlAccuracy1() throws Exception {
        // this will return one phases
        Project prj = new MockProject(new Date(), new DefaultWorkdays());

        prj.setId(1);
        prj.setStartDate(TestHelper.DATE_1);

        MockPhase phase = new MockPhase();
        phase.setFixedStartDate(TestHelper.DATE_1);
        phase.calcStartDate = TestHelper.DATE_2;
        phase.calcEndDate = TestHelper.DATE_2;
        phase.setLength(1000);
        phase.setId(33);
        phase.setPhaseType(new MockPhaseType(1, "Appeal"));
        prj.addPhase(phase);

        String xml = LoadTimelineTemplateHandler.timelineToXml(prj);

        // verify the result

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes("iso-8859-1")));

        // verify the result
        Element root = doc.getDocumentElement();
        assertEquals("The name should be timeline", "timeline", root.getNodeName());

        // verify the phase
        Node phases = root.getElementsByTagName("phases").item(0);
        Node phaseNode = ((Element) phases).getElementsByTagName("phase").item(0);
        Node startDateNode = ((Element) phaseNode).getElementsByTagName("start-date").item(0);
        Node endDateNode = ((Element) phaseNode).getElementsByTagName("end-date").item(0);
        Node lengthNode = ((Element) phaseNode).getElementsByTagName("length").item(0);
        assertEquals("The length is not right.", "1000", lengthNode.getFirstChild().getNodeValue());

        // there is no dependencies
        NodeList dependencies = ((Element) phaseNode).getElementsByTagName("dependencies");
        assertEquals("There is no dependencies node.", 0, dependencies.getLength());
    }

    /**
     * Test method for timelineToXml(com.topcoder.project.phases.Project).
     * In this case, the fixed date is null so dependencies will be renderered.
     * @throws Exception to JUnit
     */
    public void testTimelineToXmlAccuracy2() throws Exception {
        // this will return one phases
        Project prj = new MockProject(new Date(), new DefaultWorkdays());

        prj.setId(1);
        prj.setStartDate(TestHelper.DATE_1);

        MockPhase phase = new MockPhase();
        phase.setFixedStartDate(null);
        phase.calcStartDate = TestHelper.DATE_2;
        phase.calcEndDate = TestHelper.DATE_2;
        phase.setLength(1000);
        phase.setId(33);
        phase.setPhaseType(new MockPhaseType(1, "Appeal"));
        prj.addPhase(phase);

        String xml = LoadTimelineTemplateHandler.timelineToXml(prj);

        // verify the result

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes("iso-8859-1")));

        // verify the result
        Element root = doc.getDocumentElement();
        assertEquals("The name should be timeline", "timeline", root.getNodeName());

        // verify the phase
        Node phases = root.getElementsByTagName("phases").item(0);
        Node phaseNode = ((Element) phases).getElementsByTagName("phase").item(0);
        Node startDateNode = ((Element) phaseNode).getElementsByTagName("start-date").item(0);
        Node endDateNode = ((Element) phaseNode).getElementsByTagName("end-date").item(0);
        Node lengthNode = ((Element) phaseNode).getElementsByTagName("length").item(0);
        assertEquals("The length is not right.", "1000", lengthNode.getFirstChild().getNodeValue());

        // there is dependencies node
        NodeList dependencies = ((Element) phaseNode).getElementsByTagName("dependencies");
        assertEquals("There is dependencies node.", 1, dependencies.getLength());
        Element deps = (Element) dependencies.item(0);
        Element dependencyId = (Element) deps.getElementsByTagName("dependency-phase-id").item(0);
        assertEquals("The id should be 31", "31", dependencyId.getFirstChild().getNodeValue());
        Element dependentId = (Element) deps.getElementsByTagName("dependent-phase-id").item(0);
        assertEquals("The id should be 32", "32", dependentId.getFirstChild().getNodeValue());
    }

    /**
     * Test method for timelineToXml(com.topcoder.project.phases.Project).
     * In this case, the project is null.
     * Expected exception : IllegalArgumentException.
     * @throws Exception to JUnit
     */
    public void testTimelineToXmlFailure() throws Exception {
        try {
            LoadTimelineTemplateHandler.timelineToXml(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }
}
