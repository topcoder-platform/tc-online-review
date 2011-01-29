<%--
  - Author: FireIce, isv
  - Version: 1.1
  - Since: Online Review Late Deliverables Search Assembly 1.0
  - Copyright (C) 2010-2011 TopCoder Inc., All Rights Reserved.
  -
  - Description: This page displays late deliverables page
  -
  - Version 1.1 (Online Review Late Deliverables Edit assembly): Added "Advanced Search Parameters" area; added "Edit"
  - button for each of listed late deliverables records and linked them to respective Edit Late Deliverable page.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page language="java" isELIgnored="false" %>
<%@ page import="java.util.Date" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="html" uri="/tags/struts-html" %>
<%@ taglib prefix="bean" uri="/tags/struts-bean" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>
<%@ taglib prefix="tc-webtag" uri="/tags/tc-webtags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="now" value="<%=new Date()%>"/>
<fmt:formatDate value="${now}" pattern="z" var="currentTimezoneLabel"/>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html xhtml="true">
<head>
    <title>Online Review - Late Deliverables</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

    <!-- TopCoder CSS -->
    <link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/style.css' />" />
    <link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/coders.css' />" />
    <link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/tcStyles.css' />" />
    <link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/stats.css' />" />
    <link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/or/new_styles.css' />" />
    <link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/or/phasetabs.css' />" />

    <script language="JavaScript" type="text/javascript" src="<html:rewrite href='/js/or/rollovers2.js' />"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript" src="<html:rewrite href='/js/or/dojo.js' />"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript" src="<html:rewrite href='/js/or/util.js' />"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript" src="<html:rewrite href='/js/or/validation_util2.js' />"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript" src="<html:rewrite href='/js/or/parseDate.js' />"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript" src="<html:rewrite href='/js/or/late_deliverable_search.js' />"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript" src="<html:rewrite href='/js/or/expand_collapse.js' />"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript">
        var ajaxSupportUrl = "<html:rewrite page='/ajaxSupport' />";
    </script>
    <script language="JavaScript" type="text/javascript" src="<html:rewrite href='/js/or/ajax1.js' />"><!-- @ --></script>
