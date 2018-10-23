<%@ page import="javax.naming.*,
                 com.topcoder.dde.catalog.ComponentSummary,
                 com.topcoder.dde.catalog.CatalogException,
                 com.topcoder.dde.catalog.CatalogHome,
                 com.topcoder.dde.catalog.Catalog,
                 com.topcoder.message.email.EmailEngine,
                 com.topcoder.message.email.TCSEmailMessage,
                 com.topcoder.dde.user.User,
                 com.topcoder.dde.user.RegistrationInfo,
                 com.topcoder.dde.user.UserManagerRemote,
                 com.topcoder.dde.user.UserManagerRemoteHome,
                 com.topcoder.web.common.MultipartRequest" %>
<%@ page import="javax.ejb.CreateException" %>
<%@ page import="java.io.*" %>
<%@ page import="java.rmi.*" %>
<%@ page import="javax.rmi.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.lang.reflect.*" %>
<%@ page import="com.topcoder.servlet.request.*" %>
<%@ page import="com.topcoder.dde.submission.Utility" %>

<%@ include file="/includes/util.jsp" %>
<%@ include file="/includes/session.jsp" %>
<%@ include file="/includes/formclasses.jsp" %>


<%
    // STANDARD PAGE VARIABLES
    String page_name = "s_about.jsp";
    String action = request.getParameter("a");

    MultipartRequest upload = null;
    try {
        upload = new MultipartRequest(request);
    } catch (InvalidContentTypeException e) {}
    boolean submit;
    long comp_ver_id = -1;
    long phase_id = -1;
    long version = -1;
    long component_id = -1;
    String comp_name = null;
    String comment= null;
    if (upload == null) {
        submit = false;
        try {
            component_id = Long.parseLong(request.getParameter("comp_id"));
        } catch (NullPointerException e) {
        } catch (NumberFormatException e) {}
        try {
            version = Long.parseLong(request.getParameter("version"));
        } catch (NullPointerException e) {
        } catch (NumberFormatException e) {}
        try {
            comp_ver_id = Long.parseLong(request.getParameter("compvers"));
        } catch (NullPointerException e) {
        } catch (NumberFormatException e) {}
        try {
            comp_name = request.getParameter("Project");
        } catch (NullPointerException e) {
        } catch (NumberFormatException e) {}
        try {
            phase_id = Long.parseLong(request.getParameter("phase"));
        } catch (NullPointerException e) {
        } catch (NumberFormatException e) {}
    } else {
        submit = true;
        try {
            component_id = Long.parseLong(upload.getParameter("comp_id"));
        } catch (NullPointerException e) {
        } catch (NumberFormatException e) {}
        try {
            version = Long.parseLong(upload.getParameter("version"));
        } catch (NullPointerException e) {
        } catch (NumberFormatException e) {}
        try {
            comp_ver_id = Long.parseLong(upload.getParameter("compvers"));
        } catch (NullPointerException e) {
        } catch (NumberFormatException e) {}
        try {
            comp_name = upload.getParameter("Project");
        } catch (NullPointerException e) {
        } catch (NumberFormatException e) {}
        try {
            phase_id = Long.parseLong(upload.getParameter("phase"));
        } catch (NullPointerException e) {
        } catch (NumberFormatException e) {}
        comment = upload.getParameter("comment");
    }
    RegistrationInfo regInfo = null;
    if (tcSubject == null || tcUser == null) {
        session.putValue("nav_redirect", "/catalog/s_upload_submission.jsp?compvers=" + comp_ver_id + "&Project=" + comp_name + "&phase=" + phase_id + "&comp_id=" + component_id + "&version=" + version);
        session.putValue("nav_redirect_msg", "You must login before you can upload submissions.");
        response.sendRedirect("/login.jsp");
    }
    if (tcUser != null) {
        regInfo = tcUser.getRegInfo();
        if (USER_MANAGER.getRatingForInquiry(tcUser.getId(),version,component_id) == 0) {
            response.sendRedirect("/catalog/s_norating.jsp");
        }
    }

    if (submit) {
        //do the submission
        boolean successful = false;
        try {
            successful = Utility.submit(upload,tcUser,comp_ver_id,phase_id,comment);
        } catch (Exception e) {
            e.printStackTrace(new PrintWriter(out));
        }
        if (successful) {
            try {
                TCSEmailMessage message = new TCSEmailMessage();
                message.addToAddress(regInfo.getEmail(), regInfo.getFirstName()+" "+regInfo.getLastName(), TCSEmailMessage.TO);
                message.setFromAddress("service@topcodersoftware.com", "TopCoder Software");
                message.setSubject("Your submission has been recieved");
                message.setBody("Your submission has been received.  Thank You!  Please check the Project Status (http://www.topcoder.com/tc?module=ContestStatus&ph=112 and http://www.topcoder.com/tc?module=ContestStatus&ph=113) page to keep up to date with the status of your project.  Also, a reminder that this is a competition, and only the 1st, 2nd and 3rd place finishers will receive payment.\n\nIf you have any questions please contact service@topcodersoftware.com\n\nThank you,\n\nThe TopCoder Software Team");
                EmailEngine.send(message);
            } catch (Exception e) {
//                e.printStackTrace(new PrintWriter(out));
            }
            response.sendRedirect("s_upload_success.jsp");
        } else {  // not successful
            response.sendRedirect("s_upload_error.jsp");
        }
    } else { // not submission
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <title>Upload a submission to TopCoder</title>
    <link rel="stylesheet" type="text/css" href="/includes/tcs_style.css" />
    <script language="JavaScript" type="text/javascript" src="/scripts/javascript.js"></script>
    <jsp:include page="/includes/header-files.jsp" />
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
                <jsp:param name="level1" value="catalog"/>
                <jsp:param name="level2" value="submission"/>
            </jsp:include>
        </td>
<!-- Left Column ends -->

<!-- Gutter begins -->
        <td width="15"><img src="/images/clear.gif" alt="" width="15" height="10" border="0" /></td>
<!-- Gutter ends -->

<!-- Middle Column begins -->
        <td width="99%">

        <form action="s_upload_submission.jsp" enctype="multipart/form-data" method="POST">
        <input type="hidden" name="compvers" value=<% out.print("\""+comp_ver_id+"\""); %>>
        <input type="hidden" name="Project" value=<% out.print("\""+comp_name+"\""); %>>
        <input type="hidden" name="phase" value=<% out.print("\""+phase_id+"\""); %>>
        <input type="hidden" name="comp_id" value=<%= component_id %>>
        <input type="hidden" name="version" value=<%= version %>>
        <table width="100%" border="0" cellpadding="0" cellspacing="0" class="middle">
                <tr><td height="15" colspan="2"><img src="/images/clear.gif" alt="" width="10" height="15" border="0" /></td></tr>
                <tr><td class="normal" width="246"><img src="/images/headUploadSubmission.gif" alt="Upload Submission" width="246" height="32" border="0" /></td>
                       <td class="subhead" align="left"><% out.print(comp_name); %></td>
                </tr>
                <tr colspan="2"><td ><img src="/images/clear.gif" alt="" width="10" height="10" border="0" /></td></tr>
            </table>



            <table width="100%" cellpadding="0" cellspacing="1" border="0" class="forumBkgd">
                <tr valign="top">
                <td>


            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="middle">

                <tr><td class="adminControl" width="100%" ><img src="/images/clear.gif" alt="" width="10" height="6" border="0" /></td></tr>
                <tr><td class="forumBkgd" width="100%" ><img src="/images/clear.gif" alt="" width="10" height="2" border="0" /></td></tr>

                <tr valign="top">
                    <td class="forumTextCenterEven" width="100%" >
                        <table cellpadding="0" cellspacing="0" border="0" width="70%" align="center">

                            <tr valign="top">
                                <td class="forumText">
                                    <p><strong>Comments</strong></p>
                                    <p><textarea class="adminControlForm" name="comment" rows="24" cols="90"></textarea></p>
                                    &nbsp;</td>
                            </tr>
                        </table></td>
                </tr>

                <tr><td  class="forumTextCenterEven" width="100%"><img src="/images/clear.gif" alt="" width="10" height="1" border="0" /></td></tr>

                <tr>
                    <td class="forumTextCenterEven" width="100%" >
                        <table cellpadding="0" cellspacing="2" border="0" align="center">
                            <tr><td class="forumTextEven"><strong>Upload File</strong></td></tr>
                            <tr><td class="forumText"><input type="file" name="file1" size="30" /></td></tr>
                        </table></td>
                </tr>

                <tr><td  class="forumTextCenterEven" width="100%"><img src="/images/clear.gif" alt="" width="10" height="5" border="0" /></td></tr>

                <tr><td class="adminControl" width="100%" ><input class="adminControlButton" type="submit" name="a" value="Submit"></input></td></tr>



            </table>

        </td>
        </tr>

        </table>
        </form>
<% } %>
        <table width="100%" border="0" cellpadding="0" cellspacing="0">
            <tr colspan="2"><td ><img src="/images/clear.gif" alt="" width="10" height="40" border="0" /></td></tr>
           </table>

        </td>

<!-- Middle Column ends -->

<!-- Gutter begins -->
        <td width="15"><img src="/images/clear.gif" alt="" width="15" height="10" border="0" /></td>
<!-- Gutter ends -->
    </tr>
</table>

<!-- Footer begins -->
<jsp:include page="/includes/foot.jsp" flush="true" />
<!-- Footer ends -->

</body>
</html>
