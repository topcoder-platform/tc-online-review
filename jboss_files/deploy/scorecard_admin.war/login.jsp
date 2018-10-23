<%@ page language="java" pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/2000/REC-xhtml1-20000126/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>TopCoder Software</title>

<link rel="stylesheet" type="text/css" href="/includes/tcs_style.css" />

<script language="JavaScript" type="text/javascript" src="/scripts/javascriptAdmin.js">
</script>
</head>

<body class="body" onLoad="frmLogin.txtHandle.focus()">

<!-- Header begins -->
<!--Header begins-->
    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="head">
    	<tr valign="top">
		<td width="219" height="57" align="left" rowspan="2"><a href="/pages/s_index.jsp"><img src="/images/logoTCSHome.gif" alt="TopCoder Software" width="216" height="57" border="0" /></a></td>
		<td width="155" height="57" align="left" rowspan="2"><img src="/images/homeBannerAdmin.gif" alt="Administration" width="155" height="57" border="0" /></td>

    		
		<td class="headRight">
			<table border="0" cellpadding="0" cellspacing="0" class="adminSearch" align="right">
				<tr valign="middle">
					<td width="24" height="25" valign="bottom"><img src="/images/searchHeaderLeft.gif" alt="" width="24" height="25" border="0"></td>
					<td><img src="/images/clear.gif" alt="" width="6" border="0" /></td>
    					<td class="login">You are not logged in.</td>
					<td><img src="/images/clear.gif" alt="" width="10" border="0" /></td>

        				<td class="login"><a href="/admin/c_login.jsp" class="loginLinks">:: Login</a></td>

        				<td><img src="/images/clear.gif" alt="" width="10" border="0" /></td>

				</tr>
			</table>
		</td>
    	</tr>
    				
	<tr valign="bottom">
		<td class="headRight"><img src="/images/homeBannerHead.gif" alt="The Future of Software Development" width="510" height="13" border="0" /></td>
	</tr>

	
	<tr><td height="4" class="headStripe" colspan="3"><img src="/images/clear.gif" alt="" height="4" border="0" /></td></tr>
</table>
<!--Header ends -->

<!-- Nav Bar begins -->
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="topNav">
	<tr>
		<td width="20" class="adminTopNav"><img src="../images/clear.gif" alt="" width="20" height="21" border="0"></td>
		<td>



			<table width="3%" border="0" cellpadding="0" cellspacing="0" class="adminTopNav">

				<tr valign="middle">
					<td width="46" align="center" class="adminTopNav"><a onmouseover="document.images['siteAdmin'].src = siteAdminon.src; window.status='Return to Regular Site'; return true;" onmouseout="document.images['siteAdmin'].src = siteAdminoff.src" class="topNavLinks" href="http://software.topcoder.com"><img src="/images/siteAdmin_off.gif" alt="RETURN TO SITE" name="siteAdmin" width="84" height="21" border="0" /></a></td>
					<td width="66" align="center" class="adminTopNav"><a onmouseover="document.images['catalogAdmin'].src = catalogAdminon.src; window.status='Catalog Administration'; return true;" onmouseout="document.images['catalogAdmin'].src = catalogAdminoff.src" class="topNavLinks" href="catalog.jsp"><img src="/images/catalogAdmin_off.gif" alt="CATALOG ADMIN" name="catalogAdmin" width="97" height="21" border="0" /></a></td>
					<td width="106" align="center" class="adminTopNav"><a onmouseover="document.images['categoryAdmin'].src = categoryAdminon.src; window.status='Category Administration'; return true;" onmouseout="document.images['categoryAdmin'].src = categoryAdminoff.src" class="topNavLinks" href="category_admin.jsp"><img src="/images/categoryAdmin_off.gif" alt="CATEGORY ADMIN" name="categoryAdmin" width="104" height="21" border="0" /></a></td>
					<td width="156" align="center" class="adminTopNav"><a onmouseover="document.images['userAdmin'].src = userAdminon.src; window.status='User Administration'; return true;" onmouseout="document.images['userAdmin'].src = userAdminoff.src" class="topNavLinks" href="user_admin.jsp"><img src="/images/userAdmin_off.gif" alt="USER ADMIN" name="userAdmin" width="79" height="21" border="0" /></a></td>
					<td width="106" align="center" class="adminTopNav"><a onmouseover="document.images['listsAdmin'].src = listsAdminon.src; window.status='Lists Administration'; return true;" onmouseout="document.images['listsAdmin'].src = listsAdminoff.src" class="topNavLinks" href="lists.jsp"><img src="/images/listAdmin_off.gif" alt="LISTS ADMIN" name="listsAdmin" width="79" height="21" border="0" /></a></td>
					<td width="106" align="center" class="adminTopNav"><a onmouseover="window.status='Scorecard Administration'; return true;" class="topNavLinks" href="../scorecard_admin/scorecardAdmin.do?actionName=listScorecards">SCORECARD ADMIN</a></td>

				</tr>

			</table>
			

			
		</td>
		<td width="97%" class="adminTopNav"><img src="../images/clear.gif" alt="" border="0"></td>
	</tr>
</table>
<!-- Nav Bar ends -->


<!-- Header ends -->

