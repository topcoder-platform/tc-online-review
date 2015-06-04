<%--
  - Author: TCSASSEMBLER
  - Version: 2.0
  - Copyright (C) 2013-2014 TopCoder Inc., All Rights Reserved.
  -
  - Description: This page displays project resource edition section.
--%>
<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="or" uri="/or-tags" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>

<table class="scorecard" id="resources_tbl" cellpadding="0" width="100%" style="border-collapse: collapse;">
    <tr>
        <td class="title" colspan="3"><or:text key="editProject.Resources.title" /></td>
    </tr>
    <tr>
        <td class="header"><or:text key="editProject.Resources.Role"/></td>
        <td class="header"><or:text key="editProject.Resources.Name"/></td>
        <td class="headerC"><!-- @ --></td>
    </tr>
    <c:forEach var="resourceIdx" varStatus="resourceStatus" begin="0" end="${fn:length(projectForm.map['resources_role']) - 1}">
        <c:set var="resourceRoles" value="${resourceStatus.index == 0 ? requestScope.allowedResourceRoles : requestScope.resourceRoles}"/>
        <c:set var="resourceId" value="${projectForm.map['resources_id'][resourceIdx]}"/>
        <c:set var="paid" value="${not empty resourcePaid[resourceId] and resourcePaid[resourceId]}"/>
        <tr class="${(resourceStatus.index % 2 == 0) ? 'light' : 'dark'}" style="${projectForm.map['resources_action'][resourceIdx] eq 'delete' ? 'display:none' : ''}">
            <td class="value" nowrap="nowrap">
                <select class="inputBox" name="resources_role[${resourceIdx}]"
                             <c:if test="${requestScope.trueSubmitters[resourceIdx] or requestScope.trueReviewers[resourceIdx] or paid}">
                             disabled</c:if>
                        style="width:150px;" onchange="onResourceRoleChange(this.parentNode.parentNode);"><c:set var="OR_FIELD_TO_SELECT" value="resources_role[${resourceIdx}]"/>
                    <option  value="-1"  <or:selected value="-1"/>><or:text key="editProject.Resources.SelectRole" /></option>
                    <c:forEach items="${resourceRoles}" var="role">
                        <c:if test="${role.id eq projectForm.map['resources_role'][resourceIdx] or not orfn:contains(requestScope.disabledResourceRoles, role.id)}">
                            <option  value="${role.id}" <or:selected value="${role.id}"/>><or:text key="ResourceRole.${fn:replace(role.name, ' ', '')}" def="${role.name}" /></option>
                        </c:if>
                    </c:forEach>
                </select>
                <select class="inputBox" name="resources_phase[${resourceIdx}]"
                             <c:if test="${requestScope.trueSubmitters[resourceIdx] or requestScope.trueReviewers[resourceIdx] or paid}">disabled</c:if>>
                             <c:set var="OR_FIELD_TO_SELECT" value="resources_phase[${resourceIdx}]"/>
                    <c:forEach items="${requestScope.resourceRoles}" var="role">
                        <c:if test="${role.id eq projectForm.map['resources_role'][resourceIdx] and not empty role.phaseType}">
                            <c:forEach var="phaseIdx" begin="1" end="${fn:length(projectForm.map['phase_type'])}">
                                <c:if test="${projectForm.map['phase_type'][phaseIdx] eq role.phaseType}">
                                    <option value="${projectForm.map['phase_js_id'][phaseIdx]}" <or:selected value="${projectForm.map['phase_js_id'][phaseIdx]}"/>>${projectForm.map['phase_number'][phaseIdx]}</option>
                                </c:if>
                            </c:forEach>
                        </c:if>
                    </c:forEach>
                </select>
                <c:if test="${requestScope.trueSubmitters[resourceIdx] or requestScope.trueReviewers[resourceIdx] or paid}">
                    <input type="hidden" name="resources_role[${resourceIdx}]" value="<or:fieldvalue field='resources_role[${resourceIdx}]' />" />
                    <input type="hidden" name="resources_phase[${resourceIdx}]" value="<or:fieldvalue field='resources_phase[${resourceIdx}]' />" />
                    <input type="hidden" name="resources_name[${resourceIdx}]" value="<or:fieldvalue field='resources_name[${resourceIdx}]' />" />
                </c:if>
                <div name="role_validation_msg" class="error" style="display:none"></div>
            </td>
            <td class="value">
                <input type="text" class="inputBoxName" name="resources_name[${resourceIdx}]"
                           <c:if test="${requestScope.trueSubmitters[resourceIdx] or requestScope.trueReviewers[resourceIdx] or paid}">disabled</c:if> 
                           value="<or:fieldvalue field='resources_name[${resourceIdx}]' />" />
                <div name="name_validation_msg" class="error" style="display:none"></div>
                <div class="error"><s:fielderror escape="false"><s:param>resources_name[${resourceIdx}]</s:param></s:fielderror></div>
            </td>
            <td class="valueC" nowrap="nowrap">
                <c:if test="${resourceIdx eq 0}">
                    <img src="<or:text key='editProject.Resources.AddResource.img' />" alt="<or:text key='editProject.Resources.AddResource.alt' />" onclick="addNewResource();" style="cursor:hand;" />
                </c:if>
                <c:if test="${not (requestScope.trueSubmitters[resourceIdx] or requestScope.trueReviewers[resourceIdx] or paid)}">
                    <img style="cursor:hand;${(resourceIdx eq 0) ? 'display: none;' : ''}"
                    src="<or:text key='editProject.Resources.DeleteResource.img' />"
                    alt="<or:text key='editProject.Resources.DeleteResource.alt' />"
                    onclick="deleteResource(this.parentNode.parentNode);"/>
                </c:if>
                <input type="hidden" name="resources_action[${resourceIdx}]"  value="<or:fieldvalue field='resources_action[${resourceIdx}]' />" />
                <input type="hidden" name="resources_id[${resourceIdx}]"  value="<or:fieldvalue field='resources_id[${resourceIdx}]' />" />
            </td>

            <%-- TODO: Iterate through exisitng resources here (for edit page only) --%>
        </tr>
    </c:forEach>
    <tr>
        <td class="lastRowTD" colspan="3"><!-- @ --></td>
    </tr>
</table><br />
