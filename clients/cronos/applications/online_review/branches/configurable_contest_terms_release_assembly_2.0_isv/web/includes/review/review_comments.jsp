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
		<c:if test="${(not managerEdit) || (comment.commentType.name != 'Manager Comment')}">
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
				<td class="value" colspan="${canPlaceAppeal ? 5 : (canPlaceAppealResponse ? 4 : 3)}"><!-- @ --></td>
			</tr>
		</c:if>
	</c:forEach>
