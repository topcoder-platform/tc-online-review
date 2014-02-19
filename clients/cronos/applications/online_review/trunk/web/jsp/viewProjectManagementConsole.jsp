<%--
  - Author: TCSASSEMBLER
  - Version: 2.0
  - Copyright (C) 2010 - 2014 TopCoder Inc., All Rights Reserved.
  -
  - Description: This page provides a web form for Project Management Console. Such a form includes areas for extending
  - Registration phase, extending Submission phase and adding new resources to target project.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="or" uri="/or-tags" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>
<%@ taglib prefix="tc-webtag" uri="/tags/tc-webtags" %>
<c:set var="project" value="${requestScope.project}"/>
<fmt:setLocale value="en_US"/>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
            <jsp:include page="/includes/project/project_title.jsp"/>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

        <!-- TopCoder CSS -->
        <link type="text/css" rel="stylesheet" href="/css/style.css"/>
        <link type="text/css" rel="stylesheet" href="/css/coders.css"/>
        <link type="text/css" rel="stylesheet" href="/css/tcStyles.css"/>

        <!-- CSS and JS by Petar -->
        <link type="text/css" rel="stylesheet" href="/css/or/new_styles.css"/>
        <link type="text/css" rel="stylesheet" href="/css/or/phasetabs.css"/>
        <script language="JavaScript" type="text/javascript"
                src="/js/or/rollovers2.js"><!-- @ --></script>
        <script language="JavaScript" type="text/javascript"
                src="/js/or/dojo.js"><!-- @ --></script>
        <script language="JavaScript" type="text/javascript"
                src="/js/or/util.js"><!-- @ --></script>
        <script language="JavaScript" type="text/javascript"
                src="/js/or/validation_util.js"><!-- @ --></script>
        <script language="JavaScript" type="text/javascript"
                src="/js/or/validation_edit_project_links.js"><!-- @ --></script>
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
                                onClick="return activateTab('sc1', this)"><or:text key="manageProject.TimelineResources.title"/></a></li>
                            <c:if test="${((project.projectCategory.id == 1) || (project.projectCategory.id == 2))}"> <%-- Only show the tab for design and development --%>
                            <li><a href="javascript:void(0)"
                                    onClick="return activateTab('sc2', this)"><or:text key="manageProject.Distributions.title"/></a></li>
                            </c:if>
                            <c:if test="${reviewFeedbacksExist || reviewFeedbackAllowed}">
                                <li><a href="javascript:void(0)" onClick="return activateTab('sc3', this)">
                                    <or:text key="manageProject.ReviewPerformance.title"/></a></li>
                            </c:if>
                            <li><a href="javascript:void(0)" onclick="return activateTab('sc4', this)">
                                <or:text key="manageProject.ReviewPayments.title"/></a></li>
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
                                                    <or:text key="Error.manageProject.ValidationFailed"/>
                                                </span>
                                            </td>
                                        </tr>
                                        <s:actionerror escape="false" />
                                    </table>
                                    <br/>
                                </c:if>
                            </c:if>
                                
                            <s:form action="ManageProject" namespace="/actions">
                                <input type="hidden" name="pid" value="${project.id}" />
        
                                <div id="tabNewLinks">
                                    <%-- Extend Registration Phase area --%>
                                    <table class="scorecard" id="reg_phase_tbl" cellpadding="0" width="100%"
                                           style="border-collapse: collapse;">
                                        <tr>
                                            <td class="title" colspan="2">
                                                <c:choose>
                                                    <c:when test="${requestScope.registrationPhaseClosed}">
                                                        <or:text key="manageProject.RegPhase.title2"/>
                                                    </c:when>
                                                    <c:otherwise><or:text key="manageProject.RegPhase.title"/></c:otherwise>
                                                </c:choose>
                                            </td>
                                        </tr>
                                        <tr class="light">
                                            <td class="value">
                                                <or:text key="manageProject.RegPhase.deadline"/>
                                            </td>
                                            <td class="value">
                                                <fmt:formatDate value="${requestScope.registrationPhase.scheduledEndDate}"
                                                                pattern="MM.dd.yyyy HH:mm z"/>
                                            </td>
                                        </tr>
                                        <tr class="dark">
                                            <td class="value">
                                                <or:text key="manageProject.RegPhase.duration"/>
                                            </td>
                                            <td class="value">
                                                <c:out value="${requestScope.registrationPhaseDuration}"/>
                                            </td>
                                        </tr>
                                        <tr class="light">
                                            <td class="value">
                                                <or:text key="manageProject.RegPhase.extension"/>
                                            </td>
                                            <td class="value">
                                                <input type="text" class=".inputBoxDuration" name="registration_phase_extension"
                                                           <c:if test="${not requestScope.allowRegistrationPhaseExtension}">disabled="disabled" </c:if>
                                                           value="<or:fieldvalue field='registration_phase_extension' />" />
                                                <div class="error">
                                                    <s:fielderror escape="false"><s:param>registration_phase_extension</s:param></s:fielderror>
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
                                                    <or:text key="manageProject.SubmissionPhase.title"/>
                                                </td>
                                            </tr>
                                            <tr class="light">
                                                <td class="value">
                                                    <or:text key="manageProject.SubmissionPhase.deadline"/>
                                                </td>
                                                <td class="value">
                                                    <fmt:formatDate value="${requestScope.submissionPhase.scheduledEndDate}"
                                                                    pattern="MM.dd.yyyy HH:mm z"/>
                                                </td>
                                            </tr>
                                            <tr class="dark">
                                                <td class="value">
                                                    <or:text key="manageProject.SubmissionPhase.duration"/>
                                                </td>
                                                <td class="value">
                                                    <c:out value="${requestScope.submissionPhaseDuration}"/>
                                                </td>
                                            </tr>
                                            <tr class="light">
                                                <td class="value">
                                                    <or:text key="manageProject.SubmissionPhase.extension"/>
                                                </td>
                                                <td class="value">
                                                    <input type="text" class=".inputBoxDuration" name="submission_phase_extension"
                                                               <c:if test="${not requestScope.allowSubmissionPhaseExtension}">disabled="disabled" </c:if>
                                                               value="<or:fieldvalue field='submission_phase_extension' />" />
                                                    <div class="error">
                                                        <s:fielderror escape="false"><s:param>submission_phase_extension</s:param></s:fielderror>
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
                                            <td class="title" colspan="5"><or:text key="manageProject.Resources.title" /></td>
                                        </tr>
                                        <tr>
                                            <td class="header"><or:text key="manageProject.Resources.Role"/></td>
                                            <td class="header"><or:text key="manageProject.Resources.Handles"/></td>
                                        </tr>
        
                                        <c:forEach items="${requestScope.availableRoles}" var="role" varStatus="index">
                                            <tr class="${(index.index % 2 == 0) ? 'light' : 'dark'}">
                                                <td class="value">
                                                    <input type="hidden" name="resource_role_id[${index.index}]" value="${role.id}"/>
                                                    <c:out value="${role.name}"/>
                                                    <div class="error">
                                                        <s:fielderror escape="false"><s:param>resource_role_id[${index.index}]</s:param></s:fielderror>
                                                    </div>
                                                </td>
                                                <td class="value">
                                                    <input type="text" class="inputTextBox" name="resource_handles[${index.index}]"
                                                               size="" value="<or:fieldvalue field='resource_handles[${index.index}]' />" />
                                                    <div class="error">
                                                        <s:fielderror escape="false"><s:param>resource_handles[${index.index}]</s:param></s:fielderror>
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
                                    <input type="image"  src="<or:text key='btnSaveChanges.img' />" alt="<or:text key='btnSaveChanges.alt' />" border="0"/>&#160;
                                    <a href="<or:url value='/actions/ViewProjectDetails?pid=${project.id}' />"><img
                                            src="<or:text key='btnCancel.img' />" alt="<or:text key='btnCancel.alt' />" border="0"/></a>
                                            &nbsp;
                                </div>
                            </s:form>
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
                                    onClick="return activateTab('sc1', this)"><or:text key="manageProject.TimelineResources.title"/></a></li>
                                <li id='current'><a href="javascript:void(0)"
                                        onClick="return activateTab('sc2', this)"><or:text key="manageProject.Distributions.title"/></a></li>
                                <c:if test="${reviewFeedbacksExist || reviewFeedbackAllowed}">
                                    <li><a href="javascript:void(0)" onClick="return activateTab('sc3', this)">
                                        <or:text key="manageProject.ReviewPerformance.title"/></a></li>
                                </c:if>
                                <li><a href="javascript:void(0)" onclick="return activateTab('sc4', this)">
                                    <or:text key="manageProject.ReviewPayments.title"/></a></li>
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
                                                        <or:text key="Error.manageProject.ValidationFailed"/>
                                                    </span>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td colspan="2">
                                            <s:actionerror escape="false" />
                                                </td>
                                            </tr>
                                        </table>
                                        <br/>
                                    </c:if>
                                </c:if>
                            
                                <%-- Create Design Distribution --%>
                                <s:form action="ManageDistribution?activeTabIdx=2" method="POST" enctype="multipart/form-data" namespace="/actions">
                                
                                <input type="hidden" name="postBack" value="y" />
                                <input type="hidden" name="pid" value="${project.id}" />
                                
                                <table class="scorecard" id="distribution_tbl" cellpadding="0" width="100%"
                                       style="border-collapse: collapse;">
                                    <tr>
                                        <td class="title" colspan="2">
                                            <or:text key="manageProject.Distributions.Create.Design.title"/>
                                        </td>
                                    </tr>
                                    <tr class="light">
                                        <td class="value" width="25%">
                                            <or:text key="manageProject.Distributions.packagename"/>&nbsp;
                                               <c:if test="${needsPackageName == true}">
                                                <or:text key="global.required.paren"/>
                                            </c:if>   
                                        </td>
                                        <td class="value" width="75%">
                                            <input type="text" class="inputBox" name="distribution_package_name" value="<or:fieldvalue field='distribution_package_name' />" />
                                            <div class="error">
                                                <s:fielderror escape="false"><s:param>distribution_package_name</s:param></s:fielderror>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr class="dark">
                                        <td class="value" width="25%">
                                            <or:text key="manageProject.Distributions.rs"/>&nbsp;
                                            <or:text key="global.required.paren"/>
                                        </td>
                                        <td class="value" width="75%">
                                            <input type="file" name="distribution_rs" size="20" class="inputBox" style="width:250px;vertical-align:middle;"  value="<or:fieldvalue field='distribution_rs' />" />
                                            <div class="error">
                                                <s:fielderror escape="false"><s:param>distribution_rs</s:param></s:fielderror>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr class="light">
                                        <td class="value" width="25%">
                                            <or:text key="manageProject.Distributions.additional1"/>
                                        </td>
                                        <td class="value" width="75%">
                                            <table border="0" cellpadding="0" cellspacing="0">
                                            <tr>
                                            <td id="additionalFile1">
                                               <input type="file" name="distribution_additional1" size="20" class="inputBox" style="width:250px;vertical-align:middle;"  value="<or:fieldvalue field='distribution_additional1' />" />
                                                <div class="error">
                                                    <s:fielderror escape="false"><s:param>distribution_additional1</s:param></s:fielderror>
                                                </div>
                                            </td>
                                            <td>  
                                                   <img src="<or:text key='btnClear.img' />" alt="<or:text key='btnClear.alt' />" onclick="javascript:clearFileInputField('additionalFile1')" />
                                               </td>
                                               </tr>
                                               </table>
                                        </td>
                                    </tr>
                                    <tr class="dark">
                                        <td class="value" width="25%">
                                            <or:text key="manageProject.Distributions.additional2"/>
                                        </td>
                                        <td class="value" width="75%">
                                               <table border="0" cellpadding="0" cellspacing="0">
                                               <tr>
                                               <td id="additionalFile2">
                                                  <input type="file" name="distribution_additional2" size="20" class="inputBox" style="width:250px;vertical-align:middle;"  value="<or:fieldvalue field='distribution_additional2' />" />
                                                   <div class="error">
                                                       <s:fielderror escape="false"><s:param>distribution_additional2</s:param></s:fielderror>
                                                   </div>
                                               </td>
                                               <td>  
                                                   <img src="<or:text key='btnClear.img' />" alt="<or:text key='btnClear.alt' />" onclick="javascript:clearFileInputField('additionalFile2')" />
                                               </td>
                                               </tr>
                                               </table>
                                        </td>
                                    </tr>
                                    <tr class="light">
                                        <td class="value" width="25%">
                                            <or:text key="manageProject.Distributions.additional3"/>
                                        </td>
                                        <td class="value" width="75%">
                                               <table border="0" cellpadding="0" cellspacing="0">
                                               <tr>
                                               <td id="additionalFile3">
                                                  <input type="file" name="distribution_additional3" size="20" class="inputBox" style="width:250px;vertical-align:middle;"  value="<or:fieldvalue field='distribution_additional3' />" />
                                                   <div class="error">
                                                       <s:fielderror escape="false"><s:param>distribution_additional3</s:param></s:fielderror>
                                                   </div>
                                               </td>
                                               <td>  
                                                   <img src="<or:text key='btnClear.img' />" alt="<or:text key='btnClear.alt' />" onclick="javascript:clearFileInputField('additionalFile3')" />
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
                                                   <input type="checkbox" name="upload_to_server" id="uploadToServer"  <or:checked name='upload_to_server' value='on|yes|true' def="true"/> />
                                                </c:otherwise>
                                            </c:choose>
                                            <input type="hidden" name="upload_to_server" value="false" />
                                            <label for="uploadToServer"><or:text key="manageProject.Distributions.Upload" /></label>
                                            <div class="error">
                                                <s:fielderror escape="false"><s:param>upload_to_server</s:param></s:fielderror>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr class="light">
                                        <td class="value" colspan="2">
                                            <input type="checkbox" name="return_distribution" id="returnDistribution" <or:checked name='return_distribution' value='on|yes|true' def="false"/> /> 
                                            <input type="hidden" name="return_distribution" value="false" />
                                            <label for="returnDistribution"><or:text key="manageProject.Distributions.Return_distribution" /></label>
                                            <div class="error">
                                                <s:fielderror escape="false"><s:param>return_distribution</s:param></s:fielderror>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr class="dark">
                                        <td class="value" colspan="2">
                                            <input type="image"  src="<or:text key='btnGenerate.img' />" alt="<or:text key='btnGenerate.alt' />" border="0"/>&#160;
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="lastRowTD" colspan="2"><!-- @ --></td>
                                    </tr>
                                </table>
                                </s:form>
                                
                                <%-- Upload Distribution --%>
                                <s:form action="UploadDistribution?activeTabIdx=2" method="POST" enctype="multipart/form-data" namespace="/actions">
                                <input type="hidden" name="postBack" value="y" />
                                <input type="hidden" name="pid" value="${project.id}" />
                                <table class="scorecard" id="upload_distribution_tbl" cellpadding="0" width="100%"
                                       style="border-collapse: collapse;">
                                    <tr>
                                        <td class="title" colspan="2">
                                            <c:choose>
                                                <c:when test="${(project.projectCategory.id == 1)}">
                                                    <or:text key="manageProject.Distributions.Upload.Design.title"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <or:text key="manageProject.Distributions.Upload.Development.title"/>
                                                </c:otherwise>                                                
                                            </c:choose>
                                        </td>
                                    </tr>
                                    <tr class="light">
                                        <td class="value" width="25%">
                                            <or:text key="manageProject.Distributions.File"/>
                                        </td>
                                        <td class="value" width="75%">
                                            <input type="file" name="distribution_file" size="20" class="inputBox" style="width:250px;vertical-align:middle;"  value="<or:fieldvalue field='distribution_file' />" />
                                            <div class="error">
                                                <s:fielderror escape="false"><s:param>distribution_file</s:param></s:fielderror>
                                            </div>
                                            <input type="image"  src="<or:text key='btnUpload.img' />" alt="<or:text key='btnUpload.alt' />" border="0"/>&#160;
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="lastRowTD" colspan="2"><!-- @ --></td>
                                    </tr>
                                </table><br/>
                                <div align="right">
                                   <a href="<or:url value='/actions/ViewProjectDetails?pid=${project.id}' />"><img
                                           src="<or:text key='btnReturnToProjDet.img' />" alt="<or:text key='btnReturnToProjDet.alt' />" border="0"/></a>
                                   &nbsp;
                                </div>
                                </s:form>
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
                               onClick="return activateTab('sc1', this)"><or:text
                                key="manageProject.TimelineResources.title"/></a></li>
                        <c:if test="${((project.projectCategory.id == 1) || (project.projectCategory.id == 2))}"> 
                        <%-- Only show the tab for design and development --%>
                            <li><a href="javascript:void(0)"
                                   onClick="return activateTab('sc2', this)"><or:text
                                    key="manageProject.Distributions.title"/></a></li>
                        </c:if>
                        <li id='current'><a href="javascript:void(0)" onClick="return activateTab('sc3', this)">
                            <or:text key="manageProject.ReviewPerformance.title"/></a></li>
                        <li><a href="javascript:void(0)" onclick="return activateTab('sc4', this)">
                            <or:text key="manageProject.ReviewPayments.title"/></a></li>
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
                                            <or:text key="manageProject.ReviewPerformance.Feedback.From.title"/>
                                            <tc-webtag:handle coderId="${feedback.createUser}"/>
                                            <or:text key="manageProject.ReviewPerformance.Feedback.From.at"/>
                                            <fmt:formatDate value="${feedback.createDate}" pattern="MM.dd.yyyy HH:mm zzz"/><c:if test="${not empty feedback.modifyUser and (feedback.modifyUser ne feedback.createUser or feedback.modifyDate ne feedback.createDate)}">,
                                                <or:text key="manageProject.ReviewPerformance.Feedback.UpdateBy.title"/>
                                                <tc-webtag:handle coderId="${feedback.modifyUser}"/>
                                                <or:text key="manageProject.ReviewPerformance.Feedback.UpdateBy.at"/>
                                                <fmt:formatDate value="${feedback.modifyDate}" pattern="MM.dd.yyyy HH:mm zzz"/>
                                            </c:if>
                                        </td>
                                    </tr>
                                    <c:if test="${empty feedback.comment}">
                                        <tr>
                                            <td class="value"><b><or:text key="manageProject.ReviewPerformance.Reviewer.title"/></b></td>
                                            <td class="valueC"><b><or:text key="manageProject.ReviewPerformance.Score.Bad"/></b></td>
                                            <td class="valueC"><b><or:text key="manageProject.ReviewPerformance.Score.Average"/></b></td>
                                            <td class="valueC"><b><or:text key="manageProject.ReviewPerformance.Score.Good"/></b></td>
                                            <td class="valueC" width="65%"><b><or:text key="manageProject.ReviewPerformance.Feedback.title"/></b></td>
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
                                                <strong><or:text key="manageProject.ReviewPerformance.Feedback.NotAvailable"/></strong>
                                            </td>
                                        </tr>
                                        <tr class="light">
                                            <td class="value" colspan="5">${orfn:htmlEncode(feedback.comment)}</td>
                                        </tr>
                                    </c:if>
                                </table>

                                <br/>

                                <div align="right">
                                    <a href="<or:url value='/actions/EditReviewFeedback?activeTabIdx=3&pid=${project.id}' />"><img
                                            src="<or:text key='manageProject.ReviewPerformance.btnEdit.img' />" alt="<or:text key='manageProject.ReviewPerformance.btnEdit.alt' />" border="0"/></a>
                                    &nbsp;
                                    <a href="<or:url value='/actions/ViewProjectDetails?pid=${project.id}' />"><img
                                            src="<or:text key='btnReturnToProjDet.img' />" alt="<or:text key='btnReturnToProjDet.alt' />" border="0"/></a>&nbsp;&nbsp;
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
                                                        <or:text key="Error.manageProject.ValidationFailed"/>
                                                    </span>
                                                </td>
                                            </tr>
                                            <s:actionerror escape="false" />
                                        </table>
                                        <br/>
                                    </c:if>
                                </c:if>
                                <c:set var="unavailable" value="${not toEdit or not empty feedback.comment}"/>
                                <%-- Save Review Feedback form  --%>
                                <s:form id="reviewFeedbackForm" action="SaveReviewFeedback?activeTabIdx=3" method="POST" namespace="/actions">
                                    <input type="hidden" name="postBack" value="y"/>
                                    <input type="hidden" name="pid" value="${project.id}"/>

                                    <table class="scorecard" cellpadding="0" width="100%" style="border-collapse: collapse;">
                                        <tr>
                                            <td class="title" colspan="5">
                                                <or:text key="manageProject.ReviewPerformance.Review.Performance.title"/>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td class="value" colspan="5">
                                                <or:text key="manageProject.ReviewPerformance.Review.Performance.hint"/>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td class="value"><b><or:text
                                                    key="manageProject.ReviewPerformance.Reviewer.title"/></b></td>
                                            <td class="valueC"><b><or:text key="manageProject.ReviewPerformance.Score.Bad"/></b></td>
                                            <td class="valueC"><b><or:text key="manageProject.ReviewPerformance.Score.Average"/></b></td>
                                            <td class="valueC"><b><or:text key="manageProject.ReviewPerformance.Score.Good"/></b></td>
                                            <td class="valueC" width="65%"><b><or:text key="manageProject.ReviewPerformance.Feedback.title"/></b></td>
                                        </tr>
                                        <c:forEach items="${reviewerResourcesMap}" var="reviewerEntry" varStatus="loop">
                                            <c:set var="reviewerUserId" value="${reviewerEntry.key}"/>
                                            <c:set var="reviewerResource" value="${reviewerEntry.value}"/>
                                            <c:set var="resourceIdx" value="${loop.index}"/>
                                            <tr class="${(loop.index % 2 == 0) ? 'dark' : 'light'}">
                                                <td class="value">
                                                    <tc-webtag:handle coderId="${reviewerUserId}"/>
                                                    <input type="hidden" name="reviewerUserId[${resourceIdx}]" value="${reviewerUserId}"/>
                                                    <div class="error">
                                                        <s:fielderror escape="false"><s:param>reviewerScore[${resourceIdx}]</s:param></s:fielderror>
                                                    </div>
                                                </td>
                                                <td class="valueC">
                                                    <input type="radio" name="reviewerScore[${resourceIdx}]" value="0" <c:if test="${unavailable}">disabled="disabled" </c:if>  <or:checked name='reviewerScore[${resourceIdx}]' value='0' />/>
                                                </td>
                                                <td class="valueC">
                                                    <input type="radio" name="reviewerScore[${resourceIdx}]" value="1" <c:if test="${unavailable}">disabled="disabled" </c:if>  <or:checked name='reviewerScore[${resourceIdx}]' value='1' />/>
                                                </td>
                                                <td class="valueC">
                                                    <input type="radio" name="reviewerScore[${resourceIdx}]" value="2" <c:if test="${unavailable}">disabled="disabled" </c:if>  <or:checked name='reviewerScore[${resourceIdx}]' value='2' />/>
                                                </td>
                                                <td class="value ffednack-text-wrapper">
                                                    <textarea name="reviewerFeedback[${resourceIdx}]" rows="3" 
                                                                   class="feedback-text" <c:if test="${unavailable}">disabled="disabled" </c:if> ><or:fieldvalue field="reviewerFeedback[${resourceIdx}]" /></textarea>
                                                    <div class="error">
                                                        <s:fielderror escape="false"><s:param>reviewerFeedback[${resourceIdx}]</s:param></s:fielderror>
                                                    </div>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                        <tr class="${(fn:length(reviewerResourcesMap) % 2 == 0) ? 'dark' : 'light'}">
                                            <td colspan="5" class="value">
                                                <input type="checkbox" name="unavailable" id="unavailable" onclick="javascript:unAvailableFeedbackCheckboxHandler();" <or:checked name='unavailable' value='on|yes|true' /> /><or:text key="manageProject.ReviewPerformance.Feedback.NotAvailable.Checkbox"/><br/>
                                                <textarea name="explanation" class="feedback-text" rows="3" <c:if test="${not unavailable}">disabled="disabled" </c:if> ><or:fieldvalue field="explanation" /></textarea>
                                                <div class="error">
                                                    <s:fielderror escape="false"><s:param>explanation</s:param></s:fielderror>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td class="lastRowTD" colspan="5"><!-- @ --></td>
                                        </tr>
                                    </table>
                                    <br/>
                                    <div align="right">
                                        <input type="image" 
                                                    src="<or:text key='btnSaveChanges.img' />" alt="<or:text key='btnSaveChanges.alt' />" border="0"/>
                                        &nbsp;
                                        <c:if test="${not toEdit}">
                                            <a href="<or:url value='/actions/ViewProjectDetails?pid=${project.id}' />"><img
                                                    src="<or:text key='btnCancel.img' />" alt="<or:text key='btnCancel.alt' />" border="0"/></a>&nbsp;&nbsp;
                                        </c:if>
                                        <c:if test="${toEdit}">
                                            <a href="<or:url value='/actions/ViewManagementConsole?activeTabIdx=3&pid=${project.id}' />"><img
                                                    src="<or:text key='btnCancel.img' />" alt="<or:text key='btnCancel.alt' />" border="0"/></a>&nbsp;&nbsp;
                                        </c:if>
                                        <br/>
                                    </div>
                                </s:form>

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
                                   onClick="return activateTab('sc1', this)"><or:text
                                    key="manageProject.TimelineResources.title"/></a></li>
                            <c:if test="${((project.projectCategory.id == 1) || (project.projectCategory.id == 2))}">
                                <%-- Only show the tab for design and development --%>
                                <li><a href="javascript:void(0)"
                                       onClick="return activateTab('sc2', this)"><or:text
                                        key="manageProject.Distributions.title"/></a></li>
                            </c:if>
                            <c:if test="${reviewFeedbacksExist || reviewFeedbackAllowed}">
                                <li><a href="javascript:void(0)" onClick="return activateTab('sc3', this)">
                                    <or:text key="manageProject.ReviewPerformance.title"/></a></li>
                            </c:if>
                            <li id='current'><a href="javascript:void(0)" onclick="return activateTab('sc4', this)">
                                <or:text key="manageProject.ReviewPayments.title"/></a></li>
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
                                                    <or:text key="Error.manageProject.ValidationFailed"/>
                                                </span>
                                                    </td>
                                                </tr>
                                                <s:actionerror escape="false" />
                                            </table>
                                            <br/>
                                        </c:if>
                                    </c:if>

                                    <s:form action="SaveReviewPayments?activeTabIdx=4" namespace="/actions">
                                        <input type="hidden" name="pid" value="${project.id}" />

                                        <div id="tabNewLinks">
                                            <table class="scorecard" id="review_payments_tbl" cellpadding="0" width="100%"
                                                   style="border-collapse: collapse;">
                                                <tr>
                                                    <td class="title" colspan="4"><or:text key="manageProject.ReviewPayments.title"/></a></li></td>
                                                </tr>
                                                <tr>
                                                    <td class="title"><or:text key="manageProject.ReviewPayments.ResourceRole"/></td>
                                                    <td class="title"><or:text key="manageProject.ReviewPayments.default"/> *</td>
                                                    <td class="title"><or:text key="manageProject.ReviewPayments.FixedAmount"/></td>
                                                    <td class="title"><or:text key="manageProject.ReviewPayments.percent"/></td>
                                                </tr>
                                                <c:set var="isSubmitReturn" value="${fn:length(reviewPaymentsForm.map['resources_roles_id']) gt 0}" />
                                                <c:set var="reviewPaymentsLength" value="${fn:length(resourceRoleIds)}" />
                                                <c:forEach begin="0" end="${reviewPaymentsLength - 1}" var="reviewPaymentsIdx" varStatus="vs">
                                                <c:set var="roleId" value="${resourceRoleIds[reviewPaymentsIdx]}" />
                                                <tr <c:if test="${vs.index % 2 eq 0}">class="light"</c:if> <c:if test="${vs.index % 2 eq 1}">class="dark"</c:if> >
                                                    <td class="value" nowrap="nowrap">
                                                            ${resourceRoleNames[reviewPaymentsIdx]}
                                                        <div class="error"><s:fielderror escape="false"><s:param>resources_roles_id[${reviewPaymentsIdx}]</s:param></s:fielderror></div>
                                                        <input type="hidden" name="resources_roles_id[${reviewPaymentsIdx}]" value="${roleId}" />
                                                    </td>
                                                    <td class="value" nowrap="nowrap">
                                                        <c:if test="${isSubmitReturn}">
                                                            <input type="radio" id="resource_payments_radio_default[${reviewPaymentsIdx}]" name="resource_payments_radio[${reviewPaymentsIdx}]" value="default" <or:checked name='resource_payments_radio[${reviewPaymentsIdx}]' value='default' />/>
                                                        </c:if>
                                                        <c:if test="${not isSubmitReturn}">
                                                            <input type="radio" value="default" id="resource_payments_radio_default[${reviewPaymentsIdx}]" name="resource_payments_radio[${reviewPaymentsIdx}]" <c:if test="${reviewPaymentsRadio[reviewPaymentsIdx] eq 'default'}">checked="checked"</c:if> >
                                                        </c:if>
                                                        <label for="resource_payments_radio_default[${reviewPaymentsIdx}]">${"$"}<c:choose><c:when test="${empty defaultPayments[roleId]}">0</c:when><c:otherwise>${orfn:displayPaymentAmt(pageContext.request, defaultPayments[roleId])}</c:otherwise></c:choose></label>
                                                    </td>
                                                    <td class="value" nowrap="nowrap">
                                                        <c:if test="${isSubmitReturn}">
                                                            <input type="radio" id="resource_payments_radio_fixed[${reviewPaymentsIdx}]" name="resource_payments_radio[${reviewPaymentsIdx}]" value="fixed" <or:checked name='resource_payments_radio[${reviewPaymentsIdx}]' value='fixed' />/>
                                                        </c:if>
                                                        <c:if test="${not isSubmitReturn}">
                                                            <input type="radio" value="fixed" id="resource_payments_radio_fixed[${reviewPaymentsIdx}]" name="resource_payments_radio[${reviewPaymentsIdx}]" <c:if test="${reviewPaymentsRadio[reviewPaymentsIdx] eq 'fixed'}">checked="checked"</c:if> >
                                                        </c:if>
                                                        <label for="resource_payments_radio_fixed[${reviewPaymentsIdx}]">${"$"}</label>
                                                        <c:if test="${isSubmitReturn}">
                                                            <input type="text" name="resource_payments_fixed_amount[${reviewPaymentsIdx}]" class="inputBoxDuration" value="<or:fieldvalue field='resource_payments_fixed_amount[${reviewPaymentsIdx}]' />" />
                                                        </c:if>
                                                        <c:if test="${not isSubmitReturn}">
                                                            <input type="text" name="resource_payments_fixed_amount[${reviewPaymentsIdx}]" class="inputBoxDuration" value="${reviewPaymentsFixed[reviewPaymentsIdx]}" />
                                                        </c:if>
                                                        <div class="error"><s:fielderror escape="false"><s:param>resource_payments_fixed_amount[${reviewPaymentsIdx}]</s:param></s:fielderror></div>
                                                    </td>
                                                    <td class="value" nowrap="nowrap">
                                                        <c:if test="${isSubmitReturn}">
                                                            <input type="radio" id="resource_payments_radio_percentage[${reviewPaymentsIdx}]" name="resource_payments_radio[${reviewPaymentsIdx}]" value="percentage" <or:checked name='resource_payments_radio[${reviewPaymentsIdx}]' value='percentage' />/>
                                                            <input type="text" name="resource_payments_percent_amount[${reviewPaymentsIdx}]" class="inputBoxDuration" value="<or:fieldvalue field='resource_payments_percent_amount[${reviewPaymentsIdx}]' />" />
                                                        </c:if>
                                                        <c:if test="${not isSubmitReturn}">
                                                            <input type="radio" value="percentage" id="resource_payments_radio_percentage[${reviewPaymentsIdx}]" name="resource_payments_radio[${reviewPaymentsIdx}]" <c:if test="${reviewPaymentsRadio[reviewPaymentsIdx] eq 'percentage'}">checked="checked"</c:if> >
                                                            <input type="text" name="resource_payments_percent_amount[${reviewPaymentsIdx}]" class="inputBoxDuration" value="${reviewPaymentsPercentage[reviewPaymentsIdx]}" />
                                                        </c:if>
                                                        <label for="resource_payments_radio_percentage[${reviewPaymentsIdx}]">%</label>
                                                        <div class="error"><s:fielderror escape="false"><s:param>resource_payments_percent_amount[${reviewPaymentsIdx}]</s:param></s:fielderror></div>
                                                    </td>
                                                    </c:forEach>
                                                <tr>
                                                    <td class="lastRowTD" colspan="4"><!-- @ --></td>
                                                </tr>
                                            </table>
                                        </div>
                                        <br/>
                                        <div><or:text key="manageProject.ReviewPayments.desc"/></div>
                                        <div align="right">
                                            <input type="image"  src="<or:text key='btnSaveChanges.img' />" alt="<or:text key='btnSaveChanges.alt' />" border="0"/>&#160;
                                            <a href="<or:url value='/actions/ViewProjectDetails?pid=${project.id}' />"><img
                                                    src="<or:text key='btnCancel.img' />" alt="<or:text key='btnCancel.alt' />" border="0"/></a>
                                            &nbsp;
                                        </div>
                                    </s:form>
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
</html>
