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
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<head>
    <jsp:include page="/includes/project/project_title.jsp">
        <jsp:param name="thirdLevelPageKey" value="reviewCommitted.title" />
    </jsp:include>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

    <!-- TopCoder CSS -->
    <link type="text/css" rel="stylesheet" href="/css/style.css">
    <link type="text/css" rel="stylesheet" href="/css/coders.css">
    <link type="text/css" rel="stylesheet" href="/css/stats.css">
    <link type="text/css" rel="stylesheet" href="/css/tcStyles.css">

    <!-- JS from wireframes -->
    <script language="javascript" type="text/javascript" src="/js/or/popup.js"></script>
    <script language="javascript" type="text/javascript" src="/js/or/expand_collapse.js"></script>

    <!-- CSS and JS by Petar -->
    <link type="text/css" rel="stylesheet" href="/css/or/new_styles.css">
    <script language="JavaScript" type="text/javascript" src="/js/or/rollovers2.js"></script>
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

                    <table class="scorecard" width="100%" cellpadding="0" cellspacing="0" style="border-collapse:collapse;">
                        <tr>
                            <td class="title"><or:text key="reviewCommitted.Committed" /></td>
                        </tr>
                        <tr class="light">
                            <td class="valueC">
                                <br />
                                <or:text key="reviewCommitted.Score" />
                                <b>${orfn:displayScore(null, reviewScore)}</b><br /><br />
                            </td>
                        </tr>
                        <tr>
                            <td class="lastRowTD"><!-- @ --></td>
                        </tr>
                    </table><br />

                    <div align="right">
                        <a href="<or:url value='/actions/ViewProjectDetails?pid=${project.id}' />"><img src="<or:text key='reviewCommitted.ReturnToProjDet.img' />" alt="<or:text key='reviewCommitted.ReturnToProjDet.alt' />" border="0" /></a>&#160;
                        <a href="<or:url value="/actions/View${fn:replace(reviewType, ' ', '')}?rid=${rid}" />"><img src="<or:text key='reviewCommitted.ViewScorecard.img' />" alt="<or:text key='reviewCommitted.ViewScorecard.alt' />" border="0" /></a>
                    </div><br />

                </div>
            </div>
        
        <jsp:include page="/includes/inc_footer.jsp" />

    </div>

</div>

</body>
</html>
