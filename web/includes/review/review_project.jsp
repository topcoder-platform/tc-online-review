<%--
  - Author: TCSASSEMBLER
  - Version: 2.0
  - Copyright (C)  - 2014 TopCoder Inc., All Rights Reserved.
 --%>
<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="or" uri="/or-tags" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>
<%@ taglib prefix="tc-webtag" uri="/tags/tc-webtags" %>
<c:set var="toPDF" value="${param.pdf eq 'true'}"/>
<c:if test="${param.showFillScorecardLink}">
<script language="javascript" type="text/javascript">
    <!--
    function fillScorecard() {
        if (confirm("<or:text key="global.fillScorecardConfirmation" />")) {
            var scores = document.getElementsByTagName("select");
            for (var i = 0; i < scores.length; i++) {
                if (scores[i].getAttribute("name").indexOf("answer[") == 0) {
                    if (scores[i].selectedIndex == 0) {
                        var options = scores[i].options;
                        for (var j = 1; j < options.length ; j++) {
                            if (options[j].value.indexOf("/") >= 0) {
                                scores[i].selectedIndex = scores[i].options.length - 1;
                                break;
                            } else if (options[j].value == "1") {
                                scores[i].selectedIndex = j;
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
     // -->
    </script>
</c:if>

<div class="scoreInfo__title">
    <button type="button" class="back-btn" onclick="history.back()">
        <i class="arrow-prev-icon"></i>
    </button>
    <h1 class="projectInfo__projectName">
        ${param.showScorecard ? orfn:htmlEncode(scorecardTemplate.name) : orfn:htmlEncode(project.allProperties['Project Name'])}
    </h1>
</div>
<c:if test="${!param.hideScoreInfo}">
<div class="scoreInfo__info">
    <div class="scoreInfo__reviewer">
        <c:if test="${reviewType ne 'AutoScreening' and reviewType ne 'CompositeReview'}">
            <c:if test="${reviewType eq 'SpecificationReview'}">
            <or:text key="editReview.SpecificationReviewer" />
            </c:if>
            <c:if test="${reviewType eq 'CheckpointScreening'}">
                <or:text key="editReview.CheckpointScreener" />
            </c:if>
            <c:if test="${reviewType eq 'CheckpointReview'}">
                <or:text key="editReview.CheckpointReviewer" />
            </c:if>
            <c:if test="${reviewType eq 'Screening'}">
                <or:text key="editReview.Screener" />
            </c:if>
            <c:if test="${reviewType eq 'Review'}">
                <or:text key="editReview.Reviewer" />
            </c:if>
            <c:if test="${reviewType eq 'Approval'}">
                <or:text key="editReview.Approver" />
            </c:if>
            <c:if test="${reviewType eq 'Aggregation' or reviewType eq 'AggregationReview'}">
                <or:text key="editReview.Aggregator" />
            </c:if>
            <c:if test="${reviewType eq 'FinalReview'}">
                <or:text key="editReview.FinalReviewer" />
            </c:if>
            <c:if test="${reviewType eq 'PostMortem'}">
                <or:text key="editReview.Post-MortemReviewer" />
            </c:if>
            <c:if test="${reviewType eq 'IterativeReview'}">
                <or:text key="editReview.IterativeReviewer" />
            </c:if>
            <c:if test="${not empty authorId}">
                <tc-webtag:handle coderId="${authorId}" context="${orfn:getHandlerContext(pageContext.request)}" />
            </c:if>
        </c:if>
    </div>
    <div class="scoreInfo__submission">
        <c:if test="${not empty sid}">
        <or:text key="editReview.Submission" /> ${sid}
        <c:if test="${not empty submitterId and reviewType ne 'IterativeReview' and not toPDF}">
            (<tc-webtag:handle coderId="${submitterId}" context="${orfn:getHandlerContext(pageContext.request)}" />)
        </c:if>
        <br />
        </c:if>
    </div>
    <div class="scoreInfo__date">
        <c:if test="${not empty modificationDate}">
        <or:text key="editReview.ModificationDate" />
        <c:out value="${orfn:displayDate(pageContext.request, modificationDate)}"/><br />
        </c:if>
    </div>
    <div class="scoreInfo__date">
        <or:text key="editReview.MyRole" /> ${myRole}<br />
    </div>
    <div class="scoreInfo__links">
        <c:if test="${canExport}">
            <a class="scoreInfo__link" href="<or:url value='/actions/ExportReview?reviewType=${reviewType}&rid=${param.rid}' />"><or:text key="exportReview.ExportToExcel" /></a>
        </c:if>
        <c:if test="${canReopenScorecard}">
            <a class="scoreInfo__link" href="<or:url value='/actions/ReopenScorecard?rid=${review.id}' />"><or:text key="editReview.ReopenScorecard" /></a>
        </c:if>
        <c:if test="${canEditScorecard}">
            <a class="scoreInfo__link" href="<or:url value='/actions/Edit${reviewType}?rid=${review.id}' />"><or:text key="editReview.EditScorecard" /></a>
        </c:if>
        <c:if test="${param.showFillScorecardLink}">
            <a class="scoreInfo__link" href="javascript:fillScorecard();"><or:text key="global.fillScorecard" /></a>
        </c:if>
    </div>
</div>
</c:if>






