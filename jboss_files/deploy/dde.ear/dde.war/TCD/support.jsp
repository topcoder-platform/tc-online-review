<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
		"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
		
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<jsp:include page="/includes/header-files.jsp" />
<title>TopCoder Direct - Support &amp; Maintenance</title>

<script src="scripts/direct.js" type="text/javascript"></script>
</head>
<body>
<jsp:include page="/includes/top.jsp">
<jsp:param name="TCDlevel" value="support" />
</jsp:include>

<%-- CONTENT BLOCKS --%>
	<div id="content">
		<div class="wrapper">
		
			<div id="content_main">
<%-- TABS BUTTON --%>
				<div id="tabs_button">
					<h3 class="hide">Tabs Navigation</h3>
					<ul>
						<li id="tabs_intro" class="tabs_bar first"><a href="#tabsContent01"><span>Support &amp; Maintenance</span></a></li>
					</ul>
				</div>
				
<%-- TABS CONTENT --%>
				<div id="tabs_panel">
					
					<%-- *************************************************
					TABS 01
					************************************************** --%>
					<div id="tabsContent01" class="tabs_content">
					
						<div class="content_columns">
							<h2><img src="/i/support_and_maintenance.png" width="245" height="27" alt="Support &amp; Maintenance" /></h2>
							<p>With TopCoder's unique method of building and delivering software, it follows suit that there is a unique model to maintain and support the software that is produced.</p>
							<p>As a TopCoder customer, you have access to a number of support resources including on-site resources, online support, phone support, self-help documentation and tools.  Additionally, TopCoder has integrated its unique competition platform into the defect resolution process in the form of Bug Races to find and fix problems fast.</p>
							<p><strong>TopCoder applications average 1 defect per KLOC and 3:1 ratio of test code to application code.</strong></p>
							<p><strong>TopCoder is completing over 55 Bug Races per month</strong></p>

							<h3>Types of On-Site Support</h3>
							<h4 class="red">Bug Races</h4>
							<p>TopCoder's unique method of resolving software defects.</p>
							<ul class="bug_race_list">
								<li>You control the priority and cost of each fix</li>
								<li>Multiple people racing to fix your bug accurately as fast as possible</li>

								<li>The first person to pass regression tests (including a test case for the new issue) and fix the bug is paid.</li>
								<li>The length of time of each race depends on the complexity of the issue, but typical durections are under a few hours (for trivial issues), and less than 2 days (for more complex issues).</li>
							</ul>
							<h4 class="red">Knowledge Base Access</h4>
							<p>Access to all of TopCoder's documentation and "How-To's" so your team can efficiently utilize the TopCoder Platform to minimize the need for external support.</p>
							<h4 class="red">Online Support</h4>

							<p>The TopCoder Platform provides 24/7 access to online forums and instant messaging services to interact with TopCoder resources, including Co-Pilots. Talk to experts via forums that are specific to each component and project.</p>
							<h4 class="red">Co-Pilots</h4>
							<p>TopCoder resources are available virtually to service your support requests.  A co-pilot is assigned to your project and is responsible for working with your team to complete tasks at hand.</p>
							<h4 class="red">Telephone &amp; Email Support</h4>
							<p>Immediate access is available to TopCoder resources via 1-866-TOPCODER and/or <a href="mailto:service@topcoder.com">service@topcoder.com</a>.</p>
							<h4 class="red">Catalog Upgrades</h4>
							<p>Includes support of existing components in the TopCoder Catalog.</p>
							<h4 class="red">Alerts and Notifications</h4>
							<p>Choose to be alerted about upgrades and improvements to components you've purchased.</p>
							<h4 class="red">On-Site Support</h4>
							<p>TopCoder resources are deployable on-site to help troubleshoot and manage issues and/or change.</p>

						</div><%-- .content_columns --%>
					</div><%-- .tabs_content #04 --%>
						
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