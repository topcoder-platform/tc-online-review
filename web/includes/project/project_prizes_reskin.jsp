<%--
  - Author: TCSASSEMBLER
  - Version: 2.0
  - Copyright (C) 2013 - 2014 TopCoder Inc., All Rights Reserved.
  -
  - Description: This page fragment displays the content of prizes on Project Details screen.
--%>
<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="or" uri="/or-tags" %>
<%@ taglib prefix="tc-webtag" uri="/tags/tc-webtags" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>

<div class="projectDetails">
    <div class="projectDetails__sectionHeader">
        <div class="projectDetails__title">
            Prizes
        </div>
        <div class="projectDetails__accordion">
        </div>
    </div>
    

    <div class="projectDetails__sectionBody">  

        <div class="projectDetails__tabs">
            <div class="projectDetails__tab projectDetails__tab--active"><a onclick="return showPrizeTab('contest-prizes-table', this);" href="javascript:void(0)"><or:text key="editProject.ProjectDetails.ContestPrizes" /></a></div>
            <div class="projectDetails__tab"><a onclick="return showPrizeTab('checkpoint-prizes-table', this);" href="javascript:void(0)"><or:text key="editProject.ProjectDetails.CheckpointPrizes" /></a></div>
        </div>
  
        <table id="contest-prizes-table" class="prizesTable" width="100%" cellpadding="0" cellspacing="0" style="border-collapse:collapse;">
            <thead class="prizesTable__header">
                <tr>
                    <th><or:text key="editProject.ProjectDetails.Prize.Place" /></th>
                    <th><or:text key="editProject.ProjectDetails.Prize.Amount" /></th>
                    <th><or:text key="editProject.ProjectDetails.Prize.NoOfPrizes" /></th>
                </tr>
            </thead>
            <tbody class="prizesTable__body">
            <c:forEach items="${contestPrizes}" var="prize" varStatus="vs">
                <tr>
                    <td>${vs.count}</td>
                    <td nowrap="nowrap">${"$"}${orfn:displayPaymentAmt(pageContext.request, prize.prizeAmount)}</td>
                    <td nowrap="nowrap">${prize.numberOfSubmissions}</td>
                </tr>
            </c:forEach>
            <c:if test="${fn:length(contestPrizes) eq 0}">
                <tr><td nowrap="nowrap" colspan="3">There are no prizes.</td></tr>
            </c:if>
            </tbody>
        </table>

        <table id="checkpoint-prizes-table" class="prizesTable" width="100%" cellpadding="0" cellspacing="0" style="border-collapse:collapse;display: none">
            <thead class="prizesTable__header">
                <tr>
                    <th><or:text key="editProject.ProjectDetails.Prize.Place" /></th>
                    <th><or:text key="editProject.ProjectDetails.Prize.Amount" /></th>
                    <th><or:text key="editProject.ProjectDetails.Prize.NoOfPrizes" /></th>
                </tr>
            </thead>
            <tbody class="prizesTable__body">
            <c:forEach items="${checkpointPrizes}" var="prize" varStatus="vs">
                <tr>
                    <td>${vs.count}</td>
                    <td nowrap="nowrap">${"$"}${orfn:displayPaymentAmt(pageContext.request, prize.prizeAmount)}</td>
                    <td nowrap="nowrap">${prize.numberOfSubmissions}</td>
                </tr>
            </c:forEach>
            <c:if test="${fn:length(checkpointPrizes) eq 0}">
                <tr><td nowrap="nowrap" colspan="3">There are no prizes.</td></tr>
            </c:if>
            </tbody>
        </table>
    </div>

</div>

<script type="text/javascript">
    function showPrizeTab(id, aObject) {
        var liEles = aObject.parentNode.parentNode.getElementsByTagName("div");
        for (var i = 0; i < liEles.length; i++) {
            liEles[i].className = "projectDetails__tab";
        }
        aObject.parentNode.className = "projectDetails__tab projectDetails__tab--active";

        // Remove focus from the link that triggered the activation
        if (aObject.blur) {
            aObject.blur();
        }

        document.getElementById("contest-prizes-table").style.display = "none";
        document.getElementById("checkpoint-prizes-table").style.display = "none";
        document.getElementById(id).style.display = "table";
    }
</script>
