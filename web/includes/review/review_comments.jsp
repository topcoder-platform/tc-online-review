<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="html" uri="/tags/struts-html" %>
<%@ taglib prefix="bean" uri="/tags/struts-bean" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>
<%@ taglib prefix="tc-webtag" uri="/tags/tc-webtags" %>
<c:forEach items="${item.allComments}" var="comment" varStatus="commentStatus">
	<tr class="dark">
		<td class="value" width="100%">
			<b><bean:message key="editReview.Question.Response.title" />
				${commentStatus.index + 1}:
				<bean:message key="CommentType.${fn:replace(comment.commentType.name, ' ', '')}" /></b>
				&#160;${orfn:htmlEncode(comment.comment)}
		</td>
		<td class="value"><!-- @ --></td>
		<td class="value"><!-- @ --></td>
	</tr>
</c:forEach>