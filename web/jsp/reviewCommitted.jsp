<%--
  - Author: TCSASSEMBLER
  - Version: 2.0
  - Copyright: Copyright (C)  - 2014 TopCoder Inc., All Rights Reserved.
  -
  - Description: The page renders the commit review result.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="or" uri="/or-tags" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<head>
    <jsp:include page="/includes/project/project_title.jsp">
        <jsp:param name="thirdLevelPageKey" value="reviewCommitted.title" />
    </jsp:include>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

    <!-- Reskin -->
    <link type="text/css" rel="stylesheet" href="/css/reskin-or/reskin.css">
    <link type="text/css" rel="stylesheet" href="/css/reskin-or/toasts.css">

    <!-- TopCoder CSS -->
    <link type="text/css" rel="stylesheet" href="/css/style.css">
    <link type="text/css" rel="stylesheet" href="/css/coders.css">

    <!-- JS from wireframes -->
    <script language="javascript" type="text/javascript" src="/js/or/popup.js"></script>
    <script language="javascript" type="text/javascript" src="/js/or/expand_collapse.js"></script>

    <!-- CSS and JS by Petar -->
    <script language="JavaScript" type="text/javascript" src="/js/or/rollovers2.js"></script>
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

    <div class="content">
        <div class="content__inner">
            <jsp:include page="/includes/review/review_project.jsp" />
            <div class="divider"></div>
            <div id="mainContent">
                <div style="position: relative; width: 100%;">
                    <h3 class="scoreBoard__title" style="margin-bottom: 32px;">${orfn:htmlEncode(scorecardTemplate.name)}</h3>

                    <table class="scorecard" width="100%" cellpadding="0" cellspacing="0" style="border-collapse:collapse;">
                        <tr>
                            <td class="title"><or:text key="reviewCommitted.Committed" /></td>
                        </tr>
                        <tr class="light">
                            <td class="finalScore">
                                <br />
                                <or:text key="reviewCommitted.Score" />
                                <b>${orfn:displayScore(null, reviewScore)}</b><br /><br />
                            </td>
                        </tr>
                        <tr>
                            <td class="lastRowTD"><!-- @ --></td>
                        </tr>
                    </table>
                    <div class="saveChanges__button">
                        <a href="<or:url value='/actions/ViewProjectDetails?pid=${project.id}' />" class="saveChanges__save"><or:text key='reviewCommitted.ReturnToProjDet.alt' /></a>
                        <a href="<or:url value="/actions/View${fn:replace(reviewType, ' ', '')}?rid=${rid}" />" class="saveChanges__save"><or:text key='reviewCommitted.ViewScorecard.alt' /></a>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <jsp:include page="/includes/inc_footer_reskin.jsp" />
</body>
</html>
