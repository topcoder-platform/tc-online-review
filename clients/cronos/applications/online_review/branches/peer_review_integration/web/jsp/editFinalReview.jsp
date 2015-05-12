<%--
  - Author: TCSASSEMBLER
  - Version: 2.0
  - Copyright (C)  - 2014 TopCoder Inc., All Rights Reserved.
  -
  - Description: This page renders the editing final review scorecard.
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
        <jsp:param name="thirdLevelPageKey" value="editFinalReview.title" />
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
    var approveCheckBox = document.getElementById("approveFixes");
    var isRejected = (approveCheckBox.checked != true);

    if (isRejected) {
        return confirm("<or:text key='editFinalReview.BeforeReject' />");
    } else {
        <c:choose>
            <c:when test="${requestScope.projectHasSVNModuleSet}">
                return confirm("<or:text key='editFinalReview.SVNConfirm' />");
            </c:when>
            <c:otherwise>
                return true;
            </c:otherwise>
        </c:choose>
    }
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
                    <jsp:include page="/includes/review/review_table_title.jsp" />

                    <s:form action="SaveFinalReview" namespace="/actions">
                        <input type="hidden" name="rid" value="${review.id}" />

                        <c:set var="itemIdx" value="0" />
                        <c:set var="globalStatusIdx" value="0" />
                        <c:set var="globalCommentIdx" value="0" />

                        <c:forEach items="${scorecardTemplate.allGroups}" var="group" varStatus="groupStatus">
                            <table class="scorecard" width="100%" cellpadding="0" cellspacing="0" border="0" style="border-collapse:collapse;">
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
                                            <c:if test="${not approvalBased}">
                                                <td class="header"><or:text key="editReview.EditAggregation.Reviewer" /></td>
                                            </c:if>
                                            <td class="headerC"><or:text key="editReview.EditAggregation.CommentNumber" /></td>
                                            <td class="header"><or:text key="editReview.EditAggregation.Response" /></td>
                                            <td class="header"><or:text key="editReview.EditAggregation.Type" /></td>
                                            <td class="headerC"><or:text key="FinalReviewItemStatus.Fixed" /></td>
                                            <td class="headerC"><or:text key="FinalReviewItemStatus.NotFixed" /></td>
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
                                                            (commentType == "Submitter Comment")}'>
                                                        <c:set var="isLastCommentForItem" value="${commentStatus.index == lastCommentIdx - 1}" />
                                                        <c:set var="rowClass" value='${(isLastCommentForItem == true) ? "value" : "valueNotLast"}'/>
                                                        <tr class="dark">
                                                            <c:if test="${firstTime == true and not approvalBased}">
                                                                <td class="value" rowspan="${lastCommentIdx}">
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
                                                                    <c:if test="${!(empty item.document)}">
                                                                        <br /><a href="<or:url value='/actions/DownloadDocument?uid=${item.document}' />"><or:text key="editReview.Document.Download" /></a>
                                                                    </c:if>
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
                                                            <td class="${rowClass}" width="50%">
                                                                <c:choose>
                                                                    <c:when test="${isReviewerComment == true}">
                                                                        <b><or:text key="editReview.EditAggregation.ReviewerResponse" /></b>
                                                                    </c:when>
                                                                    <c:when test='${(commentType == "Manager Comment") ||
                                                                            (commentType == "Appeal") || (commentType == "Appeal Response") ||
                                                                            (commentType == "Aggregation Comment") || (commentType == "Submitter Comment")}'>
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
                                                                ${orfn:htmlEncode(comment.comment)}
                                                                <c:if test="${isLastCommentForItem == true}">
                                                                    <div style="padding-top:4px;">
                                                                        <b><or:text key="editReview.EditAggregation.ResponseText" /></b> &#160;
                                                                        <span class="error"><s:fielderror escape="false"><s:param>final_comment[${globalCommentIdx}]</s:param></s:fielderror></span><br />
                                                                        <textarea rows="2" name="final_comment[${globalCommentIdx}]" cols="20" class="inputTextBox" ><or:fieldvalue field="final_comment[${globalCommentIdx}]" /></textarea>
                                                                        <c:set var="globalCommentIdx" value="${globalCommentIdx + 1}" />
                                                                    </div>
                                                                </c:if>
                                                            </td>
                                                            <c:if test="${isReviewerComment == true}">
                                                                <td class="${rowClass}">
                                                                    <or:text key='CommentType.${fn:replace(commentType, " ", "")}' />
                                                                    <div class="error" align="right"><s:fielderror escape="false"><s:param>fix_status[${globalStatusIdx}]</s:param></s:fielderror></div>
                                                                </td>
                                                                <td class="${rowClass}C" valign="top">
                                                                    <input type="radio" name="fix_status[${globalStatusIdx}]" value="Fixed"  <or:checked name='fix_status[${globalStatusIdx}]' value='Fixed' />/></td>
                                                                <td class="${rowClass}C" valign="top">
                                                                    <input type="radio" name="fix_status[${globalStatusIdx}]" value="Not Fixed"  <or:checked name='fix_status[${globalStatusIdx}]' value='Not Fixed' />/></td>
                                                                    <c:set var="globalStatusIdx" value="${globalStatusIdx + 1}" />
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
                                <tr>
                                    <td class="lastRowTD" colspan="6"><!-- @ --></td>
                                </tr>
                            </table><br />
                        </c:forEach>

                        <table class="scorecard" cellpadding="0" cellspacing="0" width="100%" style="border-collapse:collapse;">
                            <tr>
                                <td class="title"><label for="approveFixes"><or:text key="editFinalReview.box.Approval" /></label></td>
                            </tr>
                            <tr class="highlighted">
                                <td class="value">
                                    <input type="checkbox" id="approveFixes" name="approve_fixes"  <or:checked name='approve_fixes' value='on|yes|true' /> />
                                    <b><or:text key="editFinalReview.ApproveFinalFixes" /></b></td>
                            </tr>
                            <tr>
                                <td class="lastRowTD"><!-- @ --></td>
                            </tr>
                        </table><br />

                        <div align="right">
                            <input type="hidden" name="save" value=""/>
                            <input type="image"  onclick="javascript:this.form.save.value='submit'; this.parentNode.parentNode.target='_self'; return OnCompleteScorecardClick();" src="<or:text key='editReview.Button.SaveAndCommit.img' />" alt="<or:text key='editReview.Button.SaveAndCommit.alt' />" border="0"/>&#160;
                            <input type="image"  onclick="javascript:this.form.save.value='save'; this.parentNode.parentNode.target='_self';" src="<or:text key='editReview.Button.SaveForLater.img' />" alt="<or:text key='editReview.Button.SaveForLater.alt' />" border="0"/>&#160;
                            <input type="image"  onclick="javascript:this.form.save.value='preview'; this.parentNode.parentNode.target='_blank';" src="<or:text key='editReview.Button.Preview.img' />" alt="<or:text key='editReview.Button.Preview.alt' />" border="0"/>
                        </div>
                    </s:form>

                </div>
            </div>
        
        <jsp:include page="/includes/inc_footer.jsp" />

    </div>

</div>

</body>
</html>
