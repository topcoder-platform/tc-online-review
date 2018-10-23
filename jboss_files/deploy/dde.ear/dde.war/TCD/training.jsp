<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
		"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
		
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<jsp:include page="/includes/header-files.jsp" />
<title>TopCoder Direct - Training and Mentoring</title>

<script src="scripts/direct.js" type="text/javascript"></script>
</head>
<body>
<jsp:include page="/includes/top.jsp">
<jsp:param name="TCDlevel" value="training"/>
</jsp:include>


<%-- CONTENT BLOCKS --%>
	<div id="content">
		<div class="wrapper">
		
			<div id="content_main">
<%-- TABS BUTTON --%>
				<div id="tabs_button">
					<h3 class="hide">Tabs Navigation</h3>
					<ul>
						<li id="tabs_intro" class="tabs_bar first"><a href="#tabsContent01"><span>Training and Mentoring</span></a></li>
					</ul>
				</div>
				
<%-- TABS CONTENT --%>
				<div id="tabs_panel">
					
					<%-- *************************************************
					TABS 01
					************************************************** --%>
					<div id="tabsContent01" class="tabs_content">
					
						<div class="content_columns">
							<h2><img src="/i/training_and_mentoring.png" width="241" height="27" alt="Training and Mentoring" /></h2>
							<h3>Overview</h3>
							<p>Because TopCoder's method of building and delivering software is unique, clients can take advantage of training and mentoring their project's personnel. Think of it as TopCoder 101 to jump start your team's proficiency.</p>
						  <p>Our training program can take the form of traditional classroom-style training with a qualified TopCoder educator on-site or via a self-paced program allowing participants to engage with the materials any time of the day.</p>
						  <p>Each participant is paired with a TopCoder mentor who will help guide them through the training process by monitoring progress, reviewing periodic deliverables and answering questions. This helps to ensure you are maximizing your investment in the  Platform subscription.</p>

						  <h3>Training Tracks</h3>
						  <h4 class="red">Project Management</h4>
						  <p>Project Managers are the primary resource that interact with clients.  Essentially, they create a bridge between TopCoder clients who need software built and the TopCoder member base.  Establishing and managing client relationships is a critical part of success.  Without a solid working relationship it is difficult to effectively deliver systems with success.</p>
						  <h4 class="red">Application Architecture</h4>
						  <p>An application architect typically joins a project full time during the architecture phase of application production. However, an architect may be involved as early as the proposal stage. Often, a senior architect and/or the architect expected to support the project will be present at proposal meetings to help scope the project and provide technical insight to the requirements involved.</p>
						  <h4 class="red">Information Architecture</h4>
						  <p>An information architect works with the project manager and client in ensuring a successful project. The IA gathers project information, evaluates the IA and/or TopCoder Studio needs for a project. The IA is involved in every step of the project from information gathering, wireframe development, design and prototyping. When the project moves into assembly, the IA continues to help with any issues that may arise.</p>
						  <h4 class="red">Deployment Engineering</h4>
						  <p>The deployment engineer works with the delivery team and client to gather deployment related requirements for the project. The DE will create and manage the development and test environments on which the delivery team will debug, test, and certify the project. Also, the DE creates documentation to ensure that other resources at TopCoder or the client’s site can replicate a deployment and its development environment.</p>

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