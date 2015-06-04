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
                            <b><or:text key="editReview.Question.ManagerComment.title" />:</b>
                        </c:when>
                        <c:when test="${comment.commentType.name eq 'Appeal'}">
                            <b><or:text key="editReview.Question.AppealText.title" />:</b>
                        </c:when>
                        <c:when test="${comment.commentType.name eq 'Appeal Response'}">
                            <span class="coderTextBlue">
                            <b><or:text key="editReview.Question.AppealResponseText.title" />:</b>
                        </c:when>
                        <c:otherwise>
                            <b><or:text key="editReview.Question.Response.title" />
                                ${responseNum}: </b>
                            <c:choose>
                                <c:when test="${canPlaceAppealResponse && appealStatuses[itemIdx] eq 'Unresolved' && (comment.commentType.name eq 'Comment' || comment.commentType.name eq 'Required' || comment.commentType.name eq 'Recommended')}">
                                    <input type="hidden" name="comment_id[${itemIdx}]" value="${comment.id}" value="<or:fieldvalue field='comment_id[${itemIdx}]' />" />
                                    <select name="comment_type[${itemIdx}]" class="inputBox"><c:set var="OR_FIELD_TO_SELECT" value="comment_type[${itemIdx}]"/>
                                        <c:forEach items="${allCommentTypes}" var="commentType" >
                                            <option value="${commentType.name}" <c:if test="${comment.commentType.name == commentType.name}">selected="selected"</c:if> >
                                            <or:text key="CommentType.${fn:replace(commentType.name, ' ', '')}" def="${commentType.name}" /></option>
                                        </c:forEach>
                                    </select><b name="cmtTypeStatic_${itemIdx}" style="display:none;"></b>
                                </c:when>
                                <c:otherwise>
                                    <b><or:text key="CommentType.${fn:replace(comment.commentType.name, ' ', '')}" /></b>
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
                        <br /><a href="<or:url value='/actions/DownloadDocument?uid=${item.document}' />"><or:text key="editReview.Document.Download" /></a>
                    </c:if>
                </td>
                <td class="value" colspan="${canPlaceAppeal ? 4 : (canPlaceAppealResponse ? 3 : 2)}"><!-- @ --></td>
            </tr>
        </c:if>
    </c:forEach>
