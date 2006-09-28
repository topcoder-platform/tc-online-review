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
	<script language="JavaScript" type="text/javascript">

		// create the request object
		function createXMLHttpRequest() {
			var xmlHttp;
			if (window.ActiveXObject) {
				xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
			} else if (window.XMLHttpRequest) {
				xmlHttp = new XMLHttpRequest();
			}
			return xmlHttp;
		}

		/**
		 * TODO: Document it
		 */
		function placeAppeal(itemIdx, itemId, reviewId) {
			// Find appeal text input node
			appealTextNode = document.getElementsByName("appeal_text[" + itemIdx + "]");
			// Get appeal text
			var appealText = appealTextNode.value;


			// create the Ajax request
			var myRequest = createXMLHttpRequest();

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
				'<parameter name="AppealText">' +
				appealText +
				"</parameter>" +
				"</parameters>" +
				"</request>";
			// set the callback function
			// TODO: Check for errors no handled by Ajax Support
			myRequest.onReadyStateChange = function() {
				if (myRequest.readyState == 4 && myRequest.status == 200) {
					// the response is ready
					var respXML = myRequest.responseXML;
					// retrieve the result
					var result = respXML.getElementsByTagName("result")[0].getAttribute("status");
					if (result == "Success") {
						// operation succeeded
						// TODO: Some changes to here
						toggleDisplay("appealText_${itemIdx}");
						toggleDisplay("placeAppeal_${itemIdx}");
					} else {
						// operation failed, alert the error message to the user
						alert("An error occured while placing the appeal: " + result);
					}
				}
			};

			// send the request
			myRequest.open("POST", "<html:rewrite page='/ajaxSupport' />", true);
			myRequest.setRequestHeader("Content-Type", "text/xml");
			myRequest.send(content);
		}
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

					<c:set var="itemIdx" value="0" />
					<c:forEach items="${scorecardTemplate.allGroups}" var="group" varStatus="groupStatus">
						<table class="scorecard" cellpadding="0" width="100%" style="border-collapse: collapse;" id="table2">
							<tr>
								<td class="title" colspan="${canPlaceAppeal ? 5 : (canPlaceAppealResponse ? 4 : 3)}">${orfn:htmlEncode(group.name)}</td>
							</tr>
							<c:forEach items="${group.allSections}" var="section" varStatus="sectionStatus">
								<tr>
									<td class="subheader" width="100%">${orfn:htmlEncode(section.name)}</td>
									<td class="subheader" align="center" width="49%"><bean:message key="editReview.SectionHeader.Weight" /></td>
									<td class="subheader" align="center" width="1%"><bean:message key="editReview.SectionHeader.Response" /></td>
									<c:if test="${canPlaceAppeal or canPlaceAppealResponse}">
										<td class="subheader" align="center" width="1%"><bean:message key="editReview.SectionHeader.AppealStatus" /></td>
									</c:if>
									<c:if test="${canPlaceAppeal}">
										<td class="subheader" align="center" width="1%"><bean:message key="editReview.SectionHeader.Appeal" /></td>
									</c:if>
								</tr>
								<c:forEach items="${section.allQuestions}" var="question" varStatus="questionStatus">
									<c:set var="item" value="${review.allItems[itemIdx]}" />

									<tr class="light">
										<%@ include file="../includes/review/review_question.jsp" %>
										<%@ include file="../includes/review/review_static_answer.jsp" %>
										<c:if test="${canPlaceAppeal or canPlaceAppealResponse}">
											<td class="valueC"><%-- TODO: Appeal status should go here --%><!-- @ --></td>
										</c:if>
										<c:if test="${canPlaceAppeal}">
											<td class="valueC">
												<html:link href="javascript:toggleDisplay('appealText_${itemIdx}');toggleDisplay('placeAppeal_${itemIdx}');">
													<html:img styleId="placeAppeal_${itemIdx}" styleClass="showText" srcKey="editReview.Button.Appeal.img" altKey="editReview.Button.Appeal.alt" />
												</html:link>
											</td>
										</c:if>
									</tr>
									<%@ include file="../includes/review/review_comments.jsp" %>
									<c:if test="${canPlaceAppeal}">
										<tr class="highlighted">
											<td class="value" colspan="6">
												<div id="appealText_${itemIdx}" class="hideText">
													<b><bean:message key="editReview.Question.AppealText.title"/>:</b>
													<br/>
													<textarea name="appeal_text[${itemIdx}]" rows="2" cols="20" style="font-size: 10px; font-family: sans-serif;width:99%;height:50px;border:1px solid #ccc;margin:3px;"></textarea>
													<br/>
													<html:link href="javascript:placeAppeal(${itemIdx}, ${item.id}, ${review.id});">
														<html:img srcKey="editReview.Button.SubmitAppeal.img" altKey="editReview.Button.SubmitAppeal.alt" border="0" hspace="5" vspace="9" />
													</html:link>
													<br/>
												</div>
											</td>
										</tr>
									</c:if>

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
										<td class="valueC" nowrap="nowrap"><b>${orfn:htmlEncode(review.score)}</b></td>
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
						<a href="javascript:history.go(-1)"><html:img srcKey="btnBack.img" altKey="btnBack.alt" border="0" /></a>
						<br />
					</div><br />
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
