<%--
  - Author: TCSASSEMBLER
  - Version: 2.0
  - Copyright (C) 2005 - 2014 TopCoder Inc., All Rights Reserved.
 --%>
<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="or" uri="/or-tags" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>
<c:set var="toPDF" value="${param.pdf eq 'true'}"/>

    <td class="value pdf_section" width="100%">
        <div class="${toPDF ? 'hideText' : 'showText'}" id="shortQ_${itemIdx}">
            <a href="javascript:toggleDisplay('shortQ_${itemIdx}');toggleDisplay('longQ_${itemIdx}');" class="statLink"><img src="/i/or/plus.gif" alt="<or:text key='global.plus.alt'/>" border="0" /></a>
            <b><or:text key="editReview.Question.title" /> ${groupStatus.index + 1}.${sectionStatus.index + 1}.${questionStatus.index + 1}</b>
            ${orfn:htmlEncode(question.description)}
        </div>
        <div class="${toPDF ? 'showText' : 'hideText'}" id="longQ_${itemIdx}">
            <a href="javascript:toggleDisplay('shortQ_${itemIdx}');toggleDisplay('longQ_${itemIdx}');" class="statLink"><img src="/i/or/minus.gif" alt="<or:text key='global.minus.alt'/>" border="0" /></a>
            <b><or:text key="editReview.Question.title" /> ${groupStatus.index + 1}.${sectionStatus.index + 1}.${questionStatus.index + 1}</b>
            ${orfn:htmlEncode(question.description)}<br />
            ${orfn:htmlEncode(question.guideline)}
        </div>
    </td>
    <c:if test="${not noQuestionWeight}">
        <td class="valueC pdf_weight">${orfn:displayScore(pageContext.request, question.weight)}</td>
    </c:if>
                                            