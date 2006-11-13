<%@ page import="com.topcoder.shared.util.ApplicationServer" %>
<%@ taglib prefix="html" uri="/tags/struts-html" %>
	<script language="JavaScript" type="text/javascript" src="<html:rewrite href='/js/arena.js' />"></script>
	<script language="javascript" type="text/javascript">
	<!--
		function toggleMenu(menuTitle,menuID) {
			var menu = document.getElementById(menuID);
			if(menu.style.display == 'block') menu.className = 'CLOSED';
			else if(menu.className == 'OPEN' && menu.style.display != 'none') menu.className = 'CLOSED';
			else { menu.className = 'OPEN'; }
			
			if(menuTitle.blur) menuTitle.blur();
			if(menuTitle.className == 'exp') menuTitle.className = 'exp_ed';
			else menuTitle.className = 'exp';
			return;
		}
		
		function flipMenu(myMenuName) {
			var menuName = document.getElementById(myMenuName);
			menuName.className = 'exp_ed';
		}
	// -->
	</script>

	<!--node is des_contest_status -->
	<div style="float: left; padding: 0px 0px 0px 0px;">
		<html:img src="/i/interface/leftnav_top.gif" alt="" /></div>
	<div id="navbar">

	<ul>
		<li class="exp_ed">
			<a href="javascript:void(0)" onClick="Javascript:toggleMenu(this.parentNode,'m_competitions')">Competitions</a>
			<ul id="m_competitions" class="OPEN">
				<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc">Home</a></li>
				<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/Registration">Register to Compete</a></li>
				<li class="exp"><a href="javascript:void(0)" onClick="Javascript:toggleMenu(this.parentNode,'m_competitor_stats')">Competitor Statistics</a>
					<ul id="m_competitor_stats">
						<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=ViewAdvanced">Find a Competitor</a></li>
						<li class="exp"><a href="javascript:void(0)" onClick="Javascript:toggleMenu(this.parentNode,'m_top_rated_competitors')">Top Ranked</a>
							<ul id="m_top_rated_competitors">
								<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=AlgoRank">Algorithm</a></li>
								<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/stat?c=top_designers">Design</a></li>
								<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/stat?c=top_developers">Development</a></li>
							</ul>
						</li>
						<li class="exp"><a href="javascript:void(0)" onClick="Javascript:toggleMenu(this.parentNode,'m_recordbook')">Record Book</a>
							<ul id="m_recordbook">
								<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&amp;d1=statistics&amp;d2=recordbook_home">Algorithm</a></li>
								<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&amp;d1=compstats&amp;d2=comp_recordbook_home">Component</a></li>
							</ul>
						</li>
						<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=SimpleStats&amp;trans=true&amp;c=com_history&amp;d1=statistics&amp;d2=coderOfMonth">Coder of the Month</a></li>
					</ul>
				</li>
				<li class="exp"><a href="javascript:void(0)" onClick="Javascript:toggleMenu(this.parentNode,'m_algo_competitions')">Algorithm</a>
					<ul id="m_algo_competitions">
						<li><a href="javascript:arena();">Compete<br />(Launch Arena)</a></li>
						<li class="exp"><a href="javascript:void(0)" onClick="Javascript:toggleMenu(this.parentNode,'m_algo_stats')">Statistics</a>
							<ul id="m_algo_stats">
								<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=MatchList">Match Archive</a></li>
								<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/stat?c=round_overview">Match Overviews</a></li>
								<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=SrmDivisionWins">Match Winners</a></li>
								<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/stat?c=last_match">Match Results</a></li>
								<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&amp;d1=match_editorials&amp;d2=archive">Match Editorials</a></li>
								<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=ProblemArchive">Problem Archive</a></li>
							</ul>
						</li>
					</ul>
				</li>
				<li class="exp"><a href="javascript:void(0)" onClick="Javascript:toggleMenu(this.parentNode,'m_des_competitions')">Software Design</a>
					<ul id="m_des_competitions" class="exp">
						<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=ViewActiveContests&amp;ph=112">Active Contests</a></li>
						<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=ContestStatus&amp;ph=112">Contest Status</a></li>
						<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=ViewReviewProjects&amp;ph=112">Review Opportunities</a></li>
						<li><a href="http://<%=ApplicationServer.SOFTWARE_SERVER_NAME %>/forum/c_active_collab.jsp?ft=2">TCS Developer Forums</a></li>
						<li><a href="http://<%=ApplicationServer.SOFTWARE_SERVER_NAME %>/review">Submit Deliverables &amp; Reviews</a></li>
						<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=CompList&amp;ph=112">Statistics</a></li>
						<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=ReviewBoard&amp;ph=112">Meet the Review Board</a></li>
					</ul>
				</li>
				<li class="exp"><a href="javascript:void(0)" onClick="Javascript:toggleMenu(this.parentNode,'m_dev_competitions')">Software Development</a>
					<ul id="m_dev_competitions">
						<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=ViewActiveContests&amp;ph=113">Active Contests</a></li>
						<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=ContestStatus&amp;ph=113">Contest Status</a></li>
						<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=ViewReviewProjects&amp;ph=113">Review Opportunities</a></li>
						<li><a href="http://<%=ApplicationServer.SOFTWARE_SERVER_NAME %>/forum/c_active_collab.jsp?ft=2">TCS Developer Forums</a></li>
						<li><a href="http://<%=ApplicationServer.SOFTWARE_SERVER_NAME %>/review">Submit Deliverables &amp; Reviews</a></li>
						<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=CompList&amp;ph=113">Statistics</a></li>
						<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=ReviewBoard&amp;ph=113">Meet the Review Board</a></li>
					</ul>
				</li>
				<li class="exp"><a href="javascript:void(0)" onClick="Javascript:toggleMenu(this.parentNode,'m_assembly_competitions')">Software Assembly</a>
					<ul id="m_assembly_competitions">
						<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&d1=dev&d2=assembly&d3=overview">Overview</a></li>
						<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&d1=dev&d2=assembly&d3=activeContests">Active Contests</a></li>
					</ul>
				</li>
				<li class="exp"><a href="javascript:void(0)" onClick="Javascript:toggleMenu(this.parentNode,'m_testing_competitions')">Software Testing</a>
					<ul id="m_testing_competitions">
						<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&d1=dev&d2=testing&d3=overview">Overview</a></li>
					</ul>
				</li>
				<li class="exp"><a href="javascript:void(0)" onClick="Javascript:toggleMenu(this.parentNode,'m_long_contests')">Marathon Matches</a>
					<ul id="m_long_contests">
						<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/longcontest/?module=ViewActiveContests">Compete</a></li>
						<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/longcontest/?module=ViewPractice&amp;rt=14">Practice</a></li>
						<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/longcontest/?module=Static&amp;d1=intel_overview">Intel Multi-Threading Competition Series</a></li>
						<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/longcontest/?module=ViewActiveContests&amp;rt=15">Intel Active Contests</a></li>
						<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/longcontest/?module=ViewPractice&amp;rt=16">Intel Practice</a></li>
						<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/longcontest/?module=ViewQueue">Queue Status</a></li>
						<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/longcontest/?module=Static&amp;d1=match_editorials&amp;d2=archive">Match Editorials</a></li>
					</ul>
				</li>
				<li class="exp"><a href="javascript:void(0)" onClick="Javascript:toggleMenu(this.parentNode,'m_tournaments')">Tournaments</a>
					<ul id="m_tournaments">
						<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&amp;d1=tournaments&amp;d2=home">TopCoder</a></li>
						<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/pl/">Powered by TopCoder</a></li>
						<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=CRPFStatic&amp;d1=crpf&amp;d2=crpf_overview">Charity</a></li>
					</ul>
				</li>
				<li class="exp"><a href="javascript:void(0)" onClick="Javascript:toggleMenu(this.parentNode,'m_edu_content')">Educational Content</a>
					<ul id="m_edu_content">
						<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&amp;d1=tutorials&amp;d2=alg_index">Algorithm Tutorials</a></li>
						<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&amp;d1=tutorials&amp;d2=comp_index">Component Tutorials</a></li>
						<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&amp;d1=features&amp;d2=archive">Features</a></li>
						<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&amp;d1=tutor_transcripts&amp;d2=index">Tutor Transcripts</a></li>
					</ul>
				</li>
				<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?&module=SurveyList">Surveys</a></li>
					<li class="exp"><a href="javascript:void(0)" onClick="Javascript:toggleMenu(this.parentNode,'m_my_tc')">My TopCoder</a>
						<ul id="m_my_tc">
							<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/Registration?update=true">Update My Profile</a></li>
							<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=ViewReferrals">Members I've Referred</a></li>
							<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/PactsMemberServlet?t=affidavit&amp;c=affidavit_history">Affidavits</a></li>
							<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&amp;d1=card&amp;d2=description">Cards / Badges</a></li>
						</ul>
					</li>
					<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&amp;d1=calendar&amp;d2=thisMonth">Event Calendar</a></li>
					<li class="exp"><a href="javascript:void(0)" onClick="Javascript:toggleMenu(this.parentNode,'m_support')">Support / FAQs / Docs</a>
						<ul id="m_support">
							<li class="exp"><a href="javascript:void(0)" onClick="Javascript:toggleMenu(this.parentNode,'m_algo_support')">Algorithm</a>
								<ul id="m_algo_support">
									<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&amp;d1=help&amp;d2=index">How to Compete</a></li>
									<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&amp;d1=help&amp;d2=faqIndex">FAQs</a></li>
									<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&amp;d1=help&amp;d2=sampleProblems">Sample Problems</a></li>
									<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&amp;d1=help&amp;d2=ratings">Rating System</a></li>
									<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&amp;d1=help&amp;d2=algPayment">How to Get Paid</a></li>
									<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&amp;d1=help&amp;d2=charity">Charity Donations</a></li>
								</ul>
							</li>
							<li class="exp"><a href="javascript:void(0)" onClick="Javascript:toggleMenu(this.parentNode,'m_des_support')">Design</a>
								<ul id="m_des_support">
									<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&amp;d1=dev&amp;d2=support&amp;d3=desGettingStarted">How to Compete</a></li>
									<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&amp;d1=dev&amp;d2=support&amp;d3=desGettingPaid">How to Get Paid</a></li>
									<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&amp;d1=dev&amp;d2=support&amp;d3=desDocumentation">Documentation</a></li>
									<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&amp;d1=dev&amp;d2=support&amp;d3=desRatings">Ratings</a></li>
									<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&amp;d1=dev&amp;d2=support&amp;d3=desReliability">Reliability Ratings</a></li>
								</ul>
							</li>
							<li class="exp"><a href="javascript:void(0)" onClick="Javascript:toggleMenu(this.parentNode,'m_dev_support')">Development</a>
								<ul id="m_dev_support">
									<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&amp;d1=dev&amp;d2=support&amp;d3=devGettingStarted">How to Compete</a></li>
									<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&amp;d1=dev&amp;d2=support&amp;d3=devGettingPaid">How to Get Paid</a></li>
									<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&amp;d1=dev&amp;d2=support&amp;d3=devDocumentation">Documentation</a></li>
									<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&amp;d1=dev&amp;d2=support&amp;d3=devRatings">Ratings</a></li>
									<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&amp;d1=dev&amp;d2=support&amp;d3=devReliability">Reliability Ratings</a></li>
								</ul>
							</li>
							<li class="exp"><a href="javascript:void(0)" onClick="Javascript:toggleMenu(this.parentNode,'m_assembly_support')">Assembly</a>
								<ul id="m_assembly_support">
									<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&d1=dev&d2=assembly&d3=instructions">How to Compete</a></li>
									<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&d1=dev&d2=assembly&d3=tutorial">Tutorial</a></li>
									<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&d1=dev&d2=assembly&d3=documentation">Documentation</a></li>
								</ul>
							</li>
							<li class="exp"><a href="javascript:void(0)" onClick="Javascript:toggleMenu(this.parentNode,'m_testing_support')">Testing</a>
								<ul id="m_testing_support">
									<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&d1=dev&d2=testing&d3=instructions">How to Compete</a></li>
									<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&d1=dev&d2=testing&d3=documentation">Documentation</a></li>
								</ul>
							</li>
							<li class="exp"><a href="javascript:void(0)" onClick="Javascript:toggleMenu(this.parentNode,'m_long_support')">Marathon Match</a>
								<ul id="m_long_support">
									<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/longcontest/?module=Static&d1=instructions">How to Compete</a></li>
								</ul>
							</li>
						</ul>
					</li>
				</ul>
			</li>
			<li><a href="http://<%=ApplicationServer.FORUMS_SERVER_NAME%>/">Forums</a></li>
			<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&amp;d1=pressroom&amp;d2=index">Press Room</a></li>
			<li class="exp"><a href="javascript:void(0)" onClick="Javascript:toggleMenu(this.parentNode,'m_about_tc')">About TopCoder</a>
				<ul id="m_about_tc">
					<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&amp;d1=about&amp;d2=index">Overview</a></li>
					<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&amp;d1=about&amp;d2=contactus">Contact Us</a></li>
					<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&amp;d1=about&amp;d2=whyjoin">Why Join TopCoder?</a></li>
					<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&amp;d1=about&amp;d2=jobs">Working at TopCoder</a></li>
					<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&amp;d1=about&amp;d2=management">Management Team</a></li>
					<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&amp;d1=about&amp;d2=terms">Terms, Revisions</a></li>
					<li><a href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=Static&amp;d1=about&amp;d2=privacy">Privacy Policy</a></li>
				</ul>
			</li>
		</ul>
		<div id="navbarSearch">
			<b>Member Search:</b>
			<form name="simpleSearchForm" method="get" action="http://<%=ApplicationServer.SERVER_NAME%>/tc" class="noSpace">
				<input class="noSpace" type="hidden" name="module" value="SimpleSearch" />
				<input class="noSpace" type="text" name="ha" value="Handle:" size="12" maxlength="15" onFocus="javascript: if (document.simpleSearchForm.ha.value=='Handle:') document.simpleSearchForm.ha.value = '';" onBlur="javascript: if (document.simpleSearchForm.ha.value == '') document.simpleSearchForm.ha.value = 'Handle:';" />
				<a class="gMetal" href="javascript:void document.simpleSearchForm.submit();">Go</a><br />
				<a class="gMetal" href="http://<%=ApplicationServer.SERVER_NAME%>/tc?module=ViewAdvanced">Advanced Search</a>
			</form>
		</div>
	</div>
	<div style="float: left; clear:left; padding: 0px 0px 23px 0px;">
		<html:img src="/i/interface/leftnav_bottom.gif" alt="" />
	</div>
