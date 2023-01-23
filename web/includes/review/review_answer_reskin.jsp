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
                <div class="editReview__input" align="center">
                    <input type="hidden" name="answer[${itemIdx}]" class="scoreInput">
                    <div class="custom-select-wrapper" style="width:82px;">
                        <div class="custom-select grey">
                            <div class="custom-select__trigger"><span></span>
                                <div class="arrow"></div>
                            </div>
                            <div class="custom-options" align="left">
                                <c:set var="OR_FIELD_TO_SELECT" value="answer[${itemIdx}]"/>
                                <span class="custom-option custom-option-grey" data-value="" <or:selected value=""/>><or:text key="Answer.Select" def=""/></span>
                                <span class="custom-option custom-option-grey" data-value="1" <or:selected value="1"/>><or:text key="global.answer.Yes" def="1"/></span>
                                <span class="custom-option custom-option-grey" data-value="0" <or:selected value="0"/>><or:text key="global.answer.No" def="0"/></span>
                            </div>
                        </div>
                    </div>
                </div>
            </c:when>
            <c:when test="${question.questionType.name eq 'Scale (1-4)'}">
                <div class="editReview__input" align="center">
                    <input type="hidden" name="answer[${itemIdx}]" class="scoreInput">
                    <div class="custom-select-wrapper score4">
                        <div class="custom-select grey">
                            <div class="custom-select__trigger"><span></span>
                                <div class="arrow"></div>
                            </div>
                            <div class="custom-options" align="left">
                                <c:set var="OR_FIELD_TO_SELECT" value="answer[${itemIdx}]"/>
                                <span class="custom-option custom-option-grey" data-value="" <or:selected value=""/>><or:text key="Answer.Select" def=""/></span>
                                <span class="custom-option custom-option-grey" data-value="1/4" <or:selected value="1/4"/>><or:text key="Answer.Score4.ans1" def="1/4"/></span>
                                <span class="custom-option custom-option-grey" data-value="2/4" <or:selected value="2/4"/>><or:text key="Answer.Score4.ans2" def="2/4"/></span>
                                <span class="custom-option custom-option-grey" data-value="3/4" <or:selected value="3/4"/>><or:text key="Answer.Score4.ans3" def="3/4"/></span>
                                <span class="custom-option custom-option-grey" data-value="4/4" <or:selected value="4/4"/>><or:text key="Answer.Score4.ans4" def="4/4"/></span>
                            </div>
                        </div>
                    </div>
                </div>
            </c:when>
            <c:when test="${question.questionType.name eq 'Scale (0-3)'}">
                <div class="editReview__input" align="center">
                    <input type="hidden" name="answer[${itemIdx}]" class="scoreInput">
                    <div class="custom-select-wrapper score3">
                        <div class="custom-select grey">
                            <div class="custom-select__trigger"><span></span>
                                <div class="arrow"></div>
                            </div>
                            <div class="custom-options" align="left">
                                <c:set var="OR_FIELD_TO_SELECT" value="answer[${itemIdx}]"/>
                                <span class="custom-option custom-option-grey" data-value="" <or:selected value=""/>><or:text key="Answer.Select" def=""/></span>
                                <span class="custom-option custom-option-grey" data-value="0/3" <or:selected value="0/3"/>><or:text key="Answer.Score3.ans0" def="0/3"/></span>
                                <span class="custom-option custom-option-grey" data-value="1/3" <or:selected value="1/3"/>><or:text key="Answer.Score3.ans1" def="1/3"/></span>
                                <span class="custom-option custom-option-grey" data-value="2/3" <or:selected value="2/3"/>><or:text key="Answer.Score3.ans2" def="2/3"/></span>
                                <span class="custom-option custom-option-grey" data-value="3/3" <or:selected value="3/3"/>><or:text key="Answer.Score3.ans3" def="3/3"/></span>
                            </div>
                        </div>
                    </div>
                </div>
            </c:when>
            <c:when test="${question.questionType.name eq 'Scale (0-9)'}">
                <div class="editReview__input" align="center">
                    <input type="hidden" name="answer[${itemIdx}]" class="scoreInput">
                    <div class="custom-select-wrapper score9">
                        <div class="custom-select grey">
                            <div class="custom-select__trigger"><span></span>
                                <div class="arrow"></div>
                            </div>
                            <div class="custom-options" align="left">
                                <c:set var="OR_FIELD_TO_SELECT" value="answer[${itemIdx}]"/>
                                <span class="custom-option custom-option-grey" data-value="" <or:selected value=""/>><or:text key="Answer.Select" def=""/></span>
                                <c:forEach var="rating" begin="0" end="9">
                                    <span class="custom-option custom-option-grey" data-value="${rating}/9" <or:selected value="${rating}/9"/>><or:text key="Answer.Score9.Rating.title" def="${rating}/9"/> ${rating}</span>
                                </c:forEach>
                            </div>
                        </div>
                    </div>
                </div>
            </c:when>
            <c:when test="${question.questionType.name eq 'Scale (1-10)'}">
                <div class="editReview__input" align="center">
                    <input type="hidden" name="answer[${itemIdx}]" class="scoreInput">
                    <div class="custom-select-wrapper score10">
                        <div class="custom-select grey">
                            <div class="custom-select__trigger"><span></span>
                                <div class="arrow"></div>
                            </div>
                            <div class="custom-options" align="left">
                                <c:set var="OR_FIELD_TO_SELECT" value="answer[${itemIdx}]"/>
                                <span class="custom-option custom-option-grey" data-value="" <or:selected value=""/>><or:text key="Answer.Select" def=""/></span>
                                <c:forEach var="rating" begin="1" end="10">
                                    <span class="custom-option custom-option-grey" data-value="${rating}/10" <or:selected value="${rating}/10"/>><or:text key="Answer.Score10.Rating.title" def="${rating}/10"/> ${rating}</span>
                                </c:forEach>
                            </div>
                        </div>
                    </div>
                </div>
            </c:when>
            <c:when test="${question.questionType.name eq 'Scale (0-4)'}">
                <div class="editReview__input" align="center">
                    <input type="hidden" name="answer[${itemIdx}]" class="scoreInput">
                    <div class="custom-select-wrapper score10">
                        <div class="custom-select grey">
                            <div class="custom-select__trigger"><span></span>
                                <div class="arrow"></div>
                            </div>
                            <div class="custom-options" align="left">
                                <c:set var="OR_FIELD_TO_SELECT" value="answer[${itemIdx}]"/>
                                <span class="custom-option custom-option-grey" data-value="" <or:selected value=""/>><or:text key="Answer.Select" def=""/></span>
                                <span class="custom-option custom-option-grey" data-value="0/4" <or:selected value="0/4"/>><or:text key="Answer.Score0_4.ans0" def="0/4"/></span>
                                <span class="custom-option custom-option-grey" data-value="1/4" <or:selected value="1/4"/>><or:text key="Answer.Score0_4.ans1" def="1/4"/></span>
                                <span class="custom-option custom-option-grey" data-value="2/4" <or:selected value="2/4"/>><or:text key="Answer.Score0_4.ans2" def="2/4"/></span>
                                <span class="custom-option custom-option-grey" data-value="3/4" <or:selected value="3/4"/>><or:text key="Answer.Score0_4.ans3" def="3/4"/></span>
                                <span class="custom-option custom-option-grey" data-value="4/4" <or:selected value="4/4"/>><or:text key="Answer.Score0_4.ans4" def="4/4"/></span>
                            </div>
                        </div>
                    </div>
                </div>
            </c:when>
            <c:when test="${question.questionType.name eq 'Test Case'}">
                <input type="text" name="passed_tests" value="" class="inputBox" style="width:25;" onchange="populateTestCaseAnswer(${itemIdx});"/>
                <or:text key="editReview.Question.Response.TestCase.of" />
                <input type="text" name="all_tests" value="" class="inputBox" style="width:25;" onchange="populateTestCaseAnswer(${itemIdx});"/>
                <input type="hidden" name="answer[${itemIdx}] class="scoreInput""  value="<or:fieldvalue field='answer[${itemIdx}]' />" />
            </c:when>
        </c:choose>
