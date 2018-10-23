<%@ page import="javax.naming.*" %>
<%@ page import="javax.ejb.CreateException" %>
<%@ page import="java.io.*" %>
<%@ page import="java.rmi.*" %>
<%@ page import="javax.rmi.*" %>
<%@ page import="java.net.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.lang.reflect.*" %>

<%@ page import="com.topcoder.dde.catalog.*" %>

<%@ page import="com.topcoder.apps.review.document.*" %>

<%@ include file="/includes/util.jsp" %>
<%@ include file="session.jsp" %>
<%@ include file="/includes/formclasses.jsp" %>


<%
    // STANDARD PAGE VARIABLES
    String page_name = "scorecard.jsp";
    String action = request.getParameter("a");	

    if (session.getValue(/* "TCSUBJECT" */ "AdminTCSubject") == null) {
        session.putValue("nav_redirect", "/admin/scorecard_admin.jsp");
        session.putValue("nav_redirect_msg", "You must login first.");
        response.sendRedirect("" + "/admin/c_login.jsp");
        return;
    }
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/2000/REC-xhtml1-20000126/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>TopCoder Software</title>
<link rel="stylesheet" type="text/css" href="/includes/tcs_style.css" />
  <link rel="stylesheet" type="text/css" href="/includes/tcs_style.css"/>
<script language="JavaScript" type="text/javascript" src="/scripts/javascriptAdmin.js">
</script>


<%

Object objDMB = CONTEXT.lookup("com.topcoder.apps.review.document.DocumentManagerLocalHome");
DocumentManagerLocalHome home = (DocumentManagerLocalHome) PortableRemoteObject.narrow(objDMB, DocumentManagerLocalHome.class);
DocumentManagerLocal dmb = home.create();

String strError = "";
String strMessage = "";

%>
</head>
<body class="body">

<!-- Header begins -->
<%@ include file="/includes/adminHeader.jsp" %>
<%@ include file="/includes/adminNav.jsp" %>
<!-- Header ends -->


<% if (strError.length() > 0) { %>
<H3><FONT COLOR="RED"><%= strError %></FONT></H3>
<HR>
<% } %>

<% if (strMessage.length() > 0) { %>
<H3><%= strMessage %></H3>
<HR>
<% } %>

<H2> Scorecard Admin </H2>

<%
String aString = request.getParameter("a");
if (aString != null && aString.equals("status")) {
// change status on scorecard template
  String sidString = request.getParameter("sid");
  long sid = -1;
  if (sidString != null) {
    sid = Long.parseLong(sidString);
    ScorecardTemplate template = dmb.getScorecardTemplate(sid);
    int status = template.getStatus();
    if (status != 1) status = 1;
    else status = 2;
    template.setStatus(status);
    dmb.saveScorecardTemplate(template, false, false);
  }
} else if (aString != null && aString.equals("delete")) {
  String sidString = request.getParameter("sid");
  long sid = -1;
  if (sidString != null) {
    sid = Long.parseLong(sidString);
    ScorecardTemplate template = new ScorecardTemplate(-sid,"",0,0,0,null,false);
    dmb.saveScorecardTemplate(template, false, false);
  }
}  else if (aString != null && aString.equals("default")) {
    String sidString = request.getParameter("sid");
    long sid = -1;
    if (sidString != null) {
        sid = Long.parseLong(sidString);
        ScorecardTemplate template = dmb.getScorecardTemplate(sid);
        if(template.isDefault())
        {
            template.setDefault(false);
        } else {
            template.setDefault(true);
        }
        dmb.saveScorecardTemplate(template, false, false);
    }
}

ScorecardTemplate[] scorecards = dmb.getScorecardTemplates();
%>

<a href="/admin/scorecard.jsp">Create a new scorecard</a>
<br><br>

<table border>
  <tr>
    <td><strong>Scorecard Name</td>
    <td><strong>Project Type</td>
    <td><strong>Scorecard Type</td>
    <td><strong>Status</td>
    <td><strong>Action</td>
    <td><strong>Default</td>
    <td><strong>Action</td>
    <td><strong>Delete</td>
  </tr>
<%
for (int idx=0; idx<scorecards.length; idx++) {
  ScorecardTemplate sc = scorecards[idx];
  String tStatus = "";
  if (sc.getStatus() == 0) tStatus = "Editing";
  else if (sc.getStatus() == 1) tStatus = "Active";
  else if (sc.getStatus() == 2) tStatus = "Inactive";
  String[] ptOptions = {"Design","Development"};
  String[] stOptions = {"Screening","Review"};
  String statusButton = "Activate";
  if (sc.getStatus() == 1) statusButton = "Inactivate";
  
  String defaultButton = "Clear Default";
  if (!sc.isDefault()) defaultButton = "Make Default";
%>
  <tr>
    <td><a href="/admin/scorecard.jsp?sid=<%=sc.getId()%>"><%=sc.getName()%></a></td>
    <td><%=ptOptions[sc.getProjectType()-1]%></td>
    <td><%=stOptions[sc.getScorecardType()-1]%></td>
    <td><%=tStatus%></td>
    <td><a href="/admin/scorecard_admin.jsp?a=status&sid=<%=sc.getId()%>">
    <%=statusButton%></a></td>
    
    <td>
    <%if (sc.isDefault()) {%>
        Yes
    <%} else {%>
        No
    <%} %>
    </td>
    <td><a href="/admin/scorecard_admin.jsp?a=default&sid=<%=sc.getId()%>">
    <%=defaultButton%></a></td>
    <td>
<% if (sc.getStatus() == 0) { %>
    To delete template:
    <a href="/admin/scorecard_admin.jsp?a=delete&sid=<%=sc.getId()%>"><strong>DELETE</strong></a>
<% } %>
    </td>
  </tr>
<% } %>
</table>

</body>
</html>
