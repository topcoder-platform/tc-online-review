<%--
  - Author: TCSASSEMBLER
  - Version: 2.0
  - Copyright (C) 2010 - 2014 TopCoder Inc., All Rights Reserved.
  -
  - Description: This page displays late deliverables page.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page language="java" isELIgnored="false" %>
<%@ page import="java.util.Date" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="or" uri="/or-tags" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>
<%@ taglib prefix="tc-webtag" uri="/tags/tc-webtags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="now" value="<%=new Date()%>"/>
<fmt:formatDate value="${now}" pattern="z" var="currentTimezoneLabel"/>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>Online Review - Late Deliverables</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

    <!-- TopCoder CSS -->
    <link type="text/css" rel="stylesheet" href="/css/style.css" />
    <link type="text/css" rel="stylesheet" href="/css/coders.css" />
    <link type="text/css" rel="stylesheet" href="/css/tcStyles.css" />
    <link type="text/css" rel="stylesheet" href="/css/stats.css" />
    <link type="text/css" rel="stylesheet" href="/css/or/new_styles.css" />
    <link type="text/css" rel="stylesheet" href="/css/or/phasetabs.css" />

    <script language="JavaScript" type="text/javascript" src="/js/or/rollovers2.js"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript" src="/js/or/dojo.js"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript" src="/js/or/util.js"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript" src="/js/or/validation_util.js"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript" src="/js/or/parseDate.js"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript" src="/js/or/late_deliverable_search.js"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript" src="/js/or/expand_collapse.js"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript">
        var ajaxSupportUrl = "<or:url value='/ajaxSupport' />";
    </script>
    <script language="JavaScript" type="text/javascript" src="/js/or/ajax1.js"><!-- @ --></script>
