<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page language="java" isELIgnored="false"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="html" uri="/tags/struts-html"%>
<%@ taglib prefix="bean" uri="/tags/struts-bean"%>
<%@ taglib prefix="tc-webtag" uri="/tags/tc-webtags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html xhtml="true">

<head>
<jsp:include page="/includes/project/project_title.jsp">
	<jsp:param name="thirdLevelPageKey" value="viewAutoScreening.title" />
</jsp:include>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<!-- TopCoder CSS -->
<link type="text/css" rel="stylesheet"
	href="<html:rewrite href='/css/style.css' />" />
<link type="text/css" rel="stylesheet"
	href="<html:rewrite href='/css/coders.css' />" />
<link type="text/css" rel="stylesheet"
	href="<html:rewrite href='/css/stats.css' />" />
<link type="text/css" rel="stylesheet"
	href="<html:rewrite href='/css/tcStyles.css' />" />

<!-- CSS and JS by Petar -->
<link type="text/css" rel="stylesheet"
	href="<html:rewrite href='/css/or/new_styles.css' />" />
<script language="JavaScript" type="text/javascript"
	src="<html:rewrite href='/js/or/rollovers2.js' />"><!-- @ --></script>
</head>

<body>
<div align="center">

<div class="maxWidthBody" align="left"><jsp:include
	page="/includes/inc_header.jsp" /> <jsp:include
	page="/includes/project/project_tabs.jsp" />

<div id="mainMiddleContent">
<div style="position: relative; width: 100%;"><jsp:include
	page="/includes/review/review_project.jsp" /> <%-- TODO: Should have distinction between warnings, results, errors  --%>
<h3><bean:message key="viewAutoScreening.Warnings" /></h3>

<table class="scorecard" cellpadding="0" width="100%"
	style="border-collapse: collapse;">
	<tr>
		<td class="title"><bean:message
			key="viewAutoScreening.ScreeningResponses" /></td>
	</tr>
	<c:forEach var="severityEntry" items="${screeningResultsMap}">
		<c:forEach var="responseEntry" varStatus="responseStatus"
			items="${severityEntry.value}">
			<c:forEach var="result" varStatus="resultStatus"
				items="${responseEntry.value}">
				<c:if test="${resultStatus.first}">
					<c:if test="${responseStatus.first}">
						<tr>
							<td class="header"><b><bean:message
								key="ScreeningResponseSeverity.${fn:replace(result.screeningResponse.responseSeverity.name, ' ', '')}" /></b>
							</td>
						</tr>
						<tr class="light">
							<td class="value">
					</c:if>
					<br />
					<font color="#CC0000"><b>${result.screeningResponse.responseCode}:</b>
					${result.screeningResponse.responseText}<br />
					</font>
					<ul>
				</c:if>
				<li>${result.dynamicText}</li>
				<c:if test="${resultStatus.last}">
					</ul>
					<c:if test="${responseStatus.last}">
						</td>
						</tr>
					</c:if>
				</c:if>
			</c:forEach>
		</c:forEach>
	</c:forEach>
	<tr>
		<td class="lastRowTD"><!-- @ --></td>
	</tr>
</table>
<br />

<div align="right"><a href="javascript:history.go(-1)"><html:img
	srcKey="btnBack.img" altKey="btnBack.alt" border="0" /></a><br />
</div>
<br />

</div>
</div>

<jsp:include page="/includes/inc_footer.jsp" /></div>

</div>

</body>
</html:html>
