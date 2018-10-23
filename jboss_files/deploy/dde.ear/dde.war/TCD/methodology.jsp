

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
		"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
		
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<jsp:include page="/includes/header-files.jsp" />
<title>TopCoder Direct - About TopCoder Direct</title>
</head>
<body>
<jsp:include page="/includes/top.jsp">
<jsp:param name="TCDlevel" value="about"/>
</jsp:include>

<!-- CONTENT BLOCKS -->
	<div id="content">
		<div class="wrapper">
			
			<div id="content_main">
<!-- TABS BUTTON -->
				<div id="tabs_button">
					<h3 class="hide">Tabs Navigation</h3>
					<ul>
						<li id="tabs_intro" class="tabs_bar first"><a href="about.jsp"><span>Welcome to TopCoder Direct</span></a></li>
						<li id="tabs_tcdirect" class="tabs_bar"><a href="how.jsp"><span>How does TC Direct Work?</span></a></li>
						<li id="tabs_pricing" class="tabs_bar on"><a href="methodology.jsp"><span>The TopCoder Methodology</span></a></li>
					</ul>
				</div>
				
<!-- TABS CONTENT -->
				<div id="tabs_panel">
					
					
					<!-- *************************************************
					TABS 03
					************************************************** -->
					<div id="tabsContent03" class="tabs_content">
						<div class="content_columns">
							<h2><img src="/i/the_topcoder_methodology.png" width="284" height="27" alt="The TopCoder Methodology" /></h2>			
							<h3>How will this Global Community build my application?</h3>
							<p>Through this methodology, you have a consistent and clear mechanism to utilize the TopCoder Platform. We provide a distinct set of contests and tools to define the specific deliverables for your project, which are then presented to the TopCoder community through various competition types.</p>
							<p>The phases and competitions may be used in series from start to finish, individually or in creative combinations to complete your tasks in the most effective way possible.</p>
							<h3>The Process</h3>
							<div class="process">
								<h4 class="red"><img src="/i/specification.png" alt="Specification" class="app-icon" />Specification</h4>
								<p>Your application requirements are defined using standard UML, prototyping, written specifi cations and test plans. Like every phase of the TopCoder Methodology, outputs of the specifi cation process are peer reviewed and scored to ensure completeness and quality. Creating prototypes up front helps our clients visualize the look and feel of the application early and saves valuable time later in the process.</p>
							</div>
							<div class="process">
								<h4 class="red"><img src="/i/application_architecture.png" alt="Application Architecture" class="app-icon" />Application Architecture</h4>
								<p>The architecture of the application is defined using standard UML and Architecture Competitions. The application is broken down into a component-based architecture, relevant pre-built catalog components are identified and component specifi cations are developed for Component Production.</p>
							</div>
							<div class="process">
								<h4 class="red"><img src="/i/component_production.png" alt="Component Production" class="app-icon" />Component Production</h4>
								<p>The heart of the TopCoder methodology is component production. Each professionally packaged, high quality software component is produced as the result of Component Design, Component Development and Component Testing Competitions. Components fall into one of two categories: catalog or custom. Catalog components are generic and are added to the TopCoder Component Catalog whereas custom components become the intellectual property of the client.</p>
							</div>
							<div class="process">
								<h4 class="red"><img src="/i/assembly.png" alt="Assembly" class="app-icon" />Assembly</h4>
								<p>The assembly of your application comes through a series of competitions as well. First the components are assembled into functioning modules, then the modules into full applications. In many cases, groups of developers form within the community to compete collaboratively as teams.</p>
							</div>
							<div class="process">
								<h4 class="red"><img src="/i/certification.png" alt="Certification" class="app-icon" />Certification</h4>
								<p>This phase uses testing competitions to test and certify application functionality based on requirements. Certifi cation also leverages Bug Races to address component and application code issues.</p>
							</div>
							<div class="process">
								<h4 class="red"><img src="/i/deployment.png" alt="Deployment" class="app-icon" />Deployment</h4>
								<p>The last phase of The TopCoder Methodology results in the deployment and testing of the fi nal deliverables in the target environment. This is where the very best results from multiple competitions and competitors from around the world get delivered to your organization!</p><p>&nbsp;</p>
							</div>
							<h3>The Value</h3>
							<p>Centered on software best practices and proven engineering and manufacturing disciplines from a variety of industries, the TopCoder Methodology</p>
							<ul class="methodology_list">
								<li>Delivers quality through measurable performance reviews of all work product via an online scorecard system and automated peer review process</li>
								<li>Provides formal and consistent documentation for every work product delivered, meaning complete traceability from requirements through final deliverables</li>
								<li>Promotes speed and scalability through parallel competitions and a growing virtual community</li>
								<li>Incorporates constant measurement and analytics to ensure you are always making well educated decisions</li>
							</ul>

						</div><!-- .content_columns ends -->
					</div><!-- .tabs_content #03 -->
					
											
				</div><!-- #tabs_panel ends -->
			
			</div><!-- #content_main -->
			
		</div><!-- .wrapper ends -->
	</div><!-- #content ends -->

<!-- Footer begins -->
<jsp:include page="/includes/foot.jsp" flush="true" />
<!-- Footer ends -->
	
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