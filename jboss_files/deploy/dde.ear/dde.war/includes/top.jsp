<%@ taglib uri="/WEB-INF/tc-webtags.tld" prefix="tc-webtag" %>
<%@ page import="com.topcoder.dde.user.User" %>
<%@ page import="com.topcoder.shared.util.ApplicationServer" %>
       <%@ include file="/includes/util.jsp" %>
       <%@ include file="/includes/session.jsp" %>
<%--Header begins--%>
<%
    boolean isHomePage = "true".equals(request.getParameter("isHomePage"));
%>
<%
    String TCDlevel = request.getParameter("TCDlevel") == null ? "" : request.getParameter("TCDlevel");

%>
<div id="header">
		<div class="wrapper">

<%-- MASTHEAD AND LOGO --%>
			<h1><a href="http://<%=ApplicationServer.SERVER_NAME%>/" title="TopCoder Direct"><span>TopCoder Direct</span></a></h1>
			<h2 id="ready_engage"><span>Ready.. ENGAGE</span></h2>

<%-- MAIN NAVIGATION --%>

			<div id="nav">
				<h3 class="hide">Main Navigation</h3>
				<ul>
					<li class="left"><a href="http://<%=ApplicationServer.SERVER_NAME%>/">TopCoder Home</a></li>
					<li class="on"><a href="http://<%=ApplicationServer.SOFTWARE_SERVER_NAME%>/">Engage</a>
                    	<ul>
                            <li><a href="http://<%=ApplicationServer.SERVER_NAME%>/direct/">TopCoder Direct</a></li>
                            <li><a href="http://<%=ApplicationServer.SOFTWARE_SERVER_NAME%>/catalog/">Components</a></li>
                            <li><a href="http://<%=ApplicationServer.SOFTWARE_SERVER_NAME%>/TCD/platform-tools.jsp">Platform Tools</a></li>
                            <li><a href="http://<%=ApplicationServer.SOFTWARE_SERVER_NAME%>/TCD/training.jsp">Training &amp; Mentoring</a></li>
                            <li><a href="http://<%=ApplicationServer.SOFTWARE_SERVER_NAME%>/TCD/support.jsp">Support &amp; Maintenance</a></li>
                            <li class="last-li"><a href="http://<%=ApplicationServer.SOFTWARE_SERVER_NAME%>/contact.jsp">Contact Us</a></li>
                        </ul>
                    </li>
					<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc">Compete</a>
                    	<ul>
                    	    <li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=ActiveContests&amp;pt=23">Conceptualization</a></li>
                    	    <li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=ActiveContests&amp;pt=6">Specification</a></li>
                    	    <li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=ActiveContests&amp;pt=7">Architecture</a></li>
                            <li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=ViewActiveContests&ph=112">Component Design</a></li>
                            <li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=ViewActiveContests&ph=113">Component Development</a></li>
                            <li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=ActiveContests&amp;pt=14">Assembly</a></li>
							<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/longcontest/?module=ViewActiveContests">Marathon Matches</a></li>
							<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/wiki/display/tc/Bug+Races">Bug Races</a></li>
                            <li class="last-li"><a href="http://<%=ApplicationServer.STUDIO_SERVER_NAME%>/?module=ViewActiveContests">Studio Competitions</a></li>
                    	</ul>
                    </li>
					<li><a href="http://<%=ApplicationServer.STUDIO_SERVER_NAME%>/">Studio</a>
                    	<ul>
                            <li><a href="http://<%=ApplicationServer.STUDIO_SERVER_NAME%>/?module=ViewActiveContests">Active Contests</a></li>
                            <li><a href="http://<%=ApplicationServer.SERVER_NAME%>/direct/">Launch a Contest</a></li>
                            <li><a href="http://<%=ApplicationServer.STUDIO_SERVER_NAME%>/forums">Studio Forums</a></li>
                            <li><a href="http://<%=ApplicationServer.STUDIO_SERVER_NAME%>/blog/">Studio Blog</a></li>
                            <li><a href="http://<%=ApplicationServer.STUDIO_SERVER_NAME%>/?module=MyStudioHome">My Studio</a></li>
                            <li class="last-li"><a href="http://<%=ApplicationServer.STUDIO_SERVER_NAME%>/?module=MyStudioHome">Contact Studio</a></li>
                    	</ul>
                    </li>
					<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc">Community</a>
                    	<ul>
                            <li><a href="http://<%=ApplicationServer.SERVER_NAME%>/reg/">Join TopCoder</a></li>
                            <li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=MyHome">My TopCoder</a></li>
                            <li><a href="http://<%=ApplicationServer.FORUMS_SERVER_NAME%>">TopCoder Forums</a></li>
                            <li class="last-li"><a href="http://<%=ApplicationServer.STUDIO_SERVER_NAME%>/forums">Studio Forums</a></li>
                       	</ul>
                    </li>

				</ul>
			</div><!-- #navigation ends -->


			<%-- SUB NAVIGATION --%>
			<div id="nav_support">
				<h3 class="hide">SUB Navigation</h3>
				<ul>
                	<li class="left"><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&d1=about&d2=index">About TopCoder</a></li>
					<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&amp;d1=pressroom&amp;d2=index">News</a></li>
					<li><a href="http://<%=ApplicationServer.SOFTWARE_SERVER_NAME%>/contact.jsp">Contact Us</a></li>


                           <% if (session.getAttribute("TCUSER") == null) { %>
                           <li class="right">
                 <a href="http://<%=ApplicationServer.SOFTWARE_SERVER_NAME%>/login.jsp">Login</a>
                 			</li>
     						<% } else { %>
                            <li>
               <a href="#">Hello, <tc-webtag:handle coderId='<%=((User) session.getAttribute("TCUSER")).getId()%>'/></a>
               				</li>
                            <li class="right">
                            	<a href="http://<%=ApplicationServer.SOFTWARE_SERVER_NAME%>/login.jsp?a=logout">Logout</a>
                            </li>
      						<% } %>
                    </li>
				</ul>
			</div><%-- #navigation ends --%>

		</div><%-- .wrapper ends --%>
	</div><%-- #header ends --%>