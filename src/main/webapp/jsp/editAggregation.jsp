<%--
  - Author: TCSASSEMBLER
  - Version: 2.0
  - Copyright (C)  - 2014 TopCoder Inc., All Rights Reserved.
  -
  - Description: This page renders the editing aggregation scorecard.
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
        <jsp:param name="thirdLevelPageKey" value="editAggregation.title" />
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

                    <s:form action="SaveAggregation" namespace="/actions">
                        <input type="hidden" name="rid" value="${review.id}" />

                        <c:if test="${orfn:isErrorsPresent(pageContext.request)}">
                            <table cellpadding="0" cellspacing="0" border="0">
                                <tr><td class="errorText"><or:text key="Error.saveReview.ValidationFailed" /></td></tr>
                            </table><br />
                        </c:if>

                        <c:set var="itemIdx" value="0" />
                        <c:set var="respIdx" value="0" />
                        <c:set var="globalCommentIdx" value="0" />
                        <c:set var="globalResponseIdx" value="0" />

                        <c:forEach items="${scorecardTemplate.allGroups}" var="group" varStatus="groupStatus">
                            <table cellpadding="0" border="0" width="100%" class="scorecard" style="border-collapse:collapse;">
                                <tr>
                                    <td class="title" colspan="7">${orfn:htmlEncode(group.name)}</td>
                                </tr>
                                <c:forEach items="${group.allSections}" var="section" varStatus="sectionStatus">
                                    <tr>
                                        <td class="subheader" width="100%" colspan="7">${orfn:htmlEncode(section.name)}</td>
                                    </tr>
                                    <c:forEach items="${section.allQuestions}" var="question" varStatus="questionStatus">
                                        <tr class="light">
                                            <td class="value" colspan="7">
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
                                            <td class="header"><or:text key="AggregationItemStatus.Rejected" /></td>
                                            <td class="header"><or:text key="AggregationItemStatus.Accepted" /></td>
                                            <td class="header"><or:text key="AggregationItemStatus.Duplicate" /></td>
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
                                                    <c:if test='${(isReviewerComment == true) || (commentType == "Manager Comment") ||
                                                        (commentType == "Appeal") || (commentType == "Appeal Response") ||
                                                        (commentType == "Aggregation Review Comment") || (commentType == "Submitter Comment")}'>
                                                        <tr class="dark">
                                                            <td class="value">
                                                                <c:if test="${firstTime == true}">
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
                                                                        <a href="<or:url value='/actions/DownloadDocument?uid=${item.document}' />"><or:text key="editReview.Document.Download" /></a>
                                                                    </c:if>
                                                                    <c:set var="firstTime" value="${false}" />
                                                                </c:if>
                                                            </td>
                                                            <c:if test="${isReviewerComment == true}">
                                                                <td class="valueC">${commentNum}</td>
                                                                <c:set var="commentNum" value="${commentNum + 1}" />
                                                            </c:if>
                                                            <c:if test="${isReviewerComment != true}">
                                                                <td class="value"><!-- @ --></td>
                                                            </c:if>
                                                            <td class="value" width="85%">
                                                                <c:choose>
                                                                    <c:when test="${isReviewerComment == true}">
                                                                        <b><or:text key="editReview.EditAggregation.ReviewerResponse" /></b>
                                                                        ${orfn:htmlEncode(comment.comment)}
                                                                    </c:when>
                                                                    <c:when test='${(commentType == "Manager Comment") ||
                                                                        (commentType == "Appeal") || (commentType == "Appeal Response")}'>
                                                                        <b><or:text key='editReview.EditAggregation.${fn:replace(commentType, " ", "")}' /></b>
                                                                        ${orfn:htmlEncode(comment.comment)}
                                                                    </c:when>
                                                                    <c:when test='${commentType == "Aggregation Review Comment"}'>
                                                                        <c:forEach items="${reviewResources}" var="resource">
                                                                            <c:if test="${resource.id == comment.author}">
                                                                                <div class="showText" id="shortR_${respIdx}">
                                                                                    <c:if test="${not empty comment.comment}">
                                                                                        <a href="javascript:void(0)" onclick="javascript:toggleDisplay('shortR_${respIdx}');toggleDisplay('longR_${respIdx}');return false;" class="statLink"><img src="/i/or/plus.gif" alt="<or:text key='global.plus.alt' />" border="0" /></a>
                                                                                    </c:if>
                                                                                    <b><or:text key='ResourceRole.${fn:replace(resource.resourceRole.name, " ", "")}' />
                                                                                    (<tc-webtag:handle coderId='${resource.allProperties["External Reference ID"]}' context="${orfn:getHandlerContext(pageContext.request)}" />)
                                                                                    <or:text key="viewAggregationReview.Response" /></b>
                                                                                    <or:text key='AggregationItemStatus.${fn:replace(comment.extraInfo, " ", "")}' />
                                                                                </div>
                                                                                <c:if test="${not empty comment.comment}">
                                                                                    <div class="hideText" id="longR_${respIdx}">
                                                                                        <a href="javascript:void(0)" onclick="javascript:toggleDisplay('shortR_${respIdx}');toggleDisplay('longR_${respIdx}');return false;" class="statLink"><img src="/i/or/minus.gif" alt="<or:text key='global.minus.alt' />" border="0" /></a>
                                                                                        <b><or:text key='ResourceRole.${fn:replace(resource.resourceRole.name, " ", "")}' />
                                                                                        (<tc-webtag:handle coderId='${resource.allProperties["External Reference ID"]}' context="${orfn:getHandlerContext(pageContext.request)}" />)
                                                                                        <or:text key="viewAggregationReview.Response" /></b>
                                                                                        <or:text key='AggregationItemStatus.${fn:replace(comment.extraInfo, " ", "")}' /><br />
                                                                                        ${orfn:htmlEncode(comment.comment)}
                                                                                    </div>
                                                                                </c:if>
                                                                                <c:set var="respIdx" value="${respIdx + 1}" />
                                                                            </c:if>
                                                                        </c:forEach>
                                                                    </c:when>
                                                                    <c:when test='${(submitterResource.id == comment.author) && (commentType == "Submitter Comment")}'>
                                                                        <div class="showText" id="shortR_${respIdx}">
                                                                            <c:if test="${not empty comment.comment}">
                                                                                <a href="javascript:void(0)" onclick="javascript:toggleDisplay('shortR_${respIdx}');toggleDisplay('longR_${respIdx}');return false;" class="statLink"><img src="/i/or/plus.gif" alt="<or:text key='global.plus.alt' />" border="0" /></a>
                                                                            </c:if>
                                                                            <b><or:text key='ResourceRole.${fn:replace(submitterResource.resourceRole.name, " ", "")}' />
                                                                            <or:text key="viewAggregationReview.Response" /></b>
                                                                            <or:text key='AggregationItemStatus.${fn:replace(comment.extraInfo, " ", "")}' />
                                                                        </div>
                                                                        <c:if test="${not empty comment.comment}">
                                                                            <div class="hideText" id="longR_${respIdx}">
                                                                                <a href="javascript:void(0)" onclick="javascript:toggleDisplay('shortR_${respIdx}');toggleDisplay('longR_${respIdx}');return false;" class="statLink"><img src="/i/or/minus.gif" alt="<or:text key='global.minus.alt' />" border="0" /></a>
                                                                                <b><or:text key='ResourceRole.${fn:replace(submitterResource.resourceRole.name, " ", "")}' />
                                                                                <or:text key="viewAggregationReview.Response" /></b>
                                                                                <or:text key='AggregationItemStatus.${fn:replace(comment.extraInfo, " ", "")}' /><br />
                                                                                ${orfn:htmlEncode(comment.comment)}
                                                                            </div>
                                                                        </c:if>
                                                                        <c:set var="respIdx" value="${respIdx + 1}" />
                                                                    </c:when>
                                                                </c:choose>
                                                                <c:if test="${commentStatus.index == lastCommentIdxs[itemStatus.index]}">
                                                                    <div style="padding-top:4px;">
                                                                        <b><or:text key="editReview.EditAggregation.ResponseText" /></b> &#160;
                                                                        <span class="error"><s:fielderror escape="false"><s:param>aggregator_response[${globalResponseIdx}]</s:param></s:fielderror></span><br />
                                                                        <textarea rows="2" name="aggregator_response[${globalResponseIdx}]" cols="20" class="inputTextBox" ><or:fieldvalue field="aggregator_response[${globalResponseIdx}]" /></textarea>
                                                                    </div>
                                                                    <c:set var="globalResponseIdx" value="${globalResponseIdx + 1}" />
                                                                </c:if>
                                                            </td>

                                                            <c:if test="${isReviewerComment == true}">
                                                                <td class="value">
                                                                    <select size="1" name="aggregator_response_type[${globalCommentIdx}]" class="inputBox"><c:set var="OR_FIELD_TO_SELECT" value="aggregator_response_type[${globalCommentIdx}]"/>
                                                                        <c:forEach items="${allCommentTypes}" var="commentType2">
                                                                            <option value="${commentType2.id}" <or:selected value="${commentType2.id}"/>>${commentType2.name}</option>
                                                                        </c:forEach>
                                                                    </select>
                                                                    <div class="error" align="right"><s:fielderror escape="false"><s:param>aggregate_function[${globalCommentIdx}]</s:param></s:fielderror></div>
                                                                </td>
                                                                <td class="valueC"><input type="radio" value="Reject" name="aggregate_function[${globalCommentIdx}]"  <or:checked name='aggregate_function[${globalCommentIdx}]' value='Reject' />/></td>
                                                                <td class="valueC"><input type="radio" value="Accept" name="aggregate_function[${globalCommentIdx}]"  <or:checked name='aggregate_function[${globalCommentIdx}]' value='Accept' />/></td>
                                                                <td class="valueC"><input type="radio" value="Duplicate" name="aggregate_function[${globalCommentIdx}]"  <or:checked name='aggregate_function[${globalCommentIdx}]' value='Duplicate' />/></td>
                                                                <c:set var="globalCommentIdx" value="${globalCommentIdx + 1}" />
                                                            </c:if>
                                                            <c:if test="${isReviewerComment != true}">
                                                                <td class="value"><!-- @ --></td>
                                                                <td class="value"><!-- @ --></td>
                                                                <td class="value"><!-- @ --></td>
                                                                <td class="value"><!-- @ --></td>
                                                            </c:if>
                                                        </tr>
                                                    </c:if>
                                                </c:forEach>
                                            </c:if>
                                        </c:forEach>
                                    </c:forEach>
                                </c:forEach>
                                <tr>
                                    <td class="lastRowTD" colspan="7"><!-- @ --></td>
                                </tr>
                            </table><br />
                        </c:forEach>

                        <div align="right">
                            <input type="hidden" name="save" value="" />
                            <input type="image"  onclick="javascript:this.form.save.value='submit'; this.parentNode.parentNode.target='_self';" src="<or:text key='editReview.Button.SaveAndCommit.img' />" alt="<or:text key='editReview.Button.SaveAndCommit.alt' />" border="0" />&#160;
                            <input type="image"  onclick="javascript:this.form.save.value='save'; this.parentNode.parentNode.target='_self';" src="<or:text key='editReview.Button.SaveForLater.img' />" alt="<or:text key='editReview.Button.SaveForLater.alt' />" border="0" />&#160;
                            <input type="image"  onclick="javascript:this.form.save.value='preview'; this.parentNode.parentNode.target='_blank';" src="<or:text key='editReview.Button.Preview.img' />" alt="<or:text key='editReview.Button.Preview.alt' />" border="0" />
                        </div>
                    </s:form>


                </div>
            </div>
        
        <jsp:include page="/includes/inc_footer.jsp" />

    </div>

</div>

</body>
</html>
