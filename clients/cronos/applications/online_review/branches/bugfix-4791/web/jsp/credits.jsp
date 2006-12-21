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
table.credits tr th {
	color: #CC0000;
	font-family: Arial, Helvetica, sans-serif;
	font-size: 14px;
	font-weight: bold;
	padding-bottom: 3px;
	padding-top: 27px;
	text-align: center;
}
table.credits tr td {
	font-family: Arial, Helvetica, sans-serif;
	font-size: 14px;
	padding-bottom: 5px;
	text-align: center;
	vertical-align: top;
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
				<div style="padding: 9px 6px 6px 3px;"><span class="subTitle"><bean:message key="credits.SubTitle" /></span></div>
				<table class="stat credits" width="100%" cellpadding="0" cellspacing="0" border="0" style="background-color:#E8E8E8;border-bottom:1px solid #999999;">
					<tr>
						<th width="33%"><bean:message key="credits.QA" /></th>
						<th width="34%"><bean:message key="credits.QA" /> / <bean:message key="credits.BugFixers" /></th>
						<th width="33%"><bean:message key="credits.ProjectMgmt" /></th>
					</tr>
					<tr>
						<td>Kelly Anne Costello - <tc-webtag:handle coderId="22644233" context="component" /><%-- soapbxr --%></td>
						<td>
							<tc-webtag:handle coderId="16096823" context="component" /><%-- hohosky --%><br />
							<tc-webtag:handle coderId="10336829" context="component" /><%-- albertwang --%></td>
						<td>Lorie Norman - <tc-webtag:handle coderId="278287" context="component" /></td>
					</tr>
					<tr>
						<th><bean:message key="credits.BusinessAnalyst" /></th>
						<th><bean:message key="credits.Architect" /></th>
						<th><bean:message key="credits.DeplEngineer" /></th>
					</tr>
					<tr>
						<td>
							David Messinger - <tc-webtag:handle coderId="305384" context="component" /><br />
							Javier-Fernandez Ivern - <tc-webtag:handle coderId="156859" context="component" /></td>
						<td><tc-webtag:handle coderId="286907" context="component" /><%-- WishingBone --%></td>
						<td>Daniel Ulery - <tc-webtag:handle coderId="107160" context="component" /></td>
					</tr>
					<tr>
						<th colspan="3"><bean:message key="credits.Assemblers" /></th>
					</tr>
					<tr>
						<td colspan="3">
							<tc-webtag:handle coderId="20003257" context="component" /><%-- George1 --%> &#160;
							<tc-webtag:handle coderId="15891862" context="component" /><%-- real_vg --%> &#160;
							<tc-webtag:handle coderId="11770376" context="component" /><%-- brian_cn --%> &#160;
							<tc-webtag:handle coderId="22498968" context="component" /><%-- flying2hk --%></td>
					</tr>
					<tr>
						<th colspan="3"><bean:message key="credits.ProdRolloutTeam" /></th>
					</tr>
					<tr>
						<td colspan="3">
							Greg Paul - <tc-webtag:handle coderId="132456" context="component" /> &#160;
							Travis Haas - <tc-webtag:handle coderId="150148" context="component" /> &#160;
							Mike Lydon - <tc-webtag:handle coderId="100142" context="component" /></td>
					</tr>
					<tr>
						<td colspan="3" style="padding-left:20px;padding-right:20px;"><br /><hr width="100%" noshade="noshade" size="1" /></td>
					</tr>
					<tr>
						<td colspan="3">
							<table class="credits" width="100%" cellpadding="0" cellspacing="0" border="0">
								<tr>
									<th colspan="5"><bean:message key="credits.Des_Dev_Review" /></th>
								</tr>
								<tr>
									<td width="20%"><tc-webtag:handle coderId="251989" context="component" /><%-- AdamSelene --%></td>
									<td width="20%"><tc-webtag:handle coderId="252022" context="component" /><%-- AleaActaEst --%></td>
									<td width="20%"><tc-webtag:handle coderId="12029342" context="component" /><%-- Angen --%></td>
									<td width="20%"><tc-webtag:handle coderId="7442489" context="component" /><%-- BEHiker57W --%></td>
									<td width="20%"><tc-webtag:handle coderId="151360" context="component" /><%-- BigDaddy --%></td>
								</tr>
								<tr>
									<td><tc-webtag:handle coderId="290448" context="component" /><%-- BryanChen --%></td>
									<td><tc-webtag:handle coderId="12006665" context="component" /><%-- Chenhong --%></td>
									<td><tc-webtag:handle coderId="108281" context="component" /><%-- Code-Guru --%></td>
									<td><tc-webtag:handle coderId="159921" context="component" /><%-- DaTwinkDaddy --%></td>
									<td><tc-webtag:handle coderId="10143068" context="component" /><%-- DanLazar --%></td>
								</tr>
								<tr>
									<td><tc-webtag:handle coderId="289864" context="component" /><%-- FTolToaster --%></td>
									<td><tc-webtag:handle coderId="15050434" context="component" /><%-- FireIce --%></td>
									<td><tc-webtag:handle coderId="11981278" context="component" /><%-- GavinWang --%></td>
									<td><tc-webtag:handle coderId="151743" context="component" /><%-- Ghostar --%></td>
									<td><tc-webtag:handle coderId="154754" context="component" /><%-- Ken_Vogel --%></td>
								</tr>
								<tr>
									<td><tc-webtag:handle coderId="15692538" context="component" /><%-- King_Bette --%></td>
									<td><tc-webtag:handle coderId="10348862" context="component" /><%-- Luca --%></td>
									<td><tc-webtag:handle coderId="289824" context="component" /><%-- MPhk --%></td>
									<td><tc-webtag:handle coderId="292451" context="component" /><%-- Mr.Sketch --%></td>
									<td><tc-webtag:handle coderId="9998760" context="component" /><%-- PE --%></td>
								</tr>
								<tr>
									<td><tc-webtag:handle coderId="112438" context="component" /><%-- PabloGilberto --%></td>
									<td><tc-webtag:handle coderId="119676" context="component" /><%-- Pops --%></td>
									<td><tc-webtag:handle coderId="150424" context="component" /><%-- RachaelLCook --%></td>
									<td><tc-webtag:handle coderId="10269872" context="component" /><%-- RoyItaqi --%></td>
									<td><tc-webtag:handle coderId="7463987" context="component" /><%-- ShindouHikaru --%></td>
								</tr>
								<tr>
									<td><tc-webtag:handle coderId="266705" context="component" /><%-- Sleeve --%></td>
									<td><tc-webtag:handle coderId="310233" context="component" /><%-- Standlove --%></td>
									<td><tc-webtag:handle coderId="283991" context="component" /><%-- StinkyCheeseMan --%></td>
									<td><tc-webtag:handle coderId="297731" context="component" /><%-- TheCois --%></td>
									<td><tc-webtag:handle coderId="150498" context="component" /><%-- ThinMan --%></td>
								</tr>
								<tr>
									<td><tc-webtag:handle coderId="301597" context="component" /><%-- Thinfox --%></td>
									<td><tc-webtag:handle coderId="302018" context="component" /><%-- Tomson --%></td>
									<td><tc-webtag:handle coderId="260952" context="component" /><%-- UFP2161 --%></td>
									<td><tc-webtag:handle coderId="8544935" context="component" /><%-- Wendell --%></td>
									<td><tc-webtag:handle coderId="286907" context="component" /><%-- WishingBone --%></td>
								</tr>
								<tr>
									<td><tc-webtag:handle coderId="286911" context="component" /><%-- XuChuan --%></td>
									<td><tc-webtag:handle coderId="20188980" context="component" /><%-- abelli --%></td>
									<td><tc-webtag:handle coderId="278342" context="component" /><%-- adic --%></td>
									<td><tc-webtag:handle coderId="11824548" context="component" /><%-- air2cold --%></td>
									<td><tc-webtag:handle coderId="266149" context="component" /><%-- akhil_bansal --%></td>
								</tr>
								<tr>
									<td><tc-webtag:handle coderId="277356" context="component" /><%-- aksonov --%></td>
									<td><tc-webtag:handle coderId="7231913" context="component" /><%-- alanSunny --%></td>
									<td><tc-webtag:handle coderId="10336829" context="component" /><%-- albertwang --%></td>
									<td><tc-webtag:handle coderId="260911" context="component" /><%-- amitc --%></td>
									<td><tc-webtag:handle coderId="287614" context="component" /><%-- argolite --%></td>
								</tr>
								<tr>
									<td><tc-webtag:handle coderId="7489235" context="component" /><%-- arylio --%></td>
									<td><tc-webtag:handle coderId="20076717" context="component" /><%-- assistant --%></td>
									<td><tc-webtag:handle coderId="11797255" context="component" /><%-- aubergineanode --%></td>
									<td><tc-webtag:handle coderId="14926554" context="component" /><%-- biotrail --%></td>
									<td><tc-webtag:handle coderId="153434" context="component" /><%-- bokbok --%></td>
								</tr>
								<tr>
									<td><tc-webtag:handle coderId="13379412" context="component" /><%-- bose_java --%></td>
									<td><tc-webtag:handle coderId="11770376" context="component" /><%-- brian_cn --%></td>
									<td><tc-webtag:handle coderId="15868222" context="component" /><%-- cmax --%></td>
									<td><tc-webtag:handle coderId="10535364" context="component" /><%-- codedoc --%></td>
									<td><tc-webtag:handle coderId="10098406" context="component" /><%-- colau --%></td>
								</tr>
								<tr>
									<td><tc-webtag:handle coderId="8389509" context="component" /><%-- cosherx --%></td>
									<td><tc-webtag:handle coderId="20708384" context="component" /><%-- crackme --%></td>
									<td><tc-webtag:handle coderId="7545675" context="component" /><%-- cucu --%></td>
									<td><tc-webtag:handle coderId="281421" context="component" /><%-- custos --%></td>
									<td><tc-webtag:handle coderId="8347577" context="component" /><%-- daiwb --%></td>
								</tr>
								<tr>
									<td><tc-webtag:handle coderId="260578" context="component" /><%-- danno --%></td>
									<td><tc-webtag:handle coderId="7360309" context="component" /><%-- dmks --%></td>
									<td><tc-webtag:handle coderId="251184" context="component" /><%-- dplass --%></td>
									<td><tc-webtag:handle coderId="7390772" context="component" /><%-- duner --%></td>
									<td><tc-webtag:handle coderId="13324255" context="component" /><%-- fairytale --%></td>
								</tr>
								<tr>
									<td><tc-webtag:handle coderId="22498968" context="component" /><%-- flying2hk --%></td>
									<td>fuyungarykgeorgepf</td>
									<td><tc-webtag:handle coderId="11770877" context="component" /><%-- gua --%></td>
									<td><tc-webtag:handle coderId="15832162" context="component" /><%-- haozhangr --%></td>
									<td><tc-webtag:handle coderId="11889718" context="component" /><%-- henryouly --%></td>
								</tr>
								<tr>
									<td><tc-webtag:handle coderId="16096823" context="component" /><%-- hohosky --%></td>
									<td><tc-webtag:handle coderId="154307" context="component" /><%-- holmana --%></td>
									<td><tc-webtag:handle coderId="22627015" context="component" /><%-- iamajia --%></td>
									<td><tc-webtag:handle coderId="10275123" context="component" /><%-- icyriver --%></td>
									<td><tc-webtag:handle coderId="20719960" context="component" /><%-- idx --%></td>
								</tr>
								<tr>
									<td><tc-webtag:handle coderId="10447013" context="component" /><%-- iggy36 --%></td>
									<td><tc-webtag:handle coderId="299180" context="component" /><%-- isv --%></td>
									<td><tc-webtag:handle coderId="156859" context="component" /><%-- ivern --%></td>
									<td><tc-webtag:handle coderId="288429" context="component" /><%-- jfjiang --%></td>
									<td><tc-webtag:handle coderId="11775761" context="component" /><%-- justforplay --%></td>
								</tr>
								<tr>
									<td><tc-webtag:handle coderId="11950083" context="component" /><%-- kinfkong --%></td>
									<td><tc-webtag:handle coderId="10353806" context="component" /><%-- kr00tki --%></td>
									<td><tc-webtag:handle coderId="15147311" context="component" /><%-- littlebull --%></td>
									<td><tc-webtag:handle coderId="21075542" context="component" /><%-- lyt --%></td>
									<td><tc-webtag:handle coderId="14788013" context="component" /><%-- magicpig --%></td>
								</tr>
								<tr>
									<td><tc-webtag:handle coderId="10425804" context="component" /><%-- maone --%></td>
									<td><tc-webtag:handle coderId="7444051" context="component" /><%-- marijnk --%></td>
									<td><tc-webtag:handle coderId="7251152" context="component" /><%-- matmis --%></td>
									<td><tc-webtag:handle coderId="15655112" context="component" /><%-- mayi --%></td>
									<td><tc-webtag:handle coderId="11971764" context="component" /><%-- mgmg --%></td>
								</tr>
								<tr>
									<td><tc-webtag:handle coderId="299904" context="component" /><%-- mishagam --%></td>
									<td><tc-webtag:handle coderId="20758806" context="component" /><%-- myxgyy --%></td>
									<td><tc-webtag:handle coderId="11789293" context="component" /><%-- nhzp339 --%></td>
									<td><tc-webtag:handle coderId="293874" context="component" /><%-- nicka81 --%></td>
									<td><tc-webtag:handle coderId="302053" context="component" /><%-- oldbig --%></td>
								</tr>
								<tr>
									<td><tc-webtag:handle coderId="15832159" context="component" /><%-- oodinary --%></td>
									<td><tc-webtag:handle coderId="301504" context="component" /><%-- opi --%></td>
									<td><tc-webtag:handle coderId="150940" context="component" /><%-- orb --%></td>
									<td><tc-webtag:handle coderId="153089" context="component" /><%-- preben --%></td>
									<td><tc-webtag:handle coderId="296745" context="component" /><%-- pzhao --%></td>
								</tr>
								<tr>
									<td><tc-webtag:handle coderId="10650643" context="component" /><%-- qiucx0161 --%></td>
									<td><tc-webtag:handle coderId="15891862" context="component" /><%-- real_vg --%></td>
									<td><tc-webtag:handle coderId="8394868" context="component" /><%-- rem --%></td>
									<td><tc-webtag:handle coderId="7389864" context="component" /><%-- roma --%></td>
									<td><tc-webtag:handle coderId="293470" context="component" /><%-- sapro --%></td>
								</tr>
								<tr>
									<td><tc-webtag:handle coderId="15816101" context="component" /><%-- sb99 --%></td>
									<td><tc-webtag:handle coderId="278595" context="component" /><%-- seaniswise --%></td>
									<td><tc-webtag:handle coderId="11802577" context="component" /><%-- semi_sleep --%></td>
									<td><tc-webtag:handle coderId="7548200" context="component" /><%-- sindu --%></td>
									<td><tc-webtag:handle coderId="15845095" context="component" /><%-- singlewood --%></td>
								</tr>
								<tr>
									<td><tc-webtag:handle coderId="10119301" context="component" /><%-- skatou --%></td>
									<td><tc-webtag:handle coderId="10022398" context="component" /><%-- slion --%></td>
									<td><tc-webtag:handle coderId="9971384" context="component" /><%-- smell --%></td>
									<td><tc-webtag:handle coderId="158236" context="component" /><%-- snard6 --%></td>
									<td><tc-webtag:handle coderId="11798503" context="component" /><%-- southwang --%></td>
								</tr>
								<tr>
									<td><tc-webtag:handle coderId="296145" context="component" /><%-- srowen --%></td>
									<td><tc-webtag:handle coderId="15500330" context="component" /><%-- still --%></td>
									<td><tc-webtag:handle coderId="10169506" context="component" /><%-- telly12 --%></td>
									<td><tc-webtag:handle coderId="15608845" context="component" /><%-- topgear --%></td>
									<td><tc-webtag:handle coderId="280167" context="component" /><%-- ttsuchi --%></td>
								</tr>
								<tr>
									<td><tc-webtag:handle coderId="11781622" context="component" /><%-- tuenm --%></td>
									<td><tc-webtag:handle coderId="21471587" context="component" /><%-- urtks --%></td>
									<td><tc-webtag:handle coderId="269515" context="component" /><%-- valeriy --%></td>
									<td><tc-webtag:handle coderId="13377493" context="component" /><%-- victor_lxd --%></td>
									<td><tc-webtag:handle coderId="8416548" context="component" /><%-- viking_hz --%></td>
								</tr>
								<tr>
									<td><tc-webtag:handle coderId="15002482" context="component" /><%-- vilain --%></td>
									<td><tc-webtag:handle coderId="288617" context="component" /><%-- vinnath --%></td>
									<td><tc-webtag:handle coderId="299979" context="component" /><%-- visualage --%></td>
									<td><tc-webtag:handle coderId="8375801" context="component" /><%-- vividmxx --%></td>
									<td><tc-webtag:handle coderId="15197513" context="component" /><%-- waits --%></td>
								</tr>
								<tr>
									<td><tc-webtag:handle coderId="10405908" context="component" /><%-- wiedzmin --%></td>
									<td><tc-webtag:handle coderId="9981727" context="component" /><%-- woodjhon --%></td>
									<td><tc-webtag:handle coderId="272311" context="component" /><%-- yellow_gecko --%></td>
									<td><tc-webtag:handle coderId="19929536" context="component" /><%-- zhuzeyuan --%></td>
									<td><tc-webtag:handle coderId="264803" context="component" /><%-- zimmy --%></td>
								</tr>
								<tr>
									<td><tc-webtag:handle coderId="10526732" context="component" /><%-- zjq --%></td>
									<td><tc-webtag:handle coderId="14820574" context="component" /><%-- zsudraco --%></td>
									<td colspan="3"><!-- @ --></td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td colspan="3">&#160;</td>
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