<%--
  - Author: TCSASSEMBLER
  - Version: 2.0
  - Copyright (C) 2014 TopCoder Inc., All Rights Reserved.
  -
  - Description: This page fragment displays the content of links on Project Details screen.
--%>
<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="or" uri="/or-tags" %>
<%@ taglib prefix="tc-webtag" uri="/tags/tc-webtags" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>

<c:if test="${not empty destProjectLinks}">
<table class="scorecard" width="100%" cellpadding="0" cellspacing="0" style="border-collapse:collapse;">
        <tr>
            <td class="title" colspan="2"><or:text key="viewProjectDetails.box.Links" /></td>
        </tr>
        <tr>
            <td class="header"><or:text key="viewProjectDetails.Link.LinkType" /></td>
            <td class="header"><or:text key="viewProjectDetails.Link.Project" /></td>
            
    </tr>
    <c:forEach var="projectLink" items="${destProjectLinks}" varStatus="linkStatus">
    <tr class='${(linkStatus.index % 2 == 0) ? "light" : "dark"}'>
        <c:set var="linkTypeElement" value="${projectLink.type}" />        
        <c:set var="projectElement" value="${projectLink.destProject}" />          
          
          <td class="value" nowrap="nowrap">              
        <c:choose>
           <c:when test="${isAllowedToEditProjects}">
                 <a href="EditProjectLinks?pid=${project.id}">${linkTypeElement.name}</a>
           </c:when>
           <c:otherwise>
                 ${linkTypeElement.name}
           </c:otherwise>
        </c:choose>          
          </td>
          <td class="value" nowrap="nowrap"><a href="<or:url value='/actions/ViewProjectDetails?pid=${projectElement.id}' />">${projectElement.allProperties["Project Name"]} v${projectElement.allProperties["Project Version"]} (${projectElement.projectCategory.name}) (${projectElement.projectStatus.name})</a></td>          
          
    </tr>
    </c:forEach>    
        <tr>
            <td class="lastRowTD" colspan='2'><!-- @ --></td>
        </tr>
    </table><br />
</c:if>

<c:if test="${not empty srcProjectLinks}">
<table class="scorecard" width="100%" cellpadding="0" cellspacing="0" style="border-collapse:collapse;">
        <tr>
            <td class="title" colspan="2"><or:text key="viewProjectDetails.box.LinkTos" /></td>
        </tr>
        <tr>
            <td class="header"><or:text key="viewProjectDetails.LinkTo.Project" /></td>
            <td class="header"><or:text key="viewProjectDetails.LinkTo.LinkType" /></td>
    </tr>
    <c:forEach var="projectLink" items="${srcProjectLinks}" varStatus="linkStatus">
    <tr class='${(linkStatus.index % 2 == 0) ? "light" : "dark"}'>
        <c:set var="projectElement" value="${projectLink.sourceProject}" />
        <c:set var="linkTypeElement" value="${projectLink.type}" />          
          <td class="value" nowrap="nowrap"><a href="<or:url value='/actions/ViewProjectDetails?pid=${projectElement.id}' />">${projectElement.allProperties["Project Name"]} v${projectElement.allProperties["Project Version"]} (${projectElement.projectCategory.name}) (${projectElement.projectStatus.name})</a></td>
          <td class="value" nowrap="nowrap">${linkTypeElement.name}</td>
    </tr>
    </c:forEach>    
        <tr>
            <td class="lastRowTD" colspan='2'><!-- @ --></td>
        </tr>
    </table><br />
</c:if>