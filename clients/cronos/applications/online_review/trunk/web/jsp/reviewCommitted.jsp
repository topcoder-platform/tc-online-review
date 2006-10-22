<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="html" uri="/tags/struts-html" %>
<%@ taglib prefix="bean" uri="/tags/struts-bean" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html xhtml="true">

<head>
	<title><bean:message key="OnlineReviewApp.title" /></title>

	<!-- TopCoder CSS -->
	<link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/style.css' />">
	<link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/coders.css' />">
	<link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/stats.css' />">
	<link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/tcStyles.css' />">

	<!-- JS from wireframes -->
	<script language="javascript" type="text/javascript" src="<html:rewrite href='/js/or/popup.js' />"></script>
	<script language="javascript" type="text/javascript" src="<html:rewrite href='/js/or/expand_collapse.js' />"></script>

	<!-- CSS and JS by Petar -->
	<link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/or/new_styles.css' />">
	<script language="JavaScript" type="text/javascript" src="<html:rewrite href='/js/or/rollovers.js' />"></script>
</head>

<body>
	<jsp:include page="/includes/inc_header.jsp" />
	<table width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr valign="top">
			<!-- Left Column Begins -->
			<td width="180"><jsp:include page="/includes/inc_leftnav.jsp" /></td>
			<!-- Left Column Ends -->

			<!-- Gutter Begins -->
			<td width="15"><html:img src="/i/clear.gif" width="15" height="1" border="0"/></td>
			<!-- Gutter Ends -->

			<!-- Center Column Begins -->
			<td class="bodyText">
				<jsp:include page="/includes/project/project_tabs.jsp" />

				<div id="mainMiddleContent">
					<jsp:include page="/includes/review/review_project.jsp" />
					<h3>${orfn:htmlEncode(scorecardTemplate.name)}</h3>

					<table class="scorecard" width="100%" cellpadding="0" cellspacing="0" style="border-collapse:collapse;">
						<tr>
							<td class="title"><bean:message key="reviewCommitted.Committed" /></td>
						</tr>
						<tr class="light">
							<td class="valueC">
								<br />
								<bean:message key="reviewCommitted.Score" />
								<b>${orfn:displayScore(null, reviewScore)}</b><br /><br />
							</td>
						</tr>
						<tr>
							<td class="lastRowTD"><!-- @ --></td>
						</tr>
					</table><br />

					<div align="right">
						<html:link page="/actions/ViewProjectDetails.do?method=viewProjectDetails&pid=${project.id}"><html:img srcKey="reviewCommitted.ReturnToProjDet.img" altKey="reviewCommitted.ReturnToProjDet.alt" border="0" /></html:link>&#160;
						<html:link page="/actions/View${reviewType}.do?method=view${reviewType}&rid=${rid}"><html:img srcKey="reviewCommitted.ViewScorecard.img" altKey="reviewCommitted.ViewScorecard.alt" border="0" /></html:link>
					</div><br />
				</div>
				<br /><br />
			</td>
			<!-- Center Column Ends -->

			<!-- Gutter -->
			<td width="15"><html:img src="/i/clear.gif" width="25" height="1" border="0" /></td>
			<!-- Gutter Ends -->

			<!-- Gutter -->
			<td width="2"><html:img src="/i/clear.gif" width="2" height="1" border="0" /></td>
			<!-- Gutter Ends -->
		</tr>
	</table>

	<jsp:include page="/includes/inc_footer.jsp" />
</body>
</html:html>