<%--
  - Author: TCSASSEMBLER
  - Version: 2.0
  - Copyright (C) 2005 - 2014 TopCoder Inc., All Rights Reserved.
  -
  - Description: This page renders the Final Review scorecard.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="or" uri="/or-tags" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>
<%@ taglib prefix="tc-webtag" uri="/tags/tc-webtags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<head>
    <jsp:include page="/includes/project/project_title.jsp">
        <jsp:param name="thirdLevelPageKey" value="viewFinalReview.title" />
    </jsp:include>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

    <!-- Reskin -->
    <link type="text/css" rel="stylesheet" href="/css/reskin-or/reskin.css">
    <link type="text/css" rel="stylesheet" href="/css/reskin-or/toasts.css">

    <!-- TopCoder CSS -->
    <link type="text/css" rel="stylesheet" href="/css/style.css" />

    <!-- CSS and JS by Petar -->
    <script language="JavaScript" type="text/javascript"
        src="/js/or/rollovers2.js"><!-- @ --></script>

    <script type="text/javascript">
        document.addEventListener("DOMContentLoaded", function(){
            let avatar = document.querySelector('.webHeader__avatar a');
            let avatarImage = document.createElement('div');
            avatarImage.className = "webHeader__avatarImage";
            let twoChar = avatar.text.substring(0, 2);
            avatarImage.innerText = twoChar;
            avatar.innerHTML = avatarImage.outerHTML;
        });
    </script>
</head>

