<%--
  - Author: TCSASSEMBLER
  - Version: 2.0
  - Copyright (C) 2014 TopCoder Inc., All Rights Reserved.
  -
  - Description: This page displays the credits page.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="or" uri="/or-tags" %>
<%@ taglib prefix="tc-webtag" uri="/tags/tc-webtags" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>
<%@ page import="com.topcoder.shared.util.ApplicationServer"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<head>
    <title><or:text key="global.title.level2.noDash"
        arg0='${orfn:getMessage(pageContext, "OnlineReviewApp.title")}'
        arg1='${orfn:getMessage(pageContext, "credits.title")}' /></title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

    <!-- TopCoder CSS -->
    <link type="text/css" rel="stylesheet" href="/css/style.css" />
    <link type="text/css" rel="stylesheet" href="/css/coders.css" />
    <link type="text/css" rel="stylesheet" href="/css/stats.css" />
    <link type="text/css" rel="stylesheet" href="/css/tcStyles.css" />

    <!-- CSS and JS from wireframes -->
    <script language="javascript" type="text/javascript" src="/js/or/expand_collapse.js"><!-- @ --></script>
    <link rel="stylesheet" type="text/css" href="/css/or/stylesheet.css" />

    <!-- CSS and JS by Petar -->
    <link type="text/css" rel="stylesheet" href="/css/or/new_styles.css" />
    <script language="JavaScript" type="text/javascript" src="/js/or/rollovers2.js"><!-- @ --></script>

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
                        <td class="creditsHeader" colspan="3"><or:text key="credits.SubTitle" /></td>
                    </tr>
                    <tr style="background-image:url(/i/or/credits_bk.gif);background-repeat:repeat-x;">
                        <td valign="top">
                            <table width="100%" cellpadding="0" cellspacing="0">
                                <tr>
                                    <td class="creditsTitle"><br /><or:text key="credits.QA" /></td>
                                </tr>
                                <tr>
                                    <td class="creditsSub">
                                        <tc-webtag:handle coderId="16096823" /><%-- hohosky --%><br />
                                        <tc-webtag:handle coderId="10336829" /><%-- albertwang --%></td>
                                </tr>
                                <tr>
                                    <td>&#160;</td>
                                </tr>
                            </table>
                        </td>
                        <td valign="top">
                            <table width="100%" cellpadding="0" cellspacing="0">
                                <tr>
                                    <td class="creditsTitle"><br /><or:text key="credits.Architect" /></td>
                                </tr>
                                <tr>
                                    <td class="creditsSub"><tc-webtag:handle coderId="286907" /><%-- WishingBone --%></td>
                                </tr>
                            </table>
                        </td>
                        <td valign="top">
                            <table width="100%" cellpadding="0" cellspacing="0">
                                <tr>
                                    <td class="creditsTitle" colspan="4"><br /><or:text key="credits.Assemblers" /></td>
                                </tr>
                                <tr>
                                    <td class="creditsSub">
                                        <tc-webtag:handle coderId="20003257" /><%-- George1 --%> &#160;
                                        <tc-webtag:handle coderId="15891862" /><%-- real_vg --%><br />
                                        <tc-webtag:handle coderId="11770376" /><%-- brian_cn --%> &#160;
                                        <tc-webtag:handle coderId="22498968" /><%-- flying2hk --%></td>
                                </tr>
                            </table>
                        </td>
                    </tr>

                    <tr style="background-image:url(/i/or/credits_bk.gif);background-repeat:repeat-x;">
                        <td valign="top" colspan="3">
                            <table width="100%" cellpadding="0" cellspacing="0">
                                <tr>
                                    <td class="creditsTitle" colspan="5"><br /><br /><or:text key="credits.Des_Dev_Review" /></td>
                                </tr>
                                <tr>
                                    <td colspan="5" class="creditsText" style="padding:5px 15px 16px 15px;"><or:text key="credits.MebersExplanatory" /></td>
                                </tr>
                                <tr>
                                    <td class="creditsName" style="padding-left:15px;"><tc-webtag:handle coderId="251989" /><%-- AdamSelene --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="252022" /><%-- AleaActaEst --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="12029342" /><%-- Angen --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="7442489" /><%-- BEHiker57W --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="151360" /><%-- BigDaddy --%></td>
                                </tr>
                                <tr>
                                    <td class="creditsName" style="padding-left:15px;"><tc-webtag:handle coderId="290448" /><%-- BryanChen --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="12006665" /><%-- Chenhong --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="108281" /><%-- Code-Guru --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="159921" /><%-- DaTwinkDaddy --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="10143068" /><%-- DanLazar --%></td>
                                </tr>
                                <tr>
                                    <td class="creditsName" style="padding-left:15px"><tc-webtag:handle coderId="289864" /><%-- FTolToaster --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="15050434" /><%-- FireIce --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="11981278" /><%-- GavinWang --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="151743" /><%-- Ghostar --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="154754" /><%-- Ken_Vogel --%></td>
                                </tr>
                                <tr>
                                    <td class="creditsName" style="padding-left:15px;"><tc-webtag:handle coderId="15692538" /><%-- King_Bette --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="10348862" /><%-- Luca --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="289824" /><%-- MPhk --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="292451" /><%-- Mr.Sketch --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="9998760" /><%-- PE --%></td>
                                </tr>
                                <tr>
                                    <td class="creditsName" style="padding-left:15px;"><tc-webtag:handle coderId="112438" /><%-- PabloGilberto --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="119676" /><%-- Pops --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="150424" /><%-- RachaelLCook --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="10269872" /><%-- RoyItaqi --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="7463987" /><%-- ShindouHikaru --%></td>
                                </tr>
                                <tr>
                                    <td class="creditsName" style="padding-left:15px;"><tc-webtag:handle coderId="266705" /><%-- Sleeve --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="310233" /><%-- Standlove --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="283991" /><%-- StinkyCheeseMan --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="278460" /><%-- TAG --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="297731" /><%-- TheCois --%></td>
                                </tr>
                                <tr>
                                    <td class="creditsName" style="padding-left:15px;"><tc-webtag:handle coderId="150498" /><%-- ThinMan --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="301597" /><%-- Thinfox --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="302018" /><%-- Tomson --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="260952" /><%-- UFP2161 --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="8544935" /><%-- Wendell --%></td>
                                </tr>
                                <tr>
                                    <td class="creditsName" style="padding-left:15px;"><tc-webtag:handle coderId="286907" /><%-- WishingBone --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="286911" /><%-- XuChuan --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="20188980" /><%-- abelli --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="278342" /><%-- adic --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="11824548" /><%-- air2cold --%></td>
                                </tr>
                                <tr>
                                    <td class="creditsName" style="padding-left:15px;"><tc-webtag:handle coderId="266149" /><%-- akhil_bansal --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="277356" /><%-- aksonov --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="7231913" /><%-- alanSunny --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="10336829" /><%-- albertwang --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="154579" /><%-- Altrag --%></td>
                                </tr>
                                <tr>
                                    <td class="creditsName" style="padding-left:15px;"><tc-webtag:handle coderId="260911" /><%-- amitc --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="287614" /><%-- argolite --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="7489235" /><%-- arylio --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="20076717" /><%-- assistant --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="11797255" /><%-- aubergineanode --%></td>
                                </tr>
                                <tr>
                                    <td class="creditsName" style="padding-left:15px;"><tc-webtag:handle coderId="14926554" /><%-- biotrail --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="153434" /><%-- bokbok --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="13379412" /><%-- bose_java --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="11770376" /><%-- brian_cn --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="15868222" /><%-- cmax --%></td>
                                </tr>
                                <tr>
                                    <td class="creditsName" style="padding-left:15px;"><tc-webtag:handle coderId="10535364" /><%-- codedoc --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="10098406" /><%-- colau --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="8389509" /><%-- cosherx --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="20708384" /><%-- crackme --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="7545675" /><%-- cucu --%></td>
                                </tr>
                                <tr>
                                    <td class="creditsName" style="padding-left:15px;"><tc-webtag:handle coderId="281421" /><%-- custos --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="8347577" /><%-- daiwb --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="260578" /><%-- danno --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="7360309" /><%-- dmks --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="251184" /><%-- dplass --%></td>
                                </tr>
                                <tr>
                                    <td class="creditsName" style="padding-left:15px;"><tc-webtag:handle coderId="7390772" /><%-- duner --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="13324255" /><%-- fairytale --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="22498968" /><%-- flying2hk --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="152605" /><%-- georgepf --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="11770877" /><%-- gua --%></td>
                                </tr>
                                <tr>
                                    <td class="creditsName" style="padding-left:15px;"><tc-webtag:handle coderId="15832162" /><%-- haozhangr --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="11889718" /><%-- henryouly --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="16096823" /><%-- hohosky --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="154307" /><%-- holmana --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="22627015" /><%-- iamajia --%></td>
                                </tr>
                                <tr>
                                    <td class="creditsName" style="padding-left:15px;"><tc-webtag:handle coderId="10275123" /><%-- icyriver --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="20719960" /><%-- idx --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="10447013" /><%-- iggy36 --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="299180" /><%-- isv --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="156859" /><%-- ivern --%></td>
                                </tr>
                                <tr>
                                    <td class="creditsName" style="padding-left:15px;"><tc-webtag:handle coderId="288429" /><%-- jfjiang --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="11775761" /><%-- justforplay --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="11950083" /><%-- kinfkong --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="10353806" /><%-- kr00tki --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="15147311" /><%-- littlebull --%></td>
                                </tr>
                                <tr>
                                    <td class="creditsName" style="padding-left:15px;"><tc-webtag:handle coderId="21075542" /><%-- lyt --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="14788013" /><%-- magicpig --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="10425804" /><%-- maone --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="7444051" /><%-- marijnk --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="7251152" /><%-- matmis --%></td>
                                </tr>
                                <tr>
                                    <td class="creditsName" style="padding-left:15px;"><tc-webtag:handle coderId="15655112" /><%-- mayi --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="11971764" /><%-- mgmg --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="299904" /><%-- mishagam --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="20758806" /><%-- myxgyy --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="11789293" /><%-- nhzp339 --%></td>
                                </tr>
                                <tr>
                                    <td class="creditsName" style="padding-left:15px;"><tc-webtag:handle coderId="293874" /><%-- nicka81 --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="302053" /><%-- oldbig --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="15832159" /><%-- oodinary --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="301504" /><%-- opi --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="150940" /><%-- orb --%></td>
                                </tr>
                                <tr>
                                    <td class="creditsName" style="padding-left:15px;"><tc-webtag:handle coderId="153089" /><%-- preben --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="296745" /><%-- pzhao --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="10650643" /><%-- qiucx0161 --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="15891862" /><%-- real_vg --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="8394868" /><%-- rem --%></td>
                                </tr>
                                <tr>
                                    <td class="creditsName" style="padding-left:15px;"><tc-webtag:handle coderId="7389864" /><%-- roma --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="293470" /><%-- sapro --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="15816101" /><%-- sb99 --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="278595" /><%-- seaniswise --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="11802577" /><%-- semi_sleep --%></td>
                                </tr>
                                <tr>
                                    <td class="creditsName" style="padding-left:15px;"><tc-webtag:handle coderId="7548200" /><%-- sindu --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="15845095" /><%-- singlewood --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="10119301" /><%-- skatou --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="10022398" /><%-- slion --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="9971384" /><%-- smell --%></td>
                                </tr>
                                <tr>
                                    <td class="creditsName" style="padding-left:15px;"><tc-webtag:handle coderId="158236" /><%-- snard6 --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="11798503" /><%-- southwang --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="296145" /><%-- srowen --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="15500330" /><%-- still --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="299761" /><%-- techie1 --%></td>
                                </tr>
                                <tr>
                                    <td class="creditsName" style="padding-left:15px;"><tc-webtag:handle coderId="10169506" /><%-- telly12 --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="15608845" /><%-- topgear --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="280167" /><%-- ttsuchi --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="11781622" /><%-- tuenm --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="21471587" /><%-- urtks --%></td>
                                </tr>
                                <tr>
                                    <td class="creditsName" style="padding-left:15px;"><tc-webtag:handle coderId="269515" /><%-- valeriy --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="13377493" /><%-- victor_lxd --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="8416548" /><%-- viking_hz --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="15002482" /><%-- vilain --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="288617" /><%-- vinnath --%></td>
                                </tr>
                                <tr>
                                    <td class="creditsName" style="padding-left:15px;"><tc-webtag:handle coderId="299979" /><%-- visualage --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="8375801" /><%-- vividmxx --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="15197513" /><%-- waits --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="10405908" /><%-- wiedzmin --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="9981727" /><%-- woodjhon --%></td>
                                </tr>
                                <tr>
                                    <td class="creditsName" style="padding-left:15px;"><tc-webtag:handle coderId="272311" /><%-- yellow_gecko --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="19929536" /><%-- zhuzeyuan --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="264803" /><%-- zimmy --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="10526732" /><%-- zjq --%></td>
                                    <td class="creditsName"><tc-webtag:handle coderId="14820574" /><%-- zsudraco --%></td>
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
                                    <td class="creditsTitle" colspan="3"><br /><br /><or:text key="credits.Components" /></td>
                                </tr>
                                <tr>
                                    <td colspan="3" class="creditsText" style="padding:5px 15px 16px 15px;"><or:text key="credits.ComponentsExplanatory" /></td>
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
</html>
