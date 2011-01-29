<%--
  - Author: isv
  - Version: 1.0
  - Since: Online Review Late Deliverables Edit Assembly 1.0
  - Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
  -
  - Description: This page displays form for editing a single selected late deliverable
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
                <html:form action="/actions/SaveLateDeliverable" 
                           onsubmit="return submitEditLateDeliverableForm(this);" 
                           method="post" styleId="EditLateDeliverableForm">
                    <html:hidden property="method" value="saveLateDeliverable" />
                    <html:hidden property="late_deliverable_id" />
                    
                        <div id="globalMesssage"
                             style="display:${orfn:isErrorsPresent(pageContext.request) ? 'block' : 'none'}">
                            <div style="color:red;">
                                <bean:message key="viewLateDeliverables.ValidationFailed" />
                            </div>
                            <html:errors property="org.apache.struts.action.GLOBAL_MESSAGE" />
                        </div>
                    
                    <c:set var="lateDeliverable" value="${requestScope.lateDeliverable}"/>
                    <c:set var="project" value="${requestScope.project}"/>
                    
                    <table width="100%" cellspacing="0" cellpadding="0" style="border-collapse: collapse;"
                           class="scorecard">
                        <tbody>
                        <tr>
                            <td colspan="2" class="title"><bean:message key="editLateDeliverable.title"/></td>
                        </tr>


                        <tr class="light">
                            <td class="valueB"><bean:message key="editLateDeliverable.LateDeliverableId.label"/></td>
                            <td width="100%" nowrap="nowrap" class="value"><c:out value="${lateDeliverable.id}"/></td>
                        </tr>
                        <tr class="dark">
                            <td width="9%" class="valueB">
                                <bean:message key="editLateDeliverable.ProjectName.label"/>
                            </td>
                            <td width="91%" nowrap="nowrap" class="value">
                                <html:link page="/actions/ViewProjectDetails.do?method=viewProjectDetails&amp;pid=${project.id}">
                                    <strong><c:out value="${project.allProperties['Project Name']}"/></strong> 
                                    <bean:message key="global.version"/> 
                                    <c:out value="${project.allProperties['Project Version']}"/>
                                </html:link>
                            </td>
                        </tr>
                        <tr class="light">
                            <td class="valueB"><bean:message key="editLateDeliverable.DeliverableType.label"/></td>
                            <td nowrap="nowrap" class="value">
                                <c:out value="${orfn:getDeliverableName(pageContext.request, lateDeliverable.deliverableId)}"/>
                            </td>
                        </tr>
                        <tr class="dark">
                            <td nowrap="nowrap" class="valueB">
                                <bean:message key="editLateDeliverable.LateMember.label"/>
                            </td>
                            <td nowrap="nowrap" class="value">
                                <tc-webtag:handle coderId="${orfn:getUserId(pageContext.request, lateDeliverable.resourceId)}" 
                                                  context="${orfn:getHandlerContextByCategoryId(project.projectCategory.id)}" />
                            </td>
                        </tr>
                        <tr class="light">
                            <td nowrap="nowrap" class="valueB">
                                <bean:message key="editLateDeliverable.Deadline.label"/>
                            </td>
                            <td nowrap="nowrap" class="value">
                                <c:out value="${orfn:displayDate(pageContext.request, lateDeliverable.deadline)}"/>
                            </td>
                        </tr>
                        <tr class="dark">
                            <td nowrap="nowrap" class="valueB">
                                <bean:message key="editLateDeliverable.Delay.label"/>
                            </td>
                            <td nowrap="nowrap" class="value">
                                <c:out value="${orfn:displayDelay(lateDeliverable.delay)}"/>
                            </td>
                        </tr>
                        <tr class="light">
                            <td nowrap="nowrap" class="valueB">
                                <bean:message key="editLateDeliverable.Forgiven.label"/>
                            </td>
                            <td nowrap="nowrap" class="value">
                                <c:choose>
                                    <c:when test="${requestScope.isForgivenEditable}">
                                        <html:select styleClass="inputBox" property="forgiven">
                                            <html:option value="false"><bean:message key="global.answer.No"/></html:option>
                                            <html:option value="true"><bean:message key="global.answer.Yes"/></html:option>
                                        </html:select>
                                    </c:when>
                                    <c:otherwise>
                                        <c:choose>
                                            <c:when test="${lateDeliverable.forgiven}">
                                                <bean:message key="global.answer.Yes"/>
                                            </c:when>
                                            <c:otherwise><bean:message key="global.answer.No"/></c:otherwise>
                                        </c:choose>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>

                        <tr class="dark">
                            <td nowrap="nowrap" class="valueB">
                                <bean:message key="editLateDeliverable.Explanation.label"/>
                            </td>
                            <td class="value">
                                <c:choose>
                                    <c:when test="${requestScope.isExplanationEditable}">
                                        <html:textarea rows="5" styleClass="inputTextBox" property="explanation"/>
                                        <div id="explanation_serverside_validation" class="error">
                                            <html:errors property="explanation" prefix="" suffix="" />
                                        </div>
                                        <div id="explanation_validation_msg" style="display:none;" class="error"></div>
                                    </c:when>
                                    <c:when test="${lateDeliverable.explanation ne null}">
                                        ${orfn:htmlEncode(lateDeliverable.explanation)}
                                    </c:when>
                                    <c:otherwise><bean:message key="NotAvailable"/></c:otherwise>
                                </c:choose>
                            </td>
                        </tr>

                        <tr class="light">
                            <td nowrap="nowrap" class="valueB">
                                <bean:message key="editLateDeliverable.Response.label"/>
                            </td>
                            <td nowrap="nowrap" class="value">
                                <c:choose>
                                    <c:when test="${requestScope.isResponseEditable}">
                                        <html:textarea rows="5" styleClass="inputTextBox" property="response"/>
                                        <div id="response_serverside_validation" class="error">
                                            <html:errors property="response" prefix="" suffix="" />
                                        </div>
                                        <div id="response_validation_msg" style="display:none;" class="error"></div>
                                    </c:when>
                                    <c:when test="${lateDeliverable.response ne null}">
                                        ${orfn:htmlEncode(lateDeliverable.response)}
                                    </c:when>
                                    <c:otherwise><bean:message key="NotAvailable"/></c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2" class="lastRowTD"><!-- @ --></td>
                        </tr>
                        </tbody>
                    </table>
                    <br/>
                    <c:if test="${requestScope.isFormSubmittable}">
                        <div align="right">
                            <html:image srcKey="btnSaveChanges.img" altKey="btnSaveChanges.alt" border="0"/>
                        </div>
                    </c:if>
                </html:form>
            </div>
        </div>
        <jsp:include page="/includes/inc_footer.jsp" />
    </div>
</div>
</body>
</html:html>