<%--
  - Author: TCSASSEMBLER
  - Version: 2.2
  - Copyright (C) 2005 - 2014 TopCoder Inc., All Rights Reserved.
  - changes in the Topcoder - Online Review Update - Post to Event BUS v1.0
  - - add the handleEventOfAppealResponse js function to fire the appeal response event
  - changes in the version 2.2 (Topcoder - Online Review Update - Post to Event BUS Part 2 v1.0)
  - - add the handleEventOfAppeal js function to fire the appeal submit event
  - Description: This page renders the Review scorecard.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
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

    <!-- Reskin -->
    <link type="text/css" rel="stylesheet" href="/css/reskin-or/reskin.css">
    <link type="text/css" rel="stylesheet" href="/css/reskin-or/toasts.css">

    <!-- TopCoder CSS -->
    <link type="text/css" rel="stylesheet" href="/css/style.css" />

    <!-- CSS and JS by Petar -->
    <script language="JavaScript" type="text/javascript" src="/js/or/rollovers2.js"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript" src="/js/or/dojo.js"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript">
        var ajaxSupportUrl = "<or:url value='/ajaxSupport' />";
        var challengeId = '${project.id}';
        function handleEventOfAppealResponse(reviewId) {
            // create the Ajax request
            var myRequest = createXMLHttpRequest();

            // set the callback function
            // TODO: Check for errors not handled by Ajax Support
            myRequest.onreadystatechange = function() {
                if (myRequest.readyState == 4 && myRequest.status == 200) {
                    // the response is ready.
                }
            };

            // send the request
            myRequest.open('GET', '<%=request.getContextPath() %>/actions/eventBusHandleAppealResponseAction?reviewId=' + reviewId, true);
            myRequest.send(null);
        }

        /**
         * Fire the appeal submit event to the event bus
         *
         * @param challengeId the challenge id of the appeal
         */
        function handleEventOfAppeal(challengeId) {
            // create the Ajax request
            var myRequest = createXMLHttpRequest();

            // set the callback function
            // TODO: Check for errors not handled by Ajax Support
            myRequest.onreadystatechange = function() {
                if (myRequest.readyState == 4 && myRequest.status == 200) {
                    // the response is ready.
                }
            };

            // send the request
            myRequest.open('GET', '<%=request.getContextPath() %>/actions/eventBusHandleAppealAction?challengeId=' + challengeId, true);
            myRequest.send(null);
        }
    </script>
    <script language="JavaScript" type="text/javascript" src="/js/or/ajax1.js"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript" src="/js/or/util.js"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript">

        /**
         * TODO: Document it
         */
        function placeAppeal(itemIdx, itemId, reviewId) {
            // Find appeal text input node
            appealTextNode = document.getElementsByName("appeal_text[" + itemIdx + "]")[0];
            // Get html-encoded Appeal text
            var appealText = appealTextNode.value.replace(/\r\n/g, "\n");
            appealText = trimString(appealText);
            var appealTextLength = appealText.length;

            // assemble the request XML
            var content =
                '<?xml version="1.0" ?>' +
                '<request type="PlaceAppeal">' +
                "<parameters>" +
                '<parameter name="ReviewId">' +
                reviewId +
                "</parameter>" +
                '<parameter name="ItemId">' +
                itemId +
                "</parameter>" +
                '<parameter name="Text"><![CDATA[' +
                appealText +
                "]]></parameter>" +
                '<parameter name="TextLength">' +
                appealTextLength +
                "</parameter>" +
                "</parameters>" +
                "</request>";

            // Send the AJAX request
            sendRequest(content,
                function (result, respXML) {
                    // operation succeeded
                    // TODO: Some changes to here
                    toggleDisplay("appealText_" + itemIdx);
                    handleEventOfAppeal(challengeId);
                },
                function (result, respXML) {
                    // operation failed, alert the error message to the user
                    if (result.toLowerCase() == "possible text cutoff error") {
                        alert("<or:text key='viewReview.appealCutoffWarning' />");
                    } else alert("An error occured while placing the appeal: " + result);
                }
            );
        }

        /**
         * TODO: Document it
         */
        function placeAppealResponse(itemIdx, itemId, reviewId) {
            // Find appeal response text input node
            responseTextNode = document.getElementsByName("appeal_response_text[" + itemIdx + "]")[0];
            // Get appeal response text
            var responseText = responseTextNode.value;

            // Find appeal response modified answer node
            answerNode = document.getElementsByName("answer[" + itemIdx + "]")[0];
            // Retrieve modified answer value
            modifiedAnswer = answerNode.value;

            // Find the appeal success status node
            appealSuccessNode = document.getElementsByName("appeal_response_success[" + itemIdx + "]")[0];

            // assemble the request XML
            var content =
                '<?xml version="1.0" ?>' +
                '<request type="ResolveAppeal">' +
                '<parameters>' +
                '<parameter name="ReviewId">' +
                reviewId +
                '</parameter>' +
                '<parameter name="ItemId">' +
                itemId +
                '</parameter>' +
                '<parameter name="Text"><![CDATA[' +
                responseText +
                ']]></parameter>' +
                '<parameter name="Answer">' +
                modifiedAnswer +
                '</parameter>' +
                '<parameter name="Status">' +
                (appealSuccessNode.checked ? "Succeeded" : "Failed") +
                '</parameter>';

            commentTypeNodes = document.getElementsByName("comment_type[" + itemIdx + "]");
            commentIdNodes = document.getElementsByName("comment_id[" + itemIdx + "]");

            for (var i = 0; i < commentTypeNodes.length; i++) {
                content = content + '<parameter name="CommentType' +
                    commentIdNodes[i].value + '">' +
                    commentTypeNodes[i].value + '</parameter>';
            }

            content = content + "</parameters>" + "</request>";

            // Send the AJAX request
            sendRequest(content,
                function (result, respXML) {
                    // operation succeeded
                    // TODO: Some changes to here
                    hideRow("placeAppealResponse_" + itemIdx);
                    alterComment(itemIdx);
                    alterScore(respXML.getElementsByTagName("result")[0]);

                    handleEventOfAppealResponse(reviewId);
                },
                function (result, respXML) {
                    // operation failed, alert the error message to the user
                    alert("An error occured while resolving the appeal: " + result);
                }
            );
        }

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
        function alterComment(itemIdx) {
            var commentTypeNodes = document.getElementsByName("comment_type[" + itemIdx + "]");
            var commentTextNodes = document.getElementsByName("cmtTypeStatic_" + itemIdx);
            if (commentTypeNodes == null || commentTextNodes == null) {
                return;
            }

            for (var i = 0; i < commentTypeNodes.length; i++) {
                var text = commentTypeNodes[i].options[commentTypeNodes[i].selectedIndex].value;

                commentTextNodes[i].innerHTML = text;
                commentTypeNodes[i].style.display = "none";
                commentTextNodes[i].style.display = "inline";
        }
        }

        /**
         * TODO: Document it
         */
        function alterScore(resultNode) {
            if (document == null || document.getElementById == null) {
                return;
            }

            var newScore = parseFloat(dojo.dom.textContent(resultNode));
            var newScoreNormalized = Math.round(newScore * 100);

            var scoreWhole = Math.floor(newScoreNormalized / 100);
            var scoreFrac = newScoreNormalized % 100;

            if (scoreFrac % 10 == 0) {
                scoreFrac = Math.floor(scoreFrac / 10);
            }

            var scoreEl = document.getElementById("scoreHere");
            if (scoreEl != null) scoreEl.innerHTML = scoreWhole.toString() + "." + scoreFrac.toString();
        }

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
    <script type="text/javascript">
        document.addEventListener("DOMContentLoaded", function(){
            for (const dropdown of document.querySelectorAll(".custom-select-wrapper")) {
                dropdown.addEventListener('click', function () {
                    this.querySelector('.custom-select').classList.toggle('open');
                });
            }

            for (const options of document.querySelectorAll('.custom-options')) {
                let selected = options.querySelector('.custom-option[selected]');
                if (!selected) {
                    let option = options.querySelector('.custom-option');
                    option.selected = true;
                    option.classList.add('selected');
                } else {
                    selected.classList.add('selected');
                }
            }

            for (const option of document.querySelectorAll(".custom-option")) {
                const input = option.closest('.editReview__input').querySelector('input');
                const selectedSpan = option.closest('.custom-select').querySelector('.custom-select__trigger span');
                if (option.classList.contains('selected')) {
                    const currentSelected = option.textContent;
                    if (selectedSpan.textContent == '') {
                        selectedSpan.textContent = currentSelected;
                    }
                    input.value = option.getAttribute('data-value');
                }
                option.addEventListener('click', function (e) {
                    if (!this.classList.contains('selected')) {
                        this.parentNode.querySelector('.custom-option.selected').classList.remove('selected');
                        this.classList.add('selected');
                        selectedSpan.textContent = this.textContent;
                        input.value = this.getAttribute('data-value');
                    }
                });
            }

            window.addEventListener('click', function (e) {
                for (const select of document.querySelectorAll('.custom-select')) {
                    if (!select.contains(e.target)) {
                        select.classList.remove('open');
                    }
                }
            });
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

            <%-- Note, that the form is a "dummy" one, only needed to support Struts tags inside of it --%>
            <s:set var="actionName">View${fn:replace(reviewType, ' ', '')}?rid=${review.id}</s:set>
            <s:form action="%{#actionName}" namespace="/actions">

            <c:set var="itemIdx" value="0" />
            <table class="scorecard" cellpadding="0" width="100%" style="border-collapse: collapse;" id="table2">
                <c:forEach items="${scorecardTemplate.allGroups}" var="group" varStatus="groupStatus">
                    <tr>
                        <td class="title" colspan="${canPlaceAppeal ? 5 : (canPlaceAppealResponse ? 4 : 3)}">
                            ${orfn:htmlEncode(group.name)} &#xA0&#xA0&#xA0;
                            (${orfn:displayScore(pageContext.request, group.weight)})</td>
                    </tr>
                    <c:forEach items="${group.allSections}" var="section" varStatus="sectionStatus">
                        <tr>
                            <td class="subheader" width="100%">
                                ${orfn:htmlEncode(section.name)} &#xA0&#xA0&#xA0;
                                (${orfn:displayScore(pageContext.request, section.weight)})</td>
                            <td class="subheader__weight" align="center" width="49%"><or:text key="editReview.SectionHeader.Weight" /></td>
                            <td class="subheader__response" align="center" width="1%"><or:text key="editReview.SectionHeader.Response" /></td>
                            <c:if test="${canPlaceAppeal or canPlaceAppealResponse}">
                                <td class="subheader__weight" align="center" width="1%"><or:text key="editReview.SectionHeader.AppealStatus" /></td>
                            </c:if>
                            <c:if test="${canPlaceAppeal}">
                                <td class="subheader__weight" align="center" width="1%"><or:text key="editReview.SectionHeader.Appeal" /></td>
                            </c:if>
                        </tr>
                        <c:forEach items="${section.allQuestions}" var="question" varStatus="questionStatus">
                            <c:set var="item" value="${review.allItems[itemIdx]}" />

                            <tr class="light">
                                <%@ include file="../includes/review/review_question.jsp" %>
                                <%@ include file="../includes/review/review_static_answer.jsp" %>
                                <c:if test="${canPlaceAppeal or canPlaceAppealResponse}">
                                    <td class="valueC" align="center">${appealStatuses[itemIdx]}<!-- @ --></td>
                                </c:if>
                                <c:if test="${canPlaceAppeal}">
                                    <c:if test="${not empty appealStatuses[itemIdx]}">
                                        <td class="value"><!-- @ --></td>
                                    </c:if>
                                    <c:if test="${empty appealStatuses[itemIdx]}">
                                        <td class="valueC">
                                            <a class="showText" id="placeAppeal_${itemIdx}"
                                                href="javascript:toggleDisplay('appealText_${itemIdx}');toggleDisplay('placeAppeal_${itemIdx}');focusControl('appealArea_${itemIdx}');"><img
                                                src="<or:text key='editReview.Button.Appeal.img' />" alt="<or:text key='editReview.Button.Appeal.alt' />" /></a>
                                        </td>
                                    </c:if>
                                </c:if>
                            </tr>
                            <%@ include file="../includes/review/review_comments.jsp" %>
                            <c:if test="${canPlaceAppeal and (empty appealStatuses[itemIdx])}">
                                <tr class="highlighted">
                                    <td class="value" colspan="6">
                                        <div id="appealText_${itemIdx}" class="hideText">
                                            <b><or:text key="editReview.Question.AppealText.title"/>:</b><br />
                                            <div class="review__comment">
                                                <textarea id="appealArea_${itemIdx}" name="appeal_text[${itemIdx}]" rows="2" cols="20"></textarea><br />
                                            </div>
                                            <a href="javascript:placeAppeal(${itemIdx}, ${item.id}, ${review.id});"><img
                                                src="<or:text key='editReview.Button.SubmitAppeal.img' />" alt="<or:text key='editReview.Button.SubmitAppeal.alt' />"
                                                border="0" hspace="5" vspace="9" /></a><br />
                                        </div>
                                    </td>
                                </tr>
                            </c:if>
                            <c:if test="${canPlaceAppealResponse and (appealStatuses[itemIdx] == 'Unresolved')}">
                                <tr id="placeAppealResponse_${itemIdx}" class="highlighted">
                                    <td class="value" colspan="3">
                                        <b><or:text key="editReview.Question.AppealResponseText.title"/>:</b><br />
                                        <div class="review__comment">
                                            <textarea rows="2" name="appeal_response_text[${itemIdx}]" cols="20"></textarea><br />
                                        </div>
                                        <input type="checkbox" name="appeal_response_success[${itemIdx}]" />
                                        <or:text key="editReview.Question.AppealSucceeded.title" />
                                    </td>
                                    <td class="value" nowrap="nowrap">
                                        <or:text key="editReview.Question.ModifiedResponse.title"/>:<br />
                                        <%@ include file="../includes/review/review_answer_reskin.jsp" %><br /><br />
                                        <a class="addResponse" href="javascript:placeAppealResponse(${itemIdx}, ${item.id}, ${review.id});">
                                            <or:text key='editReview.Button.SubmitAppealResponse.alt' /></a>
                                    </td>
                                </tr>
                            </c:if>

                            <c:set var="itemIdx" value="${itemIdx + 1}" />
                        </c:forEach>
                    </c:forEach>
                    <c:if test="${groupStatus.index == scorecardTemplate.numberOfGroups - 1}">
                        <c:if test="${not empty review.score}">
                            <tr class="review">
                                <td class="header"><!-- @ --></td>
                                <td class="headerC"><or:text key="editReview.SectionHeader.Total" /></td>
                                <td class="headerC" colspan="${canPlaceAppeal ? 3 : (canPlaceAppealResponse ? 2 : 1)}"><!-- @ --></td>
                            </tr>
                            <tr>
                                <td class="totalValue"><!-- @ --></td>
                                <td class="totalValueC" nowrap="nowrap"><p id="scoreHere">${orfn:displayScore(pageContext.request, review.score)}</p></td>
                                <td class="totalValueC"><!-- @ --></td>
                                <c:if test="${canPlaceAppeal or canPlaceAppealResponse}">
                                    <td class="totalValueC"><!-- @ --></td>
                                </c:if>
                                <c:if test="${canPlaceAppeal}">
                                    <td class="totalValueC"><!-- @ --></td>
                                </c:if>
                            </tr>
                        </c:if>
                        <tr>
                            <td class="lastRowTD" colspan="${canPlaceAppeal ? 5 : (canPlaceAppealResponse ? 4 : 3)}"><!-- @ --></td>
                        </tr>
                    </c:if>
                </c:forEach>
            </table><br />
            </s:form>

            <div align="right">
                <c:if test="${isPreview}">
                    <a class="backToHome" href="javascript:window.close();">
                        Close
                    </a>
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
