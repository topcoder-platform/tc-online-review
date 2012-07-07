<%--
  - Author: pulky, FireIce
  - Version: 1.3
  - Copyright (C) 2004 - 2010 TopCoder Inc., All Rights Reserved.
  -
  - Description: This page displays project information
  -
  - Version 1.1 (Configurable Contest Terms Release Assembly v1.0) changes: added Unregistration link.
  - Version 1.2 (Appeals Early Completion Release Assembly 1.0) changes: added "appeals completed" / "resume appeals" links.
  - Version 1.3 (Online Review Late Deliverables Search Assembly 1.0) changes: added "Late Deliverables" link.
--%>
<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="bean" uri="/tags/struts-bean" %>
<%@ taglib prefix="html" uri="/tags/struts-html" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>
    <table border="0" width="100%" id="table12" cellspacing="0" cellpadding="0">
        <tr>
            <td valign="top"><html:img alt="" src="/i/${categoryIconName}" /></td>
            <td width="6%" height="45" valign="middle"><html:img src="/i/${rootCatalogIcon}" alt="${rootCatalogName}" border="0" /></td>
            <td width="40%"><span class="bodyTitle">${project.allProperties["Project Name"]}</span>
                <c:if test='${!(empty project.allProperties["Project Version"])}'>
                    <font size="4"><bean:message key="global.version" />
                        ${project.allProperties["Project Version"]}</font>
                </c:if></td>
            <td width="50%" valign="top" align="right">
                <c:if test="${isAllowedToCompleteAppeals}">
                    <html:link page="/actions/EarlyAppeals.do?method=earlyAppeals&amp;pid=${project.id}"><bean:message key="viewProjectDetails.CompleteAppeals" /></html:link> |
                </c:if>
                <c:if test="${isAllowedToResumeAppeals}">
                    <html:link page="/actions/EarlyAppeals.do?method=earlyAppeals&amp;pid=${project.id}"><bean:message key="viewProjectDetails.ResumeAppeals" /></html:link> |
                </c:if>
                <c:if test="${isAllowedToUnregister}">
                    <html:link page="/actions/Unregister.do?method=unregister&amp;pid=${project.id}"><bean:message key="viewProjectDetails.Unregister" /></html:link> |
                </c:if>
                <html:link page="/actions/ViewLateDeliverables.do?method=viewLateDeliverables&amp;project_id=${project.id}"><bean:message key="viewProjectDetails.LateDeliverables" /></html:link> |
                <c:if test="${isAllowedToContactPM}">
                    <html:link page="/actions/ContactManager.do?method=contactManager&amp;pid=${project.id}"><bean:message key="viewProjectDetails.ContactPM" /></html:link> |
                </c:if>
                <a class="breadcrumbLinks"
                    href="${viewContestLink}"><bean:message key="viewProjectDetails.ViewContest" /></a> |
                <a class="breadcrumbLinks"
                    href="${forumLink}"><bean:message key="viewProjectDetails.DevelopmentForum" /></a></td>
        </tr>
    </table>

    <c:set var="notesText" value='${project.allProperties["Notes"]}' />
    <c:if test='${!(empty notesText)}'>
        <br />
        <table class="scorecard" width="100%" cellpadding="0" cellspacing="0" style="border-collapse:collapse;">
            <tr>
                <td class="title"><bean:message key="viewProjectDetails.box.Notes" /></td>
            </tr>
            <tr class="light">
                <td class="value" align="left">${orfn:htmlEncode(notesText)}</td>
            </tr>
            <tr>
                <td class="lastRowTD"><!-- @ --></td>
            </tr>
        </table>
    </c:if>
