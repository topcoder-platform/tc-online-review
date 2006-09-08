<%@ page language="java" %>
<%@ taglib  uri="/tags/struts-html" prefix="html" %>
<%@ taglib  uri="/tags/struts-bean" prefix="bean" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html xhtml="true">

<head>
	<title><bean:message key="login.title" /></title>

	<!-- TopCoder CSS -->
	<link type="text/css" rel="stylesheet" href="../css/style.css" />
	<link type="text/css" rel="stylesheet" href="../css/coders.css" />
	<link type="text/css" rel="stylesheet" href="../css/stats.css" />
	<link type="text/css" rel="stylesheet" href="../css/tcStyles.css" />

	<!-- CSS and JS from wireframes -->
	<script language="javascript" type="text/javascript" src="../scripts/expand_collapse.js"><!-- @ --></script>

	<!-- CSS and JS by Petar -->
	<link type="text/css" rel="stylesheet" href="../css/new_styles.css" />
	<script language="JavaScript" type="text/javascript" src="../scripts/rollovers.js"><!-- @ --></script>
</head>
<body>

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
				<br /><br />
				<div align="center">
					<html:errors />
					<html:form action="/actions/Login" focus="userName">
						<html:hidden property="method" value="login" />
						<table class="stat" cellpadding="0" cellspacing="0" width="50%">
							<tr>
								<td class="title" colspan="2"><bean:message key="login.formLogin.title" /></td>
							</tr>
							<tr>
								<td class="value" colspan="2"><div align="right">&nbsp;</div></td>
							</tr>
							<tr>
								<td class="value"><div align="right"><bean:message key="login.formLogin.userName" /> </div></td>
								<td class="value"><html:text property="userName" /></td>
							</tr>
							<tr>
								<td class="value"><div align="right"><bean:message key="login.formLogin.password" /> </div></td>
								<td class="value"><html:password property="password" /></td>
							</tr>
							<tr>
								<td class="value"><div align="right">&nbsp; </div></td>
								<td class="value"><html:image altKey="login.formLogin.btnLogin.alt"
										srcKey="login.formLogin.btnLogin.img" border="0" styleClass="imgLogin" /></td>
							</tr>
						</table><br />
					</html:form>
					<table cellpadding="0" cellspacing="0" width="50%">
						<tr>
							<td class="value" colspan="2">
								<strong><bean:message key="login.forgotPassword" /></strong><br/>
								<bean:message key="login.cannotRememberPassword1" /> <a href="http://www.topcoder.com/tc?&module=PasswordEmail"><bean:message key="clickHere" /></a> <bean:message key="login.cannotRememberPassword2" /><br /><br />
								<strong><bean:message key="login.newToTopCoder" /></strong><br/>
								<a href="https://www.topcoder.com/reg/"><bean:message key="login.registerNow" /></a> <bean:message key="login.afterYouCompleteTheRegProcess" />
							</td>
						</tr>
					</table>
				</div>
				<br /><br />
			</td>
			<!-- Center Column Ends -->
			<!-- Gutter -->
			<td width="15"><img src="../i/clear.gif" width="25" height="1" border="0" /></td>
			<!-- Gutter Ends -->
			<!-- Right Column Begins -->
			<!-- Right Column Ends -->
			<!-- Gutter -->
			<td width="2"><img src="../i/clear.gif" width="2" height="1" border="0" /></td>
			<!-- Gutter Ends -->
		</tr>
	</table>

	<jsp:include page="../includes/inc_footer.jsp" />
</body>
</html:html>