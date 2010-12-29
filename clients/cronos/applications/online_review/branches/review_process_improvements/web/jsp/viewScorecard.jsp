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
    <title>Online Review - View Scorecard</title>
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
    <script language="JavaScript" type="text/javascript">

        /**
         * TODO: Document it
         */
        function hideRow(rowId) {
            if (document != null && document.getElementById != null) {
                var row = document.getElementById(rowId);
                if (row != null) row.style.display = "none";
            }
        }

        /**
         * TODO: Document it
         */
        function focusControl(ctrlId) {
            if (document != null && document.getElementById != null) {
                var ctrl = document.getElementById(ctrlId);
                if (ctrl != null && ctrl.focus != null) ctrl.focus();
            }
        }
    </script>

</head>

<body onload="showAll()">
<div align="center">
    
    <div class="maxWidthBody" align="left">

        <jsp:include page="/includes/inc_header.jsp" />
        
        <jsp:include page="/includes/project/project_tabs.jsp" />
        
            <div id="mainMiddleContent">
                <div style="position: relative; width: 100%;">

                    <h3>${orfn:htmlEncode(scorecardTemplate.name)}</h3>

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
                                <tr>
                                    <td class="lastRowTD" colspan="${canPlaceAppeal ? 5 : (canPlaceAppealResponse ? 4 : 3)}"><!-- @ --></td>
                                </tr>
                            </c:if>
                        </c:forEach>
                    </table><br />

                </div>
            </div>
        
        <jsp:include page="/includes/inc_footer.jsp" />

    </div>

</div>

</body>
</html:html>
