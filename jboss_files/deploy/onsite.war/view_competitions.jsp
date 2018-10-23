<%@ page import="com.topcoder.shared.util.ApplicationServer"%>
<%@ page contentType="text/html; charset=ISO-8859-1"
         import="com.topcoder.web.common.BaseServlet,
                 com.topcoder.web.onsite.Constants" %>
<%@ page import="com.topcoder.shared.dataAccess.resultSet.ResultSetContainer" %>
<jsp:useBean id="sessionInfo" class="com.topcoder.web.common.SessionInfo" scope="request" />
<%@ taglib uri="rsc-taglib.tld" prefix="rsc" %>
<%@ taglib uri="tc-webtags.tld" prefix="tc-webtag" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%
  String nextpage = (String)request.getAttribute(BaseServlet.NEXT_PAGE_KEY);
  if(nextpage==null) nextpage = request.getParameter(BaseServlet.NEXT_PAGE_KEY);
  if(nextpage==null) nextpage = request.getHeader("Referer");
  if(nextpage==null) nextpage = "http://"+request.getServerName();
  String minWagerAmount = (String)request.getAttribute(Constants.MIN_WAGER_AMOUNT_KEY);
  String maxWagerAmount = (String)request.getAttribute(Constants.MAX_WAGER_AMOUNT_KEY);
%>

<html>
    <% ResultSetContainer currentCompetitions = (ResultSetContainer) request.getAttribute(Constants.CURRENT_COMPETITION_RESULT_KEY);
        ResultSetContainer wagerHistory = (ResultSetContainer) request.getAttribute(Constants.WAGER_HISTORY_KEY);%>
<head>
<title>2007 TopCoder Open - Computer Programming Tournament</title>
<link type="image/x-icon" rel="shortcut icon" href="http://<%=ApplicationServer.SERVER_NAME%>/i/favicon.ico"/>
<%--
<style TYPE="text/css">
html{ 
margin:0px;
padding:0px;
font-family: Arial, Verdana, Helvetica, sans-serif;
}

body{ 
font-family: Arial, Verdana, Helvetica, sans-serif;
line-height: normal;
font-size: 12px;
color: #666666;
background: #FFA500;
margin:0px;
padding:0px;
}

img{
border: none;
display: block;
}

h1{
color: #333333;
font-style: italic;
font-size: 125%;
}

p{
padding: 0px;
margin: 0px 0px 10px 0px;
}

td, th{ 
line-height: normal;
font-size: 12px;
}

#content{
width: 700px;
margin: 20px;
background: #FFFFFF;
border: 4px solid #cc6600;
}
</style>
--%>
<link type="text/css" rel="stylesheet" href="http://<%=ApplicationServer.SERVER_NAME%>/css/tournaments/tccc07.css"/>
<link type="text/css" rel="stylesheet" href="http://<%=ApplicationServer.SERVER_NAME%>/css/coders.css"/>
        <SCRIPT type="text/javascript">
            function submitEnter(e) {
                var keycode;
                if (window.event) keycode = window.event.keyCode;
                else if (e) keycode = e.which;
                else return true;
                if (keycode == 13) {
                    document.frmWager.submit();
                    return false;
                } else return true;
            }
        </SCRIPT>
    </head>
<body>
<%--
<div align="center" style="background: transparent;">
    <div id="content">
        <div style="margin: 20px;" align="left">
--%>
<div align="center" style="background: transparent;">
    <div id="containAll">
    <div id="content">

<div style="float:right;">
<a href="http://<%=ApplicationServer.SERVER_NAME%>/"><img style="margin-right:30px;" src="http://<%=ApplicationServer.SERVER_NAME%>/i/tournament/tccc07/topcoderlogo.png" border=0/></a>
</div>
<A href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&d1=tournaments&d2=tccc07&d3=about"><img style="margin-left:40px;" src="http://<%=ApplicationServer.SERVER_NAME%>/i/tournament/tccc07/tourneyLogo.png" border=0/></A>

    <div style="float:right; margin-right:40px;">
        <% if (sessionInfo.isAnonymous()) { %>
            <a href="<jsp:getProperty name="sessionInfo" property="servletPath"/>?module=Login&nextpage=<jsp:getProperty name="sessionInfo" property="servletPath"/>">Login</a>
        <% } else { %>
            Logged in as <tc-webtag:handle coderId='<%=sessionInfo.getUserId()%>' darkBG="true"/>
            | <a href="<jsp:getProperty name="sessionInfo" property="servletPath"/>?module=Logout&nextpage=<jsp:getProperty name="sessionInfo" property="servletPath"/>">Logout</a>
        <% } %>
    </div>
   <h1 style="margin:80px 0px 0px 40px; padding:20px 0px 0px 20px;">Component Competition Wager System</h1>


    <div align="center" style="margin: 40px 0px 80px 0px;">
            <% if (currentCompetitions != null && currentCompetitions.size() > 0) {%>
                <% if (wagerHistory != null && wagerHistory.size() > 0) {%>
                You have already wagered on the following competitions:
               <br><br>
               <table width="400" border="0" cellpadding="6" cellspacing="2" class="sidebarBox">
                    <tr valign="top">
                        <td class="sidebarTitle">
                            Contest
                        </td>
                        <td class="sidebarTitle" align="right">
                            Wager amount
                        </td>
                    </tr>
                    <rsc:iterator list="<%=wagerHistory%>" id="resultRow">
                        <tr valign="top">
                            <td class="sidebarText">
                                <rsc:item name="contest_name" row="<%=resultRow%>"/>
                            </td>
                            <td class="sidebarText" align="right">
                                <rsc:item name="wager_amount" row="<%=resultRow%>"/>
                            </td>
                        </tr>
                    </rsc:iterator>
                </table>
                <% } else {%>
                This is the first competition you are wagering on.
                <% } %>
               <br><br>
               Please enter your wager for:
               <strong>
               <rsc:item set="<%=currentCompetitions%>" name="contest_name"/>
               </strong><br>
               You may wager between <strong><%=minWagerAmount%></strong> and <strong><%=maxWagerAmount%></strong>
              <form method="post" name="frmWager" action="<jsp:getProperty name="sessionInfo" property="servletPath"/>">
                  <tc-webtag:hiddenInput name="<%=Constants.PROJECT_ID_KEY%>"/>
                  <tc-webtag:hiddenInput name="<%=Constants.MODULE_KEY%>"/>
                      <table border="0" cellpadding="3" cellspacing="0">
                          <tr valign="middle">
                              <td nowrap class="bodyText" align="right">Amount:</td>
                              <td colspan="2" align="left"><input type="text" name="<%=Constants.WAGER_AMOUNT_KEY%>" value="" maxlength="2" size="2" onkeypress="submitEnter(event)"></td>
                              <td nowrap class="bodyText">&#160;&#160;<a href="JavaScript:document.frmWager.submit()">Wager&#160;&gt;</a></td>
                          </tr>
                      </table>
                      <script>
                          document.frmWager.<%=Constants.WAGER_AMOUNT_KEY%>.focus();
                      </script>
              </form>
            <% } else {%>
               You do not currently have competitions to wager on.
               <br><br>
               <a href="<jsp:getProperty name="sessionInfo" property="servletPath"/>">Back to menu<a/>
            <% } %>
    </div>
    <div style="height:20px;">&nbsp;</div>

        </div>
    </div>
</div>

</body>
</html>
