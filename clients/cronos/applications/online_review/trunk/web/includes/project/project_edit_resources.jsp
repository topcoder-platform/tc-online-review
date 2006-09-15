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
	<c:forEach var="resourceIdx" begin="0" end="${fn:length(projectForm.map['resources_role']) - 1}">
		<tr class="light">
			<td class="value" nowrap="nowrap">
				<html:select styleClass="inputBox" property="resources_role[${resourceIdx}]" style="width:150px;">
					<html:option key="editProject.Resources.SelectRole" value="-1" />
					<c:forEach items="${requestScope.resourceRoles}" var="role">
						<html:option key="ResourceRole.${fn:replace(role.name, ' ', '')}" value="${role.id}" />
					</c:forEach>
				</html:select>
			</td>
			<td class="value">
				<html:text styleClass="inputBoxName" property="resources_name[${resourceIdx}]" />
			</td>
			<td class="value" nowrap="nowrap">
				<html:radio property="resources_payment[${resourceIdx}]" value="true" />${"$"}
				<html:text styleClass="inputBoxDuration" property="resources_payment_amount[${resourceIdx}]" />
				<html:radio property="resources_payment[${resourceIdx}]" value="false" /><bean:message key="NotAvailable" />									
			</td>
			<td class="value" nowrap="nowrap">
				<html:select styleClass="inputBox" property="resources_paid[${resourceIdx}]" style="width:120px;">
					<%-- TODO: How to decide wheather Select or N/A is displayed (probably by NewProject attr.) --%>
					<html:option key="Answer.Select" value="" />
					<html:option key="NotApplicable" value="N/A" />
					<html:option key="editProject.Resources.Paid.NotPaid" value="No" />
					<html:option key="editProject.Resources.Paid.Paid" value="Yes" />
				</html:select>
			</td>
			<td class="valueC" nowrap="nowrap">
				<html:img srcKey="editProject.Resources.AddResource.img" altKey="editProject.Resources.AddResource.alt" onclick="addNewResource();" />
				<html:img style="display: none;" srcKey="editProject.Resources.DeleteResource.img" altKey="editProject.Resources.DeleteResource.alt" onclick="deleteResource(this.parentNode.parentNode);" />
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
