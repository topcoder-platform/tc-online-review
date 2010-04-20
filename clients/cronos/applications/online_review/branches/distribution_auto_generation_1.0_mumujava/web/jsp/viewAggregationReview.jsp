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
        <jsp:param name="thirdLevelPageKey" value="viewAggregationReview.title" />
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
                    <h3><bean:message key="viewAggregation.AggregationWorksheet" /></h3>

                    <c:set var="itemIdx" value="0" />
                    <c:set var="respIdx" value="0" />

                    <table class="scorecard" width="100%" cellpadding="0" cellspacing="0" border="0" style="border-collapse:collapse;">
                        <c:forEach items="${scorecardTemplate.allGroups}" var="group" varStatus="groupStatus">
                            <tr>
                                <td class="title" colspan="5">${orfn:htmlEncode(group.name)}</td>
                            </tr>
                            <c:forEach items="${group.allSections}" var="section" varStatus="sectionStatus">
                                <tr>
                                    <td class="subheader" width="100%" colspan="5">${orfn:htmlEncode(section.name)}</td>
                                </tr>
                                <c:forEach items="${section.allQuestions}" var="question" varStatus="questionStatus">
                                    <tr class="light">
                                        <td class="value" width="100%" colspan="5">
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
                                        <td class="header"><bean:message key="editReview.EditAggregation.Response" /></td>
                                        <td class="header"><bean:message key="editReview.EditAggregation.Type" /></td>
                                        <td class="header"><bean:message key="editReview.EditAggregation.Status" /></td>
                                    </tr>

                                    <c:forEach items="${review.allItems}" var="item" varStatus="itemStatus">
                                        <c:if test="${item.question == question.id}">
                                            <c:set var="commentNum" value="1" />
                                            <c:set var="firstTime" value="${true}" />
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
                                                                        <html:link page="/actions/ViewReview.do?method=viewReview&rid=${subReview.id}"><bean:message key="editReview.EditAggregation.ViewReview" /></html:link>
                                                                    </c:if>
                                                                </c:forEach>
                                                                <c:set var="firstTime" value="${false}" />
                                                            </c:if>
                                                        </td>
                                                        <c:if test="${isReviewerComment == true}">
                                                            <td class="valueC">${commentNum}</td>
                                                            <c:set var="commentNum" value="${commentNum + 1}" />
                                                        </c:if>
                                                        <c:if test="${isReviewerComment != true}">
                                                            <td class="valueC"><!-- @ --></td>
                                                        </c:if>
                                                        <td class="value">
                                                            <c:choose>
                                                                <c:when test='${isReviewerComment == true}'>
                                                                    <b><bean:message key="editReview.EditAggregation.ReviewerResponse" /></b>
                                                                    ${orfn:htmlEncode(comment.comment)}
                                                                </c:when>
                                                                <c:when test='${(commentType == "Manager Comment") ||
                                                                        (commentType == "Appeal") || (commentType == "Appeal Response") ||
                                                                        (commentType == "Aggregation Comment")}'>
                                                                    <b><bean:message key='editReview.EditAggregation.${fn:replace(commentType, " ", "")}' /></b>
                                                                    ${orfn:htmlEncode(comment.comment)}
                                                                </c:when>
                                                                <c:when test='${commentType == "Aggregation Review Comment"}'>
                                                                    <c:forEach items="${reviewResources}" var="resource">
                                                                        <c:if test="${resource.id == comment.author}">
                                                                            <div class="showText" id="shortR_${respIdx}">
                                                                                <c:if test="${not empty comment.comment}">
                                                                                    <a href="javascript:void(0)" onclick="javascript:toggleDisplay('shortR_${respIdx}');toggleDisplay('longR_${respIdx}');return false;" class="statLink"><html:img src="/i/or/plus.gif" altKey="global.plus.alt" border="0" /></a>
                                                                                </c:if>
                                                                                <b><bean:message key='ResourceRole.${fn:replace(resource.resourceRole.name, " ", "")}' />
                                                                                (<tc-webtag:handle coderId='${resource.allProperties["External Reference ID"]}' context="${orfn:getHandlerContext(pageContext.request)}" />)
                                                                                <bean:message key="viewAggregationReview.Response" /></b>
                                                                                <bean:message key='AggregationItemStatus.${fn:replace(comment.extraInfo, " ", "")}' />
                                                                            </div>
                                                                            <c:if test="${not empty comment.comment}">
                                                                                <div class="hideText" id="longR_${respIdx}">
                                                                                    <a href="javascript:void(0)" onclick="javascript:toggleDisplay('shortR_${respIdx}');toggleDisplay('longR_${respIdx}');return false;" class="statLink"><html:img src="/i/or/minus.gif" altKey="global.minus.alt" border="0" /></a>
                                                                                    <b><bean:message key='ResourceRole.${fn:replace(resource.resourceRole.name, " ", "")}' />
                                                                                    (<tc-webtag:handle coderId='${resource.allProperties["External Reference ID"]}' context="${orfn:getHandlerContext(pageContext.request)}" />)
                                                                                    <bean:message key="viewAggregationReview.Response" /></b>
                                                                                    <bean:message key='AggregationItemStatus.${fn:replace(comment.extraInfo, " ", "")}' /><br />
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
                                                                            <a href="javascript:void(0)" onclick="javascript:toggleDisplay('shortR_${respIdx}');toggleDisplay('longR_${respIdx}');return false;" class="statLink"><html:img src="/i/or/plus.gif" altKey="global.plus.alt" border="0" /></a>
                                                                        </c:if>
                                                                        <b><bean:message key='ResourceRole.${fn:replace(submitterResource.resourceRole.name, " ", "")}' />
                                                                        <bean:message key="viewAggregationReview.Comment" /></b>
                                                                    </div>
                                                                    <c:if test="${not empty comment.comment}">
                                                                        <div class="hideText" id="longR_${respIdx}">
                                                                            <a href="javascript:void(0)" onclick="javascript:toggleDisplay('shortR_${respIdx}');toggleDisplay('longR_${respIdx}');return false;" class="statLink"><html:img src="/i/or/minus.gif" altKey="global.minus.alt" border="0" /></a>
                                                                            <b><bean:message key='ResourceRole.${fn:replace(submitterResource.resourceRole.name, " ", "")}' />
                                                                            <bean:message key="viewAggregationReview.Comment" />:</b>
                                                                            ${orfn:htmlEncode(comment.comment)}
                                                                        </div>
                                                                    </c:if>
                                                                    <c:set var="respIdx" value="${respIdx + 1}" />
                                                                </c:when>
                                                            </c:choose>
                                                        </td>
                                                        <c:if test="${isReviewerComment == true}">
                                                            <td class="value" ><bean:message key="CommentType.${commentType}" /></td>
                                                            <td class="value" ><bean:message key="AggregationItemStatus.${comment.extraInfo}" /></td>
                                                        </c:if>
                                                        <c:if test="${isReviewerComment != true}">
                                                            <td class="value" ><!-- @ --></td>
                                                            <td class="value" ><!-- @ --></td>
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
                            <td class="lastRowTD" colspan="5"><!-- @ --></td>
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
