<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="html" uri="/tags/struts-html" %>
<%@ taglib prefix="bean" uri="/tags/struts-bean" %>

<table class="scorecard" id="resources_tbl" cellpadding="0" width="100%" style="border-collapse: collapse;">
	<tr>
		<td class="title" colspan="5"><bean:message key="editProject.Resources.title" /></td>
	</tr>
	<tr>
		<td class="header"><bean:message key="editProject.Resources.Role"/></td>
		<td class="header"><bean:message key="editProject.Resources.Name"/></td>
		<td class="header"><bean:message key="editProject.Resources.Payment"/></td>
		<td class="header"><bean:message key="editProject.Resources.Paid"/></td>
		<td class="headerC"><!-- @ --></td>
	</tr>
	<c:forEach var="resourceIdx" varStatus="resourceStatus" begin="0" end="${fn:length(projectForm.map['resources_role']) - 1}">
		<tr class="${(resourceStatus.index % 2 == 0) ? 'light' : 'dark'}" style="${projectForm.map['resources_action'][resourceIdx] eq 'delete' ? 'display:none' : ''}">
			<td class="value" nowrap="nowrap">
				<html:select styleClass="inputBox" property="resources_role[${resourceIdx}]"
						style="width:150px;" onchange="onResourceRoleChange(this.parentNode.parentNode);">
					<html:option key="editProject.Resources.SelectRole" value="-1" />
					<c:forEach items="${requestScope.resourceRoles}" var="role">
						<html:option key="ResourceRole.${fn:replace(role.name, ' ', '')}" value="${role.id}" />
					</c:forEach>
				</html:select>
				<html:select styleClass="inputBox" property="resources_phase[${resourceIdx}]" >
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
				<div name="role_validation_msg" class="error" style="display:none"></div>
			</td>
			<td class="value">
				<html:text styleClass="inputBoxName" property="resources_name[${resourceIdx}]" />
				<div name="name_validation_msg" class="error" style="display:none"></div>
				<div class="error"><html:errors property="resources_name[${resourceIdx}]" prefix="" suffix="" /></div>
			</td>
			<td class="value" nowrap="nowrap">
				<html:radio styleId="resourcePaymentOnRadio${resourceIdx}" property="resources_payment[${resourceIdx}]" value="true" /><label
					for="resourcePaymentOnRadio${resourceIdx}">${"$"}</label>
				<html:text styleClass="inputBoxDuration" property="resources_payment_amount[${resourceIdx}]" />
				<html:radio styleId="resourcePaymentOffRadio${resourceIdx}" property="resources_payment[${resourceIdx}]" value="false" /><label
					for="resourcePaymentOffRadio${resourceIdx}"><bean:message key="NotAvailable" /></label>
				<div name="payment_validation_msg" class="error" style="display:none"></div>
			</td>
			<td class="value" nowrap="nowrap">
				<html:select styleClass="inputBox" property="resources_paid[${resourceIdx}]" style="width:120px;">
					<%-- TODO: How to decide wheather Select or N/A is displayed (probably by NewProject attr.) --%>
					<html:option key="Answer.Select" value="" />
					<html:option key="NotApplicable" value="N/A" />
					<html:option key="editProject.Resources.Paid.NotPaid" value="No" />
					<html:option key="editProject.Resources.Paid.Paid" value="Yes" />
				</html:select>
				<div name="paid_validation_msg" class="error" style="display:none"></div>
			</td>
			<td class="valueC" nowrap="nowrap">
				<c:if test="${resourceIdx eq 0}">
					<html:img srcKey="editProject.Resources.AddResource.img" altKey="editProject.Resources.AddResource.alt" onclick="addNewResource();" style="cursor:hand;" />
				</c:if>
				<html:img style="cursor:hand;${(resourceIdx eq 0) ? 'display: none;' : ''}" srcKey="editProject.Resources.DeleteResource.img" altKey="editProject.Resources.DeleteResource.alt" onclick="deleteResource(this.parentNode.parentNode);" />
				<html:hidden property="resources_action[${resourceIdx}]" />
				<html:hidden property="resources_id[${resourceIdx}]" />
			</td>

			<%-- TODO: Iterate through exisitng resources here (for edit page only) --%>
		</tr>
	</c:forEach>
	<tr>
		<td class="lastRowTD" colspan="5"><!-- @ --></td>
	</tr>
</table><br />
