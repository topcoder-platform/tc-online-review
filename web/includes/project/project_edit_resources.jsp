<%--
  - Author: flexme
  - Version: 1.1
  - Copyright (C) ? - 2013 TopCoder Inc., All Rights Reserved.
  -
  - Description: This page displays project resource edition section
  -
  - Version 1.1 (Online Review - Project Payments Integration Part 3 v1.0) changes: removed "Payment" and "Paid"
  - columns.
--%>
<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="html" uri="/tags/struts-html" %>
<%@ taglib prefix="bean" uri="/tags/struts-bean" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>

<table class="scorecard" id="resources_tbl" cellpadding="0" width="100%" style="border-collapse: collapse;">
	<tr>
		<td class="title" colspan="3"><bean:message key="editProject.Resources.title" /></td>
	</tr>
	<tr>
		<td class="header"><bean:message key="editProject.Resources.Role"/></td>
		<td class="header"><bean:message key="editProject.Resources.Name"/></td>
		<td class="headerC"><!-- @ --></td>
	</tr>
	<c:forEach var="resourceIdx" varStatus="resourceStatus" begin="0" end="${fn:length(projectForm.map['resources_role']) - 1}">
        <c:set var="resourceRoles" value="${resourceStatus.index == 0 ? requestScope.allowedResourceRoles : requestScope.resourceRoles}"/>
        <c:set var="resourceId" value="${projectForm.map['resources_id'][resourceIdx]}"/>
        <c:set var="paid" value="${not empty resourcePaid[resourceId] and resourcePaid[resourceId]}"/>
		<tr class="${(resourceStatus.index % 2 == 0) ? 'light' : 'dark'}" style="${projectForm.map['resources_action'][resourceIdx] eq 'delete' ? 'display:none' : ''}">
			<td class="value" nowrap="nowrap">
				<html:select styleClass="inputBox" property="resources_role[${resourceIdx}]"
                             disabled="${requestScope.trueSubmitters[resourceIdx] or requestScope.trueReviewers[resourceIdx] or paid}"
						style="width:150px;" onchange="onResourceRoleChange(this.parentNode.parentNode);">
					<html:option key="editProject.Resources.SelectRole" value="-1" />
					<c:forEach items="${resourceRoles}" var="role">
                        <c:if test="${role.id eq projectForm.map['resources_role'][resourceIdx] or not orfn:contains(requestScope.disabledResourceRoles, role.id)}">
                            <html:option key="ResourceRole.${fn:replace(role.name, ' ', '')}" value="${role.id}"/>
                        </c:if>
                    </c:forEach>
				</html:select>
				<html:select styleClass="inputBox" property="resources_phase[${resourceIdx}]"
                             disabled="${requestScope.trueSubmitters[resourceIdx] or requestScope.trueReviewers[resourceIdx] or paid}">
					<c:forEach items="${requestScope.resourceRoles}" var="role">
						<c:if test="${role.id eq projectForm.map['resources_role'][resourceIdx] and not empty role.phaseType}">
							<c:forEach var="phaseIdx" begin="1" end="${fn:length(projectForm.map['phase_type'])}">
								<c:if test="${projectForm.map['phase_type'][phaseIdx] eq role.phaseType}">
									<html:option value="${projectForm.map['phase_js_id'][phaseIdx]}">${projectForm.map['phase_number'][phaseIdx]}</html:option>
								</c:if>
							</c:forEach>
						</c:if>
					</c:forEach>
				</html:select>
				<c:if test="${requestScope.trueSubmitters[resourceIdx] or requestScope.trueReviewers[resourceIdx] or paid}">
					<html:hidden property="resources_role[${resourceIdx}]"/>
					<html:hidden property="resources_phase[${resourceIdx}]"/>
					<html:hidden property="resources_name[${resourceIdx}]"/>
				</c:if>
				<div name="role_validation_msg" class="error" style="display:none"></div>
			</td>
			<td class="value">
				<html:text styleClass="inputBoxName" property="resources_name[${resourceIdx}]"
                           disabled="${requestScope.trueSubmitters[resourceIdx] or requestScope.trueReviewers[resourceIdx] or paid}"/>
				<div name="name_validation_msg" class="error" style="display:none"></div>
				<div class="error"><html:errors property="resources_name[${resourceIdx}]" prefix="" suffix="" /></div>
			</td>
			<td class="valueC" nowrap="nowrap">
				<c:if test="${resourceIdx eq 0}">
					<html:img srcKey="editProject.Resources.AddResource.img" altKey="editProject.Resources.AddResource.alt" onclick="addNewResource();" style="cursor:hand;" />
				</c:if>
				<c:if test="${not (requestScope.trueSubmitters[resourceIdx] or requestScope.trueReviewers[resourceIdx] or paid)}">
					<html:img style="cursor:hand;${(resourceIdx eq 0) ? 'display: none;' : ''}"
					srcKey="editProject.Resources.DeleteResource.img"
					altKey="editProject.Resources.DeleteResource.alt"
					onclick="deleteResource(this.parentNode.parentNode);"/>
				</c:if>
				<html:hidden property="resources_action[${resourceIdx}]" />
				<html:hidden property="resources_id[${resourceIdx}]" />
			</td>

			<%-- TODO: Iterate through exisitng resources here (for edit page only) --%>
		</tr>
	</c:forEach>
	<tr>
		<td class="lastRowTD" colspan="3"><!-- @ --></td>
	</tr>
</table><br />