<body>
    <jsp:include page="/includes/inc_header_reskin.jsp" />
    <jsp:include page="/includes/project/project_tabs_reskin.jsp" />

    <div class="content content">
        <div class="content__inner">
            <jsp:include page="/includes/review/review_project.jsp" />
            <div class="divider"></div>
            <jsp:include page="/includes/review/review_table_title.jsp" />

            <c:set var="itemIdx" value="0" />

            <table class="scorecard" width="100%" cellpadding="0" cellspacing="0" border="0" style="border-collapse:collapse;">
                <c:forEach items="${scorecardTemplate.allGroups}" var="group" varStatus="groupStatus">
                    <tr>
                        <td class="title" colspan="6">${orfn:htmlEncode(group.name)}</td>
                    </tr>
                    <c:forEach items="${group.allSections}" var="section" varStatus="sectionStatus">
                        <tr>
                            <td class="subheader" width="100%" colspan="6">${orfn:htmlEncode(section.name)}</td>
                        </tr>
                        <c:forEach items="${section.allQuestions}" var="question" varStatus="questionStatus">
                            <tr class="light">
                                <td class="value" colspan="6">
                                    <div class="showText" id="shortQ_${itemIdx}">
                                        <a href="javascript:toggleDisplay('shortQ_${itemIdx}');toggleDisplay('longQ_${itemIdx}');" class="statLink"><img src="/i/reskin/chevron-down-sm.svg" alt="<or:text key='global.plus.alt' />" border="0" /></a>
                                        <b class="question"><or:text key="editReview.Question.title" /> ${groupStatus.index + 1}.${sectionStatus.index + 1}.${questionStatus.index + 1}</b>
                                        ${orfn:htmlEncode(question.description)}
                                    </div>
                                    <div class="hideText" id="longQ_${itemIdx}">
                                        <a href="javascript:toggleDisplay('shortQ_${itemIdx}');toggleDisplay('longQ_${itemIdx}');" class="statLink"><img src="/i/reskin/chevron-up-sm.svg" alt="<or:text key='global.minus.alt' />" border="0" /></a>
                                        <b class="question"><or:text key="editReview.Question.title" /> ${groupStatus.index + 1}.${sectionStatus.index + 1}.${questionStatus.index + 1}</b>
                                        ${orfn:htmlEncode(question.description)}
                                        <div class="guideline">
                                            ${orfn:htmlEncode(question.guideline)}
                                        </div>
                                    </div>
                                </td>
                                <c:set var="itemIdx" value="${itemIdx + 1}" />
                            </tr>

                            <tr class="finalResponse">
                                <td class="headerT"><or:text key="editReview.EditAggregation.CommentNumber" /></td>
                                <td class="header" width="89%"><or:text key="editReview.EditAggregation.Response" /></td>
                                <td class="header" width="11%"><or:text key="editReview.EditAggregation.Type" /></td>
                                <td class="header"><or:text key="FinalReviewItemStatus.Fixed" /></td>
                                <td class="headerN"><or:text key="FinalReviewItemStatus.NotFixed" /></td>
                            </tr>

                            <c:forEach items="${review.allItems}" var="item" varStatus="itemStatus">
                                <c:set var="commentNum" value="1" />
                                <c:set var="firstTime" value="${true}" />
                                <c:set var="lastCommentIdx" value="${lastCommentIdxs[itemStatus.index]}" />
                                <c:if test="${item.question == question.id}">
                                    <c:forEach items="${item.allComments}" var="comment" varStatus="commentStatus">
                                        <c:set var="commentType" value="${comment.commentType.name}" />
                                        <c:choose>
                                            <c:when test='${(commentType == "Required") || (commentType == "Recommended") || (commentType == "Comment")}'>
                                                <c:set var="isReviewerComment" value="${true}" />
                                            </c:when>
                                            <c:otherwise>
                                                <c:set var="isReviewerComment" value="${false}" />
                                            </c:otherwise>
                                        </c:choose>
                                        <c:if test='${(isReviewerComment == true) || (commentType == "Manager Comment") ||
                                                (commentType == "Appeal") || (commentType == "Appeal Response") ||
                                                (commentType == "Aggregation Comment") || (commentType == "Aggregation Review Comment") ||
                                                (commentType == "Submitter Comment") || (commentType == "Final Review Comment")}'>
                                            <c:set var="isLastCommentForItem" value="${commentStatus.index == lastCommentIdx - 1}" />
                                            <c:set var="rowClass" value='${(isLastCommentForItem == true) ? "value" : "valueNotLast"}'/>
                                            <tr class="finalComment">
                                                <c:if test="${isReviewerComment == true}">
                                                    <td class="headerT"><b>${commentNum}</b></td>
                                                    <c:set var="commentNum" value="${commentNum + 1}" />
                                                </c:if>
                                                <c:if test="${isReviewerComment != true}">
                                                    <td class="${rowClass}"><!-- @ --></td>
                                                </c:if>
                                                <td class="reviewerResp" width="89%">
                                                    <c:choose>
                                                        <c:when test="${isReviewerComment == true}">
                                                            <b><or:text key="editReview.EditAggregation.ReviewerResponse" /></b>
                                                        </c:when>
                                                        <c:when test='${(commentType == "Manager Comment") ||
                                                                (commentType == "Appeal") || (commentType == "Appeal Response") ||
                                                                (commentType == "Aggregation Comment") || (commentType == "Submitter Comment") ||
                                                                (commentType == "Final Review Comment")}'>
                                                            <b><or:text key='editReview.EditAggregation.${fn:replace(commentType, " ", "")}' /></b>
                                                        </c:when>
                                                        <c:when test='${commentType == "Aggregation Review Comment"}'>
                                                            <c:forEach items="${reviewResources}" var="resource">
                                                                <c:if test="${resource.id == comment.author}">
                                                                    <b><or:text key='ResourceRole.${fn:replace(resource.resourceRole.name, " ", "")}' />
                                                                    (<tc-webtag:handle coderId='${resource.allProperties["External Reference ID"]}' context="${orfn:getHandlerContext(pageContext.request)}" />)
                                                                    <or:text key="viewAggregationReview.Response" /></b>
                                                                </c:if>
                                                            </c:forEach>
                                                        </c:when>
                                                    </c:choose>
                                                    &nbsp;${orfn:htmlEncode(comment.comment)}
                                                </td>
                                                <c:if test="${isReviewerComment == true}">
                                                    <td class="reviewerResp" width="11%">
                                                        <b><or:text key='CommentType.${fn:replace(commentType, " ", "")}' /></b></td>
                                                    <td class="reviewerResp">
                                                        <input class="radio" type="radio" disabled="disabled" ${(comment.extraInfo == "Fixed") ? "checked='checked'" : ""} /></td>
                                                    <td class="reviewerResp">
                                                        <input class="radio" type="radio" disabled="disabled" ${(comment.extraInfo != "Fixed") ? "checked='checked'" : ""} /></td>
                                                </c:if>
                                                <c:if test="${isReviewerComment != true}">
                                                    <td class="${rowClass}"><!-- @ --></td>
                                                    <td class="${rowClass}"><!-- @ --></td>
                                                    <td class="${rowClass}"><!-- @ --></td>
                                                </c:if>
                                            </tr>
                                        </c:if>
                                    </c:forEach>
                                </c:if>
                            </c:forEach>
                        </c:forEach>
                    </c:forEach>
                </c:forEach>
                <tr>
                    <td class="lastRowTD" colspan="6"><!-- @ --></td>
                </tr>
            </table>

            <c:forEach items="${review.allComments}" var="comment">
                <c:if test='${(empty finalComment) && (comment.commentType.name == "Final Review Comment")}'>
                    <c:set var="finalComment" value="${comment}" />
                </c:if>
            </c:forEach>

            <table class="scorecard" width="100%" cellpadding="0" cellspacing="0" style="border-collapse:collapse;">
                <tr>
                    <td class="title"><or:text key="editFinalReview.box.Approval" /></td>
                </tr>
                <tr class="highlighted">
                    <c:choose>
                        <c:when test='${!(empty finalComment) && (finalComment.extraInfo == "Approved")}'>
                            <td class="approvalText"><or:text key="FinalReviewApprovalStatus.Approved" /></td>
                        </c:when>
                        <c:otherwise>
                            <td class="approvalText"><or:text key="FinalReviewApprovalStatus.Rejected" /></td>
                        </c:otherwise>
                    </c:choose>
                </tr>
                <tr>
                    <td class="lastRowTD"><!-- @ --></td>
                </tr>
            </table>

            <div align="right" style="margin-top: 56px;">
                <c:if test="${isPreview}">
                    <a href="javascript:window.close();"><img src="<or:text key='btnClose.img' />" alt="<or:text key='btnClose.alt' />" border="0" /></a>
                </c:if>
                <c:if test="${not isPreview}">
                    <a class="backToHome" href="<or:url value='/actions/ViewProjectDetails?pid=${project.id}' />">
                        <or:text key="btnBack.home"/>
                    </a>
                </c:if>
            </div>

        </div>
<jsp:include page="/includes/inc_footer_reskin.jsp" />
</body>
</html>
