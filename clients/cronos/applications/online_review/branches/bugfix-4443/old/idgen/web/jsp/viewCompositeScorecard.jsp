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
	<link type="text/css" rel="stylesheet" href="<html:rewrite page='/css/style.css' />" />
	<link type="text/css" rel="stylesheet" href="<html:rewrite page='/css/coders.css' />" />
	<link type="text/css" rel="stylesheet" href="<html:rewrite page='/css/stats.css' />" />
	<link type="text/css" rel="stylesheet" href="<html:rewrite page='/css/tcStyles.css' />" />

	<!-- CSS and JS by Petar -->
	<link type="text/css" rel="stylesheet" href="<html:rewrite page='/css/new_styles.css' />" />
	<script language="JavaScript" type="text/javascript"
		src="<html:rewrite page='/scripts/rollovers.js' />"><!-- @ --></script>
</head>

<body>
	<jsp:include page="/includes/inc_header.jsp" />
	<table width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr valign="top">
			<!-- Left Column Begins -->
			<td width="180">
				<jsp:include page="/includes/inc_leftnav.jsp" />
			</td>
			<!-- Left Column Ends -->

			<!-- Gutter Begins -->
			<td width="15"><html:img page="/i/clear.gif" width="15" height="1" border="0" /></td>
			<!-- Gutter Ends -->

			<!-- Center Column Begins -->
			<td class="bodyText">
				<jsp:include page="/includes/project/project_tabs.jsp" />

				<div id="mainMiddleContent">
					<jsp:include page="/includes/review/review_project.jsp" />
					
					<h3><bean:message key="editReview.CompositeScorecard.title" /><br /></h3>

					<c:set var="itemIdx" value="0" />
					<c:forEach items="${scorecardTemplate.allGroups}" var="group" varStatus="groupStatus">
						<table class="scorecard" cellpadding="0" width="100%" style="border-collapse: collapse;" id="table1">
							<tr>
								<td class="title" colspan="6">${orfn:htmlEncode(group.name)}</td>
							</tr>
							<c:forEach items="${group.allSections}" var="section" varStatus="sectionStatus">
								<tr>
									<td class="subheader" width="100%">${orfn:htmlEncode(section.name)}</td>
									<td class="subheader" align="center" width="49%"><bean:message key="editReview.SectionHeader.Weight" /></td>
									<td class="subheader" align="center"><b><bean:message key="editReview.SectionHeader.Average" /></b></td>
									<c:forEach items="${reviews}" var="review" varStatus="reviewStatus">
										<td class="subheader" align="center" nowrap="nowrap">
											<b><a href="ViewReview.do?method=viewReview&rid=${review.id}"><bean:message key="editReview.SectionHeader.Review" /></a><br /></b>
											(<tc-webtag:handle coderId="${authors[reviewStatus.index]}" context="component" />)
										</td>
									</c:forEach>
								</tr>
								<c:forEach items="${section.allQuestions}" var="question" varStatus="questionStatus">
									<c:set var="item" value="${review.allItems[itemIdx]}" />
									<tr class="light">
										<jsp:include page="/includes/review/review_question.jsp" />
										
										<td class="valueC" nowrap="nowrap">${avgScores[itemIdx]}</td>
										<c:forEach items="${reviews}" var="review" varStatus="reviewStatus">
											<td class="valueC" nowrap="nowrap">${scores[reviewStatus.index][itemIdx]}</td>
										</c:forEach>
									</tr>
									<tr class="dark">
										<td class="value" align="left">
											<div class="showText" id="shortQ_resp_${itemIdx}" style="width: 100%;">
												<a href="javascript:toggleDisplay('shortQ_resp_${itemIdx}');toggleDisplay('longQ_resp_${itemIdx}');" class="statLink"><html:img page="/i/plus.gif" alt="open" border="0" /></a>
												<b><bean:message key="editReview.Question.Response.plural" /></b>
											</div>
											<div class="hideText" id="longQ_resp_${itemIdx}">
												<a href="javascript:toggleDisplay('shortQ_resp_${itemIdx}');toggleDisplay('longQ_resp_${itemIdx}');" class="statLink"><html:img page="/i/minus.gif" alt="close" border="0" /></a>
												<c:forEach items="${reviews}" var="review" varStatus="reviewStatus">
												  	<c:set var="item" value="${review.allItems[itemIdx]}" />
												  	<c:set var="commentNum" value="1" />
													<c:forEach items="${item.allComments}" var="comment">
														<c:if test="${(comment.commentType.name eq 'Required') or (comment.commentType.name eq 'Recommended') or (comment.commentType.name eq 'Comment')}">
															<b><bean:message key="editReview.Question.Response.title" />
																${commentNum}:
																<bean:message key='CommentType.${fn:replace(comment.commentType.name, " ", "")}' />
															</b>
														</c:if>
														(<tc-webtag:handle coderId="${authors[reviewStatus.index]}" context="component" />) <br>
														${orfn:htmlEncode(comment.comment)}<br>
														<%-- TODO: Complete handling of documents--%>
														<b>Document </b>(required)<b>: </b><a href="#">test.doc</a><br /><br />
													</c:forEach>
												</c:forEach>
											</div>
										</td>
										<td class="value" align="left">&nbsp;</td>
										<c:forEach begin="0" end="${fn:length(reviews)}">
											<td class="valueC" nowrap="nowrap"><!-- @ --></td>
										</c:forEach>
									</tr>

									<c:set var="itemIdx" value="${itemIdx + 1}" />
								</c:forEach>
								<c:if test="${groupStatus.index eq (fn:length(scorecardTemplate.allGroups) - 1)}">
									<tr>
										<td class="value" align="left" colspan="2" > </td>
										<td class="valueC" nowrap="nowrap"><b>${avgScore}</b></td>
										<c:forEach items="${reviews}" var="review">
											<td class="valueC" nowrap="nowrap"><b>${orfn:htmlEncode(review.score)}</b></td>
										</c:forEach>
									</tr>
									<tr>
										<td class="lastRowTD" colspan="6"><!-- @ --></td>
									</tr>
								</c:if>
							</c:forEach>
						</table>
					</c:forEach>

					<div align="right">
						<a href="javascript:history.go(-1)"><html:img srcKey="btnBack.img" altKey="btnBack.alt" border="0" /></a>
					</div><br /><br /><br />
				</div>
				<p><br /></p>
			</td>
			<!-- Center Column Ends -->

			<!-- Gutter -->
			<td width="15"><html:img page="/i/clear.gif" width="25" height="1" border="0" /></td>
			<!-- Gutter Ends -->

			<!-- Gutter -->
			<td width="2"><html:img page="/i/clear.gif" width="2" height="1" border="0" /></td>
			<!-- Gutter Ends -->
		</tr>
	</table>

	<jsp:include page="/includes/inc_footer.jsp" />
</body>
</html:html>
