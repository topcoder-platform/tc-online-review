<%@ page import="javax.naming.*" %>
<%@ page import="javax.ejb.CreateException" %>
<%@ page import="java.io.*" %>
<%@ page import="java.rmi.*" %>
<%@ page import="javax.rmi.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.lang.reflect.*" %>

<%@ page import="com.topcoder.shared.util.ApplicationServer" %>

<%@ include file="/includes/util.jsp" %>
<%@ include file="/includes/session.jsp" %>
<%@ include file="/includes/formclasses.jsp" %>


<%
    // STANDARD PAGE VARIABLES
    String page_name = "c_register_activate.jsp";
    String action = request.getParameter("a");

	long tcsForumsID = WebConstants.TCS_FORUMS_ROOT_CATEGORY_ID;
%>

<%
    String code = request.getParameter("code");

    TCSubject requestor = null;
    String strMsg = null;
    boolean activated = false;

    // LOGIN USER AND SET SESSION
    try {
          tcUser = USER_MANAGER.activate(code);
          activated = true;
        debug.addMsg("register_activate", "loaded tcUser with activation code " + code);
        try {
            tcSubject = LOGIN.login(tcUser.getRegInfo().getUsername(), tcUser.getRegInfo().getPassword());
            debug.addMsg("register_activate", "user logged in with user id " + tcSubject.getUserId());
            debug.addMsg("register_activate", "user has " + tcSubject.getPrincipals());
            debug.addMsg("register_activate", "principals are " + tcSubject.getPrincipals().toString());
            session.putValue("TCSubject", tcSubject);
        } catch (com.topcoder.security.login.AuthenticationException ae) {
            debug.addMsg("login_message", ae.getMessage());
            strMsg = ae.getMessage();
        }
    } catch (UndeclaredThrowableException e) {
        debug.addMsg("login", "" + e.getUndeclaredThrowable());
        strMsg = "Invalid user";
    } catch (Exception e) {
        debug.addMsg("login", "" + e);
        e.printStackTrace();
        strMsg = "Invalid user";
    }
%>


<html>
<head>
    <title>TopCoder Software</title>

<link rel="stylesheet" type="text/css" href="/includes/tcs_style.css">

<script language="JavaScript" src="/scripts/javascript.js">
</script>

</head>

<body class="body">

<!-- Header begins -->
<jsp:include page="/includes/top.jsp">
<jsp:param name="TCDlevel" value="software" />
</jsp:include>
<jsp:include page="/includes/menu.jsp" >
    <jsp:param name="isSoftwarePage" value="true"/>
</jsp:include>
<!-- Header ends -->

<table width="100%" border="0" cellpadding="0" cellspacing="0" align="center" class="middle">
    <tr valign="top">

<!-- Left Column begins -->
        <td width="165" class="leftColumn">
        <jsp:include page="/includes/left.jsp" >
            <jsp:param name="level1" value="index"/>
        </jsp:include>
        </td>
<!-- Left Column ends -->

<!-- Gutter 1 begins -->
        <td width="15"><img src="/images/clear.gif" alt="" width="15" height="10" border="0" /></td>
<!-- Gutter 1 ends -->

<!-- Middle Column begins -->
        <td width="100%">
            <table width="100%" border="0" cellpadding="0" cellspacing="0" align="center">
                <tr><td height="15"><img src="/images/clear.gif" alt="" width="10" height="15" border="0"/></td></tr>
                <tr><td class="normal"><img src="/images/headRegNewAcct.gif" alt="Register New Account" width="545" height="35" border="0" /></td></tr>
<%  if (activated) { %>
                <tr><td><h3>New Account Activated</h3></td></tr>
<%  } else { %>
                <tr><td class="errorText">Invalid Activation Code!</td></tr>
<%  } %>
            </table>

            <table width="100%" cellpadding="0" cellspacing="6" align="center" border="0">
                <tr valign="top">
                    <td class="normal">
<%  if (activated) { %>
                        <p><strong>Congratulations! Your new TopCoder Software Account has been activated.</strong></p>

                        <p>Start experiencing the TopCoder difference today! Visit our <a href="catalog/c_showroom.jsp">Component Catalog,</a>
                        participate in an online <a href="http://<%=ApplicationServer.FORUMS_SERVER_NAME%>/?module=Category&categoryID=<%=tcsForumsID%>">Collaboration Forum</a>, or obtain a
                        <a href="components/subscriptions.jsp">subscription package</a> so you can start downloading components.</p>
<%  } else { %>
                        <p><strong>Please verify that you have entered the supplied URL correctly.</strong></p>
<%  } %>
                        <p>If you have any questions about your TopCoder Software Account, call us at 866.TOP.CODE (866.867.2633).</p>
                    </td>
                </tr>
                <tr><td height="40"><img src="/images/clear.gif" alt="" width="10" height="40" border="0"/></td></tr>
            </table>
<!--Middle Column ends -->

<!-- Gutter 2 begins -->
        <td width="15"><img src="/images/clear.gif" alt="" width="15" height="10" border="0"></td></form>
<!-- Gutter 2 ends -->
    </tr>
</table>

<!-- Footer begins -->
<jsp:include page="/includes/foot.jsp" flush="true" />
<!-- Footer ends -->

</body>
</html>
