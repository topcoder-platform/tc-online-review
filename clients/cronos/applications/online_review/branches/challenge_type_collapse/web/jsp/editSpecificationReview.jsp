<%--
  - Author: TCSASSEMBLER
  - Version: 2.0
  - Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
  -
  - Description: This page renders the Specification Review scorecard for editing.
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
        <jsp:param name="thirdLevelPageKey" value="editReview.title.SpecificationReview" />
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
    <script language="JavaScript" type="text/javascript" src="/js/or/util.js"><!-- @ --></script>

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

    <script language="JavaScript" type="text/javascript">
        function OnCompleteScorecardClick() {
            var approveCheckBox = document.getElementById("approveSpec");
            var isRejected = !(approveCheckBox.checked);

            return (isRejected) ? confirm("<or:text key='editSpecificationReview.BeforeReject' />") : true;
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

                    <jsp:include page="/includes/review/review_project.jsp">
                        <jsp:param name="showFillScorecardLink" value="true" />
                    </jsp:include>
                    <jsp:include page="/includes/review/review_table_title.jsp" />

                    <s:form action="SaveSpecificationReview" method="POST" enctype="multipart/form-data" namespace="/actions">
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
                                        <td class="subheader" width="49%" align="center"><or:text key="editReview.SectionHeader.Weight" /></td>
                                        <td class="subheader" width="1%" align="center"><or:text key="editReview.SectionHeader.Response" /></td>
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
                                                <c:forEach var="commentIdx" begin="0" end="${specificationReviewForm.map['comment_count'][itemIdx]}">
                                                    <div name="response" style="${commentIdx eq 0 ? 'display: none;' : ''}">
                                                        <c:if test="${not managerEdit}">
                                                            <b><or:text key="editReview.Question.Response.title"/>
                                                                <span name="comment_number">${commentIdx}</span>:
                                                            </b>
                                                            <select name="comment_type(${itemIdx}.${commentIdx})" class="inputBox"><c:set var="OR_FIELD_TO_SELECT" value="comment_type(${itemIdx}.${commentIdx})"/>
                                                                <c:forEach items="${allCommentTypes}" var="commentType" >
                                                                    <option  value="${commentType.id}" <or:selected value="${commentType.id}"/>><or:text key="CommentType.${fn:replace(commentType.name, ' ', '')}" def="${commentType.id}" /></option>
                                                                </c:forEach>
                                                            </select>
                                                        </c:if>
                                                        <c:if test="${managerEdit}">
                                                            <b><or:text key="editReview.Question.ManagerComment.title"/>:</b>
                                                        </c:if>
                                                        <span class="error"><s:fielderror escape="false"><s:param>comment(${itemIdx}.${commentIdx})</s:param></s:fielderror></span>
                                                        <textarea rows="2" name="comment(${itemIdx}.${commentIdx})" cols="20" class="inputTextBox" ><or:fieldvalue field="comment(${itemIdx}.${commentIdx})" /></textarea>
                                                    </div>
                                                </c:forEach>
                                                <img src="<or:text key='editReview.Button.AddResponse.img' />" alt="<or:text key='editReview.Button.AddResponse.alt' />"
                                                    onclick="addReviewResponse(${itemIdx}, this.parentNode);" style="cursor:hand;" /><br />
                                                <c:if test="${(not managerEdit) and question.uploadDocument}">
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
                                                        <a href="<or:url value='/actions/DownloadDocument?uid=${uploadedFileIds[fileIdx]}' />"><or:text key="editReview.Document.Download"/></a>
                                                        <b>&#160; <or:text key="editReview.Document.Update"/>
                                                        <span style="font-weight:normal;"><or:text key="global.optional.paren"/></span>:</b>
                                                    </c:if>
                                                    &#160;<input type="file" name="file[${fileIdx}]" size="20" class="inputBox" style="width:350px;vertical-align:middle;" value="<or:fieldvalue field='file[${fileIdx}]' />" />
                                                    &#160; <span class="error"><s:fielderror escape="false"><s:param>file[${fileIdx}]</s:param></s:fielderror></span>
                                                    <c:set var="fileIdx" value="${fileIdx + 1}" />
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
                            </table><br />
                        </c:forEach>

                        <table class="scorecard" cellpadding="0" cellspacing="0" width="100%" style="border-collapse:collapse;">
                            <tr>
                                <td class="title"><label for="rejectFixes"><or:text key="editSpecificationReview.box.Approval" /></label></td>
                            </tr>
                            <tr class="highlighted">
                                <td class="value">
                                    <input type="checkbox" id="approveSpec" name="approve_specification"  <or:checked name='approve_specification' value='on|yes|true' /> />
                                    <b><or:text key="editSpecificationReview.ApproveSpecification" /></b></td>
                            </tr>
                            <tr>
                                <td class="lastRowTD"><!-- @ --></td>
                            </tr>
                        </table>
                        <br/>

                        <div align="right">
                            <input type="hidden" name="save" value="" />
                            <c:if test="${not managerEdit}">
                                <input type="image"  onclick="javascript:this.form.save.value='submit'; this.parentNode.parentNode.target='_self';return OnCompleteScorecardClick();" src="<or:text key='editReview.Button.SaveAndCommit.img' />" alt="<or:text key='editReview.Button.SaveAndCommit.alt' />" border="0"/>&#160;
                                <input type="image"  onclick="javascript:this.form.save.value='save'; this.parentNode.parentNode.target='_self';" src="<or:text key='editReview.Button.SaveForLater.img' />" alt="<or:text key='editReview.Button.SaveForLater.alt' />" border="0"/>&#160;
                                <input type="image"  onclick="javascript:this.form.save.value='preview'; this.parentNode.parentNode.target='_blank';" src="<or:text key='editReview.Button.Preview.img' />" alt="<or:text key='editReview.Button.Preview.alt' />" border="0"/>
                            </c:if>
                            <c:if test="${managerEdit}">
                                <input type="image"  onclick="javascript:this.form.save.value='save'; this.parentNode.parentNode.target='_self';" src="<or:text key='btnSaveChanges.img' />" alt="<or:text key='btnSaveChanges.alt' />" border="0"/>&#160;
                            </c:if>
                        </div>
                    </s:form>

                </div>
            </div>

        <jsp:include page="/includes/inc_footer.jsp" />

    </div>

</div>

</body>
</html>
