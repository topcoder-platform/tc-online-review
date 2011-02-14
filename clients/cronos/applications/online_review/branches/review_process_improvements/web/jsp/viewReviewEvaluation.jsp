<%--
  - Author: TCSDEVELOPER
  - Version: 1
  - Copyright (C) 2005 - 2010 TopCoder Inc., All Rights Reserved.
  -
  - Description: This page renders the Review scorecard.
  -
  - Version 1 
--%>
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
        <jsp:param name="thirdLevelPageKey" value="viewReview.title.${reviewType}" />
    </jsp:include>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

    <!-- TopCoder CSS -->
    <link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/style.css' />" />
    <link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/coders.css' />" />
    <link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/stats.css' />" />
    <link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/tcStyles.css' />" />

    <!-- CSS and JS by Petar -->
    <link type="text/css" rel="stylesheet" href="<html:rewrite href='/css/or/new_styles.css' />" />
    <script language="JavaScript" type="text/javascript" src="<html:rewrite href='/js/or/rollovers2.js' />"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript" src="<html:rewrite href='/js/or/dojo.js' />"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript">
        var ajaxSupportUrl = "<html:rewrite page='/ajaxSupport' />";
    </script>
    <script language="JavaScript" type="text/javascript" src="<html:rewrite href='/js/or/ajax1.js' />"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript" src="<html:rewrite href='/js/or/util.js' />"><!-- @ --></script>


</head>

<body>
<div align="center">
    
    <div class="maxWidthBody" align="left">

        <jsp:include page="/includes/inc_header.jsp" />
        
        <jsp:include page="/includes/project/project_tabs.jsp" />
        
            <div id="mainMiddleContent">
                <div style="position: relative; width: 100%;">

                    <jsp:include page="/includes/review/review_project.jsp" />
                    <h3>${orfn:htmlEncode(scorecardTemplate.name)}</h3>

                    <%-- Note, that the form is a "dummy" one, only needed to support Struts tags inside of it --%>
                    <html:form action="/actions/View${reviewType}.do?method=view${reviewType}&rid=${review.id}">

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
                                    <td class="subheader" align="center" width="49%"><bean:message key="editReview.SectionHeader.Weight" /></td>
                                    <td class="subheader" align="center" width="1%"><bean:message key="editReview.SectionHeader.Response" /></td>
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
                                        <td class="headerC"><bean:message key="editReview.SectionHeader.Total" /></td>
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
                    </html:form>

                    <div align="right">
                        <c:if test="${isPreview}">
                            <a href="javascript:window.close();"><html:img srcKey="btnClose.img" altKey="btnClose.alt" border="0" /></a>
                        </c:if>
                        <c:if test="${not isPreview}">
                            <html:link page="/actions/ViewProjectDetails.do?method=viewProjectDetails&pid=${project.id}">
                                <html:img srcKey="btnBack.img" altKey="btnBack.alt" border="0" /></html:link>
                        </c:if>
                        <br />
                    </div>

                </div>
            </div>
        
        <jsp:include page="/includes/inc_footer.jsp" />

    </div>

</div>

</body>
</html:html>
