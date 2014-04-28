<%--
  - Author: TCSASSEMBLER
  - Version: 2.0
  - Copyright (C) 2005-2014 TopCoder Inc., All Rights Reserved.
  -
  - Description: This page fragment renders the details on selected project.
--%>
<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="or" uri="/or-tags" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>

    <table class="scorecard" style="border-collapse:collapse;" cellpadding="0" cellspacing="0" width="100%">
        <tr>
            <td class="title" colspan="2"><or:text key="viewProjectDetails.ProjectDetails" /></td>
        </tr>
        <c:set var="rowIndex" value="0" />
        <c:if test="${isAllowedToViewSVNLink}">
            <tr class='${(rowIndex % 2 == 0) ? "light" : "dark"}'>
                <td class="value" width="15%" nowrap="nowrap"><b><or:text key="viewProjectDetails.SVNModule" /></b></td>
                <td class="value" width="100%">
                    <a href='${fn:escapeXml(project.allProperties["SVN Module"])}'>${orfn:htmlEncode(project.allProperties["SVN Module"])}</a></td>
            </tr>
            <c:set var="rowIndex" value="${rowIndex+1}" />
        </c:if>
        <tr class='${(rowIndex % 2 == 0) ? "light" : "dark"}'>
                <td class="value" width="15%" nowrap="nowrap"><b>Type:</b></td>
                <td class="value" width="100%">${projectType}</td>
        </tr><c:set var="rowIndex" value="${rowIndex+1}" />
        <tr class='${(rowIndex % 2 == 0) ? "light" : "dark"}'>
                <td class="value" width="15%" nowrap="nowrap"><b>Category:</b></td>
                <td class="value" width="100%">${projectCategory}</td>
        </tr><c:set var="rowIndex" value="${rowIndex+1}" />
        <tr class='${(rowIndex % 2 == 0) ? "light" : "dark"}'>
                <td class="value" width="15%" nowrap="nowrap"><b>This contest is part of the Digital Run:</b></td>
                <td class="value" width="100%">${projectDRFlag}</td>
        </tr><c:set var="rowIndex" value="${rowIndex+1}" />
        <tr class='${(rowIndex % 2 == 0) ? "light" : "dark"}'>
                <td class="value" width="15%" nowrap="nowrap"><b>DR Points:</b></td>
                <td class="value" width="100%">${projectDRFlag=="Yes"?orfn:displayPaymentAmt(pageContext.request, projectDRP):""}</td>
        </tr><c:set var="rowIndex" value="${rowIndex+1}" />
        <c:if test="${isAllowedToViewAutopilotStatus}">
            <c:set var="autopilotStatus" value="${project.allProperties['Autopilot Option']}" />
            <c:if test='${autopilotStatus != "On" and autopilotStatus != "Off"}'>
                <c:set var="autopilotStatus" value="Off" />
            </c:if>
            <tr class='${(rowIndex % 2 == 0) ? "light" : "dark"}'>
                <td class="value" width="15%" nowrap="nowrap"><b><or:text key="viewProjectDetails.AutopilotStatus" /></b></td>
                <td class="value" width="100%"><or:text key="global.option.${autopilotStatus}" /></td>
            </tr>
            <c:set var="rowIndex" value="${rowIndex+1}" />
        </c:if>
        <c:forEach items="${scorecardTemplates}" var="scorecard" varStatus="index">
            <tr class='${(rowIndex % 2 == 0) ? "light" : "dark"}'>
                <td class="value" width="15%" nowrap="nowrap">
                    <b><or:text key='ScorecardType.${fn:replace(scorecard.scorecardType.name, " ", "")}.scorecard' />:</b></td>
                <td class="value" width="100%">
                    <a href="${requestScope.scorecardLinks[index.index]}">
                    ${orfn:htmlEncode(scorecard.name)}
                    <or:text key="global.version.shortened"/>${orfn:htmlEncode(scorecard.version)}
                    </a>
                </td>
            </tr>
            <c:set var="rowIndex" value="${rowIndex+1}" />
        </c:forEach>
        <c:if test="${isAdmin}">
            <tr class='${(rowIndex % 2 == 0) ? "light" : "dark"}'>
                    <td class="value" width="15%" nowrap="nowrap"><b><or:text key="viewProjectDetails.BillingProject" /></b></td>
                    <td class="value" width="100%">${billingProject}</td>
            </tr><c:set var="rowIndex" value="${rowIndex+1}" />
        </c:if>
        <c:if test="${not empty requestScope.cockpitProject and requestScope.isAllowedToViewCockpitProjectName}">
            <tr class='${(rowIndex % 2 == 0) ? "light" : "dark"}'>
                <td class="value" width="15%" nowrap="nowrap"><b><or:text key="viewProjectDetails.CockpitProject" /></b></td>
                <td class="value" width="100%">
                                    <a href="${requestScope.cockpitProjectLink}">${requestScope.cockpitProject}</a>
                                </td>
            </tr><c:set var="rowIndex" value="${rowIndex+1}" />
        </c:if>
        <tr class='${(rowIndex % 2 == 0) ? "light" : "dark"}'>
            <td class="value" width="15%" nowrap="nowrap"><b><or:text key="viewProjectDetails.ProjectStatus" /></b></td>
            <td class="value" width="100%">${projectStatus}</td>
        </tr><c:set var="rowIndex" value="${rowIndex+1}" />
        <tr>
            <td class="lastRowTD" colspan="2"><!-- @ --></td>
        </tr>
    </table><br />
