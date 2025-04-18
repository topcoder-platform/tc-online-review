<%--
  - Author: TCSASSEMBLER
  - Version: 2.0
  - Copyright (C) 2005 - 2014 TopCoder Inc., All Rights Reserved.
  -
  - Description: This page renders the composite scorecard.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="or" uri="/or-tags" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>
<%@ taglib prefix="tc-webtag" uri="/tags/tc-webtags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "">
<c:set var="toPDF" value="${param.pdf eq 'true'}"/>
<% try { %>
<html>
<head>
    <jsp:include page="/includes/project/project_title.jsp">
        <jsp:param name="thirdLevelPageKey" value="viewCompositeScorecard.title" />
    </jsp:include>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

    <!-- TopCoder CSS -->
    <link type="text/css" rel="stylesheet" href="/css/style.css" />
    <link type="text/css" rel="stylesheet" href="/css/coders.css" />
    <link type="text/css" rel="stylesheet" href="/css/stats.css" />
    <link type="text/css" rel="stylesheet" href="/css/tcStyles.css" />
    <c:if test="${toPDF}">
        <link type="text/css" rel="stylesheet" href="/css/compositeReviewPDF.css" media="print"/>
    </c:if>
    <!-- Reskin -->
    <link type="text/css" rel="stylesheet" href="/css/reskin-or/reskin.css">
    <link type="text/css" rel="stylesheet" href="/css/reskin-or/toasts.css">
    
    
    <!-- CSS and JS by Petar ${toPDF} -->
    <link type="text/css" rel="stylesheet" href="/css/or/new_styles.css" />
    <script language="JavaScript" type="text/javascript"
        src="/js/or/rollovers2.js"><!-- @ --></script>

    </script>
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
            <c:set var="colSpan" value="${3 + fn:length(reviews)}" />

            <table class="scorecard" width="100%" cellpadding="0" style="border-collapse:collapse;">
                <c:forEach items="${scorecardTemplate.allGroups}" var="group" varStatus="groupStatus">
                    <tr>
                        <td class="title" colspan="${colSpan}">${orfn:htmlEncode(group.name)}</td>
                    </tr>
                    <c:forEach items="${group.allSections}" var="section" varStatus="sectionStatus">
                        <tr>
                            <td class="subheader pdf_section" width="100%">${orfn:htmlEncode(section.name)}</td>
                            <td class="subheader pdf_weight" align="center" width="49%"><or:text key="editReview.SectionHeader.Weight" /></td>
                            <td class="subheader pdf_average" align="center"><or:text key="editReview.SectionHeader.Average" /></td>
                            <c:forEach items="${reviews}" var="review" varStatus="reviewStatus">
                                <td class="subheader pdf_review" align="center" nowrap="nowrap">
                                    <a href="ViewReview?rid=${review.id}"><or:text key="editReview.SectionHeader.Review" /></a><br />
                                    (<tc-webtag:handle coderId="${authors[reviewStatus.index]}" context="${orfn:getHandlerContext(pageContext.request)}" />)
                                </td>
                            </c:forEach>
                        </tr>
                        <c:forEach items="${section.allQuestions}" var="question" varStatus="questionStatus">
                            <c:set var="item" value="${review.allItems[itemIdx]}" />
                            <tr class="light">
                                <%@ include file="../includes/review/review_question.jsp" %>
                                
                                <td class="valueC pdf_average" nowrap="nowrap">${orfn:displayScore(pageScope.request, avgScores[itemIdx])}</td>
                                <c:forEach items="${reviews}" var="review" varStatus="reviewStatus">
                                    <td class="valueC pdf_review" nowrap="nowrap">${orfn:displayScore(pageContext.request, scores[reviewStatus.index][itemIdx])}</td>
                                </c:forEach>
                            </tr>
                            <tr class="dark">
                                <td class="value" align="left">
                                    <div class="${toPDF ? 'hideText' : 'showText'}" id="shortQ_resp_${itemIdx}" style="width:100%;">
                                        <a href="javascript:toggleDisplay('shortQ_resp_${itemIdx}');toggleDisplay('longQ_resp_${itemIdx}');" class="statLink"><img src="/i/or/plus.gif" alt="open" border="0" /></a>
                                        <b><or:text key="editReview.Question.Response.plural" /></b>
                                    </div>
                                    <div class="${toPDF ? 'showText' : 'hideText'}" id="longQ_resp_${itemIdx}">
                                        <a href="javascript:toggleDisplay('shortQ_resp_${itemIdx}');toggleDisplay('longQ_resp_${itemIdx}');" class="statLink"><img src="/i/or/minus.gif" alt="close" border="0" /></a>
                                        <c:forEach items="${reviews}" var="review" varStatus="reviewStatus">
                                          <c:set var="item" value="${review.allItems[itemIdx]}" />
                                          <c:set var="commentNum" value="1" />
                                            <c:forEach items="${item.allComments}" var="comment" varStatus="commentStatus">
                                                <c:set var="commentType" value="${comment.commentType.name}" />
                                                <c:choose>
                                                    <c:when test='${(commentType == "Comment") || (commentType == "Required") || (commentType == "Recommended")}'>
                                                        <b><or:text key="editReview.Question.Response.title" />
                                                            ${commentNum}:
                                                            <or:text key='CommentType.${fn:replace(comment.commentType.name, " ", "")}' /></b>
                                                        (<tc-webtag:handle coderId="${authors[reviewStatus.index]}" context="${orfn:getHandlerContext(pageContext.request)}" />)<br />
                                                    </c:when>
                                                    <c:when test='${(commentType == "Appeal") || (commentType == "Appeal Response") || (commentType == "Manager Comment")}'>
                                                        <b><or:text key='editReview.EditAggregation.${fn:replace(commentType, " ", "")}'/></b>
                                                    </c:when>
                                                </c:choose>
                                                ${orfn:htmlEncode(comment.comment)}<br />
                                                <c:if test="${(not empty item.document) && (commentStatus.index == (item.numberOfComments - 1))}">
                                                    <a href="<or:url value='/actions/DownloadDocument?uid=${item.document}' />"><or:text key="editReview.Document.Download" /></a><br />
                                                </c:if>
                                            </c:forEach>
                                        </c:forEach>
                                    </div>
                                </td>
                                <td class="value" colspan="${colSpan - 1}"><!-- @ --></td>
                            </tr>

                            <c:set var="itemIdx" value="${itemIdx + 1}" />
                        </c:forEach>
                    </c:forEach>
                    <c:if test="${groupStatus.index eq (fn:length(scorecardTemplate.allGroups) - 1)}">
                        <tr>
                            <td class="subheader" colspan="2"><!-- @ --></td>
                            <td class="subheader pdf_average" align="center"><or:text key="editReview.SectionHeader.Average" /></td>
                            <c:forEach items="${reviews}" var="review" varStatus="reviewStatus">
                                <td class="subheader pdf_review" align="center" nowrap="nowrap">
                                    <a href="ViewReview?rid=${review.id}"><or:text key="editReview.SectionHeader.Review" /></a><br />
                                    (<tc-webtag:handle coderId="${authors[reviewStatus.index]}" context="${orfn:getHandlerContext(pageContext.request)}" />)
                                </td>
                            </c:forEach>
                        </tr>
                        <tr>
                            <td class="value" align="left" colspan="2"><!-- @ --></td>
                            <td class="valueC" nowrap="nowrap">${orfn:displayScore(pageContext.request, avgScore)}</td>
                            <c:forEach items="${reviews}" var="review">
                                <td class="valueC" nowrap="nowrap">${orfn:displayScore(pageContext.request, review.score)}</td>
                            </c:forEach>
                        </tr>
                        <tr>
                            <td class="lastRowTD" colspan="${colSpan}"><!-- @ --></td>
                        </tr>
                    </c:if>
                </c:forEach>
            </table><br />

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
    </div>
    <jsp:include page="/includes/inc_footer_reskin.jsp" />
</body>
</html>
<% } catch (Throwable e) {
    org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger("com.cronos.onlinereview");
    log.error(e, e);
    throw new RuntimeException(e);
}
%>

