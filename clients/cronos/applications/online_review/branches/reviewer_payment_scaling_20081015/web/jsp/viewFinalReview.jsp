<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="html" uri="/tags/struts-html" %>
<%@ taglib prefix="bean" uri="/tags/struts-bean" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>
<%@ taglib prefix="tc-webtag" uri="/tags/tc-webtags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html xhtml="true">

<head>
    <jsp:include page="/includes/project/project_title.jsp">
        <jsp:param name="thirdLevelPageKey" value="viewFinalReview.title" />
    </jsp:include>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

    <!-- TopCoder CSS -->
    <link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/style.css' />" />
    <link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/coders.css' />" />
    <link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/stats.css' />" />
    <link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/tcStyles.css' />" />

    <!-- CSS and JS by Petar -->
    <link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/or/new_styles.css' />" />
    <script language="JavaScript" type="text/javascript"
        src="<html:rewrite href='/js/or/rollovers2.js' />"><!-- @ --></script>
</head>

<body>
<div align="center">
    
    <div class="maxWidthBody" align="left">

        <jsp:include page="/includes/inc_header.jsp" />
        
        <jsp:include page="/includes/project/project_tabs.jsp" />
        
            <div id="mainMiddleContent">
                <div style="position: relative; width: 100%;">

                    <jsp:include page="/includes/review/review_project.jsp" />
                    <h3><bean:message key="editFinalReview.Scorecard.title" /></h3>

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
                                                <a href="javascript:toggleDisplay('shortQ_${itemIdx}');toggleDisplay('longQ_${itemIdx}');" class="statLink"><html:img src="/i/or/plus.gif" altKey="global.plus.alt" border="0" /></a>
                                                <b><bean:message key="editReview.Question.title" /> ${groupStatus.index + 1}.${sectionStatus.index + 1}.${questionStatus.index + 1}</b>
                                                ${orfn:htmlEncode(question.description)}
                                            </div>
                                            <div class="hideText" id="longQ_${itemIdx}">
                                                <a href="javascript:toggleDisplay('shortQ_${itemIdx}');toggleDisplay('longQ_${itemIdx}');" class="statLink"><html:img src="/i/or/minus.gif" altKey="global.minus.alt" border="0" /></a>
                                                <b><bean:message key="editReview.Question.title" /> ${groupStatus.index + 1}.${sectionStatus.index + 1}.${questionStatus.index + 1}</b>
                                                ${orfn:htmlEncode(question.description)}<br />
                                                ${orfn:htmlEncode(question.guideline)}
                                            </div>
                                        </td>
                                        <c:set var="itemIdx" value="${itemIdx + 1}" />
                                    </tr>

                                    <tr>
                                        <td class="header"><bean:message key="editReview.EditAggregation.Reviewer" /></td>
                                        <td class="headerC"><bean:message key="editReview.EditAggregation.CommentNumber" /></td>
                                        <td class="header" width="100%"><bean:message key="editReview.EditAggregation.Response" /></td>
                                        <td class="header"><bean:message key="editReview.EditAggregation.Type" /></td>
                                        <td class="headerC"><bean:message key="FinalReviewItemStatus.Fixed" /></td>
                                        <td class="headerC"><bean:message key="FinalReviewItemStatus.NotFixed" /></td>
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
                                                    <tr class="dark">
                                                        <c:if test="${firstTime == true}">
                                                            <td class="value" rowspan="${lastCommentIdx}">
                                                                <c:forEach items="${reviewResources}" var="resource">
                                                                    <c:if test="${resource.id == comment.author}">
                                                                        <tc-webtag:handle coderId='${resource.allProperties["External Reference ID"]}' context="${orfn:getHandlerContext(pageContext.request)}" /><br />
                                                                    </c:if>
                                                                </c:forEach>
                                                                <c:forEach items="${reviews}" var="subReview">
                                                                    <c:if test="${subReview.author == comment.author}">
                                                                        <html:link page="/actions/ViewReview.do?method=viewReview&rid=${subReview.id}"><bean:message key="editReview.EditAggregation.ViewReview" /></html:link>
                                                                    </c:if>
                                                                </c:forEach>
                                                            </td>
                                                            <c:set var="firstTime" value="${false}" />
                                                        </c:if>
                                                        <c:if test="${isReviewerComment == true}">
                                                            <td class="${rowClass}C">${commentNum}</td>
                                                            <c:set var="commentNum" value="${commentNum + 1}" />
                                                        </c:if>
                                                        <c:if test="${isReviewerComment != true}">
                                                            <td class="${rowClass}"><!-- @ --></td>
                                                        </c:if>
                                                        <td class="${rowClass}" width="100%">
                                                            <c:choose>
                                                                <c:when test="${isReviewerComment == true}">
                                                                    <b><bean:message key="editReview.EditAggregation.ReviewerResponse" /></b>
                                                                </c:when>
                                                                <c:when test='${(commentType == "Manager Comment") ||
                                                                        (commentType == "Appeal") || (commentType == "Appeal Response") ||
                                                                        (commentType == "Aggregation Comment") || (commentType == "Submitter Comment") ||
                                                                        (commentType == "Final Review Comment")}'>
                                                                    <b><bean:message key='editReview.EditAggregation.${fn:replace(commentType, " ", "")}' /></b>
                                                                </c:when>
                                                                <c:when test='${commentType == "Aggregation Review Comment"}'>
                                                                    <c:forEach items="${reviewResources}" var="resource">
                                                                        <c:if test="${resource.id == comment.author}">
                                                                            <b><bean:message key='ResourceRole.${fn:replace(resource.resourceRole.name, " ", "")}' />
                                                                            (<tc-webtag:handle coderId='${resource.allProperties["External Reference ID"]}' context="${orfn:getHandlerContext(pageContext.request)}" />)
                                                                            <bean:message key="viewAggregationReview.Response" /></b>
                                                                        </c:if>
                                                                    </c:forEach>
                                                                </c:when>
                                                            </c:choose>
                                                            ${orfn:htmlEncode(comment.comment)}
                                                        </td>
                                                        <c:if test="${isReviewerComment == true}">
                                                            <td class="${rowClass}">
                                                                <bean:message key='CommentType.${fn:replace(commentType, " ", "")}' /></td>
                                                            <td class="${rowClass}C">
                                                                <input type="radio" disabled="disabled" ${(comment.extraInfo == "Fixed") ? "checked='checked'" : ""} /></td>
                                                            <td class="${rowClass}C">
                                                                <input type="radio" disabled="disabled" ${(comment.extraInfo != "Fixed") ? "checked='checked'" : ""} /></td>
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
                    </table><br />

                    <c:forEach items="${review.allComments}" var="comment">
                        <c:if test='${(empty finalComment) && (comment.commentType.name == "Final Review Comment")}'>
                            <c:set var="finalComment" value="${comment}" />
                        </c:if>
                    </c:forEach>

                    <table class="scorecard" width="100%" cellpadding="0" cellspacing="0" style="border-collapse:collapse;">
                        <tr>
                            <td class="title"><bean:message key="editFinalReview.box.Approval" /></td>
                        </tr>
                        <tr class="highlighted">
                            <c:choose>
                                <c:when test='${!(empty finalComment) && (finalComment.extraInfo == "Approved")}'>
                                    <td class="value"><bean:message key="FinalReviewApprovalStatus.Approved" /></td>
                                </c:when>
                                <c:otherwise>
                                    <td class="value"><bean:message key="FinalReviewApprovalStatus.Rejected" /></td>
                                </c:otherwise>
                            </c:choose>
                        </tr>
                        <tr>
                            <td class="lastRowTD"><!-- @ --></td>
                        </tr>
                    </table><br />

                    <div align="right">
                        <a href="javascript:history.go(-1)"><html:img srcKey="btnBack.img" altKey="btnBack.alt" border="0" /></a><br />
                    </div><br />

                </div>
            </div>
        
        <jsp:include page="/includes/inc_footer.jsp" />

    </div>

</div>

</body>
</html:html>
