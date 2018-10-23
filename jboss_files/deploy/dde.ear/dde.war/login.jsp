<%--
  - Author: vangavroche, TCSASSEMBLER
  - Version: 1.1 (Release Assembly - TopCoder Password Recovery Revamp v1.0)
  - Copyright (C) 2010 - 2013 TopCoder Inc., All Rights Reserved.
  -
  - Description: Login page.
  - Changes in 1.1 (Release Assembly - TopCoder Password Recovery Revamp v1.0 )
  - - Change the entry link of password recovery from /tc?module=RecoverPassword" to /tc?module=FindUser".
  - - Add this code document
  - 
--%>
<%@ page import="javax.naming.*,
                 com.topcoder.dde.util.Cookies,
                 com.topcoder.dde.user.UserNotActivatedException" %>
<%@ page import="com.topcoder.shared.util.ApplicationServer" %>
<%@ page import="javax.ejb.CreateException" %>
<%@ page import="java.io.*" %>
<%@ page import="java.rmi.*" %>
<%@ page import="javax.rmi.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.lang.reflect.*" %>

<%@ include file="includes/util.jsp" %>
<%@ include file="includes/session.jsp" %>

<%
    // STANDARD PAGE VARIABLES
    String page_name = "/login.jsp";
    String action = request.getParameter("a");
    String strMsg = "";
%>

