<%@ page contentType="text/html; charset=ISO-8859-1"
         import="com.topcoder.web.common.BaseServlet,
                 com.topcoder.web.onsite.controller.request.SubmitWager,
                 com.topcoder.web.onsite.Constants" %>
<jsp:useBean id="sessionInfo" class="com.topcoder.web.common.SessionInfo" scope="request" />
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%
  String nextpage = (String)request.getAttribute(BaseServlet.NEXT_PAGE_KEY);
  if(nextpage==null) nextpage = request.getParameter(BaseServlet.NEXT_PAGE_KEY);
  if(nextpage==null) nextpage = request.getHeader("Referer");
  if(nextpage==null) nextpage = "http://"+request.getServerName();
  String message = (String)request.getAttribute("message");
  if(message==null) message = "";
%>

<html>
<head>
<title>TopCoder | Wager</title>


</head>

<body>


<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr valign="top">


<!-- Gutter Begins -->
        <td valign="top"><img src="/i/clear.gif" width="10" height="1" alt="" border="0"></td>
<!-- Gutter Ends -->

<!-- Center Column begins -->
        <td width="100%"><img src="/i/clear.gif" width="400" height="11" alt="" border="0"><br>

Wager page

        </td>
<!-- Center Column ends -->

<!-- Gutter -->
        <td width="10"><img src="/i/clear.gif" width="10" height="1" alt="" border="0"></td>
<!-- Gutter Ends -->

<!-- Right Column Begins -->
        <td width="170"><img src="/i/clear.gif" width="170" height="1" alt="" border="0"></td>
<!-- Left Column Ends -->

<!-- Gutter -->
        <td width="10"><img src="/i/clear.gif" width="10" height="1" alt="" border="0"></td>
<!-- Gutter Ends -->

    </tr>
</table>


</body>
</html>

