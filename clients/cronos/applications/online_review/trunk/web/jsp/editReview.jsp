<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="html" uri="/tags/struts-html" %>
<%@ taglib prefix="bean" uri="/tags/struts-bean" %>
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
	<link type="text/css" rel="stylesheet" href="../css/new_styles.css">
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
					<div style="padding: 11px 6px 9px 0px;">
						<table border="0" cellpadding="0" cellspacing="0" width="100%" id="table1">
							<tr>
								<td>
									<table cellspacing="0" cellpadding="0" border="0">
										<tr valign="middle">
											<td><img src="../i/${categoryIconName}" border="0" /></td>
											<td><img src="../i/${rootCatalogIcon}" alt="${rootCatalogName}" border="0" /></td>
											<td>
												<span class="bodyTitle">${project.allProperties["Project Name"]}</span>
												<c:if test='${!(empty project.allProperties["Project Version"])}'>
													<font size="4"><bean:message key="global.version" />
														${project.allProperties["Project Version"]}</font>
												</c:if>
											</td>
										</tr>
									</table>
								</td>
								<td align="right" valign="top">
									<a href="javascript:showAll();"><bean:message key="global.expandAll" /></a>&#160;|
									<a href="javascript:hideAll();"><bean:message key="global.collapseAll" /></a>
								</td>
							</tr>
						</table>
					</div>

					&nbsp;<b><bean:message key="editReview.Submission" /></b> ${sid}<br />
					&nbsp;<b><bean:message key="editReview.MyRole" /></b> ${myRole}<br />
					<h3>${scorecardTemplate.name}<br /></h3>

					<html:form action="/actions/Save${reviewType}">

						<c:set var="itemIdx" value="0" />
						<c:if test="${!(empty review)}">
							<html:hidden property="rid" value="${review.id}" />
						</c:if>
						<c:if test="${empty review}">
							<html:hidden property="sid" value="${sid}" />
						</c:if>
						<html:hidden property="method" value="save${reviewType}" />

						<c:forEach items="${scorecardTemplate.allGroups}" var="group" varStatus="groupStatus">
							<table class="scorecard" cellpadding="0" width="100%" style="border-collapse: collapse;">
								<tr>
									<td class="title" colspan="3">${group.name}</td>
								</tr>
								<c:forEach items="${group.allSections}" var="section" varStatus="sectionStatus">
									<tr>
										<td class="subheader" width="100%">${section.name}</td>
										<td class="subheader" width="49%" align="center"><bean:message key="editReview.SectionHeader.Weight"/></td>
										<td class="subheader" width="1%" align="center"><bean:message key="editReview.SectionHeader.Response"/></td>
									</tr>
									<c:forEach items="${section.allQuestions}" var="question" varStatus="questionStatus">
										<tr class="light">
											<td class="value" width="100%">

											<div class="showText" id="shortQ_${itemIdx}">
												<a href="javascript:toggleDisplay('shortQ_${itemIdx}');toggleDisplay('longQ_${itemIdx}');" class="statLink"><html:img src="../i/plus.gif" altKey="global.plus.alt" border="0" /></a>
												<b><bean:message key="editReview.Question.title" /> ${groupStatus.index + 1}.${sectionStatus.index + 1}.${questionStatus.index + 1}</b>
												${question.description}
											</div>
											<div class="hideText" id="longQ_${itemIdx}">
												<a href="javascript:toggleDisplay('shortQ_${itemIdx}');toggleDisplay('longQ_${itemIdx}');" class="statLink"><html:img src="../i/minus.gif" altKey="global.minus.alt" border="0" /></a>
												<b><bean:message key="editReview.Question.title" /> ${groupStatus.index + 1}.${sectionStatus.index + 1}.${questionStatus.index + 1}</b>
												${question.description}<br />
												${question.guideline}
											</div>
											</td>
											<td class="valueC">${question.weight}</td>
											<td class="valueC" nowrap="nowrap">
												<c:choose>
													<c:when test="${question.questionType.name eq 'Yes/No'}">
														<html:select property="answer[${itemIdx}]" styleClass="inputBox">
															<html:option value=""><bean:message key="Answer.Select" /></html:option>
															<html:option value="1"><bean:message key="global.answer.Yes" /></html:option>
															<html:option value="0"><bean:message key="global.answer.No" /></html:option>
														</html:select>
													</c:when>
													<c:when test="${question.questionType.name eq 'Scale (1-4)'}">
														<html:select property="answer[${itemIdx}]" styleClass="inputBox">
															<html:option value=""><bean:message key="Answer.Select" /></html:option>
															<html:option value="1/4"><bean:message key="Answer.Score4.ans1" /></html:option>
															<html:option value="2/4"><bean:message key="Answer.Score4.ans2" /></html:option>
															<html:option value="3/4"><bean:message key="Answer.Score4.ans3" /></html:option>
															<html:option value="4/4"><bean:message key="Answer.Score4.ans4" /></html:option>
														</html:select>
													</c:when>
													<c:when test="${question.questionType.name eq 'Scale (1-10)'}">
														<html:select property="answer[${itemIdx}]" styleClass="inputBox">
															<html:option value=""><bean:message key="Answer.Select" /></html:option>
															<c:forEach var="rating" begin="1" end="10">
																<html:option value="${rating}/10"><bean:message key="Answer.Score10.Rating.title" /> ${rating}</html:option>
															</c:forEach>
														</html:select>
													</c:when>
													<c:when test="${question.questionType.name eq 'Test Case'}">
														<html:text property="passed_tests" value="" styleClass="inputBox" style="width:25;" onchange="populateTestCaseAnswer(${itemIdx});"/>
														<bean:message key="editReview.Question.Response.TestCase.of" />
														<html:text property="all_tests" value="" styleClass="inputBox" style="width:25;" onchange="populateTestCaseAnswer(${itemIdx});"/>
														<html:hidden property="answer[${itemIdx}]" />
													</c:when>
												</c:choose>
											</td>
										</tr>
										<tr class="highlighted">
											<td class="value" colspan="3">
												<b><bean:message key="editReview.Question.Response.title"/> 1:</b>
												<html:select property="commentType[${itemIdx}]" styleClass="inputBox">
													<c:forEach items="${allCommentTypes}" var="commentType" >
														<html:option value="${commentType.id}">${commentType.name}</html:option>
													</c:forEach>
												</html:select>
												<html:textarea rows="2" property="comment[${itemIdx}]" cols="20" styleClass="inputTextBox" />
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
							<html:image onclick="javascript:this.form.save.value='submit';" srcKey="editReview.Button.SaveAndCommit.img" altKey="editReview.Button.SaveAndCommit.alt" border="0" />&#160;
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
