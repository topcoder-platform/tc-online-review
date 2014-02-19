<%--
  - Author: TCSASSEMBLER
  - Version: 2.0
  - Copyright (C) 2013 - 2014 TopCoder Inc., All Rights Reserved.
  -
  - Description: This page fragment displays the content of prizes on Project Details screen.
--%>
<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="or" uri="/or-tags" %>
<%@ taglib prefix="tc-webtag" uri="/tags/tc-webtags" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>

<div>
    <ul id="tablist">
        <li class="current"><a onclick="return showPrizeTab('contest-prizes-table', this);" href="javascript:void(0)"><or:text key="editProject.ProjectDetails.ContestPrizes" /></a></li>
        <li><a onclick="return showPrizeTab('checkpoint-prizes-table', this);" href="javascript:void(0)"><or:text key="editProject.ProjectDetails.CheckpointPrizes" /></a></li>
    </ul>
    <div style="clear:both;"></div>
</div>

<table id="contest-prizes-table" class="scorecard" width="100%" cellpadding="0" cellspacing="0" style="border-collapse:collapse;">
    <tr>
        <td class="title" colspan="3"><or:text key="editProject.ProjectDetails.ContestPrizes" /></td>
    </tr>
    <tr>
        <td class="header"><or:text key="editProject.ProjectDetails.Prize.Place" /></td>
        <td class="header"><or:text key="editProject.ProjectDetails.Prize.Amount" /></td>
        <td class="header"><or:text key="editProject.ProjectDetails.Prize.NoOfPrizes" /></td>
    </tr>
    <c:forEach items="${contestPrizes}" var="prize" varStatus="vs">
        <tr <c:if test="${vs.index % 2 eq 0}">class="light"</c:if> <c:if test="${vs.index % 2 eq 1}">class="dark"</c:if> >
            <td class="value">${vs.count}</td>
            <td class="value" nowrap="nowrap">${"$"}${orfn:displayPaymentAmt(pageContext.request, prize.prizeAmount)}</td>
            <td class="value" nowrap="nowrap">${prize.numberOfSubmissions}</td>
        </tr>
    </c:forEach>
    <c:if test="${fn:length(contestPrizes) eq 0}">
        <tr class="light"><td class="value" nowrap="nowrap" colspan="3">There are no prizes.</td></tr>
    </c:if>
    <tr>
        <td class="lastRowTD" colspan="3"><!-- @ --></td>
    </tr>
</table>

<table id="checkpoint-prizes-table" class="scorecard" width="100%" cellpadding="0" cellspacing="0" style="border-collapse:collapse;display: none">
    <tr>
        <td class="title" colspan="3"><or:text key="editProject.ProjectDetails.CheckpointPrizes" /></td>
    </tr>
    <tr>
        <td class="header"><or:text key="editProject.ProjectDetails.Prize.Place" /></td>
        <td class="header"><or:text key="editProject.ProjectDetails.Prize.Amount" /></td>
        <td class="header"><or:text key="editProject.ProjectDetails.Prize.NoOfPrizes" /></td>
    </tr>
    <c:forEach items="${checkpointPrizes}" var="prize" varStatus="vs">
        <tr <c:if test="${vs.index % 2 eq 0}">class="light"</c:if> <c:if test="${vs.index % 2 eq 1}">class="dark"</c:if> >
            <td class="value">${vs.count}</td>
            <td class="value" nowrap="nowrap">${"$"}${orfn:displayPaymentAmt(pageContext.request, prize.prizeAmount)}</td>
            <td class="value" nowrap="nowrap">${prize.numberOfSubmissions}</td>
        </tr>
    </c:forEach>
    <c:if test="${fn:length(checkpointPrizes) eq 0}">
        <tr class="light"><td class="value" nowrap="nowrap" colspan="3">There are no prizes.</td></tr>
    </c:if>
    <tr>
        <td class="lastRowTD" colspan="3"><!-- @ --></td>
    </tr>
</table><br/>

<script type="text/javascript">
    function showPrizeTab(id, aObject) {
        var liEles = aObject.parentNode.parentNode.getElementsByTagName("li");
        for (var i = 0; i < liEles.length; i++) {
            liEles[i].className = "";
        }
        aObject.parentNode.className = "current";

        // Remove focus from the link that triggered the activation
        if (aObject.blur) {
            aObject.blur();
        }

        document.getElementById("contest-prizes-table").style.display = "none";
        document.getElementById("checkpoint-prizes-table").style.display = "none";
        document.getElementById(id).style.display = "table";
    }
</script>
