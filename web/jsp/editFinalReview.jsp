<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="html" uri="/tags/struts-html" %>
<%@ taglib prefix="bean" uri="/tags/struts-bean" %>
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
</head>

<body>
	<jsp:include page="../includes/inc_header.jsp" />
	<table width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr valign="top">
			<!-- Left Column Begins -->
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

					&#160;<b><bean:message key="editReview.Submission" /></b> ${sid}
					(<tc-webtag:handle coderId="${submitterId}" context="component" />)<br />
					&#160;<b><bean:message key="editReview.MyRole" /></b> ${myRole}<br />
					<h3><bean:message key="editFinalReview.Scorecard.title" /></h3>

					<html:form action="/actions/SaveFinalReview">
						<html:hidden property="method" value="saveFinalReview" />
						<html:hidden property="rid" value="${review.id}" />

						<c:set var="itemIdx" value="0" />
						<c:set var="globalCommentIdx" value="0" />

						<c:forEach items="${scorecardTemplate.allGroups}" var="group" varStatus="groupStatus">
							<table cellpadding="0" cellspacing="0" border="0" width="100%" class="scorecard" style="border-collapse:collapse;">
								<tr>
									<td class="title" colspan="7">${group.name}</td>
								</tr>
								<c:forEach items="${group.allSections}" var="section" varStatus="sectionStatus">
									<tr>
										<td class="subheader" width="100%" colspan="7">${section.name}</td>
									</tr>
									<c:forEach items="${section.allQuestions}" var="question" varStatus="questionStatus">
										<tr class="light">
											<td class="value" colspan="7">
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
										</tr>
										<tr>
											<td class="header"><bean:message key="editReview.EditAggregation.Reviewer" /></td>
											<td class="headerC"><bean:message key="editReview.EditAggregation.CommentNumber" /></td>
											<td class="header"><bean:message key="editReview.EditAggregation.Response" /></td>
											<td class="headerC"><bean:message key="editReview.EditAggregation.Weight" /></td>
											<td class="header"><bean:message key="editReview.EditAggregation.Type" /></td>
											<td class="header"><bean:message key="editReview.EditAggregation.Fixed" /></td>
											<td class="header"><bean:message key="editReview.EditAggregation.NotFixed" /></td>
										</tr>

										<c:forEach items="${review.allItems}" var="item" varStatus="itemStatus">
											<c:set var="commentNum" value="1" />
											<c:set var="firstTime" value="${true}" />
											<c:if test="${item.question == question.id}">
												<c:forEach items="${item.allComments}" var="comment" varStatus="commentStatus">
													<c:set var="commentType" value="${comment.commentType.name}" />
													<c:choose>
														<c:when test='${(commentType == "Required") || (commentType == "Recommended") || (commentType == "Comment")}'>
															<c:set var="isReviewerComment" value="${true}" />
														</c:when>
														<c:otherwise>
															<c:set var="isReviewerComment" value="${false}" />
														</c:otherwise>
													</c:choose>
													<c:if test='${(isReviewerComment == true) || (commentType == "Manager Comment") ||
															(commentType == "Appeal") || (commentType == "Appeal Response") ||
															(commentType == "Aggregation Comment") || (commentType == "Aggregation Review Comment") ||
															(commentType == "Submitter Comment")}'>
														<tr class="dark">
															<td class="value" nowrap="nowrap">
																<c:if test="${commentStatus.index == 0}">
																	<c:forEach items="${reviewResources}" var="resource">
																		<c:if test="${resource.id == comment.author}">
																			<tc-webtag:handle coderId='${resource.allProperties["External Reference ID"]}' context="component" /><br />
																		</c:if>
																	</c:forEach>
																	<c:forEach items="${reviews}" var="subReview">
																		<c:if test="${subReview.author == comment.author}">
																			<html:link page="/actions/ViewReview.do?method=viewReview&rid=${subReview.id}"><bean:message key="editReview.EditAggregation.ViewReview" /></html:link>
																		</c:if>
																	</c:forEach>
																</c:if>
															</td>
															<c:if test="${isReviewerComment == true}">
																<td class="valueC">${commentNum}</td>
																<c:set var="commentNum" value="${commentNum + 1}" />
															</c:if>
															<c:if test="${isReviewerComment != true}">
																<td class="valueC"><!-- @ --></td>
															</c:if>
															<td class="value" width="43%">
																<c:choose>
																	<c:when test="${isReviewerComment == true}">
																		<b><bean:message key="editReview.EditAggregation.ReviewerResponse" /></b>
																	</c:when>
																	<c:when test='${commentType == "Manager Comment"}'>
																		<b><bean:message key="editReview.EditAggregation.ManagerComment" /></b>
																	</c:when>
																	<c:when test='${commentType == "Aggregation Comment"}'>
																		<b><bean:message key="editReview.EditAggregation.AggregatorResponse" /></b>
																	</c:when>
																	<c:when test='${commentType == "Aggregation Review Comment"}'>
																		<c:forEach items="${reviewResources}" var="resource">
																			<c:if test="${resource.id == comment.author}">
																				<b><bean:message key='ResourceRole.${fn:replace(resource.resourceRole.name, " ", "")}' />
