<%--
  - Author: isv
  - Version: 1.0 (Online Review Project Management Console assembly v1.0)
  - Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
  -
  - Description: This page provides a web form for Project Managemen Console. Such a form includes areas for extending
  - Registration phase, extending Submission phase and adding new resources to target project.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="html" uri="/tags/struts-html" %>
<%@ taglib prefix="bean" uri="/tags/struts-bean" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>
<c:set var="project" value="${requestScope.project}"/>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html xhtml="true">
    <head>
            <jsp:include page="/includes/project/project_title.jsp"/>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

        <!-- TopCoder CSS -->
        <link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/style.css' />"/>
        <link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/coders.css' />"/>
        <link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/tcStyles.css' />"/>

        <!-- CSS and JS by Petar -->
        <link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/or/new_styles.css' />"/>
        <link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/or/phasetabs.css' />"/>
        <script language="JavaScript" type="text/javascript"
                src="<html:rewrite href='/js/or/rollovers2.js' />"><!-- @ --></script>
        <script language="JavaScript" type="text/javascript"
                src="<html:rewrite href='/js/or/dojo.js' />"><!-- @ --></script>
        <script language="JavaScript" type="text/javascript"
                src="<html:rewrite href='/js/or/util.js' />"><!-- @ --></script>
        <script language="JavaScript" type="text/javascript"
                src="<html:rewrite href='/js/or/validation_util2.js' />"><!-- @ --></script>
        <script language="JavaScript" type="text/javascript"
                src="<html:rewrite href='/js/or/validation_edit_project_links.js' />"><!-- @ --></script>
    </head>
    <body>
    <div align="center">
        <div class="maxWidthBody" align="left">

            <jsp:include page="/includes/inc_header.jsp"/>
            <jsp:include page="/includes/project/project_tabs.jsp"/>

            <div id="mainMiddleContent">
                <div class="clearfix"></div>
                <div id="tabcontentcontainer">
                    <html:form action="/actions/ManageProject">
                        <html:hidden property="method" value="manageProject"/>
                        <html:hidden property="pid"/>

                        <%-- Validation errors area --%>
                        <c:if test="${orfn:isErrorsPresent(pageContext.request)}">
                            <table cellpadding="0" cellspacing="0" border="0">
                                <tr>
                                    <td colspan="2">
                                        <span style="color:red;">
                                            <bean:message key="Error.manageProject.ValidationFailed"/>
                                        </span>
                                    </td>
                                </tr>
                                <html:errors property="org.apache.struts.action.GLOBAL_MESSAGE"/>
                            </table>
                            <br/>
                        </c:if>

                        <div id="contentTitle">
                            <h3>${project.allProperties["Project Name"]}
                                version ${project.allProperties["Project Version"]} - Manage Project</h3>
                        </div>

                        <div id="tabNewLinks">
                            <%-- Extend Registration Phase area --%>
                            <table class="scorecard" id="reg_phase_tbl" cellpadding="0" width="100%"
                                   style="border-collapse: collapse;">
                                <tr>
                                    <td class="title" colspan="2">
                                        <c:choose>
                                            <c:when test="${requestScope.registrationPhaseClosed}">
                                                <bean:message key="manageProject.RegPhase.title2"/>
                                            </c:when>
                                            <c:otherwise><bean:message key="manageProject.RegPhase.title"/></c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                                <tr class="light">
                                    <td class="value">
                                        <bean:message key="manageProject.RegPhase.deadline"/>
                                    </td>
                                    <td class="value">
                                        <fmt:formatDate value="${requestScope.registrationPhase.scheduledEndDate}"
                                                        pattern="MM.dd.yyyy hh:mm a"/>
                                    </td>
                                </tr>
                                <tr class="dark">
                                    <td class="value">
                                        <bean:message key="manageProject.RegPhase.duration"/>
                                    </td>
                                    <td class="value">
                                        <c:out value="${requestScope.registrationPhaseDuration}"/>
                                    </td>
                                </tr>
                                <tr class="light">
                                    <td class="value">
                                        <bean:message key="manageProject.RegPhase.extension"/>
                                    </td>
                                    <td class="value">
                                        <html:text styleClass=".inputBoxDuration"
                                                   disabled="${not requestScope.allowRegistrationPhaseExtension}" 
                                                   property="registration_phase_extension"/>
                                        <div class="error">
                                            <html:errors property="registration_phase_extension" prefix="" suffix=""/>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="lastRowTD" colspan="2"><!-- @ --></td>
                                </tr>
                            </table>
                                <%-- Extend Submission Phase area --%>
                                <table class="scorecard" id="submission_phase_tbl" cellpadding="0" width="100%"
                                       style="border-collapse: collapse;">
                                    <tr>
                                        <td class="title" colspan="2">
                                            <bean:message key="manageProject.SubmissionPhase.title"/>
                                        </td>
                                    </tr>
                                    <tr class="light">
                                        <td class="value">
                                            <bean:message key="manageProject.SubmissionPhase.deadline"/>
                                        </td>
                                        <td class="value">
                                            <fmt:formatDate value="${requestScope.submissionPhase.scheduledEndDate}"
                                                            pattern="MM.dd.yyyy hh:mm a"/>
                                        </td>
                                    </tr>
                                    <tr class="dark">
                                        <td class="value">
                                            <bean:message key="manageProject.SubmissionPhase.duration"/>
                                        </td>
                                        <td class="value">
                                            <c:out value="${requestScope.submissionPhaseDuration}"/>
                                        </td>
                                    </tr>
                                    <tr class="light">
                                        <td class="value">
                                            <bean:message key="manageProject.SubmissionPhase.extension"/>
                                        </td>
                                        <td class="value">
                                            <html:text styleClass=".inputBoxDuration"
                                                       disabled="${not requestScope.allowSubmissionPhaseExtension}"
                                                       property="submission_phase_extension"/>
                                            <div class="error">
                                                <html:errors property="submission_phase_extension" prefix=""
                                                             suffix=""/>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="lastRowTD" colspan="2"><!-- @ --></td>
                                    </tr>
                                </table>

                            <%-- Add Resources area --%>
                            <table class="scorecard" id="resources_tbl" cellpadding="0" width="100%"
                                   style="border-collapse: collapse;">
                                <tr>
                                    <td class="title" colspan="5"><bean:message key="manageProject.Resources.title" /></td>
                                </tr>
                                <tr>
                                    <td class="header"><bean:message key="manageProject.Resources.Role"/></td>
                                    <td class="header"><bean:message key="manageProject.Resources.Handles"/></td>
                                </tr>

                                <c:forEach items="${requestScope.availableRoles}" var="role" varStatus="index">
                                    <tr class="${(index.index % 2 == 0) ? 'light' : 'dark'}">
                                        <td class="value">
                                            <html:hidden property="resource_role_id[${index.index}]" value="${role.id}"/>
                                            <c:out value="${role.name}"/>
                                            <div class="error">
                                                <html:errors property="resource_role_id[${index.index}]"
                                                             prefix="" suffix=""/>
                                            </div>
                                        </td>
                                        <td class="value">
                                            <html:text styleClass="inputTextBox" property="resource_handles[${index.index}]"
                                                       size=""/>
                                            <div class="error">
                                                <html:errors property="resource_handles[${index.index}]"
                                                             prefix="" suffix=""/>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>

                                <tr>
                                    <td class="lastRowTD" colspan="2"><!-- @ --></td>
                                </tr>
                            </table><br />
                        </div>

                        <div class="bottomButtonBar">
                            <html:image srcKey="btnSaveChanges.img" altKey="btnSaveChanges.alt" border="0"/>&#160;
                            <html:link
                                    page="/actions/ViewProjectDetails.do?method=viewProjectDetails&pid=${project.id}"><html:img
                                    srcKey="btnCancel.img" altKey="btnCancel.alt" border="0"/></html:link>
                        </div>
                    </html:form>
                </div>
                <!-- //tabconentcontainer -->
            </div>
            <!-- //mainMiddleContent -->
            <jsp:include page="/includes/inc_footer.jsp"/>
        </div>
        <!-- //maxWidthBody -->
    </div>
    </body>
</html:html>
