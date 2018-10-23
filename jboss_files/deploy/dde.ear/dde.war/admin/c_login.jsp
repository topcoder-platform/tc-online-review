<%--
  - Author: vangavroche, TCSASSEMBLER
  - Version: 1.1 (Release Assembly - TopCoder Password Recovery Revamp v1.0)
  - Copyright (C) 2010 - 2013 TopCoder Inc., All Rights Reserved.
  -
  - Changes in 1.1 (Release Assembly - TopCoder Password Recovery Revamp v1.0 )
  - - Change the entry link of password recovery from /tc?module=RecoverPassword" to /tc?module=FindUser".
  - - Add this code document
  - 
--%>
<%@ page import="com.topcoder.dde.user.UserNotActivatedException,
                 com.topcoder.security.GeneralSecurityException"
 %>
<%@ page import="com.topcoder.security.TCSubject" %>
<%@ page import="com.topcoder.shared.util.ApplicationServer" %>

<%@ include file="/includes/util.jsp" %>
<%@ include file="session.jsp" %>

<%
	// STANDARD PAGE VARIABLES
	String page_name = "c_login.jsp";
	String action = request.getParameter("a");
	String strMsg = "";
%>

<%
	if (action != null) {
		action = action.trim();
		debug.addMsg("login", "action occurred: '" + action + "'");

		if (action.equalsIgnoreCase("logout")) {
			session.removeValue(/* "TCSUBJECT" */ "AdminTCSubject");
            //remove stuff used by online review
            session.removeAttribute("user");
            session.removeAttribute("projects");
            session.removeValue("TCSUBJECT");
            session.removeValue("TCUSER");
            session.removeAttribute("LOGIN_FLAG");
            session.removeValue("nav_redirect");
            Cookies.clearLoginCookies(response);
			tcUser = null;
			tcSubject = null;
		}

		if (action.equalsIgnoreCase("login")) {
			String handle = "";
			String password = "";

			handle = request.getParameter("txtHandle");
			password = request.getParameter("txtPassword");

			debug.addMsg("login", "handle: '" + handle + "'");
			//debug.addMsg("login", "password: '" + password + "'");

            TCSubject requestor = null;

			// LOGIN USER AND SET SESSION
			try {
                tcSubject = ADMIN_LOGIN.login(handle, password);
				debug.addMsg("login", "user logged in with user id " + tcSubject.getUserId());
                debug.addMsg("login", "user has " + tcSubject.getPrincipals());
                debug.addMsg("login", "principals are " + tcSubject.getPrincipals().toString());
				session.putValue(/* "TCSUBJECT" */ "AdminTCSubject", tcSubject);

                tcUser = USER_MANAGER.getUser(tcSubject.getUserId());
				debug.addMsg("login", "loaded tcUser with id " + tcSubject.getUserId());

                session.setAttribute("LOGIN_FLAG", new Object());
                debug.addMsg("login", "this user has been verified");

				// CHECK IF REDIRECT IS NECESSARY
				Object redirectURL = session.getValue("nav_redirect");
				if (redirectURL != null) {
					session.removeValue("nav_redirect");
					response.sendRedirect("" + redirectURL);
    				return;
				}
			} catch (com.topcoder.security.login.AuthenticationException ae) {
				debug.addMsg("login_message", ae.getMessage());
				strMsg = ae.getMessage();
			} catch (GeneralSecurityException gse) {
				debug.addMsg("login_message", gse.getMessage());
				strMsg = gse.getMessage();
            } catch (UserNotActivatedException unae) {
                debug.addMsg("login_message", "Account not activated for user " + handle);
                strMsg = "You must activate your account to login.";
			} catch (UndeclaredThrowableException e) {
				debug.addMsg("login", "" + e.getUndeclaredThrowable());
				strMsg = "Invalid user";
			} catch (Exception e) {
				debug.addMsg("login", "" + e);
				e.printStackTrace();
				strMsg = "Invalid user";
			}
		}
	} else {
		debug.addMsg("login", "no action occurred");
		Object redirectMsg = session.getValue("nav_redirect_msg");
		if (redirectMsg != null) {
		    strMsg = redirectMsg.toString();
		}
	}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/2000/REC-xhtml1-20000126/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>TopCoder Software</title>

<jsp:include page="/includes/header-files.jsp" />    
<link rel="stylesheet" type="text/css" href="/includes/tcs_style.css"/>

    <script language="JavaScript" type="text/javascript" src="/scripts/javascriptAdmin.js">
    </script>
</head>

<body class="body" onLoad="frmLogin.txtHandle.focus()">

<!-- Header begins -->
<%@ include file="/includes/adminHeader.jsp" %>
<%@ include file="/includes/adminNav.jsp" %>
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
									<form name="frmLogin" action="<%= page_name %>" method="post">
									<img src="/images/clear.gif" alt="" width="5" height="1" border="0" /></td>
								<td width="1%" class="adminText"></td>
								<td width="1%" class="errorText"><%= strMsg %></td>
								<td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0" /></td>
							</tr>

<!-- User Name -->
							<tr valign="middle">
								<td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0" /></td>
								<td class="adminLabel" nowrap="nowrap">User Name</td>
								<td class="adminText"><input class="registerForm" type="text" name="txtHandle" value ="" size="30" maxlength="30" /></td>
								<td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0" /></td>
							</tr>

<!-- Password -->
							<tr valign="middle">
								<td width="5"><img src="/images/clear.gif" alt="" width="5" height="1" border="0" /></td>
								<td class="adminLabel" nowrap="nowrap">Password</td>
								<td class="adminText"><input class="registerForm" type="password" name="txtPassword" value ="" size="30" maxlength="30" /></td>
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

						<table width="500" cellpadding="0" cellspacing="8" border="0" class="admin">
							<tr>
								<td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0" /></td>
                                <td class="loginCenter" nowrap="nowrap"><a class="loginLinks" href='http://<%= ApplicationServer.SERVER_NAME%>/tc?module=FindUser '>I forgot my password</a></td>
                                
								<td width="48%"><img src="/images/clear.gif" alt="" width="5" height="1" border="0" /></td>
							</tr>
						</table>

						<table width="500" cellpadding="0" cellspacing="0" border="0" align="center">
							<tr><td><img src="../images/adminFoot.gif" alt="" width="500" height="11" border="0" /></td></tr>
							<tr><td height="40"><img src="/images/clear.gif" alt="" width="10" height="40" border="0" /></td></form></tr>
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
<jsp:include page="/includes/foot.jsp" flush="true" />
<!-- Footer ends -->

</body>
</html>