<%--																				(<tc-webtag:handle coderId='${resource.allProperties["External Reference ID"]}' context="component" />) --%>
																				<bean:message key="viewAggregationReview.Response" /></b>
																			</c:if>
																		</c:forEach>
																	</c:when>
																	<c:when test='${commentType == "Submitter Comment"}'>
																		<b><bean:message key="editReview.EditAggregation.SubmitterComment" /></b>
																	</c:when>
																</c:choose>
																${comment.comment}
																<c:if test="${commentStatus.index == lastCommentIdxs[itemStatus.index]}">
																	<div style="padding-top:4px;">
																		<b><bean:message key="editReview.EditAggregation.ResponseText" /></b><br />
																		<div id="Response${globalCommentIdx}a" style="display:hide;padding-top:4px;">
																			<b><bean:message key="editReview.EditAggregation.ResponseText.no_colon" /></b>
																			<bean:message key="global.optional.paren" /><b>:</b><br />
																		</div>
																		<div id="Response${globalCommentIdx}b" style="display:none;padding-top:4px;">
																			<b><bean:message key="editReview.EditAggregation.ResponseText.no_colon" />
																			<font color="#CC0000">(required)<bean:message key="global.optional.paren" /></font>:</b><br />
																		</div>
																		<html:textarea rows="2" property="final_comment[${globalCommentIdx}]" cols="20" styleClass="inputTextBox" />
																	</div>
																	<c:set var="globalResponseIdx" value="${globalResponseIdx + 1}" />
																</c:if>
															</td>
															<c:if test="${firstTime == true}">
																<td class="valueC" width="42%">${question.weight}</td>
																<c:set var="firstTime" value="${false}" />
															</c:if>
															<c:if test="${firstTime != true}">
																<td class="valueC" width="42%"><!-- @ --></td>
															</c:if>
															<c:if test="${isReviewerComment == true}">
																<td class="value"><bean:message key='CommentType.${fn:replace(commentType, " ", "")}' /></td>
																<td class="valueC">
																	<html:radio property="fix_status[${globalCommentIdx}]" value="Fixed"
																		onClick='javascript:swapLayer("Response${globalCommentIdx}b", "Response${globalCommentIdx}a");' /></td>
																<td class="valueC">
																	<html:radio property="fix_status[${globalCommentIdx}]" value="Not Fixed"
																		onClick='javascript:swapLayer("Response${globalCommentIdx}a", "Response${globalCommentIdx}b");' /></td>
															</c:if>
															<c:if test="${isReviewerComment != true}">
																<td class="value"><!-- @ --></td>
																<td class="value"><!-- @ --></td>
																<td class="value"><!-- @ --></td>
															</c:if>
														</tr>
														<c:set var="globalCommentIdx" value="${globalCommentIdx + 1}" />
													</c:if>
												</c:forEach>
											</c:if>
										</c:forEach>
									</c:forEach>
								</c:forEach>
								<tr>
									<td class="lastRowTD" colspan="7"><!-- @ --></td>
								</tr>
							</table><br />
						</c:forEach>

						<table class="scorecard" cellpadding="0" cellspacing="0" width="100%" style="border-collapse:collapse;">
							<tr>
								<td class="title">Approval</td>
							</tr>
							<tr class="highlighted">
								<td class="value">
									<html:checkbox property="approve_fixes" />
									<b><bean:message key="editFinalReview.ApproveFinalFixes" /></b></td>
							</tr>
							<tr>
								<td class="lastRowTD"><!-- @ --></td>
							</tr>
						</table><br />

						<div align="right">
							<html:hidden property="save" value="" />
							<html:image onclick="javascript:this.form.save.value='submit';" srcKey="editReview.Button.SaveAndCommit.img" altKey="editReview.Button.SaveAndCommit.alt" border="0" />&#160;
							<html:image onclick="javascript:this.form.save.value='save';" srcKey="editReview.Button.SaveForLater.img" altKey="editReview.Button.SaveForLater.alt" border="0" />&#160;
							<html:image onclick="javascript:this.form.save.value='preview';" srcKey="editReview.Button.Preview.img" altKey="editReview.Button.Preview.alt" border="0" />
						</div>
					</html:form>
				</div>
				<br /><br />
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