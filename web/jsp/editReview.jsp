<%--
  - Author: TCSASSEMBLER
  - Version: 2.0
  - Copyright (C)  - 2014 TopCoder Inc., All Rights Reserved.
  -
  - Description: This page renders the editing review scorecard.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="or" uri="/or-tags" %>
<%@ page import="java.text.DecimalFormat,com.topcoder.onlinereview.component.webcommon.ApplicationServer" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>
<%@ taglib prefix="tc-webtag" uri="/tags/tc-webtags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<c:set var="reviewTypeWSStrippedOut" value="${fn:replace(reviewType, ' ', '')}"/>
<head>
    <jsp:include page="/includes/project/project_title.jsp">
        <jsp:param name="thirdLevelPageKey" value="editReview.title.${reviewTypeWSStrippedOut}" />
    </jsp:include>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

    <!-- Reskin -->
    <link type="text/css" rel="stylesheet" href="/css/reskin-or/reskin.css">
    <link type="text/css" rel="stylesheet" href="/css/reskin-or/toasts.css">

    <!-- TopCoder CSS -->
    <link type="text/css" rel="stylesheet" href="/css/style.css" />
    <link type="text/css" rel="stylesheet" href="/css/coders.css" />
    <link type="text/css" rel="stylesheet" href="/css/tcStyles.css" />

    <!-- CSS and JS by Petar -->
    <script language="JavaScript" type="text/javascript" src="/js/or/rollovers2.js"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript" src="/js/or/dojo.js"><!-- @ --></script>
    <script language="JavaScript" type="text/javascript" src="/js/or/util.js"><!-- @ --></script>

    <script type="text/javascript">
        function updateForumLink(projectId) {
            return fetch("<%=com.cronos.onlinereview.util.ConfigHelper.getChallengeByLegacyIdUrlV5()%>" + projectId)
                .then((response) => response.json())
                .then((data) => {
                    let with_forum = data.filter(item => 'discussions' in item);
                    let id = with_forum?.[0]?.id;
                    if (id !== undefined) {
                        let forumLinkEl = document.querySelector('.projectInfo__forumLink');
                        return forumLinkEl.href = "https://<%=ApplicationServer.FORUMS_SERVER_NAME%>/categories/" + id;
                    }
                });
        }
        document.addEventListener("DOMContentLoaded", function(){
            let projectId = ${project.id};
            updateForumLink(projectId);

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

    <script language="javascript" type="text/javascript">
    <!--

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


    /*
     * TODO: Document it
     */
    function addReviewResponse(itemIdx, responsesCellNode) {
        // Get the count of responses
        var responseCountNode = getChildByNamePrefix(responsesCellNode, "comment_count");
        var responseCount = parseInt(responseCountNode.value);
        // Increase response count
        responseCount++;
        responseCountNode.value = responseCount + "";

        // Get the response nodes
        var responseNodes = getChildrenByName(responsesCellNode, "response");

        // Clone template response node
        var newNode = responseNodes[0].cloneNode(true);
        // Alter the style of node, so that it is visible
        newNode.style["display"] = "";
        // Rename all the inputs to have a new index
        patchAllChildParamIndexes(newNode, itemIdx + "." + responseCount);
        // Append the newly created node to the existing ones
        dojo.dom.insertAfter(newNode, responseNodes[responseNodes.length - 1]);

        var responseNumberNode = getChildByName(newNode, "comment_number");
        if (responseNumberNode) {
            // Change the number of the response
            dojo.dom.textContent(responseNumberNode, responseCount + "");
        }
    }

    function submitReview(btn, value, target) {
        btn.form.save.value = value;
        btn.parentNode.parentNode.target = target;
    }

    /*
     * TODO: Delete this function
     */
    function removeReviewResponse(itemIdx, responseNode) {
        // Get responses cell node
        var responsesCellNode = responseNode.parentNode;

        // Delete the node for particular response
        responsesCellNode.removeChild(responseNode);

        // Get the count of responses
        var responseCountNode = getChildByNamePrefix(responsesCellNode, "comment_count");
        var responseCount = parseInt(responseCountNode.value);
        // Decrease response count
        responseCount--;
        responseCountNode.value = responseCount + "";

        // Get the response nodes
        var responseNodes = getChildrenByName(responsesCellNode, "response");

        // Renumber the other responses
        for (var i = 1; i < responseNodes.length; i++) {
            // Rename all the inputs to have a new index
            patchAllChildParamIndexes(responseNodes[i], itemIdx + "." + i);

            var responseNumberNode = getChildByName(responseNodes[i], "comment_number");
            if (responseNumberNode) {
                // Change the number of the response
                dojo.dom.textContent(responseNumberNode, i + "");
            }
        }
    }
    // -->
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

    <div class="content">
        <div class="content__inner">
            <jsp:include page="/includes/review/review_project.jsp">
                <jsp:param name="showFillScorecardLink" value="true" />
            </jsp:include>
            <div class="divider"></div>
            <jsp:include page="/includes/review/review_table_title.jsp" />
            <div id="mainContent">
                <div style="position: relative; width: 100%;">
                    <s:set var="actionName">Save${reviewTypeWSStrippedOut}</s:set>
                    <s:form id="reviewForm" action="%{#actionName}" method="POST" enctype="multipart/form-data" namespace="/actions">
                        <c:choose>
                            <c:when test="${review.id > -1}">
                                <input type="hidden" name="rid" value="${review.id}" />
                            </c:when>
                            <c:when test="${not empty requestScope.pid && requestScope.pid > 0}">
                                <input type="hidden" name="pid" value="${requestScope.pid}" />
                            </c:when>
                            <c:otherwise>
                                <input type="hidden" name="sid" value="${sid}" />
                            </c:otherwise>
                        </c:choose>

                        <c:if test="${orfn:isErrorsPresent(pageContext.request)}">
                            <table cellpadding="0" cellspacing="0" border="0">
                                <tr><td class="errorText"><or:text key="Error.saveReview.ValidationFailed" /></td></tr>
                            </table><br />
                        </c:if>

                        <c:set var="itemIdx" value="0" />
                        <c:set var="fileIdx" value="0" />

                        <c:forEach items="${scorecardTemplate.allGroups}" var="group" varStatus="groupStatus">
                            <table cellpadding="0" cellspacing="0" width="100%" class="scorecard" style="border-collapse:collapse;">
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
                                        <td class="subheader__weight" width="49%" align="center"><or:text key="editReview.SectionHeader.Weight" /></td>
                                        <td class="subheader__response" width="1%" align="center"><or:text key="editReview.SectionHeader.Response" /></td>
                                    </tr>
                                    <c:forEach items="${section.allQuestions}" var="question" varStatus="questionStatus">
                                        <c:if test="${managerEdit}">
                                            <c:set var="item" value="${review.allItems[itemIdx]}" />
                                        </c:if>

                                        <tr class="light">
                                            <%@ include file="../includes/review/review_question.jsp" %>
                                            <c:if test="${not managerEdit}">
                                                <td class="valueC" nowrap="nowrap">
                                                    <%@ include file="../includes/review/review_answer.jsp" %>
                                                    <div class="error"><s:fielderror escape="false"><s:param>answer[${itemIdx}]</s:param></s:fielderror></div>
                                                </td>
                                            </c:if>
                                            <c:if test="${managerEdit}">
                                                <%@ include file="../includes/review/review_static_answer.jsp" %>
                                            </c:if>
                                        </tr>
                                        <c:if test="${managerEdit}">
                                            <%@ include file="../includes/review/review_comments.jsp" %>
                                        </c:if>
                                        <tr class="highlighted">
                                            <td class="value" colspan="${managerEdit ? 2 : 3}">
                                                <input type="hidden" name="comment_count[${itemIdx}]"  value="<or:fieldvalue field='comment_count[${itemIdx}]' />" />
                                                <c:forEach var="commentIdx" begin="0" end="${reviewForm.map['comment_count'][itemIdx]}">
                                                    <div name="response" style="${commentIdx eq 0 ? 'display: none;' : ''}">
                                                        <c:if test="${not managerEdit}">
                                                            <div class="reviewResponse">
                                                                <b><or:text key="editReview.Question.Response.title"/>
                                                                    <span name="comment_number">${commentIdx}</span>:
                                                                </b>
                                                                <div class="editReview__input scoreResponse">
                                                                    <input type="hidden" name="comment_type(${itemIdx}.${commentIdx})">
                                                                    <div class="custom-select-wrapper">
                                                                        <div class="custom-select grey">
                                                                            <div class="custom-select__trigger"><span></span>
                                                                                <div class="arrow"></div>
                                                                            </div>
                                                                            <div class="custom-options">
                                                                                <c:set var="OR_FIELD_TO_SELECT" value="comment_type(${itemIdx}.${commentIdx})"/>
                                                                                <c:forEach items="${allCommentTypes}" var="commentType" >
                                                                                    <span class="custom-option custom-option-grey" data-value="${commentType.id}" <or:selected value="${commentType.id}"/>><or:text key="CommentType.${fn:replace(commentType.name, ' ', '')}" def="${commentType.id}"/></span>
                                                                                </c:forEach>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </c:if>
                                                        <c:if test="${managerEdit}">
                                                            <b><or:text key="editReview.Question.ManagerComment.title"/>:</b>
                                                        </c:if>
                                                        <span class="error"><s:fielderror escape="false"><s:param>comment(${itemIdx}.${commentIdx})</s:param></s:fielderror></span>
                                                        <div class="review__comment">
                                                            <textarea rows="5" name="comment(${itemIdx}.${commentIdx})" cols="20" class="inputTextBox" ><or:fieldvalue field="comment(${itemIdx}.${commentIdx})" /></textarea>
                                                        </div>
                                                    </div>
                                                </c:forEach>
                                                <a class="addResponse"
                                                    onclick="addReviewResponse(${itemIdx}, this.parentNode);" style="cursor:hand;">
                                                    <or:text key='editReview.Button.AddResponse.alt' />
                                                </a>
                                                <br />
                                                <br />
                                                <c:if test="${(not managerEdit) and question.uploadDocument}">
                                                    <div class="fileUpload">
                                                        <c:if test="${empty uploadedFileIds[fileIdx]}">
                                                            <b><or:text key="editReview.Document.Upload"/>
                                                            <c:if test="${question.uploadRequired}">
                                                                <font color="#CC0000"><or:text key="global.required.paren"/></font>:
                                                            </c:if>
                                                            <c:if test="${not question.uploadRequired}">
                                                                <span style="font-weight:normal;"><or:text key="global.optional.paren"/></span>:
                                                            </c:if></b>
                                                        </c:if>
                                                        <c:if test="${not empty uploadedFileIds[fileIdx]}">
                                                            <a href="<or:url value='/actions/DownloadDocument?uid=${uploadedFileIds[fileIdx]}' />" style="color: #0d61bf;text-decoration: none;"><or:text key="editReview.Document.Download"/></a>
                                                            <b>&#160; <or:text key="editReview.Document.Update"/>
                                                            <span style="font-weight:normal;"><or:text key="global.optional.paren"/></span>:</b>
                                                        </c:if>
                                                        &#160;<input type="file" name="file[${fileIdx}]" size="20" class="inputBox fileUpload" style="width:350px;vertical-align:middle;" value="<or:fieldvalue field='file[${fileIdx}]' />" />
                                                        &#160; <span class="error"><s:fielderror escape="false"><s:param>file[${fileIdx}]</s:param></s:fielderror></span>
                                                        <c:set var="fileIdx" value="${fileIdx + 1}" />
                                                    </div>
                                                </c:if><br/>
                                            </td>
                                            <c:if test="${managerEdit}">
                                                <td class="valueC" nowrap="nowrap">
                                                    <%@ include file="../includes/review/review_answer.jsp" %>
                                                    <div class="error"><s:fielderror escape="false"><s:param>answer[${itemIdx}]</s:param></s:fielderror></div>
                                                </td>
                                            </c:if>
                                        </tr>

                                        <c:set var="itemIdx" value="${itemIdx + 1}" />
                                    </c:forEach>
                                </c:forEach>
                                <tr>
                                    <td class="lastRowTD" colspan="3"><!-- @ --></td>
                                </tr>
                            </table>
                        </c:forEach>
                        <div class="saveChanges__button">
                            <input type="hidden" name="save" value="" />
                            <c:if test="${not managerEdit}">
                                <button onclick="submitReview(this, 'submit', '_self')" class="saveChanges__save"><or:text key='editReview.Button.SaveAndCommit.alt' /></button>
                                <button onclick="submitReview(this, 'submit', '_self')" class="saveChanges__save"><or:text key='editReview.Button.SaveForLater.alt' /></button>
                                <button onclick="submitReview(this, 'preview', '_blank')" class="saveChanges__save"><or:text key='editReview.Button.Preview.alt' /></button>
                            </c:if>
                            <c:if test="${managerEdit}">
                                <button onclick="submitReview(this, 'submit', '_self')" class="saveChanges__save"><or:text key='btnSaveChanges.alt' /></button>
                            </c:if>
                        </div>
                    </s:form>

                </div>
            </div>
    </div>
    <jsp:include page="/includes/inc_footer_reskin.jsp" />
</div>
<script type="text/javascript">
    var saveReview = document.getElementById('reviewForm');
    saveReview.addEventListener('submit', function(e) {
        e.preventDefault();
        if (saveReview.save.value !== 'preview') {
        document.querySelectorAll('.saveChanges__save')
        .forEach(btn => btn.disabled = true);
        }
        saveReview.submit();
    });
</script>

</body>
</html>
