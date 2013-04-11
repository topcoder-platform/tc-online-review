<%--
  - Author: TCSASSEMBLER, flexme
  - Version: 1.1
  - Copyright (C)  - 2013 TopCoder Inc., All Rights Reserved.
  -
  - Version 1.1 (Online Review - Project Payments Integration Part 1 v1.0 ) changes: Add "Reopen" link.
--%>
<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="html" uri="/tags/struts-html" %>
<%@ taglib prefix="bean" uri="/tags/struts-bean" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>
<%@ taglib prefix="tc-webtag" uri="/tags/tc-webtags" %>
<c:set var="toPDF" value="${param.pdf eq 'true'}"/>
<c:if test="${param.showFillScorecardLink}">
<script language="javascript" type="text/javascript">
    <!--
	function fillScorecard() {
		if (confirm("<bean:message key="global.fillScorecardConfirmation" />")) {
			var scores = document.getElementsByTagName("select");
			for (var i = 0; i < scores.length; i++) {
				if (scores[i].getAttribute("name").indexOf("answer[") == 0) {
					if (scores[i].selectedIndex == 0) {
						var options = scores[i].options;
						for (var j = 1; j < options.length ; j++) {
							if (options[j].value.indexOf("/") >= 0) {
								scores[i].selectedIndex = scores[i].options.length - 1;
								break;
							} else if (options[j].value == "1") {
								scores[i].selectedIndex = j;
								break;
							}
						}
					}
				}
			}
		}
	}
	 // -->
    </script>
</c:if>

<div style="padding: 11px 0px 9px 0px;">
	<table width="100%" cellpadding="0" cellspacing="0" border="0">
		<tr>
			<td>
				<table cellspacing="0" cellpadding="0" border="0">
					<tr valign="middle">
						<td><img src="/i/${categoryIconName}" border="0" /></td>
						<td><img src="/i/${rootCatalogIcon}" alt="${rootCatalogName}" border="0" /></td>
						<td>
							<span class="bodyTitle">${orfn:htmlEncode(project.allProperties['Project Name'])}</span>
							<c:if test="${!(empty project.allProperties['Project Version'])}">
								<font size="4"><bean:message key="global.version" />
									${orfn:htmlEncode(project.allProperties['Project Version'])}</font>
							</c:if>
						</td>
					</tr>
				</table>
			</td>
			<c:if test="${not noExpandCollapse}" >
				<td align="right" valign="top">
                    <c:if test="${canReopenScorecard}">
                        <html:link page="/actions/ReopenScorecard.do?method=reopenScorecard&rid=${review.id}"><bean:message key="editReview.ReopenScorecard" /></html:link>&#160;|
                    </c:if>
					<c:if test="${canEditScorecard}">
						<html:link page="/actions/Edit${reviewType}.do?method=edit${reviewType}&rid=${review.id}"><bean:message key="editReview.EditScorecard" /></html:link>&#160;|
					</c:if>
					<c:if test="${param.showFillScorecardLink}">
						<a href="javascript:fillScorecard();"><bean:message key="global.fillScorecard" /></a> |
					</c:if>
					<a href="javascript:showAll();"><bean:message key="global.expandAll" /></a> |
					<a href="javascript:hideAll();"><bean:message key="global.collapseAll" /></a>
				</td>
			</c:if>
		</tr>
	</table>
</div>
<c:if test="${reviewType ne 'AutoScreening' and reviewType ne 'CompositeReview'}">
	<c:if test="${reviewType eq 'Screening'}">
		&#160;<b><bean:message key="editReview.Screener" /></b>
	</c:if>
	<c:if test="${reviewType eq 'Review'}">
		&#160;<b><bean:message key="editReview.Reviewer" /></b>
	</c:if>
	<c:if test="${reviewType eq 'Approval'}">
		&#160;<b><bean:message key="editReview.Approver" /></b>
	</c:if>
	<c:if test="${reviewType eq 'Aggregation' or reviewType eq 'AggregationReview' or reviewType eq 'FinalReview'}">
		&#160;<b><bean:message key="editReview.Aggregator" /></b>
	</c:if>
	<c:if test="${not empty authorId}">
		<tc-webtag:handle coderId="${authorId}" context="${orfn:getHandlerContext(pageContext.request)}" />
	</c:if>
	<br />
</c:if>
&#160;<b><bean:message key="editReview.Submission" /></b> ${sid}
<c:if test="${not empty submitterId and reviewType ne 'Screening' and reviewType ne 'Review' and reviewType ne 'Checkpoint Screening' and reviewType ne 'Checkpoint Review' and not toPDF}">
	(<tc-webtag:handle coderId="${submitterId}" context="${orfn:getHandlerContext(pageContext.request)}" />)
</c:if>
<br />

<c:if test="${not empty modificationDate}">
	&#160;<b><bean:message key="editReview.ModificationDate" /></b>
	<c:out value="${orfn:displayDate(pageContext.request, modificationDate)}"/><br />
</c:if>

&#160;<b><bean:message key="editReview.MyRole" /></b> ${myRole}<br />