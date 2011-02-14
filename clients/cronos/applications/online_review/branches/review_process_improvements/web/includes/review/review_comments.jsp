<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="html" uri="/tags/struts-html" %>
<%@ taglib prefix="bean" uri="/tags/struts-bean" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>
	<c:set var="responseNum" value="1" />
	<c:set var="lastComment" value="${-1}" />
	<c:if test="${not empty item.document}">
		<c:forEach items="${item.allComments}" var="comment" varStatus="commentStatus">
			<c:set var="commentType" value="${comment.commentType.name}" />
			<c:if test='${commentType == "Comment" || commentType == "Required" || commentType == "Recommended"}'>
				<c:set var="lastComment" value="${commentStatus.index}" />
			</c:if>
		</c:forEach>
	</c:if>
	<c:forEach items="${item.allComments}" var="comment" varStatus="commentStatus">
		<c:if test="${( (not managerEdit) || ( managerEdit && comment.commentType.name != 'Manager Comment'))  &&
					  ( (not editEvaluation) || ( editEvaluation && comment.commentType.name != 'Primary Review Evaluation Comment'))}">
		   
			<tr class="dark">
				<td class="value" width="100%">
					<c:choose>
						<c:when test="${comment.commentType.name eq 'Manager Comment'}">
							<span class="coderTextBlue">
							<b><bean:message key="editReview.Question.ManagerComment.title" />:</b>
						</c:when>
						<c:when test="${comment.commentType.name eq 'Appeal'}">
							<b><bean:message key="editReview.Question.AppealText.title" />:</b>
						</c:when>
						<c:when test="${comment.commentType.name eq 'Primary Review Evaluation Comment'}">
							<b><bean:message key="editReview.Question.EvaluationComment.title" />:</b>
						</c:when>
						<c:when test="${comment.commentType.name eq 'Appeal Response'}">
							<span class="coderTextBlue">
							<b><bean:message key="editReview.Question.AppealResponseText.title" />:</b>
						</c:when>
						<c:otherwise>
							<b><bean:message key="editReview.Question.Response.title" />
								${responseNum}: </b>
							<c:choose>
								<c:when test="${canPlaceAppealResponse && appealStatuses[itemIdx] eq 'Unresolved' && (comment.commentType.name eq 'Comment' || comment.commentType.name eq 'Required' || comment.commentType.name eq 'Recommended')}">
									<html:hidden property="comment_id[${itemIdx}]" value="${comment.id}"/>
									<html:select property="comment_type[${itemIdx}]" styleId="cmtType_${itemIdx}" styleClass="inputBox" value="${comment.commentType.name}">
										<c:forEach items="${allCommentTypes}" var="commentType" >
											<html:option value="${commentType.name}" key="CommentType.${fn:replace(commentType.name, ' ', '')}" />
										</c:forEach>
									</html:select><b id="cmtTypeStatic_${itemIdx}" style="display:none;">scuko</b>
								</c:when>
								<c:otherwise>
									<b><bean:message key="CommentType.${fn:replace(comment.commentType.name, ' ', '')}" /></b>
								</c:otherwise>
							</c:choose>		
							<c:set var="responseNum" value="${responseNum + 1}" />
						</c:otherwise>
					</c:choose>
					&#160;${orfn:htmlEncode(comment.comment)}
					<c:if test="${comment.commentType.name eq 'Appeal Response' || comment.commentType.name eq 'Manager Comment'}">
						</span>
					</c:if>
					<c:if test="${lastComment == commentStatus.index}">
						<br /><html:link page="/actions/DownloadDocument.do?method=downloadDocument&uid=${item.document}"><bean:message key="editReview.Document.Download" /></html:link>
					</c:if>
				</td>
				<c:choose>
					<c:when test='${comment.commentType.name == "Comment" || comment.commentType.name == "Required" || comment.commentType.name == "Recommended"}'>
						<c:choose>
							<c:when test="${reviewType eq 'ReviewEvaluation'}">
								<c:if
									test="${editEvaluation}">
									<td class="value" nowrap="nowrap"><b><bean:message
										key="editReview.Evaluation" /></b></td>
									<td class="valueC"><html:select
										property="comment_eval_type(${itemIdx}.${commentStatus.count})"
										styleClass="inputBox">
										<c:forEach items="${allEvaluationTypes}" var="evaluationType">
											<html:option value="${evaluationType.id}"
												key="EvaluationType.${fn:replace(evaluationType.name, ' ', '')}" />
										</c:forEach>
									</html:select></td>
								</c:if>
								<c:if
									test="${not editEvaluation}">
									<td class="value" nowrap="nowrap"><b><bean:message
										key="editReview.Evaluation" /></b></td>
									<td class="valueC"><c:if
										test="${not empty comment.evaluationType.name}">
									${orfn:htmlEncode(comment.evaluationType.name)}
									</c:if> <c:if test="${empty comment.evaluationType.name}">
										<bean:message key="NotAvailable" />
									</c:if></td>
								</c:if>
							</c:when>
							<c:otherwise>
								<td class="value"
								colspan="${canPlaceAppeal ? 5 : (canPlaceAppealResponse ? 4 : 3)}"><!-- @ --></td>
							</c:otherwise>
						</c:choose>
					</c:when>
					<c:otherwise>
						<td class="value"
							colspan="${canPlaceAppeal ? 5 : (canPlaceAppealResponse ? 4 : 3)}"><!-- @ --></td>
					</c:otherwise>
				</c:choose>
		</tr>
		</c:if>
	</c:forEach>
