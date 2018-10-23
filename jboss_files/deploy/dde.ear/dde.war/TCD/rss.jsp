<%@ page import="com.topcoder.dde.user.User" %>
<%@ page import="com.topcoder.shared.util.ApplicationServer" %>
       <%@ include file="/includes/util.jsp" %>
       <%@ include file="/includes/session.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
		"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
		
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<jsp:include page="/includes/header-files.jsp" />
<title>TopCoder Direct</title>

<script src="scripts/direct.js" type="text/javascript"></script>
</head>
<body>
<jsp:include page="/includes/top.jsp">
<jsp:param name="TCDlevel" value="support	"/>
</jsp:include>

<%-- CONTENT BLOCKS --%>
	<div id="content">
		<div class="wrapper">
		
			<div id="content_main">
<%-- TABS BUTTON --%>
				<div id="tabs_button">
					<h3 class="hide">Tabs Navigation</h3>
					<ul>
						<li id="tabs_intro" class="tabs_bar first"><a href="#tabsContent01"><span>RSS &amp; Calendar Feeds</span></a></li>
					</ul>
				</div>
				
<%-- TABS CONTENT --%>
				<div id="tabs_panel">
					
					<%-- *************************************************
					TABS 01
					************************************************** --%>
					<div id="tabsContent01" class="tabs_content">
					
						<div class="content_columns">
							<h2>RSS &amp; Calendar Feeds</h2>
						  <h3>Studio RSS</h3>
                          <ul class="rss_links">
                              <li><a href="http://www.topcoder.com/tc?module=BasicRSS&amp;c=rss_active_contests&amp;dsid=33">Active Contests</a></li>
                              <li><a href="http://studio.topcoder.com/forums?module=RSS&amp;categoryID=1">Studio Forums</a></li>
                              <li><a href="http://www.topcoder.com/wiki/display/tc/Upcoming+Contests">Upcoming Contests</a></li>
                          </ul>
                          <h3>TopCoder Feeds</h3>
                          <ul class="rss_links">
                          	<li><a href="http://www.topcoder.com/tc?module=ViewActiveContests&ph=112">Active Contests</a></li>
                            <li><a href="http://forums.topcoder.com/?module=RSS&categoryID=1">TopCoder Forums</a><br />
                            </li><li><a href="http://www.topcoder.com/tc?module=ViewArchitectureActiveContests">Software Architecture Active Contests</a></li>
							<li><a href="http://www.topcoder.com/tc?module=ViewActiveContests&ph=112">Software Design Active Contests</a></li>
						    <li><a href="http://www.topcoder.com/tc?module=ViewActiveContests&amp;ph=113">Software Development Active Contests</a></li>
                            <li><a href="http://www.topcoder.com/tc?module=ActiveContests&amp;pt=14">Software Assembly Active Contests</a></li>
                            <li><a href="http://www.topcoder.com/tc?module=ActiveContests&amp;pt=13">Application Testing Active Contests</a></li>
                            <li><a href="http://www.topcoder.com/wiki/display/tc/Active+Bug+Races">Bug Races Active Contests</a> </li>
                            <li><a href="http://www.topcoder.com/wiki/display/tc/Upcoming+Review+Positions+RSS">Upcoming Review Positions</a></li>
                          </ul>
                          <h3>TopCoder Blogs</h3>
                          <ul class="rss_links">
                       	      <li><a href="http://tcstudioblogs.com/?feed=rss2">Studio Blog</a></li>
                               <li><a href="http://topcoderblogs.com/direct/?feed=rss2">TopCoder Direct Blog</a></li>
                          </ul>
                          
                          <h3>Calendar Feeds</h3>
                          <ul class="rss_links">
                            <li>  <% if (session.getAttribute("TCUSER") == null) { %>
                     <a href="http://<%=ApplicationServer.SOFTWARE_SERVER_NAME%>/login.jsp">Login</a> to see your calendar feed
     						<% } else { %>
               <a href="webcal://www.topcoder.com/tc?module=BasicRSS&c=project_calendar&dsid=28&d1=icalendar&d2=default&uid=<%=((User) session.getAttribute("TCUSER")).getId()%>"><%=((User) session.getAttribute("TCUSER")).getRegInfo().getUsername()%>'s custom calendar feed</a></li>
      						<% } %>
                          </ul>
					  </div><%-- .content_columns --%>
					</div><%-- .tabs_content #04 --%>
						
				</div><%-- #tabs_panel ends --%>
			
			</div><%-- #content_main --%>
			
		</div><%-- .wrapper ends --%>
	</div><%-- #content ends --%>

<%-- Footer begins --%>
<jsp:include page="/includes/foot.jsp" flush="true" />
<%-- Footer ends s --%>
	
<script type="text/javascript">
//<![CDATA[
var tabsBar = ["tabs_button"];
var tabsPanel = ["tabs_panel"];
var defaultTab = 0;	// index of default tab started from 0

window.onload = initDocument;
//]]>
</script>
	
</body>
</html>