<%--
  - Author: TCSASSEMBLER
  - Version: 2.0
  - Copyright (C) 2004 - 2014 TopCoder Inc., All Rights Reserved.
  -
  - Description: This page displays reopen scorecard confirmation page.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="or" uri="/or-tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <jsp:include page="/includes/project/project_title.jsp">
            <jsp:param name="thirdLevelPageKey" value="confirmReopenScorecard.title" />
        </jsp:include>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

        <!-- Reskin -->
        <link type="text/css" rel="stylesheet" href="/css/reskin-or/reskin.css">
        <link type="text/css" rel="stylesheet" href="/css/reskin-or/toasts.css">

        <!-- TopCoder CSS -->
        <link type="text/css" rel="stylesheet" href="/css/style.css" />
        <link type="text/css" rel="stylesheet" href="/css/coders.css" />

        <!-- CSS and JS by Petar -->
        <script language="JavaScript" type="text/javascript"
                src="/js/or/rollovers2.js"><!-- @ --></script>
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
            <jsp:include page="/includes/review/review_project.jsp">
                <jsp:param name="hideScoreInfo" value="true" />
            </jsp:include>
            <div class="divider"></div>
            <div id="mainContent">
                <div style="position: relative; width: 100%;">
                    <table class="scorecard" width="100%" cellpadding="0" cellspacing="0" style="border-collapse:collapse;">
                        <tr>
                            <td class="title"><or:text key="confirmReopenScorecard.box.title" /></td>
                        </tr>
                        <tr class="light">
                            <td class="finalScore">
                                <br />
                                <or:text key="confirmReopenScorecard.question" /><br /><br />
                            </td>
                        </tr>
                        <tr>
                            <td class="lastRowTD"><!-- @ --></td>
                        </tr>
                    </table>
                    <div class="saveChanges__button">
                        <a href="<or:url value='/actions/ReopenScorecard?rid=${review.id}&reopen=y' />" class="saveChanges__save"><or:text key='confirmReopenScorecard.btnConfirm.alt' /></a>
                        <a href="<or:url value='/actions/ViewProjectDetails?pid=${project.id}' />" class="saveChanges__save">
                            <or:text key='btnCancel.alt' />
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <jsp:include page="/includes/inc_footer_reskin.jsp" />
    </body>
</html>
