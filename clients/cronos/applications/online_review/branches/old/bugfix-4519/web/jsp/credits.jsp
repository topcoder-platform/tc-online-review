<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="html" uri="/tags/struts-html" %>
<%@ taglib prefix="bean" uri="/tags/struts-bean" %>
<%@ taglib prefix="tc-webtag" uri="/tags/tc-webtags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html xhtml="true">

<head>
	<title><bean:message key="OnlineReviewApp.Credits.title" /></title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

	<!-- TopCoder CSS -->
	<link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/style.css' />" />
	<link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/coders.css' />" />
	<link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/stats.css' />" />
	<link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/tcStyles.css' />" />

	<!-- CSS and JS from wireframes -->
	<script language="javascript" type="text/javascript" src="<html:rewrite href='/js/or/expand_collapse.js' />"><!-- @ --></script>
	<link rel="stylesheet" type="text/css" href="<html:rewrite href='/css/or/stylesheet.css' />" />

	<!-- CSS and JS by Petar -->
	<link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/or/new_styles.css' />" />
	<script language="JavaScript" type="text/javascript" src="<html:rewrite href='/js/or/rollovers.js' />"><!-- @ --></script>

<style type="text/css">
<!--
.creditsHeader {
	background-image: url(/i/or/credits_hd.gif);
	background-repeat: repeat-x;
	color: white;
	font-family: Arial, Helvetica, sans-serif;
	font-size: 14px;
	font-weight: bold;
	height: 24px;
	padding-left: 4px;
}
.creditsTitle {
	color: cc0000;
	font-family: Arial, Helvetica, sans-serif;
	font-size: 14px;
	font-weight: bold;
	padding-bottom: 5px;
	text-align: center;
}
.creditsSub {
	font-family: Arial, Helvetica, sans-serif;
	font-size: 14px;
	text-align: center;
}
.creditsName {
	font-family: Arial, Helvetica, sans-serif;
	font-size: 14px;
	padding-bottom: 5px;
	text-align: left;
}
.creditsName2 {
	font-family: Arial, Helvetica, sans-serif;
	font-size: 12px;
	padding-bottom: 5px;
	text-align: left;
}
.creditsText {
	color: #666666;
	font-family: Arial, Helvetica, sans-serif;
	font-size: 12px;
	font-style: italic;
}
-->
</style>
</head>

