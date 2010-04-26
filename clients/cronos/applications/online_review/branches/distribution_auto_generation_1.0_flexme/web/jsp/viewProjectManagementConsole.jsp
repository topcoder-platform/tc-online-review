<%--
  - Author: isv, TCSDEVELOPER
  - Version: 1.0 (Online Review Project Management Console assembly v1.0)
  - Version: 1.1 (Distribution Auto Generation assembly v1.0)
  - Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
  -
  - Description: This page provides a web form for Project Managemen Console. Such a form includes areas for extending
  - Registration phase, extending Submission phase and adding new resources to target project, create design distribution, upload design and development distribution.
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
<c:set var="tabName" value="${requestScope.tab}"/>
<c:set var="projectCategory" value="${project.projectCategory.name}"/>
<c:set var="showDistTab" value="${project.projectCategory.projectType.name == 'Component' && (projectCategory == 'Design' || projectCategory == 'Development')}" />

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
        <style text="text/css">
            form {padding:0;margin:0;}
        </style>
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
                    <div id="contentTitle">
                         <h3>${project.allProperties["Project Name"]}
                            version ${project.allProperties["Project Version"]} - Manage Project</h3>
                    </div>
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
                    <div id="sc1" style='display:<c:choose><c:when test="${tabName == 'generate'}">none</c:when><c:otherwise>block</c:otherwise></c:choose>;'>
                        <ul id="tablist">
                            <li id='current'><a href="javascript:void(0)">Timeline/Resources</a></li>
                            <c:if test="${showDistTab}">
                            <li><a href="javascript:void(0)" onclick="return activateTab('sc2', this)">Distributions</a></li>
                            </c:if>
                        </ul>
                        <div style="clear:both;"></div>
                        <html:form action="/actions/ManageProject">
                            <html:hidden property="method" value="manageProject"/>
                            <html:hidden property="pid" value="${project.id}"/>

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
                    <c:if test="${showDistTab}">
                    <div id="sc2" style='display:<c:choose><c:when test="${tabName == 'generate'}">block</c:when><c:otherwise>none</c:otherwise></c:choose>;'>
                        <ul id="tablist">
                            <li><a href="javascript:void(0)" onclick="return activateTab('sc1', this)">Timeline/Resources</a></li>
                            <li id='current'><a href="javascript:void(0)">Distributions</a></li>
                        </ul>
                        <div style="clear:both;"></div>
                        <table class="scorecard" id="reg_phase_tbl" cellpadding="0" width="100%" style="border-collapse: collapse;">
                            <tr><td class="title"><bean:message key="manageProject.Distribution.title1"/></td></tr>
                            <tr><td style="margin:0;padding:0;">
                                <html:form action="/actions/CreateDesignDistribution" method="POST" enctype="multipart/form-data">
                                    <html:hidden property="method" value="createDesignDistribution"/>
                                    <html:hidden property="pid" value="${project.id}"/>
                                    <table cellpadding="0" width="100%" style="border-collapse: collapse;">
                                        <tr class="light">
                                            <td class="value" width="30%">
                                                <bean:message key="manageProject.Distribution.packagename"/>
                                            </td>
                                            <td class="value">
                                                <html:text property="distribution_package_name"/>
                                                <div class="error">
                                                    <html:errors property="distribution_package_name" prefix="" suffix=""/>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr class="dark">
                                            <td class="value">
                                                <bean:message key="manageProject.Distribution.rs"/>
                                            </td>
                                            <td class="value">
                                                <html:file property="distribution_rs" size="20" style="vertical-align:middle;" />
                                                <div class="error">
                                                    <html:errors property="distribution_rs" prefix="" suffix=""/>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr class="light">
                                            <td class="value">
                                                <bean:message key="manageProject.Distribution.supportingdocument"/> 1    
                                            </td>
                                            <td class="value">
                                                <html:file property="distribution_document1" size="20" style="vertical-align:middle;" />
                                            </td>
                                        </tr>
                                        <tr class="dark">
                                            <td class="value">
                                                <bean:message key="manageProject.Distribution.supportingdocument"/> 2    
                                            </td>
                                            <td class="value">
                                                <html:file property="distribution_document2" size="20" style="vertical-align:middle;" />
                                            </td>
                                        </tr>
                                        <tr class="light">
                                            <td class="value">
                                                <bean:message key="manageProject.Distribution.supportingdocument"/> 3
                                            </td>
                                            <td class="value">
                                                <html:file property="distribution_document3" size="20" style="vertical-align:middle;" />
                                            </td>
                                        </tr>
                                        <tr class="dark">
                                            <td class="value">
                                                <bean:message key="manageProject.Distribution.uploadtoserver"/>
                                            </td>
                                            <td class="value">
                                                <input type="checkbox" value="true" name="distribution_upload" <c:if test="${distribution_upload}">checked</c:if> />
                                            </td>
                                        </tr>
                                        <tr class="light">
                                            <td class="value">
                                                <bean:message key="manageProject.Distribution.returntouser"/>
                                            </td>
                                            <td class="value">
                                                <input type="checkbox" value="true" name="distribution_return" <c:if test="${distribution_return}">checked</c:if> />
                                                <div class="error">
                                                    <html:errors property="distribution_return" prefix="" suffix=""/>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr class="dark">
                                            <td class="value" colspan="2">
                                                <html:image srcKey="btnGenerate.img" altKey="btnGenerate.alt" border="0"/>
                                            </td>
                                        </tr>
                                    </table>
                                </html:form>
                            </td></tr>
                            <c:if test="${projectCategory == 'Design'}">
                            <tr><td class="title"><bean:message key="manageProject.Distribution.title2"/></td></tr>
                            <tr><td style="margin:0;padding:0;">
                                <html:form action="/actions/UploadDesignDistribution" method="POST" enctype="multipart/form-data">
                                    <html:hidden property="method" value="uploadDesignDistribution"/>
                                    <html:hidden property="pid" value="${project.id}"/>
                                    <table cellpadding="0" width="100%" style="border-collapse: collapse;">
                                        <tr class="light">
                                            <td class="value" width="30%">
                                                <bean:message key="manageProject.Distribution.designdist"/>
                                            </td>
                                            <td class="value">
                                                <html:file property="file" size="20" style="vertical-align:middle;" />
                                                <div class="error">
                                                    <html:errors property="file" prefix="" suffix=""/>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr class="dark">
                                            <td class="value" colspan="2">
                                                <html:image srcKey="btnUpload.img" altKey="btnUpload.alt" border="0"/>
                                            </td>
                                        </tr>
                                    </table>
                                </html:form>
                            </td></tr>
                            </c:if>
                            <c:if test="${projectCategory == 'Development'}">
                            <tr><td class="title"><bean:message key="manageProject.Distribution.title3"/></td></tr>
                            <tr><td style="margin:0;padding:0;">
                                <html:form action="/actions/UploadDevelopmentDistribution" method="POST" enctype="multipart/form-data">
                                    <html:hidden property="method" value="uploadDevelopmentDistribution"/>
                                    <html:hidden property="pid" value="${project.id}"/>
                                    <table cellpadding="0" width="100%" style="border-collapse: collapse;">
                                        <tr class="light">
                                            <td class="value" width="30%">
                                                <bean:message key="manageProject.Distribution.developmentdist"/>
                                            </td>
                                            <td class="value">
                                                <html:file property="file" size="20" style="vertical-align:middle;" />
                                                <html:errors property="file" prefix="" suffix=""/>
                                            </td>
                                        </tr>
                                        <tr class="dark">
                                            <td class="value" colspan="2">
                                                <html:image srcKey="btnUpload.img" altKey="btnUpload.alt" border="0"/>
                                            </td>
                                        </tr>
                                    </table>
                                </html:form>
                            </td></tr>
                            </c:if>
                        </table>
                    </div>
                    </c:if>
                </div>
                <!-- //tabconentcontainer -->
            </div>
            <!-- //mainMiddleContent -->
            <jsp:include page="/includes/inc_footer.jsp"/>
        </div>
        <!-- //maxWidthBody -->
    </div>
    
<script language="JavaScript" type="text/javascript">
<!--
    var previousActiveTab = null;
    var firstTab = '<c:choose><c:when test="${tabName == 'generate'}">sc2</c:when><c:otherwise>sc1</c:otherwise></c:choose>';
    /*
     * This function will deactivate the previously active tab (if there was any),
     * and activate the new one.
     */
    function activateTab(tabId, aObject) {
        var tabToActivate = document.getElementById(tabId);
        if (tabToActivate == null) {
            return false;
        }
        // Deactivate the previously active tab
        if (previousActiveTab != null) {
            previousActiveTab.style.display = "none";
        } else {
            document.getElementById(firstTab).style.display = "none";
        }
        // Activate the new one and update the reference to the previously active tab
        tabToActivate.style.display = "block";
        previousActiveTab = tabToActivate;
        // Remove focus from the link that triggered the activation
        if (aObject.blur) {
            aObject.blur();
        }
        return false;
    }
//-->
</script>
    </body>
</html:html>
