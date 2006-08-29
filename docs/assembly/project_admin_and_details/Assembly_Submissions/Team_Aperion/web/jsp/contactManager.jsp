<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="html" uri="/tags/struts-html" %>
<%@ taglib prefix="bean" uri="/tags/struts-bean" %>
<%@ page import="com.cronos.onlinereview.actions.AuthorizationHelper" %>
<%@ page import="com.cronos.onlinereview.actions.Constants" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html xhtml="true">

<head>
	<title><bean:message key="OnlineReviewApp.title" /></title>

	<!-- TopCoder CSS -->
	<link type="text/css" rel="stylesheet" href="../css/style.css">
	<link type="text/css" rel="stylesheet" href="../css/coders.css">
	<link type="text/css" rel="stylesheet" href="../css/stats.css">
	<link type="text/css" rel="stylesheet" href="../css/tcStyles.css">

	<!-- JS from wireframes -->
	<script language="javascript" type="text/javascript" src="../scripts/popup.js"></script>
	<script language="javascript" type="text/javascript" src="../scripts/expand_collapse.js"></script>

	<!-- CSS and JS by Petar -->
	<link type="text/css" rel="stylesheet" href="../css/new_styles.css">
	<script language="JavaScript" type="text/javascript" src="../scripts/rollovers.js"></script>
</head>

<body>
	<jsp:include page="../includes/inc_header.jsp" />
	<table width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr valign="top">
			<!-- Left Column Begins-->
			<td width="180"><jsp:include page="../includes/inc_leftnav.jsp" /></td>
			<!-- Left Column Ends -->

			<!-- Gutter Begins -->
			<td width="15"><img src="../i/clear.gif" width="15" height="1" border="0"/></td>
			<!-- Gutter Ends -->

			<!-- Center Column Begins -->
			<td class="bodyText">
				<jsp:include page="../includes/project/project_tabs.jsp" />

				<div id="mainMiddleContent">
					<div style="padding: 11px 6px 9px 0px;">
						<table cellspacing="0" cellpadding="0" border="0">
							<tr valign="middle">
								<td><img src="../i/${categoryIconName}" alt="" border="0" /></td>
								<td><img src="../i/${rootCatalogIcon}" alt="${rootCatalogName}" border="0" /></td>
								<td>
									<span class="bodyTitle">${project.allProperties["Project Name"]}</span>
									<c:if test='${!(empty project.allProperties["Project Name"])}'>
										<font size="4"><bean:message key="viewProjectDetails.version" />
											${project.allProperties["Project Version"]}</font>
									</c:if>
								</td>
							</tr>
						</table>
					</div><br />

					<html:errors />
					<html:form action="/actions/ContactManager">
						<html:hidden property="method" value="contactManager" />
						<html:hidden property="postBack" value="y" />
						<table class="scorecard" id="table1">
							<tr>
								<td class="title"><b><bean:message key="contactManager.Contact" /></b></td>
							</tr>
							<tr class="light">
								<td class="Value"><br>
									<b><bean:message key="contactManager.Category" /></b>
									<html:select property="cat" size="1" style="width:150px;" styleClass="inputBox">
										<html:option key="contactManager.Category.no" value="" />
										<html:option key="contactManager.Category.Question" value="Question" />
										<html:option key="contactManager.Category.Comment" value="Comment" />
										<html:option key="contactManager.Category.Complaint" value="Complaint" />
										<html:option key="contactManager.Category.Other" value="Other" />
									</html:select>
									<html:textarea property="msg" cols="20" rows="5" styleClass="inputTextBox" />

									<p><!-- @ --></p>
									<div align="right">
										<html:image srcKey="btnSubmit.img" altKey="btnSubmit.alt" border="0" />&#160;
										<a href="javascript:history.go(-1)"><html:img srcKey="btnCancel.img" altKey="btnCancel.alt" border="0" /></a>
									</div><br />
								</td>
							</tr>
							<tr>
								<td class="lastRowTD"><!-- @ --></td>
							</tr>
						</table>
					</html:form>
				</div>
			</td>
			<!-- Center Column Ends -->

			<!-- Gutter -->
			<td width="15"><img src="../i/clear.gif" width="25" height="1" border="0"></td>
			<!-- Gutter Ends -->

			<!-- Gutter -->
			<td width="2"><img src="../i/clear.gif" width="2" height="1" border="0"></td>
			<!-- Gutter Ends -->
		</tr>
	</table>

	<jsp:include page="../includes/inc_footer.jsp" />
</body>
</html:html>