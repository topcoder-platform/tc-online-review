<%--
  - Author: isv, romanoTC
  - Version: 1.0 (Online Review Project Management Console assembly v1.0)
  - Version: 1.1 (Distribution Auto Generation Assembly v1.0) Change notes: Added support for managing design and 
    development distributions.
  - Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
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
                        </ul>
                        <div style="clear:both;"></div>
                        <table class="scorecard" cellpadding="0" width="100%" style="border-collapse: collapse;">
                        <tr>
                        <td class="title">
                            <bean:message key="manageProject.TimelineResources.title"/>
                        </td>
                        </tr>
                        <tr>
                            <td class="lastRowTD"><!-- @ --></td>
                        </tr>
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
		
		                        <div class="bottomButtonBar">
		                            <html:image srcKey="btnSaveChanges.img" altKey="btnSaveChanges.alt" border="0"/>&#160;
		                            <html:link
		                                    page="/actions/ViewProjectDetails.do?method=viewProjectDetails&pid=${project.id}"><html:img
		                                    srcKey="btnCancel.img" altKey="btnCancel.alt" border="0"/></html:link>
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
	                        </ul>
	                        <div style="clear:both;"></div>
	                        <table class="scorecard" cellpadding="0" width="100%" style="border-collapse: collapse;">
	                        <tr>
		                        <td class="title">
	                                <bean:message key="manageProject.Distributions.title"/>
	                            </td>
                            </tr>
                            <tr>
	                            <td class="lastRowTD"><!-- @ --></td>
	                        </tr>
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
	                                        <html:image srcKey="btnGenerate.img" altKey="btnGenerate.alt" border="0"/>&#160;
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
	                                        <html:image srcKey="btnUpload.img" altKey="btnUpload.alt" border="0"/>&#160;
	                                    </td>
	                                </tr>
	                                <tr>
	                                    <td class="lastRowTD" colspan="2"><!-- @ --></td>
	                                </tr>
	                            </table><br/>
                                <div class="bottomButtonBar">
                                   &nbsp;
                                   <html:link
                                           page="/actions/ViewProjectDetails.do?method=viewProjectDetails&pid=${project.id}"><html:img
                                           srcKey="btnCancel.img" altKey="btnCancel.alt" border="0"/></html:link>
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


    
//-->
</script>
</html:html>
