<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="html" uri="/tags/struts-html" %>
<%@ taglib prefix="bean" uri="/tags/struts-bean" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>
<%@ taglib prefix="tc-webtag" uri="/tags/tc-webtags" %>

<c:set var="responseNum" value="1" />
<c:forEach items="${item.allComments}" var="comment">
	<c:if test="${not (managerEdit and comment.commentType.name eq 'Manager Comment')}">
		<tr class="dark">
			<td class="value" width="100%">
				<c:choose>
					<c:when test="${comment.commentType.name eq 'Manager Comment'}">
						
						<b><bean:message key="editReview.Question.ManagerComment.title" />:</b>
					</c:when>					
					<c:when test="${comment.commentType.name eq 'Appeal'}">
						
						<b><bean:message key="editReview.Question.AppealText.title" />:</b>
					</c:when>					
					<c:when test="${comment.commentType.name eq 'Appeal Response'}">
						
						<b><bean:message key="editReview.Question.AppealResponseText.title" />:</b>
					</c:when>
					<c:otherwise>
						<b><bean:message key="editReview.Question.Response.title" />
							${responseNum}:
							<bean:message key="CommentType.${fn:replace(comment.commentType.name, ' ', '')}" />
						</b>
						<c:set var="responseNum" value="${responseNum + 1}" />
					</c:otherwise>
				</c:choose>
				&#160;${orfn:htmlEncode(comment.comment)}		
			</td>
			<td class="value" colspan="${canPlaceAppeal ? 5 : (canPlaceAppealResponse ? 4 : 3)}"><!-- @ --></td>
		</tr>
	</c:if>
</c:forEach>