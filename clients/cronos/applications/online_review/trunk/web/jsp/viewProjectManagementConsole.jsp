<%--
  - Author: isv, romanoTC, flexme
  - Version: 1.0 (Online Review Project Management Console assembly v1.0)
  - Version: 1.1 (Distribution Auto Generation Assembly v1.0) Change notes: Added support for managing design and 
    development distributions.
  - Version: 1.2 (Review Feedback Integration Assembly v1.0) Change notes: Added Review Performance tab to display and
  - manage review feedbacks.
  - Version: 1.3 (Online Review - Project Payments Integration Part 1 v1.0 ) Change notes:
  - Added Review Payments tab to display and management review payments.
  - Version: 1.4 (Module Assembly - Enhanced Review Feedback Integration) Change notes:
  - Updated review feedback section to adopt for the new review feedback management component.
  - Update review feedback section to support for editing existing review feedback.
  - Copyright (C) 2010-2013 TopCoder Inc., All Rights Reserved.
  -
  - Description: This page provides a web form for Project Management Console. Such a form includes areas for extending
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
<%@ taglib prefix="tc-webtag" uri="/tags/tc-webtags" %>
<c:set var="project" value="${requestScope.project}"/>
<fmt:setLocale value="en_US"/>

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
                src="<html:rewrite href='/js/or/validation_util.js' />"><!-- @ --></script>
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
                
                <div id="titlecontainer">
	                <div id="contentTitle">
	                    <h3>${project.allProperties["Project Name"]}
	                        version ${project.allProperties["Project Version"]} - Manage Project</h3>
	                </div>
                </div>
                                
                <div id="tabcontentcontainer">
                    <div id="sc1" style='display:${((empty param.activeTabIdx) || (param.activeTabIdx == 1)) ? "block" : "none"};'>
                        <ul id="tablist">
                            <li id='current'><a href="javascript:void(0)"
                                onClick="return activateTab('sc1', this)"><bean:message key="manageProject.TimelineResources.title"/></a></li>
                            <c:if test="${((project.projectCategory.id == 1) || (project.projectCategory.id == 2))}"> <%-- Only show the tab for design and development --%>
                            <li><a href="javascript:void(0)"
                                    onClick="return activateTab('sc2', this)"><bean:message key="manageProject.Distributions.title"/></a></li>
                            </c:if>
                            <c:if test="${reviewFeedbacksExist || reviewFeedbackAllowed}">
                                <li><a href="javascript:void(0)" onClick="return activateTab('sc3', this)">
                                    <bean:message key="manageProject.ReviewPerformance.title"/></a></li>
                            </c:if>
                            <li><a href="javascript:void(0)" onclick="return activateTab('sc4', this)">
                                <bean:message key="manageProject.ReviewPayments.title"/></a></li>
                            </a></li>
                        </ul>
                        <div style="clear:both;"></div>
                        <table class="scorecard" cellpadding="0" width="100%" style="border-collapse: collapse;">
                        <tr class="light">
                        <td>
                            <c:if test="${((empty param.activeTabIdx) || (param.activeTabIdx == 1))}">
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
                            </c:if>
                                
                            <html:form action="/actions/ManageProject">
		                        <html:hidden property="method" value="manageProject"/>
		                        <html:hidden property="pid" value="${project.id}" />
		
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
		                                                        pattern="MM.dd.yyyy HH:mm z"/>
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
		                                                            pattern="MM.dd.yyyy HH:mm z"/>
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
		
		                        <div align="right">
		                            <html:image property="savePMCChangesBtn" srcKey="btnSaveChanges.img" altKey="btnSaveChanges.alt" border="0"/>&#160;
		                            <html:link
		                                    page="/actions/ViewProjectDetails.do?method=viewProjectDetails&pid=${project.id}"><html:img
		                                    srcKey="btnCancel.img" altKey="btnCancel.alt" border="0"/></html:link>
                                            &nbsp;
		                        </div>
		                    </html:form>
                        </td>
                        </tr>
                        <tr>
                            <td class="lastRowTD"><!-- @ --></td>
                        </tr>
                        </table>
                    </div>
                    <%-- Design or Development only --%> 
                    <c:if test="${((project.projectCategory.id == 1) || (project.projectCategory.id == 2))}">
	                    <div id="sc2" style='display:${(param.activeTabIdx == 2) ? "block" : "none"};'>
	                        <ul id="tablist">
	                            <li><a href="javascript:void(0)"
	                                onClick="return activateTab('sc1', this)"><bean:message key="manageProject.TimelineResources.title"/></a></li>
	                            <li id='current'><a href="javascript:void(0)"
	                                    onClick="return activateTab('sc2', this)"><bean:message key="manageProject.Distributions.title"/></a></li>
                                <c:if test="${reviewFeedbacksExist || reviewFeedbackAllowed}">
                                    <li><a href="javascript:void(0)" onClick="return activateTab('sc3', this)">
                                        <bean:message key="manageProject.ReviewPerformance.title"/></a></li>
                                </c:if>
                                <li><a href="javascript:void(0)" onclick="return activateTab('sc4', this)">
                                    <bean:message key="manageProject.ReviewPayments.title"/></a></li>
                                </a></li>
                            </ul>
	                        <div style="clear:both;"></div>
	                        <table class="scorecard" cellpadding="0" width="100%" style="border-collapse: collapse;">
	                        <tr class="light">
	                        <td>
	                           <c:if test="${param.activeTabIdx == 2}">
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
	                            </c:if>
	                        
	                            <%-- Create Design Distribution --%>
	                            <html:form action="/actions/UploadDistribution?activeTabIdx=2" method="POST" enctype="multipart/form-data">
		                        <html:hidden property="method" value="manageDistribution" />
		                        <html:hidden property="postBack" value="y" />
		                        <html:hidden property="pid" value="${project.id}" />
		                        
		                        <table class="scorecard" id="distribution_tbl" cellpadding="0" width="100%"
		                               style="border-collapse: collapse;">
		                            <tr>
		                                <td class="title" colspan="2">
		                                    <bean:message key="manageProject.Distributions.Create.Design.title"/>
		                                </td>
		                            </tr>
		                            <tr class="light">
		                                <td class="value" width="25%">
		                                    <bean:message key="manageProject.Distributions.packagename"/>&nbsp;
                                               <c:if test="${needsPackageName == true}">
                                                <bean:message key="global.required.paren"/>
                                            </c:if>   
		                                </td>
		                                <td class="value" width="75%">
		                                    <html:text styleClass="inputBox"
		                                               property="distribution_package_name"/>
	                                        <div class="error">
	                                            <html:errors property="distribution_package_name" prefix=""
	                                                         suffix=""/>
	                                        </div>
	                                    </td>
		                            </tr>
		                            <tr class="dark">
		                                <td class="value" width="25%">
		                                    <bean:message key="manageProject.Distributions.rs"/>&nbsp;
	                                        <bean:message key="global.required.paren"/>
		                                </td>
		                                <td class="value" width="75%">
		                                    <html:file property="distribution_rs" size="20" styleClass="inputBox" style="width:250px;vertical-align:middle;" />
	                                        <div class="error">
	                                            <html:errors property="distribution_rs" prefix=""
	                                                         suffix=""/>
	                                        </div>
		                                </td>
		                            </tr>
	                                <tr class="light">
	                                    <td class="value" width="25%">
	                                        <bean:message key="manageProject.Distributions.additional1"/>
	                                    </td>
	                                    <td class="value" width="75%">
	                                        <table border="0" cellpadding="0" cellspacing="0">
	                                        <tr>
	                                        <td id="additionalFile1">
                                               <html:file property="distribution_additional1" size="20" styleClass="inputBox" style="width:250px;vertical-align:middle;" />
                                                <div class="error">
                                                    <html:errors property="distribution_additional1" prefix=""
                                                                 suffix=""/>
                                                </div>
	                                        </td>
	                                        <td>  
                                                   <html:img srcKey="btnClear.img" altKey="btnClear.alt" onclick="javascript:clearFileInputField('additionalFile1')" />
                                               </td>
                                               </tr>
                                               </table>
	                                    </td>
	                                </tr>
	                                <tr class="dark">
	                                    <td class="value" width="25%">
	                                        <bean:message key="manageProject.Distributions.additional2"/>
	                                    </td>
	                                    <td class="value" width="75%">
                                               <table border="0" cellpadding="0" cellspacing="0">
                                               <tr>
                                               <td id="additionalFile2">
                                                  <html:file property="distribution_additional2" size="20" styleClass="inputBox" style="width:250px;vertical-align:middle;" />
                                                   <div class="error">
                                                       <html:errors property="distribution_additional2" prefix=""
                                                                    suffix=""/>
                                                   </div>
                                               </td>
                                               <td>  
                                                   <html:img srcKey="btnClear.img" altKey="btnClear.alt" onclick="javascript:clearFileInputField('additionalFile2')" />
                                               </td>
                                               </tr>
                                               </table>
	                                    </td>
	                                </tr>
	                                <tr class="light">
	                                    <td class="value" width="25%">
	                                        <bean:message key="manageProject.Distributions.additional3"/>
	                                    </td>
	                                    <td class="value" width="75%">
                                               <table border="0" cellpadding="0" cellspacing="0">
                                               <tr>
                                               <td id="additionalFile3">
                                                  <html:file property="distribution_additional3" size="20" styleClass="inputBox" style="width:250px;vertical-align:middle;" />
                                                   <div class="error">
                                                       <html:errors property="distribution_additional3" prefix=""
                                                                    suffix=""/>
                                                   </div>
                                               </td>
                                               <td>  
                                                   <html:img srcKey="btnClear.img" altKey="btnClear.alt" onclick="javascript:clearFileInputField('additionalFile3')" />
                                               </td>
                                               </tr>
                                               </table>
	                                    </td>
	                                </tr>                                                                	                            
	                                <tr class="dark">
	                                    <td class="value" colspan="2">
	                                        <c:choose>
		                                        <c:when test="${(project.projectCategory.id == 2)}">
		                                           <input type="checkbox" name="upload_to_server" id="uploadToServer" disabled="disabled" />
		                                        </c:when>
	                                            <c:otherwise>
	                                               <html:checkbox property="upload_to_server" styleId="uploadToServer" />
	                                            </c:otherwise>
                                            </c:choose>
                                            <html:hidden property="upload_to_server" value="false" />
	                                        <label for="uploadToServer"><bean:message key="manageProject.Distributions.Upload" /></label>
	                                        <div class="error">
	                                            <html:errors property="upload_to_server" prefix=""
	                                                         suffix=""/>
	                                        </div>
	                                    </td>
	                                </tr>
	                                <tr class="light">
	                                    <td class="value" colspan="2">
	                                        <html:checkbox property="return_distribution" styleId="returnDistribution" /> 
                                            <html:hidden property="return_distribution" value="false" />
	                                        <label for="returnDistribution"><bean:message key="manageProject.Distributions.Return_distribution" /></label>
	                                        <div class="error">
	                                            <html:errors property="return_distribution" prefix=""
	                                                         suffix=""/>
	                                        </div>
	                                    </td>
	                                </tr>
	                                <tr class="dark">
	                                    <td class="value" colspan="2">
	                                        <html:image property="generateBtn" srcKey="btnGenerate.img" altKey="btnGenerate.alt" border="0"/>&#160;
	                                    </td>
	                                </tr>
	                                <tr>
	                                    <td class="lastRowTD" colspan="2"><!-- @ --></td>
	                                </tr>
		                        </table>
		                        </html:form>
		                        
		                        <%-- Upload Distribution --%>
	                            <html:form action="/actions/UploadDistribution?activeTabIdx=2" method="POST" enctype="multipart/form-data">
	                            <html:hidden property="method" value="uploadDistribution" />
	                            <html:hidden property="postBack" value="y" />
	                            <html:hidden property="pid" value="${project.id}" />
	                            <table class="scorecard" id="upload_distribution_tbl" cellpadding="0" width="100%"
	                                   style="border-collapse: collapse;">
	                                <tr>
	                                    <td class="title" colspan="2">
	                                        <c:choose>
	                                            <c:when test="${(project.projectCategory.id == 1)}">
	                                                <bean:message key="manageProject.Distributions.Upload.Design.title"/>
	                                            </c:when>
	                                            <c:otherwise>
	                                                <bean:message key="manageProject.Distributions.Upload.Development.title"/>
	                                            </c:otherwise>                                                
	                                        </c:choose>
	                                    </td>
	                                </tr>
	                                <tr class="light">
	                                    <td class="value" width="25%">
	                                        <bean:message key="manageProject.Distributions.File"/>
	                                    </td>
	                                    <td class="value" width="75%">
	                                        <html:file property="distribution_file" size="20" styleClass="inputBox" style="width:250px;vertical-align:middle;" />
	                                        <div class="error">
	                                            <html:errors property="distribution_file" prefix=""
	                                                         suffix=""/>
	                                        </div>
	                                        <html:image property="uploadDistBtn" srcKey="btnUpload.img" altKey="btnUpload.alt" border="0"/>&#160;
	                                    </td>
	                                </tr>
	                                <tr>
	                                    <td class="lastRowTD" colspan="2"><!-- @ --></td>
	                                </tr>
	                            </table><br/>
                                <div align="right">
                                   <html:link
                                           page="/actions/ViewProjectDetails.do?method=viewProjectDetails&pid=${project.id}"><html:img
                                           srcKey="btnReturnToProjDet.img" altKey="btnReturnToProjDet.alt" border="0"/></html:link>
                                   &nbsp;
                                </div>
	                            </html:form>
	                        </td>
	                        </tr>
	                        <tr>
	                            <td class="lastRowTD"><!-- @ --></td>
	                        </tr>
	                        </table>
	                    </div>
                    </c:if> <%-- // Design or Development only --%>
                    <%-- Review Feedback tab --%>
                <c:if test="${not empty feedback || reviewFeedbackAllowed}">
                    <div id="sc3" style='display:${(param.activeTabIdx == 3) ? "block" : "none"};'>
                    <ul id="tablist">
                        <li><a href="javascript:void(0)"
                               onClick="return activateTab('sc1', this)"><bean:message
                                key="manageProject.TimelineResources.title"/></a></li>
                        <c:if test="${((project.projectCategory.id == 1) || (project.projectCategory.id == 2))}"> 
                        <%-- Only show the tab for design and development --%>
                            <li><a href="javascript:void(0)"
                                   onClick="return activateTab('sc2', this)"><bean:message
                                    key="manageProject.Distributions.title"/></a></li>
                        </c:if>
                        <li id='current'><a href="javascript:void(0)" onClick="return activateTab('sc3', this)">
                            <bean:message key="manageProject.ReviewPerformance.title"/></a></li>
                        <li><a href="javascript:void(0)" onclick="return activateTab('sc4', this)">
                            <bean:message key="manageProject.ReviewPayments.title"/></a></li>
                        </a></li>
                    </ul>
                    <div style="clear:both;"></div>
                    <table class="scorecard" cellpadding="0" width="100%" style="border-collapse: collapse;">
                    <tr class="light">
                    <td>
                    
                        <c:choose>
                            <c:when test="${not toEdit and not empty feedback}">
                                <%-- If any feedback already exist then just display the existing feedback --%>
                                <table class="scorecard" cellpadding="0" width="100%" style="border-collapse: collapse;">
                                    <tr>
                                        <td class="title" colspan="5">
                                            <bean:message key="manageProject.ReviewPerformance.Feedback.From.title"/>
                                            <tc-webtag:handle coderId="${feedback.createUser}"/>
                                            <bean:message key="manageProject.ReviewPerformance.Feedback.From.at"/>
                                            <fmt:formatDate value="${feedback.createDate}" pattern="MM.dd.yyyy HH:mm zzz"/><c:if test="${not empty feedback.modifyUser and (feedback.modifyUser ne feedback.createUser or feedback.modifyDate ne feedback.createDate)}">,
                                                <bean:message key="manageProject.ReviewPerformance.Feedback.UpdateBy.title"/>
                                                <tc-webtag:handle coderId="${feedback.modifyUser}"/>
                                                <bean:message key="manageProject.ReviewPerformance.Feedback.UpdateBy.at"/>
                                                <fmt:formatDate value="${feedback.modifyDate}" pattern="MM.dd.yyyy HH:mm zzz"/>
                                            </c:if>
                                        </td>
                                    </tr>
                                    <c:if test="${empty feedback.comment}">
                                        <tr>
                                            <td class="value"><b><bean:message key="manageProject.ReviewPerformance.Reviewer.title"/></b></td>
                                            <td class="valueC"><b><bean:message key="manageProject.ReviewPerformance.Score.Bad"/></b></td>
                                            <td class="valueC"><b><bean:message key="manageProject.ReviewPerformance.Score.Average"/></b></td>
                                            <td class="valueC"><b><bean:message key="manageProject.ReviewPerformance.Score.Good"/></b></td>
                                            <td class="valueC" width="65%"><b><bean:message key="manageProject.ReviewPerformance.Feedback.title"/></b></td>
                                        </tr>
                                        <c:forEach items="${feedback.details}" var="detail" varStatus="loop">
                                            <tr class="${loop.index mod 2 eq 0 ? 'dark' : 'light'}">
                                                <td class="value">
                                                    <tc-webtag:handle coderId="${detail.reviewerUserId}"/>
                                                </td>
                                                <td class="valueC">
                                                    <input type="radio" disabled="disabled"
                                                           <c:if test="${detail.score eq 0}">checked="checked"</c:if>/>
                                                </td>
                                                <td class="valueC">
                                                    <input type="radio" disabled="disabled"
                                                           <c:if test="${detail.score eq 1}">checked="checked"</c:if>/>
                                                </td>
                                                <td class="valueC">
                                                    <input type="radio" disabled="disabled"
                                                           <c:if test="${detail.score eq 2}">checked="checked"</c:if>/>
                                                </td>
                                                <td class="value">
                                                        ${orfn:htmlEncode(detail.feedbackText)}
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:if>
                                    <c:if test="${not empty feedback.comment}">
                                        <tr class="dark">
                                            <td class="value" colspan="5">
                                                <strong><bean:message key="manageProject.ReviewPerformance.Feedback.NotAvailable"/></strong>
                                            </td>
                                        </tr>
                                        <tr class="light">
                                            <td class="value" colspan="5">${orfn:htmlEncode(feedback.comment)}</td>
                                        </tr>
                                    </c:if>
                                </table>

                                <br/>

                                <div align="right">
                                    <html:link
                                            page="/actions/EditReviewFeedback.do?method=editReviewFeedback&activeTabIdx=3&pid=${project.id}"><html:img
                                            srcKey="manageProject.ReviewPerformance.btnEdit.img" altKey="manageProject.ReviewPerformance.btnEdit.alt" border="0"/></html:link>
                                    &nbsp;
                                    <html:link
                                            page="/actions/ViewProjectDetails.do?method=viewProjectDetails&pid=${project.id}"><html:img
                                            srcKey="btnReturnToProjDet.img" altKey="btnReturnToProjDet.alt" border="0"/></html:link>&nbsp;&nbsp;
                                    <br/>
                                    <br/>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <%-- Otherwise since there are no feedbacks yet then display the form for submitting 
                                feedbacks --%>
                                <c:if test="${param.activeTabIdx == 3}">
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
                                </c:if>
                                <c:set var="unavailable" value="${not toEdit or not empty feedback.comment}"/>
                                <%-- Save Review Feedback form  --%>
                                <html:form action="/actions/SaveReviewFeedback?activeTabIdx=3" method="POST">
                                    <html:hidden property="method" value="manageReviewFeedback"/>
                                    <html:hidden property="postBack" value="y"/>
                                    <html:hidden property="pid" value="${project.id}"/>

                                    <table class="scorecard" cellpadding="0" width="100%" style="border-collapse: collapse;">
                                        <tr>
                                            <td class="title" colspan="5">
                                                <bean:message key="manageProject.ReviewPerformance.Review.Performance.title"/>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td class="value" colspan="5">
                                                <bean:message key="manageProject.ReviewPerformance.Review.Performance.hint"/>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td class="value"><b><bean:message
                                                    key="manageProject.ReviewPerformance.Reviewer.title"/></b></td>
                                            <td class="valueC"><b><bean:message key="manageProject.ReviewPerformance.Score.Bad"/></b></td>
                                            <td class="valueC"><b><bean:message key="manageProject.ReviewPerformance.Score.Average"/></b></td>
                                            <td class="valueC"><b><bean:message key="manageProject.ReviewPerformance.Score.Good"/></b></td>
                                            <td class="valueC" width="65%"><b><bean:message key="manageProject.ReviewPerformance.Feedback.title"/></b></td>
                                        </tr>
                                        <c:forEach items="${reviewerResourcesMap}" var="reviewerEntry" varStatus="loop">
                                            <c:set var="reviewerUserId" value="${reviewerEntry.key}"/>
                                            <c:set var="reviewerResource" value="${reviewerEntry.value}"/>
                                            <c:set var="resourceIdx" value="${loop.index}"/>
                                            <tr class="${(loop.index % 2 == 0) ? 'dark' : 'light'}">
                                                <td class="value">
                                                    <tc-webtag:handle coderId="${reviewerUserId}"/>
                                                    <html:hidden property="reviewerUserId[${resourceIdx}]" value="${reviewerUserId}"/>
                                                    <div class="error">
                                                        <html:errors property="reviewerScore[${resourceIdx}]"
                                                                     prefix="" suffix=""/>
                                                    </div>
                                                </td>
                                                <td class="valueC">
                                                    <html:radio property="reviewerScore[${resourceIdx}]" value="0" disabled="${unavailable}" />
                                                </td>
                                                <td class="valueC">
                                                    <html:radio property="reviewerScore[${resourceIdx}]" value="1" disabled="${unavailable}" />
                                                </td>
                                                <td class="valueC">
                                                    <html:radio property="reviewerScore[${resourceIdx}]" value="2" disabled="${unavailable}" />
                                                </td>
                                                <td class="value ffednack-text-wrapper">
                                                    <html:textarea property="reviewerFeedback[${resourceIdx}]" rows="3" 
                                                                   styleClass="feedback-text" disabled="${unavailable}" />
                                                    <div class="error">
                                                        <html:errors property="reviewerFeedback[${resourceIdx}]" 
                                                                     prefix="" suffix=""/>
                                                    </div>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                        <tr class="${(fn:length(reviewerResourcesMap) % 2 == 0) ? 'dark' : 'light'}">
                                            <td colspan="5" class="value">
                                                <html:checkbox property="unavailable" styleId="unavailable" onclick="javascript:unAvailableFeedbackCheckboxHandler();"/><bean:message key="manageProject.ReviewPerformance.Feedback.NotAvailable.Checkbox"/><br/>
                                                <html:textarea property="explanation" styleClass="feedback-text" rows="3" disabled="${not unavailable}" />
                                                <div class="error">
                                                    <html:errors property="explanation"
                                                                 prefix="" suffix=""/>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td class="lastRowTD" colspan="5"><!-- @ --></td>
                                        </tr>
                                    </table>
                                    <br/>
                                    <div align="right">
                                        <html:image property="saveChangesBtn"
                                                    srcKey="btnSaveChanges.img" altKey="btnSaveChanges.alt" border="0"/>
                                        &nbsp;
                                        <c:if test="${not toEdit}">
                                            <html:link
                                                    page="/actions/ViewProjectDetails.do?method=viewProjectDetails&pid=${project.id}"><html:img
                                                    srcKey="btnCancel.img" altKey="btnCancel.alt" border="0"/></html:link>&nbsp;&nbsp;
                                        </c:if>
                                        <c:if test="${toEdit}">
                                            <html:link
                                                    page="/actions/ViewManagementConsole.do?method=viewConsole&activeTabIdx=3&pid=${project.id}"><html:img
                                                    srcKey="btnCancel.img" altKey="btnCancel.alt" border="0"/></html:link>&nbsp;&nbsp;
                                        </c:if>
                                        <br/>
                                    </div>
                                </html:form>

                            </c:otherwise>
                        </c:choose>
                    </td>
                    </tr>
                    <tr>
                        <td class="lastRowTD"><!-- @ --></td>
                    </tr>
                    </table>
                    </div>
                    </c:if> <%-- // Review Feedbacks --%>

                    <div id="sc4" style='display:${(param.activeTabIdx == 4) ? "block" : "none"};'>
                        <ul id="tablist">
                            <li><a href="javascript:void(0)"
                                   onClick="return activateTab('sc1', this)"><bean:message
                                    key="manageProject.TimelineResources.title"/></a></li>
                            <c:if test="${((project.projectCategory.id == 1) || (project.projectCategory.id == 2))}">
                                <%-- Only show the tab for design and development --%>
                                <li><a href="javascript:void(0)"
                                       onClick="return activateTab('sc2', this)"><bean:message
                                        key="manageProject.Distributions.title"/></a></li>
                            </c:if>
                            <c:if test="${reviewFeedbacksExist || reviewFeedbackAllowed}">
                                <li><a href="javascript:void(0)" onClick="return activateTab('sc3', this)">
                                    <bean:message key="manageProject.ReviewPerformance.title"/></a></li>
                            </c:if>
                            <li id='current'><a href="javascript:void(0)" onclick="return activateTab('sc4', this)">
                                <bean:message key="manageProject.ReviewPayments.title"/></a></li>
                            </a></li>
                        </ul>

                        <div style="clear:both;"></div>
                        <table class="scorecard" cellpadding="0" width="100%" style="border-collapse: collapse;">
                            <tr class="light">
                                <td>
                                    <c:if test="${param.activeTabIdx == 4}">
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
                                    </c:if>

                                    <html:form action="/actions/SaveReviewPayments?activeTabIdx=4">
                                        <html:hidden property="method" value="manageReviewPayments"/>
                                        <html:hidden property="pid" value="${project.id}" />

                                        <div id="tabNewLinks">
                                            <table class="scorecard" id="review_payments_tbl" cellpadding="0" width="100%"
                                                   style="border-collapse: collapse;">
                                                <tr>
                                                    <td class="title" colspan="4"><bean:message key="manageProject.ReviewPayments.title"/></a></li></td>
                                                </tr>
                                                <tr>
                                                    <td class="title"><bean:message key="manageProject.ReviewPayments.ResourceRole"/></td>
                                                    <td class="title"><bean:message key="manageProject.ReviewPayments.default"/> *</td>
                                                    <td class="title"><bean:message key="manageProject.ReviewPayments.FixedAmount"/></td>
                                                    <td class="title"><bean:message key="manageProject.ReviewPayments.percent"/></td>
                                                </tr>
                                                <c:set var="isSubmitReturn" value="${fn:length(reviewPaymentsForm.map['resources_roles_id']) gt 0}" />
                                                <c:set var="reviewPaymentsLength" value="${fn:length(resourceRoleIds)}" />
                                                <c:forEach begin="0" end="${reviewPaymentsLength - 1}" var="reviewPaymentsIdx" varStatus="vs">
                                                <c:set var="roleId" value="${resourceRoleIds[reviewPaymentsIdx]}" />
                                                <tr <c:if test="${vs.index % 2 eq 0}">class="light"</c:if> <c:if test="${vs.index % 2 eq 1}">class="dark"</c:if> >
                                                    <td class="value" nowrap="nowrap">
                                                            ${resourceRoleNames[reviewPaymentsIdx]}
                                                        <div class="error"><html:errors property="resources_roles_id[${reviewPaymentsIdx}]" prefix="" suffix="" /></div>
                                                        <input type="hidden" name="resources_roles_id[${reviewPaymentsIdx}]" value="${roleId}" />
                                                    </td>
                                                    <td class="value" nowrap="nowrap">
                                                        <c:if test="${isSubmitReturn}">
                                                            <html:radio styleId="resource_payments_radio_default[${reviewPaymentsIdx}]" property="resource_payments_radio[${reviewPaymentsIdx}]" value="default"/>
                                                        </c:if>
                                                        <c:if test="${not isSubmitReturn}">
                                                            <input type="radio" value="default" id="resource_payments_radio_default[${reviewPaymentsIdx}]" name="resource_payments_radio[${reviewPaymentsIdx}]" <c:if test="${reviewPaymentsRadio[reviewPaymentsIdx] eq 'default'}">checked="checked"</c:if> >
                                                        </c:if>
                                                        <label for="resource_payments_radio_default[${reviewPaymentsIdx}]">${"$"}<c:choose><c:when test="${empty defaultPayments[roleId]}">0</c:when><c:otherwise>${orfn:displayPaymentAmt(pageContext.request, defaultPayments[roleId])}</c:otherwise></c:choose></label>
                                                    </td>
                                                    <td class="value" nowrap="nowrap">
                                                        <c:if test="${isSubmitReturn}">
                                                            <html:radio styleId="resource_payments_radio_fixed[${reviewPaymentsIdx}]" property="resource_payments_radio[${reviewPaymentsIdx}]" value="fixed"/>
                                                        </c:if>
                                                        <c:if test="${not isSubmitReturn}">
                                                            <input type="radio" value="fixed" id="resource_payments_radio_fixed[${reviewPaymentsIdx}]" name="resource_payments_radio[${reviewPaymentsIdx}]" <c:if test="${reviewPaymentsRadio[reviewPaymentsIdx] eq 'fixed'}">checked="checked"</c:if> >
                                                        </c:if>
                                                        <label for="resource_payments_radio_fixed[${reviewPaymentsIdx}]">${"$"}</label>
                                                        <c:if test="${isSubmitReturn}">
                                                            <html:text property="resource_payments_fixed_amount[${reviewPaymentsIdx}]" styleClass="inputBoxDuration"/>
                                                        </c:if>
                                                        <c:if test="${not isSubmitReturn}">
                                                            <input type="text" name="resource_payments_fixed_amount[${reviewPaymentsIdx}]" class="inputBoxDuration" value="${reviewPaymentsFixed[reviewPaymentsIdx]}" />
                                                        </c:if>
                                                        <div class="error"><html:errors property="resource_payments_fixed_amount[${reviewPaymentsIdx}]" prefix="" suffix="" /></div>
                                                    </td>
                                                    <td class="value" nowrap="nowrap">
                                                        <c:if test="${isSubmitReturn}">
                                                            <html:radio styleId="resource_payments_radio_percentage[${reviewPaymentsIdx}]" property="resource_payments_radio[${reviewPaymentsIdx}]" value="percentage"/>
                                                            <html:text property="resource_payments_percent_amount[${reviewPaymentsIdx}]" styleClass="inputBoxDuration"/>
                                                        </c:if>
                                                        <c:if test="${not isSubmitReturn}">
                                                            <input type="radio" value="percentage" id="resource_payments_radio_percentage[${reviewPaymentsIdx}]" name="resource_payments_radio[${reviewPaymentsIdx}]" <c:if test="${reviewPaymentsRadio[reviewPaymentsIdx] eq 'percentage'}">checked="checked"</c:if> >
                                                            <input type="text" name="resource_payments_percent_amount[${reviewPaymentsIdx}]" class="inputBoxDuration" value="${reviewPaymentsPercentage[reviewPaymentsIdx]}" />
                                                        </c:if>
                                                        <label for="resource_payments_radio_percentage[${reviewPaymentsIdx}]">%</label>
                                                        <div class="error"><html:errors property="resource_payments_percent_amount[${reviewPaymentsIdx}]" prefix="" suffix="" /></div>
                                                    </td>
                                                    </c:forEach>
                                                <tr>
                                                    <td class="lastRowTD" colspan="4"><!-- @ --></td>
                                                </tr>
                                            </table>
                                        </div>
                                        <br/>
                                        <div><bean:message key="manageProject.ReviewPayments.desc"/></div>
                                        <div align="right">
                                            <html:image property="saveChangesBtn" srcKey="btnSaveChanges.img" altKey="btnSaveChanges.alt" border="0"/>&#160;
                                            <html:link
                                                    page="/actions/ViewProjectDetails.do?method=viewProjectDetails&pid=${project.id}"><html:img
                                                    srcKey="btnCancel.img" altKey="btnCancel.alt" border="0"/></html:link>
                                            &nbsp;
                                        </div>
                                    </html:form>
                                </td>
                            </tr>
                            <tr>
                                <td class="lastRowTD"><!-- @ --></td>
                            </tr>
                        </table>
                    </div>
                </div>
                <!-- //tabconentcontainer -->
            </div>
            <!-- //mainMiddleContent -->
            <jsp:include page="/includes/inc_footer.jsp"/>
        </div>
        <!-- //maxWidthBody -->
    </div>
    </body>
    
