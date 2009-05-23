<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="html" uri="/tags/struts-html" %>
<%@ taglib prefix="bean" uri="/tags/struts-bean" %>

<c:if test="${requestScope.isAllowedToEditProjects}">
    <c:set var="links" value="${requestScope.projectToLinks}"/>
    <table width="100%" cellspacing="0" cellpadding="0" style="border-collapse: collapse;" class="scorecard">
        <tbody>
        <tr>
            <td colspan="2" class="title"><bean:message key="projectLinks.title"/></td>
        </tr>
        <tr>
            <td class="header" width="50%"><b><bean:message key="projectLinks.tblHeader.project"/></b></td>
            <td class="header" width="50%"><b><bean:message key="projectLinks.tblHeader.linkType"/></b></td>
        </tr>

        <c:forEach items="${links}" var="link" varStatus="index">
            <tr class="${index.index mod 2 ne 0 ? 'dark' : 'light'}">
                <td nowrap="nowrap" class="value">
                    <html:link page="/actions/ViewProjectDetails.do?method=viewProjectDetails&amp;pid=${link.targetProjectId}">
                        <c:out value="${link.targetProjectName}"/>
                        <bean:message key="global.version.shortened"/><c:out value="${link.targetProjectVersion}"/></html:link>                
                </td>
                <td nowrap="nowrap" class="value">
                    <html:link page="/actions/EditProjectLinks.do?method=editProjectLinks&pid=${link.sourceProjectId}">
                        <c:out value="${link.type.name}"/></html:link>                
                </td>
            </tr>
        </c:forEach>
        <tr>
            <td colspan="2" class="lastRowTD"><!-- @ --></td>
        </tr>
        </tbody>
    </table>
    <br/>
</c:if>
