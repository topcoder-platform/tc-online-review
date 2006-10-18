<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="html" uri="/tags/struts-html" %>
<%@ taglib prefix="bean" uri="/tags/struts-bean" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html xhtml="true">

<head>
	<title><bean:message key="OnlineReviewApp.title" /></title>

	<!-- TopCoder CSS -->
	<link type="text/css" rel="stylesheet" href="<html:rewrite page='/css/style.css' />" />
	<link type="text/css" rel="stylesheet" href="<html:rewrite page='/css/coders.css' />" />
	<link type="text/css" rel="stylesheet" href="<html:rewrite page='/css/stats.css' />" />
	<link type="text/css" rel="stylesheet" href="<html:rewrite page='/css/tcStyles.css' />" />

	<!-- CSS and JS by Petar -->
	<link type="text/css" rel="stylesheet" href="<html:rewrite page='/css/or/new_styles.css' />" />
	<script language="JavaScript" type="text/javascript"
		src="<html:rewrite page='/js/or/rollovers.js' />"><!-- @ --></script>
</head>

<body>
	<jsp:include page="/includes/inc_header.jsp" />
	<table width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr valign="top">
			<!-- Left Column Begins -->
			<td width="180">
				<jsp:include page="/includes/inc_leftnav.jsp" />
			</td>
			<!-- Left Column Ends -->

			<!-- Gutter Begins -->
			<td width="15"><html:img page="/i/clear.gif" width="15" height="1" border="0" /></td>
			<!-- Gutter Ends -->

			<!-- Center Column Begins -->
			<td class="bodyText">
				<jsp:include page="/includes/project/project_tabs.jsp" />


				<div id="mainMiddleContent">
					<div style="padding: 11px 6px 9px 0px;">
						<table cellspacing="0" cellpadding="0" border="0">
							<tr valign="middle">
								<td><html:img page="/i/${categoryIconName}" alt="" border="0" /></td>
								<td><html:img page="/i/${rootCatalogIcon}" alt="${rootCatalogName}" border="0" /></td>
								<td>
									<span class="bodyTitle">${project.allProperties["Project Name"]}</span>
									<c:if test='${!(empty project.allProperties["Project Version"])}'>
										<font size="4"><bean:message key="global.version" />
											${project.allProperties["Project Version"]}</font>
									</c:if>
								</td>
							</tr>
						</table>
					</div><br />

					<table class="scorecard" width="100%" cellpadding="0" cellspacing="0" style="border-collapse:collapse;">
						<tr>
							<td class="title"><bean:message key="confirmDeleteSubmission.box.title" /></td>
						</tr>
						<tr class="light">
							<td class="valueC">
								<br />
								<bean:message key="confirmDeleteSubmission.question" /><br /><br />
							</td>
						</tr>
						<tr>
							<td class="lastRowTD"><!-- @ --></td>
						</tr>
					</table><br />

					<div align="right">
						<html:link page="/actions/DeleteSubmission.do?method=deleteSubmission&uid=${uid}&delete=y"><html:img srcKey="confirmDeleteSubmission.btnDelete.img" altKey="confirmDeleteSubmission.btnDelete.alt" border="0" /></html:link>&#160;
						<a href="javascript:history.go(-1)"><html:img srcKey="btnCancel.img" altKey="btnCancel.alt" border="0" /></a>
					</div><br />
				</div>
				<br /><br />
			</td>
			<!-- Center Column Ends -->

			<!-- Gutter -->
			<td width="15"><html:img page="/i/clear.gif" width="25" height="1" border="0" /></td>
			<!-- Gutter Ends -->

			<!-- Gutter -->
			<td width="2"><html:img page="/i/clear.gif" width="2" height="1" border="0" /></td>
			<!-- Gutter Ends -->
		</tr>
	</table>

	<jsp:include page="/includes/inc_footer.jsp" />
</body>
</html:html>