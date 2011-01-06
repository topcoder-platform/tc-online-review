<%--
  - Author: FireIce
  - Version: 1.0
  - Since: Online Review Late Deliverables Search Assembly 1.0
  - Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
  -
  - Description: This page displays late deliverables page
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="html" uri="/tags/struts-html" %>
<%@ taglib prefix="bean" uri="/tags/struts-bean" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>
<%@ taglib prefix="tc-webtag" uri="/tags/tc-webtags" %>
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
    <script language="JavaScript" type="text/javascript" src="<html:rewrite href='/js/or/late_deliverable_search.js' />"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript" src="<html:rewrite href='/js/or/expand_collapse.js' />"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript">
        var ajaxSupportUrl = "<html:rewrite page='/ajaxSupport' />";
    </script>
    <script language="JavaScript" type="text/javascript" src="<html:rewrite href='/js/or/ajax1.js' />"><!-- @ --></script>
</head>
<body>
<div align="center">
    <div class="maxWidthBody" align="left">
        <link type="image/x-icon" rel="shortcut icon" href="https://software.topcoder.com/i/favicon.ico">
        <jsp:include page="/includes/inc_header.jsp" />
        <jsp:include page="/includes/project/project_tabs.jsp" />

        <div id="mainMiddleContent">
            <div style="position: relative; width: 100%;">
                <html:form action="/actions/ViewLateDeliverables" onsubmit="return submit_form(this, true);" method="GET">
                    <html:hidden property="method" value="viewLateDeliverables" />
                    <c:if test="${orfn:isErrorsPresent(pageContext.request)}">
                        <div id="globalMesssage">
	                        <div style="color:red;">
	                            <bean:message key="viewLateDeliverables.ValidationFailed" />
	                        </div>
	                        <html:errors property="org.apache.struts.action.GLOBAL_MESSAGE" />
                        </div>
                    </c:if>
					<table class="scorecard" style="border-collapse: collapse;" cellpadding="0" cellspacing="0" width="100%">
						<tbody>
							<tr>
								<td class="title" colspan="4"><bean:message key="viewLateDeliverables.SearchForm.title" /></td>
							</tr>

							<tr class="light">
								<td class="value" nowrap="nowrap" width="120"><b><bean:message key="viewLateDeliverables.ProjectCategory.Label" /></b></td>
								<td class="value" width="360">
							        <html:select styleClass="inputBox" property="project_categories" multiple="true" size="5">
										<html:option key='global.any' value="0" />
                                        <c:forEach items="${projectCategories}" var="category">
                                            <html:option key='ProjectCategory.${fn:replace(category.name, " ", "")}' value="${category.id}" />
                                        </c:forEach>
                                    </html:select>
                                    <div id="project_categories_validation_msg" style="display:none;" class="error"></div>
								</td>

								<td class="value" nowrap="nowrap" width="100"><b><bean:message key="viewLateDeliverables.ProjectStatus.Label" /></b></td>
								<td class="value">
							        <html:select styleClass="inputBox" property="project_statuses" multiple="true" size="5">
							            <html:option key='global.any' value="0" />
                                        <c:forEach var="status" items="${projectStatuses}">
                                            <html:option key='ProjectStatus.${fn:replace(status.name, " ", "")}' value="${status.id}" />
                                        </c:forEach>
                                    </html:select>
                                    <div id="project_statuses_validation_msg" style="display:none;" class="error"></div>
								</td>
							</tr>

							<tr class="dark">
								<td class="value" nowrap="nowrap"><b><bean:message key="viewLateDeliverables.ProjectID.Label" /></b></td>
								<td class="value" colspan="3">
								    <html:text property="project_id" styleClass="inputBox" style="width: 100px;" />
									<span id="project_id_validation_msg" style="display:none;" class="error"></span>
					                <div>
					                    <b><bean:message key="viewLateDeliverables.ProjectID.Tips" /></b>
					                </div>
					                <div id="project_id_serverside_validation" class="error"><html:errors property="project_id" prefix="" suffix="" /></div>
								</td>
							</tr>


							<tr class="light">
								<td class="value" nowrap="nowrap"><b><bean:message key="viewLateDeliverables.DeliverableType.Label" /></b></td>
								<td class="value">
								    <html:select styleClass="inputBox" property="deliverable_types" multiple="true" size="5">
								        <c:forEach var="entry" items="${deliverableTypes}">
								            <html:option key='DeliverableType.${fn:replace(entry.key, " ", "")}' value="${entry.value}" />
                                        </c:forEach>
								    </html:select>
								    <div id="deliverable_types_validation_msg" style="display:none;" class="error"></div>
								</td>

								<td class="value" nowrap="nowrap"><b><bean:message key="viewLateDeliverables.ForgivenStatus.Label" /></b></td>
								<td class="value">
								    <html:select styleClass="inputBox" property="forgiven">
								        <html:option value="Any" />
								        <html:option value="Forgiven" />
								        <html:option value="Not forgiven" />
									</html:select>
								</td>
							</tr>

							<tr class="dark">
								<td class="value" nowrap="nowrap"><b><bean:message key="viewLateDeliverables.LateMemberHandle.Label" /></b></td>
								<td class="value" colspan="3">
								    <html:text property="handle" styleClass="inputBox" style="width: 100px;" />
					                <div>
					                <b><bean:message key="viewLateDeliverables.LateMemberHandle.Tips" /></b>
					                </div>
					                <div id="handle_serverside_validation" class="error"><html:errors property="handle" prefix="" suffix="" /></div>
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
								<td class="header" width="700"><bean:message key="viewLateDeliverables.SearchResults.column.ProjectName" /></td>
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
											<td class="header" nowrap="nowrap"><bean:message key="viewLateDeliverables.SearchResults.innerTable.column.Handle" /></td>
											<td class="header" nowrap="nowrap"><bean:message key="viewLateDeliverables.SearchResults.innerTable.column.Deliverable" /></td>
											<td class="header" nowrap="nowrap"><bean:message key="viewLateDeliverables.SearchResults.innerTable.column.Deadline" /></td>
											<td class="header" nowrap="nowrap"><bean:message key="viewLateDeliverables.SearchResults.innerTable.column.Delay" /></td>
											<td class="header" nowrap="nowrap"><bean:message key="viewLateDeliverables.SearchResults.innerTable.column.Forgiven" /></td>
										</tr>
										<c:set var="lateDeliverables" value="${groupedLateDeliverables[projectId]}" />
										<c:forEach items="${lateDeliverables}" var="lateDeliverable" varStatus="status">
										<tr class="${(status.index % 2 == 0) ? 'light' : 'dark'}">
											<td class="value"><tc-webtag:handle coderId="${orfn:getUserId(pageContext.request, lateDeliverable.resourceId)}" context="${orfn:getHandlerContextByCategoryId(project.projectCategory.id)}" /></td>
											<td class="value">${orfn:getDeliverableName(pageContext.request, lateDeliverable.deliverableId)}</td>
											<td class="value">${orfn:displayDate(pageContext.request, lateDeliverable.deadline)}</td>
											<td class="value">${orfn:displayDelay(lateDeliverable.delay)}</td>
											<td class="value">${lateDeliverable.forgiven ? 'Yes' : 'No'}</td>
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