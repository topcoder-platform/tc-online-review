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
        <jsp:param name="thirdLevelPageKey" value="editAggregationReview.title" />
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

    return (isRejected) ? confirm("<bean:message key='editAggregationReview.BeforeReject' />") : true;
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

                    <jsp:include page="/includes/review/review_project.jsp" />
                    <h3><bean:message key="editAggregationReview.Scorecard.title" /></h3>

                    <html:form action="/actions/SaveAggregationReview">
                        <html:hidden property="method" value="saveAggregationReview" />
                        <html:hidden property="rid" value="${review.id}" />

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
                                                                            <html:link page="/actions/ViewReview.do?method=viewReview&rid=${subReview.id}"><bean:message key="editReview.EditAggregation.ViewReview" /></html:link>
                                                                        </c:if>
                                                                    </c:forEach>
                                                                    <c:if test="${not empty item.document}">
                                                                        <br /><html:link page="/actions/DownloadDocument.do?method=downloadDocument&uid=${item.document}"><bean:message key="editReview.Document.Download" /></html:link>
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
                                                                        <b><bean:message key="editReview.EditAggregation.ReviewerResponse" /></b>
                                                                    </c:when>
                                                                    <c:when test='${(commentType == "Manager Comment") ||
                                                                            (commentType == "Appeal") || (commentType == "Appeal Response") ||
                                                                            (commentType == "Aggregation Comment") || (commentType == "Submitter Comment")}'>
                                                                        <b><bean:message key='editReview.EditAggregation.${fn:replace(commentType, " ", "")}' /></b>
                                                                    </c:when>
                                                                </c:choose>
                                                                ${orfn:htmlEncode(comment.comment)}
                                                            </td>
                                                            <c:if test="${isReviewerComment}">
                                                                <td class="value"><bean:message key="CommentType.${commentType}" /></td>
                                                                <td class="value"><bean:message key="AggregationItemStatus.${comment.extraInfo}" /></td>
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
                                                                <bean:message key="editAggregationReview.EnterComment" /><br />
                                                            </c:when>
                                                            <c:otherwise>
                                                                <html:radio styleId="functionAcceptCheckbox${globalItemIndex}" property="review_function[${globalItemIndex}]" value="Accept" /><label
                                                                    for="functionAcceptCheckbox${globalItemIndex}"><bean:message key="editAggregationReview.Function.Accept" /></label>
                                                                <html:radio styleId="functionRejectCheckbox${globalItemIndex}" property="review_function[${globalItemIndex}]" value="Reject" /><label
                                                                    for="functionRejectCheckbox${globalItemIndex}"><bean:message key="editAggregationReview.Function.Reject" /></label> &#160;
                                                                <span class="error"><html:errors property="reject_reason[${globalItemIndex}]" prefix="" suffix="" /></span><br />
                                                            </c:otherwise>
                                                        </c:choose>
                                                        <html:textarea rows="3" property="reject_reason[${globalItemIndex}]" cols="20" styleClass="inputTextBox" />
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
                            <html:hidden property="save" value="" />
                            <html:image onclick="javascript:this.form.save.value='submit';return OnCompleteScorecardClick();" srcKey="editReview.Button.SaveAndCommit.img" altKey="editReview.Button.SaveAndCommit.alt" border="0" />&#160;
                            <html:image onclick="javascript:this.form.save.value='save';" srcKey="editReview.Button.SaveForLater.img" altKey="editReview.Button.SaveForLater.alt" border="0" />
                        </div>
                    </html:form>

                </div>
            </div>
        
        <jsp:include page="/includes/inc_footer.jsp" />

    </div>

</div>

</body>
</html:html>
