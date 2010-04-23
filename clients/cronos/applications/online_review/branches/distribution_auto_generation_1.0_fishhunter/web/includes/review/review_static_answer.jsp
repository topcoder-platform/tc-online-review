<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="bean" uri="/tags/struts-bean" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>
	<td class="valueC" nowrap="nowrap">
		<c:choose>
			<c:when test="${empty item.answer}"><!-- @ --></c:when>
			<c:when test="${question.questionType.name == 'Yes/No'}">
				<c:if test="${item.answer != '1'}">
					<bean:message key="global.answer.No" />
				</c:if>
				<c:if test="${item.answer == '1'}">
					<bean:message key="global.answer.Yes" />
				</c:if>
			</c:when>
			<c:when test="${question.questionType.name == 'Scale (1-4)'}">
				<c:if test="${!(empty fn:replace(item.answer, '/4', ''))}">
					<bean:message key="Answer.Score4.ans${fn:replace(item.answer, '/4', '')}"/>
				</c:if>
			</c:when>			
			<c:when test="${question.questionType.name == 'Scale (1-10)'}">
				<bean:message key="Answer.Score10.Rating.title" /> ${fn:replace(item.answer, '/10', '')}
			</c:when>
			<c:when test="${question.questionType.name == 'Test Case'}">
				<c:set var="wordOf" value=" ${orfn:getMessage(pageContext, 'editReview.Question.Response.TestCase.of')} " />
				${fn:replace(item.answer, '/', wordOf)}
			</c:when>
			<c:when test="${question.questionType.name == 'Scale (0-3)'}">
				<c:if test="${!(empty fn:replace(item.answer, '/3', ''))}">
					<bean:message key="Answer.Score3.ans${fn:replace(item.answer, '/3', '')}"/>
				</c:if>
			</c:when>
			<c:when test="${question.questionType.name == 'Scale (0-9)'}">
				<bean:message key="Answer.Score9.Rating.title" /> ${fn:replace(item.answer, '/9', '')}
			</c:when>
			<c:when test="${question.questionType.name == 'Scale (0-4)'}">
				<c:if test="${!(empty fn:replace(item.answer, '/4', ''))}">
					<bean:message key="Answer.Score0_4.ans${fn:replace(item.answer, '/4', '')}"/>
				</c:if>
			</c:when>
			<c:otherwise>
				${orfn:htmlEncode(item.answer)}
			</c:otherwise>
		</c:choose>
	</td>
