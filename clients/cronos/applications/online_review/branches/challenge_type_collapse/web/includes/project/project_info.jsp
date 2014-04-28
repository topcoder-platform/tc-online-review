<%--
  - Author: TCSASSEMBLER
  - Version: 2.0
  - Copyright (C) 2004 - 2014 TopCoder Inc., All Rights Reserved.
  -
  - Description: This page displays project information.
--%>
<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="or" uri="/or-tags" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>
    <table border="0" width="100%" id="table12" cellspacing="0" cellpadding="0">
        <tr>
            <td valign="top"><img alt="" src="/i/${categoryIconName}" /></td>
            <td width="6%" height="45" valign="middle"><img src="/i/${rootCatalogIcon}" alt="${rootCatalogName}" border="0" /></td>
            <td width="40%"><span class="bodyTitle">${project.allProperties["Project Name"]}</span>
                <c:if test='${!(empty project.allProperties["Project Version"])}'>
                    <font size="4"><or:text key="global.version" />
                        ${project.allProperties["Project Version"]}</font>
                </c:if></td>
            <td width="50%" valign="top" align="right">
                <c:if test="${isAllowedToCompleteAppeals}">
                    <a href="<or:url value='/actions/EarlyAppeals?pid=${project.id}' />"><or:text key="viewProjectDetails.CompleteAppeals" /></a> |
                </c:if>
                <c:if test="${isAllowedToResumeAppeals}">
                    <a href="<or:url value='/actions/EarlyAppeals?pid=${project.id}' />"><or:text key="viewProjectDetails.ResumeAppeals" /></a> |
                </c:if>
                <c:if test="${isAllowedToUnregister}">
                    <a href="<or:url value='/actions/Unregister?pid=${project.id}' />"><or:text key="viewProjectDetails.Unregister" /></a> |
                </c:if>
                <a href="<or:url value='/actions/ViewLateDeliverables?project_id=${project.id}' />"><or:text key="viewProjectDetails.LateDeliverables" /></a> |
                <c:if test="${isAllowedToContactPM}">
                    <a href="<or:url value='/actions/ContactManager?pid=${project.id}' />"><or:text key="viewProjectDetails.ContactPM" /></a> |
                </c:if>
                <a class="breadcrumbLinks"
                    href="${viewContestLink}"><or:text key="viewProjectDetails.ViewContest" /></a> |
                <a class="breadcrumbLinks"
                    href="${forumLink}"><or:text key="viewProjectDetails.DevelopmentForum" /></a></td>
        </tr>
    </table>

    <c:set var="notesText" value='${project.allProperties["Notes"]}' />
    <c:if test='${!(empty notesText)}'>
        <br />
        <table class="scorecard" width="100%" cellpadding="0" cellspacing="0" style="border-collapse:collapse;">
            <tr>
                <td class="title"><or:text key="viewProjectDetails.box.Notes" /></td>
            </tr>
            <tr class="light">
                <td class="value" align="left">${orfn:htmlEncode(notesText)}</td>
            </tr>
            <tr>
                <td class="lastRowTD"><!-- @ --></td>
            </tr>
        </table>
    </c:if>
