<%--
  - Author: TCSASSEMBLER
  - Version: 2.0
  - Copyright (C)  - 2014 TopCoder Inc., All Rights Reserved.
 --%>
<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="or" uri="/or-tags" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>
<%@ taglib prefix="tc-webtag" uri="/tags/tc-webtags" %>
<c:set var="toPDF" value="${param.pdf eq 'true'}"/>
<c:if test="${param.showFillScorecardLink}">
<script language="javascript" type="text/javascript">
    <!--
    function fillScorecard() {
        if (confirm("<or:text key="global.fillScorecardConfirmation" />")) {
            var scores = document.getElementsByTagName("select");
            for (var i = 0; i < scores.length; i++) {
                if (scores[i].getAttribute("name").indexOf("answer[") == 0) {
                    if (scores[i].selectedIndex == 0) {
                        var options = scores[i].options;
                        for (var j = 1; j < options.length ; j++) {
                            if (options[j].value.indexOf("/") >= 0) {
                                scores[i].selectedIndex = scores[i].options.length - 1;
                                break;
                            } else if (options[j].value == "1") {
                                scores[i].selectedIndex = j;
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
     // -->
    </script>
</c:if>

<div style="padding: 11px 0px 9px 0px;">
    <table width="100%" cellpadding="0" cellspacing="0" border="0">
        <tr>
            <td>
                <table cellspacing="0" cellpadding="0" border="0">
                    <tr valign="middle">
                        <td><img src="/i/${categoryIconName}" border="0" /></td>
                        <td><img src="/i/${rootCatalogIcon}" alt="${rootCatalogName}" border="0" /></td>
                        <td>
                            <span class="bodyTitle">${orfn:htmlEncode(project.allProperties['Project Name'])}</span>
                            <c:if test="${!(empty project.allProperties['Project Version'])}">
                                <font size="4"><or:text key="global.version" />
                                    ${orfn:htmlEncode(project.allProperties['Project Version'])}</font>
                            </c:if>
                        </td>
                    </tr>
                </table>
            </td>
            <td align="right" valign="top">
                <c:if test="${canExport}">
                    <a href="<or:url value='/actions/ExportReview?reviewType=${reviewType}&rid=${param.rid}' />"><or:text key="exportReview.ExportToExcel" /></a>
                </c:if>
                <c:if test="${canReopenScorecard}">
                    <c:if test="${canExport}">
                        |
                    </c:if>
                    <a href="<or:url value='/actions/ReopenScorecard?rid=${review.id}' />"><or:text key="editReview.ReopenScorecard" /></a>
                </c:if>
                <c:if test="${canEditScorecard}">
                    <c:if test="${canExport or canReopenScorecard}">
                        |
                    </c:if>
                    <a href="<or:url value='/actions/Edit${reviewType}?rid=${review.id}' />"><or:text key="editReview.EditScorecard" /></a>
                </c:if>
                <c:if test="${param.showFillScorecardLink}">
                    <c:if test="${canExport or canReopenScorecard or canEditScorecard}">
                        |
                    </c:if>
                    <a href="javascript:fillScorecard();"><or:text key="global.fillScorecard" /></a>
                </c:if>
            </td>
        </tr>
    </table>
</div>
<c:if test="${reviewType ne 'AutoScreening' and reviewType ne 'CompositeReview'}">
    <c:if test="${reviewType eq 'SpecificationReview'}">
        &#160;<b><or:text key="editReview.SpecificationReviewer" /></b>
    </c:if>
    <c:if test="${reviewType eq 'CheckpointScreening'}">
        &#160;<b><or:text key="editReview.CheckpointScreener" /></b>
    </c:if>
    <c:if test="${reviewType eq 'CheckpointReview'}">
        &#160;<b><or:text key="editReview.CheckpointReviewer" /></b>
    </c:if>
    <c:if test="${reviewType eq 'Screening'}">
        &#160;<b><or:text key="editReview.Screener" /></b>
    </c:if>
    <c:if test="${reviewType eq 'Review'}">
        &#160;<b><or:text key="editReview.Reviewer" /></b>
    </c:if>
    <c:if test="${reviewType eq 'Approval'}">
        &#160;<b><or:text key="editReview.Approver" /></b>
    </c:if>
    <c:if test="${reviewType eq 'Aggregation' or reviewType eq 'AggregationReview'}">
        &#160;<b><or:text key="editReview.Aggregator" /></b>
    </c:if>
    <c:if test="${reviewType eq 'FinalReview'}">
        &#160;<b><or:text key="editReview.FinalReviewer" /></b>
    </c:if>
    <c:if test="${reviewType eq 'PostMortem'}">
        &#160;<b><or:text key="editReview.Post-MortemReviewer" /></b>
    </c:if>
    <c:if test="${reviewType eq 'IterativeReview'}">
        &#160;<b><or:text key="editReview.IterativeReviewer" /></b>
    </c:if>
    <c:if test="${not empty authorId}">
        <tc-webtag:handle coderId="${authorId}" context="${orfn:getHandlerContext(pageContext.request)}" />
    </c:if>
    <br />
</c:if>

<c:if test="${not empty sid}">
    &#160;<b><or:text key="editReview.Submission" /></b> ${sid}
    <c:if test="${not empty submitterId and reviewType ne 'Screening' and reviewType ne 'Review' and reviewType ne 'CheckpointScreening' and reviewType ne 'CheckpointReview' and reviewType ne 'IterativeReview' and not toPDF}">
        (<tc-webtag:handle coderId="${submitterId}" context="${orfn:getHandlerContext(pageContext.request)}" />)
    </c:if>
    <br />
</c:if>

<c:if test="${not empty modificationDate}">
    &#160;<b><or:text key="editReview.ModificationDate" /></b>
    <c:out value="${orfn:displayDate(pageContext.request, modificationDate)}"/><br />
</c:if>

&#160;<b><or:text key="editReview.MyRole" /></b> ${myRole}<br />