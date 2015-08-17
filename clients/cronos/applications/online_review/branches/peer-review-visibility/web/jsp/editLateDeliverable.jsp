<%--
  - Author: TCSASSEMBLER
  - Version: 2.0
  - Copyright (C) 2011 - 2014 TopCoder Inc., All Rights Reserved.
  -
  - Description: This page displays form for editing a single selected late deliverable
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
    <script language="JavaScript" type="text/javascript" src="/js/or/late_deliverable_search.js"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript" src="/js/or/expand_collapse.js"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript">
        var ajaxSupportUrl = "<or:url value='/ajaxSupport' />";
    </script>
    <script language="JavaScript" type="text/javascript" src="/js/or/ajax1.js"><!-- @ --></script>
</head>
<body>
<div align="center">
    <div class="maxWidthBody" align="left">
        <link type="image/x-icon" rel="shortcut icon" href="https://software.topcoder.com/i/favicon.ico">
        <jsp:include page="/includes/inc_header.jsp" />
        <jsp:include page="/includes/project/project_tabs.jsp" />

        <div id="mainMiddleContent">
            <div style="position: relative; width: 100%;">
                <s:form action="SaveLateDeliverable" 
                           onsubmit="return submitEditLateDeliverableForm(this);" 
                           method="post" namespace="/actions">
                    <input type="hidden" name="late_deliverable_id"  value="<or:fieldvalue field='late_deliverable_id' />" />
                    
                        <div id="globalMesssage"
                             style="display:${orfn:isErrorsPresent(pageContext.request) ? 'block' : 'none'}">
                            <div style="color:red;">
                                <or:text key="viewLateDeliverables.ValidationFailed" />
                            </div>
                            <s:actionerror escape="false" />
                        </div>
                    
                    <c:set var="lateDeliverable" value="${requestScope.lateDeliverable}"/>
                    <c:set var="project" value="${requestScope.project}"/>
                    
                    <c:set var="rowCount" value="0" />
                    <table width="100%" cellspacing="0" cellpadding="0" style="border-collapse: collapse;"
                           class="scorecard">
                        <tbody>
                        <tr>
                            <td colspan="2" class="title"><or:text key="editLateDeliverable.title"/></td>
                        </tr>

                        <tr class="${(rowCount % 2 == 0) ? 'light' : 'dark'}">
                            <td class="valueB"><or:text key="editLateDeliverable.LateDeliverableId.label"/></td>
                            <td width="100%" nowrap="nowrap" class="value"><c:out value="${lateDeliverable.id}"/></td>
                        </tr>
                        <c:set var="rowCount" value="${rowCount + 1}" />

                        <tr class="${(rowCount % 2 == 0) ? 'light' : 'dark'}">
                            <td class="valueB"><or:text key="editLateDeliverable.LateDeliverableType.label"/></td>
                            <td width="100%" nowrap="nowrap" class="value"><c:out value="${lateDeliverable.type.name}"/></td>
                        </tr>
                        <c:set var="rowCount" value="${rowCount + 1}" />

                        <tr class="${(rowCount % 2 == 0) ? 'light' : 'dark'}">
                            <td width="9%" class="valueB">
                                <or:text key="editLateDeliverable.ProjectName.label"/>
                            </td>
                            <td width="91%" nowrap="nowrap" class="value">
                                <a href="<or:url value='/actions/ViewProjectDetails?pid=${project.id}' />">
                                    <strong><c:out value="${project.allProperties['Project Name']}"/></strong> 
                                    <or:text key="global.version"/> 
                                    <c:out value="${project.allProperties['Project Version']}"/>
                                </a>
                            </td>
                        </tr>
                        <c:set var="rowCount" value="${rowCount + 1}" />

                        <tr class="${(rowCount % 2 == 0) ? 'light' : 'dark'}">
                            <td class="valueB"><or:text key="editLateDeliverable.DeliverableType.label"/></td>
                            <td nowrap="nowrap" class="value">
                                <c:out value="${orfn:getDeliverableName(pageContext.request, lateDeliverable.deliverableId)}"/>
                            </td>
                        </tr>
                        <c:set var="rowCount" value="${rowCount + 1}" />

                        <tr class="${(rowCount % 2 == 0) ? 'light' : 'dark'}">
                            <td nowrap="nowrap" class="valueB">
                                <or:text key="editLateDeliverable.LateMember.label"/>
                            </td>
                            <td nowrap="nowrap" class="value">
                                <tc-webtag:handle coderId="${orfn:getUserId(lateDeliverable.resourceId)}" 
                                                  context="${orfn:getHandlerContextByCategoryId(project.projectCategory.id)}" />
                            </td>
                        </tr>
                        <c:set var="rowCount" value="${rowCount + 1}" />

                        <c:if test='${lateDeliverable.deadline ne null}'>
                            <tr class="${(rowCount % 2 == 0) ? 'light' : 'dark'}">
                                <td nowrap="nowrap" class="valueB">
                                    <or:text key="editLateDeliverable.Deadline.label"/>
                                </td>
                                <td nowrap="nowrap" class="value">
                                    <c:choose>
                                        <c:when test="${lateDeliverable.compensatedDeadline ne null}">
                                            <c:out value="${orfn:displayDate(pageContext.request, lateDeliverable.compensatedDeadline)}"/>
                                            (compensated due to the premature end of the previous phase)
                                        </c:when>
                                        <c:otherwise>
                                            <c:out value="${orfn:displayDate(pageContext.request, lateDeliverable.deadline)}"/>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                            <c:set var="rowCount" value="${rowCount + 1}" />
                        </c:if>

                        <c:if test='${lateDeliverable.delay ne null}'>
                            <tr class="${(rowCount % 2 == 0) ? 'light' : 'dark'}">
                                <td nowrap="nowrap" class="valueB">
                                    <or:text key="editLateDeliverable.Delay.label"/>
                                </td>
                                <td nowrap="nowrap" class="value">
                                    <c:out value="${orfn:displayDelay(lateDeliverable.delay)}"/>
                                </td>
                            </tr>
                            <c:set var="rowCount" value="${rowCount + 1}" />
                        </c:if>

                        <tr class="${(rowCount % 2 == 0) ? 'light' : 'dark'}">
                            <td nowrap="nowrap" class="valueB">
                                <or:text key="editLateDeliverable.Justified.label"/>
                            </td>
                            <td nowrap="nowrap" class="value">
                                <c:choose>
                                    <c:when test="${requestScope.isJustifiedEditable}">
                                        <select class="inputBox" name="justified"><c:set var="OR_FIELD_TO_SELECT" value="justified"/>
                                            <option value="false" <or:selected value="false"/>><or:text key="global.answer.No"/></option>
                                            <option value="true" <or:selected value="true"/>><or:text key="global.answer.Yes"/></option>
                                        </select>
                                    </c:when>
                                    <c:otherwise>
                                        <c:choose>
                                            <c:when test="${lateDeliverable.forgiven}">
                                                <or:text key="global.answer.Yes"/>
                                            </c:when>
                                            <c:otherwise><or:text key="global.answer.No"/></c:otherwise>
                                        </c:choose>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                        <c:set var="rowCount" value="${rowCount + 1}" />

                        <tr class="${(rowCount % 2 == 0) ? 'light' : 'dark'}">
                            <td nowrap="nowrap" class="valueB">
                                <or:text key="editLateDeliverable.Explanation.label"/>
                            </td>
                            <td class="value">
                                <c:choose>
                                    <c:when test="${requestScope.isExplanationEditable}">
                                        <textarea rows="5" class="inputTextBox" name="explanation"><or:fieldvalue field="explanation" /></textarea>
                                        <div id="explanation_serverside_validation" class="error">
                                            <s:fielderror escape="false"><s:param>explanation</s:param></s:fielderror>
                                        </div>
                                        <div id="explanation_validation_msg" style="display:none;" class="error"></div>
                                    </c:when>
                                    <c:when test="${lateDeliverable.explanation ne null}">
                                        <c:choose>
                                            <c:when test="${lateDeliverable.explanationDate ne null}">
                                                By <tc-webtag:handle coderId="${orfn:getUserId(lateDeliverable.resourceId)}" 
                                                    context="${orfn:getHandlerContextByCategoryId(project.projectCategory.id)}" />
                                                at <c:out value="${orfn:displayDate(pageContext.request, lateDeliverable.explanationDate)}"/>
                                                <br/><br/>
                                            </c:when>
                                        </c:choose>
                                        ${orfn:htmlEncode(lateDeliverable.explanation)}
                                    </c:when>
                                    <c:otherwise><or:text key="NotAvailable"/></c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                        <c:set var="rowCount" value="${rowCount + 1}" />

                        <tr class="${(rowCount % 2 == 0) ? 'light' : 'dark'}">
                            <td nowrap="nowrap" class="valueB">
                                <or:text key="editLateDeliverable.Response.label"/>
                            </td>
                            <td class="value">
                                <c:choose>
                                    <c:when test="${requestScope.isResponseEditable}">
                                        <textarea rows="5" class="inputTextBox" name="response"><or:fieldvalue field="response" /></textarea>
                                        <div id="response_serverside_validation" class="error">
                                            <s:fielderror escape="false"><s:param>response</s:param></s:fielderror>
                                        </div>
                                        <div id="response_validation_msg" style="display:none;" class="error"></div>
                                    </c:when>
                                    <c:when test="${lateDeliverable.response ne null}">
                                        <c:choose>
                                            <c:when test="${(lateDeliverable.responseUser ne null) and (lateDeliverable.responseDate ne null)}">
                                                By <tc-webtag:handle coderId="${lateDeliverable.responseUser}"
                                                    context="${orfn:getHandlerContextByCategoryId(project.projectCategory.id)}" />
                                                at <c:out value="${orfn:displayDate(pageContext.request, lateDeliverable.responseDate)}"/>
                                                <br/><br/>
                                            </c:when>
                                        </c:choose>
                                        ${orfn:htmlEncode(lateDeliverable.response)}
                                    </c:when>
                                    <c:otherwise><or:text key="NotAvailable"/></c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                        <c:set var="rowCount" value="${rowCount + 1}" />

                        <tr>
                            <td colspan="2" class="lastRowTD"><!-- @ --></td>
                        </tr>
                        </tbody>
                    </table>
                    <br/>
                    <c:if test="${requestScope.isFormSubmittable}">
                        <div align="right">
                            <input type="image" property="saveLateDeliverableBtn" src="<or:text key='btnSaveChanges.img' />" alt="<or:text key='btnSaveChanges.alt' />" border="0"/>
                        </div>
                    </c:if>
                </s:form>
            </div>
        </div>
        <jsp:include page="/includes/inc_footer.jsp" />
    </div>
</div>
</body>
</html>