</head>
<body onload="colorFormFields();">
<div align="center">
    <div class="maxWidthBody" align="left">
        <link type="image/x-icon" rel="shortcut icon" href="https://software.topcoder.com/i/favicon.ico">
        <jsp:include page="/includes/inc_header.jsp" />
        <jsp:include page="/includes/project/project_tabs.jsp" />

        <div id="mainMiddleContent">
            <div style="position: relative; width: 100%;">
                <s:form action="ViewLateDeliverables" onsubmit="return submit_form(this, true);" 
                           method="GET" id="ViewLateDeliverablesForm" namespace="/actions">
                        <div id="globalMesssage" 
                             style="display:${orfn:isErrorsPresent(pageContext.request) ? 'block' : 'none'}">
                            <div style="color:red;">
                                <or:text key="viewLateDeliverables.ValidationFailed" />
                            </div>
                            <s:actionerror escape="false" />
                        </div>
                    <table class="scorecard" style="border-collapse: collapse;" cellpadding="0" cellspacing="0" width="100%">
                        <tbody>
                            <tr>
                                <td class="title" colspan="6"><or:text key="viewLateDeliverables.SearchForm.title" /></td>
                            </tr>

                            <tr class="light">
                                <td class="value" nowrap="nowrap"><b><or:text key="viewLateDeliverables.ProjectCategory.Label" /></b></td>
                                <td class="value">
                                        <select class="inputBox" name="project_categories" multiple="multiple" 
                                                     size="5" onkeypress="return submitOnEnter(this, event);"><c:set var="OR_FIELD_TO_SELECT" value="project_categories"/>
                                            <option  value="0" <or:selected value="0"/>><or:text key="global.any" def="0"/></option>
                                            <c:forEach items="${projectCategories}" var="category">
                                                <option  value="${category.id}"  <or:selected value="${category.id}"/>><or:text key="ProjectCategory.${fn:replace(category.name, ' ', '')}"  def="${category.id}"/></option>
                                            </c:forEach>
                                        </select>
                                        <div id="project_categories_validation_msg" style="display:none;" class="error"></div>
                                </td>

                                <td class="value" nowrap="nowrap"><b><or:text key="viewLateDeliverables.ProjectStatus.Label" /></b></td>
                                <td class="value">
                                        <select class="inputBox" name="project_statuses" multiple="multiple" 
                                                     size="5" onkeypress="return submitOnEnter(this, event);"><c:set var="OR_FIELD_TO_SELECT" value="project_statuses"/>
                                        <option  value="0" <or:selected value="0"/>><or:text key="global.any" def="0"/></option>
                                            <c:forEach var="status" items="${projectStatuses}">
                                                <option  value="${status.id}" <or:selected value="${status.id}"/>><or:text key="ProjectStatus.${fn:replace(status.name, ' ', '')}" def="${status.id}"/></option>
                                            </c:forEach>
                                        </select>
                                        <div id="project_statuses_validation_msg" style="display:none;" class="error"></div>
                                    </td>
                                
                                <td class="value" nowrap="nowrap"><b><or:text key="viewLateDeliverables.DeliverableType.Label" /></b></td>
                                <td class="value">
                                    <select class="inputBox" name="deliverable_types" multiple="multiple" 
                                                 size="5" onkeypress="return submitOnEnter(this, event);"><c:set var="OR_FIELD_TO_SELECT" value="deliverable_types"/>
                                        <c:forEach var="entry" items="${deliverableTypes}">
                                            <option  value="${entry.value}"  <or:selected value="${entry.value}"/>><or:text key="DeliverableType.${fn:replace(entry.key, ' ', '')}" def="${entry.key}"/></option>
                                        </c:forEach>
                                    </select>
                                    <div id="deliverable_types_validation_msg" style="display:none;" class="error"></div>
                                </td>                                
                            </tr>

                            <tr class="dark">
                                <td class="value" nowrap="nowrap"><b><or:text key="viewLateDeliverables.ProjectID.Label" /></b></td>
                                <td class="value" >
                                    <input type="text" name="project_id" class="inputBox" style="width: 100px;" 
                                               onfocus="this.value = getTextValueOnFocus(this, true);" 
                                               onblur="this.value = getTextValueOnFocus(this, false);" value="<or:fieldvalue field='project_id' />" />
                                    <div id="project_id_validation_msg" style="display:none;" class="error"></div>
                                    <div id="project_id_serverside_validation" class="error"><s:fielderror escape="false"><s:param>project_id</s:param></s:fielderror></div>
                                </td>
                                                                    
                                <td class="value" style="width: 100px;"><b><or:text key="viewLateDeliverables.LateMemberHandle.Label" /></b></td>
                                <td class="value">
                                    <input type="text" name="handle" class="inputBox" style="width: 100px;"
                                               onfocus="this.value = getTextValueOnFocus(this, true);" 
                                               onblur="this.value = getTextValueOnFocus(this, false);" value="<or:fieldvalue field='handle' />" />
                                    <div id="handle_serverside_validation" class="error"><s:fielderror escape="false"><s:param>handle</s:param></s:fielderror></div>
                                </td>    
                                
                                <td class="value" nowrap="nowrap"><b><or:text key="viewLateDeliverables.JustifiedStatus.Label" /></b></td>
                                <td class="value">
                                    <select class="inputBox" name="justified" 
                                                 onkeypress="return submitOnEnter(this, event);"><c:set var="OR_FIELD_TO_SELECT" value="justified"/>
                                        <option value="Any" <or:selected value="Any"/>>Any</option>
                                        <option value="Justified" <or:selected value="Justified"/>>Justified</option>
                                        <option value="Not justified" <or:selected value="Not justified"/>>Not justified</option>
                                    </select>
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
                                    <or:text key="viewLateDeliverables.AdvancedSearchParameters"/></a></td>
                            </tr>
                        </tbody>
                        <tbody ID="Outar" <c:if test="${not requestScope.hasAnyAdvancedSearchParameter}">style="display:none"</c:if>>
                            <tr class="light">
                                <td class="value" nowrap="nowrap">
                                    <b><or:text key="viewLateDeliverables.CockpitProject.Label"/></b>
                                </td>
                                <td class="value">
                                    <select name="tcd_project_id" style="width:150px;" class="inputBox" 
                                                 onkeypress="return submitOnEnter(this, event);"><c:set var="OR_FIELD_TO_SELECT" value="tcd_project_id"/>
                                        <option  value="" <or:selected value=""/>><or:text key="global.any" def=""/></option>
                                        <c:forEach var="entry" items="${requestScope.cockpitProjects}">
                                            <option value="${entry.id}" <or:selected value="${entry.id}"/>>
                                                <c:out value="${entry.name}"/>
                                            </option>
                                        </c:forEach>
                                    </select>
                                </td>

                                <td class="value" nowrap="nowrap">
                                    <b><or:text key="viewLateDeliverables.ExplanationStatus.Label"/></b>
                                </td>
                                <td class="value">
                                    <select name="explanation_status" class="inputBox" 
                                                 onkeypress="return submitOnEnter(this, event);"><c:set var="OR_FIELD_TO_SELECT" value="explanation_status"/>
                                        <option  value="" <or:selected value=""/>><or:text key="global.any" def=""/></option>
                                        <option  value="true" <or:selected value="true"/>><or:text key="global.explained" def="true"/></option>
                                        <option  value="false" <or:selected value="false"/>><or:text key="global.notExplained" def="false"/></option>
                                    </select>
                                </td> 
                                                                                       
                                <td class="value" nowrap="nowrap">
                                    <b><or:text key="viewLateDeliverables.ResponseStatus.Label"/></b>
                                </td>
                                <td class="value">
                                    <select name="response_status" class="inputBox"
                                                 onkeypress="return submitOnEnter(this, event);"><c:set var="OR_FIELD_TO_SELECT" value="response_status"/>
                                        <option  value="" <or:selected value=""/>><or:text key="global.any" def=""/></option>
                                        <option  value="true" <or:selected value="true"/>><or:text key="global.responded" def="true"/></option>
                                        <option  value="false" <or:selected value="false"/>><or:text key="global.notResponded" def="false"/></option>
                                    </select>
                                </td>
                            </tr>
                            <tr class="dark">
                                <td class="value" nowrap="nowrap">
                                    <b><or:text key="viewLateDeliverables.DeadlineMinDate.Label"/></b>
                                </td>
                                <td class="value">
                                    <input type="text" style="width: 75px;" name="min_deadline"
                                               class="inputBoxDate"
                                               onfocus="this.value = getDateValueOnFocus(this, true);" 
                                               onblur="this.value = getDateValueOnFocus(this, false);" value="<or:fieldvalue field='min_deadline' />" />
                                    <c:out value="${currentTimezoneLabel}"/>
                                    <div id="min_deadline_validation_msg" style="display:none;" class="error"></div>
                                </td>

                                <td class="value" nowrap="nowrap">
                                    <b><or:text key="viewLateDeliverables.DeadlineMaxDate.Label"/></b>
                                </td>
                                <td class="value" nowrap="nowrap">
                                    <input type="text" style="width: 75px;" name="max_deadline"
                                               class="inputBoxDate"
                                               onfocus="this.value = getDateValueOnFocus(this, true);" 
                                               onblur="this.value = getDateValueOnFocus(this, false);" value="<or:fieldvalue field='max_deadline' />" />
                                    <c:out value="${currentTimezoneLabel}"/>
                                    <div id="max_deadline_validation_msg" style="display:none;" class="error"></div>
                                    <div id="deadline_serverside_validation" class="error">
                                        <s:fielderror escape="false"><s:param>max_deadline</s:param></s:fielderror></div>
                                </td>
                                <td class="value" nowrap="nowrap">
                                    <b><or:text key="viewLateDeliverables.LateDeliverableType.Label"/></b>
                                </td>
                                <td class="value">
                                    <select name="late_deliverable_type" class="inputBox"
                                                 onkeypress="return submitOnEnter(this, event);"><c:set var="OR_FIELD_TO_SELECT" value="late_deliverable_type"/>
                                        <option  value="" <or:selected value=""/>><or:text key="global.any" def=""/></option>
                                        <c:forEach items="${lateDeliverableTypes}" var="entry">
                                            <option value="${entry.id}" <or:selected value="${entry.id}"/>><or:text key="LateDeliverableType.${fn:replace(entry.name, ' ', '')}" def="${entry.id}"/></option>
                                        </c:forEach>
                                    </select>

                                </td>
                            </tr>
                        </tbody>                
                    </table>
                    <br/>
                    <div align="center">
                        <input type="image"  src="<or:text key='btnSearch.img' />" alt="<or:text key='btnSearch.alt' />" border="0" onclick="submitForm=true;"/>
                        &nbsp;&nbsp;
                        <input type="image"  src="<or:text key='btnClear.img' />" alt="<or:text key='btnClear.alt' />" border="0" onclick="submitForm=false;"/>
                    </div>
                </s:form>

                <c:if test="${not empty showSearchResultsSection}">
                <c:choose>
                    <c:when test="${empty projectMap}">
                        <div align="center">
                            <or:text key="viewLateDeliverables.SearchResults.NoMatch" />
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div align="right">
                        [ <a href="javascript:void(0)" onclick="return expandAll()"><or:text key="global.expandAll" /></a> | <a href="javascript:void(0)" onclick="return collapseAll()"><or:text key="global.collapseAll" /></a> ]
                        </div><br/>

                        <table class="scorecard" style="border-collapse: collapse;" cellpadding="0" cellspacing="0" width="100%">
                            <tbody>
                            <tr>
                                <td class="title" colspan="3"><or:text key="viewLateDeliverables.SearchResults.title" /></td>
                            </tr>

                            <tr>
                                <td class="header" width="580"><or:text key="viewLateDeliverables.SearchResults.column.ProjectName" /></td>
                                <td class="header" nowrap="nowrap"><or:text key="viewLateDeliverables.SearchResults.column.ProjectCategory" /></td>
                                <td class="header" nowrap="nowrap"><or:text key="viewLateDeliverables.SearchResults.column.ProjectStatus" /></td>
                            </tr>

                            <c:forEach items="${projectMap}" var="entry" varStatus="idxProj">
                            <c:set var="projectId" value="${entry.key}" />
                            <c:set var="project" value="${entry.value}" />
                            <tr class="${(idxProj.index % 2 == 0) ? 'light' : 'dark'}">
                                <td class="value">
                                    <a onclick="return expcollHandler(this)" href="javascript:void(0)" id="Out${idxProj.index}" class="Outline">
                                    <img id="Out${idxProj.index}i" class="Outline" border="0" src="/i/or/plus.gif" width="9" height="9" style="margin-right:5px;" />
                                    </a>
                                    <a href="<or:url value='/actions/ViewProjectDetails?pid=${projectId}' />">
                                        <strong>${project.allProperties['Project Name']}</strong> version ${project.allProperties['Project Version']}
                                    </a>
                                </td>

                                <td class="value">${project.projectCategory.name}</td>
                                <td class="value">${project.projectStatus.name}</td>
                            </tr>

                            <tr id="Out${idxProj.index}r" style="display:none">
                                <td colspan="3" class="value">
                                    <table border="1" cellpadding="0" cellspacing="0" width="100%">
                                        <tr>
                                            <td class="header" nowrap="nowrap" width="20%"><or:text key="viewLateDeliverables.SearchResults.innerTable.column.Handle" /></td>
                                            <td class="header" nowrap="nowrap" width="20%"><or:text key="viewLateDeliverables.SearchResults.innerTable.column.Deliverable" /></td>
                                            <td class="header" nowrap="nowrap" width="20%"><or:text key="viewLateDeliverables.SearchResults.innerTable.column.Deadline" /></td>
                                            <td class="header" nowrap="nowrap" width="20%"><or:text key="viewLateDeliverables.SearchResults.innerTable.column.Delay" /></td>
                                            <td class="header" nowrap="nowrap" width="15%"><or:text key="viewLateDeliverables.SearchResults.innerTable.column.Justified" /></td>
                                            <td class="header" nowrap="nowrap" width="5%"><or:text key="viewLateDeliverables.SearchResults.innerTable.column.Edit"/></td>
                                        </tr>
                                        <c:set var="lateDeliverables" value="${groupedLateDeliverables[projectId]}" />
                                        <c:forEach items="${lateDeliverables}" var="lateDeliverable" varStatus="status">
                                        <tr class="${(status.index % 2 == 0) ? 'light' : 'dark'}">
                                            <td class="value" width="20%"><tc-webtag:handle coderId="${orfn:getUserId(lateDeliverable.resourceId)}" context="${orfn:getHandlerContextByCategoryId(project.projectCategory.id)}" /></td>
                                            <c:choose>
                                                <c:when test="${lateDeliverable.type.id == 1}">
                                                    <td class="value" width="20%">${orfn:getDeliverableName(pageContext.request, lateDeliverable.deliverableId)}</td>
                                                </c:when>
                                                <c:otherwise>
                                                    <td class="value" width="20%"><or:text key="LateDeliverableType.RejectedFinalFix" /></td>
                                                </c:otherwise>
                                            </c:choose>
                                            <c:choose>
                                                <c:when test="${lateDeliverable.deadline != null}">
                                                    <td class="value" width="20%">${orfn:displayDate(pageContext.request, lateDeliverable.deadline)}</td>
                                                </c:when>
                                                <c:otherwise>
                                                    <td class="value" width="20%">N/A</td>
                                                </c:otherwise>
                                            </c:choose>
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
                                                <a href="<or:url value='/actions/EditLateDeliverable?late_deliverable_id=${lateDeliverable.id}' />">
                                                    <or:text key="global.Edit"/></a>
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
</html>