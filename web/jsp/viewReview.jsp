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
	<link type="text/css" rel="stylesheet" href="../css/new_styles.css" />
	<script language="JavaScript" type="text/javascript" src="../scripts/rollovers.js"><!-- @ --></script>
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

					&#160;<b><bean:message key="editReview.Screener" /></b> <a href="#" class="coderTextRed">ScreenerHandle</a><br />
					&#160;<b><bean:message key="editReview.Submission" /></b> ${sid}<b><br />
					&#160;<b><bean:message key="editReview.MyRole" /></b> ${myRole}<br />
					<h3>${scorecardTemplate.name}<br /></h3>

					<c:set var="itemIdx" value="0" />
					<c:forEach items="${scorecardTemplate.allGroups}" var="group" varStatus="groupStatus">
						<table class="scorecard" cellpadding="0" width="100%" style="border-collapse: collapse;" id="table2">
							<tr>
	        					<td class="title" colspan="3">${group.name}</td>
							</tr>
							<c:forEach items="${group.allSections}" var="section" varStatus="sectionStatus">
								<tr>
									<td class="subheader" width="100%">${section.name}</td>
									<td class="subheader" align="center" width="49%"><bean:message key="editReview.SectionHeader.Weight" /></td>
									<td class="subheader" align="center" width="1%"><bean:message key="editReview.SectionHeader.Response" /></td>
								</tr>
								<c:forEach items="${section.allQuestions}" var="question" varStatus="questionStatus">
									<c:set var="item" value="${review.allItems[itemIdx]}" />
									<tr class="light">
										<td class="value">
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
												<c:when test="${empty item.answer}"><!-- @ --></c:when>
												<c:when test='${question.questionType.name == "Yes/No"}'>
													<c:if test='${item.answer != "1"}'>
														<bean:message key="global.answer.No" />
													</c:if>
													<c:if test='${item.answer == "1"}'>
														<bean:message key="global.answer.Yes" />
													</c:if>
												</c:when>
												<c:when test='${question.questionType.name == "Scale (1-4)"}'>
													<c:if test='${!(empty fn:replace(item.answer, "/4", ""))}'>
														<bean:message key='Answer.Score4.ans${fn:replace(item.answer, "/4", "")}'/>
													</c:if>
												</c:when>
												<c:when test='${question.questionType.name == "Scale (1-10)"}'>
													<bean:message key="Answer.Score10.Rating.title" /> ${fn:replace(item.answer, "/10", "")}
												</c:when>
												<c:when test='${question.questionType.name == "Test Case"}'>
													${fn:replace(item.answer, "/", wordOf)}
												</c:when>
												<c:otherwise>
													${item.answer}
												</c:otherwise>
											</c:choose>
										</td>
									</tr>

									<c:forEach items="${item.allComments}" var="comment" varStatus="commentStatus">
										<tr class="dark">
											<td class="value" width="100%">
												<b><bean:message key="editReview.Question.Response.title" />
													${commentStatus.index + 1}:
													${comment.commentType.name}</b>&#160;
													${fn:escapeXml(comment.comment)}
											</td>
											<td class="value"><!-- @ --></td>
											<td class="value"><!-- @ --></td>
										</tr>
									</c:forEach>

									<c:set var="itemIdx" value="${itemIdx + 1}" />
								</c:forEach>
							</c:forEach>
							<c:if test="${groupStatus.index == scorecardTemplate.numberOfGroups - 1}">
								<c:if test="${!(empty review.score)}">
									<tr>
										<td class="header"><!-- @ --></td>
										<td class="headerC"><bean:message key="editReview.SectionHeader.Total" /></td>
										<td class="headerC"><!-- @ --></td>
									</tr>
									<tr>
										<td class="value"><!-- @ --></td>
										<td class="valueC" nowrap="nowrap"><b>${review.score}</b></td>
										<td class="valueC"><!-- @ --></td>
									</tr>
								</c:if>
								<tr>
									<td class="lastRowTD" colspan="3"><!-- @ --></td>
								</tr>
							</c:if>
						</table>
					</c:forEach><br />

					<div align="right">
						<a href="javascript:history.go(-1)"><html:img srcKey="btnBack.img" altKey="btnBack.alt" border="0" /></a><br />
					</div>
					<br />

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