<c:if test="${sessionScope.success_upload != null}">
<script language="JavaScript" type="text/javascript">
    alert('<%=session.getAttribute("success_upload")%>');
</script>
<%session.removeAttribute("success_upload");%>
</c:if>
    
<script language="JavaScript" type="text/javascript">
<!--
	// A reference to the previously active tab
	<c:choose>
		<c:when test="${(not empty param.activeTabIdx) && (param.activeTabIdx != -1)}">
		var previousActiveTab = document.getElementById("sc${param.activeTabIdx}");
		</c:when>
		<c:otherwise>
	    var previousActiveTab = document.getElementById("sc1");
	    </c:otherwise>
	</c:choose>
	
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

    /**
     * Function used to clear a file input field.
     */
    function clearFileInputField(tagId) { 
        document.getElementById(tagId).innerHTML = document.getElementById(tagId).innerHTML; 
    }

    /**
     * The handler when unavailable review feedback checkbox is clicked.
     */
    function unAvailableFeedbackCheckboxHandler() {
        var ck = document.getElementById("unavailable");
        if (ck) {
            var disabled1 = ck.checked ? "disabled" : "";
            var disabled2 = ck.checked ? "" : "disabled";
            var eles1 = getChildrenByNamePrefix(document.getElementById('reviewFeedbackForm'), 'reviewerScore[');
            eles1 = eles1.concat(getChildrenByNamePrefix(document.getElementById('reviewFeedbackForm'), 'reviewerFeedback['));
            for (var i = 0; i < eles1.length; i++) {
                eles1[i].disabled = disabled1;
            }
            document.getElementsByName('explanation')[0].disabled = disabled2;
        }
    }
    unAvailableFeedbackCheckboxHandler();
//-->
</script>
</html:html>
