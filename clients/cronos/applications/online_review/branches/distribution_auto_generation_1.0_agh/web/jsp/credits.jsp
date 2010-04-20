<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="html" uri="/tags/struts-html" %>
<%@ taglib prefix="bean" uri="/tags/struts-bean" %>
<%@ taglib prefix="tc-webtag" uri="/tags/tc-webtags" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>
<%@ page import="com.topcoder.shared.util.ApplicationServer"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html xhtml="true">

<head>
    <title><bean:message key="global.title.level2.noDash"
        arg0='${orfn:getMessage(pageContext, "OnlineReviewApp.title")}'
        arg1='${orfn:getMessage(pageContext, "credits.title")}' /></title>
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
    <script language="JavaScript" type="text/javascript" src="<html:rewrite href='/js/or/rollovers2.js' />"><!-- @ --></script>

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
    white-space: nowrap;
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
<div align="center">
    
    <div class="maxWidthBody" align="left">

        <jsp:include page="/includes/inc_header.jsp" />
        
            <div id="mainMiddleContent">
                <div style="position: relative; width: 100%;">

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
                                    <td class="creditsSub">
                                        <tc-webtag:handle coderId="16096823" context="component" /><%-- hohosky --%><br />
                                        <tc-webtag:handle coderId="10336829" context="component" /><%-- albertwang --%></td>
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
                                    <td class="creditsSub"><tc-webtag:handle coderId="286907" context="component" /><%-- WishingBone --%></td>
                                </tr>
                            </table>
                        </td>
                        <td valign="top">
                            <table width="100%" cellpadding="0" cellspacing="0">
                                <tr>
                                    <td class="creditsTitle" colspan="4"><br /><bean:message key="credits.Assemblers" /></td>
                                </tr>
                                <tr>
                                    <td class="creditsSub">
                                        <tc-webtag:handle coderId="20003257" context="component" /><%-- George1 --%> &#160;
                                        <tc-webtag:handle coderId="15891862" context="component" /><%-- real_vg --%><br />
                                        <tc-webtag:handle coderId="11770376" context="component" /><%-- brian_cn --%> &#160;
                                        <tc-webtag:handle coderId="22498968" context="component" /><%-- flying2hk --%></td>
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
                                    <td class="creditsName" style="padding-left:15px;"><tc-webtag:handle coderId="251989" context="component" /><%-- AdamSelene --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="252022" context="component" /><%-- AleaActaEst --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="12029342" context="component" /><%-- Angen --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="7442489" context="component" /><%-- BEHiker57W --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="151360" context="component" /><%-- BigDaddy --%></td>
                                </tr>
                                <tr>
                                    <td class="creditsName" style="padding-left:15px;"><tc-webtag:handle coderId="290448" context="component" /><%-- BryanChen --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="12006665" context="component" /><%-- Chenhong --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="108281" context="component" /><%-- Code-Guru --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="159921" context="component" /><%-- DaTwinkDaddy --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="10143068" context="component" /><%-- DanLazar --%></td>
                                </tr>
                                <tr>
                                    <td class="creditsName" style="padding-left:15px"><tc-webtag:handle coderId="289864" context="component" /><%-- FTolToaster --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="15050434" context="component" /><%-- FireIce --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="11981278" context="component" /><%-- GavinWang --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="151743" context="component" /><%-- Ghostar --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="154754" context="component" /><%-- Ken_Vogel --%></td>
                                </tr>
                                <tr>
                                    <td class="creditsName" style="padding-left:15px;"><tc-webtag:handle coderId="15692538" context="component" /><%-- King_Bette --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="10348862" context="component" /><%-- Luca --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="289824" context="component" /><%-- MPhk --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="292451" context="component" /><%-- Mr.Sketch --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="9998760" context="component" /><%-- PE --%></td>
                                </tr>
                                <tr>
                                    <td class="creditsName" style="padding-left:15px;"><tc-webtag:handle coderId="112438" context="component" /><%-- PabloGilberto --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="119676" context="component" /><%-- Pops --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="150424" context="component" /><%-- RachaelLCook --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="10269872" context="component" /><%-- RoyItaqi --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="7463987" context="component" /><%-- ShindouHikaru --%></td>
                                </tr>
                                <tr>
                                    <td class="creditsName" style="padding-left:15px;"><tc-webtag:handle coderId="266705" context="component" /><%-- Sleeve --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="310233" context="component" /><%-- Standlove --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="283991" context="component" /><%-- StinkyCheeseMan --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="278460" context="component" /><%-- TAG --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="297731" context="component" /><%-- TheCois --%></td>
                                </tr>
                                <tr>
                                    <td class="creditsName" style="padding-left:15px;"><tc-webtag:handle coderId="150498" context="component" /><%-- ThinMan --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="301597" context="component" /><%-- Thinfox --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="302018" context="component" /><%-- Tomson --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="260952" context="component" /><%-- UFP2161 --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="8544935" context="component" /><%-- Wendell --%></td>
                                </tr>
                                <tr>
                                    <td class="creditsName" style="padding-left:15px;"><tc-webtag:handle coderId="286907" context="component" /><%-- WishingBone --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="286911" context="component" /><%-- XuChuan --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="20188980" context="component" /><%-- abelli --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="278342" context="component" /><%-- adic --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="11824548" context="component" /><%-- air2cold --%></td>
                                </tr>
                                <tr>
                                    <td class="creditsName" style="padding-left:15px;"><tc-webtag:handle coderId="266149" context="component" /><%-- akhil_bansal --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="277356" context="component" /><%-- aksonov --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="7231913" context="component" /><%-- alanSunny --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="10336829" context="component" /><%-- albertwang --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="154579" context="component" /><%-- Altrag --%></td>
                                </tr>
                                <tr>
                                    <td class="creditsName" style="padding-left:15px;"><tc-webtag:handle coderId="260911" context="component" /><%-- amitc --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="287614" context="component" /><%-- argolite --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="7489235" context="component" /><%-- arylio --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="20076717" context="component" /><%-- assistant --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="11797255" context="component" /><%-- aubergineanode --%></td>
                                </tr>
                                <tr>
                                    <td class="creditsName" style="padding-left:15px;"><tc-webtag:handle coderId="14926554" context="component" /><%-- biotrail --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="153434" context="component" /><%-- bokbok --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="13379412" context="component" /><%-- bose_java --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="11770376" context="component" /><%-- brian_cn --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="15868222" context="component" /><%-- cmax --%></td>
                                </tr>
                                <tr>
                                    <td class="creditsName" style="padding-left:15px;"><tc-webtag:handle coderId="10535364" context="component" /><%-- codedoc --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="10098406" context="component" /><%-- colau --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="8389509" context="component" /><%-- cosherx --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="20708384" context="component" /><%-- crackme --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="7545675" context="component" /><%-- cucu --%></td>
                                </tr>
                                <tr>
                                    <td class="creditsName" style="padding-left:15px;"><tc-webtag:handle coderId="281421" context="component" /><%-- custos --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="8347577" context="component" /><%-- daiwb --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="260578" context="component" /><%-- danno --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="7360309" context="component" /><%-- dmks --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="251184" context="component" /><%-- dplass --%></td>
                                </tr>
                                <tr>
                                    <td class="creditsName" style="padding-left:15px;"><tc-webtag:handle coderId="7390772" context="component" /><%-- duner --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="13324255" context="component" /><%-- fairytale --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="22498968" context="component" /><%-- flying2hk --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="152605" context="component" /><%-- georgepf --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="11770877" context="component" /><%-- gua --%></td>
                                </tr>
                                <tr>
                                    <td class="creditsName" style="padding-left:15px;"><tc-webtag:handle coderId="15832162" context="component" /><%-- haozhangr --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="11889718" context="component" /><%-- henryouly --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="16096823" context="component" /><%-- hohosky --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="154307" context="component" /><%-- holmana --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="22627015" context="component" /><%-- iamajia --%></td>
                                </tr>
                                <tr>
                                    <td class="creditsName" style="padding-left:15px;"><tc-webtag:handle coderId="10275123" context="component" /><%-- icyriver --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="20719960" context="component" /><%-- idx --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="10447013" context="component" /><%-- iggy36 --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="299180" context="component" /><%-- isv --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="156859" context="component" /><%-- ivern --%></td>
                                </tr>
                                <tr>
                                    <td class="creditsName" style="padding-left:15px;"><tc-webtag:handle coderId="288429" context="component" /><%-- jfjiang --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="11775761" context="component" /><%-- justforplay --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="11950083" context="component" /><%-- kinfkong --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="10353806" context="component" /><%-- kr00tki --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="15147311" context="component" /><%-- littlebull --%></td>
                                </tr>
                                <tr>
                                    <td class="creditsName" style="padding-left:15px;"><tc-webtag:handle coderId="21075542" context="component" /><%-- lyt --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="14788013" context="component" /><%-- magicpig --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="10425804" context="component" /><%-- maone --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="7444051" context="component" /><%-- marijnk --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="7251152" context="component" /><%-- matmis --%></td>
                                </tr>
                                <tr>
                                    <td class="creditsName" style="padding-left:15px;"><tc-webtag:handle coderId="15655112" context="component" /><%-- mayi --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="11971764" context="component" /><%-- mgmg --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="299904" context="component" /><%-- mishagam --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="20758806" context="component" /><%-- myxgyy --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="11789293" context="component" /><%-- nhzp339 --%></td>
                                </tr>
                                <tr>
                                    <td class="creditsName" style="padding-left:15px;"><tc-webtag:handle coderId="293874" context="component" /><%-- nicka81 --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="302053" context="component" /><%-- oldbig --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="15832159" context="component" /><%-- oodinary --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="301504" context="component" /><%-- opi --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="150940" context="component" /><%-- orb --%></td>
                                </tr>
                                <tr>
                                    <td class="creditsName" style="padding-left:15px;"><tc-webtag:handle coderId="153089" context="component" /><%-- preben --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="296745" context="component" /><%-- pzhao --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="10650643" context="component" /><%-- qiucx0161 --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="15891862" context="component" /><%-- real_vg --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="8394868" context="component" /><%-- rem --%></td>
                                </tr>
                                <tr>
                                    <td class="creditsName" style="padding-left:15px;"><tc-webtag:handle coderId="7389864" context="component" /><%-- roma --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="293470" context="component" /><%-- sapro --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="15816101" context="component" /><%-- sb99 --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="278595" context="component" /><%-- seaniswise --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="11802577" context="component" /><%-- semi_sleep --%></td>
                                </tr>
                                <tr>
                                    <td class="creditsName" style="padding-left:15px;"><tc-webtag:handle coderId="7548200" context="component" /><%-- sindu --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="15845095" context="component" /><%-- singlewood --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="10119301" context="component" /><%-- skatou --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="10022398" context="component" /><%-- slion --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="9971384" context="component" /><%-- smell --%></td>
                                </tr>
                                <tr>
                                    <td class="creditsName" style="padding-left:15px;"><tc-webtag:handle coderId="158236" context="component" /><%-- snard6 --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="11798503" context="component" /><%-- southwang --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="296145" context="component" /><%-- srowen --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="15500330" context="component" /><%-- still --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="299761" context="component" /><%-- techie1 --%></td>
                                </tr>
                                <tr>
                                    <td class="creditsName" style="padding-left:15px;"><tc-webtag:handle coderId="10169506" context="component" /><%-- telly12 --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="15608845" context="component" /><%-- topgear --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="280167" context="component" /><%-- ttsuchi --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="11781622" context="component" /><%-- tuenm --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="21471587" context="component" /><%-- urtks --%></td>
                                </tr>
                                <tr>
                                    <td class="creditsName" style="padding-left:15px;"><tc-webtag:handle coderId="269515" context="component" /><%-- valeriy --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="13377493" context="component" /><%-- victor_lxd --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="8416548" context="component" /><%-- viking_hz --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="15002482" context="component" /><%-- vilain --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="288617" context="component" /><%-- vinnath --%></td>
                                </tr>
                                <tr>
                                    <td class="creditsName" style="padding-left:15px;"><tc-webtag:handle coderId="299979" context="component" /><%-- visualage --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="8375801" context="component" /><%-- vividmxx --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="15197513" context="component" /><%-- waits --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="10405908" context="component" /><%-- wiedzmin --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="9981727" context="component" /><%-- woodjhon --%></td>
                                </tr>
                                <tr>
                                    <td class="creditsName" style="padding-left:15px;"><tc-webtag:handle coderId="272311" context="component" /><%-- yellow_gecko --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="19929536" context="component" /><%-- zhuzeyuan --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="264803" context="component" /><%-- zimmy --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="10526732" context="component" /><%-- zjq --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="14820574" context="component" /><%-- zsudraco --%></td>
                                </tr>
                                <tr>
                                    <td colspan="5">&#160;</td>
                                </tr>
                            </table>
                        </td>
                    </tr>

                    <c:set var="softawreServer" value="<%= ApplicationServer.SOFTWARE_SERVER_NAME %>" />
                    <c:set var="componentURL" value="http://${softawreServer}/catalog/c_component.jsp?comp=" />
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
                                    <td class="creditsName2"><a href="${componentURL}6403441">Authentication Factory</a> 2.0</td>
                                    <td class="creditsName2"><a href="${componentURL}22461805">Auto Pilot</a> 1.0</td>
                                </tr>
                                <tr>
                                    <td class="creditsName2" style="padding-left:15px;"><a href="${componentURL}22462052">Auto Screening Tool</a> 1.0</td>
                                    <td class="creditsName2"><a href="${componentURL}5710093">Base Exception</a> 1.0</td>
                                    <td class="creditsName2"><a href="${componentURL}22132721">Cached Web Element Tag</a> 1.0</td>
                                </tr>
                                <tr>
                                    <td class="creditsName2" style="padding-left:15px;"><a href="${componentURL}7534130">Class Associations</a> 1.0</td>
                                    <td class="creditsName2"><a href="${componentURL}5700527">Command Line Utility</a> 1.0</td>
                                    <td class="creditsName2"><a href="${componentURL}5703130">Compression Utility</a> 2.0.1</td>
                                </tr>
                                <tr>
                                    <td class="creditsName2" style="padding-left:15px;"><a href="${componentURL}500004">Configuration Manager</a> 2.1.5</td>
                                    <td class="creditsName2"><a href="${componentURL}7339708">Data Paging Tag</a> 2.0</td>
                                    <td class="creditsName2"><a href="${componentURL}7220290">Data Validation</a> 1.0</td>
                                </tr>
                                <tr>
                                    <td class="creditsName2" style="padding-left:15px;"><a href="${componentURL}2809552">Database Abstraction</a> 1.1</td>
                                    <td class="creditsName2"><a href="${componentURL}14803866">DB Connection Factory</a> 1.0</td>
                                    <td class="creditsName2"><a href="${componentURL}22398447">Deliverable Management</a> 1.0</td>
                                </tr>
                                <tr>
                                    <td class="creditsName2" style="padding-left:15px;"><a href="${componentURL}11842267">Directory Validation</a> 1.0</td>
                                    <td class="creditsName2"><a href="${componentURL}600038">Document Generator</a> 2.0</td>
                                    <td class="creditsName2"><a href="${componentURL}600056">Email Engine</a> 3.0</td>
                                </tr>
                                <tr>
                                    <td class="creditsName2" style="padding-left:15px;"><a href="${componentURL}3102797">Executable Wrapper 1.0</a></td>
                                    <td class="creditsName2"><a href="${componentURL}6509957">File Class 1.0</a></td>
                                    <td class="creditsName2"><a href="${componentURL}16069060">File System Server</a> 1.0</td>
                                </tr>
                                <tr>
                                    <td class="creditsName2" style="padding-left:15px;"><a href="${componentURL}600131">File Upload</a> 2.0</td>
                                    <td class="creditsName2"><a href="${componentURL}7323082">Generic Event Manager</a> 1.0</td>
                                    <td class="creditsName2"><a href="${componentURL}10092124">Guid Generator</a> 1.0</td>
                                </tr>
                                <tr>
                                    <td class="creditsName2" style="padding-left:15px;"><a href="${componentURL}4201389">Heartbeat</a> 1.0</td>
                                    <td class="creditsName2"><a href="${componentURL}3105029">ID Generator</a> 3.0</td>
                                    <td class="creditsName2"><a href="${componentURL}11979667">IP Server</a> 2.0.1</td>
                                </tr>
                                <tr>
                                    <td class="creditsName2" style="padding-left:15px;"><a href="${componentURL}5800426">JNDI Context Utility</a> 1.0</td>
                                    <td class="creditsName2"><a href="${componentURL}3300911">Job Scheduler</a> 1.0</td>
                                    <td class="creditsName2"><a href="${componentURL}2300015">Logging Wrapper</a> 1.2</td>
                                </tr>
                                <tr>
                                    <td class="creditsName2" style="padding-left:15px;"><a href="${componentURL}3501804">Magic Numbers</a> 1.0</td>
                                    <td class="creditsName2"><a href="${componentURL}4228093">Object Factory</a> 2.0.1</td>
                                    <td class="creditsName2"><a href="${componentURL}22398541">Phase Management</a> 1.0</td>
                                </tr>
                                <tr>
                                    <td class="creditsName2" style="padding-left:15px;"><a href="${componentURL}22398400">Project Management</a> 1.0</td>
                                    <td class="creditsName2"><a href="${componentURL}22398562">Project Phase Template</a> 1.0</td>
                                    <td class="creditsName2"><a href="${componentURL}11821273">Project Phases</a> 2.0</td>
                                </tr>
                                <tr>
                                    <td class="creditsName2" style="padding-left:15px;"><a href="${componentURL}22398426">Resource Management</a> 1.0</td>
                                    <td class="creditsName2"><a href="${componentURL}22268426">Review Data Structure</a> 1.0</td>
                                    <td class="creditsName2"><a href="${componentURL}22268453">Review Management</a> 1.0</td>
                                </tr>
                                <tr>
                                    <td class="creditsName2" style="padding-left:15px;"><a href="${componentURL}22398339">Review Score Aggregator</a> 1.0</td>
                                    <td class="creditsName2"><a href="${componentURL}22397890">Review Score Calculator</a> 1.0</td>
                                    <td class="creditsName2"><a href="${componentURL}22268595">Scorecard Data Structure</a> 1.0</td>
                                </tr>
                                <tr>
                                    <td class="creditsName2" style="padding-left:15px;"><a href="${componentURL}22268578">Scorecard Management</a> 1.0</td>
                                    <td class="creditsName2"><a href="${componentURL}11884906">Search Builder</a> 1.3</td>
                                    <td class="creditsName2"><a href="${componentURL}600215">Security Manager</a> 1.1</td>
                                </tr>
                                <tr>
                                    <td class="creditsName2" style="padding-left:15px;"><a href="${componentURL}2804505">Simple Cache</a> 2.0</td>
                                    <td class="creditsName2"><a href="${componentURL}7361823">Type Safe Enum</a> 1.0</td>
                                    <td class="creditsName2"><a href="${componentURL}3500966">Weighted Calculator</a> 1.0</td>
                                </tr>
                                <tr>
                                    <td class="creditsName2" style="padding-left:15px;"><a href="${componentURL}11821243">Workdays</a> 1.0</td>
                                    <td class="creditsName2"><a href="${componentURL}13272138">XMI Parser</a> 1.1</td>
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

                </div>
            </div>
        
        <jsp:include page="/includes/inc_footer.jsp" />

    </div>

</div>

</body>
</html:html>
