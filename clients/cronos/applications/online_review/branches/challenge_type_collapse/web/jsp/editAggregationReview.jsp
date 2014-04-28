<%--
  - Author: TCSASSEMBLER
  - Version: 2.0
  - Copyright (C)  - 2014 TopCoder Inc., All Rights Reserved.
  -
  - Description: This page renders the editing aggregation review.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="or" uri="/or-tags" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>
<%@ taglib prefix="tc-webtag" uri="/tags/tc-webtags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<head>
    <jsp:include page="/includes/project/project_title.jsp">
        <jsp:param name="thirdLevelPageKey" value="editAggregationReview.title" />
    </jsp:include>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

    <!-- TopCoder CSS -->
    <link type="text/css" rel="stylesheet" href="/css/style.css" />
    <link type="text/css" rel="stylesheet" href="/css/coders.css" />
    <link type="text/css" rel="stylesheet" href="/css/stats.css" />
    <link type="text/css" rel="stylesheet" href="/css/tcStyles.css" />

    <!-- CSS and JS by Petar -->
    <link type="text/css" rel="stylesheet" href="/css/or/new_styles.css" />
    <script language="JavaScript" type="text/javascript"
        src="/js/or/rollovers2.js"><!-- @ --></script>

    <script language="JavaScript" type="text/javascript">
function OnCompleteScorecardClick() {
    var x = document.getElementsByTagName("input");
    var isRejected = false;

    for (var i = 0; i < x.length; ++i) {
        var element = x[i];
        if (element.type.toLowerCase() != "radio" || element.value.toLowerCase() != "reject") {
            continue;
        }
        if (element.checked) {
            isRejected = true;
            break;
        }
    }

    return (isRejected) ? confirm("<or:text key='editAggregationReview.BeforeReject' />") : true;
}
    </script>
</head>

