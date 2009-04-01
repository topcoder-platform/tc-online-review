<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="html" uri="/tags/struts-html" %>
<%@ taglib prefix="bean" uri="/tags/struts-bean" %>
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
			<c:when test="${question.questionType.name eq 'Scale (0-3)'}">
				<html:select property="answer[${itemIdx}]" styleClass="inputBox">
					<html:option value=""><bean:message key="Answer.Select" /></html:option>
					<html:option value="0/3"><bean:message key="Answer.Score3.ans0" /></html:option>
					<html:option value="1/3"><bean:message key="Answer.Score3.ans1" /></html:option>
					<html:option value="2/3"><bean:message key="Answer.Score3.ans2" /></html:option>
					<html:option value="3/3"><bean:message key="Answer.Score3.ans3" /></html:option>
				</html:select>
			</c:when>
			<c:when test="${question.questionType.name eq 'Scale (0-9)'}">
				<html:select property="answer[${itemIdx}]" styleClass="inputBox">
					<html:option value=""><bean:message key="Answer.Select" /></html:option>
					<c:forEach var="rating" begin="0" end="9">
						<html:option value="${rating}/9"><bean:message key="Answer.Score9.Rating.title" /> ${rating}</html:option>
					</c:forEach>
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
			<c:when test="${question.questionType.name eq 'Scale (0-4)'}">
				<html:select property="answer[${itemIdx}]" styleClass="inputBox">
					<html:option value=""><bean:message key="Answer.Select" /></html:option>
					<html:option value="0/4"><bean:message key="Answer.Score0_4.ans0" /></html:option>
					<html:option value="1/4"><bean:message key="Answer.Score0_4.ans1" /></html:option>
					<html:option value="2/4"><bean:message key="Answer.Score0_4.ans2" /></html:option>
					<html:option value="3/4"><bean:message key="Answer.Score0_4.ans3" /></html:option>
					<html:option value="4/4"><bean:message key="Answer.Score0_4.ans4" /></html:option>
				</html:select>
			</c:when>
			<c:when test="${question.questionType.name eq 'Test Case'}">
				<html:text property="passed_tests" value="" styleClass="inputBox" style="width:25;" onchange="populateTestCaseAnswer(${itemIdx});"/>
				<bean:message key="editReview.Question.Response.TestCase.of" />
				<html:text property="all_tests" value="" styleClass="inputBox" style="width:25;" onchange="populateTestCaseAnswer(${itemIdx});"/>
				<html:hidden property="answer[${itemIdx}]" />
			</c:when>
		</c:choose>
