<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="html" uri="/tags/struts-html" %>
<%@ taglib prefix="bean" uri="/tags/struts-bean" %>
<%@ taglib prefix="tc-webtag" uri="/tags/tc-webtags" %>
<div style="padding: 11px 6px 9px 0px;">
	<table border="0" cellpadding="0" cellspacing="0" width="100%" id="table1">
		<tr>
			<td>
				<table cellspacing="0" cellpadding="0" border="0">
					<tr valign="middle">
						<td><img src="../i/${categoryIconName}" border="0" /></td>
						<td><img src="../i/${rootCatalogIcon}" alt="${rootCatalogName}" border="0" /></td>
						<td>
							<span class="bodyTitle">${project.allProperties['Project Name']}</span>
							<c:if test="${!(empty project.allProperties['Project Version'])}">
								<font size="4"><bean:message key="global.version" />
									${project.allProperties['Project Version']}</font>
							</c:if>
						</td>
					</tr>
				</table>
			</td>
			<td align="right" valign="top">
				<a href="javascript:showAll();"><bean:message key="global.expandAll" /></a>&#160;|
				<a href="javascript:hideAll();"><bean:message key="global.collapseAll" /></a>
			</td>
		</tr>
	</table>
</div>
<%--  
<c:if test="${reviewType ne 'AutoScreening'}">
&#160;	<c:if test="${reviewType eq 'Screening'}">
		<b><bean:message key="editReview.Screener" /></b>
	</c:if>
	<c:if test="${reviewType eq 'Review'}">
		<b><bean:message key="editReview.Reviewer" /></b>
	</c:if>
	<c:if test="${reviewType eq 'Approval'}">
		<b><bean:message key="editReview.Approver" /></b>
	</c:if>
	<c:if test="${reviewType eq 'Aggregation'}">
		<b><bean:message key="editReview.Aggregator" /></b>
	</c:if>
	<tc-webtag:handle coderId="${authorId}" context="component" />
<br />
</c:if>
--%>	
&#160;<b><bean:message key="editReview.Submission" /></b> ${sid}
<c:if test="${reviewType ne 'Screening' and reviewType ne 'Review'}">
	(<tc-webtag:handle coderId="${submitterId}" context="component" />)
</c:if>	
<br />
&#160;<b><bean:message key="editReview.MyRole" /></b> ${myRole}<br />
