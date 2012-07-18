<%--
  - Author: George1, real_vg, isv
  - Version: 1.2
  - Copyright (C) 2005 - 2012 TopCoder Inc., All Rights Reserved.
  -
  - Description: This page renders the review scorecard question.
  -
  - Version 1.2 (Contest Results Export 2 assembly) changes: Updated the page to display differently in case conversion
  - to PDF is requested
--%>
<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="html" uri="/tags/struts-html" %>
<%@ taglib prefix="bean" uri="/tags/struts-bean" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>
<c:set var="toPDF" value="${param.pdf eq 'true'}"/>

    <td class="value pdf_section" width="100%">
		<div class="${toPDF ? 'hideText' : 'showText'}" id="shortQ_${itemIdx}">
			<a href="javascript:toggleDisplay('shortQ_${itemIdx}');toggleDisplay('longQ_${itemIdx}');" class="statLink"><html:img src="/i/or/plus.gif" altKey="global.plus.alt" border="0" /></a>
			<b><bean:message key="editReview.Question.title" /> ${groupStatus.index + 1}.${sectionStatus.index + 1}.${questionStatus.index + 1}</b>
			${orfn:htmlEncode(question.description)}
		</div>
		<div class="${toPDF ? 'showText' : 'hideText'}" id="longQ_${itemIdx}">
			<a href="javascript:toggleDisplay('shortQ_${itemIdx}');toggleDisplay('longQ_${itemIdx}');" class="statLink"><html:img src="/i/or/minus.gif" altKey="global.minus.alt" border="0" /></a>
			<b><bean:message key="editReview.Question.title" /> ${groupStatus.index + 1}.${sectionStatus.index + 1}.${questionStatus.index + 1}</b>
			${orfn:htmlEncode(question.description)}<br />
			${orfn:htmlEncode(question.guideline)}
		</div>
	</td>
	<c:if test="${not noQuestionWeight}">
		<td class="valueC pdf_weight">${orfn:displayScore(pageContext.request, question.weight)}</td>
	</c:if>
											