<body>
	<jsp:include page="/includes/inc_header.jsp" />
	<table width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr valign="top">
			<!-- Left Column Begins -->
			<td width="180"><jsp:include page="/includes/inc_leftnav.jsp" /></td>
			<!-- Left Column Ends -->

			<!-- Gutter Begins -->
			<td width="15"><img src="/i/clear.gif" width="15" height="1" border="0" /></td>
			<!-- Gutter Ends -->

			<!-- Center Column Begins -->
			<td class="bodyText">
				<br /><br />
				<table class="stat" width="100%" cellpadding="0" cellspacing="0">
					<tr>
						<td class="creditsHeader" colspan="3"><bean:message key="credits.SubTitle" /></td>
					</tr>
					<tr style="background-image:url(/i/or/credits_bk.gif);background-repeat:repeat-x;">
						<td valign="top">
							<table width="100%" cellpadding="0" cellspacing="0">
								<tr>
									<td class="creditsTitle"><br /><bean:message key="credits.QA" /></td>
								</tr>
								<tr>
									<td class="creditsSub">hohosky<br />albertwang</td>
								</tr>
								<tr>
									<td>&#160;</td>
								</tr>
							</table>
						</td>
						<td valign="top">
							<table width="100%" cellpadding="0" cellspacing="0">
								<tr>
									<td class="creditsTitle"><br /><bean:message key="credits.Architect" /></td>
								</tr>
								<tr>
									<td class="creditsSub">WishingBone</td>
								</tr>
							</table>
						</td>
						<td valign="top">
							<table width="100%" cellpadding="0" cellspacing="0">
								<tr>
									<td class="creditsTitle" colspan="4"><br /><bean:message key="credits.Assemblers" /></td>
								</tr>
								<tr>
									<td class="creditsSub">George1 &#160; real_vg<br />brain_cn &#160; flying2hk</td>
								</tr>
							</table>
						</td>
					</tr>

					<tr style="background-image:url(/i/or/credits_bk.gif);background-repeat:repeat-x;">
						<td valign="top" colspan="3">
							<table width="100%" cellpadding="0" cellspacing="0">
								<tr>
									<td class="creditsTitle" colspan="5"><br /><br /><bean:message key="credits.Des_Dev_Review" /></td>
								</tr>
								<tr>
									<td colspan="5" class="creditsText" style="padding:5px 15px 16px 15px;"><bean:message key="credits.MebersExplanatory" /></td>
								</tr>
								<tr>
									<td class="creditsName" style="padding-left:15px;">AdamSelene</td>
									<td class="creditsName">AleaActaEst</td>
									<td class="creditsName">Angen</td>
									<td class="creditsName">BEHiker57W</td>
									<td class="creditsName">BigDaddy</td>
								</tr>
								<tr>
									<td class="creditsName" style="padding-left:15px;">BryanChen</td>
									<td class="creditsName">Chenhong</td>
									<td class="creditsName">Code-Guru</td>
									<td class="creditsName">DaTwinkDaddy</td>
									<td class="creditsName">DanLazar</td>
								</tr>
								<tr>
									<td class="creditsName" style="padding-left:15px">FTolToaster</td>
									<td class="creditsName">FireIce</td>
									<td class="creditsName">GavinWang</td>
									<td class="creditsName">Ghostar</td>
									<td class="creditsName">Ken_Vogel</td>
								</tr>
								<tr>
									<td class="creditsName" style="padding-left:15px;">King_Bette</td>
									<td class="creditsName">Luca</td>
									<td class="creditsName">MPhk</td>
									<td class="creditsName">Mr.Sketch</td>
									<td class="creditsName">PE</td>
								</tr>
								<tr>
									<td class="creditsName" style="padding-left:15px;">PabloGilberto</td>
									<td class="creditsName">Pops</td>
									<td class="creditsName">RachaelLCook</td>
									<td class="creditsName">RoyItaqi</td>
									<td class="creditsName">ShindouHikaru</td>
								</tr>
								<tr>
									<td class="creditsName" style="padding-left:15px;">Sleeve</td>
									<td class="creditsName">Standlove</td>
									<td class="creditsName">StinkyCheeseMan</td>
									<td class="creditsName">TheCois</td>
									<td class="creditsName">ThinMan</td>
								</tr>
								<tr>
									<td class="creditsName" style="padding-left:15px;">Thinfox</td>
									<td class="creditsName">Tomson</td>
									<td class="creditsName">UFP2161</td>
									<td class="creditsName">Wendell</td>
									<td class="creditsName">WishingBone</td>
								</tr>
								<tr>
									<td class="creditsName" style="padding-left:15px;">XuChuan</td>
									<td class="creditsName">abelli</td>
									<td class="creditsName">adic</td>
									<td class="creditsName">air2cold</td>
									<td class="creditsName">akhil_bansal</td>
								</tr>
								<tr>
									<td class="creditsName" style="padding-left:15px;">aksonov</td>
									<td class="creditsName">alanSunny</td>
									<td class="creditsName">albertwang</td>
									<td class="creditsName">amitc</td>
									<td class="creditsName">argolite</td>
								</tr>
								<tr>
									<td class="creditsName" style="padding-left:15px;">arylio</td>
									<td class="creditsName">assistant</td>
									<td class="creditsName">aubergineanode</td>
									<td class="creditsName">biotrail</td>
									<td class="creditsName">bokbok</td>
								</tr>
								<tr>
									<td class="creditsName" style="padding-left:15px;">bose_java</td>
									<td class="creditsName">brain_cn</td>
									<td class="creditsName">cmax</td>
									<td class="creditsName">codedoc</td>
									<td class="creditsName">colau</td>
								</tr>
								<tr>
									<td class="creditsName" style="padding-left:15px;">cosherx</td>
									<td class="creditsName">crackme</td>
									<td class="creditsName">cucu</td>
									<td class="creditsName">custos</td>
									<td class="creditsName">daiwb</td>
								</tr>
								<tr>
									<td class="creditsName" style="padding-left:15px;">danno</td>
									<td class="creditsName">dmks</td>
									<td class="creditsName">dplass</td>
									<td class="creditsName">duner</td>
									<td class="creditsName">fairytale</td>
								</tr>
								<tr>
									<td class="creditsName" style="padding-left:15px;">flying2hk</td>
									<td class="creditsName">gua</td>
									<td class="creditsName">haozhangr</td>
									<td class="creditsName">henryouly</td>
									<td class="creditsName">hohosky</td>
								</tr>
								<tr>
									<td class="creditsName" style="padding-left:15px;">holmana</td>
									<td class="creditsName">iamajia</td>
									<td class="creditsName">icyriver</td>
									<td class="creditsName">idx</td>
									<td class="creditsName">iggy36</td>
								</tr>
								<tr>
									<td class="creditsName" style="padding-left:15px;">isv</td>
									<td class="creditsName">ivern</td>
									<td class="creditsName">jfjiang</td>
									<td class="creditsName">justforplay</td>
									<td class="creditsName">kinfkong</td>
								</tr>
								<tr>
									<td class="creditsName" style="padding-left:15px;">kr00tki</td>
									<td class="creditsName">littlebull</td>
									<td class="creditsName">lyt</td>
									<td class="creditsName">magicpig</td>
									<td class="creditsName">maone</td>
								</tr>
								<tr>
									<td class="creditsName" style="padding-left:15px;">marijnk</td>
									<td class="creditsName">matmis</td>
									<td class="creditsName">mayi</td>
									<td class="creditsName">mgmg</td>
									<td class="creditsName">mishagam</td>
								</tr>
								<tr>
									<td class="creditsName" style="padding-left:15px;">myxgyy</td>
									<td class="creditsName">nhzp339</td>
									<td class="creditsName">nicka81</td>
									<td class="creditsName">oldbig</td>
									<td class="creditsName">oodinary</td>
								</tr>
								<tr>
									<td class="creditsName" style="padding-left:15px;">opi</td>
									<td class="creditsName">orb</td>
									<td class="creditsName">preben</td>
									<td class="creditsName">pzhao</td>
									<td class="creditsName">qiucx0161</td>
								</tr>
								<tr>
									<td class="creditsName" style="padding-left:15px;">real_vg</td>
									<td class="creditsName">rem</td>
									<td class="creditsName">roma</td>
									<td class="creditsName">sapro</td>
									<td class="creditsName">sb99</td>
								</tr>
								<tr>
									<td class="creditsName" style="padding-left:15px;">seaniswise</td>
									<td class="creditsName">semi_sleep</td>
									<td class="creditsName">sindu</td>
									<td class="creditsName">singlewood</td>
									<td class="creditsName">skatou</td>
								</tr>
								<tr>
									<td class="creditsName" style="padding-left:15px;">slion</td>
									<td class="creditsName">smell</td>
									<td class="creditsName">snard6</td>
									<td class="creditsName">southwang</td>
									<td class="creditsName">srowen</td>
								</tr>
								<tr>
									<td class="creditsName" style="padding-left:15px;">still</td>
									<td class="creditsName">telly12</td>
									<td class="creditsName">topgear</td>
									<td class="creditsName">ttsuchi</td>
									<td class="creditsName">tuenm</td>
								</tr>
								<tr>
									<td class="creditsName" style="padding-left:15px;">urtks</td>
									<td class="creditsName">valeriy</td>
									<td class="creditsName">victor_lxd</td>
									<td class="creditsName">viking_hz</td>
									<td class="creditsName">vilain</td>
								</tr>
								<tr>
									<td class="creditsName" style="padding-left:15px;">vinnath</td>
									<td class="creditsName">visualage</td>
									<td class="creditsName">vividmxx</td>
									<td class="creditsName">waits</td>
									<td class="creditsName">wiedzmin</td>
								</tr>
								<tr>
									<td class="creditsName" style="padding-left:15px;">woodjhon</td>
									<td class="creditsName">yellow_gecko</td>
									<td class="creditsName">zhuzeyuan</td>
									<td class="creditsName">zimmy</td>
									<td class="creditsName">zjq</td>
								</tr>
								<tr>
									<td class="creditsName" style="padding-left:15px;">zsudraco</td>
									<td class="creditsName" colspan="4"><!-- @ --></td>
								</tr>
								<tr>
									<td colspan="5">&#160;</td>
								</tr>
							</table>
						</td>
					</tr>

					<tr style="background-image:url(/i/or/credits_bk.gif);background-repeat:repeat-x;">
						<td valign="top" colspan="3" style="border-bottom: 1px solid #999999;">
							<table width="100%" cellpadding="0" cellspacing="0">
								<tr>
									<td class="creditsTitle" colspan="3"><br /><br /><bean:message key="credits.Components" /></td>
								</tr>
								<tr>
									<td colspan="3" class="creditsText" style="padding:5px 15px 16px 15px;"><bean:message key="credits.ComponentsExplanatory" /></td>
								</tr>
								<tr>
									<td class="creditsName2" style="padding-left:15px;">Auto Screening Management 1.0</td>
									<td class="creditsName2">Auto Screening Tool Persistence 1.0</td>
									<td class="creditsName2">Deliverable Management Persistence 1.0</td>
								</tr>
								<tr>
									<td class="creditsName2" style="padding-left:15px;">Online Review Ajax Support 1.0</td>
									<td class="creditsName2">Online Review Deliverables 1.0</td>
									<td class="creditsName2">Online Review Login 1.0</td>
								</tr>
								<tr>
									<td class="creditsName2" style="padding-left:15px;">Online Review Phases 1.0</td>
									<td class="creditsName2">Phase Management Persistence 1.0</td>
									<td class="creditsName2">Project Management Persistence 1.0</td>
								</tr>
								<tr>
									<td class="creditsName2" style="padding-left:15px;">Resource Management Persistence 1.0</td>
									<td class="creditsName2">Review Management Persistence 1.0</td>
									<td class="creditsName2">Scorecard Management Persistence 1.0</td>
								</tr>
								<tr>
									<td class="creditsName2" style="padding-left:15px;">User Project Data Store 1.0</td>
									<td class="creditsName2">Authentication Factory 2.0</td>
									<td class="creditsName2">Auto Pilot 1.0</td>
								</tr>
								<tr>
									<td class="creditsName2" style="padding-left:15px;">Auto Screening Tool 1.0</td>
									<td class="creditsName2">Base Exception 1.0</td>
									<td class="creditsName2">Cached Web Element Tag 1.0</td>
								</tr>
								<tr>
									<td class="creditsName2" style="padding-left:15px;">Class Associations 1.0</td>
									<td class="creditsName2">Command Line Utility 1.0</td>
									<td class="creditsName2">Compression Utility 2.0.1</td>
								</tr>
								<tr>
									<td class="creditsName2" style="padding-left:15px;">Configuration Manager 2.1.5</td>
									<td class="creditsName2">Data Paging Tag 2.0</td>
									<td class="creditsName2">Data Validation 1.0</td>
								</tr>
								<tr>
									<td class="creditsName2" style="padding-left:15px;">Database Abstraction 1.1</td>
									<td class="creditsName2">DB Connection Factory 1.0</td>
									<td class="creditsName2">Deliverable Management 1.0</td>
								</tr>
								<tr>
									<td class="creditsName2" style="padding-left:15px;">Directory Validation 1.0</td>
									<td class="creditsName2">Document Generator 2.0</td>
									<td class="creditsName2">Email Engine 3.0</td>
								</tr>
								<tr>
									<td class="creditsName2" style="padding-left:15px;">Executable Wrapper 1.0</td>
									<td class="creditsName2">File Class 1.0</td>
									<td class="creditsName2">File System Server 1.0</td>
								</tr>
								<tr>
									<td class="creditsName2" style="padding-left:15px;">File Upload 2.0</td>
									<td class="creditsName2">Generic Event Manager 1.0</td>
									<td class="creditsName2">Guid Generator 1.0</td>
								</tr>
								<tr>
									<td class="creditsName2" style="padding-left:15px;">Heartbeat 1.0</td>
									<td class="creditsName2">ID Generator 3.0</td>
									<td class="creditsName2">IP Server 2.0.1</td>
								</tr>
								<tr>
									<td class="creditsName2" style="padding-left:15px;">JNDI Utility 1.0</td>
									<td class="creditsName2">Job Scheduler 1.0</td>
									<td class="creditsName2">Logging Wrapper 1.2</td>
								</tr>
								<tr>
									<td class="creditsName2" style="padding-left:15px;">Magic Numbers 1.0</td>
									<td class="creditsName2">Object Factory 2.0.1</td>
									<td class="creditsName2">Phase Management 1.0</td>
								</tr>
								<tr>
									<td class="creditsName2" style="padding-left:15px;">Project Management 1.0</td>
									<td class="creditsName2">Project Phase Template 1.0</td>
									<td class="creditsName2">Project Phases 2.0</td>
								</tr>
								<tr>
									<td class="creditsName2" style="padding-left:15px;">Resource Management 1.0</td>
									<td class="creditsName2">Review Data Structure 1.0</td>
									<td class="creditsName2">Review Management 1.0</td>
								</tr>
								<tr>
									<td class="creditsName2" style="padding-left:15px;">Review Score Aggregator 1.0</td>
									<td class="creditsName2">Review Score Calculator 1.0</td>
									<td class="creditsName2">Scorecard Data Structure 1.0</td>
								</tr>
								<tr>
									<td class="creditsName2" style="padding-left:15px;">Scorecard Management 1.0</td>
									<td class="creditsName2">Search Builder 1.3</td>
									<td class="creditsName2">Security Manager 1.1</td>
								</tr>
								<tr>
									<td class="creditsName2" style="padding-left:15px;">Simple Cache 2.0</td>
									<td class="creditsName2">Typesafe Enum 1.0</td>
									<td class="creditsName2">Weighted Calculator 1.0</td>
								</tr>
								<tr>
									<td class="creditsName2" style="padding-left:15px;">Workdays 1.0</td>
									<td class="creditsName2">XMI Parser 1.1</td>
									<td class="creditsName2"><!-- @ --></td>
								</tr>
								<tr>
									<td colspan="3">&#160;</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td class="lastRowTD" colspan="3"><!-- @ --></td>
					</tr>
				</table>

				<br /><br />
			</td>
			<!-- Center Column Ends -->

			<!-- Gutter -->
			<td width="15"><html:img src="/i/clear.gif" width="25" height="1" border="0" /></td>
			<!-- Gutter Ends -->

			<!-- Gutter -->
			<td width="2"><img src="/i/clear.gif" width="2" height="1" border="0" /></td>
			<!-- Gutter Ends -->
		</tr>
	</table>

	<jsp:include page="/includes/inc_footer.jsp" />
</body>
</html:html>
