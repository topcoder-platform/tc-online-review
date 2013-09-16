<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="html" uri="/tags/struts-html" %>
<%@ taglib prefix="bean" uri="/tags/struts-bean" %>
<%@ taglib prefix="tc-webtag" uri="/tags/tc-webtags" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>

<c:if test="${not empty destProjectLinks}">
<table class="scorecard" width="100%" cellpadding="0" cellspacing="0" style="border-collapse:collapse;">
		<tr>
			<td class="title" colspan="2"><bean:message key="viewProjectDetails.box.Links" /></td>
		</tr>
		<tr>
			<td class="header"><bean:message key="viewProjectDetails.Link.LinkType" /></td>
			<td class="header"><bean:message key="viewProjectDetails.Link.Project" /></td>
			
    </tr>
    <c:forEach var="projectLink" items="${destProjectLinks}" varStatus="linkStatus">
    <tr class='${(linkStatus.index % 2 == 0) ? "light" : "dark"}'>
		<c:set var="linkTypeElement" value="${projectLink.type}" />    	
        <c:set var="projectElement" value="${projectLink.destProject}" />    	  
          
		  <td class="value" nowrap="nowrap">    	  	
        <c:choose>
           <c:when test="${isAllowedToEditProjects}">
           	  <a href="EditProjectLinks.do?method=editProjectLinks&pid=${project.id}">${linkTypeElement.name}</a>
           </c:when>
           <c:otherwise>
           	  ${linkTypeElement.name}
           </c:otherwise>
        </c:choose>    	  
    	  </td>
    	  <td class="value" nowrap="nowrap"><html:link page="/actions/ViewProjectDetails.do?method=viewProjectDetails&pid=${projectElement.id}">${projectElement.allProperties["Project Name"]} v${projectElement.allProperties["Project Version"]} (${projectElement.projectCategory.name}) (${projectElement.projectStatus.name})</html:link></td>    	  
    	  
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
			<td class="title" colspan="2"><bean:message key="viewProjectDetails.box.LinkTos" /></td>
		</tr>
		<tr>
			<td class="header"><bean:message key="viewProjectDetails.LinkTo.Project" /></td>
			<td class="header"><bean:message key="viewProjectDetails.LinkTo.LinkType" /></td>
    </tr>
    <c:forEach var="projectLink" items="${srcProjectLinks}" varStatus="linkStatus">
    <tr class='${(linkStatus.index % 2 == 0) ? "light" : "dark"}'>
        <c:set var="projectElement" value="${projectLink.sourceProject}" />
        <c:set var="linkTypeElement" value="${projectLink.type}" />    	  
    	  <td class="value" nowrap="nowrap"><html:link page="/actions/ViewProjectDetails.do?method=viewProjectDetails&pid=${projectElement.id}">${projectElement.allProperties["Project Name"]} v${projectElement.allProperties["Project Version"]} (${projectElement.projectCategory.name}) (${projectElement.projectStatus.name})</html:link></td>
    	  <td class="value" nowrap="nowrap">${linkTypeElement.name}</td>
    </tr>
    </c:forEach>    
		<tr>
			<td class="lastRowTD" colspan='2'><!-- @ --></td>
		</tr>
	</table><br />
</c:if>