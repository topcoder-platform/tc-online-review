

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
						<li id="tabs_tcdirect" class="tabs_bar on"><a href="how.jsp"><span>How does TC Direct Work?</span></a></li>
						<li id="tabs_pricing" class="tabs_bar"><a href="methodology.jsp"><span>The TopCoder Methodology</span></a></li>
					</ul>
				</div>
				
<!-- TABS CONTENT -->
				<div id="tabs_panel">
					
		
					
					<!-- *************************************************
					TABS 02
					************************************************** -->
					<div id="tabsContent02" class="tabs_content">
						<div class="content_columns">
							<h2><img src="/i/how_does_direct_works.png" width="315" height="27" alt="How does TC Direct Work?" /></h2>				
							<h3>Some of the most talented people in the world are ready to compete to build your next application.</h3>
							<ul class="bug_race_list">
                            	<li>On-demand access to the TopCoder community of 160,000+ developers, architects and graphic designers</li>
                            	<li>The tools to launch a wide variety of competitions to source high-quality products</li>
                                <li>Unlimited enterprise usage of the full TopCoder component catalog</li>
                            </ul>
							<h3>Subscribers to the TopCoder Global Platform have on-demand access to:</h3>
							<h4 class="red">The TopCoder Developer Network</h4>
							<p>Over 160,000 members strong, the TopCoder community represents your virtual workforce comprised of brilliant individuals from virtually every country on Earth.</p>
							<h4 class="red">Our Asset-Based Development Methodology</h4>
							<p>On average, 50% of your next application already exists in the TopCoder Component Catalog. These components are free to subscribers.</p>
							<h4 class="red">Software Built Through Competition</h4>
							<p>TopCoder is a true meritocracy, which means you only pay for the best.</p>

							<h3>Still not sure?</h3>
							<p>Don't worry, you can change your tier at any time!</p>

							</div><!-- .content_columns ends -->
					</div><!-- .tabs_content #02 -->
					
					
					
											
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