<%
    if (action != null) {
        action = action.trim();
        debug.addMsg("login", "action occurred: '" + action + "'");

        if (action.equalsIgnoreCase("subscription")) {
            strMsg="In order to download components you must login again for security reasons.";
            session.putValue("nav_redirect","/catalog/showroom.jsp");
            session.removeValue("nav_redirect_msg");
        }

        if (action.equalsIgnoreCase("logout")) {
            session.putValue("nav_redirect","/index.jsp");
            session.removeValue("nav_redirect_msg");
            session.removeValue("TCSUBJECT");
            session.removeValue("TCUSER");
            session.removeValue("AdminTCSubject");
            session.removeAttribute("LOGIN_FLAG");
            //remove stuff used by online review
            session.removeAttribute("user");
            session.removeAttribute("projects");
            Cookies.clearLoginCookies(response);
//            To log user out of Online Review App
//            session.removeAttribute("user");
//            session.removeAttribute("projects");
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
                tcSubject = USER_MANAGER.login(handle, password);
                debug.addMsg("login", "user logged in with user id " + tcSubject.getUserId());
                debug.addMsg("login", "user has " + tcSubject.getPrincipals());
                debug.addMsg("login", "principals are " + tcSubject.getPrincipals().toString());
                session.putValue("TCSUBJECT", tcSubject);

                tcUser = USER_MANAGER.getUser(tcSubject.getUserId());
                debug.addMsg("login", "loaded tcUser with id " + tcSubject.getUserId());
                session.putValue("TCUSER", tcUser);

                session.setAttribute("LOGIN_FLAG", new Object());
                debug.addMsg("login", "this user has been verified");

                if ("true".equalsIgnoreCase(request.getParameter("remember_me"))) {
                    Cookies.setLoginCookies(tcSubject.getUserId(), password, response);
                    debug.addMsg("login", "auto-login cookies set");
                }

                // CHECK IF REDIRECT IS NECESSARY
                Object redirectURL = session.getValue("nav_redirect");
                if (redirectURL != null) {
                    session.removeValue("nav_redirect");
                    response.sendRedirect("" + redirectURL);
                    return;
                }
            } catch (UserNotActivatedException unae) {
                debug.addMsg("login_message", "Account not activated for user " + handle);
                strMsg = "You must activate your account to login.";
            } catch (com.topcoder.security.login.AuthenticationException ae) {
                debug.addMsg("login_message", ae.getMessage());
                strMsg = ae.getMessage();
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
    <title>TopCoder | Login</title>

<link rel="stylesheet" type="text/css" href="/includes/tcs_style.css" />

<script language="JavaScript" type="text/javascript" src="/scripts/javascript.js"></script>
<script language="JavaScript" type="text/javascript" src="http://www.TopCoder.com/js/script.js"></script>
<jsp:include page="/includes/header-files.jsp" />
</head>

<body class="body" onLoad="frmLogin.txtHandle.focus()">

<!-- Header begins -->
<jsp:include page="/includes/top.jsp">
    <jsp:param name="page_name" value="login.jsp" />
</jsp:include>
<jsp:include page="/includes/menu.jsp" >
    <jsp:param name="isSoftwarePage" value="true" />
</jsp:include>
<!-- Header ends -->

<table width="100%" border="0" cellpadding="0" cellspacing="0" align="center" class="middle">
    <tr valign="top">

<!-- Left Column begins -->
        <td width="180" class="leftColumn">
        <jsp:include page="/includes/left.jsp" >
            <jsp:param name="level1" value="index" />
        </jsp:include>
        </td>
<!-- Left Column ends -->

<!-- Gutter 1 begins -->
        <td width="25"><img src="/images/clear.gif" alt="" width="25" height="10" border="0" /></td>
<!-- Gutter 1 ends -->

<!-- Center Column begins -->
        <td width="99%"><img src="/images/clear.gif" width="400" height="11" alt="" border="0" /><br />
		<div class="minheight" align="center">
            <table width="75%" border="0" cellpadding="0" cellspacing="0">
               <tr valign="top">
                    <td class="bodyText" width="100%">
                    
                        <img src="/images/clear.gif" alt="" width="380" height="10" border="0" /><br />
                        <h2>Forgot your password?</h2>
                        If you cannot remember your password <a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=FindUser" class="bodyText">click here</a>
                        and we can help you restore your account.</p>
						<br /><br />
                        <h2>New to TopCoder?</h2>
                        <a href="http://<%=ApplicationServer.SERVER_NAME%>/reg/" class="bodyText">Register now.</a>
                        After you complete the registration process, we will send your account activation code via email.</p>
                      
                    </td>
                </tr>

                <tr valign="middle">
                    <form name="frmLogin" action="<%= page_name %>" METHOD="POST">
                    <input type="hidden" name="a" value="login" />
                    <td align="center">
                    	<br />
                        <table border="0" cellpadding="3" cellspacing="0" class="login-table">
                           

<!-- User Name Error Text -->
                            <tr valign="top">
                                <td class="registerError" colspan="3"><br /><%= strMsg %><br /><br /></td>
                            </tr>

<!-- User Name -->
                            <tr valign="middle">
                                <td nowrap class="bodyText" align="right">Username:</td>
                                <td colspan="2" align="left"> &nbsp; <input type="text" name="txtHandle" value="" size="20" maxlength="30"></td>
                            </tr>

<!-- Password -->
                            <tr valign="middle">
                                <td nowrap class="bodyText" align="right">Password:</td>
                                <td align="left"> &nbsp; <input onkeypress="submitEnter(event,document.frmLogin)" type="password" name="txtPassword" value="" size="20" maxlength="30"></td>
                                <td nowrap="nowrap" class="bodyText" align="left">&#160;&#160;<strong><a href="javascript:document.frmLogin.submit()">Login &gt;</a></strong></td>
                            </tr>

                            <tr valign="middle">
                                <td align="right">&nbsp;</td>
                                <td nowrap class="bodyText" colspan="2" align="left"> &nbsp; <input onkeypress="submitEnter(event,document.frmLogin)" type="checkbox" name="remember_me" value="true"> Remember Me</td>
                            </tr>

                           <tr><td colspan="3"><img src="/images/clear.gif" width="10" height="3" alt="" border="0" /></td></tr>
                        </table>

                        <p><br /></p>

                    </td>
                    </form>
                </tr>
            </table>
			  </div>
            <p><br></p>
            <p><br></p>

            </div>
        </td>
<!-- Center Column ends -->

<!-- Gutter begins -->
        <td width="195"><img src="/images/clear.gif" alt="" width="195" height="10" border="0" /></td>
<!-- Gutter ends -->

    </tr>
</table>

<!-- Footer begins -->
<jsp:include page="/includes/foot.jsp" flush="true" />
<!-- Footer ends -->

</body>
</html>
