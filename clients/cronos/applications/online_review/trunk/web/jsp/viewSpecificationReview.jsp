<%--
  - Author: TCSASSEMBLER
  - Version: 2.0
  - Copyright (C) 2010 - 2014 TopCoder Inc., All Rights Reserved.
  -
  - Description: This page renders the Specification Review scorecard.
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
        <jsp:param name="thirdLevelPageKey" value="viewReview.title.${reviewType}" />
    </jsp:include>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

    <!-- TopCoder CSS -->
    <link type="text/css" rel="stylesheet" href="/css/style.css" />
    <link type="text/css" rel="stylesheet" href="/css/coders.css" />
    <link type="text/css" rel="stylesheet" href="/css/stats.css" />
    <link type="text/css" rel="stylesheet" href="/css/tcStyles.css" />

    <!-- CSS and JS by Petar -->
    <link type="text/css" rel="stylesheet" href="/css/or/new_styles.css" />
    <script language="JavaScript" type="text/javascript" src="/js/or/rollovers2.js"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript" src="/js/or/dojo.js"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript">
        var ajaxSupportUrl = "<or:url value='/ajaxSupport' />";
    </script>
    <script language="JavaScript" type="text/javascript" src="/js/or/ajax1.js"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript" src="/js/or/util.js"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript">

        // TODO: Make the following code reusable with editReview.jsp

        // The "passed_tests" and "all_tests" inputs should be populated
        // by the values taken from appropriate hidden "answer" input
        dojo.addOnLoad(function () {
            var passedTestsNodes = document.getElementsByName("passed_tests");
            for (var i = 0; i < passedTestsNodes.length; i++) {
                var allTestsNode = getChildByName(passedTestsNodes[i].parentNode, "all_tests");
                var answerNode = getChildByNamePrefix(passedTestsNodes[i].parentNode, "answer[");
                var parts = answerNode.value.split("/");
                if (parts && parts.length >= 2) {
                    passedTestsNodes[i].value = parts[0];
                    allTestsNode.value = parts[1];
                }
            }
        });
        
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
                    <jsp:include page="/includes/review/review_table_title.jsp" />

                    <%-- Note, that the form is a "dummy" one, only needed to support Struts tags inside of it --%>
                    <s:set var="actionName">View${reviewType}?rid=${review.id}</s:set>
                    <s:form action="%{#actionName}" namespace="/actions">

                    <c:set var="itemIdx" value="0" />
                    <table class="scorecard" cellpadding="0" width="100%" style="border-collapse: collapse;" id="table2">
                        <c:forEach items="${scorecardTemplate.allGroups}" var="group" varStatus="groupStatus">
                            <tr>
                                <td class="title" colspan="3">
                                    ${orfn:htmlEncode(group.name)} &#xA0;
                                    (${orfn:displayScore(pageContext.request, group.weight)})</td>
                            </tr>
                            <c:forEach items="${group.allSections}" var="section" varStatus="sectionStatus">
                                <tr>
                                    <td class="subheader" width="100%">
                                        ${orfn:htmlEncode(section.name)} &#xA0;
                                        (${orfn:displayScore(pageContext.request, section.weight)})</td>
                                    <td class="subheader" align="center" width="49%"><or:text key="editReview.SectionHeader.Weight" /></td>
                                    <td class="subheader" align="center" width="1%"><or:text key="editReview.SectionHeader.Response" /></td>
                                </tr>
                                <c:forEach items="${section.allQuestions}" var="question" varStatus="questionStatus">
                                    <c:set var="item" value="${review.allItems[itemIdx]}" />

                                    <tr class="light">
                                        <%@ include file="../includes/review/review_question.jsp" %>
                                        <%@ include file="../includes/review/review_static_answer.jsp" %>
                                    </tr>
                                    <%@ include file="../includes/review/review_comments.jsp" %>
                                    <c:set var="itemIdx" value="${itemIdx + 1}" />
                                </c:forEach>
                            </c:forEach>
                            <c:if test="${groupStatus.index == scorecardTemplate.numberOfGroups - 1}">
                                <c:if test="${not empty review.score}">
                                    <tr>
                                        <td class="header"><!-- @ --></td>
                                        <td class="headerC"><or:text key="editReview.SectionHeader.Total" /></td>
                                        <td class="headerC" colspan="1"><!-- @ --></td>
                                    </tr>
                                    <tr>
                                        <td class="value"><!-- @ --></td>
                                        <td class="valueC" nowrap="nowrap"><b id="scoreHere">${orfn:displayScore(pageContext.request, review.score)}</b></td>
                                        <td class="valueC"><!-- @ --></td>
                                    </tr>
                                </c:if>
                                <tr>
                                    <td class="lastRowTD" colspan="3"><!-- @ --></td>
                                </tr>
                            </c:if>
                        </c:forEach>
                    </table><br />
                    </s:form>
                    <c:forEach items="${review.allComments}" var="comment">
                        <c:if test='${comment.commentType.name == "Specification Review Comment"}'>
                            <c:set var="acceptSpecComment" value="${comment}" />
                        </c:if>
                    </c:forEach>

                    <table class="scorecard" cellpadding="0" cellspacing="0" width="100%" style="border-collapse:collapse;">
                        <tr>
                            <td class="title"><or:text key="editSpecificationReview.box.Approval" /></td>
                        </tr>
                        <tr class="highlighted">
                            <td class="value">
                                <c:choose>
                                    <c:when test='${(acceptSpecComment.extraInfo == "Rejected")}'>
                                        <or:text key="SpecificationReviewStatus.Rejected" />
                                    </c:when>
                                    <c:otherwise>
                                        <or:text key="SpecificationReviewStatus.Approved" />
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                        <tr>
                            <td class="lastRowTD"><!-- @ --></td>
                        </tr>
                    </table>
                    <br/>

                    <div align="right">
                        <c:if test="${isPreview}">
                            <a href="javascript:window.close();"><img src="<or:text key='btnClose.img' />" alt="<or:text key='btnClose.alt' />" border="0" /></a>
                        </c:if>
                        <c:if test="${not isPreview}">
                            <a href="<or:url value='/actions/ViewProjectDetails?pid=${project.id}' />">
                                <img src="<or:text key='btnBack.img' />" alt="<or:text key='btnBack.alt' />" border="0" /></a>
                        </c:if>
                        <br />
                    </div>

                </div>
            </div>

        <jsp:include page="/includes/inc_footer.jsp" />

    </div>

</div>

</body>
</html>
