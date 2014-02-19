<%--
  - Author: TCSASSEMBLER
  - Version: 2.0
  - Copyright (C) 2005 - 2014 TopCoder Inc., All Rights Reserved.
  -
  - Description: This page fragment renders the tabs for all pages from Online Review application.
--%>
<%@ page language="java" isELIgnored="false" %>
<%@ page import="java.text.DecimalFormat,com.topcoder.shared.util.ApplicationServer" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="or" uri="/or-tags" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>

<%-- We need to have no spaces between tabs, that's why the formatting is weird --%>
<div id="mainTabs">
    <div style="float: left;">
    <c:if test="${orfn:isUserLoggedIn(pageContext.request)}">
        <c:if test="${(not empty projectTabIndex) and (projectTabIndex == 1)}">
            <img src="/i/or/tab_my_open_projects_on.gif" width="119" height="36" border="0" name="tab1" />
        </c:if>
        <c:if test="${(empty projectTabIndex) or (projectTabIndex != 1)}">
            <a href="<or:url value='/actions/ListProjects?scope=my' />" onmouseover="img_act('tab1')" onmouseout="img_inact('tab1')">
                <img src="/i/or/tab_my_open_projects.gif" width="119" height="36" border="0" name="tab1" />
            </a>
        </c:if>
    </c:if>
    </div>
    <div style="float: left;">
    <c:if test="${(not empty projectTabIndex) and (projectTabIndex == 2)}">
        <img src="/i/or/tab_all_open_projects_on.gif" width="119" height="36" name="tab2" />
    </c:if>
    </div>
    <div style="float: left;">
    <c:if test="${(empty projectTabIndex) or (projectTabIndex != 2)}">
        <a href="<or:url value='/actions/ListProjects?scope=all' />" onmouseover="img_act('tab2')" onmouseout="img_inact('tab2')">
            <img src="/i/or/tab_all_open_projects.gif" width="119"height="36" border="0" name="tab2" />
        </a>
    </c:if>
    </div>
    <div style="float: left;">
        <c:if test="${(not empty projectTabIndex) and (projectTabIndex == 4)}">
            <img src="/i/or/tab_draft_projects_on.gif" width="119" height="36" border="0" name="tab4"/>
        </c:if>
        <c:if test="${(empty projectTabIndex) or (projectTabIndex != 4)}">
            <a href="<or:url value='/actions/ListProjects?scope=draft' />" onmouseover="img_act('tab4')"
                       onmouseout="img_inact('tab4')">
                <img src="/i/or/tab_draft_projects.gif" width="119" height="36" border="0" name="tab4"/>
            </a>
        </c:if>
    </div>
    <div style="float: left;">
    <c:if test="${orfn:isUserLoggedIn(pageContext.request)}">
        <c:if test="${(not empty projectTabIndex) and (projectTabIndex == 5)}">
            <img src="/i/or/tab_late_deliverables_on.gif" width="119" height="36" border="0" name="tab5"/>
        </c:if>
        <c:if test="${(empty projectTabIndex) or (projectTabIndex != 5)}">
            <a href="<or:url value='/actions/ViewLateDeliverables' />" onmouseover="img_act('tab5')"
                       onmouseout="img_inact('tab5')">
                <img src="/i/or/tab_late_deliverables.gif" width="119" height="36" border="0" name="tab5"/>
            </a>
        </c:if>
    </c:if>
    </div>
    <div style="float: left;">
    <c:if test="${isAllowedToCreateProject}">
        <c:if test="${(not empty projectTabIndex) and (projectTabIndex == 3)}">
            <img src="/i/or/tab_create_project_on.gif" width="119" height="36" border="0" name="tab3" />
        </c:if>
        <c:if test="${(empty projectTabIndex) or (projectTabIndex != 3)}">
            <a href="<or:url value='/actions/NewProject' />" onmouseover="img_act('tab3')" onmouseout="img_inact('tab3')">
                <img src="/i/or/tab_create_project.gif" width="119" height="36" border="0" name="tab3" />
            </a>
        </c:if>
    </c:if>
    </div>
</div>
