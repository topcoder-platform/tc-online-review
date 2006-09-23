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
	<title><bean:message key="OnlineReviewApp.title" /></title>

	<!-- TopCoder CSS -->
	<link type="text/css" rel="stylesheet" href="../css/style.css" />
	<link type="text/css" rel="stylesheet" href="../css/coders.css" />
	<link type="text/css" rel="stylesheet" href="../css/stats.css" />
	<link type="text/css" rel="stylesheet" href="../css/tcStyles.css" />

	<!-- CSS and JS by Petar -->
	<link type="text/css" rel="stylesheet" href="../css/new_styles.css" />
	<script language="JavaScript" type="text/javascript" src="../scripts/rollovers.js"><!-- @ --></script>
	<script language="JavaScript" type="text/javascript" src="../scripts/dojo.js"><!-- @ --></script>
	<script language="JavaScript" type="text/javascript" src="../scripts/util.js"><!-- @ --></script>

	<script language="javascript" type="text/javascript">
	<!--

	/*
	 * This function sets the value of the "answer" input with the specified index,
	 * to the value composed from values of appropriate "passed_tests" and "all_tests" inputs.
	 */
	function populateTestCaseAnswer(itemIdx) {
		var answerNode = document.getElementsByName("answer[" + itemIdx + "]")[0];
		var passedNode = getChildByName(answerNode.parentNode, "passed_tests");
		var allNode = getChildByName(answerNode.parentNode, "all_tests");
		answerNode.value = passedNode.value + "/" + allNode.value;
	}

	// The "passed_tests" and "all_tests" inputs should be populated
	// by the values taken from appropriate hidden "answer" input
	dojo.addOnLoad(function () {
		var passedTestsNodes = document.getElementsByName("passed_tests");
		for (var i = 0; i < passedTestsNodes.length; i++) {
			var allTestsNode = getChildByName(passedTestsNodes[i].parentNode, "all_tests");
			var answerNode = getChildByNamePrefix(passedTestsNodes[i].parentNode, "answer[");
			var parts = answerNode.value.split("/");
			if (parts && parts.length == 2) {
				passedTestsNodes[i].value = parts[0];
				allTestsNode.value = parts[1];
			}
		}
	});

	// -->
	</script>
</head>

<body>
	<jsp:include page="../includes/inc_header.jsp" />
	<table width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr valign="top">
			<!-- Left Column Begins-->
			<td width="180">
				<jsp:include page="../includes/inc_leftnav.jsp" />
			</td>
			<!-- Left Column Ends -->

			<!-- Gutter Begins -->
			<td width="15"><img src="../i/clear.gif" width="15" height="1" border="0" /></td>
			<!-- Gutter Ends -->

			<!-- Center Column Begins -->
			<td class="bodyText">
				<jsp:include page="../includes/project/project_tabs.jsp" />

				<div id="mainMiddleContent">
					<jsp:include page="../includes/review/review_project.jsp" />

					<h3>${orfn:htmlEncode(scorecardTemplate.name)}</h3>

					<html:form action="/actions/Save${reviewType}">
						<html:hidden property="method" value="save${reviewType}" />
						<c:if test="${!(empty review)}">
							<html:hidden property="rid" value="${review.id}" />
						</c:if>
						<c:if test="${empty review}">
							<html:hidden property="sid" value="${sid}" />
						</c:if>

						<c:set var="itemIdx" value="0" />

						<c:forEach items="${scorecardTemplate.allGroups}" var="group" varStatus="groupStatus">
							<table cellpadding="0" cellspacing="0" width="100%" class="scorecard" style="border-collapse:collapse;">
								<tr>
									<td class="title" colspan="3">${orfn:htmlEncode(group.name)}</td>
								</tr>
								<c:forEach items="${group.allSections}" var="section" varStatus="sectionStatus">
									<tr>
										<td class="subheader" width="100%">${orfn:htmlEncode(section.name)}</td>
										<td class="subheader" width="49%" align="center"><bean:message key="editReview.SectionHeader.Weight" /></td>
										<td class="subheader" width="1%" align="center"><bean:message key="editReview.SectionHeader.Response" /></td>
									</tr>
									<c:forEach items="${section.allQuestions}" var="question" varStatus="questionStatus">
										<c:if test="${managerEdit}">
											<c:set var="item" value="${review.allItems[itemIdx]}" />									
										</c:if>
										
										<tr class="light">
											<%@ include file="../includes/review/review_question.jsp" %>	
											<c:if test="${not managerEdit}">																					
												<%@ include file="../includes/review/review_answer.jsp" %>
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
												<c:if test="${not managerEdit}">
													<b><bean:message key="editReview.Question.Response.title"/> 1:</b>
													<html:select property="commentType[${itemIdx}]" styleClass="inputBox">
														<c:forEach items="${allCommentTypes}" var="commentType" >
															<html:option value="${commentType.id}">${commentType.name}</html:option>
														</c:forEach>
													</html:select>
												</c:if>
												<c:if test="${managerEdit}">
													<b><bean:message key="editReview.Question.ManagerComment.title"/>:</b>
												</c:if>		
												<html:textarea rows="2" property="comment[${itemIdx}]" cols="20" styleClass="inputTextBox" />			
											</td>
											<c:if test="${managerEdit}">																					
												<%@ include file="../includes/review/review_answer.jsp" %>
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

						<div align="right">
							<html:hidden property="save" value="" />		
							<c:if test="${not managerEdit}">
								<html:image onclick="javascript:this.form.save.value='submit';" srcKey="editReview.Button.SaveAndCommit.img" altKey="editReview.Button.SaveAndCommit.alt" border="0" />&#160;
							</c:if>
							<html:image onclick="javascript:this.form.save.value='save';" srcKey="editReview.Button.SaveForLater.img" altKey="editReview.Button.SaveForLater.alt" border="0" />&#160;
							<html:image onclick="javascript:this.form.save.value='preview';" srcKey="editReview.Button.Preview.img" altKey="editReview.Button.Preview.alt" border="0" />
						</div>
					</html:form>
				</div>
				<p><br /></p>
			</td>
			<!-- Center Column Ends -->

			<!-- Gutter -->
			<td width="15"><img src="../i/clear.gif" width="25" height="1" border="0" /></td>
			<!-- Gutter Ends -->

			<!-- Gutter -->
			<td width="2"><img src="../i/clear.gif" width="2" height="1" border="0" /></td>
			<!-- Gutter Ends -->
		</tr>
	</table>

	<jsp:include page="../includes/inc_footer.jsp" />
</body>
</html:html>