<!-- breadcrumb begins -->
<table width="100%" cellpadding="0" cellspacing="0" border="0">
	<tr>

		<td valign="middle" class="breadcrumb" width="1%"><img src="/images/clear.gif" alt="" width="10" height="1" border="0" /></td>
		<td valign="middle" class="breadcrumb" nowrap="nowrap"><strong>Admin Login</strong></td>
		<td valign="middle" class="breadcrumb" width="98%"><img src="/images/clear.gif" alt="" width="10" height="1" border="0" /></td>
	</tr>
</table>
<!-- breadcrumb ends -->

<table width="100%" border="0" cellpadding="0" cellspacing="0" align="center" class="middle">
	<tr valign="top">

<!-- Left Column begins -->

		<td width="165" class="leftColumn"><img src="/images/clear.gif" alt="" width="165" height="5" border="0" /></td>
		<td width="5" class="leftColumn"><img src="/images/clear.gif" alt="" width="5" height="10" border="0" /></td>
<!-- Left Column ends -->

<!-- Gutter 1 begins -->
		<td width="15"><img src="/images/clear.gif" alt="" width="15" height="10" border="0" /></td>
<!-- Gutter 1 ends -->

<!-- Middle Column begins -->
		<td width="100%">
			<table width="100%" border="0" cellpadding="0" cellspacing="0" align="center">
				<tr><td height="15"><img src="/images/clear.gif" alt="" width="10" height="15" border="0" /></td></tr>

				<tr><td class="normal"><img src="/images/headAdminLogin.gif" alt="Login" width="545" height="35" border="0" /></td></tr>
				<tr><td class="subhead"></td></tr>
			</table>

			<table width="100%" cellpadding="0" cellspacing="6" align="center" border="0">
				<tr valign="top">
					<td align="center">
						<table width="500" cellpadding="0" cellspacing="0" border="0" align="center">
							<tr><td><img src="../images/adminNamePassHead.gif" alt="User Name &amp; Password" width="500" height="29" border="0" /></td></tr>

						</table>

						<table width="500" border="0" cellspacing="8" cellpadding="0" align="center" class="admin">
<!-- Error Message Area -->
							<tr valign="middle">
								<td width="48%">
    <html:form action="/login" method="post">
    	<html:hidden property="method" value="login"/>
									<img src="/images/clear.gif" alt="" width="5" height="1" border="0" /></td>
								<td width="1%" class="adminText"></td>
								<td width="1%" class="errorText"></td>

								<td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0" /></td>
							</tr>

<!-- User Name -->
							<tr valign="middle">
								<td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0" /></td>
								<td class="adminLabel" nowrap="nowrap">User Name</td>
								<td class="adminText"><input class="registerForm" type="text" name="userName" value ="" size="30" maxlength="30" /></td>
								<td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0" /></td>

							</tr>

<!-- Password -->
							<tr valign="middle">
								<td width="5"><img src="/images/clear.gif" alt="" width="5" height="1" border="0" /></td>
								<td class="adminLabel" nowrap="nowrap">Password</td>
								<td class="adminText"><input class="registerForm" type="password" name="password" value ="" size="30" maxlength="15" /></td>
								<td width="5"><img src="/images/clear.gif" alt="" width="5" height="1" border="0" /></td>
							</tr>

						</table>

						<table width="500" border="0" cellspacing="8" cellpadding="0" align="center" class="admin">
							<tr valign="middle">
								<td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0" /></td>
								<td class="adminTextCenter"><input class="adminButton" type="submit" name="a" value="Login"></input></td>
								<td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0" /></td>
							</tr>
						</table>

						<table width="500" cellpadding="0" cellspacing="0" border="0" align="center">
							<tr><td><img src="../images/adminFoot.gif" alt="" width="500" height="11" border="0" /></td></tr>
							<tr><td height="40"><img src="/images/clear.gif" alt="" width="10" height="40" border="0" /></td>
						
					</html:form>
					</tr>
						</table></td>
				</tr>
			</table></td>
<!--Middle Column ends -->

<!-- Gutter 2 begins -->
		<td width="15"><img src="/images/clear.gif" alt="" width="15" height="10" border="0" /></td>

<!-- Gutter 2 ends -->

<!-- Right Column begins -->
		<td width="245"><img src="/images/clear.gif" alt="" width="245" height="15" border="0" /></td>
<!--Right Column ends -->

<!-- Gutter 3 begins -->
		<td width="10"><img src="/images/clear.gif" alt="" width="10" height="10" border="0" /></td>
<!-- Gutter 3 ends -->
	</tr>
</table>

<!-- Footer begins -->
<table width="100%" border="0" cellpadding="0" cellspacing="0">

    <tr><td width="100%" class="footer"><a href="s_index.jsp" class="footerLinks">Home</a>&#160;&#160;|&#160;&#160;<a href="s_about.jsp" class="footerLinks">About Us</a>&#160;&#160;|&#160;&#160;<a href="s_privacy.jsp" class="footerLinks">Privacy</a>&#160;&#160;|&#160;&#160;<a href="s_terms.jsp" class="footerLinks">Terms</a>&#160;&#160;|&#160;&#160;<a href="s_contact.jsp" class="footerLinks">Contact us</a></td></tr>

    <tr><td height="4" class="footerStripe"><img src="/images/clear.gif" width="10" height="4" border="0"/></td></tr>

    <tr><td width="100%" class="copyright">Copyright &#169; 2000-2006, TopCoder, Inc. All rights reserved.</td></tr>

</table>


<!-- Footer ends -->

</body>
</html>