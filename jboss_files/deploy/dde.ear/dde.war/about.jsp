<%@ page import="com.topcoder.shared.util.ApplicationServer" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
		"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
		
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<jsp:include page="/includes/header-files.jsp" />
<title>TopCoder Direct - About TopCoder</title>

<script src="/scripts/direct.js" type="text/javascript"></script>
</head>
<body>
<jsp:include page="/includes/top.jsp">
<jsp:param name="TCDlevel" value="about-coder" />
</jsp:include>


<%-- CONTENT BLOCKS --%>
	<div id="content">
		<div class="wrapper">
		
			<div id="content_main">
<%-- TABS BUTTON --%>
				<div id="tabs_button">
					<h3 class="hide">Tabs Navigation</h3>
					<ul>
						<li id="tabs_intro" class="tabs_bar first"><a href="#tabsContent01"><span>About TopCoder</span></a></li>
					</ul>
				</div>
				
<%-- TABS CONTENT --%>
					<div id="tabs_panel">
						<%-- *************************************************
						TABS 01
						************************************************** --%>
						<div id="tabsContent01" class="tabs_content">
							<div class="content_columns">
								
						<p>TopCoder is fast becoming the major league for programming competitions. TopCoder brings members together once a week to compete online
						&#40;Single Round Match&#41; and twice a year both online and on location &#40;Tournaments&#41;.</p>
	
						<h4>Why Online Competitions</h4>
	
						<p>Competitions provide an understanding of a person's capabilities through a demonstration of skill. What was lacking in the world of programming
						competitions was a format that offered immediate and objective scoring. The solution was the creation of a "Single Round Match".</p>
	
						<p>In addition to regular Single Round Matches, TopCoder holds two major multiple-round, elimination tournaments each year.  These tournaments span many weeks and include significant prize purses along three independent tracks of competition: algorithm, component design, and component development<br />
						</p>
	
						<h4>How Companies Benefit</h4>
	
						<ul>
							<li><a href="http://www.topcoder.com/corp/?module=Static&d1=corp&d2=tces_home"><strong>Employment Services</strong></a><br />
							The market for developers is either feast or famine. Regardless of how many developers there are or how many are available,
							the issue remains the same&#151;how to determine which candidate is best suited to the needs of your organization. While
							certification has offered some credibility, competitions that test a developer's skills in real-world challenges goes much
							further to differentiating one candidate from another.</li><br />
	
							<li><a href="http://software.topcoder.com/index.jsp"><strong>Software</strong></a><br />
							The best way to drive down the cost of software development is through re-use. The <a href="http://software.topcoder.com/components/index.jsp">Component Catalog</a>
							is a tool for increasing re-use and developer productivity within an organization. The component catalog continues to grow on a weekly basis as component
							requirements are generated through requests from catalog customers, research from TopCoder Product Managers and from application development.</li><br />
	
							<li><a href="http://www.topcoder.com/corp/?module=Static&d1=corp&d2=spon_prog&d3=index"><strong>Sponsorship</strong></a><br />
							Reaching the developer community is a difficult task at best. Marketing budgets are limited and with the advent of the Internet,
							the expectation for measuring results has only grown. TopCoder offers companies a means of targeting this desired community
							through the targeted sponsorship of Single Round Matches
							and <a href="http://www.topcoder.com/corp/?module=Static&d1=corp&d2=spon_prog&d3=tourny_index">Tournaments.</a></li>
							</ul>
	
						  <h4>How Members Benefit</h4>
						  <ul>
							<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&amp;d1=about&amp;d2=whyjoin"><strong>Competition</strong></a><br />
							  Each Single Round Match offers a fun, time-based, and challenging set of problems that put a developer's skills to the test. This, coupled
							  with Divisions, offer a more level field of competition for newer members in order to build ratings and move into potential <a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&amp;d1=tournaments&amp;d2=home">Tournament</a> opportunities.</li>
							<br />
							<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=ViewActiveContests&amp;ph=112"><strong>Design</strong></a> and <a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=ViewActiveContests&amp;ph=113"><strong>Development</strong></a><br />
							  Rated TopCoder members are eligible to participate in TopCoder Component Development. Members submit design and development solutions
							  for these challenging and potentially lucrative projects. Winning solutions are rewarded with cash payouts and royalties. For every
							  commercial sale, TopCoder will pay the members who designed and developed the component a percentage of the sale. </li>
							</ul>
						  <p>&nbsp;</p>
						  <ul>
					    <br />
					      </ul>
					  </div><%-- .content_columns --%>
						
					</div><%-- .tabs_content #01 --%>
						
				</div><%-- #tabs_panel ends --%>
			
			</div><%-- #content_main --%>
			
		</div><%-- .wrapper ends --%>
	</div><%-- #content ends --%>
	
<%-- Footer begins --%>
<jsp:include page="/includes/foot.jsp" flush="true" />
<%-- Footer ends --%>
	
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