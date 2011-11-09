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
        <jsp:param name="thirdLevelPageKey" value="editReview.title.${reviewType}" />
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
    <script language="JavaScript" type="text/javascript" src="<html:rewrite href='/js/or/util.js' />"><!-- @ --></script>

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
   
    // -->
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
                    <h3>${orfn:htmlEncode(scorecardTemplate.name)}</h3>

                    <html:form action="/actions/Save${reviewType}" method="POST" enctype="multipart/form-data">
                        <html:hidden property="method" value="save${reviewType}" />
                        <c:choose>
                            <c:when test="${review.id > -1}">
                                <html:hidden property="rid" value="${review.id}" />
                            </c:when>
                            <c:when test="${not empty requestScope.pid}">
                                <html:hidden property="pid" value="${requestScope.pid}" />
                            </c:when>
                            <c:otherwise>
                                <html:hidden property="sid" value="${sid}" />
                            </c:otherwise>
                        </c:choose>

                        <c:if test="${orfn:isErrorsPresent(pageContext.request)}">
                            <table cellpadding="0" cellspacing="0" border="0">
                                <tr><td class="errorText"><bean:message key="Error.saveReview.ValidationFailed" /></td></tr>
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
                                        <td class="subheader" width="49%" align="center"><bean:message key="editReview.SectionHeader.Weight" /></td>
                                        <td class="subheader" width="1%" align="center"><bean:message key="editReview.SectionHeader.Response" /></td>
                                    </tr>
                                    <c:forEach items="${section.allQuestions}" var="question" varStatus="questionStatus">
                                        <c:set var="item" value="${review.allItems[itemIdx]}" />
                                        <tr class="light">
                                            <%@ include file="../includes/review/review_question.jsp" %>
                                            <%@ include file="../includes/review/review_static_answer.jsp" %>
                                        </tr>
                                        <%@ include file="../includes/review/review_comments.jsp" %>
                                        <tr class="highlighted">
                                            <td class="value" colspan="2">
                                                <html:hidden property="comment_count[${itemIdx}]" />
                                                <c:forEach var="commentIdx" begin="0" end="${reviewForm.map['comment_count'][itemIdx]}">
                                                	<!-- Show only Manager comments as editable for manager edit  -->
                                                	<c:set var="commentkey" value="${itemIdx}.${commentIdx}"></c:set>
                                                	<c:set var="commentType" value="${reviewForm.map['comment_type'][commentkey]}"></c:set>
                                                	<c:if test="${managerEdit}">
                                                		<c:if test="${commentType eq 11}">
	                                                		<div name="response" style="${commentIdx eq 0 ? 'display: none;' : ''}">
	                                                        	<b><bean:message key="editReview.Question.ManagerComment.title"/>:</b>
	                                                        	<span class="error"><html:errors property="comment(${itemIdx}.${commentIdx})" prefix="" suffix="" /></span>
	                                                        	<html:textarea rows="2" property="comment(${itemIdx}.${commentIdx})" cols="20" styleClass="inputTextBox" />
	                                                        </div>
                                                        </c:if>
                                                    
                                                	</c:if>
                                                	<!-- Show only Evaluator comments as editable for evaluator edit  -->
                                                	<c:if test="${not managerEdit}">
                                                		<c:if test="${commentType eq 15}" >
	                                                		<div name="response" style="${commentIdx eq 0 ? 'display: none;' : ''}">
	                                                        	<b><bean:message key="editReviewEvaluation.Question.EvaluatorComment.title"/>:</b>
	                                                        	<span class="error"><html:errors property="comment(${itemIdx}.${commentIdx})" prefix="" suffix="" /></span>
	                                                        	<html:textarea rows="2" property="comment(${itemIdx}.${commentIdx})" cols="20" styleClass="inputTextBox" />
	                                                        </div>
                                                        </c:if>
                                                	</c:if>
                                                    
                                                </c:forEach>
                                                <html:img srcKey="editReview.Button.AddResponse.img" altKey="editReview.Button.AddResponse.alt"
                                                    onclick="addReviewResponse(${itemIdx}, this.parentNode);" style="cursor:hand;" /><br />
                                                <br/>
                                            </td>
											<td class="valueC" nowrap="nowrap">
                                                    <%@ include file="../includes/review/review_answer.jsp" %>
                                            		<div class="error"><html:errors property="answer[${itemIdx}]" prefix="" suffix="" /></div>
                                            </td>
                                            
                                        </tr>

                                        <c:set var="itemIdx" value="${itemIdx + 1}" />
                                    </c:forEach>
                                </c:forEach>
                                <tr>
                                    <td class="lastRowTD" colspan="3"><!-- @ --></td>
                                </tr>
                            </table><br />
                        </c:forEach>

                        <div align="right">
                            <html:hidden property="save" value="" />
                            <c:if test="${not managerEdit}">
                                <html:image onclick="javascript:this.form.save.value='submit'; this.parentNode.parentNode.target='_self';" srcKey="editReview.Button.SaveAndCommit.img" altKey="editReview.Button.SaveAndCommit.alt" border="0"/>&#160;
                                <html:image onclick="javascript:this.form.save.value='save'; this.parentNode.parentNode.target='_self';" srcKey="editReview.Button.SaveForLater.img" altKey="editReview.Button.SaveForLater.alt" border="0"/>&#160;
                            </c:if>
                            <c:if test="${managerEdit}">
                                <html:image onclick="javascript:this.form.save.value='save'; this.parentNode.parentNode.target='_self';" srcKey="btnSaveChanges.img" altKey="btnSaveChanges.alt" border="0"/>&#160;
                            </c:if>
                            <html:image onclick="javascript:this.form.save.value='preview'; this.parentNode.parentNode.target='_blank';" srcKey="editReview.Button.Preview.img" altKey="editReview.Button.Preview.alt" border="0"/>
                        </div>
                    </html:form>

                </div>
            </div>
        
        <jsp:include page="/includes/inc_footer.jsp" />

    </div>

</div>

</body>
</html:html>