<body>
<div align="center">
    
    <div class="maxWidthBody" align="left">

        <jsp:include page="/includes/inc_header.jsp" />
        
        <jsp:include page="/includes/project/project_tabs.jsp" />
        
            <div id="mainMiddleContent">
                <div style="position: relative; width: 100%;">

                    <jsp:include page="/includes/review/review_project.jsp">
                        <jsp:param name="showFillScorecardLink" value="false" />
                    </jsp:include>
                    <h3><or:text key="editAggregationReview.Scorecard.title" /></h3>

                    <s:form action="SaveAggregationReview" namespace="/actions">
                        <input type="hidden" name="rid" value="${review.id}" />

                        <c:set var="itemIdx" value="0" />
                        <c:set var="globalItemIndex" value="0" />

                        <c:forEach items="${scorecardTemplate.allGroups}" var="group" varStatus="groupStatus">
                            <table cellpadding="0" cellspacing="0" border="0" width="100%" class="scorecard" style="border-collapse:collapse">
                                <tr>
                                    <td class="title" colspan="5">${orfn:htmlEncode(group.name)}</td>
                                </tr>
                                <c:forEach items="${group.allSections}" var="section" varStatus="sectionStatus">
                                    <tr class="light">
                                        <td class="subheader" width="100%" colspan="5">${orfn:htmlEncode(section.name)}</td>
                                    </tr>
                                    <c:forEach items="${section.allQuestions}" var="question" varStatus="questionStatus">
                                        <tr class="light">
                                            <td class="value" width="100%" colspan="5">
                                                <div class="showText" id="shortQ_${itemIdx}">
                                                    <a href="javascript:toggleDisplay('shortQ_${itemIdx}');toggleDisplay('longQ_${itemIdx}');" class="statLink"><img src="/i/or/plus.gif" alt="<or:text key='global.plus.alt' />" border="0" /></a>
                                                    <b><or:text key="editReview.Question.title" /> ${groupStatus.index + 1}.${sectionStatus.index + 1}.${questionStatus.index + 1}</b>
                                                    ${orfn:htmlEncode(question.description)}
                                                </div>
                                                <div class="hideText" id="longQ_${itemIdx}">
                                                    <a href="javascript:toggleDisplay('shortQ_${itemIdx}');toggleDisplay('longQ_${itemIdx}');" class="statLink"><img src="/i/or/minus.gif" alt="<or:text key='global.minus.alt' />" border="0" /></a>
                                                    <b><or:text key="editReview.Question.title" /> ${groupStatus.index + 1}.${sectionStatus.index + 1}.${questionStatus.index + 1}</b>
                                                    ${orfn:htmlEncode(question.description)}<br />
                                                    ${orfn:htmlEncode(question.guideline)}
                                                </div>
                                            </td>
                                            <c:set var="itemIdx" value="${itemIdx + 1}" />
                                        </tr>
                                        <tr>
                                            <td class="header"><or:text key="editReview.EditAggregation.Reviewer" /></td>
                                            <td class="headerC"><or:text key="editReview.EditAggregation.CommentNumber" /></td>
                                            <td class="header"><or:text key="editReview.EditAggregation.Response" /></td>
                                            <td class="header"><or:text key="editReview.EditAggregation.Type" /></td>
                                            <td class="header"><or:text key="editReview.EditAggregation.Status" /></td>
                                        </tr>

                                        <c:forEach items="${review.allItems}" var="item" varStatus="itemStatus">
                                            <c:set var="commentNum" value="1" />
                                            <c:set var="firstTime" value="${true}" />
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
                                                    <c:if test='${isReviewerComment || (commentType == "Manager Comment") ||
                                                            (commentType == "Appeal") || (commentType == "Appeal Response") ||
                                                            (commentType == "Aggregation Comment") ||
                                                            ((empty isSubmitter) && (not empty submitterCommitted) && (commentType == "Submitter Comment"))}'>
                                                        <tr class="dark">
                                                            <td class="value">
                                                                <c:if test="${firstTime}">
                                                                    <c:forEach items="${reviewResources}" var="resource">
                                                                        <c:if test="${resource.id == comment.author}">
                                                                            <tc-webtag:handle coderId='${resource.allProperties["External Reference ID"]}' context="${orfn:getHandlerContext(pageContext.request)}" /><br />
                                                                        </c:if>
                                                                    </c:forEach>
                                                                    <c:forEach items="${reviews}" var="subReview">
                                                                        <c:if test="${subReview.author == comment.author}">
                                                                            <a href="<or:url value='/actions/ViewReview?rid=${subReview.id}' />"><or:text key="editReview.EditAggregation.ViewReview" /></a>
                                                                        </c:if>
                                                                    </c:forEach>
                                                                    <c:if test="${not empty item.document}">
                                                                        <br /><a href="<or:url value='/actions/DownloadDocument?uid=${item.document}' />"><or:text key="editReview.Document.Download" /></a>
                                                                    </c:if>
                                                                    <c:set var="firstTime" value="${false}" />
                                                                </c:if>
                                                            </td>
                                                            <c:if test="${isReviewerComment}">
                                                                <td class="valueC">${commentNum}</td>
                                                                <c:set var="commentNum" value="${commentNum + 1}" />
                                                            </c:if>
                                                            <c:if test="${not isReviewerComment}">
                                                                <td class="valueC"><!-- @ --></td>
                                                            </c:if>
                                                            <td class="value">
                                                                <c:choose>
                                                                    <c:when test="${isReviewerComment}">
                                                                        <b><or:text key="editReview.EditAggregation.ReviewerResponse" /></b>
                                                                    </c:when>
                                                                    <c:when test='${(commentType == "Manager Comment") ||
                                                                            (commentType == "Appeal") || (commentType == "Appeal Response") ||
                                                                            (commentType == "Aggregation Comment") || (commentType == "Submitter Comment")}'>
                                                                        <b><or:text key='editReview.EditAggregation.${fn:replace(commentType, " ", "")}' /></b>
                                                                    </c:when>
                                                                </c:choose>
                                                                ${orfn:htmlEncode(comment.comment)}
                                                            </td>
                                                            <c:if test="${isReviewerComment}">
                                                                <td class="value"><or:text key="CommentType.${commentType}" /></td>
                                                                <td class="value"><or:text key="AggregationItemStatus.${comment.extraInfo}" /></td>
                                                            </c:if>
                                                            <c:if test="${not isReviewerComment}">
                                                                <td class="value"><!-- @ --></td>
                                                                <td class="value"><!-- @ --></td>
                                                            </c:if>
                                                        </tr>
                                                    </c:if>
                                                </c:forEach>

                                                <tr class="highlighted">
                                                    <td class="value" colspan="5">
                                                        <c:choose>
                                                            <c:when test="${isSubmitter}">
                                                                <input type="hidden" name="review_function[${globalItemIndex}]" value="Accept" />
                                                                <or:text key="editAggregationReview.EnterComment" /><br />
                                                            </c:when>
                                                            <c:otherwise>
                                                                <input type="radio" id="functionAcceptCheckbox${globalItemIndex}" name="review_function[${globalItemIndex}]" value="Accept"  <or:checked name='review_function[${globalItemIndex}]' value='Accept' />/><label
                                                                    for="functionAcceptCheckbox${globalItemIndex}"><or:text key="editAggregationReview.Function.Accept" /></label>
                                                                <input type="radio" id="functionRejectCheckbox${globalItemIndex}" name="review_function[${globalItemIndex}]" value="Reject"  <or:checked name='review_function[${globalItemIndex}]' value='Reject' />/><label
                                                                    for="functionRejectCheckbox${globalItemIndex}"><or:text key="editAggregationReview.Function.Reject" /></label> &#160;
                                                                <span class="error"><s:fielderror escape="false"><s:param>reject_reason[${globalItemIndex}]</s:param></s:fielderror></span><br />
                                                            </c:otherwise>
                                                        </c:choose>
                                                        <textarea rows="3" name="reject_reason[${globalItemIndex}]" cols="20" class="inputTextBox" ><or:fieldvalue field="reject_reason[${globalItemIndex}]" /></textarea>
                                                    </td>
                                                </tr>
                                                <c:set var="globalItemIndex" value="${globalItemIndex + 1}" />
                                            </c:if>
                                        </c:forEach>
                                    </c:forEach>
                                </c:forEach>
                                <tr>
                                    <td class="lastRowTD" colspan="5"><!-- @ --></td>
                                </tr>
                            </table><br />
                        </c:forEach>

                        <div align="right">
                            <input type="hidden" name="save" value="" />
                            <input type="image"  onclick="javascript:this.form.save.value='submit';return OnCompleteScorecardClick();" src="<or:text key='editReview.Button.SaveAndCommit.img' />" alt="<or:text key='editReview.Button.SaveAndCommit.alt' />" border="0" />&#160;
                            <input type="image"  onclick="javascript:this.form.save.value='save';" src="<or:text key='editReview.Button.SaveForLater.img' />" alt="<or:text key='editReview.Button.SaveForLater.alt' />" border="0" />
                        </div>
                    </s:form>

                </div>
            </div>
        
        <jsp:include page="/includes/inc_footer.jsp" />

    </div>

</div>

</body>
</html>
