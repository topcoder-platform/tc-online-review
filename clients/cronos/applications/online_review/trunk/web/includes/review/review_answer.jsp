<%--
  - Author: TCSASSEMBLER
  - Version: 2.0
  - Copyright (C)  - 2014 TopCoder Inc., All Rights Reserved.
 --%>
<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="or" uri="/or-tags" %>
        <c:choose>
            <c:when test="${question.questionType.name eq 'Yes/No'}">
                <select name="answer[${itemIdx}]" class="inputBox"><c:set var="OR_FIELD_TO_SELECT" value="answer[${itemIdx}]"/>
                    <option value="" <or:selected value=""/>><or:text key="Answer.Select" /></option>
                    <option value="1" <or:selected value="1"/>><or:text key="global.answer.Yes" /></option>
                    <option value="0" <or:selected value="0"/>><or:text key="global.answer.No" /></option>
                </select>
            </c:when>
            <c:when test="${question.questionType.name eq 'Scale (1-4)'}">
                <select name="answer[${itemIdx}]" class="inputBox"><c:set var="OR_FIELD_TO_SELECT" value="answer[${itemIdx}]"/>
                    <option value="" <or:selected value=""/>><or:text key="Answer.Select" /></option>
                    <option value="1/4" <or:selected value="1/4"/>><or:text key="Answer.Score4.ans1" /></option>
                    <option value="2/4" <or:selected value="2/4"/>><or:text key="Answer.Score4.ans2" /></option>
                    <option value="3/4" <or:selected value="3/4"/>><or:text key="Answer.Score4.ans3" /></option>
                    <option value="4/4" <or:selected value="4/4"/>><or:text key="Answer.Score4.ans4" /></option>
                </select>
            </c:when>
            <c:when test="${question.questionType.name eq 'Scale (0-3)'}">
                <select name="answer[${itemIdx}]" class="inputBox"><c:set var="OR_FIELD_TO_SELECT" value="answer[${itemIdx}]"/>
                    <option value="" <or:selected value=""/>><or:text key="Answer.Select" /></option>
                    <option value="0/3" <or:selected value="0/3"/>><or:text key="Answer.Score3.ans0" /></option>
                    <option value="1/3" <or:selected value="1/3"/>><or:text key="Answer.Score3.ans1" /></option>
                    <option value="2/3" <or:selected value="2/3"/>><or:text key="Answer.Score3.ans2" /></option>
                    <option value="3/3" <or:selected value="3/3"/>><or:text key="Answer.Score3.ans3" /></option>
                </select>
            </c:when>
            <c:when test="${question.questionType.name eq 'Scale (0-9)'}">
                <select name="answer[${itemIdx}]" class="inputBox"><c:set var="OR_FIELD_TO_SELECT" value="answer[${itemIdx}]"/>
                    <option value="" <or:selected value=""/>><or:text key="Answer.Select" /></option>
                    <c:forEach var="rating" begin="0" end="9">
                        <option value="${rating}/9" <or:selected value="${rating}/9"/>><or:text key="Answer.Score9.Rating.title" /> ${rating}</option>
                    </c:forEach>
                </select>
            </c:when>
            <c:when test="${question.questionType.name eq 'Scale (1-10)'}">
                <select name="answer[${itemIdx}]" class="inputBox"><c:set var="OR_FIELD_TO_SELECT" value="answer[${itemIdx}]"/>
                    <option value="" <or:selected value=""/>><or:text key="Answer.Select" /></option>
                    <c:forEach var="rating" begin="1" end="10">
                        <option value="${rating}/10" <or:selected value="${rating}/10"/>><or:text key="Answer.Score10.Rating.title" /> ${rating}</option>
                    </c:forEach>
                </select>
            </c:when>
            <c:when test="${question.questionType.name eq 'Scale (0-4)'}">
                <select name="answer[${itemIdx}]" class="inputBox"><c:set var="OR_FIELD_TO_SELECT" value="answer[${itemIdx}]"/>
                    <option value="" <or:selected value=""/>><or:text key="Answer.Select" /></option>
                    <option value="0/4" <or:selected value="0/4"/>><or:text key="Answer.Score0_4.ans0" /></option>
                    <option value="1/4" <or:selected value="1/4"/>><or:text key="Answer.Score0_4.ans1" /></option>
                    <option value="2/4" <or:selected value="2/4"/>><or:text key="Answer.Score0_4.ans2" /></option>
                    <option value="3/4" <or:selected value="3/4"/>><or:text key="Answer.Score0_4.ans3" /></option>
                    <option value="4/4" <or:selected value="4/4"/>><or:text key="Answer.Score0_4.ans4" /></option>
                </select>
            </c:when>
            <c:when test="${question.questionType.name eq 'Test Case'}">
                <input type="text" name="passed_tests" value="" class="inputBox" style="width:25;" onchange="populateTestCaseAnswer(${itemIdx});"/>
                <or:text key="editReview.Question.Response.TestCase.of" />
                <input type="text" name="all_tests" value="" class="inputBox" style="width:25;" onchange="populateTestCaseAnswer(${itemIdx});"/>
                <input type="hidden" name="answer[${itemIdx}]"  value="<or:fieldvalue field='answer[${itemIdx}]' />" />
            </c:when>
        </c:choose>