</head>
<body onload="colorFormFields();">
<div align="center">
    <div class="maxWidthBody" align="left">
        <link type="image/x-icon" rel="shortcut icon" href="https://software.topcoder.com/i/favicon.ico">
        <jsp:include page="/includes/inc_header.jsp" />
        <jsp:include page="/includes/project/project_tabs.jsp" />

        <div id="mainMiddleContent">
            <div style="position: relative; width: 100%;">
                <html:form action="/actions/ViewLateDeliverables" onsubmit="return submit_form(this, true);" 
                           method="GET" styleId="ViewLateDeliverablesForm">
                    <html:hidden property="method" value="viewLateDeliverables" />
                        <div id="globalMesssage" 
                             style="display:${orfn:isErrorsPresent(pageContext.request) ? 'block' : 'none'}">
                            <div style="color:red;">
                                <bean:message key="viewLateDeliverables.ValidationFailed" />
                            </div>
                            <html:errors property="org.apache.struts.action.GLOBAL_MESSAGE" />
                        </div>
                    <table class="scorecard" style="border-collapse: collapse;" cellpadding="0" cellspacing="0" width="100%">
                        <tbody>
                            <tr>
                                <td class="title" colspan="6"><bean:message key="viewLateDeliverables.SearchForm.title" /></td>
                            </tr>

                            <tr class="light">
                                <td class="value" nowrap="nowrap"><b><bean:message key="viewLateDeliverables.ProjectCategory.Label" /></b></td>
                                <td class="value">
                                        <html:select styleClass="inputBox" property="project_categories" multiple="true" 
                                                     size="5" onkeypress="return submitOnEnter(this, event);">
                                            <html:option key='global.any' value="0" />
                                            <c:forEach items="${projectCategories}" var="category">
                                                <html:option key='ProjectCategory.${fn:replace(category.name, " ", "")}' value="${category.id}" />
                                            </c:forEach>
                                        </html:select>
                                        <div id="project_categories_validation_msg" style="display:none;" class="error"></div>
                                </td>

                                <td class="value" nowrap="nowrap"><b><bean:message key="viewLateDeliverables.ProjectStatus.Label" /></b></td>
                                <td class="value">
                                        <html:select styleClass="inputBox" property="project_statuses" multiple="true" 
                                                     size="5" onkeypress="return submitOnEnter(this, event);">
                                        <html:option key='global.any' value="0" />
                                            <c:forEach var="status" items="${projectStatuses}">
                                                <html:option key='ProjectStatus.${fn:replace(status.name, " ", "")}' value="${status.id}" />
                                            </c:forEach>
                                        </html:select>
                                        <div id="project_statuses_validation_msg" style="display:none;" class="error"></div>
                                    </td>
                                
                                <td class="value" nowrap="nowrap"><b><bean:message key="viewLateDeliverables.DeliverableType.Label" /></b></td>
                                <td class="value">
                                    <html:select styleClass="inputBox" property="deliverable_types" multiple="true" 
                                                 size="5" onkeypress="return submitOnEnter(this, event);">
                                        <c:forEach var="entry" items="${deliverableTypes}">
                                            <html:option key='DeliverableType.${fn:replace(entry.key, " ", "")}' value="${entry.value}" />
                                        </c:forEach>
                                    </html:select>
                                    <div id="deliverable_types_validation_msg" style="display:none;" class="error"></div>
                                </td>                                
                            </tr>

                            <tr class="dark">
                                <td class="value" nowrap="nowrap"><b><bean:message key="viewLateDeliverables.ProjectID.Label" /></b></td>
                                <td class="value" >
                                    <html:text property="project_id" styleClass="inputBox" style="width: 100px;" 
                                               onfocus="this.value = getTextValueOnFocus(this, true);" 
                                               onblur="this.value = getTextValueOnFocus(this, false);"/>
                                    <div id="project_id_validation_msg" style="display:none;" class="error"></div>
                                    <div id="project_id_serverside_validation" class="error"><html:errors property="project_id" prefix="" suffix="" /></div>
                                </td>
                                                                    
                                <td class="value" style="width: 100px;"><b><bean:message key="viewLateDeliverables.LateMemberHandle.Label" /></b></td>
                                <td class="value">
                                    <html:text property="handle" styleClass="inputBox" style="width: 100px;"
                                               onfocus="this.value = getTextValueOnFocus(this, true);" 
                                               onblur="this.value = getTextValueOnFocus(this, false);"/>
                                    <div id="handle_serverside_validation" class="error"><html:errors property="handle" prefix="" suffix="" /></div>
                                </td>    
                                
                                <td class="value" nowrap="nowrap"><b><bean:message key="viewLateDeliverables.ForgivenStatus.Label" /></b></td>
                                <td class="value">
                                    <html:select styleClass="inputBox" property="forgiven" 
                                                 onkeypress="return submitOnEnter(this, event);">
                                        <html:option value="Any" />
                                        <html:option value="Forgiven" />
                                        <html:option value="Not forgiven" />
                                    </html:select>
                                </td>                                                                                        
                            </tr>
                        </tbody>
                    </table>
                    <br/>
                    <table class="scorecard" style="border-collapse: collapse;" cellpadding="0" cellspacing="0" width="100%">
                        <tbody>
                            <tr>
                                <td class="title" colspan="6">
                                    <a onclick="return expcollHandler(this)" href="javascript:void(0)" id="Outa" 
                                       class="Outline">
                                    <img id="Outai" class="Outline" border="0" 
                                         src="/i/or/${requestScope.hasAnyAdvancedSearchParameter ? 'minus' : 'plus'}.gif" 
                                         width="9" height="9" style="margin-right:5px;" />
                                    <bean:message key="viewLateDeliverables.AdvancedSearchParameters"/></a></td>
                            </tr>
                        </tbody>
                        <tbody ID="Outar" <c:if test="${not requestScope.hasAnyAdvancedSearchParameter}">style="display:none"</c:if>>
                            <tr class="light">
                                <td class="value" nowrap="nowrap">
                                    <b><bean:message key="viewLateDeliverables.CockpitProject.Label"/></b>
                                </td>
                                <td class="value">
                                    <html:select property="tcd_project_id" style="width:150px;" styleClass="inputBox" 
                                                 onkeypress="return submitOnEnter(this, event);">
                                        <html:option key='global.any' value=""/>
                                        <c:forEach var="entry" items="${requestScope.cockpitProjects}">
                                            <html:option value="${entry.key}">
                                                <c:out value="${entry.value}"/>
                                            </html:option>
                                        </c:forEach>
                                    </html:select>
                                </td>

                                <td class="value" nowrap="nowrap">
                                    <b><bean:message key="viewLateDeliverables.ExplanationStatus.Label"/></b>
                                </td>
                                <td class="value">
                                    <html:select property="explanation_status" styleClass="inputBox" 
                                                 onkeypress="return submitOnEnter(this, event);">
                                        <html:option key='global.any' value=""/>
                                        <html:option key='global.explained' value="true"/>
                                        <html:option key='global.notExplained' value="false"/>
                                    </html:select>
                                </td> 
                                                                                       
                                <td class="value" nowrap="nowrap">
                                    <b><bean:message key="viewLateDeliverables.ResponseStatus.Label"/></b>
                                </td>
                                <td class="value">
                                    <html:select property="response_status" styleClass="inputBox"
                                                 onkeypress="return submitOnEnter(this, event);">
                                        <html:option key='global.any' value=""/>
                                        <html:option key='global.responded' value="true"/>
                                        <html:option key='global.notResponded' value="false"/>
                                    </html:select>
                                </td>                                                                                        
                            </tr>
                            <tr class="dark">
                                <td class="value" nowrap="nowrap">
                                    <b><bean:message key="viewLateDeliverables.DeadlineMinDate.Label"/></b>
                                </td>
                                <td class="value">
                                    <html:text style="width: 75px;"  
                                               onfocus="this.value = getDateValueOnFocus(this, true);" 
                                               onblur="this.value = getDateValueOnFocus(this, false);"
                                               property="min_deadline" styleClass="inputBoxDate"/>
                                    <c:out value="${currentTimezoneLabel}"/>
                                    <div id="min_deadline_validation_msg" style="display:none;" class="error"></div>
                                </td>

                                <td class="value" nowrap="nowrap">
                                    <b><bean:message key="viewLateDeliverables.DeadlineMaxDate.Label"/></b>
                                </td>
                                <td class="value" nowrap="nowrap" colspan="3">
                                    <html:text style="width: 75px;" property="max_deadline"
                                               styleClass="inputBoxDate"
                                               onfocus="this.value = getDateValueOnFocus(this, true);" 
                                               onblur="this.value = getDateValueOnFocus(this, false);"/>
                                    <c:out value="${currentTimezoneLabel}"/>
                                    <div id="max_deadline_validation_msg" style="display:none;" class="error"></div>
                                    <div id="deadline_serverside_validation" class="error">
                                        <html:errors property="max_deadline" prefix="" suffix="" /></div>
                                </td>
                            </tr>
                        </tbody>                
                    </table>
                    <br/>
                    <div align="center">
                        <html:image srcKey="btnSearch.img" altKey="btnSearch.alt" border="0" onclick="submitForm=true;"/>
                        &nbsp;&nbsp;
                        <html:image srcKey="btnClear.img" altKey="btnClear.alt" border="0" onclick="submitForm=false;"/>
                    </div>
                </html:form>

                <c:if test="${not empty showSearchResultsSection}">
                <c:choose>
                    <c:when test="${empty projectMap}">
                        <div align="center">
                            <bean:message key="viewLateDeliverables.SearchResults.NoMatch" />
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div align="right">
                        [ <a href="javascript:void(0)" onclick="return expandAll()"><bean:message key="global.expandAll" /></a> | <a href="javascript:void(0)" onclick="return collapseAll()"><bean:message key="global.collapseAll" /></a> ]
                        </div><br/>

                        <table class="scorecard" style="border-collapse: collapse;" cellpadding="0" cellspacing="0" width="100%">
                            <tbody>
                            <tr>
                                <td class="title" colspan="3"><bean:message key="viewLateDeliverables.SearchResults.title" /></td>
                            </tr>

                            <tr>
                                <td class="header" width="580"><bean:message key="viewLateDeliverables.SearchResults.column.ProjectName" /></td>
                                <td class="header" nowrap="nowrap"><bean:message key="viewLateDeliverables.SearchResults.column.ProjectCategory" /></td>
                                <td class="header" nowrap="nowrap"><bean:message key="viewLateDeliverables.SearchResults.column.ProjectStatus" /></td>
                            </tr>

                            <c:forEach items="${projectMap}" var="entry" varStatus="idxProj">
                            <c:set var="projectId" value="${entry.key}" />
                            <c:set var="project" value="${entry.value}" />
                            <tr class="${(idxProj.index % 2 == 0) ? 'light' : 'dark'}">
                                <td class="value">
                                    <a onclick="return expcollHandler(this)" href="javascript:void(0)" id="Out${idxProj.index}" class="Outline">
                                    <img id="Out${idxProj.index}i" class="Outline" border="0" src="/i/or/plus.gif" width="9" height="9" style="margin-right:5px;" />
                                    </a>
                                    <html:link page="/actions/ViewProjectDetails.do?method=viewProjectDetails&amp;pid=${projectId}">
                                        <strong>${project.allProperties['Project Name']}</strong> version ${project.allProperties['Project Version']}
                                    </html:link>
                                </td>

                                <td class="value">${project.projectCategory.name}</td>
                                <td class="value">${project.projectStatus.name}</td>
                            </tr>

                            <tr id="Out${idxProj.index}r" style="display:none">
                                <td colspan="3" class="value">
                                    <table border="1" cellpadding="0" cellspacing="0" width="100%">
                                        <tr>
                                            <td class="header" nowrap="nowrap" width="20%"><bean:message key="viewLateDeliverables.SearchResults.innerTable.column.Handle" /></td>
                                            <td class="header" nowrap="nowrap" width="20%"><bean:message key="viewLateDeliverables.SearchResults.innerTable.column.Deliverable" /></td>
                                            <td class="header" nowrap="nowrap" width="20%"><bean:message key="viewLateDeliverables.SearchResults.innerTable.column.Deadline" /></td>
                                            <td class="header" nowrap="nowrap" width="20%"><bean:message key="viewLateDeliverables.SearchResults.innerTable.column.Delay" /></td>
                                            <td class="header" nowrap="nowrap" width="15%"><bean:message key="viewLateDeliverables.SearchResults.innerTable.column.Forgiven" /></td>
                                            <td class="header" nowrap="nowrap" width="5%"><bean:message key="viewLateDeliverables.SearchResults.innerTable.column.Edit"/></td>
                                        </tr>
                                        <c:set var="lateDeliverables" value="${groupedLateDeliverables[projectId]}" />
                                        <c:forEach items="${lateDeliverables}" var="lateDeliverable" varStatus="status">
                                        <tr class="${(status.index % 2 == 0) ? 'light' : 'dark'}">
                                            <td class="value" width="20%"><tc-webtag:handle coderId="${orfn:getUserId(pageContext.request, lateDeliverable.resourceId)}" context="${orfn:getHandlerContextByCategoryId(project.projectCategory.id)}" /></td>
                                            <td class="value" width="20%">${orfn:getDeliverableName(pageContext.request, lateDeliverable.deliverableId)}</td>
                                            <td class="value" width="20%">${orfn:displayDate(pageContext.request, lateDeliverable.deadline)}</td>
                                            <c:choose>
                                                <c:when test="${lateDeliverable.delay != null}">
                                                    <td class="value" width="20%">${orfn:displayDelay(lateDeliverable.delay)}</td>
                                                </c:when>
                                                <c:otherwise>
                                                    <td class="value" width="20%">N/A</td>
                                                </c:otherwise>
                                            </c:choose>
                                            <td class="value" width="15%">${lateDeliverable.forgiven ? 'Yes' : 'No'}</td>
                                            <td class="value" width="5%">
                                                <html:link page="/actions/EditLateDeliverable.do?method=editLateDeliverable&amp;late_deliverable_id=${lateDeliverable.id}">
                                                    <bean:message key="global.Edit"/></html:link>
                                            </td>
                                        </tr>
                                        </c:forEach>
                                    </table>
                                </td>
                            </tr>
                            </c:forEach>
                            <tr>
                               <td class="lastRowTD" colspan="3"></td>
                            </tr>
                            </tbody>
                        </table>
                    </c:otherwise>
                </c:choose>
                </c:if>
            </div>
        </div>
        <jsp:include page="/includes/inc_footer.jsp" />
    </div>
</div>
</body>
</html:html>